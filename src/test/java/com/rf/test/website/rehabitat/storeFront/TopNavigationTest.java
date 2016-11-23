package com.rf.test.website.rehabitat.storeFront;

import org.testng.annotations.Test;

import com.rf.core.utils.CommonUtils;
import com.rf.test.website.rehabitat.storeFront.baseTest.StoreFrontWebsiteBaseTest;

public class TopNavigationTest extends StoreFrontWebsiteBaseTest{

	/***
	 * qTest : TC-72 Become a Consultant- Why R+F
	 * 
	 * Description : This test validates that Enroll now button on Why R+F page is redirecting to 
	 * sponsor search page
	 *     
	 */
	@Test //Incomplete
	public void testBecomeAConsultantAtWhyRFPage_72(){
		sfHomePage.clickOnWhyRF();
		sfHomePage.clickEnrollNowButton();
	}

	/***
	 * qTest : TC-73 Become a Consultant- Events
	 * 
	 * Description : This test validates that Event Calendar button is redirecting to 
	 * new tab which contain calendar
	 *     
	 */
	@Test
	public void testBecomeAConsultantEvents_73(){
		String currentWindowID = null; 
		String currentURL = null;
		sfHomePage.clickOnEvents();
		currentWindowID = CommonUtils.getCurrentWindowHandle();
		sfHomePage.clickFirstEventCalendar();
		sfHomePage.switchToChildWindow(currentWindowID);
		currentURL = sfHomePage.getCurrentURL().toLowerCase();
		s_assert.assertTrue(currentURL.contains("events/calendar"), "Expected URL should contain events/Calendar but actual on UI is"+currentURL);
		sfHomePage.switchToParentWindow(currentWindowID);
		s_assert.assertAll();
	}

	/***
	 * qTest : TC-74 Become a Consultant- Programs and Incentives
	 * 
	 * Description : This test validates that Enroll now button is redirecting to 
	 * sponsor search page through Programs and Incentives Page
	 *     
	 */
	@Test //Incomplete
	public void testBecomeAConsultantProgramsAndIncentives_74(){
		sfHomePage.clickProgramsAndIncentives();
		sfHomePage.clickEnrollNowButton();
	}

	/***
	 * qTest : TC-134 Becoming a Consultant- Meet our Community
	 * Description : This test validates Meet our community link and social icons
	 *  
	 *     
	 */
	@Test
	public void testBecomeAConsultantMeetOurCommunity_74(){
		String currentWindowID = null;
		String currentURL = null;
		String socialIconFacebook = "facebook";
		String socialIconTwitter = "twitter";
		String socialIconInstagram = "instagram";
		String socialIconDerm = "derm";
		String socialIconPinterest = "pinterest";
		String socialIconYoutube = "youtube";

		sfHomePage.clickMeetOurCommunityLink();
		currentWindowID = CommonUtils.getCurrentWindowHandle();
		sfHomePage.clickVisitTheBlock();
		sfHomePage.switchToChildWindow(currentWindowID);
		currentURL = sfHomePage.getCurrentURL().toLowerCase();
		s_assert.assertTrue(currentURL.contains("redefine."), "Expected URL should contain redefine. but actual on UI is"+currentURL);
		sfHomePage.switchToParentWindow(currentWindowID);
		//Verify Social Icon
		//Facebook
		sfHomePage.clickSocialMediaIcon(socialIconFacebook);
		sfHomePage.switchToChildWindow(currentWindowID);
		currentURL = sfHomePage.getCurrentURL().toLowerCase();
		s_assert.assertTrue(currentURL.contains("facebook"), "Expected URL should contain facebook but actual on UI is"+currentURL);
		sfHomePage.switchToParentWindow(currentWindowID);

		//Twitter
		sfHomePage.clickSocialMediaIcon(socialIconTwitter);
		sfHomePage.switchToChildWindow(currentWindowID);
		currentURL = sfHomePage.getCurrentURL().toLowerCase();
		s_assert.assertTrue(currentURL.contains("twitter"), "Expected URL should contain twitter but actual on UI is"+currentURL);
		sfHomePage.switchToParentWindow(currentWindowID);

		//Instagram
		sfHomePage.clickSocialMediaIcon(socialIconInstagram);
		sfHomePage.switchToChildWindow(currentWindowID);
		currentURL = sfHomePage.getCurrentURL().toLowerCase();
		s_assert.assertTrue(currentURL.contains("instagram"), "Expected URL should contain instagram but actual on UI is"+currentURL);
		sfHomePage.switchToParentWindow(currentWindowID);

		//Derm
		sfHomePage.clickSocialMediaIcon(socialIconDerm);
		sfHomePage.switchToChildWindow(currentWindowID);
		currentURL = sfHomePage.getCurrentURL().toLowerCase();
		s_assert.assertTrue(currentURL.contains("derm"), "Expected URL should contain derm but actual on UI is"+currentURL);
		sfHomePage.switchToParentWindow(currentWindowID);

		//Pinterest
		sfHomePage.clickSocialMediaIcon(socialIconPinterest);
		sfHomePage.switchToChildWindow(currentWindowID);
		currentURL = sfHomePage.getCurrentURL().toLowerCase();
		s_assert.assertTrue(currentURL.contains("pinterest"), "Expected URL should contain pinterest but actual on UI is"+currentURL);
		sfHomePage.switchToParentWindow(currentWindowID);

		//Youtube
		sfHomePage.clickSocialMediaIcon(socialIconYoutube);
		sfHomePage.switchToChildWindow(currentWindowID);
		currentURL = sfHomePage.getCurrentURL().toLowerCase();
		s_assert.assertTrue(currentURL.contains("youtube"), "Expected URL should contain youtube but actual on UI is"+currentURL);
		sfHomePage.switchToParentWindow(currentWindowID);
		s_assert.assertAll();
	}

}