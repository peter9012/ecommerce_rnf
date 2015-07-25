package com.rf.test.website.storeFront;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.text.WordUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.annotations.Test;
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

public class RFBillingInfoTest extends RFWebsiteBaseTest{
	private static final Logger logger = LogManager
			.getLogger(RFBillingInfoTest.class.getName());

	private StoreFrontHomePage storeFrontHomePage;
	private StoreFrontConsultantPage storeFrontConsulatantPage;
	private StoreFrontBillingInfoPage storeFrontBillingInfoPage;
	private StoreFrontOrdersPage storeFrontOrdersPage;
	private StoreFrontCartAutoShipPage storeFrontCartAutoShipPage;
	private StoreFrontUpdateCartPage storeFrontUpdateCartPage;
	private String RFL_DB = null;
	private String RFO_DB = null;

	//Hybris Phase 2-4327:View new billing profile on 'Billing Profile' page	
	@Test
	public void testViewNewBillingProfile_HP2_4327() throws InterruptedException, SQLException{
		RFL_DB = driver.getDBNameRFL();
		RFO_DB = driver.getDBNameRFO();		
		int totalBillingAddressesFromDB = 0;
		List<Map<String, Object>> billingAddressCountList =  null;
		List<Map<String, Object>> defaultBillingAddressList =  null;
		String address1=null;
		storeFrontHomePage = new StoreFrontHomePage(driver);
		storeFrontConsulatantPage = storeFrontHomePage.loginAsConsultant(TestConstants.CONSULTANT_EMAIL_ID_TST4, TestConstants.CONSULTANT_PASSWORD_TST4);
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
			assertEquals("Billing Addresses count on UI is different from DB", totalBillingAddressesFromDB,storeFrontBillingInfoPage.getTotalBillingAddressesDisplayed());			
		}

		if(totalBillingAddressesFromDB > 1){
			defaultBillingAddressList = DBUtil.performDatabaseQuery(DBQueries.callQueryWithArguement(DBQueries.GET_DEFAULT_BILLING_ADDRESS_QUERY_TST4,TestConstants.CONSULTANT_EMAIL_ID_TST4),RFL_DB);
			address1 = (String) getValueFromQueryResult(defaultBillingAddressList, "Address1");
			//assert with RFL
			if(assertTrueDB("Default radio button in Billing page is not selected", storeFrontBillingInfoPage.isDefaultBillingAddressSelected(address1),RFL_DB)==false){
				//assert with RFO
				defaultBillingAddressList = DBUtil.performDatabaseQuery(DBQueries.callQueryWithArguement(DBQueries.GET_DEFAULT_BILLING_ADDRESS_QUERY,TestConstants.CONSULTANT_EMAIL_ID_TST4),RFO_DB);
				address1 = (String) getValueFromQueryResult(defaultBillingAddressList, "AddressLine1");
				s_assert.assertTrue(storeFrontBillingInfoPage.isDefaultBillingAddressSelected(address1),"Default radio button in Billing page is not selected");
			}
		}

		logout();
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

	// Hybris Phase 2-2041 :: Version : 1 :: Add new billing profile on 'Billing Profile' page
	@Test
	public void testAddNewBillingProfileOnBillingProfilePage_2041() throws InterruptedException, SQLException{
		RFL_DB = driver.getDBNameRFL();
		RFO_DB = driver.getDBNameRFO();		
		int totalBillingAddressesFromDB = 0;
		List<Map<String, Object>> billingAddressCountList =  null;
		storeFrontHomePage = new StoreFrontHomePage(driver);
		storeFrontConsulatantPage = storeFrontHomePage.loginAsConsultant(TestConstants.CONSULTANT_EMAIL_ID_TST4, TestConstants.CONSULTANT_PASSWORD_TST4);
		s_assert.assertTrue(storeFrontConsulatantPage.verifyConsultantPage(),"Consultant Page doesn't contain Welcome User Message");
		storeFrontConsulatantPage.clickOnWelcomeDropDown();
		storeFrontBillingInfoPage = storeFrontConsulatantPage.clickBillingInfoLinkPresentOnWelcomeDropDown();
		s_assert.assertTrue(storeFrontBillingInfoPage.verifyBillingInfoPageIsDisplayed(),"Billing Info page has not been displayed");
		storeFrontBillingInfoPage.clickAddNewBillingProfileLink();
		storeFrontBillingInfoPage.enterNewBillingCardNumber(TestConstants.CARD_NUMBER);
		storeFrontBillingInfoPage.enterNewBillingNameOnCard(TestConstants.FIRST_NAME_NEW_BILLING_ADDRESS);
		storeFrontBillingInfoPage.selectNewBillingCardExpirationDate(TestConstants.CARD_EXP_MONTH, TestConstants.CARD_EXP_YEAR);
		storeFrontBillingInfoPage.enterNewBillingSecurityCode(TestConstants.SECURITY_CODE);
		storeFrontBillingInfoPage.selectNewBillingCardAddress();
		storeFrontBillingInfoPage.selectUseThisBillingProfileFutureAutoshipChkbox();
		storeFrontBillingInfoPage.clickOnSaveBillingProfile();

		// assert with RFL
		billingAddressCountList = DBUtil.performDatabaseQuery(DBQueries.callQueryWithArguement(DBQueries.GET_BILLING_ADDRESS_COUNT_QUERY_TST4,TestConstants.CONSULTANT_EMAIL_ID_TST4),RFL_DB);
		totalBillingAddressesFromDB = (Integer) getValueFromQueryResult(billingAddressCountList, "count");
		if(assertEqualsDB("Billing Addresses count on UI is different from RFL DB", totalBillingAddressesFromDB,storeFrontBillingInfoPage.getTotalBillingAddressesDisplayed(),RFL_DB)==false){
			//assert with RFO
			billingAddressCountList = DBUtil.performDatabaseQuery(DBQueries.callQueryWithArguement(DBQueries.GET_BILLING_ADDRESS_COUNT_QUERY,TestConstants.CONSULTANT_EMAIL_ID_TST4),RFO_DB);
			totalBillingAddressesFromDB = (Integer) getValueFromQueryResult(billingAddressCountList, "count");			
			assertEquals("Billing Addresses count on UI is different from RFO DB", totalBillingAddressesFromDB,storeFrontBillingInfoPage.getTotalBillingAddressesDisplayed());			
		}

		storeFrontConsulatantPage.clickOnWelcomeDropDown();
		storeFrontOrdersPage = storeFrontConsulatantPage.clickOrdersLinkPresentOnWelcomeDropDown();
		storeFrontOrdersPage.clickAutoshipOrderNumber();
		s_assert.assertTrue(storeFrontOrdersPage.isPaymentMethodContainsName(TestConstants.FIRST_NAME_NEW_BILLING_ADDRESS),"autoship order payment name is not as expected");
		storeFrontCartAutoShipPage = storeFrontConsulatantPage.clickNextCRP();
		storeFrontUpdateCartPage = storeFrontCartAutoShipPage.clickUpdateMoreInfoLink();
		storeFrontUpdateCartPage.clickOnEditPaymentBillingProfile();
		s_assert.assertTrue(storeFrontUpdateCartPage.isNewBillingProfileIsSelectedByDefault(TestConstants.FIRST_NAME_NEW_BILLING_ADDRESS),"New Billing Profile is not selected by default on update cart page");
		storeFrontConsulatantPage = storeFrontUpdateCartPage.clickRodanAndFieldsLogo();
		logout();
		s_assert.assertAll();
	}


	// Hybris Phase 2-2047 :: Version : 1 :: Edit billing profile on 'Billing Profile' page 
	@Test
	public void testEditBillingProfileOnBillingProfilePage_2047() throws InterruptedException, SQLException{
		storeFrontHomePage = new StoreFrontHomePage(driver);
		storeFrontConsulatantPage = storeFrontHomePage.loginAsConsultant(TestConstants.CONSULTANT_EMAIL_ID_TST4, TestConstants.CONSULTANT_PASSWORD_TST4);
		s_assert.assertTrue(storeFrontConsulatantPage.verifyConsultantPage(),"Consultant Page doesn't contain Welcome User Message");
		storeFrontConsulatantPage.clickOnWelcomeDropDown();
		storeFrontBillingInfoPage = storeFrontConsulatantPage.clickBillingInfoLinkPresentOnWelcomeDropDown();
		s_assert.assertTrue(storeFrontBillingInfoPage.verifyBillingInfoPageIsDisplayed(),"Billing Info page has not been displayed");
		String initialBillingProfileName =  storeFrontBillingInfoPage.getBillingProfileName();
		initialBillingProfileName = WordUtils.uncapitalize(initialBillingProfileName);
		storeFrontBillingInfoPage.clickOnEditBillingProfile();
		storeFrontBillingInfoPage.enterNewBillingNameOnCard(TestConstants.FIRST_NAME_NEW_BILLING_ADDRESS);
		storeFrontBillingInfoPage.enterNewBillingSecurityCode(TestConstants.SECURITY_CODE);
		storeFrontBillingInfoPage.selectNewBillingCardAddress();
		storeFrontBillingInfoPage.selectUseThisBillingProfileFutureAutoshipChkbox();
		storeFrontBillingInfoPage.clickOnSaveBillingProfile();		
		storeFrontConsulatantPage.clickOnWelcomeDropDown();
		storeFrontOrdersPage = storeFrontConsulatantPage.clickOrdersLinkPresentOnWelcomeDropDown();
		storeFrontOrdersPage.clickAutoshipOrderNumber();
		s_assert.assertTrue(storeFrontOrdersPage.isPaymentMethodContainsName(TestConstants.FIRST_NAME_NEW_BILLING_ADDRESS),"autoship order payment name is not as expected");
		storeFrontCartAutoShipPage = storeFrontConsulatantPage.clickNextCRP();
		storeFrontUpdateCartPage = storeFrontCartAutoShipPage.clickUpdateMoreInfoLink();
		storeFrontUpdateCartPage.clickOnEditPaymentBillingProfile();
		s_assert.assertTrue(storeFrontUpdateCartPage.isNewBillingProfileIsSelectedByDefault(TestConstants.FIRST_NAME_NEW_BILLING_ADDRESS),"New Billing Address is not selected by default");
		storeFrontConsulatantPage = storeFrontUpdateCartPage.clickRodanAndFieldsLogo();

		storeFrontConsulatantPage.clickOnWelcomeDropDown();
		storeFrontBillingInfoPage = storeFrontConsulatantPage.clickBillingInfoLinkPresentOnWelcomeDropDown();
		s_assert.assertTrue(storeFrontBillingInfoPage.verifyBillingInfoPageIsDisplayed(),"Billing Info page has not been displayed");
		storeFrontBillingInfoPage.clickOnEditBillingProfile();
		storeFrontBillingInfoPage.enterNewBillingNameOnCard(initialBillingProfileName);
		storeFrontBillingInfoPage.enterNewBillingSecurityCode(TestConstants.SECURITY_CODE);
		storeFrontBillingInfoPage.selectNewBillingCardAddress();
		storeFrontBillingInfoPage.selectUseThisBillingProfileFutureAutoshipChkbox();
		storeFrontBillingInfoPage.clickOnSaveBillingProfile();
		logout();
		s_assert.assertAll();
	}

	// Hybris Phase 2-2049 :: Version : 1 :: Edit billing profile in autoship template
	@Test
	public void testEditBillingProfileInAutoshipTemplate_2049() throws InterruptedException, SQLException{
		storeFrontHomePage = new StoreFrontHomePage(driver);
		storeFrontConsulatantPage = storeFrontHomePage.loginAsConsultant(TestConstants.CONSULTANT_EMAIL_ID_TST4, TestConstants.CONSULTANT_PASSWORD_TST4);
		s_assert.assertTrue(storeFrontConsulatantPage.verifyConsultantPage(),"Consultant Page doesn't contain Welcome User Message");
		storeFrontConsulatantPage.clickOnWelcomeDropDown();
		storeFrontCartAutoShipPage = storeFrontConsulatantPage.clickNextCRP();
		storeFrontUpdateCartPage = storeFrontCartAutoShipPage.clickUpdateMoreInfoLink();
		storeFrontUpdateCartPage.clickOnEditPaymentBillingProfile();
		String initialBillingProfileName =  storeFrontUpdateCartPage.getBillingProfileName();
		initialBillingProfileName = WordUtils.uncapitalize(initialBillingProfileName);
		System.out.println("initialBillingProfileName= "+initialBillingProfileName);
		storeFrontUpdateCartPage.clickOnEditBillingProfile(initialBillingProfileName);
		storeFrontUpdateCartPage.enterNewBillingNameOnCard(TestConstants.FIRST_NAME_NEW_BILLING_ADDRESS);
		storeFrontUpdateCartPage.enterNewBillingSecurityCode(TestConstants.SECURITY_CODE);
		storeFrontUpdateCartPage.selectNewBillingCardAddress();
		storeFrontUpdateCartPage.selectUseThisBillingProfileFutureAutoshipChkbox();
		storeFrontUpdateCartPage.clickOnSaveBillingProfile();
		// Verify that The profile should have been saved and should be listed in payment section having 'bill to this card' radio button selected
		storeFrontUpdateCartPage.clickOnNextStepBtn();
		storeFrontUpdateCartPage.clickUpdateCartBtn();
		storeFrontConsulatantPage = storeFrontUpdateCartPage.clickRodanAndFieldsLogo();
		storeFrontConsulatantPage.clickOnWelcomeDropDown();
		storeFrontOrdersPage = storeFrontConsulatantPage.clickOrdersLinkPresentOnWelcomeDropDown();
		storeFrontOrdersPage.clickAutoshipOrderNumber();
		s_assert.assertTrue(storeFrontOrdersPage.isPaymentMethodContainsName(TestConstants.FIRST_NAME_NEW_BILLING_ADDRESS),"autoship order payment name is not as expected");
		logout();
		s_assert.assertAll();
	}
}

