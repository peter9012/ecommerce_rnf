package com.rf.test.website.storeFront.account;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.annotations.Test;

import com.rf.core.utils.CommonUtils;
import com.rf.core.utils.DBUtil;
import com.rf.core.website.constants.DBQueries;
import com.rf.core.website.constants.TestConstants;
import com.rf.pages.website.StoreFrontBillingInfoPage;
import com.rf.pages.website.StoreFrontCartAutoShipPage;
import com.rf.pages.website.StoreFrontConsultantPage;
import com.rf.pages.website.StoreFrontHomePage;
import com.rf.pages.website.StoreFrontOrdersPage;
import com.rf.pages.website.StoreFrontUpdateCartPage;
import com.rf.test.website.RFWebsiteBaseTest;

public class AddBillingTest extends RFWebsiteBaseTest{
	private static final Logger logger = LogManager
			.getLogger(AddBillingTest.class.getName());

	private StoreFrontHomePage storeFrontHomePage;
	private StoreFrontConsultantPage storeFrontConsulatantPage;
	private StoreFrontBillingInfoPage storeFrontBillingInfoPage;
	private StoreFrontOrdersPage storeFrontOrdersPage;
	private StoreFrontCartAutoShipPage storeFrontCartAutoShipPage;
	private StoreFrontUpdateCartPage storeFrontUpdateCartPage;
	private String RFL_DB = null;
	private String RFO_DB = null;


	// Hybris Phase 2-2041 :: Version : 1 :: Add new billing profile on 'Billing Profile' page
	@Test
	public void testAddNewBillingProfileOnBillingProfilePage_2041() throws InterruptedException, SQLException{
		int randomNum = CommonUtils.getRandomNum(10000, 1000000);
		RFL_DB = driver.getDBNameRFL();
		RFO_DB = driver.getDBNameRFO();		
		int totalBillingAddressesFromDB = 0;
		List<Map<String, Object>> billingAddressCountList =  null;
		List<Map<String, Object>> randomConsultantList =  null;
		List<Map<String, Object>> defaultBillingAddressList =  null;
		String consultantEmailID = null;
		String address1=null;	
		String newBillingProfileName = TestConstants.NEW_BILLING_PROFILE_NAME_US+randomNum;
		String lastName = "lN";
		//		randomConsultantList = DBUtil.performDatabaseQuery(DBQueries.GET_RANDOM_CONSULTANT_EMAIL_ID_RFL,RFL_DB);
		//		consultantEmailID = (String) getValueFromQueryResult(randomConsultantList, "EmailAddress");
		consultantEmailID = "sharonvdk@msn.com";
		storeFrontHomePage = new StoreFrontHomePage(driver);
		storeFrontConsulatantPage = storeFrontHomePage.loginAsConsultant(consultantEmailID, TestConstants.CONSULTANT_PASSWORD_TST4);
		s_assert.assertTrue(storeFrontConsulatantPage.verifyConsultantPage(),"Consultant Page doesn't contain Welcome User Message");
		logger.info("login is successful");
		storeFrontConsulatantPage.clickOnWelcomeDropDown();
		storeFrontBillingInfoPage = storeFrontConsulatantPage.clickBillingInfoLinkPresentOnWelcomeDropDown();
		s_assert.assertTrue(storeFrontBillingInfoPage.verifyBillingInfoPageIsDisplayed(),"Billing Info page has not been displayed");
		storeFrontBillingInfoPage.clickAddNewBillingProfileLink();
		storeFrontBillingInfoPage.enterNewBillingCardNumber(TestConstants.CARD_NUMBER);
		storeFrontBillingInfoPage.enterNewBillingNameOnCard(newBillingProfileName+" "+lastName);
		storeFrontBillingInfoPage.selectNewBillingCardExpirationDate(TestConstants.CARD_EXP_MONTH, TestConstants.CARD_EXP_YEAR);
		storeFrontBillingInfoPage.enterNewBillingSecurityCode(TestConstants.SECURITY_CODE);
		storeFrontBillingInfoPage.selectNewBillingCardAddress();
		storeFrontBillingInfoPage.selectUseThisBillingProfileFutureAutoshipChkbox();
		storeFrontBillingInfoPage.clickOnSaveBillingProfile();

		// assert with RFL
		billingAddressCountList = DBUtil.performDatabaseQuery(DBQueries.callQueryWithArguement(DBQueries.GET_BILLING_ADDRESS_COUNT_QUERY_TST4,consultantEmailID),RFL_DB);
		totalBillingAddressesFromDB = (Integer) getValueFromQueryResult(billingAddressCountList, "count");
		logger.info("Total Billing Profiles from RFL DB are "+totalBillingAddressesFromDB);
		if(assertEqualsDB("Billing Addresses count on UI is different from RFL DB", totalBillingAddressesFromDB,storeFrontBillingInfoPage.getTotalBillingAddressesDisplayed(),RFL_DB)==false){
			//assert with RFO
			billingAddressCountList = DBUtil.performDatabaseQuery(DBQueries.callQueryWithArguement(DBQueries.GET_BILLING_ADDRESS_COUNT_QUERY,consultantEmailID),RFO_DB);
			totalBillingAddressesFromDB = (Integer) getValueFromQueryResult(billingAddressCountList, "count");
			logger.info("Total Billing Profiles from RFO DB are "+totalBillingAddressesFromDB);
			s_assert.assertEquals(totalBillingAddressesFromDB,storeFrontBillingInfoPage.getTotalBillingAddressesDisplayed(),"Billing Addresses count on UI is different from RFO DB");			
		}

		if(totalBillingAddressesFromDB > 1){
			defaultBillingAddressList = DBUtil.performDatabaseQuery(DBQueries.callQueryWithArguement(DBQueries.GET_DEFAULT_BILLING_ADDRESS_QUERY_TST4,consultantEmailID),RFL_DB);
			newBillingProfileName = (String) getValueFromQueryResult(defaultBillingAddressList, "Name");
			newBillingProfileName = newBillingProfileName.split(" ")[0];
			
			//assert with RFL
			if(assertTrueDB("Default Billing Address radio button as per RFL DB is not selected on UI", storeFrontBillingInfoPage.isDefaultBillingAddressSelected(newBillingProfileName),RFL_DB)==false){
				//assert with RFO
				defaultBillingAddressList = DBUtil.performDatabaseQuery(DBQueries.callQueryWithArguement(DBQueries.GET_DEFAULT_BILLING_ADDRESS_QUERY,consultantEmailID),RFO_DB);
				newBillingProfileName = (String) getValueFromQueryResult(defaultBillingAddressList, "AddressProfileName");
				newBillingProfileName = newBillingProfileName.split(" ")[0];
				s_assert.assertTrue(storeFrontBillingInfoPage.isDefaultBillingAddressSelected(newBillingProfileName),"Default Billing Address radio button as per RFO DB is not selected on UI");
			}
		}

		storeFrontConsulatantPage.clickOnWelcomeDropDown();
		storeFrontOrdersPage = storeFrontConsulatantPage.clickOrdersLinkPresentOnWelcomeDropDown();
		storeFrontOrdersPage.clickAutoshipOrderNumber();
		s_assert.assertTrue(storeFrontOrdersPage.isPaymentMethodContainsName(newBillingProfileName),"autoship order payment name is not as expected");
		storeFrontCartAutoShipPage = storeFrontConsulatantPage.clickNextCRP();
		storeFrontUpdateCartPage = storeFrontCartAutoShipPage.clickUpdateMoreInfoLink();
		storeFrontUpdateCartPage.clickOnEditPaymentBillingProfile();
		s_assert.assertTrue(storeFrontUpdateCartPage.isNewBillingProfileIsSelectedByDefault(newBillingProfileName),"New Billing Profile is not selected by default on update cart page");
		storeFrontConsulatantPage = storeFrontUpdateCartPage.clickRodanAndFieldsLogo();
		logout();
		s_assert.assertAll();		
	}

	// Hybris Phase 2-2042 :: Version : 1 :: Add billing profile during checkout 
	public void testAddBillingProfileDuringCheckout(){
		//TODO
	}

	// Hybris Phase 2-2045 :: Version : 1 :: Add billing address during consultant enrollment
	public void testAddBillingAddressDuringConsultantEnrollment(){
		//TODO
	}

	// Hybris Phase 2-2046 :: Version : 1 :: Add billing profile during CRP enrollment through my account
	public void testAddBillingProfileDuringCRPEnrollmentMyAccount(){
		//TODO
	}

	//Hybris Phase 2-4327:View new billing profile on 'Billing Profile' page	
	@Test(enabled=false)
	public void testViewNewBillingProfile_HP2_4327() throws InterruptedException, SQLException{
		RFL_DB = driver.getDBNameRFL();
		RFO_DB = driver.getDBNameRFO();		
		int totalBillingAddressesFromDB = 0;
		List<Map<String, Object>> billingAddressCountList =  null;
		List<Map<String, Object>> defaultBillingAddressList =  null;
		List<Map<String, Object>> randomConsultantList =  null;
		String address1=null;		
		String consultantEmailID = null;
		String newBillingProfileName = null;
		//		randomConsultantList = DBUtil.performDatabaseQuery(DBQueries.GET_RANDOM_CONSULTANT_EMAIL_ID_RFL,RFL_DB);
		//		consultantEmailID = (String) getValueFromQueryResult(randomConsultantList, "EmailAddress");
		consultantEmailID = "sharonvdk@msn.com";
		storeFrontHomePage = new StoreFrontHomePage(driver);
		storeFrontConsulatantPage = storeFrontHomePage.loginAsConsultant(consultantEmailID, TestConstants.CONSULTANT_PASSWORD_TST4);
		s_assert.assertTrue(storeFrontConsulatantPage.verifyConsultantPage(),"Consultant Page doesn't contain Welcome User Message");
		logger.info("login is successful");
		storeFrontConsulatantPage.clickOnWelcomeDropDown();
		storeFrontBillingInfoPage = storeFrontConsulatantPage.clickBillingInfoLinkPresentOnWelcomeDropDown();
		s_assert.assertTrue(storeFrontBillingInfoPage.verifyBillingInfoPageIsDisplayed(),"Billing Info page has not been displayed");

		// assert with RFL
		billingAddressCountList = DBUtil.performDatabaseQuery(DBQueries.callQueryWithArguement(DBQueries.GET_BILLING_ADDRESS_COUNT_QUERY_TST4,consultantEmailID),RFL_DB);
		totalBillingAddressesFromDB = (Integer) getValueFromQueryResult(billingAddressCountList, "count");
		logger.info("Total Billing Profiles from RFL DB are "+totalBillingAddressesFromDB);
		if(assertEqualsDB("Billing Addresses count on UI is different from RFL DB", totalBillingAddressesFromDB,storeFrontBillingInfoPage.getTotalBillingAddressesDisplayed(),RFL_DB)==false){
			//assert with RFO
			billingAddressCountList = DBUtil.performDatabaseQuery(DBQueries.callQueryWithArguement(DBQueries.GET_BILLING_ADDRESS_COUNT_QUERY,consultantEmailID),RFO_DB);
			totalBillingAddressesFromDB = (Integer) getValueFromQueryResult(billingAddressCountList, "count");
			logger.info("Total Billing Profiles from RFO DB are "+totalBillingAddressesFromDB);
			s_assert.assertEquals(totalBillingAddressesFromDB,storeFrontBillingInfoPage.getTotalBillingAddressesDisplayed(),"Billing Addresses count on UI is different from RFO DB");			
		}

		if(totalBillingAddressesFromDB > 1){
			defaultBillingAddressList = DBUtil.performDatabaseQuery(DBQueries.callQueryWithArguement(DBQueries.GET_DEFAULT_BILLING_ADDRESS_QUERY_TST4,consultantEmailID),RFL_DB);
			newBillingProfileName = (String) getValueFromQueryResult(defaultBillingAddressList, "Name");
			newBillingProfileName = newBillingProfileName.split(" ")[0];
			System.out.println("----------- "+newBillingProfileName);
			//assert with RFL
			if(assertTrueDB("Default Billing Address radio button as per RFL DB is not selected on UI", storeFrontBillingInfoPage.isDefaultBillingAddressSelected(newBillingProfileName),RFL_DB)==false){
				//assert with RFO
				defaultBillingAddressList = DBUtil.performDatabaseQuery(DBQueries.callQueryWithArguement(DBQueries.GET_DEFAULT_BILLING_ADDRESS_QUERY,consultantEmailID),RFO_DB);
				newBillingProfileName = (String) getValueFromQueryResult(defaultBillingAddressList, "AddressProfileName");
				newBillingProfileName = newBillingProfileName.split(" ")[0];
				s_assert.assertTrue(storeFrontBillingInfoPage.isDefaultBillingAddressSelected(newBillingProfileName),"Default Billing Address radio button as per RFO DB is not selected on UI");
			}
		}

		logout();
		s_assert.assertAll();
	}

}
