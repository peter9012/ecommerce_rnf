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
import com.rf.pages.website.cscockpit.CSCockpitAutoshipSearchTabPage;
import com.rf.pages.website.cscockpit.CSCockpitAutoshipTemplateTabPage;
import com.rf.pages.website.cscockpit.CSCockpitCartTabPage;
import com.rf.pages.website.cscockpit.CSCockpitCheckoutTabPage;
import com.rf.pages.website.cscockpit.CSCockpitCustomerSearchTabPage;
import com.rf.pages.website.cscockpit.CSCockpitCustomerTabPage;
import com.rf.pages.website.cscockpit.CSCockpitLoginPage;
import com.rf.pages.website.cscockpit.CSCockpitOrderSearchTabPage;
import com.rf.pages.website.cscockpit.CSCockpitOrderTabPage;
import com.rf.pages.website.storeFront.StoreFrontConsultantPage;
import com.rf.pages.website.storeFront.StoreFrontHomePage;
import com.rf.pages.website.storeFront.StoreFrontOrdersPage;
import com.rf.pages.website.storeFront.StoreFrontPCUserPage;
import com.rf.pages.website.storeFront.StoreFrontRCUserPage;
import com.rf.pages.website.storeFront.StoreFrontUpdateCartPage;
import com.rf.test.website.RFWebsiteBaseTest;


public class ReturnVerificationTest extends RFWebsiteBaseTest{
	private static final Logger logger = LogManager
			.getLogger(ReturnVerificationTest.class.getName());

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
	private StoreFrontHomePage storeFrontHomePage; 
	private StoreFrontConsultantPage storeFrontConsultantPage;
	private StoreFrontOrdersPage storeFrontOrdersPage;
	private StoreFrontPCUserPage storeFrontPCUserPage;
	private StoreFrontRCUserPage storeFrontRCUserPage;	
	private StoreFrontUpdateCartPage storeFrontUpdateCartPage;

	//-----------------------------------------------------------------------------------------------------------------

	public ReturnVerificationTest() {
		cscockpitLoginPage = new CSCockpitLoginPage(driver);
		cscockpitAutoshipSearchTabPage = new CSCockpitAutoshipSearchTabPage(driver);
		cscockpitCheckoutTabPage = new CSCockpitCheckoutTabPage(driver);
		cscockpitCustomerSearchTabPage = new CSCockpitCustomerSearchTabPage(driver);
		cscockpitCustomerTabPage = new CSCockpitCustomerTabPage(driver);
		cscockpitOrderSearchTabPage = new CSCockpitOrderSearchTabPage(driver);
		cscockpitOrderTabPage = new CSCockpitOrderTabPage(driver);
		cscockpitCartTabPage = new CSCockpitCartTabPage(driver);
		cscockpitAutoshipTemplateTabPage = new CSCockpitAutoshipTemplateTabPage(driver);	
		storeFrontHomePage = new StoreFrontHomePage(driver);
		storeFrontConsultantPage = new StoreFrontConsultantPage(driver);
		storeFrontOrdersPage = new StoreFrontOrdersPage(driver);
		storeFrontPCUserPage = new StoreFrontPCUserPage(driver);
		storeFrontRCUserPage = new StoreFrontRCUserPage(driver);
		storeFrontUpdateCartPage = new StoreFrontUpdateCartPage(driver);
	}

	private String RFO_DB = null;

	//Hybris Project-4661:Change the Sponsor of RC user from Cscockpit
	@Test(enabled=false)//WIP
	public void testChangeSponserOfRCFromCSCockpit_4661() throws InterruptedException{
		String randomCustomerSequenceNumber = null;
		String consultantEmailID = null;
		String accountID=null;
		String orderNumber = null;
		String existingSponserName=null;
		RFO_DB = driver.getDBNameRFO();

		List<Map<String, Object>> randomConsultantList =  null;
		List<Map<String, Object>> randomRCList =  null;
		String rcUserEmailID =null;
		String accountId = null;

		//-------------------FOR US----------------------------------
		driver.get(driver.getStoreFrontURL()+"/us");		
		while(true){
			randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_CONSULTANT_WITH_ORDERS_AND_AUTOSHIPS_RFO,"236"),RFO_DB);
			consultantEmailID = (String) getValueFromQueryResult(randomConsultantList, "UserName");  
			accountID= String.valueOf(getValueFromQueryResult(randomConsultantList, "AccountID"));
			logger.info("Account Id of the consultant user is "+accountId);
			storeFrontConsultantPage = storeFrontHomePage.loginAsConsultant(consultantEmailID, password);
			boolean isLoginError = driver.getCurrentUrl().contains("error");
			if(isLoginError){
				logger.info("Login error for the user "+consultantEmailID);
				driver.get(driver.getStoreFrontURL()+"/us");
			}
			else
				break;
		}
		logout();
		driver.get(driver.getStoreFrontURL()+"/us");
		//Get account number from account id.
		List<Map<String, Object>>sponsorIdList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_ACCOUNT_NUMBER_FOR_PWS,accountID),RFO_DB);
		String	sponsorID = (String) getValueFromQueryResult(sponsorIdList, "AccountNumber");
		logger.info("login is successful");
		//Get random active RC User
		while(true){
			randomRCList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_RC_HAVING_ORDERS_RFO,"236"),RFO_DB);
			rcUserEmailID = (String) getValueFromQueryResult(randomRCList, "UserName");  
			accountId = String.valueOf(getValueFromQueryResult(randomRCList, "AccountID"));
			logger.info("Account Id of the RC user is "+accountId);
			storeFrontRCUserPage = storeFrontHomePage.loginAsRCUser(rcUserEmailID, password);
			boolean isError = driver.getCurrentUrl().contains("error");
			if(isError){
				logger.info("login error for the user "+rcUserEmailID);
				driver.get(driver.getStoreFrontURL()+"/us");
			}
			else
				break;
		} 
		logout();
		driver.get(driver.getCSCockpitURL());		
		cscockpitLoginPage.enterUsername("cscommissionadmin");
		cscockpitCustomerSearchTabPage =cscockpitLoginPage.clickLoginBtn();
		cscockpitCustomerSearchTabPage.enterEmailIdInSearchFieldInCustomerSearchTab(rcUserEmailID);
		cscockpitCustomerSearchTabPage.clickSearchBtn();
		randomCustomerSequenceNumber = String.valueOf(cscockpitCustomerSearchTabPage.getRandomCustomerFromSearchResult());
		cscockpitCustomerSearchTabPage.clickCIDNumberInCustomerSearchTab(randomCustomerSequenceNumber);
		orderNumber=cscockpitCustomerTabPage.clickAndGetOrderNumberInCustomerTab();
		logger.info("Order Number fetched from UI is  "+orderNumber);
		existingSponserName=cscockpitOrderTabPage.getExistingSponserNameInOrderTab();
		logger.info("Existing Sponser name and account no on UI is  "+existingSponserName);
		cscockpitOrderTabPage.clickChangeSponserLinkInOrderTab();
		cscockpitOrderTabPage.enterConsultantCIDAndClickSearchInOrderTab(sponsorID);
		cscockpitOrderTabPage.clickSelectToSelectSponserInOrderTab();
		s_assert.assertTrue(cscockpitOrderTabPage.getNewSponserNameFromUIInOrderTab().contains(sponsorID),"CSCockpit Sponser CID expected = "+sponsorID+" and on UI = " +cscockpitOrderTabPage.getNewSponserNameFromUIInOrderTab());
		//		//Verify the sponser change in RFO Database.
		//		randomUserDetailList=DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_ORDERID_RFO,orderNumber),RFO_DB);
		//		String newSponserAccountId=String.valueOf(getValueFromQueryResult(randomUserDetailList, "AccountID"));
		//		//Get account number from account id of newly selected sponser.
		//		List<Map<String, Object>> newSponsorIdList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_ACCOUNT_NUMBER_FOR_PWS,newSponserAccountId),RFO_DB);
		//		String	AccountNumberDB = (String) getValueFromQueryResult(newSponsorIdList, "AccountNumber");
		//		s_assert.assertTrue(AccountNumberDB.contains(sponsorID),"CSCockpit Sponser CID expected = "+sponsorID+" and In Database  = "+AccountNumberDB);

		//-------------------FOR CA----------------------------------
		driver.get(driver.getStoreFrontURL()+"/ca");
		while(true){
			randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_CONSULTANT_WITH_ORDERS_AND_AUTOSHIPS_RFO,"40"),RFO_DB);
			consultantEmailID = (String) getValueFromQueryResult(randomConsultantList, "UserName");  
			accountID= String.valueOf(getValueFromQueryResult(randomConsultantList, "AccountID"));
			logger.info("Account Id of the consultant user is "+accountId);
			storeFrontConsultantPage = storeFrontHomePage.loginAsConsultant(consultantEmailID, password);
			boolean isLoginError = driver.getCurrentUrl().contains("error");
			if(isLoginError){
				logger.info("Login error for the user "+consultantEmailID);
				driver.get(driver.getStoreFrontURL()+"/us");
			}
			else
				break;
		}
		logout();
		driver.get(driver.getStoreFrontURL()+"/ca");
		//Get account number from account id.
		sponsorIdList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_ACCOUNT_NUMBER_FOR_PWS,accountID),RFO_DB);
		sponsorID = (String) getValueFromQueryResult(sponsorIdList, "AccountNumber");
		logger.info("login is successful");
		//Get random active RC User
		while(true){
			randomRCList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_RC_HAVING_ORDERS_RFO,"40"),RFO_DB);
			rcUserEmailID = (String) getValueFromQueryResult(randomRCList, "UserName");  
			accountId = String.valueOf(getValueFromQueryResult(randomRCList, "AccountID"));
			logger.info("Account Id of the RC user is "+accountId);
			storeFrontRCUserPage = storeFrontHomePage.loginAsRCUser(rcUserEmailID, password);
			boolean isError = driver.getCurrentUrl().contains("error");
			if(isError){
				logger.info("login error for the user "+rcUserEmailID);
				driver.get(driver.getStoreFrontURL()+"/us");
			}
			else
				break;
		} 
		logout();
		driver.get(driver.getCSCockpitURL());		
		cscockpitLoginPage.enterUsername("cscommissionadmin");
		cscockpitCustomerSearchTabPage =cscockpitLoginPage.clickLoginBtn();
		cscockpitCustomerSearchTabPage.enterEmailIdInSearchFieldInCustomerSearchTab(rcUserEmailID);
		cscockpitCustomerSearchTabPage.clickSearchBtn();
		randomCustomerSequenceNumber = String.valueOf(cscockpitCustomerSearchTabPage.getRandomCustomerFromSearchResult());
		cscockpitCustomerSearchTabPage.clickCIDNumberInCustomerSearchTab(randomCustomerSequenceNumber);
		orderNumber=cscockpitCustomerTabPage.clickAndGetOrderNumberInCustomerTab();
		logger.info("Order Number fetched from UI is  "+orderNumber);
		existingSponserName=cscockpitOrderTabPage.getExistingSponserNameInOrderTab();
		logger.info("Existing Sponser name and account no on UI is  "+existingSponserName);
		cscockpitOrderTabPage.clickChangeSponserLinkInOrderTab();
		cscockpitOrderTabPage.enterConsultantCIDAndClickSearchInOrderTab(sponsorID);
		cscockpitOrderTabPage.clickSelectToSelectSponserInOrderTab();
		s_assert.assertTrue(cscockpitOrderTabPage.getNewSponserNameFromUIInOrderTab().contains(sponsorID),"CSCockpit Sponser CID expected = "+sponsorID+" and on UI = " +cscockpitOrderTabPage.getNewSponserNameFromUIInOrderTab());
		//		//Verify the sponser change in RFO Database.
		//		randomUserDetailList=DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_ORDERID_RFO,orderNumber),RFO_DB);
		//		newSponserAccountId=String.valueOf(getValueFromQueryResult(randomUserDetailList, "AccountID"));
		//		//Get account number from account id of newly selected sponser.
		//		newSponsorIdList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_ACCOUNT_NUMBER_FOR_PWS,newSponserAccountId),RFO_DB);
		//		AccountNumberDB = (String) getValueFromQueryResult(newSponsorIdList, "AccountNumber");
		//		s_assert.assertTrue(AccountNumberDB.contains(sponsorID),"CSCockpit Sponser CID expected = "+sponsorID+" and In Database  = "+AccountNumberDB);
		s_assert.assertAll();	
	}

	// Hybris Project-2013:To verify that for terminated user can do returns
	@Test(enabled=false) //WIP
	public void testToVerifyThatForTerminatedUserCanDoReturns_2013(){
		String randomCustomerSequenceNumber = null;
		driver.get(driver.getCSCockpitURL());
		cscockpitCustomerSearchTabPage = cscockpitLoginPage.clickLoginBtn();
		cscockpitCustomerSearchTabPage.selectCustomerTypeFromDropDownInCustomerSearchTab("CONSULTANT");
		cscockpitCustomerSearchTabPage.selectCountryFromDropDownInCustomerSearchTab("United States");
		cscockpitCustomerSearchTabPage.selectAccountStatusFromDropDownInCustomerSearchTab("Inactive");
		cscockpitOrderSearchTabPage.clickSearchBtn();
		randomCustomerSequenceNumber = String.valueOf(cscockpitCustomerSearchTabPage.getRandomCustomerFromSearchResult());
		cscockpitCustomerSearchTabPage.clickCIDNumberInCustomerSearchTab(randomCustomerSequenceNumber);
		cscockpitCustomerTabPage.clickAndGetOrderNumberInCustomerTab();
		s_assert.assertTrue(cscockpitOrderTabPage.verifyRefundOrderButtonPresentOnOrderTab(),"Refund order button is not present on order tab for US");
		//-----------------------FOR CA---------------------
		driver.get(driver.getCSCockpitURL());
		cscockpitCustomerSearchTabPage = cscockpitLoginPage.clickLoginBtn();

		cscockpitCustomerSearchTabPage.selectCustomerTypeFromDropDownInCustomerSearchTab("CONSULTANT");
		cscockpitCustomerSearchTabPage.selectCountryFromDropDownInCustomerSearchTab("Canada");
		cscockpitCustomerSearchTabPage.selectAccountStatusFromDropDownInCustomerSearchTab("Inactive");
		cscockpitOrderSearchTabPage.clickSearchBtn();
		randomCustomerSequenceNumber = String.valueOf(cscockpitCustomerSearchTabPage.getRandomCustomerFromSearchResult());
		cscockpitCustomerSearchTabPage.clickCIDNumberInCustomerSearchTab(randomCustomerSequenceNumber);
		cscockpitCustomerTabPage.clickAndGetOrderNumberInCustomerTab();
		s_assert.assertTrue(cscockpitOrderTabPage.verifyRefundOrderButtonPresentOnOrderTab(),"Refund order button is not present on order tab for CA");
		s_assert.assertAll();
	}

	//Hybris Project-2014:To verify the refund Request Popup UI
	@Test(enabled=false) //WIP
	public void testToVerifyTheRefundRequestPopUpUI_2014(){
		String randomCustomerSequenceNumber = null;
		String returnQuantity = "1";
		String refundReason = "Unsatisfactory";
		driver.get(driver.getCSCockpitURL());
		cscockpitCustomerSearchTabPage = cscockpitLoginPage.clickLoginBtn();

		cscockpitCustomerSearchTabPage.selectCustomerTypeFromDropDownInCustomerSearchTab("CONSULTANT");
		cscockpitCustomerSearchTabPage.selectCountryFromDropDownInCustomerSearchTab("United States");
		cscockpitCustomerSearchTabPage.selectAccountStatusFromDropDownInCustomerSearchTab("Active");
		cscockpitOrderSearchTabPage.clickSearchBtn();
		randomCustomerSequenceNumber = String.valueOf(cscockpitCustomerSearchTabPage.getRandomCustomerFromSearchResult());
		cscockpitCustomerSearchTabPage.clickCIDNumberInCustomerSearchTab(randomCustomerSequenceNumber);
		cscockpitCustomerTabPage.clickAndGetOrderNumberInCustomerTab();
		cscockpitOrderTabPage.clickRefundOrderBtnOnOrderTab();
		s_assert.assertTrue(cscockpitOrderTabPage.verifyRefundRequestPopUpPresent(),"Refund Request PopUp not present on us");
		s_assert.assertTrue(cscockpitOrderTabPage.verifyOrderLevelCheckBoxSection(),"order level checkBoxes not present as expected on us");
		s_assert.assertTrue(cscockpitOrderTabPage.verifyRefundReasonDDPresent(),"Refund Reason DD not present on pop up on us");
		s_assert.assertTrue(cscockpitOrderTabPage.verifyCreditCardDDPresent(),"credit card DD not present on pop up on us");
		s_assert.assertTrue(cscockpitOrderTabPage.verifyRefundTypeDDPresent(),"Refund Type DD not present on pop up on us");
		cscockpitOrderTabPage.clickCloseRefundRequestPopUP();
		cscockpitOrderTabPage.clickRefundOrderBtnOnOrderTab();
		cscockpitOrderTabPage.checkProductInfoCheckboxInPopUp();
		cscockpitOrderTabPage.clickCreateBtnOnRefundPopUp();
		cscockpitOrderTabPage.clickOKBtnOnRMAPopUp();
		cscockpitOrderTabPage.selectReturnQuantityOnPopUp(returnQuantity);
		cscockpitOrderTabPage.clickCreateBtnOnRefundPopUp();
		cscockpitOrderTabPage.clickOKBtnOnRMAPopUp();
		cscockpitOrderTabPage.selectRefundReasonOnRefundPopUp(refundReason);
		cscockpitOrderTabPage.clickCreateBtnOnRefundPopUp();
		cscockpitOrderTabPage.clickOKBtnOnRMAPopUp();
		cscockpitOrderTabPage.selectFirstRefundTypeOnRefundPopUp();
		cscockpitOrderTabPage.clickCreateBtnOnRefundPopUp();
		cscockpitOrderTabPage.clickCloseRefundConfirmationPopUP();
		cscockpitOrderTabPage.clickCreateBtnOnRefundPopUp();
		cscockpitOrderTabPage.clickConfirmBtnOnConfirmPopUp();
		cscockpitOrderTabPage.clickOKBtnOnRMAPopUp();
		s_assert.assertTrue(cscockpitOrderTabPage.verifyPlaceAnOrderOrderBtnIsPresentInOrderTab(),"This is not Order Page on US");
		//----------------------FOR CA---------------------------
		driver.get(driver.getCSCockpitURL());
		cscockpitCustomerSearchTabPage = cscockpitLoginPage.clickLoginBtn();

		cscockpitCustomerSearchTabPage.selectCustomerTypeFromDropDownInCustomerSearchTab("CONSULTANT");
		cscockpitCustomerSearchTabPage.selectCountryFromDropDownInCustomerSearchTab("Canada");
		cscockpitCustomerSearchTabPage.selectAccountStatusFromDropDownInCustomerSearchTab("Active");
		cscockpitOrderSearchTabPage.clickSearchBtn();
		randomCustomerSequenceNumber = String.valueOf(cscockpitCustomerSearchTabPage.getRandomCustomerFromSearchResult());
		cscockpitCustomerSearchTabPage.clickCIDNumberInCustomerSearchTab(randomCustomerSequenceNumber);
		cscockpitCustomerTabPage.clickAndGetOrderNumberInCustomerTab();
		cscockpitOrderTabPage.clickRefundOrderBtnOnOrderTab();
		s_assert.assertTrue(cscockpitOrderTabPage.verifyRefundRequestPopUpPresent(),"Refund Request PopUp not present on ca");
		s_assert.assertTrue(cscockpitOrderTabPage.verifyOrderLevelCheckBoxSection(),"order level checkBoxes not present as expected on ca");
		s_assert.assertTrue(cscockpitOrderTabPage.verifyRefundReasonDDPresent(),"Refund Reason DD not present on pop up on ca");
		s_assert.assertTrue(cscockpitOrderTabPage.verifyCreditCardDDPresent(),"credit card DD not present on pop up on ca");
		s_assert.assertTrue(cscockpitOrderTabPage.verifyRefundTypeDDPresent(),"Refund Type DD not present on pop up on ca");
		cscockpitOrderTabPage.clickCloseRefundRequestPopUP();
		cscockpitOrderTabPage.clickRefundOrderBtnOnOrderTab();
		cscockpitOrderTabPage.checkProductInfoCheckboxInPopUp();
		cscockpitOrderTabPage.clickCreateBtnOnRefundPopUp();
		cscockpitOrderTabPage.clickOKBtnOnRMAPopUp();
		cscockpitOrderTabPage.selectReturnQuantityOnPopUp(returnQuantity);
		cscockpitOrderTabPage.clickCreateBtnOnRefundPopUp();
		cscockpitOrderTabPage.clickOKBtnOnRMAPopUp();
		cscockpitOrderTabPage.selectRefundReasonOnRefundPopUp(refundReason);
		cscockpitOrderTabPage.clickCreateBtnOnRefundPopUp();
		cscockpitOrderTabPage.clickOKBtnOnRMAPopUp();
		cscockpitOrderTabPage.selectFirstRefundTypeOnRefundPopUp();
		cscockpitOrderTabPage.clickCreateBtnOnRefundPopUp();
		cscockpitOrderTabPage.clickCloseRefundConfirmationPopUP();
		cscockpitOrderTabPage.clickCreateBtnOnRefundPopUp();
		cscockpitOrderTabPage.clickConfirmBtnOnConfirmPopUp();
		cscockpitOrderTabPage.clickOKBtnOnRMAPopUp();
		s_assert.assertTrue(cscockpitOrderTabPage.verifyPlaceAnOrderOrderBtnIsPresentInOrderTab(),"This is not Order Page on CA");
		s_assert.assertAll();
	}

	//Hybris Project-2017:To verify that Return Complete Order functionality
	@Test
	public void testVerifyReturnCompleteOrderFunctionality_2017() throws InterruptedException{
		String randomCustomerSequenceNumber = null;
		String consultantEmailID = null;
		String orderNumber = null;
		String orderHistoryNumber = null;
		String randomProductSequenceNumber = null;
		String SKUValue = null;
		RFO_DB = driver.getDBNameRFO();

		driver.get(driver.getStoreFrontURL()+"/us");
		List<Map<String, Object>> randomConsultantList =  null;
		storeFrontHomePage = new StoreFrontHomePage(driver);
		while(true){
			randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_CONSULTANT_WITH_ORDERS_AND_AUTOSHIPS_RFO,"236"),RFO_DB);
			consultantEmailID = (String) getValueFromQueryResult(randomConsultantList, "UserName");  
			storeFrontConsultantPage = storeFrontHomePage.loginAsConsultant(consultantEmailID, password);
			boolean isLoginError = driver.getCurrentUrl().contains("error");
			if(isLoginError){
				logger.info("Login error for the user "+consultantEmailID);
				driver.get(driver.getStoreFrontURL()+"/us");
			}
			else
				break;
		}
		logout();
		logger.info("login is successful");
		cscockpitLoginPage = new CSCockpitLoginPage(driver);
		driver.get(driver.getCSCockpitURL());		
		cscockpitCustomerSearchTabPage = cscockpitLoginPage.clickLoginBtn();
		cscockpitCustomerSearchTabPage.enterEmailIdInSearchFieldInCustomerSearchTab(consultantEmailID);
		cscockpitCustomerSearchTabPage.clickSearchBtn();
		randomCustomerSequenceNumber = String.valueOf(cscockpitCustomerSearchTabPage.getRandomCustomerFromSearchResult());
		cscockpitCustomerSearchTabPage.clickCIDNumberInCustomerSearchTab(randomCustomerSequenceNumber);
		cscockpitCustomerTabPage.clickPlaceOrderButtonInCustomerTab();
		cscockpitCartTabPage.selectValueFromSortByDDInCartTab("Price: High to Low");
		cscockpitCartTabPage.selectCatalogFromDropDownInCartTab();	
		randomProductSequenceNumber = String.valueOf(cscockpitCartTabPage.getRandomProductWithSKUFromSearchResult()); 
		SKUValue = cscockpitCartTabPage.getCustomerSKUValueInCartTab(randomProductSequenceNumber);
		cscockpitCartTabPage.searchSKUValueInCartTab(SKUValue);
		cscockpitCartTabPage.clickAddToCartBtnInCartTab();
		cscockpitCartTabPage.clickCheckoutBtnInCartTab();
		s_assert.assertTrue(cscockpitCheckoutTabPage.getCreditCardNumberInCheckoutTab().contains("************"),"CSCockpit checkout tab credit card number expected = ************ and on UI = " +cscockpitCheckoutTabPage.getCreditCardNumberInCheckoutTab());
		s_assert.assertTrue(cscockpitCheckoutTabPage.getDeliverModeTypeInCheckoutTab().contains("FedEx Ground (HD)"),"CSCockpit checkout tab delivery mode type expected = FedEx Ground (HD) and on UI = " +cscockpitCheckoutTabPage.getDeliverModeTypeInCheckoutTab());
		s_assert.assertTrue(cscockpitCheckoutTabPage.isCommissionDatePopulatedInCheckoutTab(), "Commission date is not populated in UI");
		cscockpitCheckoutTabPage.clickPlaceOrderButtonInCheckoutTab();
		s_assert.assertTrue(cscockpitCheckoutTabPage.verifySelectPaymentDetailsPopupInCheckoutTab(), "Select payment details popup is not present");
		cscockpitCheckoutTabPage.clickOkButtonOfSelectPaymentDetailsPopupInCheckoutTab();
		cscockpitCheckoutTabPage.enterCVVValueInCheckoutTab(TestConstants.SECURITY_CODE);
		cscockpitCheckoutTabPage.clickUseThisCardBtnInCheckoutTab();
		cscockpitCheckoutTabPage.clickPlaceOrderButtonInCheckoutTab();
		orderNumber = cscockpitOrderTabPage.getOrderNumberInOrderTab();
		logger.info("RETURN ORDER NUMBER IS "+orderNumber);		
		s_assert.assertTrue(cscockpitOrderSearchTabPage.clickOrderLinkOnOrderSearchTabAndVerifyOrderDetailsPage(orderNumber)>0, "Order was NOT placed successfully,expected count after placing order in order detail items section >0 but actual count on UI = "+cscockpitOrderTabPage.getCountOfOrdersOnOrdersDetailsPageAfterPlacingOrder());
		cscockpitOrderTabPage.clickRefundOrderBtnOnOrderTab();
		boolean isReturnCompleteOrderChecked = cscockpitOrderTabPage.checkReturnCompleteOrderChkBoxOnRefundPopUpAndReturnTrueElseFalse();
		if(isReturnCompleteOrderChecked==true){
			s_assert.assertTrue(cscockpitOrderTabPage.areAllCheckBoxesGettingDisabledAfterCheckingReturnCompleteOrderChkBox(), "All other checkboxes are not disabled after checking 'Return Complete Order' checkbox");
		}
		cscockpitOrderTabPage.selectRefundReasonOnRefundPopUp("Test");
		cscockpitOrderTabPage.selectFirstReturnActionOnRefundPopUp();
		cscockpitOrderTabPage.selectFirstRefundTypeOnRefundPopUp();
		cscockpitOrderTabPage.clickCreateBtnOnRefundPopUp();
		String refundTotal = cscockpitOrderTabPage.getRefundTotalFromRefundConfirmationPopUp();
		cscockpitOrderTabPage.clickConfirmBtnOnConfirmPopUp();
		cscockpitOrderTabPage.clickOKBtnOnRMAPopUp();
		s_assert.assertTrue(cscockpitOrderTabPage.isReturnRequestSectionDisplayed(), "Return request section is NOT displayed");
		cscockpitOrderTabPage.clickRMATreeBtnUnderReturnRequestOnOrderTab();
		cscockpitOrderTabPage.clickRMATreeBtnUnderReturnRequestOnOrderTab();
		cscockpitOrderTabPage.clickRefundOrderBtnOnOrderTab();
		s_assert.assertTrue(cscockpitOrderTabPage.isNoRefundableItemsTxtPresent(), "Order Number = "+orderNumber+" has NOT refund Successfully");

		driver.get(driver.getStoreFrontURL()+"/us");
		storeFrontHomePage = new StoreFrontHomePage(driver);
		storeFrontConsultantPage = storeFrontHomePage.loginAsConsultant(consultantEmailID, password);
		storeFrontConsultantPage.clickOnWelcomeDropDown();
		storeFrontOrdersPage =  storeFrontConsultantPage.clickOrdersLinkPresentOnWelcomeDropDown();
		s_assert.assertTrue(storeFrontOrdersPage.verifyOrdersPageIsDisplayed(),"Orders page has not been displayed");
		orderHistoryNumber = storeFrontOrdersPage.getFirstOrderNumberFromOrderHistory();
		s_assert.assertTrue(orderHistoryNumber.contains(orderNumber.split("\\-")[0].trim()),"CSCockpit Order number expected = "+orderNumber.split("\\-")[0].trim()+" and on UI = " +orderHistoryNumber);

		s_assert.assertAll();		
	}

	//Hybris Project-4073:CSCockpit Refund Order
	@Test
	public void testVerifyCscockpitRefundOrderFunctionality_4073() throws InterruptedException{
		String randomCustomerSequenceNumber = null;
		String consultantEmailID = null;
		String orderNumber = null;
		String orderHistoryNumber = null;
		String randomProductSequenceNumber = null;
		String SKUValue = null;
		RFO_DB = driver.getDBNameRFO();

		driver.get(driver.getStoreFrontURL()+"/us");
		List<Map<String, Object>> randomConsultantList =  null;
		storeFrontHomePage = new StoreFrontHomePage(driver);
		while(true){
			randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_CONSULTANT_WITH_ORDERS_AND_AUTOSHIPS_RFO,"236"),RFO_DB);
			consultantEmailID = (String) getValueFromQueryResult(randomConsultantList, "UserName");  
			storeFrontConsultantPage = storeFrontHomePage.loginAsConsultant(consultantEmailID, password);
			boolean isLoginError = driver.getCurrentUrl().contains("error");
			if(isLoginError){
				logger.info("Login error for the user "+consultantEmailID);
				driver.get(driver.getStoreFrontURL()+"/us");
			}
			else
				break;
		}
		logout();
		logger.info("login is successful");
		cscockpitLoginPage = new CSCockpitLoginPage(driver);
		driver.get(driver.getCSCockpitURL());		
		cscockpitCustomerSearchTabPage = cscockpitLoginPage.clickLoginBtn();
		cscockpitCustomerSearchTabPage.enterEmailIdInSearchFieldInCustomerSearchTab(consultantEmailID);
		cscockpitCustomerSearchTabPage.clickSearchBtn();
		randomCustomerSequenceNumber = String.valueOf(cscockpitCustomerSearchTabPage.getRandomCustomerFromSearchResult());
		cscockpitCustomerSearchTabPage.clickCIDNumberInCustomerSearchTab(randomCustomerSequenceNumber);
		cscockpitCustomerTabPage.clickPlaceOrderButtonInCustomerTab();
		cscockpitCartTabPage.selectValueFromSortByDDInCartTab("Price: High to Low");
		cscockpitCartTabPage.selectCatalogFromDropDownInCartTab();	
		randomProductSequenceNumber = String.valueOf(cscockpitCartTabPage.getRandomProductWithSKUFromSearchResult()); 
		SKUValue = cscockpitCartTabPage.getCustomerSKUValueInCartTab(randomProductSequenceNumber);
		cscockpitCartTabPage.searchSKUValueInCartTab(SKUValue);
		cscockpitCartTabPage.clickAddToCartBtnInCartTab();
		cscockpitCartTabPage.clickCheckoutBtnInCartTab();
		s_assert.assertTrue(cscockpitCheckoutTabPage.getCreditCardNumberInCheckoutTab().contains("************"),"CSCockpit checkout tab credit card number expected = ************ and on UI = " +cscockpitCheckoutTabPage.getCreditCardNumberInCheckoutTab());
		s_assert.assertTrue(cscockpitCheckoutTabPage.getDeliverModeTypeInCheckoutTab().contains("FedEx Ground (HD)"),"CSCockpit checkout tab delivery mode type expected = FedEx Ground (HD) and on UI = " +cscockpitCheckoutTabPage.getDeliverModeTypeInCheckoutTab());
		s_assert.assertTrue(cscockpitCheckoutTabPage.isCommissionDatePopulatedInCheckoutTab(), "Commission date is not populated in UI");
		cscockpitCheckoutTabPage.clickPlaceOrderButtonInCheckoutTab();
		s_assert.assertTrue(cscockpitCheckoutTabPage.verifySelectPaymentDetailsPopupInCheckoutTab(), "Select payment details popup is not present");
		cscockpitCheckoutTabPage.clickOkButtonOfSelectPaymentDetailsPopupInCheckoutTab();
		cscockpitCheckoutTabPage.enterCVVValueInCheckoutTab(TestConstants.SECURITY_CODE);
		cscockpitCheckoutTabPage.clickUseThisCardBtnInCheckoutTab();
		cscockpitCheckoutTabPage.clickPlaceOrderButtonInCheckoutTab();
		orderNumber = cscockpitOrderTabPage.getOrderNumberInOrderTab();
		logger.info("RETURN ORDER NUMBER IS "+orderNumber);		
		s_assert.assertTrue(cscockpitOrderSearchTabPage.clickOrderLinkOnOrderSearchTabAndVerifyOrderDetailsPage(orderNumber)>0, "Order was NOT placed successfully,expected count after placing order in order detail items section >0 but actual count on UI = "+cscockpitOrderTabPage.getCountOfOrdersOnOrdersDetailsPageAfterPlacingOrder());
		cscockpitOrderTabPage.clickRefundOrderBtnOnOrderTab();
		boolean isReturnCompleteOrderChecked = cscockpitOrderTabPage.checkReturnCompleteOrderChkBoxOnRefundPopUpAndReturnTrueElseFalse();
		if(isReturnCompleteOrderChecked==true){
			s_assert.assertTrue(cscockpitOrderTabPage.areAllCheckBoxesGettingDisabledAfterCheckingReturnCompleteOrderChkBox(), "All other checkboxes are not disabled after checking 'Return Complete Order' checkbox");
		}
		cscockpitOrderTabPage.selectRefundReasonOnRefundPopUp("Test");
		cscockpitOrderTabPage.selectFirstReturnActionOnRefundPopUp();
		cscockpitOrderTabPage.selectFirstRefundTypeOnRefundPopUp();
		cscockpitOrderTabPage.clickCreateBtnOnRefundPopUp();
		String refundTotal = cscockpitOrderTabPage.getRefundTotalFromRefundConfirmationPopUp();
		cscockpitOrderTabPage.clickConfirmBtnOnConfirmPopUp();
		cscockpitOrderTabPage.clickOKBtnOnRMAPopUp();
		s_assert.assertTrue(cscockpitOrderTabPage.isReturnRequestSectionDisplayed(), "Return request section is NOT displayed");
		cscockpitOrderTabPage.clickRMATreeBtnUnderReturnRequestOnOrderTab();
		cscockpitOrderTabPage.clickRMATreeBtnUnderReturnRequestOnOrderTab();
		cscockpitOrderTabPage.clickRefundOrderBtnOnOrderTab();
		s_assert.assertTrue(cscockpitOrderTabPage.isNoRefundableItemsTxtPresent(), "Order Number = "+orderNumber+" has NOT refund Successfully");

		driver.get(driver.getStoreFrontURL()+"/us");
		storeFrontHomePage = new StoreFrontHomePage(driver);
		storeFrontConsultantPage = storeFrontHomePage.loginAsConsultant(consultantEmailID, password);
		storeFrontConsultantPage.clickOnWelcomeDropDown();
		storeFrontOrdersPage =  storeFrontConsultantPage.clickOrdersLinkPresentOnWelcomeDropDown();
		s_assert.assertTrue(storeFrontOrdersPage.verifyOrdersPageIsDisplayed(),"Orders page has not been displayed");
		orderHistoryNumber = storeFrontOrdersPage.getFirstOrderNumberFromOrderHistory();
		s_assert.assertTrue(orderHistoryNumber.contains(orderNumber.split("\\-")[0].trim()),"CSCockpit Order number expected = "+orderNumber.split("\\-")[0].trim()+" and on UI = " +orderHistoryNumber);

		s_assert.assertAll();		
	}

}
