package com.rf.test.website.rehabitat.storeFront.homePage;

import org.testng.annotations.Test;

import com.rf.core.website.constants.TestConstants;
import com.rf.test.website.rehabitat.storeFront.baseTest.StoreFrontWebsiteBaseTest;

public class FindAConsultantLinkTest extends StoreFrontWebsiteBaseTest{

	/***
	 * qTest : TC-7 Corporate Sites Should Have "Find A Consultant" Link On The Home Page
	 * Description : This test validates that the 'Find a Consultant' link navigates to the
	 * Find Consultant page
	 * 				
	 */
	@Test(enabled=true)
	public void testCorporateSitesShouldHaveFindAConsultantLinkOnTheHomePage_7(){
		sfHomePage.clickFindAConsultantLinkOnHomePage();
		s_assert.assertTrue(sfHomePage.isFindAConsultantPagePresent(),"'Find A Consultant' page either doesn't have the URL as 'find-consultant' or sponsor search field is not present for Consultant");
		sfHomePage.loginToStoreFront(pcUserWithoutSponsor(),  password,true);
		sfHomePage.clickFindAConsultantLinkOnHomePage();
		s_assert.assertTrue(sfHomePage.isFindAConsultantPagePresent(),"'Find A Consultant' page either doesn't have the URL as 'find-consultant' or sponsor search field is not present for PC");
		sfHomePage.clickWelcomeDropdown();
		sfHomePage.logout();
		sfHomePage.loginToStoreFront(rcWithOrderWithoutSponsor(),  password,true);
		sfHomePage.clickFindAConsultantLinkOnHomePage();
		s_assert.assertTrue(sfHomePage.isFindAConsultantPagePresent(),"'Find A Consultant' page either doesn't have the URL as 'find-consultant' or sponsor search field is not present for RC");
		s_assert.assertAll();
	}
	
	/***
	 * qTest : TC-8 BIZ and Com sites shouldn’t have "Find a consultant" link on the header of the home page
	 * Description : This test validates that the 'Find a Consultant' link not present
	 * on homepage of biz and com site.
	 * 				
	 */
	@Test(enabled=true)
	public void testPWSSitesShouldNotHaveFindAConsultantLinkOnTheHomePage_8(){
		String prefix = pwsPrefix();
		sfHomePage.navigateToUrl(sfHomePage.getBaseUrl()+"/" +country.toUpperCase()+"/pws/" + prefix);
		s_assert.assertFalse(sfHomePage.isFindAConsultantLinkOnHomePagePresent(),"'Find a consultant' link is present on home page of PWS site");
		s_assert.assertAll();
	}

}