package com.rf.test.website.storeFront.dataMigration.rfl.pws;

import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.annotations.Test;

import com.rf.core.utils.DBUtil;
import com.rf.core.website.constants.TestConstants;
import com.rf.core.website.constants.dbQueries.DBQueries_RFL;
import com.rf.pages.website.storeFront.StoreFrontConsultantPage;
import com.rf.pages.website.storeFront.StoreFrontHomePage;
import com.rf.test.website.RFWebsiteBaseTest;

public class PWSLoginTest extends RFWebsiteBaseTest {
	private static final Logger logger = LogManager
			.getLogger(PWSLoginTest.class.getName());

	private StoreFrontHomePage storeFrontHomePage;
	private StoreFrontConsultantPage storeFrontConsulatantPage;
	private String RFL_DB = null;

	//Hybris Phase 2-4396 Consultant with PWS - Corporate site - Her own .com PWS
	@Test(enabled=false)
	public void testConsultantWithPWSLoginFromCorp_4396(){
		RFL_DB = driver.getDBNameRFL();		
		List<Map<String, Object>> randomConsultantList =  null;
		String consultantWithPWSEmailID = null;
		String consultantPWSURL = null;
		storeFrontHomePage = new StoreFrontHomePage(driver);
		// Get Consultant with PWS from database
		randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFL.GET_RANDOM_CONSULTANT_WITH_PWS_RFL,RFL_DB);
		consultantWithPWSEmailID = (String) getValueFromQueryResult(randomConsultantList, "UserName");
		consultantPWSURL = (String) getValueFromQueryResult(randomConsultantList, "URL");
		consultantPWSURL = consultantPWSURL.toLowerCase();
		storeFrontConsulatantPage = storeFrontHomePage.loginAsConsultant(consultantWithPWSEmailID, password);
		s_assert.assertTrue(driver.getCurrentUrl().toLowerCase().contains(".com"),"Consultant is not on her own .com PWS");
			
		s_assert.assertAll();
	}

	//Hybris Phase 2-4394 Consultant with PWS - Her own PWS - Her own PWS
	@Test
	public void testConsultantWithPWSLoginFromOwnPWS_4394() throws InterruptedException{
		RFL_DB = driver.getDBNameRFL();		
		List<Map<String, Object>> randomConsultantList =  null;
		//List<Map<String, Object>> randomConsultantPWSList =  null;
		String consultantWithPWSEmailID = null;
		String consultantPWSURL = null;
		storeFrontHomePage = new StoreFrontHomePage(driver);
		// Get Consultant with PWS from database
		randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFL.GET_RANDOM_CONSULTANT_WITH_PWS_RFL,RFL_DB);
		consultantWithPWSEmailID = (String) getValueFromQueryResult(randomConsultantList, "UserName");
		consultantPWSURL = (String) getValueFromQueryResult(randomConsultantList, "URL");
		consultantPWSURL = consultantPWSURL.toLowerCase();
		storeFrontHomePage.openConsultantPWS(consultantPWSURL);
		storeFrontConsulatantPage = storeFrontHomePage.loginAsConsultant(consultantWithPWSEmailID, password);
		s_assert.assertTrue(storeFrontConsulatantPage.getCurrentURL().toLowerCase().contains(consultantPWSURL),"Consultant is not on her own PWS");
			
		s_assert.assertAll();			
	}

	//Hybris Phase 2-4395 Consultant with PWS -Someone else’s PWS - Her own PW
	@Test
	public void testConsultantWithPWSLoginFromOthersPWS_4395() throws InterruptedException{
		RFL_DB = driver.getDBNameRFL();		
		List<Map<String, Object>> randomConsultantList =  null;
		List<Map<String, Object>> randomOtherPWSList =  null;
		String consultantWithPWSEmailID = null;
		String consultantPWSURL = null;
		String otherPWSURL = null;

		// Get Consultant with PWS from database
		randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFL.GET_RANDOM_CONSULTANT_WITH_PWS_RFL,RFL_DB);
		consultantWithPWSEmailID = (String) getValueFromQueryResult(randomConsultantList, "UserName");

		// Get consultant's PWS from database.
		consultantPWSURL = (String) getValueFromQueryResult(randomConsultantList, "URL");
		consultantPWSURL = consultantPWSURL.toLowerCase();
		// Get another PWS from database
		randomOtherPWSList = DBUtil.performDatabaseQuery(DBQueries_RFL.GET_RANDOM_CONSULTANT_WITH_PWS_RFL,RFL_DB);
		otherPWSURL = (String) getValueFromQueryResult(randomOtherPWSList, "URL");
		otherPWSURL = otherPWSURL.toLowerCase();
		storeFrontHomePage = new StoreFrontHomePage(driver);
		storeFrontHomePage.openConsultantPWS(otherPWSURL);
		storeFrontConsulatantPage = storeFrontHomePage.loginAsConsultant(consultantWithPWSEmailID, password);
		s_assert.assertTrue(storeFrontConsulatantPage.getCurrentURL().toLowerCase().contains(consultantPWSURL),"Consultant is not on her own PWS");
			
		s_assert.assertAll();
	}


	//Hybris Phase 2-4398 Consultant W/O PWS - Corporate site - Corporate site
	@Test
	public void testConsultantWithoutPWSLoginFromCorp_4398() throws InterruptedException{
		RFL_DB = driver.getDBNameRFL();		
		List<Map<String, Object>> randomConsultantList =  null;
		String consultantEmailID = null;
		randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFL.GET_RANDOM_CONSULTANT_NO_PWS_RFL,RFL_DB);
		consultantEmailID = (String) getValueFromQueryResult(randomConsultantList, "EmailAddress");
		storeFrontHomePage = new StoreFrontHomePage(driver);
		storeFrontConsulatantPage = storeFrontHomePage.loginAsConsultant(consultantEmailID, password);
		s_assert.assertTrue(storeFrontConsulatantPage.getCurrentURL().contains(driver.getURL()),"Consultant is not on corporate site");
		
		s_assert.assertAll();		
	}


	//Hybris Phase 2-4397 Consultant W/O PWS - Someone else’s PWS - Corporate site
	@Test
	public void testConsultantWithoutPWSLoginFromOthersPWS_4397() throws InterruptedException{
		RFL_DB = driver.getDBNameRFL();		
		List<Map<String, Object>> randomConsultantList =  null;
		List<Map<String, Object>> randomOtherPWSList =  null;
		String consultantEmailID = null;
		String otherPWSURL = null;

		// get consultant without PWS email Id from database
		randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFL.GET_RANDOM_CONSULTANT_NO_PWS_RFL,RFL_DB);
		consultantEmailID = (String) getValueFromQueryResult(randomConsultantList, "EmailAddress");

		// Get another PWS from database
		randomOtherPWSList = DBUtil.performDatabaseQuery(DBQueries_RFL.GET_RANDOM_CONSULTANT_WITH_PWS_RFL,RFL_DB);
		otherPWSURL = (String) getValueFromQueryResult(randomOtherPWSList, "URL");
		otherPWSURL = otherPWSURL.toLowerCase();
		storeFrontHomePage = new StoreFrontHomePage(driver);
		storeFrontHomePage.openConsultantPWS(otherPWSURL);
		storeFrontConsulatantPage = storeFrontHomePage.loginAsConsultant(consultantEmailID, password);		
		s_assert.assertTrue(storeFrontConsulatantPage.getCurrentURL().toLowerCase().contains(driver.getURL().toLowerCase()),"Consultant is not on corporate site");
		
		s_assert.assertAll();				
	}


	//Hybris Phase 2-4406 RC - someone's PWS - someone's PWS
	@Test
	public void testRetailCustomerLoginFromOthersPWS_4406() throws InterruptedException{
		RFL_DB = driver.getDBNameRFL();		
		List<Map<String, Object>> randomRCList =  null;
		List<Map<String, Object>> randomOtherPWSList =  null;
		String rcEmailID = null;
		String otherPWSURL = null;

		randomRCList = DBUtil.performDatabaseQuery(DBQueries_RFL.GET_RANDOM_RC_RFL,RFL_DB);
		rcEmailID = (String) getValueFromQueryResult(randomRCList, "EmailAddress");

		// Get another PWS from database
		randomOtherPWSList = DBUtil.performDatabaseQuery(DBQueries_RFL.GET_RANDOM_CONSULTANT_WITH_PWS_RFL,RFL_DB);
		otherPWSURL = (String) getValueFromQueryResult(randomOtherPWSList, "URL");
		otherPWSURL =otherPWSURL.toLowerCase();
		storeFrontHomePage = new StoreFrontHomePage(driver);
		storeFrontHomePage.openConsultantPWS(otherPWSURL);
		storeFrontConsulatantPage = storeFrontHomePage.loginAsConsultant(rcEmailID, password);
		s_assert.assertTrue(storeFrontConsulatantPage.getCurrentURL().toLowerCase().contains(otherPWSURL),"RC is not on someone's PWS");
		
		s_assert.assertAll();		
	}


	//Hybris Phase 2-4407 RC - corporate site - Corporate Site
	@Test
	public void testRetailCustomerLoginFromCorp_4407() throws InterruptedException{
		RFL_DB = driver.getDBNameRFL();		
		List<Map<String, Object>> randomRCList =  null;
		String rcEmailID = null;

		randomRCList = DBUtil.performDatabaseQuery(DBQueries_RFL.GET_RANDOM_RC_RFL,RFL_DB);
		rcEmailID = (String) getValueFromQueryResult(randomRCList, "EmailAddress");
		
		storeFrontHomePage = new StoreFrontHomePage(driver);
		storeFrontConsulatantPage = storeFrontHomePage.loginAsConsultant(rcEmailID, password);
		s_assert.assertTrue(storeFrontConsulatantPage.getCurrentURL().toLowerCase().contains(driver.getURL().toLowerCase()),"RC is not on corporate site");
		
		s_assert.assertAll();				
	}


	//Hybris Phase 2-4399 PC, whose Sponsor has PWS - Sponsor’s PWS - Sponsor’s PWS
	@Test
	public void testPreferredCustomerWithPWSSponsorLoginFromSponsorsPWS_4399() throws InterruptedException{
		RFL_DB = driver.getDBNameRFL();		
		List<Map<String, Object>> randomPCWithPWSSponsorList =  null;
		String pcEmailID = null;
		String sponsorsPWS = null;

		randomPCWithPWSSponsorList = DBUtil.performDatabaseQuery(DBQueries_RFL.GET_RANDOM_PC_WHOSE_SPONSOR_HAS_PWS,RFL_DB);
		pcEmailID = (String) getValueFromQueryResult(randomPCWithPWSSponsorList, "UserName");
		sponsorsPWS = (String) getValueFromQueryResult(randomPCWithPWSSponsorList, "URL");
		sponsorsPWS = sponsorsPWS.toLowerCase();

		storeFrontHomePage = new StoreFrontHomePage(driver);
		storeFrontHomePage.openConsultantPWS(sponsorsPWS);
		storeFrontConsulatantPage = storeFrontHomePage.loginAsConsultant(pcEmailID, password);
		s_assert.assertTrue(storeFrontConsulatantPage.getCurrentURL().toLowerCase().contains(sponsorsPWS.toLowerCase()),"PC is not on Sponsor's PWS");
		
		s_assert.assertAll();
	}


	//Hybris Phase 2-4400 PC, whose Sponsor has PWS - Not Sponsor’s PWS - Sponsor’s PWS
	@Test
	public void testPreferredCustomerWithPWSSponsorLoginFromOtherSponsorsPWS_4400() throws InterruptedException{
		RFL_DB = driver.getDBNameRFL();		
		List<Map<String, Object>> randomPCWithPWSSponsorList =  null;
		List<Map<String, Object>> otherPWSList =  null;
		String pcEmailID = null;
		String sponsorsPWS = null;
		String otherPWS = null;		

		randomPCWithPWSSponsorList = DBUtil.performDatabaseQuery(DBQueries_RFL.GET_RANDOM_PC_WHOSE_SPONSOR_HAS_PWS,RFL_DB);
		pcEmailID = (String) getValueFromQueryResult(randomPCWithPWSSponsorList, "UserName");

		//sponsorPWSList = DBUtil.performDatabaseQuery(,RFL_DB);
		sponsorsPWS = (String) getValueFromQueryResult(randomPCWithPWSSponsorList, "URL");
		sponsorsPWS = sponsorsPWS.toLowerCase();
		otherPWSList = DBUtil.performDatabaseQuery(DBQueries_RFL.GET_RANDOM_CONSULTANT_WITH_PWS_RFL,RFL_DB);
		otherPWS = (String) getValueFromQueryResult(otherPWSList, "URL");
		otherPWS = otherPWS.toLowerCase();
		storeFrontHomePage = new StoreFrontHomePage(driver);
		storeFrontHomePage.openConsultantPWS(otherPWS);
		storeFrontConsulatantPage = storeFrontHomePage.loginAsConsultant(pcEmailID, password);
		s_assert.assertTrue(storeFrontConsulatantPage.getCurrentURL().toLowerCase().contains(sponsorsPWS),"PC is not on Sponsor's PWS");
		
		s_assert.assertAll();
	}


	//Hybris Phase 2-4401 PC, whose Sponsor has PWS - Corporate Site - Sponsor’s PWS
	@Test
	public void testPreferredCustomerWithPWSSponsorLoginFromCorp_4401() throws InterruptedException{
		RFL_DB = driver.getDBNameRFL();		
		List<Map<String, Object>> randomPCWithPWSSponsorList =  null;
		List<Map<String, Object>> sponsorPWSList =  null;
		String pcEmailID = null;
		String sponsorsPWS = null;

		randomPCWithPWSSponsorList = DBUtil.performDatabaseQuery(DBQueries_RFL.GET_RANDOM_PC_WHOSE_SPONSOR_HAS_PWS,RFL_DB);
		pcEmailID = (String) getValueFromQueryResult(randomPCWithPWSSponsorList, "UserName");

		//sponsorPWSList = DBUtil.performDatabaseQuery(,RFL_DB);
		sponsorsPWS = (String) getValueFromQueryResult(randomPCWithPWSSponsorList, "URL");
		sponsorsPWS = sponsorsPWS.toLowerCase();
		storeFrontHomePage = new StoreFrontHomePage(driver);
		storeFrontConsulatantPage = storeFrontHomePage.loginAsConsultant(pcEmailID, password);
		s_assert.assertTrue(storeFrontConsulatantPage.getCurrentURL().toLowerCase().contains(sponsorsPWS),"PC is not on Sponsor's PWS");
		
		s_assert.assertAll();
	}



	//Hybris Phase 2-4402 PC, whose Sponsor has No PWS - someone's PWS - Corporate Site
	@Test(enabled=false)
	public void testPreferredCustomerWithNoPWSSponsorLoginFromOtherPWS_4402() throws InterruptedException{
		RFL_DB = driver.getDBNameRFL();		
		List<Map<String, Object>> randomPCWithSponsorNoPWSList =  null;
		List<Map<String, Object>> otherPWSList =  null;
		String pcEmailID = null;
		String otherPWS = null;		
		otherPWS = otherPWS.toLowerCase();
		randomPCWithSponsorNoPWSList = DBUtil.performDatabaseQuery(DBQueries_RFL.GET_RANDOM_PC_WHOSE_SPONSOR_HAS_NOPWS,RFL_DB);
		pcEmailID = (String) getValueFromQueryResult(randomPCWithSponsorNoPWSList, "UserName");

		otherPWSList = DBUtil.performDatabaseQuery(DBQueries_RFL.GET_RANDOM_CONSULTANT_WITH_PWS_RFL,RFL_DB);
		otherPWS = (String) getValueFromQueryResult(otherPWSList, "URL");
		otherPWS = otherPWS.toLowerCase();
		storeFrontHomePage = new StoreFrontHomePage(driver);
		storeFrontHomePage.openConsultantPWS(otherPWS);
		storeFrontConsulatantPage = storeFrontHomePage.loginAsConsultant(pcEmailID, password);
		s_assert.assertTrue(storeFrontConsulatantPage.getCurrentURL().toLowerCase().contains(driver.getURL().toLowerCase()),"PC is not on Corporate");
		
		s_assert.assertAll();
	}


	//Hybris Phase 2-4403 PC, whose Sponsor has No PWS - Corporate Site - Corporate Site
	@Test(enabled=false)
	public void testPreferredCustomerWithNoPWSSponsorLoginFromCorp_4403() throws InterruptedException{
		RFL_DB = driver.getDBNameRFL();		
		List<Map<String, Object>> randomPCWithSponsorNoPWSList =  null;
		String pcEmailID = null;

		randomPCWithSponsorNoPWSList = DBUtil.performDatabaseQuery(DBQueries_RFL.GET_RANDOM_PC_WHOSE_SPONSOR_HAS_NOPWS,RFL_DB);
		pcEmailID = (String) getValueFromQueryResult(randomPCWithSponsorNoPWSList, "UserName");

		storeFrontHomePage = new StoreFrontHomePage(driver);
		storeFrontConsulatantPage = storeFrontHomePage.loginAsConsultant(pcEmailID, password);
		s_assert.assertTrue(storeFrontConsulatantPage.getCurrentURL().toLowerCase().contains(driver.getURL().toLowerCase()),"PC is not on Corporate");
		
		s_assert.assertAll();

	}


	//Hybris Phase 2-4404 PC, with no sponsor - someone's site - Corporate Site 
	@Test
	public void testPreferredCustomerNoSponsorLoginFromOthersPWS_4404() throws InterruptedException{
		RFL_DB = driver.getDBNameRFL();		
		List<Map<String, Object>> randomPCWithNoSponsorList =  null;
		List<Map<String, Object>> otherPWSList =  null;
		String pcEmailID = null;
		String otherPWS = null;		

		randomPCWithNoSponsorList = DBUtil.performDatabaseQuery(DBQueries_RFL.GET_RANDOM_PC_WITH_NO_SPONSOR,RFL_DB);
		pcEmailID = (String) getValueFromQueryResult(randomPCWithNoSponsorList, "UserName");

		otherPWSList = DBUtil.performDatabaseQuery(DBQueries_RFL.GET_RANDOM_CONSULTANT_WITH_PWS_RFL,RFL_DB);
		otherPWS = (String) getValueFromQueryResult(otherPWSList, "URL");
		otherPWS = otherPWS.toLowerCase();
		storeFrontHomePage = new StoreFrontHomePage(driver);
		storeFrontHomePage.openConsultantPWS(otherPWS);
		storeFrontConsulatantPage = storeFrontHomePage.loginAsConsultant(pcEmailID, password);
		s_assert.assertTrue(storeFrontConsulatantPage.getCurrentURL().toLowerCase().contains(driver.getURL().toLowerCase()),"PC is not on Corporate");
		
		s_assert.assertAll();

	}

	//Hybris Phase 2-4405 PC, with no sponsor - corporate site - Corporate Site
	@Test
	public void testPreferredCustomerNoSponsorLoginFromCorp_4405() throws InterruptedException{
		RFL_DB = driver.getDBNameRFL();		
		List<Map<String, Object>> randomPCWithNoSponsorList =  null;
		String pcEmailID = null;

		randomPCWithNoSponsorList = DBUtil.performDatabaseQuery(DBQueries_RFL.GET_RANDOM_PC_WITH_NO_SPONSOR,RFL_DB);
		pcEmailID = (String) getValueFromQueryResult(randomPCWithNoSponsorList, "UserName");

		storeFrontHomePage = new StoreFrontHomePage(driver);
		storeFrontConsulatantPage = storeFrontHomePage.loginAsConsultant(pcEmailID, password);
		s_assert.assertTrue(storeFrontConsulatantPage.getCurrentURL().toLowerCase().contains(driver.getURL().toLowerCase()),"PC is not on Corporate");
		
		s_assert.assertAll();
	}

}


