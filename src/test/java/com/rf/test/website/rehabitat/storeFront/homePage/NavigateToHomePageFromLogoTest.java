package com.rf.test.website.rehabitat.storeFront.homePage;

import org.testng.annotations.Test;
import com.rf.test.website.rehabitat.storeFront.baseTest.StoreFrontWebsiteBaseTest;

public class NavigateToHomePageFromLogoTest extends StoreFrontWebsiteBaseTest{

	/***
	 * qTest : TC-6 RF Logo Redirects to Home page
	 * Description : This test validates that the R+F logo navigates to the home page from
	 * shopskinCare page,product detail page and cart page.
	 * 				
	 */
	@Test
	public void testRFLogoRedirectsToHomePage_6(){
		String currentURL = null;
		sfShopSkinCarePage = sfHomePage.clickAllProducts();
		currentURL = sfShopSkinCarePage.getCurrentURL().toLowerCase();
		s_assert.assertTrue(currentURL.contains("shopskincare"), "Expected URL should contain shopskincare but actual on UI is"+currentURL);
		sfShopSkinCarePage.clickRodanAndFieldsLogo();
		currentURL = sfHomePage.getCurrentURL().toLowerCase();
		s_assert.assertTrue(sfHomePage.getCurrentURL().contains("https://www.dev1.rodanandfields.com/AU/"), "Expected URL should be like https://www.dev1.rodanandfields.com/AU/ but actual on UI is"+currentURL);
		sfShopSkinCarePage = sfHomePage.clickAllProducts();
		sfShopSkinCarePage.clickNameOfFirstProduct();
		s_assert.assertTrue(sfShopSkinCarePage.isAddToCartButtonIsPresentAtProductDetailsPage(),"User is not redirecting to product details page after clicking on product name");
		sfShopSkinCarePage.clickRodanAndFieldsLogo();
		currentURL = sfHomePage.getCurrentURL().toLowerCase();
		s_assert.assertTrue(sfHomePage.getCurrentURL().contains("https://www.dev1.rodanandfields.com/AU/"), "Expected URL should be like https://www.dev1.rodanandfields.com/AU/ but actual on UI is"+currentURL);
		sfShopSkinCarePage = sfHomePage.clickAllProducts();
		sfShopSkinCarePage.addFirstProductToBag();
		sfCartPage = sfShopSkinCarePage.checkoutTheCartFromPopUp();
		sfCartPage.clickRodanAndFieldsLogo();
		currentURL = sfHomePage.getCurrentURL().toLowerCase();
		s_assert.assertTrue(sfHomePage.getCurrentURL().contains("https://www.dev1.rodanandfields.com/AU/"), "Expected URL should be like https://www.dev1.rodanandfields.com/AU/ but actual on UI is"+currentURL);
		s_assert.assertAll();
	}


}