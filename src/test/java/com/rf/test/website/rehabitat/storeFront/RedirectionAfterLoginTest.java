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
	@Test(enabled=true)
	public void testAsAConsultantAfterLoggingInFromCorpSponsorWithPWS_374(){
		String currentURL = null;
		sfHomePage.loginToStoreFront(TestConstants.CONSULTANT_EMAIL_WITH_CRP_AND_PULSE,  password,true);
		currentURL = sfHomePage.getCurrentURL();
		s_assert.assertTrue(currentURL.contains("pws"),"Current url should contain for consultant with pws is PWS but actual on UI is "+currentURL);
		s_assert.assertAll();
	}

	/***
	 * qTest : TC-376 As a PC or Consultant, after logging in from PWS - Sponsor without PWS
	 * Description : This test validate the URL while login with a consultant who's sponsor not having pws
	 *     
	 */
	@Test(enabled=true)
	public void testAsAConsultantAfterLoggingInFromCorpSponsorWithoutPWS_376(){
		String currentURL = null;
		sfHomePage.loginToStoreFront(TestConstants.PC_EMAIL_WHO_ENROLLED_UNDER_SPONSOR_WITHOUT_PWS, password, true);
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
	@Test(enabled=true)
	public void testConsultantWithPWSLoggingInFromCorpSite_530(){
		String currentURL = null;
		String prefix = TestConstants.CONSULTANT_PWS_PREFIX;
		sfHomePage.loginToStoreFront(TestConstants.CONSULTANT_EMAIL_WITH_CRP_AND_PULSE,  password,true);
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
	@Test(enabled=true)
	public void testPCUserEnrollrdUnderSponsorWithPWSAndLogsInFromDifferentSponsorPWS_531(){
		String currentURL = null;
		String prefix = TestConstants.CONSULTANT_PWS_PREFIX;
		sfHomePage.navigateToUrl(sfHomePage.getBaseUrl()+"/" +sfHomePage.getCountry() +"/pws/" + prefix);
		currentURL = sfHomePage.getCurrentURL();
		s_assert.assertTrue(currentURL.contains(prefix),"Current url should contain for "+prefix+" but actual on UI is "+currentURL);
		sfHomePage.loginToStoreFront(TestConstants.PC_EMAIL_WHO_ENROLLED_UNDER_SPONSOR_WITH_PWS,  password,true);
		currentURL = sfHomePage.getCurrentURL();
		s_assert.assertFalse(currentURL.contains(prefix),"Current url should  not contain "+prefix+" but after login actual on UI is "+currentURL);
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
		String prefix = TestConstants.CONSULTANT_PWS_PREFIX;
		sfHomePage.navigateToUrl(sfHomePage.getBaseUrl()+"/" +sfHomePage.getCountry() +"/pws/" + prefix);
		currentURL = sfHomePage.getCurrentURL();
		s_assert.assertFalse(currentURL.contains(prefix),"Current url should contain for "+prefix+" but actual on UI is "+currentURL);
		sfHomePage.loginToStoreFront(TestConstants.CONSULTANT_EMAIL_WITH_CRP_AND_PULSE,  password,true);
		currentURL = sfHomePage.getCurrentURL();
		s_assert.assertTrue(currentURL.contains(prefix),"Current url should contain for "+prefix+" but after login actual on UI is "+currentURL);
		s_assert.assertAll();
	}

	/***
	 * qTest : TC-533 PC user who has RF corporate as Sponsor should redirect user to Corporate site
	 * Description : This test validate login with PC who enrolled under RF Corporate sponsor
	 * after login it is redirecting to corp site
	 *     
	 */
	@Test(enabled=true)
	public void testPCUserWhoHasRFCorporateAsSponsorShouldRedirectUserTOCorporateSite_533(){
		String currentURL = null;
		sfHomePage.loginToStoreFront(TestConstants.PC_EMAIL_HAVING_AUTOSHIP,  password,true);
		currentURL = sfHomePage.getCurrentURL();
		s_assert.assertTrue(!currentURL.contains("pws"),"Current url for PC with RF corporate as sponsor is containing 'PWS' and actual on UI is "+currentURL);
		s_assert.assertAll();
	}

	/***
	 * qTest : TC-534 Consultant who doesn't have a PWS and logs in from the R+F corp home page
	 * Description : This test validate login with Consultant who doesn't have a PWS and logs in from the R+F corp
	 * after login it is redirecting to corp site
	 *     
	 */
	@Test(enabled=true)
	public void testConsultantWhoDoesNotHavePWSAndLogsInFromRFCorp_534(){
		String currentURL = null;
		sfHomePage.loginToStoreFront(TestConstants.CONSULTANT_EMAIL_SPONSOR_WITHOUT_PWS,  password,true);
		currentURL = sfHomePage.getCurrentURL();
		s_assert.assertTrue(!currentURL.contains("pws"),"Current url for consultant without PWS after login it is containing 'PWS' and actual on UI is "+currentURL);
		s_assert.assertAll();
	}

	/***
	 * qTest : TC-535 PC user whose sponsor doesn't have a PWS and logs in from the R+F corp home page
	 * Description : This test validate login with Pc who enrolled under sponsor without PWS
	 * after login it is redirecting to corp site
	 *     
	 */
	@Test(enabled=true)
	public void testPCUserWhoseSponsorDoesNotHavePWSAndLogsFromRFCorp_535(){
		String currentURL = null;
		sfHomePage.loginToStoreFront(TestConstants.PC_EMAIL_WHO_ENROLLED_UNDER_SPONSOR_WITHOUT_PWS,  password,true);
		currentURL = sfHomePage.getCurrentURL();
		s_assert.assertTrue(!currentURL.contains("pws"),"Current url for PC who enrolled under sponsor without PWS after login it is containing 'PWS' and actual on UI is "+currentURL);
		s_assert.assertAll();
	}

	/***
	 * qTest : TC-375 As a PC or Consultant, after logging in from PWS - Sponsor with PWS
	 * Description : This test validates the URL, login with a consultant having PWS and click on R+F logo
	 *     
	 */
	@Test(enabled=true)
	public void testAsAPCOrConsultantAfterLoggingInFromPWSSponsorWithPWS_375(){
		String homePageURL = null;
		String currentURL = null;
		String prefix = TestConstants.CONSULTANT_PWS_PREFIX;
		homePageURL = sfHomePage.getCurrentURL();
		sfHomePage.navigateToUrl(homePageURL + "/pws/" + prefix);
		sfHomePage.loginToStoreFront(TestConstants.CONSULTANT_EMAIL_WITH_CRP_AND_PULSE,  password,true);
		currentURL = sfHomePage.getCurrentURL();
		s_assert.assertTrue(currentURL.contains("pws"),"Current url should contain 'PWS' but actual on UI is "+currentURL);
		sfHomePage.clickRodanAndFieldsLogo();
		currentURL = sfHomePage.getCurrentURL();
		s_assert.assertTrue(currentURL.contains("pws"),"Current url should redirected to PWS of sponser but actual on UI is "+currentURL);
		s_assert.assertAll();
	}


	/***
	 * qTest : TC-529 PC user enrolled under Sponsor PWS and logs in from the R+F corporate website
	 * Description : This test validate the URL while login with a PC who's sponsor having pws
	 *     
	 */
	@Test(enabled=true)
	public void testPCUserEnrolledUnderSponsorPWSAndLogsInFromTheRFCorporateWebsite_529(){
		String currentURL=null;
		String sponser=TestConstants.SPONSOR;
		sfHomePage.loginToStoreFront(TestConstants.PC_EMAIL_WHO_ENROLLED_UNDER_SPONSOR_WITH_PWS,  password,true);
		currentURL = sfHomePage.getCurrentURL().toLowerCase();
		s_assert.assertTrue(currentURL.contains(sponser),"Current url should contain sponser name:"+sponser+" but actual URL on UI is "+currentURL);
		s_assert.assertAll();
	}


}