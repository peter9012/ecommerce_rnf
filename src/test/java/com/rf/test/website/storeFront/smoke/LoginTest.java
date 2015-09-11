package com.rf.test.website.storeFront.smoke;

import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.annotations.Test;
import org.apache.commons.lang3.RandomStringUtils;

import com.rf.core.utils.DBUtil;
import com.rf.core.website.constants.TestConstants;
import com.rf.core.website.constants.dbQueries.DBQueries_RFL;
import com.rf.pages.website.StoreFrontConsultantPage;
import com.rf.pages.website.StoreFrontHomePage;
import com.rf.test.website.RFWebsiteBaseTest;


public class LoginTest extends RFWebsiteBaseTest{
	private static final Logger logger = LogManager
			.getLogger(LoginTest.class.getName());
	private StoreFrontHomePage storeFrontHomePage;
	private StoreFrontConsultantPage storeFrontConsultantPage;
	private String RFL_DB = null;
	public String emailID = null;
	
	@Test//(dataProvider="rfTestData")
	public void testLogin(String accountID,String emailID,String accNumber,String sid,String name, String hasOrder ,String sourceName) throws InterruptedException{
//		RFL_DB = driver.getDBNameRFL();
//		logger.info("AccountID= "+accountID);
//		List<Map<String, Object>> emailIdList =  null;
//		String emailID = null;
//		emailIdList = DBUtil.performDatabaseQuery(DBQueries_RFL.callQueryWithArguement(DBQueries_RFL.GET_EMAILID_FROM_ACCOUNTID,accountID),RFL_DB);
//		emailID = (String) getValueFromQueryResult(emailIdList, "EmailAddress");
		String password = "111maiden";
		logger.info("EmailID= "+emailID);
		storeFrontHomePage = new StoreFrontHomePage(driver);
		storeFrontConsultantPage = storeFrontHomePage.loginAsConsultant(emailID, password);
		assertFalse(storeFrontConsultantPage.getCurrentURL().toLowerCase().contains("error=true")||storeFrontConsultantPage.getCurrentURL().toLowerCase().contains("sitenotfound"),"login failed for the user "+emailID+" with accountID = "+accountID+" url is "+storeFrontConsultantPage.getCurrentURL());//
		assertTrue(storeFrontConsultantPage.verifyConsultantPage(),"Consultant Page doesn't contain Welcome User Message");
	}
	
	
	@Test (invocationCount = 1)
	public void testLoginDynamic() throws InterruptedException {//String accountID,String emailID,String accNumber,String sid,String name, String hasOrder ,String sourceName) throws InterruptedException{
		String sRandNum = RandomStringUtils.randomNumeric(5);
		System.out.println(sRandNum);
		//***** HARD CODED FOR US AT PRESENT *****
		String sQuery="select top "+sRandNum+" emailaddress FROM  RFO_Accounts.vw_GetAccount_Reporting vgar WITH (NOEXPAND ) JOIN Hybris.Sites s ON SponsorId = s.AccountID WHERE   vgar.active= 1 AND SoftTerminationDate IS NULL AND HardTerminationDate IS NULL AND CountryID = 236 AND s.SitePrefix IS NOT NULL AND s.Active IS NULL";
	    List<Map<String, Object>> sEmail = DBUtil.performDatabaseQuery(sQuery, "RFOperations");
	 	emailID = (String) getValueFromQueryResult(sEmail, "EmailAddress");
		    System.out.println (emailID);
		    System.out.println(" ");
		   
		String password = TestConstants.PASSWORD;
		storeFrontHomePage = new StoreFrontHomePage(driver);
		storeFrontConsultantPage = storeFrontHomePage.loginAsConsultant(emailID,password);
		assertFalse(storeFrontConsultantPage.getCurrentURL().toLowerCase().contains("error=true")||storeFrontConsultantPage.getCurrentURL().toLowerCase().contains("sitenotfound"),"login failed for the user "+emailID+" url is "+storeFrontConsultantPage.getCurrentURL());//
		assertTrue(storeFrontConsultantPage.verifyConsultantPage(),"Consultant Page doesn't contain Welcome User Message");
		
	}
}