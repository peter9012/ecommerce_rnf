package com.rf.test.website.rehabitat.storeFront.homePage;

import org.testng.annotations.Test;

import com.rf.test.website.rehabitat.storeFront.baseTest.StoreFrontWebsiteBaseTest;

public class ViewSearchBoxInHeaderNavigationTest extends StoreFrontWebsiteBaseTest{

	/***
	 * qTest : TC-35 View search text box
	 * Description : This test validates search text box functionality in header navigation.
	 * 
	 *     
	 */
	@Test
	public void testViewSearchTextBoxInHeaderNavigation_35(){
		sfHomePage.clickSearchIcon();
		s_assert.assertTrue(sfHomePage.isSearchBoxPresent(),"'Search box' is not present after clicking search icon in header navigation");
		s_assert.assertAll();
	}

	/***
	 * qTest : TC-36 Close the search text box
	 * Description : This test validates search text box functionality in header navigation and close.
	 * 
	 *     
	 */
	@Test
	public void testViewSearchTextBoxInHeaderNavigationAndClose_36(){
		sfHomePage.clickSearchIcon();
		s_assert.assertTrue(sfHomePage.isSearchBoxPresent(),"'Search box' is not present after clicking search icon in header navigation");
		sfHomePage.clickCloseIconOfSearchTextBoxPopup();
		s_assert.assertFalse(sfHomePage.isSearchBoxPresent(),"'Search box' is present after clicking close icon");
		s_assert.assertAll();
	}
}