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

}