package com.rf.test.website.rehabitat.storeFront.homePage;

import org.testng.annotations.Test;

import com.rf.test.website.rehabitat.storeFront.baseTest.StoreFrontWebsiteBaseTest;

public class NavigateToHomePageFromLogoTest extends StoreFrontWebsiteBaseTest{

	@Test(enabled=true)
	public void testRFLogoRedirectsToHomePage_6(){
		String currentURL = null;
		sfShopSkinCarePage = sfHomePage.clickAllProducts();
		currentURL = sfShopSkinCarePage.getCurrentURL().toLowerCase();
		s_assert.assertTrue(currentURL.contains("shopskincare"), "Expected URL should contain shopskincare but actual on UI is"+currentURL);
		sfShopSkinCarePage.clickRodanAndFieldsLogo();
		currentURL = sfHomePage.getCurrentURL().toLowerCase();
		s_assert.assertTrue(sfHomePage.getCurrentURL().contains("/"+country.toUpperCase())&& sfHomePage.isHomePageBannerDisplayed(), "Not redirected to US home page");
		//		sfShopSkinCarePage = sfHomePage.clickAllProducts();
		//		sfProductDetailPage= sfShopSkinCarePage.clickNameOfFirstProduct();
		//		s_assert.assertTrue(sfProductDetailPage.isAddToCartButtonIsPresentAtProductDetailsPage(),"User is not redirecting to product details page after clicking on product name");
		//		sfShopSkinCarePage.clickRodanAndFieldsLogo();
		//		currentURL = sfHomePage.getCurrentURL().toLowerCase();
		//		s_assert.assertTrue(sfHomePage.getCurrentURL().contains("https://www.dev1.rodanandfields.com/AU/"), "Expected URL should be like https://www.dev1.rodanandfields.com/AU/ but actual on UI is"+currentURL);
		//		sfShopSkinCarePage = sfHomePage.clickAllProducts();
		//		sfShopSkinCarePage.selectFirstProduct();
		//		sfCartPage = sfShopSkinCarePage.checkoutTheCartFromPopUp();
		//		sfCartPage.clickRodanAndFieldsLogo();
		//		currentURL = sfHomePage.getCurrentURL().toLowerCase();
		//		s_assert.assertTrue(sfHomePage.getCurrentURL().contains("https://www.dev1.rodanandfields.com/AU/"), "Expected URL should be like https://www.dev1.rodanandfields.com/AU/ but actual on UI is"+currentURL);
		s_assert.assertAll();
	}
}