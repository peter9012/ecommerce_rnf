package com.rf.test.website.rehabitat.storeFront;

import org.testng.annotations.Test;

import com.rf.core.utils.CommonUtils;
import com.rf.core.website.constants.TestConstants;
import com.rf.test.website.rehabitat.storeFront.baseTest.StoreFrontWebsiteBaseTest;

public class ProductsAndCartDetailsTest extends StoreFrontWebsiteBaseTest{

	/***
	 * 
	 * qTest : TC-62 Sort/Order Removal by default option section
	 * 
	 * Description : This test validates the sorting of product base on price filter applied
	 * and also Unsorting of product when filter removed.
	 * 				
	 */ 
	@Test(enabled=true) 
	public void testSortAndUnsortProductBaseOnPriceFilter_62(){
		String allProduct = "ALL PRODUCTS";
		sfShopSkinCarePage=sfHomePage.navigateToShopSkincareLink(allProduct);
		sfShopSkinCarePage.productPriceFilterLowToHigh();
		s_assert.assertTrue(sfShopSkinCarePage.isPriceFilterLowToHighAppliedSuccessfully(),"Selected Price filter 'Low To High' is not applied to product successfully");
		sfShopSkinCarePage.productPriceFilterHighToLow();
		s_assert.assertTrue(sfShopSkinCarePage.isPriceFilterHighToLowAppliedSuccessfully(),"Selected Price filter 'High To Low' is not applied to product successfully");
		sfShopSkinCarePage.productPriceFilterDefault();
		//Verify the default price filter applied.
		s_assert.assertTrue(sfShopSkinCarePage.isDefaultFilterAppliedSuccessfully(),"Default Filter has not been applied successfully on Price filter");
		s_assert.assertAll();
	}

	/***
	 * qTest : TC-64 Product Detail Page- Product Tabs
	 * 
	 * Description : This test validates the product details page and various tabs on product detail page.
	 * 
	 * 				
	 */
	@Test(enabled=true) 
	public void testVerifyProductDetailPageAndTabsOnProductDetailPage_64(){
		String allProduct = "ALL PRODUCTS";
		String productName = null;
		String usageNoteTab = "Usage Notes";
		String ingredientsTab = "Ingredients";

		sfShopSkinCarePage = sfHomePage.clickAllProducts();
		productName = sfShopSkinCarePage.getProductNameFromAllProductPage(TestConstants.PRODUCT_NUMBER);
		sfProductDetailPage = sfShopSkinCarePage.clickNameOfProduct(TestConstants.PRODUCT_NUMBER);
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
	@Test(enabled=true) 
	public void testVerifyProductDetailPageAndRecentlyViewTabsOnProductDetailPage_65(){
		String allProduct = "ALL PRODUCTS";
		String firstProductName = null;
		String secondProductName = null;
		String thirdProductName = null;
		String fourthProductName = null;
		String fifthProductName = null;

		sfShopSkinCarePage = sfHomePage.clickAllProducts();
		firstProductName = sfShopSkinCarePage.getProductNameFromAllProductPage(TestConstants.PRODUCT_NUMBER);
		sfProductDetailPage = sfShopSkinCarePage.clickNameOfProduct(TestConstants.PRODUCT_NUMBER);
		s_assert.assertTrue(sfProductDetailPage.getProductNameFromProductDetailsPage().contains(firstProductName),"User is not redirecting to product details page after clicking on product name");
		s_assert.assertTrue(sfProductDetailPage.isAddToCartButtonIsPresentAtProductDetailsPage(),"Add to Cart button not present on product detail page");
		//Verify Recently view tab on product detail page.
		s_assert.assertFalse(sfProductDetailPage.isRecentlyViewTabPresentAtProductDetailsPage(),"Recently view tab is present on product detail page");
		sfProductDetailPage.navigateToBackPage();
		secondProductName = sfShopSkinCarePage.getProductNameFromAllProductPage(String.valueOf(Integer.parseInt(TestConstants.PRODUCT_NUMBER)+1));
		sfProductDetailPage = sfShopSkinCarePage.clickNameOfProductOnAllProductPage(String.valueOf(Integer.parseInt(TestConstants.PRODUCT_NUMBER)+1));
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
		sfShopSkinCarePage = sfHomePage.clickAllProducts();
		thirdProductName = sfShopSkinCarePage.getProductNameFromAllProductPage(String.valueOf(Integer.parseInt(TestConstants.PRODUCT_NUMBER)+2));
		sfProductDetailPage = sfShopSkinCarePage.clickNameOfProductOnAllProductPage(String.valueOf(Integer.parseInt(TestConstants.PRODUCT_NUMBER)+2));
		sfProductDetailPage.navigateToBackPage();
		fourthProductName = sfShopSkinCarePage.getProductNameFromAllProductPage(String.valueOf(Integer.parseInt(TestConstants.PRODUCT_NUMBER)+3));
		sfProductDetailPage = sfShopSkinCarePage.clickNameOfProductOnAllProductPage(String.valueOf(Integer.parseInt(TestConstants.PRODUCT_NUMBER)+3));
		sfProductDetailPage.navigateToBackPage();
		fifthProductName = sfShopSkinCarePage.getProductNameFromAllProductPage(String.valueOf(Integer.parseInt(TestConstants.PRODUCT_NUMBER)+4));
		sfProductDetailPage = sfShopSkinCarePage.clickNameOfProductOnAllProductPage(String.valueOf(Integer.parseInt(TestConstants.PRODUCT_NUMBER)+4));
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
	@Test(enabled=true)
	public void testVerifyLinksUnderWelcomeDD_66(){
		String currentURL = null;
		String title = null;
		String accountPage = "my-account";
		String accountPageTitle = "My Account | Rodan and Fields";
		String orderPage = "/orders";
		String orderPageTitle = "Order History | Rodan and Fields";
		String shippingPage="address-book";
		String shippingPageTitle = "Address Book | Rodan and Fields";
		String billingPage = "payment-details";
		String billingPageTitle = "Payment Details | Rodan and Fields";
		String editPCPerksPage = "/cart";
		String editPCPerksPageTitle = "Replenishment Order Details | Rodan and Fields";
		String pcPerksFAQPage = "pc-perks-faq";
		String pcPerksFAQPageTitle = "Rodan and Fields";
		String pcPerksStatusPage = "pc-perks-status";
		String pcPerksStatusPageTitle = "Rodan and Fields";

		sfHomePage.loginToStoreFront(pcUserWithPWSSponsor(), password,true);
		sfHomePage.clickWelcomeDropdown();
		sfAccountInfoPage=sfHomePage.navigateToAccountInfoPage();
		currentURL = sfAccountInfoPage.getCurrentURL().toLowerCase();
		title = sfAccountInfoPage.getCurrentpageTitle();
		s_assert.assertTrue(currentURL.contains(accountPage), "Expected URL should contain "+accountPage+" but actual on UI is"+currentURL);
		s_assert.assertTrue(title.contains(accountPageTitle), "Expected title should contain "+accountPageTitle+" but actual title on UI is"+title);
		sfHomePage.clickWelcomeDropdown();
		sfOrdersPage = sfHomePage.navigateToOrdersPage();
		currentURL = sfOrdersPage.getCurrentURL().toLowerCase();
		title = sfOrdersPage.getCurrentpageTitle();
		s_assert.assertTrue(currentURL.contains(orderPage), "Expected URL should contain "+orderPage+" but actual on UI is"+currentURL);
		s_assert.assertTrue(title.contains(orderPageTitle), "Expected title should contain "+orderPageTitle+" but actual title on UI is"+title);
		sfHomePage.clickWelcomeDropdown();
		sfShippingInfoPage = sfHomePage.navigateToShippingInfoPage();
		currentURL = sfShippingInfoPage.getCurrentURL().toLowerCase();
		title = sfShippingInfoPage.getCurrentpageTitle();
		s_assert.assertTrue(currentURL.contains(shippingPage), "Expected URL should contain "+shippingPage+" but actual on UI is"+currentURL);
		s_assert.assertTrue(title.contains(shippingPageTitle), "Expected title should contain "+shippingPageTitle+" but actual title on UI is"+title);
		sfHomePage.clickWelcomeDropdown();
		sfBillingInfoPage = sfHomePage.navigateToBillingInfoPage();
		currentURL = sfBillingInfoPage.getCurrentURL().toLowerCase();
		title = sfBillingInfoPage.getCurrentpageTitle();
		s_assert.assertTrue(currentURL.contains(billingPage), "Expected URL should contain "+billingPage+" but actual on UI is"+currentURL);
		s_assert.assertTrue(title.contains(billingPageTitle), "Expected title should contain "+billingPageTitle+" but actual title on UI is"+title);
		sfHomePage.clickWelcomeDropdown();
		sfAutoshipCartPage = sfHomePage.navigateToEditPCPerksPage();
		currentURL = sfAutoshipCartPage.getCurrentURL().toLowerCase();
		title = sfAutoshipCartPage.getCurrentpageTitle();
		s_assert.assertTrue(currentURL.contains(editPCPerksPage), "Expected URL should contain "+editPCPerksPage+" but actual on UI is"+currentURL);
		s_assert.assertTrue(title.contains(editPCPerksPageTitle), "Expected title should contain "+editPCPerksPageTitle+" but actual title on UI is"+title);
		sfHomePage.clickWelcomeDropdown();
		sfHomePage.navigateToPCPerksFAQPage();
		currentURL = sfHomePage.getCurrentURL().toLowerCase();
		title = sfHomePage.getCurrentpageTitle();
		s_assert.assertTrue(currentURL.contains(pcPerksFAQPage), "Expected URL should contain "+pcPerksFAQPage+" but actual on UI is"+currentURL);
		s_assert.assertTrue(title.contains(pcPerksFAQPageTitle), "Expected title should contain "+pcPerksFAQPageTitle+" but actual title on UI is"+title);
		sfHomePage.clickWelcomeDropdown();
		sfAutoshipStatusPage = sfHomePage.navigateToPCPerksStatusPage();
		currentURL = sfAutoshipStatusPage.getCurrentURL().toLowerCase();
		title = sfAutoshipStatusPage.getCurrentpageTitle();
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
	@Test(enabled=true) //TODO Incomplete as not asserted for product added to cart.
	public void testQuickViewAddToCart_342(){
		sfShopSkinCarePage=sfHomePage.clickAllProducts();
		sfShopSkinCarePage.clickOnFirstProductQuickViewButtonAndReturnProductName();
		s_assert.assertTrue(sfShopSkinCarePage.isProductImageQuickViewPopupDisplayed(),"Expected product Image Quick view popup is not displayed");
		sfShopSkinCarePage.closeQuickViewOptionPopup();
		s_assert.assertFalse(sfShopSkinCarePage.isProductImageQuickViewPopupDisplayed(),"Product Image Quick view popup is not closed");
		sfShopSkinCarePage.clickOnFirstProductQuickViewButtonAndReturnProductName();
		s_assert.assertTrue(sfShopSkinCarePage.isProductImageQuickViewPopupDisplayed(),"Expected product Image Quick view popup is not displayed after clicking on product again");
		sfShopSkinCarePage.pressEscapeFromQuickViewPopup();
		s_assert.assertFalse(sfShopSkinCarePage.isProductImageQuickViewPopupDisplayed(),"Product Image Quick view popup is not closed after clicking outside of quick view popup");
		sfShopSkinCarePage.clickOnFirstProductQuickViewButtonAndReturnProductName();
		sfShopSkinCarePage.clickAddToCartFromQuickViewPopup();
		s_assert.assertAll();
	}

	/***
	 * qTest : TC-339 Add to Cart
	 * Description : This test validates add a product to cart
	 *     
	 */
	@Test(enabled=true)
	public void testAddToCart_339(){
		sfHomePage.loginToStoreFront(consultantWithPulseAndWithCRP(), password,true);
		sfShopSkinCarePage=sfHomePage.clickAllProducts();
		sfShopSkinCarePage.addProductToCart(TestConstants.PRODUCT_NUMBER, TestConstants.ORDER_TYPE_ADHOC);
		s_assert.assertTrue(sfShopSkinCarePage.isCheckoutPopupDisplayed(),"Expected checkout popup is not displayed");
		s_assert.assertAll();
	}

	/***
	 * qTest : TC-497 Cart Page- Checkout CTA - Consultant/PC
	 * Description : This test validates add a product to cart and checkout the product
	 *     
	 */
	@Test(enabled=true)
	public void testCartPageCheckoutCTAConsultantOrPC_497(){
		sfHomePage.loginToStoreFront(consultantWithPulseAndWithCRP(), password,true);
		sfShopSkinCarePage=sfHomePage.clickAllProducts();
		sfShopSkinCarePage.addProductToCart(TestConstants.PRODUCT_NUMBER, TestConstants.ORDER_TYPE_ADHOC);
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
	@Test(enabled=true)
	public void testCartPageCheckoutCTARC_498(){
		sfHomePage.loginToStoreFront(rcWithOrderWithoutSponsor(), password,true);
		sfShopSkinCarePage=sfHomePage.clickAllProducts();
		sfShopSkinCarePage.addProductToCart(TestConstants.PRODUCT_NUMBER, TestConstants.ORDER_TYPE_ENROLLMENT);
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
	@Test(enabled=true)//TODO Unable to login at checkout page
	public void testCartPageCheckoutCTAAnonymousLogin_499(){
		sfShopSkinCarePage=sfHomePage.clickAllProducts();
		sfShopSkinCarePage.addProductToCart(TestConstants.PRODUCT_NUMBER, TestConstants.ORDER_TYPE_ENROLLMENT);
		sfShopSkinCarePage.checkoutTheCartFromPopUp();
		sfCheckoutPage=sfShopSkinCarePage.checkoutTheCart();
		s_assert.assertTrue(sfCheckoutPage.isLoginOrRegisterTextDisplayed()," user is not redirected to expected login page");
		sfCheckoutPage.loginAtCheckoutPage(rcWithOrderWithoutSponsor(), password);
		String currentURL=sfCheckoutPage.getCurrentURL().toLowerCase();
		s_assert.assertTrue(currentURL.contains("checkout"),"Current url should contain checkout but actual on UI is "+currentURL);
		s_assert.assertAll();
	}

	/***
	 * qTest : TC-185 Login prior to checkout - Login as existing user
	 * 
	 * 
	 */
	@Test(enabled=true)
	public void testLoginPriorToCheckoutLoginAsExistingUser_185(){
		//Duplicate TestCase same as TC-499 Cart Page- Checkout CTA - Anonymous - Login
	}

	/***
	 * qTest : TC-186 Login prior to checkout - New user
	 * 
	 * 
	 */
	@Test(enabled=true)
	public void testLoginPriorToCheckoutNewUser_186(){
		//Duplicate TestCase same as TC-500 Cart Page- Checkout CTA - Anonymous - Create account  
	}

	/***
	 * qTest : TC-222 Cart Page- PC Perks Terms and Conditions - PC User
	 * Description : This test validates PC Perks Terms and Conditions Link for PC User
	 * 
	 */
	@Test(enabled=false)
	public void testCartPagePCPerksTermsAndConditionsPCUser_222(){
		String currentWindowID = null; 
		String currentURL = null;
		String utlToAssertForTCDetailsPage = "common/pdf/Archives/PCPerks-TCs";
		sfHomePage.loginToStoreFront(pcUserWithPWSSponsor(),password,true);
		sfShopSkinCarePage = sfHomePage.clickAllProducts();
		sfShopSkinCarePage.addProductToCart(TestConstants.PRODUCT_NUMBER, TestConstants.ORDER_TYPE_ADHOC);
		sfCartPage = sfShopSkinCarePage.checkoutTheCartFromPopUp();
		currentWindowID = CommonUtils.getCurrentWindowHandle();
		sfCartPage.clickPCTermsAndConditionsLink();
		sfCartPage.switchToChildWindow(currentWindowID);
		currentURL = sfCartPage.getCurrentURL();
		s_assert.assertTrue(currentURL.contains(utlToAssertForTCDetailsPage),"Current URL : " + currentURL + " does not contain : " + utlToAssertForTCDetailsPage);
		sfCartPage.switchToParentWindow(currentWindowID);
		s_assert.assertAll();
	}

	/***
	 * qTest : TC-221 Cart Page- PC Perks Terms and Conditions - RC User
	 * Description : This test validates PC Perks Terms and Conditions Link for PC User
	 * 
	 */
	@Test(enabled=false)
	public void testCartPagePCPerksTermsAndConditionsPCUser_221(){
		String currentWindowID = null; 
		String currentURL = null;
		String utlToAssertForTCDetailsPage = "common/pdf/Archives/PCPerks-TCs";
		sfHomePage.loginToStoreFront(rcWithOrderWithoutSponsor(),password,true);
		sfShopSkinCarePage = sfHomePage.clickAllProducts();
		sfShopSkinCarePage.addProductToCart(TestConstants.PRODUCT_NUMBER, TestConstants.ORDER_TYPE_ENROLLMENT);
		sfCartPage = sfShopSkinCarePage.checkoutTheCartFromPopUp();
		currentWindowID = CommonUtils.getCurrentWindowHandle();
		sfCartPage.clickPCTermsAndConditionsLink();
		sfCartPage.switchToChildWindow(currentWindowID);
		currentURL = sfCartPage.getCurrentURL();
		s_assert.assertTrue(currentURL.contains(utlToAssertForTCDetailsPage),"Current URL : " + currentURL + " does not contain : " + utlToAssertForTCDetailsPage);
		sfCartPage.switchToParentWindow(currentWindowID);
		s_assert.assertAll();
	}	



	/***
	 * qTest : TC-67 Cart Page- Update Qty
	 * Description : This test validates add a product to cart and check total by updating 
	 *  quantity of product   
	 */
	@Test(enabled=true)
	public void testAddProductToCartAndUpdateQuantity_67(){
		String subTotalFromCart = null;
		String quantityOfProduct = null;
		String updatedSubtotal = null;
		String calculatedSubtotal = null;
		String updatedQuantity = null;
		String reducedQuantity = null;
		String quantityAsZero = "0";

		sfShopSkinCarePage=sfHomePage.clickAllProducts();
		sfShopSkinCarePage.addProductToCart(TestConstants.PRODUCT_NUMBER, TestConstants.ORDER_TYPE_ENROLLMENT);
		sfCartPage = sfShopSkinCarePage.checkoutTheCartFromPopUp();
		subTotalFromCart = sfCartPage.getSubtotalofItems(); 
		quantityOfProduct = sfCartPage.getQuantityOfProductFromCart("1");
		//Update and increase product qty by one and assert for subtotal.
		updatedQuantity = sfCartPage.updateQuantityByOne(quantityOfProduct);
		sfCartPage.enterQuantityOfProductAtCart("1", updatedQuantity);
		sfCartPage.clickOnUpdateLinkThroughItemNumber("1");
		calculatedSubtotal = sfCartPage.getCalculatedSubtotalValueAfterUpdatingQuantity(subTotalFromCart,quantityOfProduct,updatedQuantity);
		updatedSubtotal = sfCartPage.getSubtotalofItems();
		s_assert.assertTrue(updatedSubtotal.contains(calculatedSubtotal),"Expected subtotal"+calculatedSubtotal+"while actual on UI"+updatedSubtotal);
		//Reduce product qty by one and assert for subtotal.
		reducedQuantity = sfCartPage.updateAndReduceProductQuantityByOne(updatedQuantity);
		sfCartPage.enterQuantityOfProductAtCart("1", reducedQuantity);
		sfCartPage.clickOnUpdateLinkThroughItemNumber("1");
		calculatedSubtotal = sfCartPage.getCalculatedSubtotalValueAfterUpdatingQuantity(updatedSubtotal,updatedQuantity,reducedQuantity);
		updatedSubtotal = sfCartPage.getSubtotalofItems();
		s_assert.assertTrue(updatedSubtotal.contains(calculatedSubtotal),"Expected subtotal"+calculatedSubtotal+"while actual on UI"+updatedSubtotal);
		//Update qty as 'Zero' and update at cart page.
		sfCartPage.enterQuantityOfProductAtCart("1", quantityAsZero);
		sfCartPage.clickOnUpdateLinkThroughItemNumber("1");
		s_assert.assertFalse(sfCartPage.isProductPresentInCart(),"No product expected in cart but product are present in cart");
		s_assert.assertAll();
	}


	/***
	 * qTest : TC-163 Cart Page- Preferred Customer - Ad Hoc Cart
	 * Description : This test validates PC user details on cart page accessed via all products and mini cart..
	 *   
	 */
	@Test(enabled=true)//TODO
	public void testVerifyPCUserProductDetailsOnCartPage_163(){
		String productName = null;
		String productQTY = null;

		//Access cart page via all product page.
		sfHomePage.loginToStoreFront(pcUserWithPWSSponsor(),password,true);
		sfCartPage = sfHomePage.clickMiniCartBagLink();
		sfCartPage.removeAllProductsFromCart();
		s_assert.assertFalse(sfCartPage.isProductPresentInCart(),"No product expected in cart but product are present in cart");
		sfHomePage.clickRodanAndFieldsLogo();
		sfShopSkinCarePage = sfHomePage.clickAllProducts();
		productName = sfShopSkinCarePage.getProductNameFromAllProductPage(TestConstants.PRODUCT_NUMBER);
		sfShopSkinCarePage.addProductToCart(TestConstants.PRODUCT_NUMBER, TestConstants.ORDER_TYPE_ADHOC);
		sfCartPage = sfShopSkinCarePage.checkoutTheCartFromPopUp();
		s_assert.assertTrue(sfCartPage.isProductAddedToCartPresentOnCartPage(productName),"Product added in cart is not present on cart page.Product name : " + productName);
		productQTY = sfCartPage.getQuantityOfSpecificProductFromCart(productName);
		s_assert.assertEquals(productQTY,"1","Quantity of Product on cart page is not found as expected when navigate from product popup. Expceted : 1 . Actual : " + productQTY);
		sfCartPage.clickRodanAndFieldsLogo();
		sfCartPage.clickMiniCartBagLink();
		s_assert.assertTrue(sfCartPage.isProductAddedToCartPresentOnCartPage(productName),"Product added in cart is not present on cart page.Product name : " + productName);
		productQTY = sfCartPage.getQuantityOfSpecificProductFromCart(productName);
		s_assert.assertEquals(productQTY,"1","Quantity of Product on cart page is not found as expected when navigate from Mini cart link. Expceted : 1 . Actual : " + productQTY);
		s_assert.assertAll();  
	}

	/***
	 * qTest : TC-164 Cart Page- Consultant Customer - Ad Hoc Cart
	 * Description : This test validates consultant user details on cart page accessed via all products and mini cart..
	 *   
	 */
	@Test(enabled=true)
	public void testVerifyConsultantUserProductDetailsOnCartPage_164(){
		String productName = null;
		String productQTY = null;

		//Access cart page via all product page.
		sfHomePage.loginToStoreFront(consultantWithPulseAndWithCRP(), password,true);
		sfCartPage = sfHomePage.clickMiniCartBagLink();
		sfCartPage.removeAllProductsFromCart();
		s_assert.assertFalse(sfCartPage.isProductPresentInCart(),"No product expected in cart but product are present in cart");
		sfHomePage.clickRodanAndFieldsLogo();
		sfShopSkinCarePage = sfHomePage.clickAllProducts();
		productName = sfShopSkinCarePage.getProductNameFromAllProductPage(TestConstants.PRODUCT_NUMBER);
		sfShopSkinCarePage.addProductToCart(TestConstants.PRODUCT_NUMBER, TestConstants.ORDER_TYPE_ADHOC);
		sfCartPage = sfShopSkinCarePage.checkoutTheCartFromPopUp();
		s_assert.assertTrue(sfCartPage.isProductAddedToCartPresentOnCartPage(productName),"Product added in cart is not present on cart page.Product name : " + productName);
		productQTY = sfCartPage.getQuantityOfSpecificProductFromCart(productName);
		s_assert.assertEquals(productQTY,"1","Quantity of Product on cart page is not found as expected when navigate from product popup. Expceted : 1 . Actual : " + productQTY);
		sfCartPage.clickRodanAndFieldsLogo();
		sfCartPage.clickMiniCartBagLink();
		s_assert.assertTrue(sfCartPage.isProductAddedToCartPresentOnCartPage(productName),"Product added in cart is not present on cart page.Product name : " + productName);
		productQTY = sfCartPage.getQuantityOfSpecificProductFromCart(productName);
		s_assert.assertEquals(productQTY,"1","Quantity of Product on cart page is not found as expected when navigate from Mini cart link. Expceted : 1 . Actual : " + productQTY);
		s_assert.assertAll();  
	}

	/***
	 * qTest : TC-501 Product Pricing - Anonymous
	 * Description : This test validates the presence of price of product
	 * 
	 */
	@Test(enabled=true)
	public void testProductPricingAnonymous_501(){
		sfShopSkinCarePage = sfHomePage.clickOnCategoryFromShopSkinCare("REDEFINE");
		s_assert.assertTrue(sfShopSkinCarePage.isRetailPricePresentForProductNumber("1"),"Retail price is not present for Product on category Page");
		sfHomePage.clickAllProducts();
		s_assert.assertTrue(sfShopSkinCarePage.isRetailPricePresentForProductNumber("1"),"Retail price is not present for Product on All Products listing Page");
		sfShopSkinCarePage.clickOnQuickViewLinkForProduct("1");
		s_assert.assertTrue(sfShopSkinCarePage.isPricePresentOnQuickViewPopup(),"Price is not present for Product on Quick view popup");
		sfProductDetailPage = sfShopSkinCarePage.clickOnViewProductDetailsLinkOnQuickViewPopup();
		s_assert.assertTrue(sfProductDetailPage.isPricePresentOnPDPAsExpected(),"Price is not present for Product on Product detail Page");
		s_assert.assertAll();
	}


	/***
	 * qTest : TC-502 Product Pricing - RC
	 * Description : This test validates the presence of price of product for RC User
	 * 
	 */
	@Test(enabled=true)
	public void testProductPricingRC_502(){
		sfHomePage.loginToStoreFront(rcWithOrderWithoutSponsor(),password,true);
		sfShopSkinCarePage = sfHomePage.clickOnCategoryFromShopSkinCare("REDEFINE");
		s_assert.assertTrue(sfShopSkinCarePage.isRetailPricePresentForProductNumber("1"),"Retail price is not present for Product on category Page");
		sfHomePage.clickAllProducts();
		s_assert.assertTrue(sfShopSkinCarePage.isRetailPricePresentForProductNumber("1"),"Retail price is not present for Product on All Products listing Page");
		sfShopSkinCarePage.clickOnQuickViewLinkForProduct(TestConstants.PRODUCT_NUMBER);
		s_assert.assertTrue(sfShopSkinCarePage.isPricePresentOnQuickViewPopup(),"Price is not present for Product on Quick view popup");
		sfProductDetailPage = sfShopSkinCarePage.clickOnViewProductDetailsLinkOnQuickViewPopup();
		s_assert.assertTrue(sfProductDetailPage.isPricePresentOnPDPAsExpected(),"Price is not present for Product on Product detail Page");
		s_assert.assertAll();
	}


	/***
	 * qTest : TC-157 Product Details page- Qty
	 * Description : This test validates quantity with different values
	 *     
	 */
	@Test(enabled=true)
	public void testProductDetailsPageQty_157(){
		String productQty=null;
		String qtyToEnter=null;
		String qtyOnCheckoutPopup = null;
		sfShopSkinCarePage=sfHomePage.clickAllProducts();
		sfShopSkinCarePage.clickOnFirstProductQuickViewButtonAndReturnProductName();
		productQty=sfShopSkinCarePage.getProductQuantityFromQuickViewOption();

		// Enter product quantity increased by 1
		qtyToEnter = sfShopSkinCarePage.updateQuantityByOne(productQty);
		sfShopSkinCarePage.enterQuantityonQuickViewPopup(qtyToEnter);
		sfShopSkinCarePage.addProductToCartFromQuickViewPopup(TestConstants.ORDER_TYPE_ENROLLMENT);
		qtyOnCheckoutPopup = sfShopSkinCarePage.getQuantityAddedFromCheckoutPopup();
		s_assert.assertTrue(qtyToEnter.equals(qtyOnCheckoutPopup),"Product Quantity is not found same on Checkout popup. Expected : "+qtyToEnter+". Actual : "+qtyOnCheckoutPopup);
		sfShopSkinCarePage.clickOnCloseButtonForCheckoutPopUp();

		// Enter Product Quantity as zero
		qtyToEnter = "0";
		sfShopSkinCarePage.clickOnFirstProductQuickViewButtonAndReturnProductName();
		sfShopSkinCarePage.enterQuantityonQuickViewPopup(qtyToEnter);
		sfShopSkinCarePage.addProductToCartFromQuickViewPopup(TestConstants.ORDER_TYPE_ENROLLMENT);
		qtyOnCheckoutPopup = sfShopSkinCarePage.getQuantityAddedFromCheckoutPopup();
		s_assert.assertTrue(qtyOnCheckoutPopup.equals("1"),"Product Quantity is not found same on Checkout popup. Expected : 1. Actual : "+qtyOnCheckoutPopup);
		sfShopSkinCarePage.clickOnCloseButtonForCheckoutPopUp();

		// Enter Product Quantity as Special character
		qtyToEnter = "$";
		sfShopSkinCarePage.clickOnFirstProductQuickViewButtonAndReturnProductName();
		sfShopSkinCarePage.enterQuantityonQuickViewPopup(qtyToEnter);
		sfShopSkinCarePage.addProductToCartFromQuickViewPopup(TestConstants.ORDER_TYPE_ENROLLMENT);
		qtyOnCheckoutPopup = sfShopSkinCarePage.getQuantityAddedFromCheckoutPopup();
		s_assert.assertTrue(qtyOnCheckoutPopup.equals("1"),"Product Quantity is not found same on Checkout popup. Expected : 1. Actual : "+qtyOnCheckoutPopup);
		sfShopSkinCarePage.clickOnCloseButtonForCheckoutPopUp();

		// Enter Product Quantity as blank
		qtyToEnter = "";
		sfShopSkinCarePage.clickOnFirstProductQuickViewButtonAndReturnProductName();
		sfShopSkinCarePage.enterQuantityonQuickViewPopup(qtyToEnter);
		sfShopSkinCarePage.addProductToCartFromQuickViewPopup(TestConstants.ORDER_TYPE_ENROLLMENT);
		qtyOnCheckoutPopup = sfShopSkinCarePage.getQuantityAddedFromCheckoutPopup();
		s_assert.assertTrue(qtyOnCheckoutPopup.equals("1"),"Product Quantity is not found same on Checkout popup. Expected : 1. Actual : "+qtyOnCheckoutPopup);
		s_assert.assertAll();
	}

	/***
	 * qTest : TC-158 Product Details Page- Product Pricing (Consultant)
	 * Description : This test validates product price for different country
	 *     
	 */
	@Test(enabled=true)
	public void testProductDetailsPageProductPricingConsultant_158(){
		sfHomePage.loginToStoreFront(consultantWithPulseAndWithCRP(), password,true);
		sfShopSkinCarePage=sfHomePage.clickAllProducts();
		String consultantPrice=sfShopSkinCarePage.getProductPrice(TestConstants.PRODUCT_NUMBER);
		String retailAndSvPrice=sfShopSkinCarePage.getProductRetailAndSVPrice(TestConstants.PRODUCT_NUMBER);
		s_assert.assertTrue(consultantPrice.contains("$") && consultantPrice.contains("Your Price:") && retailAndSvPrice.contains("Retail:") && retailAndSvPrice.contains("$") && retailAndSvPrice.contains("QV"),"Expected price should contain '$' , 'Retail', 'Your Price' and 'QV'");
		s_assert.assertAll();
	}

	/***
	 * qTest : TC-159 Product Details Page- Product Pricing (PC)
	 * Description : This test validates product price for different country
	 *     
	 */
	@Test(enabled=true)
	public void testProductDetailsPageProductPricingPC_159(){
		sfHomePage.loginToStoreFront(pcUserWithPWSSponsor(), password,true);
		sfShopSkinCarePage=sfHomePage.clickAllProducts();
		String consultantPrice=sfShopSkinCarePage.getProductPrice(TestConstants.PRODUCT_NUMBER);
		String retailAndSvPrice=sfShopSkinCarePage.getProductRetailAndSVPrice(TestConstants.PRODUCT_NUMBER);
		s_assert.assertTrue(consultantPrice.contains("$") && consultantPrice.contains("Your Price:") && retailAndSvPrice.contains("Retail:"),"Expected price should contain '$' ,'Your Price' and 'Retail'");
		s_assert.assertAll();
	}

	/***
	 * qTest : TC-160 Featured Products Category Landing Page
	 * Description : This test validates click on featured products category and validate page
	 *     
	 */
	@Test(enabled=true)
	public void testFeaturedProductsCategoryLandingPage_160(){
		String category="FEATURED";
		sfShopSkinCarePage = sfHomePage.navigateToShopSkincareLink(category);
		String pageTitle=sfShopSkinCarePage.getCurrentpageTitle();
		s_assert.assertTrue(sfShopSkinCarePage.isProductsDisplayedOnPage() && pageTitle.contains(category),"Expected featured products not displayed for selected category or Expected page title contains:"+category+" But actual on UI is: "+pageTitle);
		s_assert.assertAll();
	}

	/***
	 * qTest : TC-165 Cart Page- PC Perks Promotion Message/Link - Anonymous user
	 * Description : This test validates PC Perks Promotion Message/Link should be displayed
	 *     
	 */
	@Test(enabled=true)
	public void testCartPagePCPerksPromotionMessageLinkAnonymousUser_165(){
		sfShopSkinCarePage=sfHomePage.clickAllProducts();
		sfShopSkinCarePage.addProductToCart(TestConstants.PRODUCT_NUMBER, TestConstants.ORDER_TYPE_ENROLLMENT);
		sfShopSkinCarePage.checkoutTheCartFromPopUp();
		s_assert.assertTrue(sfShopSkinCarePage.isPCPerksPromoMsgDisplayed(),"PC Perks promo msg is NOT displayed for anonymous user");
		s_assert.assertAll();
	}

	/***
	 * qTest : TC-166 Cart Page- PC Perks Promotion Message/Link - Retail user
	 * Description : This test validates PC Perks Promotion Message/Link should be displayed
	 *     
	 */
	@Test(enabled=true)
	public void testCartPagePCPerksPromotionMessageLinkRetailUser_166(){
		sfHomePage.loginToStoreFront(rcWithOrderWithoutSponsor(), password,true);
		sfShopSkinCarePage=sfHomePage.clickAllProducts();
		sfShopSkinCarePage.addProductToCart(TestConstants.PRODUCT_NUMBER, TestConstants.ORDER_TYPE_ENROLLMENT);
		sfShopSkinCarePage.checkoutTheCartFromPopUp();
		s_assert.assertTrue(sfShopSkinCarePage.isPCPerksPromoMsgDisplayed(),"PC Perks promo msg is NOT displayed RC user");
		s_assert.assertAll();
	}

	/***
	 * qTest : TC-167 Cart Page- PC Perks Promotion Message/Link - Preferred Customer
	 * Description : This test validates PC Perks Promotion Message/Link should not be displayed
	 *     
	 */
	@Test(enabled=true)
	public void testCartPagePCPerksPromotionMessageLinkPreferredCustomer_167(){
		sfHomePage.loginToStoreFront(pcUserWithPWSSponsor(), password,true);
		sfShopSkinCarePage=sfHomePage.clickAllProducts();
		sfShopSkinCarePage.addProductToCart(TestConstants.PRODUCT_NUMBER, TestConstants.ORDER_TYPE_ADHOC);
		sfShopSkinCarePage.checkoutTheCartFromPopUp();
		s_assert.assertFalse(sfShopSkinCarePage.isPCPerksPromoMsgDisplayed(),"PC Perks promo msg is NOT displayed for anonymous user");
		s_assert.assertAll();	
	}

	/***
	 * qTest : TC-168 Cart Page- PC Perks Promotion Message/Link - Consultant User
	 * Description : This test validates PC Perks Promotion Message/Link should not be displayed
	 *     
	 */
	@Test(enabled=true)
	public void testCartPagePCPerksPromotionMessageLinkConsultantUser_168(){
		sfHomePage.loginToStoreFront(consultantWithPulseAndWithCRP(), password,true);
		sfShopSkinCarePage=sfHomePage.clickAllProducts();
		sfShopSkinCarePage.addProductToCart(TestConstants.PRODUCT_NUMBER, TestConstants.ORDER_TYPE_ADHOC);
		sfShopSkinCarePage.checkoutTheCartFromPopUp();
		s_assert.assertFalse(sfShopSkinCarePage.isPCPerksPromoMsgDisplayed(),"PC Perks promo msg is NOT displayed for anonymous user");
		s_assert.assertAll();	
	}


	/***
	 * qTest : TC-503 Product Pricing - PC
	 * Description : This test validates the presence of price of product for PC User
	 * 
	 */
	@Test(enabled=true)
	public void testProductPricingPC_503(){
		sfHomePage.loginToStoreFront(pcUserWithPWSSponsor(),password,true);
		//		sfShopSkinCarePage = sfHomePage.clickOnCategoryFromShopSkinCare("REDEFINE");
		//		s_assert.assertTrue(sfShopSkinCarePage.isRetailPricePresentForProductNumber("1"),"Retail price is not present for Product on category Page for PC User");
		//		s_assert.assertTrue(sfShopSkinCarePage.isSpecificPricePresentForProductNumber("1"),"Specific price is not present for Product on category Page for PC User");
		sfShopSkinCarePage=sfHomePage.clickAllProducts();
		s_assert.assertTrue(sfShopSkinCarePage.isRetailPricePresentForProductNumber("1"),"Retail price is not present for Product on All Products listing Page for PC User");
		s_assert.assertTrue(sfShopSkinCarePage.isSpecificPricePresentForProductNumber("1"),"Specific price is not present for Product on All Products listing Page for PC User");
		sfShopSkinCarePage.clickOnQuickViewLinkForProduct("1");
		s_assert.assertTrue(sfShopSkinCarePage.isRetailPricePresentOnQuickViewPopUp(),"Retail Price is not present for Product on Quick view popup for PC User");
		s_assert.assertTrue(sfShopSkinCarePage.isPricePresentOnQuickViewPopup(),"Specific Price is not present for Product on Quick view popup for PC User");
		sfProductDetailPage = sfShopSkinCarePage.clickOnViewProductDetailsLinkOnQuickViewPopup();
		s_assert.assertTrue(sfProductDetailPage.isRetailPricePresentOnPDPPage(),"Retail Price is not present for Product on Product detail Page for PC User");
		s_assert.assertTrue(sfProductDetailPage.isPricePresentOnPDPAsExpected(),"Specific Price is not present for Product on Product detail Page for PC User");
		s_assert.assertAll();
	}

	/***
	 * qTest : TC-504 Product Pricing - Consultant
	 * Description : This test validates the presence of price of product for Consultant User
	 * 
	 */
	@Test(enabled=true)
	public void testProductPricingConsultant_504(){
		sfHomePage.loginToStoreFront(consultantWithPulseAndWithCRP(),password,true);
		//		sfShopSkinCarePage = sfHomePage.clickOnCategoryFromShopSkinCare("REDEFINE");
		//		s_assert.assertTrue(sfShopSkinCarePage.isRetailPricePresentForProductNumber("1"),"Retail price is not present for Product on category Page for Consultant");
		//		s_assert.assertTrue(sfShopSkinCarePage.isSpecificPricePresentForProductNumber("1"),"Specific price is not present for Product on category Page for Consultant");
		sfShopSkinCarePage=sfHomePage.clickAllProducts();
		s_assert.assertTrue(sfShopSkinCarePage.isRetailPricePresentForProductNumber("1"),"Retail price is not present for Product on All Products listing Page for Consultant");
		s_assert.assertTrue(sfShopSkinCarePage.isSpecificPricePresentForProductNumber("1"),"Specific price is not present for Product on All Products listing Page for Consultant");
		sfShopSkinCarePage.clickOnQuickViewLinkForProduct("1");
		s_assert.assertTrue(sfShopSkinCarePage.isRetailPricePresentOnQuickViewPopUp(),"Retail Price is not present for Product on Quick view popup for Consultant");
		s_assert.assertTrue(sfShopSkinCarePage.isPricePresentOnQuickViewPopup(),"Specific Price is not present for Product on Quick view popup for Consultant");
		sfProductDetailPage = sfShopSkinCarePage.clickOnViewProductDetailsLinkOnQuickViewPopup();
		s_assert.assertTrue(sfProductDetailPage.isRetailPricePresentOnPDPPage(),"Retail Price is not present for Product on Product detail Page for Consultant");
		s_assert.assertTrue(sfProductDetailPage.isPricePresentOnPDPAsExpected(),"Specific Price is not present for Product on Product detail Page for Consultant");
		s_assert.assertAll();
	}

	/***
	 * qTest : TC-337 Product Details Page- Product images
	 * Description : This test validates click on product name to view product details and click on product image
	 *     
	 */
	@Test(enabled=true)
	public void testProductDetailsPageProductImages_337(){
		sfShopSkinCarePage=sfHomePage.clickAllProducts();
		sfProductDetailPage=sfShopSkinCarePage.clickNameOfProduct(TestConstants.PRODUCT_NUMBER);
		s_assert.assertTrue(sfProductDetailPage.isProductImageDisplayed(),"Expected user is not redirected to product detail page");
		sfProductDetailPage.clickToViewLargerImage();
		s_assert.assertTrue(sfProductDetailPage.isProductImageDisplayedInLargerSize(),"Expected product image is not displayed in larger size");
		sfProductDetailPage.closeZoomImage();
		s_assert.assertFalse(sfProductDetailPage.isProductImageDisplayedInLargerSize(),"Expected product image overlay(X) is not closed");
		s_assert.assertAll();
	}

	/***
	 * qTest : TC-196 Product Detail Page
	 * Description : This test validates product details page with different scenarios
	 *     
	 */
	@Test(enabled=true)
	public void testProductDetailPage_196(){
		String productName = null;
		String productNameOnPDP = null;
		String recentlyViewedProduct = null;

		sfShopSkinCarePage=sfHomePage.clickAllProducts();
		productName = sfShopSkinCarePage.getProductNameFromAllProductPage(TestConstants.PRODUCT_NUMBER);
		sfProductDetailPage = sfShopSkinCarePage.clickNameOfProduct(TestConstants.PRODUCT_NUMBER);
		productNameOnPDP = sfProductDetailPage.getProductNameFromProductDetailsPage();
		s_assert.assertTrue(productNameOnPDP.contains(productName),"Expected first product name is:"+productName+" But Actual on product details page is:"+productNameOnPDP);
		sfShopSkinCarePage = sfProductDetailPage.clickAllProducts();
		sfShopSkinCarePage.clickNameOfProduct(sfShopSkinCarePage.updateQuantityByOne(TestConstants.PRODUCT_NUMBER));
		recentlyViewedProduct = sfProductDetailPage.clickRecentlyViewedProductNameAndReturnProductName();
		productNameOnPDP = sfProductDetailPage.getProductNameFromProductDetailsPage();
		s_assert.assertTrue(productNameOnPDP.toLowerCase().contains(recentlyViewedProduct.toLowerCase()),"Expected recently viewed product name is:"+recentlyViewedProduct+" But actual product name on PDP is:" +productNameOnPDP);
		sfShopSkinCarePage=sfProductDetailPage.clickAllProducts();
		sfShopSkinCarePage.clickOnQuickViewLinkForProduct(TestConstants.PRODUCT_NUMBER);
		sfShopSkinCarePage.clickOnViewProductDetailsLinkOnQuickViewPopup();
		productNameOnPDP = sfProductDetailPage.getProductNameFromProductDetailsPage();
		s_assert.assertTrue(productNameOnPDP.contains(productName)," Expected product name at all products page is:"+productName+" But actual product name at PDP is:"+productNameOnPDP);
		sfProductDetailPage.clickSearchIcon();
		sfProductDetailPage.searchProduct(productName);
		s_assert.assertTrue(sfShopSkinCarePage.isProductPresentOnPage(productName),"Searched product : " + productName + " is not displayed on the page");
		s_assert.assertAll();
	}

	/***
	 * qTest : TC-70 Persistent Cart on Same device
	 * Description : This test validates login with consultant,PC and RC and observe the products
	 *     are added to cart successfully and remain persisent when cart is visited again.
	 */
	@Test(enabled=true)
	public void testAddProductToCartAndVerifyAfterRELogin_70(){
		int productInAdhocCart;
		int newProductCount;
		//Add product to consultant adhoc cart.
		sfHomePage.loginToStoreFront(consultantWithPulseAndWithCRP(), password,true);
		sfCartPage = sfHomePage.clickMiniCartBagLink();
		sfCartPage.removeAllProductsFromCart();
		s_assert.assertFalse(sfCartPage.isProductPresentInCart(),"No product expected in cart but product are present in cart");
		sfHomePage.clickRodanAndFieldsLogo();
		sfShopSkinCarePage=sfHomePage.clickAllProducts();
		sfShopSkinCarePage.addProductToCart(TestConstants.PRODUCT_NUMBER, TestConstants.ORDER_TYPE_ADHOC);
		sfCartPage = sfShopSkinCarePage.checkoutTheCartFromPopUp();
		s_assert.assertTrue(sfCartPage.isProductPresentInCart(),"Newly Added product not present in adhoc cart of consultant");
		productInAdhocCart = sfCartPage.getProductCountInAdhocCart();
		sfCartPage.clickWelcomeDropdown();
		sfCartPage.logout();
		sfHomePage.loginToStoreFront(consultantWithPulseAndWithCRP(), password,true);
		sfHomePage.clickMiniCartBagLink();
		newProductCount = sfCartPage.getProductCountInAdhocCart();
		s_assert.assertTrue(newProductCount==productInAdhocCart,"Expected product count"+productInAdhocCart+" while actual on UI "+newProductCount);
		sfCartPage.clickWelcomeDropdown();
		sfCartPage.logout();
		//Add  product to PC adhoc cart.
		sfHomePage.loginToStoreFront(pcUserWithPWSSponsor(), password,true);
		sfCartPage = sfHomePage.clickMiniCartBagLink();
		sfCartPage.removeAllProductsFromCart();
		s_assert.assertFalse(sfCartPage.isProductPresentInCart(),"No product expected in cart but product are present in cart");
		sfHomePage.clickRodanAndFieldsLogo();
		sfShopSkinCarePage=sfHomePage.clickAllProducts();
		sfShopSkinCarePage.addProductToCart(TestConstants.PRODUCT_NUMBER, TestConstants.ORDER_TYPE_ADHOC);
		sfCartPage = sfShopSkinCarePage.checkoutTheCartFromPopUp();
		s_assert.assertTrue(sfCartPage.isProductPresentInCart(),"Newly Added product not present in adhoc cart of PC user");
		productInAdhocCart = sfCartPage.getProductCountInAdhocCart();
		sfCartPage.clickWelcomeDropdown();
		sfCartPage.logout();
		sfHomePage.loginToStoreFront(pcUserWithPWSSponsor(),password,true);
		sfHomePage.clickMiniCartBagLink();
		newProductCount = sfCartPage.getProductCountInAdhocCart();
		s_assert.assertTrue(newProductCount==productInAdhocCart,"Expected product count"+productInAdhocCart+" while actual on UI "+newProductCount);
		sfCartPage.clickWelcomeDropdown();
		sfCartPage.logout();
		//Add  product to RC adhoc cart.
		sfHomePage.loginToStoreFront(rcWithOrderWithoutSponsor(),password,true);
		sfCartPage = sfHomePage.clickMiniCartBagLink();
		sfCartPage.removeAllProductsFromCart();
		s_assert.assertFalse(sfCartPage.isProductPresentInCart(),"No product expected in cart but product are present in cart");
		sfHomePage.clickRodanAndFieldsLogo();
		sfShopSkinCarePage=sfHomePage.clickAllProducts();
		sfShopSkinCarePage.addProductToCart(TestConstants.PRODUCT_NUMBER, TestConstants.ORDER_TYPE_ENROLLMENT);
		sfCartPage = sfShopSkinCarePage.checkoutTheCartFromPopUp();
		s_assert.assertTrue(sfCartPage.isProductPresentInCart(),"Newly Added product not present in adhoc cart of RC user");
		productInAdhocCart = sfCartPage.getProductCountInAdhocCart();
		sfCartPage.clickWelcomeDropdown();
		sfCartPage.logout();
		sfHomePage.loginToStoreFront(rcWithOrderWithoutSponsor(), password,true);
		sfHomePage.clickMiniCartBagLink();
		newProductCount = sfCartPage.getProductCountInAdhocCart();
		s_assert.assertTrue(newProductCount==productInAdhocCart,"Expected product count"+productInAdhocCart+" while actual on UI "+newProductCount);
		s_assert.assertAll();
	}

	/***
	 * qTest : TC-61 Sort/Order
	 * 
	 * Description : This test validates the sorting of product base on price filter applied
	 * and also sorting for different category based on price filter.
	 * 				
	 */ 
	@Test(enabled=true)
	public void testSortProductBaseOnPriceFilter_61(){
		sfShopSkinCarePage = sfHomePage.clickAllProducts();
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
	 * qTest:TC-154 Product Detail pages- PC Perks Promo as Anonymous user
	 * 
	 * Description: This test verifies the PC Perks promo msg
	 * for anonymous user and also the Learn More link
	 */
	@Test(enabled=true)
	public void testPCPerksPromoMsgAnonymousUser_154(){
		sfShopSkinCarePage = sfHomePage.clickAllProducts();
		sfProductDetailPage=sfShopSkinCarePage.clickNameOfProductOnAllProductPage(TestConstants.PRODUCT_NUMBER);
		s_assert.assertTrue(sfProductDetailPage.isDescriptionTabPresentAtProductDetailsPage(), "Not on Product details page");
		//		s_assert.assertTrue(sfShopSkinCarePage.isProductImagePresentAtQuickViewPopup(),
		//				"Product image is not present at Quick view popup");
		//		s_assert.assertTrue(sfShopSkinCarePage.isPCPerksPromoMessageAndSaveAmountPresentOnQuickViewPopup(),
		//				"PC Perks Promo message is NOT present on Quick view popup for anonymous user");
		//		s_assert.assertTrue(sfShopSkinCarePage.isViewProductDetailsLinkDisplayedOnQuickViewPopup(),
		//				"View product Details link is not presnt on quick view poup");		
		sfShopSkinCarePage.clickLearnMoreLink();
		s_assert.assertTrue(sfShopSkinCarePage.isLearnMoreAboutPCPromoPopupDisplayed(), "pc perks promo popup not displayed after clicking on learn more link");
		sfShopSkinCarePage.closePCPerksPromoPopUp();
		s_assert.assertFalse(sfShopSkinCarePage.isLearnMoreAboutPCPromoPopupDisplayed(), "pc perks promo popup has NOT closed");
		s_assert.assertAll();
	}

	/***
	 * qTest:TC-155 Product Detail pages- PC Perks Promo as RC user
	 * 
	 * Description: This test verifies the PC Perks promo msg
	 * for RC user and also the Learn More link
	 */
	@Test(enabled=true)
	public void testPCPerksPromoMsgRCUser_155(){
		sfHomePage.loginToStoreFront(rcWithOrderWithoutSponsor(),password,true);
		sfShopSkinCarePage = sfHomePage.clickAllProducts();
		sfProductDetailPage=sfShopSkinCarePage.clickNameOfProductOnAllProductPage(TestConstants.PRODUCT_NUMBER);
		s_assert.assertTrue(sfProductDetailPage.isDescriptionTabPresentAtProductDetailsPage(), "Not on Product details page");
		//		s_assert.assertTrue(sfShopSkinCarePage.isProductImagePresentAtQuickViewPopup(),
		//				"Product image is not present at Quick view popup");
		//		s_assert.assertTrue(sfShopSkinCarePage.isPCPerksPromoMessageAndSaveAmountPresentOnQuickViewPopup(),
		//				"PC Perks Promo message is NOT present on Quick view popup for anonymous user");
		//		s_assert.assertTrue(sfShopSkinCarePage.isViewProductDetailsLinkDisplayedOnQuickViewPopup(),
		//				"View product Details link is not presnt on quick view poup");		
		sfShopSkinCarePage.clickLearnMoreLink();
		s_assert.assertTrue(sfShopSkinCarePage.isLearnMoreAboutPCPromoPopupDisplayed(), "pc perks promo popup not displayed after clicking on learn more link");
		sfShopSkinCarePage.closePCPerksPromoPopUp();
		s_assert.assertFalse(sfShopSkinCarePage.isLearnMoreAboutPCPromoPopupDisplayed(), "pc perks promo popup has NOT closed");
		s_assert.assertAll();
	}
	/***
	 * qTest:TC-156 Product details page- Image gallery
	 * 
	 * Description: This test verifies the products's image zoom functionality 
	 */
	@Test(enabled=true)
	public void testProductDetailsImageGallery_156(){
		//Duplicate Test: same as TC-337 Product Details Page- Product images
	}

	/***
	 * qTest : TC-68 Cart Page- Remove a product
	 * Description : This test validates add multiple product to cart and remove product from cart functionality
	 *   
	 */
	@Test(enabled=true)
	public void testAddMultipleProductToCartAndRemoveTillCartEmpty_68(){
		String productNumber = TestConstants.PRODUCT_NUMBER;
		sfShopSkinCarePage = sfHomePage.clickAllProducts();
		sfShopSkinCarePage.addProductToCart(productNumber, TestConstants.ORDER_TYPE_ENROLLMENT);
		sfCartPage = sfShopSkinCarePage.checkoutTheCartFromPopUp();
		sfCartPage.clickAddMoreItemsBtn();
		productNumber = sfShopSkinCarePage.getProductNumberIncrementedByOne(productNumber);
		sfShopSkinCarePage.addProductToCart(productNumber, TestConstants.ORDER_TYPE_ENROLLMENT);
		sfCartPage = sfShopSkinCarePage.checkoutTheCartFromPopUp();
		sfCartPage.clickAddMoreItemsBtn();
		productNumber = sfShopSkinCarePage.getProductNumberIncrementedByOne(productNumber);
		sfShopSkinCarePage.addProductToCart(productNumber, TestConstants.ORDER_TYPE_ENROLLMENT);
		sfShopSkinCarePage.checkoutTheCartFromPopUp();
		sfCartPage.clickAddMoreItemsBtn();
		productNumber = sfShopSkinCarePage.getProductNumberIncrementedByOne(productNumber);
		sfShopSkinCarePage.addProductToCart(productNumber, TestConstants.ORDER_TYPE_ENROLLMENT);
		sfShopSkinCarePage.checkoutTheCartFromPopUp();
		s_assert.assertTrue(sfCartPage.isProductPresentInCart(),"No product is present in cart.");
		sfCartPage.removeAllProductsFromCart();
		s_assert.assertFalse(sfCartPage.isProductPresentInCart(),"No product expected in cart but product are present in cart");
		s_assert.assertAll();
	}

	/***
	 * qTest : TC-161 Cart Page- Anonymous User - Ad Hoc Cart
	 * Description : This test validates adhoc product is added to the cart and verifies from product popup
	 *     
	 */
	@Test(enabled=true)
	public void testCartPageAnonymousUserAdHocCart_161(){
		String allProduct = "ALL PRODUCTS";
		int numberOfProductInCart;
		int newProductCount;
		String itemsOfProduct = null;
		String newItemsOfProduct = null;
		String updatedQuantity = null;

		sfShopSkinCarePage = sfHomePage.clickAllProducts();
		sfShopSkinCarePage.addProductToCart(TestConstants.PRODUCT_NUMBER, TestConstants.ORDER_TYPE_ENROLLMENT);
		sfCartPage=sfShopSkinCarePage.checkoutTheCartFromPopUp();
		numberOfProductInCart=sfCartPage.getProductCountInAdhocCart();
		sfCartPage.clickRodanAndFieldsLogo();
		//View cart page through mini cart.
		sfHomePage.clickMiniCartBagLink();
		newProductCount = sfCartPage.getProductCountInAdhocCart();
		itemsOfProduct = sfCartPage.getQuantityOfProductFromCart("1");
		updatedQuantity = sfCartPage.updateQuantityByOne(itemsOfProduct);
		s_assert.assertTrue(newProductCount==numberOfProductInCart,"Expected product count"+numberOfProductInCart+" while actual on UI "+newProductCount);
		sfCartPage.clickRodanAndFieldsLogo();
		//Navigate to cart page through product popup.
		sfShopSkinCarePage=sfHomePage.clickAllProducts();
		sfShopSkinCarePage.addProductToCart(TestConstants.PRODUCT_NUMBER, TestConstants.ORDER_TYPE_ENROLLMENT);
		sfCartPage = sfShopSkinCarePage.checkoutTheCartFromPopUp();
		newItemsOfProduct = sfCartPage.getQuantityOfProductFromCart("1");
		s_assert.assertTrue(newItemsOfProduct.equalsIgnoreCase(updatedQuantity),"Expected items in cart"+updatedQuantity+" but actual in UI"+newItemsOfProduct);
		s_assert.assertAll();	
	}

	/***
	 * qTest : TC-500 Cart Page- Checkout CTA - Anonymous - Create account
	 * Description : This test validates add a product to cart and enter details to create an account
	 *     
	 */
	@Test(enabled=true)
	public void testCartPageCheckoutCTAAnonymousCreateAccount_500(){
		String userType=TestConstants.USER_TYPE_PC;
		lastName = TestConstants.LAST_NAME;
		timeStamp = CommonUtils.getCurrentTimeStamp();
		email = firstName+timeStamp+TestConstants.EMAIL_SUFFIX;

		sfShopSkinCarePage=sfHomePage.clickAllProducts();
		sfShopSkinCarePage.addProductToCart(TestConstants.PRODUCT_NUMBER, TestConstants.ORDER_TYPE_ENROLLMENT);
		sfShopSkinCarePage.checkoutTheCartFromPopUp();
		sfCheckoutPage=sfShopSkinCarePage.checkoutTheCart();
		s_assert.assertTrue(sfCheckoutPage.isLoginOrRegisterTextDisplayed()," user is not redirected to expected login page");
		sfCheckoutPage.fillNewUserDetails(userType, firstName, lastName, email, password);
		sfCheckoutPage.clickCreateAccountButton(TestConstants.USER_TYPE_PC);
		String currentURL=sfCheckoutPage.getCurrentURL().toLowerCase();
		s_assert.assertTrue(currentURL.contains("cart"),"Current url should contain checkout but actual on UI is "+currentURL);
		s_assert.assertAll();
	}
	/***
	 * qTest : TC-220 Cart Page- PC Perks Terms and Conditions - anonymous user
	 * Description : This test validates PC Perks Terms and Conditions Link for Anonymous user
	 * 
	 */
	@Test(enabled=false)
	public void testCartPagePCPerksTermsAndConditionsAnonymousUser_220(){
		String currentWindowID = null; 
		String currentURL = null;
		String utlToAssertForTCDetailsPage = "common/pdf/Archives/PCPerks-TCs";
		sfShopSkinCarePage = sfHomePage.clickAllProducts();
		sfShopSkinCarePage.addProductToCart(TestConstants.PRODUCT_NUMBER, TestConstants.ORDER_TYPE_ENROLLMENT);
		sfCartPage = sfShopSkinCarePage.checkoutTheCartFromPopUp();
		sfCheckoutPage = sfCartPage.clickCheckoutBtn();
		currentWindowID = CommonUtils.getCurrentWindowHandle();
		sfCheckoutPage.clickPCTermsAndConditionsLink();
		sfCheckoutPage.switchToChildWindow(currentWindowID);
		currentURL = sfCheckoutPage.getCurrentURL();
		s_assert.assertTrue(currentURL.contains(utlToAssertForTCDetailsPage),"Current URL : " + currentURL + " does not contain : " + utlToAssertForTCDetailsPage);
		sfCheckoutPage.switchToParentWindow(currentWindowID);
		s_assert.assertAll();
	}

	/***
	 * qTest : TC-162 Cart Page- Retail User - Ad Hoc Cart
	 * Description : This test validates adhoc product is added to the cart and verifies from product popup
	 *     
	 */
	@Test(enabled=true)
	public void testCartPageRetailUserAdHocCart_162(){
		int numberOfProductInCart;
		String itemsOfProduct = null;
		String newItemsOfProduct = null;
		String updatedQuantity = null;
		int newProductCount;
		sfHomePage.loginToStoreFront(rcWithOrderWithoutSponsor(), password,true);
		//Remove all products from cart.
		sfCartPage = sfHomePage.clickMiniCartBagLink();
		sfCartPage.removeAllProductsFromCart();
		s_assert.assertFalse(sfCartPage.isProductPresentInCart(),"No product expected in cart but product are present in cart");
		sfHomePage.clickRodanAndFieldsLogo();
		//Add new product to cart page.
		sfShopSkinCarePage=sfHomePage.clickAllProducts();
		sfShopSkinCarePage.addProductToCart(TestConstants.PRODUCT_NUMBER, TestConstants.ORDER_TYPE_ENROLLMENT);
		sfCartPage = sfShopSkinCarePage.checkoutTheCartFromPopUp();
		s_assert.assertTrue(sfCartPage.isProductPresentInCart(),"Newly Added product not present in adhoc cart");
		numberOfProductInCart = sfCartPage.getProductCountInAdhocCart();
		sfCartPage.clickRodanAndFieldsLogo();
		//Access cart page via mini cart.
		sfHomePage.clickMiniCartBagLink();
		newProductCount = sfCartPage.getProductCountInAdhocCart();
		itemsOfProduct = sfCartPage.getQuantityOfProductFromCart("1");
		updatedQuantity = sfCartPage.updateQuantityByOne(itemsOfProduct);
		s_assert.assertTrue(newProductCount==numberOfProductInCart,"Expected product count"+numberOfProductInCart+" while actual on UI "+newProductCount);
		sfCartPage.clickRodanAndFieldsLogo();
		//Access cart page via product popup.
		sfShopSkinCarePage=sfHomePage.clickAllProducts();
		sfShopSkinCarePage.addProductToCart(TestConstants.PRODUCT_NUMBER, TestConstants.ORDER_TYPE_ENROLLMENT);
		sfCartPage = sfShopSkinCarePage.checkoutTheCartFromPopUp();
		newItemsOfProduct = sfCartPage.getQuantityOfProductFromCart("1");
		s_assert.assertTrue(newItemsOfProduct.equalsIgnoreCase(updatedQuantity),"Expected items in cart"+updatedQuantity+" but actual in UI"+newItemsOfProduct);
		s_assert.assertAll();	
	}

	/***
	 * qTest : TC-602 Proceed to Checkout Confirmation alert for Ad-hoc orders - Consultant
	 * 
	 * Description : This test validates the checkout confirmation popup for consulatnt user.
	 *     
	 */ 
	@Test(enabled=true)
	public void testProceedToCheckoutConfirmationAlertForAdhocOrdersConsultant_602(){
		String currentURL = null;
		String urlToAssertForCartPage = "cart";
		String urlToAssertForCheckoutPage = "checkout";
		sfHomePage.loginToStoreFront(consultantWithPulseAndWithCRP(), password,true);
		sfShopSkinCarePage = sfHomePage.clickAllProducts();
		sfShopSkinCarePage.addProductToCart(TestConstants.PRODUCT_NUMBER, TestConstants.ORDER_TYPE_ADHOC);
		sfCartPage = sfShopSkinCarePage.checkoutTheCartFromPopUp();
		sfCartPage.clickCheckoutTheCartFromCartPage();
		sfCartPage.clickCloseBtnOfCheckoutConfirmationPopup();
		currentURL = sfCartPage.getCurrentURL();
		s_assert.assertTrue(currentURL.contains(urlToAssertForCartPage),"Expected URL should contain "+urlToAssertForCartPage+" but actual on UI is "+currentURL);
		s_assert.assertTrue(sfCartPage.isYourShoppingCartHeaderPresentOnCartPage(),"Your Shopping cart header is not present as expected on cart page");
		sfCartPage.clickCheckoutTheCartFromCartPage();
		sfCheckoutPage = sfCartPage.clickOkOnCheckoutConfirmationPopup();
		currentURL = sfCheckoutPage.getCurrentURL();
		s_assert.assertTrue(currentURL.contains(urlToAssertForCheckoutPage),"Expected URL should contain "+urlToAssertForCheckoutPage+" but actual on UI is "+currentURL);
		s_assert.assertTrue(sfCheckoutPage.isShippingLinkPresentAtCheckoutPage(),"Shipping Link is not present on Checkout page when clicked on Ok button on confirmation popup");
		s_assert.assertAll();
	}

	/***
	 * qTest : TC-169 PC Perks information overlay
	 * 
	 * Description : This test validates the PC Perks Promotion Overlay
	 *     
	 */ 
	@Test(enabled=true) 
	public void testPCPerksInformationOverlay_169(){
		String overlayPromoMsg = null;
		sfShopSkinCarePage = sfHomePage.clickAllProducts();
		sfShopSkinCarePage.addProductToCart(TestConstants.PRODUCT_NUMBER, TestConstants.ORDER_TYPE_ENROLLMENT);
		sfCartPage = sfShopSkinCarePage.checkoutTheCartFromPopUp();
		sfCartPage.clickLearnMoreLink();
		s_assert.assertTrue(sfCartPage.isPCPerksPromotionPopupPresent(),"PC Perks Promotion overlay is not present");
		overlayPromoMsg = sfCartPage.getPromotionOverlayContent();
		s_assert.assertTrue(overlayPromoMsg.contains(TestConstants.PROMOTION_OVERLAY_MSG),"Promotion overlay msg is not present as expected");
		sfCartPage.clickCloseBtnOfPromotionOverlay();
		s_assert.assertFalse(sfCartPage.isPCPerksPromotionPopupPresent(),"PC Perks Promotion overlay is still present after clicking on close Button");

		sfCartPage.clickLearnMoreLink();
		s_assert.assertTrue(sfCartPage.isPCPerksPromotionPopupPresent(),"PC Perks Promotion overlay is not present");
		overlayPromoMsg = sfCartPage.getPromotionOverlayContent();
		s_assert.assertTrue(overlayPromoMsg.contains(TestConstants.PROMOTION_OVERLAY_MSG),"Promotion overlay msg is not present as expected");
		sfCartPage.pressEscapeKeyForDismissingPromotionOverlay();
		s_assert.assertFalse(sfCartPage.isPCPerksPromotionPopupPresent(),"PC Perks Promotion overlay is still present after after clicking outside of the poupup");
		s_assert.assertAll();
	}

	/***
	 * qTest : TC-69 Cart Page- Add More Items CTA
	 * Description : This test validates login with consultant,PC and RC and observe the products
	 *     are added to cart successfully.
	 */
	@Test(enabled=true)
	public void testAddMultipleProductsToCartAfterLogin_69(){
		String itemInAdhocCart= "1";
		String noOfItemFromUI = null;   
		//Verify anonymous user cart page product.
		sfShopSkinCarePage = sfHomePage.clickAllProducts();
		sfShopSkinCarePage.addProductToCart(TestConstants.PRODUCT_NUMBER, TestConstants.ORDER_TYPE_ENROLLMENT);
		sfCartPage = sfShopSkinCarePage.checkoutTheCartFromPopUp();
		noOfItemFromUI = sfCartPage.getNumberOfItemFromMiniCart();
		s_assert.assertTrue(noOfItemFromUI.equalsIgnoreCase(itemInAdhocCart), "Expected no of item is "+itemInAdhocCart+" Actual on UI is "+noOfItemFromUI);
		sfCartPage.clickAddMoreItemsBtn();
		s_assert.assertTrue(sfShopSkinCarePage.isProductsDisplayedOnPage(),"All product page not present after clicking continue shopping for anonymous user.");
		sfShopSkinCarePage.clickRodanAndFieldsLogo();
		//Add multiple products to consultant adhoc and autoship cart.
		//Adhoc cart
		sfHomePage.loginToStoreFront(consultantWithPulseAndWithCRP(), password,true);
		sfShopSkinCarePage=sfHomePage.clickAllProducts();
		sfShopSkinCarePage.addProductToCart(TestConstants.PRODUCT_NUMBER, TestConstants.ORDER_TYPE_ADHOC,validProductId);
		sfCartPage = sfShopSkinCarePage.checkoutTheCartFromPopUp();
		sfCartPage.clickAddMoreItemsBtn();
		s_assert.assertTrue(sfShopSkinCarePage.isProductsDisplayedOnPage(),"All product page not present after clicking continue shopping on adhoc cart of consultant.");
		//Autoship cart.
		sfShopSkinCarePage.addProductToCart(TestConstants.PRODUCT_NUMBER, TestConstants.ORDER_TYPE_CRP,validProductId);
		sfCartPage = sfShopSkinCarePage.checkoutTheCartFromPopUp();
		sfCartPage.clickAddMoreItemsBtn();
		s_assert.assertTrue(sfShopSkinCarePage.isProductsDisplayedOnPage(),"All product page not present after clicking continue shopping on autoship cart of consultant.");
		sfShopSkinCarePage.clickRodanAndFieldsLogo();
		sfHomePage.clickWelcomeDropdown();
		sfHomePage.logout();
		navigateToStoreFrontBaseURL();
		//Add multiple products to PC adhoc And Autoship cart
		//PC adhoc cart
		sfHomePage.loginToStoreFront(pcUserWithPWSSponsor(),password,true);
		sfShopSkinCarePage=sfHomePage.clickAllProducts();
		sfShopSkinCarePage.addProductToCart(TestConstants.PRODUCT_NUMBER, TestConstants.ORDER_TYPE_ADHOC,validProductId);
		sfCartPage = sfShopSkinCarePage.checkoutTheCartFromPopUp();
		sfCartPage.clickAddMoreItemsBtn();
		s_assert.assertTrue(sfShopSkinCarePage.isProductsDisplayedOnPage(),"All product page not present after clicking continue shopping on adhoc cart of PC.");
		sfShopSkinCarePage.clickRodanAndFieldsLogo();
		//PC Autoship Cart
		sfShopSkinCarePage.clickAllProducts();
		sfShopSkinCarePage.addProductToCart(TestConstants.PRODUCT_NUMBER, TestConstants.ORDER_TYPE_PC_PERKS,validProductId);
		sfCartPage = sfShopSkinCarePage.checkoutTheCartFromPopUp();
		sfCartPage.clickAddMoreItemsBtn();
		s_assert.assertTrue(sfShopSkinCarePage.isProductsDisplayedOnPage(),"All product page not present after clicking continue shopping on autoship cart of consultant.");
		sfShopSkinCarePage.clickRodanAndFieldsLogo();
		sfHomePage.clickWelcomeDropdown();
		sfHomePage.logout();
		navigateToStoreFrontBaseURL();
		//Add multiple products to RC adhoc And Autoship cart
		//RC adhoc cart
		sfHomePage.loginToStoreFront(rcWithOrderWithoutSponsor(),password,true);
		sfShopSkinCarePage=sfHomePage.clickAllProducts();
		sfShopSkinCarePage.addProductToCart(TestConstants.PRODUCT_NUMBER, TestConstants.ORDER_TYPE_ENROLLMENT,validProductId);
		sfCartPage = sfShopSkinCarePage.checkoutTheCartFromPopUp();
		sfCartPage.clickAddMoreItemsBtn();
		s_assert.assertTrue(sfShopSkinCarePage.isProductsDisplayedOnPage(),"All product page not present after clicking continue shopping on RC adhoc cart.");
		s_assert.assertAll();
	}

	/***
	 * qTest : TC-63 Sort/Order Removal by clicking Clear All link
	 * 
	 * Description : This test validates the sorting of product base on price filter applied
	 * and also Unsorting of product when filter removed by clicking clear all link.
	 *     
	 */ 
	@Test(enabled=true)
	public void testSortAndUnsortProductBaseOnPriceFilterApplied_63(){
		sfShopSkinCarePage=sfHomePage.clickAllProducts();
		sfShopSkinCarePage.refineProductByCategoryAndReturnCategoryName();
		sfShopSkinCarePage.productPriceFilterLowToHighSelect();
		s_assert.assertTrue(sfShopSkinCarePage.isPriceFilterLowToHighAppliedSuccessfully(),"Selected Price filter 'Low To High' is not applied to product successfully");
		//Click clear all link and verify all filters are removed.
		sfShopSkinCarePage.selectClearAllLink(); 
		s_assert.assertTrue(sfShopSkinCarePage.isDefaultFilterAppliedSuccessfully(),"Default Filter has not been applied successfully on Price filter");
		s_assert.assertAll();
	}

	/***
	 * qTest : TC-603 Proceed to Checkout Confirmation alert for Ad-hoc orders - PC
	 * 
	 * Description : This test validates the checkout confirmation popup for PC user.
	 *     
	 */ 
	@Test
	public void testProceedToCheckoutConfirmationAlertForAdhocOrdersPC_603(){
		String currentURL = null;
		String urlToAssertForCartPage = "cart";
		String urlToAssertForCheckoutPage = "checkout";
		sfHomePage.loginToStoreFront(pcUserWithPWSSponsor(), password,true);
		sfShopSkinCarePage = sfHomePage.clickAllProducts();
		sfShopSkinCarePage.refineProductByCategory(TestConstants.PC_PERKS_AUTOSHIP_PRODUCT_CATEGORY);
		sfShopSkinCarePage.addProductToCart(TestConstants.PRODUCT_NUMBER, TestConstants.ORDER_TYPE_ADHOC);
		sfCartPage = sfShopSkinCarePage.checkoutTheCartFromPopUp();
		sfCartPage.clickCheckoutTheCartFromCartPage();
		sfCartPage.clickCloseBtnOfCheckoutConfirmationPopup();
		currentURL = sfCartPage.getCurrentURL();
		s_assert.assertTrue(currentURL.contains(urlToAssertForCartPage),"Expected URL should contain "+urlToAssertForCartPage+" but actual on UI is "+currentURL);
		s_assert.assertTrue(sfCartPage.isYourShoppingCartHeaderPresentOnCartPage(),"Your Shopping cart header is not present as expected on cart page");
		sfCartPage.clickCheckoutTheCartFromCartPage();
		sfCheckoutPage = sfCartPage.clickOkOnCheckoutConfirmationPopup();
		currentURL = sfCheckoutPage.getCurrentURL();
		s_assert.assertTrue(currentURL.contains(urlToAssertForCheckoutPage),"Expected URL should contain "+urlToAssertForCheckoutPage+" but actual on UI is "+currentURL);
		s_assert.assertTrue(sfCheckoutPage.isShippingLinkPresentAtCheckoutPage(),"Shipping Link is not present on Checkout page when clicked on Ok button on confirmation popup");
		s_assert.assertAll();
	}

	/***
	 * qTest : TC-256 Added to your Shopping Cart popup for adhoc flow
	 * Description : This test validates the 'Add more items' and 'checkout' funtionality of checkout popup for One time order
	 * 
	 */
	@Test(enabled=true)
	public void testAddedToYourShoppingCartPopupForAdhocFlow_256(){
		String firstProductName = null;
		String secondProductName = null;
		String productNameOnCheckoutPopup = null;
		String productPriceOnListingPage = null;
		String productPriceOnCheckoutPopup = null;
		String cardType = TestConstants.CARD_TYPE;
		String cardNumber = TestConstants.CARD_NUMBER;
		String cardName = TestConstants.CARD_NAME;
		String CVV = TestConstants.CVV;
		sfHomePage.loginToStoreFront(consultantWithPulseAndWithCRP(), password,true);
		sfShopSkinCarePage = sfHomePage.clickAllProducts();
		sfShopSkinCarePage.refineProductByCategory(TestConstants.CONSULTANT_CRP_AUTOSHIP_PRODUCT_CATEGORY);
		firstProductName = sfShopSkinCarePage.getProductNameFromAllProductPage(TestConstants.PRODUCT_NUMBER);
		productPriceOnListingPage = sfShopSkinCarePage.addProductToCart(TestConstants.PRODUCT_NUMBER, TestConstants.ORDER_TYPE_ADHOC);
		productNameOnCheckoutPopup = sfShopSkinCarePage.getProductNameFromCheckoutPopup();
		productPriceOnCheckoutPopup = sfShopSkinCarePage.getProductPriceFromCheckoutPopup();
		s_assert.assertTrue(productNameOnCheckoutPopup.contains(firstProductName),"Name of Product added to Bag does not matches with the product name on checkout popup. Expeced : " + firstProductName + ". Actual : " + productNameOnCheckoutPopup);
		s_assert.assertTrue(productPriceOnListingPage.trim().equals(productPriceOnCheckoutPopup.trim()),"Price of Product added to Bag does not matches with the product price on checkout popup for Product : " + firstProductName + ". Expeced : " + productPriceOnListingPage + ". Actual : " + productPriceOnCheckoutPopup);
		s_assert.assertTrue(sfShopSkinCarePage.isQuantityOfProductonIsPresentOnCheckoutPopUp(),"Quantity of Product is not present on checkout pop up when added Product : " + firstProductName);
		s_assert.assertTrue(sfShopSkinCarePage.isCheckoutButtonPresentOnCheckoutPopup(),"Checkout Button is not present on checkout pop up when added Product : " + firstProductName);
		s_assert.assertTrue(sfShopSkinCarePage.isAddMoreItemsButtonPresentOnCheckoutPopup(),"Add more Items Button is not present on checkout pop up when added Product : " + firstProductName);
		s_assert.assertTrue(sfShopSkinCarePage.isCloseButtonPresentForCheckoutPopup(),"Close Button is not present for checkout pop up when added Product : " + firstProductName);
		sfShopSkinCarePage.clickOnAddMoreItemsOnCheckoutPopUp();
		sfShopSkinCarePage.refineProductByCategory(TestConstants.CONSULTANT_CRP_AUTOSHIP_PRODUCT_CATEGORY);
		secondProductName = sfShopSkinCarePage.getProductNameFromAllProductPage(String.valueOf(Integer.parseInt(TestConstants.PRODUCT_NUMBER)+1));
		productPriceOnListingPage = sfShopSkinCarePage.addProductToCart(String.valueOf(Integer.parseInt(TestConstants.PRODUCT_NUMBER)+1), TestConstants.ORDER_TYPE_ADHOC);
		productNameOnCheckoutPopup = sfShopSkinCarePage.getProductNameFromCheckoutPopup();
		productPriceOnCheckoutPopup = sfShopSkinCarePage.getProductPriceFromCheckoutPopup();
		s_assert.assertTrue(productNameOnCheckoutPopup.contains(secondProductName),"Name of Product added to Bag does not matches with the product name on checkout popup. Expeced : " + secondProductName + ". Actual : " + productNameOnCheckoutPopup);
		s_assert.assertTrue(productPriceOnListingPage.trim().equals(productPriceOnCheckoutPopup.trim()),"Price of Product added to Bag does not matches with the product price on checkout popup for Product : " + secondProductName + ". Expeced : " + productPriceOnListingPage + ". Actual : " + productPriceOnCheckoutPopup);
		s_assert.assertTrue(sfShopSkinCarePage.isQuantityOfProductonIsPresentOnCheckoutPopUp(),"Quantity of Product is not present on checkout pop up when added Product : " + secondProductName);
		s_assert.assertTrue(sfShopSkinCarePage.isCheckoutButtonPresentOnCheckoutPopup(),"Checkout Button is not present on checkout pop up when added Product : " + secondProductName);
		s_assert.assertTrue(sfShopSkinCarePage.isAddMoreItemsButtonPresentOnCheckoutPopup(),"Add more Items Button is not present on checkout pop up when added Product : " + secondProductName);
		s_assert.assertTrue(sfShopSkinCarePage.isCloseButtonPresentForCheckoutPopup(),"Close Button is not present for checkout pop up when added Product : " + secondProductName);
		sfCartPage = sfShopSkinCarePage.checkoutTheCartFromPopUp();
		s_assert.assertTrue(sfCartPage.isYourShoppingCartHeaderPresentOnCartPage(),"Your Shopping cart header is not present when redirected after clicking the checkout button from checkout pop up");
		sfCheckoutPage = sfCartPage.checkoutTheCart();
		sfCheckoutPage.clickSaveButton();
		sfCheckoutPage.clickShippingDetailsNextbutton();
		sfCheckoutPage.clickAddNewBillingProfileButton();
		sfCheckoutPage.enterUserBillingDetails(cardType, cardNumber, cardName, CVV);
		sfCheckoutPage.clickBillingDetailsNextbutton();
		if(sfCheckoutPage.hasTokenizationFailed()==true){
			sfCheckoutPage.enterUserBillingDetails(cardType, cardNumber, cardName, CVV);
			sfCheckoutPage.clickBillingDetailsNextbutton();
		}
		sfCheckoutPage.selectTermsAndConditionsChkBox();
		sfCheckoutPage.clickPlaceOrderButton();
		s_assert.assertTrue(sfCheckoutPage.isOrderPlacedSuccessfully(), "Adhoc order is not placed successfully by consultant");
		s_assert.assertAll();
	}

	/***
	 * qTest : TC-258 Added to your PC perks Autoship popup for PC autoship flow
	 * Description : This test validates the 'Add more items' and 'checkout' funtionality of checkout popup for PC User and PC perks order
	 * 
	 */
	@Test(enabled=true)
	public void testAddedToYourPCPerksAutoshipPopupForPCAutoshipFlow_258(){
		String firstProductName = null;
		String secondProductName = null;
		String productNameOnCheckoutPopup = null;
		String productPriceOnListingPage = null;
		String productPriceOnCheckoutPopup = null;
		String secondProductNumber = Integer.toString(Integer.parseInt(TestConstants.PRODUCT_NUMBER) + 1);
		sfHomePage.loginToStoreFront(pcUserWithPWSSponsor(), password,true);
		sfShopSkinCarePage = sfHomePage.clickAllProducts();
		sfShopSkinCarePage.refineProductByCategory(TestConstants.PC_PERKS_AUTOSHIP_PRODUCT_CATEGORY);
		firstProductName = sfShopSkinCarePage.getProductNameFromAllProductPage(TestConstants.PRODUCT_NUMBER);
		productPriceOnListingPage = sfShopSkinCarePage.addProductToCart(TestConstants.PRODUCT_NUMBER, TestConstants.ORDER_TYPE_PC_PERKS);
		productNameOnCheckoutPopup = sfShopSkinCarePage.getProductNameFromCheckoutPopup();
		productPriceOnCheckoutPopup = sfShopSkinCarePage.getProductPriceFromCheckoutPopup();
		s_assert.assertTrue(productNameOnCheckoutPopup.contains(firstProductName),"Name of Product added to Bag does not matches with the product name on checkout popup. Expeced : " + firstProductName + ". Actual : " + productNameOnCheckoutPopup);
		s_assert.assertTrue(productPriceOnListingPage.trim().contains(productPriceOnCheckoutPopup.trim()),"Price of Product added to Bag does not matches with the product price on checkout popup for Product : " + firstProductName + ". Expeced : " + productPriceOnListingPage + ". Actual : " + productPriceOnCheckoutPopup);
		s_assert.assertTrue(sfShopSkinCarePage.isQuantityOfProductonIsPresentOnCheckoutPopUp(),"Quantity of Product is not present on checkout pop up when added Product : " + firstProductName);
		s_assert.assertTrue(sfShopSkinCarePage.isCheckoutButtonPresentOnCheckoutPopup(),"Checkout Button is not present on checkout pop up when added Product : " + firstProductName);
		s_assert.assertTrue(sfShopSkinCarePage.isAddMoreItemsButtonPresentOnCheckoutPopup(),"Add more Items Button is not present on checkout pop up when added Product : " + firstProductName);
		s_assert.assertTrue(sfShopSkinCarePage.isCloseButtonPresentForCheckoutPopup(),"Close Button is not present for checkout pop up when added Product : " + firstProductName);
		sfShopSkinCarePage.clickOnAddMoreItemsOnCheckoutPopUp();
		sfShopSkinCarePage.refineProductByCategory(TestConstants.PC_PERKS_AUTOSHIP_PRODUCT_CATEGORY);
		secondProductName = sfShopSkinCarePage.getProductNameFromAllProductPage(secondProductNumber);
		productPriceOnListingPage = sfShopSkinCarePage.addProductToCart(secondProductNumber, TestConstants.ORDER_TYPE_PC_PERKS);
		productNameOnCheckoutPopup = sfShopSkinCarePage.getProductNameFromCheckoutPopup();
		productPriceOnCheckoutPopup = sfShopSkinCarePage.getProductPriceFromCheckoutPopup();
		s_assert.assertTrue(productNameOnCheckoutPopup.contains(secondProductName),"Name of Product added to Bag does not matches with the product name on checkout popup. Expected : " + secondProductName + ". Actual : " + productNameOnCheckoutPopup);
		s_assert.assertTrue(productPriceOnListingPage.trim().equalsIgnoreCase(productPriceOnCheckoutPopup.trim()),"Price of Product added to Bag does not matches with the product price on checkout popup for Product : " + secondProductName + ". Expeced : " + productPriceOnListingPage + ". Actual : " + productPriceOnCheckoutPopup);
		s_assert.assertTrue(sfShopSkinCarePage.isQuantityOfProductonIsPresentOnCheckoutPopUp(),"Quantity of Product is not present on checkout pop up when added Product : " + secondProductName);
		s_assert.assertTrue(sfShopSkinCarePage.isCheckoutButtonPresentOnCheckoutPopup(),"Checkout Button is not present on checkout pop up when added Product : " + secondProductName);
		s_assert.assertTrue(sfShopSkinCarePage.isAddMoreItemsButtonPresentOnCheckoutPopup(),"Add more Items Button is not present on checkout pop up when added Product : " + secondProductName);
		s_assert.assertTrue(sfShopSkinCarePage.isCloseButtonPresentForCheckoutPopup(),"Close Button is not present for checkout pop up when added Product : " + secondProductName);
		sfCartPage = sfShopSkinCarePage.checkoutTheCartFromPopUp();
		s_assert.assertTrue(sfCartPage.isPCPerksCartHeaderPresentOnCartPage(),"Pc perks cart header is not present when redirected after clicking the checkout button from checkout pop up");
		s_assert.assertAll();
	}

	/***
	 * qTest : TC-340 Product Listing Page- PC
	 * Description : This test validates login with pc and observe the products w.r.t the options
	 *     
	 */
	@Test(enabled=true)
	public void testProductListingPagePC_340(){
		//Already covered in other tests
	}

	/***
	 * qTest : TC-341 Product Listing Page- Consultant
	 * Description : This test validates login with consultant and observe the products w.r.t the options
	 *     
	 */
	@Test(enabled=true)
	public void testProductListingPageConsultant_341(){
		//Already covered in other tests
	}

	/***
	 * qTest : TC-257 Added to your CRP Autoship Cart Popup For CRP flow
	 * 
	 * Description : This test validates checkout popup details and autoship cart
	 */ 
	@Test(enabled=true)
	public void testAddedToYourCRPAutoshipCartPopupForCRPFlow_257(){
		String productName = null;
		String productNameOnCheckoutPopup = null;
		String productPriceOnCheckoutPopup = null;
		String productPriceOnListingPage = null;
		String currentURL  = null;
		sfHomePage.loginToStoreFront(consultantWithPulseAndWithCRP(), password,true);
		sfShopSkinCarePage = sfHomePage.clickAllProducts();
		sfShopSkinCarePage.refineProductByCategory(TestConstants.CONSULTANT_CRP_AUTOSHIP_PRODUCT_CATEGORY);
		productName = sfShopSkinCarePage.getProductNameFromAllProductPage(TestConstants.PRODUCT_NUMBER);
		productPriceOnListingPage = sfShopSkinCarePage.addProductToCart(TestConstants.PRODUCT_NUMBER, TestConstants.ORDER_TYPE_CRP);
		productNameOnCheckoutPopup = sfShopSkinCarePage.getProductNameFromCheckoutPopup();
		productPriceOnCheckoutPopup = sfShopSkinCarePage.getProductPriceFromCheckoutPopup();
		s_assert.assertTrue(productNameOnCheckoutPopup.contains(productName),"Name of Product added to Bag does not matches with the product name on checkout popup. Expeced : " + productName + ". Actual : " + productNameOnCheckoutPopup);
		s_assert.assertTrue(productPriceOnListingPage.trim().contains(productPriceOnCheckoutPopup.trim()),"Price of Product added to Bag does not matches with the product price on checkout popup for Product : " + productName + ". Expeced : " + productPriceOnListingPage + ". Actual : " + productPriceOnCheckoutPopup);
		s_assert.assertTrue(sfShopSkinCarePage.isQuantityOfProductonIsPresentOnCheckoutPopUp(),"Quantity of Product is not present on checkout pop up when added Product : " + productName);
		s_assert.assertTrue(sfShopSkinCarePage.isCheckoutButtonPresentOnCheckoutPopup(),"Checkout Button is not present on checkout pop up when added Product : " + productName);
		s_assert.assertTrue(sfShopSkinCarePage.isAddMoreItemsButtonPresentOnCheckoutPopup(),"Add more Items Button is not present on checkout pop up when added Product : " + productName);
		s_assert.assertTrue(sfShopSkinCarePage.isCloseButtonPresentForCheckoutPopup(),"Close Button is not present for checkout pop up when added Product : " + productName);
		sfShopSkinCarePage.clickOnAddMoreItemsOnCheckoutPopUp();
		currentURL = sfShopSkinCarePage.getCurrentURL().toLowerCase();
		s_assert.assertTrue(currentURL.contains("shopskincare"), "Expected URL should contains 'shopskincare' but actual on UI is"+currentURL);
		sfShopSkinCarePage.refineProductByCategory(TestConstants.CONSULTANT_CRP_AUTOSHIP_PRODUCT_CATEGORY);
		sfShopSkinCarePage.addProductToCart(TestConstants.PRODUCT_NUMBER, TestConstants.ORDER_TYPE_CRP);
		sfAutoshipCartPage = sfShopSkinCarePage.checkoutTheCartFromPopUpForAutoship();
		currentURL = sfAutoshipCartPage.getCurrentURL().toLowerCase();
		s_assert.assertTrue(currentURL.contains("autoship/cart"), "Expected URL should contains 'autoship/cart' but actual on UI is"+currentURL);
		s_assert.assertAll();
	}

}
