package com.rf.test.website.storeFront.smoke;

import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.annotations.Test;

import com.rf.core.utils.DBUtil;
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

	@Test(dataProvider="rfTestData")
	public void testLogin(String accountID,String fName,String mName,String lName,String password, String customerType ,String accountType,String active, String asignedUsers) throws InterruptedException{
		RFL_DB = driver.getDBNameRFL();
		logger.info("AccountID= "+accountID);
		List<Map<String, Object>> emailIdList =  null;
		String emailID = null;
		emailIdList = DBUtil.performDatabaseQuery(DBQueries_RFL.callQueryWithArguement(DBQueries_RFL.GET_EMAILID_FROM_ACCOUNTID,accountID),RFL_DB);
		emailID = (String) getValueFromQueryResult(emailIdList, "EmailAddress");
		logger.info("EmailID= "+emailID);
		storeFrontHomePage = new StoreFrontHomePage(driver);
		storeFrontConsultantPage = storeFrontHomePage.loginAsConsultant(emailID, password);
		assertFalse(storeFrontConsultantPage.getCurrentURL().toLowerCase().contains("error=true")||storeFrontConsultantPage.getCurrentURL().toLowerCase().contains("sitenotfound"),"login failed for the user "+emailID+" with accountID = "+accountID+" url is "+storeFrontConsultantPage.getCurrentURL());//
		
	}
}