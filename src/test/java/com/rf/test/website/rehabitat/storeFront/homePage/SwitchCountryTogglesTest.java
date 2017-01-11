package com.rf.test.website.rehabitat.storeFront.homePage;

import org.testng.annotations.Test;

import com.rf.core.website.constants.TestConstants;
import com.rf.test.website.rehabitat.storeFront.baseTest.StoreFrontWebsiteBaseTest;

public class SwitchCountryTogglesTest extends StoreFrontWebsiteBaseTest{

	/***
	 * qTest : TC-2 On the US Website Switch Counties Using Toggle Selector
	 * Description : This test switch the countries from toggle button and verify
	 *     
	 */
	@Test(enabled=false)//TODO Incomplete(need to com & biz url)
	public void testOnTheUSWebsiteSwitchCountriesUsingToggleSelector_2(){
		String country_CA = "CAN";
		String country_AU ="AUS";
		String country_US ="USA";
		String countryNameFromUI =null;
		countryNameFromUI = sfHomePage.getDefaultSelectedCountryNameFromToggle().toUpperCase();
		s_assert.assertTrue(countryNameFromUI.contains(country_US), "Expected default country name is "+country_US+" Actual on UI is:"+countryNameFromUI);
		//Select CAN country
		sfHomePage.clickToggleButtonOfCountry();
		sfHomePage.selectCountryFromToggleButton(country_CA);
		countryNameFromUI = sfHomePage.getDefaultSelectedCountryNameFromToggle().toUpperCase();
		s_assert.assertTrue(countryNameFromUI.contains(country_CA), "Expected country name is "+country_CA+" Actual on UI is:"+countryNameFromUI);
		//Select AU country
		sfHomePage.clickToggleButtonOfCountry();
		sfHomePage.selectCountryFromToggleButton(country_AU);
		countryNameFromUI = sfHomePage.getDefaultSelectedCountryNameFromToggle().toUpperCase();
		s_assert.assertTrue(countryNameFromUI.contains(country_AU), "Expected country name is "+country_AU+" Actual on UI is:"+countryNameFromUI);
		//Select US country
		sfHomePage.clickToggleButtonOfCountry();
		sfHomePage.selectCountryFromToggleButton(country_US);
		countryNameFromUI = sfHomePage.getDefaultSelectedCountryNameFromToggle().toUpperCase();
		s_assert.assertTrue(countryNameFromUI.contains(country_US), "Expected country name is "+country_US+" Actual on UI is:"+countryNameFromUI);
		s_assert.assertAll();
	}

	/***
	 * qTest : TC-3 Toggle Should Be Deactivated If User Is Logged Into Their Account
	 * 
	 * Description : This test validates toggle button is disabled after 
	 * logged into their account
	 *     
	 */
	@Test(enabled=false) //Incomplete toggle is present after login.
	public void testToggleShouldBeDeactivatedIfUserIsLoggedIn_3(){
		sfHomePage.loginToStoreFront(TestConstants.CONSULTANT_EMAIL, password);
		s_assert.assertFalse(sfHomePage.isToggleButtonPresent(), "Expected toggle should not be present after login");
		s_assert.assertAll();
	}

	/***
	 * qTest : TC-1 Accessing RF URL Should Defaults User To US Country
	 * 
	 * Description : This test validates default corporate URL redirects to US country. 
	 * 
	 *     
	 */
	@Test(enabled=false) //Incomplete (need com and biz url)
	public void testDefaultCorpAndBizURLRedirectsToUS_1(){
		String currentURL = null;
		String usCorpURL = "US";
		//Verify corp url redirects to US site.
		currentURL = sfHomePage.getCurrentURL().toLowerCase();
		s_assert.assertTrue(currentURL.contains(usCorpURL.toLowerCase()), "Expected URL should contain" +usCorpURL+" but actual on UI is"+currentURL);
		s_assert.assertAll();
	}
}
