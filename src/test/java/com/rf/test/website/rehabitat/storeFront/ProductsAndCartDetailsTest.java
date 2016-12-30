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
		String usageNoteTab = "Usage Notes";
		String ingredientsTab = "Ingredients";

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
		s_assert.assertTrue(sfProductDetailPage.isContentUnderDescriptionTabPresentAtProductDetailsPage(),"Contents under description tab not present on product detail page");
		sfProductDetailPage.selectTabOnProductDetailPage(usageNoteTab);
		s_assert.assertTrue(sfProductDetailPage.isContentUnderUsageNoteTabPresentAtProductDetailsPage(),"Contents under Usage note tab not present on product detail page");
		sfProductDetailPage.selectTabOnProductDetailPage(ingredientsTab);
		s_assert.assertTrue(sfProductDetailPage.isContentUnderIngredientsTabPresentAtProductDetailsPage(),"Contents under Ingredients tab not present on product detail page");
		s_assert.assertAll();
	}
	/***
	 * qTest : TC-65 Product Detail Page- Recently Viewed Products
	 * 
	 * Description : This test validates the product details page and Recently view tab details.
	 * 
	 * 				
	 */
	@Test 
	public void testVerifyProductDetailPageAndRecentlyViewTabsOnProductDetailPage_65(){
		String allProduct = "ALL PRODUCTS";
		String firstProductName = null;
		String secondProductName = null;
		String thirdProductName = null;
		String fourthProductName = null;
		String fifthProductName = null;

		sfShopSkinCarePage = sfHomePage.clickAllProducts();
		firstProductName = sfShopSkinCarePage.getFirstProductNameFromAllProductPage();
		sfProductDetailPage = sfShopSkinCarePage.clickNameOfFirstProduct();
		s_assert.assertTrue(sfProductDetailPage.getProductNameFromProductDetailsPage().contains(firstProductName),"User is not redirecting to product details page after clicking on product name");
		s_assert.assertTrue(sfProductDetailPage.isAddToCartButtonIsPresentAtProductDetailsPage(),"Add to Cart button not present on product detail page");
		//Verify Recently view tab on product detail page.
		s_assert.assertFalse(sfProductDetailPage.isRecentlyViewTabPresentAtProductDetailsPage(),"Recently view tab is present on product detail page");
		sfProductDetailPage.navigateToBackPage();
		secondProductName = sfShopSkinCarePage.getProductNameFromAllProductPage("2");
		sfProductDetailPage = sfShopSkinCarePage.clickNameOfProductOnAllProductPage("2");
		s_assert.assertTrue(sfProductDetailPage.getProductNameFromProductDetailsPage().contains(secondProductName),"User is not redirecting to product details page after clicking on product name of second product");
		//Verify Recently view tab on product detail page.
		s_assert.assertTrue(sfProductDetailPage.isRecentlyViewTabPresentAtProductDetailsPage(),"Recently view tab is not present on product detail page for second product view");
		//Verify first product present under recently view tab.
		s_assert.assertTrue(sfProductDetailPage.isProductUnderRecentlyViewTabPresentAtProductDetailsPage(firstProductName),"First Product is not present under recently view tab on product detail page.");
		sfProductDetailPage.addProductToCartFromProductDetailPage();
		sfCartPage = sfShopSkinCarePage.checkoutTheCartFromPopUp();
		//Verify the recently view products present on cart page.
		s_assert.assertTrue(sfCartPage.isRecentlyViewProductPresentOnCartPage(firstProductName),"First Product is not present under recently viewed on cart page");
		s_assert.assertTrue(sfCartPage.isRecentlyViewProductPresentOnCartPage(secondProductName),"First Product is not present under recently viewed on cart page");
		//Click on product name under recently view on cart page.
		sfCartPage.clickProductUnderRecentlyView(firstProductName);
		//Verify product detail page.
		s_assert.assertTrue(sfProductDetailPage.getProductNameFromProductDetailsPage().contains(firstProductName),"User is not redirecting to product details page after clicking on product name from cart page");
		//View more products from product detail page.
		sfShopSkinCarePage = sfHomePage.navigateToShopSkincareLink(allProduct);
		thirdProductName = sfShopSkinCarePage.getProductNameFromAllProductPage("3");
		sfProductDetailPage = sfShopSkinCarePage.clickNameOfProductOnAllProductPage("3");
		sfProductDetailPage.navigateToBackPage();
		fourthProductName = sfShopSkinCarePage.getProductNameFromAllProductPage("4");
		sfProductDetailPage = sfShopSkinCarePage.clickNameOfProductOnAllProductPage("4");
		sfProductDetailPage.navigateToBackPage();
		fifthProductName = sfShopSkinCarePage.getProductNameFromAllProductPage("5");
		sfProductDetailPage = sfShopSkinCarePage.clickNameOfProductOnAllProductPage("5");
		//Verify recently viewed product present under recently view tab.
		s_assert.assertTrue(sfProductDetailPage.isProductUnderRecentlyViewTabPresentAtProductDetailsPage(secondProductName),"First Product is not present under recently view tab on product detail page.");
		s_assert.assertTrue(sfProductDetailPage.isProductUnderRecentlyViewTabPresentAtProductDetailsPage(thirdProductName),"Second Product is not present under recently view tab on product detail page.");
		s_assert.assertTrue(sfProductDetailPage.isProductUnderRecentlyViewTabPresentAtProductDetailsPage(fourthProductName),"Third Product is not present under recently view tab on product detail page.");
		//Click carousel dots to view more products.
		sfProductDetailPage.scrollCarouselToViewProductOnProductDetailPage();
		s_assert.assertTrue(sfProductDetailPage.isProductUnderRecentlyViewTabPresentAtProductDetailsPage(firstProductName),"Fourth Product is not present under recently view tab on product detail page.");
		s_assert.assertAll();			
	}
	/***
	 * qTest : TC-66 As a PC user, I will be able to navigate to the account pages from the account drop down in the header
	 * 
	 * Description : This test validates the links Present under welcome DD.
	 * 
	 * 				
	 */
	@Test //Incomplete as shipping profile page is not visible.
	public void testVerifyLinksUnderWelcomeDD_66(){
		String currentURL = null;
		String title = null;
		String accountPage = "my-account";
		String accountPageTitle = "My Account | Rodan and Fields";
		String orderPage = "/orders";
		String orderPageTitle = "Order History | Rodan and Fields";
		String shippingPage="";
		String shippingPageTitle = "";
		String billingPage = "payment-details";
		String billingPageTitle = "Payment Details | Rodan and Fields";
		String editPCPerksPage = "/cart";
		String editPCPerksPageTitle = "Replenishment Order Details | Rodan and Fields";
		String pcPerksFAQPage = "pc-perks-faq";
		String pcPerksFAQPageTitle = "Rodan and Fields";
		String pcPerksStatusPage = "pc-perks-status";
		String pcPerksStatusPageTitle = "Rodan and Fields";

		sfHomePage.loginToStoreFront(TestConstants.PC_EMAIL, password);
		sfHomePage.clickWelcomeDropdown();
		sfAccountInfoPage=sfHomePage.navigateToAccountInfoPage();
		currentURL = sfAccountInfoPage.getCurrentURL().toLowerCase();
		title = sfAccountInfoPage.getCurrentpageTitle();
		s_assert.assertTrue(currentURL.contains(accountPage), "Expected URL should contain "+accountPage+" but actual on UI is"+currentURL);
		s_assert.assertTrue(title.contains(accountPageTitle), "Expected title should contain "+accountPageTitle+" but actual title on UI is"+title);
		sfHomePage.clickWelcomeDropdown();
		sfHomePage.navigateToOrdersPage();
		currentURL = sfAccountInfoPage.getCurrentURL().toLowerCase();
		title = sfAccountInfoPage.getCurrentpageTitle();
		s_assert.assertTrue(currentURL.contains(orderPage), "Expected URL should contain "+orderPage+" but actual on UI is"+currentURL);
		s_assert.assertTrue(title.contains(orderPageTitle), "Expected title should contain "+orderPageTitle+" but actual title on UI is"+title);
		//		sfHomePage.clickWelcomeDropdown();
		//		sfHomePage.navigateToShippingInfoPage();
		//		currentURL = sfAccountInfoPage.getCurrentURL().toLowerCase();
		//		title = sfAccountInfoPage.getCurrentpageTitle();
		//		s_assert.assertTrue(currentURL.contains(shippingPage), "Expected URL should contain "+shippingPage+" but actual on UI is"+currentURL);
		//		s_assert.assertTrue(title.contains(shippingPageTitle), "Expected title should contain "+shippingPageTitle+" but actual title on UI is"+title);
		sfHomePage.clickWelcomeDropdown();
		sfHomePage.navigateToBillingInfoPage();
		currentURL = sfAccountInfoPage.getCurrentURL().toLowerCase();
		title = sfAccountInfoPage.getCurrentpageTitle();
		s_assert.assertTrue(currentURL.contains(billingPage), "Expected URL should contain "+billingPage+" but actual on UI is"+currentURL);
		s_assert.assertTrue(title.contains(billingPageTitle), "Expected title should contain "+billingPageTitle+" but actual title on UI is"+title);
		sfHomePage.clickWelcomeDropdown();
		sfHomePage.logout();
		currentURL = sfAccountInfoPage.getCurrentURL().toLowerCase();
		title = sfAccountInfoPage.getCurrentpageTitle();
		s_assert.assertTrue(currentURL.contains(editPCPerksPage), "Expected URL should contain "+editPCPerksPage+" but actual on UI is"+currentURL);
		s_assert.assertTrue(title.contains(editPCPerksPageTitle), "Expected title should contain "+editPCPerksPageTitle+" but actual title on UI is"+title);
		sfHomePage.clickWelcomeDropdown();
		sfHomePage.navigateToPCPerksFAQPage();
		currentURL = sfAccountInfoPage.getCurrentURL().toLowerCase();
		title = sfAccountInfoPage.getCurrentpageTitle();
		s_assert.assertTrue(currentURL.contains(pcPerksFAQPage), "Expected URL should contain "+pcPerksFAQPage+" but actual on UI is"+currentURL);
		s_assert.assertTrue(title.contains(pcPerksFAQPageTitle), "Expected title should contain "+pcPerksFAQPageTitle+" but actual title on UI is"+title);
		sfHomePage.clickWelcomeDropdown();
		sfHomePage.navigateToPCPerksStatusPage();
		currentURL = sfAccountInfoPage.getCurrentURL().toLowerCase();
		title = sfAccountInfoPage.getCurrentpageTitle();
		s_assert.assertTrue(currentURL.contains(pcPerksStatusPage), "Expected URL should contain "+pcPerksStatusPage+" but actual on UI is"+currentURL);
		s_assert.assertTrue(title.contains(pcPerksStatusPageTitle), "Expected title should contain "+pcPerksStatusPageTitle+" but actual title on UI is"+title);
		sfHomePage.clickWelcomeDropdown();
		sfHomePage.logout();
		s_assert.assertTrue(sfHomePage.isLogoutSuccessful(),"Sign out link has not redirected to login page.");
		s_assert.assertAll();
	}

	/***
	 * qTest : TC-342 Quick View-Add to Cart
	 * Description : This test validates Quick view details of product and add a product to cart
	 *     
	 */
	@Test//Incomplete as not asserted for product added to cart.
	public void testQuickViewAddToCart_342(){
		sfShopSkinCarePage=sfHomePage.clickAllProducts();
		sfShopSkinCarePage.clickOnFirstProductQuickViewButtonAndReturnProductName();
		s_assert.assertTrue(sfShopSkinCarePage.isProductImageQuickViewPopupDisplayed(),"Expected product Image Quick view popup not displayed");
		sfShopSkinCarePage.closeQuickViewOptionPopup();
		s_assert.assertFalse(sfShopSkinCarePage.isProductImageQuickViewPopupDisplayed(),"Expected product Image Quick view popup is not closed");
		sfShopSkinCarePage.clickOnFirstProductQuickViewButtonAndReturnProductName();
		s_assert.assertTrue(sfShopSkinCarePage.isProductImageQuickViewPopupDisplayed(),"Expected product Image Quick view popup not displayed after clicking on product again");
		sfShopSkinCarePage.pressEscapeFromQuickViewPopup();
		s_assert.assertFalse(sfShopSkinCarePage.isProductImageQuickViewPopupDisplayed(),"Expected product Image Quick view popup not displayed after clicking outside of quick view popup");
		sfShopSkinCarePage.clickOnFirstProductQuickViewButtonAndReturnProductName();
		sfShopSkinCarePage.clickAddToCartFromQuickViewPopup();
		s_assert.assertAll();
	}

	/***
	 * qTest : TC-339 Add to Cart
	 * Description : This test validates add a product to cart
	 *     
	 */
	@Test
	public void testAddToCart_339(){
		sfHomePage.loginToStoreFront(TestConstants.CONSULTANT_USERNAME, password);
		sfShopSkinCarePage=sfHomePage.clickAllProducts();
		sfShopSkinCarePage.selectFirstProduct();
		s_assert.assertTrue(sfShopSkinCarePage.isCheckoutPopupDisplayed(),"Expected checkout popup is not displayed");
		s_assert.assertAll();
	}

	/***
	 * qTest : TC-497 Cart Page- Checkout CTA - Consultant/PC
	 * Description : This test validates add a product to cart and checkout the product
	 *     
	 */
	@Test
	public void testCartPageCheckoutCTAConsultantOrPC_497(){
		sfHomePage.loginToStoreFront(TestConstants.CONSULTANT_USERNAME, password);
		sfShopSkinCarePage=sfHomePage.clickAllProducts();
		sfShopSkinCarePage.selectFirstProduct();
		sfCartPage = sfShopSkinCarePage.checkoutTheCartFromPopUp();
		sfCartPage.clickCheckoutBtn();
		s_assert.assertTrue(sfCartPage.isCheckoutConfirmationDisplayed(),"Expected checkout confirmation popup is not displayed cart page");
		s_assert.assertAll();

	}

	/***
	 * qTest : TC-498 Cart Page- Checkout CTA - RC
	 * Description : This test validates add a product to cart and checkout the product using RC
	 *     
	 */
	@Test
	public void testCartPageCheckoutCTARC_498(){
		sfHomePage.loginToStoreFront(TestConstants.RC_USERNAME, password);
		sfShopSkinCarePage=sfHomePage.clickAllProducts();
		sfShopSkinCarePage.addFirstProductToBag();
		sfShopSkinCarePage.checkoutTheCartFromPopUp();
		sfCheckoutPage=sfShopSkinCarePage.checkoutTheCart();
		String currentURL=sfCheckoutPage.getCurrentURL().toLowerCase();
		String checkoutPageText="Account Info";
		s_assert.assertTrue(currentURL.contains("checkout") && sfCheckoutPage.isTextPresent(checkoutPageText),"Current url should contain checkout but actual on UI is "+currentURL+" and checkout page is not present");
		s_assert.assertAll();
	}

	/***
	 * qTest : TC-499 Cart Page- Checkout CTA - Anonymous - Login
	 * Description : This test validates add a product to cart and checkout the product
	 *     
	 */
	@Test//Unable to login at checkout page
	public void testCartPageCheckoutCTAAnonymousLogin_499(){
		sfShopSkinCarePage=sfHomePage.clickAllProducts();
		sfShopSkinCarePage.addFirstProductToBag();
		sfShopSkinCarePage.checkoutTheCartFromPopUp();
		sfCheckoutPage=sfShopSkinCarePage.checkoutTheCart();
		s_assert.assertTrue(sfCheckoutPage.isLoginOrRegisterTextDisplayed()," user is not redirected to expected login page");
		sfCheckoutPage.loginAtCheckoutPage(TestConstants.RC_USERNAME, password);
		String currentURL=sfCheckoutPage.getCurrentURL().toLowerCase();
		s_assert.assertTrue(currentURL.contains("checkout"),"Current url should contain checkout but actual on UI is "+currentURL);
		s_assert.assertAll();
	}

	/***
	 * qTest : TC-500 Cart Page- Checkout CTA - Anonymous - Create account
	 * Description : This test validates add a product to cart and enter details to create an account
	 *     
	 */
	@Test
	public void testCartPageCheckoutCTAAnonymousCreateAccount_500(){
		String userType=TestConstants.USER_TYPE_PC;
		String firstName=TestConstants.FIRST_NAME;
		String lastName=TestConstants.LAST_NAME;
		String email=TestConstants.PC_EMAIL;
		sfShopSkinCarePage=sfHomePage.clickAllProducts();
		sfShopSkinCarePage.addFirstProductToBag();
		sfShopSkinCarePage.checkoutTheCartFromPopUp();
		sfCheckoutPage=sfShopSkinCarePage.checkoutTheCart();
		s_assert.assertTrue(sfCheckoutPage.isLoginOrRegisterTextDisplayed()," user is not redirected to expected login page");
		sfCheckoutPage.fillNewUserDetails(userType, firstName, lastName, email, password);
		sfCheckoutPage.clickCreateAccountButton();
		String currentURL=sfCheckoutPage.getCurrentURL().toLowerCase();
		s_assert.assertTrue(currentURL.contains("checkout"),"Current url should contain checkout but actual on UI is "+currentURL);
		s_assert.assertAll();
	}

	/***
	 * qTest : TC-340 Product Listing Page- PC
	 * Description : This test validates login with pc and observe the products w.r.t the options
	 *     
	 */
	@Test
	public void testProductListingPagePC_340(){
		String userType=TestConstants.USER_TYPE_PC;
		sfHomePage.loginToStoreFront(TestConstants.PC_USERNAME, password);
		sfShopSkinCarePage=sfHomePage.clickAllProducts();
		sfShopSkinCarePage.mouseHoverOnAddToCart();
		s_assert.assertTrue(sfShopSkinCarePage.isAddToCartDDOptionsDisplayed(userType),"Expected add to cart dropdown option not displayed");
		s_assert.assertAll();
	}
	/***
	 * qTest : TC-341 Product Listing Page- Consultant
	 * Description : This test validates login with consultant and observe the products w.r.t the options
	 *     
	 */
	@Test
	public void testProductListingPageConsultant_341(){
		String userType="Consultant";
		sfHomePage.loginToStoreFront(TestConstants.CONSULTANT_USERNAME, password);
		sfShopSkinCarePage=sfHomePage.clickAllProducts();
		sfShopSkinCarePage.mouseHoverOnAddToCart();
		s_assert.assertTrue(sfShopSkinCarePage.isAddToCartDDOptionsDisplayed(userType),"Expected add to cart dropdown option not displayed");
		s_assert.assertAll();
	}

	/***
	 * qTest : TC-185 Login prior to checkout - Login as existing user
	 * 
	 * 
	 */
	@Test
	public void testLoginPriorToCheckoutLoginAsExistingUser_185(){
		//Duplicate TestCase same as TC-499 Cart Page- Checkout CTA - Anonymous - Login
	}

	/***
	 * qTest : TC-186 Login prior to checkout - New user
	 * 
	 * 
	 */
	@Test
	public void testLoginPriorToCheckoutNewUser_186(){
		//Duplicate TestCase same as TC-500 Cart Page- Checkout CTA - Anonymous - Create account  
	}
}
