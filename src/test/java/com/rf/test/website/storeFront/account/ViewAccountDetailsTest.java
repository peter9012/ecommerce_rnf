package com.rf.test.website.storeFront.account;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.annotations.Test;

import com.rf.core.utils.DBUtil;
import com.rf.core.website.constants.DBQueries;
import com.rf.core.website.constants.TestConstants;
import com.rf.pages.website.StoreFrontAccountInfoPage;
import com.rf.pages.website.StoreFrontBillingInfoPage;
import com.rf.pages.website.StoreFrontConsultantPage;
import com.rf.pages.website.StoreFrontHomePage;
import com.rf.pages.website.StoreFrontOrdersAutoshipStatusPage;
import com.rf.pages.website.StoreFrontOrdersPage;
import com.rf.test.website.RFWebsiteBaseTest;

public class ViewAccountDetailsTest extends RFWebsiteBaseTest{
	private static final Logger logger = LogManager
			.getLogger(ViewAccountDetailsTest.class.getName());

	private StoreFrontHomePage storeFrontHomePage;
	private StoreFrontConsultantPage storeFrontConsulatantPage;	
	private StoreFrontOrdersPage storeFrontOrdersPage;
	private StoreFrontAccountInfoPage storeFrontAccountInfoPage;
	private StoreFrontOrdersAutoshipStatusPage storeFrontOrdersAutoshipStatusPage;
	private StoreFrontBillingInfoPage storeFrontBillingInfoPage;
	
	private String RFL_DB = null;
	private String RFO_DB = null;
	
	// Hybris Phase 2-4179:Enrolled Consultant, No CRP/Pulse, No Orders, No Downlines, Inctive
	@Test
	public void testEnrolledConsultantNoCRPNoPulseNoOrdersINACTIVE_4179() throws InterruptedException{
		RFL_DB = driver.getDBNameRFL();
		RFO_DB = driver.getDBNameRFO();
		List<Map<String, Object>> randomConsultantList =  null;
		String consultantEmailID = null;
		randomConsultantList = DBUtil.performDatabaseQuery(DBQueries.GET_RANDOM_CONSULTANT_INACTIVE_RFL_4179,RFL_DB);
		consultantEmailID = (String) getValueFromQueryResult(randomConsultantList, "EmailAddress");
		storeFrontHomePage = new StoreFrontHomePage(driver);
		storeFrontHomePage.loginAsConsultant(consultantEmailID,TestConstants.CONSULTANT_PASSWORD_RFL);
		s_assert.assertTrue(storeFrontHomePage.isCurrentURLShowsError(),"Inactive User doesn't get Login failed");
		s_assert.assertAll();		
	}

	// Hybris Phase 2-4181:Enrolled Consultant, No CRP/Pulse, Failed Orders, No Downlines, InActive
	@Test
	public void testEnrolledConsultantNoCRPNoFailedOrdersINACTIVE_4181() throws InterruptedException{
		RFL_DB = driver.getDBNameRFL();
		RFO_DB = driver.getDBNameRFO();
		List<Map<String, Object>> randomConsultantList =  null;
		String consultantEmailID = null;
		randomConsultantList = DBUtil.performDatabaseQuery(DBQueries.GET_RANDOM_CONSULTANT_INACTIVE_RFL_4181,RFL_DB);
		consultantEmailID = (String) getValueFromQueryResult(randomConsultantList, "EmailAddress");
		storeFrontHomePage = new StoreFrontHomePage(driver);
		storeFrontHomePage.loginAsConsultant(consultantEmailID,TestConstants.CONSULTANT_PASSWORD_RFL);
		s_assert.assertTrue(storeFrontHomePage.isCurrentURLShowsError(),"Inactive User doesn't get Login failed");
		s_assert.assertAll();
	}


	// Hybris Phase 2-4184:Enrolled Consultant, Has CRP/ No Pulse, No Orders, No Downlines, InActive
	@Test
	public void testEnrolledConsultantHasCRPNoOrdersACTIVE_4184() throws InterruptedException{
		RFL_DB = driver.getDBNameRFL();
		RFO_DB = driver.getDBNameRFO();
		List<Map<String, Object>> randomConsultantList =  null;
		String consultantEmailID = null;
		randomConsultantList = DBUtil.performDatabaseQuery(DBQueries.GET_RANDOM_CONSULTANT_INACTIVE_RFL_4184,RFL_DB);
		consultantEmailID = (String) getValueFromQueryResult(randomConsultantList, "EmailAddress");
		storeFrontHomePage = new StoreFrontHomePage(driver);
		storeFrontHomePage.loginAsConsultant(consultantEmailID,TestConstants.CONSULTANT_PASSWORD_RFL);
		s_assert.assertTrue(storeFrontHomePage.isCurrentURLShowsError(),"Inactive User doesn't get Login failed");
		s_assert.assertAll();
	}


	// Hybris Phase 2-4186:Enrolled Consultant, No CRP/ Has Pulse, No Orders, No Downlines, InActive
	@Test
	public void testEnrolledConsultantNoCRPHasPulseNoOrdersINACTIVE_4186() throws InterruptedException{
		RFL_DB = driver.getDBNameRFL();
		RFO_DB = driver.getDBNameRFO();
		List<Map<String, Object>> randomConsultantList =  null;
		String consultantEmailID = null;
		randomConsultantList = DBUtil.performDatabaseQuery(DBQueries.GET_RANDOM_CONSULATNT_NOCRP_HAS_PULSE_NO_ORDERS_INACTIVE_RFL_4186,RFL_DB);
		consultantEmailID = (String) getValueFromQueryResult(randomConsultantList, "EmailAddress");
		storeFrontHomePage = new StoreFrontHomePage(driver);
		storeFrontHomePage.loginAsConsultant(consultantEmailID,TestConstants.CONSULTANT_PASSWORD_RFL);
		s_assert.assertTrue(storeFrontHomePage.isCurrentURLShowsError(),"Inactive User doesn't get Login failed");
		s_assert.assertAll();
	}

	// Hybris Phase 2-4188:Enrolled Consultant, Has CRP/ Has Pulse, No Orders, No Downlines, InActive
	@Test
	public void testEnrolledConsultantHasCRPHasPulseNoOrdersINACTIVE_4188() throws InterruptedException{
		RFL_DB = driver.getDBNameRFL();
		RFO_DB = driver.getDBNameRFO();
		List<Map<String, Object>> randomConsultantList =  null;
		String consultantEmailID = null;
		randomConsultantList = DBUtil.performDatabaseQuery(DBQueries.GET_RANDOM_CONSULTANT_HAS_CRP_HAS_PULSE_NO_ORDERS_INACTIVE_RFL_4188,RFL_DB);
		consultantEmailID = (String) getValueFromQueryResult(randomConsultantList, "EmailAddress");
		storeFrontHomePage = new StoreFrontHomePage(driver);
		storeFrontHomePage.loginAsConsultant(consultantEmailID,TestConstants.CONSULTANT_PASSWORD_RFL);
		s_assert.assertTrue(storeFrontHomePage.isCurrentURLShowsError(),"Inactive User doesn't get Login failed");			
		s_assert.assertAll();
	}

	// Hybris Phase 2-4190:Enrolled Consultant, Has CRP/ Has Pulse, Failed Orders, No Downlines, Inactive
	@Test
	public void testEnrolledConsultantHasCRPHasPulseFailedOrdersINACTIVE_4190() throws InterruptedException{
		RFL_DB = driver.getDBNameRFL();
		RFO_DB = driver.getDBNameRFO();
		List<Map<String, Object>> randomConsultantList =  null;
		String consultantEmailID = null;
		randomConsultantList = DBUtil.performDatabaseQuery(DBQueries.GET_RANDOM_CONSULTANT_HAS_CRP_HAS_PULSE_NO_ORDERS_INACTIVE_RFL_4188,RFL_DB);
		consultantEmailID = (String) getValueFromQueryResult(randomConsultantList, "EmailAddress");
		storeFrontHomePage = new StoreFrontHomePage(driver);
		storeFrontHomePage.loginAsConsultant(consultantEmailID,TestConstants.CONSULTANT_PASSWORD_RFL);
		s_assert.assertTrue(storeFrontHomePage.isCurrentURLShowsError(),"Inactive User doesn't get Login failed");			
		s_assert.assertAll();
	}

	// Hybris Phase 2-4192:Enrolled Consultant, Has CRP/ Has Pulse, Has Submitted Orders, No Downlines, Inactive
	@Test
	public void testEnrolledConsultantHasCRPHasPulseSubmittedOrdersINACTIVE_4192() throws InterruptedException{
		RFL_DB = driver.getDBNameRFL();
		RFO_DB = driver.getDBNameRFO();
		List<Map<String, Object>> randomConsultantList =  null;
		String consultantEmailID = null;
		randomConsultantList = DBUtil.performDatabaseQuery(DBQueries.GET_RANDOM_CONSULTANT_HAS_CRP_HAS_PULSE_SUBMITTED_ORDERS_INACTIVE_RFL_4192,RFL_DB);
		consultantEmailID = (String) getValueFromQueryResult(randomConsultantList, "EmailAddress");
		storeFrontHomePage = new StoreFrontHomePage(driver);
		storeFrontHomePage.loginAsConsultant(consultantEmailID,TestConstants.CONSULTANT_PASSWORD_RFL);
		s_assert.assertTrue(storeFrontHomePage.isCurrentURLShowsError(),"Inactive User doesn't get Login failed");		
		s_assert.assertAll();
	}

	// Hybris Phase 2-4194:Enrolled Consultant, Has CRP/ Has Pulse, Has Failed Order, Has Downlines, Inactive
	@Test
	public void testEnrolledConsultantHasCRPHasPulseHasFailedOrdersINACTIVE_4194() throws InterruptedException{
		RFL_DB = driver.getDBNameRFL();
		RFO_DB = driver.getDBNameRFO();
		List<Map<String, Object>> randomConsultantList =  null;
		String consultantEmailID = null;
		randomConsultantList = DBUtil.performDatabaseQuery(DBQueries.GET_RANDOM_CONSULTANT_HAS_CRP_HAS_PULSE_FAILED_ORDERS_INACTIVE_RFL_4194,RFL_DB);
		consultantEmailID = (String) getValueFromQueryResult(randomConsultantList, "EmailAddress");
		storeFrontHomePage = new StoreFrontHomePage(driver);
		storeFrontHomePage.loginAsConsultant(consultantEmailID,TestConstants.CONSULTANT_PASSWORD_RFL);
		s_assert.assertTrue(storeFrontHomePage.isCurrentURLShowsError(),"Inactive User doesn't get Login failed");		

	}

	// Hybris Phase 2-4196:Enrolled Consultant, Has CRP/ Has Pulse, Has Submitted Orders, Has Downlines, Inactive
	@Test
	public void testEnrolledConsultantHasCRPHasPulseHasSubmittedOrdersINACTIVE_4196() throws InterruptedException{
		RFL_DB = driver.getDBNameRFL();
		RFO_DB = driver.getDBNameRFO();
		List<Map<String, Object>> randomConsultantList =  null;
		String consultantEmailID = null;
		randomConsultantList = DBUtil.performDatabaseQuery(DBQueries.GET_RANDOM_CONSULTANT_HAS_CRP_HAS_PULSE_HAS_SUBMITTED_ORDERS_INACTIVE_RFL_4196,RFL_DB);
		consultantEmailID = (String) getValueFromQueryResult(randomConsultantList, "EmailAddress");
		storeFrontHomePage = new StoreFrontHomePage(driver);
		storeFrontHomePage.loginAsConsultant(consultantEmailID,TestConstants.CONSULTANT_PASSWORD_RFL);
		s_assert.assertTrue(storeFrontHomePage.isCurrentURLShowsError(),"Inactive User doesn't get Login failed");			
		s_assert.assertAll();
	}	

	//Hybris Phase 2-4227 :: Version : 1 :: Account with NULL email Address
	@Test
	public void testAccountWithNullEmailAddress_4227() throws InterruptedException{
		RFL_DB = driver.getDBNameRFL();
		List<Map<String, Object>> userWithNoEmailAddressIDList =  null;		
		userWithNoEmailAddressIDList = DBUtil.performDatabaseQuery(DBQueries.GET_ACCOUNTS_WITH_NULL_EMAIL_ADDRESS,RFL_DB);
		s_assert.assertTrue(userWithNoEmailAddressIDList.size()==0,"Account with NULL email Address exist");			
		s_assert.assertAll();
	}	

	//Hybris Phase 2-4223 :: Version : 1 :: Account with multiple payment profiles
	@Test
	public void testBillingInfoPageDetails_4223() throws SQLException, InterruptedException{
		RFL_DB = driver.getDBNameRFL();
		RFO_DB = driver.getDBNameRFO();
		List<Map<String, Object>> randomEmailList =  null;
		List<Map<String, Object>> billingAddressCountList =  null;
		int totalBillingAddressesFromDB = 0;
		String userEmailId = null;
		randomEmailList = DBUtil.performDatabaseQuery(DBQueries.GET_RANDOM_USER_MULTIPLE_PAYMENTS_RFL,RFL_DB);
		userEmailId =  (String) getValueFromQueryResult(randomEmailList, "UserName");
		storeFrontHomePage = new StoreFrontHomePage(driver);
		storeFrontConsulatantPage = storeFrontHomePage.loginAsConsultant(userEmailId, TestConstants.CONSULTANT_PASSWORD_TST4);
		s_assert.assertTrue(storeFrontConsulatantPage.verifyConsultantPage(),"Consultant Page doesn't contain Welcome User Message");
		storeFrontConsulatantPage.clickOnWelcomeDropDown();
		storeFrontBillingInfoPage = storeFrontConsulatantPage.clickBillingInfoLinkPresentOnWelcomeDropDown();
		s_assert.assertTrue(storeFrontBillingInfoPage.verifyBillingInfoPageIsDisplayed(),"Billing Info page has not been displayed");

		// assert with RFL
		billingAddressCountList = DBUtil.performDatabaseQuery(DBQueries.callQueryWithArguement(DBQueries.GET_BILLING_ADDRESS_COUNT_QUERY_TST4,TestConstants.CONSULTANT_EMAIL_ID_TST4),RFL_DB);
		totalBillingAddressesFromDB = (Integer) getValueFromQueryResult(billingAddressCountList, "count");
		if(assertEqualsDB("Billing Addresses count on UI is different from DB", totalBillingAddressesFromDB,storeFrontBillingInfoPage.getTotalBillingAddressesDisplayed(),RFL_DB)==false){
			//assert with RFO
			billingAddressCountList = DBUtil.performDatabaseQuery(DBQueries.callQueryWithArguement(DBQueries.GET_BILLING_ADDRESS_COUNT_QUERY,TestConstants.CONSULTANT_EMAIL_ID_TST4),RFO_DB);
			totalBillingAddressesFromDB = (Integer) getValueFromQueryResult(billingAddressCountList, "count");			
			s_assert.assertEquals(totalBillingAddressesFromDB,storeFrontBillingInfoPage.getTotalBillingAddressesDisplayed(),"Billing Addresses count on UI is different from DB");			
		}
		logout();
		s_assert.assertAll();
	}

	// Hybris Phase 2-4195 : Enrolled Consultant, Has CRP/ Has Pulse, Has Submitted Orders
	@Test
	public void testEnrolledConsultantHasCRPPULSESubmittedOrders_HP2_4195() throws InterruptedException, SQLException{
		RFL_DB = driver.getDBNameRFL();
		RFO_DB = driver.getDBNameRFO();
		List<Map<String, Object>> randomConsultantEmailIdList =  null;
		List<Map<String, Object>> orderNumberList =  null;		
		String orderNumberDB = null;
		String consultantEmail = null;
		randomConsultantEmailIdList = DBUtil.performDatabaseQuery(DBQueries.GET_RANDOM_CONSULTANT_HAS_CRP_HAS_ORDERS_RFL,RFL_DB);
		consultantEmail = (String) getValueFromQueryResult(randomConsultantEmailIdList, "UserName");
		storeFrontHomePage = new StoreFrontHomePage(driver);
		storeFrontConsulatantPage = storeFrontHomePage.loginAsConsultant(consultantEmail, TestConstants.CONSULTANT_PASSWORD_TST4);
		s_assert.assertTrue(storeFrontConsulatantPage.verifyConsultantPage(),"Consultant Page doesn't contain Welcome User Message");
		storeFrontConsulatantPage.clickOnWelcomeDropDown();
		storeFrontOrdersPage = storeFrontConsulatantPage.clickOrdersLinkPresentOnWelcomeDropDown();
		s_assert.assertTrue(storeFrontOrdersPage.verifyOrdersPageIsDisplayed(),"Orders page has not been displayed");

		orderNumberList = DBUtil.performDatabaseQuery(DBQueries.callQueryWithArguement(DBQueries.GET_ORDER_NUMBER_FOR_CRP_ORDER_HISTORY_QUERY_RFL, consultantEmail),RFL_DB);
		//assert on RFL
		orderNumberDB = (String) getValueFromQueryResult(orderNumberList, "OrderNumber");
		if(assertTrueDB("Status of Order Number is not found as Submitted",storeFrontOrdersPage.verifyOrderStatusToBeSubmitted(orderNumberDB),RFL_DB)==false){
			//assert On RFO
			orderNumberList = DBUtil.performDatabaseQuery(DBQueries.callQueryWithArguement(DBQueries.GET_ORDER_NUMBER_FOR_CRP_ORDER_HISTORY_QUERY_RFO, consultantEmail),RFO_DB);
			orderNumberDB = (String) getValueFromQueryResult(orderNumberList, "OrderNumber");
			assertTrue("Status of Order Number is not found as Submitted",storeFrontOrdersPage.verifyOrderStatusToBeSubmitted(orderNumberDB));	
		}		
		storeFrontAccountInfoPage = storeFrontOrdersPage.clickOnAccountInfoFromLeftPanel();
		storeFrontOrdersAutoshipStatusPage = storeFrontAccountInfoPage.clickOnAutoShipStatus();
		s_assert.assertTrue(storeFrontOrdersAutoshipStatusPage.verifyAutoShipStatusHeader(),"Autoship status header is not as expected");
		s_assert.assertTrue(storeFrontOrdersAutoshipStatusPage.verifyAutoShipCRPStatus(),"AutoShip CRP Status is not as expected");
		s_assert.assertTrue(storeFrontOrdersAutoshipStatusPage.verifyAutoShipPulseSubscriptionStatus(),"AutoShip Pulse Subscription Status is not as expected");
		logout();
		s_assert.assertAll();
	}



}
