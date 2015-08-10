package com.rf.test.website.storeFront.pws.rfo;

import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.annotations.Test;

import com.rf.core.utils.DBUtil;
import com.rf.core.website.constants.DBQueries;
import com.rf.core.website.constants.TestConstants;
import com.rf.pages.website.StoreFrontConsultantPage;
import com.rf.pages.website.StoreFrontHomePage;
import com.rf.test.website.RFWebsiteBaseTest;

public class RFPWSLoginTest extends RFWebsiteBaseTest {
	private static final Logger logger = LogManager
			.getLogger(RFPWSLoginTest.class.getName());

	private StoreFrontHomePage storeFrontHomePage;
	private StoreFrontConsultantPage storeFrontConsulatantPage;
	private String RFO_DB = null;

	//Consultant with PWS - Corporate site - Her own .com PWS
	@Test(enabled=false)
	public void testConsultantWithPWSLoginFromCorp(){
		RFO_DB = driver.getDBNameRFO();		
		List<Map<String, Object>> randomConsultantList =  null;
		//List<Map<String, Object>> randomConsultantPWSList =  null;
		String consultantWithPWSEmailID = null;
		String consultantPWSURL = null;
		storeFrontHomePage = new StoreFrontHomePage(driver);
		// Get Consultant with PWS from database
		randomConsultantList = DBUtil.performDatabaseQuery(DBQueries.GET_RANDOM_CONSULTANT_WITH_PWS_RFO,RFO_DB);
		consultantWithPWSEmailID = (String) getValueFromQueryResult(randomConsultantList, "UserName");
		consultantPWSURL = (String) getValueFromQueryResult(randomConsultantList, "URL");

		storeFrontConsulatantPage = storeFrontHomePage.loginAsConsultant(consultantWithPWSEmailID, TestConstants.CONSULTANT_PASSWORD_TST4);
		s_assert.assertTrue(!consultantPWSURL.contains(".biz"),"Consultant is not on her own .com PWS");
		logout();	
		s_assert.assertAll();
	}

	//Consultant with PWS - Her own PWS - Her own PWS
	@Test(enabled=false)
	public void testConsultantWithPWSLoginFromOwnPWS() throws InterruptedException{
		RFO_DB = driver.getDBNameRFO();		
		List<Map<String, Object>> randomConsultantList =  null;
		//List<Map<String, Object>> randomConsultantPWSList =  null;
		String consultantWithPWSEmailID = null;
		String consultantPWSURL = null;
		storeFrontHomePage = new StoreFrontHomePage(driver);
		// Get Consultant with PWS from database
		randomConsultantList = DBUtil.performDatabaseQuery(DBQueries.GET_RANDOM_CONSULTANT_WITH_PWS_RFO,RFO_DB);
		consultantWithPWSEmailID = (String) getValueFromQueryResult(randomConsultantList, "Username");
		consultantPWSURL = (String) getValueFromQueryResult(randomConsultantList, "URL");
		storeFrontHomePage.openConsultantPWS(consultantPWSURL);
		storeFrontConsulatantPage = storeFrontHomePage.loginAsConsultant(consultantWithPWSEmailID, TestConstants.CONSULTANT_PASSWORD_TST4);
		s_assert.assertTrue(storeFrontConsulatantPage.getCurrentURL().contains(consultantPWSURL),"Consultant is not on her own PWS");
		logout();	
		s_assert.assertAll();			
	}

	//Consultant with PWS -Someone else’s PWS - Her own PW
	@Test(enabled=false)
	public void testConsultantWithPWSLoginFromOthersPWS() throws InterruptedException{
		RFO_DB = driver.getDBNameRFO();		
		List<Map<String, Object>> randomConsultantList =  null;
		List<Map<String, Object>> randomOtherPWSList =  null;
		String consultantWithPWSEmailID = null;
		String consultantPWSURL = null;
		String otherPWSURL = null;

		// Get Consultant with PWS from database
		randomConsultantList = DBUtil.performDatabaseQuery(DBQueries.GET_RANDOM_CONSULTANT_WITH_PWS_RFO,RFO_DB);
		consultantWithPWSEmailID = (String) getValueFromQueryResult(randomConsultantList, "Username");

		// Get consultant's PWS from database.
		consultantPWSURL = (String) getValueFromQueryResult(randomConsultantList, "URL");

		// Get another PWS from database
		randomOtherPWSList = DBUtil.performDatabaseQuery(DBQueries.GET_RANDOM_CONSULTANT_WITH_PWS_RFO,RFO_DB);
		otherPWSURL = (String) getValueFromQueryResult(randomOtherPWSList, "URL");

		storeFrontHomePage = new StoreFrontHomePage(driver);
		storeFrontHomePage.openConsultantPWS(otherPWSURL);
		storeFrontConsulatantPage = storeFrontHomePage.loginAsConsultant(consultantWithPWSEmailID, TestConstants.CONSULTANT_PASSWORD_TST4);
		s_assert.assertTrue(storeFrontConsulatantPage.getCurrentURL().contains(consultantPWSURL),"Consultant is not on her own PWS");
		logout();	
		s_assert.assertAll();
	}


	//Consultant W/O PWS - Corporate site - Corporate site
	@Test(enabled=false)
	public void testConsultantWithoutPWSLoginFromCorp() throws InterruptedException{
		RFO_DB = driver.getDBNameRFO();		
		List<Map<String, Object>> randomConsultantList =  null;
		String consultantEmailID = null;
		randomConsultantList = DBUtil.performDatabaseQuery(DBQueries.GET_RANDOM_CONSULTANT_NO_PWS_RFO,RFO_DB);
		consultantEmailID = (String) getValueFromQueryResult(randomConsultantList, "EmailAddress");
		storeFrontHomePage = new StoreFrontHomePage(driver);
		storeFrontConsulatantPage = storeFrontHomePage.loginAsConsultant(consultantEmailID, TestConstants.CONSULTANT_PASSWORD_TST4);
		s_assert.assertTrue(storeFrontConsulatantPage.getCurrentURL().contains(driver.getURL()),"Consultant is not on corporate site");
		logout();
		s_assert.assertAll();		
	}


	//Consultant W/O PWS - Someone else’s PWS - Corporate site
	@Test(enabled=false)
	public void testConsultantWithoutPWSLoginFromOthersPWS() throws InterruptedException{
		RFO_DB = driver.getDBNameRFO();		
		List<Map<String, Object>> randomConsultantList =  null;
		List<Map<String, Object>> randomOtherPWSList =  null;
		String consultantEmailID = null;
		String otherPWSURL = null;

		// get consultant without PWS email Id from database
		randomConsultantList = DBUtil.performDatabaseQuery(DBQueries.GET_RANDOM_CONSULTANT_NO_PWS_RFO,RFO_DB);
		consultantEmailID = (String) getValueFromQueryResult(randomConsultantList, "EmailAddress");

		// Get another PWS from database
		randomOtherPWSList = DBUtil.performDatabaseQuery(DBQueries.GET_RANDOM_CONSULTANT_WITH_PWS_RFO,RFO_DB);
		otherPWSURL = (String) getValueFromQueryResult(randomOtherPWSList, "SiteName");

		storeFrontHomePage = new StoreFrontHomePage(driver);
		storeFrontHomePage.openConsultantPWS(otherPWSURL);
		storeFrontConsulatantPage = storeFrontHomePage.loginAsConsultant(consultantEmailID, TestConstants.CONSULTANT_PASSWORD_TST4);		
		s_assert.assertTrue(storeFrontConsulatantPage.getCurrentURL().contains(driver.getURL()),"Consultant is not on corporate site");
		logout();
		s_assert.assertAll();				
	}


	//RC - someone's PWS - someone's PWS
	@Test(enabled=false)
	public void testRetailCustomerLoginFromOthersPWS() throws InterruptedException{
		RFO_DB = driver.getDBNameRFO();		
		List<Map<String, Object>> randomRCList =  null;
		List<Map<String, Object>> randomOtherPWSList =  null;
		String rcEmailID = null;
		String otherPWSURL = null;

		randomRCList = DBUtil.performDatabaseQuery(DBQueries.GET_RANDOM_RC_RFO,RFO_DB);
		rcEmailID = (String) getValueFromQueryResult(randomRCList, "Username");

		// Get another PWS from database
		randomOtherPWSList = DBUtil.performDatabaseQuery(DBQueries.GET_RANDOM_CONSULTANT_WITH_PWS_RFO,RFO_DB);
		otherPWSURL = (String) getValueFromQueryResult(randomOtherPWSList, "URL");

		storeFrontHomePage = new StoreFrontHomePage(driver);
		storeFrontHomePage.openConsultantPWS(otherPWSURL);
		storeFrontConsulatantPage = storeFrontHomePage.loginAsConsultant(rcEmailID, TestConstants.RC_PASSWORD_TST4);
		s_assert.assertTrue(storeFrontConsulatantPage.getCurrentURL().contains(otherPWSURL),"RC is not on someone's PWS");
		logout();
		s_assert.assertAll();		
	}


	//RC - corporate site - Corporate Site
	@Test(enabled=false)
	public void testRetailCustomerLoginFromCorp() throws InterruptedException{
		RFO_DB = driver.getDBNameRFO();		
		List<Map<String, Object>> randomRCList =  null;
		String rcEmailID = null;

		randomRCList = DBUtil.performDatabaseQuery(DBQueries.GET_RANDOM_RC_RFO,RFO_DB);
		rcEmailID = (String) getValueFromQueryResult(randomRCList, "Username");

		storeFrontHomePage = new StoreFrontHomePage(driver);
		storeFrontConsulatantPage = storeFrontHomePage.loginAsConsultant(rcEmailID, TestConstants.RC_PASSWORD_TST4);
		s_assert.assertTrue(storeFrontConsulatantPage.getCurrentURL().contains(driver.getURL()),"RC is not on corporate site");
		logout();
		s_assert.assertAll();				
	}



	//PC, whose Sponsor has PWS - Sponsor’s PWS - Sponsor’s PWS
	@Test(enabled=false)
	public void testPreferredCustomerWithPWSSponsorLoginFromSponsorsPWS() throws InterruptedException{
		RFO_DB = driver.getDBNameRFO();		
		List<Map<String, Object>> randomPCWithPWSSponsorList =  null;
		String pcEmailID = null;
		String sponsorsPWS = null;

		randomPCWithPWSSponsorList = DBUtil.performDatabaseQuery(DBQueries.GET_RANDOM_PC_WHOSE_SPONSOR_HAS_PWS_RFO,RFO_DB);
		pcEmailID = (String) getValueFromQueryResult(randomPCWithPWSSponsorList, "Username");
		sponsorsPWS = (String) getValueFromQueryResult(randomPCWithPWSSponsorList, "Sponsor_PWS");

		storeFrontHomePage = new StoreFrontHomePage(driver);
		storeFrontHomePage.openConsultantPWS(sponsorsPWS);
		storeFrontConsulatantPage = storeFrontHomePage.loginAsConsultant(pcEmailID, TestConstants.PC_PASSWORD_TST4);
		s_assert.assertTrue(storeFrontConsulatantPage.getCurrentURL().contains(sponsorsPWS),"PC is not on Sponsor's PWS");
		logout();
		s_assert.assertAll();
	}


	//PC, whose Sponsor has PWS - Not Sponsor’s PWS - Sponsor’s PWS
	@Test(enabled=false)
	public void testPreferredCustomerWithPWSSponsorLoginFromOtherSponsorsPWS() throws InterruptedException{
		RFO_DB = driver.getDBNameRFO();		
		List<Map<String, Object>> randomPCWithPWSSponsorList =  null;
		List<Map<String, Object>> otherPWSList =  null;
		List<Map<String, Object>> sponsorPWSList =  null;
		String pcEmailID = null;
		String sponsorsPWS = null;
		String otherPWS = null;		

		randomPCWithPWSSponsorList = DBUtil.performDatabaseQuery(DBQueries.GET_RANDOM_PC_WHOSE_SPONSOR_HAS_PWS_RFO,RFO_DB);
		pcEmailID = (String) getValueFromQueryResult(randomPCWithPWSSponsorList, "Username");

		//sponsorPWSList = DBUtil.performDatabaseQuery(,RFO_DB);
		sponsorsPWS = (String) getValueFromQueryResult(randomPCWithPWSSponsorList, "Sponsor_PWS");

		otherPWSList = DBUtil.performDatabaseQuery(DBQueries.GET_RANDOM_CONSULTANT_WITH_PWS_RFO,RFO_DB);
		otherPWS = (String) getValueFromQueryResult(otherPWSList, "URL");

		storeFrontHomePage = new StoreFrontHomePage(driver);
		storeFrontHomePage.openConsultantPWS(otherPWS);
		storeFrontConsulatantPage = storeFrontHomePage.loginAsConsultant(pcEmailID, TestConstants.PC_PASSWORD_TST4);
		s_assert.assertTrue(storeFrontConsulatantPage.getCurrentURL().contains(sponsorsPWS),"PC is not on Sponsor's PWS");
		logout();
		s_assert.assertAll();
	}


	//PC, whose Sponsor has PWS - Corporate Site - Sponsor’s PWS
	@Test(enabled=false)
	public void testPreferredCustomerWithPWSSponsorLoginFromCorp() throws InterruptedException{
		RFO_DB = driver.getDBNameRFO();		
		List<Map<String, Object>> randomPCWithPWSSponsorList =  null;
		List<Map<String, Object>> sponsorPWSList =  null;
		String pcEmailID = null;
		String sponsorsPWS = null;

		randomPCWithPWSSponsorList = DBUtil.performDatabaseQuery(DBQueries.GET_RANDOM_PC_WHOSE_SPONSOR_HAS_PWS_RFO,RFO_DB);
		pcEmailID = (String) getValueFromQueryResult(randomPCWithPWSSponsorList, "Username");

		//sponsorPWSList = DBUtil.performDatabaseQuery(,RFO_DB);
		sponsorsPWS = (String) getValueFromQueryResult(randomPCWithPWSSponsorList, "Sponsor_PWS");

		storeFrontHomePage = new StoreFrontHomePage(driver);
		storeFrontConsulatantPage = storeFrontHomePage.loginAsConsultant(pcEmailID, TestConstants.PC_PASSWORD_TST4);
		s_assert.assertTrue(storeFrontConsulatantPage.getCurrentURL().contains(sponsorsPWS),"PC is not on Sponsor's PWS");
		logout();
		s_assert.assertAll();
	}



	//PC, whose Sponsor has No PWS - someone's PWS - Corporate Site
	@Test(enabled=false)
	public void testPreferredCustomerWithNoPWSSponsorLoginFromOtherPWS() throws InterruptedException{
		RFO_DB = driver.getDBNameRFO();		
		List<Map<String, Object>> randomPCWithSponsorNoPWSList =  null;
		List<Map<String, Object>> otherPWSList =  null;
		String pcEmailID = null;
		String otherPWS = null;		

		randomPCWithSponsorNoPWSList = DBUtil.performDatabaseQuery(DBQueries.GET_RANDOM_PC_WHOSE_SPONSOR_HAS_NOPWS_RFO,RFO_DB);
		pcEmailID = (String) getValueFromQueryResult(randomPCWithSponsorNoPWSList, "Username");

		otherPWSList = DBUtil.performDatabaseQuery(DBQueries.GET_RANDOM_CONSULTANT_WITH_PWS_RFO,RFO_DB);
		otherPWS = (String) getValueFromQueryResult(otherPWSList, "URL");

		storeFrontHomePage = new StoreFrontHomePage(driver);
		storeFrontHomePage.openConsultantPWS(otherPWS);
		storeFrontConsulatantPage = storeFrontHomePage.loginAsConsultant(pcEmailID, TestConstants.PC_PASSWORD_TST4);
		s_assert.assertTrue(storeFrontConsulatantPage.getCurrentURL().contains(driver.getURL()),"PC is not on Corporate");
		logout();
		s_assert.assertAll();
	}


	//PC, whose Sponsor has No PWS - Corporate Site - Corporate Site
	@Test(enabled=false)
	public void testPreferredCustomerWithNoPWSSponsorLoginFromCorp() throws InterruptedException{
		RFO_DB = driver.getDBNameRFO();		
		List<Map<String, Object>> randomPCWithSponsorNoPWSList =  null;
		String pcEmailID = null;

		randomPCWithSponsorNoPWSList = DBUtil.performDatabaseQuery(DBQueries.GET_RANDOM_PC_WHOSE_SPONSOR_HAS_NOPWS_RFO,RFO_DB);
		pcEmailID = (String) getValueFromQueryResult(randomPCWithSponsorNoPWSList, "Username");

		storeFrontHomePage = new StoreFrontHomePage(driver);
		storeFrontConsulatantPage = storeFrontHomePage.loginAsConsultant(pcEmailID, TestConstants.PC_PASSWORD_TST4);
		s_assert.assertTrue(storeFrontConsulatantPage.getCurrentURL().contains(driver.getURL()),"PC is not on Corporate");
		logout();
		s_assert.assertAll();

	}


	//PC, with no sponsor - someone's site - Corporate Site 
	@Test(enabled=false)
	public void testPreferredCustomerNoSponsorLoginFromOthersPWS() throws InterruptedException{
		RFO_DB = driver.getDBNameRFO();		
		List<Map<String, Object>> randomPCWithNoSponsorList =  null;
		List<Map<String, Object>> otherPWSList =  null;
		String pcEmailID = null;
		String otherPWS = null;		

		randomPCWithNoSponsorList = DBUtil.performDatabaseQuery(DBQueries.GET_RANDOM_PC_WITH_NO_SPONSOR_RFO,RFO_DB);
		pcEmailID = (String) getValueFromQueryResult(randomPCWithNoSponsorList, "Username");

		otherPWSList = DBUtil.performDatabaseQuery(DBQueries.GET_RANDOM_CONSULTANT_WITH_PWS_RFO,RFO_DB);
		otherPWS = (String) getValueFromQueryResult(otherPWSList, "URL");

		storeFrontHomePage = new StoreFrontHomePage(driver);
		storeFrontHomePage.openConsultantPWS(otherPWS);
		storeFrontConsulatantPage = storeFrontHomePage.loginAsConsultant(pcEmailID, TestConstants.PC_PASSWORD_TST4);
		s_assert.assertTrue(storeFrontConsulatantPage.getCurrentURL().contains(driver.getURL()),"PC is not on Corporate");
		logout();
		s_assert.assertAll();

	}

	//PC, with no sponsor - corporate site - Corporate Site
	@Test(enabled=false)
	public void testPreferredCustomerNoSponsorLoginFromCorp() throws InterruptedException{
		RFO_DB = driver.getDBNameRFO();		
		List<Map<String, Object>> randomPCWithNoSponsorList =  null;
		String pcEmailID = null;

		randomPCWithNoSponsorList = DBUtil.performDatabaseQuery(DBQueries.GET_RANDOM_PC_WITH_NO_SPONSOR_RFO,RFO_DB);
		pcEmailID = (String) getValueFromQueryResult(randomPCWithNoSponsorList, "Username");

		storeFrontHomePage = new StoreFrontHomePage(driver);
		storeFrontConsulatantPage = storeFrontHomePage.loginAsConsultant(pcEmailID, TestConstants.PC_PASSWORD_TST4);
		s_assert.assertTrue(storeFrontConsulatantPage.getCurrentURL().contains(driver.getURL()),"PC is not on Corporate");
		logout();
		s_assert.assertAll();
	}

}


