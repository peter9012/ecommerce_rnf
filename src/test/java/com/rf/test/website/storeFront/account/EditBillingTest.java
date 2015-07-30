package com.rf.test.website.storeFront.account;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.text.WordUtils;
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

public class EditBillingTest extends RFWebsiteBaseTest{
	private static final Logger logger = LogManager
			.getLogger(EditBillingTest.class.getName());

	private StoreFrontHomePage storeFrontHomePage;
	private StoreFrontConsultantPage storeFrontConsulatantPage;
	private StoreFrontBillingInfoPage storeFrontBillingInfoPage;
	private StoreFrontOrdersPage storeFrontOrdersPage;
	private StoreFrontCartAutoShipPage storeFrontCartAutoShipPage;
	private StoreFrontUpdateCartPage storeFrontUpdateCartPage;
	private String RFL_DB = null;
	private String RFO_DB = null;

	// Hybris Phase 2-2047 :: Version : 1 :: Edit billing profile on 'Billing Profile' page 
	@Test
	public void testEditBillingProfileOnBillingProfilePage_2047() throws InterruptedException, SQLException{
		int randomNum = CommonUtils.getRandomNum(10000, 1000000);
		RFL_DB = driver.getDBNameRFL();
		RFO_DB = driver.getDBNameRFO();
		List<Map<String, Object>> randomConsultantList =  null;
		List<Map<String, Object>> defaultBillingAddressList =  null;
		String consultantEmailID = null;
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
		String initialBillingProfileName =  storeFrontBillingInfoPage.getBillingProfileName();
		initialBillingProfileName = WordUtils.uncapitalize(initialBillingProfileName);
		storeFrontBillingInfoPage.clickOnEditBillingProfile();
		storeFrontBillingInfoPage.enterNewBillingNameOnCard(newBillingProfileName+" "+lastName);
		storeFrontBillingInfoPage.enterNewBillingSecurityCode(TestConstants.SECURITY_CODE);
		storeFrontBillingInfoPage.selectNewBillingCardAddress();
		storeFrontBillingInfoPage.selectUseThisBillingProfileFutureAutoshipChkbox();
		storeFrontBillingInfoPage.clickOnSaveBillingProfile();

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

		storeFrontConsulatantPage.clickOnWelcomeDropDown();
		storeFrontOrdersPage = storeFrontConsulatantPage.clickOrdersLinkPresentOnWelcomeDropDown();
		storeFrontOrdersPage.clickAutoshipOrderNumber();
		s_assert.assertTrue(storeFrontOrdersPage.isPaymentMethodContainsName(newBillingProfileName),"autoship order payment name is not as expected");
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
	@Test(enabled=false)
	public void testEditBillingProfileInAutoshipTemplate_2049() throws InterruptedException, SQLException{
		RFL_DB = driver.getDBNameRFL();
		RFO_DB = driver.getDBNameRFO();
		List<Map<String, Object>> randomConsultantList =  null;
		String consultantEmailID = null;
		//		randomConsultantList = DBUtil.performDatabaseQuery(DBQueries.GET_RANDOM_CONSULTANT_EMAIL_ID_RFL,RFL_DB);
		//		consultantEmailID = (String) getValueFromQueryResult(randomConsultantList, "EmailAddress");
		consultantEmailID = "sharonvdk@msn.com";
		storeFrontHomePage = new StoreFrontHomePage(driver);
		storeFrontConsulatantPage = storeFrontHomePage.loginAsConsultant(consultantEmailID, TestConstants.CONSULTANT_PASSWORD_TST4);
		s_assert.assertTrue(storeFrontConsulatantPage.verifyConsultantPage(),"Consultant Page doesn't contain Welcome User Message");
		logger.info("login is successful");
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
