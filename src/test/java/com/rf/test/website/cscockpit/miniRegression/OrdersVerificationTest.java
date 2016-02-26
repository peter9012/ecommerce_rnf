package com.rf.test.website.cscockpit.miniRegression;

import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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
	private CSCockpitAutoshipTemplateUpdateTabPage cscockpitAutoshipTemplateUpdateTabPage;
	private CSCockpitAutoshipCartTabPage cscockpitAutoshipCartTabPage;

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
		storeFrontUpdateCartPage = new StoreFrontUpdateCartPage(driver);
		cscockpitAutoshipTemplateUpdateTabPage = new CSCockpitAutoshipTemplateUpdateTabPage(driver);
		cscockpitAutoshipCartTabPage = new CSCockpitAutoshipCartTabPage(driver);
	}

	private String RFO_DB = null;

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
		String pstDate = cscockpitCheckoutTabPage.getPSTDate();
		String orderDate = cscockpitCheckoutTabPage.converPSTDateToUIFormat(pstDate);
		s_assert.assertTrue(cscockpitCheckoutTabPage.convertUIDateFormatToPSTFormat(orderNotevalueFromUI.split("\\ ")[0]).trim().contains(cscockpitCheckoutTabPage.getPSTDate()) || cscockpitCheckoutTabPage.convertUIDateFormatToPSTFormat(orderNotevalueFromUI.split("\\ ")[0]).trim().contains(orderDate),"CSCockpit added order note date in checkout tab expected"+cscockpitCheckoutTabPage.getPSTDate()+"and on UI" +cscockpitCheckoutTabPage.convertUIDateFormatToPSTFormat(orderNotevalueFromUI.split("\\ ")[0]).trim());
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
		pstDate = cscockpitCheckoutTabPage.getPSTDate();
		orderDate = cscockpitCheckoutTabPage.converPSTDateToUIFormat(pstDate);
		s_assert.assertTrue(cscockpitCheckoutTabPage.convertUIDateFormatToPSTFormat(orderNotevalueFromUI.split("\\ ")[0]).trim().contains(cscockpitCheckoutTabPage.getPSTDate()) || cscockpitCheckoutTabPage.convertUIDateFormatToPSTFormat(orderNotevalueFromUI.split("\\ ")[0]).trim().contains(orderDate),"CSCockpit added order note date in checkout tab expected"+cscockpitCheckoutTabPage.getPSTDate()+"and on UI" +cscockpitCheckoutTabPage.convertUIDateFormatToPSTFormat(orderNotevalueFromUI.split("\\ ")[0]).trim());
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
				logger.info("login error for the user "+pcUserEmailID);
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
		String pstDate = cscockpitCheckoutTabPage.getPSTDate();
		String orderDate = cscockpitCheckoutTabPage.converPSTDateToUIFormat(pstDate);
		s_assert.assertTrue(cscockpitCheckoutTabPage.convertUIDateFormatToPSTFormat(orderNotevalueFromUI.split("\\ ")[0]).trim().contains(cscockpitCheckoutTabPage.getPSTDate()) || cscockpitCheckoutTabPage.convertUIDateFormatToPSTFormat(orderNotevalueFromUI.split("\\ ")[0]).trim().contains(orderDate),"CSCockpit added order note date in checkout tab expected"+cscockpitCheckoutTabPage.getPSTDate()+"and on UI" +cscockpitCheckoutTabPage.convertUIDateFormatToPSTFormat(orderNotevalueFromUI.split("\\ ")[0]).trim());
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
		pstDate = cscockpitCheckoutTabPage.getPSTDate();
		orderDate = cscockpitCheckoutTabPage.converPSTDateToUIFormat(pstDate);
		s_assert.assertTrue(cscockpitCheckoutTabPage.convertUIDateFormatToPSTFormat(orderNotevalueFromUI.split("\\ ")[0]).trim().contains(cscockpitCheckoutTabPage.getPSTDate()) || cscockpitCheckoutTabPage.convertUIDateFormatToPSTFormat(orderNotevalueFromUI.split("\\ ")[0]).trim().contains(orderDate),"CSCockpit added order note date in checkout tab expected"+cscockpitCheckoutTabPage.getPSTDate()+"and on UI" +cscockpitCheckoutTabPage.convertUIDateFormatToPSTFormat(orderNotevalueFromUI.split("\\ ")[0]).trim());
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

	// Hybris Project-1733:To verify create payment address functionality in the Checkout Page
	@Test
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
		orderNumber=cscockpitCustomerTabPage.clickAndGetOrderNumberInCustomerTab();
		logger.info("clicked order number 1 is "+orderNumber);
		cscockpitCustomerTabPage.clickPlaceOrderButtonInCustomerTab();
		cscockpitCartTabPage.selectValueFromSortByDDInCartTab("Price: High to Low");
		cscockpitCartTabPage.selectCatalogFromDropDownInCartTab();
		randomProductSequenceNumber = String.valueOf(cscockpitCartTabPage.getRandomProductWithSKUFromSearchResult()); 
		SKUValue = cscockpitCartTabPage.getCustomerSKUValueInCartTab(randomProductSequenceNumber);
		cscockpitCartTabPage.searchSKUValueInCartTab(SKUValue);
		cscockpitCartTabPage.clickAddToCartBtnInCartTab();
		cscockpitCartTabPage.clickCheckoutBtnInCartTab();
		cscockpitCheckoutTabPage.clickAddNewPaymentAddressInCheckoutTab();
		cscockpitCheckoutTabPage.clickAddNewAddressLinkInPopUpInCheckoutTab();
		s_assert.assertTrue(cscockpitCheckoutTabPage.verifyAddressTextBoxInPopUpInCheckoutTab(),"Address Line 1 text box is not present in popup at checkout tab");
		s_assert.assertTrue(cscockpitCheckoutTabPage.verifyPostalCodeTextBoxInPopUpInCheckoutTab(),"Postal code text box is not present in popup at checkout tab");
		cscockpitCheckoutTabPage.clickCloseOfPaymentAddressPopUpInCheckoutTab();
		cscockpitCheckoutTabPage.clickAddNewPaymentAddressInCheckoutTab();
		// cscockpitCheckoutTabPage.clickAddNewAddressLinkInPopUpInCheckoutTab();
		cscockpitCheckoutTabPage.clickSaveOfShippingAddressPopUpInCheckoutTab();
		s_assert.assertTrue(cscockpitCheckoutTabPage.getErrorMessageOfPopupWithoutFillingDataInCheckoutTab().contains("Please review credit card information entered"),"CSCockpit checkout tab popup error message expected = Please review credit card information entered and on UI = " +cscockpitCheckoutTabPage.getErrorMessageOfPopupWithoutFillingDataInCheckoutTab());
		cscockpitCheckoutTabPage.enterPaymentDetailsInPopUpInCheckoutTab(cardNumber, newBillingProfileName,securityCode,"09","2023","VISA");
		cscockpitCheckoutTabPage.enterShippingDetailsInPopUpInCheckoutTab(attendeeFirstName,attendeeLastName,addressLine,city,postalCode,Country,Province,phoneNumber);
		cscockpitCheckoutTabPage.clickSaveOfShippingAddressPopUpInCheckoutTab();
		s_assert.assertTrue(cscockpitCheckoutTabPage.getNewBillingAddressNameInCheckoutTab().contains(attendeeFirstName),"CSCockpit checkout tab newly created billing address name expected ="+ attendeeFirstName+ "and on UI = " +cscockpitCheckoutTabPage.getNewBillingAddressNameInCheckoutTab());
		cscockpitCheckoutTabPage.enterCVVValueInCheckoutTab(TestConstants.SECURITY_CODE);
		cscockpitCheckoutTabPage.clickUseThisCardBtnInCheckoutTab();
		cscockpitCheckoutTabPage.clickPlaceOrderButtonInCheckoutTab();
		orderNumberOfOrderTab = cscockpitOrderTabPage.getOrderNumberInOrderTab();
		logger.info("order number 2 in order tab is "+orderNumberOfOrderTab);
		cscockpitOrderTabPage.clickCustomerTab();
		s_assert.assertTrue(cscockpitCustomerTabPage.getOrderTypeInCustomerTab(orderNumberOfOrderTab.split("\\-")[0].trim()).contains("Consultant Order"),"CSCockpit Customer tab Order type expected = Consultant Order and on UI = " +cscockpitCustomerTabPage.getOrderTypeInCustomerTab(orderNumberOfOrderTab.split("\\-")[0].trim()));
		driver.get(driver.getStoreFrontURL()+"/us");
		storeFrontConsultantPage = storeFrontHomePage.loginAsConsultant(consultantEmailID, password);
		storeFrontConsultantPage.clickOnWelcomeDropDown();
		storeFrontOrdersPage =  storeFrontConsultantPage.clickOrdersLinkPresentOnWelcomeDropDown();
		s_assert.assertTrue(storeFrontOrdersPage.verifyOrdersPageIsDisplayed(),"Orders page has not been displayed");
		orderHistoryNumber = storeFrontOrdersPage.getFirstOrderNumberFromOrderHistory();
		logger.info("order number 3 from order history is "+orderHistoryNumber);
		s_assert.assertTrue(orderHistoryNumber.contains(orderNumberOfOrderTab.split("\\-")[0].trim()),"CSCockpit Order number expected = "+orderNumberOfOrderTab.split("\\-")[0].trim()+" and on UI = " +orderHistoryNumber);
		logout();
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
		driver.get(driver.getCSCockpitURL());

		cscockpitCustomerSearchTabPage = cscockpitLoginPage.clickLoginBtn();
		cscockpitCustomerSearchTabPage.selectCustomerTypeFromDropDownInCustomerSearchTab("CONSULTANT");
		cscockpitCustomerSearchTabPage.selectCountryFromDropDownInCustomerSearchTab("Canada");
		cscockpitCustomerSearchTabPage.selectAccountStatusFromDropDownInCustomerSearchTab("Active");
		cscockpitCustomerSearchTabPage.enterEmailIdInSearchFieldInCustomerSearchTab(consultantEmailID);
		cscockpitCustomerSearchTabPage.clickSearchBtn();
		randomCustomerSequenceNumber = String.valueOf(cscockpitCustomerSearchTabPage.getRandomCustomerFromSearchResult());
		cscockpitCustomerSearchTabPage.clickCIDNumberInCustomerSearchTab(randomCustomerSequenceNumber);
		orderNumber=cscockpitCustomerTabPage.clickAndGetOrderNumberInCustomerTab();
		cscockpitCustomerTabPage.clickPlaceOrderButtonInCustomerTab();
		cscockpitCartTabPage.selectValueFromSortByDDInCartTab("Price: High to Low");
		cscockpitCartTabPage.selectCatalogFromDropDownInCartTab();
		randomProductSequenceNumber = String.valueOf(cscockpitCartTabPage.getRandomProductWithSKUFromSearchResult()); 
		SKUValue = cscockpitCartTabPage.getCustomerSKUValueInCartTab(randomProductSequenceNumber);
		cscockpitCartTabPage.searchSKUValueInCartTab(SKUValue);
		cscockpitCartTabPage.clickAddToCartBtnInCartTab();
		cscockpitCartTabPage.clickCheckoutBtnInCartTab();
		cscockpitCheckoutTabPage.clickAddNewPaymentAddressInCheckoutTab();
		cscockpitCheckoutTabPage.clickAddNewAddressLinkInPopUpInCheckoutTab();
		s_assert.assertTrue(cscockpitCheckoutTabPage.verifyAddressTextBoxInPopUpInCheckoutTab(),"Address Line 1 text box is not present in popup at checkout tab");
		s_assert.assertTrue(cscockpitCheckoutTabPage.verifyPostalCodeTextBoxInPopUpInCheckoutTab(),"Postal code text box is not present in popup at checkout tab");
		cscockpitCheckoutTabPage.clickCloseOfPaymentAddressPopUpInCheckoutTab();
		cscockpitCheckoutTabPage.clickAddNewPaymentAddressInCheckoutTab();
		// cscockpitCheckoutTabPage.clickAddNewAddressLinkInPopUpInCheckoutTab();
		cscockpitCheckoutTabPage.clickSaveOfShippingAddressPopUpInCheckoutTab();
		s_assert.assertTrue(cscockpitCheckoutTabPage.getErrorMessageOfPopupWithoutFillingDataInCheckoutTab().contains("Please review credit card information entered"),"CSCockpit checkout tab popup error message expected = Please review credit card information entered and on UI = " +cscockpitCheckoutTabPage.getErrorMessageOfPopupWithoutFillingDataInCheckoutTab());
		cscockpitCheckoutTabPage.enterPaymentDetailsInPopUpInCheckoutTab(cardNumber, newBillingProfileName,securityCode,"09","2023","VISA");
		cscockpitCheckoutTabPage.enterShippingDetailsInPopUpInCheckoutTab(attendeeFirstName,attendeeLastName,addressLine,city,postalCode,Country,Province,phoneNumber);
		cscockpitCheckoutTabPage.clickSaveOfShippingAddressPopUpInCheckoutTab();
		s_assert.assertTrue(cscockpitCheckoutTabPage.getNewBillingAddressNameInCheckoutTab().contains(attendeeFirstName),"CSCockpit checkout tab newly created billing address name expected ="+ attendeeFirstName+ "and on UI = " +cscockpitCheckoutTabPage.getNewBillingAddressNameInCheckoutTab());
		cscockpitCheckoutTabPage.enterCVVValueInCheckoutTab(TestConstants.SECURITY_CODE);
		cscockpitCheckoutTabPage.clickUseThisCardBtnInCheckoutTab();
		cscockpitCheckoutTabPage.clickPlaceOrderButtonInCheckoutTab();
		orderNumberOfOrderTab = cscockpitOrderTabPage.getOrderNumberInOrderTab();
		cscockpitOrderTabPage.clickCustomerTab();
		s_assert.assertTrue(cscockpitCustomerTabPage.getOrderTypeInCustomerTab(orderNumberOfOrderTab.split("\\-")[0].trim()).contains("Consultant Order"),"CSCockpit Customer tab Order type expected = Consultant Order and on UI = " +cscockpitCustomerTabPage.getOrderTypeInCustomerTab(orderNumberOfOrderTab.split("\\-")[0].trim()));
		driver.get(driver.getStoreFrontURL()+"/us");
		storeFrontConsultantPage = storeFrontHomePage.loginAsConsultant(consultantEmailID, password);
		storeFrontConsultantPage.clickOnWelcomeDropDown();
		storeFrontOrdersPage =  storeFrontConsultantPage.clickOrdersLinkPresentOnWelcomeDropDown();
		s_assert.assertTrue(storeFrontOrdersPage.verifyOrdersPageIsDisplayed(),"Orders page has not been displayed");
		orderHistoryNumber = storeFrontOrdersPage.getFirstOrderNumberFromOrderHistory();
		s_assert.assertTrue(orderHistoryNumber.contains(orderNumberOfOrderTab.split("\\-")[0].trim()),"CSCockpit Order number expected = "+orderNumberOfOrderTab.split("\\-")[0].trim()+" and on UI = " +orderHistoryNumber);
		logout();
		s_assert.assertAll();
	}

	//Hybris Project-1734:To verify create delivery address functionality in the Checkout Page
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
		String orderNumberOfOrderTab=null;
		String orderHistoryNumber=null;
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
		cscockpitCheckoutTabPage.clickAddNewAddressUnderDeliveryAddressInCheckoutTab();
		s_assert.assertTrue(cscockpitCheckoutTabPage.verifyAddressTextBoxInPopUpInCheckoutTab(),"Address Line 1 text box is not present in popup at checkout tab");
		s_assert.assertTrue(cscockpitCheckoutTabPage.verifyPostalCodeTextBoxInPopUpInCheckoutTab(),"Postal code text box is not present in popup at checkout tab");
		cscockpitCheckoutTabPage.clickCloseOfDeliveryAddressPopUpInCheckoutTab();
		cscockpitCheckoutTabPage.clickAddNewAddressUnderDeliveryAddressInCheckoutTab();
		cscockpitCheckoutTabPage.clickSaveOfDeliveryAddressPopUpInCheckoutTab();
		s_assert.assertTrue(cscockpitCheckoutTabPage.getErrorMessageOfDeliveryAddressPopupWithoutFillingDataInCheckoutTab().contains("Attention should contain First Name and Last Name"),"CSCockpit checkout tab popup error message expected = Attention should contain First Name and Last Name and on UI = " +cscockpitCheckoutTabPage.getErrorMessageOfDeliveryAddressPopupWithoutFillingDataInCheckoutTab());
		cscockpitCheckoutTabPage.clickOKOfDeliveryAddressPopupInCheckoutTab();
		cscockpitCheckoutTabPage.enterShippingDetailsInPopUpInCheckoutTab(attendeeFirstName,attendeeLastName,addressLine,city,postalCode,Country,Province,phoneNumber);
		cscockpitCheckoutTabPage.clickSaveOfDeliveryAddressPopUpInCheckoutTab();
		cscockpitCheckoutTabPage.clickUseAsEnteredPopupOkayInCheckoutTab();
		s_assert.assertTrue(cscockpitCheckoutTabPage.getNewlyCreatedDeliveryAddressNameInCheckoutTab().contains(attendeeFirstName),"CSCockpit checkout tab newly created Delivery address name expected ="+ attendeeFirstName+ "and on UI = " +cscockpitCheckoutTabPage.getNewlyCreatedDeliveryAddressNameInCheckoutTab());
		cscockpitCheckoutTabPage.clickAddNewAddressUnderDeliveryAddressInCheckoutTab();
		cscockpitCheckoutTabPage.enterShippingDetailsInPopUpInCheckoutTab(attendeeNewFirstName,attendeeLastName,newAddressLine,newCity,newPostalCode,Country,newProvince,phoneNumber);
		cscockpitCheckoutTabPage.clickSaveOfDeliveryAddressPopUpInCheckoutTab();
		cscockpitCheckoutTabPage.clickUseAsEnteredPopupOkayInCheckoutTab();
		s_assert.assertTrue(cscockpitCheckoutTabPage.getNewlyCreatedDeliveryAddressNameInCheckoutTab().contains(attendeeNewFirstName),"CSCockpit checkout tab newly created Delivery address name expected ="+ attendeeFirstName+ "and on UI = " +cscockpitCheckoutTabPage.getNewlyCreatedDeliveryAddressNameInCheckoutTab());
		cscockpitCheckoutTabPage.enterCVVValueInCheckoutTab(TestConstants.SECURITY_CODE);
		cscockpitCheckoutTabPage.clickUseThisCardBtnInCheckoutTab();
		cscockpitCheckoutTabPage.clickPlaceOrderButtonInCheckoutTab();
		orderNumberOfOrderTab = cscockpitOrderTabPage.getOrderNumberInOrderTab();
		cscockpitOrderTabPage.clickCustomerTab();
		s_assert.assertTrue(cscockpitCustomerTabPage.getOrderTypeInCustomerTab(orderNumberOfOrderTab.split("\\-")[0].trim()).contains("Consultant Order"),"CSCockpit Customer tab Order type expected = Consultant Order and on UI = " +cscockpitCustomerTabPage.getOrderTypeInCustomerTab(orderNumberOfOrderTab.split("\\-")[0].trim()));
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
		cscockpitCheckoutTabPage.clickAddNewAddressUnderDeliveryAddressInCheckoutTab();
		s_assert.assertTrue(cscockpitCheckoutTabPage.verifyAddressTextBoxInPopUpInCheckoutTab(),"Address Line 1 text box is not present in popup at checkout tab");
		s_assert.assertTrue(cscockpitCheckoutTabPage.verifyPostalCodeTextBoxInPopUpInCheckoutTab(),"Postal code text box is not present in popup at checkout tab");
		cscockpitCheckoutTabPage.clickCloseOfDeliveryAddressPopUpInCheckoutTab();
		cscockpitCheckoutTabPage.clickAddNewAddressUnderDeliveryAddressInCheckoutTab();
		cscockpitCheckoutTabPage.clickSaveOfDeliveryAddressPopUpInCheckoutTab();
		s_assert.assertTrue(cscockpitCheckoutTabPage.getErrorMessageOfDeliveryAddressPopupWithoutFillingDataInCheckoutTab().contains("Attention should contain First Name and Last Name"),"CSCockpit checkout tab popup error message expected = Attention should contain First Name and Last Name and on UI = " +cscockpitCheckoutTabPage.getErrorMessageOfDeliveryAddressPopupWithoutFillingDataInCheckoutTab());
		cscockpitCheckoutTabPage.clickOKOfDeliveryAddressPopupInCheckoutTab();
		cscockpitCheckoutTabPage.enterShippingDetailsInPopUpInCheckoutTab(attendeeFirstName,attendeeLastName,addressLine,city,postalCode,Country,Province,phoneNumber);
		cscockpitCheckoutTabPage.clickSaveOfDeliveryAddressPopUpInCheckoutTab();
		cscockpitCheckoutTabPage.clickUseAsEnteredPopupOkayInCheckoutTab();
		s_assert.assertTrue(cscockpitCheckoutTabPage.getNewlyCreatedDeliveryAddressNameInCheckoutTab().contains(attendeeFirstName),"CSCockpit checkout tab newly created Delivery address name expected ="+ attendeeFirstName+ "and on UI = " +cscockpitCheckoutTabPage.getNewlyCreatedDeliveryAddressNameInCheckoutTab());
		cscockpitCheckoutTabPage.clickAddNewAddressUnderDeliveryAddressInCheckoutTab();
		cscockpitCheckoutTabPage.enterShippingDetailsInPopUpInCheckoutTab(attendeeNewFirstName,attendeeLastName,newAddressLine,newCity,newPostalCode,Country,newProvince,phoneNumber);
		cscockpitCheckoutTabPage.clickSaveOfDeliveryAddressPopUpInCheckoutTab();
		cscockpitCheckoutTabPage.clickUseAsEnteredPopupOkayInCheckoutTab();
		s_assert.assertTrue(cscockpitCheckoutTabPage.getNewlyCreatedDeliveryAddressNameInCheckoutTab().contains(attendeeNewFirstName),"CSCockpit checkout tab newly created Delivery address name expected ="+ attendeeFirstName+ "and on UI = " +cscockpitCheckoutTabPage.getNewlyCreatedDeliveryAddressNameInCheckoutTab());
		cscockpitCheckoutTabPage.enterCVVValueInCheckoutTab(TestConstants.SECURITY_CODE);
		cscockpitCheckoutTabPage.clickUseThisCardBtnInCheckoutTab();
		cscockpitCheckoutTabPage.clickPlaceOrderButtonInCheckoutTab();
		orderNumberOfOrderTab = cscockpitOrderTabPage.getOrderNumberInOrderTab();
		cscockpitOrderTabPage.clickCustomerTab();
		s_assert.assertTrue(cscockpitCustomerTabPage.getOrderTypeInCustomerTab(orderNumberOfOrderTab.split("\\-")[0].trim()).contains("Consultant Order"),"CSCockpit Customer tab Order type expected = Consultant Order and on UI = " +cscockpitCustomerTabPage.getOrderTypeInCustomerTab(orderNumberOfOrderTab.split("\\-")[0].trim()));
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

	//Hybris Project-1964:To verify the CV and QV update functionality in the order detail Page
	@Test
	public void testVerifyCVAndQVValuesUpdateFunctionalityInOrderDetailPage_1964() throws InterruptedException{
		String randomCustomerSequenceNumber = null;
		String consultantEmailID = null;
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
		logger.info("login is successful");
		storeFrontConsultantPage.clickOnWelcomeDropDown();
		storeFrontOrdersPage = storeFrontConsultantPage.clickOrdersLinkPresentOnWelcomeDropDown();
		orderHistoryNumber= storeFrontOrdersPage.getFirstOrderNumberFromOrderHistory();
		logout();
		driver.get(driver.getCSCockpitURL());
		cscockpitCustomerSearchTabPage = cscockpitLoginPage.clickLoginBtn();
		cscockpitCustomerSearchTabPage.clickOnFindOrderInCustomerSearchTab();
		cscockpitOrderSearchTabPage.enterOrderNumberInOrderSearchTab(orderHistoryNumber);
		cscockpitOrderSearchTabPage.clickSearchBtn();
		cscockpitOrderSearchTabPage.clickOrderLinkOnOrderSearchTabAndVerifyOrderDetailsPage(orderHistoryNumber);
		s_assert.assertTrue(cscockpitOrderTabPage.isQVandCVUpdateBtnDisabledOnOrderTab(), "Username Csagent can update the CV and QV value");
		cscockpitOrderTabPage.clickMenuButton();
		cscockpitOrderTabPage.clickLogoutButton();
		//Login with admin
		// need admin credentials
		//login with cscommission admin credentials
		cscockpitLoginPage.enterUsername(TestConstants.CS_COMMISION_ADMIN_USERNAME);
		cscockpitCustomerSearchTabPage = cscockpitLoginPage.clickLoginBtn();
		cscockpitCustomerSearchTabPage.clickOnFindOrderInCustomerSearchTab();
		cscockpitOrderSearchTabPage.enterOrderNumberInOrderSearchTab(orderHistoryNumber);
		cscockpitOrderSearchTabPage.clickSearchBtn();
		cscockpitOrderSearchTabPage.clickOrderLinkOnOrderSearchTabAndVerifyOrderDetailsPage(orderHistoryNumber);
		s_assert.assertFalse(cscockpitOrderTabPage.isQVandCVUpdateBtnDisabledOnOrderTab(), "Username cscommission admin can update the CV and QV value");
		String CVValueBeforeUpdate = cscockpitOrderTabPage.getCVValueInOrderTab();
		String QVValueBeforeUpdate = cscockpitOrderTabPage.getQVValueInOrderTab();
		cscockpitOrderTabPage.enterCVValueInOrderTab(TestConstants.CV_VALUE);
		cscockpitOrderTabPage.enterQVValueInOrderTab(TestConstants.QV_VALUE);
		cscockpitOrderTabPage.clickupdateButtonForCVAndQVInOrderTab();
		cscockpitOrderTabPage.clickCancelBtnAfterUpdateCVAndQVInOrderTab();
		s_assert.assertTrue(cscockpitOrderTabPage.getCVValueInOrderTab().equalsIgnoreCase(CVValueBeforeUpdate),"CV value before update for cscommission admin "+CVValueBeforeUpdate+"and on UI is"+cscockpitOrderTabPage.getCVValueInOrderTab());
		s_assert.assertTrue(cscockpitOrderTabPage.getQVValueInOrderTab().equalsIgnoreCase(QVValueBeforeUpdate),"QV value before update for cscommission admin "+QVValueBeforeUpdate+"and on UI is"+cscockpitOrderTabPage.getQVValueInOrderTab());
		cscockpitOrderTabPage.enterCVValueInOrderTab(TestConstants.CV_VALUE);
		cscockpitOrderTabPage.enterQVValueInOrderTab(TestConstants.QV_VALUE);
		cscockpitOrderTabPage.clickupdateButtonForCVAndQVInOrderTab();
		cscockpitOrderTabPage.clickOKBtnAfterUpdateCVAndQVInOrderTab();
		s_assert.assertTrue(cscockpitOrderTabPage.getCVValueInOrderTab().equalsIgnoreCase(TestConstants.CV_VALUE.trim()),"CV value before update for cscommission admin "+TestConstants.CV_VALUE.trim()+"and on UI is"+cscockpitOrderTabPage.getCVValueInOrderTab());
		s_assert.assertTrue(cscockpitOrderTabPage.getQVValueInOrderTab().equalsIgnoreCase(TestConstants.QV_VALUE.trim()),"QV value before update for cscommission admin "+TestConstants.QV_VALUE.trim()+"and on UI is"+cscockpitOrderTabPage.getQVValueInOrderTab());
		cscockpitOrderTabPage.clickMenuButton();
		cscockpitOrderTabPage.clickLogoutButton();

		//login with CS SALES SUPERVISORY credentials
		cscockpitLoginPage.enterUsername(TestConstants.CS_SALES_SUPERVISORY_USERNAME);
		cscockpitCustomerSearchTabPage = cscockpitLoginPage.clickLoginBtn();
		cscockpitCustomerSearchTabPage.clickOnFindOrderInCustomerSearchTab();
		cscockpitOrderSearchTabPage.enterOrderNumberInOrderSearchTab(orderHistoryNumber);
		cscockpitOrderSearchTabPage.clickSearchBtn();
		cscockpitOrderSearchTabPage.clickOrderLinkOnOrderSearchTabAndVerifyOrderDetailsPage(orderHistoryNumber);
		s_assert.assertFalse(cscockpitOrderTabPage.isQVandCVUpdateBtnDisabledOnOrderTab(), "Username cs sales supervisory can update the CV and QV value");
		CVValueBeforeUpdate = cscockpitOrderTabPage.getCVValueInOrderTab();
		QVValueBeforeUpdate = cscockpitOrderTabPage.getQVValueInOrderTab();
		cscockpitOrderTabPage.enterCVValueInOrderTab(TestConstants.CV_VALUE);
		cscockpitOrderTabPage.enterQVValueInOrderTab(TestConstants.QV_VALUE);
		cscockpitOrderTabPage.clickupdateButtonForCVAndQVInOrderTab();
		cscockpitOrderTabPage.clickCancelBtnAfterUpdateCVAndQVInOrderTab();
		s_assert.assertTrue(cscockpitOrderTabPage.getCVValueInOrderTab().equalsIgnoreCase(CVValueBeforeUpdate),"CV value before update for cs sales supervisory "+CVValueBeforeUpdate+"and on UI is"+cscockpitOrderTabPage.getCVValueInOrderTab());
		s_assert.assertTrue(cscockpitOrderTabPage.getQVValueInOrderTab().equalsIgnoreCase(QVValueBeforeUpdate),"QV value before update for cs sales supervisory "+QVValueBeforeUpdate+"and on UI is"+cscockpitOrderTabPage.getQVValueInOrderTab());
		cscockpitOrderTabPage.enterCVValueInOrderTab(TestConstants.CV_VALUE);
		cscockpitOrderTabPage.enterQVValueInOrderTab(TestConstants.QV_VALUE);
		cscockpitOrderTabPage.clickupdateButtonForCVAndQVInOrderTab();
		cscockpitOrderTabPage.clickOKBtnAfterUpdateCVAndQVInOrderTab();
		s_assert.assertTrue(cscockpitOrderTabPage.getCVValueInOrderTab().equalsIgnoreCase(TestConstants.CV_VALUE.trim()),"CV value before update for cs sales supervisory "+TestConstants.CV_VALUE.trim()+"and on UI is"+cscockpitOrderTabPage.getCVValueInOrderTab());
		s_assert.assertTrue(cscockpitOrderTabPage.getQVValueInOrderTab().equalsIgnoreCase(TestConstants.QV_VALUE.trim()),"QV value before update for cs sales supervisory "+TestConstants.QV_VALUE.trim()+"and on UI is"+cscockpitOrderTabPage.getQVValueInOrderTab());
		cscockpitOrderTabPage.clickMenuButton();
		cscockpitOrderTabPage.clickLogoutButton();
		//using inactive user
		driver.get(driver.getCSCockpitURL());
		cscockpitCustomerSearchTabPage = cscockpitLoginPage.clickLoginBtn();
		cscockpitCustomerSearchTabPage.selectCustomerTypeFromDropDownInCustomerSearchTab("CONSULTANT");
		cscockpitCustomerSearchTabPage.selectCountryFromDropDownInCustomerSearchTab("United States");
		cscockpitCustomerSearchTabPage.selectAccountStatusFromDropDownInCustomerSearchTab("Inactive");
		cscockpitCustomerSearchTabPage.clickSearchBtn();
		randomCustomerSequenceNumber = String.valueOf(cscockpitCustomerSearchTabPage.getRandomCustomerFromSearchResult());
		cscockpitCustomerSearchTabPage.clickCIDNumberInCustomerSearchTab(randomCustomerSequenceNumber);
		String orderNumber=cscockpitCustomerTabPage.clickAndGetOrderNumberInCustomerTab();
		s_assert.assertTrue(cscockpitOrderTabPage.isQVandCVUpdateBtnDisabledOnOrderTab(), "Username Csagent for inactive user can update the CV and QV value");
		cscockpitOrderTabPage.clickMenuButton();
		cscockpitOrderTabPage.clickLogoutButton();
		//login with cscommission admin credentials
		cscockpitLoginPage.enterUsername(TestConstants.CS_COMMISION_ADMIN_USERNAME);
		cscockpitCustomerSearchTabPage = cscockpitLoginPage.clickLoginBtn();
		cscockpitCustomerSearchTabPage.clickOnFindOrderInCustomerSearchTab();
		cscockpitOrderSearchTabPage.enterOrderNumberInOrderSearchTab(orderNumber.split("\\-")[0].trim());
		cscockpitOrderSearchTabPage.clickSearchBtn();
		cscockpitOrderSearchTabPage.clickOrderLinkOnOrderSearchTabAndVerifyOrderDetailsPage(orderNumber.split("\\-")[0].trim());
		s_assert.assertTrue(cscockpitOrderTabPage.isQVandCVUpdateBtnDisabledOnOrderTab(), "Username cscommission admin for inactive can update the CV and QV value");
		cscockpitOrderTabPage.clickMenuButton();
		cscockpitOrderTabPage.clickLogoutButton();
		//login with CS SALES SUPERVISORY credentials
		cscockpitLoginPage.enterUsername(TestConstants.CS_SALES_SUPERVISORY_USERNAME);
		cscockpitCustomerSearchTabPage = cscockpitLoginPage.clickLoginBtn();
		cscockpitCustomerSearchTabPage.clickOnFindOrderInCustomerSearchTab();
		cscockpitOrderSearchTabPage.enterOrderNumberInOrderSearchTab(orderNumber.split("\\-")[0].trim());
		cscockpitOrderSearchTabPage.clickSearchBtn();
		cscockpitOrderSearchTabPage.clickOrderLinkOnOrderSearchTabAndVerifyOrderDetailsPage(orderNumber.split("\\-")[0].trim());
		s_assert.assertTrue(cscockpitOrderTabPage.isQVandCVUpdateBtnDisabledOnOrderTab(), "Username cs sales supervisory for inactive can update the CV and QV value");
		cscockpitOrderTabPage.clickMenuButton();
		cscockpitOrderTabPage.clickLogoutButton();
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

	//Hybris Project-1723:Verify the Find Autoship Search Criteria
	@Test
	public void testFindAutoshipSearchCriteria_1723(){
		String searchByDDValue_All = "All";
		String searchByDDValue_All_Due_Today = "All Due Today"; 
		String searchByDDValue_NextDueDate = "Next Due Date";	
		String templateTypeDDValue_All = "All";
		String templateTypeDDValue_Pulse = "Pulse";
		String templateTypeDDValue_Consultant = "Consultant";
		String templateTypeDDValue_PC_Customer = "PC Customer";
		String lastOrderStatusDDValue_All = "All";
		String lastOrderStatusDDValue_SUCCESSFUL = "SUCCESSFUL";
		String lastOrderStatusDDValue_FAILED = "FAILED";
		String lastOrderStatusDDValue_CANCELLED = "CANCELLED";
		String templateStatusDDValue_All = "All";
		String templateStatusDDValue_PENDING = "PENDING";
		String templateStatusDDValue_CANCELLED = "CANCELLED";
		String templateStatusDDValue_FAILED = "FAILED";
		String searchResultColumn_Template = "Template#";
		String searchResultColumn_Type = "Type";
		String searchResultColumn_TemplateStatus = "Template status";
		String searchResultColumn_Customer = "Customer";
		String searchResultColumn_AccountStatus = "Account Status";
		String searchResultColumn_Sponsor = "Sponsor";
		String searchResultColumn_NextDueDate = "Next Due Date";
		String searchResultColumn_Details = "Details";
		String searchResultColumn_ConsecutiveOrders = "Consecutive orders";
		String searchResultColumn_LastOrder = "Last Order #";
		String searchResultColumn_LastOrderStatus = "Last Order Status";
		String searchResultColumn_FailedReason = "Failed Reason";

		cscockpitLoginPage = new CSCockpitLoginPage(driver);
		driver.get(driver.getCSCockpitURL());
		cscockpitCustomerSearchTabPage = cscockpitLoginPage.clickLoginBtn();
		cscockpitCustomerSearchTabPage.clickFindAutoshipInLeftNavigation();
		s_assert.assertTrue(cscockpitAutoshipSearchTabPage.verifySearchByFieldPresentOnAutoshipSearch(),"Search By field is not present On autoship Search page");
		cscockpitAutoshipSearchTabPage.clickSearchByDropDown();
		s_assert.assertTrue(cscockpitAutoshipSearchTabPage.isSearchByDropDownValuePresent(searchByDDValue_All),"search By field DD option "+searchByDDValue_All+" is not present on UI");
		s_assert.assertTrue(cscockpitAutoshipSearchTabPage.isSearchByDropDownValuePresent(searchByDDValue_All_Due_Today),"search By field DD option "+searchByDDValue_All_Due_Today+" is not present on UI");
		s_assert.assertTrue(cscockpitAutoshipSearchTabPage.isSearchByDropDownValuePresent(searchByDDValue_NextDueDate),"search By field DD option "+searchByDDValue_NextDueDate+" is not present on UI");
		s_assert.assertTrue(cscockpitAutoshipSearchTabPage.isCalenderIconPresent(),"Calender icon is not present On autoship Search page");
		s_assert.assertTrue(cscockpitAutoshipSearchTabPage.verifyTemplateTypeFieldPresentOnAutoshipSearch(),"Template type field is not present on autoship search page");
		cscockpitAutoshipSearchTabPage.clickTemplateTypeDropDown();
		s_assert.assertTrue(cscockpitAutoshipSearchTabPage.isTemplateTypeDropDownValuePresent(templateTypeDDValue_All),"template field DD option "+templateTypeDDValue_All+" is not present on UI");
		s_assert.assertTrue(cscockpitAutoshipSearchTabPage.isTemplateTypeDropDownValuePresent(templateTypeDDValue_Pulse),"template field DD option "+templateTypeDDValue_Pulse+" is not present on UI");
		s_assert.assertTrue(cscockpitAutoshipSearchTabPage.isTemplateTypeDropDownValuePresent(templateTypeDDValue_Consultant),"template field DD option "+templateTypeDDValue_Consultant+" is not present on UI");
		s_assert.assertTrue(cscockpitAutoshipSearchTabPage.isTemplateTypeDropDownValuePresent(templateTypeDDValue_PC_Customer),"template field DD option "+templateTypeDDValue_PC_Customer+" is not present on UI");
		s_assert.assertTrue(cscockpitAutoshipSearchTabPage.verifyLastOrderStatusFieldPresentOnAutoshipSearch(),"Order status field is not present on autoship search page");
		cscockpitAutoshipSearchTabPage.clickLastOrderStatusDropDown();
		s_assert.assertTrue(cscockpitAutoshipSearchTabPage.islastOrderStatusDropDownValuePresent(lastOrderStatusDDValue_All),"last order status DD option "+lastOrderStatusDDValue_All+" is not present on UI");
		s_assert.assertTrue(cscockpitAutoshipSearchTabPage.islastOrderStatusDropDownValuePresent(lastOrderStatusDDValue_SUCCESSFUL),"last order status DD option "+lastOrderStatusDDValue_SUCCESSFUL+" is not present on UI");
		s_assert.assertTrue(cscockpitAutoshipSearchTabPage.islastOrderStatusDropDownValuePresent(lastOrderStatusDDValue_FAILED),"last order status DD option "+lastOrderStatusDDValue_FAILED+" is not present on UI");
		s_assert.assertTrue(cscockpitAutoshipSearchTabPage.islastOrderStatusDropDownValuePresent(lastOrderStatusDDValue_CANCELLED),"last order status DD option "+lastOrderStatusDDValue_CANCELLED+" is not present on UI");
		cscockpitAutoshipSearchTabPage.clickTemplateStatusDropDown();
		s_assert.assertTrue(cscockpitAutoshipSearchTabPage.isTemplateStatusDropDownValuePresent(templateStatusDDValue_All),"template status DD option "+templateStatusDDValue_All+" is not present on UI");
		s_assert.assertTrue(cscockpitAutoshipSearchTabPage.isTemplateStatusDropDownValuePresent(templateStatusDDValue_PENDING),"template status DD option "+templateStatusDDValue_PENDING+" is not present on UI");
		s_assert.assertTrue(cscockpitAutoshipSearchTabPage.isTemplateStatusDropDownValuePresent(templateStatusDDValue_CANCELLED),"template status DD option "+templateStatusDDValue_CANCELLED+" is not present on UI");
		s_assert.assertTrue(cscockpitAutoshipSearchTabPage.isTemplateStatusDropDownValuePresent(templateStatusDDValue_FAILED),"template status DD option "+templateStatusDDValue_FAILED+" is not present on UI");
		s_assert.assertTrue(cscockpitAutoshipSearchTabPage.verifyCustomerNameCidFieldPresentOnAutoshipPage(),"cusotmer name and cid field not present on the page");
		s_assert.assertTrue(cscockpitAutoshipSearchTabPage.verifySponsorNameCidFieldPresentOnAutoshipPage(),"SponsorName field not present on the page");
		s_assert.assertTrue(cscockpitAutoshipSearchTabPage.verifyTemplateNumberCidFieldPresentOnAutoshipPage(),"TemplateNumber field not present on the page");
		s_assert.assertTrue(cscockpitAutoshipSearchTabPage.verifyRunSelectedButtonPresent(),"Run selected button is not present on the page");
		s_assert.assertTrue(cscockpitAutoshipSearchTabPage.verifySearchAutoshipButtonPresent(),"search autoship button is not present on the page");
		cscockpitAutoshipSearchTabPage.clickSearchAutoshipButton();
		//assert search results column name
		s_assert.assertTrue(cscockpitAutoshipSearchTabPage.isSearchResultsColumnNamePresent(searchResultColumn_Template),"Search reults column name "+searchResultColumn_Template+" is not present on UI");
		s_assert.assertTrue(cscockpitAutoshipSearchTabPage.isSearchResultsColumnNamePresent(searchResultColumn_Type),"Search reults column name "+searchResultColumn_Type+" is not present on UI");
		s_assert.assertTrue(cscockpitAutoshipSearchTabPage.isSearchResultsColumnNamePresent(searchResultColumn_TemplateStatus),"Search reults column name "+searchResultColumn_TemplateStatus+" is not present on UI");
		s_assert.assertTrue(cscockpitAutoshipSearchTabPage.isSearchResultsColumnNamePresent(searchResultColumn_Customer),"Search reults column name "+searchResultColumn_Customer+" is not present on UI");
		s_assert.assertTrue(cscockpitAutoshipSearchTabPage.isSearchResultsColumnNamePresent(searchResultColumn_AccountStatus),"Search reults column name "+searchResultColumn_AccountStatus+" is not present on UI");
		s_assert.assertTrue(cscockpitAutoshipSearchTabPage.isSearchResultsColumnNamePresent(searchResultColumn_Sponsor),"Search reults column name "+searchResultColumn_Sponsor+" is not present on UI");
		s_assert.assertTrue(cscockpitAutoshipSearchTabPage.isSearchResultsColumnNamePresent(searchResultColumn_NextDueDate),"Search reults column name "+searchResultColumn_NextDueDate+" is not present on UI");
		s_assert.assertTrue(cscockpitAutoshipSearchTabPage.isSearchResultsColumnNamePresent(searchResultColumn_Details),"Search reults column name "+searchResultColumn_Details+" is not present on UI");
		s_assert.assertTrue(cscockpitAutoshipSearchTabPage.isSearchResultsColumnNamePresent(searchResultColumn_ConsecutiveOrders),"Search reults column name "+searchResultColumn_ConsecutiveOrders+" is not present on UI");
		s_assert.assertTrue(cscockpitAutoshipSearchTabPage.isSearchResultsColumnNamePresent(searchResultColumn_LastOrder),"Search reults column name "+searchResultColumn_LastOrder+" is not present on UI");
		s_assert.assertTrue(cscockpitAutoshipSearchTabPage.isSearchResultsColumnNamePresent(searchResultColumn_LastOrderStatus),"Search reults column name "+searchResultColumn_LastOrderStatus+" is not present on UI");
		s_assert.assertTrue(cscockpitAutoshipSearchTabPage.isSearchResultsColumnNamePresent(searchResultColumn_FailedReason),"Search reults column name "+searchResultColumn_FailedReason+" is not present on UI");
		s_assert.assertTrue(cscockpitAutoshipSearchTabPage.isSelectAllColumnPresent(), "Select all link is not present in search reults");
		String randomCustomerSequenceNumber = String.valueOf(cscockpitCustomerSearchTabPage.getRandomCustomerFromSearchResult());
		cscockpitCustomerSearchTabPage.clickAndReturnCIDNumberInCustomerSearchTab(randomCustomerSequenceNumber);
		s_assert.assertTrue(cscockpitAutoshipSearchTabPage.isAutoshipTemplateDisplayedInAutoshipTemplateTab(), "After click autoship template id user is not redirecting to autoship template page");
		cscockpitAutoshipSearchTabPage.clickAutoshipSearchTab();
		cscockpitAutoshipSearchTabPage.clickLastOrderNumber(randomCustomerSequenceNumber);
		s_assert.assertTrue(cscockpitOrderTabPage.isOrderTemplateDisplayedInOrderTab(), "After click order number user is not redirecting to order template page");
		cscockpitAutoshipSearchTabPage.clickAutoshipSearchTab();
		cscockpitAutoshipSearchTabPage.clickLastOrderStatusDropDown();
		cscockpitAutoshipSearchTabPage.selectlastOrderStatusDropDownValue(lastOrderStatusDDValue_SUCCESSFUL);
		cscockpitAutoshipSearchTabPage.clickSearchAutoshipButton();
		randomCustomerSequenceNumber = String.valueOf(cscockpitCustomerSearchTabPage.getRandomCustomerFromSearchResult());
		String lastOrder = cscockpitAutoshipSearchTabPage.getLastOrderStatus(randomCustomerSequenceNumber);
		s_assert.assertTrue(lastOrder.contains("SUCCESSFUL"), "select last order status as successful expected result is successful actual on UI is" +lastOrder+" in autoship template page");
		cscockpitAutoshipSearchTabPage.clickLastOrderStatusDropDown();
		cscockpitAutoshipSearchTabPage.selectlastOrderStatusDropDownValue(lastOrderStatusDDValue_All);
		cscockpitAutoshipSearchTabPage.enterCustomerNameOrCID(TestConstants.SPONSOR_ID_FOR_PC);
		cscockpitAutoshipSearchTabPage.clickSearchAutoshipButton();
		s_assert.assertTrue(cscockpitAutoshipSearchTabPage.isAutoshipSearchResultsPresent(),"After click on search autoship button results are not present on the page");
		cscockpitAutoshipSearchTabPage.enterCustomerNameOrCID(TestConstants.INVALID_SPONSOR_NAME);
		cscockpitAutoshipSearchTabPage.clickSearchAutoshipButton();
		s_assert.assertFalse(cscockpitAutoshipSearchTabPage.isAutoshipSearchResultsPresent(),"After click on search autoship button results are present on the page when entered Invalid customer name");
		cscockpitAutoshipSearchTabPage.clearCustomerNameOrCID();
		//assert with search by all due date
		cscockpitAutoshipSearchTabPage.clickSearchByDropDown();
		cscockpitAutoshipSearchTabPage.selectSearchByDropDownValue(searchByDDValue_All_Due_Today);
		cscockpitAutoshipSearchTabPage.clickSearchAutoshipButton();
		s_assert.assertFalse(cscockpitAutoshipSearchTabPage.isCalenderIconPresentForAllDueDate(),"Calender icon is present On autoship Search page after select All due date dd value");
		//assert with search by next due date
		cscockpitAutoshipSearchTabPage.clickSearchByDropDown();
		cscockpitAutoshipSearchTabPage.selectSearchByDropDownValue(searchByDDValue_NextDueDate);
		cscockpitAutoshipSearchTabPage.clickSearchAutoshipButton();
		//assert with failed order
		cscockpitAutoshipSearchTabPage.clickSearchByDropDown();
		cscockpitAutoshipSearchTabPage.selectSearchByDropDownValue(searchByDDValue_All);
		cscockpitAutoshipSearchTabPage.clickLastOrderStatusDropDown();
		cscockpitAutoshipSearchTabPage.selectlastOrderStatusDropDownValue(lastOrderStatusDDValue_FAILED);
		cscockpitAutoshipSearchTabPage.clickSearchAutoshipButton();
		randomCustomerSequenceNumber = String.valueOf(cscockpitCustomerSearchTabPage.getRandomCustomerFromSearchResult());
		lastOrder = cscockpitAutoshipSearchTabPage.getLastOrderStatus(randomCustomerSequenceNumber);
		s_assert.assertTrue(lastOrder.contains("FAILED"), "select last order status as failed expected result is FAILED actual on UI is" +lastOrder+" in autoship template page");
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
		storeFrontUpdateCartPage.clickAddToBagButtonWithoutFilter();
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
		storeFrontUpdateCartPage.clickAddToBagButtonWithoutFilter();
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

	//Hybris Project-1953:To verify Sales Override from Customer detail Page
	@Test
	public void testToVerifySalesOverrideFromCustomerDetailPage_1953() throws InterruptedException{
		RFO_DB = driver.getDBNameRFO();
		String randomCustomerSequenceNumber = null;
		String randomProductSequenceNumber = null;
		String consultantEmailID = null;
		String SKUValue = null;
		String priceValue = "500";
		String cvValue = "500";
		String qvValue = "500";
		String delCost = "20";
		String handCost = "5";
		int randomNum = CommonUtils.getRandomNum(10000, 1000000);
		List<Map<String, Object>> randomConsultantList =  null;
		List<Map<String, Object>> emailIdFromAccountIdList =  null;		
		String accountID = null;


		//-------------------FOR US----------------------------------
		driver.get(driver.getStoreFrontURL()+"/us");
		while(true){
			randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_CONSULTANT_WITH_ORDERS_AND_AUTOSHIPS_RFO,"236"),RFO_DB);
			//			consultantEmailID = (String) getValueFromQueryResult(randomConsultantList, "UserName");
			accountID=String.valueOf(getValueFromQueryResult(randomConsultantList, "AccountID"));
			emailIdFromAccountIdList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_EMAIL_ID_FROM_ACCOUNT_ID,accountID),RFO_DB);
			consultantEmailID=(String) getValueFromQueryResult(emailIdFromAccountIdList, "EmailAddress");
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
		cscockpitCheckoutTabPage.clickPlaceOrderButtonInCheckoutTab();
		cscockpitCheckoutTabPage.clickOkButtonOfSelectPaymentDetailsPopupInCheckoutTab();

		cscockpitCheckoutTabPage.enterOrderNotesInCheckoutTab(TestConstants.ORDER_NOTE+randomNum);
		cscockpitCheckoutTabPage.clickOrderNoteEditButton(TestConstants.ORDER_NOTE+randomNum);
		cscockpitCheckoutTabPage.updateOrderNoteOnCheckOutPage(TestConstants.UPDATED_ORDER_NOTE+randomNum);
		cscockpitCheckoutTabPage.entervalidCV2OnPaymentInfoSection(TestConstants.VALID_CV2_NUMBER);
		cscockpitCheckoutTabPage.clickUseThisCardButtonOnCheckoutPage();
		cscockpitCheckoutTabPage.clickPerformSooButton();
		cscockpitCheckoutTabPage.enterPriceValueInSalesOrderOverridePopUp(priceValue);
		cscockpitCheckoutTabPage.enterCVValueInSalesOrderOverrridePoPuP(cvValue);
		cscockpitCheckoutTabPage.enterQVValueInSalesOrderOvverridePopUp(qvValue);
		cscockpitCheckoutTabPage.enterDeliveryCostsInSalesOrderOvverridePopUp(delCost);
		cscockpitCheckoutTabPage.enterHandlingCostsInSalesOrderOvveridePOpUp(handCost);
		cscockpitCheckoutTabPage.selectOverrideReasonSooDept();
		cscockpitCheckoutTabPage.selectOverrideReasonSooType();
		cscockpitCheckoutTabPage.selectOverrideReasonSooReason();
		cscockpitCheckoutTabPage.clickUpdateButtonSalesOverridePopUp();
		cscockpitCheckoutTabPage.clickPlaceOrderButtonInCheckoutTab();
		s_assert.assertTrue(cscockpitOrderTabPage.getOrderStatusAfterPlaceOrderInOrderTab().contains("SUBMITTED"),"order is not submitted successfully");
		String orderNumber = cscockpitOrderTabPage.getOrderNumberFromCsCockpitUIOnOrderTab();
		cscockpitOrderTabPage.clickCustomerTab();

		s_assert.assertTrue(cscockpitCustomerTabPage.getOrderTypeOnCustomerTab(orderNumber).contains("Override Order"),"This is not Override Order");
		driver.get(driver.getStoreFrontURL()+"/us");
		storeFrontConsultantPage = storeFrontHomePage.loginAsConsultant(consultantEmailID, password);
		storeFrontConsultantPage.clickOnWelcomeDropDown();
		storeFrontOrdersPage = storeFrontConsultantPage.clickOrdersLinkPresentOnWelcomeDropDown();

		s_assert.assertTrue(orderNumber.contains(storeFrontOrdersPage.getFirstOrderNumberFromOrderHistory()),"This Order is not present on the StoreFront of US");
		logout();
		//----------------------FOR CA------------------------
		driver.get(driver.getStoreFrontURL()+"/ca");
		while(true){
			randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_CONSULTANT_WITH_ORDERS_AND_AUTOSHIPS_RFO,"40"),RFO_DB);
			//			consultantEmailID = (String) getValueFromQueryResult(randomConsultantList, "UserName");
			accountID=String.valueOf(getValueFromQueryResult(randomConsultantList, "AccountID"));
			emailIdFromAccountIdList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_EMAIL_ID_FROM_ACCOUNT_ID,accountID),RFO_DB);
			consultantEmailID=(String) getValueFromQueryResult(emailIdFromAccountIdList, "EmailAddress");
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
		cscockpitCheckoutTabPage.clickPlaceOrderButtonInCheckoutTab();
		cscockpitCheckoutTabPage.clickOkButtonOfSelectPaymentDetailsPopupInCheckoutTab();
		cscockpitCheckoutTabPage.enterOrderNotesInCheckoutTab(TestConstants.ORDER_NOTE+randomNum);
		cscockpitCheckoutTabPage.clickOrderNoteEditButton(TestConstants.ORDER_NOTE+randomNum);
		cscockpitCheckoutTabPage.updateOrderNoteOnCheckOutPage(TestConstants.UPDATED_ORDER_NOTE+randomNum);
		cscockpitCheckoutTabPage.entervalidCV2OnPaymentInfoSection(TestConstants.VALID_CV2_NUMBER);
		cscockpitCheckoutTabPage.clickUseThisCardButtonOnCheckoutPage();
		cscockpitCheckoutTabPage.clickPerformSooButton();
		cscockpitCheckoutTabPage.enterPriceValueInSalesOrderOverridePopUp(priceValue);
		cscockpitCheckoutTabPage.enterCVValueInSalesOrderOverrridePoPuP(cvValue);
		cscockpitCheckoutTabPage.enterQVValueInSalesOrderOvverridePopUp(qvValue);
		cscockpitCheckoutTabPage.enterDeliveryCostsInSalesOrderOvverridePopUp(delCost);
		cscockpitCheckoutTabPage.enterHandlingCostsInSalesOrderOvveridePOpUp(handCost);
		cscockpitCheckoutTabPage.selectOverrideReasonSooDept();
		cscockpitCheckoutTabPage.selectOverrideReasonSooType();
		cscockpitCheckoutTabPage.selectOverrideReasonSooReason();
		cscockpitCheckoutTabPage.clickUpdateButtonSalesOverridePopUp();
		cscockpitCheckoutTabPage.clickPlaceOrderButtonInCheckoutTab();
		s_assert.assertTrue(cscockpitOrderTabPage.getOrderStatusAfterPlaceOrderInOrderTab().contains("SUBMITTED"),"order is not submitted successfully on CA");
		String orderNumberForCA = cscockpitOrderTabPage.getOrderNumberFromCsCockpitUIOnOrderTab();
		cscockpitOrderTabPage.clickCustomerTab();
		s_assert.assertTrue(cscockpitCustomerTabPage.getOrderTypeOnCustomerTab(orderNumberForCA).contains("Override Order"),"This is not Override Order on CA");
		driver.get(driver.getStoreFrontURL()+"/ca");
		storeFrontConsultantPage = storeFrontHomePage.loginAsConsultant(consultantEmailID, password);
		storeFrontConsultantPage.clickOnWelcomeDropDown();
		storeFrontOrdersPage = storeFrontConsultantPage.clickOrdersLinkPresentOnWelcomeDropDown();
		s_assert.assertTrue(orderNumberForCA.contains(storeFrontOrdersPage.getFirstOrderNumberFromOrderHistory()),"This Order is not present on the StoreFront of CA");
		logout();
		s_assert.assertAll();
	}

	//Hybris Project-1927:Verify the Find Customer Page UI
	@Test
	public void testToVerifyTheFindCustomerPageUI_1927(){
		cscockpitCustomerSearchTabPage = cscockpitLoginPage.clickLoginBtn();
		s_assert.assertTrue(cscockpitCustomerSearchTabPage.verifyCustomerTypePresenceOnPage(),"customer type select DD not present on customerSearchPage");
		s_assert.assertTrue(cscockpitCustomerSearchTabPage.verifyCustomerCountryPresenceOnPage(),"customer country select DD not present on customerSearchPage");
		s_assert.assertTrue(cscockpitCustomerSearchTabPage.verifyAccountStatusPresenceOnPage(),"customer account status DD not present on customerSearchPage");
		s_assert.assertTrue(cscockpitCustomerSearchTabPage.verifyCustomerNameFieldPresenceOnPage(),"customer name field not presenct on customerSearchPage");
		s_assert.assertTrue(cscockpitCustomerSearchTabPage.verifyPostcodeFieldPresenceOnPage(),"postcode field not present on customerSearchPage");
		s_assert.assertTrue(cscockpitCustomerSearchTabPage.verifyEmailAddressFieldPresenceOnPage(),"EmailAddress field not present on customerSearchPage");
		s_assert.assertAll();
	}

	// Hybris Project-1702:To verify edit CRP Autoship template
	@Test
	public void testVerifyEditCRPAutoshipTemplate_1702() throws InterruptedException{
		RFO_DB = driver.getDBNameRFO();
		String randomCustomerSequenceNumber = null;
		int randomNum = CommonUtils.getRandomNum(10000, 1000000);
		String cid=null;
		String SKUValue = null;
		String autoshipNumber=null;
		String subtotal=null;
		String consultantEmailID=null;
		String orderNote="test"+randomNum;
		String beforeProductCountInAutoshipCart=null;
		String afterProductCountInAutoshipCart=null;
		String accountID = null;
		//-------------------FOR US----------------------------------
		List<Map<String, Object>> randomConsultantList =  null;
		driver.get(driver.getStoreFrontURL()+"/us");
		while(true){
			randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_CONSULTANT_WITH_ORDERS_AND_AUTOSHIPS_RFO,"236"),RFO_DB);
			consultantEmailID = (String) getValueFromQueryResult(randomConsultantList, "UserName");  
			accountID = String.valueOf(getValueFromQueryResult(randomConsultantList, "AccountID"));
			storeFrontConsultantPage = storeFrontHomePage.loginAsConsultant(consultantEmailID, password);
			boolean isLoginError = driver.getCurrentUrl().contains("error");
			if(isLoginError){
				logger.info("Login error for the user "+consultantEmailID);
				driver.get(driver.getStoreFrontURL()+"/us");
			}
			else
				break;
		}
		storeFrontHomePage.clickOnAutoshipCart();
		beforeProductCountInAutoshipCart=storeFrontUpdateCartPage.getProductCountOnAutoShipCartPage();
		logout();
		//get emailId of username
		List<Map<String, Object>> randomConsultantUsernameList =  null;
		randomConsultantUsernameList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_EMAIL_ID_FROM_ACCOUNT_ID,accountID),RFO_DB);
		consultantEmailID = String.valueOf(getValueFromQueryResult(randomConsultantUsernameList, "EmailAddress"));  
		logger.info("emaild of consultant username "+consultantEmailID);
		logger.info("login is successful");
		driver.get(driver.getCSCockpitURL());
		cscockpitCustomerSearchTabPage = cscockpitLoginPage.clickLoginBtn();
		cscockpitCustomerSearchTabPage.selectCustomerTypeFromDropDownInCustomerSearchTab("CONSULTANT");
		cscockpitCustomerSearchTabPage.selectCountryFromDropDownInCustomerSearchTab("United States");
		cscockpitCustomerSearchTabPage.selectAccountStatusFromDropDownInCustomerSearchTab("Active");
		cscockpitCustomerSearchTabPage.enterEmailIdInSearchFieldInCustomerSearchTab(consultantEmailID);
		cscockpitCustomerSearchTabPage.clickSearchBtn();
		randomCustomerSequenceNumber = String.valueOf(cscockpitCustomerSearchTabPage.getRandomCustomerFromSearchResult());
		cid=cscockpitCustomerSearchTabPage.clickAndReturnCIDNumberInCustomerSearchTab(randomCustomerSequenceNumber);
		//Verify Autoship template Section on Customer tab page.
		s_assert.assertTrue(cscockpitCustomerTabPage.verifyAutoshipTemplateSectionInCustomerTab(),"AutoShip Template section is not on Customer Tab Page");
		autoshipNumber=cscockpitCustomerTabPage.getAndClickAutoshipIDHavingTypeAsCRPAutoshipAndStatusIsPending();
		cscockpitAutoshipTemplateTabPage.clickEditTemplateLinkInAutoshipTemplateTab();
		s_assert.assertTrue(cscockpitAutoshipTemplateTabPage.verifyCancelEditLinkInAutoshipTemplateTab(),"Cancel Edit link is not on Autoship template Tab Page");
		cscockpitAutoshipTemplateTabPage.clickRemoveLinkOfOrderDetailInAutoShipTemplateTab();
		s_assert.assertTrue(cscockpitAutoshipTemplateTabPage.verifyThresholdPopupInAutoshipTemplateTab(),"Threshold popup does not appear");
		cscockpitAutoshipTemplateTabPage.clickOKOfThresholdPopupInAutoshipTemplateTab();
		cscockpitAutoshipTemplateTabPage.clickAddMoreLinesLinkInAutoShipTemplateTab();
		cscockpitAutoshipCartTabPage.selectValueFromSortByDDInCartTab("Price: High to Low");
		cscockpitAutoshipCartTabPage.selectCatalogFromDropDownInCartTab();	
		String randomProductSequenceNumber = String.valueOf(cscockpitAutoshipCartTabPage.getRandomProductWithSKUFromSearchResult()); 
		SKUValue = cscockpitAutoshipCartTabPage.getCustomerSKUValueInCartTab(randomProductSequenceNumber);
		cscockpitAutoshipCartTabPage.searchSKUValueInCartTab(SKUValue);
		cscockpitAutoshipCartTabPage.clickAddToCartBtnInCartTab();
		cscockpitAutoshipCartTabPage.clickCheckoutBtnInCartTab();
		cscockpitAutoshipTemplateUpdateTabPage.enterCVVValueInCheckoutTab(TestConstants.SECURITY_CODE);
		cscockpitAutoshipTemplateUpdateTabPage.clickUseThisCardBtnInCheckoutTab();
		cscockpitAutoshipTemplateUpdateTabPage.clickUpdateAutoshipTemplateInAutoshipTemplateUpdateTab();
		cscockpitAutoshipTemplateTabPage.addProductInAutoShipCartTillHaveTwoProduct();
		subtotal=cscockpitAutoshipTemplateTabPage.getSubtotalInAutoshipTemplateTab();
		cscockpitAutoshipTemplateTabPage.updateQuantityOfSecondProduct("3");
		String subtotalAfterUpdateIncrement=cscockpitAutoshipTemplateTabPage.getSubtotalInAutoshipTemplateTab();
		s_assert.assertFalse(subtotal.equalsIgnoreCase(subtotalAfterUpdateIncrement), "Quantity of second product has not been increased updated in us");
		cscockpitAutoshipTemplateTabPage.updateQuantityOfSecondProduct("2");
		String subtotalAfterUpdateDecrement=cscockpitAutoshipTemplateTabPage.getSubtotalInAutoshipTemplateTab();
		s_assert.assertFalse(subtotalAfterUpdateIncrement.equalsIgnoreCase(subtotalAfterUpdateDecrement), "Quantity of second product has not been decreased updated in us");
		cscockpitAutoshipTemplateTabPage.enterOrderNotesInCheckoutTab(TestConstants.ORDER_NOTE+randomNum);
		String orderNotevalueFromUI = cscockpitAutoshipTemplateTabPage.getAddedNoteValueInCheckoutTab(TestConstants.ORDER_NOTE+randomNum);
		String pstDate = cscockpitAutoshipTemplateTabPage.getPSTDate();
		String orderDate = cscockpitAutoshipTemplateTabPage.converPSTDateToUIFormat(pstDate);
		s_assert.assertTrue(cscockpitAutoshipTemplateTabPage.convertUIDateFormatToPSTFormat(orderNotevalueFromUI.split("\\ ")[0]).trim().contains(cscockpitAutoshipTemplateTabPage.getPSTDate()) || cscockpitAutoshipTemplateTabPage.convertUIDateFormatToPSTFormat(orderNotevalueFromUI.split("\\ ")[0]).trim().contains(orderDate),"CSCockpit added order note date in checkout tab expected"+cscockpitAutoshipTemplateTabPage.getPSTDate()+"and on UI" +cscockpitAutoshipTemplateTabPage.convertUIDateFormatToPSTFormat(orderNotevalueFromUI.split("\\ ")[0]).trim());
		s_assert.assertTrue(orderNotevalueFromUI.contains("PM")||orderNotevalueFromUI.contains("AM"), "Added order note does not contain time zone");
		s_assert.assertTrue(cscockpitAutoshipTemplateTabPage.verifyEditButtonIsPresentForOrderNoteInCheckoutTab(TestConstants.ORDER_NOTE+randomNum), "Added order note does not have Edit button");
		subtotal=cscockpitAutoshipTemplateTabPage.getSubtotalInAutoshipTemplateTab();
		cscockpitAutoshipTemplateTabPage.removeProductInOrderDetailInAutoshipTemplateTab();
		String subtotalAfterProductRemoval=cscockpitAutoshipTemplateTabPage.getSubtotalInAutoshipTemplateTab();
		s_assert.assertFalse(subtotal.equalsIgnoreCase(subtotalAfterProductRemoval),"product has not been removed successfully");
		cscockpitAutoshipTemplateTabPage.clickMenuButton();
		cscockpitAutoshipTemplateTabPage.clickLogoutButton();
		//Login to storefront and check the added item in mini cart page.
		driver.get(driver.getStoreFrontURL()+"/us");
		storeFrontConsultantPage = storeFrontHomePage.loginAsConsultant(consultantEmailID, password);
		storeFrontHomePage.clickOnAutoshipCart();
		afterProductCountInAutoshipCart=storeFrontUpdateCartPage.getProductCountOnAutoShipCartPage();
		s_assert.assertFalse(beforeProductCountInAutoshipCart.equalsIgnoreCase(afterProductCountInAutoshipCart), "Product has not been successfully in storefront cart page.");
		logout(); 
		//-------------------FOR CA----------------------------------
		randomConsultantList =  null;
		driver.get(driver.getStoreFrontURL()+"/ca");
		while(true){
			randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_CONSULTANT_WITH_ORDERS_AND_AUTOSHIPS_RFO,"40"),RFO_DB);
			consultantEmailID = (String) getValueFromQueryResult(randomConsultantList, "UserName");  
			accountID = String.valueOf(getValueFromQueryResult(randomConsultantList, "AccountID"));
			storeFrontConsultantPage = storeFrontHomePage.loginAsConsultant(consultantEmailID, password);
			boolean isLoginError = driver.getCurrentUrl().contains("error");
			if(isLoginError){
				logger.info("Login error for the user "+consultantEmailID);
				driver.get(driver.getStoreFrontURL()+"/ca");
			}
			else
				break;
		}
		storeFrontHomePage.clickOnAutoshipCart();
		beforeProductCountInAutoshipCart=storeFrontUpdateCartPage.getProductCountOnAutoShipCartPage();
		logout();
		logger.info("login is successful");
		//get emailId of username
		randomConsultantUsernameList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_EMAIL_ID_FROM_ACCOUNT_ID,accountID),RFO_DB);
		consultantEmailID = String.valueOf(getValueFromQueryResult(randomConsultantUsernameList, "EmailAddress"));  
		logger.info("emaild of consultant username "+consultantEmailID);
		driver.get(driver.getCSCockpitURL());
		cscockpitCustomerSearchTabPage = cscockpitLoginPage.clickLoginBtn();
		cscockpitCustomerSearchTabPage.selectCustomerTypeFromDropDownInCustomerSearchTab("CONSULTANT");
		cscockpitCustomerSearchTabPage.selectCountryFromDropDownInCustomerSearchTab("Canada");
		cscockpitCustomerSearchTabPage.selectAccountStatusFromDropDownInCustomerSearchTab("Active");
		cscockpitCustomerSearchTabPage.enterEmailIdInSearchFieldInCustomerSearchTab(consultantEmailID);
		cscockpitCustomerSearchTabPage.clickSearchBtn();
		String newRandomCustomerSequenceNumber = String.valueOf(cscockpitCustomerSearchTabPage.getRandomCustomerFromSearchResult());
		//randomCustomerSequenceNumber="15";
		cid=cscockpitCustomerSearchTabPage.clickAndReturnCIDNumberInCustomerSearchTab(newRandomCustomerSequenceNumber);
		//Verify Autoship template Section on Customer tab page.
		s_assert.assertTrue(cscockpitCustomerTabPage.verifyAutoshipTemplateSectionInCustomerTab(),"AutoShip Template section is not on Customer Tab Page");
		autoshipNumber=cscockpitCustomerTabPage.getAndClickAutoshipIDHavingTypeAsCRPAutoshipAndStatusIsPending();
		cscockpitAutoshipTemplateTabPage.clickEditTemplateLinkInAutoshipTemplateTab();
		s_assert.assertTrue(cscockpitAutoshipTemplateTabPage.verifyCancelEditLinkInAutoshipTemplateTab(),"Cancel Edit link is not on Autoship template Tab Page");
		cscockpitAutoshipTemplateTabPage.clickRemoveLinkOfOrderDetailInAutoShipTemplateTab();
		s_assert.assertTrue(cscockpitAutoshipTemplateTabPage.verifyThresholdPopupInAutoshipTemplateTab(),"Threshold popup does not appear");
		cscockpitAutoshipTemplateTabPage.clickOKOfThresholdPopupInAutoshipTemplateTab();	
		cscockpitAutoshipTemplateTabPage.clickAddMoreLinesLinkInAutoShipTemplateTab();
		cscockpitAutoshipCartTabPage.selectValueFromSortByDDInCartTab("Price: High to Low");
		cscockpitAutoshipCartTabPage.selectCatalogFromDropDownInCartTab();	
		String newRandomProductSequenceNumber = String.valueOf(cscockpitAutoshipCartTabPage.getRandomProductWithSKUFromSearchResult()); 
		SKUValue = cscockpitAutoshipCartTabPage.getCustomerSKUValueInCartTab(newRandomProductSequenceNumber);
		cscockpitAutoshipCartTabPage.searchSKUValueInCartTab(SKUValue);
		cscockpitAutoshipCartTabPage.clickAddToCartBtnInCartTab();
		cscockpitAutoshipCartTabPage.clickCheckoutBtnInCartTab();
		cscockpitAutoshipTemplateUpdateTabPage.enterCVVValueInCheckoutTab(TestConstants.SECURITY_CODE);
		cscockpitAutoshipTemplateUpdateTabPage.clickUseThisCardBtnInCheckoutTab();
		cscockpitAutoshipTemplateUpdateTabPage.clickUpdateAutoshipTemplateInAutoshipTemplateUpdateTab();
		cscockpitAutoshipTemplateTabPage.addProductInAutoShipCartTillHaveTwoProduct();

		subtotal=cscockpitAutoshipTemplateTabPage.getSubtotalInAutoshipTemplateTab();
		cscockpitAutoshipTemplateTabPage.updateQuantityOfSecondProduct("3");
		subtotalAfterUpdateIncrement=cscockpitAutoshipTemplateTabPage.getSubtotalInAutoshipTemplateTab();
		s_assert.assertFalse(subtotal.equalsIgnoreCase(subtotalAfterUpdateIncrement), "Quantity of second product has not been increased updated in ca");
		cscockpitAutoshipTemplateTabPage.updateQuantityOfSecondProduct("2");
		subtotalAfterUpdateDecrement=cscockpitAutoshipTemplateTabPage.getSubtotalInAutoshipTemplateTab();
		s_assert.assertFalse(subtotalAfterUpdateIncrement.equalsIgnoreCase(subtotalAfterUpdateDecrement), "Quantity of second product has not been decreased updated in ca");
		cscockpitAutoshipTemplateTabPage.enterOrderNotesInCheckoutTab(TestConstants.ORDER_NOTE+randomNum);
		orderNotevalueFromUI = cscockpitAutoshipTemplateTabPage.getAddedNoteValueInCheckoutTab(TestConstants.ORDER_NOTE+randomNum);
		pstDate = cscockpitAutoshipTemplateTabPage.getPSTDate();
		orderDate = cscockpitAutoshipTemplateTabPage.converPSTDateToUIFormat(pstDate);
		s_assert.assertTrue(cscockpitAutoshipTemplateTabPage.convertUIDateFormatToPSTFormat(orderNotevalueFromUI.split("\\ ")[0]).trim().contains(cscockpitAutoshipTemplateTabPage.getPSTDate()) || cscockpitAutoshipTemplateTabPage.convertUIDateFormatToPSTFormat(orderNotevalueFromUI.split("\\ ")[0]).trim().contains(orderDate),"CSCockpit added order note date in checkout tab expected"+cscockpitAutoshipTemplateTabPage.getPSTDate()+"and on UI" +cscockpitAutoshipTemplateTabPage.convertUIDateFormatToPSTFormat(orderNotevalueFromUI.split("\\ ")[0]).trim());
		s_assert.assertTrue(orderNotevalueFromUI.contains("PM")||orderNotevalueFromUI.contains("AM"), "Added order note does not contain time zone");
		s_assert.assertTrue(cscockpitAutoshipTemplateTabPage.verifyEditButtonIsPresentForOrderNoteInCheckoutTab(TestConstants.ORDER_NOTE+randomNum), "Added order note does not have Edit button");
		subtotal=cscockpitAutoshipTemplateTabPage.getSubtotalInAutoshipTemplateTab();
		cscockpitAutoshipTemplateTabPage.removeProductInOrderDetailInAutoshipTemplateTab();
		subtotalAfterProductRemoval=cscockpitAutoshipTemplateTabPage.getSubtotalInAutoshipTemplateTab();
		s_assert.assertFalse(subtotal.equalsIgnoreCase(subtotalAfterProductRemoval),"product has not been removed successfully");
		cscockpitAutoshipTemplateTabPage.clickMenuButton();
		cscockpitAutoshipTemplateTabPage.clickLogoutButton();
		//Login to storefront and check the added item in mini cart page.
		driver.get(driver.getStoreFrontURL()+"/ca");
		storeFrontConsultantPage = storeFrontHomePage.loginAsConsultant(consultantEmailID, password);
		storeFrontHomePage.clickOnAutoshipCart();
		afterProductCountInAutoshipCart=storeFrontUpdateCartPage.getProductCountOnAutoShipCartPage();
		s_assert.assertFalse(beforeProductCountInAutoshipCart.equalsIgnoreCase(afterProductCountInAutoshipCart), "Product has not been successfully in storefront cart page.");
		logout();
		s_assert.assertAll();
	}

	//Hybris Project-1705:To verify change CRP date
	@Test
	public void testVerifyChangeCRPDate_1705() throws InterruptedException{
		RFO_DB = driver.getDBNameRFO();
		String randomCustomerSequenceNumber = null;
		String consultantEmailID=null;

		//-------------------FOR US----------------------------------
		List<Map<String, Object>> randomConsultantList =  null;
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
		cscockpitCustomerSearchTabPage.clickAndReturnCIDNumberInCustomerSearchTab(randomCustomerSequenceNumber);
		cscockpitCustomerTabPage.getAndClickAutoshipIDHavingTypeAsCRPAutoshipAndStatusIsPending();
		cscockpitAutoshipTemplateTabPage.clickEditAutoshiptemplate();
		String nextDueDate = cscockpitAutoshipTemplateTabPage.getCRPAutoshipDateFromCalendar();
		String modifiedDate = cscockpitAutoshipTemplateTabPage.addOneMoreDayInCRPAutoshipDate(nextDueDate);
		cscockpitAutoshipTemplateTabPage.enterCRPAutoshipDate(modifiedDate);
		cscockpitAutoshipTemplateTabPage.clickCustomerTab();
		String day = modifiedDate.split("\\ ")[1];
		s_assert.assertTrue(cscockpitCustomerTabPage.getNextDueDateOfCRPAutoshipAndStatusIsPending().split("\\/")[1].contains(day.split("\\,")[0]),"Expected day of CRP is "+day.split("\\,")[0]+"Actual on UI "+cscockpitCustomerTabPage.getNextDueDateOfCRPAutoshipAndStatusIsPending().split("\\/"));
		driver.get(driver.getStoreFrontURL()+"/us");
		storeFrontConsultantPage = storeFrontHomePage.loginAsConsultant(consultantEmailID, password);
		storeFrontConsultantPage.clickOnWelcomeDropDown();
		storeFrontAccountInfoPage = storeFrontConsultantPage.clickAccountInfoLinkPresentOnWelcomeDropDown();
		storeFrontAccountInfoPage.clickOnYourAccountDropdown();
		storeFrontAccountInfoPage.clickOnAutoShipStatus();
		String crpDate = storeFrontAccountInfoPage.getNextDueDateOfCRPTemplate();
		s_assert.assertTrue(crpDate.trim().split("\\ ")[1].contains(day.split("\\,")[0]), "Expected next day of CRP is "+day.split("\\,")[0]+"Actual on UI in storeFront "+crpDate);
		logout();

		//-------------------FOR CA----------------------------------
		randomConsultantList =  null;
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
		logger.info("login is successful");
		driver.get(driver.getCSCockpitURL());
		cscockpitCustomerSearchTabPage = cscockpitLoginPage.clickLoginBtn();
		cscockpitCustomerSearchTabPage.selectCustomerTypeFromDropDownInCustomerSearchTab("CONSULTANT");
		cscockpitCustomerSearchTabPage.selectCountryFromDropDownInCustomerSearchTab("Canada");
		cscockpitCustomerSearchTabPage.selectAccountStatusFromDropDownInCustomerSearchTab("Active");
		cscockpitCustomerSearchTabPage.enterEmailIdInSearchFieldInCustomerSearchTab(consultantEmailID);
		cscockpitCustomerSearchTabPage.clickSearchBtn();
		randomCustomerSequenceNumber = String.valueOf(cscockpitCustomerSearchTabPage.getRandomCustomerFromSearchResult());
		cscockpitCustomerSearchTabPage.clickAndReturnCIDNumberInCustomerSearchTab(randomCustomerSequenceNumber);
		cscockpitCustomerTabPage.getAndClickAutoshipIDHavingTypeAsCRPAutoshipAndStatusIsPending();
		cscockpitAutoshipTemplateTabPage.clickEditAutoshiptemplate();
		nextDueDate = cscockpitAutoshipTemplateTabPage.getCRPAutoshipDateFromCalendar();
		modifiedDate = cscockpitAutoshipTemplateTabPage.addOneMoreDayInCRPAutoshipDate(nextDueDate);
		cscockpitAutoshipTemplateTabPage.enterCRPAutoshipDate(modifiedDate);
		cscockpitAutoshipTemplateTabPage.clickCustomerTab();
		day = modifiedDate.split("\\ ")[1];
		s_assert.assertTrue(cscockpitCustomerTabPage.getNextDueDateOfCRPAutoshipAndStatusIsPending().split("\\/")[1].contains(day.split("\\,")[0]),"Expected day of CRP is "+day.split("\\,")[0]+"Actual on UI "+cscockpitCustomerTabPage.getNextDueDateOfCRPAutoshipAndStatusIsPending().split("\\/"));
		driver.get(driver.getStoreFrontURL()+"/ca");
		storeFrontConsultantPage = storeFrontHomePage.loginAsConsultant(consultantEmailID, password);
		storeFrontConsultantPage.clickOnWelcomeDropDown();
		storeFrontAccountInfoPage = storeFrontConsultantPage.clickAccountInfoLinkPresentOnWelcomeDropDown();
		storeFrontAccountInfoPage.clickOnYourAccountDropdown();
		storeFrontAccountInfoPage.clickOnAutoShipStatus();
		crpDate = storeFrontAccountInfoPage.getNextDueDateOfCRPTemplate();
		s_assert.assertTrue(crpDate.trim().split("\\ ")[1].contains(day.split("\\,")[0]), "Expected next day of CRP is "+day.split("\\,")[0]+"Actual on UI in storeFront "+crpDate);
		logout();
		s_assert.assertAll();
	}

	//Hybris Project-1718:To verify edit Pulse template
	@Test
	public void testVerifyEditPulseTemplate_1718() throws InterruptedException{
		RFO_DB = driver.getDBNameRFO();
		String randomCustomerSequenceNumber = null;
		String consultantEmailID=null;
		String accountID = null;

		//-------------------FOR US----------------------------------
		List<Map<String, Object>> randomConsultantList =  null;
		List<Map<String, Object>> emailIdFromAccountIdList =  null;
		driver.get(driver.getStoreFrontURL()+"/us");
		while(true){
			randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_CONSULTANT_WITH_ORDERS_AND_AUTOSHIPS_RFO,"236"),RFO_DB);
			//			consultantEmailID = (String) getValueFromQueryResult(randomConsultantList, "UserName");
			accountID=String.valueOf(getValueFromQueryResult(randomConsultantList, "AccountID"));
			emailIdFromAccountIdList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_EMAIL_ID_FROM_ACCOUNT_ID,accountID),RFO_DB);
			consultantEmailID=(String) getValueFromQueryResult(emailIdFromAccountIdList, "EmailAddress");
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
		cscockpitCustomerSearchTabPage.clickAndReturnCIDNumberInCustomerSearchTab(randomCustomerSequenceNumber);
		cscockpitCustomerTabPage.getAndClickPulseTemplateAutoshipIDHavingStatusIsPending();
		cscockpitAutoshipTemplateTabPage.clickEditAutoshiptemplate();
		s_assert.assertTrue(cscockpitAutoshipTemplateTabPage.IsUpdateBtnDisabledForPulseSubscription(), "Update button is enabled for pulse subscription");
		s_assert.assertTrue(cscockpitAutoshipTemplateTabPage.IsRemoveBtnDisabledForPulseSubscription(), "Remove button is enabled for pulse subscription");
		s_assert.assertTrue(cscockpitAutoshipTemplateTabPage.IsInputTxtDisabledForPulseSuscription(), "Qty input txt box is enabled for pulse subscription");
		String nextDueDate = cscockpitAutoshipTemplateTabPage.getCRPAutoshipDateFromCalendar();
		String modifiedDate = cscockpitAutoshipTemplateTabPage.addOneMoreDayInCRPAutoshipDate(nextDueDate);
		cscockpitAutoshipTemplateTabPage.enterCRPAutoshipDate(modifiedDate);
		cscockpitAutoshipTemplateTabPage.clickCustomerTab();
		String day = modifiedDate.split("\\ ")[1];
		s_assert.assertTrue(cscockpitCustomerTabPage.getNextDueDateOfPulseAutoshipSubscriptionAndStatusIsPending().split("\\/")[1].contains(day.split("\\,")[0]),"Expected day of CRP is "+day.split("\\,")[0]+"Actual on UI "+cscockpitCustomerTabPage.getNextDueDateOfPulseAutoshipSubscriptionAndStatusIsPending().split("\\/"));
		driver.get(driver.getStoreFrontURL()+"/us");
		storeFrontConsultantPage = storeFrontHomePage.loginAsConsultant(consultantEmailID, password);
		storeFrontConsultantPage.clickOnWelcomeDropDown();
		storeFrontAccountInfoPage = storeFrontConsultantPage.clickAccountInfoLinkPresentOnWelcomeDropDown();
		storeFrontAccountInfoPage.clickOnYourAccountDropdown();
		storeFrontAccountInfoPage.clickOnAutoShipStatus();
		String crpDate = storeFrontAccountInfoPage.getNextDueDateOfPulseSubscriptionTemplate();
		s_assert.assertTrue(crpDate.trim().split("\\ ")[1].contains(day.split("\\,")[0]), "Expected next day of CRP is "+day.split("\\,")[0]+"Actual on UI in storeFront "+crpDate);
		logout();

		//-------------------FOR CA----------------------------------
		randomConsultantList =  null;
		driver.get(driver.getStoreFrontURL()+"/ca");
		while(true){
			randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_CONSULTANT_WITH_ORDERS_AND_AUTOSHIPS_RFO,"40"),RFO_DB);
			//			consultantEmailID = (String) getValueFromQueryResult(randomConsultantList, "UserName");
			accountID=String.valueOf(getValueFromQueryResult(randomConsultantList, "AccountID"));
			emailIdFromAccountIdList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_EMAIL_ID_FROM_ACCOUNT_ID,accountID),RFO_DB);
			consultantEmailID=(String) getValueFromQueryResult(emailIdFromAccountIdList, "EmailAddress");
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
		driver.get(driver.getCSCockpitURL());
		cscockpitCustomerSearchTabPage = cscockpitLoginPage.clickLoginBtn();
		cscockpitCustomerSearchTabPage.selectCustomerTypeFromDropDownInCustomerSearchTab("CONSULTANT");
		cscockpitCustomerSearchTabPage.selectCountryFromDropDownInCustomerSearchTab("Canada");
		cscockpitCustomerSearchTabPage.selectAccountStatusFromDropDownInCustomerSearchTab("Active");
		cscockpitCustomerSearchTabPage.enterEmailIdInSearchFieldInCustomerSearchTab(consultantEmailID);
		cscockpitCustomerSearchTabPage.clickSearchBtn();
		randomCustomerSequenceNumber = String.valueOf(cscockpitCustomerSearchTabPage.getRandomCustomerFromSearchResult());
		cscockpitCustomerSearchTabPage.clickAndReturnCIDNumberInCustomerSearchTab(randomCustomerSequenceNumber);
		cscockpitCustomerTabPage.getAndClickPulseTemplateAutoshipIDHavingStatusIsPending();
		cscockpitAutoshipTemplateTabPage.clickEditAutoshiptemplate();
		s_assert.assertTrue(cscockpitAutoshipTemplateTabPage.IsUpdateBtnDisabledForPulseSubscription(), "Update button is enabled for pulse subscription");
		s_assert.assertTrue(cscockpitAutoshipTemplateTabPage.IsRemoveBtnDisabledForPulseSubscription(), "Remove button is enabled for pulse subscription");
		s_assert.assertTrue(cscockpitAutoshipTemplateTabPage.IsInputTxtDisabledForPulseSuscription(), "Qty input txt box is enabled for pulse subscription");
		nextDueDate = cscockpitAutoshipTemplateTabPage.getCRPAutoshipDateFromCalendar();
		modifiedDate = cscockpitAutoshipTemplateTabPage.addOneMoreDayInCRPAutoshipDate(nextDueDate);
		cscockpitAutoshipTemplateTabPage.enterCRPAutoshipDate(modifiedDate);
		cscockpitAutoshipTemplateTabPage.clickCustomerTab();
		day = modifiedDate.split("\\ ")[1];
		s_assert.assertTrue(cscockpitCustomerTabPage.getNextDueDateOfPulseAutoshipSubscriptionAndStatusIsPending().split("\\/")[1].contains(day.split("\\,")[0]),"Expected day of CRP is "+day.split("\\,")[0]+"Actual on UI "+cscockpitCustomerTabPage.getNextDueDateOfPulseAutoshipSubscriptionAndStatusIsPending().split("\\/"));
		driver.get(driver.getStoreFrontURL()+"/us");
		storeFrontConsultantPage = storeFrontHomePage.loginAsConsultant(consultantEmailID, password);
		storeFrontConsultantPage.clickOnWelcomeDropDown();
		storeFrontAccountInfoPage = storeFrontConsultantPage.clickAccountInfoLinkPresentOnWelcomeDropDown();
		storeFrontAccountInfoPage.clickOnYourAccountDropdown();
		storeFrontAccountInfoPage.clickOnAutoShipStatus();
		crpDate = storeFrontAccountInfoPage.getNextDueDateOfPulseSubscriptionTemplate();
		s_assert.assertTrue(crpDate.trim().split("\\ ")[1].contains(day.split("\\,")[0]), "Expected next day of CRP is "+day.split("\\,")[0]+"Actual on UI in storeFront "+crpDate);
		logout();
		s_assert.assertAll();
	}

	//Hybris Project-2024:To verify Return shipping functionality
	@Test
	public void testVerifyReturnShippingFunctionality_2024() throws InterruptedException{
		String randomCustomerSequenceNumber = null;
		String consultantEmailID = null;
		String orderNumber = null;
		String returnQuantity = "1";
		String orderHistoryNumber = null;
		String randomProductSequenceNumber = null;
		String SKUValue = null;
		String accountID = null;
		String refundSubtotal="Refunded SubTotals";
		String refundTax="Refunded Tax";
		String refundShipping="Refunded Shipping";
		String refundHandling="Refunded Handling";
		String restockingFee="Restocking Fee";
		String restockingFeeTax="Restocking Fee Tax";
		RFO_DB = driver.getDBNameRFO();
		//-------------------FOR US----------------------------------
		driver.get(driver.getStoreFrontURL()+"/us");
		List<Map<String, Object>> randomConsultantList =  null;
		List<Map<String, Object>> emailIdFromAccountIdList =  null;
		storeFrontHomePage = new StoreFrontHomePage(driver);
		while(true){
			randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_CONSULTANT_WITH_ORDERS_AND_AUTOSHIPS_RFO,"236"),RFO_DB);
			accountID=String.valueOf(getValueFromQueryResult(randomConsultantList, "AccountID"));
			emailIdFromAccountIdList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_EMAIL_ID_FROM_ACCOUNT_ID,accountID),RFO_DB);
			consultantEmailID=(String) getValueFromQueryResult(emailIdFromAccountIdList, "EmailAddress"); 
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
		s_assert.assertTrue(cscockpitCheckoutTabPage.getSizeOfDeliveryModeDDValues().contains("3"),"CSCockpit checkout tab delivery mode address count expected = 3 and on UI = " +cscockpitCheckoutTabPage.getSizeOfDeliveryModeDDValues());
		s_assert.assertTrue(cscockpitCheckoutTabPage.isCommissionDatePopulatedInCheckoutTab(), "Commission date is not populated in UI");
		cscockpitCheckoutTabPage.clickPlaceOrderButtonInCheckoutTab();
		s_assert.assertTrue(cscockpitCheckoutTabPage.verifySelectPaymentDetailsPopupInCheckoutTab(), "Select payment details popup is not present");
		cscockpitCheckoutTabPage.clickOkButtonOfSelectPaymentDetailsPopupInCheckoutTab();
		cscockpitCheckoutTabPage.clickAddNewPaymentAddressInCheckoutTab();
		cscockpitCheckoutTabPage.enterBillingInfo();
		cscockpitCheckoutTabPage.clickSaveAddNewPaymentProfilePopUP();
		cscockpitCheckoutTabPage.enterCVVValueInCheckoutTab(TestConstants.SECURITY_CODE);
		cscockpitCheckoutTabPage.clickUseThisCardBtnInCheckoutTab();
		cscockpitCheckoutTabPage.clickPlaceOrderButtonInCheckoutTab();
		orderNumber = cscockpitOrderTabPage.getOrderNumberInOrderTab();
		logger.info("RETURN ORDER NUMBER IS "+orderNumber);		
		s_assert.assertTrue(cscockpitOrderSearchTabPage.clickOrderLinkOnOrderSearchTabAndVerifyOrderDetailsPage(orderNumber)>0, "Order was NOT placed successfully,expected count after placing order in order detail items section >0 but actual count on UI = "+cscockpitOrderTabPage.getCountOfOrdersOnOrdersDetailsPageAfterPlacingOrder());
		cscockpitOrderTabPage.clickRefundOrderBtnOnOrderTab();
		s_assert.assertTrue(cscockpitOrderTabPage.verifyRefundRequestPopUpPresent(),"Refund Request PopUp not present on us");
		cscockpitOrderTabPage.checkReturnShippingCheckboxInPopUp();
		cscockpitOrderTabPage.selectRefundReasonOnRefundPopUp("Test");
		cscockpitOrderTabPage.selectFirstReturnActionOnRefundPopUp();
		cscockpitOrderTabPage.selectFirstRefundTypeOnRefundPopUp();
		cscockpitOrderTabPage.clickCreateBtnOnRefundPopUp();
		String refundTotal = cscockpitOrderTabPage.getRefundTotalFromRefundConfirmationPopUp();
		//Verify Shipping and tax on shipping details.
		s_assert.assertTrue(cscockpitOrderTabPage.getShippingAndHandlingVariousSectionTaxInPopup(refundSubtotal).contains("0.00"),"Refund subtotal expected on UI for US "+0.00+"while Actual on UI is "+cscockpitOrderTabPage.getShippingAndHandlingVariousSectionTaxInPopup(refundSubtotal));
		s_assert.assertFalse(cscockpitOrderTabPage.getShippingAndHandlingVariousSectionTaxInPopup(refundTax).contains("0.00"),"Refund tax expected on UI for US "+1.28+"while Actual on UI is "+cscockpitOrderTabPage.getShippingAndHandlingVariousSectionTaxInPopup(refundTax));
		s_assert.assertFalse(cscockpitOrderTabPage.getShippingAndHandlingVariousSectionTaxInPopup(refundShipping).contains("0.00"),"Refund shipping expected on UI for US "+15.00+"while Actual on UI is "+cscockpitOrderTabPage.getShippingAndHandlingVariousSectionTaxInPopup(refundShipping));
		s_assert.assertTrue(cscockpitOrderTabPage.getShippingAndHandlingVariousSectionTaxInPopup(refundHandling).contains("0.00"),"Refund Handling expected on UI for US "+0.00+"while Actual on UI is "+cscockpitOrderTabPage.getShippingAndHandlingVariousSectionTaxInPopup(refundHandling));
		s_assert.assertTrue(cscockpitOrderTabPage.getShippingAndHandlingVariousSectionTaxInPopup(restockingFee).contains("0.00"),"Restocking fee expected on UI for US "+0.00+"while Actual on UI is "+cscockpitOrderTabPage.getShippingAndHandlingVariousSectionTaxInPopup(restockingFee));
		s_assert.assertTrue(cscockpitOrderTabPage.getShippingAndHandlingVariousSectionTaxInPopup(restockingFeeTax).contains("0.00"),"Restocking fee tax expected on UI for US "+0.00+"while Actual on UI is "+cscockpitOrderTabPage.getShippingAndHandlingVariousSectionTaxInPopup(restockingFeeTax));

		cscockpitOrderTabPage.clickConfirmBtnOnConfirmPopUp();
		cscockpitOrderTabPage.clickOKBtnOnRMAPopUp();
		s_assert.assertTrue(cscockpitOrderTabPage.isReturnRequestSectionDisplayed(), "Return request section is NOT displayed");
		cscockpitOrderTabPage.clickRMATreeBtnUnderReturnRequestOnOrderTab();
		cscockpitOrderTabPage.clickRMATreeBtnUnderReturnRequestOnOrderTab();
		cscockpitOrderTabPage.clickMenuButton();
		cscockpitOrderTabPage.clickLogoutButton();

		driver.get(driver.getStoreFrontURL()+"/us");
		storeFrontHomePage = new StoreFrontHomePage(driver);
		storeFrontConsultantPage = storeFrontHomePage.loginAsConsultant(consultantEmailID, password);
		storeFrontConsultantPage.clickOnWelcomeDropDown();
		storeFrontOrdersPage =  storeFrontConsultantPage.clickOrdersLinkPresentOnWelcomeDropDown();
		s_assert.assertTrue(storeFrontOrdersPage.verifyOrdersPageIsDisplayed(),"Orders page has not been displayed");
		orderHistoryNumber = storeFrontOrdersPage.getFirstOrderNumberFromOrderHistory();
		s_assert.assertTrue(orderHistoryNumber.contains(orderNumber.split("\\-")[0].trim()),"CSCockpit Order number expected = "+orderNumber.split("\\-")[0].trim()+" and on UI = " +orderHistoryNumber);
		logout();
		driver.get(driver.getCSCockpitURL());		
		cscockpitCustomerSearchTabPage = cscockpitLoginPage.clickLoginBtn();
		cscockpitCustomerSearchTabPage.clickOrderSearchTab();
		cscockpitOrderSearchTabPage.enterOrderNumberInOrderSearchTab(orderNumber);
		cscockpitOrderSearchTabPage.clickSearchBtn();
		cscockpitOrderSearchTabPage.clickOrderLinkOnOrderSearchTabAndVerifyOrderDetailsPage(orderNumber);
		cscockpitOrderTabPage.clickRefundOrderBtnOnOrderTab();
		s_assert.assertTrue(cscockpitOrderTabPage.verifyRefundRequestPopUpPresent(),"Refund Request PopUp not present on us");
		s_assert.assertTrue(cscockpitOrderTabPage.isRMAIdTxtPresent(), "RMA id text is not present in refund request popup.");
		s_assert.assertTrue(cscockpitOrderTabPage.getOrderNumbertxtFromRefundRequestPopup().contains(orderNumber.split("\\-")[0].trim()), "Order Number = "+orderNumber+" is not present in refund request popup");
		cscockpitOrderTabPage.checkProductInfoCheckboxInPopUp();
		cscockpitOrderTabPage.selectReturnQuantityOnPopUp(returnQuantity);
		cscockpitOrderTabPage.checkRestockingFeeCheckboxInPopUp();
		cscockpitOrderTabPage.selectRefundReasonOnRefundPopUp("Test");
		cscockpitOrderTabPage.selectFirstReturnActionOnRefundPopUp();
		cscockpitOrderTabPage.selectFirstRefundTypeOnRefundPopUp();
		cscockpitOrderTabPage.clickCreateBtnOnRefundPopUp();
		cscockpitOrderTabPage.clickConfirmBtnOnConfirmPopUp();
		cscockpitOrderTabPage.clickOKBtnOnRMAPopUp();
		cscockpitOrderTabPage.clickMenuButton();
		cscockpitOrderTabPage.clickLogoutButton();


		driver.get(driver.getStoreFrontURL()+"/us");
		storeFrontHomePage = new StoreFrontHomePage(driver);
		storeFrontConsultantPage = storeFrontHomePage.loginAsConsultant(consultantEmailID, password);
		storeFrontConsultantPage.clickOnWelcomeDropDown();
		storeFrontOrdersPage =  storeFrontConsultantPage.clickOrdersLinkPresentOnWelcomeDropDown();
		s_assert.assertTrue(storeFrontOrdersPage.verifyOrdersPageIsDisplayed(),"Orders page has not been displayed");
		orderHistoryNumber = storeFrontOrdersPage.getFirstOrderNumberFromOrderHistory();
		s_assert.assertTrue(orderHistoryNumber.contains(orderNumber.split("\\-")[0].trim()),"CSCockpit Order number expected = "+orderNumber.split("\\-")[0].trim()+" and on UI = " +orderHistoryNumber);
		logout();
		//-------------------FOR CA----------------------------------
		driver.get(driver.getStoreFrontURL()+"/ca");
		randomConsultantList =  null;
		storeFrontHomePage = new StoreFrontHomePage(driver);
		while(true){
			randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_CONSULTANT_WITH_ORDERS_AND_AUTOSHIPS_RFO,"40"),RFO_DB);
			accountID=String.valueOf(getValueFromQueryResult(randomConsultantList, "AccountID"));
			emailIdFromAccountIdList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_EMAIL_ID_FROM_ACCOUNT_ID,accountID),RFO_DB);
			consultantEmailID=(String) getValueFromQueryResult(emailIdFromAccountIdList, "EmailAddress");   
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
		cscockpitCustomerSearchTabPage = cscockpitLoginPage.clickLoginBtn();
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
		s_assert.assertTrue(cscockpitCheckoutTabPage.getSizeOfDeliveryModeDDValues().contains("3"),"CSCockpit checkout tab delivery mode address count expected = 3 and on UI = " +cscockpitCheckoutTabPage.getSizeOfDeliveryModeDDValues());
		s_assert.assertTrue(cscockpitCheckoutTabPage.isCommissionDatePopulatedInCheckoutTab(), "Commission date is not populated in UI");
		cscockpitCheckoutTabPage.clickPlaceOrderButtonInCheckoutTab();
		s_assert.assertTrue(cscockpitCheckoutTabPage.verifySelectPaymentDetailsPopupInCheckoutTab(), "Select payment details popup is not present");
		cscockpitCheckoutTabPage.clickOkButtonOfSelectPaymentDetailsPopupInCheckoutTab();
		cscockpitCheckoutTabPage.clickAddNewPaymentAddressInCheckoutTab();
		cscockpitCheckoutTabPage.enterBillingInfo();
		cscockpitCheckoutTabPage.clickSaveAddNewPaymentProfilePopUP();
		cscockpitCheckoutTabPage.enterCVVValueInCheckoutTab(TestConstants.SECURITY_CODE);
		cscockpitCheckoutTabPage.clickUseThisCardBtnInCheckoutTab();
		cscockpitCheckoutTabPage.clickPlaceOrderButtonInCheckoutTab();
		orderNumber = cscockpitOrderTabPage.getOrderNumberInOrderTab();
		logger.info("RETURN ORDER NUMBER IS "+orderNumber);		
		s_assert.assertTrue(cscockpitOrderSearchTabPage.clickOrderLinkOnOrderSearchTabAndVerifyOrderDetailsPage(orderNumber)>0, "Order was NOT placed successfully,expected count after placing order in order detail items section >0 but actual count on UI = "+cscockpitOrderTabPage.getCountOfOrdersOnOrdersDetailsPageAfterPlacingOrder());
		cscockpitOrderTabPage.clickRefundOrderBtnOnOrderTab();
		s_assert.assertTrue(cscockpitOrderTabPage.verifyRefundRequestPopUpPresent(),"Refund Request PopUp not present on us");
		cscockpitOrderTabPage.checkReturnShippingCheckboxInPopUp();
		cscockpitOrderTabPage.selectRefundReasonOnRefundPopUp("Test");
		cscockpitOrderTabPage.selectFirstReturnActionOnRefundPopUp();
		cscockpitOrderTabPage.selectFirstRefundTypeOnRefundPopUp();
		cscockpitOrderTabPage.clickCreateBtnOnRefundPopUp();
		refundTotal = cscockpitOrderTabPage.getRefundTotalFromRefundConfirmationPopUp();
		//Verify Shipping and tax on shipping details.
		s_assert.assertTrue(cscockpitOrderTabPage.getShippingAndHandlingVariousSectionTaxInPopup(refundSubtotal).contains("0.00"),"Refund Total expected on UI for ca "+0.00+"while Actual on UI is "+cscockpitOrderTabPage.getShippingAndHandlingVariousSectionTaxInPopup(refundSubtotal));
		s_assert.assertFalse(cscockpitOrderTabPage.getShippingAndHandlingVariousSectionTaxInPopup(refundTax).contains("0.00"),"Refund Tax expected on UI for ca "+1.28+"while Actual on UI is "+cscockpitOrderTabPage.getShippingAndHandlingVariousSectionTaxInPopup(refundTax));
		s_assert.assertFalse(cscockpitOrderTabPage.getShippingAndHandlingVariousSectionTaxInPopup(refundShipping).contains("0.00"),"Refund Shipping expected on UI for ca "+15.00+"while Actual on UI is "+cscockpitOrderTabPage.getShippingAndHandlingVariousSectionTaxInPopup(refundShipping));
		s_assert.assertTrue(cscockpitOrderTabPage.getShippingAndHandlingVariousSectionTaxInPopup(refundHandling).contains("0.00"),"Refund Handling expected on UI for ca "+0.00+"while Actual on UI is "+cscockpitOrderTabPage.getShippingAndHandlingVariousSectionTaxInPopup(refundHandling));
		s_assert.assertTrue(cscockpitOrderTabPage.getShippingAndHandlingVariousSectionTaxInPopup(restockingFee).contains("0.00"),"Restocking fee expected on UI for ca "+0.00+"while Actual on UI is "+cscockpitOrderTabPage.getShippingAndHandlingVariousSectionTaxInPopup(restockingFee));
		s_assert.assertTrue(cscockpitOrderTabPage.getShippingAndHandlingVariousSectionTaxInPopup(restockingFeeTax).contains("0.00"),"Restocking fee tax expected on UI for ca "+0.00+"while Actual on UI is "+cscockpitOrderTabPage.getShippingAndHandlingVariousSectionTaxInPopup(restockingFeeTax));

		cscockpitOrderTabPage.clickConfirmBtnOnConfirmPopUp();
		cscockpitOrderTabPage.clickOKBtnOnRMAPopUp();
		s_assert.assertTrue(cscockpitOrderTabPage.isReturnRequestSectionDisplayed(), "Return request section is NOT displayed");
		cscockpitOrderTabPage.clickRMATreeBtnUnderReturnRequestOnOrderTab();
		cscockpitOrderTabPage.clickRMATreeBtnUnderReturnRequestOnOrderTab();
		cscockpitOrderTabPage.clickMenuButton();
		cscockpitOrderTabPage.clickLogoutButton();

		driver.get(driver.getStoreFrontURL()+"/ca");
		storeFrontHomePage = new StoreFrontHomePage(driver);
		storeFrontConsultantPage = storeFrontHomePage.loginAsConsultant(consultantEmailID, password);
		storeFrontConsultantPage.clickOnWelcomeDropDown();
		storeFrontOrdersPage =  storeFrontConsultantPage.clickOrdersLinkPresentOnWelcomeDropDown();
		s_assert.assertTrue(storeFrontOrdersPage.verifyOrdersPageIsDisplayed(),"Orders page has not been displayed");
		orderHistoryNumber = storeFrontOrdersPage.getFirstOrderNumberFromOrderHistory();
		s_assert.assertTrue(orderHistoryNumber.contains(orderNumber.split("\\-")[0].trim()),"CSCockpit Order number expected = "+orderNumber.split("\\-")[0].trim()+" and on UI = " +orderHistoryNumber);
		logout();
		driver.get(driver.getCSCockpitURL());		
		cscockpitCustomerSearchTabPage = cscockpitLoginPage.clickLoginBtn();
		cscockpitCustomerSearchTabPage.clickOrderSearchTab();
		cscockpitOrderSearchTabPage.enterOrderNumberInOrderSearchTab(orderNumber);
		cscockpitOrderSearchTabPage.clickSearchBtn();
		cscockpitOrderSearchTabPage.clickOrderLinkOnOrderSearchTabAndVerifyOrderDetailsPage(orderNumber);
		cscockpitOrderTabPage.clickRefundOrderBtnOnOrderTab();
		s_assert.assertTrue(cscockpitOrderTabPage.verifyRefundRequestPopUpPresent(),"Refund Request PopUp not present on us");
		s_assert.assertTrue(cscockpitOrderTabPage.isRMAIdTxtPresent(), "RMA id text is not present in refund request popup.");
		s_assert.assertTrue(cscockpitOrderTabPage.getOrderNumbertxtFromRefundRequestPopup().contains(orderNumber.split("\\-")[0].trim()), "Order Number = "+orderNumber+" is not present in refund request popup");
		cscockpitOrderTabPage.checkProductInfoCheckboxInPopUp();
		cscockpitOrderTabPage.selectReturnQuantityOnPopUp(returnQuantity);
		cscockpitOrderTabPage.checkRestockingFeeCheckboxInPopUp();
		cscockpitOrderTabPage.selectRefundReasonOnRefundPopUp("Test");
		cscockpitOrderTabPage.selectFirstReturnActionOnRefundPopUp();
		cscockpitOrderTabPage.selectFirstRefundTypeOnRefundPopUp();
		cscockpitOrderTabPage.clickCreateBtnOnRefundPopUp();
		cscockpitOrderTabPage.clickConfirmBtnOnConfirmPopUp();
		cscockpitOrderTabPage.clickOKBtnOnRMAPopUp();
		cscockpitOrderTabPage.clickMenuButton();
		cscockpitOrderTabPage.clickLogoutButton();

		driver.get(driver.getStoreFrontURL()+"/ca");
		storeFrontHomePage = new StoreFrontHomePage(driver);
		storeFrontConsultantPage = storeFrontHomePage.loginAsConsultant(consultantEmailID, password);
		storeFrontConsultantPage.clickOnWelcomeDropDown();
		storeFrontOrdersPage =  storeFrontConsultantPage.clickOrdersLinkPresentOnWelcomeDropDown();
		s_assert.assertTrue(storeFrontOrdersPage.verifyOrdersPageIsDisplayed(),"Orders page has not been displayed");
		orderHistoryNumber = storeFrontOrdersPage.getFirstOrderNumberFromOrderHistory();
		s_assert.assertTrue(orderHistoryNumber.contains(orderNumber.split("\\-")[0].trim()),"CSCockpit Order number expected = "+orderNumber.split("\\-")[0].trim()+" and on UI = " +orderHistoryNumber);
		logout();
		s_assert.assertAll();		
	}

	//Hybris Project-2025:To verify Return Handling functionality
	@Test
	public void testVerifyReturnHandlingFunctionality_2025() throws InterruptedException{
		String randomCustomerSequenceNumber = null;
		String consultantEmailID = null;
		String orderNumber = null;
		String returnQuantity = "1";
		String orderHistoryNumber = null;
		String randomProductSequenceNumber = null;
		String SKUValue = null;
		String accountID = null;
		String refundSubtotal="Refunded SubTotals";
		String refundDiscount="Refunded Discount";
		String refundTax="Refunded Tax";
		String refundShipping="Refunded Shipping";
		String refundHandling="Refunded Handling";
		String restockingFee="Restocking Fee";
		String restockingFeeTax="Restocking Fee Tax";
		RFO_DB = driver.getDBNameRFO();
		//-------------------FOR US----------------------------------
		driver.get(driver.getStoreFrontURL()+"/us");
		List<Map<String, Object>> randomConsultantList =  null;
		List<Map<String, Object>> emailIdFromAccountIdList =  null;
		storeFrontHomePage = new StoreFrontHomePage(driver);
		while(true){
			randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_CONSULTANT_WITH_ORDERS_AND_AUTOSHIPS_RFO,"236"),RFO_DB);
			accountID=String.valueOf(getValueFromQueryResult(randomConsultantList, "AccountID"));
			emailIdFromAccountIdList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_EMAIL_ID_FROM_ACCOUNT_ID,accountID),RFO_DB);
			consultantEmailID=(String) getValueFromQueryResult(emailIdFromAccountIdList, "EmailAddress");   
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
		cscockpitCheckoutTabPage.clickAddNewPaymentAddressInCheckoutTab();
		cscockpitCheckoutTabPage.enterBillingInfo();
		cscockpitCheckoutTabPage.clickSaveAddNewPaymentProfilePopUP();
		cscockpitCheckoutTabPage.enterCVVValueInCheckoutTab(TestConstants.SECURITY_CODE);
		cscockpitCheckoutTabPage.clickUseThisCardBtnInCheckoutTab();
		cscockpitCheckoutTabPage.clickPlaceOrderButtonInCheckoutTab();
		orderNumber = cscockpitOrderTabPage.getOrderNumberInOrderTab();
		logger.info("RETURN ORDER NUMBER IS "+orderNumber);		
		s_assert.assertTrue(cscockpitOrderSearchTabPage.clickOrderLinkOnOrderSearchTabAndVerifyOrderDetailsPage(orderNumber)>0, "Order was NOT placed successfully,expected count after placing order in order detail items section >0 but actual count on UI = "+cscockpitOrderTabPage.getCountOfOrdersOnOrdersDetailsPageAfterPlacingOrder());
		cscockpitOrderTabPage.clickRefundOrderBtnOnOrderTab();
		s_assert.assertTrue(cscockpitOrderTabPage.verifyRefundRequestPopUpPresent(),"Refund Request PopUp not present on us");
		cscockpitOrderTabPage.checkReturnHandlingCheckboxInPopUp();
		cscockpitOrderTabPage.selectRefundReasonOnRefundPopUp("Test");
		cscockpitOrderTabPage.selectFirstReturnActionOnRefundPopUp();
		cscockpitOrderTabPage.selectFirstRefundTypeOnRefundPopUp();
		cscockpitOrderTabPage.clickCreateBtnOnRefundPopUp();
		String refundTotal = cscockpitOrderTabPage.getRefundTotalFromRefundConfirmationPopUp();
		//Verify Handling and tax on Handling details.
		s_assert.assertTrue(cscockpitOrderTabPage.getShippingAndHandlingVariousSectionTaxInPopup(refundSubtotal).contains("0.00"),"Refund subtotal expected on UI for us "+0.00+"while Actual on UI is "+cscockpitOrderTabPage.getShippingAndHandlingVariousSectionTaxInPopup(refundSubtotal));
		s_assert.assertTrue(cscockpitOrderTabPage.getShippingAndHandlingVariousSectionTaxInPopup(refundDiscount).contains("0.00"),"Refund discount expected on UI for us "+0.00+"while Actual on UI is "+cscockpitOrderTabPage.getShippingAndHandlingVariousSectionTaxInPopup(refundDiscount));
		s_assert.assertFalse(cscockpitOrderTabPage.getShippingAndHandlingVariousSectionTaxInPopup(refundTax).contains("0.00"),"Refund tax expected on UI for us "+0.24+"while Actual on UI is "+cscockpitOrderTabPage.getShippingAndHandlingVariousSectionTaxInPopup(refundTax));
		s_assert.assertTrue(cscockpitOrderTabPage.getShippingAndHandlingVariousSectionTaxInPopup(refundShipping).contains("0.00"),"Refund shipping expected on UI for us "+0.00+"while Actual on UI is "+cscockpitOrderTabPage.getShippingAndHandlingVariousSectionTaxInPopup(refundShipping));
		s_assert.assertFalse(cscockpitOrderTabPage.getShippingAndHandlingVariousSectionTaxInPopup(refundHandling).contains("0.00"),"Refund handling expected on UI for us "+2.50+"while Actual on UI is "+cscockpitOrderTabPage.getShippingAndHandlingVariousSectionTaxInPopup(refundHandling));
		s_assert.assertTrue(cscockpitOrderTabPage.getShippingAndHandlingVariousSectionTaxInPopup(restockingFee).contains("0.00"),"Restocking fee expected on UI for us "+0.00+"while Actual on UI is "+cscockpitOrderTabPage.getShippingAndHandlingVariousSectionTaxInPopup(restockingFee));
		s_assert.assertTrue(cscockpitOrderTabPage.getShippingAndHandlingVariousSectionTaxInPopup(restockingFeeTax).contains("0.00"),"Restocking fee tax expected on UI for us "+0.00+"while Actual on UI is "+cscockpitOrderTabPage.getShippingAndHandlingVariousSectionTaxInPopup(restockingFeeTax));

		cscockpitOrderTabPage.clickConfirmBtnOnConfirmPopUp();
		cscockpitOrderTabPage.clickOKBtnOnRMAPopUp();
		s_assert.assertTrue(cscockpitOrderTabPage.isReturnRequestSectionDisplayed(), "Return request section is NOT displayed");
		cscockpitOrderTabPage.clickRMATreeBtnUnderReturnRequestOnOrderTab();
		cscockpitOrderTabPage.clickRMATreeBtnUnderReturnRequestOnOrderTab();
		cscockpitOrderTabPage.clickMenuButton();
		cscockpitOrderTabPage.clickLogoutButton();

		driver.get(driver.getStoreFrontURL()+"/us");
		storeFrontHomePage = new StoreFrontHomePage(driver);
		storeFrontConsultantPage = storeFrontHomePage.loginAsConsultant(consultantEmailID, password);
		storeFrontConsultantPage.clickOnWelcomeDropDown();
		storeFrontOrdersPage =  storeFrontConsultantPage.clickOrdersLinkPresentOnWelcomeDropDown();
		s_assert.assertTrue(storeFrontOrdersPage.verifyOrdersPageIsDisplayed(),"Orders page has not been displayed");
		orderHistoryNumber = storeFrontOrdersPage.getFirstOrderNumberFromOrderHistory();
		s_assert.assertTrue(orderHistoryNumber.contains(orderNumber.split("\\-")[0].trim()),"CSCockpit Order number expected = "+orderNumber.split("\\-")[0].trim()+" and on UI = " +orderHistoryNumber);
		logout();
		driver.get(driver.getCSCockpitURL());		
		cscockpitCustomerSearchTabPage = cscockpitLoginPage.clickLoginBtn();
		cscockpitCustomerSearchTabPage.clickOrderSearchTab();
		cscockpitOrderSearchTabPage.enterOrderNumberInOrderSearchTab(orderNumber);
		cscockpitOrderSearchTabPage.clickSearchBtn();
		cscockpitOrderSearchTabPage.clickOrderLinkOnOrderSearchTabAndVerifyOrderDetailsPage(orderNumber);
		cscockpitOrderTabPage.clickRefundOrderBtnOnOrderTab();
		s_assert.assertTrue(cscockpitOrderTabPage.verifyRefundRequestPopUpPresent(),"Refund Request PopUp not present on us");
		s_assert.assertTrue(cscockpitOrderTabPage.isRMAIdTxtPresent(), "RMA id text is not present in refund request popup.");
		s_assert.assertTrue(cscockpitOrderTabPage.getOrderNumbertxtFromRefundRequestPopup().contains(orderNumber.split("\\-")[0].trim()), "Order Number = "+orderNumber+" is not present in refund request popup");
		cscockpitOrderTabPage.checkProductInfoCheckboxInPopUp();
		cscockpitOrderTabPage.selectReturnQuantityOnPopUp(returnQuantity);
		cscockpitOrderTabPage.checkRestockingFeeCheckboxInPopUp();
		cscockpitOrderTabPage.selectRefundReasonOnRefundPopUp("Test");
		cscockpitOrderTabPage.selectFirstReturnActionOnRefundPopUp();
		cscockpitOrderTabPage.selectFirstRefundTypeOnRefundPopUp();
		cscockpitOrderTabPage.clickCreateBtnOnRefundPopUp();
		cscockpitOrderTabPage.clickConfirmBtnOnConfirmPopUp();
		cscockpitOrderTabPage.clickOKBtnOnRMAPopUp();
		cscockpitOrderTabPage.clickMenuButton();
		cscockpitOrderTabPage.clickLogoutButton();


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
		randomConsultantList =  null;
		storeFrontHomePage = new StoreFrontHomePage(driver);
		while(true){
			randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_CONSULTANT_WITH_ORDERS_AND_AUTOSHIPS_RFO,"40"),RFO_DB);
			accountID=String.valueOf(getValueFromQueryResult(randomConsultantList, "AccountID"));
			emailIdFromAccountIdList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_EMAIL_ID_FROM_ACCOUNT_ID,accountID),RFO_DB);
			consultantEmailID=(String) getValueFromQueryResult(emailIdFromAccountIdList, "EmailAddress");   
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
		cscockpitCustomerSearchTabPage = cscockpitLoginPage.clickLoginBtn();
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
		s_assert.assertTrue(cscockpitCheckoutTabPage.getDeliverModeTypeInCheckoutTab().contains("UPS Ground (HD)"),"CSCockpit checkout tab delivery mode type expected = UPS Ground (HD) and on UI = " +cscockpitCheckoutTabPage.getDeliverModeTypeInCheckoutTab());
		s_assert.assertTrue(cscockpitCheckoutTabPage.isCommissionDatePopulatedInCheckoutTab(), "Commission date is not populated in UI");
		cscockpitCheckoutTabPage.clickPlaceOrderButtonInCheckoutTab();
		s_assert.assertTrue(cscockpitCheckoutTabPage.verifySelectPaymentDetailsPopupInCheckoutTab(), "Select payment details popup is not present");
		cscockpitCheckoutTabPage.clickOkButtonOfSelectPaymentDetailsPopupInCheckoutTab();
		cscockpitCheckoutTabPage.clickAddNewPaymentAddressInCheckoutTab();
		cscockpitCheckoutTabPage.enterBillingInfo();
		cscockpitCheckoutTabPage.clickSaveAddNewPaymentProfilePopUP();
		cscockpitCheckoutTabPage.enterCVVValueInCheckoutTab(TestConstants.SECURITY_CODE);
		cscockpitCheckoutTabPage.clickUseThisCardBtnInCheckoutTab();
		cscockpitCheckoutTabPage.clickPlaceOrderButtonInCheckoutTab();
		orderNumber = cscockpitOrderTabPage.getOrderNumberInOrderTab();
		logger.info("RETURN ORDER NUMBER IS "+orderNumber);		
		s_assert.assertTrue(cscockpitOrderSearchTabPage.clickOrderLinkOnOrderSearchTabAndVerifyOrderDetailsPage(orderNumber)>0, "Order was NOT placed successfully,expected count after placing order in order detail items section >0 but actual count on UI = "+cscockpitOrderTabPage.getCountOfOrdersOnOrdersDetailsPageAfterPlacingOrder());
		cscockpitOrderTabPage.clickRefundOrderBtnOnOrderTab();
		s_assert.assertTrue(cscockpitOrderTabPage.verifyRefundRequestPopUpPresent(),"Refund Request PopUp not present on us");
		cscockpitOrderTabPage.checkReturnHandlingCheckboxInPopUp();
		cscockpitOrderTabPage.selectRefundReasonOnRefundPopUp("Test");
		cscockpitOrderTabPage.selectFirstReturnActionOnRefundPopUp();
		cscockpitOrderTabPage.selectFirstRefundTypeOnRefundPopUp();
		cscockpitOrderTabPage.clickCreateBtnOnRefundPopUp();
		refundTotal = cscockpitOrderTabPage.getRefundTotalFromRefundConfirmationPopUp();
		//Verify Handling and tax on Handling details.
		s_assert.assertTrue(cscockpitOrderTabPage.getShippingAndHandlingVariousSectionTaxInPopup(refundSubtotal).contains("0.00"),"Refund subtotal expected on UI for ca "+0.00+"while Actual on UI is "+cscockpitOrderTabPage.getShippingAndHandlingVariousSectionTaxInPopup(refundSubtotal));
		s_assert.assertTrue(cscockpitOrderTabPage.getShippingAndHandlingVariousSectionTaxInPopup(refundDiscount).contains("0.00"),"Refund discount expected on UI for ca "+0.00+"while Actual on UI is "+cscockpitOrderTabPage.getShippingAndHandlingVariousSectionTaxInPopup(refundDiscount));
		s_assert.assertFalse(cscockpitOrderTabPage.getShippingAndHandlingVariousSectionTaxInPopup(refundTax).contains("0.00"),"Refund tax expected on UI for ca "+0.24+"while Actual on UI is "+cscockpitOrderTabPage.getShippingAndHandlingVariousSectionTaxInPopup(refundTax));
		s_assert.assertTrue(cscockpitOrderTabPage.getShippingAndHandlingVariousSectionTaxInPopup(refundShipping).contains("0.00"),"Refund shipping expected on UI for ca "+0.00+"while Actual on UI is "+cscockpitOrderTabPage.getShippingAndHandlingVariousSectionTaxInPopup(refundShipping));
		s_assert.assertFalse(cscockpitOrderTabPage.getShippingAndHandlingVariousSectionTaxInPopup(refundHandling).contains("0.00"),"Refund handling expected on UI for ca "+2.50+"while Actual on UI is "+cscockpitOrderTabPage.getShippingAndHandlingVariousSectionTaxInPopup(refundHandling));
		s_assert.assertTrue(cscockpitOrderTabPage.getShippingAndHandlingVariousSectionTaxInPopup(restockingFee).contains("0.00"),"Restocking fee expected on UI for ca "+0.00+"while Actual on UI is "+cscockpitOrderTabPage.getShippingAndHandlingVariousSectionTaxInPopup(restockingFee));
		s_assert.assertTrue(cscockpitOrderTabPage.getShippingAndHandlingVariousSectionTaxInPopup(restockingFeeTax).contains("0.00"),"Restocking fee tax expected on UI for ca "+0.00+"while Actual on UI is "+cscockpitOrderTabPage.getShippingAndHandlingVariousSectionTaxInPopup(restockingFeeTax));

		cscockpitOrderTabPage.clickConfirmBtnOnConfirmPopUp();
		cscockpitOrderTabPage.clickOKBtnOnRMAPopUp();
		s_assert.assertTrue(cscockpitOrderTabPage.isReturnRequestSectionDisplayed(), "Return request section is NOT displayed");
		cscockpitOrderTabPage.clickRMATreeBtnUnderReturnRequestOnOrderTab();
		cscockpitOrderTabPage.clickRMATreeBtnUnderReturnRequestOnOrderTab();
		cscockpitOrderTabPage.clickMenuButton();
		cscockpitOrderTabPage.clickLogoutButton();

		driver.get(driver.getStoreFrontURL()+"/ca");
		storeFrontHomePage = new StoreFrontHomePage(driver);
		storeFrontConsultantPage = storeFrontHomePage.loginAsConsultant(consultantEmailID, password);
		storeFrontConsultantPage.clickOnWelcomeDropDown();
		storeFrontOrdersPage =  storeFrontConsultantPage.clickOrdersLinkPresentOnWelcomeDropDown();
		s_assert.assertTrue(storeFrontOrdersPage.verifyOrdersPageIsDisplayed(),"Orders page has not been displayed");
		orderHistoryNumber = storeFrontOrdersPage.getFirstOrderNumberFromOrderHistory();
		s_assert.assertTrue(orderHistoryNumber.contains(orderNumber.split("\\-")[0].trim()),"CSCockpit Order number expected = "+orderNumber.split("\\-")[0].trim()+" and on UI = " +orderHistoryNumber);
		logout();
		driver.get(driver.getCSCockpitURL());		
		cscockpitCustomerSearchTabPage = cscockpitLoginPage.clickLoginBtn();
		cscockpitCustomerSearchTabPage.clickOrderSearchTab();
		cscockpitOrderSearchTabPage.enterOrderNumberInOrderSearchTab(orderNumber);
		cscockpitOrderSearchTabPage.clickSearchBtn();
		cscockpitOrderSearchTabPage.clickOrderLinkOnOrderSearchTabAndVerifyOrderDetailsPage(orderNumber);
		cscockpitOrderTabPage.clickRefundOrderBtnOnOrderTab();
		s_assert.assertTrue(cscockpitOrderTabPage.verifyRefundRequestPopUpPresent(),"Refund Request PopUp not present on us");
		s_assert.assertTrue(cscockpitOrderTabPage.isRMAIdTxtPresent(), "RMA id text is not present in refund request popup.");
		s_assert.assertTrue(cscockpitOrderTabPage.getOrderNumbertxtFromRefundRequestPopup().contains(orderNumber.split("\\-")[0].trim()), "Order Number = "+orderNumber+" is not present in refund request popup");
		cscockpitOrderTabPage.checkProductInfoCheckboxInPopUp();
		cscockpitOrderTabPage.selectReturnQuantityOnPopUp(returnQuantity);
		cscockpitOrderTabPage.checkRestockingFeeCheckboxInPopUp();
		cscockpitOrderTabPage.selectRefundReasonOnRefundPopUp("Test");
		cscockpitOrderTabPage.selectFirstReturnActionOnRefundPopUp();
		cscockpitOrderTabPage.selectFirstRefundTypeOnRefundPopUp();
		cscockpitOrderTabPage.clickCreateBtnOnRefundPopUp();
		cscockpitOrderTabPage.clickConfirmBtnOnConfirmPopUp();
		cscockpitOrderTabPage.clickOKBtnOnRMAPopUp();
		cscockpitOrderTabPage.clickMenuButton();
		cscockpitOrderTabPage.clickLogoutButton();

		driver.get(driver.getStoreFrontURL()+"/ca");
		storeFrontHomePage = new StoreFrontHomePage(driver);
		storeFrontConsultantPage = storeFrontHomePage.loginAsConsultant(consultantEmailID, password);
		storeFrontConsultantPage.clickOnWelcomeDropDown();
		storeFrontOrdersPage =  storeFrontConsultantPage.clickOrdersLinkPresentOnWelcomeDropDown();
		s_assert.assertTrue(storeFrontOrdersPage.verifyOrdersPageIsDisplayed(),"Orders page has not been displayed");
		orderHistoryNumber = storeFrontOrdersPage.getFirstOrderNumberFromOrderHistory();
		s_assert.assertTrue(orderHistoryNumber.contains(orderNumber.split("\\-")[0].trim()),"CSCockpit Order number expected = "+orderNumber.split("\\-")[0].trim()+" and on UI = " +orderHistoryNumber);
		s_assert.assertAll();		
	}

	// Hybris Project-2409:Check Return order information on the Order history page. (Returning Consultant Autoship Order)
	@Test
	public void testCheckReturnOrderInformationOnTheOrderHistoryPage_2409() throws InterruptedException{
		String randomCustomerSequenceNumber = null;
		String consultantEmailID = null;
		String accountID = null;
		RFO_DB = driver.getDBNameRFO();

		//-------------------FOR US----------------------------------
		driver.get(driver.getStoreFrontURL()+"/us");
		List<Map<String, Object>> randomConsultantList =  null;
		while(true){
			randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_CONSULTANT_WITH_ORDERS_AND_AUTOSHIPS_RFO,"236"),RFO_DB);
			consultantEmailID = (String) getValueFromQueryResult(randomConsultantList, "UserName");  
			accountID = String.valueOf(getValueFromQueryResult(randomConsultantList, "AccountID"));	
			logger.info("Account Id of user "+accountID);
			storeFrontConsultantPage = storeFrontHomePage.loginAsConsultant(consultantEmailID, password);
			boolean isLoginError = driver.getCurrentUrl().contains("error");
			if(isLoginError){
				logger.info("Login error for the user "+consultantEmailID);
				driver.get(driver.getStoreFrontURL()+"/us");
			}
			else{
				storeFrontConsultantPage.clickOnWelcomeDropDown();
				storeFrontAccountInfoPage = storeFrontConsultantPage.clickAccountInfoLinkPresentOnWelcomeDropDown();
				storeFrontAccountInfoPage.clickOnYourAccountDropdown();
				storeFrontAccountInfoPage.clickOnAutoShipStatus();
				if(storeFrontAccountInfoPage.verifyCRPCancelled()==true){
					logout();
					driver.get(driver.getStoreFrontURL()+"/us");
					continue;
				}else{
					break;
				}
			}

			break;
		}
		logger.info("login is successful");	
		logout();
		//get emailId of username
		List<Map<String, Object>> randomConsultantUsernameList =  null;
		randomConsultantUsernameList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_EMAIL_ID_FROM_ACCOUNT_ID,accountID),RFO_DB);
		consultantEmailID = String.valueOf(getValueFromQueryResult(randomConsultantUsernameList, "EmailAddress"));  
		logger.info("emaild of consultant username "+consultantEmailID);	
		driver.get(driver.getCSCockpitURL());		
		cscockpitCustomerSearchTabPage = cscockpitLoginPage.clickLoginBtn();
		cscockpitCustomerSearchTabPage.selectCustomerTypeFromDropDownInCustomerSearchTab("CONSULTANT");
		cscockpitCustomerSearchTabPage.selectCountryFromDropDownInCustomerSearchTab("United States");
		cscockpitCustomerSearchTabPage.selectAccountStatusFromDropDownInCustomerSearchTab("Active");
		cscockpitCustomerSearchTabPage.enterEmailIdInSearchFieldInCustomerSearchTab(consultantEmailID);
		cscockpitCustomerSearchTabPage.clickSearchBtn();
		randomCustomerSequenceNumber = String.valueOf(cscockpitCustomerSearchTabPage.getRandomCustomerFromSearchResult());
		cscockpitCustomerSearchTabPage.clickCIDNumberInCustomerSearchTab(randomCustomerSequenceNumber);
		cscockpitCustomerTabPage.getAndClickAutoshipIDHavingTypeAsCRPAutoshipAndStatusIsPending();
		cscockpitAutoshipTemplateTabPage.clickRunNowButtonOnAutoshipTemplateTab();
		String confirmationMsgOfRunNow = cscockpitAutoshipTemplateTabPage.getConfirmMessageAfterClickOnRunNowBtn();
		String orderNumberFromMsg = cscockpitAutoshipTemplateTabPage.getOrderNumberFromConfirmationMsg(confirmationMsgOfRunNow);
		cscockpitAutoshipTemplateTabPage.clickOkConfirmMessagePopUp();
		cscockpitAutoshipTemplateTabPage.clickCustomerTab();
		cscockpitCustomerTabPage.clickOrderNumberInCustomerOrders(orderNumberFromMsg);
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
		String RMANumber = cscockpitOrderTabPage.getRMANumberFromPopup().split("\\:")[1].trim();
		cscockpitOrderTabPage.clickOKBtnOnRMAPopUp();
		s_assert.assertTrue(cscockpitOrderTabPage.isReturnRequestSectionDisplayed(), "Return request section is NOT displayed");
		//verification on storefornt
		driver.get(driver.getStoreFrontURL()+"/us");
		storeFrontConsultantPage = storeFrontHomePage.loginAsConsultant(consultantEmailID, password);
		storeFrontConsultantPage.clickOnWelcomeDropDown();
		storeFrontOrdersPage = storeFrontConsultantPage.clickOrdersLinkPresentOnWelcomeDropDown();
		s_assert.assertTrue(storeFrontOrdersPage.verifyRMANumberIsPresentInReturnOrderHistory(RMANumber),"RMA number "+RMANumber+" Is not present in order history page");
		s_assert.assertTrue(storeFrontOrdersPage.getGranTotalOfRMANumberInReturnOrderHistory(RMANumber).trim().contains(refundTotal.trim()),"refund total expected "+refundTotal+" actual on UI "+storeFrontOrdersPage.getGranTotalOfRMANumberInReturnOrderHistory(RMANumber).trim());
		logout();

		//-------------------FOR CA----------------------------------
		driver.get(driver.getStoreFrontURL()+"/ca");
		while(true){
			randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_CONSULTANT_WITH_ORDERS_AND_AUTOSHIPS_RFO,"40"),RFO_DB);
			consultantEmailID = (String) getValueFromQueryResult(randomConsultantList, "UserName");  
			accountID = String.valueOf(getValueFromQueryResult(randomConsultantList, "AccountID"));	
			logger.info("Account Id of user "+accountID);
			storeFrontConsultantPage = storeFrontHomePage.loginAsConsultant(consultantEmailID, password);
			boolean isLoginError = driver.getCurrentUrl().contains("error");
			if(isLoginError){
				logger.info("Login error for the user "+consultantEmailID);
				driver.get(driver.getStoreFrontURL()+"/us");
			}
			else{
				storeFrontConsultantPage.clickOnWelcomeDropDown();
				storeFrontAccountInfoPage = storeFrontConsultantPage.clickAccountInfoLinkPresentOnWelcomeDropDown();
				storeFrontAccountInfoPage.clickOnYourAccountDropdown();
				storeFrontAccountInfoPage.clickOnAutoShipStatus();
				if(storeFrontAccountInfoPage.verifyCRPCancelled()==true){
					logout();
					driver.get(driver.getStoreFrontURL()+"/us");
					continue;
				}else{
					break;
				}
			}

			break;
		}
		logger.info("login is successful");	
		logout();
		//get emailId of username
		randomConsultantUsernameList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_EMAIL_ID_FROM_ACCOUNT_ID,accountID),RFO_DB);
		consultantEmailID = String.valueOf(getValueFromQueryResult(randomConsultantUsernameList, "EmailAddress"));  
		logger.info("emaild of consultant username "+consultantEmailID);	
		driver.get(driver.getCSCockpitURL());		
		cscockpitCustomerSearchTabPage = cscockpitLoginPage.clickLoginBtn();
		cscockpitCustomerSearchTabPage.selectCustomerTypeFromDropDownInCustomerSearchTab("CONSULTANT");
		cscockpitCustomerSearchTabPage.selectCountryFromDropDownInCustomerSearchTab("Canada");
		cscockpitCustomerSearchTabPage.selectAccountStatusFromDropDownInCustomerSearchTab("Active");
		cscockpitCustomerSearchTabPage.enterEmailIdInSearchFieldInCustomerSearchTab(consultantEmailID);
		cscockpitCustomerSearchTabPage.clickSearchBtn();
		randomCustomerSequenceNumber = String.valueOf(cscockpitCustomerSearchTabPage.getRandomCustomerFromSearchResult());
		cscockpitCustomerSearchTabPage.clickCIDNumberInCustomerSearchTab(randomCustomerSequenceNumber);
		cscockpitCustomerTabPage.getAndClickAutoshipIDHavingTypeAsCRPAutoshipAndStatusIsPending();
		cscockpitAutoshipTemplateTabPage.clickRunNowButtonOnAutoshipTemplateTab();
		confirmationMsgOfRunNow = cscockpitAutoshipTemplateTabPage.getConfirmMessageAfterClickOnRunNowBtn();
		orderNumberFromMsg = cscockpitAutoshipTemplateTabPage.getOrderNumberFromConfirmationMsg(confirmationMsgOfRunNow);
		cscockpitAutoshipTemplateTabPage.clickOkConfirmMessagePopUp();
		cscockpitAutoshipTemplateTabPage.clickCustomerTab();
		cscockpitCustomerTabPage.clickOrderNumberInCustomerOrders(orderNumberFromMsg);
		cscockpitOrderTabPage.clickRefundOrderBtnOnOrderTab();
		isReturnCompleteOrderChecked = cscockpitOrderTabPage.checkReturnCompleteOrderChkBoxOnRefundPopUpAndReturnTrueElseFalse();
		if(isReturnCompleteOrderChecked==true){
			s_assert.assertTrue(cscockpitOrderTabPage.areAllCheckBoxesGettingDisabledAfterCheckingReturnCompleteOrderChkBox(), "All other checkboxes are not disabled after checking 'Return Complete Order' checkbox");
		}
		cscockpitOrderTabPage.selectRefundReasonOnRefundPopUp("Test");
		cscockpitOrderTabPage.selectFirstReturnActionOnRefundPopUp();
		cscockpitOrderTabPage.selectFirstRefundTypeOnRefundPopUp();
		cscockpitOrderTabPage.clickCreateBtnOnRefundPopUp();
		refundTotal = cscockpitOrderTabPage.getRefundTotalFromRefundConfirmationPopUp();
		cscockpitOrderTabPage.clickConfirmBtnOnConfirmPopUp();
		RMANumber = cscockpitOrderTabPage.getRMANumberFromPopup().split("\\:")[1].trim();
		cscockpitOrderTabPage.clickOKBtnOnRMAPopUp();
		s_assert.assertTrue(cscockpitOrderTabPage.isReturnRequestSectionDisplayed(), "Return request section is NOT displayed");
		//verification on storefornt
		driver.get(driver.getStoreFrontURL()+"/us");
		storeFrontConsultantPage = storeFrontHomePage.loginAsConsultant(consultantEmailID, password);
		storeFrontConsultantPage.clickOnWelcomeDropDown();
		storeFrontOrdersPage = storeFrontConsultantPage.clickOrdersLinkPresentOnWelcomeDropDown();
		s_assert.assertTrue(storeFrontOrdersPage.verifyRMANumberIsPresentInReturnOrderHistory(RMANumber),"RMA number "+RMANumber+" Is not present in order history page on CA");
		s_assert.assertTrue(storeFrontOrdersPage.getGranTotalOfRMANumberInReturnOrderHistory(RMANumber).trim().contains(refundTotal.trim()),"refund total expected on CA "+refundTotal+" actual on UI "+storeFrontOrdersPage.getGranTotalOfRMANumberInReturnOrderHistory(RMANumber).trim());
		logout();
		s_assert.assertAll();
	}

	//Hybris Project-1700:To verify create a CRP Autoship
	@Test
	public void testVerifyCreateCRPAutoship_1700() throws InterruptedException{
		RFO_DB = driver.getDBNameRFO();
		String randomCustomerSequenceNumber = null;
		String SKUValue = null;
		String consultantEmailID=null;
		//-------------------FOR US----------------------------------
		List<Map<String, Object>> randomConsultantList =  null;
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
		//cancel CRP
		storeFrontConsultantPage.clickOnWelcomeDropDown();
		storeFrontAccountInfoPage=storeFrontConsultantPage.clickAccountInfoLinkPresentOnWelcomeDropDown();
		storeFrontAccountInfoPage.clickOnYourAccountDropdown();
		storeFrontAccountInfoPage.clickOnAutoShipStatus();
		storeFrontAccountInfoPage.clickOnCancelMyCRP();
		//validate CRP has been cancelled..
		s_assert.assertTrue(storeFrontAccountInfoPage.verifyCRPCancelled(), "CRP has not been cancelled");
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
		cscockpitCustomerSearchTabPage.clickAndReturnCIDNumberInCustomerSearchTab(randomCustomerSequenceNumber);
		cscockpitCustomerTabPage.clickCreateAutoshipTemplateBtn();
		cscockpitAutoshipCartTabPage.selectValueFromSortByDDInCartTab("Consultant Price: Low to High");
		cscockpitAutoshipCartTabPage.selectCatalogFromDropDownInCartTab();	
		SKUValue = cscockpitAutoshipCartTabPage.getCustomerSKUValueInCartTab("1");
		cscockpitAutoshipCartTabPage.searchSKUValueInCartTab(SKUValue);
		boolean isProductPresent = cscockpitAutoshipCartTabPage.clickAddToCartBtnInCartTabForThreeProducts();
		if(isProductPresent==true){
			cscockpitAutoshipCartTabPage.clickCheckoutBtnInCartTab();
			//assert SV value less than 100 popup
			s_assert.assertTrue(cscockpitAutoshipTemplateTabPage.verifyThresholdPopupForUS(),"Threshold popup does not appear");
			cscockpitAutoshipTemplateTabPage.clickOKOfThresholdPopupInAutoshipTemplateTab();
		}
		//add to cart product SV value >100
		cscockpitAutoshipCartTabPage.clearCatalogSearchFieldAndClickSearchBtn();
		cscockpitAutoshipCartTabPage.selectValueFromSortByDDInCartTab("Price: High to Low");
		cscockpitAutoshipCartTabPage.selectCatalogFromDropDownInCartTab();	
		String randomProductSequenceNumber = String.valueOf(cscockpitAutoshipCartTabPage.getRandomProductWithSKUFromSearchResult()); 
		SKUValue = cscockpitAutoshipCartTabPage.getCustomerSKUValueInCartTab(randomProductSequenceNumber);
		cscockpitAutoshipCartTabPage.searchSKUValueInCartTab(SKUValue);
		cscockpitAutoshipCartTabPage.clickAddToCartBtnInCartTab();
		cscockpitAutoshipCartTabPage.clickCheckoutBtnInCartTab();
		//add new billing profile
		cscockpitAutoshipTemplateUpdateTabPage.clickAddNewPaymentAddressInCheckoutTab();
		cscockpitAutoshipTemplateUpdateTabPage.enterBillingInfo();
		cscockpitAutoshipTemplateUpdateTabPage.clickSaveAddNewPaymentProfilePopUP();
		cscockpitAutoshipTemplateUpdateTabPage.enterCVVValueInCheckoutTab(TestConstants.SECURITY_CODE);
		cscockpitAutoshipTemplateUpdateTabPage.clickUseThisCardBtnInCheckoutTab();
		cscockpitAutoshipTemplateUpdateTabPage.clickCreateAutoshipTemplateBtn();
		String dueDate = cscockpitAutoshipTemplateTabPage.getNextDueDateOfCRPAutoship();
		String nextDueDate = cscockpitAutoshipTemplateTabPage.convertCRPDateToFormat(dueDate);
		String currentPSTDate = cscockpitAutoshipTemplateTabPage.getPSTDate();
		String PSTDate = cscockpitAutoshipTemplateTabPage.convertPSTDateToNextDueDateFormat(currentPSTDate);
		int day = Integer.parseInt(currentPSTDate.split("\\ ")[0]);
		String oneMonthExtendedDate = null;
		if(day<=17){
			oneMonthExtendedDate = cscockpitAutoshipTemplateTabPage.getOneMonthOutDate(PSTDate);
		}else{
			oneMonthExtendedDate = cscockpitAutoshipTemplateTabPage.getOneMonthOutDateAfter17(PSTDate);
		}
		s_assert.assertTrue(oneMonthExtendedDate.trim().contains(nextDueDate), "Expected next due date of CRP is "+nextDueDate+"Actual on UI in Cscockpit "+oneMonthExtendedDate.trim());
		driver.get(driver.getStoreFrontURL()+"/us");
		storeFrontConsultantPage = storeFrontHomePage.loginAsConsultant(consultantEmailID, password);
		storeFrontConsultantPage.clickOnWelcomeDropDown();
		storeFrontAccountInfoPage = storeFrontConsultantPage.clickAccountInfoLinkPresentOnWelcomeDropDown();
		storeFrontAccountInfoPage.clickOnYourAccountDropdown();
		storeFrontAccountInfoPage.clickOnAutoShipStatus();
		String crpDate = storeFrontAccountInfoPage.getNextDueDateOfCRPTemplate();
		s_assert.assertTrue(crpDate.trim().contains(oneMonthExtendedDate.trim()), "Expected next due date of CRP is "+oneMonthExtendedDate+"Actual on UI in storeFront "+crpDate);
		logout();

		//-------------------FOR CA----------------------------------
		randomConsultantList =  null;
		driver.get(driver.getStoreFrontURL()+"/us");
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
		//cancel CRP
		storeFrontConsultantPage.clickOnWelcomeDropDown();
		storeFrontAccountInfoPage=storeFrontConsultantPage.clickAccountInfoLinkPresentOnWelcomeDropDown();
		storeFrontAccountInfoPage.clickOnYourAccountDropdown();
		storeFrontAccountInfoPage.clickOnAutoShipStatus();
		storeFrontAccountInfoPage.clickOnCancelMyCRP();
		//validate CRP has been cancelled..
		s_assert.assertTrue(storeFrontAccountInfoPage.verifyCRPCancelled(), "CRP has not been cancelled");
		logout();
		logger.info("login is successful");
		driver.get(driver.getCSCockpitURL());
		cscockpitCustomerSearchTabPage = cscockpitLoginPage.clickLoginBtn();
		cscockpitCustomerSearchTabPage.selectCustomerTypeFromDropDownInCustomerSearchTab("CONSULTANT");
		cscockpitCustomerSearchTabPage.selectCountryFromDropDownInCustomerSearchTab("Canada");
		cscockpitCustomerSearchTabPage.selectAccountStatusFromDropDownInCustomerSearchTab("Active");
		cscockpitCustomerSearchTabPage.enterEmailIdInSearchFieldInCustomerSearchTab(consultantEmailID);
		cscockpitCustomerSearchTabPage.clickSearchBtn();
		randomCustomerSequenceNumber = String.valueOf(cscockpitCustomerSearchTabPage.getRandomCustomerFromSearchResult());
		cscockpitCustomerSearchTabPage.clickAndReturnCIDNumberInCustomerSearchTab(randomCustomerSequenceNumber);
		cscockpitCustomerTabPage.clickCreateAutoshipTemplateBtn();
		cscockpitAutoshipCartTabPage.selectValueFromSortByDDInCartTab("Price: Low to High");
		cscockpitAutoshipCartTabPage.selectCatalogFromDropDownInCartTab();	
		SKUValue = cscockpitAutoshipCartTabPage.getCustomerSKUValueInCartTab("1");
		cscockpitAutoshipCartTabPage.searchSKUValueInCartTab(SKUValue);
		isProductPresent = cscockpitAutoshipCartTabPage.clickAddToCartBtnInCartTabForThreeProducts();
		if(isProductPresent==true){
			System.out.println("Found true");
			cscockpitAutoshipCartTabPage.clickCheckoutBtnInCartTab();
			//assert SV value less than 100 popup
			s_assert.assertTrue(cscockpitAutoshipTemplateTabPage.verifyThresholdPopupForCA(),"Threshold popup does not appear");
			cscockpitAutoshipTemplateTabPage.clickOKOfThresholdPopupInAutoshipTemplateTab();
		}
		//add to cart product SV value >100
		cscockpitAutoshipCartTabPage.clearCatalogSearchFieldAndClickSearchBtn();
		cscockpitAutoshipCartTabPage.selectValueFromSortByDDInCartTab("Price: High to Low");
		cscockpitAutoshipCartTabPage.selectCatalogFromDropDownInCartTab();	
		randomProductSequenceNumber = String.valueOf(cscockpitAutoshipCartTabPage.getRandomProductWithSKUFromSearchResult()); 
		SKUValue = cscockpitAutoshipCartTabPage.getCustomerSKUValueInCartTab(randomProductSequenceNumber);
		cscockpitAutoshipCartTabPage.searchSKUValueInCartTab(SKUValue);
		cscockpitAutoshipCartTabPage.clickAddToCartBtnInCartTab();
		cscockpitAutoshipCartTabPage.clickCheckoutBtnInCartTab();
		//add new billing profile
		cscockpitAutoshipTemplateUpdateTabPage.clickAddNewPaymentAddressInCheckoutTab();
		cscockpitAutoshipTemplateUpdateTabPage.enterBillingInfo();
		cscockpitAutoshipTemplateUpdateTabPage.clickSaveAddNewPaymentProfilePopUP();
		cscockpitAutoshipTemplateUpdateTabPage.enterCVVValueInCheckoutTab(TestConstants.SECURITY_CODE);
		cscockpitAutoshipTemplateUpdateTabPage.clickUseThisCardBtnInCheckoutTab();
		cscockpitAutoshipTemplateUpdateTabPage.clickCreateAutoshipTemplateBtn();
		dueDate = cscockpitAutoshipTemplateTabPage.getNextDueDateOfCRPAutoship();
		currentPSTDate = cscockpitAutoshipTemplateTabPage.getPSTDate();
		PSTDate = cscockpitAutoshipTemplateTabPage.convertPSTDateToNextDueDateFormat(currentPSTDate);
		nextDueDate = cscockpitAutoshipTemplateTabPage.convertCRPDateToFormat(dueDate);
		day = Integer.parseInt(currentPSTDate.split("\\ ")[0]);
		oneMonthExtendedDate = null;
		if(day<=17){
			oneMonthExtendedDate = cscockpitAutoshipTemplateTabPage.getOneMonthOutDate(PSTDate);
		}else{
			oneMonthExtendedDate = cscockpitAutoshipTemplateTabPage.getOneMonthOutDateAfter17(PSTDate);
		}
		s_assert.assertTrue(oneMonthExtendedDate.trim().contains(nextDueDate), "Expected next due date of CRP is "+nextDueDate+"Actual on UI in Cscockpit "+oneMonthExtendedDate.trim());
		driver.get(driver.getStoreFrontURL()+"/us");
		storeFrontConsultantPage = storeFrontHomePage.loginAsConsultant(consultantEmailID, password);
		storeFrontConsultantPage.clickOnWelcomeDropDown();
		storeFrontAccountInfoPage = storeFrontConsultantPage.clickAccountInfoLinkPresentOnWelcomeDropDown();
		storeFrontAccountInfoPage.clickOnYourAccountDropdown();
		storeFrontAccountInfoPage.clickOnAutoShipStatus();
		crpDate = storeFrontAccountInfoPage.getNextDueDateOfCRPTemplate();
		s_assert.assertTrue(crpDate.trim().contains(oneMonthExtendedDate.trim()), "Expected next due date of CRP is "+oneMonthExtendedDate+"Actual on UI in storeFront "+crpDate);
		logout();
		s_assert.assertAll();
	}

	//Hybris Project-1928:Verify the Find Customer Search Criteria
	@Test
	public void testVerifyFindCustomerSearchCriteria_1928() throws InterruptedException{
		String randomCustomerSequenceNumber = null;
		String consultantEmailID = null;
		String accountID=null;
		String firstNameFromDatabase=null;
		String lastNameFromDatabase=null;
		String fullNameFromDatabase=null;
		String firstName = "First Name";
		String lastName = "Last Name";
		String emailAddress = "Email Address";
		RFO_DB = driver.getDBNameRFO();
		cscockpitCustomerSearchTabPage = cscockpitLoginPage.clickLoginBtn();
		//get valid cid from database.
		cscockpitCustomerSearchTabPage.selectCustomerTypeFromDropDownInCustomerSearchTab("CONSULTANT");
		cscockpitCustomerSearchTabPage.selectAccountStatusFromDropDownInCustomerSearchTab("Active");
		cscockpitCustomerSearchTabPage.clickSearchBtn();

		String randomValidCustomerSequenceNumber = String.valueOf(cscockpitCustomerSearchTabPage.getRandomCustomerFromSearchResult());
		String validCid=cscockpitCustomerSearchTabPage.getCIDNumberInCustomerSearchTab(randomValidCustomerSequenceNumber);
		consultantEmailID=cscockpitCustomerSearchTabPage.getEmailIdOfTheCustomerInCustomerSearchTab(randomValidCustomerSequenceNumber);

		//Get account Id from account number.
		List<Map<String, Object>>accountIdList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_ACCOUNT_ID_FOR_PWS,validCid),RFO_DB);
		accountID = String.valueOf(getValueFromQueryResult(accountIdList, "AccountID"));

		//Get first name and last name from database.
		List<Map<String, Object>>userDetailsList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_USER_DETAILS_FROM_ACCOUNTID_RFO,accountID),RFO_DB);
		firstNameFromDatabase = (String) getValueFromQueryResult(userDetailsList, "FirstName");
		lastNameFromDatabase = (String) getValueFromQueryResult(userDetailsList, "LastName");
		fullNameFromDatabase=firstNameFromDatabase+" "+lastNameFromDatabase;

		//driver.get(driver.getCSCockpitURL());		

		//get invalid cid from database.
		cscockpitCustomerSearchTabPage.selectCustomerTypeFromDropDownInCustomerSearchTab("CONSULTANT");
		cscockpitCustomerSearchTabPage.selectAccountStatusFromDropDownInCustomerSearchTab("Inactive");
		cscockpitCustomerSearchTabPage.clickSearchBtn();
		randomCustomerSequenceNumber = String.valueOf(cscockpitCustomerSearchTabPage.getRandomCustomerFromSearchResult());
		String invalidCid=cscockpitCustomerSearchTabPage.getCIDNumberInCustomerSearchTab(randomCustomerSequenceNumber);
		cscockpitCustomerSearchTabPage.selectCustomerTypeFromDropDownAsSelectInCustomerSearchTab("Select");
		cscockpitCustomerSearchTabPage.selectAccountStatusFromDropDownInCustomerSearchTab("Select");
		cscockpitCustomerSearchTabPage.clickSearchBtn();
		//enter invalid cid and click search.
		cscockpitCustomerSearchTabPage.enterCIDInOrderSearchTab(TestConstants.INVALID_CUSTOMER_NAME);
		cscockpitCustomerSearchTabPage.clickSearchBtn();
		s_assert.assertTrue(cscockpitCustomerSearchTabPage.isNoResultDisplayed(),"CSCockpit Sponser CID search expected = No Results and on UI = Results are displayed");
		//Enter first name and click search
		cscockpitCustomerSearchTabPage.enterCIDInOrderSearchTab(firstNameFromDatabase);
		cscockpitCustomerSearchTabPage.clickSearchBtn();
		s_assert.assertTrue(cscockpitCustomerSearchTabPage.verifyCIDSectionIsPresentWithClickableLinks(), "CID section is not present with clickable links for valid first name Entered");
		s_assert.assertTrue(cscockpitCustomerSearchTabPage.verifySectionsIsPresentInOrderSearchTab(firstName), "First name section is not present in customer search tab for valid first name Entered");
		s_assert.assertTrue(cscockpitCustomerSearchTabPage.verifySectionsIsPresentInOrderSearchTab(lastName), "Last name section is not present in customer search tab for valid first name Entered");
		s_assert.assertTrue(cscockpitCustomerSearchTabPage.verifySectionsIsPresentInOrderSearchTab(emailAddress), "Email address section is not present in customer search tab for valid first name Entered");

		//Enter last name and click search
		cscockpitCustomerSearchTabPage.enterCIDInOrderSearchTab(lastNameFromDatabase);
		cscockpitCustomerSearchTabPage.clickSearchBtn();
		s_assert.assertTrue(cscockpitCustomerSearchTabPage.verifyCIDSectionIsPresentWithClickableLinks(), "CID section is not present with clickable links for valid last name Entered");
		s_assert.assertTrue(cscockpitCustomerSearchTabPage.verifySectionsIsPresentInOrderSearchTab(firstName), "First name section is not present in customer search tab for valid last name Entered");
		s_assert.assertTrue(cscockpitCustomerSearchTabPage.verifySectionsIsPresentInOrderSearchTab(lastName), "Last name section is not present in customer search tab for valid last name Entered");
		s_assert.assertTrue(cscockpitCustomerSearchTabPage.verifySectionsIsPresentInOrderSearchTab(emailAddress), "Email address section is not present in customer search tab for valid last name Entered");

		//Enter Full name and click search
		cscockpitCustomerSearchTabPage.enterCIDInOrderSearchTab(fullNameFromDatabase);
		cscockpitCustomerSearchTabPage.clickSearchBtn();
		s_assert.assertTrue(cscockpitCustomerSearchTabPage.verifyCIDSectionIsPresentWithClickableLinks(), "CID section is not present with clickable links for valid full name Entered");
		s_assert.assertTrue(cscockpitCustomerSearchTabPage.verifySectionsIsPresentInOrderSearchTab(firstName), "First name section is not present in customer search tab for valid full name Entered");
		s_assert.assertTrue(cscockpitCustomerSearchTabPage.verifySectionsIsPresentInOrderSearchTab(lastName), "Last name section is not present in customer search tab for valid full name Entered");
		s_assert.assertTrue(cscockpitCustomerSearchTabPage.verifySectionsIsPresentInOrderSearchTab(emailAddress), "Email address section is not present in customer search tab for valid full name Entered");

		//Search cid as customer Name which in database Having account status pending or terminated click search.
		cscockpitCustomerSearchTabPage.enterCIDInOrderSearchTab(invalidCid);
		cscockpitCustomerSearchTabPage.clickSearchBtn();
		s_assert.assertTrue(cscockpitCustomerSearchTabPage.verifyCIDSectionIsPresentWithClickableLinks(), "CID section is not present with clickable links for invalid CID Entered");
		s_assert.assertTrue(cscockpitCustomerSearchTabPage.verifySectionsIsPresentInOrderSearchTab(firstName), "First name section is not present in customer search tab for invalid CID Entered");
		s_assert.assertTrue(cscockpitCustomerSearchTabPage.verifySectionsIsPresentInOrderSearchTab(lastName), "Last name section is not present in customer search tab for invalid CID Entered");
		s_assert.assertTrue(cscockpitCustomerSearchTabPage.verifySectionsIsPresentInOrderSearchTab(emailAddress), "Email address section is not present in customer search tab for invalid CID Entered");

		//Search cid as customer CID which in not database and click search.
		cscockpitCustomerSearchTabPage.enterCIDInOrderSearchTab("000000000");
		cscockpitCustomerSearchTabPage.clickSearchBtn();
		s_assert.assertTrue(cscockpitCustomerSearchTabPage.isNoResultDisplayed(),"CSCockpit Sponser CID search expected = No Results and on UI = Results are displayed");

		//Search cid as customer CID which in database and click search.
		cscockpitCustomerSearchTabPage.enterCIDInOrderSearchTab(validCid);
		cscockpitCustomerSearchTabPage.clickSearchBtn();
		s_assert.assertTrue(cscockpitCustomerSearchTabPage.verifyCIDSectionIsPresentWithClickableLinks(), "CID section is not present with clickable links for valid CID Entered");
		s_assert.assertTrue(cscockpitCustomerSearchTabPage.verifySectionsIsPresentInOrderSearchTab(firstName), "First name section is not present in customer search tab for valid CID Entered");
		s_assert.assertTrue(cscockpitCustomerSearchTabPage.verifySectionsIsPresentInOrderSearchTab(lastName), "Last name section is not present in customer search tab for valid CID Entered");
		s_assert.assertTrue(cscockpitCustomerSearchTabPage.verifySectionsIsPresentInOrderSearchTab(emailAddress), "Email address section is not present in customer search tab for valid CID Entered");

		//Clear CID field
		cscockpitCustomerSearchTabPage.clearCidFieldInOrderSearchTab();
		cscockpitCustomerSearchTabPage.clickSearchBtn();
		//Enter invalid email address and click search.
		cscockpitCustomerSearchTabPage.enterEmailIdInSearchFieldInCustomerSearchTab(TestConstants.INVALID_EMAIL_ADDRESS);
		cscockpitCustomerSearchTabPage.clickSearchBtn();
		s_assert.assertTrue(cscockpitCustomerSearchTabPage.isNoResultDisplayed(),"CSCockpit Sponser CID search expected = No Results and on UI = Results are displayed");

		//Enter valid email address and click search.
		cscockpitCustomerSearchTabPage.enterEmailIdInSearchFieldInCustomerSearchTab(consultantEmailID);
		cscockpitCustomerSearchTabPage.clickSearchBtn();
		s_assert.assertTrue(cscockpitCustomerSearchTabPage.verifyCIDSectionIsPresentWithClickableLinks(), "CID section is not present with clickable links for valid email address");
		s_assert.assertTrue(cscockpitCustomerSearchTabPage.verifySectionsIsPresentInOrderSearchTab(firstName), "First name section is not present in customer search tab for valid email address");
		s_assert.assertTrue(cscockpitCustomerSearchTabPage.verifySectionsIsPresentInOrderSearchTab(lastName), "Last name section is not present in customer search tab for valid email address");
		s_assert.assertTrue(cscockpitCustomerSearchTabPage.verifySectionsIsPresentInOrderSearchTab(emailAddress), "Email address section is not present in customer search tab for valid email address");
		s_assert.assertTrue(cscockpitCustomerSearchTabPage.verifyCountForCustomerFromSearchResult(), "count of result per page is greater than 20");

		//Clear Email address field
		cscockpitCustomerSearchTabPage.clearEmailAddressFieldInOrderSearchTab();
		cscockpitCustomerSearchTabPage.clickSearchBtn();
		//Select one by one status from customer type dropdown and click search.
		cscockpitCustomerSearchTabPage.selectCustomerTypeFromDropDownInCustomerSearchTab("PC");
		cscockpitCustomerSearchTabPage.clickSearchBtn();
		s_assert.assertTrue(cscockpitCustomerSearchTabPage.verifyCIDSectionIsPresentWithClickableLinks(), "CID section is not present with clickable links for valid email address");
		s_assert.assertTrue(cscockpitCustomerSearchTabPage.verifySectionsIsPresentInOrderSearchTab(firstName), "First name section is not present in customer search tab for valid email address");
		s_assert.assertTrue(cscockpitCustomerSearchTabPage.verifySectionsIsPresentInOrderSearchTab(lastName), "Last name section is not present in customer search tab for valid email address");
		s_assert.assertTrue(cscockpitCustomerSearchTabPage.verifySectionsIsPresentInOrderSearchTab(emailAddress), "Email address section is not present in customer search tab for valid email address");
		s_assert.assertTrue(cscockpitCustomerSearchTabPage.verifyCountForCustomerFromSearchResult(), "count of result per page is greater than 20");

		cscockpitCustomerSearchTabPage.selectCustomerTypeFromDropDownInCustomerSearchTab("CONSULTANT");
		cscockpitCustomerSearchTabPage.clickSearchBtn();
		s_assert.assertTrue(cscockpitCustomerSearchTabPage.verifyCIDSectionIsPresentWithClickableLinks(), "CID section is not present with clickable links for valid email address");
		s_assert.assertTrue(cscockpitCustomerSearchTabPage.verifySectionsIsPresentInOrderSearchTab(firstName), "First name section is not present in customer search tab for valid email address");
		s_assert.assertTrue(cscockpitCustomerSearchTabPage.verifySectionsIsPresentInOrderSearchTab(lastName), "Last name section is not present in customer search tab for valid email address");
		s_assert.assertTrue(cscockpitCustomerSearchTabPage.verifySectionsIsPresentInOrderSearchTab(emailAddress), "Email address section is not present in customer search tab for valid email address");
		s_assert.assertTrue(cscockpitCustomerSearchTabPage.verifyCountForCustomerFromSearchResult(), "count of result per page is greater than 20");

		cscockpitCustomerSearchTabPage.selectCustomerTypeFromDropDownInCustomerSearchTab("RETAIL");
		cscockpitCustomerSearchTabPage.clickSearchBtn();
		s_assert.assertTrue(cscockpitCustomerSearchTabPage.verifyCIDSectionIsPresentWithClickableLinks(), "CID section is not present with clickable links for valid email address");
		s_assert.assertTrue(cscockpitCustomerSearchTabPage.verifySectionsIsPresentInOrderSearchTab(firstName), "First name section is not present in customer search tab for valid email address");
		s_assert.assertTrue(cscockpitCustomerSearchTabPage.verifySectionsIsPresentInOrderSearchTab(lastName), "Last name section is not present in customer search tab for valid email address");
		s_assert.assertTrue(cscockpitCustomerSearchTabPage.verifySectionsIsPresentInOrderSearchTab(emailAddress), "Email address section is not present in customer search tab for valid email address");
		s_assert.assertTrue(cscockpitCustomerSearchTabPage.verifyCountForCustomerFromSearchResult(), "count of result per page is greater than 20");

		//select customer type as Select.
		cscockpitCustomerSearchTabPage.selectCustomerTypeFromDropDownAsSelectInCustomerSearchTab("Select");

		//Select one by one status from account status dropdown and click search.
		cscockpitCustomerSearchTabPage.selectAccountStatusFromDropDownInCustomerSearchTab("Active");
		cscockpitCustomerSearchTabPage.clickSearchBtn();
		s_assert.assertTrue(cscockpitCustomerSearchTabPage.verifyCIDSectionIsPresentWithClickableLinks(), "CID section is not present with clickable links for valid email address");
		s_assert.assertTrue(cscockpitCustomerSearchTabPage.verifySectionsIsPresentInOrderSearchTab(firstName), "First name section is not present in customer search tab for valid email address");
		s_assert.assertTrue(cscockpitCustomerSearchTabPage.verifySectionsIsPresentInOrderSearchTab(lastName), "Last name section is not present in customer search tab for valid email address");
		s_assert.assertTrue(cscockpitCustomerSearchTabPage.verifySectionsIsPresentInOrderSearchTab(emailAddress), "Email address section is not present in customer search tab for valid email address");
		s_assert.assertTrue(cscockpitCustomerSearchTabPage.verifyCountForCustomerFromSearchResult(), "count of result per page is greater than 20");

		cscockpitCustomerSearchTabPage.selectAccountStatusFromDropDownInCustomerSearchTab("Inactive");
		cscockpitCustomerSearchTabPage.clickSearchBtn();
		s_assert.assertTrue(cscockpitCustomerSearchTabPage.verifyCIDSectionIsPresentWithClickableLinks(), "CID section is not present with clickable links for valid email address");
		s_assert.assertTrue(cscockpitCustomerSearchTabPage.verifySectionsIsPresentInOrderSearchTab(firstName), "First name section is not present in customer search tab for valid email address");
		s_assert.assertTrue(cscockpitCustomerSearchTabPage.verifySectionsIsPresentInOrderSearchTab(lastName), "Last name section is not present in customer search tab for valid email address");
		s_assert.assertTrue(cscockpitCustomerSearchTabPage.verifySectionsIsPresentInOrderSearchTab(emailAddress), "Email address section is not present in customer search tab for valid email address");
		s_assert.assertTrue(cscockpitCustomerSearchTabPage.verifyCountForCustomerFromSearchResult(), "count of result per page is greater than 20");

		//select account status as Select.
		cscockpitCustomerSearchTabPage.selectAccountStatusFromDropDownInCustomerSearchTab("Select");

		//select country one by one and assert values.
		cscockpitCustomerSearchTabPage.selectCountryFromDropDownInCustomerSearchTab(TestConstants.COUNTRY_DD_VALUE_US);
		cscockpitCustomerSearchTabPage.clickSearchBtn();
		s_assert.assertTrue(cscockpitCustomerSearchTabPage.verifyCIDSectionIsPresentWithClickableLinks(), "CID section is not present with clickable links for valid email address");
		s_assert.assertTrue(cscockpitCustomerSearchTabPage.verifySectionsIsPresentInOrderSearchTab(firstName), "First name section is not present in customer search tab for valid email address");
		s_assert.assertTrue(cscockpitCustomerSearchTabPage.verifySectionsIsPresentInOrderSearchTab(lastName), "Last name section is not present in customer search tab for valid email address");
		s_assert.assertTrue(cscockpitCustomerSearchTabPage.verifySectionsIsPresentInOrderSearchTab(emailAddress), "Email address section is not present in customer search tab for valid email address");
		s_assert.assertTrue(cscockpitCustomerSearchTabPage.verifyCountForCustomerFromSearchResult(), "count of result per page is greater than 20");

		cscockpitCustomerSearchTabPage.selectCountryFromDropDownInCustomerSearchTab(TestConstants.COUNTRY_DD_VALUE_CA);
		cscockpitCustomerSearchTabPage.clickSearchBtn();
		s_assert.assertTrue(cscockpitCustomerSearchTabPage.verifyCIDSectionIsPresentWithClickableLinks(), "CID section is not present with clickable links for valid email address");
		s_assert.assertTrue(cscockpitCustomerSearchTabPage.verifySectionsIsPresentInOrderSearchTab(firstName), "First name section is not present in customer search tab for valid email address");
		s_assert.assertTrue(cscockpitCustomerSearchTabPage.verifySectionsIsPresentInOrderSearchTab(lastName), "Last name section is not present in customer search tab for valid email address");
		s_assert.assertTrue(cscockpitCustomerSearchTabPage.verifySectionsIsPresentInOrderSearchTab(emailAddress), "Email address section is not present in customer search tab for valid email address");
		s_assert.assertTrue(cscockpitCustomerSearchTabPage.verifyCountForCustomerFromSearchResult(), "count of result per page is greater than 20");
		s_assert.assertAll();
	}

	//Hybris Project-4722:Verify that ' Pulse fee' product not available to add into CRP cart.
	@Test
	public void testVerifyPulseFeeProductNotAvailableToAddIntoCrpCart_4722() throws InterruptedException{
		String randomCustomerSequenceNumber = null;
		String consultantEmailID = null;
		String accountID = null;
		RFO_DB = driver.getDBNameRFO();
		//-------------------FOR US----------------------------------
		driver.get(driver.getStoreFrontURL()+"/us");
		List<Map<String, Object>> randomConsultantList =  null;
		while(true){
			randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_CONSULTANT_WITH_ORDERS_AND_AUTOSHIPS_RFO,"236"),RFO_DB);
			consultantEmailID = (String) getValueFromQueryResult(randomConsultantList, "UserName");  
			accountID = String.valueOf(getValueFromQueryResult(randomConsultantList, "AccountID")); 
			logger.info("Account Id of user "+accountID);
			storeFrontConsultantPage = storeFrontHomePage.loginAsConsultant(consultantEmailID, password);
			boolean isLoginError = driver.getCurrentUrl().contains("error");
			if(isLoginError){
				logger.info("Login error for the user "+consultantEmailID);
				driver.get(driver.getStoreFrontURL()+"/us");
				continue;
			}
			else{
				storeFrontConsultantPage.clickOnWelcomeDropDown();
				storeFrontAccountInfoPage = storeFrontConsultantPage.clickAccountInfoLinkPresentOnWelcomeDropDown();
				storeFrontAccountInfoPage.clickOnYourAccountDropdown();
				storeFrontAccountInfoPage.clickOnAutoShipStatus();
				if(storeFrontAccountInfoPage.verifyCRPCancelled()==true){
					logout();
					driver.get(driver.getStoreFrontURL()+"/us");
					continue;
				}else{
					break;
				}
			}
		}
		logger.info("login is successful"); 
		logout();
		//get emailId of username
		List<Map<String, Object>> randomConsultantUsernameList =  null;
		randomConsultantUsernameList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_EMAIL_ID_FROM_ACCOUNT_ID,accountID),RFO_DB);
		consultantEmailID = String.valueOf(getValueFromQueryResult(randomConsultantUsernameList, "EmailAddress"));  
		logger.info("emaild of consultant username "+consultantEmailID);
		driver.get(driver.getCSCockpitURL());  
		cscockpitCustomerSearchTabPage = cscockpitLoginPage.clickLoginBtn();
		cscockpitCustomerSearchTabPage.selectCustomerTypeFromDropDownInCustomerSearchTab("CONSULTANT");
		cscockpitCustomerSearchTabPage.selectCountryFromDropDownInCustomerSearchTab("United States");
		cscockpitCustomerSearchTabPage.selectAccountStatusFromDropDownInCustomerSearchTab("Active");
		cscockpitCustomerSearchTabPage.enterEmailIdInSearchFieldInCustomerSearchTab(consultantEmailID);
		cscockpitCustomerSearchTabPage.clickSearchBtn();
		randomCustomerSequenceNumber = String.valueOf(cscockpitCustomerSearchTabPage.getRandomCustomerFromSearchResult());
		cscockpitCustomerSearchTabPage.clickCIDNumberInCustomerSearchTab(randomCustomerSequenceNumber);
		cscockpitCustomerTabPage.getAndClickAutoshipIDHavingTypeAsCRPAutoshipAndStatusIsPending();
		cscockpitAutoshipTemplateTabPage.clickEditTemplateLinkInAutoshipTemplateTab();
		cscockpitAutoshipTemplateTabPage.clickAddMoreLinesLinkInAutoShipTemplateTab();
		cscockpitCartTabPage.searchSKUValueInCartTab("Pulse");
		cscockpitCartTabPage.clickAddToCartForSingleProduct();
		cscockpitCartTabPage.verifyProductNotAvailablePopUp();
		s_assert.assertTrue(cscockpitCartTabPage.verifyProductNotAvailablePopUp(),"pulse product is available to be added to cart in crp autoship");
		s_assert.assertAll();
	}

	//Hybris Project-1719:To verify PC perks/Pulse Run autoship from autoship template
	@Test
	public void testVerifyPCPerksRunAutoshipFromAutoshipTemplate_1719() throws InterruptedException{
		String randomCustomerSequenceNumber = null;
		String pcEmailID = null;
		String accountID = null;
		RFO_DB = driver.getDBNameRFO();
		driver.get(driver.getStoreFrontURL()+"/us");
		List<Map<String, Object>> randomPCList =  null;
		while(true){
			randomPCList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_PC_WITH_ORDERS_AND_AUTOSHIPS_RFO,"236"),RFO_DB);
			pcEmailID = (String) getValueFromQueryResult(randomPCList, "UserName");  
			accountID = String.valueOf(getValueFromQueryResult(randomPCList, "AccountID")); 
			logger.info("Account Id of user "+accountID);
			storeFrontPCUserPage = storeFrontHomePage.loginAsPCUser(pcEmailID, password);
			boolean isLoginError = driver.getCurrentUrl().contains("error");
			if(isLoginError){
				logger.info("Login error for the user "+pcEmailID);
				driver.get(driver.getStoreFrontURL()+"/us");
			}
			else
				break;
		}
		logger.info("login is successful"); 
		logout();
		//get emailId of username
		List<Map<String, Object>> randomConsultantUsernameList =  null;
		randomConsultantUsernameList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_EMAIL_ID_FROM_ACCOUNT_ID,accountID),RFO_DB);
		pcEmailID = String.valueOf(getValueFromQueryResult(randomConsultantUsernameList, "EmailAddress"));  
		logger.info("emaild of consultant username "+pcEmailID); 
		driver.get(driver.getCSCockpitURL());  
		cscockpitCustomerSearchTabPage = cscockpitLoginPage.clickLoginBtn();
		cscockpitCustomerSearchTabPage.selectCustomerTypeFromDropDownInCustomerSearchTab("PC");
		cscockpitCustomerSearchTabPage.selectCountryFromDropDownInCustomerSearchTab("United States");
		cscockpitCustomerSearchTabPage.selectAccountStatusFromDropDownInCustomerSearchTab("Active");
		cscockpitCustomerSearchTabPage.enterEmailIdInSearchFieldInCustomerSearchTab(pcEmailID);
		cscockpitCustomerSearchTabPage.clickSearchBtn();
		randomCustomerSequenceNumber = String.valueOf(cscockpitCustomerSearchTabPage.getRandomCustomerFromSearchResult());
		cscockpitCustomerSearchTabPage.clickCIDNumberInCustomerSearchTab(randomCustomerSequenceNumber);
		cscockpitCustomerTabPage.getAndClickAutoshipIDHavingTypeAsPCAutoshipAndStatusIsPending();
		cscockpitAutoshipTemplateTabPage.clickEditTemplateLinkInAutoshipTemplateTab();
		cscockpitAutoshipTemplateTabPage.clickRunNowButtonOnAutoshipTemplateTab();
		s_assert.assertTrue(cscockpitAutoshipTemplateTabPage.verifyConfirmMessageOrReRunPopupAfterClickOnRunNowBtn(), "Run now confirmation message popup is not present");
		s_assert.assertTrue(cscockpitAutoshipTemplateTabPage.verifyOkBtnOnConfirmMessagePopUp(), "OK btn is not present on confirm message popup");
		s_assert.assertAll();
	}

	//Hybris Project-2028:To verify Partially return functionality
	@Test
	public void testVerifyPartiallyReturnFunctionality_2028() throws InterruptedException{
		String randomCustomerSequenceNumber = null;
		String consultantEmailID = null;
		String orderNumber = null;
		String returnQuantity = "1";
		String orderHistoryNumber = null;
		String randomProductSequenceNumber = null;
		String SKUValue = null;
		String accountID = null;
		String refundSubtotal="Refunded SubTotals";
		String refundDiscount="Refunded Discount";
		String refundTax="Refunded Tax";
		String refundShipping="Refunded Shipping";
		String refundHandling="Refunded Handling";
		String restockingFee="Restocking Fee";
		String restockingFeeTax="Restocking Fee Tax";
		String qtyOfFirstProduct=null;
		String qtyOfSecondProduct=null;
		String qtyOfThirdProduct=null;
		String qtyOfFourthProduct=null;
		RFO_DB = driver.getDBNameRFO();
		//-------------------FOR US----------------------------------
		driver.get(driver.getStoreFrontURL()+"/us");
		List<Map<String, Object>> randomConsultantList =  null;
		List<Map<String, Object>> emailIdFromAccountIdList =  null;
		storeFrontHomePage = new StoreFrontHomePage(driver);
		while(true){
			randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_CONSULTANT_WITH_ORDERS_AND_AUTOSHIPS_RFO,"236"),RFO_DB);
			accountID=String.valueOf(getValueFromQueryResult(randomConsultantList, "AccountID"));
			emailIdFromAccountIdList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_EMAIL_ID_FROM_ACCOUNT_ID,accountID),RFO_DB);
			consultantEmailID=(String) getValueFromQueryResult(emailIdFromAccountIdList, "EmailAddress"); 
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
		cscockpitCartTabPage.clickAddToCartBtnTillProductAddedInCartTab();
		cscockpitCartTabPage.addProductToCartPageTillAtleastFourDistinctProducts();
		qtyOfFirstProduct=cscockpitCartTabPage.getProductCountFromcartPage("1");
		qtyOfSecondProduct=cscockpitCartTabPage.getProductCountFromcartPage("2");
		qtyOfThirdProduct=cscockpitCartTabPage.getProductCountFromcartPage("3");
		qtyOfFourthProduct=cscockpitCartTabPage.getProductCountFromcartPage("4");
		cscockpitCartTabPage.clickCheckoutBtnInCartTab();
		s_assert.assertTrue(cscockpitCheckoutTabPage.getCreditCardNumberInCheckoutTab().contains("************"),"CSCockpit checkout tab credit card number expected = ************ and on UI = " +cscockpitCheckoutTabPage.getCreditCardNumberInCheckoutTab());
		s_assert.assertTrue(cscockpitCheckoutTabPage.getDeliverModeTypeInCheckoutTab().contains("FedEx Ground (HD)"),"CSCockpit checkout tab delivery mode type expected = FedEx Ground (HD) and on UI = " +cscockpitCheckoutTabPage.getDeliverModeTypeInCheckoutTab());
		s_assert.assertTrue(cscockpitCheckoutTabPage.isCommissionDatePopulatedInCheckoutTab(), "Commission date is not populated in UI");
		cscockpitCheckoutTabPage.clickPlaceOrderButtonInCheckoutTab();
		s_assert.assertTrue(cscockpitCheckoutTabPage.verifySelectPaymentDetailsPopupInCheckoutTab(), "Select payment details popup is not present");
		cscockpitCheckoutTabPage.clickOkButtonOfSelectPaymentDetailsPopupInCheckoutTab();
		cscockpitCheckoutTabPage.clickAddNewPaymentAddressInCheckoutTab();
		cscockpitCheckoutTabPage.enterBillingInfo();
		cscockpitCheckoutTabPage.clickSaveAddNewPaymentProfilePopUP();
		cscockpitCheckoutTabPage.enterCVVValueInCheckoutTab(TestConstants.SECURITY_CODE);
		cscockpitCheckoutTabPage.clickUseThisCardBtnInCheckoutTab();
		cscockpitCheckoutTabPage.clickPlaceOrderButtonInCheckoutTab();
		orderNumber = cscockpitOrderTabPage.getOrderNumberInOrderTab();
		logger.info("PARTIALLY RETURN ORDER NUMBER IS "+orderNumber);		
		s_assert.assertTrue(cscockpitOrderSearchTabPage.clickOrderLinkOnOrderSearchTabAndVerifyOrderDetailsPage(orderNumber)>0, "Order was NOT placed successfully,expected count after placing order in order detail items section >0 but actual count on UI = "+cscockpitOrderTabPage.getCountOfOrdersOnOrdersDetailsPageAfterPlacingOrder());
		cscockpitOrderTabPage.clickRefundOrderBtnOnOrderTab();
		s_assert.assertTrue(cscockpitOrderTabPage.verifyRefundRequestPopUpPresent(),"Refund Request PopUp not present on us");
		cscockpitOrderTabPage.checkProductInfoCheckboxInPopUp("1");
		cscockpitOrderTabPage.selectReturnQuantityOnPopUp(qtyOfFirstProduct,"1");
		cscockpitOrderTabPage.checkRestockingFeeCheckboxInPopUp();
		cscockpitOrderTabPage.checkReturnShippingCheckboxInPopUp();
		cscockpitOrderTabPage.checkReturnHandlingCheckboxInPopUp();
		cscockpitOrderTabPage.selectRefundReasonOnRefundPopUp("Test");
		cscockpitOrderTabPage.selectFirstReturnActionOnRefundPopUp();
		cscockpitOrderTabPage.selectFirstRefundTypeOnRefundPopUp();
		cscockpitOrderTabPage.clickCreateBtnOnRefundPopUp();
		String refundTotal = cscockpitOrderTabPage.getRefundTotalFromRefundConfirmationPopUp();
		//Verify Shipping and tax on shipping details.
		s_assert.assertFalse(cscockpitOrderTabPage.getShippingAndHandlingVariousSectionTaxInPopup(refundSubtotal).contains("0.00"),"Refund subtotal expected on UI for US "+240.00+"while Actual on UI is "+cscockpitOrderTabPage.getShippingAndHandlingVariousSectionTaxInPopup(refundSubtotal));
		s_assert.assertFalse(cscockpitOrderTabPage.getShippingAndHandlingVariousSectionTaxInPopup(refundDiscount).contains("0.00"),"Refund discount expected on UI for US "+6.72+"while Actual on UI is "+cscockpitOrderTabPage.getShippingAndHandlingVariousSectionTaxInPopup(refundDiscount));
		s_assert.assertFalse(cscockpitOrderTabPage.getShippingAndHandlingVariousSectionTaxInPopup(refundShipping).contains("0.00"),"Refund shipping expected on UI for US "+19.00+"while Actual on UI is "+cscockpitOrderTabPage.getShippingAndHandlingVariousSectionTaxInPopup(refundShipping));
		s_assert.assertFalse(cscockpitOrderTabPage.getShippingAndHandlingVariousSectionTaxInPopup(refundHandling).contains("0.00"),"Refund Handling expected on UI for US "+2.50+"while Actual on UI is "+cscockpitOrderTabPage.getShippingAndHandlingVariousSectionTaxInPopup(refundHandling));
		s_assert.assertFalse(cscockpitOrderTabPage.getShippingAndHandlingVariousSectionTaxInPopup(restockingFee).contains("0.00"),"Restocking fee expected on UI for US "+23.33+"while Actual on UI is "+cscockpitOrderTabPage.getShippingAndHandlingVariousSectionTaxInPopup(restockingFee));
		s_assert.assertTrue(cscockpitOrderTabPage.getShippingAndHandlingVariousSectionTaxInPopup(restockingFeeTax).contains("0.00"),"Restocking fee tax expected on UI for US "+0.00+"while Actual on UI is "+cscockpitOrderTabPage.getShippingAndHandlingVariousSectionTaxInPopup(restockingFeeTax));
		cscockpitOrderTabPage.clickConfirmBtnOnConfirmPopUp();
		String rmaNumber=cscockpitOrderTabPage.getRMANumberFromPopup();
		cscockpitOrderTabPage.clickOKBtnOnRMAPopUp();
		s_assert.assertTrue(cscockpitOrderTabPage.isReturnRequestSectionDisplayed(), "Return request section is NOT displayed");
		cscockpitOrderTabPage.clickRMATreeBtnUnderReturnRequestOnOrderTab();
		cscockpitOrderTabPage.clickRMATreeBtnUnderReturnRequestOnOrderTab();
		cscockpitOrderTabPage.clickMenuButton();
		cscockpitOrderTabPage.clickLogoutButton();

		driver.get(driver.getStoreFrontURL()+"/us");
		storeFrontHomePage = new StoreFrontHomePage(driver);
		storeFrontConsultantPage = storeFrontHomePage.loginAsConsultant(consultantEmailID, password);
		storeFrontConsultantPage.clickOnWelcomeDropDown();
		storeFrontOrdersPage =  storeFrontConsultantPage.clickOrdersLinkPresentOnWelcomeDropDown();
		s_assert.assertTrue(storeFrontOrdersPage.verifyOrdersPageIsDisplayed(),"Orders page has not been displayed");
		orderHistoryNumber = storeFrontOrdersPage.getFirstOrderNumberFromOrderHistory();
		s_assert.assertTrue(orderHistoryNumber.contains(orderNumber.split("\\-")[0].trim()),"CSCockpit Order number expected = "+orderNumber.split("\\-")[0].trim()+" and on UI = " +orderHistoryNumber);
		logout();
		driver.get(driver.getCSCockpitURL());		
		cscockpitCustomerSearchTabPage = cscockpitLoginPage.clickLoginBtn();
		cscockpitCustomerSearchTabPage.clickOrderSearchTab();
		cscockpitOrderSearchTabPage.enterOrderNumberInOrderSearchTab(orderNumber);
		cscockpitOrderSearchTabPage.clickSearchBtn();
		cscockpitOrderSearchTabPage.clickOrderLinkOnOrderSearchTabAndVerifyOrderDetailsPage(orderNumber);
		cscockpitOrderTabPage.clickRefundOrderBtnOnOrderTab();
		s_assert.assertTrue(cscockpitOrderTabPage.verifyRefundRequestPopUpPresent(),"Refund Request PopUp not present on us");
		s_assert.assertTrue(cscockpitOrderTabPage.isRMAIdTxtPresent(), "RMA id text is not present in refund request popup.");
		s_assert.assertTrue(cscockpitOrderTabPage.getOrderNumbertxtFromRefundRequestPopup().contains(orderNumber.split("\\-")[0].trim()), "Order Number = "+orderNumber+" is not present in refund request popup");
		cscockpitOrderTabPage.checkReturnTaxOnlyCheckboxInPopUp("2");
		cscockpitOrderTabPage.selectRefundReasonOnRefundPopUp("Test");
		s_assert.assertTrue(cscockpitOrderTabPage.isProductCheckBoxesGettingDisabledAfterCheckingReturnTaxOnlyChkBox("2"), "Product checkboxe is not disabled after checking 'Return Tax Only' checkbox");
		cscockpitOrderTabPage.selectFirstReturnActionOnRefundPopUp();
		cscockpitOrderTabPage.selectFirstRefundTypeOnRefundPopUp();
		cscockpitOrderTabPage.clickCreateBtnOnRefundPopUp();
		cscockpitOrderTabPage.clickCloseRefundConfirmationPopUP();
		//Uncheck return tax popup
		cscockpitOrderTabPage.checkReturnTaxOnlyCheckboxInPopUp("2");
		//Select Another product
		cscockpitOrderTabPage.checkProductInfoCheckboxInPopUp("3");
		cscockpitOrderTabPage.selectReturnQuantityOnPopUp(qtyOfFourthProduct,"3");
		cscockpitOrderTabPage.checkRestockingFeeCheckboxInPopUp("3");
		cscockpitOrderTabPage.clickCreateBtnOnRefundPopUp();
		cscockpitOrderTabPage.clickConfirmBtnOnConfirmPopUp();
		String secondRMANumber=cscockpitOrderTabPage.getRMANumberFromPopup();
		cscockpitOrderTabPage.clickOKBtnOnRMAPopUp();
		s_assert.assertTrue(cscockpitOrderTabPage.isReturnRequestSectionDisplayed(), "Return request section is NOT displayed");
		cscockpitOrderTabPage.clickRMATreeBtnUnderReturnRequestOnOrderTab();
		cscockpitOrderTabPage.clickRMATreeBtnUnderReturnRequestOnOrderTab();
		cscockpitOrderTabPage.clickMenuButton();
		cscockpitOrderTabPage.clickLogoutButton();

		driver.get(driver.getStoreFrontURL()+"/us");
		storeFrontHomePage = new StoreFrontHomePage(driver);
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
			accountID=String.valueOf(getValueFromQueryResult(randomConsultantList, "AccountID"));
			emailIdFromAccountIdList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_EMAIL_ID_FROM_ACCOUNT_ID,accountID),RFO_DB);
			consultantEmailID=(String) getValueFromQueryResult(emailIdFromAccountIdList, "EmailAddress"); 
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
		cscockpitCustomerSearchTabPage = cscockpitLoginPage.clickLoginBtn();
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
		cscockpitCartTabPage.clickAddToCartBtnTillProductAddedInCartTab();
		cscockpitCartTabPage.addProductToCartPageTillAtleastFourDistinctProducts();
		qtyOfFirstProduct=cscockpitCartTabPage.getProductCountFromcartPage("1");
		qtyOfSecondProduct=cscockpitCartTabPage.getProductCountFromcartPage("2");
		qtyOfThirdProduct=cscockpitCartTabPage.getProductCountFromcartPage("3");
		qtyOfFourthProduct=cscockpitCartTabPage.getProductCountFromcartPage("4");
		cscockpitCartTabPage.clickCheckoutBtnInCartTab();
		s_assert.assertTrue(cscockpitCheckoutTabPage.getCreditCardNumberInCheckoutTab().contains("************"),"CSCockpit checkout tab credit card number expected = ************ and on UI = " +cscockpitCheckoutTabPage.getCreditCardNumberInCheckoutTab());
		//s_assert.assertTrue(cscockpitCheckoutTabPage.getDeliverModeTypeInCheckoutTab().contains("UPS Ground (HD)"),"CSCockpit checkout tab delivery mode type expected = UPS Ground (HD) and on UI = " +cscockpitCheckoutTabPage.getDeliverModeTypeInCheckoutTab());
		s_assert.assertTrue(cscockpitCheckoutTabPage.isCommissionDatePopulatedInCheckoutTab(), "Commission date is not populated in UI");
		cscockpitCheckoutTabPage.clickPlaceOrderButtonInCheckoutTab();
		s_assert.assertTrue(cscockpitCheckoutTabPage.verifySelectPaymentDetailsPopupInCheckoutTab(), "Select payment details popup is not present");
		cscockpitCheckoutTabPage.clickOkButtonOfSelectPaymentDetailsPopupInCheckoutTab();
		cscockpitCheckoutTabPage.clickAddNewPaymentAddressInCheckoutTab();
		cscockpitCheckoutTabPage.enterBillingInfo();
		cscockpitCheckoutTabPage.clickSaveAddNewPaymentProfilePopUP();
		cscockpitCheckoutTabPage.enterCVVValueInCheckoutTab(TestConstants.SECURITY_CODE);
		cscockpitCheckoutTabPage.clickUseThisCardBtnInCheckoutTab();
		cscockpitCheckoutTabPage.clickPlaceOrderButtonInCheckoutTab();
		orderNumber = cscockpitOrderTabPage.getOrderNumberInOrderTab();
		logger.info("PARTIALLY RETURN ORDER NUMBER IS "+orderNumber);		
		s_assert.assertTrue(cscockpitOrderSearchTabPage.clickOrderLinkOnOrderSearchTabAndVerifyOrderDetailsPage(orderNumber)>0, "Order was NOT placed successfully,expected count after placing order in order detail items section >0 but actual count on UI = "+cscockpitOrderTabPage.getCountOfOrdersOnOrdersDetailsPageAfterPlacingOrder());
		cscockpitOrderTabPage.clickRefundOrderBtnOnOrderTab();
		s_assert.assertTrue(cscockpitOrderTabPage.verifyRefundRequestPopUpPresent(),"Refund Request PopUp not present on us");
		cscockpitOrderTabPage.checkProductInfoCheckboxInPopUp("1");
		cscockpitOrderTabPage.selectReturnQuantityOnPopUp(qtyOfFirstProduct,"1");
		cscockpitOrderTabPage.checkRestockingFeeCheckboxInPopUp();
		cscockpitOrderTabPage.checkReturnShippingCheckboxInPopUp();
		cscockpitOrderTabPage.checkReturnHandlingCheckboxInPopUp();
		cscockpitOrderTabPage.selectRefundReasonOnRefundPopUp("Test");
		cscockpitOrderTabPage.selectFirstReturnActionOnRefundPopUp();
		cscockpitOrderTabPage.selectFirstRefundTypeOnRefundPopUp();
		cscockpitOrderTabPage.clickCreateBtnOnRefundPopUp();
		refundTotal = cscockpitOrderTabPage.getRefundTotalFromRefundConfirmationPopUp();
		//Verify Shipping and tax on shipping details.
		s_assert.assertFalse(cscockpitOrderTabPage.getShippingAndHandlingVariousSectionTaxInPopup(refundSubtotal).contains("0.00"),"Refund subtotal expected on UI for US "+240.00+"while Actual on UI is "+cscockpitOrderTabPage.getShippingAndHandlingVariousSectionTaxInPopup(refundSubtotal));
		s_assert.assertFalse(cscockpitOrderTabPage.getShippingAndHandlingVariousSectionTaxInPopup(refundDiscount).contains("0.00"),"Refund discount expected on UI for US "+6.72+"while Actual on UI is "+cscockpitOrderTabPage.getShippingAndHandlingVariousSectionTaxInPopup(refundDiscount));
		s_assert.assertFalse(cscockpitOrderTabPage.getShippingAndHandlingVariousSectionTaxInPopup(refundShipping).contains("0.00"),"Refund shipping expected on UI for US "+19.00+"while Actual on UI is "+cscockpitOrderTabPage.getShippingAndHandlingVariousSectionTaxInPopup(refundShipping));
		s_assert.assertFalse(cscockpitOrderTabPage.getShippingAndHandlingVariousSectionTaxInPopup(refundHandling).contains("0.00"),"Refund Handling expected on UI for US "+2.50+"while Actual on UI is "+cscockpitOrderTabPage.getShippingAndHandlingVariousSectionTaxInPopup(refundHandling));
		s_assert.assertFalse(cscockpitOrderTabPage.getShippingAndHandlingVariousSectionTaxInPopup(restockingFee).contains("0.00"),"Restocking fee expected on UI for US "+23.33+"while Actual on UI is "+cscockpitOrderTabPage.getShippingAndHandlingVariousSectionTaxInPopup(restockingFee));
		s_assert.assertTrue(cscockpitOrderTabPage.getShippingAndHandlingVariousSectionTaxInPopup(restockingFeeTax).contains("0.00"),"Restocking fee tax expected on UI for US "+0.00+"while Actual on UI is "+cscockpitOrderTabPage.getShippingAndHandlingVariousSectionTaxInPopup(restockingFeeTax));
		cscockpitOrderTabPage.clickConfirmBtnOnConfirmPopUp();
		rmaNumber=cscockpitOrderTabPage.getRMANumberFromPopup();
		cscockpitOrderTabPage.clickOKBtnOnRMAPopUp();
		s_assert.assertTrue(cscockpitOrderTabPage.isReturnRequestSectionDisplayed(), "Return request section is NOT displayed");
		cscockpitOrderTabPage.clickRMATreeBtnUnderReturnRequestOnOrderTab();
		cscockpitOrderTabPage.clickRMATreeBtnUnderReturnRequestOnOrderTab();
		cscockpitOrderTabPage.clickMenuButton();
		cscockpitOrderTabPage.clickLogoutButton();

		driver.get(driver.getStoreFrontURL()+"/ca");
		storeFrontHomePage = new StoreFrontHomePage(driver);
		storeFrontConsultantPage = storeFrontHomePage.loginAsConsultant(consultantEmailID, password);
		storeFrontConsultantPage.clickOnWelcomeDropDown();
		storeFrontOrdersPage =  storeFrontConsultantPage.clickOrdersLinkPresentOnWelcomeDropDown();
		s_assert.assertTrue(storeFrontOrdersPage.verifyOrdersPageIsDisplayed(),"Orders page has not been displayed");
		orderHistoryNumber = storeFrontOrdersPage.getFirstOrderNumberFromOrderHistory();
		s_assert.assertTrue(orderHistoryNumber.contains(orderNumber.split("\\-")[0].trim()),"CSCockpit Order number expected = "+orderNumber.split("\\-")[0].trim()+" and on UI = " +orderHistoryNumber);
		logout();
		driver.get(driver.getCSCockpitURL());		
		cscockpitCustomerSearchTabPage = cscockpitLoginPage.clickLoginBtn();
		cscockpitCustomerSearchTabPage.clickOrderSearchTab();
		cscockpitOrderSearchTabPage.enterOrderNumberInOrderSearchTab(orderNumber);
		cscockpitOrderSearchTabPage.clickSearchBtn();
		cscockpitOrderSearchTabPage.clickOrderLinkOnOrderSearchTabAndVerifyOrderDetailsPage(orderNumber);
		cscockpitOrderTabPage.clickRefundOrderBtnOnOrderTab();
		s_assert.assertTrue(cscockpitOrderTabPage.verifyRefundRequestPopUpPresent(),"Refund Request PopUp not present on us");
		s_assert.assertTrue(cscockpitOrderTabPage.isRMAIdTxtPresent(), "RMA id text is not present in refund request popup.");
		s_assert.assertTrue(cscockpitOrderTabPage.getOrderNumbertxtFromRefundRequestPopup().contains(orderNumber.split("\\-")[0].trim()), "Order Number = "+orderNumber+" is not present in refund request popup");
		cscockpitOrderTabPage.checkReturnTaxOnlyCheckboxInPopUp("2");
		cscockpitOrderTabPage.selectRefundReasonOnRefundPopUp("Test");
		s_assert.assertTrue(cscockpitOrderTabPage.isProductCheckBoxesGettingDisabledAfterCheckingReturnTaxOnlyChkBox("2"), "Product checkboxe is not disabled after checking 'Return Tax Only' checkbox");
		cscockpitOrderTabPage.selectFirstReturnActionOnRefundPopUp();
		cscockpitOrderTabPage.selectFirstRefundTypeOnRefundPopUp();
		cscockpitOrderTabPage.clickCreateBtnOnRefundPopUp();
		cscockpitOrderTabPage.clickCloseRefundConfirmationPopUP();
		//Uncheck return tax popup
		cscockpitOrderTabPage.checkReturnTaxOnlyCheckboxInPopUp("2");
		//Select Another product
		cscockpitOrderTabPage.checkProductInfoCheckboxInPopUp("3");
		cscockpitOrderTabPage.selectReturnQuantityOnPopUp(qtyOfFourthProduct,"3");
		cscockpitOrderTabPage.checkRestockingFeeCheckboxInPopUp("3");
		cscockpitOrderTabPage.clickCreateBtnOnRefundPopUp();
		cscockpitOrderTabPage.clickConfirmBtnOnConfirmPopUp();
		secondRMANumber=cscockpitOrderTabPage.getRMANumberFromPopup();
		cscockpitOrderTabPage.clickOKBtnOnRMAPopUp();
		s_assert.assertTrue(cscockpitOrderTabPage.isReturnRequestSectionDisplayed(), "Return request section is NOT displayed");
		cscockpitOrderTabPage.clickRMATreeBtnUnderReturnRequestOnOrderTab();
		cscockpitOrderTabPage.clickRMATreeBtnUnderReturnRequestOnOrderTab();
		cscockpitOrderTabPage.clickMenuButton();
		cscockpitOrderTabPage.clickLogoutButton();

		driver.get(driver.getStoreFrontURL()+"/ca");
		storeFrontHomePage = new StoreFrontHomePage(driver);
		storeFrontConsultantPage = storeFrontHomePage.loginAsConsultant(consultantEmailID, password);
		storeFrontConsultantPage.clickOnWelcomeDropDown();
		storeFrontOrdersPage =  storeFrontConsultantPage.clickOrdersLinkPresentOnWelcomeDropDown();
		s_assert.assertTrue(storeFrontOrdersPage.verifyOrdersPageIsDisplayed(),"Orders page has not been displayed");
		orderHistoryNumber = storeFrontOrdersPage.getFirstOrderNumberFromOrderHistory();
		s_assert.assertTrue(orderHistoryNumber.contains(orderNumber.split("\\-")[0].trim()),"CSCockpit Order number expected = "+orderNumber.split("\\-")[0].trim()+" and on UI = " +orderHistoryNumber);
		s_assert.assertAll();		
	}

	// Hybris Project-3817:Perform Return on PARTIALLY SHIPPED Orders
	@Test
	public void testVerifyPartiallyShippedOrder_3817() throws InterruptedException{
		String randomCustomerSequenceNumber = null;
		String orderNumber = null;
		RFO_DB = driver.getDBNameRFO();

		cscockpitCustomerSearchTabPage = cscockpitLoginPage.clickLoginBtn();
		cscockpitCustomerSearchTabPage.clickFindOrderLinkOnLeftNavigation();
		cscockpitOrderSearchTabPage.selectOrderStatusOnOrderSearchTab("Partially_Shipped");
		cscockpitOrderSearchTabPage.clickSearchBtn();
		randomCustomerSequenceNumber = String.valueOf(cscockpitOrderSearchTabPage.getRandomCustomerFromSearchResult());
		orderNumber=cscockpitOrderSearchTabPage.clickAndReturnCIDNumberInCustomerSearchTab(randomCustomerSequenceNumber);
		//Return partially shipped order.
		cscockpitOrderTabPage.clickRefundOrderBtnOnOrderTab();
		s_assert.assertTrue(cscockpitOrderTabPage.verifyRefundRequestPopUpPresent(),"Refund Request PopUp not present.");
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
		String rmaNumber=cscockpitOrderTabPage.getRMANumberFromPopup();
		cscockpitOrderTabPage.clickOKBtnOnRMAPopUp();
		s_assert.assertTrue(cscockpitOrderTabPage.isReturnRequestSectionDisplayed(), "Return request section is NOT displayed");
		s_assert.assertTrue(cscockpitOrderTabPage.verifyReturnOrderRMANumberInOrderTab(rmaNumber), "Expected RMA number in return request section is "+rmaNumber+"Actual on UI, it is not present ");
		s_assert.assertAll();  
	}

	//Hybris Project-5286:to verify that Return Complete Order functionality
	@Test
	public void testVerifyReturnCompleteOrderFunctionality_5286() throws InterruptedException{
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

	//Hybris Project-1839:To verify the commission date change functionality in the order detail Page
	@Test
	public void testVerifyCommissionDateChangeFunctionalityInOrderDetailPage_1839(){
		String randomCustomerSequenceNumber = null;
		cscockpitCustomerSearchTabPage = cscockpitLoginPage.clickLoginBtn();
		cscockpitCustomerSearchTabPage.selectCustomerTypeFromDropDownInCustomerSearchTab("CONSULTANT");
		cscockpitCustomerSearchTabPage.selectAccountStatusFromDropDownInCustomerSearchTab("Active");
		cscockpitCustomerSearchTabPage.clickSearchBtn();
		randomCustomerSequenceNumber = String.valueOf(cscockpitCustomerSearchTabPage.getRandomCustomerFromSearchResult());
		cscockpitCustomerSearchTabPage.clickCIDNumberInCustomerSearchTab(randomCustomerSequenceNumber);
		cscockpitCustomerTabPage.clickFirstOrderInCustomerTab();
		s_assert.assertTrue(cscockpitOrderTabPage.isUpdateBtnDisabledInCommissionInfoSection(), "Update btn is enabled for user csagent");
		cscockpitOrderTabPage.clickMenuButton();
		cscockpitOrderTabPage.clickLogoutButton();
		//using admin credentials
		/*cscockpitLoginPage.enterUsername("admin");
				cscockpitCustomerSearchTabPage = cscockpitLoginPage.clickLoginBtn();
				cscockpitCustomerSearchTabPage.selectCustomerTypeFromDropDownInCustomerSearchTab("CONSULTANT");
				cscockpitCustomerSearchTabPage.selectAccountStatusFromDropDownInCustomerSearchTab("Active");
				cscockpitCustomerSearchTabPage.clickSearchBtn();
				randomCustomerSequenceNumber = String.valueOf(cscockpitCustomerSearchTabPage.getRandomCustomerFromSearchResult());
				cscockpitCustomerSearchTabPage.clickCIDNumberInCustomerSearchTab(randomCustomerSequenceNumber);
				cscockpitCustomerTabPage.clickFirstOrderInCustomerTab();
				s_assert.assertTrue(cscockpitOrderTabPage.isUpdateBtnDisabledInCommissionInfoSection(), "Update btn is enabled for user admin");
				cscockpitOrderTabPage.clickMenuButton();
				cscockpitOrderTabPage.clickLogoutButton();*/

		//for cscommissionadmin user
		cscockpitLoginPage.enterUsername(TestConstants.CS_COMMISION_ADMIN_USERNAME);
		cscockpitCustomerSearchTabPage = cscockpitLoginPage.clickLoginBtn();
		cscockpitCustomerSearchTabPage.selectCustomerTypeFromDropDownInCustomerSearchTab("CONSULTANT");
		cscockpitCustomerSearchTabPage.selectAccountStatusFromDropDownInCustomerSearchTab("Active");
		cscockpitCustomerSearchTabPage.clickSearchBtn();
		randomCustomerSequenceNumber = String.valueOf(cscockpitCustomerSearchTabPage.getRandomCustomerFromSearchResult());
		cscockpitCustomerSearchTabPage.clickCIDNumberInCustomerSearchTab(randomCustomerSequenceNumber);
		cscockpitCustomerTabPage.clickFirstOrderInCustomerTab();
		String commissionDate = cscockpitOrderTabPage.getCommissionDateFromCommissionInfoSection();
		String modifiedDate = cscockpitOrderTabPage.addOneMoreDayInCommissionInfoDate(commissionDate);
		cscockpitOrderTabPage.enterCommissionDate(modifiedDate);
		cscockpitOrderTabPage.clickUpdateBtnInCommissionInfoSection();
		String updatedCommissionDate  = cscockpitOrderTabPage.getCommissionDateFromCommissionInfoSection();
		s_assert.assertTrue(updatedCommissionDate.contains(modifiedDate), "Expected commission date is" + modifiedDate+" And actual on UI "+updatedCommissionDate+" for user cscommissionadmin");
		cscockpitOrderTabPage.clickMenuButton();
		cscockpitOrderTabPage.clickLogoutButton();

		//for cssalessupervisory user
		cscockpitLoginPage.enterUsername(TestConstants.CS_SALES_SUPERVISORY_USERNAME);
		cscockpitCustomerSearchTabPage = cscockpitLoginPage.clickLoginBtn();
		cscockpitCustomerSearchTabPage.selectCustomerTypeFromDropDownInCustomerSearchTab("CONSULTANT");
		cscockpitCustomerSearchTabPage.selectAccountStatusFromDropDownInCustomerSearchTab("Active");
		cscockpitCustomerSearchTabPage.clickSearchBtn();
		randomCustomerSequenceNumber = String.valueOf(cscockpitCustomerSearchTabPage.getRandomCustomerFromSearchResult());
		cscockpitCustomerSearchTabPage.clickCIDNumberInCustomerSearchTab(randomCustomerSequenceNumber);
		cscockpitCustomerTabPage.clickFirstOrderInCustomerTab();
		commissionDate = cscockpitOrderTabPage.getCommissionDateFromCommissionInfoSection();
		modifiedDate = cscockpitOrderTabPage.addOneMoreDayInCommissionInfoDate(commissionDate);
		cscockpitOrderTabPage.enterCommissionDate(modifiedDate);
		cscockpitOrderTabPage.clickUpdateBtnInCommissionInfoSection();
		updatedCommissionDate  = cscockpitOrderTabPage.getCommissionDateFromCommissionInfoSection();
		s_assert.assertTrue(updatedCommissionDate.contains(modifiedDate), "Expected commission date is" + modifiedDate+" And actual on UI "+updatedCommissionDate+" for user cssalessupervisory");
		s_assert.assertAll();
	}

	//Hybris Project-2023:To verify Return Tax only functionality at product level
	@Test
	public void testVerifyReturnTaxInlyFunctionalityAtProductLevel_2023() throws InterruptedException{
		String randomCustomerSequenceNumber = null;
		String consultantEmailID = null;
		String orderNumber = null;
		String SKUValue = null;
		String refundSubtotal="Refunded SubTotals";
		String refundDiscount="Refunded Discount";
		String refundShipping="Refunded Shipping";
		String refundHandling="Refunded Handling";
		String restockingFee="Restocking Fee";
		String restockingFeeTax="Restocking Fee Tax";
		String randomProductSequenceNumber = null;
		RFO_DB = driver.getDBNameRFO();

		List<Map<String, Object>> randomConsultantList =  null;
		String accountId = null;

		//-------------------FOR US----------------------------------
		driver.get(driver.getStoreFrontURL()+"/us");		
		while(true){
			randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_CONSULTANT_WITH_ORDERS_AND_AUTOSHIPS_RFO,"236"),RFO_DB);
			consultantEmailID = (String) getValueFromQueryResult(randomConsultantList, "UserName");  
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
		driver.get(driver.getCSCockpitURL());		
		cscockpitCustomerSearchTabPage = cscockpitLoginPage.clickLoginBtn();
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
		cscockpitCartTabPage.clickAddToCartBtnTillProductAddedInCartTab();
		cscockpitCartTabPage.addProductToCartPageTillAtleastFourDistinctProducts();
		cscockpitCartTabPage.clickCheckoutBtnInCartTab();
		s_assert.assertTrue(cscockpitCheckoutTabPage.getCreditCardNumberInCheckoutTab().contains("************"),"CSCockpit checkout tab credit card number expected = ************ and on UI = " +cscockpitCheckoutTabPage.getCreditCardNumberInCheckoutTab());
		s_assert.assertTrue(cscockpitCheckoutTabPage.getDeliverModeTypeInCheckoutTab().contains("FedEx Ground (HD)"),"CSCockpit checkout tab delivery mode type expected = FedEx Ground (HD) and on UI = " +cscockpitCheckoutTabPage.getDeliverModeTypeInCheckoutTab());
		s_assert.assertTrue(cscockpitCheckoutTabPage.isCommissionDatePopulatedInCheckoutTab(), "Commission date is not populated in UI");
		cscockpitCheckoutTabPage.clickPlaceOrderButtonInCheckoutTab();
		s_assert.assertTrue(cscockpitCheckoutTabPage.verifySelectPaymentDetailsPopupInCheckoutTab(), "Select payment details popup is not present");
		cscockpitCheckoutTabPage.clickOkButtonOfSelectPaymentDetailsPopupInCheckoutTab();
		cscockpitCheckoutTabPage.clickAddNewPaymentAddressInCheckoutTab();
		cscockpitCheckoutTabPage.enterBillingInfo();
		cscockpitCheckoutTabPage.clickSaveAddNewPaymentProfilePopUP();
		cscockpitCheckoutTabPage.enterCVVValueInCheckoutTab(TestConstants.SECURITY_CODE);
		cscockpitCheckoutTabPage.clickUseThisCardBtnInCheckoutTab();
		cscockpitCheckoutTabPage.clickPlaceOrderButtonInCheckoutTab();
		orderNumber = cscockpitOrderTabPage.getOrderNumberInOrderTab();
		logger.info("PARTIALLY RETURN ORDER NUMBER IS "+orderNumber);		
		s_assert.assertTrue(cscockpitOrderSearchTabPage.clickOrderLinkOnOrderSearchTabAndVerifyOrderDetailsPage(orderNumber)>0, "Order was NOT placed successfully,expected count after placing order in order detail items section >0 but actual count on UI = "+cscockpitOrderTabPage.getCountOfOrdersOnOrdersDetailsPageAfterPlacingOrder());
		cscockpitOrderTabPage.clickRefundOrderBtnOnOrderTab();
		s_assert.assertTrue(cscockpitOrderTabPage.verifyRefundRequestPopUpPresent(),"Refund Request PopUp not present on us");
		cscockpitOrderTabPage.checkReturnTaxOnlyCheckboxInPopUp("1");
		cscockpitOrderTabPage.selectRefundReasonOnRefundPopUp("Test");
		cscockpitOrderTabPage.selectFirstReturnActionOnRefundPopUp();
		cscockpitOrderTabPage.selectFirstRefundTypeOnRefundPopUp();
		cscockpitOrderTabPage.clickCreateBtnOnRefundPopUp();
		String refundTotal = cscockpitOrderTabPage.getRefundTotalFromRefundConfirmationPopUp();
		s_assert.assertTrue(cscockpitOrderTabPage.getShippingAndHandlingVariousSectionTaxInPopup(refundSubtotal).contains("0.00"),"Refund subtotal expected on UI for US "+0.00+"while Actual on UI is "+cscockpitOrderTabPage.getShippingAndHandlingVariousSectionTaxInPopup(refundSubtotal));
		s_assert.assertTrue(cscockpitOrderTabPage.getShippingAndHandlingVariousSectionTaxInPopup(refundDiscount).contains("0.00"),"Refund discount expected on UI for US "+0.00+"while Actual on UI is "+cscockpitOrderTabPage.getShippingAndHandlingVariousSectionTaxInPopup(refundDiscount));
		s_assert.assertTrue(cscockpitOrderTabPage.getShippingAndHandlingVariousSectionTaxInPopup(refundShipping).contains("0.00"),"Refund shipping expected on UI for US "+15.00+"while Actual on UI is "+cscockpitOrderTabPage.getShippingAndHandlingVariousSectionTaxInPopup(refundShipping));
		s_assert.assertTrue(cscockpitOrderTabPage.getShippingAndHandlingVariousSectionTaxInPopup(refundHandling).contains("0.00"),"Refund Handling expected on UI for US "+0.00+"while Actual on UI is "+cscockpitOrderTabPage.getShippingAndHandlingVariousSectionTaxInPopup(refundHandling));
		s_assert.assertTrue(cscockpitOrderTabPage.getShippingAndHandlingVariousSectionTaxInPopup(restockingFee).contains("0.00"),"Restocking fee expected on UI for US "+0.00+"while Actual on UI is "+cscockpitOrderTabPage.getShippingAndHandlingVariousSectionTaxInPopup(restockingFee));
		s_assert.assertTrue(cscockpitOrderTabPage.getShippingAndHandlingVariousSectionTaxInPopup(restockingFeeTax).contains("0.00"),"Restocking fee tax expected on UI for US "+0.00+"while Actual on UI is "+cscockpitOrderTabPage.getShippingAndHandlingVariousSectionTaxInPopup(restockingFeeTax));
		cscockpitOrderTabPage.clickConfirmBtnOnConfirmPopUp();
		String RMANumber = cscockpitOrderTabPage.getRMANumberFromPopup().split("\\:")[1].trim();
		cscockpitOrderTabPage.clickOKBtnOnRMAPopUp();
		s_assert.assertTrue(cscockpitOrderTabPage.isReturnRequestSectionDisplayed(), "Return request section is NOT displayed");
		cscockpitOrderTabPage.clickRMATreeBtnUnderReturnRequestOnOrderTab();
		s_assert.assertTrue(cscockpitOrderTabPage.getOnlyTaxReturnValueFromReturnRequestSection().contains("Yes"), "Expected Only tax return value in return request section is yes actual on UI is "+cscockpitOrderTabPage.getOnlyTaxReturnValueFromReturnRequestSection());
		cscockpitOrderTabPage.clickRMATreeBtnUnderReturnRequestOnOrderTab();
		cscockpitOrderTabPage.clickRefundOrderBtnOnOrderTab();
		s_assert.assertTrue(cscockpitOrderTabPage.verifyRefundRequestPopUpPresent(),"Refund Request PopUp not present on us");
		cscockpitOrderTabPage.clickCloseRefundRequestPopUP();
		//verification on storefornt
		driver.get(driver.getStoreFrontURL()+"/us");
		storeFrontConsultantPage = storeFrontHomePage.loginAsConsultant(consultantEmailID, password);
		storeFrontConsultantPage.clickOnWelcomeDropDown();
		storeFrontOrdersPage = storeFrontConsultantPage.clickOrdersLinkPresentOnWelcomeDropDown();
		s_assert.assertTrue(storeFrontOrdersPage.verifyRMANumberIsPresentInReturnOrderHistory(RMANumber),"RMA number "+RMANumber+" Is not present in order history page on CA");
		s_assert.assertTrue(storeFrontOrdersPage.getGranTotalOfRMANumberInReturnOrderHistory(RMANumber).trim().contains(refundTotal.trim()),"refund total expected on CA "+refundTotal+" actual on UI "+storeFrontOrdersPage.getGranTotalOfRMANumberInReturnOrderHistory(RMANumber).trim());
		logout();
		driver.get(driver.getCSCockpitURL());		
		cscockpitCustomerSearchTabPage = cscockpitLoginPage.clickLoginBtn();
		cscockpitCustomerSearchTabPage.clickFindOrderLinkOnLeftNavigation();
		cscockpitOrderSearchTabPage.enterOrderNumberInOrderSearchTab(orderNumber);
		cscockpitOrderSearchTabPage.clickSearchBtn();
		String randomOrderSequenceNumber = String.valueOf(cscockpitOrderSearchTabPage.getRandomOrdersFromOrderResultSearchFirstPageInOrderSearchTab());
		cscockpitOrderSearchTabPage.clickOrderNumberInOrderSearchResultsInOrderSearchTab(randomOrderSequenceNumber);
		cscockpitOrderTabPage.clickRefundOrderBtnOnOrderTab();
		cscockpitOrderTabPage.checkReturnTaxOnlyCheckboxInPopUp("2");
		cscockpitOrderTabPage.selectRefundReasonOnRefundPopUp("Test");
		cscockpitOrderTabPage.selectFirstReturnActionOnRefundPopUp();
		cscockpitOrderTabPage.selectFirstRefundTypeOnRefundPopUp();
		cscockpitOrderTabPage.clickCreateBtnOnRefundPopUp();
		refundTotal = cscockpitOrderTabPage.getRefundTotalFromRefundConfirmationPopUp();
		s_assert.assertTrue(cscockpitOrderTabPage.getShippingAndHandlingVariousSectionTaxInPopup(refundSubtotal).contains("0.00"),"Refund subtotal expected on UI for US "+0.00+"while Actual on UI is for 2nd product "+cscockpitOrderTabPage.getShippingAndHandlingVariousSectionTaxInPopup(refundSubtotal));
		s_assert.assertTrue(cscockpitOrderTabPage.getShippingAndHandlingVariousSectionTaxInPopup(refundDiscount).contains("0.00"),"Refund discount expected on UI for US "+0.00+"while Actual on UI is for 2nd product "+cscockpitOrderTabPage.getShippingAndHandlingVariousSectionTaxInPopup(refundDiscount));
		s_assert.assertTrue(cscockpitOrderTabPage.getShippingAndHandlingVariousSectionTaxInPopup(refundShipping).contains("0.00"),"Refund shipping expected on UI for US "+0.00+"while Actual on UI is for 2nd product "+cscockpitOrderTabPage.getShippingAndHandlingVariousSectionTaxInPopup(refundShipping));
		s_assert.assertTrue(cscockpitOrderTabPage.getShippingAndHandlingVariousSectionTaxInPopup(refundHandling).contains("0.00"),"Refund Handling expected on UI for US "+0.00+"while Actual on UI is for 2nd product "+cscockpitOrderTabPage.getShippingAndHandlingVariousSectionTaxInPopup(refundHandling));
		s_assert.assertTrue(cscockpitOrderTabPage.getShippingAndHandlingVariousSectionTaxInPopup(restockingFee).contains("0.00"),"Restocking fee expected on UI for US "+0.00+"while Actual on UI is for 2nd product "+cscockpitOrderTabPage.getShippingAndHandlingVariousSectionTaxInPopup(restockingFee));
		s_assert.assertTrue(cscockpitOrderTabPage.getShippingAndHandlingVariousSectionTaxInPopup(restockingFeeTax).contains("0.00"),"Restocking fee tax expected on UI for US "+0.00+"while Actual on UI is for 2nd product "+cscockpitOrderTabPage.getShippingAndHandlingVariousSectionTaxInPopup(restockingFeeTax));
		cscockpitOrderTabPage.clickConfirmBtnOnConfirmPopUp();
		RMANumber = cscockpitOrderTabPage.getRMANumberFromPopup().split("\\:")[1].trim();
		cscockpitOrderTabPage.clickOKBtnOnRMAPopUp();
		s_assert.assertTrue(cscockpitOrderTabPage.isReturnRequestSectionDisplayed(), "Return request section is NOT displayed");
		cscockpitOrderTabPage.clickRMATreeBtnUnderReturnRequestOnOrderTab();
		s_assert.assertTrue(cscockpitOrderTabPage.getOnlyTaxReturnValueFromReturnRequestSection().contains("Yes"), "Expected Only tax return value in return request section is yes actual on UI is "+cscockpitOrderTabPage.getOnlyTaxReturnValueFromReturnRequestSection());
		cscockpitOrderTabPage.clickRMATreeBtnUnderReturnRequestOnOrderTab();
		cscockpitOrderTabPage.clickRefundOrderBtnOnOrderTab();
		s_assert.assertTrue(cscockpitOrderTabPage.verifyRefundRequestPopUpPresent(),"Refund Request PopUp not present on us");
		cscockpitOrderTabPage.clickCloseRefundRequestPopUP();
		//verification on storefornt
		driver.get(driver.getStoreFrontURL()+"/us");
		storeFrontConsultantPage = storeFrontHomePage.loginAsConsultant(consultantEmailID, password);
		storeFrontConsultantPage.clickOnWelcomeDropDown();
		storeFrontOrdersPage = storeFrontConsultantPage.clickOrdersLinkPresentOnWelcomeDropDown();
		s_assert.assertTrue(storeFrontOrdersPage.verifyRMANumberIsPresentInReturnOrderHistory(RMANumber),"RMA number "+RMANumber+" Is not present in order history page on CA");
		s_assert.assertTrue(storeFrontOrdersPage.getGranTotalOfRMANumberInReturnOrderHistory(RMANumber).trim().contains(refundTotal.trim()),"refund total expected on CA "+refundTotal+" actual on UI "+storeFrontOrdersPage.getGranTotalOfRMANumberInReturnOrderHistory(RMANumber).trim());
		logout();
		s_assert.assertAll();
	}


	//Hybris Project-2537:To verify partial return after order level tax
	@Test
	public void testVerifyPartialReturnAfterOrderLevelTax_2537(){

		String randomCustomerSequenceNumber = null;
		String consultantEmailID = null;
		String orderNumber = null;
		String SKUValue = null;
		String qtyOfFirstProduct=null;
		String randomProductSequenceNumber = null;
		RFO_DB = driver.getDBNameRFO();

		List<Map<String, Object>> randomConsultantList =  null;
		String accountId = null;

		//-------------------FOR US----------------------------------
		driver.get(driver.getStoreFrontURL()+"/us");		
		while(true){
			randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_CONSULTANT_WITH_ORDERS_AND_AUTOSHIPS_RFO,"236"),RFO_DB);
			consultantEmailID = (String) getValueFromQueryResult(randomConsultantList, "UserName");  
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
		driver.get(driver.getCSCockpitURL());		
		cscockpitCustomerSearchTabPage = cscockpitLoginPage.clickLoginBtn();
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
		cscockpitCartTabPage.clickAddToCartBtnTillProductAddedInCartTab();
		cscockpitCartTabPage.addProductToCartPageTillAtleastFourDistinctProducts();
		qtyOfFirstProduct=cscockpitCartTabPage.getProductCountFromcartPage("1");
		cscockpitCartTabPage.getProductCountFromcartPage("2");
		cscockpitCartTabPage.getProductCountFromcartPage("3");
		cscockpitCartTabPage.getProductCountFromcartPage("4");
		cscockpitCartTabPage.clickCheckoutBtnInCartTab();
		cscockpitCheckoutTabPage.clickAddNewPaymentAddressInCheckoutTab();
		cscockpitCheckoutTabPage.enterBillingInfo();
		cscockpitCheckoutTabPage.clickSaveAddNewPaymentProfilePopUP();
		cscockpitCheckoutTabPage.enterCVVValueInCheckoutTab(TestConstants.SECURITY_CODE);
		cscockpitCheckoutTabPage.clickUseThisCardBtnInCheckoutTab();
		cscockpitCheckoutTabPage.clickPlaceOrderButtonInCheckoutTab();
		orderNumber = cscockpitOrderTabPage.getOrderNumberInOrderTab();
		logger.info("PARTIALLY RETURN ORDER NUMBER IS "+orderNumber);		
		s_assert.assertTrue(cscockpitOrderSearchTabPage.clickOrderLinkOnOrderSearchTabAndVerifyOrderDetailsPage(orderNumber)>0, "Order was NOT placed successfully,expected count after placing order in order detail items section >0 but actual count on UI = "+cscockpitOrderTabPage.getCountOfOrdersOnOrdersDetailsPageAfterPlacingOrder());
		cscockpitOrderTabPage.clickRefundOrderBtnOnOrderTab();
		s_assert.assertTrue(cscockpitOrderTabPage.verifyRefundRequestPopUpPresent(),"Refund Request PopUp not present on us");
		cscockpitOrderTabPage.checkReturnOnlyTaxChkBoxOnRefundPopUp();
		cscockpitOrderTabPage.selectRefundReasonOnRefundPopUp("Test");
		cscockpitOrderTabPage.selectFirstReturnActionOnRefundPopUp();
		cscockpitOrderTabPage.selectFirstRefundTypeOnRefundPopUp();
		cscockpitOrderTabPage.clickCreateBtnOnRefundPopUp();
		cscockpitOrderTabPage.clickConfirmBtnOnConfirmPopUp();
		String RMANumber = cscockpitOrderTabPage.getRMANumberFromPopup().split("\\:")[1].trim();
		cscockpitOrderTabPage.clickOKBtnOnRMAPopUp();
		s_assert.assertTrue(cscockpitOrderTabPage.isReturnRequestSectionDisplayed(), "Return request section is NOT displayed");
		s_assert.assertTrue(cscockpitOrderTabPage.verifyReturnOrderRMANumberInOrderTab(RMANumber), "Expected RMA number in return request section is "+RMANumber+"Actual on UI, it is not present ");
		cscockpitOrderTabPage.clickRefundOrderBtnOnOrderTab();
		cscockpitOrderTabPage.checkProductInfoCheckboxInPopUp("1");
		cscockpitOrderTabPage.selectReturnQuantityOnPopUp(qtyOfFirstProduct,"1");
		cscockpitOrderTabPage.selectRefundReasonOnRefundPopUp("Test");
		cscockpitOrderTabPage.selectFirstReturnActionOnRefundPopUp();
		cscockpitOrderTabPage.selectFirstRefundTypeOnRefundPopUp();
		cscockpitOrderTabPage.clickCreateBtnOnRefundPopUp();
		cscockpitOrderTabPage.clickConfirmBtnOnConfirmPopUp();
		RMANumber = cscockpitOrderTabPage.getRMANumberFromPopup().split("\\:")[1].trim();
		cscockpitOrderTabPage.clickOKBtnOnRMAPopUp();
		s_assert.assertTrue(cscockpitOrderTabPage.isReturnRequestSectionDisplayed(), "Return request section is NOT displayed");
		s_assert.assertTrue(cscockpitOrderTabPage.verifyReturnOrderRMANumberInOrderTab(RMANumber), "Expected RMA number in return request section is "+RMANumber+"Actual on UI, it is not present ");
		s_assert.assertAll();
	}



}
