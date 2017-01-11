package com.rf.test.website.rehabitat.storeFront.homePage;

import org.testng.annotations.Test;

import com.rf.test.website.rehabitat.storeFront.baseTest.StoreFrontWebsiteBaseTest;

public class MeetTheDoctorsTest extends StoreFrontWebsiteBaseTest{

	/***
	 * qTest : TC-18 Place hover the cursor on "About R+F"  and  click on the "Meet the Doctors" link
	 * Description : This test validates Meets the doctors details page.
	 *  
	 *     
	 */
	@Test(enabled=true)
	public void testHoverAboutRFAndClickMeetTheDoctors_18(){
		sfHomePage.clickMeetTheDoctorsLink();
		s_assert.assertTrue(sfHomePage.isMeetTheDoctorsPagePresent(),"'Meet the Doctors' page either doesn't have the URL as 'meet-the-doctors' or meet the doctor Text is not present on page");
		s_assert.assertAll();
	}

	/***
	 * qTest : TC-19 Verify "Meet the executives " link on "Meet the Doctors" Page
	 * Description : This test validates "Meets the executives" details page from "Meet the doctors" page.
	 *  
	 *     
	 */
	@Test(enabled=true)
	public void testVerifyMeetTheExecutivePageFromMeetTheDoctorsPage_19(){
		String currentURL = null;
		String urlToAssert = "executive-team";
		sfHomePage.clickMeetTheDoctorsLink();
		s_assert.assertTrue(sfHomePage.isMeetTheDoctorsPagePresent(),"'Meet the Doctors' page either doesn't have the URL as 'meet-the-doctors' or meet the doctor Text is not present on page");
		//Click meet RF executive team link on meet the doctors page.
		sfHomePage.clickMeetRFExecutiveTeamLinkOnMeetTheDoctorPage();
		currentURL = sfHomePage.getCurrentURL().toLowerCase();
		s_assert.assertTrue(currentURL.contains(urlToAssert), "Expected URL should contain executive-team. but actual on UI is"+currentURL);
		s_assert.assertAll();
	}

}