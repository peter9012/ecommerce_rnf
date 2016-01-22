package com.rf.test.website.cscockpit.miniRegression;

import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.annotations.Test;

import com.rf.core.utils.DBUtil;
import com.rf.core.website.constants.TestConstants;
import com.rf.core.website.constants.dbQueries.DBQueries_RFO;
import com.rf.pages.website.CSCockpitHomePage;
import com.rf.pages.website.CSCockpitLoginPage;
import com.rf.pages.website.StoreFrontConsultantPage;
import com.rf.pages.website.StoreFrontHomePage;
import com.rf.pages.website.StoreFrontOrdersPage;
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
}