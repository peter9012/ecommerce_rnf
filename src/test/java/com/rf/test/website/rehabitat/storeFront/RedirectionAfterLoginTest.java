package com.rf.test.website.rehabitat.storeFront;

import org.testng.annotations.Test;

import com.rf.core.utils.CommonUtils;
import com.rf.core.website.constants.TestConstants;
import com.rf.test.website.rehabitat.storeFront.baseTest.StoreFrontWebsiteBaseTest;

public class RedirectionAfterLoginTest extends StoreFrontWebsiteBaseTest{

	/***
	 * qTest : TC-374 As a PC or Consultant, after logging in from Corp - Sponsor with PWS
	 * Description : This test validate the URL while login with a consultant having PWS
	 *     
	 */
	@Test(enabled=false)
	public void testAsAConsultantAfterLoggingInFromCorpSponsorWithPWS_374(){
		String currentURL = null;
		sfHomePage.loginToStoreFront(TestConstants.CONSULTANT_USERNAME_WITH_CRP_AND_PULSE, password);
		currentURL = sfHomePage.getCurrentURL();
		s_assert.assertTrue(currentURL.contains("pws"),"Current url should contain for consultant with pws is PWS but actual on UI is "+currentURL);
		s_assert.assertAll();
	}
	
	/***
	 * qTest : TC-376 As a PC or Consultant, after logging in from PWS - Sponsor without PWS
	 * Description : This test validate the URL while login with a consultant who's sponsor not having pws
	 *     
	 */
	@Test(enabled=false)
	public void testAsAConsultantAfterLoggingInFromCorpSponsorWithoutPWS_376(){
		String currentURL = null;
		sfHomePage.loginToStoreFront(TestConstants.CONSULTANT_USERNAME_SPONSOR_WITHOUT_PWS, password, true);
		sfHomePage.clickRodanAndFieldsLogo();
		currentURL = sfHomePage.getCurrentURL();
		s_assert.assertFalse(currentURL.contains("pws"),"Current url should contain for consultant with pws is PWS but actual on UI is "+currentURL);
		s_assert.assertAll();
	}
	
	/***
	 * qTest : TC-530 Consultant with PWS Logs in from the R+F corporate website
	 * Description : This test validate the URL while login with a consultant who's having PWS
	 *     
	 */
	@Test(enabled=false)
	public void testConsultantWithPWSLoggingInFromCorpSite_530(){
		String currentURL = null;
		String prefix = TestConstants.CONSULTANT_PREFIX;
		sfHomePage.loginToStoreFront(TestConstants.CONSULTANT_USERNAME_WITH_CRP_AND_PULSE, password);
		sfHomePage.clickRodanAndFieldsLogo();
		currentURL = sfHomePage.getCurrentURL();
		s_assert.assertTrue(currentURL.contains("pws") && currentURL.contains(prefix),"Current url should contain for consultant with pws is PWS and "+prefix+" but actual on UI is "+currentURL);
		s_assert.assertAll();
	}
	
	/***
	 * qTest : TC-531 PC user enrolled under sponsor PWS and logs in from different sponsor PWS
	 * Description : This test validate PWS of PC's sponsor after login from different sponsor PWS
	 *     
	 */
	@Test(enabled=false)
	public void testPCUserEnrollrdUnderSponsorWithPWSAndLogsInFromDifferentSponsorPWS_531(){
		String currentURL = null;
		String PWS = TestConstants.PWS;
		String PWSPrefix = TestConstants.CONSULTANT_PREFIX;
		sfHomePage.navigateToUrl(PWS);
		currentURL = sfHomePage.getCurrentURL();
		s_assert.assertFalse(currentURL.contains(PWSPrefix),"Current url should contain for "+PWSPrefix+" but actual on UI is "+currentURL);
		sfHomePage.loginToStoreFront(TestConstants.PC_EMAIL_WHO_ENROLLED_UNDER_SPONSOR_WITH_PWS, password);
		currentURL = sfHomePage.getCurrentURL();
		s_assert.assertTrue(currentURL.contains(PWSPrefix),"Current url should contain for "+PWSPrefix+" but after login actual on UI is "+currentURL);
		s_assert.assertAll();
	}

	/***
	 * qTest : TC-532 Consultnat with PWS and logs in from different sponsor PWS
	 * Description : This test validate prefix of consultant after login from different sponsor PWS
	 *     
	 */
	@Test(enabled=false)
	public void testConsultantWithPWSAndLogsInFromDifferentSponsorPWS_532(){
		String currentURL = null;
		String PWS = TestConstants.PWS;
		String PWSPrefix = TestConstants.CONSULTANT_PREFIX;
		sfHomePage.navigateToUrl(PWS);
		currentURL = sfHomePage.getCurrentURL();
		s_assert.assertFalse(currentURL.contains(PWSPrefix),"Current url should contain for "+PWSPrefix+" but actual on UI is "+currentURL);
		sfHomePage.loginToStoreFront(TestConstants.CONSULTANT_USERNAME_WITH_CRP_AND_PULSE, password);
		currentURL = sfHomePage.getCurrentURL();
		s_assert.assertTrue(currentURL.contains(PWSPrefix),"Current url should contain for "+PWSPrefix+" but after login actual on UI is "+currentURL);
		s_assert.assertAll();
	}

	
}