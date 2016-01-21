package com.rf.test.website.cscockpit.smoke;

import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.annotations.Test;

import com.rf.core.utils.CommonUtils;
import com.rf.core.utils.DBUtil;
import com.rf.core.website.constants.TestConstants;
import com.rf.core.website.constants.dbQueries.DBQueries_RFO;
import com.rf.pages.website.CSCockpitHomePage;
import com.rf.pages.website.CSCockpitLoginPage;
import com.rf.pages.website.StoreFrontConsultantPage;
import com.rf.pages.website.StoreFrontHomePage;
import com.rf.pages.website.StoreFrontOrdersPage;
import com.rf.pages.website.StoreFrontPCUserPage;
import com.rf.pages.website.StoreFrontRCUserPage;
import com.rf.test.website.RFWebsiteBaseTest;


public class OrdersVerificationTest extends RFWebsiteBaseTest{
	private static final Logger logger = LogManager
			.getLogger(OrdersVerificationTest.class.getName());

	//-------------------------------------------------Pages---------------------------------------------------------
	private CSCockpitLoginPage cscockpitLoginPage;	
	private CSCockpitHomePage cscockpitHomePage; 
	private StoreFrontHomePage storeFrontHomePage; 
	private StoreFrontConsultantPage storeFrontConsultantPage;
	private StoreFrontOrdersPage storeFrontOrdersPage;
	private StoreFrontPCUserPage storeFrontPCUserPage;
	private StoreFrontRCUserPage storeFrontRCUserPage;	
	//-----------------------------------------------------------------------------------------------------------------

	private String RFO_DB = null;
	
	//Hybris Project-1934:To verify Consultant adhoc order
	@Test(enabled=false)
	public void testVerifyAdhocConsultantOrder_1934() throws InterruptedException{
		String randomCustomerSequenceNumber = null;
		String randomProductSequenceNumber = null;
		String consultantEmailID = null;
		String SKUValue = null;
		String orderNumber = null;
		String orderHistoryNumber = null;
		RFO_DB = driver.getDBNameRFO();

		//-------------------FOR US----------------------------------
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
		driver.pauseExecutionFor(5000);
		cscockpitHomePage = cscockpitLoginPage.clickLoginBtn();
		cscockpitHomePage.selectCustomerTypeFromDropDownInCustomerSearchTab("CONSULTANT");
		cscockpitHomePage.selectCountryFromDropDownInCustomerSearchTab("United States");
		cscockpitHomePage.selectAccountStatusFromDropDownInCustomerSearchTab("Active");
		cscockpitHomePage.enterEmailIdInSearchFieldInCustomerSearchTab(consultantEmailID);
		cscockpitHomePage.clickSearchBtn();
		randomCustomerSequenceNumber = String.valueOf(cscockpitHomePage.getRandomCustomerFromSearchResult());
		cscockpitHomePage.clickCIDNumberInCustomerSearchTab(randomCustomerSequenceNumber);
		cscockpitHomePage.clickPlaceOrderButtonInCustomerTab();
		cscockpitHomePage.selectValueFromSortByDDInCartTab("Price: High to Low");
		cscockpitHomePage.selectCatalogFromDropDownInCartTab();	
		randomProductSequenceNumber = String.valueOf(cscockpitHomePage.getRandomProductWithSKUFromSearchResult()); 
		SKUValue = cscockpitHomePage.getCustomerSKUValueInCartTab(randomProductSequenceNumber);
		cscockpitHomePage.searchSKUValueInCartTab(SKUValue);
		cscockpitHomePage.clickAddToCartBtnInCartTab();
		cscockpitHomePage.clickCheckoutBtnInCartTab();
		s_assert.assertTrue(cscockpitHomePage.getCreditCardNumberInCheckoutTab().contains("************"),"CSCockpit checkout tab credit card number expected = ************ and on UI = " +cscockpitHomePage.getCreditCardNumberInCheckoutTab());
		s_assert.assertTrue(cscockpitHomePage.getDeliverModeTypeInCheckoutTab().contains("FedEx Ground (HD)"),"CSCockpit checkout tab delivery mode type expected = FedEx Ground (HD) and on UI = " +cscockpitHomePage.getDeliverModeTypeInCheckoutTab());
		s_assert.assertTrue(cscockpitHomePage.isCommissionDatePopulatedInCheckoutTab(), "Commission date is not populated in UI");
		cscockpitHomePage.clickPlaceOrderButtonInCheckoutTab();
		s_assert.assertTrue(cscockpitHomePage.verifySelectPaymentDetailsPopupInCheckoutTab(), "Select payment details popup is not present");
		cscockpitHomePage.clickOkButtonOfSelectPaymentDetailsPopupInCheckoutTab();
		cscockpitHomePage.enterCVVValueInCheckoutTab(TestConstants.SECURITY_CODE);
		cscockpitHomePage.clickUseThisCardBtnInCheckoutTab();
		cscockpitHomePage.clickPlaceOrderButtonInCheckoutTab();
		orderNumber = cscockpitHomePage.getOrderNumberInOrderTab();
		cscockpitHomePage.clickCustomerTab();
		s_assert.assertTrue(cscockpitHomePage.getOrderTypeInCustomerTab(orderNumber.split("\\-")[0].trim()).contains("Consultant Order"),"CSCockpit Customer tab Order type expected = Consultant Order and on UI = " +cscockpitHomePage.getOrderTypeInCustomerTab(orderNumber.split("\\-")[0].trim()));
		driver.get(driver.getStoreFrontURL()+"/us");
		storeFrontHomePage = new StoreFrontHomePage(driver);
		storeFrontConsultantPage = storeFrontHomePage.loginAsConsultant(consultantEmailID, password);
		storeFrontConsultantPage.clickOnWelcomeDropDown();
		storeFrontOrdersPage =  storeFrontConsultantPage.clickOrdersLinkPresentOnWelcomeDropDown();
		s_assert.assertTrue(storeFrontOrdersPage.verifyOrdersPageIsDisplayed(),"Orders page has not been displayed");
		orderHistoryNumber = storeFrontOrdersPage.getFirstOrderNumberFromOrderHistory();
		s_assert.assertTrue(orderHistoryNumber.contains(orderNumber.split("\\-")[0].trim()),"CSCockpit Order number expected = "+orderNumber.split("\\-")[0].trim()+" and on UI = " +orderHistoryNumber);

		//-------------------FOR CA----------------------------------
		driver.get(driver.getStoreFrontURL()+"/ca");
		while(true){
			randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_CONSULTANT_WITH_ORDERS_AND_AUTOSHIPS_RFO,"40"),RFO_DB);
			consultantEmailID = (String) getValueFromQueryResult(randomConsultantList, "UserName");  
			storeFrontConsultantPage = storeFrontHomePage.loginAsConsultant(consultantEmailID, password);
			boolean isLoginError = driver.getCurrentUrl().contains("error");
			if(isLoginError){
				logger.info("Login error for the user "+consultantEmailID);
				driver.get(driver.getStoreFrontURL()+"/ca");
			}
			else
				break;
		}
		logout();
		cscockpitLoginPage = new CSCockpitLoginPage(driver);
		driver.get(driver.getCSCockpitURL());
		driver.pauseExecutionFor(5000);
		cscockpitHomePage = cscockpitLoginPage.clickLoginBtn();
		cscockpitHomePage.selectCustomerTypeFromDropDownInCustomerSearchTab("CONSULTANT");
		cscockpitHomePage.selectCountryFromDropDownInCustomerSearchTab("Canada");
		cscockpitHomePage.selectAccountStatusFromDropDownInCustomerSearchTab("Active");
		cscockpitHomePage.enterEmailIdInSearchFieldInCustomerSearchTab(consultantEmailID);
		cscockpitHomePage.clickSearchBtn();
		randomCustomerSequenceNumber = String.valueOf(cscockpitHomePage.getRandomCustomerFromSearchResult());
		cscockpitHomePage.clickCIDNumberInCustomerSearchTab(randomCustomerSequenceNumber);
		cscockpitHomePage.clickPlaceOrderButtonInCustomerTab();
		cscockpitHomePage.selectValueFromSortByDDInCartTab("Price: High to Low");
		cscockpitHomePage.selectCatalogFromDropDownInCartTab();	
		randomProductSequenceNumber = String.valueOf(cscockpitHomePage.getRandomProductWithSKUFromSearchResult()); 
		SKUValue = cscockpitHomePage.getCustomerSKUValueInCartTab(randomProductSequenceNumber);
		cscockpitHomePage.searchSKUValueInCartTab(SKUValue);
		cscockpitHomePage.clickAddToCartBtnInCartTab();
		cscockpitHomePage.clickCheckoutBtnInCartTab();
		s_assert.assertTrue(cscockpitHomePage.getCreditCardNumberInCheckoutTab().contains("************"),"CSCockpit checkout tab credit card number expected = ************ and on UI = " +cscockpitHomePage.getCreditCardNumberInCheckoutTab());
		//s_assert.assertTrue(cscockpitHomePage.getDeliverModeTypeInCheckoutTab().contains("UPS Ground (HD)"),"CSCockpit checkout tab delivery mode type expected = UPS Ground (HD) and on UI = " +cscockpitHomePage.getDeliverModeTypeInCheckoutTab());
		s_assert.assertTrue(cscockpitHomePage.isCommissionDatePopulatedInCheckoutTab(), "Commission date is not populated in UI");
		cscockpitHomePage.clickPlaceOrderButtonInCheckoutTab();
		s_assert.assertTrue(cscockpitHomePage.verifySelectPaymentDetailsPopupInCheckoutTab(), "Select payment details popup is not present");
		cscockpitHomePage.clickOkButtonOfSelectPaymentDetailsPopupInCheckoutTab();
		cscockpitHomePage.enterCVVValueInCheckoutTab(TestConstants.SECURITY_CODE);
		cscockpitHomePage.clickUseThisCardBtnInCheckoutTab();
		cscockpitHomePage.clickPlaceOrderButtonInCheckoutTab();
		orderNumber = cscockpitHomePage.getOrderNumberInOrderTab();
		cscockpitHomePage.clickCustomerTab();
		s_assert.assertTrue(cscockpitHomePage.getOrderTypeInCustomerTab(orderNumber.split("\\-")[0].trim()).contains("Consultant Order"),"CSCockpit Customer tab Order type expected = Consultant Order and on UI = " +cscockpitHomePage.getOrderTypeInCustomerTab(orderNumber.split("\\-")[0].trim()));
		driver.get(driver.getStoreFrontURL()+"/us");
		storeFrontConsultantPage = storeFrontHomePage.loginAsConsultant(consultantEmailID, password);
		storeFrontConsultantPage.clickOnWelcomeDropDown();
		storeFrontOrdersPage =  storeFrontConsultantPage.clickOrdersLinkPresentOnWelcomeDropDown();
		s_assert.assertTrue(storeFrontOrdersPage.verifyOrdersPageIsDisplayed(),"Orders page has not been displayed");
		orderHistoryNumber = storeFrontOrdersPage.getFirstOrderNumberFromOrderHistory();
		s_assert.assertTrue(orderHistoryNumber.contains(orderNumber.split("\\-")[0].trim()),"CSCockpit Order number expected = "+orderNumber.split("\\-")[0].trim()+" and on UI = " +orderHistoryNumber);

		s_assert.assertAll();
	}

	//Hybris Project-1941:To verify Adhoc PC order
	@Test(enabled=false)
	public void testToVerifyAdhocPCOrder_1941() throws InterruptedException{
		int randomNum = CommonUtils.getRandomNum(10000, 1000000);
		String randomCustomerSequenceNumber = null;
		String randomProductSequenceNumber = null;
		String SKUValue = null;
		String orderNumber = null;
		String orderHistoryNumber = null;

		//-------------------FOR US----------------------------------
		driver.get(driver.getStoreFrontURL()+"/us");
		String RFO_DB = driver.getDBNameRFO(); 
		List<Map<String, Object>> randomPCUserList =  null;
		String pcUserEmailID = null;
		String accountId = null;
		storeFrontHomePage = new StoreFrontHomePage(driver);
		while(true){
			randomPCUserList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_PC_WITH_ORDERS_AND_AUTOSHIPS_RFO,"236"),RFO_DB);
			pcUserEmailID = (String) getValueFromQueryResult(randomPCUserList, "UserName");		
			accountId = String.valueOf(getValueFromQueryResult(randomPCUserList, "AccountID"));
			logger.info("Account Id of the user is "+accountId);
			storeFrontPCUserPage = storeFrontHomePage.loginAsPCUser(pcUserEmailID, password);
			boolean isError = driver.getCurrentUrl().contains("error");
			if(isError){
				logger.info("SITE NOT FOUND for the user "+pcUserEmailID);
				driver.get(driver.getStoreFrontURL()+"/us");
			}
			else
				break;
		}	
		logout();
		cscockpitLoginPage = new CSCockpitLoginPage(driver);
		driver.get(driver.getCSCockpitURL());
		driver.pauseExecutionFor(5000);
		cscockpitHomePage = cscockpitLoginPage.clickLoginBtn();
		cscockpitHomePage.selectCustomerTypeFromDropDownInCustomerSearchTab("PC");
		cscockpitHomePage.selectCountryFromDropDownInCustomerSearchTab("United States");
		cscockpitHomePage.selectAccountStatusFromDropDownInCustomerSearchTab("Active");
		cscockpitHomePage.enterEmailIdInSearchFieldInCustomerSearchTab(pcUserEmailID);
		cscockpitHomePage.clickSearchBtn();
		randomCustomerSequenceNumber = String.valueOf(cscockpitHomePage.getRandomCustomerFromSearchResult());
		//PCEmailID = cscockpitHomePage.getEmailIdOfTheCustomerInCustomerSearchTab(randomCustomerSequenceNumber);
		cscockpitHomePage.clickCIDNumberInCustomerSearchTab(randomCustomerSequenceNumber);
		cscockpitHomePage.clickPlaceOrderButtonInCustomerTab();
		cscockpitHomePage.selectValueFromSortByDDInCartTab("Price: High to Low");
		cscockpitHomePage.selectCatalogFromDropDownInCartTab();	
		randomProductSequenceNumber = String.valueOf(cscockpitHomePage.getRandomProductWithSKUFromSearchResult()); 
		SKUValue = cscockpitHomePage.getCustomerSKUValueInCartTab(randomProductSequenceNumber);
		cscockpitHomePage.searchSKUValueInCartTab(SKUValue);
		cscockpitHomePage.clickAddToCartBtnInCartTab();
		cscockpitHomePage.clickCheckoutBtnInCartTab();
		s_assert.assertTrue(cscockpitHomePage.getCreditCardNumberInCheckoutTab().contains("************"),"CSCockpit checkout tab credit card number expected = ************ and on UI = " +cscockpitHomePage.getCreditCardNumberInCheckoutTab());
		s_assert.assertTrue(cscockpitHomePage.getDeliverModeTypeInCheckoutTab().contains("FedEx Ground (HD)"),"CSCockpit checkout tab delivery mode type expected = FedEx Ground (HD) and on UI = " +cscockpitHomePage.getDeliverModeTypeInCheckoutTab());
		s_assert.assertTrue(cscockpitHomePage.isCommissionDatePopulatedInCheckoutTab(), "Commission date is not populated in UI");
		cscockpitHomePage.clickPlaceOrderButtonInCheckoutTab();
		s_assert.assertTrue(cscockpitHomePage.verifySelectPaymentDetailsPopupInCheckoutTab(), "Select payment details popup is not present");
		cscockpitHomePage.clickOkButtonOfSelectPaymentDetailsPopupInCheckoutTab();
		cscockpitHomePage.enterOrderNotesInCheckoutTab(TestConstants.ORDER_NOTE+randomNum);
		String orderNotevalueFromUI = cscockpitHomePage.getAddedNoteValueInCheckoutTab(TestConstants.ORDER_NOTE+randomNum);
		s_assert.assertTrue(cscockpitHomePage.getPSTDate().contains(cscockpitHomePage.convertUIDateFormatToPSTFormat(orderNotevalueFromUI.split("\\ ")[0]).trim()),"CSCockpit added order note date in checkout tab expected"+cscockpitHomePage.getPSTDate()+"and on UI" +cscockpitHomePage.convertUIDateFormatToPSTFormat(orderNotevalueFromUI.split("\\ ")[0]).trim());
		s_assert.assertTrue(orderNotevalueFromUI.contains("PM")||orderNotevalueFromUI.contains("AM"), "Added order note does not contain time zone");
		s_assert.assertTrue(cscockpitHomePage.verifyEditButtonIsPresentForOrderNoteInCheckoutTab(TestConstants.ORDER_NOTE+randomNum), "Added order note does not have Edit button");
		cscockpitHomePage.enterCVVValueInCheckoutTab(TestConstants.SECURITY_CODE);
		cscockpitHomePage.clickUseThisCardBtnInCheckoutTab();
		cscockpitHomePage.clickPlaceOrderButtonInCheckoutTab();
		orderNumber = cscockpitHomePage.getOrderNumberInOrderTab();
		cscockpitHomePage.clickCustomerTab();
		s_assert.assertTrue(cscockpitHomePage.getOrderTypeInCustomerTab(orderNumber.split("\\-")[0].trim()).contains("PC Order"),"CSCockpit Customer tab Order type expected = PC Order and on UI = " +cscockpitHomePage.getOrderTypeInCustomerTab(orderNumber.split("\\-")[0].trim()));
		driver.get(driver.getStoreFrontURL()+"/us");
		storeFrontPCUserPage = storeFrontHomePage.loginAsPCUser(pcUserEmailID, password);
		storeFrontPCUserPage.clickOnWelcomeDropDown();
		storeFrontOrdersPage =  storeFrontPCUserPage.clickOrdersLinkPresentOnWelcomeDropDown();
		s_assert.assertTrue(storeFrontOrdersPage.verifyOrdersPageIsDisplayed(),"Orders page has not been displayed");
		orderHistoryNumber = storeFrontOrdersPage.getFirstOrderNumberFromOrderHistory();
		s_assert.assertTrue(orderHistoryNumber.contains(orderNumber.split("\\-")[0].trim()),"CSCockpit Order number expected = "+orderNumber.split("\\-")[0].trim()+" and on UI = " +orderHistoryNumber);

		//-------------------FOR CA----------------------------------
		driver.get(driver.getStoreFrontURL()+"/ca");
		while(true){
			randomPCUserList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_PC_WITH_ORDERS_AND_AUTOSHIPS_RFO,"40"),RFO_DB);
			pcUserEmailID = (String) getValueFromQueryResult(randomPCUserList, "UserName");		
			accountId = String.valueOf(getValueFromQueryResult(randomPCUserList, "AccountID"));
			logger.info("Account Id of the user is "+accountId);
			storeFrontPCUserPage = storeFrontHomePage.loginAsPCUser(pcUserEmailID, password);
			boolean isError = driver.getCurrentUrl().contains("error");
			if(isError){
				logger.info("SITE NOT FOUND for the user "+pcUserEmailID);
				driver.get(driver.getStoreFrontURL()+"/us");
			}
			else
				break;
		}
		logout();
		cscockpitLoginPage = new CSCockpitLoginPage(driver);
		driver.pauseExecutionFor(5000);
		driver.get(driver.getCSCockpitURL());
		driver.pauseExecutionFor(5000);
		cscockpitHomePage = cscockpitLoginPage.clickLoginBtn();
		cscockpitHomePage.selectCustomerTypeFromDropDownInCustomerSearchTab("PC");
		cscockpitHomePage.selectCountryFromDropDownInCustomerSearchTab("Canada");
		cscockpitHomePage.selectAccountStatusFromDropDownInCustomerSearchTab("Active");
		cscockpitHomePage.enterEmailIdInSearchFieldInCustomerSearchTab(pcUserEmailID);
		cscockpitHomePage.clickSearchBtn();
		cscockpitHomePage.clickSearchBtn();
		randomCustomerSequenceNumber = String.valueOf(cscockpitHomePage.getRandomCustomerFromSearchResult());
		cscockpitHomePage.clickCIDNumberInCustomerSearchTab(randomCustomerSequenceNumber);
		cscockpitHomePage.clickPlaceOrderButtonInCustomerTab();
		cscockpitHomePage.selectValueFromSortByDDInCartTab("Price: High to Low");
		cscockpitHomePage.selectCatalogFromDropDownInCartTab();	
		randomProductSequenceNumber = String.valueOf(cscockpitHomePage.getRandomProductWithSKUFromSearchResult()); 
		SKUValue = cscockpitHomePage.getCustomerSKUValueInCartTab(randomProductSequenceNumber);
		cscockpitHomePage.searchSKUValueInCartTab(SKUValue);
		cscockpitHomePage.clickAddToCartBtnInCartTab();
		cscockpitHomePage.clickCheckoutBtnInCartTab();
		s_assert.assertTrue(cscockpitHomePage.getCreditCardNumberInCheckoutTab().contains("************"),"CSCockpit checkout tab credit card number expected = ************ and on UI = " +cscockpitHomePage.getCreditCardNumberInCheckoutTab());
		s_assert.assertTrue(cscockpitHomePage.getDeliverModeTypeInCheckoutTab().contains("UPS Ground (HD)"),"CSCockpit checkout tab delivery mode type expected = UPS Ground (HD) and on UI = " +cscockpitHomePage.getDeliverModeTypeInCheckoutTab());
		s_assert.assertTrue(cscockpitHomePage.isCommissionDatePopulatedInCheckoutTab(), "Commission date is not populated in UI");
		cscockpitHomePage.clickPlaceOrderButtonInCheckoutTab();
		s_assert.assertTrue(cscockpitHomePage.verifySelectPaymentDetailsPopupInCheckoutTab(), "Select payment details popup is not present");
		cscockpitHomePage.clickOkButtonOfSelectPaymentDetailsPopupInCheckoutTab();
		cscockpitHomePage.enterOrderNotesInCheckoutTab(TestConstants.ORDER_NOTE+randomNum);
		orderNotevalueFromUI = cscockpitHomePage.getAddedNoteValueInCheckoutTab(TestConstants.ORDER_NOTE+randomNum);
		s_assert.assertTrue(cscockpitHomePage.getPSTDate().contains(cscockpitHomePage.convertUIDateFormatToPSTFormat(orderNotevalueFromUI.split("\\ ")[0]).trim()),"CSCockpit added order note date in checkout tab expected"+cscockpitHomePage.getPSTDate()+"and on UI" +cscockpitHomePage.convertUIDateFormatToPSTFormat(orderNotevalueFromUI.split("\\ ")[0]).trim());
		s_assert.assertTrue(orderNotevalueFromUI.contains("PM")||orderNotevalueFromUI.contains("AM"), "Added order note does not contain time zone");
		s_assert.assertTrue(cscockpitHomePage.verifyEditButtonIsPresentForOrderNoteInCheckoutTab(TestConstants.ORDER_NOTE+randomNum), "Added order note does not have Edit button");
		cscockpitHomePage.enterCVVValueInCheckoutTab(TestConstants.SECURITY_CODE);
		cscockpitHomePage.clickUseThisCardBtnInCheckoutTab();
		cscockpitHomePage.clickPlaceOrderButtonInCheckoutTab();
		orderNumber = cscockpitHomePage.getOrderNumberInOrderTab();
		cscockpitHomePage.clickCustomerTab();
		s_assert.assertTrue(cscockpitHomePage.getOrderTypeInCustomerTab(orderNumber.split("\\-")[0].trim()).contains("PC Order"),"CSCockpit Customer tab Order type expected = PC Order and on UI = " +cscockpitHomePage.getOrderTypeInCustomerTab(orderNumber.split("\\-")[0].trim()));
		driver.get(driver.getStoreFrontURL()+"/ca");
		storeFrontHomePage = new StoreFrontHomePage(driver);
		storeFrontPCUserPage = storeFrontHomePage.loginAsPCUser(pcUserEmailID, password);
		storeFrontPCUserPage.clickOnWelcomeDropDown();
		storeFrontOrdersPage =  storeFrontPCUserPage.clickOrdersLinkPresentOnWelcomeDropDown();
		s_assert.assertTrue(storeFrontOrdersPage.verifyOrdersPageIsDisplayed(),"Orders page has not been displayed");
		orderHistoryNumber = storeFrontOrdersPage.getFirstOrderNumberFromOrderHistory();
		s_assert.assertTrue(orderHistoryNumber.contains(orderNumber.split("\\-")[0].trim()),"CSCockpit Order number expected = "+orderNumber.split("\\-")[0].trim()+" and on UI = " +orderHistoryNumber);

		s_assert.assertAll();
	}


	//Hybris Project-1942:To verify Adhoc retail order
	@Test(enabled=false)
	public void testToVerifyAdhocRetailOrder_1942() throws InterruptedException{
		int randomNum = CommonUtils.getRandomNum(10000, 1000000);
		String randomCustomerSequenceNumber = null;
		String randomProductSequenceNumber = null;
		String SKUValue = null;
		String orderNumber = null;
		String orderHistoryNumber = null;

		//-------------------FOR US----------------------------------
		driver.get(driver.getStoreFrontURL()+"/us");
		String RFO_DB = driver.getDBNameRFO(); 
		List<Map<String, Object>> randomRCList =  null;
		String rcUserEmailID =null;
		String accountId = null;
		storeFrontHomePage = new StoreFrontHomePage(driver);
		while(true){
			randomRCList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_RC_HAVING_ORDERS_RFO,"236"),RFO_DB);
			rcUserEmailID = (String) getValueFromQueryResult(randomRCList, "UserName");		
			accountId = String.valueOf(getValueFromQueryResult(randomRCList, "AccountID"));
			logger.info("Account Id of the user is "+accountId);

			storeFrontRCUserPage = storeFrontHomePage.loginAsRCUser(rcUserEmailID, password);
			boolean isError = driver.getCurrentUrl().contains("error");
			if(isError){
				logger.info("login error for the user "+rcUserEmailID);
				driver.get(driver.getURL());
			}
			else
				break;
		}
		logger.info("login is successful");
		logout();
		cscockpitLoginPage = new CSCockpitLoginPage(driver);
		driver.get(driver.getCSCockpitURL());
		driver.pauseExecutionFor(5000);
		cscockpitHomePage = cscockpitLoginPage.clickLoginBtn();
		cscockpitHomePage.selectCustomerTypeFromDropDownInCustomerSearchTab("RETAIL");
		cscockpitHomePage.selectCountryFromDropDownInCustomerSearchTab("United States");
		cscockpitHomePage.selectAccountStatusFromDropDownInCustomerSearchTab("Active");
		cscockpitHomePage.enterEmailIdInSearchFieldInCustomerSearchTab(rcUserEmailID);
		cscockpitHomePage.clickSearchBtn();
		randomCustomerSequenceNumber = String.valueOf(cscockpitHomePage.getRandomCustomerFromSearchResult());
		cscockpitHomePage.clickCIDNumberInCustomerSearchTab(randomCustomerSequenceNumber);
		cscockpitHomePage.clickPlaceOrderButtonInCustomerTab();
		cscockpitHomePage.selectValueFromSortByDDInCartTab("Price: High to Low");
		cscockpitHomePage.selectCatalogFromDropDownInCartTab();	
		randomProductSequenceNumber = String.valueOf(cscockpitHomePage.getRandomProductWithSKUFromSearchResult()); 
		SKUValue = cscockpitHomePage.getCustomerSKUValueInCartTab(randomProductSequenceNumber);
		cscockpitHomePage.searchSKUValueInCartTab(SKUValue);
		cscockpitHomePage.clickAddToCartBtnInCartTab();
		cscockpitHomePage.clickCheckoutBtnInCartTab();
		s_assert.assertTrue(cscockpitHomePage.getCreditCardNumberInCheckoutTab().contains("************"),"CSCockpit checkout tab credit card number expected = ************ and on UI = " +cscockpitHomePage.getCreditCardNumberInCheckoutTab());
		s_assert.assertTrue(cscockpitHomePage.getDeliverModeTypeInCheckoutTab().contains("FedEx Ground (HD)"),"CSCockpit checkout tab delivery mode type expected = FedEx Ground (HD) and on UI = " +cscockpitHomePage.getDeliverModeTypeInCheckoutTab());
		s_assert.assertTrue(cscockpitHomePage.isCommissionDatePopulatedInCheckoutTab(), "Commission date is not populated in UI");
		cscockpitHomePage.clickPlaceOrderButtonInCheckoutTab();
		s_assert.assertTrue(cscockpitHomePage.verifySelectPaymentDetailsPopupInCheckoutTab(), "Select payment details popup is not present");
		cscockpitHomePage.clickOkButtonOfSelectPaymentDetailsPopupInCheckoutTab();
		cscockpitHomePage.enterOrderNotesInCheckoutTab(TestConstants.ORDER_NOTE+randomNum);
		String orderNotevalueFromUI = cscockpitHomePage.getAddedNoteValueInCheckoutTab(TestConstants.ORDER_NOTE+randomNum);
		s_assert.assertTrue(cscockpitHomePage.getPSTDate().contains(cscockpitHomePage.convertUIDateFormatToPSTFormat(orderNotevalueFromUI.split("\\ ")[0]).trim()),"CSCockpit added order note date in checkout tab expected"+cscockpitHomePage.getPSTDate()+"and on UI" +cscockpitHomePage.convertUIDateFormatToPSTFormat(orderNotevalueFromUI.split("\\ ")[0]).trim());
		s_assert.assertTrue(orderNotevalueFromUI.contains("PM")||orderNotevalueFromUI.contains("AM"), "Added order note does not contain time zone");
		s_assert.assertTrue(cscockpitHomePage.verifyEditButtonIsPresentForOrderNoteInCheckoutTab(TestConstants.ORDER_NOTE+randomNum), "Added order note does not have Edit button");
		cscockpitHomePage.enterCVVValueInCheckoutTab(TestConstants.SECURITY_CODE);
		cscockpitHomePage.clickUseThisCardBtnInCheckoutTab();
		cscockpitHomePage.clickPlaceOrderButtonInCheckoutTab();
		orderNumber = cscockpitHomePage.getOrderNumberInOrderTab();
		cscockpitHomePage.clickCustomerTab();
		s_assert.assertTrue(cscockpitHomePage.getOrderTypeInCustomerTab(orderNumber.split("\\-")[0].trim()).contains("Retail Order"),"CSCockpit Customer tab Order type expected = Retail Order and on UI = " +cscockpitHomePage.getOrderTypeInCustomerTab(orderNumber.split("\\-")[0].trim()));
		driver.get(driver.getStoreFrontURL()+"/us");
		storeFrontRCUserPage = storeFrontHomePage.loginAsRCUser(rcUserEmailID, password);
		storeFrontRCUserPage.clickOnWelcomeDropDown();
		storeFrontOrdersPage =  storeFrontRCUserPage.clickOrdersLinkPresentOnWelcomeDropDown();
		s_assert.assertTrue(storeFrontOrdersPage.verifyOrdersPageIsDisplayed(),"Orders page has not been displayed");
		orderHistoryNumber = storeFrontOrdersPage.getFirstOrderNumberFromOrderHistory();
		s_assert.assertTrue(orderHistoryNumber.contains(orderNumber.split("\\-")[0].trim()),"CSCockpit Order number expected = "+orderNumber.split("\\-")[0].trim()+" and on UI = " +orderHistoryNumber);

		//-------------------FOR CA----------------------------------
		driver.get(driver.getStoreFrontURL()+"/ca");
		while(true){
			randomRCList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_RC_HAVING_ORDERS_RFO,"40"),RFO_DB);
			rcUserEmailID = (String) getValueFromQueryResult(randomRCList, "UserName");		
			accountId = String.valueOf(getValueFromQueryResult(randomRCList, "AccountID"));
			logger.info("Account Id of the user is "+accountId);

			storeFrontRCUserPage = storeFrontHomePage.loginAsRCUser(rcUserEmailID, password);
			boolean isError = driver.getCurrentUrl().contains("error");
			if(isError){
				logger.info("login error for the user "+rcUserEmailID);
				driver.get(driver.getURL());
			}
			else
				break;
		}
		logger.info("login is successful");
		logout();
		cscockpitLoginPage = new CSCockpitLoginPage(driver);
		driver.get(driver.getCSCockpitURL());
		driver.pauseExecutionFor(5000);
		cscockpitHomePage = cscockpitLoginPage.clickLoginBtn();
		cscockpitHomePage.selectCustomerTypeFromDropDownInCustomerSearchTab("RETAIL");
		cscockpitHomePage.selectCountryFromDropDownInCustomerSearchTab("Canada");
		cscockpitHomePage.selectAccountStatusFromDropDownInCustomerSearchTab("Active");
		cscockpitHomePage.enterEmailIdInSearchFieldInCustomerSearchTab(rcUserEmailID);
		cscockpitHomePage.clickSearchBtn();
		randomCustomerSequenceNumber = String.valueOf(cscockpitHomePage.getRandomCustomerFromSearchResult());
		cscockpitHomePage.clickCIDNumberInCustomerSearchTab(randomCustomerSequenceNumber);
		cscockpitHomePage.clickPlaceOrderButtonInCustomerTab();
		cscockpitHomePage.selectValueFromSortByDDInCartTab("Price: High to Low");
		cscockpitHomePage.selectCatalogFromDropDownInCartTab();	
		randomProductSequenceNumber = String.valueOf(cscockpitHomePage.getRandomProductWithSKUFromSearchResult()); 
		SKUValue = cscockpitHomePage.getCustomerSKUValueInCartTab(randomProductSequenceNumber);
		cscockpitHomePage.searchSKUValueInCartTab(SKUValue);
		cscockpitHomePage.clickAddToCartBtnInCartTab();
		cscockpitHomePage.clickCheckoutBtnInCartTab();
		s_assert.assertTrue(cscockpitHomePage.getCreditCardNumberInCheckoutTab().contains("************"),"CSCockpit checkout tab credit card number expected = ************ and on UI = " +cscockpitHomePage.getCreditCardNumberInCheckoutTab());
		s_assert.assertTrue(cscockpitHomePage.getDeliverModeTypeInCheckoutTab().contains("UPS Ground (HD)"),"CSCockpit checkout tab delivery mode type expected = UPS Ground (HD) and on UI = " +cscockpitHomePage.getDeliverModeTypeInCheckoutTab());
		s_assert.assertTrue(cscockpitHomePage.isCommissionDatePopulatedInCheckoutTab(), "Commission date is not populated in UI");
		cscockpitHomePage.clickPlaceOrderButtonInCheckoutTab();
		s_assert.assertTrue(cscockpitHomePage.verifySelectPaymentDetailsPopupInCheckoutTab(), "Select payment details popup is not present");
		cscockpitHomePage.clickOkButtonOfSelectPaymentDetailsPopupInCheckoutTab();
		cscockpitHomePage.enterOrderNotesInCheckoutTab(TestConstants.ORDER_NOTE+randomNum);
		orderNotevalueFromUI = cscockpitHomePage.getAddedNoteValueInCheckoutTab(TestConstants.ORDER_NOTE+randomNum);
		s_assert.assertTrue(cscockpitHomePage.getPSTDate().contains(cscockpitHomePage.convertUIDateFormatToPSTFormat(orderNotevalueFromUI.split("\\ ")[0]).trim()),"CSCockpit added order note date in checkout tab expected"+cscockpitHomePage.getPSTDate()+"and on UI" +cscockpitHomePage.convertUIDateFormatToPSTFormat(orderNotevalueFromUI.split("\\ ")[0]).trim());
		s_assert.assertTrue(orderNotevalueFromUI.contains("PM")||orderNotevalueFromUI.contains("AM"), "Added order note does not contain time zone");
		s_assert.assertTrue(cscockpitHomePage.verifyEditButtonIsPresentForOrderNoteInCheckoutTab(TestConstants.ORDER_NOTE+randomNum), "Added order note does not have Edit button");
		cscockpitHomePage.enterCVVValueInCheckoutTab(TestConstants.SECURITY_CODE);
		cscockpitHomePage.clickUseThisCardBtnInCheckoutTab();
		cscockpitHomePage.clickPlaceOrderButtonInCheckoutTab();
		orderNumber = cscockpitHomePage.getOrderNumberInOrderTab();
		cscockpitHomePage.clickCustomerTab();
		s_assert.assertTrue(cscockpitHomePage.getOrderTypeInCustomerTab(orderNumber.split("\\-")[0].trim()).contains("Retail Order"),"CSCockpit Customer tab Order type expected = Retail Order and on UI = " +cscockpitHomePage.getOrderTypeInCustomerTab(orderNumber.split("\\-")[0].trim()));
		driver.get(driver.getStoreFrontURL()+"/ca");
		storeFrontRCUserPage = storeFrontHomePage.loginAsRCUser(rcUserEmailID, password);
		storeFrontRCUserPage.clickOnWelcomeDropDown();
		storeFrontOrdersPage =  storeFrontRCUserPage.clickOrdersLinkPresentOnWelcomeDropDown();
		s_assert.assertTrue(storeFrontOrdersPage.verifyOrdersPageIsDisplayed(),"Orders page has not been displayed");
		orderHistoryNumber = storeFrontOrdersPage.getFirstOrderNumberFromOrderHistory();
		s_assert.assertTrue(orderHistoryNumber.contains(orderNumber.split("\\-")[0].trim()),"CSCockpit Order number expected = "+orderNumber.split("\\-")[0].trim()+" and on UI = " +orderHistoryNumber);
		s_assert.assertAll();
	}

	//Hybris Project-1810:To verify that cscommissionadmin can place order
	@Test(enabled=false)
	public void testVerifyCSCommissionAdminCanPlaceOrder_1810() throws InterruptedException{
		String randomCustomerSequenceNumber = null;
		String randomProductSequenceNumber = null;
		String SKUValue = null;

		//-------------------FOR US----------------------------------
		cscockpitLoginPage = new CSCockpitLoginPage(driver);
		driver.pauseExecutionFor(5000);
		cscockpitLoginPage.enterUsername(TestConstants.CS_COMMISION_ADMIN_USERNAME);
		cscockpitHomePage = cscockpitLoginPage.clickLoginBtn();
		cscockpitHomePage.selectCustomerTypeFromDropDownInCustomerSearchTab("CONSULTANT");
		cscockpitHomePage.selectCountryFromDropDownInCustomerSearchTab("United States");
		cscockpitHomePage.selectAccountStatusFromDropDownInCustomerSearchTab("Active");
		cscockpitHomePage.clickSearchBtn();
		randomCustomerSequenceNumber = String.valueOf(cscockpitHomePage.getRandomCustomerFromSearchResult());
		cscockpitHomePage.clickCIDNumberInCustomerSearchTab(randomCustomerSequenceNumber);
		cscockpitHomePage.clickPlaceOrderButtonInCustomerTab();
		cscockpitHomePage.selectValueFromSortByDDInCartTab("Price: High to Low");
		cscockpitHomePage.selectCatalogFromDropDownInCartTab();	
		randomProductSequenceNumber = String.valueOf(cscockpitHomePage.getRandomProductWithSKUFromSearchResult()); 
		SKUValue = cscockpitHomePage.getCustomerSKUValueInCartTab(randomProductSequenceNumber);
		cscockpitHomePage.searchSKUValueInCartTab(SKUValue);
		cscockpitHomePage.clickAddToCartBtnInCartTab();
		cscockpitHomePage.clickCheckoutBtnInCartTab();
		cscockpitHomePage.enterCVVValueInCheckoutTab(TestConstants.SECURITY_CODE);
		cscockpitHomePage.clickUseThisCardBtnInCheckoutTab();
		cscockpitHomePage.clickPlaceOrderButtonInCheckoutTab();
		s_assert.assertTrue(cscockpitHomePage.getCountOfOrdersOnOrdersDetailsPageAfterPlacingOrder()>0, "Order was NOT placed successfully,expected count after placing order in order detail items section >0 but actual count on UI = "+cscockpitHomePage.getCountOfOrdersOnOrdersDetailsPageAfterPlacingOrder());

		//-------------------FOR CA----------------------------------
		driver.get(driver.getCSCockpitURL());
		driver.pauseExecutionFor(5000);
		cscockpitLoginPage.enterUsername(TestConstants.CS_COMMISION_ADMIN_USERNAME);
		cscockpitHomePage = cscockpitLoginPage.clickLoginBtn();
		cscockpitHomePage.selectCustomerTypeFromDropDownInCustomerSearchTab("CONSULTANT");
		cscockpitHomePage.selectCountryFromDropDownInCustomerSearchTab("Canada");
		cscockpitHomePage.selectAccountStatusFromDropDownInCustomerSearchTab("Active");
		cscockpitHomePage.clickSearchBtn();
		randomCustomerSequenceNumber = String.valueOf(cscockpitHomePage.getRandomCustomerFromSearchResult());
		cscockpitHomePage.clickCIDNumberInCustomerSearchTab(randomCustomerSequenceNumber);
		cscockpitHomePage.clickPlaceOrderButtonInCustomerTab();
		cscockpitHomePage.selectValueFromSortByDDInCartTab("Price: High to Low");
		cscockpitHomePage.selectCatalogFromDropDownInCartTab();	
		randomProductSequenceNumber = String.valueOf(cscockpitHomePage.getRandomProductWithSKUFromSearchResult()); 
		SKUValue = cscockpitHomePage.getCustomerSKUValueInCartTab(randomProductSequenceNumber);
		cscockpitHomePage.searchSKUValueInCartTab(SKUValue);
		cscockpitHomePage.clickAddToCartBtnInCartTab();
		cscockpitHomePage.clickCheckoutBtnInCartTab();
		cscockpitHomePage.enterCVVValueInCheckoutTab(TestConstants.SECURITY_CODE);
		cscockpitHomePage.clickUseThisCardBtnInCheckoutTab();
		cscockpitHomePage.clickPlaceOrderButtonInCheckoutTab();
		s_assert.assertTrue(cscockpitHomePage.getCountOfOrdersOnOrdersDetailsPageAfterPlacingOrder()>0, "Order was NOT placed successfully,expected count after placing order in order detail items section >0 but actual count on UI = "+cscockpitHomePage.getCountOfOrdersOnOrdersDetailsPageAfterPlacingOrder());
		s_assert.assertAll();
	}

	//Hybris Project-1820:To verify that cssalessupervisory can place order
	@Test(enabled=false)
	public void testVerifyCSSalesSuperVisoryCanPlaceOrder_1820() throws InterruptedException{
		String randomCustomerSequenceNumber = null;
		String randomProductSequenceNumber = null;
		String SKUValue = null;

		//-------------------FOR US----------------------------------
		cscockpitLoginPage = new CSCockpitLoginPage(driver);
		driver.pauseExecutionFor(5000);
		cscockpitLoginPage.enterUsername(TestConstants.CS_SALES_SUPERVISORY_USERNAME);
		cscockpitHomePage = cscockpitLoginPage.clickLoginBtn();
		cscockpitHomePage.selectCustomerTypeFromDropDownInCustomerSearchTab("CONSULTANT");
		cscockpitHomePage.selectCountryFromDropDownInCustomerSearchTab("United States");
		cscockpitHomePage.selectAccountStatusFromDropDownInCustomerSearchTab("Active");
		cscockpitHomePage.clickSearchBtn();
		randomCustomerSequenceNumber = String.valueOf(cscockpitHomePage.getRandomCustomerFromSearchResult());
		cscockpitHomePage.clickCIDNumberInCustomerSearchTab(randomCustomerSequenceNumber);
		cscockpitHomePage.clickPlaceOrderButtonInCustomerTab();
		cscockpitHomePage.selectValueFromSortByDDInCartTab("Price: High to Low");
		cscockpitHomePage.selectCatalogFromDropDownInCartTab();	
		randomProductSequenceNumber = String.valueOf(cscockpitHomePage.getRandomProductWithSKUFromSearchResult()); 
		SKUValue = cscockpitHomePage.getCustomerSKUValueInCartTab(randomProductSequenceNumber);
		cscockpitHomePage.searchSKUValueInCartTab(SKUValue);
		cscockpitHomePage.clickAddToCartBtnInCartTab();
		cscockpitHomePage.clickCheckoutBtnInCartTab();
		cscockpitHomePage.enterCVVValueInCheckoutTab(TestConstants.SECURITY_CODE);
		cscockpitHomePage.clickUseThisCardBtnInCheckoutTab();
		cscockpitHomePage.clickPlaceOrderButtonInCheckoutTab();
		s_assert.assertTrue(cscockpitHomePage.getCountOfOrdersOnOrdersDetailsPageAfterPlacingOrder()>0, "Order was NOT placed successfully,expected count after placing order in order detail items section >0 but actual count on UI = "+cscockpitHomePage.getCountOfOrdersOnOrdersDetailsPageAfterPlacingOrder());

		//-------------------FOR CA----------------------------------
		driver.get(driver.getCSCockpitURL());
		driver.pauseExecutionFor(5000);
		cscockpitLoginPage.enterUsername(TestConstants.CS_SALES_SUPERVISORY_USERNAME);
		cscockpitHomePage = cscockpitLoginPage.clickLoginBtn();
		cscockpitHomePage.selectCustomerTypeFromDropDownInCustomerSearchTab("CONSULTANT");
		cscockpitHomePage.selectCountryFromDropDownInCustomerSearchTab("Canada");
		cscockpitHomePage.selectAccountStatusFromDropDownInCustomerSearchTab("Active");
		cscockpitHomePage.clickSearchBtn();
		randomCustomerSequenceNumber = String.valueOf(cscockpitHomePage.getRandomCustomerFromSearchResult());
		cscockpitHomePage.clickCIDNumberInCustomerSearchTab(randomCustomerSequenceNumber);
		cscockpitHomePage.clickPlaceOrderButtonInCustomerTab();
		cscockpitHomePage.selectValueFromSortByDDInCartTab("Price: High to Low");
		cscockpitHomePage.selectCatalogFromDropDownInCartTab();	
		randomProductSequenceNumber = String.valueOf(cscockpitHomePage.getRandomProductWithSKUFromSearchResult()); 
		SKUValue = cscockpitHomePage.getCustomerSKUValueInCartTab(randomProductSequenceNumber);
		cscockpitHomePage.searchSKUValueInCartTab(SKUValue);
		cscockpitHomePage.clickAddToCartBtnInCartTab();
		cscockpitHomePage.clickCheckoutBtnInCartTab();
		cscockpitHomePage.enterCVVValueInCheckoutTab(TestConstants.SECURITY_CODE);
		cscockpitHomePage.clickUseThisCardBtnInCheckoutTab();
		cscockpitHomePage.clickPlaceOrderButtonInCheckoutTab();
		s_assert.assertTrue(cscockpitHomePage.getCountOfOrdersOnOrdersDetailsPageAfterPlacingOrder()>0, "Order was NOT placed successfully,expected count after placing order in order detail items section >0 but actual count on UI = "+cscockpitHomePage.getCountOfOrdersOnOrdersDetailsPageAfterPlacingOrder());
		s_assert.assertAll();
	}

	//Hybris Project-1780:To verify user permission for transaction status change in Order detail page
	@Test(enabled=false)
	public void testVerifyUserPermissionForTransactionStatus_1780(){
		String randomOrderSequenceNumber = null;
		cscockpitLoginPage = new CSCockpitLoginPage(driver);
		driver.pauseExecutionFor(5000);
		cscockpitLoginPage.enterUsername(TestConstants.CS_AGENT_USERNAME);
		cscockpitHomePage = cscockpitLoginPage.clickLoginBtn();
		cscockpitHomePage.clickOrderSearchTab();
		cscockpitHomePage.clickSearchBtn();
		randomOrderSequenceNumber = String.valueOf(cscockpitHomePage.getRandomOrdersFromOrderResultSearchFirstPageInOrderSearchTab());
		cscockpitHomePage.clickOrderNumberInOrderSearchResultsInOrderSearchTab(randomOrderSequenceNumber);
		s_assert.assertFalse(cscockpitHomePage.isPaymentTransactionStatusClickableOnOrderTab(), "Transaction Status on Payment Transactions Section is clickable for "+TestConstants.CS_AGENT_USERNAME);

		//		driver.get(driver.getCSCockpitURL());
		//		driver.pauseExecutionFor(5000);
		//		cscockpitLoginPage.enterUsername(TestConstants.ADMIN_USERNAME);
		//		cscockpitHomePage = cscockpitLoginPage.clickLoginBtn();
		//		cscockpitHomePage.clickOrderSearchTab();
		//		cscockpitHomePage.clickSearchBtn();
		//		randomOrderSequenceNumber = String.valueOf(cscockpitHomePage.getRandomOrdersFromOrderResultSearchFirstPageInOrderSearchTab());
		//		cscockpitHomePage.clickOrderNumberInOrderSearchResultsInOrderSearchTab(randomOrderSequenceNumber);
		//		s_assert.assertFalse(cscockpitHomePage.isPaymentTransactionStatusClickableOnOrderTab(), "Transaction Status on Payment Transactions Section is clickable for "+TestConstants.ADMIN_USERNAME);

		driver.get(driver.getCSCockpitURL());
		driver.pauseExecutionFor(5000);
		cscockpitLoginPage.enterUsername(TestConstants.CS_COMMISION_ADMIN_USERNAME);
		cscockpitHomePage = cscockpitLoginPage.clickLoginBtn();
		cscockpitHomePage.clickOrderSearchTab();
		cscockpitHomePage.clickSearchBtn();
		randomOrderSequenceNumber = String.valueOf(cscockpitHomePage.getRandomOrdersFromOrderResultSearchFirstPageInOrderSearchTab());
		cscockpitHomePage.clickOrderNumberInOrderSearchResultsInOrderSearchTab(randomOrderSequenceNumber);
		s_assert.assertFalse(cscockpitHomePage.isPaymentTransactionStatusClickableOnOrderTab(), "Transaction Status on Payment Transactions Section is clickable for "+TestConstants.CS_COMMISION_ADMIN_USERNAME);

		driver.get(driver.getCSCockpitURL());
		driver.pauseExecutionFor(5000);
		cscockpitLoginPage.enterUsername(TestConstants.CS_SALES_SUPERVISORY_USERNAME);
		cscockpitHomePage = cscockpitLoginPage.clickLoginBtn();
		cscockpitHomePage.clickOrderSearchTab();
		cscockpitHomePage.clickSearchBtn();
		randomOrderSequenceNumber = String.valueOf(cscockpitHomePage.getRandomOrdersFromOrderResultSearchFirstPageInOrderSearchTab());
		cscockpitHomePage.clickOrderNumberInOrderSearchResultsInOrderSearchTab(randomOrderSequenceNumber);
		s_assert.assertTrue(cscockpitHomePage.isPaymentTransactionStatusClickableOnOrderTab(), "Transaction Status on Payment Transactions Section is NOT clickable for "+TestConstants.CS_SALES_SUPERVISORY_USERNAME);

		s_assert.assertAll();
	}

}
