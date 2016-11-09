package com.rf.test.website.crm;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.rf.core.utils.CommonUtils;
import com.rf.core.utils.DBUtil;
import com.rf.core.website.constants.TestConstants;
import com.rf.core.website.constants.dbQueries.DBQueries_RFO;
import com.rf.pages.website.crm.CRMAccountDetailsPage;
import com.rf.pages.website.crm.CRMContactDetailsPage;
import com.rf.pages.website.crm.CRMHomePage;
import com.rf.pages.website.crm.CRMLoginPage;
import com.rf.pages.website.storeFront.StoreFrontHomePage;
import com.rf.test.website.RFCRMWebsiteBaseTest;

public class LogisticTeamTest extends RFCRMWebsiteBaseTest{
	private static final Logger logger = LogManager
			.getLogger(CRMRegressionTest.class.getName());

	private CRMLoginPage crmLoginpage;
	private CRMHomePage crmHomePage;
	private CRMAccountDetailsPage crmAccountDetailsPage; 
	private CRMContactDetailsPage crmContactDetailsPage;
	private StoreFrontHomePage storeFrontHomePage;

	private String RFO_DB = null;
	String consultantEmailID = null;
	String pcUserName = null;
	String accountID = null;
	String rcUserName = null;
	String pcAccountID = null;
	String rcAccountID = null;
	List<Map<String, Object>> randomConsultantList =  null;
	List<Map<String, Object>> randomConsultantUsernameList =  null;
	List<Map<String, Object>> randomPCUserList =  null;
	List<Map<String, Object>> randomRCUserList =  null;
	List<Map<String, Object>> randomPCUsernameList =  null;
	List<Map<String, Object>> randomRCUsernameList =  null;

	@BeforeClass
	public void BeforeCRM(){
		RFO_DB = driver.getDBNameRFO();
		crmLoginpage = new CRMLoginPage(driver);
		crmHomePage = new CRMHomePage(driver);
		crmAccountDetailsPage = new CRMAccountDetailsPage(driver);
		crmContactDetailsPage = new CRMContactDetailsPage(driver);
		storeFrontHomePage = new StoreFrontHomePage(driver);
		randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_CONSULTANT_WITH_AUTOSHIPS_RFO,countryId),RFO_DB);
		consultantEmailID = (String) getValueFromQueryResult(randomConsultantList, "UserName");
		accountID = String.valueOf(getValueFromQueryResult(randomConsultantList, "AccountID"));

		randomConsultantUsernameList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_EMAIL_ID_FROM_ACCOUNT_ID,accountID),RFO_DB);
		consultantEmailID = String.valueOf(getValueFromQueryResult(randomConsultantUsernameList, "EmailAddress"));

		randomPCUserList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_PC_WITH_AUTOSHIPS_RFO,countryId),RFO_DB);
		pcUserName = (String) getValueFromQueryResult(randomPCUserList, "UserName");
		pcAccountID = String.valueOf(getValueFromQueryResult(randomPCUserList, "AccountID"));

		randomPCUsernameList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_EMAIL_ID_FROM_ACCOUNT_ID,pcAccountID),RFO_DB);
		pcUserName = String.valueOf(getValueFromQueryResult(randomPCUsernameList, "EmailAddress"));

		randomRCUserList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_RC_RFO,countryId),RFO_DB);
		rcUserName = (String) getValueFromQueryResult(randomRCUserList, "UserName");
		rcAccountID = String.valueOf(getValueFromQueryResult(randomRCUserList, "AccountID"));

		randomRCUsernameList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_EMAIL_ID_FROM_ACCOUNT_ID,rcAccountID),RFO_DB);
		rcUserName = String.valueOf(getValueFromQueryResult(randomRCUsernameList, "EmailAddress"));
	}	


	// RF Saleforce CRM-167:Log In As A Logistics Agent
	@Test
	public void testLoginAsALogisticsAgent() throws InterruptedException{
		crmLoginpage.crmLogout();
		crmLoginpage.loginLogisticsUser(TestConstants.CRM_LOGIN_LOGISTICS_USERNAME, TestConstants.CRM_LOGIN_LOGISTICS_PASSWORD);
		int randomNum = CommonUtils.getRandomNum(10000, 1000000);
		String orderNote="This is automation note"+randomNum;
		logger.info("The username is "+consultantEmailID); 
		s_assert.assertTrue(crmHomePage.verifyHomePage(),"Home page does not come after login");
		crmHomePage.enterTextInSearchFieldAndHitEnter(consultantEmailID);
		while(true){
			if(crmHomePage.isSearchResultHasActiveUser("Consultant") ==false || crmHomePage.isAccountSectionPresent()==false){
				logger.info("No active user in the search results..searching new user");
				randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_CONSULTANT_WITH_AUTOSHIPS_RFO,countryId),RFO_DB);
				accountID = String.valueOf(getValueFromQueryResult(randomConsultantList, "AccountID"));
				randomConsultantUsernameList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_EMAIL_ID_FROM_ACCOUNT_ID,accountID),RFO_DB);
				consultantEmailID = String.valueOf(getValueFromQueryResult(randomConsultantUsernameList, "EmailAddress"));

				logger.info("The email address is "+consultantEmailID);
				s_assert.assertTrue(crmHomePage.verifyHomePage(),"Home page does not come after login");
				crmHomePage.enterTextInSearchFieldAndHitEnter(consultantEmailID);
			}else{
				break;
			}
		}
		String emailOnfirstRow = crmHomePage.getEmailOnFirstRowInSearchResults();
		//s_assert.assertTrue(emailOnfirstRow.toLowerCase().trim().contains(consultantEmailID.toLowerCase().trim()) || consultantEmailID.toLowerCase().trim().contains(emailOnfirstRow.toLowerCase().trim()), "the email on first row which is = "+emailOnfirstRow.toLowerCase().trim()+" is expected to contain email = "+consultantEmailID.toLowerCase().trim());

		crmHomePage.clickAnyTypeOfActiveCustomerInSearchResult("Consultant");
		//Verify account overview page
		s_assert.assertTrue(crmAccountDetailsPage.isAccountDetailsPagePresent(),"Account Details page has not displayed");
		s_assert.assertAll();
	}

	// RF Saleforce CRM-170 Verify Contact details page for Consultant/PC/RC for CA/US
	@Test
	public void verifyLoginAsALogisticsAgentContactDetailsPageForConsultant() throws InterruptedException{
		crmLoginpage.crmLogout();
		crmLoginpage.loginLogisticsUser(TestConstants.CRM_LOGIN_LOGISTICS_USERNAME, TestConstants.CRM_LOGIN_LOGISTICS_PASSWORD);
		int randomNum = CommonUtils.getRandomNum(10000, 1000000);
		String orderNote="This is automation note"+randomNum;
		logger.info("The username is "+consultantEmailID); 
		s_assert.assertTrue(crmHomePage.verifyHomePage(),"Home page does not come after login");
		crmHomePage.enterTextInSearchFieldAndHitEnter(consultantEmailID);
		/*		while(true){
			if(crmHomePage.isSearchResultHasActiveUser("Consultant") ==false || crmHomePage.isAccountSectionPresent()==false){
				logger.info("No active user in the search results..searching new user");
				randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_CONSULTANT_WITH_AUTOSHIPS_RFO,countryId),RFO_DB);
				accountID = String.valueOf(getValueFromQueryResult(randomConsultantList, "AccountID"));
				randomConsultantUsernameList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_EMAIL_ID_FROM_ACCOUNT_ID,accountID),RFO_DB);
				consultantEmailID = String.valueOf(getValueFromQueryResult(randomConsultantUsernameList, "EmailAddress"));

				logger.info("The email address is "+consultantEmailID);
				s_assert.assertTrue(crmHomePage.verifyHomePage(),"Home page does not come after login");
				crmHomePage.enterTextInSearchFieldAndHitEnter(consultantEmailID);
			}else{
				break;
			}
		}*/
		String emailOnfirstRow = crmHomePage.getEmailOnFirstRowInSearchResults();
		crmHomePage.clickAnyTypeOfActiveCustomerInSearchResult("Consultant");
		//Verify account overview page
		s_assert.assertTrue(crmAccountDetailsPage.verifyNewContactButtonUnderContactSectionNotAvaliable(),"Contact Button Not Avaliable");
		s_assert.assertTrue(crmAccountDetailsPage.isEditActionNotPresentUnderContactSection(),"Edit Link Not Avaliable");
		s_assert.assertTrue(crmAccountDetailsPage.isMouseHoverContactsSectionEditLinkNotPresentOfFields(consultantEmailID),"Edit Link Not Avaliable");
		s_assert.assertTrue(crmAccountDetailsPage.isMouseHoverContactsSectionNewContentButtonNotPresentOfFields(consultantEmailID),"Contact Button Not Avaliable");
		s_assert.assertAll();
	}


	// RF Saleforce CRM-170 Verify Contact details page for PC for CA/US
	@Test
	public void verifyLoginAsALogisticsAgentContactDetailsPageForPC() throws InterruptedException{
		crmLoginpage.crmLogout();
		crmLoginpage.loginLogisticsUser(TestConstants.CRM_LOGIN_LOGISTICS_USERNAME, TestConstants.CRM_LOGIN_LOGISTICS_PASSWORD);
		int randomNum = CommonUtils.getRandomNum(10000, 1000000);
		String orderNote="This is automation note"+randomNum;
		logger.info("The username is "+pcUserName); 
		s_assert.assertTrue(crmHomePage.verifyHomePage(),"Home page does not come after login");
		crmHomePage.enterTextInSearchFieldAndHitEnter(pcUserName);
		/*		while(true){
			if(crmHomePage.isSearchResultHasActiveUser("Consultant") ==false || crmHomePage.isAccountSectionPresent()==false){
				logger.info("No active user in the search results..searching new user");
				randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_CONSULTANT_WITH_AUTOSHIPS_RFO,countryId),RFO_DB);
				accountID = String.valueOf(getValueFromQueryResult(randomConsultantList, "AccountID"));
				randomConsultantUsernameList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_EMAIL_ID_FROM_ACCOUNT_ID,accountID),RFO_DB);
				consultantEmailID = String.valueOf(getValueFromQueryResult(randomConsultantUsernameList, "EmailAddress"));

				logger.info("The email address is "+consultantEmailID);
				s_assert.assertTrue(crmHomePage.verifyHomePage(),"Home page does not come after login");
				crmHomePage.enterTextInSearchFieldAndHitEnter(consultantEmailID);
			}else{
				break;
			}
		}*/
		String emailOnfirstRow = crmHomePage.getEmailOnFirstRowInSearchResults();
		crmHomePage.clickAnyTypeOfActiveCustomerInSearchResult("Preferred Customer");
		//Verify account overview page
		s_assert.assertTrue(crmAccountDetailsPage.verifyNewContactButtonUnderContactSectionNotAvaliable(),"Contact Button Not Avaliable");
		s_assert.assertTrue(crmAccountDetailsPage.isEditActionNotPresentUnderContactSection(),"Edit Link Not Avaliable");
		s_assert.assertTrue(crmAccountDetailsPage.isMouseHoverContactsSectionEditLinkNotPresentOfFields(pcUserName),"Edit Link Not Avaliable");
		s_assert.assertTrue(crmAccountDetailsPage.isMouseHoverContactsSectionNewContentButtonNotPresentOfFields(pcUserName),"Contact Button Not Avaliable");
		s_assert.assertAll();
	}


	// RF Saleforce CRM-170 Verify Contact details page for RC for CA/US
	@Test
	public void verifyLoginAsALogisticsAgentContactDetailsPageForRC() throws InterruptedException{
		crmLoginpage.crmLogout();
		crmLoginpage.loginLogisticsUser(TestConstants.CRM_LOGIN_LOGISTICS_USERNAME, TestConstants.CRM_LOGIN_LOGISTICS_PASSWORD);
		int randomNum = CommonUtils.getRandomNum(10000, 1000000);
		String orderNote="This is automation note"+randomNum;
		logger.info("The username is "+rcUserName); 
		s_assert.assertTrue(crmHomePage.verifyHomePage(),"Home page does not come after login");
		crmHomePage.enterTextInSearchFieldAndHitEnter(rcUserName);
		/*		while(true){
			if(crmHomePage.isSearchResultHasActiveUser("Consultant") ==false || crmHomePage.isAccountSectionPresent()==false){
				logger.info("No active user in the search results..searching new user");
				randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_CONSULTANT_WITH_AUTOSHIPS_RFO,countryId),RFO_DB);
				accountID = String.valueOf(getValueFromQueryResult(randomConsultantList, "AccountID"));
				randomConsultantUsernameList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_EMAIL_ID_FROM_ACCOUNT_ID,accountID),RFO_DB);
				consultantEmailID = String.valueOf(getValueFromQueryResult(randomConsultantUsernameList, "EmailAddress"));

				logger.info("The email address is "+consultantEmailID);
				s_assert.assertTrue(crmHomePage.verifyHomePage(),"Home page does not come after login");
				crmHomePage.enterTextInSearchFieldAndHitEnter(consultantEmailID);
			}else{
				break;
			}
		}*/
		String emailOnfirstRow = crmHomePage.getEmailOnFirstRowInSearchResults();
		crmHomePage.clickAnyTypeOfActiveCustomerInSearchResult("Retail Customer");
		//Verify account overview page
		s_assert.assertTrue(crmAccountDetailsPage.verifyNewContactButtonUnderContactSectionNotAvaliable(),"Contact Button Not Avaliable");
		s_assert.assertTrue(crmAccountDetailsPage.isEditActionNotPresentUnderContactSection(),"Edit Link Not Avaliable");
		s_assert.assertTrue(crmAccountDetailsPage.isMouseHoverContactsSectionEditLinkNotPresentOfFields(pcUserName),"Edit Link Not Avaliable");
		s_assert.assertTrue(crmAccountDetailsPage.isMouseHoverContactsSectionNewContentButtonNotPresentOfFields(pcUserName),"Contact Button Not Avaliable");
		s_assert.assertAll();

	}

	@Test
	public void testVerifyLoginAsALogisticsAgentDisplayAutoshipDetailsForConsultant() throws InterruptedException{
		crmLoginpage.crmLogout();
		crmLoginpage.loginLogisticsUser(TestConstants.CRM_LOGIN_LOGISTICS_USERNAME, TestConstants.CRM_LOGIN_LOGISTICS_PASSWORD);
		int randomNum = CommonUtils.getRandomNum(10000, 1000000);
		String orderNote="This is automation note"+randomNum;
		logger.info("The username is "+consultantEmailID); 
		s_assert.assertTrue(crmHomePage.verifyHomePage(),"Home page does not come after login");
		crmHomePage.enterTextInSearchFieldAndHitEnter(consultantEmailID);
		crmHomePage.clickAnyTypeOfActiveCustomerInSearchResult("Consultant");

		s_assert.assertTrue(crmAccountDetailsPage.isMouseHoverAutoshipSectionPresentOfFields("Name"),"Name mouse hover section is not displayed in Autoships section in account details page");
		s_assert.assertTrue(crmAccountDetailsPage.isMouseHoverAutoshipSectionPresentOfFields("Autoship Type"),"Autoship Type mouse hover section is not displayed in Autoships section in account details page");
		s_assert.assertTrue(crmAccountDetailsPage.isMouseHoverAutoshipSectionPresentOfFields("Autoship Status"),"Autoship Status mouse hover section is not displayed in Autoships section in account details page");
		s_assert.assertTrue(crmAccountDetailsPage.isMouseHoverAutoshipSectionPresentOfFields("Status"),"Status mouse hover section is not displayed in Autoships section in account details page");
		s_assert.assertTrue(crmAccountDetailsPage.isMouseHoverAutoshipSectionPresentOfFields("Last OrderDate"),"Last OrderDate mouse hover section is not displayed in Autoships section in account details page");
		s_assert.assertTrue(crmAccountDetailsPage.isMouseHoverAutoshipSectionPresentOfFields("Next Order Date"),"Next Order Date mouse hover section is not displayed in Autoships section in account details page");
		s_assert.assertTrue(crmAccountDetailsPage.isMouseHoverAutoshipSectionPresentOfFields("Total"),"Total mouse hover section is not displayed in Autoships section in account details page");
		s_assert.assertTrue(crmAccountDetailsPage.isMouseHoverAutoshipSectionPresentOfFields("QV"),"QV mouse hover section is not displayed in Autoships section in account details page");

		crmAccountDetailsPage.getCountAutoships();
		crmAccountDetailsPage.clickAutoships();
		s_assert.assertTrue(crmAccountDetailsPage.getCountAutoships().equals(crmAccountDetailsPage.getCountAutoshipNumber()), "Autoships Numbers are not equal");
		crmAccountDetailsPage.clickFirstAutoshipID();

		//s_assert.assertTrue(crmAccountDetailsPage.isLabelUnderAutoshipNumberPresent("Autoship Number"),"Autoship Number is not Present");
		s_assert.assertTrue(crmAccountDetailsPage.isLabelUnderAutoshipNumberPresent("Autoship Type"),"Autoship Type is not Present");
		s_assert.assertTrue(crmAccountDetailsPage.isLabelUnderAutoshipNumberPresent("Autoship Status"),"Autoship Status is not Present");
		s_assert.assertTrue(crmAccountDetailsPage.isLabelUnderAutoshipNumberPresent("Do Not Ship"),"Autoship Do not ship is not Present");
		s_assert.assertTrue(crmAccountDetailsPage.isLabelUnderAutoshipNumberPresent("Start Date"),"Autoship Start Date is not Present");
		s_assert.assertTrue(crmAccountDetailsPage.isLabelUnderAutoshipNumberPresent("End Date"),"Autoship Status is not Present");
		s_assert.assertTrue(crmAccountDetailsPage.isLabelUnderAutoshipNumberPresent("Completion Date"),"Autoship Completion Date is not Present");
		s_assert.assertTrue(crmAccountDetailsPage.isLabelUnderAutoshipNumberPresent("Account"),"Autoship Account is not Present");
		s_assert.assertTrue(crmAccountDetailsPage.isLabelUnderAutoshipNumberPresent("Source"),"Autoship Source is not Present"); 
		s_assert.assertTrue(crmAccountDetailsPage.isLabelUnderAutoshipNumberPresent("Last OrderDate"),"Autoship Last Order Date is not Present");
		s_assert.assertTrue(crmAccountDetailsPage.isLabelUnderAutoshipNumberPresent("Next Order Date"),"Autoship Next Order Date is not Present");
		s_assert.assertTrue(crmAccountDetailsPage.isLabelUnderAutoshipNumberPresent("Last Modified By"),"Autoship Last Modified By is not Present");
		s_assert.assertTrue(crmAccountDetailsPage.isLabelUnderAutoshipNumberPresent("Created By"),"Autoship Created By is not Present");

		s_assert.assertTrue(crmAccountDetailsPage.isLabelUnderPendingAutoshipBreakdownPresent("Is Tax Exempt"),"In Pending Autoship Breakdown Tax Exempt is not Present");
		s_assert.assertTrue(crmAccountDetailsPage.isLabelUnderPendingAutoshipBreakdownPresent("QV"),"In Pending Autoship Breakdown QV is not Present");
		s_assert.assertTrue(crmAccountDetailsPage.isLabelUnderPendingAutoshipBreakdownPresent("CV"),"In Pending Autoship Breakdown CV is not Present");
		s_assert.assertTrue(crmAccountDetailsPage.isLabelUnderPendingAutoshipBreakdownPresent("Product Discount"),"In Pending Autoship Breakdown Product Discount is not Present");
		s_assert.assertTrue(crmAccountDetailsPage.isLabelUnderPendingAutoshipBreakdownPresent("Total Discount"),"In Pending Autoship Breakdown Total Discount is not Present");
		s_assert.assertTrue(crmAccountDetailsPage.isLabelUnderPendingAutoshipBreakdownPresent("Sub Total"),"In Pending Autoship Breakdown Sub Total is not Present");
		s_assert.assertTrue(crmAccountDetailsPage.isLabelUnderPendingAutoshipBreakdownPresent("Taxable Total"),"In Pending Autoship Breakdown Taxable Total is not Present");
		s_assert.assertTrue(crmAccountDetailsPage.isLabelUnderPendingAutoshipBreakdownPresent("Total"),"In Pending Autoship Breakdown Total is not Present");
		s_assert.assertTrue(crmAccountDetailsPage.isLabelUnderPendingAutoshipBreakdownPresent("Delay Count"),"In Pending Autoship Breakdown Delay Count is not Present");
		s_assert.assertTrue(crmAccountDetailsPage.isLabelUnderPendingAutoshipBreakdownPresent("Fuel Surcharge"),"In Pending Autoship Breakdown  Fuel Surcharge is not Present");
		s_assert.assertTrue(crmAccountDetailsPage.isLabelUnderPendingAutoshipBreakdownPresent("Residential Surcharge"),"In Pending Autoship Breakdown Residential Surcharge is not Present");
		s_assert.assertTrue(crmAccountDetailsPage.isLabelUnderPendingAutoshipBreakdownPresent("Product Tax"),"In Pending Autoship Breakdown Product Tax is not Present");
		s_assert.assertTrue(crmAccountDetailsPage.isLabelUnderPendingAutoshipBreakdownPresent("Total Tax"),"In Pending Autoship Breakdown Total Tax is not Present");  
		s_assert.assertAll();

	}

	@Test
	public void testVerifyLoginAsALogisticsAgentDisplayAutoshipDetailsForPC() throws InterruptedException{
		crmLoginpage.crmLogout();
		crmLoginpage.loginLogisticsUser(TestConstants.CRM_LOGIN_LOGISTICS_USERNAME, TestConstants.CRM_LOGIN_LOGISTICS_PASSWORD);
		int randomNum = CommonUtils.getRandomNum(10000, 1000000);
		String orderNote="This is automation note"+randomNum;
		logger.info("The username is "+pcUserName); 
		s_assert.assertTrue(crmHomePage.verifyHomePage(),"Home page does not come after login");
		crmHomePage.enterTextInSearchFieldAndHitEnter(pcUserName);
		crmHomePage.clickAnyTypeOfActiveCustomerInSearchResult("Preferred Customer");

		s_assert.assertTrue(crmAccountDetailsPage.isMouseHoverAutoshipSectionPresentOfFields("Name"),"Name mouse hover section is not displayed in Autoships section in account details page");
		s_assert.assertTrue(crmAccountDetailsPage.isMouseHoverAutoshipSectionPresentOfFields("Autoship Type"),"Autoship Type mouse hover section is not displayed in Autoships section in account details page");
		s_assert.assertTrue(crmAccountDetailsPage.isMouseHoverAutoshipSectionPresentOfFields("Autoship Status"),"Autoship Status mouse hover section is not displayed in Autoships section in account details page");
		s_assert.assertTrue(crmAccountDetailsPage.isMouseHoverAutoshipSectionPresentOfFields("Status"),"Status mouse hover section is not displayed in Autoships section in account details page");
		s_assert.assertTrue(crmAccountDetailsPage.isMouseHoverAutoshipSectionPresentOfFields("Last OrderDate"),"Last OrderDate mouse hover section is not displayed in Autoships section in account details page");
		s_assert.assertTrue(crmAccountDetailsPage.isMouseHoverAutoshipSectionPresentOfFields("Next Order Date"),"Next Order Date mouse hover section is not displayed in Autoships section in account details page");
		s_assert.assertTrue(crmAccountDetailsPage.isMouseHoverAutoshipSectionPresentOfFields("Total"),"Total mouse hover section is not displayed in Autoships section in account details page");
		s_assert.assertTrue(crmAccountDetailsPage.isMouseHoverAutoshipSectionPresentOfFields("QV"),"QV mouse hover section is not displayed in Autoships section in account details page");

		crmAccountDetailsPage.getCountAutoships();
		crmAccountDetailsPage.clickAutoships();
		s_assert.assertTrue(crmAccountDetailsPage.getCountAutoships().equals(crmAccountDetailsPage.getCountAutoshipNumber()), "Autoships Numbers are not equal");
		crmAccountDetailsPage.clickFirstAutoshipID();

		//s_assert.assertTrue(crmAccountDetailsPage.isLabelUnderAutoshipNumberPresent("Autoship Number"),"Autoship Number is not Present");
		s_assert.assertTrue(crmAccountDetailsPage.isLabelUnderAutoshipNumberPresent("Autoship Type"),"Autoship Type is not Present");
		s_assert.assertTrue(crmAccountDetailsPage.isLabelUnderAutoshipNumberPresent("Autoship Status"),"Autoship Status is not Present");
		s_assert.assertTrue(crmAccountDetailsPage.isLabelUnderAutoshipNumberPresent("Do Not Ship"),"Autoship Do not ship is not Present");
		s_assert.assertTrue(crmAccountDetailsPage.isLabelUnderAutoshipNumberPresent("Start Date"),"Autoship Start Date is not Present");
		s_assert.assertTrue(crmAccountDetailsPage.isLabelUnderAutoshipNumberPresent("End Date"),"Autoship Status is not Present");
		s_assert.assertTrue(crmAccountDetailsPage.isLabelUnderAutoshipNumberPresent("Completion Date"),"Autoship Completion Date is not Present");
		s_assert.assertTrue(crmAccountDetailsPage.isLabelUnderAutoshipNumberPresent("Account"),"Autoship Account is not Present");
		s_assert.assertTrue(crmAccountDetailsPage.isLabelUnderAutoshipNumberPresent("Source"),"Autoship Source is not Present"); 
		s_assert.assertTrue(crmAccountDetailsPage.isLabelUnderAutoshipNumberPresent("Last OrderDate"),"Autoship Last Order Date is not Present");
		s_assert.assertTrue(crmAccountDetailsPage.isLabelUnderAutoshipNumberPresent("Next Order Date"),"Autoship Next Order Date is not Present");
		s_assert.assertTrue(crmAccountDetailsPage.isLabelUnderAutoshipNumberPresent("Last Modified By"),"Autoship Last Modified By is not Present");
		s_assert.assertTrue(crmAccountDetailsPage.isLabelUnderAutoshipNumberPresent("Created By"),"Autoship Created By is not Present");

		s_assert.assertTrue(crmAccountDetailsPage.isLabelUnderPendingAutoshipBreakdownPresent("Is Tax Exempt"),"In Pending Autoship Breakdown Tax Exempt is not Present");
		s_assert.assertTrue(crmAccountDetailsPage.isLabelUnderPendingAutoshipBreakdownPresent("QV"),"In Pending Autoship Breakdown QV is not Present");
		s_assert.assertTrue(crmAccountDetailsPage.isLabelUnderPendingAutoshipBreakdownPresent("CV"),"In Pending Autoship Breakdown CV is not Present");
		s_assert.assertTrue(crmAccountDetailsPage.isLabelUnderPendingAutoshipBreakdownPresent("Product Discount"),"In Pending Autoship Breakdown Product Discount is not Present");
		s_assert.assertTrue(crmAccountDetailsPage.isLabelUnderPendingAutoshipBreakdownPresent("Total Discount"),"In Pending Autoship Breakdown Total Discount is not Present");
		s_assert.assertTrue(crmAccountDetailsPage.isLabelUnderPendingAutoshipBreakdownPresent("Sub Total"),"In Pending Autoship Breakdown Sub Total is not Present");
		s_assert.assertTrue(crmAccountDetailsPage.isLabelUnderPendingAutoshipBreakdownPresent("Taxable Total"),"In Pending Autoship Breakdown Taxable Total is not Present");
		s_assert.assertTrue(crmAccountDetailsPage.isLabelUnderPendingAutoshipBreakdownPresent("Total"),"In Pending Autoship Breakdown Total is not Present");
		s_assert.assertTrue(crmAccountDetailsPage.isLabelUnderPendingAutoshipBreakdownPresent("Delay Count"),"In Pending Autoship Breakdown Delay Count is not Present");
		s_assert.assertTrue(crmAccountDetailsPage.isLabelUnderPendingAutoshipBreakdownPresent("Fuel Surcharge"),"In Pending Autoship Breakdown  Fuel Surcharge is not Present");
		s_assert.assertTrue(crmAccountDetailsPage.isLabelUnderPendingAutoshipBreakdownPresent("Residential Surcharge"),"In Pending Autoship Breakdown Residential Surcharge is not Present");
		s_assert.assertTrue(crmAccountDetailsPage.isLabelUnderPendingAutoshipBreakdownPresent("Product Tax"),"In Pending Autoship Breakdown Product Tax is not Present");
		s_assert.assertTrue(crmAccountDetailsPage.isLabelUnderPendingAutoshipBreakdownPresent("Total Tax"),"In Pending Autoship Breakdown Total Tax is not Present");  
		s_assert.assertAll();

	}

	//Verify Shipping Profile details page for Consultant/PC/RC  for CA/US
	@Test
	public void testVerifyShippingProfileDetailsPageForConsultantPCRC() throws InterruptedException{
		crmLoginpage.crmLogout();
		crmLoginpage.loginLogisticsUser(TestConstants.CRM_LOGIN_LOGISTICS_USERNAME, TestConstants.CRM_LOGIN_LOGISTICS_PASSWORD);
		s_assert.assertTrue(crmHomePage.verifyHomePage(),"Home page does not come after login");
		crmHomePage.enterTextInSearchFieldAndHitEnter(consultantEmailID);
		crmHomePage.clickAnyTypeOfActiveCustomerInSearchResult("Consultant");
		s_assert.assertTrue(crmAccountDetailsPage.isAccountDetailsPagePresent(),"Account Details page has not displayed");
		crmAccountDetailsPage.clickAccountMainMenuOptions("Shipping Profiles");
		s_assert.assertFalse(crmAccountDetailsPage.isAddNewShippingProfileButtonPresentAShippingProfileSectiont(),"Add new shipping profile button is Present at shipping profile section for consultant");
		s_assert.assertFalse(crmAccountDetailsPage.isActionPresentAtShippingProfileSectionForFirstProfile("Edit"),"For first shipping profile Edit action is Present at shipping profile section for consultant");
		s_assert.assertFalse(crmAccountDetailsPage.isActionPresentAtShippingProfileSectionForFirstProfile("Del"),"For first shipping profile Del action is Present at shipping profile section for consultant");
		crmAccountDetailsPage.clickFirstShippingProfile();
		s_assert.assertFalse(crmAccountDetailsPage.isActionPresentAtShippingProfilePage("Edit"),"Edit action is Present at shipping profile page for consultant");
		s_assert.assertFalse(crmAccountDetailsPage.isActionPresentAtShippingProfilePage("Del"),"Del action is Present at shipping profile page for consultant");
		crmAccountDetailsPage.closeSubTabOfEditShippingProfile();
		crmAccountDetailsPage.closeAllOpenedTabs();

		// For PC
		crmHomePage.enterTextInSearchFieldAndHitEnter(pcUserName);
		crmHomePage.clickAnyTypeOfActiveCustomerInSearchResult("Preferred Customer");
		s_assert.assertTrue(crmAccountDetailsPage.isAccountDetailsPagePresent(),"Account Details page has not displayed");
		crmAccountDetailsPage.clickAccountMainMenuOptions("Shipping Profiles");
		s_assert.assertFalse(crmAccountDetailsPage.isAddNewShippingProfileButtonPresentAShippingProfileSectiont(),"Add new shipping profile button is Present at shipping profile section for PC");
		s_assert.assertFalse(crmAccountDetailsPage.isActionPresentAtShippingProfileSectionForFirstProfile("Edit"),"For first shipping profile Edit action is Present at shipping profile section for PC");
		s_assert.assertFalse(crmAccountDetailsPage.isActionPresentAtShippingProfileSectionForFirstProfile("Del"),"For first shipping profile Del action is Present at shipping profile section for PC");
		crmAccountDetailsPage.clickFirstShippingProfile();
		s_assert.assertFalse(crmAccountDetailsPage.isActionPresentAtShippingProfilePage("Edit"),"Edit action is Present at shipping profile page for PC");
		s_assert.assertFalse(crmAccountDetailsPage.isActionPresentAtShippingProfilePage("Del"),"Del action is Present at shipping profile page for PC");
		crmAccountDetailsPage.closeSubTabOfEditShippingProfile();
		crmAccountDetailsPage.closeAllOpenedTabs();

		//For RC
		crmHomePage.enterTextInSearchFieldAndHitEnter(rcUserName);
		crmHomePage.clickAnyTypeOfActiveCustomerInSearchResult("Retail Customer");
		s_assert.assertTrue(crmAccountDetailsPage.isAccountDetailsPagePresent(),"Account Details page has not displayed");
		crmAccountDetailsPage.clickAccountMainMenuOptions("Shipping Profiles");
		s_assert.assertFalse(crmAccountDetailsPage.isAddNewShippingProfileButtonPresentAShippingProfileSectiont(),"Add new shipping profile button is Present at shipping profile section for RC");
		s_assert.assertFalse(crmAccountDetailsPage.isActionPresentAtShippingProfileSectionForFirstProfile("Edit"),"For first shipping profile Edit action is Present at shipping profile section for RC");
		s_assert.assertFalse(crmAccountDetailsPage.isActionPresentAtShippingProfileSectionForFirstProfile("Del"),"For first shipping profile Del action is Present at shipping profile section for RC");
		crmAccountDetailsPage.clickFirstShippingProfile();
		s_assert.assertFalse(crmAccountDetailsPage.isActionPresentAtShippingProfilePage("Edit"),"Edit action is Present at shipping profile page for RC");
		s_assert.assertFalse(crmAccountDetailsPage.isActionPresentAtShippingProfilePage("Del"),"Del action is Present at shipping profile page for RC");
		s_assert.assertAll();
	}

}