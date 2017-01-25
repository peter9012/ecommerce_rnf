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
	@Test(enabled=true)
	public void testOnTheUSWebsiteSwitchCountriesUsingToggleSelector_2(){
		String country_CA = "CAN";
		String country_AU ="AUS";
		String country_US ="USA";
		String countryNameFromUI =null;
		String prefix = TestConstants.CONSULTANT_PWS_PREFIX;
		navigateToStoreFrontBaseURL();
		countryNameFromUI = sfHomePage.getDefaultSelectedCountryNameFromToggle().toUpperCase();
		s_assert.assertTrue(countryNameFromUI.contains(country_US), "For Corp Expected default country name is "+country_US+" Actual on UI is:"+countryNameFromUI);
		//Select CAN country
		sfHomePage.clickToggleButtonOfCountry();
		sfHomePage.selectCountryFromToggleButton(country_CA);
		countryNameFromUI = sfHomePage.getDefaultSelectedCountryNameFromToggle().toUpperCase();
		s_assert.assertTrue(countryNameFromUI.contains(country_CA), "For Corp Expected country name is "+country_CA+" Actual on UI is:"+countryNameFromUI);
		//Select AU country
		sfHomePage.clickToggleButtonOfCountry();
		sfHomePage.selectCountryFromToggleButton(country_AU);
		countryNameFromUI = sfHomePage.getDefaultSelectedCountryNameFromToggle().toUpperCase();
		s_assert.assertTrue(countryNameFromUI.contains(country_AU), "For Corp Expected country name is "+country_AU+" Actual on UI is:"+countryNameFromUI);
		//Select US country
		sfHomePage.clickToggleButtonOfCountry();
		sfHomePage.selectCountryFromToggleButton(country_US);
		countryNameFromUI = sfHomePage.getDefaultSelectedCountryNameFromToggle().toUpperCase();
		s_assert.assertTrue(countryNameFromUI.contains(country_US), "For Corp Expected country name is "+country_US+" Actual on UI is:"+countryNameFromUI);
		sfHomePage.navigateToUrl(sfHomePage.getBaseUrl()+"/" +sfHomePage.getCountry() +"/pws/" + prefix);
		countryNameFromUI = sfHomePage.getDefaultSelectedCountryNameFromToggle().toUpperCase();
		s_assert.assertTrue(countryNameFromUI.contains(country_US), "For PWS Expected default country name is "+country_US+" Actual on UI is:"+countryNameFromUI);
		//Select CAN country
		sfHomePage.clickToggleButtonOfCountry();
		sfHomePage.selectCountryFromToggleButton(country_CA);
		countryNameFromUI = sfHomePage.getDefaultSelectedCountryNameFromToggle().toUpperCase();
		s_assert.assertTrue(countryNameFromUI.contains(country_CA), "For PWS Expected country name is "+country_CA+" Actual on UI is:"+countryNameFromUI);
		//Select AU country
		sfHomePage.clickToggleButtonOfCountry();
		sfHomePage.selectCountryFromToggleButton(country_AU);
		countryNameFromUI = sfHomePage.getDefaultSelectedCountryNameFromToggle().toUpperCase();
		s_assert.assertTrue(countryNameFromUI.contains(country_AU), "For PWS Expected country name is "+country_AU+" Actual on UI is:"+countryNameFromUI);
		//Select US country
		sfHomePage.clickToggleButtonOfCountry();
		sfHomePage.selectCountryFromToggleButton(country_US);
		countryNameFromUI = sfHomePage.getDefaultSelectedCountryNameFromToggle().toUpperCase();
		s_assert.assertTrue(countryNameFromUI.contains(country_US), "For PWS Expected country name is "+country_US+" Actual on UI is:"+countryNameFromUI);
		s_assert.assertAll();
	}

	/***
	 * qTest : TC-1 Accessing RF URL Should Defaults User To US Country
	 * 
	 * Description : This test validates default corporate URL redirects to US country. 
	 * 
	 *     
	 */
	@Test(enabled=true)
	public void testDefaultCorpAndBizURLRedirectsToUS_1(){
		String currentURL = null;
		String usCorpURL = "US";
		String prefix = TestConstants.CONSULTANT_PWS_PREFIX;
		navigateToStoreFrontBaseURLWithoutCountry();
		currentURL = sfHomePage.getCurrentURL().toLowerCase();
		s_assert.assertTrue(currentURL.contains(usCorpURL.toLowerCase()), "Expected corp URL should contain" +usCorpURL+" but actual on UI is"+currentURL);
		sfHomePage.navigateToUrl(sfHomePage.getBaseUrl()+"/" +sfHomePage.getCountry() +"/pws/" + prefix);
		//Verify corp url redirects to US site.
		currentURL = sfHomePage.getCurrentURL().toLowerCase();
		s_assert.assertTrue(currentURL.contains(usCorpURL.toLowerCase()), "Expected PWS URL should contain" +usCorpURL+" but actual on UI is"+currentURL);
		s_assert.assertAll();
	}

	/***
	 * qTest : TC-3 Toggle Should Be Deactivated If User Is Logged Into Their Account
	 * 
	 * Description : This test validates toggle button is disabled after 
	 * logged into their account
	 *     
	 */
	@Test(enabled=true)
	public void testToggleShouldBeDeactivatedIfUserIsLoggedIn_3(){
		String country_CA = "CAN";
		String countryNameFromUI =null;
		sfHomePage.loginToStoreFront(TestConstants.CONSULTANT_EMAIL_WITH_CRP_AND_PULSE, password);
		sfHomePage.clickToggleButtonOfCountry();
		sfHomePage.selectCountryFromToggleButton(country_CA);
		countryNameFromUI = sfHomePage.getDefaultSelectedCountryNameFromToggle().toUpperCase();
		s_assert.assertTrue(countryNameFromUI.contains(country_CA), "For Corp Expected country name is "+country_CA+" Actual on UI is:"+countryNameFromUI);
		s_assert.assertTrue(sfHomePage.isLogoutSuccessful(),"User is still logged in after switching country from US to CA");
		s_assert.assertAll();
	}
}
