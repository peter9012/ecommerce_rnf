package com.rf.test.website.rehabitat.storeFront.homePage;

import org.testng.annotations.Test;

import com.rf.core.website.constants.TestConstants;
import com.rf.test.website.rehabitat.storeFront.baseTest.StoreFrontWebsiteBaseTest;

public class AccessLinkToCreateNewRFAccountTest extends StoreFrontWebsiteBaseTest{

	/***
	 * qTest : TC-15 Access a link to create a new R+F account from corp site
	 * Description : This test validates sign up now link from corp site
	 *     
	 */
	@Test(enabled=true)
	public void testAccessALinkToCreateANewRFAccountFromCorpSite_15(){
		sfHomePage.clickLoginIcon();
		sfHomePage.clickSignUpNowLink();
		s_assert.assertTrue(sfHomePage.isLoginOrRegisterPageDisplayed()&& sfHomePage.getCurrentURL().contains("/login"), "'Login Or Register' page has not displayed");
		s_assert.assertAll();
	}

	/***
	 * qTest : TC-16 Access a link to create a new R+F account from .com site
	 * Description : This test validates sign up now link from .com site
	 *     
	 */
	@Test(enabled=true)
	public void testAccessALinkToCreateANewRFAccountFromComSite_16(){
		String prefix = pwsPrefix();
		sfHomePage.navigateToUrl(sfHomePage.getBaseUrl()+"/" +sfHomePage.getCountry() +"/pws/" + prefix);
		sfHomePage.clickLoginIcon();
		sfHomePage.clickSignUpNowLink();
		s_assert.assertTrue(sfHomePage.isLoginOrRegisterPageDisplayed()&& sfHomePage.getCurrentURL().contains("/login"), "'Login Or Register' page has not displayed");
		s_assert.assertAll();
	}

	/***
	 * qTest : TC-17 Access a link to create a new R+F account from .biz site
	 * Description : This test validates sign up now link from .biz site
	 *     
	 */
	@Test(enabled=true) //TODO Incomplete
	public void testAccessALinkToCreateANewRFAccountFromBizSite_17(){
		//Duplicate as	TC-16 Access a link to create a new R+F account from .com site
	}
}