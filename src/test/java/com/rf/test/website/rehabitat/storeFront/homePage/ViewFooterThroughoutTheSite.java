package com.rf.test.website.rehabitat.storeFront.homePage;

import org.testng.annotations.Test;

import com.rf.core.utils.CommonUtils;
import com.rf.test.website.rehabitat.storeFront.baseTest.StoreFrontWebsiteBaseTest;

public class ViewFooterThroughoutTheSite extends StoreFrontWebsiteBaseTest{

	/***
	 * qTest : TC-38 Verify footer throughout the site
	 * Description : This test validates footer section through all pages.
	 * 
	 *     
	 */
	@Test(enabled=false)
	public void testVerifyFooterSectionThroughAllPages_38(){
		String currentURL = null;
		String currentWindowID =null;
		String reverseLinkUnderShopSkincare = "REVERSE";
		String allProductLinkUnderShopSkincare = "ALL PRODUCTS";
		String contactUS = "Contact Us";
		String careers = "Careers";
		String dermRF = "Derm RF";//external
		String rfConnection= "RF Connection";//external
		String contactUsURL = "contact";
		String careersURL = "careers";
		String dermRFURL = "dermrf";
		String rfConnectionURL= "rfconnection";
		//Verify footer links on product category page
		sfShopSkinCarePage = sfHomePage.navigateToShopSkincareLink(reverseLinkUnderShopSkincare);
		s_assert.assertTrue(sfShopSkinCarePage.isTheFooterLinkDisplayed(contactUS),"Contact us link is not present in footer section on reverse category page");
		s_assert.assertTrue(sfShopSkinCarePage.isTheFooterLinkDisplayed(careers),"Careers link is not present in footer section on reverse category page");
		s_assert.assertTrue(sfShopSkinCarePage.isTheFooterLinkDisplayed(dermRF),"derm rf link is not present in footer section on reverse category page");
		s_assert.assertTrue(sfShopSkinCarePage.isTheFooterLinkDisplayed(rfConnection),"RF Connection link is not present in footer section on reverse category page");
		//Click and verify Internal and external links.
		//Internal.
		sfShopSkinCarePage.clickFooterLink(contactUS);
		currentURL = sfShopSkinCarePage.getCurrentURL();
		s_assert.assertTrue(currentURL.contains(contactUsURL), "Expected URL should contain "+contactUsURL+" on product category page but actual on UI is"+currentURL);
		//External link
		currentWindowID = CommonUtils.getCurrentWindowHandle();
		sfShopSkinCarePage.clickFooterLink(dermRF);
		sfHomePage.switchToChildWindow(currentWindowID);
		currentURL = sfShopSkinCarePage.getCurrentURL();
		s_assert.assertTrue(currentURL.contains(dermRFURL), "Expected URL should contain "+dermRFURL+" on product category page but actual on UI is"+currentURL);	
		sfHomePage.switchToParentWindow(currentWindowID);
		//Verify footer links on all products page
		sfShopSkinCarePage = sfHomePage.navigateToShopSkincareLink(allProductLinkUnderShopSkincare);
		s_assert.assertTrue(sfShopSkinCarePage.isTheFooterLinkDisplayed(contactUS),"Contact Us link is not present in footer section on all products page");
		s_assert.assertTrue(sfShopSkinCarePage.isTheFooterLinkDisplayed(careers),"Careers link is not present in footer section on reverse category page");
		s_assert.assertTrue(sfShopSkinCarePage.isTheFooterLinkDisplayed(dermRF),"derm rf link is not present in footer section on reverse category page");
		s_assert.assertTrue(sfShopSkinCarePage.isTheFooterLinkDisplayed(rfConnection),"RF Connection link is not present in footer section on reverse category page");
		//verify footer links on product detail page.
		sfProductDetailPage = sfShopSkinCarePage.clickNameOfFirstProduct();
		s_assert.assertTrue(sfProductDetailPage.isTheFooterLinkDisplayed(contactUS),"Contact Us link is not present in footer section on product detail page");
		s_assert.assertTrue(sfProductDetailPage.isTheFooterLinkDisplayed(careers),"Careers link is not present in footer section on reverse category page");
		s_assert.assertTrue(sfProductDetailPage.isTheFooterLinkDisplayed(dermRF),"derm rf link is not present in footer section on reverse category page");
		s_assert.assertTrue(sfProductDetailPage.isTheFooterLinkDisplayed(rfConnection),"RF Connection link is not present in footer section on reverse category page");
		//Verify footer links on cart page.
		sfProductDetailPage.addProductToCartFromProductDetailPage();
		sfCartPage = sfProductDetailPage.checkoutTheCartFromPopUp();
		s_assert.assertTrue(sfCartPage.isTheFooterLinkDisplayed(contactUS),"Contact Us link is not present in footer section on cart page");
		s_assert.assertTrue(sfCartPage.isTheFooterLinkDisplayed(careers),"Careers link is not present in footer section on reverse category page");
		s_assert.assertTrue(sfCartPage.isTheFooterLinkDisplayed(dermRF),"derm rf link is not present in footer section on reverse category page");
		s_assert.assertTrue(sfCartPage.isTheFooterLinkDisplayed(rfConnection),"RF Connection link is not present in footer section on reverse category page");
		//Verify footer links on checkout page.
		sfCheckoutPage = sfCartPage.clickCheckoutBtn();
		s_assert.assertTrue(sfCheckoutPage.isTheFooterLinkDisplayed(contactUS),"Contact Us link is not present in footer section on checkout page");
		s_assert.assertTrue(sfCheckoutPage.isTheFooterLinkDisplayed(careers),"Careers link is not present in footer section on reverse category page");
		s_assert.assertTrue(sfCheckoutPage.isTheFooterLinkDisplayed(dermRF),"derm rf link is not present in footer section on reverse category page");
		s_assert.assertTrue(sfCheckoutPage.isTheFooterLinkDisplayed(rfConnection),"RF Connection link is not present in footer section on reverse category page");
		//Click and verify Internal and external links on checkout page.
		//Internal.
		sfCheckoutPage.clickFooterLink(careers);
		currentURL = sfCheckoutPage.getCurrentURL();
		s_assert.assertTrue(currentURL.contains(careersURL), "Expected URL should contain "+careersURL+" on checkout page but actual on UI is"+currentURL);
		//External link
		currentWindowID = CommonUtils.getCurrentWindowHandle();
		sfCheckoutPage.clickFooterLink(rfConnection);
		sfHomePage.switchToChildWindow(currentWindowID);
		currentURL = sfCheckoutPage.getCurrentURL();
		s_assert.assertTrue(currentURL.contains(rfConnectionURL), "Expected URL should contain "+rfConnectionURL+" on checkout page but actual on UI is"+currentURL);	
		sfHomePage.switchToParentWindow(currentWindowID);
		s_assert.assertAll();
	}
}
