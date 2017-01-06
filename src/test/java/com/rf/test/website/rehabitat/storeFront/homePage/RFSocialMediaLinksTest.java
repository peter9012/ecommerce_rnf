package com.rf.test.website.rehabitat.storeFront.homePage;

import org.testng.annotations.Test;

import com.rf.core.utils.CommonUtils;
import com.rf.test.website.rehabitat.storeFront.baseTest.StoreFrontWebsiteBaseTest;

public class RFSocialMediaLinksTest  extends StoreFrontWebsiteBaseTest{

	/***
	 * qTest : TC-88 Verify Social Media Links on the home page
	 * 
	 * Description : This test validates social icons on home page. 
	 */
	@Test
	public void testVerifySocialIconsOnHomePage_88(){
		String currentWindowID = null;
		String currentURL = null;
		String socialIconFacebook = "facebook";
		String socialIconTwitter = "twitter";
		String socialIconInstagram = "instagram";
		String socialIconYoutube = "youtube";
		//String socialIconDerm = "derm";
		String socialIconPinterest = "pinterest";
		String socialIconGooglePlus = "google";
		
		//Verify Social Icon on Home Page
		//Facebook
		currentWindowID = CommonUtils.getCurrentWindowHandle();
		sfHomePage.clickSocialMediaIcon(socialIconFacebook);
		sfHomePage.switchToChildWindow(currentWindowID);
		currentURL = sfHomePage.getCurrentURL().toLowerCase();
		s_assert.assertTrue(currentURL.contains(socialIconFacebook), "Expected URL should contain "+socialIconFacebook+" but actual on UI is"+currentURL);
		sfHomePage.switchToParentWindow(currentWindowID);

		//Twitter
		sfHomePage.clickSocialMediaIcon(socialIconTwitter);
		sfHomePage.switchToChildWindow(currentWindowID);
		currentURL = sfHomePage.getCurrentURL().toLowerCase();
		s_assert.assertTrue(currentURL.contains(socialIconTwitter), "Expected URL should contain "+socialIconTwitter+" but actual on UI is"+currentURL);
		sfHomePage.switchToParentWindow(currentWindowID);

		//Instagram
		sfHomePage.clickSocialMediaIcon(socialIconInstagram);
		sfHomePage.switchToChildWindow(currentWindowID);
		currentURL = sfHomePage.getCurrentURL().toLowerCase();
		s_assert.assertTrue(currentURL.contains(socialIconInstagram), "Expected URL should contain "+socialIconInstagram+" but actual on UI is"+currentURL);
		sfHomePage.switchToParentWindow(currentWindowID);

		//Youtube
		sfHomePage.clickSocialMediaIcon(socialIconYoutube);
		sfHomePage.switchToChildWindow(currentWindowID);
		currentURL = sfHomePage.getCurrentURL().toLowerCase();
		s_assert.assertTrue(currentURL.contains(socialIconYoutube), "Expected URL should contain "+socialIconYoutube+" but actual on UI is"+currentURL);
		sfHomePage.switchToParentWindow(currentWindowID);

		//Pinterest
		sfHomePage.clickSocialMediaIcon(socialIconPinterest);
		sfHomePage.switchToChildWindow(currentWindowID);
		currentURL = sfHomePage.getCurrentURL().toLowerCase();
		s_assert.assertTrue(currentURL.contains(socialIconPinterest), "Expected URL should contain "+socialIconPinterest+" but actual on UI is"+currentURL);
		sfHomePage.switchToParentWindow(currentWindowID);

		//Google+
		sfHomePage.clickSocialMediaIcon(socialIconGooglePlus);
		sfHomePage.switchToChildWindow(currentWindowID);
		currentURL = sfHomePage.getCurrentURL().toLowerCase();
		s_assert.assertTrue(currentURL.contains(socialIconGooglePlus), "Expected URL should contain "+socialIconGooglePlus+" but actual on UI is"+currentURL);
		sfHomePage.switchToParentWindow(currentWindowID);
		s_assert.assertAll();
	}
}
