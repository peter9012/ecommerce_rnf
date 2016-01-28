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
import com.rf.pages.website.CSCockpitHomePage;
import com.rf.pages.website.CSCockpitLoginPage;
import com.rf.pages.website.StoreFrontConsultantPage;
import com.rf.pages.website.StoreFrontHomePage;
import com.rf.pages.website.StoreFrontOrdersPage;
import com.rf.pages.website.StoreFrontPCUserPage;
import com.rf.pages.website.StoreFrontRCUserPage;
import com.rf.pages.website.StoreFrontUpdateCartPage;
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
	private StoreFrontUpdateCartPage storeFrontUpdateCartPage;

	//-----------------------------------------------------------------------------------------------------------------

	private String RFO_DB = null;

	//Hybris Project-1934:To verify Consultant adhoc order
	@Test
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
	@Test
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
		driver.get(driver.getCSCockpitURL());		
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
	@Test
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
	@Test
	public void testVerifyCSCommissionAdminCanPlaceOrder_1810() throws InterruptedException{
		String randomCustomerSequenceNumber = null;
		String randomProductSequenceNumber = null;
		String SKUValue = null;

		//-------------------FOR US----------------------------------
		cscockpitLoginPage = new CSCockpitLoginPage(driver);
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
	@Test
	public void testVerifyCSSalesSuperVisoryCanPlaceOrder_1820() throws InterruptedException{
		String randomCustomerSequenceNumber = null;
		String randomProductSequenceNumber = null;
		String SKUValue = null;

		//-------------------FOR US----------------------------------
		cscockpitLoginPage = new CSCockpitLoginPage(driver);
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
	@Test
	public void testVerifyUserPermissionForTransactionStatus_1780(){
		String randomOrderSequenceNumber = null;
		cscockpitLoginPage = new CSCockpitLoginPage(driver);
		cscockpitLoginPage.enterUsername(TestConstants.CS_AGENT_USERNAME);
		cscockpitHomePage = cscockpitLoginPage.clickLoginBtn();
		cscockpitHomePage.clickOrderSearchTab();
		cscockpitHomePage.clickSearchBtn();
		randomOrderSequenceNumber = String.valueOf(cscockpitHomePage.getRandomOrdersFromOrderResultSearchFirstPageInOrderSearchTab());
		cscockpitHomePage.clickOrderNumberInOrderSearchResultsInOrderSearchTab(randomOrderSequenceNumber);
		s_assert.assertFalse(cscockpitHomePage.isPaymentTransactionStatusClickableOnOrderTab(), "Transaction Status on Payment Transactions Section is clickable for "+TestConstants.CS_AGENT_USERNAME);

		//		driver.get(driver.getCSCockpitURL());
		//		
		//		cscockpitLoginPage.enterUsername(TestConstants.ADMIN_USERNAME);
		//		cscockpitHomePage = cscockpitLoginPage.clickLoginBtn();
		//		cscockpitHomePage.clickOrderSearchTab();
		//		cscockpitHomePage.clickSearchBtn();
		//		randomOrderSequenceNumber = String.valueOf(cscockpitHomePage.getRandomOrdersFromOrderResultSearchFirstPageInOrderSearchTab());
		//		cscockpitHomePage.clickOrderNumberInOrderSearchResultsInOrderSearchTab(randomOrderSequenceNumber);
		//		s_assert.assertFalse(cscockpitHomePage.isPaymentTransactionStatusClickableOnOrderTab(), "Transaction Status on Payment Transactions Section is clickable for "+TestConstants.ADMIN_USERNAME);

		driver.get(driver.getCSCockpitURL());
		cscockpitLoginPage.enterUsername(TestConstants.CS_COMMISION_ADMIN_USERNAME);
		cscockpitHomePage = cscockpitLoginPage.clickLoginBtn();
		cscockpitHomePage.clickOrderSearchTab();
		cscockpitHomePage.clickSearchBtn();
		randomOrderSequenceNumber = String.valueOf(cscockpitHomePage.getRandomOrdersFromOrderResultSearchFirstPageInOrderSearchTab());
		cscockpitHomePage.clickOrderNumberInOrderSearchResultsInOrderSearchTab(randomOrderSequenceNumber);
		s_assert.assertFalse(cscockpitHomePage.isPaymentTransactionStatusClickableOnOrderTab(), "Transaction Status on Payment Transactions Section is clickable for "+TestConstants.CS_COMMISION_ADMIN_USERNAME);

		driver.get(driver.getCSCockpitURL());
		cscockpitLoginPage.enterUsername(TestConstants.CS_SALES_SUPERVISORY_USERNAME);
		cscockpitHomePage = cscockpitLoginPage.clickLoginBtn();
		cscockpitHomePage.clickOrderSearchTab();
		cscockpitHomePage.clickSearchBtn();
		randomOrderSequenceNumber = String.valueOf(cscockpitHomePage.getRandomOrdersFromOrderResultSearchFirstPageInOrderSearchTab());
		cscockpitHomePage.clickOrderNumberInOrderSearchResultsInOrderSearchTab(randomOrderSequenceNumber);
		s_assert.assertTrue(cscockpitHomePage.isPaymentTransactionStatusClickableOnOrderTab(), "Transaction Status on Payment Transactions Section is NOT clickable for "+TestConstants.CS_SALES_SUPERVISORY_USERNAME);

		s_assert.assertAll();
	}

	//Hybris Project-1924:Verify the ability to mark Test Order functionality for all users
	@Test(enabled=false)//WIP
	public void testVerifyTheAbilityToMarkTestOrderFunctionalityForAllUsers_1924(){
		String randomCustomerSequenceNumber = null;
		String randomProductSequenceNumber = null;
		String consultantEmailID = null;
		String SKUValue = null;
		cscockpitLoginPage = new CSCockpitLoginPage(driver);
		//-------------------FOR US----------------------------------
		cscockpitHomePage = cscockpitLoginPage.clickLoginBtn();
		cscockpitHomePage.selectCustomerTypeFromDropDownInCustomerSearchTab("CONSULTANT");
		cscockpitHomePage.selectCountryFromDropDownInCustomerSearchTab("United States");
		cscockpitHomePage.selectAccountStatusFromDropDownInCustomerSearchTab("Active");
		cscockpitHomePage.clickSearchBtn();
		randomCustomerSequenceNumber = String.valueOf(cscockpitHomePage.getRandomCustomerFromSearchResult());
		consultantEmailID = cscockpitHomePage.getEmailIdOfTheCustomerInCustomerSearchTab(randomCustomerSequenceNumber);
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
		cscockpitHomePage.clickTestOrderCheckBoxInCheckoutTab();
		cscockpitHomePage.clickPlaceOrderButtonInCheckoutTab();
		s_assert.assertTrue(cscockpitHomePage.verifyTestOrderCheckBoxIsSelectedInOrderTab(), "Test order checkbox is not selected after place an order");
		s_assert.assertTrue(cscockpitHomePage.getOrderStatusAfterPlaceOrderInOrderTab().contains("SUBMITTED"),"CSCockpit checkout tab order status expected = SUBMITTED and on UI = " +cscockpitHomePage.getOrderStatusAfterPlaceOrderInOrderTab());

		//-------------------FOR CA----------------------------------
		driver.get(driver.getCSCockpitURL());
		cscockpitHomePage = cscockpitLoginPage.clickLoginBtn();
		cscockpitHomePage.selectCustomerTypeFromDropDownInCustomerSearchTab("CONSULTANT");
		cscockpitHomePage.selectCountryFromDropDownInCustomerSearchTab("Canada");
		cscockpitHomePage.selectAccountStatusFromDropDownInCustomerSearchTab("Active");
		cscockpitHomePage.clickSearchBtn();
		randomCustomerSequenceNumber = String.valueOf(cscockpitHomePage.getRandomCustomerFromSearchResult());
		consultantEmailID = cscockpitHomePage.getEmailIdOfTheCustomerInCustomerSearchTab(randomCustomerSequenceNumber);
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
		cscockpitHomePage.clickTestOrderCheckBoxInCheckoutTab();
		cscockpitHomePage.clickPlaceOrderButtonInCheckoutTab();
		s_assert.assertTrue(cscockpitHomePage.verifyTestOrderCheckBoxIsSelectedInOrderTab(), "Test order checkbox is not selected after place an order");
		s_assert.assertTrue(cscockpitHomePage.getOrderStatusAfterPlaceOrderInOrderTab().contains("SUBMITTED"),"CSCockpit checkout tab order status expected = SUBMITTED and on UI = " +cscockpitHomePage.getOrderStatusAfterPlaceOrderInOrderTab());
		s_assert.assertAll();
	}

	//Hybris Project-1925:Verify the ability to mark donotship Order functionality for all users
	@Test(enabled=false)//WIP
	public void testToVerifyTheAbilityToMarkDoNotShipOrderFunctionality_1925(){
		String randomCustomerSequenceNumber = null;
		String randomProductSequenceNumber = null;
		String consultantEmailID = null;
		String SKUValue = null;
		cscockpitLoginPage = new CSCockpitLoginPage(driver);
		//-------------------FOR US----------------------------------
		cscockpitHomePage = cscockpitLoginPage.clickLoginBtn();
		cscockpitHomePage.selectCustomerTypeFromDropDownInCustomerSearchTab("CONSULTANT");
		cscockpitHomePage.selectCountryFromDropDownInCustomerSearchTab("United States");
		cscockpitHomePage.selectAccountStatusFromDropDownInCustomerSearchTab("Active");
		cscockpitHomePage.clickSearchBtn();
		randomCustomerSequenceNumber = String.valueOf(cscockpitHomePage.getRandomCustomerFromSearchResult());
		consultantEmailID = cscockpitHomePage.getEmailIdOfTheCustomerInCustomerSearchTab(randomCustomerSequenceNumber);
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
		cscockpitHomePage.clickDoNotShipCheckBoxInCheckoutTab();
		cscockpitHomePage.clickPlaceOrderButtonInCheckoutTab();
		s_assert.assertTrue(cscockpitHomePage.verifyDoNotShipCheckBoxIsSelectedInOrderTab(), "Do not ship checkbox is not selected after place an order");
		s_assert.assertTrue(cscockpitHomePage.getOrderStatusAfterPlaceOrderInOrderTab().contains("COMPLETED"),"CSCockpit checkout tab order status expected = COMPLETED and on UI = " +cscockpitHomePage.getOrderStatusAfterPlaceOrderInOrderTab());


		//-------------------FOR CA----------------------------------
		driver.get(driver.getCSCockpitURL());
		cscockpitHomePage = cscockpitLoginPage.clickLoginBtn();
		cscockpitHomePage.selectCustomerTypeFromDropDownInCustomerSearchTab("CONSULTANT");
		cscockpitHomePage.selectCountryFromDropDownInCustomerSearchTab("Canada");
		cscockpitHomePage.selectAccountStatusFromDropDownInCustomerSearchTab("Active");
		cscockpitHomePage.clickSearchBtn();
		randomCustomerSequenceNumber = String.valueOf(cscockpitHomePage.getRandomCustomerFromSearchResult());
		consultantEmailID = cscockpitHomePage.getEmailIdOfTheCustomerInCustomerSearchTab(randomCustomerSequenceNumber);
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
		cscockpitHomePage.clickDoNotShipCheckBoxInCheckoutTab();
		cscockpitHomePage.clickPlaceOrderButtonInCheckoutTab();
		s_assert.assertTrue(cscockpitHomePage.verifyDoNotShipCheckBoxIsSelectedInOrderTab(), "Do not ship checkbox is not selected after place an order");
		s_assert.assertTrue(cscockpitHomePage.getOrderStatusAfterPlaceOrderInOrderTab().contains("COMPLETED"),"CSCockpit checkout tab order status expected = COMPLETED and on UI = " +cscockpitHomePage.getOrderStatusAfterPlaceOrderInOrderTab());
		s_assert.assertAll();
	}

	//Hybris Project-1926:Verify the ability to mark Test Order and donot ship functionality for all users
	@Test(enabled=false)//WIP
	public void testVerifyTheAbilityToMarkTestOrderAndDoNotShipFunctionality_1926(){
		String randomCustomerSequenceNumber = null;
		String randomProductSequenceNumber = null;
		String consultantEmailID = null;
		String SKUValue = null;
		cscockpitLoginPage = new CSCockpitLoginPage(driver);
		//-------------------FOR US----------------------------------
		cscockpitHomePage = cscockpitLoginPage.clickLoginBtn();
		cscockpitHomePage.selectCustomerTypeFromDropDownInCustomerSearchTab("CONSULTANT");
		cscockpitHomePage.selectCountryFromDropDownInCustomerSearchTab("United States");
		cscockpitHomePage.selectAccountStatusFromDropDownInCustomerSearchTab("Active");
		cscockpitHomePage.clickSearchBtn();
		randomCustomerSequenceNumber = String.valueOf(cscockpitHomePage.getRandomCustomerFromSearchResult());
		consultantEmailID = cscockpitHomePage.getEmailIdOfTheCustomerInCustomerSearchTab(randomCustomerSequenceNumber);
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
		cscockpitHomePage.clickTestOrderCheckBoxInCheckoutTab();
		cscockpitHomePage.clickDoNotShipCheckBoxInCheckoutTab();
		cscockpitHomePage.clickPlaceOrderButtonInCheckoutTab();
		s_assert.assertTrue(cscockpitHomePage.verifyTestOrderCheckBoxIsSelectedInOrderTab(), "Test order checkbox is not selected after place an order");
		s_assert.assertTrue(cscockpitHomePage.verifyDoNotShipCheckBoxIsSelectedInOrderTab(), "Do not ship checkbox is not selected after place an order");
		s_assert.assertTrue(cscockpitHomePage.getOrderStatusAfterPlaceOrderInOrderTab().contains("COMPLETED"),"CSCockpit checkout tab order status expected = COMPLETED and on UI = " +cscockpitHomePage.getOrderStatusAfterPlaceOrderInOrderTab());

		//-------------------FOR CA----------------------------------
		driver.get(driver.getCSCockpitURL());
		cscockpitHomePage = cscockpitLoginPage.clickLoginBtn();
		cscockpitHomePage.selectCustomerTypeFromDropDownInCustomerSearchTab("CONSULTANT");
		cscockpitHomePage.selectCountryFromDropDownInCustomerSearchTab("Canada");
		cscockpitHomePage.selectAccountStatusFromDropDownInCustomerSearchTab("Active");
		cscockpitHomePage.clickSearchBtn();
		randomCustomerSequenceNumber = String.valueOf(cscockpitHomePage.getRandomCustomerFromSearchResult());
		consultantEmailID = cscockpitHomePage.getEmailIdOfTheCustomerInCustomerSearchTab(randomCustomerSequenceNumber);
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
		cscockpitHomePage.clickTestOrderCheckBoxInCheckoutTab();
		cscockpitHomePage.clickDoNotShipCheckBoxInCheckoutTab();
		cscockpitHomePage.clickPlaceOrderButtonInCheckoutTab();
		s_assert.assertTrue(cscockpitHomePage.verifyTestOrderCheckBoxIsSelectedInOrderTab(), "Test order checkbox is not selected after place an order");
		s_assert.assertTrue(cscockpitHomePage.verifyDoNotShipCheckBoxIsSelectedInOrderTab(), "Do not ship checkbox is not selected after place an order");
		s_assert.assertTrue(cscockpitHomePage.getOrderStatusAfterPlaceOrderInOrderTab().contains("COMPLETED"),"CSCockpit checkout tab order status expected = COMPLETED and on UI = " +cscockpitHomePage.getOrderStatusAfterPlaceOrderInOrderTab());
		s_assert.assertAll();
	}

	//Hybris Project-1948:To Verify the Display Order Origination In the Order Detail Page
	@Test(enabled=false)//WIP
	public void testVerifyTheDisplayOrderOrigination_1948() throws InterruptedException{
		// CREATE ADHOC ORDER FROM CORP
		int randomNum = CommonUtils.getRandomNum(10000, 1000000);
		RFO_DB = driver.getDBNameRFO();
		List<Map<String, Object>> randomCustomerList =  null;
		String customerEmailID = null;
		String newBillingProfileName = TestConstants.NEW_BILLING_PROFILE_NAME_US+randomNum;
		String lastName = "lN";
		String accountId = null;
		String randomCustomerSequenceNumber = null;
		String randomProductSequenceNumber = null;
		String SKUValue = null;
		RFO_DB = driver.getDBNameRFO();
		storeFrontHomePage = new StoreFrontHomePage(driver);
		storeFrontUpdateCartPage = new StoreFrontUpdateCartPage(driver);
		driver.get(driver.getStoreFrontURL()+"/us");

		while(true){
			randomCustomerList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_RC_RFO,"236"),RFO_DB);
			customerEmailID = (String) getValueFromQueryResult(randomCustomerList, "UserName");		
			accountId = String.valueOf(getValueFromQueryResult(randomCustomerList, "AccountID"));
			logger.info("Account Id of the user is "+accountId);

			storeFrontConsultantPage = storeFrontHomePage.loginAsConsultant(customerEmailID, password);
			boolean isLoginError = driver.getCurrentUrl().contains("error");
			if(isLoginError){
				logger.info("Login Error for the user "+customerEmailID);
				driver.get(driver.getStoreFrontURL()+"/us");
			}
			else
				break;
		}
		logger.info("login is successful");
		storeFrontConsultantPage.hoverOnShopLinkAndClickAllProductsLinksAfterLogin();
		storeFrontUpdateCartPage.clickOnBuyNowButton("us");
		storeFrontUpdateCartPage.clickOnCheckoutButton();
		storeFrontUpdateCartPage.clickOnContinueWithoutSponsorLink();
		storeFrontUpdateCartPage.clickOnNextButtonAfterSelectingSponsor();
		storeFrontUpdateCartPage.clickOnShippingAddressNextStepBtn();
		storeFrontUpdateCartPage.clickOnDefaultBillingProfileEdit();
		storeFrontUpdateCartPage.enterNewBillingNameOnCard(newBillingProfileName+" "+lastName);
		storeFrontUpdateCartPage.enterNewBillingCardNumber(TestConstants.CARD_NUMBER);
		storeFrontUpdateCartPage.selectNewBillingCardExpirationDate();
		storeFrontUpdateCartPage.enterNewBillingSecurityCode(TestConstants.SECURITY_CODE);
		storeFrontUpdateCartPage.selectNewBillingCardAddress();
		storeFrontUpdateCartPage.clickOnSaveBillingProfile();

		storeFrontUpdateCartPage.clickOnBillingNextStepBtn();
		storeFrontUpdateCartPage.clickPlaceOrderBtn();
		String orderNumberCreatedFromCorp = storeFrontUpdateCartPage.getOrderNumberAfterPlaceOrder();
		logger.info("Order Number created from Corp site is "+orderNumberCreatedFromCorp);
		s_assert.assertTrue(storeFrontUpdateCartPage.verifyOrderPlacedConfirmationMessage(), "Order has been not placed successfully from Corp site");
		logout();
		// CREATE ADHOC ORDER FROM BIZ PWS
		driver.get(driver.getStoreFrontURL()+"/us");

		while(true){
			randomCustomerList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguementPWS(DBQueries_RFO.GET_RANDOM_ACTIVE_CONSULTANT_WITH_PWS_RFO,driver.getEnvironment()+".biz","us","236"),RFO_DB);
			customerEmailID = (String) getValueFromQueryResult(randomCustomerList, "UserName");		
			accountId = String.valueOf(getValueFromQueryResult(randomCustomerList, "AccountID"));
			logger.info("Account Id of the user is "+accountId);

			storeFrontConsultantPage = storeFrontHomePage.loginAsConsultant(customerEmailID, password);
			boolean isLoginError = driver.getCurrentUrl().contains("error");
			if(isLoginError){
				logger.info("Login Error for the user "+customerEmailID);
				driver.get(driver.getStoreFrontURL()+"/us");
			}
			else
				break;
		}

		driver.get(storeFrontHomePage.convertComSiteToBizSite(driver.getCurrentUrl()));
		try{
			storeFrontConsultantPage = storeFrontHomePage.loginAsConsultant(customerEmailID, password);
		}catch(NoSuchElementException e){

		}
		logger.info("login is successful");
		storeFrontConsultantPage.hoverOnShopLinkAndClickAllProductsLinksAfterLogin();
		storeFrontUpdateCartPage.clickOnBuyNowButton("us");
		storeFrontUpdateCartPage.clickOnCheckoutButton();
		storeFrontUpdateCartPage.clickOnShippingAddressNextStepBtn();
		storeFrontUpdateCartPage.clickOnDefaultBillingProfileEdit();
		storeFrontUpdateCartPage.enterNewBillingNameOnCard(newBillingProfileName+" "+lastName);
		storeFrontUpdateCartPage.enterNewBillingCardNumber(TestConstants.CARD_NUMBER);
		storeFrontUpdateCartPage.selectNewBillingCardExpirationDate();
		storeFrontUpdateCartPage.enterNewBillingSecurityCode(TestConstants.SECURITY_CODE);
		storeFrontUpdateCartPage.selectNewBillingCardAddress();
		storeFrontUpdateCartPage.clickOnSaveBillingProfile();

		storeFrontUpdateCartPage.clickOnBillingNextStepBtn();
		storeFrontUpdateCartPage.clickPlaceOrderBtn();
		String orderNumberCreatedFromBIZPWS = storeFrontUpdateCartPage.getOrderNumberAfterPlaceOrder();
		logger.info("Order Number created from BIZ PWS site is "+orderNumberCreatedFromBIZPWS);
		s_assert.assertTrue(storeFrontUpdateCartPage.verifyOrderPlacedConfirmationMessage(), "Order has been not placed successfully from BIZ PWS");
		logout();
		// CREATE ADHOC ORDER FROM COM PWS
		driver.get(driver.getStoreFrontURL()+"/us");

		while(true){
			randomCustomerList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguementPWS(DBQueries_RFO.GET_RANDOM_ACTIVE_CONSULTANT_WITH_PWS_RFO,driver.getEnvironment()+".biz","us","236"),RFO_DB);
			customerEmailID = (String) getValueFromQueryResult(randomCustomerList, "UserName");		
			accountId = String.valueOf(getValueFromQueryResult(randomCustomerList, "AccountID"));
			logger.info("Account Id of the user is "+accountId);

			storeFrontConsultantPage = storeFrontHomePage.loginAsConsultant(customerEmailID, password);
			boolean isLoginError = driver.getCurrentUrl().contains("error");
			if(isLoginError){
				logger.info("Login Error for the user "+customerEmailID);
				driver.get(driver.getStoreFrontURL()+"/us");
			}
			else
				break;
		}
		driver.get(storeFrontHomePage.convertBizSiteToComSite(driver.getCurrentUrl()));
		try{
			storeFrontConsultantPage = storeFrontHomePage.loginAsConsultant(customerEmailID, password);
		}catch(NoSuchElementException e){

		}
		logger.info("login is successful");
		storeFrontConsultantPage.hoverOnShopLinkAndClickAllProductsLinksAfterLogin();
		storeFrontUpdateCartPage.clickOnBuyNowButton("us");
		storeFrontUpdateCartPage.clickOnCheckoutButton();
		storeFrontUpdateCartPage.clickOnShippingAddressNextStepBtn();
		storeFrontUpdateCartPage.clickOnDefaultBillingProfileEdit();
		storeFrontUpdateCartPage.enterNewBillingNameOnCard(newBillingProfileName+" "+lastName);
		storeFrontUpdateCartPage.enterNewBillingCardNumber(TestConstants.CARD_NUMBER);
		storeFrontUpdateCartPage.selectNewBillingCardExpirationDate();
		storeFrontUpdateCartPage.enterNewBillingSecurityCode(TestConstants.SECURITY_CODE);
		storeFrontUpdateCartPage.selectNewBillingCardAddress();
		storeFrontUpdateCartPage.clickOnSaveBillingProfile();

		storeFrontUpdateCartPage.clickOnBillingNextStepBtn();
		storeFrontUpdateCartPage.clickPlaceOrderBtn();
		String orderNumberCreatedFromCOMPWS = storeFrontUpdateCartPage.getOrderNumberAfterPlaceOrder();
		logger.info("Order Number created from COM PWS site is "+orderNumberCreatedFromCOMPWS);
		s_assert.assertTrue(storeFrontUpdateCartPage.verifyOrderPlacedConfirmationMessage(), "Order has been not placed successfully from COM PWS site");

		//open cscockpit
		cscockpitLoginPage = new CSCockpitLoginPage(driver);
		driver.get(driver.getCSCockpitURL());		
		cscockpitHomePage = cscockpitLoginPage.clickLoginBtn();
		cscockpitHomePage.enterEmailIdInSearchFieldInCustomerSearchTab(customerEmailID);
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
		String orderNumberCreatedFromCSCockpit = cscockpitHomePage.getOrderNumberInOrderTab();
		logger.info("Order Number created from CSCockpit is "+orderNumberCreatedFromCSCockpit);
		//Search Order created from Corp Site
		cscockpitHomePage.clickChangeOrderLinkOnLeftNavigation();		
		cscockpitHomePage.enterOrderNumberInOrderSearchTab(orderNumberCreatedFromCorp);
		cscockpitHomePage.clickSearchBtn();
		cscockpitHomePage.clickOrderLinkOnOrderSearchTabAndVerifyOrderDetailsPage(orderNumberCreatedFromCorp);
		s_assert.assertTrue(cscockpitHomePage.getOriginationValuePresentAsPerSearchedOrder().contains(".rodanandfields.com"),"Origination Value for Corp Order expected is to contain '.rodanandfields.com'"+ "but on actual UI,it is ="+ cscockpitHomePage.getOriginationValuePresentAsPerSearchedOrder());

		//Search Order created from BIZ PWS site
		cscockpitHomePage.clickChangeOrderLinkOnLeftNavigation();		
		cscockpitHomePage.enterOrderNumberInOrderSearchTab(orderNumberCreatedFromBIZPWS);
		cscockpitHomePage.clickSearchBtn();
		cscockpitHomePage.clickOrderLinkOnOrderSearchTabAndVerifyOrderDetailsPage(orderNumberCreatedFromBIZPWS);
		s_assert.assertTrue(cscockpitHomePage.getOriginationValuePresentAsPerSearchedOrder().contains(".myrfo"+driver.getEnvironment()+".biz"),"Origination Value for Corp Order expected is to contain '.myrfo"+driver.getEnvironment()+"biz'"+ "but on actual UI,it is ="+ cscockpitHomePage.getOriginationValuePresentAsPerSearchedOrder());

		//Search Order created from COM PWS site
		cscockpitHomePage.clickChangeOrderLinkOnLeftNavigation();		
		cscockpitHomePage.enterOrderNumberInOrderSearchTab(orderNumberCreatedFromCOMPWS);
		cscockpitHomePage.clickSearchBtn();
		cscockpitHomePage.clickOrderLinkOnOrderSearchTabAndVerifyOrderDetailsPage(orderNumberCreatedFromCOMPWS);
		s_assert.assertTrue(cscockpitHomePage.getOriginationValuePresentAsPerSearchedOrder().contains(".myrfo"+driver.getEnvironment()+".com"),"Origination Value for Corp Order expected is to contain '.myrfo"+driver.getEnvironment()+"com'"+ "but on actual UI,it is ="+ cscockpitHomePage.getOriginationValuePresentAsPerSearchedOrder());

		//Search Order created from CSCockpit
		cscockpitHomePage.clickChangeOrderLinkOnLeftNavigation();		
		cscockpitHomePage.enterOrderNumberInOrderSearchTab(orderNumberCreatedFromCSCockpit);
		cscockpitHomePage.clickSearchBtn();
		cscockpitHomePage.clickOrderLinkOnOrderSearchTabAndVerifyOrderDetailsPage(orderNumberCreatedFromCSCockpit);
		s_assert.assertTrue(cscockpitHomePage.getOriginationValuePresentAsPerSearchedOrder().contains("cscockpit"),"Origination Value for Corp Order expected is to contain 'cscockpit'"+ "but on actual UI,it is ="+ cscockpitHomePage.getOriginationValuePresentAsPerSearchedOrder());

		s_assert.assertAll();
	}

	// Hybris Project-1733:To verify create payment address functionality in the Checkout Page
	@Test(enabled=false)//WIP
	public void testVerifyCreatePaymentAddressOnCheckoutPage_1733() throws InterruptedException{
		RFO_DB = driver.getDBNameRFO();
		String randomCustomerSequenceNumber = null;
		String randomProductSequenceNumber = null;
		String consultantEmailID = null;
		String SKUValue = null;
		int randomNum = CommonUtils.getRandomNum(10000, 1000000);
		int randomNumber=CommonUtils.getRandomNum(10000, 1000000);
		String attendeeLastName="IN";
		String orderNumber=null;
		String orderNumberOfOrderTab=null;
		String orderHistoryNumber=null;
		String newBillingProfileName = TestConstants.NEW_BILLING_PROFILE_NAME_US+randomNum;
		String securityCode=TestConstants.SECURITY_CODE;
		String cardNumber=TestConstants.CARD_NUMBER;
		String attendeeFirstName=TestConstants.FIRST_NAME+randomNum;
		String addressLine=TestConstants.NEW_ADDRESS_LINE1_US;
		String city=TestConstants.NEW_ADDRESS_CITY_US;
		String postalCode=TestConstants.NEW_ADDRESS_POSTAL_CODE_US;
		String Country=TestConstants.COUNTRY_DD_VALUE_US;
		String Province=TestConstants.PROVINCE_ALABAMA_US;
		String phoneNumber=TestConstants.NEW_ADDRESS_PHONE_NUMBER_US;

		//-------------------FOR US----------------------------------
		driver.get(driver.getStoreFrontURL()+"/us");
		List<Map<String, Object>> randomConsultantList =  null;
		storeFrontHomePage = new StoreFrontHomePage(driver);
		while(true){
			randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_CONSULTANT_WITH_ORDERS_AND_AUTOSHIPS_RFO,"236"),RFO_DB);
			consultantEmailID = (String) getValueFromQueryResult(randomConsultantList, "UserName");  
			String accountId= String.valueOf(getValueFromQueryResult(randomConsultantList, "AccountID"));
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

		cscockpitHomePage = cscockpitLoginPage.clickLoginBtn();
		cscockpitHomePage.selectCustomerTypeFromDropDownInCustomerSearchTab("CONSULTANT");
		cscockpitHomePage.selectCountryFromDropDownInCustomerSearchTab("United States");
		cscockpitHomePage.selectAccountStatusFromDropDownInCustomerSearchTab("Active");
		cscockpitHomePage.enterEmailIdInSearchFieldInCustomerSearchTab(consultantEmailID);
		cscockpitHomePage.clickSearchBtn();
		randomCustomerSequenceNumber = String.valueOf(cscockpitHomePage.getRandomCustomerFromSearchResult());
		cscockpitHomePage.clickCIDNumberInCustomerSearchTab(randomCustomerSequenceNumber);
		orderNumber=cscockpitHomePage.clickAndGetOrderNumberInCustomerTab();
		logger.info("clicked order number 1 is "+orderNumber);
		cscockpitHomePage.clickPlaceOrderButtonInCustomerTab();
		cscockpitHomePage.selectValueFromSortByDDInCartTab("Price: High to Low");
		cscockpitHomePage.selectCatalogFromDropDownInCartTab();
		randomProductSequenceNumber = String.valueOf(cscockpitHomePage.getRandomProductWithSKUFromSearchResult()); 
		SKUValue = cscockpitHomePage.getCustomerSKUValueInCartTab(randomProductSequenceNumber);
		cscockpitHomePage.searchSKUValueInCartTab(SKUValue);
		cscockpitHomePage.clickAddToCartBtnInCartTab();
		cscockpitHomePage.clickCheckoutBtnInCartTab();
		cscockpitHomePage.clickAddNewPaymentAddressInCheckoutTab();
		cscockpitHomePage.clickAddNewAddressLinkInPopUpInCheckoutTab();
		s_assert.assertTrue(cscockpitHomePage.verifyAddressTextBoxInPopUpInCheckoutTab(),"Address Line 1 text box is not present in popup at checkout tab");
		s_assert.assertTrue(cscockpitHomePage.verifyPostalCodeTextBoxInPopUpInCheckoutTab(),"Postal code text box is not present in popup at checkout tab");
		cscockpitHomePage.clickCloseOfPaymentAddressPopUpInCheckoutTab();
		cscockpitHomePage.clickAddNewPaymentAddressInCheckoutTab();
		// cscockpitHomePage.clickAddNewAddressLinkInPopUpInCheckoutTab();
		cscockpitHomePage.clickSaveOfShippingAddressPopUpInCheckoutTab();
		s_assert.assertTrue(cscockpitHomePage.getErrorMessageOfPopupWithoutFillingDataInCheckoutTab().contains("Please review credit card information entered"),"CSCockpit checkout tab popup error message expected = Please review credit card information entered and on UI = " +cscockpitHomePage.getErrorMessageOfPopupWithoutFillingDataInCheckoutTab());
		cscockpitHomePage.enterPaymentDetailsInPopUpInCheckoutTab(cardNumber, newBillingProfileName,securityCode,"09","2023","VISA");
		cscockpitHomePage.enterShippingDetailsInPopUpInCheckoutTab(attendeeFirstName,attendeeLastName,addressLine,city,postalCode,Country,Province,phoneNumber);
		cscockpitHomePage.clickSaveOfShippingAddressPopUpInCheckoutTab();
		s_assert.assertTrue(cscockpitHomePage.getNewBillingAddressNameInCheckoutTab().contains(attendeeFirstName),"CSCockpit checkout tab newly created billing address name expected ="+ attendeeFirstName+ "and on UI = " +cscockpitHomePage.getNewBillingAddressNameInCheckoutTab());
		cscockpitHomePage.enterCVVValueInCheckoutTab(TestConstants.SECURITY_CODE);
		cscockpitHomePage.clickUseThisCardBtnInCheckoutTab();
		cscockpitHomePage.clickPlaceOrderButtonInCheckoutTab();
		orderNumberOfOrderTab = cscockpitHomePage.getOrderNumberInOrderTab();
		logger.info("order number 2 in order tab is "+orderNumberOfOrderTab);
		cscockpitHomePage.clickCustomerTab();
		s_assert.assertTrue(cscockpitHomePage.getOrderTypeInCustomerTab(orderNumberOfOrderTab.split("\\-")[0].trim()).contains("Consultant Order"),"CSCockpit Customer tab Order type expected = Consultant Order and on UI = " +cscockpitHomePage.getOrderTypeInCustomerTab(orderNumberOfOrderTab.split("\\-")[0].trim()));
		driver.get(driver.getStoreFrontURL()+"/us");
		storeFrontConsultantPage = storeFrontHomePage.loginAsConsultant(consultantEmailID, password);
		storeFrontConsultantPage.clickOnWelcomeDropDown();
		storeFrontOrdersPage =  storeFrontConsultantPage.clickOrdersLinkPresentOnWelcomeDropDown();
		s_assert.assertTrue(storeFrontOrdersPage.verifyOrdersPageIsDisplayed(),"Orders page has not been displayed");
		orderHistoryNumber = storeFrontOrdersPage.getFirstOrderNumberFromOrderHistory();
		logger.info("order number 3 from order history is "+orderHistoryNumber);
		s_assert.assertTrue(orderHistoryNumber.contains(orderNumberOfOrderTab.split("\\-")[0].trim()),"CSCockpit Order number expected = "+orderNumberOfOrderTab.split("\\-")[0].trim()+" and on UI = " +orderHistoryNumber);

		//-------------------FOR CA----------------------------------
		driver.get(driver.getStoreFrontURL()+"/ca");

		newBillingProfileName = TestConstants.NEW_BILLING_PROFILE_NAME_US+randomNumber;
		securityCode=TestConstants.SECURITY_CODE;
		cardNumber=TestConstants.CARD_NUMBER;
		attendeeFirstName=TestConstants.FIRST_NAME+randomNumber;
		addressLine=TestConstants.ADDRESS_LINE_1_CA;
		city=TestConstants.CITY_CA;
		postalCode=TestConstants.POSTAL_CODE_CA;
		Country=TestConstants.COUNTRY_DD_VALUE_CA;
		Province=TestConstants.PROVINCE_CA;
		phoneNumber=TestConstants.PHONE_NUMBER_CA;

		while(true){
			randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_CONSULTANT_WITH_ORDERS_AND_AUTOSHIPS_RFO,"40"),RFO_DB);
			consultantEmailID = (String) getValueFromQueryResult(randomConsultantList, "UserName");  
			String accountId= String.valueOf(getValueFromQueryResult(randomConsultantList, "AccountID"));
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
		logger.info("login is successful");
		cscockpitLoginPage = new CSCockpitLoginPage(driver);
		driver.get(driver.getCSCockpitURL());

		cscockpitHomePage = cscockpitLoginPage.clickLoginBtn();
		cscockpitHomePage.selectCustomerTypeFromDropDownInCustomerSearchTab("CONSULTANT");
		cscockpitHomePage.selectCountryFromDropDownInCustomerSearchTab("Canada");
		cscockpitHomePage.selectAccountStatusFromDropDownInCustomerSearchTab("Active");
		cscockpitHomePage.enterEmailIdInSearchFieldInCustomerSearchTab(consultantEmailID);
		cscockpitHomePage.clickSearchBtn();
		randomCustomerSequenceNumber = String.valueOf(cscockpitHomePage.getRandomCustomerFromSearchResult());
		cscockpitHomePage.clickCIDNumberInCustomerSearchTab(randomCustomerSequenceNumber);
		orderNumber=cscockpitHomePage.clickAndGetOrderNumberInCustomerTab();
		cscockpitHomePage.clickPlaceOrderButtonInCustomerTab();
		cscockpitHomePage.selectValueFromSortByDDInCartTab("Price: High to Low");
		cscockpitHomePage.selectCatalogFromDropDownInCartTab();
		randomProductSequenceNumber = String.valueOf(cscockpitHomePage.getRandomProductWithSKUFromSearchResult()); 
		SKUValue = cscockpitHomePage.getCustomerSKUValueInCartTab(randomProductSequenceNumber);
		cscockpitHomePage.searchSKUValueInCartTab(SKUValue);
		cscockpitHomePage.clickAddToCartBtnInCartTab();
		cscockpitHomePage.clickCheckoutBtnInCartTab();
		cscockpitHomePage.clickAddNewPaymentAddressInCheckoutTab();
		cscockpitHomePage.clickAddNewAddressLinkInPopUpInCheckoutTab();
		s_assert.assertTrue(cscockpitHomePage.verifyAddressTextBoxInPopUpInCheckoutTab(),"Address Line 1 text box is not present in popup at checkout tab");
		s_assert.assertTrue(cscockpitHomePage.verifyPostalCodeTextBoxInPopUpInCheckoutTab(),"Postal code text box is not present in popup at checkout tab");
		cscockpitHomePage.clickCloseOfPaymentAddressPopUpInCheckoutTab();
		cscockpitHomePage.clickAddNewPaymentAddressInCheckoutTab();
		// cscockpitHomePage.clickAddNewAddressLinkInPopUpInCheckoutTab();
		cscockpitHomePage.clickSaveOfShippingAddressPopUpInCheckoutTab();
		s_assert.assertTrue(cscockpitHomePage.getErrorMessageOfPopupWithoutFillingDataInCheckoutTab().contains("Please review credit card information entered"),"CSCockpit checkout tab popup error message expected = Please review credit card information entered and on UI = " +cscockpitHomePage.getErrorMessageOfPopupWithoutFillingDataInCheckoutTab());
		cscockpitHomePage.enterPaymentDetailsInPopUpInCheckoutTab(cardNumber, newBillingProfileName,securityCode,"09","2023","VISA");
		cscockpitHomePage.enterShippingDetailsInPopUpInCheckoutTab(attendeeFirstName,attendeeLastName,addressLine,city,postalCode,Country,Province,phoneNumber);
		cscockpitHomePage.clickSaveOfShippingAddressPopUpInCheckoutTab();
		s_assert.assertTrue(cscockpitHomePage.getNewBillingAddressNameInCheckoutTab().contains(attendeeFirstName),"CSCockpit checkout tab newly created billing address name expected ="+ attendeeFirstName+ "and on UI = " +cscockpitHomePage.getNewBillingAddressNameInCheckoutTab());
		cscockpitHomePage.enterCVVValueInCheckoutTab(TestConstants.SECURITY_CODE);
		cscockpitHomePage.clickUseThisCardBtnInCheckoutTab();
		cscockpitHomePage.clickPlaceOrderButtonInCheckoutTab();
		orderNumberOfOrderTab = cscockpitHomePage.getOrderNumberInOrderTab();
		cscockpitHomePage.clickCustomerTab();
		s_assert.assertTrue(cscockpitHomePage.getOrderTypeInCustomerTab(orderNumberOfOrderTab.split("\\-")[0].trim()).contains("Consultant Order"),"CSCockpit Customer tab Order type expected = Consultant Order and on UI = " +cscockpitHomePage.getOrderTypeInCustomerTab(orderNumberOfOrderTab.split("\\-")[0].trim()));
		driver.get(driver.getStoreFrontURL()+"/us");
		storeFrontConsultantPage = storeFrontHomePage.loginAsConsultant(consultantEmailID, password);
		storeFrontConsultantPage.clickOnWelcomeDropDown();
		storeFrontOrdersPage =  storeFrontConsultantPage.clickOrdersLinkPresentOnWelcomeDropDown();
		s_assert.assertTrue(storeFrontOrdersPage.verifyOrdersPageIsDisplayed(),"Orders page has not been displayed");
		orderHistoryNumber = storeFrontOrdersPage.getFirstOrderNumberFromOrderHistory();
		s_assert.assertTrue(orderHistoryNumber.contains(orderNumberOfOrderTab.split("\\-")[0].trim()),"CSCockpit Order number expected = "+orderNumberOfOrderTab.split("\\-")[0].trim()+" and on UI = " +orderHistoryNumber);
		s_assert.assertAll();
	}

	//  Hybris Project-1734:To verify create delivery address functionality in the Checkout Page
	@Test
	public void testVerifyCreateDeliveryAddressOnCheckoutPage_1734() throws InterruptedException{
		RFO_DB = driver.getDBNameRFO();
		String randomCustomerSequenceNumber = null;
		String randomProductSequenceNumber = null;
		String consultantEmailID = null;
		String SKUValue = null;
		int randomNum = CommonUtils.getRandomNum(10000, 1000000);
		int randomNumber=CommonUtils.getRandomNum(10000, 1000000);
		String attendeeLastName="IN";
		String orderNumber=null;
		String orderNumberOfOrderTab=null;
		String orderHistoryNumber=null;
		String newBillingProfileName = TestConstants.NEW_BILLING_PROFILE_NAME_US+randomNum;
		String securityCode=TestConstants.SECURITY_CODE;
		String cardNumber=TestConstants.CARD_NUMBER;
		String attendeeFirstName=TestConstants.FIRST_NAME+randomNum;
		String attendeeNewFirstName=TestConstants.FIRST_NAME+randomNumber;
		String addressLine=TestConstants.NEW_ADDRESS_LINE1_US;
		String city=TestConstants.NEW_ADDRESS_CITY_US;
		String postalCode=TestConstants.NEW_ADDRESS_POSTAL_CODE_US;
		String Country=TestConstants.COUNTRY_DD_VALUE_US;
		String Province=TestConstants.PROVINCE_ALABAMA_US;
		String phoneNumber=TestConstants.NEW_ADDRESS_PHONE_NUMBER_US;
		String newAddressLine=TestConstants.USE_THIS_ADDRESS_LINE1_US;
		String newCity=TestConstants.USE_THIS_CITY_US;
		String newPostalCode=TestConstants.USE_THIS_POSTAL_CODE_US;
		String newProvince=TestConstants.USE_THIS_PROVINCE_US;

		//-------------------FOR US----------------------------------
		driver.get(driver.getStoreFrontURL()+"/us");
		List<Map<String, Object>> randomConsultantList =  null;
		storeFrontHomePage = new StoreFrontHomePage(driver);
		while(true){
			randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_CONSULTANT_WITH_ORDERS_AND_AUTOSHIPS_RFO,"236"),RFO_DB);
			consultantEmailID = (String) getValueFromQueryResult(randomConsultantList, "UserName");  
			String accountId= String.valueOf(getValueFromQueryResult(randomConsultantList, "AccountID"));
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

		cscockpitHomePage = cscockpitLoginPage.clickLoginBtn();
		cscockpitHomePage.selectCustomerTypeFromDropDownInCustomerSearchTab("CONSULTANT");
		cscockpitHomePage.selectCountryFromDropDownInCustomerSearchTab("United States");
		cscockpitHomePage.selectAccountStatusFromDropDownInCustomerSearchTab("Active");
		cscockpitHomePage.enterEmailIdInSearchFieldInCustomerSearchTab(consultantEmailID);
		cscockpitHomePage.clickSearchBtn();
		randomCustomerSequenceNumber = String.valueOf(cscockpitHomePage.getRandomCustomerFromSearchResult());
		cscockpitHomePage.clickCIDNumberInCustomerSearchTab(randomCustomerSequenceNumber);
		orderNumber=cscockpitHomePage.clickAndGetOrderNumberInCustomerTab();
		cscockpitHomePage.clickPlaceOrderButtonInCustomerTab();
		cscockpitHomePage.selectValueFromSortByDDInCartTab("Price: High to Low");
		cscockpitHomePage.selectCatalogFromDropDownInCartTab();
		randomProductSequenceNumber = String.valueOf(cscockpitHomePage.getRandomProductWithSKUFromSearchResult()); 
		SKUValue = cscockpitHomePage.getCustomerSKUValueInCartTab(randomProductSequenceNumber);
		cscockpitHomePage.searchSKUValueInCartTab(SKUValue);
		cscockpitHomePage.clickAddToCartBtnInCartTab();
		cscockpitHomePage.clickCheckoutBtnInCartTab();
		cscockpitHomePage.clickAddNewAddressUnderDeliveryAddressInCheckoutTab();
		s_assert.assertTrue(cscockpitHomePage.verifyAddressTextBoxInPopUpInCheckoutTab(),"Address Line 1 text box is not present in popup at checkout tab");
		s_assert.assertTrue(cscockpitHomePage.verifyPostalCodeTextBoxInPopUpInCheckoutTab(),"Postal code text box is not present in popup at checkout tab");
		cscockpitHomePage.clickCloseOfDeliveryAddressPopUpInCheckoutTab();
		cscockpitHomePage.clickAddNewAddressUnderDeliveryAddressInCheckoutTab();
		cscockpitHomePage.clickSaveOfDeliveryAddressPopUpInCheckoutTab();
		s_assert.assertTrue(cscockpitHomePage.getErrorMessageOfDeliveryAddressPopupWithoutFillingDataInCheckoutTab().contains("Attention should contain First Name and Last Name"),"CSCockpit checkout tab popup error message expected = Attention should contain First Name and Last Name and on UI = " +cscockpitHomePage.getErrorMessageOfDeliveryAddressPopupWithoutFillingDataInCheckoutTab());
		cscockpitHomePage.clickOKOfDeliveryAddressPopupInCheckoutTab();
		cscockpitHomePage.enterShippingDetailsInPopUpInCheckoutTab(attendeeFirstName,attendeeLastName,addressLine,city,postalCode,Country,Province,phoneNumber);
		cscockpitHomePage.clickSaveOfDeliveryAddressPopUpInCheckoutTab();
		cscockpitHomePage.clickUseAsEnteredPopupOkayInCheckoutTab();
		s_assert.assertTrue(cscockpitHomePage.getNewlyCreatedDeliveryAddressNameInCheckoutTab().contains(attendeeFirstName),"CSCockpit checkout tab newly created Delivery address name expected ="+ attendeeFirstName+ "and on UI = " +cscockpitHomePage.getNewlyCreatedDeliveryAddressNameInCheckoutTab());
		cscockpitHomePage.clickAddNewAddressUnderDeliveryAddressInCheckoutTab();
		cscockpitHomePage.enterShippingDetailsInPopUpInCheckoutTab(attendeeNewFirstName,attendeeLastName,newAddressLine,newCity,newPostalCode,Country,newProvince,phoneNumber);
		cscockpitHomePage.clickSaveOfDeliveryAddressPopUpInCheckoutTab();
		cscockpitHomePage.clickUseAsEnteredPopupOkayInCheckoutTab();
		s_assert.assertTrue(cscockpitHomePage.getNewlyCreatedDeliveryAddressNameInCheckoutTab().contains(attendeeNewFirstName),"CSCockpit checkout tab newly created Delivery address name expected ="+ attendeeFirstName+ "and on UI = " +cscockpitHomePage.getNewlyCreatedDeliveryAddressNameInCheckoutTab());
		cscockpitHomePage.enterCVVValueInCheckoutTab(TestConstants.SECURITY_CODE);
		cscockpitHomePage.clickUseThisCardBtnInCheckoutTab();
		cscockpitHomePage.clickPlaceOrderButtonInCheckoutTab();
		orderNumberOfOrderTab = cscockpitHomePage.getOrderNumberInOrderTab();
		cscockpitHomePage.clickCustomerTab();
		s_assert.assertTrue(cscockpitHomePage.getOrderTypeInCustomerTab(orderNumberOfOrderTab.split("\\-")[0].trim()).contains("Consultant Order"),"CSCockpit Customer tab Order type expected = Consultant Order and on UI = " +cscockpitHomePage.getOrderTypeInCustomerTab(orderNumberOfOrderTab.split("\\-")[0].trim()));
		driver.get(driver.getStoreFrontURL()+"/us");
		storeFrontConsultantPage = storeFrontHomePage.loginAsConsultant(consultantEmailID, password);
		storeFrontConsultantPage.clickOnWelcomeDropDown();
		storeFrontOrdersPage =  storeFrontConsultantPage.clickOrdersLinkPresentOnWelcomeDropDown();
		s_assert.assertTrue(storeFrontOrdersPage.verifyOrdersPageIsDisplayed(),"Orders page has not been displayed");
		orderHistoryNumber = storeFrontOrdersPage.getFirstOrderNumberFromOrderHistory();
		storeFrontOrdersPage.clickOrderNumber(orderHistoryNumber);
		s_assert.assertTrue(orderHistoryNumber.contains(orderNumberOfOrderTab.split("\\-")[0].trim()),"CSCockpit Order number expected = "+orderNumberOfOrderTab.split("\\-")[0].trim()+" and on UI = " +orderHistoryNumber);
		s_assert.assertTrue(storeFrontOrdersPage.getShippingAddress().contains(attendeeNewFirstName),"CSCockpit Delivery Address expected = attendeeNewFirstName and on UI = " +storeFrontOrdersPage.getShippingAddress());

		//-------------------FOR CA----------------------------------
		driver.get(driver.getStoreFrontURL()+"/ca");

		randomNum = CommonUtils.getRandomNum(10000, 1000000);
		randomNumber=CommonUtils.getRandomNum(10000, 1000000);
		newBillingProfileName = TestConstants.NEW_BILLING_PROFILE_NAME_US+randomNumber;
		securityCode=TestConstants.SECURITY_CODE;
		cardNumber=TestConstants.CARD_NUMBER;
		attendeeFirstName=TestConstants.FIRST_NAME+randomNumber;
		addressLine=TestConstants.ADDRESS_LINE_1_CA;
		city=TestConstants.CITY_CA;
		postalCode=TestConstants.POSTAL_CODE_CA;
		Country=TestConstants.COUNTRY_DD_VALUE_CA;
		Province=TestConstants.PROVINCE_CA;
		phoneNumber=TestConstants.PHONE_NUMBER_CA;
		newAddressLine=TestConstants.ADDRESS_LINE_1_CA;
		newCity=TestConstants.CITY_CA;
		newPostalCode=TestConstants.POSTAL_CODE_CA;
		newProvince=TestConstants.PROVINCE_CA;
		attendeeFirstName=TestConstants.FIRST_NAME+randomNum;
		attendeeNewFirstName=TestConstants.FIRST_NAME+randomNumber;

		while(true){
			randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_CONSULTANT_WITH_ORDERS_AND_AUTOSHIPS_RFO,"40"),RFO_DB);
			consultantEmailID = (String) getValueFromQueryResult(randomConsultantList, "UserName");  
			String accountId= String.valueOf(getValueFromQueryResult(randomConsultantList, "AccountID"));
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
		logger.info("login is successful");
		cscockpitLoginPage = new CSCockpitLoginPage(driver);
		driver.get(driver.getCSCockpitURL());

		cscockpitHomePage = cscockpitLoginPage.clickLoginBtn();
		cscockpitHomePage.selectCustomerTypeFromDropDownInCustomerSearchTab("CONSULTANT");
		cscockpitHomePage.selectCountryFromDropDownInCustomerSearchTab("Canada");
		cscockpitHomePage.selectAccountStatusFromDropDownInCustomerSearchTab("Active");
		cscockpitHomePage.enterEmailIdInSearchFieldInCustomerSearchTab(consultantEmailID);
		cscockpitHomePage.clickSearchBtn();
		randomCustomerSequenceNumber = String.valueOf(cscockpitHomePage.getRandomCustomerFromSearchResult());
		cscockpitHomePage.clickCIDNumberInCustomerSearchTab(randomCustomerSequenceNumber);
		orderNumber=cscockpitHomePage.clickAndGetOrderNumberInCustomerTab();
		cscockpitHomePage.clickPlaceOrderButtonInCustomerTab();
		cscockpitHomePage.selectValueFromSortByDDInCartTab("Price: High to Low");
		cscockpitHomePage.selectCatalogFromDropDownInCartTab();
		randomProductSequenceNumber = String.valueOf(cscockpitHomePage.getRandomProductWithSKUFromSearchResult()); 
		SKUValue = cscockpitHomePage.getCustomerSKUValueInCartTab(randomProductSequenceNumber);
		cscockpitHomePage.searchSKUValueInCartTab(SKUValue);
		cscockpitHomePage.clickAddToCartBtnInCartTab();
		cscockpitHomePage.clickCheckoutBtnInCartTab();
		cscockpitHomePage.clickAddNewAddressUnderDeliveryAddressInCheckoutTab();
		s_assert.assertTrue(cscockpitHomePage.verifyAddressTextBoxInPopUpInCheckoutTab(),"Address Line 1 text box is not present in popup at checkout tab");
		s_assert.assertTrue(cscockpitHomePage.verifyPostalCodeTextBoxInPopUpInCheckoutTab(),"Postal code text box is not present in popup at checkout tab");
		cscockpitHomePage.clickCloseOfDeliveryAddressPopUpInCheckoutTab();
		cscockpitHomePage.clickAddNewAddressUnderDeliveryAddressInCheckoutTab();
		cscockpitHomePage.clickSaveOfDeliveryAddressPopUpInCheckoutTab();
		s_assert.assertTrue(cscockpitHomePage.getErrorMessageOfDeliveryAddressPopupWithoutFillingDataInCheckoutTab().contains("Attention should contain First Name and Last Name"),"CSCockpit checkout tab popup error message expected = Attention should contain First Name and Last Name and on UI = " +cscockpitHomePage.getErrorMessageOfDeliveryAddressPopupWithoutFillingDataInCheckoutTab());
		cscockpitHomePage.clickOKOfDeliveryAddressPopupInCheckoutTab();
		cscockpitHomePage.enterShippingDetailsInPopUpInCheckoutTab(attendeeFirstName,attendeeLastName,addressLine,city,postalCode,Country,Province,phoneNumber);
		cscockpitHomePage.clickSaveOfDeliveryAddressPopUpInCheckoutTab();
		cscockpitHomePage.clickUseAsEnteredPopupOkayInCheckoutTab();
		s_assert.assertTrue(cscockpitHomePage.getNewlyCreatedDeliveryAddressNameInCheckoutTab().contains(attendeeFirstName),"CSCockpit checkout tab newly created Delivery address name expected ="+ attendeeFirstName+ "and on UI = " +cscockpitHomePage.getNewlyCreatedDeliveryAddressNameInCheckoutTab());
		cscockpitHomePage.clickAddNewAddressUnderDeliveryAddressInCheckoutTab();
		cscockpitHomePage.enterShippingDetailsInPopUpInCheckoutTab(attendeeNewFirstName,attendeeLastName,newAddressLine,newCity,newPostalCode,Country,newProvince,phoneNumber);
		cscockpitHomePage.clickSaveOfDeliveryAddressPopUpInCheckoutTab();
		cscockpitHomePage.clickUseAsEnteredPopupOkayInCheckoutTab();
		s_assert.assertTrue(cscockpitHomePage.getNewlyCreatedDeliveryAddressNameInCheckoutTab().contains(attendeeNewFirstName),"CSCockpit checkout tab newly created Delivery address name expected ="+ attendeeFirstName+ "and on UI = " +cscockpitHomePage.getNewlyCreatedDeliveryAddressNameInCheckoutTab());
		cscockpitHomePage.enterCVVValueInCheckoutTab(TestConstants.SECURITY_CODE);
		cscockpitHomePage.clickUseThisCardBtnInCheckoutTab();
		cscockpitHomePage.clickPlaceOrderButtonInCheckoutTab();
		orderNumberOfOrderTab = cscockpitHomePage.getOrderNumberInOrderTab();
		cscockpitHomePage.clickCustomerTab();
		s_assert.assertTrue(cscockpitHomePage.getOrderTypeInCustomerTab(orderNumberOfOrderTab.split("\\-")[0].trim()).contains("Consultant Order"),"CSCockpit Customer tab Order type expected = Consultant Order and on UI = " +cscockpitHomePage.getOrderTypeInCustomerTab(orderNumberOfOrderTab.split("\\-")[0].trim()));
		driver.get(driver.getStoreFrontURL()+"/us");
		storeFrontConsultantPage = storeFrontHomePage.loginAsConsultant(consultantEmailID, password);
		storeFrontConsultantPage.clickOnWelcomeDropDown();
		storeFrontOrdersPage =  storeFrontConsultantPage.clickOrdersLinkPresentOnWelcomeDropDown();
		s_assert.assertTrue(storeFrontOrdersPage.verifyOrdersPageIsDisplayed(),"Orders page has not been displayed");
		orderHistoryNumber = storeFrontOrdersPage.getFirstOrderNumberFromOrderHistory();
		storeFrontOrdersPage.clickOrderNumber(orderHistoryNumber);
		s_assert.assertTrue(orderHistoryNumber.contains(orderNumberOfOrderTab.split("\\-")[0].trim()),"CSCockpit Order number expected = "+orderNumberOfOrderTab.split("\\-")[0].trim()+" and on UI = " +orderHistoryNumber);
		s_assert.assertTrue(storeFrontOrdersPage.getShippingAddress().contains(attendeeNewFirstName),"CSCockpit Delivery Address expected = attendeeNewFirstName and on UI = " +storeFrontOrdersPage.getShippingAddress());
		s_assert.assertAll();
	}

	//Hybris Project-1951:To verify place order from Order detail Page
	@Test(enabled=false)//WIP
	public void testToVerifyPlaceOrderFromOrderDetailPage_1951() throws InterruptedException{

		RFO_DB = driver.getDBNameRFO();
		String randomCustomerSequenceNumber = null;
		String randomProductSequenceNumber = null;
		String consultantEmailID = null;
		String SKUValue = null;
		int randomNum = CommonUtils.getRandomNum(10000, 1000000);
		List<Map<String, Object>> randomConsultantList =  null;

		//-------------------FOR US----------------------------------
		driver.get(driver.getStoreFrontURL()+"/us");
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
		cscockpitHomePage.clickPlaceOrderButtonInCheckoutTab();
		cscockpitHomePage.clickOkButtonOfSelectPaymentDetailsPopupInCheckoutTab();
		cscockpitHomePage.enterOrderNotesInCheckoutTab(TestConstants.ORDER_NOTE+randomNum);
		cscockpitHomePage.clickOrderNoteEditButton(TestConstants.ORDER_NOTE+randomNum);
		cscockpitHomePage.updateOrderNoteOnCheckOutPage(TestConstants.UPDATED_ORDER_NOTE+randomNum);
		cscockpitHomePage.enterInvalidCV2OnPaymentInfoSection(TestConstants.INVALID_CV2_NUMBER);
		cscockpitHomePage.clickUseThisCardButtonOnCheckoutPage();
		cscockpitHomePage.clickOkForCreditCardValidationFailedPopUp();
		cscockpitHomePage.entervalidCV2OnPaymentInfoSection(TestConstants.VALID_CV2_NUMBER);
		cscockpitHomePage.clickUseThisCardButtonOnCheckoutPage();
		cscockpitHomePage.clickPlaceOrderButtonInCheckoutTab();
		s_assert.assertTrue(cscockpitHomePage.getOrderStatusAfterPlaceOrderInOrderTab().contains("SUBMITTED"),"order is not submitted successfully");
		String getOrderNumberFromCsCockpitUIOnOrderTab = cscockpitHomePage.getOrderNumberFromCsCockpitUIOnOrderTab();
		driver.get(driver.getStoreFrontURL()+"/us");
		storeFrontConsultantPage = storeFrontHomePage.loginAsConsultant(consultantEmailID, password);
		storeFrontConsultantPage.clickOnWelcomeDropDown();
		storeFrontOrdersPage = storeFrontConsultantPage.clickOrdersLinkPresentOnWelcomeDropDown();

		s_assert.assertTrue(getOrderNumberFromCsCockpitUIOnOrderTab.contains(storeFrontOrdersPage.getFirstOrderNumberFromOrderHistory()),"This Order is not present on the StoreFront of US");

		//--------------------FOR CA----------------------------------
		driver.get(driver.getStoreFrontURL()+"/ca");
		storeFrontHomePage = new StoreFrontHomePage(driver);
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
		logger.info("login is successful");
		cscockpitLoginPage = new CSCockpitLoginPage(driver);
		driver.get(driver.getCSCockpitURL());

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
		cscockpitHomePage.clickPlaceOrderButtonInCheckoutTab();
		cscockpitHomePage.clickOkButtonOfSelectPaymentDetailsPopupInCheckoutTab();
		cscockpitHomePage.enterOrderNotesInCheckoutTab(TestConstants.ORDER_NOTE+randomNum);
		cscockpitHomePage.clickOrderNoteEditButton(TestConstants.ORDER_NOTE+randomNum);
		cscockpitHomePage.updateOrderNoteOnCheckOutPage(TestConstants.UPDATED_ORDER_NOTE+randomNum);
		cscockpitHomePage.enterInvalidCV2OnPaymentInfoSection(TestConstants.INVALID_CV2_NUMBER);
		cscockpitHomePage.clickUseThisCardButtonOnCheckoutPage();
		cscockpitHomePage.clickOkForCreditCardValidationFailedPopUp();
		cscockpitHomePage.entervalidCV2OnPaymentInfoSection(TestConstants.VALID_CV2_NUMBER);
		cscockpitHomePage.clickUseThisCardButtonOnCheckoutPage();
		cscockpitHomePage.clickPlaceOrderButtonInCheckoutTab();
		s_assert.assertTrue(cscockpitHomePage.getOrderStatusAfterPlaceOrderInOrderTab().contains("SUBMITTED"),"order is not submitted successfully");
		String getOrderNumberFromCsCockpitUIOnOrderTabOnCA = cscockpitHomePage.getOrderNumberFromCsCockpitUIOnOrderTab();
		driver.get(driver.getStoreFrontURL()+"/ca");
		storeFrontConsultantPage = storeFrontHomePage.loginAsConsultant(consultantEmailID, password);
		storeFrontConsultantPage.clickOnWelcomeDropDown();
		storeFrontOrdersPage = storeFrontConsultantPage.clickOrdersLinkPresentOnWelcomeDropDown();
		s_assert.assertTrue(getOrderNumberFromCsCockpitUIOnOrderTabOnCA.contains(storeFrontOrdersPage.getFirstOrderNumberFromOrderHistory()),"This Order is not present on the StoreFront of CA");
		s_assert.assertAll();
	}

	//Hybris Project-1945:To verify csr place order through Cart Page Find customer
	@Test
	public void testVerifyCSRPlaceOrderThroughCartPageFindCustomer_1945() throws InterruptedException{
		RFO_DB = driver.getDBNameRFO();
		List<Map<String, Object>> randomConsultantList =  null;
		String consultantEmailID = null;
		String randomProductSequenceNumber = null;
		String randomCustomerSequenceNumber = null;
		String SKUValue = null;
		driver.get(driver.getStoreFrontURL()+"/ca");
		storeFrontHomePage = new StoreFrontHomePage(driver);
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
		logger.info("login is successful");
		cscockpitLoginPage = new CSCockpitLoginPage(driver);
		driver.get(driver.getCSCockpitURL());

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
		cscockpitHomePage.clickPlaceOrderButtonInCheckoutTab();
		cscockpitHomePage.clickOkButtonOfSelectPaymentDetailsPopupInCheckoutTab();
		cscockpitHomePage.entervalidCV2OnPaymentInfoSection(TestConstants.VALID_CV2_NUMBER);
		cscockpitHomePage.clickUseThisCardButtonOnCheckoutPage();
		cscockpitHomePage.clickPlaceOrderButtonInCheckoutTab();
		s_assert.assertTrue(cscockpitHomePage.getOrderStatusAfterPlaceOrderInOrderTab().contains("SUBMITTED"),"order is not submitted successfully");
		String getOrderNumberFromCsCockpitUIOnOrderTabOnCA = cscockpitHomePage.getOrderNumberFromCsCockpitUIOnOrderTab();
		driver.get(driver.getStoreFrontURL()+"/ca");
		storeFrontConsultantPage = storeFrontHomePage.loginAsConsultant(consultantEmailID, password);
		storeFrontConsultantPage.clickOnWelcomeDropDown();
		storeFrontOrdersPage = storeFrontConsultantPage.clickOrdersLinkPresentOnWelcomeDropDown();

		s_assert.assertTrue(getOrderNumberFromCsCockpitUIOnOrderTabOnCA.contains(storeFrontOrdersPage.getFirstOrderNumberFromOrderHistory()),"This Order is not present on the StoreFront of CA");

		//----------------For US---------------------------------------
		driver.get(driver.getStoreFrontURL()+"/us");
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
		cscockpitHomePage.clickPlaceOrderButtonInCheckoutTab();
		cscockpitHomePage.clickOkButtonOfSelectPaymentDetailsPopupInCheckoutTab();
		cscockpitHomePage.entervalidCV2OnPaymentInfoSection(TestConstants.VALID_CV2_NUMBER);
		cscockpitHomePage.clickUseThisCardButtonOnCheckoutPage();
		cscockpitHomePage.clickPlaceOrderButtonInCheckoutTab();
		s_assert.assertTrue(cscockpitHomePage.getOrderStatusAfterPlaceOrderInOrderTab().contains("SUBMITTED"),"order is not submitted successfully");
		String getOrderNumberFromCsCockpitUIOnOrderTab = cscockpitHomePage.getOrderNumberFromCsCockpitUIOnOrderTab();
		driver.get(driver.getStoreFrontURL()+"/us");
		storeFrontConsultantPage = storeFrontHomePage.loginAsConsultant(consultantEmailID, password);
		storeFrontConsultantPage.clickOnWelcomeDropDown();
		storeFrontOrdersPage = storeFrontConsultantPage.clickOrdersLinkPresentOnWelcomeDropDown();
		s_assert.assertTrue(getOrderNumberFromCsCockpitUIOnOrderTab.contains(storeFrontOrdersPage.getFirstOrderNumberFromOrderHistory()),"This Order is not present on the StoreFront of US");
		s_assert.assertAll();
	}

	//Hybris Project-1818:To verify user permission for update QV and CV in Order detail page
	@Test
	public void testVerifyUserPermissionsForQVandCVUpdate_1818(){
		String randomCustomerSequenceNumber = null;
		cscockpitLoginPage = new CSCockpitLoginPage(driver);
		cscockpitLoginPage.enterUsername(TestConstants.CS_AGENT_USERNAME);
		cscockpitHomePage = cscockpitLoginPage.clickLoginBtn();
		cscockpitHomePage.selectCustomerTypeFromDropDownInCustomerSearchTab("CONSULTANT");
		cscockpitHomePage.selectAccountStatusFromDropDownInCustomerSearchTab("Active");
		cscockpitHomePage.clickSearchBtn();
		randomCustomerSequenceNumber = String.valueOf(cscockpitHomePage.getRandomCustomerFromSearchResult());
		cscockpitHomePage.clickCIDNumberInCustomerSearchTab(randomCustomerSequenceNumber);
		cscockpitHomePage.clickFirstOrderInCustomerTab();
		s_assert.assertTrue(cscockpitHomePage.isQVandCVUpdateBtnDisabledOnOrderTab(),"QV and CV update button is not disabled for "+TestConstants.CS_AGENT_USERNAME);
		driver.get(driver.getCSCockpitURL());

		//		cscockpitLoginPage.enterUsername(TestConstants.ADMIN_USERNAME);
		//		cscockpitHomePage = cscockpitLoginPage.clickLoginBtn();
		//		cscockpitHomePage.selectCustomerTypeFromDropDownInCustomerSearchTab("CONSULTANT");
		//		cscockpitHomePage.selectAccountStatusFromDropDownInCustomerSearchTab("Active");
		//		cscockpitHomePage.clickSearchBtn();
		//		randomCustomerSequenceNumber = String.valueOf(cscockpitHomePage.getRandomCustomerFromSearchResult());
		//		cscockpitHomePage.clickCIDNumberInCustomerSearchTab(randomCustomerSequenceNumber);
		//		cscockpitHomePage.clickFirstOrderInCustomerTab();
		//		s_assert.assertTrue(cscockpitHomePage.isQVandCVUpdateBtnDisabled(),"QV and CV update button is not disabled for "+TestConstants.ADMIN_USERNAME);
		//		driver.get(driver.getCSCockpitURL());

		cscockpitLoginPage.enterUsername(TestConstants.CS_COMMISION_ADMIN_USERNAME);
		cscockpitHomePage = cscockpitLoginPage.clickLoginBtn();
		cscockpitHomePage.selectCustomerTypeFromDropDownInCustomerSearchTab("CONSULTANT");
		cscockpitHomePage.selectAccountStatusFromDropDownInCustomerSearchTab("Active");
		cscockpitHomePage.clickSearchBtn();
		randomCustomerSequenceNumber = String.valueOf(cscockpitHomePage.getRandomCustomerFromSearchResult());
		cscockpitHomePage.clickCIDNumberInCustomerSearchTab(randomCustomerSequenceNumber);
		cscockpitHomePage.clickFirstOrderInCustomerTab();
		s_assert.assertFalse(cscockpitHomePage.isQVandCVUpdateBtnDisabledOnOrderTab(),"QV and CV update button is not disabled for "+TestConstants.CS_COMMISION_ADMIN_USERNAME);
		driver.get(driver.getCSCockpitURL());

		cscockpitLoginPage.enterUsername(TestConstants.CS_SALES_SUPERVISORY_USERNAME);
		cscockpitHomePage = cscockpitLoginPage.clickLoginBtn();
		cscockpitHomePage.selectCustomerTypeFromDropDownInCustomerSearchTab("CONSULTANT");
		cscockpitHomePage.selectAccountStatusFromDropDownInCustomerSearchTab("Active");
		cscockpitHomePage.clickSearchBtn();
		randomCustomerSequenceNumber = String.valueOf(cscockpitHomePage.getRandomCustomerFromSearchResult());
		cscockpitHomePage.clickCIDNumberInCustomerSearchTab(randomCustomerSequenceNumber);
		cscockpitHomePage.clickFirstOrderInCustomerTab();
		s_assert.assertFalse(cscockpitHomePage.isQVandCVUpdateBtnDisabledOnOrderTab(),"QV and CV update button is not disabled for "+TestConstants.CS_SALES_SUPERVISORY_USERNAME);

		s_assert.assertAll();
	}

	//Hybris Project-1779:To verify user permission for order status change in Order detail page
	@Test(enabled=false) //ISSUE change link is not present for any user
	public void testVerifyUserPermissionsForOrderStatus_1779(){
		String randomCustomerSequenceNumber = null;
		cscockpitLoginPage = new CSCockpitLoginPage(driver);
		cscockpitLoginPage.enterUsername(TestConstants.CS_AGENT_USERNAME);
		cscockpitHomePage = cscockpitLoginPage.clickLoginBtn();
		cscockpitHomePage.selectCustomerTypeFromDropDownInCustomerSearchTab("CONSULTANT");
		cscockpitHomePage.selectAccountStatusFromDropDownInCustomerSearchTab("Active");
		cscockpitHomePage.clickSearchBtn();
		randomCustomerSequenceNumber = String.valueOf(cscockpitHomePage.getRandomCustomerFromSearchResult());
		cscockpitHomePage.clickCIDNumberInCustomerSearchTab(randomCustomerSequenceNumber);
		cscockpitHomePage.clickFirstOrderInCustomerTab();
		//		s_assert.assertTrue(cscockpitHomePage.isQVandCVUpdateBtnDisabled(),"QV and CV update button is not disabled for "+TestConstants.CS_AGENT_USERNAME);
		driver.get(driver.getCSCockpitURL());

		//		cscockpitLoginPage.enterUsername(TestConstants.ADMIN_USERNAME);
		//		cscockpitHomePage = cscockpitLoginPage.clickLoginBtn();
		//		cscockpitHomePage.selectCustomerTypeFromDropDownInCustomerSearchTab("CONSULTANT");
		//		cscockpitHomePage.selectAccountStatusFromDropDownInCustomerSearchTab("Active");
		//		cscockpitHomePage.clickSearchBtn();
		//		randomCustomerSequenceNumber = String.valueOf(cscockpitHomePage.getRandomCustomerFromSearchResult());
		//		cscockpitHomePage.clickCIDNumberInCustomerSearchTab(randomCustomerSequenceNumber);
		//		cscockpitHomePage.clickFirstOrderInCustomerTab();
		//		s_assert.assertTrue(cscockpitHomePage.isQVandCVUpdateBtnDisabled(),"QV and CV update button is not disabled for "+TestConstants.ADMIN_USERNAME);
		//		driver.get(driver.getCSCockpitURL());

		cscockpitLoginPage.enterUsername(TestConstants.CS_COMMISION_ADMIN_USERNAME);
		cscockpitHomePage = cscockpitLoginPage.clickLoginBtn();
		cscockpitHomePage.selectCustomerTypeFromDropDownInCustomerSearchTab("CONSULTANT");
		cscockpitHomePage.selectAccountStatusFromDropDownInCustomerSearchTab("Active");
		cscockpitHomePage.clickSearchBtn();
		randomCustomerSequenceNumber = String.valueOf(cscockpitHomePage.getRandomCustomerFromSearchResult());
		cscockpitHomePage.clickCIDNumberInCustomerSearchTab(randomCustomerSequenceNumber);
		cscockpitHomePage.clickFirstOrderInCustomerTab();
		//		s_assert.assertFalse(cscockpitHomePage.isQVandCVUpdateBtnDisabled(),"QV and CV update button is not disabled for "+TestConstants.CS_COMMISION_ADMIN_USERNAME);
		driver.get(driver.getCSCockpitURL());

		cscockpitLoginPage.enterUsername(TestConstants.CS_SALES_SUPERVISORY_USERNAME);
		cscockpitHomePage = cscockpitLoginPage.clickLoginBtn();
		cscockpitHomePage.selectCustomerTypeFromDropDownInCustomerSearchTab("CONSULTANT");
		cscockpitHomePage.selectAccountStatusFromDropDownInCustomerSearchTab("Active");
		cscockpitHomePage.clickSearchBtn();
		randomCustomerSequenceNumber = String.valueOf(cscockpitHomePage.getRandomCustomerFromSearchResult());
		cscockpitHomePage.clickCIDNumberInCustomerSearchTab(randomCustomerSequenceNumber);
		cscockpitHomePage.clickFirstOrderInCustomerTab();
		//		s_assert.assertFalse(cscockpitHomePage.isQVandCVUpdateBtnDisabled(),"QV and CV update button is not disabled for "+TestConstants.CS_SALES_SUPERVISORY_USERNAME);

		s_assert.assertAll();
	}

	//Hybris Project-4661:Change the Sponsor of RC user from Cscockpit
	@Test//(enabled=false) //WIP
	public void testChangeSponserOfRCFromCSCockpit_4661() throws InterruptedException{
		String randomCustomerSequenceNumber = null;
		String consultantEmailID = null;
		String accountID=null;
		String orderNumber = null;
		String existingSponserName=null;
		RFO_DB = driver.getDBNameRFO();

		List<Map<String, Object>> randomConsultantList =  null;
		List<Map<String, Object>> randomUserDetailList =  null;
		List<Map<String, Object>> randomRCList =  null;
		String rcUserEmailID =null;
		String accountId = null;

		//-------------------FOR US----------------------------------
		driver.get(driver.getStoreFrontURL()+"/us");
		storeFrontHomePage = new StoreFrontHomePage(driver);
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
		cscockpitLoginPage = new CSCockpitLoginPage(driver);
		driver.get(driver.getCSCockpitURL());		
		cscockpitLoginPage.enterUsername("cscommissionadmin");
		cscockpitHomePage =cscockpitLoginPage.clickLoginBtn();
		cscockpitHomePage.enterEmailIdInSearchFieldInCustomerSearchTab(rcUserEmailID);
		cscockpitHomePage.clickSearchBtn();
		randomCustomerSequenceNumber = String.valueOf(cscockpitHomePage.getRandomCustomerFromSearchResult());
		cscockpitHomePage.clickCIDNumberInCustomerSearchTab(randomCustomerSequenceNumber);
		orderNumber=cscockpitHomePage.clickAndGetOrderNumberInCustomerTab();
		logger.info("Order Number fetched from UI is  "+orderNumber);
		existingSponserName=cscockpitHomePage.getExistingSponserNameInOrderTab();
		logger.info("Existing Sponser name and account no on UI is  "+existingSponserName);
		cscockpitHomePage.clickChangeSponserLinkInOrderTab();
		cscockpitHomePage.enterConsultantCIDAndClickSearchInOrderTab(sponsorID);
		cscockpitHomePage.clickSelectToSelectSponserInOrderTab();
		s_assert.assertTrue(cscockpitHomePage.getNewSponserNameFromUIInOrderTab().contains(sponsorID),"CSCockpit Sponser CID expected = "+sponsorID+" and on UI = " +cscockpitHomePage.getNewSponserNameFromUIInOrderTab());
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
		cscockpitLoginPage = new CSCockpitLoginPage(driver);
		driver.get(driver.getCSCockpitURL());		
		cscockpitLoginPage.enterUsername("cscommissionadmin");
		cscockpitHomePage =cscockpitLoginPage.clickLoginBtn();
		cscockpitHomePage.enterEmailIdInSearchFieldInCustomerSearchTab(rcUserEmailID);
		cscockpitHomePage.clickSearchBtn();
		randomCustomerSequenceNumber = String.valueOf(cscockpitHomePage.getRandomCustomerFromSearchResult());
		cscockpitHomePage.clickCIDNumberInCustomerSearchTab(randomCustomerSequenceNumber);
		orderNumber=cscockpitHomePage.clickAndGetOrderNumberInCustomerTab();
		logger.info("Order Number fetched from UI is  "+orderNumber);
		existingSponserName=cscockpitHomePage.getExistingSponserNameInOrderTab();
		logger.info("Existing Sponser name and account no on UI is  "+existingSponserName);
		cscockpitHomePage.clickChangeSponserLinkInOrderTab();
		cscockpitHomePage.enterConsultantCIDAndClickSearchInOrderTab(sponsorID);
		cscockpitHomePage.clickSelectToSelectSponserInOrderTab();
		s_assert.assertTrue(cscockpitHomePage.getNewSponserNameFromUIInOrderTab().contains(sponsorID),"CSCockpit Sponser CID expected = "+sponsorID+" and on UI = " +cscockpitHomePage.getNewSponserNameFromUIInOrderTab());
		//		//Verify the sponser change in RFO Database.
		//		randomUserDetailList=DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_ORDERID_RFO,orderNumber),RFO_DB);
		//		newSponserAccountId=String.valueOf(getValueFromQueryResult(randomUserDetailList, "AccountID"));
		//		//Get account number from account id of newly selected sponser.
		//		newSponsorIdList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_ACCOUNT_NUMBER_FOR_PWS,newSponserAccountId),RFO_DB);
		//		AccountNumberDB = (String) getValueFromQueryResult(newSponsorIdList, "AccountNumber");
		//		s_assert.assertTrue(AccountNumberDB.contains(sponsorID),"CSCockpit Sponser CID expected = "+sponsorID+" and In Database  = "+AccountNumberDB);
		s_assert.assertAll();	
	}

	//Hybris Project-1937:To verify for created new user the order status should be submitted
	@Test
	public void testToVerifyForCreatedNewUserTheOrderStatusShouldBeSubmitted_1937(){
		String randomCustomerSequenceNumber = null;
		String randomProductSequenceNumber = null; 
		String SKUValue = null;
		cscockpitLoginPage = new CSCockpitLoginPage(driver);
		driver.get(driver.getCSCockpitURL());

		cscockpitHomePage = cscockpitLoginPage.clickLoginBtn();
		//-------------------FOR US---------------------------
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
		cscockpitHomePage.entervalidCV2OnPaymentInfoSection(TestConstants.VALID_CV2_NUMBER);
		cscockpitHomePage.clickUseThisCardButtonOnCheckoutPage();
		cscockpitHomePage.clickPlaceOrderButtonInCheckoutTab();
		String newlyPlacedConsultantOrderNumber = cscockpitHomePage.getOrderNumberFromCsCockpitUIOnOrderTab();
		cscockpitHomePage.clickOrderSearchTab();
		cscockpitHomePage.enterOrderNumberInOrderSearchTab(newlyPlacedConsultantOrderNumber);
		cscockpitHomePage.clickSearchBtn();
		s_assert.assertTrue(cscockpitHomePage.validateOrderStatusOnOrderSearchTab(newlyPlacedConsultantOrderNumber),"Order Status is not submitted");
		cscockpitHomePage.clickCustomerSearchTab();
		cscockpitHomePage.selectCustomerTypeFromDropDownInCustomerSearchTab("PC");
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
		cscockpitHomePage.entervalidCV2OnPaymentInfoSection(TestConstants.VALID_CV2_NUMBER);
		cscockpitHomePage.clickUseThisCardButtonOnCheckoutPage();
		cscockpitHomePage.clickPlaceOrderButtonInCheckoutTab();
		String newlyPlacedPcOrderNumber = cscockpitHomePage.getOrderNumberFromCsCockpitUIOnOrderTab();
		cscockpitHomePage.clickOrderSearchTab();
		cscockpitHomePage.enterOrderNumberInOrderSearchTab(newlyPlacedPcOrderNumber);
		cscockpitHomePage.clickSearchBtn();
		s_assert.assertTrue(cscockpitHomePage.validateOrderStatusOnOrderSearchTab(newlyPlacedPcOrderNumber),"Order Status is not submitted");
		//-------------------FOR CA---------------------------
		driver.get(driver.getCSCockpitURL());

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
		cscockpitHomePage.entervalidCV2OnPaymentInfoSection(TestConstants.VALID_CV2_NUMBER);
		cscockpitHomePage.clickUseThisCardButtonOnCheckoutPage();
		cscockpitHomePage.clickPlaceOrderButtonInCheckoutTab();
		String newlyPlacedConsultantOrderNumberForCA = cscockpitHomePage.getOrderNumberFromCsCockpitUIOnOrderTab();
		cscockpitHomePage.clickOrderSearchTab();
		cscockpitHomePage.enterOrderNumberInOrderSearchTab(newlyPlacedConsultantOrderNumberForCA);
		cscockpitHomePage.clickSearchBtn();
		s_assert.assertTrue(cscockpitHomePage.validateOrderStatusOnOrderSearchTab(newlyPlacedConsultantOrderNumberForCA),"Order Status is not submitted for canada consultant");
		cscockpitHomePage.clickCustomerSearchTab();
		cscockpitHomePage.selectCustomerTypeFromDropDownInCustomerSearchTab("PC");
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
		cscockpitHomePage.entervalidCV2OnPaymentInfoSection(TestConstants.VALID_CV2_NUMBER);
		cscockpitHomePage.clickUseThisCardButtonOnCheckoutPage();
		cscockpitHomePage.clickPlaceOrderButtonInCheckoutTab();
		String newlyPlacedPcOrderNumberForCA = cscockpitHomePage.getOrderNumberFromCsCockpitUIOnOrderTab();
		cscockpitHomePage.clickOrderSearchTab();
		cscockpitHomePage.enterOrderNumberInOrderSearchTab(newlyPlacedPcOrderNumberForCA);
		cscockpitHomePage.clickSearchBtn();
		s_assert.assertTrue(cscockpitHomePage.validateOrderStatusOnOrderSearchTab(newlyPlacedPcOrderNumberForCA),"Order Status is not submitted for canada pc");
		s_assert.assertAll();
	}

	//Hybris Project-1946:Verify the Find order page UI
	@Test(enabled=false) //WIP
	public void testVerifyFindOrderPageUI_1946(){
		String shipToCountryDDValue_All = "All";
		String shipToCountryDDValue_United_States = "United States";
		String shipToCountryDDValue_Canada = "Canada";

		String orderTypeDDValue_All = "All";
		String orderTypeDDValue_PC_Order = "PC Order";
		String orderTypeDDValue_Consultant_Order = "Consultant Order";
		String orderTypeDDValue_Retail_Order = "Retail Order";
		String orderTypeDDValue_Override_Order = "Override Order";
		String orderTypeDDValue_PCPerks_Autoship = "PCPerks Autoship";
		String orderTypeDDValue_CRP_Autoship = "CRP Autoship";
		String orderTypeDDValue_Drop_Ship = "Drop-Ship";
		String orderTypeDDValue_POS_Order = "POS Order";
		String orderTypeDDValue_Pulse_Autoship = "Pulse Autoship";
		String orderTypeDDValue_Return = "Return";

		String orderStatusDDValue_All = "All";
		String orderStatusDDValue_Cancelled = "Cancelled";
		String orderStatusDDValue_Failed = "Failed";
		String orderStatusDDValue_Partially_Shipped = "Partially_Shipped";
		String orderStatusDDValue_Shipped = "Shipped";
		String orderStatusDDValue_Submitted = "Submitted";

		cscockpitLoginPage = new CSCockpitLoginPage(driver);
		cscockpitLoginPage.enterUsername(TestConstants.CS_AGENT_USERNAME);
		cscockpitHomePage = cscockpitLoginPage.clickLoginBtn();
		cscockpitHomePage.clickFindOrderLinkOnLeftNavigation();
		s_assert.assertTrue(cscockpitHomePage.isOrderSearchPageDisplayed(), "Order search page is not displayed");
		s_assert.assertTrue(cscockpitHomePage.isOrderTypeDDDisplayedOnOrderSearchTab(), "Order Type drop down is not displayed");
		s_assert.assertTrue(cscockpitHomePage.isShipToCountryDDDisplayedOnOrderSearchTab(), "Ship To Country drop down is not displayed");
		s_assert.assertTrue(cscockpitHomePage.isOrderStatusDDDisplayedOnOrderSearchTab(), "Order Search drop down is not displayed");

		s_assert.assertTrue(cscockpitHomePage.isShipToCountryDDValuePresentOnOrderSearchTab(shipToCountryDDValue_All), "'"+shipToCountryDDValue_All+"' value is not present in 'Ship To Country' drop down");	
		s_assert.assertTrue(cscockpitHomePage.isShipToCountryDDValuePresentOnOrderSearchTab(shipToCountryDDValue_United_States), "'"+shipToCountryDDValue_United_States+"' value is not present in 'Ship To Country' drop down");
		s_assert.assertTrue(cscockpitHomePage.isShipToCountryDDValuePresentOnOrderSearchTab(shipToCountryDDValue_Canada), "'"+shipToCountryDDValue_Canada+"' value is not present in 'Ship To Country' drop down");

		s_assert.assertTrue(cscockpitHomePage.isOrderTypeDDValuePresentOnOrderSearchTab(orderTypeDDValue_All), "'"+orderTypeDDValue_All+"' value is not present in 'Order Type' drop down");
		s_assert.assertTrue(cscockpitHomePage.isOrderTypeDDValuePresentOnOrderSearchTab(orderTypeDDValue_PC_Order), "'"+orderTypeDDValue_PC_Order+"' value is not present in 'Order Type' drop down");
		s_assert.assertTrue(cscockpitHomePage.isOrderTypeDDValuePresentOnOrderSearchTab(orderTypeDDValue_Consultant_Order), "'"+orderTypeDDValue_Consultant_Order+"' value is not present in 'Order Type' drop down");
		s_assert.assertTrue(cscockpitHomePage.isOrderTypeDDValuePresentOnOrderSearchTab(orderTypeDDValue_Retail_Order), "'"+orderTypeDDValue_Retail_Order+"' value is not present in 'Order Type' drop down");
		s_assert.assertTrue(cscockpitHomePage.isOrderTypeDDValuePresentOnOrderSearchTab(orderTypeDDValue_Override_Order), "'"+orderTypeDDValue_Override_Order+"' value is not present in 'Order Type' drop down");
		s_assert.assertTrue(cscockpitHomePage.isOrderTypeDDValuePresentOnOrderSearchTab(orderTypeDDValue_PCPerks_Autoship), "'"+orderTypeDDValue_PCPerks_Autoship+"' value is not present in 'Order Type' drop down");
		s_assert.assertTrue(cscockpitHomePage.isOrderTypeDDValuePresentOnOrderSearchTab(orderTypeDDValue_CRP_Autoship), "'"+orderTypeDDValue_CRP_Autoship+"' value is not present in 'Order Type' drop down");
		s_assert.assertTrue(cscockpitHomePage.isOrderTypeDDValuePresentOnOrderSearchTab(orderTypeDDValue_Drop_Ship), "'"+orderTypeDDValue_Drop_Ship+"' value is not present in 'Order Type' drop down");
		s_assert.assertTrue(cscockpitHomePage.isOrderTypeDDValuePresentOnOrderSearchTab(orderTypeDDValue_POS_Order), "'"+orderTypeDDValue_POS_Order+"' value is not present in 'Order Type' drop down");
		s_assert.assertTrue(cscockpitHomePage.isOrderTypeDDValuePresentOnOrderSearchTab(orderTypeDDValue_Pulse_Autoship), "'"+orderTypeDDValue_Pulse_Autoship+"' value is not present in 'Order Type' drop down");
		s_assert.assertTrue(cscockpitHomePage.isOrderTypeDDValuePresentOnOrderSearchTab(orderTypeDDValue_Return), "'"+orderTypeDDValue_Return+"' value is not present in 'Order Type' drop down");

		s_assert.assertTrue(cscockpitHomePage.isOrderStatusDDValuePresentOnOrderSearchTab(orderStatusDDValue_All), "'"+orderStatusDDValue_All+"' value is not present in 'Order Status' drop down");
		s_assert.assertTrue(cscockpitHomePage.isOrderStatusDDValuePresentOnOrderSearchTab(orderStatusDDValue_Cancelled), "'"+orderStatusDDValue_Cancelled+"' value is not present in 'Order Status' drop down");
		s_assert.assertTrue(cscockpitHomePage.isOrderStatusDDValuePresentOnOrderSearchTab(orderStatusDDValue_Failed), "'"+orderStatusDDValue_Failed+"' value is not present in 'Order Status' drop down");
		s_assert.assertTrue(cscockpitHomePage.isOrderStatusDDValuePresentOnOrderSearchTab(orderStatusDDValue_Partially_Shipped), "'"+orderStatusDDValue_Partially_Shipped+"' value is not present in 'Order Status' drop down");
		s_assert.assertTrue(cscockpitHomePage.isOrderStatusDDValuePresentOnOrderSearchTab(orderStatusDDValue_Shipped), "'"+orderStatusDDValue_Shipped+"' value is not present in 'Order Status' drop down");
		s_assert.assertTrue(cscockpitHomePage.isOrderStatusDDValuePresentOnOrderSearchTab(orderStatusDDValue_Submitted), "'"+orderStatusDDValue_Submitted+"' value is not present in 'Order Status' drop down");

		s_assert.assertTrue(cscockpitHomePage.isCustomerNameOrCIDTxtFieldDisplayedOnOrderSearchTab(),"Customer Name or CID text field is not displayed on order search tab");
		s_assert.assertTrue(cscockpitHomePage.isOrderNumberTxtFieldDisplayedInOrderSearchTab(),"order number txt field is not displayed on order search tab");
		s_assert.assertTrue(cscockpitHomePage.isSearchBtnDisplayedOnOrderSearchTab(),"search button is not displayed on order search tab");

		s_assert.assertTrue(cscockpitHomePage.getValueSelectedByDefaultOnOrderSearchTab("Order Type").equalsIgnoreCase("All"),"By default selected value in 'Order Type' drop down on order search tab expected is = 'All' but on actual it is = "+cscockpitHomePage.getValueSelectedByDefaultOnOrderSearchTab("Order Type"));
		s_assert.assertTrue(cscockpitHomePage.getValueSelectedByDefaultOnOrderSearchTab("Ship To Country").equalsIgnoreCase("All"),"By default selected value in 'Order Type' drop down on order search tab expected is = 'All' but on actual it is = "+cscockpitHomePage.getValueSelectedByDefaultOnOrderSearchTab("Ship To Country"));
		s_assert.assertTrue(cscockpitHomePage.getValueSelectedByDefaultOnOrderSearchTab("Order Status").equalsIgnoreCase("All"),"By default selected value in 'Order Type' drop down on order search tab expected is = 'All' but on actual it is = "+cscockpitHomePage.getValueSelectedByDefaultOnOrderSearchTab("Order Status"));

		s_assert.assertAll();
	}

	// Hybris Project-1940:To verify for created new user the Account status should be Active
	@Test(enabled=false) //WIP
	public void testToVerifyForCreatedNewUserTheAccountStatusShouldBeActive_1940(){
		String randomCustomerSequenceNumber = null;
		String randomCustomerSequenceNumberForRC = null;
		String randomProductSequenceNumber = null;
		String SKUValue = null;
		cscockpitLoginPage = new CSCockpitLoginPage(driver);
		driver.get(driver.getCSCockpitURL());
		driver.pauseExecutionFor(5000);
		cscockpitHomePage = cscockpitLoginPage.clickLoginBtn();
		cscockpitHomePage.selectCustomerTypeFromDropDownInCustomerSearchTab("PC");
		cscockpitHomePage.selectCountryFromDropDownInCustomerSearchTab("United States");
		cscockpitHomePage.selectAccountStatusFromDropDownInCustomerSearchTab("Active");
		cscockpitHomePage.clickSearchBtn();
		randomCustomerSequenceNumber = String.valueOf(cscockpitHomePage.getRandomCustomerFromSearchResult());
		cscockpitHomePage.clickCIDNumberInCustomerSearchTab(randomCustomerSequenceNumber);
		s_assert.assertTrue(cscockpitHomePage.validateAccountStatusOnCustomerTab().contains("ACTIVE"),"Account status is not Active For US PC");
		cscockpitHomePage.clickCustomerSearchTab();
		cscockpitHomePage.selectCustomerTypeFromDropDownInCustomerSearchTab("RETAIL");
		cscockpitHomePage.clickSearchBtn();
		randomCustomerSequenceNumberForRC = String.valueOf(cscockpitHomePage.getRandomCustomerFromSearchResult());
		cscockpitHomePage.clickCIDNumberInCustomerSearchTab(randomCustomerSequenceNumberForRC);
		s_assert.assertTrue(cscockpitHomePage.validateAccountStatusOnCustomerTab().contains("ACTIVE"),"Account status is not Active For US RC");

		cscockpitHomePage.clickCustomerSearchTab();
		cscockpitHomePage.selectCustomerTypeFromDropDownInCustomerSearchTab("CONSULTANT");
		cscockpitHomePage.clickSearchBtn();
		randomCustomerSequenceNumberForRC = String.valueOf(cscockpitHomePage.getRandomCustomerFromSearchResult());
		cscockpitHomePage.clickCIDNumberInCustomerSearchTab(randomCustomerSequenceNumberForRC);
		s_assert.assertTrue(cscockpitHomePage.validateAccountStatusOnCustomerTab().contains("ACTIVE"),"Account status is not Active For US consultant");
		cscockpitHomePage.clickCustomerSearchTab();
		cscockpitHomePage.selectCustomerTypeFromDropDownInCustomerSearchTab("PC");
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
		cscockpitHomePage.entervalidCV2OnPaymentInfoSection(TestConstants.VALID_CV2_NUMBER);
		cscockpitHomePage.clickUseThisCardButtonOnCheckoutPage();
		cscockpitHomePage.clickPlaceOrderButtonInCheckoutTab();
		String newlyPlacedConsultantOrderNumber = cscockpitHomePage.getOrderNumberFromCsCockpitUIOnOrderTab();
		cscockpitHomePage.clickOrderSearchTab();
		cscockpitHomePage.enterOrderNumberInOrderSearchTab(newlyPlacedConsultantOrderNumber);
		cscockpitHomePage.clickSearchBtn();
		s_assert.assertTrue(cscockpitHomePage.validateOrderStatusOnOrderSearchTab(newlyPlacedConsultantOrderNumber),"Order Status is not submitted for US");
		//-------------------------------FOR CA-----------------------
		driver.get(driver.getCSCockpitURL());
		driver.pauseExecutionFor(5000);
		cscockpitHomePage = cscockpitLoginPage.clickLoginBtn();
		cscockpitHomePage.selectCustomerTypeFromDropDownInCustomerSearchTab("PC");
		cscockpitHomePage.selectCountryFromDropDownInCustomerSearchTab("Canada");
		cscockpitHomePage.selectAccountStatusFromDropDownInCustomerSearchTab("Active");
		cscockpitHomePage.clickSearchBtn();
		randomCustomerSequenceNumber = String.valueOf(cscockpitHomePage.getRandomCustomerFromSearchResult());
		cscockpitHomePage.clickCIDNumberInCustomerSearchTab(randomCustomerSequenceNumber);
		s_assert.assertTrue(cscockpitHomePage.validateAccountStatusOnCustomerTab().contains("ACTIVE"),"Account status is not Active For CA PC");
		cscockpitHomePage.clickCustomerSearchTab();
		cscockpitHomePage.selectCustomerTypeFromDropDownInCustomerSearchTab("RETAIL");
		cscockpitHomePage.clickSearchBtn();
		randomCustomerSequenceNumberForRC = String.valueOf(cscockpitHomePage.getRandomCustomerFromSearchResult());
		cscockpitHomePage.clickCIDNumberInCustomerSearchTab(randomCustomerSequenceNumberForRC);
		s_assert.assertTrue(cscockpitHomePage.validateAccountStatusOnCustomerTab().contains("ACTIVE"),"Account status is not Active For CA RC");

		cscockpitHomePage.clickCustomerSearchTab();
		cscockpitHomePage.selectCustomerTypeFromDropDownInCustomerSearchTab("CONSULTANT");
		cscockpitHomePage.clickSearchBtn();
		randomCustomerSequenceNumberForRC = String.valueOf(cscockpitHomePage.getRandomCustomerFromSearchResult());
		cscockpitHomePage.clickCIDNumberInCustomerSearchTab(randomCustomerSequenceNumberForRC);
		s_assert.assertTrue(cscockpitHomePage.validateAccountStatusOnCustomerTab().contains("ACTIVE"),"Account status is not Active For CA consultant");
		cscockpitHomePage.clickCustomerSearchTab();
		cscockpitHomePage.selectCustomerTypeFromDropDownInCustomerSearchTab("PC");
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
		cscockpitHomePage.entervalidCV2OnPaymentInfoSection(TestConstants.VALID_CV2_NUMBER);
		cscockpitHomePage.clickUseThisCardButtonOnCheckoutPage();
		cscockpitHomePage.clickPlaceOrderButtonInCheckoutTab();
		String newlyPlacedPcOrderNumberForCA = cscockpitHomePage.getOrderNumberFromCsCockpitUIOnOrderTab();
		cscockpitHomePage.clickOrderSearchTab();
		cscockpitHomePage.enterOrderNumberInOrderSearchTab(newlyPlacedPcOrderNumberForCA);
		cscockpitHomePage.clickSearchBtn();
		s_assert.assertTrue(cscockpitHomePage.validateOrderStatusOnOrderSearchTab(newlyPlacedPcOrderNumberForCA),"Order Status is not submitted For CA");
		s_assert.assertAll();
	}

}
