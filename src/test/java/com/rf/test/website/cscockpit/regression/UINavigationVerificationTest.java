package com.rf.test.website.cscockpit.regression;

import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.NoSuchElementException;
import org.testng.annotations.Test;

import com.rf.core.utils.CommonUtils;
import com.rf.core.utils.DBUtil;
import com.rf.core.website.constants.TestConstants;
import com.rf.core.website.constants.dbQueries.DBQueries_RFO;
import com.rf.pages.website.cscockpit.CSCockpitAutoshipCartTabPage;
import com.rf.pages.website.cscockpit.CSCockpitAutoshipSearchTabPage;
import com.rf.pages.website.cscockpit.CSCockpitAutoshipTemplateTabPage;
import com.rf.pages.website.cscockpit.CSCockpitAutoshipTemplateUpdateTabPage;
import com.rf.pages.website.cscockpit.CSCockpitCartTabPage;
import com.rf.pages.website.cscockpit.CSCockpitCheckoutTabPage;
import com.rf.pages.website.cscockpit.CSCockpitCustomerSearchTabPage;
import com.rf.pages.website.cscockpit.CSCockpitCustomerTabPage;
import com.rf.pages.website.cscockpit.CSCockpitLoginPage;
import com.rf.pages.website.cscockpit.CSCockpitOrderSearchTabPage;
import com.rf.pages.website.cscockpit.CSCockpitOrderTabPage;
import com.rf.pages.website.storeFront.StoreFrontAccountInfoPage;
import com.rf.pages.website.storeFront.StoreFrontAccountTerminationPage;
import com.rf.pages.website.storeFront.StoreFrontBillingInfoPage;
import com.rf.pages.website.storeFront.StoreFrontConsultantPage;
import com.rf.pages.website.storeFront.StoreFrontHomePage;
import com.rf.pages.website.storeFront.StoreFrontOrdersPage;
import com.rf.pages.website.storeFront.StoreFrontPCUserPage;
import com.rf.pages.website.storeFront.StoreFrontRCUserPage;
import com.rf.pages.website.storeFront.StoreFrontUpdateCartPage;
import com.rf.test.website.RFWebsiteBaseTest;

public class UINavigationVerificationTest extends RFWebsiteBaseTest{
	private static final Logger logger = LogManager
			.getLogger(UINavigationVerificationTest.class.getName());

	//-------------------------------------------------Pages---------------------------------------------------------
	private CSCockpitLoginPage cscockpitLoginPage;	
	private CSCockpitAutoshipSearchTabPage cscockpitAutoshipSearchTabPage;
	private CSCockpitCheckoutTabPage cscockpitCheckoutTabPage;
	private CSCockpitCustomerSearchTabPage cscockpitCustomerSearchTabPage;
	private CSCockpitCustomerTabPage cscockpitCustomerTabPage;
	private CSCockpitOrderSearchTabPage cscockpitOrderSearchTabPage;
	private CSCockpitOrderTabPage cscockpitOrderTabPage;
	private CSCockpitCartTabPage cscockpitCartTabPage;
	private CSCockpitAutoshipTemplateTabPage cscockpitAutoshipTemplateTabPage;
	private CSCockpitAutoshipCartTabPage cscockpitAutoshipCartTabPage;
	private CSCockpitAutoshipTemplateUpdateTabPage cscockpitAutoshipTemplateUpdateTabPage;
	private StoreFrontHomePage storeFrontHomePage; 
	private StoreFrontConsultantPage storeFrontConsultantPage;
	private StoreFrontOrdersPage storeFrontOrdersPage;
	private StoreFrontPCUserPage storeFrontPCUserPage;
	private StoreFrontRCUserPage storeFrontRCUserPage;	
	private StoreFrontUpdateCartPage storeFrontUpdateCartPage;
	private StoreFrontAccountInfoPage storeFrontAccountInfoPage;
	private StoreFrontBillingInfoPage storeFrontBillingInfoPage;
	private StoreFrontAccountTerminationPage storeFrontAccountTerminationPage;

	//-----------------------------------------------------------------------------------------------------------------

	public UINavigationVerificationTest() {
		cscockpitLoginPage = new CSCockpitLoginPage(driver);
		cscockpitAutoshipSearchTabPage = new CSCockpitAutoshipSearchTabPage(driver);
		cscockpitCheckoutTabPage = new CSCockpitCheckoutTabPage(driver);
		cscockpitCustomerSearchTabPage = new CSCockpitCustomerSearchTabPage(driver);
		cscockpitCustomerTabPage = new CSCockpitCustomerTabPage(driver);
		cscockpitOrderSearchTabPage = new CSCockpitOrderSearchTabPage(driver);
		cscockpitOrderTabPage = new CSCockpitOrderTabPage(driver);
		cscockpitCartTabPage = new CSCockpitCartTabPage(driver);
		cscockpitAutoshipTemplateTabPage = new CSCockpitAutoshipTemplateTabPage(driver);
		cscockpitAutoshipCartTabPage = new CSCockpitAutoshipCartTabPage(driver);
		cscockpitAutoshipTemplateUpdateTabPage = new CSCockpitAutoshipTemplateUpdateTabPage(driver);
		storeFrontHomePage = new StoreFrontHomePage(driver);
		storeFrontConsultantPage = new StoreFrontConsultantPage(driver);
		storeFrontOrdersPage = new StoreFrontOrdersPage(driver);
		storeFrontPCUserPage = new StoreFrontPCUserPage(driver);
		storeFrontRCUserPage = new StoreFrontRCUserPage(driver);
		storeFrontUpdateCartPage = new StoreFrontUpdateCartPage(driver);
		storeFrontAccountInfoPage = new StoreFrontAccountInfoPage(driver);
		storeFrontAccountTerminationPage = new StoreFrontAccountTerminationPage(driver);
	}

	private String RFO_DB = null;

	//Hybris Project-1837:To verify that change consultant Receiving Commissions can be done for corporate sponsor for RC
	@Test(enabled=false)//WIP
	public void testToVerifyChangeTheConsultantRecievingCommisionsCanBeDoneForCorporateSponsorForRC_1837() throws InterruptedException{
		String randomOrderSequenceNumber = null;
		RFO_DB = driver.getDBNameRFO();
		String accountNumber = null;
		String accountIDForChangeConsultantReceivingCommissionsPopUP = null;
		String rcUserEmailID = null;
		String accountID = null;
		List<Map<String, Object>> randomRCList =  null;
		List<Map<String, Object>> randomRCUsernameList =  null;
		List<Map<String, Object>> otherDetailsForChangeConsultantReceivingCommissionsPopUP =  null;
		List<Map<String, Object>> randomRCListForChangeConsultantReceivingCommissionsPopUP =  null;
		cscockpitLoginPage = new CSCockpitLoginPage(driver);

		//-----------------------------------------------------For RC User ------------------------------------------------------------//

		randomRCList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_RC_EMAIL_ID_HAVING_ACTIVE_ORDER_RFO,"236"),RFO_DB);
		accountID = String.valueOf(getValueFromQueryResult(randomRCList, "AccountID"));

		randomRCUsernameList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_EMAIL_ID_FROM_ACCOUNT_ID,accountID),RFO_DB);
		rcUserEmailID = String.valueOf(getValueFromQueryResult(randomRCUsernameList, "EmailAddress"));

		randomRCListForChangeConsultantReceivingCommissionsPopUP = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_CONSULTANT_WITH_ORDERS_AND_AUTOSHIPS_RFO,"236"),RFO_DB);
		accountIDForChangeConsultantReceivingCommissionsPopUP = String.valueOf(getValueFromQueryResult(randomRCListForChangeConsultantReceivingCommissionsPopUP, "AccountID"));

		otherDetailsForChangeConsultantReceivingCommissionsPopUP = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_ACCOUNT_NUMBER_FOR_PWS,accountIDForChangeConsultantReceivingCommissionsPopUP),RFO_DB);
		accountNumber = String.valueOf(getValueFromQueryResult(otherDetailsForChangeConsultantReceivingCommissionsPopUP, "AccountNumber"));

		cscockpitLoginPage.enterUsername(TestConstants.CS_SALES_SUPERVISORY_USERNAME);
		cscockpitCustomerSearchTabPage = cscockpitLoginPage.clickLoginBtn();
		cscockpitCustomerSearchTabPage.enterEmailIdInSearchFieldInCustomerSearchTab(rcUserEmailID);
		cscockpitCustomerSearchTabPage.clickSearchBtn();
		randomOrderSequenceNumber = String.valueOf(cscockpitOrderSearchTabPage.getRandomOrdersFromOrderResultSearchFirstPageInOrderSearchTab());
		cscockpitOrderSearchTabPage.clickAndReturnCIDNumberInCustomerSearchTab(randomOrderSequenceNumber);
		cscockpitCustomerTabPage.clickFirstOrderInCustomerTab();
		String consultantReceivingCommissionsNameBeforeEdit = cscockpitOrderTabPage.getConsultantReceivingCommissionsName();
		cscockpitOrderTabPage.clickChangeSponserLinkInOrderTab();

		s_assert.assertTrue(cscockpitOrderTabPage.verifyClickingChangeLinkChangeConsultantReceivingCommissionsPopUPPresentInOrderTab(), "Change Consultant Receiving Commissions PopUP is not Present In OrderTab");

		cscockpitOrderTabPage.enterAccountNumberInConsultantNameOrCIDOfChangeConsultantReceivingCommissionsPopUPInOrderTab(accountNumber);
		cscockpitOrderTabPage.clickSearchButtonInChangeConsultantReceivingCommissionsPopUPInOrderTab();
		String consultantNameFromChangeReceivingCommissionsPopUp = cscockpitOrderTabPage.getCIDFirstAndLastNameFromChangeConsultantReceivingCommissionsPopUPInOrderTab();
		cscockpitOrderTabPage.clickSelectButtonInChangeConsultantReceivingCommissionsPopUPInOrderTab();
		String consultantReceivingCommissionsNameAfterEdit = cscockpitOrderTabPage.getConsultantReceivingCommissionsName();

		s_assert.assertTrue(consultantReceivingCommissionsNameAfterEdit.contains(consultantNameFromChangeReceivingCommissionsPopUp), "Consultant Receiving Commissions Expected "+consultantNameFromChangeReceivingCommissionsPopUp+" & Actual is "+consultantReceivingCommissionsNameAfterEdit+" & Before Edit it was "+ consultantReceivingCommissionsNameBeforeEdit);
		s_assert.assertAll();
	}

	//Hybris Project-1835:Autoship template page for cancelled Pulse change consultant Receiving Commissions should not happen
	@Test(enabled=false)//WIP
	public void testAutoshipTemplatePageForCancelledPulseChangeConsultantRecievingCommissionsShouldNotHappen_1835() throws InterruptedException{
		String randomOrderSequenceNumber = null;
		RFO_DB = driver.getDBNameRFO();
		String consultantEmailID = null;
		String accountID = null;
		List<Map<String, Object>> randomConsultantList =  null;
		List<Map<String, Object>> randomConsultantUsernameList =  null;
		cscockpitLoginPage = new CSCockpitLoginPage(driver);
		driver.get(driver.getStoreFrontURL()+"/us");
		storeFrontHomePage = new StoreFrontHomePage(driver);

		//----------------------------------------For Consultant ------------------------------------------------------------//

		while(true){
			randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_CONSULTANT_WITH_ORDERS_AND_AUTOSHIPS_RFO,"236"),RFO_DB);
			accountID = String.valueOf(getValueFromQueryResult(randomConsultantList, "AccountID"));
			randomConsultantUsernameList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_EMAIL_ID_FROM_ACCOUNT_ID,accountID),RFO_DB);
			consultantEmailID = String.valueOf(getValueFromQueryResult(randomConsultantUsernameList, "EmailAddress"));
			storeFrontConsultantPage = storeFrontHomePage.loginAsConsultant(consultantEmailID, password);
			boolean isLoginError = driver.getCurrentUrl().contains("error");
			if(isLoginError){
				logger.info("Login error for the user "+consultantEmailID);
				driver.get(driver.getStoreFrontURL()+"/us");
			}
			else
				storeFrontConsultantPage.clickOnWelcomeDropDown();
			storeFrontAccountInfoPage = storeFrontConsultantPage.clickAccountInfoLinkPresentOnWelcomeDropDown();
			storeFrontAccountInfoPage.clickOnYourAccountDropdown();
			storeFrontAccountInfoPage.clickOnCancelMyPulseSubscription();
			break;
		}
		logger.info("emaild of consultant username "+consultantEmailID);
		logger.info("login is successful");
		cscockpitLoginPage = new CSCockpitLoginPage(driver);
		driver.get(driver.getCSCockpitURL());
		cscockpitLoginPage.enterUsername(TestConstants.CS_SALES_SUPERVISORY_USERNAME);
		cscockpitCustomerSearchTabPage = cscockpitLoginPage.clickLoginBtn();
		cscockpitCustomerSearchTabPage.enterEmailIdInSearchFieldInCustomerSearchTab(consultantEmailID);
		cscockpitCustomerSearchTabPage.clickSearchBtn();
		randomOrderSequenceNumber = String.valueOf(cscockpitOrderSearchTabPage.getRandomOrdersFromOrderResultSearchFirstPageInOrderSearchTab());
		cscockpitCustomerSearchTabPage.clickCIDNumberInCustomerSearchTab(randomOrderSequenceNumber);
		cscockpitCustomerTabPage.getAndClickPulseTemplateAutoshipIDHavingStatusIsCancelled();

		s_assert.assertTrue(cscockpitAutoshipTemplateTabPage.getAutoShipTemplateStatus().equalsIgnoreCase("CANCELLED"), "Pulse Autoship Template ID is not Cancelled");
		s_assert.assertTrue(cscockpitAutoshipTemplateTabPage.getAutoShipTemplateType().equalsIgnoreCase("Pulse Autoship Template"),"Actual Template Type is "+cscockpitAutoshipTemplateTabPage.getAutoShipTemplateType() +" & Expected Template Type is Pulse Autoship Template");
		s_assert.assertFalse(cscockpitOrderTabPage.isChangeSponserLinkPresent(), "Change Link is Present on Order Tab Page for Consultant");

		s_assert.assertAll();
	}


	//Hybris Project-1829:To verify that in Order detail page consultant receiving cannot happen for cancelled CRP
	@Test(enabled=false)//WIP
	public void testToVerifyOrderDetailPageConsultantRecievingCannotHappenForCancelledCRP_1829(){
		String randomOrderSequenceNumber = null;
		RFO_DB = driver.getDBNameRFO();
		cscockpitLoginPage = new CSCockpitLoginPage(driver);
		cscockpitCustomerSearchTabPage = cscockpitLoginPage.clickLoginBtn();
		cscockpitCustomerSearchTabPage.clickFindOrderLinkOnLeftNavigation();
		cscockpitOrderSearchTabPage.selectOrderTypeOnOrderSearchTab("CRP Autoship");
		cscockpitOrderSearchTabPage.selectOrderStatusOnOrderSearchTab("Cancelled");
		cscockpitOrderSearchTabPage.clickSearchBtn();

		s_assert.assertTrue(cscockpitOrderSearchTabPage.isOrderSearchLabelPresent("Order #"), "Order # is not Present on Order Search Page");
		s_assert.assertTrue(cscockpitOrderSearchTabPage.verifyOrderNumberSectionIsPresentWithClickableLinksInOrderSearchTab(), "Order # is not Clickable");
		s_assert.assertTrue(cscockpitOrderSearchTabPage.isOrderSearchLabelPresent("Order Date"), "Order Date is not Present on Order Search Page");
		s_assert.assertTrue(cscockpitOrderSearchTabPage.isOrderSearchLabelPresent("First Name"), "First Name is not Present on Order Search Page");
		s_assert.assertTrue(cscockpitOrderSearchTabPage.isOrderSearchLabelPresent("Last Name"), "Last Name is not Present on Order Search Page");
		s_assert.assertTrue(cscockpitOrderSearchTabPage.isOrderSearchLabelPresent("R+F CID"), "R+F CID is not Present on Order Search Page");
		s_assert.assertTrue(cscockpitOrderSearchTabPage.isOrderSearchLabelPresent("Email Address"), "Email Address is not Present on Order Search Page");
		s_assert.assertTrue(cscockpitOrderSearchTabPage.isOrderSearchLabelPresent("Order Type"), "Order Type is not Present on Order Search Page");
		s_assert.assertTrue(cscockpitOrderSearchTabPage.isOrderSearchLabelPresent("Order Status"), "Order Status is not Present on Order Search Page");
		s_assert.assertTrue(cscockpitOrderSearchTabPage.isOrderSearchLabelPresent("Order Total"), "Order Total is not Present on Order Search Page");
		s_assert.assertTrue(cscockpitOrderSearchTabPage.isOrderSearchLabelPresent("Ship To Country"), "Ship To Country is not Present on Order Search Page");

		randomOrderSequenceNumber = String.valueOf(cscockpitOrderSearchTabPage.getRandomOrdersFromOrderResultSearchFirstPageInOrderSearchTab());
		cscockpitOrderSearchTabPage.clickOrderNumberInOrderSearchResultsInOrderSearchTab(randomOrderSequenceNumber);

		s_assert.assertFalse(cscockpitOrderTabPage.isChangeSponserLinkPresent(), "Change Link is Present on Order Tab Page");
		s_assert.assertAll();
	}

	// Hybris Project-1824:To verify that change the Consultant Receiving Commissions  not allowed for Inactive users
	@Test(enabled=false)//WIP
	public void testToVerifyChangeTheConsultantRecievingCommisionsNotAllowedForInactiveUsers_1824() throws InterruptedException{
		String randomOrdersCIDNumber = null;
		String randomOrderSequenceNumber = null;
		RFO_DB = driver.getDBNameRFO();
		String consultantEmailID = null;
		String pcUserEmailID = null;
		String rcUserEmailID = null;
		String accountID = null;
		List<Map<String, Object>> randomConsultantList =  null;
		List<Map<String, Object>> randomConsultantUsernameList =  null;
		List<Map<String, Object>> randomPCList =  null;
		List<Map<String, Object>> randomPCUsernameList =  null;
		List<Map<String, Object>> randomRCList =  null;
		List<Map<String, Object>> randomRCUsernameList =  null;
		cscockpitLoginPage = new CSCockpitLoginPage(driver);
		driver.get(driver.getStoreFrontURL()+"/us");
		storeFrontHomePage = new StoreFrontHomePage(driver);

		//----------------------------------------For Consultant ------------------------------------------------------------//

		while(true){
			randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_CONSULTANT_WITH_ORDERS_AND_AUTOSHIPS_RFO,"236"),RFO_DB);
			accountID = String.valueOf(getValueFromQueryResult(randomConsultantList, "AccountID"));
			randomConsultantUsernameList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_EMAIL_ID_FROM_ACCOUNT_ID,accountID),RFO_DB);
			consultantEmailID = String.valueOf(getValueFromQueryResult(randomConsultantUsernameList, "EmailAddress"));
			storeFrontConsultantPage = storeFrontHomePage.loginAsConsultant(consultantEmailID, password);
			boolean isLoginError = driver.getCurrentUrl().contains("error");
			if(isLoginError){
				logger.info("Login error for the user "+consultantEmailID);
				driver.get(driver.getStoreFrontURL()+"/us");
			}
			else
				storeFrontConsultantPage.clickOnWelcomeDropDown();
			storeFrontConsultantPage.clickAccountInfoLinkPresentOnWelcomeDropDown();
			storeFrontAccountInfoPage.clickOnYourAccountDropdown();
			storeFrontAccountInfoPage.clickTerminateMyAccount();
			storeFrontAccountTerminationPage.fillTheEntriesAndClickOnSubmitDuringTermination();
			storeFrontAccountTerminationPage.clickConfirmTerminationBtn();
			storeFrontAccountTerminationPage.clickOnCloseWindowAfterTermination();
			break;
		}
		logger.info("emaild of consultant username "+consultantEmailID);
		logger.info("login is successful");
		cscockpitLoginPage = new CSCockpitLoginPage(driver);
		driver.get(driver.getCSCockpitURL());
		cscockpitLoginPage.enterUsername(TestConstants.CS_SALES_SUPERVISORY_USERNAME);
		cscockpitCustomerSearchTabPage = cscockpitLoginPage.clickLoginBtn();
		cscockpitCustomerSearchTabPage.enterEmailIdInSearchFieldInCustomerSearchTab(consultantEmailID);
		cscockpitCustomerSearchTabPage.clickSearchBtn();
		randomOrderSequenceNumber = String.valueOf(cscockpitOrderSearchTabPage.getRandomOrdersFromOrderResultSearchFirstPageInOrderSearchTab());
		randomOrdersCIDNumber = cscockpitOrderSearchTabPage.getCIDOrderNumberInOrderSearchResultsInOrderSearchTab(randomOrderSequenceNumber);
		cscockpitCustomerSearchTabPage.clickFindOrderLinkOnLeftNavigation();
		cscockpitOrderSearchTabPage.enterCIDInOrderSearchTab(randomOrdersCIDNumber);
		cscockpitOrderSearchTabPage.clickSearchBtn();

		s_assert.assertTrue(cscockpitOrderSearchTabPage.isOrderSearchLabelPresent("Order #"), "Order # is not Present on Order Search Page for Consultant");
		s_assert.assertTrue(cscockpitOrderSearchTabPage.verifyOrderNumberSectionIsPresentWithClickableLinksInOrderSearchTab(), "Order # is not Clickable for Consultant");
		s_assert.assertTrue(cscockpitOrderSearchTabPage.isOrderSearchLabelPresent("Order Date"), "Order Date is not Present on Order Search Page for Consultant");
		s_assert.assertTrue(cscockpitOrderSearchTabPage.isOrderSearchLabelPresent("First Name"), "First Name is not Present on Order Search Page for Consultant");
		s_assert.assertTrue(cscockpitOrderSearchTabPage.isOrderSearchLabelPresent("Last Name"), "Last Name is not Present on Order Search Page for Consultant");
		s_assert.assertTrue(cscockpitOrderSearchTabPage.isOrderSearchLabelPresent("R+F CID"), "R+F CID is not Present on Order Search Page for Consultant");
		s_assert.assertTrue(cscockpitOrderSearchTabPage.isOrderSearchLabelPresent("Email Address"), "Email Address is not Present on Order Search Page for Consultant");
		s_assert.assertTrue(cscockpitOrderSearchTabPage.isOrderSearchLabelPresent("Order Type"), "Order Type is not Present on Order Search Page for Consultant");
		s_assert.assertTrue(cscockpitOrderSearchTabPage.isOrderSearchLabelPresent("Order Status"), "Order Status is not Present on Order Search Page for Consultant");
		s_assert.assertTrue(cscockpitOrderSearchTabPage.isOrderSearchLabelPresent("Order Total"), "Order Total is not Present on Order Search Page for Consultant");
		s_assert.assertTrue(cscockpitOrderSearchTabPage.isOrderSearchLabelPresent("Ship To Country"), "Ship To Country is not Present on Order Search Page for Consultant");

		randomOrderSequenceNumber = String.valueOf(cscockpitOrderSearchTabPage.getRandomOrdersFromOrderResultSearchFirstPageInOrderSearchTab());
		cscockpitOrderSearchTabPage.clickOrderNumberInOrderSearchResultsInOrderSearchTab(randomOrderSequenceNumber);

		s_assert.assertFalse(cscockpitOrderTabPage.isChangeSponserLinkPresent(), "Change Link is Present on Order Tab Page for Consultant");

		//----------------------------------------For PC User ------------------------------------------------------------//

		driver.get(driver.getStoreFrontURL()+"/us");
		while(true){
			randomPCList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_PC_WITH_ORDERS_AND_AUTOSHIPS_RFO,"236"),RFO_DB);
			accountID = String.valueOf(getValueFromQueryResult(randomPCList, "AccountID"));
			randomPCUsernameList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_EMAIL_ID_FROM_ACCOUNT_ID,accountID),RFO_DB);
			pcUserEmailID = String.valueOf(getValueFromQueryResult(randomPCUsernameList, "EmailAddress"));
			storeFrontPCUserPage = storeFrontHomePage.loginAsPCUser(pcUserEmailID, password);
			boolean isLoginError = driver.getCurrentUrl().contains("error");
			if(isLoginError){
				logger.info("Login error for the user "+pcUserEmailID);
				driver.get(driver.getStoreFrontURL()+"/us");
			}
			else
				storeFrontPCUserPage.clickOnWelcomeDropDown();
			storeFrontPCUserPage.clickAccountInfoLinkPresentOnWelcomeDropDown();
			storeFrontPCUserPage.clickOnYourAccountDropdown();
			storeFrontPCUserPage.clickOnPCPerksStatus();
			storeFrontPCUserPage.clickDelayOrCancelPCPerks();
			storeFrontPCUserPage.clickPleaseCancelMyPcPerksActBtn();
			storeFrontPCUserPage.cancelMyPCPerksAct();
			break;
		}
		logger.info("emaild of consultant username "+pcUserEmailID);
		logger.info("login is successful");
		driver.get(driver.getCSCockpitURL());
		cscockpitLoginPage.enterUsername(TestConstants.CS_SALES_SUPERVISORY_USERNAME);
		cscockpitCustomerSearchTabPage = cscockpitLoginPage.clickLoginBtn();
		cscockpitCustomerSearchTabPage.enterEmailIdInSearchFieldInCustomerSearchTab(pcUserEmailID);
		cscockpitCustomerSearchTabPage.clickSearchBtn();
		randomOrderSequenceNumber = String.valueOf(cscockpitOrderSearchTabPage.getRandomOrdersFromOrderResultSearchFirstPageInOrderSearchTab());
		randomOrdersCIDNumber = cscockpitOrderSearchTabPage.getCIDOrderNumberInOrderSearchResultsInOrderSearchTab(randomOrderSequenceNumber);
		cscockpitCustomerSearchTabPage.clickFindOrderLinkOnLeftNavigation();
		cscockpitOrderSearchTabPage.enterCIDInOrderSearchTab(randomOrdersCIDNumber);
		cscockpitOrderSearchTabPage.clickSearchBtn();

		s_assert.assertTrue(cscockpitOrderSearchTabPage.isOrderSearchLabelPresent("Order #"), "Order # is not Present on Order Search Page for PC");
		s_assert.assertTrue(cscockpitOrderSearchTabPage.verifyOrderNumberSectionIsPresentWithClickableLinksInOrderSearchTab(), "Order # is not Clickable for PC");
		s_assert.assertTrue(cscockpitOrderSearchTabPage.isOrderSearchLabelPresent("Order Date"), "Order Date is not Present on Order Search Page for PC");
		s_assert.assertTrue(cscockpitOrderSearchTabPage.isOrderSearchLabelPresent("First Name"), "First Name is not Present on Order Search Page for PC");
		s_assert.assertTrue(cscockpitOrderSearchTabPage.isOrderSearchLabelPresent("Last Name"), "Last Name is not Present on Order Search Page for PC");
		s_assert.assertTrue(cscockpitOrderSearchTabPage.isOrderSearchLabelPresent("R+F CID"), "R+F CID is not Present on Order Search Page for PC");
		s_assert.assertTrue(cscockpitOrderSearchTabPage.isOrderSearchLabelPresent("Email Address"), "Email Address is not Present on Order Search Page for PC");
		s_assert.assertTrue(cscockpitOrderSearchTabPage.isOrderSearchLabelPresent("Order Type"), "Order Type is not Present on Order Search Page for PC");
		s_assert.assertTrue(cscockpitOrderSearchTabPage.isOrderSearchLabelPresent("Order Status"), "Order Status is not Present on Order Search Page for PC");
		s_assert.assertTrue(cscockpitOrderSearchTabPage.isOrderSearchLabelPresent("Order Total"), "Order Total is not Present on Order Search Page for PC");
		s_assert.assertTrue(cscockpitOrderSearchTabPage.isOrderSearchLabelPresent("Ship To Country"), "Ship To Country is not Present on Order Search Page for PC");

		randomOrderSequenceNumber = String.valueOf(cscockpitOrderSearchTabPage.getRandomOrdersFromOrderResultSearchFirstPageInOrderSearchTab());
		cscockpitOrderSearchTabPage.clickOrderNumberInOrderSearchResultsInOrderSearchTab(randomOrderSequenceNumber);

		s_assert.assertFalse(cscockpitOrderTabPage.isChangeSponserLinkPresent(), "Change Link is Present on Order Tab Page for PC");

		//----------------------------------------For RC User ------------------------------------------------------------//

		driver.get(driver.getStoreFrontURL()+"/us");
		while(true){
			randomRCList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_RC_EMAIL_ID_RFO,"236"),RFO_DB);
			accountID = String.valueOf(getValueFromQueryResult(randomRCList, "AccountID"));
			randomRCUsernameList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_EMAIL_ID_FROM_ACCOUNT_ID,accountID),RFO_DB);
			rcUserEmailID = String.valueOf(getValueFromQueryResult(randomRCUsernameList, "EmailAddress"));
			storeFrontRCUserPage = storeFrontHomePage.loginAsRCUser(rcUserEmailID, password);
			boolean isLoginError = driver.getCurrentUrl().contains("error");
			if(isLoginError){
				logger.info("Login error for the user "+rcUserEmailID);
				driver.get(driver.getStoreFrontURL()+"/us");
			}
			else
				storeFrontRCUserPage.clickOnWelcomeDropDown();
			storeFrontAccountInfoPage = storeFrontRCUserPage.clickAccountInfoLinkPresentOnWelcomeDropDown();
			storeFrontAccountInfoPage.clickOnYourAccountDropdown();
			storeFrontAccountTerminationPage=storeFrontAccountInfoPage.clickTerminateMyAccount();
			storeFrontAccountTerminationPage.selectTerminationReason();
			storeFrontAccountTerminationPage.enterTerminationComments();
			storeFrontAccountTerminationPage.selectCheckBoxForVoluntarilyTerminate();
			storeFrontAccountTerminationPage.clickSubmitToTerminateAccount();
			storeFrontAccountTerminationPage.clickOnConfirmTerminationPopup();
			break;
		}
		logger.info("emaild of consultant username "+rcUserEmailID);
		logger.info("login is successful");
		driver.get(driver.getCSCockpitURL());
		cscockpitLoginPage.enterUsername(TestConstants.CS_SALES_SUPERVISORY_USERNAME);
		cscockpitCustomerSearchTabPage = cscockpitLoginPage.clickLoginBtn();
		cscockpitCustomerSearchTabPage.enterEmailIdInSearchFieldInCustomerSearchTab(rcUserEmailID);
		cscockpitCustomerSearchTabPage.clickSearchBtn();
		randomOrderSequenceNumber = String.valueOf(cscockpitOrderSearchTabPage.getRandomOrdersFromOrderResultSearchFirstPageInOrderSearchTab());
		randomOrdersCIDNumber = cscockpitOrderSearchTabPage.getCIDOrderNumberInOrderSearchResultsInOrderSearchTab(randomOrderSequenceNumber);
		cscockpitCustomerSearchTabPage.clickFindOrderLinkOnLeftNavigation();
		cscockpitOrderSearchTabPage.enterCIDInOrderSearchTab(randomOrdersCIDNumber);
		cscockpitOrderSearchTabPage.clickSearchBtn();

		s_assert.assertTrue(cscockpitOrderSearchTabPage.isOrderSearchLabelPresent("Order #"), "Order # is not Present on Order Search Page for RC");
		s_assert.assertTrue(cscockpitOrderSearchTabPage.verifyOrderNumberSectionIsPresentWithClickableLinksInOrderSearchTab(), "Order # is not Clickable for RC");
		s_assert.assertTrue(cscockpitOrderSearchTabPage.isOrderSearchLabelPresent("Order Date"), "Order Date is not Present on Order Search Page for RC");
		s_assert.assertTrue(cscockpitOrderSearchTabPage.isOrderSearchLabelPresent("First Name"), "First Name is not Present on Order Search Page for RC");
		s_assert.assertTrue(cscockpitOrderSearchTabPage.isOrderSearchLabelPresent("Last Name"), "Last Name is not Present on Order Search Page for RC");
		s_assert.assertTrue(cscockpitOrderSearchTabPage.isOrderSearchLabelPresent("R+F CID"), "R+F CID is not Present on Order Search Page for RC");
		s_assert.assertTrue(cscockpitOrderSearchTabPage.isOrderSearchLabelPresent("Email Address"), "Email Address is not Present on Order Search Page for RC");
		s_assert.assertTrue(cscockpitOrderSearchTabPage.isOrderSearchLabelPresent("Order Type"), "Order Type is not Present on Order Search Page for RC");
		s_assert.assertTrue(cscockpitOrderSearchTabPage.isOrderSearchLabelPresent("Order Status"), "Order Status is not Present on Order Search Page for RC");
		s_assert.assertTrue(cscockpitOrderSearchTabPage.isOrderSearchLabelPresent("Order Total"), "Order Total is not Present on Order Search Page for RC");
		s_assert.assertTrue(cscockpitOrderSearchTabPage.isOrderSearchLabelPresent("Ship To Country"), "Ship To Country is not Present on Order Search Page for RC");

		randomOrderSequenceNumber = String.valueOf(cscockpitOrderSearchTabPage.getRandomOrdersFromOrderResultSearchFirstPageInOrderSearchTab());
		cscockpitOrderSearchTabPage.clickOrderNumberInOrderSearchResultsInOrderSearchTab(randomOrderSequenceNumber);

		s_assert.assertFalse(cscockpitOrderTabPage.isChangeSponserLinkPresent(), "Change Link is Present on Order Tab Page for RC");
		s_assert.assertAll();
	}


	//Hybris Project-1823:To Verify that change link not for Consultant Receiving Commissions admin or CS agent
	@Test(enabled=false)//WIP
	public void testToVerifyChangeLinkNotForConsultantRecievingCommisionAdminOrCSAgent_1823(){
		String randomOrderSequenceNumber = null;
		RFO_DB = driver.getDBNameRFO();
		cscockpitLoginPage = new CSCockpitLoginPage(driver);
		cscockpitCustomerSearchTabPage = cscockpitLoginPage.clickLoginBtn();
		cscockpitCustomerSearchTabPage.clickFindOrderLinkOnLeftNavigation();
		cscockpitOrderSearchTabPage.selectOrderTypeOnOrderSearchTab("Consultant Order");
		cscockpitOrderSearchTabPage.selectCountryFromDropDownInCustomerSearchTab("United States");
		cscockpitOrderSearchTabPage.selectOrderStatusOnOrderSearchTab("Submitted");
		cscockpitOrderSearchTabPage.clickSearchBtn();
		s_assert.assertTrue(cscockpitOrderSearchTabPage.isOrderSearchLabelPresent("Order #"), "Order # is not Present on Order Search Page for US");
		s_assert.assertTrue(cscockpitOrderSearchTabPage.isOrderSearchLabelPresent("Order Date"), "Order Date is not Present on Order Search Page for US");
		s_assert.assertTrue(cscockpitOrderSearchTabPage.isOrderSearchLabelPresent("First Name"), "First Name is not Present on Order Search Page for US");
		s_assert.assertTrue(cscockpitOrderSearchTabPage.isOrderSearchLabelPresent("Last Name"), "Last Name is not Present on Order Search Page for US");
		s_assert.assertTrue(cscockpitOrderSearchTabPage.isOrderSearchLabelPresent("R+F CID"), "R+F CID is not Present on Order Search Page for US");
		s_assert.assertTrue(cscockpitOrderSearchTabPage.isOrderSearchLabelPresent("Email Address"), "Email Address is not Present on Order Search Page for US");
		s_assert.assertTrue(cscockpitOrderSearchTabPage.isOrderSearchLabelPresent("Order Type"), "Order Type is not Present on Order Search Page for US");
		s_assert.assertTrue(cscockpitOrderSearchTabPage.isOrderSearchLabelPresent("Order Status"), "Order Status is not Present on Order Search Page for US");
		s_assert.assertTrue(cscockpitOrderSearchTabPage.isOrderSearchLabelPresent("Order Total"), "Order Total is not Present on Order Search Page for US");
		s_assert.assertTrue(cscockpitOrderSearchTabPage.isOrderSearchLabelPresent("Ship To Country"), "Ship To Country is not Present on Order Search Page for US");
		randomOrderSequenceNumber = String.valueOf(cscockpitOrderSearchTabPage.getRandomOrdersFromOrderResultSearchFirstPageInOrderSearchTab());
		cscockpitOrderSearchTabPage.clickOrderNumberInOrderSearchResultsInOrderSearchTab(randomOrderSequenceNumber);
		s_assert.assertFalse(cscockpitOrderTabPage.isChangeSponserLinkPresent(), "Change Link is Present on Order Tab Page for US");

		//---------------------FOR CA-----------------------
		driver.get(driver.getCSCockpitURL());
		cscockpitCustomerSearchTabPage = cscockpitLoginPage.clickLoginBtn();
		cscockpitCustomerSearchTabPage.clickFindOrderLinkOnLeftNavigation();
		cscockpitOrderSearchTabPage.selectOrderTypeOnOrderSearchTab("Consultant Order");
		cscockpitOrderSearchTabPage.selectCountryFromDropDownInCustomerSearchTab("Canada");
		cscockpitOrderSearchTabPage.selectOrderStatusOnOrderSearchTab("Submitted");
		cscockpitOrderSearchTabPage.clickSearchBtn();
		s_assert.assertTrue(cscockpitOrderSearchTabPage.isOrderSearchLabelPresent("Order #"), "Order # is not Present on Order Search Page for CA");
		s_assert.assertTrue(cscockpitOrderSearchTabPage.isOrderSearchLabelPresent("Order Date"), "Order Date is not Present on Order Search Page for CA");
		s_assert.assertTrue(cscockpitOrderSearchTabPage.isOrderSearchLabelPresent("First Name"), "First Name is not Present on Order Search Page for CA");
		s_assert.assertTrue(cscockpitOrderSearchTabPage.isOrderSearchLabelPresent("Last Name"), "Last Name is not Present on Order Search Page for CA");
		s_assert.assertTrue(cscockpitOrderSearchTabPage.isOrderSearchLabelPresent("R+F CID"), "R+F CID is not Present on Order Search Page for CA");
		s_assert.assertTrue(cscockpitOrderSearchTabPage.isOrderSearchLabelPresent("Email Address"), "Email Address is not Present on Order Search Page for CA");
		s_assert.assertTrue(cscockpitOrderSearchTabPage.isOrderSearchLabelPresent("Order Type"), "Order Type is not Present on Order Search Page for CA");
		s_assert.assertTrue(cscockpitOrderSearchTabPage.isOrderSearchLabelPresent("Order Status"), "Order Status is not Present on Order Search Page for CA");
		s_assert.assertTrue(cscockpitOrderSearchTabPage.isOrderSearchLabelPresent("Order Total"), "Order Total is not Present on Order Search Page for CA");
		s_assert.assertTrue(cscockpitOrderSearchTabPage.isOrderSearchLabelPresent("Ship To Country"), "Ship To Country is not Present on Order Search Page for CA");
		randomOrderSequenceNumber = String.valueOf(cscockpitOrderSearchTabPage.getRandomOrdersFromOrderResultSearchFirstPageInOrderSearchTab());
		cscockpitOrderSearchTabPage.clickOrderNumberInOrderSearchResultsInOrderSearchTab(randomOrderSequenceNumber);
		s_assert.assertFalse(cscockpitOrderTabPage.isChangeSponserLinkPresent(), "Change Link is Present on Order Tab Page for CA");
		s_assert.assertAll();
	}	

	//Hybris Project-1830:To verify that consultant Receiving Commission cannot happen on Cancelled PCperks
	@Test(enabled=false)//WIP
	public void testVerifyConsultantRecevingCommissionCannotHappenOnCancelledPCPerks_1830() throws InterruptedException{
		int randomNum = CommonUtils.getRandomNum(10000, 1000000);
		String randomCustomerSequenceNumber = null;
		String pcEmailID = null;
		String accountID = null;
		String cid = null;
		RFO_DB = driver.getDBNameRFO();
		driver.get(driver.getStoreFrontURL()+"/"+driver.getCountry());
		List<Map<String, Object>> randomPCList =  null;
		List<Map<String, Object>> emailIdFromAccountIdList =  null;
		while(true){
			randomPCList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_PC_WITH_ORDERS_AND_AUTOSHIPS_RFO,countryId),RFO_DB);  
			accountID = String.valueOf(getValueFromQueryResult(randomPCList, "AccountID")); 
			emailIdFromAccountIdList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_EMAIL_ID_FROM_ACCOUNT_ID,accountID),RFO_DB);
			pcEmailID = String.valueOf(getValueFromQueryResult(emailIdFromAccountIdList, "EmailAddress"));
			logger.info("Account Id of user "+accountID);
			storeFrontPCUserPage = storeFrontHomePage.loginAsPCUser(pcEmailID, password);
			boolean isLoginError = driver.getCurrentUrl().contains("error");
			if(isLoginError){
				logger.info("Login error for the user "+pcEmailID);
				driver.get(driver.getStoreFrontURL()+"/"+driver.getCountry());
			}
			else
				break;
		}
		logger.info("login is successful"); 
		storeFrontPCUserPage.clickOnWelcomeDropDown();
		storeFrontPCUserPage.clickAccountInfoLinkPresentOnWelcomeDropDown();
		storeFrontPCUserPage.clickOnYourAccountDropdown();
		storeFrontPCUserPage.clickOnPCPerksStatus();
		storeFrontPCUserPage.clickDelayOrCancelPCPerks();
		storeFrontPCUserPage.clickPleaseCancelMyPcPerksActBtn();
		storeFrontPCUserPage.cancelMyPCPerksAct();
		//Navigate to cscockpit.
		driver.get(driver.getCSCockpitURL());
		cscockpitLoginPage.enterUsername(TestConstants.CS_SALES_SUPERVISORY_USERNAME);
		cscockpitCustomerSearchTabPage = cscockpitLoginPage.clickLoginBtn();
		cscockpitCustomerSearchTabPage.selectCustomerTypeFromDropDownInCustomerSearchTab("PC");
		cscockpitCustomerSearchTabPage.enterEmailIdInSearchFieldInCustomerSearchTab(pcEmailID);
		cscockpitCustomerSearchTabPage.clickSearchBtn();
		randomCustomerSequenceNumber = String.valueOf(cscockpitCustomerSearchTabPage.getRandomCustomerFromSearchResult());
		cid=cscockpitCustomerSearchTabPage.clickAndReturnCIDNumberInCustomerSearchTab(randomCustomerSequenceNumber);
		cscockpitCustomerTabPage.getAndClickAutoshipIDHavingTypeAsPCAutoshipAndStatusIsCancelled();
		s_assert.assertFalse(cscockpitAutoshipTemplateTabPage.isChangeSponserLinkPresent(), "Change Link is Present on Order Tab Page for PC");
		s_assert.assertTrue(cscockpitAutoshipTemplateTabPage.getAutoShipTemplateStatus().contains("CANCELLED"),"Autoship template status expected CANCELLED while Actual on UI"+cscockpitAutoshipTemplateTabPage.getAutoShipTemplateStatus());
		s_assert.assertTrue(cscockpitAutoshipTemplateTabPage.getAutoShipTemplateType().contains("PCPerks Autoship"),"Autoship template type expected PCPerks Autoship while Actual on UI"+cscockpitAutoshipTemplateTabPage.getAutoShipTemplateType());
		cscockpitAutoshipTemplateTabPage.clickMenuButton();
		cscockpitAutoshipTemplateTabPage.clickLogoutButton();
		s_assert.assertAll();

	}

	//Hybris Project-1831:To verify that consultant Receiving Commissions cannot happen on cancelled CRP
	@Test(enabled=false)//WIP
	public void testVerifyConsultantRecevingCommissionCannotHappenOnCancelledCRP_1831() throws InterruptedException{
		int randomNum = CommonUtils.getRandomNum(10000, 1000000);
		String randomCustomerSequenceNumber = null;
		String consultantEmailID = null;
		String accountId = null;
		String cid = null;
		RFO_DB = driver.getDBNameRFO();
		driver.get(driver.getStoreFrontURL()+"/"+driver.getCountry());
		List<Map<String, Object>> randomConsultantList =  null;
		List<Map<String, Object>> emailIdFromAccountIdList =  null;
		while(true){
			randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_CONSULTANT_WITH_ORDERS_AND_AUTOSHIPS_RFO,countryId),RFO_DB);
			accountId = String.valueOf(getValueFromQueryResult(randomConsultantList, "AccountID"));
			emailIdFromAccountIdList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_EMAIL_ID_FROM_ACCOUNT_ID,accountId),RFO_DB);
			consultantEmailID=(String) getValueFromQueryResult(emailIdFromAccountIdList, "EmailAddress");  
			storeFrontConsultantPage = storeFrontHomePage.loginAsConsultant(consultantEmailID, password);
			boolean isLoginError = driver.getCurrentUrl().contains("error");
			if(isLoginError){
				logger.info("Login error for the user "+consultantEmailID);
				driver.get(driver.getStoreFrontURL()+"/"+driver.getCountry());
			}
			else
				break;
		}
		logger.info("login is successful"); 
		storeFrontConsultantPage.clickOnWelcomeDropDown();
		storeFrontConsultantPage.clickAccountInfoLinkPresentOnWelcomeDropDown();
		storeFrontConsultantPage.clickOnYourAccountDropdown();
		storeFrontConsultantPage.clickOnAutoshipStatusLink();
		if(storeFrontAccountInfoPage.verifyCRPCancelled()==false){
			storeFrontAccountInfoPage.clickOnCancelMyCRP();
		}
		s_assert.assertTrue(storeFrontAccountInfoPage.verifyCRPCancelled(), "CRP has not been cancelled");
		logout();
		driver.get(driver.getCSCockpitURL());
		cscockpitLoginPage.enterUsername(TestConstants.CS_SALES_SUPERVISORY_USERNAME);
		cscockpitCustomerSearchTabPage = cscockpitLoginPage.clickLoginBtn();
		cscockpitCustomerSearchTabPage.selectCustomerTypeFromDropDownInCustomerSearchTab("CONSULTANT");
		cscockpitCustomerSearchTabPage.enterEmailIdInSearchFieldInCustomerSearchTab(consultantEmailID);
		cscockpitCustomerSearchTabPage.clickSearchBtn();
		randomCustomerSequenceNumber = String.valueOf(cscockpitCustomerSearchTabPage.getRandomCustomerFromSearchResult());
		cid=cscockpitCustomerSearchTabPage.clickAndReturnCIDNumberInCustomerSearchTab(randomCustomerSequenceNumber);
		cscockpitCustomerTabPage.getAndClickAutoshipIDHavingTypeAsCRPAutoshipAndStatusIsCancelled();
		s_assert.assertFalse(cscockpitAutoshipTemplateTabPage.isChangeSponserLinkPresent(), "Change Link is Present on Order Tab Page for PC");
		s_assert.assertTrue(cscockpitAutoshipTemplateTabPage.getAutoShipTemplateStatus().contains("CANCELLED"),"Autoship template status expected CANCELLED while Actual on UI"+cscockpitAutoshipTemplateTabPage.getAutoShipTemplateStatus());
		s_assert.assertTrue(cscockpitAutoshipTemplateTabPage.getAutoShipTemplateType().contains("CRP Autoship"),"Autoship template type expected CRP Autoship while Actual on UI"+cscockpitAutoshipTemplateTabPage.getAutoShipTemplateType());
		cscockpitAutoshipTemplateTabPage.clickMenuButton();
		cscockpitAutoshipTemplateTabPage.clickLogoutButton();
		s_assert.assertAll();

	}
}