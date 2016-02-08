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
import com.rf.pages.website.cscockpit.CSCockpitAutoshipSearchTabPage;
import com.rf.pages.website.cscockpit.CSCockpitAutoshipTemplateTabPage;
import com.rf.pages.website.cscockpit.CSCockpitCartTabPage;
import com.rf.pages.website.cscockpit.CSCockpitCheckoutTabPage;
import com.rf.pages.website.cscockpit.CSCockpitCommitTaxTabPage;
import com.rf.pages.website.cscockpit.CSCockpitCustomerSearchTabPage;
import com.rf.pages.website.cscockpit.CSCockpitCustomerTabPage;
import com.rf.pages.website.cscockpit.CSCockpitLoginPage;
import com.rf.pages.website.cscockpit.CSCockpitOrderSearchTabPage;
import com.rf.pages.website.cscockpit.CSCockpitOrderTabPage;
import com.rf.pages.website.storeFront.StoreFrontAccountInfoPage;
import com.rf.pages.website.storeFront.StoreFrontConsultantPage;
import com.rf.pages.website.storeFront.StoreFrontHomePage;
import com.rf.pages.website.storeFront.StoreFrontOrdersPage;
import com.rf.pages.website.storeFront.StoreFrontPCUserPage;
import com.rf.pages.website.storeFront.StoreFrontRCUserPage;
import com.rf.pages.website.storeFront.StoreFrontUpdateCartPage;
import com.rf.test.website.RFWebsiteBaseTest;


public class OrdersVerificationTest extends RFWebsiteBaseTest{
	private static final Logger logger = LogManager
			.getLogger(OrdersVerificationTest.class.getName());

	//-------------------------------------------------Pages---------------------------------------------------------
	private CSCockpitLoginPage cscockpitLoginPage;	
	private CSCockpitCheckoutTabPage cscockpitCheckoutTabPage;
	private CSCockpitCustomerSearchTabPage cscockpitCustomerSearchTabPage;
	private CSCockpitCustomerTabPage cscockpitCustomerTabPage;
	private CSCockpitOrderSearchTabPage cscockpitOrderSearchTabPage;
	private CSCockpitOrderTabPage cscockpitOrderTabPage;
	private CSCockpitCartTabPage cscockpitCartTabPage;
	private StoreFrontHomePage storeFrontHomePage; 
	private StoreFrontConsultantPage storeFrontConsultantPage;
	private StoreFrontOrdersPage storeFrontOrdersPage;
	private StoreFrontPCUserPage storeFrontPCUserPage;
	private StoreFrontRCUserPage storeFrontRCUserPage;
	private StoreFrontUpdateCartPage storeFrontUpdateCartPage;
	private StoreFrontAccountInfoPage storeFrontAccountInfoPage;
	private CSCockpitAutoshipSearchTabPage cscockpitAutoshipSearchTabPage;
	private CSCockpitAutoshipTemplateTabPage cscockpitAutoshipTemplateTabPage;
	private CSCockpitCommitTaxTabPage cscockpitCommitTaxTabPage;

	//-----------------------------------------------------------------------------------------------------------------

	public OrdersVerificationTest() {
		cscockpitLoginPage = new CSCockpitLoginPage(driver);
		cscockpitCheckoutTabPage = new CSCockpitCheckoutTabPage(driver);
		cscockpitCustomerSearchTabPage = new CSCockpitCustomerSearchTabPage(driver);
		cscockpitCustomerTabPage = new CSCockpitCustomerTabPage(driver);
		cscockpitOrderSearchTabPage = new CSCockpitOrderSearchTabPage(driver);
		cscockpitOrderTabPage = new CSCockpitOrderTabPage(driver);
		cscockpitCartTabPage = new CSCockpitCartTabPage(driver);
		cscockpitAutoshipSearchTabPage = new CSCockpitAutoshipSearchTabPage(driver);
		storeFrontHomePage = new StoreFrontHomePage(driver);
		storeFrontConsultantPage = new StoreFrontConsultantPage(driver);
		storeFrontOrdersPage = new StoreFrontOrdersPage(driver);
		storeFrontPCUserPage = new StoreFrontPCUserPage(driver);
		storeFrontRCUserPage = new StoreFrontRCUserPage(driver);
		storeFrontAccountInfoPage = new StoreFrontAccountInfoPage(driver);
		cscockpitAutoshipTemplateTabPage = new CSCockpitAutoshipTemplateTabPage(driver);
		cscockpitCommitTaxTabPage = new CSCockpitCommitTaxTabPage(driver);
	}
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
		driver.get(driver.getCSCockpitURL());		
		cscockpitCustomerSearchTabPage = cscockpitLoginPage.clickLoginBtn();
		cscockpitCustomerSearchTabPage.selectCustomerTypeFromDropDownInCustomerSearchTab("CONSULTANT");
		cscockpitCustomerSearchTabPage.selectCountryFromDropDownInCustomerSearchTab("United States");
		cscockpitCustomerSearchTabPage.selectAccountStatusFromDropDownInCustomerSearchTab("Active");
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
		cscockpitCustomerTabPage.clickCustomerTab();
		s_assert.assertTrue(cscockpitCustomerTabPage.getOrderTypeInCustomerTab(orderNumber.split("\\-")[0].trim()).contains("Consultant Order"),"CSCockpit Customer tab Order type expected = Consultant Order and on UI = " +cscockpitCustomerTabPage.getOrderTypeInCustomerTab(orderNumber.split("\\-")[0].trim()));
		driver.get(driver.getStoreFrontURL()+"/us");
		storeFrontConsultantPage = storeFrontHomePage.loginAsConsultant(consultantEmailID, password);
		storeFrontConsultantPage.clickOnWelcomeDropDown();
		storeFrontOrdersPage =  storeFrontConsultantPage.clickOrdersLinkPresentOnWelcomeDropDown();
		s_assert.assertTrue(storeFrontOrdersPage.verifyOrdersPageIsDisplayed(),"Orders page has not been displayed");
		orderHistoryNumber = storeFrontOrdersPage.getFirstOrderNumberFromOrderHistory();
		s_assert.assertTrue(orderHistoryNumber.contains(orderNumber.split("\\-")[0].trim()),"CSCockpit Order number expected = "+orderNumber.split("\\-")[0].trim()+" and on UI = " +orderHistoryNumber);
		logout();
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
		driver.get(driver.getCSCockpitURL());
		cscockpitCustomerSearchTabPage = cscockpitLoginPage.clickLoginBtn();
		cscockpitCustomerSearchTabPage.selectCustomerTypeFromDropDownInCustomerSearchTab("CONSULTANT");
		cscockpitCustomerSearchTabPage.selectCountryFromDropDownInCustomerSearchTab("Canada");
		cscockpitCustomerSearchTabPage.selectAccountStatusFromDropDownInCustomerSearchTab("Active");
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
		//s_assert.assertTrue(cscockpitCheckoutTabPage.getDeliverModeTypeInCheckoutTab().contains("UPS Ground (HD)"),"CSCockpit checkout tab delivery mode type expected = UPS Ground (HD) and on UI = " +cscockpitCheckoutTabPage.getDeliverModeTypeInCheckoutTab());
		s_assert.assertTrue(cscockpitCheckoutTabPage.isCommissionDatePopulatedInCheckoutTab(), "Commission date is not populated in UI");
		cscockpitCheckoutTabPage.clickPlaceOrderButtonInCheckoutTab();
		s_assert.assertTrue(cscockpitCheckoutTabPage.verifySelectPaymentDetailsPopupInCheckoutTab(), "Select payment details popup is not present");
		cscockpitCheckoutTabPage.clickOkButtonOfSelectPaymentDetailsPopupInCheckoutTab();
		cscockpitCheckoutTabPage.enterCVVValueInCheckoutTab(TestConstants.SECURITY_CODE);
		cscockpitCheckoutTabPage.clickUseThisCardBtnInCheckoutTab();
		cscockpitCheckoutTabPage.clickPlaceOrderButtonInCheckoutTab();
		orderNumber = cscockpitOrderTabPage.getOrderNumberInOrderTab();
		cscockpitCustomerTabPage.clickCustomerTab();
		s_assert.assertTrue(cscockpitCustomerTabPage.getOrderTypeInCustomerTab(orderNumber.split("\\-")[0].trim()).contains("Consultant Order"),"CSCockpit Customer tab Order type expected = Consultant Order and on UI = " +cscockpitCustomerTabPage.getOrderTypeInCustomerTab(orderNumber.split("\\-")[0].trim()));
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

		driver.get(driver.getCSCockpitURL());		
		cscockpitCustomerSearchTabPage = cscockpitLoginPage.clickLoginBtn();
		cscockpitCustomerSearchTabPage.selectCustomerTypeFromDropDownInCustomerSearchTab("PC");
		cscockpitCustomerSearchTabPage.selectCountryFromDropDownInCustomerSearchTab("United States");
		cscockpitCustomerSearchTabPage.selectAccountStatusFromDropDownInCustomerSearchTab("Active");
		cscockpitCustomerSearchTabPage.enterEmailIdInSearchFieldInCustomerSearchTab(pcUserEmailID);
		cscockpitCustomerSearchTabPage.clickSearchBtn();
		randomCustomerSequenceNumber = String.valueOf(cscockpitCustomerSearchTabPage.getRandomCustomerFromSearchResult());
		//PCEmailID = cscockpitHomePage.getEmailIdOfTheCustomerInCustomerSearchTab(randomCustomerSequenceNumber);
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
		cscockpitCheckoutTabPage.enterOrderNotesInCheckoutTab(TestConstants.ORDER_NOTE+randomNum);
		String orderNotevalueFromUI = cscockpitCheckoutTabPage.getAddedNoteValueInCheckoutTab(TestConstants.ORDER_NOTE+randomNum);
		s_assert.assertTrue(cscockpitCheckoutTabPage.convertUIDateFormatToPSTFormat(orderNotevalueFromUI.split("\\ ")[0]).trim().contains(cscockpitCheckoutTabPage.getPSTDate()),"CSCockpit added order note date in checkout tab expected"+cscockpitCheckoutTabPage.getPSTDate()+"and on UI" +cscockpitCheckoutTabPage.convertUIDateFormatToPSTFormat(orderNotevalueFromUI.split("\\ ")[0]).trim());
		s_assert.assertTrue(orderNotevalueFromUI.contains("PM")||orderNotevalueFromUI.contains("AM"), "Added order note does not contain time zone");
		s_assert.assertTrue(cscockpitCheckoutTabPage.verifyEditButtonIsPresentForOrderNoteInCheckoutTab(TestConstants.ORDER_NOTE+randomNum), "Added order note does not have Edit button");
		cscockpitCheckoutTabPage.clickAddNewPaymentAddressInCheckoutTab();
		cscockpitCheckoutTabPage.enterBillingInfo();
		cscockpitCheckoutTabPage.clickSaveAddNewPaymentProfilePopUP();
		cscockpitCheckoutTabPage.enterCVVValueInCheckoutTab(TestConstants.SECURITY_CODE);
		cscockpitCheckoutTabPage.clickUseThisCardBtnInCheckoutTab();
		cscockpitCheckoutTabPage.clickPlaceOrderButtonInCheckoutTab();
		orderNumber = cscockpitOrderTabPage.getOrderNumberInOrderTab();
		cscockpitOrderTabPage.clickCustomerTab();
		s_assert.assertTrue(cscockpitCustomerTabPage.getOrderTypeInCustomerTab(orderNumber.split("\\-")[0].trim()).contains("PC Order"),"CSCockpit Customer tab Order type expected = PC Order and on UI = " +cscockpitCustomerTabPage.getOrderTypeInCustomerTab(orderNumber.split("\\-")[0].trim()));
		driver.get(driver.getStoreFrontURL()+"/us");
		storeFrontPCUserPage = storeFrontHomePage.loginAsPCUser(pcUserEmailID, password);
		storeFrontPCUserPage.clickOnWelcomeDropDown();
		storeFrontOrdersPage =  storeFrontPCUserPage.clickOrdersLinkPresentOnWelcomeDropDown();
		s_assert.assertTrue(storeFrontOrdersPage.verifyOrdersPageIsDisplayed(),"Orders page has not been displayed");
		orderHistoryNumber = storeFrontOrdersPage.getFirstOrderNumberFromOrderHistory();
		s_assert.assertTrue(orderHistoryNumber.contains(orderNumber.split("\\-")[0].trim()),"CSCockpit Order number expected = "+orderNumber.split("\\-")[0].trim()+" and on UI = " +orderHistoryNumber);
		logout();
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
				driver.get(driver.getStoreFrontURL()+"/ca");
			}
			else
				break;
		}
		logout();

		driver.get(driver.getCSCockpitURL());		
		cscockpitCustomerSearchTabPage = cscockpitLoginPage.clickLoginBtn();
		cscockpitCustomerSearchTabPage.selectCustomerTypeFromDropDownInCustomerSearchTab("PC");
		cscockpitCustomerSearchTabPage.selectCountryFromDropDownInCustomerSearchTab("Canada");
		cscockpitCustomerSearchTabPage.selectAccountStatusFromDropDownInCustomerSearchTab("Active");
		cscockpitCustomerSearchTabPage.enterEmailIdInSearchFieldInCustomerSearchTab(pcUserEmailID);
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
		s_assert.assertTrue(cscockpitCheckoutTabPage.getDeliverModeTypeInCheckoutTab().contains("UPS Ground (HD)"),"CSCockpit checkout tab delivery mode type expected = UPS Ground (HD) and on UI = " +cscockpitCheckoutTabPage.getDeliverModeTypeInCheckoutTab());
		s_assert.assertTrue(cscockpitCheckoutTabPage.isCommissionDatePopulatedInCheckoutTab(), "Commission date is not populated in UI");
		cscockpitCheckoutTabPage.clickPlaceOrderButtonInCheckoutTab();
		s_assert.assertTrue(cscockpitCheckoutTabPage.verifySelectPaymentDetailsPopupInCheckoutTab(), "Select payment details popup is not present");
		cscockpitCheckoutTabPage.clickOkButtonOfSelectPaymentDetailsPopupInCheckoutTab();
		cscockpitCheckoutTabPage.enterOrderNotesInCheckoutTab(TestConstants.ORDER_NOTE+randomNum);
		orderNotevalueFromUI = cscockpitCheckoutTabPage.getAddedNoteValueInCheckoutTab(TestConstants.ORDER_NOTE+randomNum);
		s_assert.assertTrue(cscockpitCheckoutTabPage.convertUIDateFormatToPSTFormat(orderNotevalueFromUI.split("\\ ")[0]).trim().contains(cscockpitCheckoutTabPage.getPSTDate()),"CSCockpit added order note date in checkout tab expected"+cscockpitCheckoutTabPage.getPSTDate()+"and on UI" +cscockpitCheckoutTabPage.convertUIDateFormatToPSTFormat(orderNotevalueFromUI.split("\\ ")[0]).trim());
		s_assert.assertTrue(orderNotevalueFromUI.contains("PM")||orderNotevalueFromUI.contains("AM"), "Added order note does not contain time zone");
		s_assert.assertTrue(cscockpitCheckoutTabPage.verifyEditButtonIsPresentForOrderNoteInCheckoutTab(TestConstants.ORDER_NOTE+randomNum), "Added order note does not have Edit button");
		cscockpitCheckoutTabPage.clickAddNewPaymentAddressInCheckoutTab();
		cscockpitCheckoutTabPage.enterBillingInfo();
		cscockpitCheckoutTabPage.clickSaveAddNewPaymentProfilePopUP();
		cscockpitCheckoutTabPage.enterCVVValueInCheckoutTab(TestConstants.SECURITY_CODE);
		cscockpitCheckoutTabPage.clickUseThisCardBtnInCheckoutTab();
		cscockpitCheckoutTabPage.clickPlaceOrderButtonInCheckoutTab();
		orderNumber = cscockpitOrderTabPage.getOrderNumberInOrderTab();
		cscockpitOrderTabPage.clickCustomerTab();
		s_assert.assertTrue(cscockpitCustomerTabPage.getOrderTypeInCustomerTab(orderNumber.split("\\-")[0].trim()).contains("PC Order"),"CSCockpit Customer tab Order type expected = PC Order and on UI = " +cscockpitCustomerTabPage.getOrderTypeInCustomerTab(orderNumber.split("\\-")[0].trim()));
		driver.get(driver.getStoreFrontURL()+"/ca");
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
		while(true){
			randomRCList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_RC_RFO,"236"),RFO_DB);
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

		driver.get(driver.getCSCockpitURL());
		cscockpitCustomerSearchTabPage = cscockpitLoginPage.clickLoginBtn();
		cscockpitCustomerSearchTabPage.selectCustomerTypeFromDropDownInCustomerSearchTab("RETAIL");
		cscockpitCustomerSearchTabPage.selectCountryFromDropDownInCustomerSearchTab("United States");
		cscockpitCustomerSearchTabPage.selectAccountStatusFromDropDownInCustomerSearchTab("Active");
		cscockpitCustomerSearchTabPage.enterEmailIdInSearchFieldInCustomerSearchTab(rcUserEmailID);
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
		cscockpitCheckoutTabPage.enterOrderNotesInCheckoutTab(TestConstants.ORDER_NOTE+randomNum);
		String orderNotevalueFromUI = cscockpitCheckoutTabPage.getAddedNoteValueInCheckoutTab(TestConstants.ORDER_NOTE+randomNum);
		s_assert.assertTrue(cscockpitCheckoutTabPage.convertUIDateFormatToPSTFormat(orderNotevalueFromUI.split("\\ ")[0]).trim().contains(cscockpitCheckoutTabPage.getPSTDate()),"CSCockpit added order note date in checkout tab expected"+cscockpitCheckoutTabPage.getPSTDate()+"and on UI" +cscockpitCheckoutTabPage.convertUIDateFormatToPSTFormat(orderNotevalueFromUI.split("\\ ")[0]).trim());
		s_assert.assertTrue(orderNotevalueFromUI.contains("PM")||orderNotevalueFromUI.contains("AM"), "Added order note does not contain time zone");
		s_assert.assertTrue(cscockpitCheckoutTabPage.verifyEditButtonIsPresentForOrderNoteInCheckoutTab(TestConstants.ORDER_NOTE+randomNum), "Added order note does not have Edit button");
		cscockpitCheckoutTabPage.clickAddNewPaymentAddressInCheckoutTab();
		cscockpitCheckoutTabPage.enterBillingInfo();
		cscockpitCheckoutTabPage.clickSaveAddNewPaymentProfilePopUP();
		cscockpitCheckoutTabPage.enterCVVValueInCheckoutTab(TestConstants.SECURITY_CODE);
		cscockpitCheckoutTabPage.clickUseThisCardBtnInCheckoutTab();
		driver.pauseExecutionFor(2000);
		cscockpitCheckoutTabPage.clickPlaceOrderButtonInCheckoutTab();
		orderNumber = cscockpitOrderTabPage.getOrderNumberInOrderTab();
		cscockpitOrderTabPage.clickCustomerTab();
		s_assert.assertTrue(cscockpitCustomerTabPage.getOrderTypeInCustomerTab(orderNumber.split("\\-")[0].trim()).contains("Retail Order"),"CSCockpit Customer tab Order type expected = Retail Order and on UI = " +cscockpitCustomerTabPage.getOrderTypeInCustomerTab(orderNumber.split("\\-")[0].trim()));
		driver.get(driver.getStoreFrontURL()+"/us");
		storeFrontRCUserPage = storeFrontHomePage.loginAsRCUser(rcUserEmailID, password);
		storeFrontRCUserPage.clickOnWelcomeDropDown();
		storeFrontOrdersPage =  storeFrontRCUserPage.clickOrdersLinkPresentOnWelcomeDropDown();
		s_assert.assertTrue(storeFrontOrdersPage.verifyOrdersPageIsDisplayed(),"Orders page has not been displayed");
		orderHistoryNumber = storeFrontOrdersPage.getFirstOrderNumberFromOrderHistory();
		s_assert.assertTrue(orderHistoryNumber.contains(orderNumber.split("\\-")[0].trim()),"CSCockpit Order number expected = "+orderNumber.split("\\-")[0].trim()+" and on UI = " +orderHistoryNumber);
		logout();
		//-------------------FOR CA----------------------------------
		driver.get(driver.getStoreFrontURL()+"/ca");
		while(true){
			randomRCList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_RC_RFO,"40"),RFO_DB);
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
		driver.get(driver.getCSCockpitURL());
		cscockpitCustomerSearchTabPage = cscockpitLoginPage.clickLoginBtn();
		cscockpitCustomerSearchTabPage.selectCustomerTypeFromDropDownInCustomerSearchTab("RETAIL");
		cscockpitCustomerSearchTabPage.selectCountryFromDropDownInCustomerSearchTab("Canada");
		cscockpitCustomerSearchTabPage.selectAccountStatusFromDropDownInCustomerSearchTab("Active");
		cscockpitCustomerSearchTabPage.enterEmailIdInSearchFieldInCustomerSearchTab(rcUserEmailID);
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
		s_assert.assertTrue(cscockpitCheckoutTabPage.getDeliverModeTypeInCheckoutTab().contains("UPS Ground (HD)"),"CSCockpit checkout tab delivery mode type expected = UPS Ground (HD) and on UI = " +cscockpitCheckoutTabPage.getDeliverModeTypeInCheckoutTab());
		s_assert.assertTrue(cscockpitCheckoutTabPage.isCommissionDatePopulatedInCheckoutTab(), "Commission date is not populated in UI");
		cscockpitCheckoutTabPage.clickPlaceOrderButtonInCheckoutTab();
		s_assert.assertTrue(cscockpitCheckoutTabPage.verifySelectPaymentDetailsPopupInCheckoutTab(), "Select payment details popup is not present");
		cscockpitCheckoutTabPage.clickOkButtonOfSelectPaymentDetailsPopupInCheckoutTab();
		cscockpitCheckoutTabPage.enterOrderNotesInCheckoutTab(TestConstants.ORDER_NOTE+randomNum);
		orderNotevalueFromUI = cscockpitCheckoutTabPage.getAddedNoteValueInCheckoutTab(TestConstants.ORDER_NOTE+randomNum);
		s_assert.assertTrue(cscockpitCheckoutTabPage.convertUIDateFormatToPSTFormat(orderNotevalueFromUI.split("\\ ")[0]).trim().contains(cscockpitCheckoutTabPage.getPSTDate()),"CSCockpit added order note date in checkout tab expected"+cscockpitCheckoutTabPage.getPSTDate()+"and on UI" +cscockpitCheckoutTabPage.convertUIDateFormatToPSTFormat(orderNotevalueFromUI.split("\\ ")[0]).trim());
		s_assert.assertTrue(orderNotevalueFromUI.contains("PM")||orderNotevalueFromUI.contains("AM"), "Added order note does not contain time zone");
		s_assert.assertTrue(cscockpitCheckoutTabPage.verifyEditButtonIsPresentForOrderNoteInCheckoutTab(TestConstants.ORDER_NOTE+randomNum), "Added order note does not have Edit button");
		cscockpitCheckoutTabPage.clickAddNewPaymentAddressInCheckoutTab();
		cscockpitCheckoutTabPage.enterBillingInfo();
		cscockpitCheckoutTabPage.clickSaveAddNewPaymentProfilePopUP();
		cscockpitCheckoutTabPage.enterCVVValueInCheckoutTab(TestConstants.SECURITY_CODE);
		cscockpitCheckoutTabPage.clickUseThisCardBtnInCheckoutTab();
		cscockpitCheckoutTabPage.clickPlaceOrderButtonInCheckoutTab();
		orderNumber = cscockpitOrderTabPage.getOrderNumberInOrderTab();
		cscockpitOrderTabPage.clickCustomerTab();
		s_assert.assertTrue(cscockpitCustomerTabPage.getOrderTypeInCustomerTab(orderNumber.split("\\-")[0].trim()).contains("Retail Order"),"CSCockpit Customer tab Order type expected = Retail Order and on UI = " +cscockpitCustomerTabPage.getOrderTypeInCustomerTab(orderNumber.split("\\-")[0].trim()));
		driver.get(driver.getStoreFrontURL()+"/ca");
		storeFrontRCUserPage = storeFrontHomePage.loginAsRCUser(rcUserEmailID, password);
		storeFrontRCUserPage.clickOnWelcomeDropDown();
		storeFrontOrdersPage =  storeFrontRCUserPage.clickOrdersLinkPresentOnWelcomeDropDown();
		s_assert.assertTrue(storeFrontOrdersPage.verifyOrdersPageIsDisplayed(),"Orders page has not been displayed");
		orderHistoryNumber = storeFrontOrdersPage.getFirstOrderNumberFromOrderHistory();
		s_assert.assertTrue(orderHistoryNumber.contains(orderNumber.split("\\-")[0].trim()),"CSCockpit Order number expected = "+orderNumber.split("\\-")[0].trim()+" and on UI = " +orderHistoryNumber);
		s_assert.assertAll();
	}

	//Hybris Project-1950:Verify the Order Detail Page UI
	@Test
	public void testVerifyOrderDetailPageUI_1950(){
		String orderDate = "Order Date";
		String firstName = "First Name";
		String lastName = "Last Name";
		String RFCID = "R+F CID";
		String emailAddress = "Email Address";
		String orderType = "Order Type";
		String orderStatus = "Order Status";
		String orderTotal = "Order Total";
		String shipToCountry = "Ship To Country";
		String orderCreated = "Order Created";
		String commissionDate = "Commission Date";
		String consultantReceivingCommissions = "Consultant Receiving Commissions";
		String origination = "Origination";
		String store = "Store";
		String salesApplication = "Sales Application";
		String product = "Product";
		String basePrice = "Base Price";
		String totalPrice = "Total Price";
		String qty = "Qty";
		String totalCV = "Total CV";
		String totalQV = "Total QV";
		String code = "Code";
		String trackingNumber = "Tracking Number";
		String trackingURL = "Tracking Url";
		String carrier = "Carrier";
		String shippingDate = "Shipping Date";
		String description = "Description";
		String status = "Status";
		String time = "Time";
		String type = "Type";
		String requestId = "Request id";
		String amount = "Amount";
		String transactionStatus = "Transaction status";

		//-------------------FOR US----------------------------------
		cscockpitCustomerSearchTabPage = cscockpitLoginPage.clickLoginBtn();
		cscockpitCustomerSearchTabPage.clickOnFindOrderInCustomerSearchTab();
		cscockpitOrderSearchTabPage.selectOrderTypeInOrderSearchTab(TestConstants.ORDER_TYPE_DD_VALUE);
		cscockpitCustomerSearchTabPage.selectCountryFromDropDownInCustomerSearchTab("United States");
		cscockpitOrderTabPage.selectOrderStatusFromDropDownInOrderTab(TestConstants.ORDER_TYPE_DD_VALUE_SHIPPED);
		cscockpitOrderSearchTabPage.clickSearchBtn();
		String randomCustomerOrderNumber = String.valueOf(cscockpitCustomerSearchTabPage.getRandomCustomerFromSearchResult());
		String firstNameFromUI = cscockpitCustomerSearchTabPage.getfirstNameOfTheCustomerInCustomerSearchTab(randomCustomerOrderNumber);
		s_assert.assertTrue(cscockpitOrderSearchTabPage.verifyOrderNumberSectionIsPresentWithClickableLinksInOrderSearchTab(), "Order # section is not present with clickable links");
		s_assert.assertTrue(cscockpitOrderSearchTabPage.verifySectionsIsPresentInOrderSearchTab(orderDate), "Order date section is not present in order search tab");
		s_assert.assertTrue(cscockpitOrderSearchTabPage.verifySectionsIsPresentInOrderSearchTab(firstName), "First name section is not present in order search tab");
		s_assert.assertTrue(cscockpitOrderSearchTabPage.verifySectionsIsPresentInOrderSearchTab(lastName), "Last name section is not present in order search tab");
		s_assert.assertTrue(cscockpitOrderSearchTabPage.verifySectionsIsPresentInOrderSearchTab(RFCID), "RFCID section is not present in order search tab");
		s_assert.assertTrue(cscockpitOrderSearchTabPage.verifySectionsIsPresentInOrderSearchTab(emailAddress), "Email address section is not present in order search tab");
		s_assert.assertTrue(cscockpitOrderSearchTabPage.verifySectionsIsPresentInOrderSearchTab(orderType), "Order type section is not present in order search tab");
		s_assert.assertTrue(cscockpitOrderSearchTabPage.verifySectionsIsPresentInOrderSearchTab(orderStatus), "Order status section is not present in order search tab");
		s_assert.assertTrue(cscockpitOrderSearchTabPage.verifySectionsIsPresentInOrderSearchTab(orderTotal), "Order total section is not present in order search tab");
		s_assert.assertTrue(cscockpitOrderSearchTabPage.verifySectionsIsPresentInOrderSearchTab(shipToCountry), "Ship to country section is not present in order search tab");
		String orderNumber = cscockpitCustomerSearchTabPage.clickAndReturnCIDNumberInCustomerSearchTab(randomCustomerOrderNumber);
		s_assert.assertTrue(cscockpitOrderTabPage.getOrderTitleFromCsCockpitUIOnOrderTab().contains(firstNameFromUI),"full name on UI in order section of order tab "+cscockpitOrderTabPage.getOrderTitleFromCsCockpitUIOnOrderTab()+"while expected "+firstNameFromUI);
		s_assert.assertTrue(cscockpitOrderTabPage.getOrderTitleFromCsCockpitUIOnOrderTab().contains(orderNumber),"order number on UI in order section of order tab "+cscockpitOrderTabPage.getOrderTitleFromCsCockpitUIOnOrderTab()+"while expected "+orderNumber);
		s_assert.assertTrue(cscockpitOrderTabPage.verifyCartDetailsIsPresentInOrderTab(orderStatus), "Order status section is not present in order tab");
		s_assert.assertTrue(cscockpitOrderTabPage.verifyCartDetailsIsPresentInOrderTab(orderType), "Order type section is not present in order tab");
		s_assert.assertTrue(cscockpitOrderTabPage.verifyOrderDetailsIsPresentInOrderTab(orderCreated), "Order created is not present in order tab");
		s_assert.assertTrue(cscockpitOrderTabPage.verifyOrderDetailsIsPresentInOrderTab(commissionDate), "commission date is not present in order tab");
		s_assert.assertTrue(cscockpitOrderTabPage.verifyCartDetailsIsPresentInOrderTab(consultantReceivingCommissions), "conslutant receving commissions is not present in order tab");
		s_assert.assertTrue(cscockpitOrderTabPage.verifyOrderDetailsIsPresentInOrderTab(origination), "Origination is not present in order tab");
		s_assert.assertTrue(cscockpitOrderTabPage.verifyOrderDetailsIsPresentInOrderTab(store), "store is not present in order tab");
		s_assert.assertTrue(cscockpitOrderTabPage.verifyOrderDetailsIsPresentInOrderTab(salesApplication), "sales application is not present in order tab");

		s_assert.assertTrue(cscockpitOrderTabPage.verifyShippingAddressDetailsArePresentInOrderTab(), "Shipping address section is not present in order tab");
		s_assert.assertTrue(cscockpitOrderTabPage.verifyPaymentInfoDetailsArePresentInOrderTab(), "Payment info section is not present in order tab");
		s_assert.assertTrue(cscockpitCheckoutTabPage.verifyTestOrderChkBoxIsPresentInCheckoutTab(), "Test order checkbox is not present in order tab");
		s_assert.assertTrue(cscockpitCheckoutTabPage.verifyDoNotShipChkBoxIsPresentInCheckoutTab(), "Do Not Ship checkbox is not present in order tab");
		s_assert.assertTrue(cscockpitOrderTabPage.verifyReturnOrderBtnIsPresentInOrderTab(), "Return order btn is not present in order tab");
		s_assert.assertTrue(cscockpitOrderTabPage.verifyPlaceAnOrderOrderBtnIsPresentInOrderTab(), "Place an order btn is not present in order tab");

		//In order details section
		s_assert.assertTrue(cscockpitOrderSearchTabPage.verifySectionsIsPresentInOrderSearchTab(product), "Product section is not present in order tab");
		s_assert.assertTrue(cscockpitOrderSearchTabPage.verifySectionsIsPresentInOrderSearchTab(basePrice), "Base price section is not present in order tab");
		s_assert.assertTrue(cscockpitOrderSearchTabPage.verifySectionsIsPresentInOrderSearchTab(totalPrice), "total price section is not present in order tab");
		s_assert.assertTrue(cscockpitOrderSearchTabPage.verifySectionsIsPresentInOrderSearchTab(qty), "Qty section is not present in order tab");
		s_assert.assertTrue(cscockpitOrderSearchTabPage.verifySectionsIsPresentInOrderSearchTab(totalCV), "Total CV section is not present in order tab");
		s_assert.assertTrue(cscockpitOrderSearchTabPage.verifySectionsIsPresentInOrderSearchTab(totalQV), "Total QV section is not present in order tab");

		//In promotion sections
		s_assert.assertTrue(cscockpitCheckoutTabPage.verifyNoPromotionsAppliedInAppliedPromotionsSectionInCheckoutTab(), "No promotion txt is not present in order note info section of order tab");

		//In totals section
		s_assert.assertTrue(cscockpitCheckoutTabPage.verifySubtotalTxtIsPresentInTotalsSectionInCheckoutTab(), "Subtotal is not present in totals section of order tab");
		s_assert.assertTrue(cscockpitCheckoutTabPage.verifyDiscountTxtIsPresentInTotalsSectionInCheckoutTab(), "Discount is not present in totals section of order tab");
		s_assert.assertTrue(cscockpitCheckoutTabPage.verifyDeliverCostsTxtIsPresentInTotalsSectionInCheckoutTab(), "Delivery Costs is not present in totals section of order tab");
		s_assert.assertTrue(cscockpitCheckoutTabPage.verifyHandlingCostsTxtIsPresentInTotalsSectionInCheckoutTab(), "Handling Costs is not present in totals section of order tab");
		s_assert.assertTrue(cscockpitCheckoutTabPage.verifyTotalPriceTxtIsPresentInTotalsSectionInCheckoutTab(), "Total price is not present in totals section of order tab");
		s_assert.assertTrue(cscockpitCheckoutTabPage.verifyTotalCVTxtIsPresentInTotalsSectionInCheckoutTab(), "Total CV is not present in totals section of order tab");
		s_assert.assertTrue(cscockpitCheckoutTabPage.verifyTotalQVTxtIsPresentInTotalsSectionInCheckoutTab(), "Total QV is not present in totals section of order tab");
		s_assert.assertTrue(cscockpitCheckoutTabPage.verifyTaxesTxtIsPresentInTotalsSectionInCheckoutTab(), "taxes is not present in totals section of order tab");

		//consigments section
		s_assert.assertTrue(cscockpitOrderSearchTabPage.verifySectionsIsPresentInOrderSearchTab(code), "code section is not present in order tab");
		s_assert.assertTrue(cscockpitOrderSearchTabPage.verifySectionsIsPresentInOrderSearchTab(trackingNumber), "Tracking number section is not present in order tab");
		s_assert.assertTrue(cscockpitOrderSearchTabPage.verifySectionsIsPresentInOrderSearchTab(trackingURL), "Tracking URL section is not present in order tab");
		s_assert.assertTrue(cscockpitOrderSearchTabPage.verifySectionsIsPresentInOrderSearchTab(shippingDate), "shipping date section is not present in order tab");
		s_assert.assertTrue(cscockpitOrderSearchTabPage.verifySectionsIsPresentInOrderSearchTab(description), "Description section is not present in order tab");
		s_assert.assertTrue(cscockpitOrderSearchTabPage.verifySectionsIsPresentInOrderSearchTab(status), "status section is not present in order tab");
		s_assert.assertTrue(cscockpitOrderSearchTabPage.verifySectionsIsPresentInOrderSearchTab(carrier), "carrier section is not present in order tab");

		//In Payment Section
		s_assert.assertTrue(cscockpitOrderSearchTabPage.verifySectionsIsPresentInOrderSearchTab(time), "time section is not present in order tab");
		s_assert.assertTrue(cscockpitOrderSearchTabPage.verifySectionsIsPresentInOrderSearchTab(type), "type section is not present in order tab");
		s_assert.assertTrue(cscockpitOrderSearchTabPage.verifySectionsIsPresentInOrderSearchTab(requestId), "Request ID section is not present in order tab");
		s_assert.assertTrue(cscockpitOrderSearchTabPage.verifySectionsIsPresentInOrderSearchTab(amount), "Amount section is not present in order tab");
		s_assert.assertTrue(cscockpitOrderSearchTabPage.verifySectionsIsPresentInOrderSearchTab(transactionStatus), "Transaction status section is not present in order tab");

		//In order history section
		s_assert.assertTrue(cscockpitOrderTabPage.verifyOrderHistoryInOrderTab(), "order history section is not present in order tab");

		//In Modification history section
		s_assert.assertTrue(cscockpitOrderTabPage.verifyModificationHistoryInOrderTab(), "Modification history section is not present in order tab");

		//		//-------------------FOR CA----------------------------------
		driver.get(driver.getCSCockpitURL());
		cscockpitCustomerSearchTabPage = cscockpitLoginPage.clickLoginBtn();
		cscockpitCustomerSearchTabPage.clickOnFindOrderInCustomerSearchTab();
		cscockpitOrderSearchTabPage.selectOrderTypeInOrderSearchTab(TestConstants.ORDER_TYPE_DD_VALUE);
		cscockpitCustomerSearchTabPage.selectCountryFromDropDownInCustomerSearchTab("Canada");
		cscockpitOrderTabPage.selectOrderStatusFromDropDownInOrderTab(TestConstants.ORDER_TYPE_DD_VALUE_SHIPPED);
		cscockpitOrderSearchTabPage.clickSearchBtn();
		randomCustomerOrderNumber = String.valueOf(cscockpitCustomerSearchTabPage.getRandomCustomerFromSearchResult());
		firstNameFromUI = cscockpitCustomerSearchTabPage.getfirstNameOfTheCustomerInCustomerSearchTab(randomCustomerOrderNumber);
		s_assert.assertTrue(cscockpitOrderSearchTabPage.verifyOrderNumberSectionIsPresentWithClickableLinksInOrderSearchTab(), "Order # section is not present with clickable links");
		s_assert.assertTrue(cscockpitOrderSearchTabPage.verifySectionsIsPresentInOrderSearchTab(orderDate), "Order date section is not present in order search tab");
		s_assert.assertTrue(cscockpitOrderSearchTabPage.verifySectionsIsPresentInOrderSearchTab(firstName), "First name section is not present in order search tab");
		s_assert.assertTrue(cscockpitOrderSearchTabPage.verifySectionsIsPresentInOrderSearchTab(lastName), "Last name section is not present in order search tab");
		s_assert.assertTrue(cscockpitOrderSearchTabPage.verifySectionsIsPresentInOrderSearchTab(RFCID), "RFCID section is not present in order search tab");
		s_assert.assertTrue(cscockpitOrderSearchTabPage.verifySectionsIsPresentInOrderSearchTab(emailAddress), "Email address section is not present in order search tab");
		s_assert.assertTrue(cscockpitOrderSearchTabPage.verifySectionsIsPresentInOrderSearchTab(orderType), "Order type section is not present in order search tab");
		s_assert.assertTrue(cscockpitOrderSearchTabPage.verifySectionsIsPresentInOrderSearchTab(orderStatus), "Order status section is not present in order search tab");
		s_assert.assertTrue(cscockpitOrderSearchTabPage.verifySectionsIsPresentInOrderSearchTab(orderTotal), "Order total section is not present in order search tab");
		s_assert.assertTrue(cscockpitOrderSearchTabPage.verifySectionsIsPresentInOrderSearchTab(shipToCountry), "Ship to country section is not present in order search tab");
		orderNumber = cscockpitCustomerSearchTabPage.clickAndReturnCIDNumberInCustomerSearchTab(randomCustomerOrderNumber);
		s_assert.assertTrue(cscockpitOrderTabPage.getOrderTitleFromCsCockpitUIOnOrderTab().contains(firstNameFromUI),"full name on UI in order section of order tab "+cscockpitOrderTabPage.getOrderTitleFromCsCockpitUIOnOrderTab()+"while expected "+firstNameFromUI);
		s_assert.assertTrue(cscockpitOrderTabPage.getOrderTitleFromCsCockpitUIOnOrderTab().contains(orderNumber),"order number on UI in order section of order tab "+cscockpitOrderTabPage.getOrderTitleFromCsCockpitUIOnOrderTab()+"while expected "+orderNumber);
		s_assert.assertTrue(cscockpitOrderTabPage.verifyCartDetailsIsPresentInOrderTab(orderStatus), "Order status section is not present in order tab");
		s_assert.assertTrue(cscockpitOrderTabPage.verifyCartDetailsIsPresentInOrderTab(orderType), "Order type section is not present in order tab");
		s_assert.assertTrue(cscockpitOrderTabPage.verifyOrderDetailsIsPresentInOrderTab(orderCreated), "Order created is not present in order tab");
		s_assert.assertTrue(cscockpitOrderTabPage.verifyOrderDetailsIsPresentInOrderTab(commissionDate), "commission date is not present in order tab");
		s_assert.assertTrue(cscockpitOrderTabPage.verifyCartDetailsIsPresentInOrderTab(consultantReceivingCommissions), "conslutant receving commissions is not present in order tab");
		s_assert.assertTrue(cscockpitOrderTabPage.verifyOrderDetailsIsPresentInOrderTab(origination), "Origination is not present in order tab");
		s_assert.assertTrue(cscockpitOrderTabPage.verifyOrderDetailsIsPresentInOrderTab(store), "store is not present in order tab");
		s_assert.assertTrue(cscockpitOrderTabPage.verifyOrderDetailsIsPresentInOrderTab(salesApplication), "sales application is not present in order tab");

		s_assert.assertTrue(cscockpitOrderTabPage.verifyShippingAddressDetailsArePresentInOrderTab(), "Shipping address section is not present in order tab");
		s_assert.assertTrue(cscockpitOrderTabPage.verifyPaymentInfoDetailsArePresentInOrderTab(), "Payment info section is not present in order tab");
		s_assert.assertTrue(cscockpitCheckoutTabPage.verifyTestOrderChkBoxIsPresentInCheckoutTab(), "Test order checkbox is not present in order tab");
		s_assert.assertTrue(cscockpitCheckoutTabPage.verifyDoNotShipChkBoxIsPresentInCheckoutTab(), "Do Not Ship checkbox is not present in order tab");
		s_assert.assertTrue(cscockpitOrderTabPage.verifyReturnOrderBtnIsPresentInOrderTab(), "Return order btn is not present in order tab");
		s_assert.assertTrue(cscockpitOrderTabPage.verifyPlaceAnOrderOrderBtnIsPresentInOrderTab(), "Place an order btn is not present in order tab");

		//In order details section
		s_assert.assertTrue(cscockpitOrderSearchTabPage.verifySectionsIsPresentInOrderSearchTab(product), "Product section is not present in order tab");
		s_assert.assertTrue(cscockpitOrderSearchTabPage.verifySectionsIsPresentInOrderSearchTab(basePrice), "Base price section is not present in order tab");
		s_assert.assertTrue(cscockpitOrderSearchTabPage.verifySectionsIsPresentInOrderSearchTab(totalPrice), "total price section is not present in order tab");
		s_assert.assertTrue(cscockpitOrderSearchTabPage.verifySectionsIsPresentInOrderSearchTab(qty), "Qty section is not present in order tab");
		s_assert.assertTrue(cscockpitOrderSearchTabPage.verifySectionsIsPresentInOrderSearchTab(totalCV), "Total CV section is not present in order tab");
		s_assert.assertTrue(cscockpitOrderSearchTabPage.verifySectionsIsPresentInOrderSearchTab(totalQV), "Total QV section is not present in order tab");

		//In promotion sections
		s_assert.assertTrue(cscockpitCheckoutTabPage.verifyNoPromotionsAppliedInAppliedPromotionsSectionInCheckoutTab(), "No promotion txt is not present in order note info section of order tab");

		//In totals section
		s_assert.assertTrue(cscockpitCheckoutTabPage.verifySubtotalTxtIsPresentInTotalsSectionInCheckoutTab(), "Subtotal is not present in totals section of order tab");
		s_assert.assertTrue(cscockpitCheckoutTabPage.verifyDiscountTxtIsPresentInTotalsSectionInCheckoutTab(), "Discount is not present in totals section of order tab");
		s_assert.assertTrue(cscockpitCheckoutTabPage.verifyDeliverCostsTxtIsPresentInTotalsSectionInCheckoutTab(), "Delivery Costs is not present in totals section of order tab");
		s_assert.assertTrue(cscockpitCheckoutTabPage.verifyHandlingCostsTxtIsPresentInTotalsSectionInCheckoutTab(), "Handling Costs is not present in totals section of order tab");
		s_assert.assertTrue(cscockpitCheckoutTabPage.verifyTotalPriceTxtIsPresentInTotalsSectionInCheckoutTab(), "Total price is not present in totals section of order tab");
		s_assert.assertTrue(cscockpitCheckoutTabPage.verifyTotalCVTxtIsPresentInTotalsSectionInCheckoutTab(), "Total CV is not present in totals section of order tab");
		s_assert.assertTrue(cscockpitCheckoutTabPage.verifyTotalQVTxtIsPresentInTotalsSectionInCheckoutTab(), "Total QV is not present in totals section of order tab");
		s_assert.assertFalse(cscockpitCheckoutTabPage.verifyTaxesTxtIsPresentInTotalsSectionInCheckoutTab(), "taxes is present in totals section of order tab for CA");

		//consigments section
		s_assert.assertTrue(cscockpitOrderSearchTabPage.verifySectionsIsPresentInOrderSearchTab(code), "code section is not present in order tab");
		s_assert.assertTrue(cscockpitOrderSearchTabPage.verifySectionsIsPresentInOrderSearchTab(trackingNumber), "Tracking number section is not present in order tab");
		s_assert.assertTrue(cscockpitOrderSearchTabPage.verifySectionsIsPresentInOrderSearchTab(trackingURL), "Tracking URL section is not present in order tab");
		s_assert.assertTrue(cscockpitOrderSearchTabPage.verifySectionsIsPresentInOrderSearchTab(shippingDate), "shipping date section is not present in order tab");
		s_assert.assertTrue(cscockpitOrderSearchTabPage.verifySectionsIsPresentInOrderSearchTab(description), "Description section is not present in order tab");
		s_assert.assertTrue(cscockpitOrderSearchTabPage.verifySectionsIsPresentInOrderSearchTab(status), "status section is not present in order tab");
		s_assert.assertTrue(cscockpitOrderSearchTabPage.verifySectionsIsPresentInOrderSearchTab(carrier), "carrier section is not present in order tab");

		//In Payment Section
		s_assert.assertTrue(cscockpitOrderSearchTabPage.verifySectionsIsPresentInOrderSearchTab(time), "time section is not present in order tab");
		s_assert.assertTrue(cscockpitOrderSearchTabPage.verifySectionsIsPresentInOrderSearchTab(type), "type section is not present in order tab");
		s_assert.assertTrue(cscockpitOrderSearchTabPage.verifySectionsIsPresentInOrderSearchTab(requestId), "Request ID section is not present in order tab");
		s_assert.assertTrue(cscockpitOrderSearchTabPage.verifySectionsIsPresentInOrderSearchTab(amount), "Amount section is not present in order tab");
		s_assert.assertTrue(cscockpitOrderSearchTabPage.verifySectionsIsPresentInOrderSearchTab(transactionStatus), "Transaction status section is not present in order tab");

		//In order history section
		s_assert.assertTrue(cscockpitOrderTabPage.verifyOrderHistoryInOrderTab(), "order history section is not present in order tab");

		//In Modification history section
		s_assert.assertTrue(cscockpitOrderTabPage.verifyModificationHistoryInOrderTab(), "Modification history section is not present in order tab");

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

	//Hybris Project-1929:Verify Consultant Customer detail page UI
	@Test
	public void testVerifyConsultantCustomerDetailPageUI_1929(){
		String randomCustomerSequenceNumber = null;
		String accountStatus = "Account Status";
		String consultant = "CONSULTANT";
		String customerPhone = "Customer Phone";
		String mainAddress = "Main Address"; 
		String email = "Email";
		String sponsor = "Sponsor"; 
		String autoshipTemplateID = "ID";
		String autoshipTemplateType = "Type";
		String autoshipTemplateStatus = "Status";
		String autoshipTemplateActive = "Active";
		String autoshipTemplateCreationDate = "Creation Date";
		String autoshipTemplateTemplateTotal = "Template Total";
		String autoshipTemplateNextDueDate = "Next Due Date";
		String autoshipTemplateOfOrders = "# of Orders";
		String orderType = "Order Type";
		String orderStatus = "Order Status";
		String orderTotal = "Order Total";
		String orderDate = "Order Date";
		String orderNotes = "Order Notes";
		String creditCardNumber = "Credit Card number";
		String creditCardOwner = "Credit Card Owner";
		String type = "Type";
		String month = "Month";
		String validToYear = "Valid to year";
		String billingAddress = "Billing address";
		String lastName = "Last Name";
		String line1 = "Line 1";
		String line2 = "Line 2";
		String cityOrTown = "City/Town";
		String postalCode = "Postal Code";
		String country = "Country";
		String stateOrProvince = "State/Province";
		String addressType = "Address Type";

		driver.get(driver.getCSCockpitURL());		
		cscockpitCustomerSearchTabPage = cscockpitLoginPage.clickLoginBtn();
		cscockpitCustomerSearchTabPage.selectCustomerTypeFromDropDownInCustomerSearchTab("CONSULTANT");
		cscockpitCustomerSearchTabPage.selectAccountStatusFromDropDownInCustomerSearchTab("Active");
		cscockpitCustomerSearchTabPage.clickSearchBtn();
		randomCustomerSequenceNumber = String.valueOf(cscockpitCustomerSearchTabPage.getRandomCustomerFromSearchResult());
		String CID = cscockpitCustomerSearchTabPage.clickAndReturnCIDNumberInCustomerSearchTab(randomCustomerSequenceNumber);
		//assert account details
		s_assert.assertTrue(cscockpitCustomerTabPage.getOrderDetailsInCustomerTab(CID).contains(CID), "order number in customer tab"+CID+"and on UI "+cscockpitCustomerTabPage.getOrderDetailsInCustomerTab(CID));
		s_assert.assertTrue(cscockpitCustomerTabPage.getAccountDetailsInCustomerTab(accountStatus), "Account status is not present in customer tab");
		s_assert.assertTrue(cscockpitCustomerTabPage.verifyCustomerTypeIsPresentInCustomerTab(consultant), "Account type  is not consultant in customer tab");
		s_assert.assertTrue(cscockpitCustomerTabPage.getAccountDetailsInCustomerTab(customerPhone), "customer phone is not present in customer tab");
		s_assert.assertTrue(cscockpitCustomerTabPage.getAccountDetailsInCustomerTab(mainAddress), "Main address is not present in customer tab");
		s_assert.assertTrue(cscockpitCustomerTabPage.getAccountDetailsInCustomerTab(email), "Email is not present in customer tab");
		s_assert.assertTrue(cscockpitCustomerTabPage.getAccountDetailsInCustomerTab(sponsor), "sponsor is not present in customer tab");
		//assert autoship template details
		s_assert.assertTrue(cscockpitCustomerTabPage.verifyAutoshipTemplateDetailsInCustomerTab(autoshipTemplateID), "Autoship Template ID is not present in customer tab");
		s_assert.assertTrue(cscockpitCustomerTabPage.verifyAutoshipTemplateDetailsInCustomerTab(autoshipTemplateType), "Autoship Template type is not present in customer tab");
		s_assert.assertTrue(cscockpitCustomerTabPage.verifyAutoshipTemplateDetailsInCustomerTab(autoshipTemplateStatus), "Autoship Template Status is not present in customer tab");
		s_assert.assertTrue(cscockpitCustomerTabPage.verifyAutoshipTemplateDetailsInCustomerTab(autoshipTemplateActive), "Autoship Template Active is not present in customer tab");
		s_assert.assertTrue(cscockpitCustomerTabPage.verifyAutoshipTemplateDetailsInCustomerTab(autoshipTemplateCreationDate), "Autoship Template Creation Date is not present in customer tab");
		s_assert.assertTrue(cscockpitCustomerTabPage.verifyAutoshipTemplateDetailsInCustomerTab(autoshipTemplateTemplateTotal), "Autoship Template Template total is not present in customer tab");
		s_assert.assertTrue(cscockpitCustomerTabPage.verifyAutoshipTemplateDetailsInCustomerTab(autoshipTemplateNextDueDate), "Autoship Template Next Due Date is not present in customer tab");
		s_assert.assertTrue(cscockpitCustomerTabPage.verifyAutoshipTemplateDetailsInCustomerTab(autoshipTemplateOfOrders), "Autoship Template # pf orders is not present in customer tab");
		cscockpitCustomerTabPage.getAndClickFirstAutoshipIDInCustomerTab();
		s_assert.assertTrue(cscockpitAutoshipSearchTabPage.isAutoshipTemplateDisplayedInAutoshipTemplateTab(), "Autoship template page is not displayed");
		cscockpitAutoshipSearchTabPage.clickCustomerTab();
		//assert customer orders
		s_assert.assertTrue(cscockpitCustomerTabPage.verifySectionsIsPresentInCustomerTab(orderType), "Order type section is not present in customer tab");
		s_assert.assertTrue(cscockpitCustomerTabPage.verifySectionsIsPresentInCustomerTab(orderStatus), "Order status section is not present in customer tab");
		s_assert.assertTrue(cscockpitCustomerTabPage.verifySectionsIsPresentInCustomerTab(orderTotal), "Order total section is not present in customer tab");
		s_assert.assertTrue(cscockpitCustomerTabPage.verifySectionsIsPresentInCustomerTab(orderDate), "Order Date section is not present in customer tab");
		s_assert.assertTrue(cscockpitCustomerTabPage.verifySectionsIsPresentInCustomerTab(orderNotes), "Order Notes section is not present in customer tab");
		cscockpitCustomerTabPage.clickAndGetOrderNumberInCustomerTab();
		s_assert.assertTrue(cscockpitOrderTabPage.isOrderTemplateDisplayedInOrderTab(), "Order template page is not displayed");
		cscockpitOrderTabPage.clickCustomerTab();
		//assert billing information section
		s_assert.assertTrue(cscockpitCustomerTabPage.verifySectionsIsPresentInCustomerTab(creditCardNumber), "Credit card number section is not present in customer tab");
		s_assert.assertTrue(cscockpitCustomerTabPage.verifySectionsIsPresentInCustomerTab(creditCardOwner), "credit card owner section is not present in customer tab");
		s_assert.assertTrue(cscockpitCustomerTabPage.verifySectionsIsPresentInCustomerTab(type), "type section is not present in customer tab");
		s_assert.assertTrue(cscockpitCustomerTabPage.verifySectionsIsPresentInCustomerTab(month), "Month section is not present in customer tab");
		s_assert.assertTrue(cscockpitCustomerTabPage.verifySectionsIsPresentInCustomerTab(validToYear), "Valid to year section is not present in customer tab");
		s_assert.assertTrue(cscockpitCustomerTabPage.verifySectionsIsPresentInCustomerTab(billingAddress), "billing address section is not present in customer tab");
		s_assert.assertTrue(cscockpitCustomerTabPage.isAddCardButtonPresentInCustomerTab(), "Add card button is not present in billing section of customer tab");
		s_assert.assertTrue(cscockpitCustomerTabPage.isEditButtonForCreditCardPresentInCustomerTab(), "Edit button for credit card is not present in billing section of customer tab");
		cscockpitCustomerTabPage.clickAddCardButtonInCustomerTab();
		s_assert.assertTrue(cscockpitCustomerTabPage.isAddNewPaymentProfilePopupPresentInCustomerTab(), "Add new payment profile popup is not present in billing section of customer tab");
		cscockpitCheckoutTabPage.clickCloseOfPaymentAddressPopUpInCheckoutTab();
		cscockpitCustomerTabPage.clickEditButtonForCreditCardInCustomerTab();
		s_assert.assertTrue(cscockpitCustomerTabPage.isEditPaymentProfilePopupPresentInCustomerTab(), "Edit payment profile popup is not present in billing section of customer tab");
		cscockpitCheckoutTabPage.clickCloseOfEditPaymentAddressPopUpInCheckoutTab();
		//assert customer address
		s_assert.assertTrue(cscockpitCustomerTabPage.verifySectionsIsPresentInCustomerTab(lastName), "Last Name section is not present in customer tab");
		s_assert.assertTrue(cscockpitCustomerTabPage.verifySectionsIsPresentInCustomerTab(line1), "Line 1 section is not present in customer tab");
		s_assert.assertTrue(cscockpitCustomerTabPage.verifySectionsIsPresentInCustomerTab(line2), "Line 2 section is not present in customer tab");
		s_assert.assertTrue(cscockpitCustomerTabPage.verifySectionsIsPresentInCustomerTab(cityOrTown), "City/Town section is not present in customer tab");
		s_assert.assertTrue(cscockpitCustomerTabPage.verifySectionsIsPresentInCustomerTab(postalCode), "Postal Code section is not present in customer tab");
		s_assert.assertTrue(cscockpitCustomerTabPage.verifySectionsIsPresentInCustomerTab(country), "Country section is not present in customer tab");
		s_assert.assertTrue(cscockpitCustomerTabPage.verifySectionsIsPresentInCustomerTab(stateOrProvince), "State/Province section is not present in customer tab");
		s_assert.assertTrue(cscockpitCustomerTabPage.verifySectionsIsPresentInCustomerTab(addressType), "Address type section is not present in customer tab");
		cscockpitCustomerTabPage.clickEditButtonOfShippingAddressInCustomerTab();
		s_assert.assertTrue(cscockpitCustomerTabPage.isEditAddressPopupPresentInCustomerTab(), "Edit Address popup is not present in billing section of customer tab");
		cscockpitCustomerTabPage.clickCloseOfEditAddressPopUpInCustomerTab();
		cscockpitCustomerTabPage.clickAddButtonOfCustomerAddressInCustomerTab();
		s_assert.assertTrue(cscockpitCustomerTabPage.isCreateNewAddressPopupPresentInCustomerTab(), "Create new Address popup is not present in billing section of customer tab");
		s_assert.assertAll();
	}

	//Hybris Project-1946:Verify the Find order page UI
	@Test
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

		cscockpitLoginPage.enterUsername(TestConstants.CS_AGENT_USERNAME);
		cscockpitCustomerSearchTabPage = cscockpitLoginPage.clickLoginBtn();
		cscockpitCustomerSearchTabPage.clickFindOrderLinkOnLeftNavigation();
		s_assert.assertTrue(cscockpitOrderSearchTabPage.isOrderSearchPageDisplayed(), "Order search page is not displayed");
		s_assert.assertTrue(cscockpitOrderSearchTabPage.isOrderTypeDDDisplayedOnOrderSearchTab(), "Order Type drop down is not displayed");
		s_assert.assertTrue(cscockpitOrderSearchTabPage.isShipToCountryDDDisplayedOnOrderSearchTab(), "Ship To Country drop down is not displayed");
		s_assert.assertTrue(cscockpitOrderSearchTabPage.isOrderStatusDDDisplayedOnOrderSearchTab(), "Order Search drop down is not displayed");

		s_assert.assertTrue(cscockpitOrderSearchTabPage.isShipToCountryDDValuePresentOnOrderSearchTab(shipToCountryDDValue_All), "'"+shipToCountryDDValue_All+"' value is not present in 'Ship To Country' drop down");	
		s_assert.assertTrue(cscockpitOrderSearchTabPage.isShipToCountryDDValuePresentOnOrderSearchTab(shipToCountryDDValue_United_States), "'"+shipToCountryDDValue_United_States+"' value is not present in 'Ship To Country' drop down");
		s_assert.assertTrue(cscockpitOrderSearchTabPage.isShipToCountryDDValuePresentOnOrderSearchTab(shipToCountryDDValue_Canada), "'"+shipToCountryDDValue_Canada+"' value is not present in 'Ship To Country' drop down");

		s_assert.assertTrue(cscockpitOrderSearchTabPage.isOrderTypeDDValuePresentOnOrderSearchTab(orderTypeDDValue_All), "'"+orderTypeDDValue_All+"' value is not present in 'Order Type' drop down");
		s_assert.assertTrue(cscockpitOrderSearchTabPage.isOrderTypeDDValuePresentOnOrderSearchTab(orderTypeDDValue_PC_Order), "'"+orderTypeDDValue_PC_Order+"' value is not present in 'Order Type' drop down");
		s_assert.assertTrue(cscockpitOrderSearchTabPage.isOrderTypeDDValuePresentOnOrderSearchTab(orderTypeDDValue_Consultant_Order), "'"+orderTypeDDValue_Consultant_Order+"' value is not present in 'Order Type' drop down");
		s_assert.assertTrue(cscockpitOrderSearchTabPage.isOrderTypeDDValuePresentOnOrderSearchTab(orderTypeDDValue_Retail_Order), "'"+orderTypeDDValue_Retail_Order+"' value is not present in 'Order Type' drop down");
		s_assert.assertTrue(cscockpitOrderSearchTabPage.isOrderTypeDDValuePresentOnOrderSearchTab(orderTypeDDValue_Override_Order), "'"+orderTypeDDValue_Override_Order+"' value is not present in 'Order Type' drop down");
		s_assert.assertTrue(cscockpitOrderSearchTabPage.isOrderTypeDDValuePresentOnOrderSearchTab(orderTypeDDValue_PCPerks_Autoship), "'"+orderTypeDDValue_PCPerks_Autoship+"' value is not present in 'Order Type' drop down");
		s_assert.assertTrue(cscockpitOrderSearchTabPage.isOrderTypeDDValuePresentOnOrderSearchTab(orderTypeDDValue_CRP_Autoship), "'"+orderTypeDDValue_CRP_Autoship+"' value is not present in 'Order Type' drop down");
		s_assert.assertTrue(cscockpitOrderSearchTabPage.isOrderTypeDDValuePresentOnOrderSearchTab(orderTypeDDValue_Drop_Ship), "'"+orderTypeDDValue_Drop_Ship+"' value is not present in 'Order Type' drop down");
		s_assert.assertTrue(cscockpitOrderSearchTabPage.isOrderTypeDDValuePresentOnOrderSearchTab(orderTypeDDValue_POS_Order), "'"+orderTypeDDValue_POS_Order+"' value is not present in 'Order Type' drop down");
		s_assert.assertTrue(cscockpitOrderSearchTabPage.isOrderTypeDDValuePresentOnOrderSearchTab(orderTypeDDValue_Pulse_Autoship), "'"+orderTypeDDValue_Pulse_Autoship+"' value is not present in 'Order Type' drop down");
		s_assert.assertTrue(cscockpitOrderSearchTabPage.isOrderTypeDDValuePresentOnOrderSearchTab(orderTypeDDValue_Return), "'"+orderTypeDDValue_Return+"' value is not present in 'Order Type' drop down");

		s_assert.assertTrue(cscockpitOrderSearchTabPage.isOrderStatusDDValuePresentOnOrderSearchTab(orderStatusDDValue_All), "'"+orderStatusDDValue_All+"' value is not present in 'Order Status' drop down");
		s_assert.assertTrue(cscockpitOrderSearchTabPage.isOrderStatusDDValuePresentOnOrderSearchTab(orderStatusDDValue_Cancelled), "'"+orderStatusDDValue_Cancelled+"' value is not present in 'Order Status' drop down");
		s_assert.assertTrue(cscockpitOrderSearchTabPage.isOrderStatusDDValuePresentOnOrderSearchTab(orderStatusDDValue_Failed), "'"+orderStatusDDValue_Failed+"' value is not present in 'Order Status' drop down");
		s_assert.assertTrue(cscockpitOrderSearchTabPage.isOrderStatusDDValuePresentOnOrderSearchTab(orderStatusDDValue_Partially_Shipped), "'"+orderStatusDDValue_Partially_Shipped+"' value is not present in 'Order Status' drop down");
		s_assert.assertTrue(cscockpitOrderSearchTabPage.isOrderStatusDDValuePresentOnOrderSearchTab(orderStatusDDValue_Shipped), "'"+orderStatusDDValue_Shipped+"' value is not present in 'Order Status' drop down");
		s_assert.assertTrue(cscockpitOrderSearchTabPage.isOrderStatusDDValuePresentOnOrderSearchTab(orderStatusDDValue_Submitted), "'"+orderStatusDDValue_Submitted+"' value is not present in 'Order Status' drop down");

		s_assert.assertTrue(cscockpitOrderSearchTabPage.isCustomerNameOrCIDTxtFieldDisplayedOnOrderSearchTab(),"Customer Name or CID text field is not displayed on order search tab");
		s_assert.assertTrue(cscockpitOrderSearchTabPage.isOrderNumberTxtFieldDisplayedInOrderSearchTab(),"order number txt field is not displayed on order search tab");
		s_assert.assertTrue(cscockpitOrderSearchTabPage.isSearchBtnDisplayedOnOrderSearchTab(),"search button is not displayed on order search tab");

		s_assert.assertTrue(cscockpitOrderSearchTabPage.getValueSelectedByDefaultOnOrderSearchTab("Order Type").equalsIgnoreCase("All"),"By default selected value in 'Order Type' drop down on order search tab expected is = 'All' but on actual it is = "+cscockpitOrderSearchTabPage.getValueSelectedByDefaultOnOrderSearchTab("Order Type"));
		s_assert.assertTrue(cscockpitOrderSearchTabPage.getValueSelectedByDefaultOnOrderSearchTab("Ship To Country").equalsIgnoreCase("All"),"By default selected value in 'Order Type' drop down on order search tab expected is = 'All' but on actual it is = "+cscockpitOrderSearchTabPage.getValueSelectedByDefaultOnOrderSearchTab("Ship To Country"));
		s_assert.assertTrue(cscockpitOrderSearchTabPage.getValueSelectedByDefaultOnOrderSearchTab("Order Status").equalsIgnoreCase("All"),"By default selected value in 'Order Type' drop down on order search tab expected is = 'All' but on actual it is = "+cscockpitOrderSearchTabPage.getValueSelectedByDefaultOnOrderSearchTab("Order Status"));

		s_assert.assertAll();
	}


	//Hybris Project-1947:Verify the Order Search Criteria functionality
	@Test
	public void testVerifyOrderSearchCriteriaFunctionality_1947() throws InterruptedException{
		RFO_DB = driver.getDBNameRFO();
		String orderDate = "Order Date";
		String firstName = "First Name";
		String lastName = "Last Name";
		String RFCID = "R+F CID";
		String emailAddress = "Email Address";
		String orderType = "Order Type";
		String orderStatus = "Order Status";
		String orderTotal = "Order Total";
		String shipToCountry = "Ship To Country";

		//-------------------FOR US----------------------------------
		driver.get(driver.getStoreFrontURL()+"/us");
		List<Map<String, Object>> randomConsultantList =  null;
		String consultantEmailID=null;
		String accountId=null;

		while(true){
			randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_CONSULTANT_WITH_ORDERS_AND_AUTOSHIPS_RFO,"236"),RFO_DB);
			consultantEmailID = (String) getValueFromQueryResult(randomConsultantList, "UserName");  
			accountId= String.valueOf(getValueFromQueryResult(randomConsultantList, "AccountID"));
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
		List<Map<String, Object>>sponsorIdList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_ACCOUNT_NUMBER_FOR_PWS,accountId),RFO_DB);
		String	cid = (String) getValueFromQueryResult(sponsorIdList, "AccountNumber");
		//Get order number from account Id
		List<Map<String, Object>> orderNumberList=DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_ORDER_NUMBER_FROM_ACCOUNT_ID,accountId),RFO_DB);
		String	orderNumber = String.valueOf(getValueFromQueryResult(orderNumberList, "OrderNumber"));

		driver.get(driver.getCSCockpitURL());
		cscockpitCustomerSearchTabPage = cscockpitLoginPage.clickLoginBtn();
		cscockpitCustomerSearchTabPage.selectCustomerTypeFromDropDownInCustomerSearchTab("CONSULTANT");
		cscockpitCustomerSearchTabPage.selectAccountStatusFromDropDownInCustomerSearchTab("Inactive");
		cscockpitCustomerSearchTabPage.clickSearchBtn();
		String randomCustomerSequenceNumber = String.valueOf(cscockpitCustomerSearchTabPage.getRandomCustomerFromSearchResult());
		String invalidCid=cscockpitCustomerSearchTabPage.getCIDNumberInCustomerSearchTab(randomCustomerSequenceNumber);
		cscockpitCustomerSearchTabPage.clickOnFindOrderInCustomerSearchTab();
		cscockpitOrderSearchTabPage.selectOrderTypeInOrderSearchTab(TestConstants.ORDER_TYPE_DD_VALUE);
		cscockpitOrderTabPage.selectOrderStatusFromDropDownInOrderTab(TestConstants.ORDER_STATUS_DD_VALUE);
		cscockpitOrderSearchTabPage.clickSearchBtn();
		String randomSequenceNumber = String.valueOf(cscockpitCustomerSearchTabPage.getRandomCustomerFromSearchResult());
		String firstNameFromDatabase = cscockpitCustomerSearchTabPage.getfirstNameOfTheCustomerInCustomerSearchTab(randomSequenceNumber);
		String lastNameFromDatabase=cscockpitOrderSearchTabPage.getLastNameOfTheCustomerInOrderSearchTab(randomSequenceNumber);
		String completeName=firstNameFromDatabase+" "+lastNameFromDatabase;
		//select general values
		cscockpitOrderSearchTabPage.selectOrderTypeInOrderSearchTab("All");
		cscockpitOrderTabPage.selectOrderStatusFromDropDownInOrderTab("All");
		//Search cid enter customer Name not in database and click search.
		cscockpitOrderSearchTabPage.enterCIDInOrderSearchTab("Test12348976");
		cscockpitOrderSearchTabPage.clickSearchBtn();
		s_assert.assertTrue(cscockpitOrderSearchTabPage.verifyNoResultFoundForInvalidCID(),"CSCockpit Sponser CID search expected = No Results and on UI = Results are displayed");

		//Search cid as customer first Name which in database and click search.
		cscockpitOrderSearchTabPage.enterCIDInOrderSearchTab(firstNameFromDatabase);
		cscockpitOrderSearchTabPage.clickSearchBtn();
		s_assert.assertTrue(cscockpitOrderSearchTabPage.verifyOrderNumberSectionIsPresentWithClickableLinksInOrderSearchTab(), "Order # section is not present with clickable links");
		s_assert.assertTrue(cscockpitOrderSearchTabPage.verifySectionsIsPresentInOrderSearchTab(orderDate), "Order date section is not present in order search tab");
		s_assert.assertTrue(cscockpitOrderSearchTabPage.verifySectionsIsPresentInOrderSearchTab(firstName), "First name section is not present in order search tab");
		s_assert.assertTrue(cscockpitOrderSearchTabPage.verifySectionsIsPresentInOrderSearchTab(lastName), "Last name section is not present in order search tab");
		s_assert.assertTrue(cscockpitOrderSearchTabPage.verifySectionsIsPresentInOrderSearchTab(RFCID), "RFCID section is not present in order search tab");
		s_assert.assertTrue(cscockpitOrderSearchTabPage.verifySectionsIsPresentInOrderSearchTab(emailAddress), "Email address section is not present in order search tab");
		s_assert.assertTrue(cscockpitOrderSearchTabPage.verifySectionsIsPresentInOrderSearchTab(orderType), "Order type section is not present in order search tab");
		s_assert.assertTrue(cscockpitOrderSearchTabPage.verifySectionsIsPresentInOrderSearchTab(orderStatus), "Order status section is not present in order search tab");
		s_assert.assertTrue(cscockpitOrderSearchTabPage.verifySectionsIsPresentInOrderSearchTab(orderTotal), "Order total section is not present in order search tab");
		s_assert.assertTrue(cscockpitOrderSearchTabPage.verifySectionsIsPresentInOrderSearchTab(shipToCountry), "Ship to country section is not present in order search tab");

		//Search cid as customer last Name which in database and click search.
		cscockpitOrderSearchTabPage.enterCIDInOrderSearchTab(lastNameFromDatabase);
		cscockpitOrderSearchTabPage.clickSearchBtn();
		s_assert.assertTrue(cscockpitOrderSearchTabPage.verifyOrderNumberSectionIsPresentWithClickableLinksInOrderSearchTab(), "Order # section is not present with clickable links");
		s_assert.assertTrue(cscockpitOrderSearchTabPage.verifySectionsIsPresentInOrderSearchTab(firstName), "First name section is not present in order search tab");
		s_assert.assertTrue(cscockpitOrderSearchTabPage.verifySectionsIsPresentInOrderSearchTab(lastName), "Last name section is not present in order search tab");
		s_assert.assertTrue(cscockpitOrderSearchTabPage.verifySectionsIsPresentInOrderSearchTab(RFCID), "RFCID section is not present in order search tab");

		//Search cid as customer complete Name which in database and click search.
		cscockpitOrderSearchTabPage.enterCIDInOrderSearchTab(completeName);
		cscockpitOrderSearchTabPage.clickSearchBtn();
		s_assert.assertTrue(cscockpitOrderSearchTabPage.verifyOrderNumberSectionIsPresentWithClickableLinksInOrderSearchTab(), "Order # section is not present with clickable links");
		s_assert.assertTrue(cscockpitOrderSearchTabPage.verifySectionsIsPresentInOrderSearchTab(firstName), "First name section is not present in order search tab");
		s_assert.assertTrue(cscockpitOrderSearchTabPage.verifySectionsIsPresentInOrderSearchTab(lastName), "Last name section is not present in order search tab");
		s_assert.assertTrue(cscockpitOrderSearchTabPage.verifySectionsIsPresentInOrderSearchTab(RFCID), "RFCID section is not present in order search tab");

		//Search cid as customer Name which in database Having account status pending or terminated click search.
		cscockpitOrderSearchTabPage.enterCIDInOrderSearchTab(invalidCid);
		cscockpitOrderSearchTabPage.clickSearchBtn();
		s_assert.assertTrue(cscockpitOrderSearchTabPage.verifyOrderNumberSectionIsPresentWithClickableLinksInOrderSearchTab(), "Order # section is not present with clickable links");
		s_assert.assertTrue(cscockpitOrderSearchTabPage.verifySectionsIsPresentInOrderSearchTab(firstName), "First name section is not present in order search tab");
		s_assert.assertTrue(cscockpitOrderSearchTabPage.verifySectionsIsPresentInOrderSearchTab(lastName), "Last name section is not present in order search tab");
		s_assert.assertTrue(cscockpitOrderSearchTabPage.verifySectionsIsPresentInOrderSearchTab(RFCID), "RFCID section is not present in order search tab");

		//Search cid as customer CID which in not database and click search.
		cscockpitOrderSearchTabPage.enterCIDInOrderSearchTab("000000000");
		cscockpitOrderSearchTabPage.clickSearchBtn();
		s_assert.assertTrue(cscockpitOrderSearchTabPage.verifyNoResultFoundForInvalidCID(),"CSCockpit Sponser CID search expected = No Results and on UI = Results are displayed");

		//Search cid as customer CID which in database and click search.
		cscockpitOrderSearchTabPage.enterCIDInOrderSearchTab(cid);
		cscockpitOrderSearchTabPage.clickSearchBtn();
		s_assert.assertTrue(cscockpitOrderSearchTabPage.verifyOrderNumberSectionIsPresentWithClickableLinksInOrderSearchTab(), "Order # section is not present with clickable links");
		s_assert.assertTrue(cscockpitOrderSearchTabPage.verifySectionsIsPresentInOrderSearchTab(firstName), "First name section is not present in order search tab");
		s_assert.assertTrue(cscockpitOrderSearchTabPage.verifySectionsIsPresentInOrderSearchTab(lastName), "Last name section is not present in order search tab");
		s_assert.assertTrue(cscockpitOrderSearchTabPage.verifySectionsIsPresentInOrderSearchTab(RFCID), "RFCID section is not present in order search tab");

		//Clear cid field
		cscockpitOrderSearchTabPage.clearCidFieldInOrderSearchTab();
		cscockpitOrderSearchTabPage.clickSearchBtn();

		//search for order number
		cscockpitOrderSearchTabPage.enterOrderNumberInOrderSearchTab(orderNumber);
		cscockpitOrderSearchTabPage.clickSearchBtn();
		s_assert.assertTrue(cscockpitOrderSearchTabPage.verifyOrderNumberSectionIsPresentWithClickableLinksInOrderSearchTab(), "Order # section is not present with clickable links");
		s_assert.assertTrue(cscockpitOrderSearchTabPage.verifySectionsIsPresentInOrderSearchTab(firstName), "First name section is not present in order search tab");
		s_assert.assertTrue(cscockpitOrderSearchTabPage.verifySectionsIsPresentInOrderSearchTab(lastName), "Last name section is not present in order search tab");
		s_assert.assertTrue(cscockpitOrderSearchTabPage.verifySectionsIsPresentInOrderSearchTab(RFCID), "RFCID section is not present in order search tab");

		//Clear order number field
		cscockpitOrderSearchTabPage.clearOrderNumberFieldInOrderSearchTab();
		cscockpitOrderSearchTabPage.clickSearchBtn();

		//Select one by one values from order type dropdown and assert various sections.
		cscockpitOrderSearchTabPage.selectOrderTypeInOrderSearchTab(TestConstants.ORDER_TYPE_DD_FIRST_VALUE);
		cscockpitOrderSearchTabPage.clickSearchBtn();
		s_assert.assertTrue(cscockpitOrderSearchTabPage.verifySectionsIsPresentInOrderSearchTab(firstName), "First name section is not present in order search tab");
		s_assert.assertTrue(cscockpitOrderSearchTabPage.verifySectionsIsPresentInOrderSearchTab(lastName), "Last name section is not present in order search tab");
		s_assert.assertTrue(cscockpitOrderSearchTabPage.verifySectionsIsPresentInOrderSearchTab(RFCID), "RFCID section is not present in order search tab");

		cscockpitOrderSearchTabPage.selectOrderTypeInOrderSearchTab(TestConstants.ORDER_TYPE_DD_VALUE);
		cscockpitOrderSearchTabPage.clickSearchBtn();
		s_assert.assertTrue(cscockpitOrderSearchTabPage.verifySectionsIsPresentInOrderSearchTab(firstName), "First name section is not present in order search tab");
		s_assert.assertTrue(cscockpitOrderSearchTabPage.verifySectionsIsPresentInOrderSearchTab(lastName), "Last name section is not present in order search tab");
		s_assert.assertTrue(cscockpitOrderSearchTabPage.verifySectionsIsPresentInOrderSearchTab(RFCID), "RFCID section is not present in order search tab");

		cscockpitOrderSearchTabPage.selectOrderTypeInOrderSearchTab(TestConstants.ORDER_TYPE_DD_THIRD_VALUE);
		cscockpitOrderSearchTabPage.clickSearchBtn();
		s_assert.assertTrue(cscockpitOrderSearchTabPage.verifySectionsIsPresentInOrderSearchTab(firstName), "First name section is not present in order search tab");
		s_assert.assertTrue(cscockpitOrderSearchTabPage.verifySectionsIsPresentInOrderSearchTab(lastName), "Last name section is not present in order search tab");
		s_assert.assertTrue(cscockpitOrderSearchTabPage.verifySectionsIsPresentInOrderSearchTab(RFCID), "RFCID section is not present in order search tab");

		cscockpitOrderSearchTabPage.selectOrderTypeInOrderSearchTab(TestConstants.ORDER_TYPE_DD_FOURTH_VALUE);
		cscockpitOrderSearchTabPage.clickSearchBtn();
		s_assert.assertTrue(cscockpitOrderSearchTabPage.verifySectionsIsPresentInOrderSearchTab(firstName), "First name section is not present in order search tab");
		s_assert.assertTrue(cscockpitOrderSearchTabPage.verifySectionsIsPresentInOrderSearchTab(lastName), "Last name section is not present in order search tab");
		s_assert.assertTrue(cscockpitOrderSearchTabPage.verifySectionsIsPresentInOrderSearchTab(RFCID), "RFCID section is not present in order search tab");

		cscockpitOrderSearchTabPage.selectOrderTypeInOrderSearchTab(TestConstants.ORDER_TYPE_DD_FIFTH_VALUE);
		cscockpitOrderSearchTabPage.clickSearchBtn();
		s_assert.assertTrue(cscockpitOrderSearchTabPage.verifySectionsIsPresentInOrderSearchTab(firstName), "First name section is not present in order search tab");
		s_assert.assertTrue(cscockpitOrderSearchTabPage.verifySectionsIsPresentInOrderSearchTab(lastName), "Last name section is not present in order search tab");
		s_assert.assertTrue(cscockpitOrderSearchTabPage.verifySectionsIsPresentInOrderSearchTab(RFCID), "RFCID section is not present in order search tab");

		cscockpitOrderSearchTabPage.selectOrderTypeInOrderSearchTab(TestConstants.ORDER_TYPE_DD_SIXTH_VALUE);
		cscockpitOrderSearchTabPage.clickSearchBtn();
		s_assert.assertTrue(cscockpitOrderSearchTabPage.verifySectionsIsPresentInOrderSearchTab(firstName), "First name section is not present in order search tab");
		s_assert.assertTrue(cscockpitOrderSearchTabPage.verifySectionsIsPresentInOrderSearchTab(lastName), "Last name section is not present in order search tab");
		s_assert.assertTrue(cscockpitOrderSearchTabPage.verifySectionsIsPresentInOrderSearchTab(RFCID), "RFCID section is not present in order search tab");

		cscockpitOrderSearchTabPage.selectOrderTypeInOrderSearchTab(TestConstants.ORDER_TYPE_DD_SEVENTH_VALUE);
		cscockpitOrderSearchTabPage.clickSearchBtn();
		s_assert.assertTrue(cscockpitOrderSearchTabPage.verifyNoResultFoundForInvalidCID(),"CSCockpit Sponser CID search expected = No Results and on UI = Results are displayed");

		cscockpitOrderSearchTabPage.selectOrderTypeInOrderSearchTab(TestConstants.ORDER_TYPE_DD_EIGHT_VALUE);
		cscockpitOrderSearchTabPage.clickSearchBtn();
		s_assert.assertTrue(cscockpitOrderSearchTabPage.verifySectionsIsPresentInOrderSearchTab(firstName), "First name section is not present in order search tab");
		s_assert.assertTrue(cscockpitOrderSearchTabPage.verifySectionsIsPresentInOrderSearchTab(lastName), "Last name section is not present in order search tab");
		s_assert.assertTrue(cscockpitOrderSearchTabPage.verifySectionsIsPresentInOrderSearchTab(RFCID), "RFCID section is not present in order search tab");

		cscockpitOrderSearchTabPage.selectOrderTypeInOrderSearchTab(TestConstants.ORDER_TYPE_DD_NINTH_VALUE);
		cscockpitOrderSearchTabPage.clickSearchBtn();
		s_assert.assertTrue(cscockpitOrderSearchTabPage.verifySectionsIsPresentInOrderSearchTab(firstName), "First name section is not present in order search tab");
		s_assert.assertTrue(cscockpitOrderSearchTabPage.verifySectionsIsPresentInOrderSearchTab(lastName), "Last name section is not present in order search tab");
		s_assert.assertTrue(cscockpitOrderSearchTabPage.verifySectionsIsPresentInOrderSearchTab(RFCID), "RFCID section is not present in order search tab");

		cscockpitOrderSearchTabPage.selectOrderTypeInOrderSearchTab(TestConstants.ORDER_TYPE_DD_TENTH_VALUE);
		cscockpitOrderSearchTabPage.clickSearchBtn();
		s_assert.assertTrue(cscockpitOrderSearchTabPage.verifySectionsIsPresentInOrderSearchTab(firstName), "First name section is not present in order search tab");
		s_assert.assertTrue(cscockpitOrderSearchTabPage.verifySectionsIsPresentInOrderSearchTab(lastName), "Last name section is not present in order search tab");
		s_assert.assertTrue(cscockpitOrderSearchTabPage.verifySectionsIsPresentInOrderSearchTab(RFCID), "RFCID section is not present in order search tab");

		//select order type as all.
		cscockpitOrderSearchTabPage.selectOrderTypeInOrderSearchTab("All");

		//select one by one value from order status dropdown and assert various sections.
		cscockpitOrderTabPage.selectOrderStatusFromDropDownInOrderTab(TestConstants.ORDER_STATUS_DD_FIRST_VALUE);
		cscockpitOrderSearchTabPage.clickSearchBtn();
		s_assert.assertTrue(cscockpitOrderSearchTabPage.verifySectionsIsPresentInOrderSearchTab(firstName), "First name section is not present in order search tab");
		s_assert.assertTrue(cscockpitOrderSearchTabPage.verifySectionsIsPresentInOrderSearchTab(lastName), "Last name section is not present in order search tab");
		s_assert.assertTrue(cscockpitOrderSearchTabPage.verifySectionsIsPresentInOrderSearchTab(RFCID), "RFCID section is not present in order search tab");

		cscockpitOrderTabPage.selectOrderStatusFromDropDownInOrderTab(TestConstants.ORDER_STATUS_DD_SECOND_VALUE);
		cscockpitOrderSearchTabPage.clickSearchBtn();
		s_assert.assertTrue(cscockpitOrderSearchTabPage.verifySectionsIsPresentInOrderSearchTab(firstName), "First name section is not present in order search tab");
		s_assert.assertTrue(cscockpitOrderSearchTabPage.verifySectionsIsPresentInOrderSearchTab(lastName), "Last name section is not present in order search tab");
		s_assert.assertTrue(cscockpitOrderSearchTabPage.verifySectionsIsPresentInOrderSearchTab(RFCID), "RFCID section is not present in order search tab");

		cscockpitOrderTabPage.selectOrderStatusFromDropDownInOrderTab(TestConstants.ORDER_STATUS_DD_THIRD_VALUE);
		cscockpitOrderSearchTabPage.clickSearchBtn();
		s_assert.assertTrue(cscockpitOrderSearchTabPage.verifySectionsIsPresentInOrderSearchTab(firstName), "First name section is not present in order search tab");
		s_assert.assertTrue(cscockpitOrderSearchTabPage.verifySectionsIsPresentInOrderSearchTab(lastName), "Last name section is not present in order search tab");
		s_assert.assertTrue(cscockpitOrderSearchTabPage.verifySectionsIsPresentInOrderSearchTab(RFCID), "RFCID section is not present in order search tab");

		cscockpitOrderTabPage.selectOrderStatusFromDropDownInOrderTab(TestConstants.ORDER_STATUS_DD_FOURTH_VALUE);
		cscockpitOrderSearchTabPage.clickSearchBtn();
		s_assert.assertTrue(cscockpitOrderSearchTabPage.verifySectionsIsPresentInOrderSearchTab(firstName), "First name section is not present in order search tab");
		s_assert.assertTrue(cscockpitOrderSearchTabPage.verifySectionsIsPresentInOrderSearchTab(lastName), "Last name section is not present in order search tab");
		s_assert.assertTrue(cscockpitOrderSearchTabPage.verifySectionsIsPresentInOrderSearchTab(RFCID), "RFCID section is not present in order search tab");

		cscockpitOrderTabPage.selectOrderStatusFromDropDownInOrderTab(TestConstants.ORDER_STATUS_DD_VALUE);
		cscockpitOrderSearchTabPage.clickSearchBtn();
		s_assert.assertTrue(cscockpitOrderSearchTabPage.verifySectionsIsPresentInOrderSearchTab(firstName), "First name section is not present in order search tab");
		s_assert.assertTrue(cscockpitOrderSearchTabPage.verifySectionsIsPresentInOrderSearchTab(lastName), "Last name section is not present in order search tab");
		s_assert.assertTrue(cscockpitOrderSearchTabPage.verifySectionsIsPresentInOrderSearchTab(RFCID), "RFCID section is not present in order search tab");

		//select order status as all.
		cscockpitOrderTabPage.selectOrderStatusFromDropDownInOrderTab("All");

		//select country one by one and assert values.
		cscockpitCustomerSearchTabPage.selectCountryFromDropDownInCustomerSearchTab(TestConstants.COUNTRY_DD_VALUE_US);
		cscockpitCustomerSearchTabPage.clickSearchBtn();
		s_assert.assertTrue(cscockpitOrderSearchTabPage.verifySectionsIsPresentInOrderSearchTab(firstName), "First name section is not present in order search tab");
		s_assert.assertTrue(cscockpitOrderSearchTabPage.verifySectionsIsPresentInOrderSearchTab(lastName), "Last name section is not present in order search tab");
		s_assert.assertTrue(cscockpitOrderSearchTabPage.verifySectionsIsPresentInOrderSearchTab(RFCID), "RFCID section is not present in order search tab");

		cscockpitCustomerSearchTabPage.selectCountryFromDropDownInCustomerSearchTab(TestConstants.COUNTRY_DD_VALUE_CA);
		cscockpitCustomerSearchTabPage.clickSearchBtn();
		s_assert.assertTrue(cscockpitOrderSearchTabPage.verifySectionsIsPresentInOrderSearchTab(firstName), "First name section is not present in order search tab");
		s_assert.assertTrue(cscockpitOrderSearchTabPage.verifySectionsIsPresentInOrderSearchTab(lastName), "Last name section is not present in order search tab");
		s_assert.assertTrue(cscockpitOrderSearchTabPage.verifySectionsIsPresentInOrderSearchTab(RFCID), "RFCID section is not present in order search tab");
		s_assert.assertAll();
	}

	//Hybris Project-1716:Verify Create a Pulse Autoship
	@Test
	public void testCreateAPulseAutoship_1716() throws InterruptedException{
		String randomCustomerSequenceNumber = null;
		String consultantEmailID = null;
		RFO_DB = driver.getDBNameRFO();

		//-------------------FOR US----------------------------------
		driver.get(driver.getStoreFrontURL()+"/us");
		List<Map<String, Object>> randomConsultantList =  null;
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
		logger.info("login is successful");
		storeFrontConsultantPage.clickOnWelcomeDropDown();
		storeFrontAccountInfoPage = storeFrontConsultantPage.clickAccountInfoLinkPresentOnWelcomeDropDown();
		storeFrontAccountInfoPage.clickOnYourAccountDropdown();
		storeFrontAccountInfoPage.clickOnAutoShipStatus();
		storeFrontAccountInfoPage.clickOnCancelMyPulseSubscription();
		logout();
		driver.get(driver.getCSCockpitURL());		
		cscockpitCustomerSearchTabPage = cscockpitLoginPage.clickLoginBtn();
		cscockpitCustomerSearchTabPage.selectCustomerTypeFromDropDownInCustomerSearchTab("CONSULTANT");
		cscockpitCustomerSearchTabPage.selectCountryFromDropDownInCustomerSearchTab("United States");
		cscockpitCustomerSearchTabPage.selectAccountStatusFromDropDownInCustomerSearchTab("Active");
		cscockpitCustomerSearchTabPage.enterEmailIdInSearchFieldInCustomerSearchTab(consultantEmailID);
		cscockpitCustomerSearchTabPage.clickSearchBtn();
		randomCustomerSequenceNumber = String.valueOf(cscockpitCustomerSearchTabPage.getRandomCustomerFromSearchResult());
		cscockpitCustomerSearchTabPage.clickCIDNumberInCustomerSearchTab(randomCustomerSequenceNumber);
		cscockpitCustomerTabPage.clickCreatePulseTemplateBtn();
		cscockpitCustomerTabPage.clickCreatePulseTemplateBtnOnPopup();
		String nextDueDateFromCscockpit = cscockpitCustomerTabPage.getNextDueDateOfAutoshipTemplate();
		String nextDueDate = cscockpitCustomerTabPage.convertPulseTemplateDate(nextDueDateFromCscockpit);

		//verify from store front
		driver.get(driver.getStoreFrontURL()+"/us");
		storeFrontHomePage.loginAsConsultant(consultantEmailID, password);
		storeFrontConsultantPage.clickOnWelcomeDropDown();
		storeFrontAccountInfoPage = storeFrontConsultantPage.clickAccountInfoLinkPresentOnWelcomeDropDown();
		storeFrontAccountInfoPage.clickOnYourAccountDropdown();
		storeFrontAccountInfoPage.clickOnAutoShipStatus();
		String nextBillDate = storeFrontAccountInfoPage.getNextBillDateOfPulseTemplate();
		s_assert.assertTrue(storeFrontAccountInfoPage.getPulseStatusFromUI().contains("Enrolled"), "Expected pulse status is Enrolled But on UI"+storeFrontAccountInfoPage.getPulseStatusFromUI());
		s_assert.assertTrue(nextDueDate.contains(nextBillDate.trim()), "Expected next bill date of pulse template is "+nextDueDate+"actual on UI is "+nextBillDate);
		logout();

		//-------------------FOR US----------------------------------
		driver.get(driver.getStoreFrontURL()+"/ca");
		randomConsultantList =  null;
		while(true){
			randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_CONSULTANT_WITH_ORDERS_AND_AUTOSHIPS_RFO,"40"),RFO_DB);
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
		logger.info("login is successful");
		storeFrontConsultantPage.clickOnWelcomeDropDown();
		storeFrontAccountInfoPage = storeFrontConsultantPage.clickAccountInfoLinkPresentOnWelcomeDropDown();
		storeFrontAccountInfoPage.clickOnYourAccountDropdown();
		storeFrontAccountInfoPage.clickOnAutoShipStatus();
		storeFrontAccountInfoPage.clickOnCancelMyPulseSubscription();
		logout();
		driver.get(driver.getCSCockpitURL());		
		cscockpitCustomerSearchTabPage = cscockpitLoginPage.clickLoginBtn();
		cscockpitCustomerSearchTabPage.selectCustomerTypeFromDropDownInCustomerSearchTab("CONSULTANT");
		cscockpitCustomerSearchTabPage.selectCountryFromDropDownInCustomerSearchTab("Canada");
		cscockpitCustomerSearchTabPage.selectAccountStatusFromDropDownInCustomerSearchTab("Active");
		cscockpitCustomerSearchTabPage.enterEmailIdInSearchFieldInCustomerSearchTab(consultantEmailID);
		cscockpitCustomerSearchTabPage.clickSearchBtn();
		randomCustomerSequenceNumber = String.valueOf(cscockpitCustomerSearchTabPage.getRandomCustomerFromSearchResult());
		cscockpitCustomerSearchTabPage.clickCIDNumberInCustomerSearchTab(randomCustomerSequenceNumber);
		cscockpitCustomerTabPage.clickCreatePulseTemplateBtn();
		cscockpitCustomerTabPage.clickCreatePulseTemplateBtnOnPopup();
		nextDueDateFromCscockpit = cscockpitCustomerTabPage.getNextDueDateOfAutoshipTemplate();
		nextDueDate = cscockpitCustomerTabPage.convertPulseTemplateDate(nextDueDateFromCscockpit);

		//verify from store front
		driver.get(driver.getStoreFrontURL()+"/ca");
		storeFrontHomePage.loginAsConsultant(consultantEmailID, password);
		storeFrontConsultantPage.clickOnWelcomeDropDown();
		storeFrontAccountInfoPage = storeFrontConsultantPage.clickAccountInfoLinkPresentOnWelcomeDropDown();
		storeFrontAccountInfoPage.clickOnYourAccountDropdown();
		storeFrontAccountInfoPage.clickOnAutoShipStatus();
		nextBillDate = storeFrontAccountInfoPage.getNextBillDateOfPulseTemplate();
		s_assert.assertTrue(storeFrontAccountInfoPage.getPulseStatusFromUI().contains("Enrolled"), "Expected pulse status is Enrolled But on UI on CA"+storeFrontAccountInfoPage.getPulseStatusFromUI());
		s_assert.assertTrue(nextDueDate.contains(nextBillDate.trim()), "Expected next bill date of pulse template is on CA"+nextDueDate+"actual on UI is "+nextBillDate);
		s_assert.assertAll();
	}

	//Hybris Project-1717:To verify pulse Autoship template Page UI
	@Test(enabled=false) //regression tests
	public void testVerifyPulseAutoshipTemplatePageUI_1717() throws InterruptedException{
		String randomCustomerSequenceNumber = null;
		String consultantEmailID = null;
		String autoshipNumber=null;
		String orderSectionBasePrice="Base Price";
		String orderSectionAdjPrice="Adj Price";
		String orderSectionTotalCVPrice="Total CV";
		String orderSectionTotalQVPrice="Total QV";
		String orderSectionQuantity="Qty";
		String appliedPromotionDescription="Description";
		String appliedPromotionresult="Result";
		String orderNumberOfOrderFromAutoshipTemplate="Order #";
		String orderStatusOfOrderFromAutoshipTemplate="Order Status";
		String runDateOfOrderFromAutoshipTemplate="Run Date";
		String ShipDateOfOrderFromAutoshipTemplate="Ship Date";
		String orderTotalOfOrderFromAutoshipTemplate="Order Total";
		String failedReasonOfOrderFromAutoshipTemplate="Failed Reason";
		RFO_DB = driver.getDBNameRFO();

		//-------------------FOR US----------------------------------
		driver.get(driver.getStoreFrontURL()+"/us");
		List<Map<String, Object>> randomConsultantList =  null;
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
		logger.info("login is successful");
		logout();
		driver.get(driver.getCSCockpitURL());		
		cscockpitCustomerSearchTabPage = cscockpitLoginPage.clickLoginBtn();
		cscockpitCustomerSearchTabPage.selectCustomerTypeFromDropDownInCustomerSearchTab("CONSULTANT");
		cscockpitCustomerSearchTabPage.selectCountryFromDropDownInCustomerSearchTab("United States");
		cscockpitCustomerSearchTabPage.selectAccountStatusFromDropDownInCustomerSearchTab("Active");
		cscockpitCustomerSearchTabPage.enterEmailIdInSearchFieldInCustomerSearchTab(consultantEmailID);
		cscockpitCustomerSearchTabPage.clickSearchBtn();
		randomCustomerSequenceNumber = String.valueOf(cscockpitCustomerSearchTabPage.getRandomCustomerFromSearchResult());
		cscockpitCustomerSearchTabPage.clickCIDNumberInCustomerSearchTab(randomCustomerSequenceNumber);
		//Verify Different Section on Customer tab page.
		s_assert.assertTrue(cscockpitCustomerTabPage.verifyAutoshipTemplateSectionInCustomerTab(),"AutoShip Template section is not on Customer Tab Page");
		s_assert.assertTrue(cscockpitCustomerTabPage.verifyCustomerOrderSectionInCustomerTab(),"Customer Order section is not on Customer Tab Page");
		s_assert.assertTrue(cscockpitCustomerTabPage.verifyCustomerBillingInfoSectionInCustomerTab(),"Customer Billing Info section is not on Customer Tab Page");
		s_assert.assertTrue(cscockpitCustomerTabPage.verifyCustomerAddressSectionInCustomerTab(),"Customer Address section is not on Customer Tab Page");
		autoshipNumber=cscockpitCustomerTabPage.getAndClickPulseAutoshipIDHavingTypeAsPulseAutoshipTemplate();
		//Verify Different section on autoShip template tab Page.
		s_assert.assertTrue(cscockpitAutoshipTemplateTabPage.verifyAutoshipTemplateHeaderSectionInAutoshipTemplateTab(),"Autoship template header section is not present on Autoship template page.");
		s_assert.assertTrue(cscockpitAutoshipTemplateTabPage.verifyAppliedPromotionSectionInAutoshipTemplateTab(),"Applied Promotion section is not present on Autoship template page.");
		s_assert.assertTrue(cscockpitAutoshipTemplateTabPage.verifyShippingAddressSectionInAutoshipTemplateTab(),"Shipping Address section is not present on Autoship template page.");
		s_assert.assertTrue(cscockpitAutoshipTemplateTabPage.verifyPaymentInfoSectionInAutoshipTemplateTab(),"Payment info section is not present on Autoship template page.");
		s_assert.assertTrue(cscockpitAutoshipTemplateTabPage.verifyOrderFromThisAutoshipTemplateSectionInAutoshipTemplateTab(),"Order from this autoship template section is not present on Autoship template page.");
		//Verify Sub components of autoship template section in autoship template tab page.
		s_assert.assertTrue(cscockpitAutoshipTemplateTabPage.getAutoshipTemplateCIDFromAutoshipTemplateSectionInAutoshipTemplateTab().contains(autoshipNumber),"Autoship template header section Autoship id expected "+autoshipNumber+"While on UI"+cscockpitAutoshipTemplateTabPage.getAutoshipTemplateCIDFromAutoshipTemplateSectionInAutoshipTemplateTab());
		s_assert.assertFalse(cscockpitAutoshipTemplateTabPage.verifyCancelAutoshipTemplateLinkInAutoshipTemplateTab(),"Cancel autoship link in Autoship template header section is not present on Autoship template page.");
		//Verify Sub components of Order Detail section in autoship template tab page.
		s_assert.assertTrue(cscockpitAutoshipTemplateTabPage.verifyOrderDetailsInAutoshipTemplateTab(orderSectionBasePrice),"Order Detail Section Base Price Is not present.");
		s_assert.assertTrue(cscockpitAutoshipTemplateTabPage.verifyOrderDetailsInAutoshipTemplateTab(orderSectionAdjPrice),"Order Detail Section Adj Price Is not present.");
		s_assert.assertTrue(cscockpitAutoshipTemplateTabPage.verifyOrderDetailsInAutoshipTemplateTab(orderSectionTotalCVPrice),"Order Detail Section Total CV Is not present.");
		s_assert.assertTrue(cscockpitAutoshipTemplateTabPage.verifyOrderDetailsInAutoshipTemplateTab(orderSectionTotalQVPrice),"Order Detail Section Total QV Is not present.");
		s_assert.assertTrue(cscockpitAutoshipTemplateTabPage.verifyOrderDetailsInAutoshipTemplateTab(orderSectionQuantity),"Order Detail Section Quantity Is not present.");
		//Click + link to view product details.
		cscockpitAutoshipTemplateTabPage.clickPlusButtonNextToProductInAutoshipTemplateTab();
		s_assert.assertTrue(cscockpitAutoshipTemplateTabPage.verifyViewProductPageLinkInAutoshipTemplateTab(),"View Product Page Detail link is not present on autoship template page.");
		cscockpitAutoshipTemplateTabPage.clickViewProductPageLinkInAutoshipTemplateTemplateTab();
		cscockpitAutoshipTemplateTabPage.switchToSecondWindow();
		s_assert.assertTrue(cscockpitAutoshipTemplateTabPage.verifyProductDetailOfNewTabInAutoshipTemplateTab(),"View Product Page is not present after clicking view product page link in autoship template page.");
		cscockpitAutoshipTemplateTabPage.switchToPreviousTab();
		//click - link to collapse product details.
		cscockpitAutoshipTemplateTabPage.clickMinusButtonNextToProductInAutoshipTemplateTab();
		s_assert.assertTrue(cscockpitAutoshipTemplateTabPage.verifyPlusButtonNextToProductInAutoshipTemplateTab(),"Plus button next to product details does not appears after clicking Minus button");
		//Assert applied promotion details.
		//s_assert.assertTrue(cscockpitAutoshipTemplateTabPage.verifyAppliedPromotionsInAutoshipTemplateTab(appliedPromotionDescription),"Applied promotion description is not present");
		//s_assert.assertTrue(cscockpitAutoshipTemplateTabPage.verifyAppliedPromotionsInAutoshipTemplateTab(appliedPromotionresult),"Applied promotion results are not present");
		//Get Shipping Address Details from Database.
		//get Autoship Id From RFO
		List<Map<String, Object>> autoshipIdDetailsList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_AUTOSHIP_ID_FOR_RFO, autoshipNumber),RFO_DB);
		String autoshipID = String.valueOf(getValueFromQueryResult(autoshipIdDetailsList, "AutoshipID"));
		System.out.println("Autoship id "+autoshipID);
		List<Map<String,Object>> shippingAddressList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_SHIPPING_ADDRESS_FOR_AUTOSHIP_TEMPLATE_RFO, autoshipID), RFO_DB);
		String firstName = (String) getValueFromQueryResult(shippingAddressList, "FirstName");
		String lastName = (String) getValueFromQueryResult(shippingAddressList, "LastName");
		String addressLine1 = (String) getValueFromQueryResult(shippingAddressList, "Address1");
		String postalCode = (String) getValueFromQueryResult(shippingAddressList, "PostalCode");
		String locale = (String) getValueFromQueryResult(shippingAddressList, "Locale");
		String region = (String) getValueFromQueryResult(shippingAddressList, "Region");
		String country = String.valueOf(getValueFromQueryResult(shippingAddressList, "CountryID"));
		if(country.equals("40")){
			country = "canada"; 
		}else if(country.equals("236")){
			country = "United States";
		}
		String shippingAddressFromDB = firstName.trim()+" "+lastName.trim()+"\n"+ addressLine1+"\n"+locale+", "+region+" "+postalCode+"\n"+country.toUpperCase();
		shippingAddressFromDB = shippingAddressFromDB.trim().toLowerCase();
		logger.info("created Shipping Address is "+shippingAddressFromDB);
		//Assert Shipping address details.
		s_assert.assertTrue(cscockpitAutoshipTemplateTabPage.getShippingAddressNameInAutoshipTemplateTab().contains(firstName.trim()+" "+lastName.trim()),"Shipping Address Name Expected is "+firstName.trim()+" "+lastName.trim()+" While on UI"+cscockpitAutoshipTemplateTabPage.getShippingAddressNameInAutoshipTemplateTab());
		s_assert.assertTrue(cscockpitAutoshipTemplateTabPage.getShippingAddressLine1InAutoshipTemplateTab().contains(addressLine1),"Shipping Address Line 1 Expected is "+addressLine1+" While on UI"+cscockpitAutoshipTemplateTabPage.getShippingAddressLine1InAutoshipTemplateTab());
		s_assert.assertTrue(cscockpitAutoshipTemplateTabPage.getShippingAddressLocaleRegionPostCodeInAutoshipTemplateTab().contains(locale),"Shipping Address Locale Expected is "+locale+" While on UI"+cscockpitAutoshipTemplateTabPage.getShippingAddressLocaleRegionPostCodeInAutoshipTemplateTab());
		s_assert.assertTrue(cscockpitAutoshipTemplateTabPage.getShippingAddressLocaleRegionPostCodeInAutoshipTemplateTab().contains(region),"Shipping Address Region Expected is "+region+" While on UI"+cscockpitAutoshipTemplateTabPage.getShippingAddressLocaleRegionPostCodeInAutoshipTemplateTab());
		s_assert.assertTrue(cscockpitAutoshipTemplateTabPage.getShippingAddressLocaleRegionPostCodeInAutoshipTemplateTab().contains(postalCode),"Shipping Address PostCode Expected is "+postalCode+" While on UI"+cscockpitAutoshipTemplateTabPage.getShippingAddressLocaleRegionPostCodeInAutoshipTemplateTab());
		s_assert.assertTrue(cscockpitAutoshipTemplateTabPage.getShippingAddressCountryInAutoshipTemplateTab().contains(country),"Shipping Address Country Expected is "+country+" While on UI"+cscockpitAutoshipTemplateTabPage.getShippingAddressCountryInAutoshipTemplateTab());
		//Assert Billing address details.
		s_assert.assertTrue(cscockpitAutoshipTemplateTabPage.getPaymentAddressNameInAutoshipTemplateTab().contains(firstName.trim()+" "+lastName.trim()),"Payment Address Name Expected is "+firstName.trim()+" "+lastName.trim()+" While on UI"+cscockpitAutoshipTemplateTabPage.getPaymentAddressNameInAutoshipTemplateTab());
		//verify components of order from AutoShip Template section
		s_assert.assertTrue(cscockpitAutoshipTemplateTabPage.getTextOfOrderFromAutoshipTemplateInAutoshipTemplateTab().contains("Number of Consecutive Autoship Orders From Template"),"Text Of order from autoship template Expected is= Number of Consecutive Autoship Orders From Template While on UI="+cscockpitAutoshipTemplateTabPage.getTextOfOrderFromAutoshipTemplateInAutoshipTemplateTab());

		//verify sub components of order from AutoShip Template section
		s_assert.assertTrue(cscockpitAutoshipTemplateTabPage.verifySectionsOfOrderFromAutoshipTemplateInAutoshipTemplateTab(orderNumberOfOrderFromAutoshipTemplate),"Order Number is not present in order from autoship template On autoship template page.");
		s_assert.assertTrue(cscockpitAutoshipTemplateTabPage.verifySectionsOfOrderFromAutoshipTemplateInAutoshipTemplateTab(orderStatusOfOrderFromAutoshipTemplate),"Order Status is not present in order from autoship template On autoship template page.");
		s_assert.assertTrue(cscockpitAutoshipTemplateTabPage.verifySectionsOfOrderFromAutoshipTemplateInAutoshipTemplateTab(runDateOfOrderFromAutoshipTemplate),"Run date is not present in order from autoship template On autoship template page.");
		s_assert.assertTrue(cscockpitAutoshipTemplateTabPage.verifySectionsOfOrderFromAutoshipTemplateInAutoshipTemplateTab(ShipDateOfOrderFromAutoshipTemplate),"Ship Date is not present in order from autoship template On autoship template page.");
		s_assert.assertTrue(cscockpitAutoshipTemplateTabPage.verifySectionsOfOrderFromAutoshipTemplateInAutoshipTemplateTab(orderTotalOfOrderFromAutoshipTemplate),"Order Total is not present in order from autoship template On autoship template page.");
		s_assert.assertTrue(cscockpitAutoshipTemplateTabPage.verifySectionsOfOrderFromAutoshipTemplateInAutoshipTemplateTab(failedReasonOfOrderFromAutoshipTemplate),"Failed Reason is not present in order from autoship template On autoship template page.");

		//assert for pulse cancelled users
		driver.get(driver.getStoreFrontURL()+"/us");
		storeFrontHomePage.loginAsConsultant(consultantEmailID, password);
		storeFrontConsultantPage.clickOnWelcomeDropDown();
		storeFrontAccountInfoPage = storeFrontConsultantPage.clickAccountInfoLinkPresentOnWelcomeDropDown();
		storeFrontAccountInfoPage.clickOnYourAccountDropdown();
		storeFrontAccountInfoPage.clickOnAutoShipStatus();
		storeFrontAccountInfoPage.clickOnCancelMyPulseSubscription();
		logout();
		driver.get(driver.getCSCockpitURL());		
		cscockpitCustomerSearchTabPage = cscockpitLoginPage.clickLoginBtn();
		cscockpitCustomerSearchTabPage.selectCustomerTypeFromDropDownInCustomerSearchTab("CONSULTANT");
		cscockpitCustomerSearchTabPage.selectCountryFromDropDownInCustomerSearchTab("United States");
		cscockpitCustomerSearchTabPage.selectAccountStatusFromDropDownInCustomerSearchTab("Active");
		cscockpitCustomerSearchTabPage.enterEmailIdInSearchFieldInCustomerSearchTab(consultantEmailID);
		cscockpitCustomerSearchTabPage.clickSearchBtn();
		randomCustomerSequenceNumber = String.valueOf(cscockpitCustomerSearchTabPage.getRandomCustomerFromSearchResult());
		cscockpitCustomerSearchTabPage.clickCIDNumberInCustomerSearchTab(randomCustomerSequenceNumber);
		cscockpitCustomerTabPage.getAndClickPulseAutoshipIDHavingTypeAsPulseAutoshipTemplate();
		s_assert.assertTrue(cscockpitAutoshipTemplateTabPage.getAutoShipTemplateStatus().contains("CANCELLED"), "Expected pulse autoship template status is CANCELLED but actual on UI "+cscockpitAutoshipTemplateTabPage.getAutoShipTemplateStatus());
		s_assert.assertTrue(cscockpitAutoshipTemplateTabPage.verifyUpdateButtonIsDisabledAfterCancelPulseSubscription(), "Update button is enabled after cancel pulse subscription");
		s_assert.assertTrue(cscockpitAutoshipTemplateTabPage.verifyRunNowButtonIsDisabledAfterCancelPulseSubscription(), "Run now button is enabled after cancel pulse subscription");
		s_assert.assertAll();
	}

	//Hybris Project-4172:Verify committing tax for order having single line item.
	@Test
	public void testVerifyCommittingTaxForAdhocOrder_4172() throws InterruptedException{
		int randomNum = CommonUtils.getRandomNum(10000, 1000000);
		RFO_DB = driver.getDBNameRFO();
		List<Map<String, Object>> randomConsultantList =  null;
		String consultantEmailID = null;
		String newBillingProfileName = TestConstants.NEW_BILLING_PROFILE_NAME_US+randomNum;
		String lastName = "lN";
		String accountId = null;

		//-------------------FOR US----------------------------------
		driver.get(driver.getStoreFrontURL()+"/us");
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
		logger.info("login is successful");
		//Place an Adhoc Order
		storeFrontConsultantPage.hoverOnShopLinkAndClickAllProductsLinksAfterLogin();
		storeFrontUpdateCartPage.clickAddToBagButtonWithoutFilterAfterLogin();
		storeFrontUpdateCartPage.clickOnCheckoutButton();
		storeFrontUpdateCartPage.clickOnShippingAddressNextStepBtn();
		storeFrontUpdateCartPage.clickOnBillingNextStepBtn();
		storeFrontUpdateCartPage.clickPlaceOrderBtn();
		String orderNumber = storeFrontUpdateCartPage.getOrderNumberAfterPlaceOrder();
		s_assert.assertTrue(storeFrontUpdateCartPage.verifyOrderPlacedConfirmationMessage(), "Order has been not placed successfully");
		storeFrontConsultantPage = storeFrontUpdateCartPage.clickRodanAndFieldsLogo();
		storeFrontConsultantPage.clickOnWelcomeDropDown();
		storeFrontOrdersPage = storeFrontConsultantPage.clickOrdersLinkPresentOnWelcomeDropDown();
		String firstAdhocOrder=storeFrontOrdersPage.getFirstOrderNumberFromOrderHistory();
		s_assert.assertTrue(firstAdhocOrder.equalsIgnoreCase(orderNumber), "Placed Adhoc order is not present in order history");
		logout();
		driver.get(driver.getCSCockpitURL()); 
		cscockpitCustomerSearchTabPage = cscockpitLoginPage.clickLoginBtn();
		cscockpitCustomerSearchTabPage.clickCommitTaxTab();
		cscockpitCommitTaxTabPage.enterOrderNumberInCommitTaxTab(orderNumber);
		s_assert.assertTrue(cscockpitCommitTaxTabPage.verifyConfirmationPopup(), "Confirmation Popup For tax on this order number is not present.");
		cscockpitCommitTaxTabPage.clickConfirmationPopupOkayInCommitTaxTab();
		String commitTaxId=cscockpitCommitTaxTabPage.getCommitTaxIdOfOrderInCommitTaxTab();
		cscockpitCommitTaxTabPage.clickSuccessfulCommittedTaxPopupOkay();
		cscockpitCustomerSearchTabPage.clickOrderSearchTab();
		cscockpitOrderSearchTabPage.enterOrderNumberInOrderSearchTab(orderNumber);
		cscockpitOrderSearchTabPage.clickSearchBtn();
		cscockpitOrderSearchTabPage.clickOrderLinkOnOrderSearchTabAndVerifyOrderDetailsPage(orderNumber);
		s_assert.assertTrue(cscockpitOrderTabPage.verifyTaxCommittedEntryInOrderTab(orderNumber),"Tax committed entry is not present in order");
		cscockpitOrderSearchTabPage.clickMenuButton();
		cscockpitOrderSearchTabPage.clickLogoutButton();

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
		logger.info("login is successful");
		//Place an Adhoc Order
		storeFrontConsultantPage.hoverOnShopLinkAndClickAllProductsLinksAfterLogin();
		storeFrontUpdateCartPage.clickAddToBagButtonWithoutFilterAfterLogin();
		storeFrontUpdateCartPage.clickOnCheckoutButton();
		storeFrontUpdateCartPage.clickOnShippingAddressNextStepBtn();
		storeFrontUpdateCartPage.clickOnBillingNextStepBtn();
		storeFrontUpdateCartPage.clickPlaceOrderBtn();
		orderNumber = storeFrontUpdateCartPage.getOrderNumberAfterPlaceOrder();
		s_assert.assertTrue(storeFrontUpdateCartPage.verifyOrderPlacedConfirmationMessage(), "Order has been not placed successfully");
		storeFrontConsultantPage = storeFrontUpdateCartPage.clickRodanAndFieldsLogo();
		storeFrontConsultantPage.clickOnWelcomeDropDown();
		storeFrontOrdersPage = storeFrontConsultantPage.clickOrdersLinkPresentOnWelcomeDropDown();
		firstAdhocOrder=storeFrontOrdersPage.getFirstOrderNumberFromOrderHistory();
		s_assert.assertTrue(firstAdhocOrder.equalsIgnoreCase(orderNumber), "Placed Adhoc order is not present in order history");
		logout();
		driver.get(driver.getCSCockpitURL()); 
		cscockpitCustomerSearchTabPage = cscockpitLoginPage.clickLoginBtn();
		cscockpitCustomerSearchTabPage.clickCommitTaxTab();
		cscockpitCommitTaxTabPage.enterOrderNumberInCommitTaxTab(orderNumber);
		s_assert.assertTrue(cscockpitCommitTaxTabPage.verifyConfirmationPopup(), "Confirmation Popup For tax on this order number is not present.");
		cscockpitCommitTaxTabPage.clickConfirmationPopupOkayInCommitTaxTab();
		commitTaxId=cscockpitCommitTaxTabPage.getCommitTaxIdOfOrderInCommitTaxTab();
		cscockpitCommitTaxTabPage.clickSuccessfulCommittedTaxPopupOkay();
		cscockpitCustomerSearchTabPage.clickOrderSearchTab();
		cscockpitOrderSearchTabPage.enterOrderNumberInOrderSearchTab(orderNumber);
		cscockpitOrderSearchTabPage.clickSearchBtn();
		cscockpitOrderSearchTabPage.clickOrderLinkOnOrderSearchTabAndVerifyOrderDetailsPage(orderNumber);
		s_assert.assertTrue(cscockpitOrderTabPage.verifyTaxCommittedEntryInOrderTab(orderNumber),"Tax committed entry is not present in order");
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
