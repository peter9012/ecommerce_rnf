package com.rf.test.website.rehabitat.storeFront;

import org.testng.annotations.Test;

import com.rf.core.utils.CommonUtils;
import com.rf.core.website.constants.TestConstants;
import com.rf.test.website.rehabitat.storeFront.baseTest.StoreFrontWebsiteBaseTest;

public class ProductsAndCartDetailsTest extends StoreFrontWebsiteBaseTest{

	/***
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
		s_assert.assertFalse(sfShopSkinCarePage.isPriceFilterLowToHighAppliedSuccessfully(),"Selected Price filter 'Default' is not applied but 'Low To High' applied to product successfully");
		s_assert.assertFalse(sfShopSkinCarePage.isPriceFilterHighToLowAppliedSuccessfully(),"Selected Price filter 'Default' is not applied but 'High To Low' applied to product successfully");
		s_assert.assertAll();
	}

	/***
	 * qTest : TC-63 Sort/Order Removal by clicking Clear All link
	 * 
	 * Description : This test validates the sorting of product base on price filter applied
	 * and also Unsorting of product when filter removed by clicking clear all link.
	 * 				
	 */ 
	@Test(enabled=false) //TODO Blocked as no clear all link is present.
	public void testSortAndUnsortProductBaseOnPriceFilterApplied_63(){
		String allProduct = "ALL PRODUCTS";
		sfShopSkinCarePage=sfHomePage.navigateToShopSkincareLink(allProduct);
		sfShopSkinCarePage.productPriceFilterLowToHigh();
		s_assert.assertTrue(sfShopSkinCarePage.isPriceFilterLowToHighAppliedSuccessfully(),"Selected Price filter 'Low To High' is not applied to product successfully");
		//Click clear all link and verify all filters are removed.
		//sfShopSkinCarePage.selectClearAllLink(); create this method.
		s_assert.assertFalse(sfShopSkinCarePage.isPriceFilterLowToHighAppliedSuccessfully(),"Selected Price filter 'Low To High' is still applied after clicking clear all link.");
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
	@Test(enabled=true) 
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

		sfHomePage.loginToStoreFront(TestConstants.PC_EMAIL, password);
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
	@Test(enabled=false) //TODO Incomplete as not asserted for product added to cart.
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
	@Test(enabled=true)
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
	@Test(enabled=true)
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
	@Test(enabled=true)
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
	@Test(enabled=false)//TODO Unable to login at checkout page
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
	@Test(enabled=true)
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
	@Test(enabled=true)
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
	@Test(enabled=true)
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
	 * qTest : TC-220 Cart Page- PC Perks Terms and Conditions - anonymous user
	 * Description : This test validates PC Perks Terms and Conditions Link for Anonymous user
	 * 
	 */
	@Test(enabled=true)
	public void testCartPagePCPerksTermsAndConditionsAnonymousUser_220(){
		String currentWindowID = null; 
		String currentURL = null;
		String utlToAssertForTCDetailsPage = "common/pdf/Archives/PCPerks-TCs";
		sfShopSkinCarePage = sfHomePage.clickAllProducts();
		sfShopSkinCarePage.addFirstProductToBag();
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
	 * qTest : TC-222 Cart Page- PC Perks Terms and Conditions - PC User
	 * Description : This test validates PC Perks Terms and Conditions Link for PC User
	 * 
	 */
	@Test(enabled=false)
	public void testCartPagePCPerksTermsAndConditionsPCUser_222(){
		String currentWindowID = null; 
		String currentURL = null;
		String utlToAssertForTCDetailsPage = "common/pdf/Archives/PCPerks-TCs";
		sfHomePage.loginToStoreFront(TestConstants.PC_USERNAME, password);
		sfShopSkinCarePage = sfHomePage.clickAllProducts();
		sfShopSkinCarePage.selectFirstProduct();
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
		sfHomePage.loginToStoreFront(TestConstants.RC_USERNAME, password);
		sfShopSkinCarePage = sfHomePage.clickAllProducts();
		sfShopSkinCarePage.addFirstProductToBag();
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
	 * qTest : TC-258 Added to your PC perks Autoship popup for PC autoship flow
	 * Description : This test validates the 'Add more items' and 'checkout' funtionality of checkout popup for PC User and PC perks order
	 * 
	 */
	@Test(enabled=true)//NEEDS FIX
	public void testAddedToYourPCPerksAutoshipPopupForPCAutoshipFlow_258(){
		String firstProductName = null;
		String secondProductName = null;
		String productNameOnCheckoutPopup = null;
		String productPriceOnListingPage = null;
		String productPriceOnCheckoutPopup = null;
		sfHomePage.loginToStoreFront(TestConstants.PC_USERNAME, password);
		sfShopSkinCarePage = sfHomePage.clickAllProducts();
		firstProductName = sfShopSkinCarePage.getFirstProductNameFromAllProductPage();
		//productPriceOnListingPage = sfShopSkinCarePage.addProductToBagForSpecificOrderType(1,"PC Perks Order");
		productPriceOnListingPage=sfShopSkinCarePage.addProductTocartAndReturnProductPriceForProduct(1);
		productNameOnCheckoutPopup = sfShopSkinCarePage.getProductNameFromCheckoutPopup();
		productPriceOnCheckoutPopup = sfShopSkinCarePage.getProductPriceFromCheckoutPopup();
		s_assert.assertTrue(productNameOnCheckoutPopup.contains(firstProductName),"Name of Product added to Bag does not matches with the product name on checkout popup. Expeced : " + firstProductName + ". Actual : " + productNameOnCheckoutPopup);
		s_assert.assertTrue(productPriceOnListingPage.trim().contains(productPriceOnCheckoutPopup.trim()),"Price of Product added to Bag does not matches with the product price on checkout popup for Product : " + firstProductName + ". Expeced : " + productPriceOnListingPage + ". Actual : " + productPriceOnCheckoutPopup);
		s_assert.assertTrue(sfShopSkinCarePage.isQuantityOfProductonIsPresentOnCheckoutPopUp(),"Quantity of Product is not present on checkout pop up when added Product : " + firstProductName);
		s_assert.assertTrue(sfShopSkinCarePage.isCheckoutButtonPresentOnCheckoutPopup(),"Checkout Button is not present on checkout pop up when added Product : " + firstProductName);
		s_assert.assertTrue(sfShopSkinCarePage.isAddMoreItemsButtonPresentOnCheckoutPopup(),"Add more Items Button is not present on checkout pop up when added Product : " + firstProductName);
		s_assert.assertTrue(sfShopSkinCarePage.isCloseButtonPresentForCheckoutPopup(),"Close Button is not present for checkout pop up when added Product : " + firstProductName);
		sfShopSkinCarePage.clickOnAddMoreItemsOnCheckoutPopUp();
		secondProductName = sfShopSkinCarePage.getProductNameFromAllProductPage("2");
		productPriceOnListingPage = null;
		//productPriceOnListingPage = sfShopSkinCarePage.addProductToBagForSpecificOrderType(2,"PC Perks Order");
		productPriceOnListingPage=sfShopSkinCarePage.addProductTocartAndReturnProductPriceForProduct(2);
		productNameOnCheckoutPopup = null;
		productNameOnCheckoutPopup = sfShopSkinCarePage.getProductNameFromCheckoutPopup();
		productPriceOnCheckoutPopup = sfShopSkinCarePage.getProductPriceFromCheckoutPopup();
		s_assert.assertTrue(productNameOnCheckoutPopup.contains(secondProductName),"Name of Product added to Bag does not matches with the product name on checkout popup. Expeced : " + secondProductName + ". Actual : " + productNameOnCheckoutPopup);
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
		sfShopSkinCarePage.addFirstProductToBag();
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
	 * qTest : TC-68 Cart Page- Remove a product
	 * Description : This test validates add multiple product to cart and remove product from cart functionality
	 *   
	 */
	@Test(enabled=true)
	public void testAddMultipleProductToCartAndRemoveTillCartEmpty_68(){
		sfShopSkinCarePage=sfHomePage.clickAllProducts();
		sfShopSkinCarePage.addFirstProductToBag();
		sfCartPage = sfShopSkinCarePage.checkoutTheCartFromPopUp();
		sfCartPage.clickAddMoreItemsBtn();
		sfShopSkinCarePage.addProductToAdhocCart(2);
		sfCartPage = sfShopSkinCarePage.checkoutTheCartFromPopUp();
		sfCartPage.clickAddMoreItemsBtn();
		sfShopSkinCarePage.addProductToAdhocCart(3);
		sfCartPage = sfShopSkinCarePage.checkoutTheCartFromPopUp();
		sfCartPage.clickAddMoreItemsBtn();
		sfShopSkinCarePage.addProductToAdhocCart(4);
		sfCartPage = sfShopSkinCarePage.checkoutTheCartFromPopUp();
		s_assert.assertTrue(sfCartPage.isProductPresentInCart(),"No product is present in cart.");
		sfCartPage.removeAllProductsFromCart();
		s_assert.assertFalse(sfCartPage.isProductPresentInCart(),"No product expected in cart but product are present in cart");
		s_assert.assertAll();
	}

	/***
	 * qTest : TC-69 Cart Page- Add More Items CTA
	 * Description : This test validates login with consultant,PC and RC and observe the products
	 *     are added to cart successfully.
	 */
	@Test(enabled=false) //TODO Pending for consultant and PC autoship cart page
	public void testAddMultipleProductsToCartAfterLogin_69(){
		String usertype = "Consultant";
		String pcUsertype = TestConstants.USER_TYPE_PC;
		String itemInAdhocCart= "1";
		String noOfItemFromUI = null;   
		//Verify anonymous user cart page product.
		sfShopSkinCarePage = sfHomePage.clickAllProducts();
		sfShopSkinCarePage.selectFirstProduct();
		sfCartPage = sfShopSkinCarePage.checkoutTheCartFromPopUp();
		noOfItemFromUI = sfCartPage.getNumberOfItemFromMiniCart();
		s_assert.assertTrue(noOfItemFromUI.equalsIgnoreCase(itemInAdhocCart), "Expected no of item is "+itemInAdhocCart+" Actual on UI is "+noOfItemFromUI);
		sfCartPage.clickAddMoreItemsBtn();
		s_assert.assertTrue(sfShopSkinCarePage.isAllProductPageDisplayed(),"All product page not present after clicking continue shopping for anonymous user.");
		//Add multiple products to consultant adhoc and autoship cart.
		//Adhoc cart
		sfHomePage.loginToStoreFront(TestConstants.CONSULTANT_EMAIL, password);
		sfShopSkinCarePage=sfHomePage.clickAllProducts();
		sfShopSkinCarePage.selectFirstProduct();
		sfCartPage = sfShopSkinCarePage.checkoutTheCartFromPopUp();
		sfCartPage.clickAddMoreItemsBtn();
		s_assert.assertTrue(sfShopSkinCarePage.isAllProductPageDisplayed(),"All product page not present after clicking continue shopping on adhoc cart of consultant.");
		//Autoship cart.
		//sfShopSkinCarePage.addFirstProductToAutoshipCart(usertype);
		//sfAutoshipCartPage = sfShopSkinCarePage.acceptEnrollInCRPPopup();
		//sfCartPage.clickAddMoreItemsBtn();
		//s_assert.assertTrue(sfShopSkinCarePage.isAllProductPageDisplayed(),"All product page not present after clicking continue shopping on autoship cart of consultant.");
		sfShopSkinCarePage.clickRodanAndFieldsLogo();
		sfHomePage.clickWelcomeDropdown();
		sfHomePage.logout();
		//Add multiple products to PC adhoc And Autoship cart
		//PC adhoc cart
		sfHomePage.loginToStoreFront(TestConstants.PC_EMAIL, password);
		sfShopSkinCarePage=sfHomePage.clickAllProducts();
		sfShopSkinCarePage.selectFirstProduct();
		sfCartPage = sfShopSkinCarePage.checkoutTheCartFromPopUp();
		sfCartPage.clickAddMoreItemsBtn();
		s_assert.assertTrue(sfShopSkinCarePage.isAllProductPageDisplayed(),"All product page not present after clicking continue shopping on adhoc cart of PC.");
		//PC Autoship Cart
		//sfShopSkinCarePage.addFirstProductToAutoshipCart(pcUsertype);
		//sfCartPage = sfShopSkinCarePage.checkoutTheCartFromPopUp();
		//sfCartPage.clickAddMoreItemsBtn();
		//s_assert.assertTrue(sfShopSkinCarePage.isAllProductPageDisplayed(),"All product page not present after clicking continue shopping on autoship cart of PC.");
		sfShopSkinCarePage.clickRodanAndFieldsLogo();
		sfHomePage.clickWelcomeDropdown();
		sfHomePage.logout();
		//Add multiple products to RC adhoc And Autoship cart
		//RC adhoc cart
		sfHomePage.loginToStoreFront(TestConstants.RC_EMAIL, password);
		sfShopSkinCarePage=sfHomePage.clickAllProducts();
		sfShopSkinCarePage.selectFirstProduct();
		sfCartPage = sfShopSkinCarePage.checkoutTheCartFromPopUp();
		sfCartPage.clickAddMoreItemsBtn();
		s_assert.assertTrue(sfShopSkinCarePage.isAllProductPageDisplayed(),"All product page not present after clicking continue shopping on RC adhoc cart.");
		s_assert.assertAll();
	}


	/***
	 * qTest : TC-163 Cart Page- Preferred Customer - Ad Hoc Cart
	 * Description : This test validates PC user details on cart page accessed via all products and mini cart..
	 *   
	 */
	@Test(enabled=false)//TODO
	public void testVerifyPCUserProductDetailsOnCartPage_163(){
		String productName = null;
		String productQTY = null;

		//Access cart page via all product page.
		sfHomePage.loginToStoreFront(TestConstants.PC_USERNAME, password);
		sfCartPage = sfHomePage.clickMiniCartBagLink();
		sfCartPage.removeAllProductsFromCart();
		s_assert.assertFalse(sfCartPage.isProductPresentInCart(),"No product expected in cart but product are present in cart");
		sfHomePage.clickRodanAndFieldsLogo();
		sfShopSkinCarePage = sfHomePage.clickAllProducts();
		productName = sfShopSkinCarePage.getFirstProductNameFromAllProductPage();
		sfShopSkinCarePage.selectFirstProduct();
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
	@Test(enabled=false)
	public void testVerifyConsultantUserProductDetailsOnCartPage_164(){
		String productName = null;
		String productQTY = null;

		//Access cart page via all product page.
		sfHomePage.loginToStoreFront(TestConstants.CONSULTANT_USERNAME, password);
		sfCartPage = sfHomePage.clickMiniCartBagLink();
		sfCartPage.removeAllProductsFromCart();
		s_assert.assertFalse(sfCartPage.isProductPresentInCart(),"No product expected in cart but product are present in cart");
		sfHomePage.clickRodanAndFieldsLogo();
		sfShopSkinCarePage = sfHomePage.clickAllProducts();
		productName = sfShopSkinCarePage.getFirstProductNameFromAllProductPage();
		sfShopSkinCarePage.selectFirstProduct();
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
		sfHomePage.loginToStoreFront(TestConstants.RC_USERNAME, password);
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
	 * qTest : TC-157 Product Details page- Qty
	 * Description : This test validates quantity with different values
	 *     
	 */
	@Test(enabled=false) //TODO Incomplete as no quantity update link present on overlay.
	public void testProductDetailsPageQty_157(){
		String productQty=null;
		String updatedQty=null;

		sfShopSkinCarePage=sfHomePage.clickAllProducts();
		sfShopSkinCarePage.clickOnFirstProductQuickViewButtonAndReturnProductName();
		productQty=sfShopSkinCarePage.getProductQuantityFromQuickViewOption();
		updatedQty = sfCartPage.updateQuantityByOne(productQty);
		s_assert.assertAll();
	}

	/***
	 * qTest : TC-158 Product Details Page- Product Pricing (Consultant)
	 * Description : This test validates product price for different country
	 *     
	 */
	@Test(enabled=true)
	public void testProductDetailsPageProductPricingConsultant_158(){
		sfHomePage.loginToStoreFront(TestConstants.CONSULTANT_EMAIL, password);
		sfShopSkinCarePage=sfHomePage.clickAllProducts();
		String consultantPrice=sfShopSkinCarePage.getFirstProductPrice();
		String retailAndSvPrice=sfShopSkinCarePage.getFirstProductRetailAndSVPrice();
		s_assert.assertTrue(consultantPrice.contains("$") && consultantPrice.contains("Your Price:") && retailAndSvPrice.contains("Retail:") && retailAndSvPrice.contains("$") && retailAndSvPrice.contains("Total SV"),"Expected price should contain '$' , 'Retail', 'Your Price' and 'Total SV'");
		s_assert.assertAll();
	}

	/***
	 * qTest : TC-159 Product Details Page- Product Pricing (PC)
	 * Description : This test validates product price for different country
	 *     
	 */
	@Test(enabled=true)
	public void testProductDetailsPageProductPricingPC_159(){
		sfHomePage.loginToStoreFront(TestConstants.PC_EMAIL, password);
		sfShopSkinCarePage=sfHomePage.clickAllProducts();
		String consultantPrice=sfShopSkinCarePage.getFirstProductPrice();
		String retailAndSvPrice=sfShopSkinCarePage.getFirstProductRetailAndSVPrice();
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
	@Test(enabled=false)
	public void testCartPagePCPerksPromotionMessageLinkAnonymousUser_165(){
		sfShopSkinCarePage=sfHomePage.clickAllProducts();
		sfShopSkinCarePage.addFirstProductToBag();
		sfShopSkinCarePage.checkoutTheCartFromPopUp();
		s_assert.assertTrue(sfShopSkinCarePage.isPCPerksPromoMsgDisplayed(),"PC Perks promo msg is NOT displayed for anonymous user");
		s_assert.assertAll();
	}

	/***
	 * qTest : TC-166 Cart Page- PC Perks Promotion Message/Link - Retail user
	 * Description : This test validates PC Perks Promotion Message/Link should be displayed
	 *     
	 */
	@Test(enabled=false)
	public void testCartPagePCPerksPromotionMessageLinkRetailUser_166(){
		sfHomePage.loginToStoreFront(TestConstants.RC_EMAIL, password);
		sfShopSkinCarePage=sfHomePage.clickAllProducts();
		sfShopSkinCarePage.addFirstProductToBag();
		sfShopSkinCarePage.checkoutTheCartFromPopUp();
		s_assert.assertTrue(sfShopSkinCarePage.isPCPerksPromoMsgDisplayed(),"PC Perks promo msg is NOT displayed for anonymous user");
		s_assert.assertAll();
	}

	/***
	 * qTest : TC-167 Cart Page- PC Perks Promotion Message/Link - Preferred Customer
	 * Description : This test validates PC Perks Promotion Message/Link should not be displayed
	 *     
	 */
	@Test(enabled=false)
	public void testCartPagePCPerksPromotionMessageLinkPreferredCustomer_167(){
		sfHomePage.loginToStoreFront(TestConstants.PC_EMAIL, password,true);
		sfShopSkinCarePage=sfHomePage.clickAllProducts();
		sfShopSkinCarePage.addFirstProductToBag();
		sfShopSkinCarePage.checkoutTheCartFromPopUp();
		s_assert.assertFalse(sfShopSkinCarePage.isPCPerksPromoMsgDisplayed(),"PC Perks promo msg is NOT displayed for anonymous user");
		s_assert.assertAll();	
	}

	/***
	 * qTest : TC-168 Cart Page- PC Perks Promotion Message/Link - Consultant User
	 * Description : This test validates PC Perks Promotion Message/Link should not be displayed
	 *     
	 */
	@Test(enabled=false)
	public void testCartPagePCPerksPromotionMessageLinkConsultantUser_168(){
		sfHomePage.loginToStoreFront(TestConstants.CONSULTANT_EMAIL, password,true);
		sfShopSkinCarePage=sfHomePage.clickAllProducts();
		sfShopSkinCarePage.addFirstProductToBag();
		sfShopSkinCarePage.checkoutTheCartFromPopUp();
		s_assert.assertFalse(sfShopSkinCarePage.isPCPerksPromoMsgDisplayed(),"PC Perks promo msg is NOT displayed for anonymous user");
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
		sfHomePage.loginToStoreFront(TestConstants.RC_EMAIL, password);
		//Remove all products from cart.
		sfCartPage = sfHomePage.clickMiniCartBagLink();
		sfCartPage.removeAllProductsFromCart();
		s_assert.assertFalse(sfCartPage.isProductPresentInCart(),"No product expected in cart but product are present in cart");
		sfHomePage.clickRodanAndFieldsLogo();
		//Add new product to cart page.
		sfShopSkinCarePage=sfHomePage.clickAllProducts();
		sfShopSkinCarePage.addFirstProductToBag();
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
		sfShopSkinCarePage.addFirstProductToBag();
		sfCartPage = sfShopSkinCarePage.checkoutTheCartFromPopUp();
		newItemsOfProduct = sfCartPage.getQuantityOfProductFromCart("1");
		s_assert.assertTrue(newItemsOfProduct.equalsIgnoreCase(updatedQuantity),"Expected items in cart"+updatedQuantity+" but actual in UI"+newItemsOfProduct);
		s_assert.assertAll();	
	}

	/***
	 * qTest : TC-503 Product Pricing - PC
	 * Description : This test validates the presence of price of product for PC User
	 * 
	 */
	@Test(enabled=true)
	public void testProductPricingPC_503(){
		sfHomePage.loginToStoreFront(TestConstants.PC_USERNAME, password);
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
		sfHomePage.loginToStoreFront(TestConstants.CONSULTANT_USERNAME, password);
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
		sfProductDetailPage=sfShopSkinCarePage.clickNameOfProductOnAllProductPage("1");
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
	@Test(enabled=false)//TODO incomplete search functionality not working
	public void testProductDetailPage_196(){
		String productName = TestConstants.PRODUCT_NAME;
		sfShopSkinCarePage=sfHomePage.clickAllProducts();
		String firstProductName=sfShopSkinCarePage.getProductNameFromAllProductPage("1");
		sfProductDetailPage=sfShopSkinCarePage.clickNameOfProductOnAllProductPage("1");
		String productNameAtPDP=sfProductDetailPage.getProductNameFromProductDetailsPage();
		s_assert.assertTrue(productNameAtPDP.contains(firstProductName),"Expected first product name is:"+firstProductName+" But Actual on product details page is:"+productNameAtPDP);
		sfShopSkinCarePage=sfProductDetailPage.clickAllProducts();
		sfProductDetailPage=sfShopSkinCarePage.clickNameOfProductOnAllProductPage("2");
		String recentlyViewedProduct=sfProductDetailPage.clickRecentlyViewedProductNameAndReturnProductName();
		productNameAtPDP=sfProductDetailPage.getProductNameFromProductDetailsPage();
		s_assert.assertTrue(productNameAtPDP.toLowerCase().contains(recentlyViewedProduct.toLowerCase()),"Expected recently viewed product name is:"+recentlyViewedProduct+" But actual product name on PDP is:" +productNameAtPDP);
		sfShopSkinCarePage=sfProductDetailPage.clickAllProducts();
		String productNameFromAllProdutcs=sfShopSkinCarePage.clickOnFirstProductQuickViewButtonAndReturnProductName();
		sfProductDetailPage=sfShopSkinCarePage.clickOnViewProductDetailsLinkOnQuickViewPopup();
		productNameAtPDP=sfProductDetailPage.getProductNameFromProductDetailsPage();
		s_assert.assertTrue(productNameAtPDP.contains(productNameFromAllProdutcs)," Expected product name at all products page is:"+productNameFromAllProdutcs+" But actual product name at PDP is:"+productNameAtPDP);
		sfProductDetailPage.clickSearchIcon();
		sfProductDetailPage.searchProduct(productName);
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
		sfHomePage.loginToStoreFront(TestConstants.CONSULTANT_EMAIL, password);
		sfCartPage = sfHomePage.clickMiniCartBagLink();
		sfCartPage.removeAllProductsFromCart();
		s_assert.assertFalse(sfCartPage.isProductPresentInCart(),"No product expected in cart but product are present in cart");
		sfHomePage.clickRodanAndFieldsLogo();
		sfShopSkinCarePage=sfHomePage.clickAllProducts();
		sfShopSkinCarePage.selectFirstProduct();
		sfCartPage = sfShopSkinCarePage.checkoutTheCartFromPopUp();
		s_assert.assertTrue(sfCartPage.isProductPresentInCart(),"Newly Added product not present in adhoc cart of consultant");
		productInAdhocCart = sfCartPage.getProductCountInAdhocCart();
		sfCartPage.clickWelcomeDropdown();
		sfCartPage.logout();
		sfHomePage.loginToStoreFront(TestConstants.CONSULTANT_EMAIL, password);
		sfHomePage.clickMiniCartBagLink();
		newProductCount = sfCartPage.getProductCountInAdhocCart();
		s_assert.assertTrue(newProductCount==productInAdhocCart,"Expected product count"+productInAdhocCart+" while actual on UI "+newProductCount);
		sfCartPage.clickWelcomeDropdown();
		sfCartPage.logout();
		//Add  product to PC adhoc cart.
		sfHomePage.loginToStoreFront(TestConstants.PC_EMAIL, password);
		sfCartPage = sfHomePage.clickMiniCartBagLink();
		sfCartPage.removeAllProductsFromCart();
		s_assert.assertFalse(sfCartPage.isProductPresentInCart(),"No product expected in cart but product are present in cart");
		sfHomePage.clickRodanAndFieldsLogo();
		sfShopSkinCarePage=sfHomePage.clickAllProducts();
		sfShopSkinCarePage.addProductToBagForSpecificOrderType(2,"One Time Order");
		sfCartPage = sfShopSkinCarePage.checkoutTheCartFromPopUp();
		s_assert.assertTrue(sfCartPage.isProductPresentInCart(),"Newly Added product not present in adhoc cart of PC user");
		productInAdhocCart = sfCartPage.getProductCountInAdhocCart();
		sfCartPage.clickWelcomeDropdown();
		sfCartPage.logout();
		sfHomePage.loginToStoreFront(TestConstants.PC_EMAIL, password);
		sfHomePage.clickMiniCartBagLink();
		newProductCount = sfCartPage.getProductCountInAdhocCart();
		s_assert.assertTrue(newProductCount==productInAdhocCart,"Expected product count"+productInAdhocCart+" while actual on UI "+newProductCount);
		sfCartPage.clickWelcomeDropdown();
		sfCartPage.logout();
		//Add  product to RC adhoc cart.
		sfHomePage.loginToStoreFront(TestConstants.RC_EMAIL, password);
		sfCartPage = sfHomePage.clickMiniCartBagLink();
		sfCartPage.removeAllProductsFromCart();
		s_assert.assertFalse(sfCartPage.isProductPresentInCart(),"No product expected in cart but product are present in cart");
		sfHomePage.clickRodanAndFieldsLogo();
		sfShopSkinCarePage=sfHomePage.clickAllProducts();
		sfShopSkinCarePage.addFirstProductToBag();
		sfCartPage = sfShopSkinCarePage.checkoutTheCartFromPopUp();
		s_assert.assertTrue(sfCartPage.isProductPresentInCart(),"Newly Added product not present in adhoc cart of RC user");
		productInAdhocCart = sfCartPage.getProductCountInAdhocCart();
		sfCartPage.clickWelcomeDropdown();
		sfCartPage.logout();
		sfHomePage.loginToStoreFront(TestConstants.RC_EMAIL, password);
		sfHomePage.clickMiniCartBagLink();
		newProductCount = sfCartPage.getProductCountInAdhocCart();
		s_assert.assertTrue(newProductCount==productInAdhocCart,"Expected product count"+productInAdhocCart+" while actual on UI "+newProductCount);
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
		sfHomePage.loginToStoreFront(TestConstants.CONSULTANT_EMAIL, password);
		sfShopSkinCarePage = sfHomePage.clickAllProducts();
		firstProductName = sfShopSkinCarePage.getFirstProductNameFromAllProductPage();
		//productPriceOnListingPage = sfShopSkinCarePage.addProductToBagForSpecificOrderType(1,"One Time Order");
		productPriceOnListingPage=sfShopSkinCarePage.addProductTocartAndReturnProductPriceForProduct(1);
		productNameOnCheckoutPopup = sfShopSkinCarePage.getProductNameFromCheckoutPopup();
		productPriceOnCheckoutPopup = sfShopSkinCarePage.getProductPriceFromCheckoutPopup();
		s_assert.assertTrue(productNameOnCheckoutPopup.contains(firstProductName),"Name of Product added to Bag does not matches with the product name on checkout popup. Expeced : " + firstProductName + ". Actual : " + productNameOnCheckoutPopup);
		s_assert.assertTrue(productPriceOnListingPage.trim().equals(productPriceOnCheckoutPopup.trim()),"Price of Product added to Bag does not matches with the product price on checkout popup for Product : " + firstProductName + ". Expeced : " + productPriceOnListingPage + ". Actual : " + productPriceOnCheckoutPopup);
		s_assert.assertTrue(sfShopSkinCarePage.isQuantityOfProductonIsPresentOnCheckoutPopUp(),"Quantity of Product is not present on checkout pop up when added Product : " + firstProductName);
		s_assert.assertTrue(sfShopSkinCarePage.isCheckoutButtonPresentOnCheckoutPopup(),"Checkout Button is not present on checkout pop up when added Product : " + firstProductName);
		s_assert.assertTrue(sfShopSkinCarePage.isAddMoreItemsButtonPresentOnCheckoutPopup(),"Add more Items Button is not present on checkout pop up when added Product : " + firstProductName);
		s_assert.assertTrue(sfShopSkinCarePage.isCloseButtonPresentForCheckoutPopup(),"Close Button is not present for checkout pop up when added Product : " + firstProductName);
		sfShopSkinCarePage.clickOnAddMoreItemsOnCheckoutPopUp();
		// this step will be removed once add more items functionality start working from checkout pop up.
		//		sfShopSkinCarePage.clickOnCloseButtonForCheckoutPopUp();
		secondProductName = sfShopSkinCarePage.getProductNameFromAllProductPage("2");
		productPriceOnListingPage = null;
		//productPriceOnListingPage = sfShopSkinCarePage.addProductToBagForSpecificOrderType(2,"One Time Order");
		productPriceOnListingPage=sfShopSkinCarePage.addProductTocartAndReturnProductPriceForProduct(2);
		productNameOnCheckoutPopup = null;
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
		//sfCheckoutPage.checkUseMyDeliveryAddressChkBox();
		sfCheckoutPage.clickBillingDetailsNextbutton();
		sfCheckoutPage.selectTermsAndConditionsChkBox();
		sfCheckoutPage.clickPlaceOrderButton();
		s_assert.assertTrue(sfCheckoutPage.isOrderPlacedSuccessfully(), "Adhoc order is not placed successfully by consultant");
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

		sfShopSkinCarePage = sfHomePage.navigateToShopSkincareLink(allProduct);
		sfShopSkinCarePage.addFirstProductToBag();
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
		sfShopSkinCarePage.selectFirstProduct();
		sfCartPage = sfShopSkinCarePage.checkoutTheCartFromPopUp();
		newItemsOfProduct = sfCartPage.getQuantityOfProductFromCart("1");
		s_assert.assertTrue(newItemsOfProduct.equalsIgnoreCase(updatedQuantity),"Expected items in cart"+updatedQuantity+" but actual in UI"+newItemsOfProduct);
		s_assert.assertAll();	
	}

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
		sfHomePage.loginToStoreFront(TestConstants.CONSULTANT_USERNAME, password);
		sfShopSkinCarePage = sfHomePage.clickAllProducts();
		sfShopSkinCarePage.addFirstProductToBag();
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
	 * qTest:TC-154 Product Detail pages- PC Perks Promo as Anonymous user
	 * 
	 * Description: This test verifies the PC Perks promo msg
	 * for anonymous user and also the Learn More link
	 */
	@Test(enabled=false)
	public void testPCPerksPromoMsgAnonymousUser_154(){
		sfShopSkinCarePage = sfHomePage.clickAllProducts();
		sfShopSkinCarePage.clickOnQuickViewLinkForProduct("1");
		s_assert.assertTrue(sfShopSkinCarePage.isProductImagePresentAtQuickViewPopup(),
				"Product image is not present at Quick view popup");
		s_assert.assertTrue(sfShopSkinCarePage.isPCPerksPromoMessageAndSaveAmountPresentOnQuickViewPopup(),
				"PC Perks Promo message is NOT present on Quick view popup for anonymous user");
		s_assert.assertTrue(sfShopSkinCarePage.isViewProductDetailsLinkDisplayedOnQuickViewPopup(),
				"View product Details link is not presnt on quick view poup");		
		sfShopSkinCarePage.clickLearnMoreLinkOnQuickView();
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
	@Test(enabled=false)
	public void testPCPerksPromoMsgRCUser_155(){
		sfHomePage.loginToStoreFront(TestConstants.RC_USERNAME, password);
		sfShopSkinCarePage = sfHomePage.clickAllProducts();
		sfShopSkinCarePage.clickOnQuickViewLinkForProduct("1");
		s_assert.assertTrue(sfShopSkinCarePage.isProductImagePresentAtQuickViewPopup(),
				"Product image is not present at Quick view popup");
		s_assert.assertTrue(sfShopSkinCarePage.isPCPerksPromoMessageAndSaveAmountPresentOnQuickViewPopup(),
				"PC Perks Promo message is NOT present on Quick view popup for anonymous user");
		s_assert.assertTrue(sfShopSkinCarePage.isViewProductDetailsLinkDisplayedOnQuickViewPopup(),
				"View product Details link is not presnt on quick view poup");		
		sfShopSkinCarePage.clickLearnMoreLinkOnQuickView();
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

}
