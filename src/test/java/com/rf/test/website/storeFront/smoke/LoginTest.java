/**
 * This code is just for the testing purpose for the automation team and doesn't belongs to any of the test case,so marked as commented 
 */



//package com.rf.test.website.storeFront.smoke;
//
//import java.lang.reflect.Method;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Map;
//
//import org.apache.logging.log4j.LogManager;
//import org.apache.logging.log4j.Logger;
//import org.testng.annotations.DataProvider;
//import org.testng.annotations.Test;
//
//import com.rf.core.utils.DBUtil;
//import com.rf.core.website.constants.dbQueries.DBQueries_RFO;
//import com.rf.pages.website.StoreFrontConsultantPage;
//import com.rf.pages.website.StoreFrontHomePage;
//import com.rf.test.website.RFWebsiteBaseTest;
//
//public class LoginTest extends RFWebsiteBaseTest{
//	private static final Logger logger = LogManager
//			.getLogger(LoginTest.class.getName());
//	private StoreFrontHomePage storeFrontHomePage;
//	private StoreFrontConsultantPage storeFrontConsultantPage;
//	private String RFO_DB = null;
//
//	@Test(dataProvider="rfTestDataQuery")
//	public void testLogin(String emailID) throws InterruptedException{
//		
//		//	logger.info("AccountID= "+accountID);
//
//		String password = "111maiden";
//
//		logger.info("EmailID= "+emailID);
//		storeFrontHomePage = new StoreFrontHomePage(driver);
//		storeFrontConsultantPage = storeFrontHomePage.loginAsConsultant(emailID, password);
//		assertFalse(storeFrontConsultantPage.getCurrentURL().toLowerCase().contains("error=true")||storeFrontConsultantPage.getCurrentURL().toLowerCase().contains("sitenotfound"),"login failed for the user "+emailID+" url is "+storeFrontConsultantPage.getCurrentURL());
//		if((storeFrontConsultantPage.getCurrentURL().toLowerCase().contains("j_spring_security_check"))==true){
//			logger.info("ERROR !! SECURITY CHECK ERROR");
//			storeFrontHomePage.navigateToBackPage();
//		}
//		assertTrue(storeFrontConsultantPage.verifyConsultantPage(),"Consultant Page doesn't contain Welcome User Message");
//
//	}	
//
//	@DataProvider(name = "rfTestDataQuery")
//	public Object[][] rfDataProviderQuery(Method testMethod) throws Exception {
//		RFO_DB = driver.getDBNameRFO();
//		List<Map<String, Object>> emailIdList =  null;
//		List<String> emailAddressList = new ArrayList<String>();
//		int i=0;
//		//String emailID = null;
//		emailIdList = DBUtil.performDatabaseQuery(DBQueries_RFO.GET_RANDOM_100_USERS_RFO,RFO_DB);
//		emailAddressList = getValuesFromQueryResult(emailIdList, "EmailAddress");
//		
//		Object[][] testObjArray = new Object[emailAddressList.size()][1];
//		for(String emailId : emailAddressList){
//			testObjArray[i][0]=emailId;
//			logger.info("EmailId from RFO database is "+emailId);
//			System.out.println(emailId);
//			i++;
//		}
//		return (testObjArray);
//
//	}
//
//}