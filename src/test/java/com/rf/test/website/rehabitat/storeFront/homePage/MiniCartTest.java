package com.rf.test.website.rehabitat.storeFront.homePage;

import org.testng.annotations.Test;

import com.rf.core.website.constants.TestConstants;
import com.rf.test.website.rehabitat.storeFront.baseTest.StoreFrontWebsiteBaseTest;

public class MiniCartTest extends StoreFrontWebsiteBaseTest{

	/***
	 * qTest : TC-419 Mini Cart- Cleared after Checkout
	 * Description : This test validates that mini cart is empty
	 * after placed an order
	 *     
	 */
	@Test(enabled=true)
	public void testMiniCartClearedAfterCheckout_419(){
		String noOfItem = "0";
		String itemInAdhocCart= "1";
		String noOfItemFromUI = null;   
		sfHomePage.loginToStoreFront(TestConstants.CONSULTANT_EMAIL_WITH_CRP_AND_PULSE, password,true);
		sfCartPage = sfHomePage.clickMiniCartBagLink();
		sfCartPage.removeAllProductsFromCart();
		s_assert.assertFalse(sfCartPage.isProductPresentInCart(),"No product expected in cart but product are present in cart");
		sfCartPage.clickRodanAndFieldsLogo();
		sfShopSkinCarePage = sfCartPage.clickAllProducts();
		sfShopSkinCarePage.addProductToCart(TestConstants.PRODUCT_NUMBER, TestConstants.ORDER_TYPE_ADHOC);
		sfShopSkinCarePage.checkoutTheCartFromPopUp();
		noOfItemFromUI = sfHomePage.getNumberOfItemFromMiniCart();
		s_assert.assertTrue(noOfItemFromUI.equalsIgnoreCase(itemInAdhocCart), "Expected no of item is "+itemInAdhocCart+" Actual on UI is "+noOfItemFromUI);
		sfCheckoutPage = sfCartPage.checkoutTheCart();
		sfCheckoutPage.clickSaveButton();
		sfCheckoutPage.clickShippingDetailsNextbutton();
		sfCheckoutPage.clickBillingDetailsNextbutton();
		sfCheckoutPage.selectTermsAndConditionsChkBox();
		sfCheckoutPage.clickPlaceOrderButton();
		s_assert.assertTrue(sfCheckoutPage.isOrderPlacedSuccessfully(), "Adhoc order is not placed successfully by consultant");
		sfCheckoutPage.clickRodanAndFieldsLogo();
		noOfItemFromUI = sfHomePage.getNumberOfItemFromMiniCart();
		s_assert.assertTrue(noOfItemFromUI.equalsIgnoreCase(noOfItem), "Expected no of item is "+noOfItem+" Actual on UI is "+noOfItemFromUI);
		s_assert.assertAll();
	}


	/***
	 * qTest : TC-148 PC user Adds items to their cart and view logo,username, account drop down and mini cart (shouldn't have top navigation)
	 * Description : This test validates R+F logo, welcome dropdown,autoship link, mini cart icon & top navigation bar 
	 * at mini cart page    
	 */
	@Test(enabled=true)
	public void testPCUserAbleToSeeLogoUsernameDropdownAndMiniCartAtCartPage_148(){
		sfHomePage.loginToStoreFront(TestConstants.PC_EMAIL_HAVING_AUTOSHIP, password,true);
		sfShopSkinCarePage = sfHomePage.clickAllProducts();
		sfShopSkinCarePage.addProductToCart(TestConstants.PRODUCT_NUMBER, TestConstants.ORDER_TYPE_ADHOC);
		sfCartPage = sfShopSkinCarePage.checkoutTheCartFromPopUp();
		s_assert.assertTrue(sfCartPage.isRodanAndFieldsLogoPresent(), "Rodan and fields logo is not present at cart page");
		s_assert.assertTrue(sfCartPage.isWelcomeDropdownPresent(), "Welcome dropdown is not present at cart page");
		s_assert.assertTrue(sfCartPage.isAutoshipLinkPresent(), "Autoship link is not present at cart page");
		s_assert.assertTrue(sfCartPage.isMiniCartPresent(), "Mini cart icon is not present at cart page");
		s_assert.assertFalse(sfCartPage.isShopSkincareTextPresent(), "Top navigation bar is present at cart page");
		s_assert.assertAll();
	}

	/***
	 * qTest : TC-149 Consultant user Adds items to their cart and view logo,username, account drop down and mini cart (shouldn't have top navigation)
	 * Description : This test validates R+F logo, welcome dropdown,autoship link, mini cart icon & top navigation bar 
	 * at mini cart page    
	 */
	@Test(enabled=true)
	public void testConsultantUserAbleToSeeLogoUsernameDropdownAndMiniCartAtCartPage_149(){
		sfHomePage.loginToStoreFront(TestConstants.CONSULTANT_EMAIL_WITH_CRP_AND_PULSE,  password,true);
		sfShopSkinCarePage = sfHomePage.clickAllProducts();
		sfShopSkinCarePage.addProductToCart(TestConstants.PRODUCT_NUMBER, TestConstants.ORDER_TYPE_ADHOC);
		sfCartPage = sfShopSkinCarePage.checkoutTheCartFromPopUp();
		s_assert.assertTrue(sfCartPage.isRodanAndFieldsLogoPresent(), "Rodan and fields logo is not present at cart page");
		s_assert.assertTrue(sfCartPage.isWelcomeDropdownPresent(), "Welcome dropdown is not present at cart page");
		//s_assert.assertTrue(sfCartPage.isAutoshipLinkPresent(), "Autoship link is not present at cart page");
		s_assert.assertTrue(sfCartPage.isMiniCartPresent(), "Mini cart icon is not present at cart page");
		s_assert.assertFalse(sfCartPage.isShopSkincareTextPresent(), "Top navigation bar is present at cart page");
		s_assert.assertAll();
	}

	/***
	 * qTest : TC-325 Mini Cart- Autoship
	 * Description : This test validates autoship link and view pc perks cart button
	 */
	@Test(enabled=true)
	public void testMiniCartAutoship_325(){
		sfHomePage.loginToStoreFront(TestConstants.PC_EMAIL_HAVING_AUTOSHIP, password,true);
		sfHomePage.hoverOnAutoshipLink();
		s_assert.assertTrue(sfHomePage.isYourNextPCPerksCartTextPresentAtAutoshipLinkPopup(), "Autoship menu is not populated after hover on autoship link");
		s_assert.assertTrue(sfHomePage.isItemPresentInAutoshipCart(), "Items are not present in autoship cart popup");
		sfAutoshipCartPage = sfHomePage.clickViewPCPerksCartButton();
		String currentURL = sfAutoshipCartPage.getCurrentURL();
		s_assert.assertTrue(currentURL.contains("autoship/cart"), "Expected current url should contain autoship/cart but actual on UI is "+currentURL);
		s_assert.assertAll();
	}

	/***
	 * qTest : TC-343 As a PC or Consultant, I will be able to able to view a mini autoship cart
	 * Description : This test case validates mini autoship cart link
	 */
	@Test(enabled=true)
	public void testPCAbleToViewMiniAutoshipCart_343(){
		//duplicate as TC-325 Mini Cart- Autoship
	}

	/***
	 * qTest : TC-563 Anonymous user adds products to the cart for adhoc order
	 * Description : This test validates that the product added by a anonymous user should 
	 * be present in the cart for the valid user if it login in the same session.  
	 * 
	 *     
	 */
	@Test(enabled=true)
	public void testAnonymousUserAddsProductsToTheCartForAdhocOrder_563(){
		String productName = null;
		String productNameOnCheckoutPopup = null;
		sfHomePage.loginToStoreFront(TestConstants.CONSULTANT_EMAIL_WITH_CRP_AND_PULSE, password,true);
		sfCartPage = sfHomePage.clickMiniCartBagLink();
		sfCartPage.removeAllProductsFromCart();
		sfCartPage.clickWelcomeDropdown();
		sfCartPage.logout();
		sfShopSkinCarePage = sfHomePage.clickAllProducts();
		productName = sfShopSkinCarePage.getProductNameFromAllProductPage("2");
		sfShopSkinCarePage.addProductToCart(TestConstants.PRODUCT_NUMBER, TestConstants.ORDER_TYPE_ADHOC);
		productNameOnCheckoutPopup = sfShopSkinCarePage.getProductNameFromCheckoutPopup();
		s_assert.assertTrue(sfShopSkinCarePage.isAddedToYourShoppingCartHeadlinePresentOnCheckoutPopup(),"Added to ypur shopping cart header is not present on checkout popup");
		s_assert.assertTrue(productNameOnCheckoutPopup.contains(productName),"Name of Product added to Bag does not matches with the product name on checkout popup. Expeced : " + productName + ". Actual : " + productNameOnCheckoutPopup);
		sfShopSkinCarePage.checkoutTheCartFromPopUp();
		s_assert.assertTrue(sfCartPage.isYourShoppingCartHeaderPresentOnCartPage(),"Your Shopping cart header is not present when redirected after clicking the checkout button from checkout pop up");
		s_assert.assertTrue(sfCartPage.isProductAddedToCartPresentOnCartPage(productNameOnCheckoutPopup),"Product added to cart : "+productName+" is not present on the cart page");
		sfCartPage.clickOnCartLoginLink();
		sfCartPage.loginToStoreFrontExcludingClickOnLoginIcon(TestConstants.CONSULTANT_EMAIL_WITH_CRP_AND_PULSE, password);
		sfCartPage.clickMiniCartBagLink();
		s_assert.assertTrue(sfCartPage.isProductAddedToCartPresentOnCartPage(productNameOnCheckoutPopup),"Product added to cart : "+productName+" is not present on the cart page");
		s_assert.assertAll();
	}

	/***
	 * qTest : TC-564 User has previously added items to the cart
	 * Description : Description : This test validates that the product added by a anonymous user
	 *  should be present in the cart along with the items added by the valid user if it login in the same session.  
	 * 
	 *     
	 */
	@Test(enabled=true)
	public void testUserHasPreviouslyAddedItemsToTheCart_564(){
		String productName = null;
		String productNameToAddByUser = null;
		String productNameOnCheckoutPopup = null;
		String productNameAddedByUser = null;
		sfHomePage.loginToStoreFront(TestConstants.CONSULTANT_EMAIL_WITH_CRP_AND_PULSE, password,true);
		sfCartPage = sfHomePage.clickMiniCartBagLink();
		sfCartPage.removeAllProductsFromCart();
		sfCartPage.clickWelcomeDropdown();
		sfCartPage.logout();
		sfShopSkinCarePage = sfHomePage.clickAllProducts();
		productName = sfShopSkinCarePage.getProductNameFromAllProductPage("2");
		sfShopSkinCarePage.addProductToCart(TestConstants.PRODUCT_NUMBER, TestConstants.ORDER_TYPE_ADHOC);
		productNameOnCheckoutPopup = sfShopSkinCarePage.getProductNameFromCheckoutPopup();
		s_assert.assertTrue(sfShopSkinCarePage.isAddedToYourShoppingCartHeadlinePresentOnCheckoutPopup(),"Added to ypur shopping cart header is not present on checkout popup");
		s_assert.assertTrue(productNameOnCheckoutPopup.contains(productName),"Name of Product added to Bag does not matches with the product name on checkout popup. Expeced : " + productName + ". Actual : " + productNameOnCheckoutPopup);
		sfShopSkinCarePage.checkoutTheCartFromPopUp();
		s_assert.assertTrue(sfCartPage.isYourShoppingCartHeaderPresentOnCartPage(),"Your Shopping cart header is not present when redirected after clicking the checkout button from checkout pop up");
		s_assert.assertTrue(sfCartPage.isProductAddedToCartPresentOnCartPage(productNameOnCheckoutPopup),"Product added to cart : "+productName+" is not present on the cart page");
		sfCartPage.clickOnCartLoginLink();
		sfCartPage.loginToStoreFrontExcludingClickOnLoginIcon(TestConstants.CONSULTANT_EMAIL_WITH_CRP_AND_PULSE, password);
		sfCartPage.clickMiniCartBagLink();
		s_assert.assertTrue(sfCartPage.isProductAddedToCartPresentOnCartPage(productNameOnCheckoutPopup),"Product added to cart : "+productName+" is not present on the cart page");
		sfCartPage.clickRodanAndFieldsLogo();
		sfCartPage.clickAllProducts();
		productNameToAddByUser = sfShopSkinCarePage.getProductNameFromAllProductPage("3");
		sfShopSkinCarePage.addProductToCart("3", TestConstants.ORDER_TYPE_ADHOC);
		productNameAddedByUser = sfShopSkinCarePage.getProductNameFromCheckoutPopup();
		s_assert.assertTrue(sfShopSkinCarePage.isAddedToYourShoppingCartHeadlinePresentOnCheckoutPopup(),"Added to ypur shopping cart header is not present on checkout popup");
		s_assert.assertTrue(productNameAddedByUser.contains(productNameToAddByUser),"Name of Product added to Bag does not matches with the product name on checkout popup. Expeced : " + productNameToAddByUser + ". Actual : " + productNameAddedByUser);
		sfShopSkinCarePage.checkoutTheCartFromPopUp();
		s_assert.assertTrue(sfCartPage.isProductAddedToCartPresentOnCartPage(productNameOnCheckoutPopup),"Product added to cart : "+productName+" as Anonymous user is not present on the cart page");
		s_assert.assertTrue(sfCartPage.isProductAddedToCartPresentOnCartPage(productNameAddedByUser),"Product added to cart : "+productNameToAddByUser+" as valid user is not present on the cart page");
		s_assert.assertAll();
	}


	/***
	 * qTest : TC-372 Mini Cart- Autoship - not visible to consultant not enrolled in CRP
	 * Description : This test validates that mini cart is empty
	 * after placed an order
	 *     
	 */
	@Test(enabled=true)
	public void testAutoshipNotVisibleToConsultantNotEnrolledInCRP_372(){
		sfHomePage.loginToStoreFront(TestConstants.CONSULTANT_EMAIL_WITHOUT_CRP_AND_PULSE,  password,true);
		s_assert.assertFalse(sfHomePage.isAutoshipLinkPresent(), "Autoship link is not present for consultant who is not enrolled in pulse");
		s_assert.assertAll();
	}

	/***
	 * qTest : TC-344 As a PC, Retail/Anon User, Consultant, I will be able to view the mini cart
	 * Description : This test case validates product present in mini cart 
	 */
	@Test(enabled=true)
	public void testAsPCAndRCAbleToViewMiniAutoshipCart_344(){
		sfHomePage.loginToStoreFront(TestConstants.PC_EMAIL_HAVING_AUTOSHIP,  password,true);
		sfCartPage = sfHomePage.clickMiniCartBagLink();
		sfCartPage.removeAllProductsFromCart();
		sfCartPage.clickRodanAndFieldsLogo();
		sfShopSkinCarePage = sfHomePage.clickAllProducts();
		sfShopSkinCarePage.addProductToCart(TestConstants.PRODUCT_NUMBER, TestConstants.ORDER_TYPE_ADHOC);
		sfCartPage = sfShopSkinCarePage.checkoutTheCartFromPopUp();
		String productName = sfCartPage.getProductName("1").toLowerCase();
		sfCartPage.clickRodanAndFieldsLogo();
		sfCartPage.hoverOnMiniCartBagIcon();
		String productNameInMiniCart = sfCartPage.getProductNameFromMiniCart("1").toLowerCase();
		s_assert.assertTrue(productName.contains(productNameInMiniCart), "Expected first product name in mini cart is "+productName+" Actual on UI is "+productNameInMiniCart);
		s_assert.assertAll();
	}

	/***
	 * qTest : TC-565 PC or Consultant user adds products to the cart anonymously
	 * Description : Description : This test validates that the product added by a anonymous user should be present
	 *  and price should get updated in the cart for the same product for the valid user if it login in the same session.  
	 * 
	 *     
	 */
	@Test(enabled=true)
	public void testPCOrConsultantUserAddsProductsToTheCartAnonymously_565(){
		String productName = null;
		String productNameOnCheckoutPopup = null;
		String priceForAnonymousUser = null;
		String specificPriceForValidUser = null;
		String priceOnCartPageForValidUser = null;
		sfHomePage.loginToStoreFront(TestConstants.CONSULTANT_EMAIL_WITH_CRP_AND_PULSE,  password,true);
		sfCartPage = sfHomePage.clickMiniCartBagLink();
		sfCartPage.removeAllProductsFromCart();
		sfCartPage.clickWelcomeDropdown();
		sfCartPage.logout();
		sfShopSkinCarePage = sfHomePage.clickAllProducts();
		productName = sfShopSkinCarePage.getProductNameFromAllProductPage(TestConstants.PRODUCT_NUMBER);
		sfShopSkinCarePage.addProductToCart(TestConstants.PRODUCT_NUMBER, TestConstants.ORDER_TYPE_ADHOC);
		productNameOnCheckoutPopup = sfShopSkinCarePage.getProductNameFromCheckoutPopup();
		s_assert.assertTrue(sfShopSkinCarePage.isAddedToYourShoppingCartHeadlinePresentOnCheckoutPopup(),"Added to ypur shopping cart header is not present on checkout popup");
		s_assert.assertTrue(productNameOnCheckoutPopup.contains(productName),"Name of Product added to Bag does not matches with the product name on checkout popup. Expeced : " + productName + ". Actual : " + productNameOnCheckoutPopup);
		sfShopSkinCarePage.checkoutTheCartFromPopUp();
		priceForAnonymousUser = sfCartPage.getPriceOfProductFromAllItemsInCart(productName);
		s_assert.assertTrue(sfCartPage.isYourShoppingCartHeaderPresentOnCartPage(),"Your Shopping cart header is not present when redirected after clicking the checkout button from checkout pop up");
		s_assert.assertTrue(sfCartPage.isProductAddedToCartPresentOnCartPage(productNameOnCheckoutPopup),"Product added to cart : "+productName+" is not present on the cart page");
		sfCartPage.clickOnCartLoginLink();
		sfCartPage.loginToStoreFrontExcludingClickOnLoginIcon(TestConstants.CONSULTANT_EMAIL_WITH_CRP_AND_PULSE, password);
		sfCartPage.clickAllProducts();
		sfProductDetailPage = sfShopSkinCarePage.clickOnProductNameLink(productName);
		specificPriceForValidUser = sfProductDetailPage.getSpecificPricePresentOnPDPPage();
		sfCartPage.clickMiniCartBagLink();
		priceOnCartPageForValidUser = sfCartPage.getPriceOfProductFromAllItemsInCart(productName);
		s_assert.assertTrue(sfCartPage.isProductAddedToCartPresentOnCartPage(productNameOnCheckoutPopup),"Product added to cart : "+productName+" is not present on the cart page for Consutant");
		s_assert.assertFalse(sfCartPage.isProductPriceUpdatedInCartAsPerUser(priceForAnonymousUser,priceOnCartPageForValidUser),"Price of product on cart page is still the same as of anonymous user when logged in as Consultant");
		s_assert.assertTrue(sfCartPage.isProductPriceUpdatedInCartAsPerUser(specificPriceForValidUser,priceOnCartPageForValidUser),"Specific price do not get updated as per consultant user on Cart page . Expected : " + specificPriceForValidUser + ". Actual : " + priceOnCartPageForValidUser);

		// For PC User
		sfCartPage.clickWelcomeDropdown();
		sfCartPage.logout();
		sfHomePage.loginToStoreFront(TestConstants.PC_EMAIL_HAVING_AUTOSHIP, password,true);
		sfCartPage = sfHomePage.clickMiniCartBagLink();
		sfCartPage.removeAllProductsFromCart();
		sfCartPage.clickWelcomeDropdown();
		sfCartPage.logout();
		sfShopSkinCarePage = sfHomePage.clickAllProducts();
		productName = null;
		productName = sfShopSkinCarePage.getProductNameFromAllProductPage(TestConstants.PRODUCT_NUMBER);
		sfShopSkinCarePage.addProductToCart(TestConstants.PRODUCT_NUMBER, TestConstants.ORDER_TYPE_ADHOC);
		productNameOnCheckoutPopup = null;
		productNameOnCheckoutPopup = sfShopSkinCarePage.getProductNameFromCheckoutPopup();
		s_assert.assertTrue(sfShopSkinCarePage.isAddedToYourShoppingCartHeadlinePresentOnCheckoutPopup(),"Added to ypur shopping cart header is not present on checkout popup");
		s_assert.assertTrue(productNameOnCheckoutPopup.contains(productName),"Name of Product added to Bag does not matches with the product name on checkout popup. Expeced : " + productName + ". Actual : " + productNameOnCheckoutPopup);
		sfShopSkinCarePage.checkoutTheCartFromPopUp();
		priceForAnonymousUser = null;
		priceForAnonymousUser = sfCartPage.getPriceOfProductFromAllItemsInCart(productName);
		s_assert.assertTrue(sfCartPage.isYourShoppingCartHeaderPresentOnCartPage(),"Your Shopping cart header is not present when redirected after clicking the checkout button from checkout pop up");
		s_assert.assertTrue(sfCartPage.isProductAddedToCartPresentOnCartPage(productNameOnCheckoutPopup),"Product added to cart : "+productName+" is not present on the cart page");
		sfCartPage.clickOnCartLoginLink();
		sfCartPage.loginToStoreFrontExcludingClickOnLoginIcon(TestConstants.PC_EMAIL_HAVING_AUTOSHIP, password);
		sfCartPage.clickAllProducts();
		sfProductDetailPage = sfShopSkinCarePage.clickOnProductNameLink(productName);
		specificPriceForValidUser = null;
		specificPriceForValidUser = sfProductDetailPage.getSpecificPricePresentOnPDPPage();
		sfCartPage.clickMiniCartBagLink();
		priceOnCartPageForValidUser = null;
		priceOnCartPageForValidUser = sfCartPage.getPriceOfProductFromAllItemsInCart(productName);
		s_assert.assertTrue(sfCartPage.isProductAddedToCartPresentOnCartPage(productNameOnCheckoutPopup),"Product added to cart : "+productName+" is not present on the cart page for PC User");
		s_assert.assertFalse(sfCartPage.isProductPriceUpdatedInCartAsPerUser(priceForAnonymousUser,priceOnCartPageForValidUser),"Price of product on cart page is still the same as of anonymous user when logged in as PC User");
		s_assert.assertTrue(sfCartPage.isProductPriceUpdatedInCartAsPerUser(specificPriceForValidUser,priceOnCartPageForValidUser),"Specific price do not get updated as per PC user on Cart page . Expected : " + specificPriceForValidUser + ". Actual : " + priceOnCartPageForValidUser);
		s_assert.assertAll();
	}
/***
	 * qTest : TC-147 View Mini Shipping bag
	 * Description : This test validates product name, quantity ,subtotal, no of item
	 * In mini cart    
	 */
	@Test(enabled=true)
	public void testViewMiniShippingBag_147(){
		String firstProductName = null;
		String secondProductName= null;
		int totalNoOfItemsOnCartPage = 0;
		String totalNoOfItemFromMiniCart = null;
		String quantityOfFirstProduct = null;
		String quantityOfSecondProduct = null;
		String subTotalOnCartPage  = null;
		String orderTotalOnCartPage = null;
		String productNameInMiniCart = null;
		String quantityOfProductInMiniCart = null;
		String subTotalInMiniCart = null;
		String totalOfProdutsInMiniCart = null;
		String urlToAssert = "/cart";
		String currentURL = null;

		sfShopSkinCarePage = sfHomePage.clickAllProducts();
		sfShopSkinCarePage.addProductToCart(TestConstants.PRODUCT_NUMBER, TestConstants.ORDER_TYPE_ADHOC);
		sfCartPage = sfShopSkinCarePage.checkoutTheCartFromPopUp();
		totalNoOfItemsOnCartPage = sfCartPage.getTotalNoOfItemsInCart();
		totalNoOfItemFromMiniCart = sfCartPage.getNumberOfItemFromMiniCart();
		s_assert.assertTrue(totalNoOfItemsOnCartPage == Integer.parseInt(totalNoOfItemFromMiniCart), "Expected total no of items in mini shopping bag icon is "+totalNoOfItemsOnCartPage+" Actual on UI is "+totalNoOfItemsOnCartPage);
		sfCartPage.clickAddMoreItemsBtn();
		sfShopSkinCarePage.addProductToCart(String.valueOf(Integer.parseInt(TestConstants.PRODUCT_NUMBER)+1), TestConstants.ORDER_TYPE_ADHOC);
		sfShopSkinCarePage.checkoutTheCartFromPopUp();
		totalNoOfItemsOnCartPage = sfCartPage.getTotalNoOfItemsInCart();
		totalNoOfItemFromMiniCart = sfCartPage.getNumberOfItemFromMiniCart();
		s_assert.assertTrue(totalNoOfItemsOnCartPage == Integer.parseInt(totalNoOfItemFromMiniCart), "Expected total no of items in mini shopping bag icon is after added another product "+totalNoOfItemsOnCartPage+" Actual on UI is "+totalNoOfItemsOnCartPage);
		firstProductName = sfCartPage.getProductName("1").toLowerCase();
		secondProductName = sfCartPage.getProductName("2").toLowerCase();
		quantityOfFirstProduct = sfCartPage.getQuantityOfProductFromCart("1");
		quantityOfSecondProduct = sfCartPage.getQuantityOfProductFromCart("2");
		subTotalOnCartPage = sfCartPage.getSubtotalofItems();
		orderTotalOnCartPage = sfCartPage.getOrderTotal();
		sfCartPage.hoverOnMiniCartBagIcon();
		int totalNoOfItemInMiniCart = sfCartPage.getTotalNumberOfItemsInMiniCart();
		s_assert.assertTrue(totalNoOfItemsOnCartPage == totalNoOfItemInMiniCart, "Expected total no of items in mini cart is "+totalNoOfItemsOnCartPage+" Actual on UI is "+totalNoOfItemInMiniCart);
		productNameInMiniCart = sfCartPage.getProductNameFromMiniCart("1").toLowerCase();
		quantityOfProductInMiniCart = sfCartPage.getQuantityOfProductFromMiniCart("1").split("\\:")[1].trim();
		s_assert.assertTrue(firstProductName.contains(productNameInMiniCart), "Expected first product name in mini cart is "+firstProductName+" Actual on UI is "+productNameInMiniCart);
		s_assert.assertTrue(quantityOfFirstProduct.contains(quantityOfProductInMiniCart), "Expected quantity of first product in mini cart is "+quantityOfFirstProduct+" Actual on UI is "+quantityOfProductInMiniCart);
		productNameInMiniCart = sfCartPage.getProductNameFromMiniCart("2").toLowerCase();
		quantityOfProductInMiniCart = sfCartPage.getQuantityOfProductFromMiniCart("2").split("\\:")[1].trim();
		s_assert.assertTrue(secondProductName.contains(productNameInMiniCart), "Expected Second product name in mini cart is "+secondProductName+" Actual on UI is "+productNameInMiniCart);
		s_assert.assertTrue(quantityOfSecondProduct.contains(quantityOfProductInMiniCart), "Expected quantity of first product in mini cart is "+quantityOfSecondProduct+" Actual on UI is "+quantityOfProductInMiniCart);
		subTotalInMiniCart = sfCartPage.getSubtotalofItemsFromMiniCart();
		totalOfProdutsInMiniCart = sfCartPage.gettotalofItemsInMiniCart();
		s_assert.assertTrue(subTotalOnCartPage.contains(totalOfProdutsInMiniCart), "Expected total in mini cart : "+subTotalOnCartPage+" Actual in mini cart : "+totalOfProdutsInMiniCart);
		s_assert.assertTrue(orderTotalOnCartPage.contains(subTotalInMiniCart), "Expected order total in mini cart : "+orderTotalOnCartPage+" Actual in mini cart : "+subTotalInMiniCart);
		sfCartPage.clickViewShoppingCartLink();
		currentURL = sfCartPage.getCurrentURL();
		s_assert.assertTrue(currentURL.contains(urlToAssert),"Expected URL should contain : " + urlToAssert + " . Actual URL : " + currentURL);
		s_assert.assertTrue(sfCartPage.isYourShoppingCartHeaderPresentOnCartPage(),"Your Shopping cart Header is not present on cart page");
		sfCartPage.clickRodanAndFieldsLogo();
		sfCartPage.clickMiniCartBagLink();
		currentURL = sfCartPage.getCurrentURL();
		s_assert.assertTrue(currentURL.contains(urlToAssert),"Expected URL should contain : " + urlToAssert + " . Actual URL : " + currentURL);
		s_assert.assertTrue(sfCartPage.isYourShoppingCartHeaderPresentOnCartPage(),"Your Shopping cart Header is not present on cart page");
		s_assert.assertAll();
	}
}