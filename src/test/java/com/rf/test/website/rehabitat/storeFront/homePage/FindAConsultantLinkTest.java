package com.rf.test.website.rehabitat.storeFront.homePage;

import org.testng.annotations.Test;

import com.rf.test.website.rehabitat.storeFront.baseTest.StoreFrontWebsiteBaseTest;

public class FindAConsultantLinkTest extends StoreFrontWebsiteBaseTest{
	
	/***
	 * qTest : TC-7 Corporate Sites Should Have "Find A Consultant" Link On The Home Page
	 * Description : This test validates that the 'Find a Consultant' link navigates to the
	 * Find Consultant page
	 * 				
	 */
	@Test //Incomplete
	public void testCorporateSitesShouldHaveFindAConsultantLinkOnTheHomePage_7(){
		sfHomePage.clickFindAConsultantLinkOnHomePage();
		s_assert.assertTrue(sfHomePage.isFindAConsultantPagePresent(),"'Find A Consultant' page either doesn't have the URL as 'find-consultant' or sponsor search field is not present");
		s_assert.assertAll();
	}

}