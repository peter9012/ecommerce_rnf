package com.rf.test.website.storeFront;

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
	private String RFL_DB = null;
	
	//Consultant with PWS - Corporate site - Her own .com PWS
	@Test(enabled=false)
	public void testConsultantWithPWSLoginFromCorp(){
		RFL_DB = driver.getDBNameRFL();		
		List<Map<String, Object>> randomConsultantList =  null;
		List<Map<String, Object>> randomConsultantPWSList =  null;
		String consultantWithPWSEmailID = null;
		String consultantPWSURL = null;

		// Get Consultant with PWS from database
		randomConsultantList = DBUtil.performDatabaseQuery(DBQueries.GET_RANDOM_CONSULTANT_WITH_PWS_RFL,RFL_DB);
		consultantWithPWSEmailID = (String) getValueFromQueryResult(randomConsultantList, "EmailAddress");

		// Get consultant's PWS from database.
		//	randomConsultantPWSList = DBUtil.performDatabaseQuery(,RFL_DB);
		consultantPWSURL = (String) getValueFromQueryResult(randomConsultantPWSList, "URL");

		storeFrontConsulatantPage = storeFrontHomePage.loginAsConsultant(consultantWithPWSEmailID, TestConstants.CONSULTANT_PASSWORD_TST4);
		s_assert.assertTrue(!consultantPWSURL.contains(".biz"),"Consultant is not on her own .com PWS");
		logout();	
		s_assert.assertAll();

	}

	//Consultant with PWS - Her own PWS - Her own PWS
	@Test(enabled=false)
	public void testConsultantWithPWSLoginFromOwnPWS(){
		RFL_DB = driver.getDBNameRFL();		
		List<Map<String, Object>> randomConsultantList =  null;
		List<Map<String, Object>> randomConsultantPWSList =  null;
		String consultantWithPWSEmailID = null;
		String consultantPWSURL = null;

		// Get Consultant with PWS from database
		randomConsultantList = DBUtil.performDatabaseQuery(DBQueries.GET_RANDOM_CONSULTANT_WITH_PWS_RFL,RFL_DB);
		consultantWithPWSEmailID = (String) getValueFromQueryResult(randomConsultantList, "EmailAddress");

		// Get consultant's PWS from database.
		//	randomConsultantPWSList = DBUtil.performDatabaseQuery(,RFL_DB);
		consultantPWSURL = (String) getValueFromQueryResult(randomConsultantPWSList, "URL");

		storeFrontHomePage.openConsultantPWS(consultantPWSURL);
		storeFrontConsulatantPage = storeFrontHomePage.loginAsConsultant(consultantWithPWSEmailID, TestConstants.CONSULTANT_PASSWORD_TST4);
		s_assert.assertTrue(storeFrontConsulatantPage.getCurrentURL().contains(consultantPWSURL),"Consultant is not on her own PWS");
		logout();	
		s_assert.assertAll();			
	}

	//Consultant with PWS -Someone else’s PWS - Her own PW
	@Test(enabled=false)
	public void testConsultantWithPWSLoginFromOthersPWS(){
		RFL_DB = driver.getDBNameRFL();		
		List<Map<String, Object>> randomConsultantList =  null;
		List<Map<String, Object>> randomConsultantPWSList =  null;
		List<Map<String, Object>> randomOtherPWSList =  null;
		String consultantWithPWSEmailID = null;
		String consultantPWSURL = null;
		String otherPWSURL = null;

		// Get Consultant with PWS from database
		randomConsultantList = DBUtil.performDatabaseQuery(DBQueries.GET_RANDOM_CONSULTANT_WITH_PWS_RFL,RFL_DB);
		consultantWithPWSEmailID = (String) getValueFromQueryResult(randomConsultantList, "EmailAddress");

		// Get consultant's PWS from database.
		//	randomConsultantPWSList = DBUtil.performDatabaseQuery(,RFL_DB);
		consultantPWSURL = (String) getValueFromQueryResult(randomConsultantPWSList, "URL");

		// Get another PWS from database
		//randomOtherPWSList = DBUtil.performDatabaseQuery(,RFL_DB);
		otherPWSURL = (String) getValueFromQueryResult(randomOtherPWSList, "URL");

		storeFrontHomePage.openConsultantPWS(otherPWSURL);
		storeFrontConsulatantPage = storeFrontHomePage.loginAsConsultant(consultantWithPWSEmailID, TestConstants.CONSULTANT_PASSWORD_TST4);
		s_assert.assertTrue(storeFrontConsulatantPage.getCurrentURL().contains(consultantPWSURL),"Consultant is not on her own PWS");
		logout();	
		s_assert.assertAll();
	}


	//Consultant W/O PWS - Corporate site - Corporate site
	@Test(enabled=false)
	public void testConsultantWithoutPWSLoginFromCorp(){
		RFL_DB = driver.getDBNameRFL();		
		List<Map<String, Object>> randomConsultantList =  null;
		String consultantEmailID = null;
		randomConsultantList = DBUtil.performDatabaseQuery(DBQueries.GET_RANDOM_CONSULTANT_NO_PWS_RFL,RFL_DB);
		consultantEmailID = (String) getValueFromQueryResult(randomConsultantList, "EmailAddress");
		storeFrontHomePage = new StoreFrontHomePage(driver);
		storeFrontConsulatantPage = storeFrontHomePage.loginAsConsultant(consultantEmailID, TestConstants.CONSULTANT_PASSWORD_TST4);
		s_assert.assertTrue(storeFrontConsulatantPage.getCurrentURL().contains(driver.getURL()),"Consultant is not on corporate site");
		logout();
		s_assert.assertAll();		
	}


	//Consultant W/O PWS - Someone else’s PWS - Corporate site
	@Test(enabled=false)
	public void testConsultantWithoutPWSLoginFromOthersPWS(){
		RFL_DB = driver.getDBNameRFL();		
		List<Map<String, Object>> randomConsultantList =  null;
		List<Map<String, Object>> randomOtherPWSList =  null;
		String consultantEmailID = null;
		String otherPWSURL = null;

		// get consultant without PWS email Id from database
		randomConsultantList = DBUtil.performDatabaseQuery(DBQueries.GET_RANDOM_CONSULTANT_NO_PWS_RFL,RFL_DB);
		consultantEmailID = (String) getValueFromQueryResult(randomConsultantList, "EmailAddress");

		// Get another PWS from database
		//randomOtherPWSList = DBUtil.performDatabaseQuery(,RFL_DB);
		otherPWSURL = (String) getValueFromQueryResult(randomOtherPWSList, "URL");

		storeFrontHomePage = new StoreFrontHomePage(driver);
		storeFrontHomePage.openConsultantPWS(otherPWSURL);
		storeFrontConsulatantPage = storeFrontHomePage.loginAsConsultant(consultantEmailID, TestConstants.CONSULTANT_PASSWORD_TST4);		
		s_assert.assertTrue(storeFrontConsulatantPage.getCurrentURL().contains(driver.getURL()),"Consultant is not on corporate site");
		logout();
		s_assert.assertAll();				
	}


	//RC - someone's PWS - someone's PWS
	@Test(enabled=false)
	public void testRetailCustomerLoginFromOthersPWS(){
		RFL_DB = driver.getDBNameRFL();		
		List<Map<String, Object>> randomRCList =  null;
		List<Map<String, Object>> randomOtherPWSList =  null;
		String rcEmailID = null;
		String otherPWSURL = null;

		randomRCList = DBUtil.performDatabaseQuery(DBQueries.GET_RANDOM_RC_RFL,RFL_DB);
		rcEmailID = (String) getValueFromQueryResult(randomRCList, "EmailAddress");

		// Get another PWS from database
		//randomOtherPWSList = DBUtil.performDatabaseQuery(,RFL_DB);
		otherPWSURL = (String) getValueFromQueryResult(randomOtherPWSList, "URL");

		storeFrontHomePage = new StoreFrontHomePage(driver);
		storeFrontHomePage.openConsultantPWS(otherPWSURL);
		storeFrontConsulatantPage = storeFrontHomePage.loginAsConsultant(rcEmailID, TestConstants.RC_PASSWORD_TST4);
		s_assert.assertTrue(storeFrontConsulatantPage.getCurrentURL().contains(otherPWSURL),"RC is not on someone's PWS");
		logout();
		s_assert.assertAll();		
	}


	//RC - corporate site - Corporate Site
	@Test
	public void testRetailCustomerLoginFromCorp(){
		RFL_DB = driver.getDBNameRFL();		
		List<Map<String, Object>> randomRCList =  null;
		String rcEmailID = null;

		randomRCList = DBUtil.performDatabaseQuery(DBQueries.GET_RANDOM_RC_RFL,RFL_DB);
		rcEmailID = (String) getValueFromQueryResult(randomRCList, "EmailAddress");

		storeFrontHomePage = new StoreFrontHomePage(driver);
		storeFrontConsulatantPage = storeFrontHomePage.loginAsConsultant(rcEmailID, TestConstants.RC_PASSWORD_TST4);
		s_assert.assertTrue(storeFrontConsulatantPage.getCurrentURL().contains(driver.getURL()),"RC is not on corporate site");
		logout();
		s_assert.assertAll();				
	}



	//PC, whose Sponsor has PWS - Sponsor’s PWS - Sponsor’s PWS
	@Test(enabled=false)
	public void testPreferredCustomerWithPWSSponsorLoginFromSponsorsPWS(){
		RFL_DB = driver.getDBNameRFL();		
		List<Map<String, Object>> randomPCWithPWSSponsorList =  null;
		List<Map<String, Object>> sponsorPWSList =  null;
		String pcEmailID = null;
		String sponsorsPWS = null;

		//randomPCWithSponsorList = DBUtil.performDatabaseQuery(,RFL_DB);
		pcEmailID = (String) getValueFromQueryResult(randomPCWithPWSSponsorList, "EmailAddress");

		//sponsorPWSList = DBUtil.performDatabaseQuery(,RFL_DB);
		sponsorsPWS = (String) getValueFromQueryResult(sponsorPWSList, "URL");

		storeFrontHomePage = new StoreFrontHomePage(driver);
		storeFrontHomePage.openConsultantPWS(sponsorsPWS);
		storeFrontConsulatantPage = storeFrontHomePage.loginAsConsultant(pcEmailID, TestConstants.PC_PASSWORD_TST4);
		s_assert.assertTrue(storeFrontConsulatantPage.getCurrentURL().contains(sponsorsPWS),"PC is not on Sponsor's PWS");
		logout();
		s_assert.assertAll();
	}


	//PC, whose Sponsor has PWS - Not Sponsor’s PWS - Sponsor’s PWS
	@Test(enabled=false)
	public void testPreferredCustomerWithPWSSponsorLoginFromOtherSponsorsPWS(){
		RFL_DB = driver.getDBNameRFL();		
		List<Map<String, Object>> randomPCWithPWSSponsorList =  null;
		List<Map<String, Object>> otherPWSList =  null;
		List<Map<String, Object>> sponsorPWSList =  null;
		String pcEmailID = null;
		String sponsorsPWS = null;
		String otherPWS = null;		

		//randomPCWithSponsorList = DBUtil.performDatabaseQuery(,RFL_DB);
		pcEmailID = (String) getValueFromQueryResult(randomPCWithPWSSponsorList, "EmailAddress");

		//sponsorPWSList = DBUtil.performDatabaseQuery(,RFL_DB);
		sponsorsPWS = (String) getValueFromQueryResult(sponsorPWSList, "URL");

		//otherPWSList = DBUtil.performDatabaseQuery(,RFL_DB);
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
	public void testPreferredCustomerWithPWSSponsorLoginFromCorp(){
		RFL_DB = driver.getDBNameRFL();		
		List<Map<String, Object>> randomPCWithPWSSponsorList =  null;
		List<Map<String, Object>> sponsorPWSList =  null;
		String pcEmailID = null;
		String sponsorsPWS = null;

		//randomPCWithSponsorList = DBUtil.performDatabaseQuery(,RFL_DB);
		pcEmailID = (String) getValueFromQueryResult(randomPCWithPWSSponsorList, "EmailAddress");

		//sponsorPWSList = DBUtil.performDatabaseQuery(,RFL_DB);
		sponsorsPWS = (String) getValueFromQueryResult(sponsorPWSList, "URL");

		storeFrontHomePage = new StoreFrontHomePage(driver);
		storeFrontConsulatantPage = storeFrontHomePage.loginAsConsultant(pcEmailID, TestConstants.PC_PASSWORD_TST4);
		s_assert.assertTrue(storeFrontConsulatantPage.getCurrentURL().contains(sponsorsPWS),"PC is not on Sponsor's PWS");
		logout();
		s_assert.assertAll();
	}



	//PC, whose Sponsor has No PWS - someone's PWS - Corporate Site
	@Test(enabled=false)
	public void testPreferredCustomerWithNoPWSSponsorLoginFromOtherPWS(){
		RFL_DB = driver.getDBNameRFL();		
		List<Map<String, Object>> randomPCWithSponsorNoPWSList =  null;
		List<Map<String, Object>> otherPWSList =  null;
		String pcEmailID = null;
		String otherPWS = null;		

		//randomPCWithNoSponsorList = DBUtil.performDatabaseQuery(,RFL_DB);
		pcEmailID = (String) getValueFromQueryResult(randomPCWithSponsorNoPWSList, "EmailAddress");

		//otherPWSList = DBUtil.performDatabaseQuery(,RFL_DB);
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
	public void testPreferredCustomerWithNoPWSSponsorLoginFromCorp(){
		RFL_DB = driver.getDBNameRFL();		
		List<Map<String, Object>> randomPCWithSponsorNoPWSList =  null;
		String pcEmailID = null;
			
		//randomPCWithNoSponsorList = DBUtil.performDatabaseQuery(,RFL_DB);
		pcEmailID = (String) getValueFromQueryResult(randomPCWithSponsorNoPWSList, "EmailAddress");

		storeFrontHomePage = new StoreFrontHomePage(driver);
		storeFrontConsulatantPage = storeFrontHomePage.loginAsConsultant(pcEmailID, TestConstants.PC_PASSWORD_TST4);
		s_assert.assertTrue(storeFrontConsulatantPage.getCurrentURL().contains(driver.getURL()),"PC is not on Corporate");
		logout();
		s_assert.assertAll();

	}


	//PC, with no sponsor - someone's site - Corporate Site 
	@Test(enabled=false)
	public void testPreferredCustomerNoSponsorLoginFromOthersPWS(){
		RFL_DB = driver.getDBNameRFL();		
		List<Map<String, Object>> randomPCWithNoSponsorList =  null;
		List<Map<String, Object>> otherPWSList =  null;
		String pcEmailID = null;
		String otherPWS = null;		

		//randomPCWithNoSponsorList = DBUtil.performDatabaseQuery(,RFL_DB);
		pcEmailID = (String) getValueFromQueryResult(randomPCWithNoSponsorList, "EmailAddress");

		//otherPWSList = DBUtil.performDatabaseQuery(,RFL_DB);
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
	public void testPreferredCustomerNoSponsorLoginFromCorp(){
		RFL_DB = driver.getDBNameRFL();		
		List<Map<String, Object>> randomPCWithNoSponsorList =  null;
		String pcEmailID = null;
		
		//randomPCWithNoSponsorList = DBUtil.performDatabaseQuery(,RFL_DB);
		pcEmailID = (String) getValueFromQueryResult(randomPCWithNoSponsorList, "EmailAddress");

		storeFrontHomePage = new StoreFrontHomePage(driver);
		storeFrontConsulatantPage = storeFrontHomePage.loginAsConsultant(pcEmailID, TestConstants.PC_PASSWORD_TST4);
		s_assert.assertTrue(storeFrontConsulatantPage.getCurrentURL().contains(driver.getURL()),"PC is not on Corporate");
		logout();
		s_assert.assertAll();
	}

}


