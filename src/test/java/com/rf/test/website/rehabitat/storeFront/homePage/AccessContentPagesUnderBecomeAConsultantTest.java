package com.rf.test.website.rehabitat.storeFront.homePage;

import org.testng.annotations.Test;

import com.rf.core.website.constants.TestConstants;
import com.rf.test.website.rehabitat.storeFront.baseTest.StoreFrontWebsiteBaseTest;

public class AccessContentPagesUnderBecomeAConsultantTest extends StoreFrontWebsiteBaseTest{

	/***
	 * qTest : TC-12 Access specific content pages under "Become a consultant" section
	 * Description : This test validates subling and their specific content page 
	 * under Become a consultant section
	 *     
	 */
	@Test
	public void testAccessSpecificContentPagesUnderBecomeAConsultantSection_12(){
		String meetOurCommunityLink = "MEET OUR COMMUNITY";
		String whyRFLink = "WHY R+F";
		String events = "EVENTS";
		String programsAndIncentivesLink = "PROGRAMS & INCENTIVES";
		String enrollNowLink = "ENROLL NOW";
		String becomeAConsultantLink = TestConstants.BECOME_A_CONSULTANT;
		String currentURL = null;
		s_assert.assertTrue(sfHomePage.isTopNavigationSublinkDisplayed(becomeAConsultantLink, meetOurCommunityLink), meetOurCommunityLink+" link is not present under become a consultant section");
		s_assert.assertTrue(sfHomePage.isTopNavigationSublinkDisplayed(becomeAConsultantLink,whyRFLink), whyRFLink+" link is not present under become a consultant section");
		s_assert.assertTrue(sfHomePage.isTopNavigationSublinkDisplayed(becomeAConsultantLink,events), events+" link is not present under become a consultant section");
		s_assert.assertTrue(sfHomePage.isTopNavigationSublinkDisplayed(becomeAConsultantLink,programsAndIncentivesLink), programsAndIncentivesLink+" link is not present under become a consultant section");
		s_assert.assertTrue(sfHomePage.isTopNavigationSublinkDisplayed(becomeAConsultantLink,enrollNowLink), enrollNowLink+" link is not present under become a consultant section");
		//Verify links are redirecting to specific content page
		meetOurCommunityLink = "meet-our-community";
		whyRFLink = "why-rf";
		events = "events";
		programsAndIncentivesLink = "programs-incentives";
		enrollNowLink = "consultant-registration";
		//Meet our community link
		sfHomePage.clickMeetOurCommunityLink();
		currentURL = sfHomePage.getCurrentURL().toLowerCase();
		s_assert.assertTrue(currentURL.contains(meetOurCommunityLink), "Expected URL should contain "+meetOurCommunityLink+" but actual on UI is"+currentURL);
		sfHomePage.clickRodanAndFieldsLogo();
		//Why R+F link
		sfHomePage.clickWhyRF();
		currentURL = sfHomePage.getCurrentURL().toLowerCase();
		s_assert.assertTrue(currentURL.contains(whyRFLink), "Expected URL should contain "+whyRFLink+" but actual on UI is"+currentURL);
		sfHomePage.clickRodanAndFieldsLogo();
		//Events link
		sfHomePage.clickEvents();
		currentURL = sfHomePage.getCurrentURL().toLowerCase();
		s_assert.assertTrue(currentURL.contains(events), "Expected URL should contain "+events+" but actual on UI is"+currentURL);
		sfHomePage.clickRodanAndFieldsLogo();
		//programs-incentives link
		sfHomePage.clickProgramsAndIncentives();
		currentURL = sfHomePage.getCurrentURL().toLowerCase();
		s_assert.assertTrue(currentURL.contains(programsAndIncentivesLink), "Expected URL should contain "+programsAndIncentivesLink+" but actual on UI is"+currentURL);
		sfHomePage.clickRodanAndFieldsLogo();
		//Enroll now link
		sfHomePage.clickEnrollNow();
		currentURL = sfHomePage.getCurrentURL().toLowerCase();
		s_assert.assertTrue(currentURL.contains(enrollNowLink), "Expected URL should contain "+enrollNowLink+" but actual on UI is"+currentURL);
		s_assert.assertAll();
	}
}