package com.rf.test.website.rehabitat.storeFront.homePage;

import org.testng.annotations.Test;

import com.rf.core.website.constants.TestConstants;
import com.rf.test.website.rehabitat.storeFront.baseTest.StoreFrontWebsiteBaseTest;

public class AccessContentPagesUnderAboutRFSectionTest extends StoreFrontWebsiteBaseTest{

	/***
	 * qTest : TC-37 Access all links in "About R+F" section
	 * Description : This test validates subling and their specific content page 
	 * under About R+F section
	 *     
	 */
	@Test
	public void testAccessAllLinksInAboutRFSection_37(){
		String whoWeAreLink = "WHO WE ARE";
		String executiveTeamLink = "EXECUTIVE TEAM";
		String meetTheDoctorsLink = "MEET THE DOCTORS";
		String givingBackLink = "GIVING BACK";
		String aboutRFLink = TestConstants.ABOUT_RF;
		String currentURL = null;
		s_assert.assertTrue(sfHomePage.isTopNavigationSublinkDisplayed(aboutRFLink,whoWeAreLink), whoWeAreLink+" link is not present under About RF section");
		s_assert.assertTrue(sfHomePage.isTopNavigationSublinkDisplayed(aboutRFLink,executiveTeamLink), executiveTeamLink+" link is not present under About RF section");
		s_assert.assertTrue(sfHomePage.isTopNavigationSublinkDisplayed(aboutRFLink,meetTheDoctorsLink), meetTheDoctorsLink+" link is not present under About RF section");
		s_assert.assertTrue(sfHomePage.isTopNavigationSublinkDisplayed(aboutRFLink,givingBackLink), givingBackLink+" link is not present under About RF section");
		whoWeAreLink = "who-we-are";
		executiveTeamLink = "executive-team";
		meetTheDoctorsLink = "meet-the-doctors";
		givingBackLink = "giving-back";
		//Who we are link
		sfHomePage.clickWhoWeAreLink();
		currentURL = sfHomePage.getCurrentURL().toLowerCase();
		s_assert.assertTrue(currentURL.contains(whoWeAreLink), "Expected URL should contain "+whoWeAreLink+" but actual on UI is"+currentURL);
		sfHomePage.clickRodanAndFieldsLogo();
		//Executive team link
		sfHomePage.clickExecutiveTeam();
		currentURL = sfHomePage.getCurrentURL().toLowerCase();
		s_assert.assertTrue(currentURL.contains(executiveTeamLink), "Expected URL should contain "+executiveTeamLink+" but actual on UI is"+currentURL);
		sfHomePage.clickRodanAndFieldsLogo();
		//Meet the doctors link
		sfHomePage.clickMeetTheDoctorsLink();
		currentURL = sfHomePage.getCurrentURL().toLowerCase();
		s_assert.assertTrue(currentURL.contains(meetTheDoctorsLink), "Expected URL should contain "+meetTheDoctorsLink+" but actual on UI is"+currentURL);
		sfHomePage.clickRodanAndFieldsLogo();
		//Meet the doctors link
		sfHomePage.clickGivingBackLink();
		currentURL = sfHomePage.getCurrentURL().toLowerCase();
		s_assert.assertTrue(currentURL.contains(givingBackLink), "Expected URL should contain "+givingBackLink+" but actual on UI is"+currentURL);
		s_assert.assertAll();
	}
}