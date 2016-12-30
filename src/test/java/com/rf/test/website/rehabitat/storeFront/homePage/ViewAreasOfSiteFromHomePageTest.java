package com.rf.test.website.rehabitat.storeFront.homePage;

import org.testng.annotations.Test;

import com.rf.core.website.constants.TestConstants;
import com.rf.test.website.rehabitat.storeFront.baseTest.StoreFrontWebsiteBaseTest;

public class ViewAreasOfSiteFromHomePageTest extends StoreFrontWebsiteBaseTest{

	/***
	 * qTest : TC-4 Home Page Navigate And View Area Of The Site
	 * Description : This test validates category, Category landing pages, toggle button,
	 * Mini cart, social media icon at footer
	 *     
	 */
	@Test //Incomplete
	public void testHomePageNavigateAndViewAreaOfTheSite_4(){
		String category_Unblemish = "UNBLEMISH";
		String category_Featured = "FEATURED";
		String category_Essentials = "ESSENTIALS";
		String category_Reverse = "REVERSE";
		String category_Enhancement = "ENHANCEMENT";
		String category_Soothe = "SOOTHE";
		String category_Redefine = "REDEFINE";
		String socialIconFacebook = "facebook";
		String socialIconTwitter = "twitter";
		String socialIconInstagram = "instagram";
		String socialIconPinterest = "pinterest";
		String socialIconYoutube = "youtube";
		String socialIconGooglePlus = "gplus";
		String currentURL = null; 
		String topNavigationLink = TestConstants.SHOP_SKINCARE;
		//Verify Category links
		s_assert.assertTrue(sfHomePage.isTopNavigationSublinkDisplayed(topNavigationLink,category_Unblemish), category_Unblemish +"category is not present under shop skincare");
		s_assert.assertTrue(sfHomePage.isTopNavigationSublinkDisplayed(topNavigationLink,category_Featured), category_Featured +"category is not present under shop skincare");
		s_assert.assertTrue(sfHomePage.isTopNavigationSublinkDisplayed(topNavigationLink,category_Essentials), category_Essentials +"category is not present under shop skincare");
		s_assert.assertTrue(sfHomePage.isTopNavigationSublinkDisplayed(topNavigationLink,category_Reverse), category_Reverse +"category is not present under shop skincare");
		s_assert.assertTrue(sfHomePage.isTopNavigationSublinkDisplayed(topNavigationLink,category_Enhancement), category_Enhancement +"category is not present under shop skincare");
		s_assert.assertTrue(sfHomePage.isTopNavigationSublinkDisplayed(topNavigationLink,category_Soothe), category_Soothe +"category is not present under shop skincare");
		s_assert.assertTrue(sfHomePage.isTopNavigationSublinkDisplayed(topNavigationLink,category_Redefine), category_Redefine +"category is not present under shop skincare");
		//Verify Category landing pages
		//Unblemish
		sfHomePage.navigateToShopSkincareLink(category_Unblemish);
		currentURL = sfHomePage.getCurrentURL().toUpperCase();
		s_assert.assertTrue(currentURL.contains(category_Unblemish), "Expected URL should contain "+category_Unblemish+" but actual on UI is"+currentURL);
		sfHomePage.clickRodanAndFieldsLogo();
		//FEATURED
		sfHomePage.navigateToShopSkincareLink(category_Featured);
		currentURL = sfHomePage.getCurrentURL().toUpperCase();
		s_assert.assertTrue(currentURL.contains(category_Featured), "Expected URL should contain "+category_Featured+" but actual on UI is"+currentURL);
		sfHomePage.clickRodanAndFieldsLogo();
		//ESSENTIALS
		sfHomePage.navigateToShopSkincareLink(category_Essentials);
		currentURL = sfHomePage.getCurrentURL().toUpperCase();
		s_assert.assertTrue(currentURL.contains(category_Essentials), "Expected URL should contain "+category_Essentials+" but actual on UI is"+currentURL);
		sfHomePage.clickRodanAndFieldsLogo();
		//REVERSE
		sfHomePage.navigateToShopSkincareLink(category_Reverse);
		currentURL = sfHomePage.getCurrentURL().toUpperCase();
		s_assert.assertTrue(currentURL.contains(category_Reverse), "Expected URL should contain "+category_Reverse+" but actual on UI is"+currentURL);
		sfHomePage.clickRodanAndFieldsLogo();
		//ENHANCEMENT
		sfHomePage.navigateToShopSkincareLink(category_Enhancement);
		currentURL = sfHomePage.getCurrentURL().toUpperCase();
		s_assert.assertTrue(currentURL.contains(category_Enhancement), "Expected URL should contain "+category_Enhancement+" but actual on UI is"+currentURL);
		sfHomePage.clickRodanAndFieldsLogo();
		//SOOTHE
		sfHomePage.navigateToShopSkincareLink(category_Soothe);
		currentURL = sfHomePage.getCurrentURL().toUpperCase();
		s_assert.assertTrue(currentURL.contains(category_Soothe), "Expected URL should contain "+category_Soothe+" but actual on UI is"+currentURL);
		sfHomePage.clickRodanAndFieldsLogo();
		//REDEFINE
		sfHomePage.navigateToShopSkincareLink(category_Redefine);
		currentURL = sfHomePage.getCurrentURL().toUpperCase();
		s_assert.assertTrue(currentURL.contains(category_Redefine), "Expected URL should contain "+category_Redefine+" but actual on UI is"+currentURL);
		sfHomePage.clickRodanAndFieldsLogo();
		s_assert.assertTrue(sfHomePage.isMiniCartPresent(), "Mini cart icon is not present at homepage");
		s_assert.assertTrue(sfHomePage.isToggleButtonPresent(), "Country toggle button is not present at homepage");
		//Verify social media icons
		s_assert.assertTrue(sfHomePage.isSocialMediaIconPresentAtFooter(socialIconFacebook), socialIconFacebook+" icon is not present at homepage");
		s_assert.assertTrue(sfHomePage.isSocialMediaIconPresentAtFooter(socialIconTwitter), socialIconTwitter+" icon is not present at homepage");
		s_assert.assertTrue(sfHomePage.isSocialMediaIconPresentAtFooter(socialIconInstagram), socialIconInstagram+" icon is not present at homepage");
		s_assert.assertTrue(sfHomePage.isSocialMediaIconPresentAtFooter(socialIconPinterest), socialIconPinterest+" icon is not present at homepage");
		s_assert.assertTrue(sfHomePage.isSocialMediaIconPresentAtFooter(socialIconYoutube), socialIconYoutube+" icon is not present at homepage");
		s_assert.assertTrue(sfHomePage.isSocialMediaIconPresentAtFooter(socialIconGooglePlus), socialIconGooglePlus+" icon is not present at homepage");
		s_assert.assertAll();
	}

	/***
	 * qTest : TC-5 Consultant only products shouldn't be searched by anonymous user, Retail or PC user
	 * Description : This test validates consultant only products shouldn't be searched by 
	 * anonymous user, Retail or PC user
	 * 
	 * click on login link
	 *     
	 */
	@Test //Incomplete 
	public void testSearchConsultantOnlyProductByAnonymousRCPCUser_5(){
		sfHomePage.mouseHoverOn(TestConstants.SHOP_SKINCARE);
		s_assert.assertFalse(sfHomePage.isConsultantOnlyProductsLinkDisplayed(), "Consultant Only Link should NOT be present for anonymous user");
		sfHomePage.loginToStoreFront(TestConstants.PC_EMAIL, password);
		sfHomePage.mouseHoverOn(TestConstants.SHOP_SKINCARE);
		s_assert.assertFalse(sfHomePage.isConsultantOnlyProductsLinkDisplayed(), "Consultant Only Link should NOT be present for PC user");
		sfHomePage.clickWelcomeDropdown();
		sfHomePage.logout();
		sfHomePage.loginToStoreFront(TestConstants.RC_EMAIL, password);
		sfHomePage.mouseHoverOn(TestConstants.SHOP_SKINCARE);
		s_assert.assertFalse(sfHomePage.isConsultantOnlyProductsLinkDisplayed(), "Consultant Only Link should NOT be present for RC user");
		s_assert.assertAll();
	}

}