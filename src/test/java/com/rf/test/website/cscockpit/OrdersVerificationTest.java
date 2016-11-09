package com.rf.test.website.cscockpit;

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

	//-----------------------------------------------------------------------------------------------------------------

	public OrdersVerificationTest() {
		cscockpitLoginPage = new CSCockpitLoginPage(driver);
		cscockpitCheckoutTabPage = new CSCockpitCheckoutTabPage(driver);
		cscockpitCustomerSearchTabPage = new CSCockpitCustomerSearchTabPage(driver);
		cscockpitCustomerTabPage = new CSCockpitCustomerTabPage(driver);
		cscockpitOrderSearchTabPage = new CSCockpitOrderSearchTabPage(driver);
		cscockpitOrderTabPage = new CSCockpitOrderTabPage(driver);
		cscockpitCartTabPage = new CSCockpitCartTabPage(driver);
		storeFrontHomePage = new StoreFrontHomePage(driver);
		storeFrontConsultantPage = new StoreFrontConsultantPage(driver);
		storeFrontOrdersPage = new StoreFrontOrdersPage(driver);
		storeFrontPCUserPage = new StoreFrontPCUserPage(driver);
		storeFrontRCUserPage = new StoreFrontRCUserPage(driver);
		storeFrontUpdateCartPage = new StoreFrontUpdateCartPage(driver);
	}

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
		String accountID = null;
		int randomNum2 = CommonUtils.getRandomNum(10000, 1000000);
		String country = null;
		if(driver.getCountry().equalsIgnoreCase("ca")){
			country= TestConstants.COUNTRY_DD_VALUE_CA;
		}else{
			country= TestConstants.COUNTRY_DD_VALUE_US;
		}
		RFO_DB = driver.getDBNameRFO();
		driver.get(driver.getStoreFrontURL()+"/"+driver.getCountry());
		//get emailId of username
		while(true){
			List<Map<String, Object>> randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_CONSULTANT_WITH_ORDERS_AND_AUTOSHIPS_RFO,countryId),RFO_DB);
			consultantEmailID = (String) getValueFromQueryResult(randomConsultantList, "UserName");  
			accountID = String.valueOf(getValueFromQueryResult(randomConsultantList, "AccountID"));
			logger.info("Account Id of user "+accountID);
			storeFrontConsultantPage = storeFrontHomePage.loginAsConsultant(consultantEmailID, password);
			try{
				storeFrontConsultantPage = storeFrontHomePage.loginAsConsultant(consultantEmailID, password);
			}catch(Exception e){
				driver.get(driver.getStoreFrontURL()+"/"+driver.getCountry());
				storeFrontConsultantPage = storeFrontHomePage.loginAsConsultant(consultantEmailID, password);
			}
			boolean isLoginError = driver.getCurrentUrl().contains("error");
			if(isLoginError){
				logger.info("Login error for the user "+consultantEmailID);
				driver.get(driver.getStoreFrontURL()+"/"+driver.getCountry());
			}
			else
				break;
		}
		logout();
		List<Map<String, Object>> emailIdFromAccountIdList =  null;
		emailIdFromAccountIdList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_EMAIL_ID_FROM_ACCOUNT_ID,accountID),RFO_DB);
		consultantEmailID = (String) getValueFromQueryResult(emailIdFromAccountIdList, "EmailAddress");  
		logger.info("emaild of consultant username "+consultantEmailID);	

		driver.get(driver.getCSCockpitURL());
		cscockpitCustomerSearchTabPage = cscockpitLoginPage.clickLoginBtn();
		cscockpitCustomerSearchTabPage.selectCustomerTypeFromDropDownInCustomerSearchTab("CONSULTANT");
		cscockpitCustomerSearchTabPage.selectCountryFromDropDownInCustomerSearchTab(country);
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
		cscockpitCheckoutTabPage.clickPlaceOrderButtonInCheckoutTab();
		s_assert.assertTrue(cscockpitCheckoutTabPage.verifySelectPaymentDetailsPopupInCheckoutTab(), "Select payment details popup is not present");
		cscockpitCheckoutTabPage.clickOkButtonOfSelectPaymentDetailsPopupInCheckoutTab();
		cscockpitCheckoutTabPage.addANewBillingProfileIfThereIsNoStoredCreditCard();

		cscockpitCheckoutTabPage.clickUseThisCardBtnInCheckoutTab();
		cscockpitCheckoutTabPage.clickPlaceOrderButtonInCheckoutTab();
		orderNumber = cscockpitOrderTabPage.getOrderNumberInOrderTab();
		cscockpitCustomerTabPage.clickCustomerTab();
		s_assert.assertTrue(cscockpitCustomerTabPage.getOrderTypeInCustomerTab(orderNumber.split("\\-")[0].trim()).contains("Consultant Order"),"CSCockpit Customer tab Order type expected = Consultant Order and on UI = " +cscockpitCustomerTabPage.getOrderTypeInCustomerTab(orderNumber.split("\\-")[0].trim()));
		driver.get(driver.getStoreFrontURL()+"/"+driver.getCountry());

		try{
			storeFrontConsultantPage = storeFrontHomePage.loginAsConsultant(consultantEmailID, password);
		}catch(Exception e){
			driver.get(driver.getStoreFrontURL()+"/"+driver.getCountry());
			storeFrontConsultantPage = storeFrontHomePage.loginAsConsultant(consultantEmailID, password);
		}
		storeFrontConsultantPage.clickOnWelcomeDropDown();
		storeFrontOrdersPage =  storeFrontConsultantPage.clickOrdersLinkPresentOnWelcomeDropDown();
		s_assert.assertTrue(storeFrontOrdersPage.verifyOrdersPageIsDisplayed(),"Orders page has not been displayed");
		orderHistoryNumber = storeFrontOrdersPage.getFirstOrderNumberFromOrderHistory();
		s_assert.assertTrue(orderHistoryNumber.contains(orderNumber.split("\\-")[0].trim()),"CSCockpit Order number expected = "+orderNumber.split("\\-")[0].trim()+" and on UI = " +orderHistoryNumber);
		logout();
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
		String country = null;
		if(driver.getCountry().equalsIgnoreCase("ca")){
			country= TestConstants.COUNTRY_DD_VALUE_CA;
		}else{
			country= TestConstants.COUNTRY_DD_VALUE_US;
		}
		driver.get(driver.getStoreFrontURL()+"/"+driver.getCountry());
		String RFO_DB = driver.getDBNameRFO(); 
		List<Map<String, Object>> randomPCUserList =  null;
		String pcUserEmailID = null;
		String accountID = null;

		while(true){
			randomPCUserList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_PC_WITH_ORDERS_AND_AUTOSHIPS_RFO,countryId),RFO_DB);
			pcUserEmailID = (String) getValueFromQueryResult(randomPCUserList, "UserName");		
			accountID = String.valueOf(getValueFromQueryResult(randomPCUserList, "AccountID"));
			logger.info("Account Id of the user is "+accountID);

			try{
				storeFrontPCUserPage = storeFrontHomePage.loginAsPCUser(pcUserEmailID, password);
			}catch(Exception e){
				driver.get(driver.getStoreFrontURL()+"/"+driver.getCountry());
				storeFrontPCUserPage = storeFrontHomePage.loginAsPCUser(pcUserEmailID, password);
			}
			boolean isError = driver.getCurrentUrl().contains("error");
			if(isError){
				logger.info("login error for the user "+pcUserEmailID);
				driver.get(driver.getStoreFrontURL()+"/"+driver.getCountry());
			}
			else
				break;
		}	
		logout();
		List<Map<String, Object>> emailIdFromAccountIdList =  null;
		emailIdFromAccountIdList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_EMAIL_ID_FROM_ACCOUNT_ID,accountID),RFO_DB);
		pcUserEmailID = (String) getValueFromQueryResult(emailIdFromAccountIdList, "EmailAddress");  
		logger.info("emaild of username "+pcUserEmailID);	

		driver.get(driver.getCSCockpitURL());		
		cscockpitCustomerSearchTabPage = cscockpitLoginPage.clickLoginBtn();
		cscockpitCustomerSearchTabPage.selectCustomerTypeFromDropDownInCustomerSearchTab("PC");
		cscockpitCustomerSearchTabPage.selectCountryFromDropDownInCustomerSearchTab(country);
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
		if(driver.getCountry().equalsIgnoreCase("us")){
			s_assert.assertTrue(cscockpitCheckoutTabPage.getDeliverModeTypeInCheckoutTab().contains("FedEx Ground (HD)"),"CSCockpit checkout tab delivery mode type expected = FedEx Ground (HD) and on UI = " +cscockpitCheckoutTabPage.getDeliverModeTypeInCheckoutTab());
		}else{
			s_assert.assertTrue(cscockpitCheckoutTabPage.getDeliverModeTypeInCheckoutTab().contains("UPS Ground (HD)"),"CSCockpit checkout tab delivery mode type expected = UPS Ground (HD) and on UI = " +cscockpitCheckoutTabPage.getDeliverModeTypeInCheckoutTab());
		}
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
		driver.get(driver.getStoreFrontURL()+"/"+driver.getCountry());
		storeFrontPCUserPage = storeFrontHomePage.loginAsPCUser(pcUserEmailID, password);
		storeFrontPCUserPage.clickOnWelcomeDropDown();
		storeFrontOrdersPage =  storeFrontPCUserPage.clickOrdersLinkPresentOnWelcomeDropDown();
		s_assert.assertTrue(storeFrontOrdersPage.verifyOrdersPageIsDisplayed(),"Orders page has not been displayed");
		orderHistoryNumber = storeFrontOrdersPage.getFirstOrderNumberFromOrderHistory();
		s_assert.assertTrue(orderHistoryNumber.contains(orderNumber.split("\\-")[0].trim()),"CSCockpit Order number expected = "+orderNumber.split("\\-")[0].trim()+" and on UI = " +orderHistoryNumber);
		logout();
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
		String country = null;
		if(driver.getCountry().equalsIgnoreCase("ca")){
			country= TestConstants.COUNTRY_DD_VALUE_CA;
		}else{
			country= TestConstants.COUNTRY_DD_VALUE_US;
		}
		//-------------------FOR US----------------------------------
		driver.get(driver.getStoreFrontURL()+"/"+driver.getCountry());
		String RFO_DB = driver.getDBNameRFO(); 
		List<Map<String, Object>> randomRCList =  null;
		String rcUserEmailID =null;
		String accountID = null;
		while(true){
			randomRCList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_RC_RFO,countryId),RFO_DB);
			rcUserEmailID = (String) getValueFromQueryResult(randomRCList, "UserName");		
			accountID = String.valueOf(getValueFromQueryResult(randomRCList, "AccountID"));
			logger.info("Account Id of the user is "+accountID);
			try{
				storeFrontRCUserPage = storeFrontHomePage.loginAsRCUser(rcUserEmailID, password);
			}catch(Exception e){
				driver.get(driver.getStoreFrontURL()+"/"+driver.getCountry());
				storeFrontRCUserPage = storeFrontHomePage.loginAsRCUser(rcUserEmailID, password);
			}
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
		List<Map<String, Object>> emailIdFromAccountIdList =  null;
		emailIdFromAccountIdList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_EMAIL_ID_FROM_ACCOUNT_ID,accountID),RFO_DB);
		rcUserEmailID = (String) getValueFromQueryResult(emailIdFromAccountIdList, "EmailAddress");  
		logger.info("emaild of username "+rcUserEmailID);	
		driver.get(driver.getCSCockpitURL());
		cscockpitCustomerSearchTabPage = cscockpitLoginPage.clickLoginBtn();
		cscockpitCustomerSearchTabPage.selectCustomerTypeFromDropDownInCustomerSearchTab("RETAIL");
		cscockpitCustomerSearchTabPage.selectCountryFromDropDownInCustomerSearchTab(country);
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
		if(driver.getCountry().equalsIgnoreCase("us")){
			s_assert.assertTrue(cscockpitCheckoutTabPage.getDeliverModeTypeInCheckoutTab().contains("FedEx Ground (HD)"),"CSCockpit checkout tab delivery mode type expected = FedEx Ground (HD) and on UI = " +cscockpitCheckoutTabPage.getDeliverModeTypeInCheckoutTab());
		}else{
			s_assert.assertTrue(cscockpitCheckoutTabPage.getDeliverModeTypeInCheckoutTab().contains("UPS Ground (HD)"),"CSCockpit checkout tab delivery mode type expected = UPS Ground (HD) and on UI = " +cscockpitCheckoutTabPage.getDeliverModeTypeInCheckoutTab());
		}
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
		driver.get(driver.getStoreFrontURL()+"/"+driver.getCountry());
		storeFrontRCUserPage = storeFrontHomePage.loginAsRCUser(rcUserEmailID, password);
		storeFrontRCUserPage.clickOnWelcomeDropDown();
		storeFrontOrdersPage =  storeFrontRCUserPage.clickOrdersLinkPresentOnWelcomeDropDown();
		s_assert.assertTrue(storeFrontOrdersPage.verifyOrdersPageIsDisplayed(),"Orders page has not been displayed");
		orderHistoryNumber = storeFrontOrdersPage.getFirstOrderNumberFromOrderHistory();
		s_assert.assertTrue(orderHistoryNumber.contains(orderNumber.split("\\-")[0].trim()),"CSCockpit Order number expected = "+orderNumber.split("\\-")[0].trim()+" and on UI = " +orderHistoryNumber);
		logout();

		s_assert.assertAll();
	}

	//Hybris Project-1780:To verify user permission for transaction status change in Order detail page
	@Test
	public void testVerifyUserPermissionForTransactionStatus_1780(){
		String randomOrderSequenceNumber = null;
		cscockpitLoginPage.enterUsername(TestConstants.CS_AGENT_USERNAME);		
		cscockpitCustomerSearchTabPage = cscockpitLoginPage.clickLoginBtn();
		cscockpitCustomerSearchTabPage.clickOrderSearchTab();
		cscockpitOrderSearchTabPage.clickSearchBtn();
		randomOrderSequenceNumber = String.valueOf(cscockpitOrderSearchTabPage.getRandomOrdersFromOrderResultSearchFirstPageInOrderSearchTab());
		cscockpitOrderSearchTabPage.clickOrderNumberInOrderSearchResultsInOrderSearchTab(randomOrderSequenceNumber);
		s_assert.assertFalse(cscockpitOrderTabPage.isPaymentTransactionStatusClickableOnOrderTab(), "Transaction Status on Payment Transactions Section is clickable for "+TestConstants.CS_AGENT_USERNAME);

		//		driver.get(driver.getCSCockpitURL());
		//		
		//		cscockpitLoginPage.enterUsername(TestConstants.ADMIN_USERNAME);
		//		cscockpitCustomerSearchTabPage = cscockpitLoginPage.clickLoginBtn();
		//		cscockpitHomePage.clickOrderSearchTab();
		//		cscockpitHomePage.clickSearchBtn();
		//		randomOrderSequenceNumber = String.valueOf(cscockpitOrderSearchTabPage.getRandomOrdersFromOrderResultSearchFirstPageInOrderSearchTab());
		//		cscockpitOrderSearchTabPage.clickOrderNumberInOrderSearchResultsInOrderSearchTab(randomOrderSequenceNumber);
		//		s_assert.assertFalse(cscockpitOrderTabPage.isPaymentTransactionStatusClickableOnOrderTab(), "Transaction Status on Payment Transactions Section is clickable for "+TestConstants.ADMIN_USERNAME);

		driver.get(driver.getCSCockpitURL());
		cscockpitLoginPage.enterUsername(TestConstants.CS_COMMISION_ADMIN_USERNAME);
		cscockpitCustomerSearchTabPage = cscockpitLoginPage.clickLoginBtn();
		cscockpitCustomerSearchTabPage.clickOrderSearchTab();
		cscockpitOrderSearchTabPage.clickSearchBtn();
		randomOrderSequenceNumber = String.valueOf(cscockpitOrderSearchTabPage.getRandomOrdersFromOrderResultSearchFirstPageInOrderSearchTab());
		cscockpitOrderSearchTabPage.clickOrderNumberInOrderSearchResultsInOrderSearchTab(randomOrderSequenceNumber);
		s_assert.assertFalse(cscockpitOrderTabPage.isPaymentTransactionStatusClickableOnOrderTab(), "Transaction Status on Payment Transactions Section is clickable for "+TestConstants.CS_COMMISION_ADMIN_USERNAME);

		driver.get(driver.getCSCockpitURL());
		cscockpitLoginPage.enterUsername(TestConstants.CS_SALES_SUPERVISORY_USERNAME);
		cscockpitCustomerSearchTabPage = cscockpitLoginPage.clickLoginBtn();
		cscockpitCustomerSearchTabPage.clickOrderSearchTab();
		cscockpitOrderSearchTabPage.clickSearchBtn();
		randomOrderSequenceNumber = String.valueOf(cscockpitOrderSearchTabPage.getRandomOrdersFromOrderResultSearchFirstPageInOrderSearchTab());
		cscockpitOrderSearchTabPage.clickOrderNumberInOrderSearchResultsInOrderSearchTab(randomOrderSequenceNumber);
		s_assert.assertTrue(cscockpitOrderTabPage.isPaymentTransactionStatusClickableOnOrderTab(), "Transaction Status on Payment Transactions Section is NOT clickable for "+TestConstants.CS_SALES_SUPERVISORY_USERNAME);

		s_assert.assertAll();
	}

	//Hybris Project-1924:Verify the ability to mark Test Order functionality for all users
	@Test
	public void testVerifyTheAbilityToMarkTestOrderFunctionalityForAllUsers_1924(){
		String randomCustomerSequenceNumber = null;
		String randomProductSequenceNumber = null;
		String SKUValue = null;
		String country = null;
		if(driver.getCountry().equalsIgnoreCase("ca")){
			country= TestConstants.COUNTRY_DD_VALUE_CA;
		}else{
			country= TestConstants.COUNTRY_DD_VALUE_US;
		}

		cscockpitCustomerSearchTabPage = cscockpitLoginPage.clickLoginBtn();
		cscockpitCustomerSearchTabPage.selectCustomerTypeFromDropDownInCustomerSearchTab("CONSULTANT");
		cscockpitCustomerSearchTabPage.selectCountryFromDropDownInCustomerSearchTab(country);
		cscockpitCustomerSearchTabPage.selectAccountStatusFromDropDownInCustomerSearchTab("Active");
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
		cscockpitCheckoutTabPage.enterCVVValueInCheckoutTab(TestConstants.SECURITY_CODE);
		cscockpitCheckoutTabPage.clickUseThisCardBtnInCheckoutTab();
		cscockpitCheckoutTabPage.clickTestOrderCheckBoxInCheckoutTab();
		cscockpitCheckoutTabPage.clickPlaceOrderButtonInCheckoutTab();
		s_assert.assertTrue(cscockpitOrderTabPage.verifyTestOrderCheckBoxIsSelectedInOrderTab(), "Test order checkbox is not selected after place an order");
		s_assert.assertTrue(cscockpitOrderTabPage.getOrderStatusAfterPlaceOrderInOrderTab().contains("SUBMITTED"),"CSCockpit checkout tab order status expected = SUBMITTED and on UI = " +cscockpitOrderTabPage.getOrderStatusAfterPlaceOrderInOrderTab());
		s_assert.assertAll();
	}

	//Hybris Project-1925:Verify the ability to mark donotship Order functionality for all users
	@Test
	public void testToVerifyTheAbilityToMarkDoNotShipOrderFunctionality_1925(){
		String randomCustomerSequenceNumber = null;
		String randomProductSequenceNumber = null;
		String SKUValue = null;
		String country = null;
		if(driver.getCountry().equalsIgnoreCase("ca")){
			country= TestConstants.COUNTRY_DD_VALUE_CA;
		}else{
			country= TestConstants.COUNTRY_DD_VALUE_US;
		}
		cscockpitCustomerSearchTabPage = cscockpitLoginPage.clickLoginBtn();
		cscockpitCustomerSearchTabPage.selectCustomerTypeFromDropDownInCustomerSearchTab("CONSULTANT");
		cscockpitCustomerSearchTabPage.selectCountryFromDropDownInCustomerSearchTab(country);
		cscockpitCustomerSearchTabPage.selectAccountStatusFromDropDownInCustomerSearchTab("Active");
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
		cscockpitCheckoutTabPage.enterCVVValueInCheckoutTab(TestConstants.SECURITY_CODE);
		cscockpitCheckoutTabPage.clickUseThisCardBtnInCheckoutTab();
		cscockpitCheckoutTabPage.clickDoNotShipCheckBoxInCheckoutTab();
		cscockpitCheckoutTabPage.clickPlaceOrderButtonInCheckoutTab();
		s_assert.assertTrue(cscockpitOrderTabPage.verifyDoNotShipCheckBoxIsSelectedInOrderTab(), "Do not ship checkbox is not selected after place an order");
		s_assert.assertTrue(cscockpitOrderTabPage.getOrderStatusAfterPlaceOrderInOrderTab().contains("COMPLETED"),"CSCockpit checkout tab order status expected = COMPLETED and on UI = " +cscockpitOrderTabPage.getOrderStatusAfterPlaceOrderInOrderTab());
		s_assert.assertAll();
	}

	//Hybris Project-1926:Verify the ability to mark Test Order and donot ship functionality for all users
	@Test
	public void testVerifyTheAbilityToMarkTestOrderAndDoNotShipFunctionality_1926(){
		String randomCustomerSequenceNumber = null;
		String randomProductSequenceNumber = null;
		int randomNumber=CommonUtils.getRandomNum(10000, 1000000);
		String SKUValue = null;
		String attendentFirstName=TestConstants.FIRST_NAME+randomNumber;
		String attendeeLastName = TestConstants.LAST_NAME+randomNumber;
		String country = null;
		String addressLine = null;
		String city = null;
		String postal = null;
		String province = null;
		String phoneNumber = null;
		if(driver.getCountry().equalsIgnoreCase("us")){
			addressLine = TestConstants.ADDRESS_LINE_1_US;
			city = TestConstants.CITY_US;
			postal = TestConstants.POSTAL_CODE_US;
			province = TestConstants.PROVINCE_ALABAMA_US;
			phoneNumber = TestConstants.PHONE_NUMBER_US;
			country = "United States";
		}else{
			addressLine = TestConstants.ADDRESS_LINE_1_CA;
			city = TestConstants.CITY_CA;
			postal = TestConstants.POSTAL_CODE_CA;
			province = TestConstants.PROVINCE_ALBERTA;
			phoneNumber = TestConstants.PHONE_NUMBER_CA;
			country = "Canada";
		}

		cscockpitCustomerSearchTabPage = cscockpitLoginPage.clickLoginBtn();
		cscockpitCustomerSearchTabPage.selectCustomerTypeFromDropDownInCustomerSearchTab("CONSULTANT");
		cscockpitCustomerSearchTabPage.selectCountryFromDropDownInCustomerSearchTab(country);
		cscockpitCustomerSearchTabPage.selectAccountStatusFromDropDownInCustomerSearchTab("Active");
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
		cscockpitCustomerTabPage.enterShippingInfoInAddNewPaymentProfilePopupWithoutSaveBtn(attendentFirstName, attendeeLastName, addressLine, city, postal, country, province, phoneNumber);
		cscockpitCheckoutTabPage.clickOnCreateNewAddressButtonInAutoshipTemplateTabPage();
		cscockpitCheckoutTabPage.clickUseEnteredAddressOfCreateNewAddressShippingAddressPopup();
		cscockpitCheckoutTabPage.clickAddNewPaymentAddressInCheckoutTab();
		cscockpitCheckoutTabPage.enterBillingInfo();
		cscockpitCheckoutTabPage.clickSaveAddNewPaymentProfilePopUP();
		cscockpitCheckoutTabPage.enterCVVValueInCheckoutTab(TestConstants.SECURITY_CODE);
		cscockpitCheckoutTabPage.clickUseThisCardBtnInCheckoutTab();
		cscockpitCheckoutTabPage.clickTestOrderCheckBoxInCheckoutTab();
		cscockpitCheckoutTabPage.clickDoNotShipCheckBoxInCheckoutTab();
		cscockpitCheckoutTabPage.clickPlaceOrderButtonInCheckoutTab();
		s_assert.assertTrue(cscockpitOrderTabPage.verifyTestOrderCheckBoxIsSelectedInOrderTab(), "Test order checkbox is not selected after place an order");
		s_assert.assertTrue(cscockpitOrderTabPage.verifyDoNotShipCheckBoxIsSelectedInOrderTab(), "Do not ship checkbox is not selected after place an order");
		s_assert.assertTrue(cscockpitOrderTabPage.getOrderStatusAfterPlaceOrderInOrderTab().contains("COMPLETED"),"CSCockpit checkout tab order status expected = COMPLETED and on UI = " +cscockpitOrderTabPage.getOrderStatusAfterPlaceOrderInOrderTab());
		s_assert.assertAll();
	}

	//Hybris Project-1948:To Verify the Display Order Origination In the Order Detail Page
	@Test
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
		String country = null;
		if(driver.getCountry().equalsIgnoreCase("ca")){
			country= TestConstants.COUNTRY_DD_VALUE_CA;
		}else{
			country= TestConstants.COUNTRY_DD_VALUE_US;
		}
		driver.get(driver.getStoreFrontURL()+"/"+driver.getCountry());

		while(true){
			randomCustomerList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_RC_RFO,countryId),RFO_DB);
			customerEmailID = (String) getValueFromQueryResult(randomCustomerList, "UserName");		
			accountId = String.valueOf(getValueFromQueryResult(randomCustomerList, "AccountID"));
			logger.info("Account Id of the user is "+accountId);
			try{
				storeFrontConsultantPage = storeFrontHomePage.loginAsConsultant(customerEmailID, password);
			}catch(Exception e){
				driver.get(driver.getStoreFrontURL()+"/"+driver.getCountry());
				storeFrontConsultantPage = storeFrontHomePage.loginAsConsultant(customerEmailID, password);
			}
			boolean isLoginError = driver.getCurrentUrl().contains("error");
			if(isLoginError){
				logger.info("Login Error for the user "+customerEmailID);
				driver.get(driver.getStoreFrontURL()+"/"+driver.getCountry());
			}
			else
				break;
		}
		logger.info("login is successful");
		storeFrontConsultantPage.hoverOnShopLinkAndClickAllProductsLinksAfterLogin();
		storeFrontUpdateCartPage.clickOnBuyNowButton(driver.getCountry());
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
		driver.get(driver.getStoreFrontURL()+"/"+driver.getCountry());

		while(true){
			randomCustomerList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguementPWS(DBQueries_RFO.GET_RANDOM_ACTIVE_CONSULTANT_WITH_PWS_RFO,driver.getEnvironment()+".biz",driver.getCountry(),countryId),RFO_DB);
			customerEmailID = (String) getValueFromQueryResult(randomCustomerList, "UserName");		
			accountId = String.valueOf(getValueFromQueryResult(randomCustomerList, "AccountID"));
			logger.info("Account Id of the user is "+accountId);

			try{
				storeFrontConsultantPage = storeFrontHomePage.loginAsConsultant(customerEmailID, password);
			}catch(Exception e){
				driver.get(driver.getStoreFrontURL()+"/"+driver.getCountry());
				storeFrontConsultantPage = storeFrontHomePage.loginAsConsultant(customerEmailID, password);
			}
			boolean isLoginError = driver.getCurrentUrl().contains("error");
			if(isLoginError){
				logger.info("Login Error for the user "+customerEmailID);
				driver.get(driver.getStoreFrontURL()+"/"+driver.getCountry());
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
		storeFrontUpdateCartPage.clickOnBuyNowButton(driver.getCountry());
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
		driver.get(driver.getStoreFrontURL()+"/"+driver.getCountry());

		while(true){
			randomCustomerList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguementPWS(DBQueries_RFO.GET_RANDOM_ACTIVE_CONSULTANT_WITH_PWS_RFO,driver.getEnvironment()+".biz",driver.getCountry(), countryId),RFO_DB);
			customerEmailID = (String) getValueFromQueryResult(randomCustomerList, "UserName");		
			accountId = String.valueOf(getValueFromQueryResult(randomCustomerList, "AccountID"));
			logger.info("Account Id of the user is "+accountId);

			try{
				storeFrontConsultantPage = storeFrontHomePage.loginAsConsultant(customerEmailID, password);
			}catch(Exception e){
				driver.get(driver.getStoreFrontURL()+"/"+driver.getCountry());
				storeFrontConsultantPage = storeFrontHomePage.loginAsConsultant(customerEmailID, password);
			}
			boolean isLoginError = driver.getCurrentUrl().contains("error");
			if(isLoginError){
				logger.info("Login Error for the user "+customerEmailID);
				driver.get(driver.getStoreFrontURL()+"/"+driver.getCountry());
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
		storeFrontUpdateCartPage.clickOnBuyNowButton(driver.getCountry());
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
		logout();
		//open cscockpit
		List<Map<String, Object>> emailIdFromAccountIdList =  null;
		emailIdFromAccountIdList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_EMAIL_ID_FROM_ACCOUNT_ID,accountId),RFO_DB);
		customerEmailID = (String) getValueFromQueryResult(emailIdFromAccountIdList, "EmailAddress");  
		logger.info("emaild of username "+customerEmailID);	
		driver.get(driver.getCSCockpitURL());		
		cscockpitCustomerSearchTabPage = cscockpitLoginPage.clickLoginBtn();
		cscockpitCustomerSearchTabPage.selectCustomerTypeFromDropDownInCustomerSearchTab("CONSULTANT");
		cscockpitCustomerSearchTabPage.selectCountryFromDropDownInCustomerSearchTab(country);
		cscockpitCustomerSearchTabPage.selectAccountStatusFromDropDownInCustomerSearchTab("Active");
		cscockpitCustomerSearchTabPage.enterEmailIdInSearchFieldInCustomerSearchTab(customerEmailID);
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
		cscockpitCheckoutTabPage.enterCVVValueInCheckoutTab(TestConstants.SECURITY_CODE);
		cscockpitCheckoutTabPage.clickUseThisCardBtnInCheckoutTab();
		cscockpitCheckoutTabPage.clickPlaceOrderButtonInCheckoutTab();
		String orderNumberCreatedFromCSCockpit = cscockpitOrderTabPage.getOrderNumberInOrderTab();
		logger.info("Order Number created from CSCockpit is "+orderNumberCreatedFromCSCockpit);
		//Search Order created from Corp Site
		cscockpitOrderTabPage.clickChangeOrderLinkOnLeftNavigation();		
		cscockpitOrderSearchTabPage.enterOrderNumberInOrderSearchTab(orderNumberCreatedFromCorp);
		cscockpitOrderSearchTabPage.clickSearchBtn();
		cscockpitOrderSearchTabPage.clickOrderLinkOnOrderSearchTabAndVerifyOrderDetailsPage(orderNumberCreatedFromCorp);
		s_assert.assertTrue(cscockpitOrderTabPage.getOriginationValuePresentAsPerSearchedOrder().contains(".rodanandfields.com"),"Origination Value for Corp Order expected is to contain '.rodanandfields.com'"+ "but on actual UI,it is ="+ cscockpitOrderTabPage.getOriginationValuePresentAsPerSearchedOrder());

		//Search Order created from BIZ PWS site
		cscockpitOrderTabPage.clickChangeOrderLinkOnLeftNavigation();		
		cscockpitOrderSearchTabPage.enterOrderNumberInOrderSearchTab(orderNumberCreatedFromBIZPWS);
		cscockpitOrderSearchTabPage.clickSearchBtn();
		cscockpitOrderSearchTabPage.clickOrderLinkOnOrderSearchTabAndVerifyOrderDetailsPage(orderNumberCreatedFromBIZPWS);
		s_assert.assertTrue(cscockpitOrderTabPage.getOriginationValuePresentAsPerSearchedOrder().contains(".myrfo"+driver.getEnvironment()+".biz"),"Origination Value for Corp Order expected is to contain '.myrfo"+driver.getEnvironment()+"biz'"+ "but on actual UI,it is ="+ cscockpitOrderTabPage.getOriginationValuePresentAsPerSearchedOrder());

		//Search Order created from COM PWS site
		cscockpitOrderTabPage.clickChangeOrderLinkOnLeftNavigation();		
		cscockpitOrderSearchTabPage.enterOrderNumberInOrderSearchTab(orderNumberCreatedFromCOMPWS);
		cscockpitOrderSearchTabPage.clickSearchBtn();
		cscockpitOrderSearchTabPage.clickOrderLinkOnOrderSearchTabAndVerifyOrderDetailsPage(orderNumberCreatedFromCOMPWS);
		s_assert.assertTrue(cscockpitOrderTabPage.getOriginationValuePresentAsPerSearchedOrder().contains(".myrfo"+driver.getEnvironment()+".com"),"Origination Value for Corp Order expected is to contain '.myrfo"+driver.getEnvironment()+"com'"+ "but on actual UI,it is ="+ cscockpitOrderTabPage.getOriginationValuePresentAsPerSearchedOrder());

		//Search Order created from CSCockpit
		cscockpitOrderTabPage.clickChangeOrderLinkOnLeftNavigation();		
		cscockpitOrderSearchTabPage.enterOrderNumberInOrderSearchTab(orderNumberCreatedFromCSCockpit);
		cscockpitOrderSearchTabPage.clickSearchBtn();
		cscockpitOrderSearchTabPage.clickOrderLinkOnOrderSearchTabAndVerifyOrderDetailsPage(orderNumberCreatedFromCSCockpit);
		s_assert.assertTrue(cscockpitOrderTabPage.getOriginationValuePresentAsPerSearchedOrder().contains("cscockpit"),"Origination Value for Corp Order expected is to contain 'cscockpit'"+ "but on actual UI,it is ="+ cscockpitOrderTabPage.getOriginationValuePresentAsPerSearchedOrder());

		s_assert.assertAll();
	}

	//Hybris Project-1951:To verify place order from Order detail Page
	@Test
	public void testToVerifyPlaceOrderFromOrderDetailPage_1951() throws InterruptedException{
		RFO_DB = driver.getDBNameRFO();
		String randomCustomerSequenceNumber = null;
		String randomProductSequenceNumber = null;
		String consultantEmailID = null;
		String SKUValue = null;
		String accountID=null;
		int randomNum = CommonUtils.getRandomNum(10000, 1000000);
		List<Map<String, Object>> randomConsultantList =  null;
		List<Map<String, Object>> emailIdFromAccountIdList =  null;
		String country = null;
		if(driver.getCountry().equalsIgnoreCase("ca")){
			country= TestConstants.COUNTRY_DD_VALUE_CA;
		}else{
			country= TestConstants.COUNTRY_DD_VALUE_US;
		}
		driver.get(driver.getStoreFrontURL()+"/"+driver.getCountry());  
		while(true){
			randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_CONSULTANT_WITH_ORDERS_AND_AUTOSHIPS_RFO,countryId),RFO_DB);
			accountID=String.valueOf(getValueFromQueryResult(randomConsultantList, "AccountID"));
			emailIdFromAccountIdList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_EMAIL_ID_FROM_ACCOUNT_ID,accountID),RFO_DB);
			consultantEmailID=(String) getValueFromQueryResult(emailIdFromAccountIdList, "EmailAddress"); 
			try{
				storeFrontConsultantPage = storeFrontHomePage.loginAsConsultant(consultantEmailID, password);
			}catch(Exception e){
				driver.get(driver.getStoreFrontURL()+"/"+driver.getCountry());
				storeFrontConsultantPage = storeFrontHomePage.loginAsConsultant(consultantEmailID, password);
			}
			boolean isLoginError = driver.getCurrentUrl().contains("error");
			if(isLoginError){
				logger.info("Login error for the user "+consultantEmailID);
				driver.get(driver.getStoreFrontURL()+"/"+driver.getCountry());
			}
			else
				break;
		}
		logout();
		logger.info("login is successful");
		driver.get(driver.getCSCockpitURL());

		cscockpitCustomerSearchTabPage = cscockpitLoginPage.clickLoginBtn();
		cscockpitCustomerSearchTabPage.selectCustomerTypeFromDropDownInCustomerSearchTab("CONSULTANT");
		cscockpitCustomerSearchTabPage.selectCountryFromDropDownInCustomerSearchTab(country);
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
		cscockpitCheckoutTabPage.clickAddNewPaymentAddressInCheckoutTab();
		cscockpitCheckoutTabPage.enterBillingInfo();
		cscockpitCheckoutTabPage.clickSaveAddNewPaymentProfilePopUP();
		cscockpitCheckoutTabPage.enterCVVValueInCheckoutTab(TestConstants.INVALID_CV2_NUMBER);
		cscockpitCheckoutTabPage.clickUseThisCardButtonOnCheckoutPage();
		cscockpitCheckoutTabPage.clickOkForCreditCardValidationFailedPopUp();
		cscockpitCheckoutTabPage.enterCVVValueInCheckoutTab(TestConstants.VALID_CV2_NUMBER);
		cscockpitCheckoutTabPage.clickUseThisCardBtnInCheckoutTab();
		cscockpitCheckoutTabPage.clickPlaceOrderButtonInCheckoutTab();
		s_assert.assertTrue(cscockpitOrderTabPage.getOrderStatusAfterPlaceOrderInOrderTab().contains("SUBMITTED"),"order is not submitted successfully");
		String getOrderNumberFromCsCockpitUIOnOrderTab = cscockpitOrderTabPage.getOrderNumberFromCsCockpitUIOnOrderTab();
		driver.get(driver.getStoreFrontURL()+"/"+driver.getCountry());
		try{
			storeFrontConsultantPage = storeFrontHomePage.loginAsConsultant(consultantEmailID, password);
		}catch(Exception e){
			driver.get(driver.getStoreFrontURL()+"/"+driver.getCountry());
			storeFrontConsultantPage = storeFrontHomePage.loginAsConsultant(consultantEmailID, password);
		}
		storeFrontConsultantPage.clickOnWelcomeDropDown();
		storeFrontOrdersPage = storeFrontConsultantPage.clickOrdersLinkPresentOnWelcomeDropDown();

		s_assert.assertTrue(getOrderNumberFromCsCockpitUIOnOrderTab.contains(storeFrontOrdersPage.getFirstOrderNumberFromOrderHistory()),"This Order is not present on the StoreFront of US");
		logout();
		s_assert.assertAll();
	}

	//Hybris Project-1818:To verify user permission for update QV and CV in Order detail page
	@Test 
	public void testVerifyUserPermissionsForQVandCVUpdate_1818(){
		String randomCustomerSequenceNumber = null;

		cscockpitLoginPage.enterUsername(TestConstants.CS_AGENT_USERNAME);
		cscockpitCustomerSearchTabPage = cscockpitLoginPage.clickLoginBtn();
		cscockpitCustomerSearchTabPage.selectCustomerTypeFromDropDownInCustomerSearchTab("CONSULTANT");
		cscockpitCustomerSearchTabPage.selectAccountStatusFromDropDownInCustomerSearchTab("Active");
		cscockpitCustomerSearchTabPage.clickSearchBtn();
		randomCustomerSequenceNumber = String.valueOf(cscockpitCustomerSearchTabPage.getRandomCustomerFromSearchResult());
		cscockpitCustomerSearchTabPage.clickCIDNumberInCustomerSearchTab(randomCustomerSequenceNumber);
		cscockpitCustomerTabPage.clickFirstOrderInCustomerTab();
		s_assert.assertTrue(cscockpitOrderTabPage.isQVandCVUpdateBtnDisabledOnOrderTab(),"QV and CV update button is not disabled for "+TestConstants.CS_AGENT_USERNAME);
		driver.get(driver.getCSCockpitURL());

		//		cscockpitLoginPage.enterUsername(TestConstants.ADMIN_USERNAME);
		//		cscockpitCustomerSearchTabPage = cscockpitLoginPage.clickLoginBtn();
		//		cscockpitCustomerSearchTabPage.selectCustomerTypeFromDropDownInCustomerSearchTab("CONSULTANT");
		//		cscockpitCustomerSearchTabPage.selectAccountStatusFromDropDownInCustomerSearchTab("Active");
		//		cscockpitCustomerSearchTabPage.clickSearchBtn();
		//		randomCustomerSequenceNumber = String.valueOf(cscockpitCustomerSearchTabPage.getRandomCustomerFromSearchResult());
		//		cscockpitCustomerSearchTabPage.clickCIDNumberInCustomerSearchTab(randomCustomerSequenceNumber);
		//		cscockpitCustomerTabPage.clickFirstOrderInCustomerTab();
		//		s_assert.assertTrue(cscockpitHomePage.isQVandCVUpdateBtnDisabled(),"QV and CV update button is not disabled for "+TestConstants.ADMIN_USERNAME);
		//		driver.get(driver.getCSCockpitURL());

		cscockpitLoginPage.enterUsername(TestConstants.CS_COMMISION_ADMIN_USERNAME);
		cscockpitCustomerSearchTabPage = cscockpitLoginPage.clickLoginBtn();
		cscockpitCustomerSearchTabPage.selectCustomerTypeFromDropDownInCustomerSearchTab("CONSULTANT");
		cscockpitCustomerSearchTabPage.selectAccountStatusFromDropDownInCustomerSearchTab("Active");
		cscockpitCustomerSearchTabPage.clickSearchBtn();
		randomCustomerSequenceNumber = String.valueOf(cscockpitCustomerSearchTabPage.getRandomCustomerFromSearchResult());
		cscockpitCustomerSearchTabPage.clickCIDNumberInCustomerSearchTab(randomCustomerSequenceNumber);
		cscockpitCustomerTabPage.clickFirstOrderInCustomerTab();
		s_assert.assertFalse(cscockpitOrderTabPage.isQVandCVUpdateBtnDisabledOnOrderTab(),"QV and CV update button is not disabled for "+TestConstants.CS_COMMISION_ADMIN_USERNAME);
		driver.get(driver.getCSCockpitURL());

		cscockpitLoginPage.enterUsername(TestConstants.CS_SALES_SUPERVISORY_USERNAME);
		cscockpitCustomerSearchTabPage = cscockpitLoginPage.clickLoginBtn();
		cscockpitCustomerSearchTabPage.selectCustomerTypeFromDropDownInCustomerSearchTab("CONSULTANT");
		cscockpitCustomerSearchTabPage.selectAccountStatusFromDropDownInCustomerSearchTab("Active");
		cscockpitCustomerSearchTabPage.clickSearchBtn();
		randomCustomerSequenceNumber = String.valueOf(cscockpitCustomerSearchTabPage.getRandomCustomerFromSearchResult());
		cscockpitCustomerSearchTabPage.clickCIDNumberInCustomerSearchTab(randomCustomerSequenceNumber);
		cscockpitCustomerTabPage.clickFirstOrderInCustomerTab();
		s_assert.assertFalse(cscockpitOrderTabPage.isQVandCVUpdateBtnDisabledOnOrderTab(),"QV and CV update button is not disabled for "+TestConstants.CS_SALES_SUPERVISORY_USERNAME);

		s_assert.assertAll();
	}

	//Hybris Project-1779:To verify user permission for order status change in Order detail page
	@Test(enabled=false)//WIP //ISSUE change link is not present for any user
	public void testVerifyUserPermissionsForOrderStatus_1779(){
		String randomCustomerSequenceNumber = null;
		String country = null;
		if(driver.getCountry().equalsIgnoreCase("ca")){
			country= TestConstants.COUNTRY_DD_VALUE_CA;
		}else{
			country= TestConstants.COUNTRY_DD_VALUE_US;
		}
		cscockpitLoginPage.enterUsername(TestConstants.CS_AGENT_USERNAME);
		cscockpitCustomerSearchTabPage = cscockpitLoginPage.clickLoginBtn();
		cscockpitCustomerSearchTabPage.selectCustomerTypeFromDropDownInCustomerSearchTab("CONSULTANT");
		cscockpitCustomerSearchTabPage.selectCountryFromDropDownInCustomerSearchTab(country);
		cscockpitCustomerSearchTabPage.selectAccountStatusFromDropDownInCustomerSearchTab("Active");
		cscockpitCustomerSearchTabPage.clickSearchBtn();
		randomCustomerSequenceNumber = String.valueOf(cscockpitCustomerSearchTabPage.getRandomCustomerFromSearchResult());
		cscockpitCustomerSearchTabPage.clickCIDNumberInCustomerSearchTab(randomCustomerSequenceNumber);
		cscockpitCustomerTabPage.clickFirstOrderInCustomerTab();
		//		s_assert.assertTrue(cscockpitHomePage.isQVandCVUpdateBtnDisabled(),"QV and CV update button is not disabled for "+TestConstants.CS_AGENT_USERNAME);
		driver.get(driver.getCSCockpitURL());

		//		cscockpitLoginPage.enterUsername(TestConstants.ADMIN_USERNAME);
		//		cscockpitCustomerSearchTabPage = cscockpitLoginPage.clickLoginBtn();
		//		cscockpitCustomerSearchTabPage.selectCustomerTypeFromDropDownInCustomerSearchTab("CONSULTANT");
		//		cscockpitCustomerSearchTabPage.selectAccountStatusFromDropDownInCustomerSearchTab("Active");
		//		cscockpitCustomerSearchTabPage.clickSearchBtn();
		//		randomCustomerSequenceNumber = String.valueOf(cscockpitCustomerSearchTabPage.getRandomCustomerFromSearchResult());
		//		cscockpitCustomerSearchTabPage.clickCIDNumberInCustomerSearchTab(randomCustomerSequenceNumber);
		//		cscockpitCustomerTabPage.clickFirstOrderInCustomerTab();
		//		s_assert.assertTrue(cscockpitHomePage.isQVandCVUpdateBtnDisabled(),"QV and CV update button is not disabled for "+TestConstants.ADMIN_USERNAME);
		//		driver.get(driver.getCSCockpitURL());

		cscockpitLoginPage.enterUsername(TestConstants.CS_COMMISION_ADMIN_USERNAME);
		cscockpitCustomerSearchTabPage = cscockpitLoginPage.clickLoginBtn();
		cscockpitCustomerSearchTabPage.selectCustomerTypeFromDropDownInCustomerSearchTab("CONSULTANT");
		cscockpitCustomerSearchTabPage.selectCountryFromDropDownInCustomerSearchTab(country);
		cscockpitCustomerSearchTabPage.selectAccountStatusFromDropDownInCustomerSearchTab("Active");
		cscockpitCustomerSearchTabPage.clickSearchBtn();
		randomCustomerSequenceNumber = String.valueOf(cscockpitCustomerSearchTabPage.getRandomCustomerFromSearchResult());
		cscockpitCustomerSearchTabPage.clickCIDNumberInCustomerSearchTab(randomCustomerSequenceNumber);
		cscockpitCustomerTabPage.clickFirstOrderInCustomerTab();
		//		s_assert.assertFalse(cscockpitHomePage.isQVandCVUpdateBtnDisabled(),"QV and CV update button is not disabled for "+TestConstants.CS_COMMISION_ADMIN_USERNAME);
		driver.get(driver.getCSCockpitURL());

		cscockpitLoginPage.enterUsername(TestConstants.CS_SALES_SUPERVISORY_USERNAME);
		cscockpitCustomerSearchTabPage = cscockpitLoginPage.clickLoginBtn();
		cscockpitCustomerSearchTabPage.selectCustomerTypeFromDropDownInCustomerSearchTab("CONSULTANT");
		cscockpitCustomerSearchTabPage.selectCountryFromDropDownInCustomerSearchTab(country);
		cscockpitCustomerSearchTabPage.selectAccountStatusFromDropDownInCustomerSearchTab("Active");
		cscockpitCustomerSearchTabPage.clickSearchBtn();
		randomCustomerSequenceNumber = String.valueOf(cscockpitCustomerSearchTabPage.getRandomCustomerFromSearchResult());
		cscockpitCustomerSearchTabPage.clickCIDNumberInCustomerSearchTab(randomCustomerSequenceNumber);
		cscockpitCustomerTabPage.clickFirstOrderInCustomerTab();
		//		s_assert.assertFalse(cscockpitHomePage.isQVandCVUpdateBtnDisabled(),"QV and CV update button is not disabled for "+TestConstants.CS_SALES_SUPERVISORY_USERNAME);

		s_assert.assertAll();
	}

	//Hybris Project-1937:To verify for created new user the order status should be submitted
	@Test
	public void testToVerifyForCreatedNewUserTheOrderStatusShouldBeSubmitted_1937(){
		String randomCustomerSequenceNumber = null;
		String randomProductSequenceNumber = null; 
		String SKUValue = null;
		driver.get(driver.getCSCockpitURL());
		String country = null;
		if(driver.getCountry().equalsIgnoreCase("ca")){
			country= TestConstants.COUNTRY_DD_VALUE_CA;
		}else{
			country= TestConstants.COUNTRY_DD_VALUE_US;
		}

		cscockpitCustomerSearchTabPage = cscockpitLoginPage.clickLoginBtn();
		//-------------------FOR US---------------------------
		cscockpitCustomerSearchTabPage.selectCustomerTypeFromDropDownInCustomerSearchTab("CONSULTANT");
		cscockpitCustomerSearchTabPage.selectCountryFromDropDownInCustomerSearchTab(country);
		cscockpitCustomerSearchTabPage.selectAccountStatusFromDropDownInCustomerSearchTab("Active");
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
		cscockpitCheckoutTabPage.entervalidCV2OnPaymentInfoSection(TestConstants.VALID_CV2_NUMBER);
		cscockpitCheckoutTabPage.clickUseThisCardButtonOnCheckoutPage();
		cscockpitCheckoutTabPage.clickPlaceOrderButtonInCheckoutTab();
		String newlyPlacedConsultantOrderNumber = cscockpitOrderTabPage.getOrderNumberFromCsCockpitUIOnOrderTab();
		cscockpitOrderTabPage.clickOrderSearchTab();
		cscockpitOrderSearchTabPage.enterOrderNumberInOrderSearchTab(newlyPlacedConsultantOrderNumber);
		cscockpitOrderSearchTabPage.clickSearchBtn();
		s_assert.assertTrue(cscockpitOrderSearchTabPage.validateOrderStatusOnOrderSearchTab(newlyPlacedConsultantOrderNumber),"Order Status is not submitted");
		cscockpitCustomerSearchTabPage.clickCustomerSearchTab();
		cscockpitCustomerSearchTabPage.selectCustomerTypeFromDropDownInCustomerSearchTab("PC");
		cscockpitCustomerSearchTabPage.selectCountryFromDropDownInCustomerSearchTab(country);
		cscockpitCustomerSearchTabPage.selectAccountStatusFromDropDownInCustomerSearchTab("Active");
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
		cscockpitCheckoutTabPage.entervalidCV2OnPaymentInfoSection(TestConstants.VALID_CV2_NUMBER);
		cscockpitCheckoutTabPage.clickUseThisCardButtonOnCheckoutPage();
		cscockpitCheckoutTabPage.clickPlaceOrderButtonInCheckoutTab();
		String newlyPlacedPcOrderNumber = cscockpitOrderTabPage.getOrderNumberFromCsCockpitUIOnOrderTab();
		cscockpitOrderTabPage.clickOrderSearchTab();
		cscockpitOrderSearchTabPage.enterOrderNumberInOrderSearchTab(newlyPlacedPcOrderNumber);
		cscockpitOrderSearchTabPage.clickSearchBtn();
		s_assert.assertTrue(cscockpitOrderSearchTabPage.validateOrderStatusOnOrderSearchTab(newlyPlacedPcOrderNumber),"Order Status is not submitted");
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

	// Hybris Project-1940:To verify for created new user the Account status should be Active
	@Test 
	public void testToVerifyForCreatedNewUserTheAccountStatusShouldBeActive_1940(){
		String randomCustomerSequenceNumber = null;
		String randomCustomerSequenceNumberForRC = null;
		String randomProductSequenceNumber = null;
		String SKUValue = null;
		String country = null;
		if(driver.getCountry().equalsIgnoreCase("ca")){
			country= TestConstants.COUNTRY_DD_VALUE_CA;
		}else{
			country= TestConstants.COUNTRY_DD_VALUE_US;
		}
		driver.get(driver.getCSCockpitURL());
		cscockpitCustomerSearchTabPage = cscockpitLoginPage.clickLoginBtn();
		cscockpitCustomerSearchTabPage.selectCustomerTypeFromDropDownInCustomerSearchTab("PC");
		cscockpitCustomerSearchTabPage.selectCountryFromDropDownInCustomerSearchTab(country);
		cscockpitCustomerSearchTabPage.selectAccountStatusFromDropDownInCustomerSearchTab("Active");
		cscockpitCustomerSearchTabPage.clickSearchBtn();
		randomCustomerSequenceNumber = String.valueOf(cscockpitCustomerSearchTabPage.getRandomCustomerFromSearchResult());
		cscockpitCustomerSearchTabPage.clickCIDNumberInCustomerSearchTab(randomCustomerSequenceNumber);
		s_assert.assertTrue(cscockpitCustomerTabPage.validateAccountStatusOnCustomerTab().contains("ACTIVE"),"Account status is not Active For US PC");
		cscockpitCustomerTabPage.clickCustomerSearchTab();
		cscockpitCustomerSearchTabPage.selectCustomerTypeFromDropDownInCustomerSearchTab("RETAIL");
		cscockpitCustomerSearchTabPage.clickSearchBtn();
		randomCustomerSequenceNumberForRC = String.valueOf(cscockpitCustomerSearchTabPage.getRandomCustomerFromSearchResult());
		cscockpitCustomerSearchTabPage.clickCIDNumberInCustomerSearchTab(randomCustomerSequenceNumberForRC);
		s_assert.assertTrue(cscockpitCustomerTabPage.validateAccountStatusOnCustomerTab().contains("ACTIVE"),"Account status is not Active For US RC");
		cscockpitCustomerTabPage.clickCustomerSearchTab();
		cscockpitCustomerSearchTabPage.selectCustomerTypeFromDropDownInCustomerSearchTab("CONSULTANT");
		cscockpitCustomerSearchTabPage.clickSearchBtn();
		randomCustomerSequenceNumberForRC = String.valueOf(cscockpitCustomerSearchTabPage.getRandomCustomerFromSearchResult());
		cscockpitCustomerSearchTabPage.clickCIDNumberInCustomerSearchTab(randomCustomerSequenceNumberForRC);
		s_assert.assertTrue(cscockpitCustomerTabPage.validateAccountStatusOnCustomerTab().contains("ACTIVE"),"Account status is not Active For US consultant");
		cscockpitCustomerTabPage.clickCustomerSearchTab();
		cscockpitCustomerSearchTabPage.selectCustomerTypeFromDropDownInCustomerSearchTab("PC");
		//cscockpitCustomerSearchTabPage.selectCountryFromDropDownInCustomerSearchTab("United States");
		cscockpitCustomerSearchTabPage.selectAccountStatusFromDropDownInCustomerSearchTab("Active");
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
		cscockpitCheckoutTabPage.clickAddNewPaymentAddressInCheckoutTab();
		cscockpitCheckoutTabPage.enterBillingInfo();
		cscockpitCheckoutTabPage.clickSaveAddNewPaymentProfilePopUP();
		cscockpitCheckoutTabPage.enterCVVValueInCheckoutTab(TestConstants.SECURITY_CODE);
		cscockpitCheckoutTabPage.clickUseThisCardBtnInCheckoutTab();
		cscockpitCheckoutTabPage.clickPlaceOrderButtonInCheckoutTab();
		String newlyPlacedConsultantOrderNumber = cscockpitOrderTabPage.getOrderNumberFromCsCockpitUIOnOrderTab();
		cscockpitOrderTabPage.clickOrderSearchTab();
		cscockpitOrderSearchTabPage.enterOrderNumberInOrderSearchTab(newlyPlacedConsultantOrderNumber);
		cscockpitOrderSearchTabPage.clickSearchBtn();
		s_assert.assertTrue(cscockpitOrderSearchTabPage.validateOrderStatusOnOrderSearchTab(newlyPlacedConsultantOrderNumber),"Order Status is not submitted for US");
		s_assert.assertAll();
	}

	//Hybris Project-1950:Verify the Order Detail Page UI
	@Test
	public void testVerifyOrderDetailPageUI_1950(){
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
		String consultantEmailID =null;
		String accountID=null;
		String cid=null;
		String orderStatusForUser=null;
		int size=0;
		String accountStatus=null;
		List<Map<String, Object>> randomConsultantList =  null;
		List<Map<String, Object>> emailIdFromAccountIdList =  null;
		String country = null;
		if(driver.getCountry().equalsIgnoreCase("ca")){
			country= TestConstants.COUNTRY_DD_VALUE_CA;
		}else{
			country= TestConstants.COUNTRY_DD_VALUE_US;
		}
		while(true){
			randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_CONSULTANT_WITH_ORDERS_AND_AUTOSHIPS_RFO,countryId),RFO_DB);
			accountID=String.valueOf(getValueFromQueryResult(randomConsultantList, "AccountID"));
			emailIdFromAccountIdList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_EMAIL_ID_FROM_ACCOUNT_ID,accountID),RFO_DB);
			consultantEmailID=(String) getValueFromQueryResult(emailIdFromAccountIdList, "EmailAddress");
			cscockpitCustomerSearchTabPage = cscockpitLoginPage.clickLoginBtn();
			cscockpitCustomerSearchTabPage.selectCustomerTypeFromDropDownInCustomerSearchTab("CONSULTANT");
			cscockpitCustomerSearchTabPage.selectCountryFromDropDownInCustomerSearchTab(country);
			cscockpitCustomerSearchTabPage.enterEmailIdInSearchFieldInCustomerSearchTab(consultantEmailID);
			cscockpitCustomerSearchTabPage.clickSearchBtn();
			size=cscockpitCustomerSearchTabPage.getTotalResultsInCustomerSearchOnCustomerSearchTab();
			accountStatus=cscockpitCustomerSearchTabPage.getAccountStatusOfTheCustomerInCustomerSearchTab(size);
			if(accountStatus.equals("ACTIVE")){
				cid=cscockpitCustomerSearchTabPage.getCIDNumberInCustomerSearchTab(size);
				break;
			}
		}

		cscockpitCustomerSearchTabPage.clickOrderSearchTab();
		cscockpitOrderSearchTabPage.enterCIDOnOrderSearchTab(cid);
		cscockpitOrderSearchTabPage.clickSearchBtn();
		String randomCustomerOrderNumber = String.valueOf(cscockpitCustomerSearchTabPage.getRandomCustomerFromSearchResult());
		String firstNameFromUI = cscockpitOrderSearchTabPage.getfirstNameOfTheCustomerInOrderSearchTab(randomCustomerOrderNumber);
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
		orderStatusForUser=cscockpitOrderSearchTabPage.getOrderStatusOfClickedOrderInOrderSearchTab(randomCustomerOrderNumber);
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

		//consigments section
		if(orderStatusForUser.equalsIgnoreCase("Shipped")){
			s_assert.assertTrue(cscockpitOrderSearchTabPage.verifySectionsIsPresentInOrderSearchTab(code), "code section is not present in order tab");
			s_assert.assertTrue(cscockpitOrderSearchTabPage.verifySectionsIsPresentInOrderSearchTab(trackingNumber), "Tracking number section is not present in order tab");
			s_assert.assertTrue(cscockpitOrderSearchTabPage.verifySectionsIsPresentInOrderSearchTab(trackingURL), "Tracking URL section is not present in order tab");
			s_assert.assertTrue(cscockpitOrderSearchTabPage.verifySectionsIsPresentInOrderSearchTab(shippingDate), "shipping date section is not present in order tab");
			s_assert.assertTrue(cscockpitOrderSearchTabPage.verifySectionsIsPresentInOrderSearchTab(description), "Description section is not present in order tab");
			s_assert.assertTrue(cscockpitOrderSearchTabPage.verifySectionsIsPresentInOrderSearchTab(status), "status section is not present in order tab");
			s_assert.assertTrue(cscockpitOrderSearchTabPage.verifySectionsIsPresentInOrderSearchTab(carrier), "carrier section is not present in order tab");
		}
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

	//Hybris Project-1964:To verify the CV and QV update functionality in the order detail Page
	@Test
	public void testVerifyCVAndQVValuesUpdateFunctionalityInOrderDetailPage_1964() throws InterruptedException{
		String randomCustomerSequenceNumber = null;
		String consultantEmailID = null;
		String orderHistoryNumber = null;
		RFO_DB = driver.getDBNameRFO();

		//-------------------FOR US----------------------------------
		driver.get(driver.getStoreFrontURL()+"/"+driver.getCountry());
		List<Map<String, Object>> randomConsultantList =  null;
		while(true){
			randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_CONSULTANT_WITH_ORDERS_AND_AUTOSHIPS_RFO,countryId),RFO_DB);
			consultantEmailID = (String) getValueFromQueryResult(randomConsultantList, "UserName");
			try{
				storeFrontConsultantPage = storeFrontHomePage.loginAsConsultant(consultantEmailID, password);
			}catch(Exception e){
				driver.get(driver.getStoreFrontURL()+"/"+driver.getCountry());
				storeFrontConsultantPage = storeFrontHomePage.loginAsConsultant(consultantEmailID, password);
			}
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
		for(int i=0; i<=5; i++){
			randomCustomerSequenceNumber = String.valueOf(cscockpitCustomerSearchTabPage.getRandomCustomerFromSearchResult());
			cscockpitCustomerSearchTabPage.clickCIDNumberInCustomerSearchTab(randomCustomerSequenceNumber);
			cscockpitCustomerSearchTabPage.clickCustomerTab();
			if(cscockpitCustomerTabPage.isShippedOrSubmittedOrderPresentInCustomerOrderSection()==true){
				break;
			}else{
				cscockpitCustomerTabPage.clickCustomerSearchTab();
			}
		}
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

		//-------------------FOR CA----------------------------------
		driver.get(driver.getStoreFrontURL()+"/"+driver.getCountry());
		List<Map<String, Object>> randomConsultantList =  null;
		String consultantEmailID=null;
		String accountId=null;

		while(true){
			randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_CONSULTANT_WITH_ORDERS_AND_AUTOSHIPS_RFO,countryId),RFO_DB);
			consultantEmailID = (String) getValueFromQueryResult(randomConsultantList, "UserName");  
			accountId= String.valueOf(getValueFromQueryResult(randomConsultantList, "AccountID"));
			try{
				storeFrontConsultantPage = storeFrontHomePage.loginAsConsultant(consultantEmailID, password);
			}catch(Exception e){
				driver.get(driver.getStoreFrontURL()+"/"+driver.getCountry());
				storeFrontConsultantPage = storeFrontHomePage.loginAsConsultant(consultantEmailID, password);
			}
			boolean isLoginError = driver.getCurrentUrl().contains("error");
			if(isLoginError){
				logger.info("Login error for the user "+consultantEmailID);
				driver.get(driver.getStoreFrontURL()+"/"+driver.getCountry());
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
		String firstNameFromDatabase = cscockpitOrderSearchTabPage.getfirstNameOfTheCustomerInOrderSearchTab(randomSequenceNumber);
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

		//Search cid as customer Name which in database Having account status pending or terminated click search. issue
/*		cscockpitOrderSearchTabPage.enterCIDInOrderSearchTab(invalidCid);
		cscockpitOrderSearchTabPage.clickSearchBtn();
		s_assert.assertTrue(cscockpitOrderSearchTabPage.verifyOrderNumberSectionIsPresentWithClickableLinksInOrderSearchTab(), "Order # section is not present with clickable links");
		s_assert.assertTrue(cscockpitOrderSearchTabPage.verifySectionsIsPresentInOrderSearchTab(firstName), "First name section is not present in order search tab");
		s_assert.assertTrue(cscockpitOrderSearchTabPage.verifySectionsIsPresentInOrderSearchTab(lastName), "Last name section is not present in order search tab");
		s_assert.assertTrue(cscockpitOrderSearchTabPage.verifySectionsIsPresentInOrderSearchTab(RFCID), "RFCID section is not present in order search tab");*/

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
		//s_assert.assertTrue(cscockpitOrderSearchTabPage.verifyNoResultFoundForInvalidCID(),"CSCockpit Sponser CID search expected = No Results and on UI = Results are displayed");
		s_assert.assertTrue(cscockpitOrderSearchTabPage.verifySectionsIsPresentInOrderSearchTab(firstName), "First name section is not present in order search tab");
		s_assert.assertTrue(cscockpitOrderSearchTabPage.verifySectionsIsPresentInOrderSearchTab(lastName), "Last name section is not present in order search tab");
		s_assert.assertTrue(cscockpitOrderSearchTabPage.verifySectionsIsPresentInOrderSearchTab(RFCID), "RFCID section is not present in order search tab");
		
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

		String country = null;
		if(driver.getCountry().equalsIgnoreCase("ca")){
			country= TestConstants.COUNTRY_DD_VALUE_CA;
		}else{
			country= TestConstants.COUNTRY_DD_VALUE_US;
		}
		//select country one by one and assert values.
		cscockpitCustomerSearchTabPage.selectCountryFromDropDownInCustomerSearchTab(country);
		cscockpitOrderSearchTabPage.clickSearchBtn();
		s_assert.assertTrue(cscockpitOrderSearchTabPage.verifySectionsIsPresentInOrderSearchTab(firstName), "First name section is not present in order search tab");
		s_assert.assertTrue(cscockpitOrderSearchTabPage.verifySectionsIsPresentInOrderSearchTab(lastName), "Last name section is not present in order search tab");
		s_assert.assertTrue(cscockpitOrderSearchTabPage.verifySectionsIsPresentInOrderSearchTab(RFCID), "RFCID section is not present in order search tab");
		s_assert.assertAll();
	}

	//	//Hybris Project-4074:CsCockpit CRP and PC Perks autoship order
	//	@Test 
	//	public void testCsCockpitCRPandPCPerksAutoshipOrder_4074(){
	//		String randomCustomerSequenceNumber = null;
	//		driver.get(driver.getCSCockpitURL());
	//		
	//		cscockpitCustomerSearchTabPage = cscockpitLoginPage.clickLoginBtn();
	//		cscockpitCustomerSearchTabPage.selectCustomerTypeFromDropDownInCustomerSearchTab("CONSULTANT");
	//		cscockpitCustomerSearchTabPage.selectCountryFromDropDownInCustomerSearchTab("United States");
	//		cscockpitCustomerSearchTabPage.selectAccountStatusFromDropDownInCustomerSearchTab("Active");
	//		cscockpitCustomerSearchTabPage.clickSearchBtn();
	//		randomCustomerSequenceNumber = String.valueOf(cscockpitCustomerSearchTabPage.getRandomCustomerFromSearchResult());
	//		cscockpitCustomerSearchTabPage.clickCIDNumberInCustomerSearchTab(randomCustomerSequenceNumber);
	//		cscockpitCustomerTabPage.clickAutoshipIdOnCustomerTab();
	//		cscockpitAutoshipTemplateTabPage.clickRunNowButtonOnAutoshipTemplateTab();
	//		cscockpitAutoshipTemplateTabPage.clickOkForRegeneratedIdpopUp();
	//		s_assert.assertTrue(cscockpitAutoshipTemplateTabPage.verifyConfirmMessagePopUpIsAppearing(),"confirm message is not appearing for US");
	//		cscockpitAutoshipTemplateTabPage.clickOkConfirmMessagePopUp();
	//		cscockpitAutoshipTemplateTabPage.clickCustomerSearchTab();
	//		cscockpitCustomerSearchTabPage.selectCustomerTypeFromDropDownInCustomerSearchTab("PC");
	//		cscockpitCustomerSearchTabPage.selectCountryFromDropDownInCustomerSearchTab("United States");
	//		cscockpitCustomerSearchTabPage.selectAccountStatusFromDropDownInCustomerSearchTab("Active");
	//		cscockpitCustomerSearchTabPage.clickSearchBtn();
	//		randomCustomerSequenceNumber = String.valueOf(cscockpitCustomerSearchTabPage.getRandomCustomerFromSearchResult());
	//		cscockpitCustomerSearchTabPage.clickCIDNumberInCustomerSearchTab(randomCustomerSequenceNumber);
	//		cscockpitCustomerTabPage.clickAutoshipIdOnCustomerTab();
	//		cscockpitAutoshipTemplateTabPage.clickRunNowButtonOnAutoshipTemplateTab();
	//		cscockpitAutoshipTemplateTabPage.clickOkForRegeneratedIdpopUp();
	//		s_assert.assertTrue(cscockpitAutoshipTemplateTabPage.verifyConfirmMessagePopUpIsAppearing(),"confirm message is not appearing for US");
	//		cscockpitAutoshipTemplateTabPage.clickOkConfirmMessagePopUp();
	//		cscockpitAutoshipTemplateTabPage.clickCustomerSearchTab();
	//		//-----------------FOR CA-----------------------
	//		cscockpitCustomerSearchTabPage.selectCustomerTypeFromDropDownInCustomerSearchTab("CONSULTANT");
	//		cscockpitCustomerSearchTabPage.selectCountryFromDropDownInCustomerSearchTab("Canada");
	//		cscockpitCustomerSearchTabPage.selectAccountStatusFromDropDownInCustomerSearchTab("Active");
	//		cscockpitCustomerSearchTabPage.clickSearchBtn();
	//		randomCustomerSequenceNumber = String.valueOf(cscockpitCustomerSearchTabPage.getRandomCustomerFromSearchResult());
	//		cscockpitCustomerSearchTabPage.clickCIDNumberInCustomerSearchTab(randomCustomerSequenceNumber);
	//		cscockpitCustomerTabPage.clickAutoshipIdOnCustomerTab();
	//		cscockpitAutoshipTemplateTabPage.clickRunNowButtonOnAutoshipTemplateTab();
	//		cscockpitAutoshipTemplateTabPage.clickOkForRegeneratedIdpopUp();
	//		s_assert.assertTrue(cscockpitAutoshipTemplateTabPage.verifyConfirmMessagePopUpIsAppearing(),"confirm message is not appearing for CA");
	//		cscockpitAutoshipTemplateTabPage.clickOkConfirmMessagePopUp();
	//		cscockpitAutoshipTemplateTabPage.clickCustomerSearchTab();
	//		cscockpitCustomerSearchTabPage.selectCustomerTypeFromDropDownInCustomerSearchTab("PC");
	//		cscockpitCustomerSearchTabPage.selectCountryFromDropDownInCustomerSearchTab("Canada");
	//		cscockpitCustomerSearchTabPage.selectAccountStatusFromDropDownInCustomerSearchTab("Active");
	//		cscockpitCustomerSearchTabPage.clickSearchBtn();
	//		randomCustomerSequenceNumber = String.valueOf(cscockpitCustomerSearchTabPage.getRandomCustomerFromSearchResult());
	//		cscockpitCustomerSearchTabPage.clickCIDNumberInCustomerSearchTab(randomCustomerSequenceNumber);
	//		cscockpitCustomerTabPage.clickAutoshipIdOnCustomerTab();
	//		cscockpitAutoshipTemplateTabPage.clickRunNowButtonOnAutoshipTemplateTab();
	//		cscockpitAutoshipTemplateTabPage.clickOkForRegeneratedIdpopUp();
	//		s_assert.assertTrue(cscockpitAutoshipTemplateTabPage.verifyConfirmMessagePopUpIsAppearing(),"confirm message is not appearing for CA");
	//		s_assert.assertAll();
	//	}
	//

	// Hybris Project-1949:To verify the order Type in the Order Detail page
	@Test
	public void testToVerifyTheOrderTypeInTheOrderDetailPage_1949(){
		String randomOrderSequenceNumber = null;
		String orderTypeConsultant = "Consultant Order";
		String orderTypePulseAutoship = "Pulse Autoship";
		String orderTypeCRPAutoship = "CRP Autoship";
		String orderTypePCPerksAutoship = "PCPerks Autoship";
		String orderTypePCOrder = "PC Order";
		String orderTypeRetailOrder = "Retail Order";
		String orderTypeReturn = "Return";
		String orderTypeOverrideOrder = "Override Order";

		driver.get(driver.getCSCockpitURL());
		cscockpitCustomerSearchTabPage = cscockpitLoginPage.clickLoginBtn();
		cscockpitCustomerSearchTabPage.clickFindOrderLinkOnLeftNavigation();
		cscockpitOrderSearchTabPage.selectOrderTypeInOrderSearchTab(orderTypeCRPAutoship);
		cscockpitOrderSearchTabPage.clickSearchBtn();
		randomOrderSequenceNumber = String.valueOf(cscockpitOrderSearchTabPage.getRandomOrdersFromOrderResultSearchFirstPageInOrderSearchTab());
		cscockpitOrderSearchTabPage.clickOrderNumberInOrderSearchResultsInOrderSearchTab(randomOrderSequenceNumber);
		s_assert.assertTrue(cscockpitOrderTabPage.validateOrderTypeOnOrderTab(orderTypeCRPAutoship),"Order Type is not CRP Autoship");
		cscockpitOrderTabPage.clickChangeOrderLinkOnLeftNavigation();
		cscockpitOrderSearchTabPage.selectOrderTypeInOrderSearchTab(orderTypeConsultant);
		cscockpitOrderSearchTabPage.clickSearchBtn();
		randomOrderSequenceNumber = String.valueOf(cscockpitOrderSearchTabPage.getRandomOrdersFromOrderResultSearchFirstPageInOrderSearchTab());
		cscockpitOrderSearchTabPage.clickOrderNumberInOrderSearchResultsInOrderSearchTab(randomOrderSequenceNumber);
		s_assert.assertTrue(cscockpitOrderTabPage.validateOrderTypeOnOrderTab(orderTypeConsultant) ||cscockpitOrderTabPage.validateOrderTypeOnOrderTab(orderTypeCRPAutoship) || cscockpitOrderTabPage.validateOrderTypeOnOrderTab(orderTypeOverrideOrder) || cscockpitOrderTabPage.validateOrderTypeOnOrderTab(orderTypePulseAutoship) ,"Order Type is not consultant order");
		cscockpitOrderTabPage.clickChangeOrderLinkOnLeftNavigation();
		cscockpitOrderSearchTabPage.selectOrderTypeInOrderSearchTab(orderTypePulseAutoship);
		cscockpitOrderSearchTabPage.clickSearchBtn();
		randomOrderSequenceNumber = String.valueOf(cscockpitOrderSearchTabPage.getRandomOrdersFromOrderResultSearchFirstPageInOrderSearchTab());
		cscockpitOrderSearchTabPage.clickOrderNumberInOrderSearchResultsInOrderSearchTab(randomOrderSequenceNumber);
		s_assert.assertTrue(cscockpitOrderTabPage.validateOrderTypeOnOrderTab(orderTypePulseAutoship),"Order Type is not Pulse Autoship");
		cscockpitOrderTabPage.clickChangeOrderLinkOnLeftNavigation();
		cscockpitOrderSearchTabPage.selectOrderTypeInOrderSearchTab(orderTypePCPerksAutoship);
		cscockpitOrderSearchTabPage.clickSearchBtn();
		randomOrderSequenceNumber = String.valueOf(cscockpitOrderSearchTabPage.getRandomOrdersFromOrderResultSearchFirstPageInOrderSearchTab());
		cscockpitOrderSearchTabPage.clickOrderNumberInOrderSearchResultsInOrderSearchTab(randomOrderSequenceNumber);
		s_assert.assertTrue(cscockpitOrderTabPage.validateOrderTypeOnOrderTab(orderTypePCPerksAutoship),"Order Type is not PCPerks Autoship");
		cscockpitOrderTabPage.clickChangeOrderLinkOnLeftNavigation();
		cscockpitOrderSearchTabPage.selectOrderTypeInOrderSearchTab(orderTypePCOrder);
		cscockpitOrderSearchTabPage.clickSearchBtn();
		randomOrderSequenceNumber = String.valueOf(cscockpitOrderSearchTabPage.getRandomOrdersFromOrderResultSearchFirstPageInOrderSearchTab());
		cscockpitOrderSearchTabPage.clickOrderNumberInOrderSearchResultsInOrderSearchTab(randomOrderSequenceNumber);
		s_assert.assertTrue(cscockpitOrderTabPage.getOrderTypeOnOrderTab().contains(orderTypePCOrder) || cscockpitOrderTabPage.getOrderTypeOnOrderTab().contains(orderTypePCPerksAutoship),"Order Type is not PC Order");
		cscockpitOrderTabPage.clickChangeOrderLinkOnLeftNavigation();
		cscockpitOrderSearchTabPage.selectOrderTypeInOrderSearchTab(orderTypeRetailOrder);
		cscockpitOrderSearchTabPage.clickSearchBtn();
		randomOrderSequenceNumber = String.valueOf(cscockpitOrderSearchTabPage.getRandomOrdersFromOrderResultSearchFirstPageInOrderSearchTab());
		cscockpitOrderSearchTabPage.clickOrderNumberInOrderSearchResultsInOrderSearchTab(randomOrderSequenceNumber);
		s_assert.assertTrue(cscockpitOrderTabPage.validateOrderTypeOnOrderTab(orderTypeRetailOrder),"Order Type is not Retail Order");
		cscockpitOrderTabPage.clickChangeOrderLinkOnLeftNavigation();
		cscockpitOrderSearchTabPage.selectOrderTypeInOrderSearchTab(orderTypeReturn);
		cscockpitOrderSearchTabPage.selectCountryFromDropDownInCustomerSearchTab("All");
		cscockpitOrderSearchTabPage.clickSearchBtn();
		randomOrderSequenceNumber = String.valueOf(cscockpitOrderSearchTabPage.getRandomOrdersFromOrderResultSearchFirstPageInOrderSearchTab());
		cscockpitOrderSearchTabPage.clickOrderNumberInOrderSearchResultsInOrderSearchTab(randomOrderSequenceNumber);
		s_assert.assertTrue(cscockpitOrderTabPage.validateOrderTypeOnOrderTab(orderTypeReturn),"Order Type is not Return Order");
		cscockpitOrderTabPage.clickChangeOrderLinkOnLeftNavigation();
		cscockpitOrderSearchTabPage.selectOrderTypeInOrderSearchTab(orderTypeOverrideOrder);
		cscockpitOrderSearchTabPage.selectCountryFromDropDownInCustomerSearchTab("All");
		cscockpitOrderSearchTabPage.clickSearchBtn();
		randomOrderSequenceNumber = String.valueOf(cscockpitOrderSearchTabPage.getRandomOrdersFromOrderResultSearchFirstPageInOrderSearchTab());
		cscockpitOrderSearchTabPage.clickOrderNumberInOrderSearchResultsInOrderSearchTab(randomOrderSequenceNumber);
		s_assert.assertTrue(cscockpitOrderTabPage.validateOrderTypeOnOrderTab(orderTypeOverrideOrder),"Order Type is not Override Order");
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
		String country = null;
		if(driver.getCountry().equalsIgnoreCase("ca")){
			country= TestConstants.COUNTRY_DD_VALUE_CA;
		}else{
			country= TestConstants.COUNTRY_DD_VALUE_US;
		}

		driver.get(driver.getStoreFrontURL()+"/"+driver.getCountry());
		while(true){
			randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_CONSULTANT_WITH_ORDERS_AND_AUTOSHIPS_RFO,countryId),RFO_DB);
			accountID=String.valueOf(getValueFromQueryResult(randomConsultantList, "AccountID"));
			emailIdFromAccountIdList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_EMAIL_ID_FROM_ACCOUNT_ID,accountID),RFO_DB);
			consultantEmailID=(String) getValueFromQueryResult(emailIdFromAccountIdList, "EmailAddress");
			try{
				storeFrontConsultantPage = storeFrontHomePage.loginAsConsultant(consultantEmailID, password);
			}catch(Exception e){
				driver.get(driver.getStoreFrontURL()+"/"+driver.getCountry());
				storeFrontConsultantPage = storeFrontHomePage.loginAsConsultant(consultantEmailID, password);
			}
			boolean isLoginError = driver.getCurrentUrl().contains("error");
			if(isLoginError){
				logger.info("Login error for the user "+consultantEmailID);
				driver.get(driver.getStoreFrontURL()+"/"+driver.getCountry());
			}
			else
				break;
		}
		logout();
		logger.info("login is successful");
		driver.get(driver.getCSCockpitURL());
		cscockpitCustomerSearchTabPage = cscockpitLoginPage.clickLoginBtn();
		cscockpitCustomerSearchTabPage.selectCustomerTypeFromDropDownInCustomerSearchTab("CONSULTANT");
		cscockpitCustomerSearchTabPage.selectCountryFromDropDownInCustomerSearchTab(country);
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
		cscockpitCheckoutTabPage.clickAddNewPaymentAddressInCheckoutTab();
		cscockpitCheckoutTabPage.enterBillingInfo();
		cscockpitCheckoutTabPage.clickSaveAddNewPaymentProfilePopUP();
		cscockpitCheckoutTabPage.enterCVVValueInCheckoutTab(TestConstants.VALID_CV2_NUMBER);
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
		driver.get(driver.getStoreFrontURL()+"/"+driver.getCountry());
		try{
			storeFrontConsultantPage = storeFrontHomePage.loginAsConsultant(consultantEmailID, password);
		}catch(Exception e){
			driver.get(driver.getStoreFrontURL()+"/"+driver.getCountry());
			storeFrontConsultantPage = storeFrontHomePage.loginAsConsultant(consultantEmailID, password);
		}
		storeFrontConsultantPage.clickOnWelcomeDropDown();
		storeFrontOrdersPage = storeFrontConsultantPage.clickOrdersLinkPresentOnWelcomeDropDown();

		s_assert.assertTrue(orderNumber.contains(storeFrontOrdersPage.getFirstOrderNumberFromOrderHistory()),"This Order is not present on the StoreFront of US");
		logout();
		s_assert.assertAll();
	}

	//Hybris Project-1839:To verify the commission date change functionality in the order detail Page
	@Test
	public void testVerifyCommissionDateChangeFunctionalityInOrderDetailPage_1839(){
		String randomCustomerSequenceNumber = null;
		String country = null;
		if(driver.getCountry().equalsIgnoreCase("ca")){
			country= TestConstants.COUNTRY_DD_VALUE_CA;
		}else{
			country= TestConstants.COUNTRY_DD_VALUE_US;
		}
		cscockpitCustomerSearchTabPage = cscockpitLoginPage.clickLoginBtn();
		cscockpitCustomerSearchTabPage.selectCustomerTypeFromDropDownInCustomerSearchTab("CONSULTANT");
		cscockpitCustomerSearchTabPage.selectCountryFromDropDownInCustomerSearchTab(country);
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
		cscockpitCustomerSearchTabPage.selectCountryFromDropDownInCustomerSearchTab(country);
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
		cscockpitCustomerSearchTabPage.selectCountryFromDropDownInCustomerSearchTab(country);
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

	//Hybris Project-4073:CSCockpit Refund Order
	@Test
	public void testVerifyCscockpitRefundOrderFunctionality_4073() throws InterruptedException{
		String randomCustomerSequenceNumber = null;
		String consultantEmailID = null;
		String orderNumber = null;
		String orderHistoryNumber = null;
		String randomProductSequenceNumber = null;
		String SKUValue = null;
		String accountID=null;
		RFO_DB = driver.getDBNameRFO();
		String country = null;
		if(driver.getCountry().equalsIgnoreCase("ca")){
			country= TestConstants.COUNTRY_DD_VALUE_CA;
		}else{
			country= TestConstants.COUNTRY_DD_VALUE_US;
		}

		driver.get(driver.getStoreFrontURL()+"/"+driver.getCountry());
		List<Map<String, Object>> randomConsultantList =  null;
		List<Map<String, Object>> randomConsultantUsernameList =  null;
		storeFrontHomePage = new StoreFrontHomePage(driver);
		while(true){
			randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_CONSULTANT_WITH_ORDERS_AND_AUTOSHIPS_RFO,countryId),RFO_DB);
			accountID=String.valueOf(getValueFromQueryResult(randomConsultantList, "AccountID"));
			randomConsultantUsernameList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_EMAIL_ID_FROM_ACCOUNT_ID,accountID),RFO_DB);
			consultantEmailID = (String) getValueFromQueryResult(randomConsultantUsernameList, "EmailAddress");
			try{
				storeFrontConsultantPage = storeFrontHomePage.loginAsConsultant(consultantEmailID, password);
			}catch(Exception e){
				driver.get(driver.getStoreFrontURL()+"/"+driver.getCountry());
				storeFrontConsultantPage = storeFrontHomePage.loginAsConsultant(consultantEmailID, password);
			}
			boolean isLoginError = driver.getCurrentUrl().contains("error");
			if(isLoginError){
				logger.info("Login error for the user "+consultantEmailID);
				driver.get(driver.getStoreFrontURL()+"/"+driver.getCountry());
			}
			else
				break;
		}
		logout();
		logger.info("login is successful");
		cscockpitLoginPage = new CSCockpitLoginPage(driver);
		driver.get(driver.getCSCockpitURL());  
		cscockpitCustomerSearchTabPage = cscockpitLoginPage.clickLoginBtn();
		cscockpitCustomerSearchTabPage.selectCustomerTypeFromDropDownInCustomerSearchTab("CONSULTANT");
		cscockpitCustomerSearchTabPage.selectCountryFromDropDownInCustomerSearchTab(country);
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
		if(driver.getCountry().equalsIgnoreCase("us")){
			s_assert.assertTrue(cscockpitCheckoutTabPage.getDeliverModeTypeInCheckoutTab().contains("FedEx Ground (HD)"),"CSCockpit checkout tab delivery mode type expected = FedEx Ground (HD) and on UI = " +cscockpitCheckoutTabPage.getDeliverModeTypeInCheckoutTab());
		}else{
			s_assert.assertTrue(cscockpitCheckoutTabPage.getDeliverModeTypeInCheckoutTab().contains("UPS Ground (HD)"),"CSCockpit checkout tab delivery mode type expected = UPS Ground (HD) and on UI = " +cscockpitCheckoutTabPage.getDeliverModeTypeInCheckoutTab());
		}
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

		driver.get(driver.getStoreFrontURL()+"/"+driver.getCountry());
		storeFrontHomePage = new StoreFrontHomePage(driver);
		try{
			storeFrontConsultantPage = storeFrontHomePage.loginAsConsultant(consultantEmailID, password);
		}catch(Exception e){
			driver.get(driver.getStoreFrontURL()+"/"+driver.getCountry());
			storeFrontConsultantPage = storeFrontHomePage.loginAsConsultant(consultantEmailID, password);
		}
		storeFrontConsultantPage.clickOnWelcomeDropDown();
		storeFrontOrdersPage =  storeFrontConsultantPage.clickOrdersLinkPresentOnWelcomeDropDown();
		s_assert.assertTrue(storeFrontOrdersPage.verifyOrdersPageIsDisplayed(),"Orders page has not been displayed");
		orderHistoryNumber = storeFrontOrdersPage.getFirstOrderNumberFromOrderHistory();
		s_assert.assertTrue(orderHistoryNumber.contains(orderNumber.split("\\-")[0].trim()),"CSCockpit Order number expected = "+orderNumber.split("\\-")[0].trim()+" and on UI = " +orderHistoryNumber);

		s_assert.assertAll();  
	}

	//Hybris Project-1952:To verify the Sales Override from Order detail Page
	@Test
	public void testVerifyTheSalesOverrideFromOrderDetailPage_1952() throws InterruptedException{
		RFO_DB = driver.getDBNameRFO();
		String randomCustomerSequenceNumber = null;
		String randomProductSequenceNumber = null;
		String consultantEmailID = null;
		String SKUValue = null;
		String priceValue = TestConstants.PRICE_VALUE;
		String cvValue = TestConstants.CVVALUE;
		String qvValue = TestConstants.QVVALUE;
		String deliveryCost = TestConstants.DELIVERY_COST;
		String handlingCost = TestConstants.HANDLING_COST;
		int randomNum = CommonUtils.getRandomNum(10000, 1000000);
		List<Map<String, Object>> randomConsultantList =  null;
		List<Map<String, Object>> emailIdFromAccountIdList =  null;
		List<Map<String, Object>> randomPCList =  null;
		List<Map<String, Object>> randomRCList =  null;
		String accountID = null;
		String deliveryCosts = "Delivery Costs";
		String handlingCosts = "Handling Costs";
		String totalCV = "Total CV";
		String totalQV = "Total QV";
		String pcEmailID = null;
		String rcEmailID = null;
		String country = null;
		if(driver.getCountry().equalsIgnoreCase("ca")){
			country= TestConstants.COUNTRY_DD_VALUE_CA;
		}else{
			country= TestConstants.COUNTRY_DD_VALUE_US;
		}

	
		//get emailId of username
		emailIdFromAccountIdList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_EMAIL_ID_FROM_ACCOUNT_ID,accountID),RFO_DB);
		consultantEmailID = String.valueOf(getValueFromQueryResult(emailIdFromAccountIdList, "EmailAddress"));  
		logger.info("emaild of username "+consultantEmailID); 
		logger.info("login is successful");
		driver.get(driver.getCSCockpitURL());
		cscockpitCustomerSearchTabPage = cscockpitLoginPage.clickLoginBtn();
		cscockpitCustomerSearchTabPage.selectCustomerTypeFromDropDownInCustomerSearchTab("CONSULTANT");
		cscockpitCustomerSearchTabPage.selectCountryFromDropDownInCustomerSearchTab(country);
		cscockpitCustomerSearchTabPage.selectAccountStatusFromDropDownInCustomerSearchTab("Active");
		cscockpitCustomerSearchTabPage.enterEmailIdInSearchFieldInCustomerSearchTab(consultantEmailID);
		cscockpitCustomerSearchTabPage.clickSearchBtn();
		randomCustomerSequenceNumber = String.valueOf(cscockpitCustomerSearchTabPage.getRandomCustomerFromSearchResult());
		cscockpitCustomerSearchTabPage.clickCIDNumberInCustomerSearchTab(randomCustomerSequenceNumber);
		cscockpitCustomerTabPage.clickFirstOrderInCustomerTab();
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
		String orderNotevalueFromUI = cscockpitCheckoutTabPage.getAddedNoteValueInCheckoutTab(TestConstants.ORDER_NOTE+randomNum);
		String pstDate = cscockpitCheckoutTabPage.getPSTDate();
		String orderDate = cscockpitCheckoutTabPage.converPSTDateToUIFormat(pstDate);
		s_assert.assertTrue(cscockpitCheckoutTabPage.convertUIDateFormatToPSTFormat(orderNotevalueFromUI.split("\\ ")[0]).trim().contains(cscockpitCheckoutTabPage.getPSTDate()) || cscockpitCheckoutTabPage.convertUIDateFormatToPSTFormat(orderNotevalueFromUI.split("\\ ")[0]).trim().contains(orderDate),"CSCockpit added order note date in checkout tab expected"+cscockpitCheckoutTabPage.getPSTDate()+"and on UI" +cscockpitCheckoutTabPage.convertUIDateFormatToPSTFormat(orderNotevalueFromUI.split("\\ ")[0]).trim());
		s_assert.assertTrue(orderNotevalueFromUI.contains("PM")||orderNotevalueFromUI.contains("AM"), "Added order note does not contain time zone");
		s_assert.assertTrue(cscockpitCheckoutTabPage.verifyEditButtonIsPresentForOrderNoteInCheckoutTab(TestConstants.ORDER_NOTE+randomNum), "Added order note does not have Edit button");
		cscockpitCheckoutTabPage.clickOrderNoteEditButton(TestConstants.ORDER_NOTE+randomNum);
		cscockpitCheckoutTabPage.updateOrderNoteOnCheckOutPage(TestConstants.UPDATED_ORDER_NOTE+randomNum);
		orderNotevalueFromUI = cscockpitCheckoutTabPage.getAddedNoteValueInCheckoutTab(TestConstants.UPDATED_ORDER_NOTE+randomNum);
		s_assert.assertTrue(cscockpitCheckoutTabPage.convertUIDateFormatToPSTFormat(orderNotevalueFromUI.split("\\ ")[0]).trim().contains(cscockpitCheckoutTabPage.getPSTDate()) || cscockpitCheckoutTabPage.convertUIDateFormatToPSTFormat(orderNotevalueFromUI.split("\\ ")[0]).trim().contains(orderDate),"CSCockpit added order note date in checkout tab expected after update"+cscockpitCheckoutTabPage.getPSTDate()+"and on UI" +cscockpitCheckoutTabPage.convertUIDateFormatToPSTFormat(orderNotevalueFromUI.split("\\ ")[0]).trim());
		cscockpitCheckoutTabPage.clickAddNewPaymentAddressInCheckoutTab();
		cscockpitCheckoutTabPage.enterBillingInfo();
		cscockpitCheckoutTabPage.clickSaveAddNewPaymentProfilePopUP();
		cscockpitCheckoutTabPage.enterCVVValueInCheckoutTab(TestConstants.SECURITY_CODE);
		cscockpitCheckoutTabPage.clickUseThisCardBtnInCheckoutTab();
		cscockpitCheckoutTabPage.clickPerformSooButton();
		cscockpitCheckoutTabPage.enterPriceValueInSalesOrderOverridePopUp(priceValue);
		cscockpitCheckoutTabPage.enterCVValueInSalesOrderOverrridePoPuP(cvValue);
		cscockpitCheckoutTabPage.enterQVValueInSalesOrderOvverridePopUp(qvValue);
		cscockpitCheckoutTabPage.enterDeliveryCostsInSalesOrderOvverridePopUp(deliveryCost);
		cscockpitCheckoutTabPage.enterHandlingCostsInSalesOrderOvveridePOpUp(handlingCost);
		cscockpitCheckoutTabPage.selectOverrideReasonSooDept();
		cscockpitCheckoutTabPage.selectOverrideReasonSooType();
		cscockpitCheckoutTabPage.selectOverrideReasonSooReason();
		cscockpitCheckoutTabPage.clickUpdateButtonSalesOverridePopUp();
		s_assert.assertTrue(cscockpitCheckoutTabPage.getValuesFromTotalsSection(deliveryCosts).contains(deliveryCost), "Expected delivery cost for consultant is "+deliveryCost+" Actual on UI is "+cscockpitCheckoutTabPage.getValuesFromTotalsSection(deliveryCosts));
		s_assert.assertTrue(cscockpitCheckoutTabPage.getValuesFromTotalsSection(handlingCosts).contains(handlingCost), "Expected handling cost for consultant is "+handlingCost+" Actual on UI is "+cscockpitCheckoutTabPage.getValuesFromTotalsSection(handlingCosts));
		s_assert.assertTrue(cscockpitCheckoutTabPage.getValuesFromTotalsSection(totalCV).contains(cvValue), "Expected CV value for consultant is "+cvValue+" Actual on UI is "+cscockpitCheckoutTabPage.getValuesFromTotalsSection(totalCV));
		s_assert.assertTrue(cscockpitCheckoutTabPage.getValuesFromTotalsSection(totalQV).contains(qvValue), "Expected QV value for consultant is "+qvValue+" Actual on UI is "+cscockpitCheckoutTabPage.getValuesFromTotalsSection(totalQV));
		cscockpitCheckoutTabPage.clickPlaceOrderButtonInCheckoutTab();
		s_assert.assertTrue(cscockpitOrderTabPage.getOrderStatusAfterPlaceOrderInOrderTab().contains("SUBMITTED"),"order is not submitted successfully");
		String orderNumber = cscockpitOrderTabPage.getOrderNumberFromCsCockpitUIOnOrderTab();
		driver.get(driver.getStoreFrontURL()+"/"+driver.getCountry());
		storeFrontConsultantPage = storeFrontHomePage.loginAsConsultant(consultantEmailID, password);
		storeFrontConsultantPage.clickOnWelcomeDropDown();
		storeFrontOrdersPage = storeFrontConsultantPage.clickOrdersLinkPresentOnWelcomeDropDown();
		s_assert.assertTrue(orderNumber.contains(storeFrontOrdersPage.getFirstOrderNumberFromOrderHistory()),"This Order is not present on the StoreFront of US for consultant");
		storeFrontOrdersPage.clickOnFirstAdHocOrder();
		//Assert Subtotal with RFO
		s_assert.assertTrue(storeFrontOrdersPage.getSubTotalFromAutoshipTemplate().contains(priceValue),"Adhoc Order template subtotal on RFO is "+priceValue+" and on UI is "+storeFrontOrdersPage.getSubTotalFromAutoshipTemplate());
		// assert shipping amount with RFO
		s_assert.assertTrue(storeFrontOrdersPage.getShippingAmountFromAutoshipTemplate().contains(deliveryCost),"Adhoc Order template shipping amount on RFO is "+deliveryCost+" and on UI is "+storeFrontOrdersPage.getShippingAmountFromAutoshipTemplate());
		// assert Handling Value with RFO
		//s_assert.assertTrue(storeFrontOrdersPage.getHandlingAmountFromAutoshipTemplate().contains(handlingCost),"Adhoc Order template handling amount on RFO is "+handlingCost+" and on UI is "+storeFrontOrdersPage.getHandlingAmountFromAutoshipTemplate());
		logout();

		//For PC
		driver.get(driver.getStoreFrontURL()+"/"+driver.getCountry());
		while(true){
			randomPCList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_PC_WITH_ORDERS_AND_AUTOSHIPS_RFO,countryId),RFO_DB);
			pcEmailID  = (String) getValueFromQueryResult(randomPCList, "UserName");  
			accountID = String.valueOf(getValueFromQueryResult(randomPCList, "AccountID")); 
			logger.info("Account Id of user "+accountID);
			try{
				storeFrontPCUserPage = storeFrontHomePage.loginAsPCUser(pcEmailID, password);
			}catch(Exception e){
				driver.get(driver.getStoreFrontURL()+"/"+driver.getCountry());
				storeFrontPCUserPage = storeFrontHomePage.loginAsPCUser(pcEmailID, password);
			}
			boolean isLoginError = driver.getCurrentUrl().contains("error");
			if(isLoginError){
				logger.info("Login error for the user "+pcEmailID);
				driver.get(driver.getStoreFrontURL()+"/"+driver.getCountry());
			}
			else
				break;
		}
		logger.info("login is successful"); 
		logout();
		//get emailId of username
		emailIdFromAccountIdList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_EMAIL_ID_FROM_ACCOUNT_ID,accountID),RFO_DB);
		pcEmailID = String.valueOf(getValueFromQueryResult(emailIdFromAccountIdList, "EmailAddress"));  
		logger.info("emaild of username "+pcEmailID); 
		logger.info("login is successful");
		driver.get(driver.getCSCockpitURL());
		cscockpitCustomerSearchTabPage = cscockpitLoginPage.clickLoginBtn();
		cscockpitCustomerSearchTabPage.selectCustomerTypeFromDropDownInCustomerSearchTab("PC");
		cscockpitCustomerSearchTabPage.selectCountryFromDropDownInCustomerSearchTab(country);
		cscockpitCustomerSearchTabPage.selectAccountStatusFromDropDownInCustomerSearchTab("Active");
		cscockpitCustomerSearchTabPage.enterEmailIdInSearchFieldInCustomerSearchTab(pcEmailID);
		cscockpitCustomerSearchTabPage.clickSearchBtn();
		randomCustomerSequenceNumber = String.valueOf(cscockpitCustomerSearchTabPage.getRandomCustomerFromSearchResult());
		cscockpitCustomerSearchTabPage.clickCIDNumberInCustomerSearchTab(randomCustomerSequenceNumber);
		cscockpitCustomerTabPage.clickFirstOrderInCustomerTab();
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
		orderNotevalueFromUI = cscockpitCheckoutTabPage.getAddedNoteValueInCheckoutTab(TestConstants.ORDER_NOTE+randomNum);
		pstDate = cscockpitCheckoutTabPage.getPSTDate();
		orderDate = cscockpitCheckoutTabPage.converPSTDateToUIFormat(pstDate);
		s_assert.assertTrue(cscockpitCheckoutTabPage.convertUIDateFormatToPSTFormat(orderNotevalueFromUI.split("\\ ")[0]).trim().contains(cscockpitCheckoutTabPage.getPSTDate()) || cscockpitCheckoutTabPage.convertUIDateFormatToPSTFormat(orderNotevalueFromUI.split("\\ ")[0]).trim().contains(orderDate),"CSCockpit added order note date in checkout tab expected"+cscockpitCheckoutTabPage.getPSTDate()+"and on UI" +cscockpitCheckoutTabPage.convertUIDateFormatToPSTFormat(orderNotevalueFromUI.split("\\ ")[0]).trim());
		s_assert.assertTrue(orderNotevalueFromUI.contains("PM")||orderNotevalueFromUI.contains("AM"), "Added order note does not contain time zone");
		s_assert.assertTrue(cscockpitCheckoutTabPage.verifyEditButtonIsPresentForOrderNoteInCheckoutTab(TestConstants.ORDER_NOTE+randomNum), "Added order note does not have Edit button");
		cscockpitCheckoutTabPage.clickOrderNoteEditButton(TestConstants.ORDER_NOTE+randomNum);
		cscockpitCheckoutTabPage.updateOrderNoteOnCheckOutPage(TestConstants.UPDATED_ORDER_NOTE+randomNum);
		orderNotevalueFromUI = cscockpitCheckoutTabPage.getAddedNoteValueInCheckoutTab(TestConstants.UPDATED_ORDER_NOTE+randomNum);
		s_assert.assertTrue(cscockpitCheckoutTabPage.convertUIDateFormatToPSTFormat(orderNotevalueFromUI.split("\\ ")[0]).trim().contains(cscockpitCheckoutTabPage.getPSTDate()) || cscockpitCheckoutTabPage.convertUIDateFormatToPSTFormat(orderNotevalueFromUI.split("\\ ")[0]).trim().contains(orderDate),"CSCockpit added order note date in checkout tab expected after update"+cscockpitCheckoutTabPage.getPSTDate()+"and on UI" +cscockpitCheckoutTabPage.convertUIDateFormatToPSTFormat(orderNotevalueFromUI.split("\\ ")[0]).trim());
		cscockpitCheckoutTabPage.clickAddNewPaymentAddressInCheckoutTab();
		cscockpitCheckoutTabPage.enterBillingInfo();
		cscockpitCheckoutTabPage.clickSaveAddNewPaymentProfilePopUP();
		cscockpitCheckoutTabPage.enterCVVValueInCheckoutTab(TestConstants.SECURITY_CODE);
		cscockpitCheckoutTabPage.clickUseThisCardBtnInCheckoutTab();
		cscockpitCheckoutTabPage.clickPerformSooButton();
		cscockpitCheckoutTabPage.enterPriceValueInSalesOrderOverridePopUp(priceValue);
		cscockpitCheckoutTabPage.enterCVValueInSalesOrderOverrridePoPuP(cvValue);
		cscockpitCheckoutTabPage.enterQVValueInSalesOrderOvverridePopUp(qvValue);
		cscockpitCheckoutTabPage.enterDeliveryCostsInSalesOrderOvverridePopUp(deliveryCost);
		cscockpitCheckoutTabPage.enterHandlingCostsInSalesOrderOvveridePOpUp(handlingCost);
		cscockpitCheckoutTabPage.selectOverrideReasonSooDept();
		cscockpitCheckoutTabPage.selectOverrideReasonSooType();
		cscockpitCheckoutTabPage.selectOverrideReasonSooReason();
		cscockpitCheckoutTabPage.clickUpdateButtonSalesOverridePopUp();
		s_assert.assertTrue(cscockpitCheckoutTabPage.getValuesFromTotalsSection(deliveryCosts).contains(deliveryCost), "Expected delivery cost for PC is "+deliveryCost+" Actual on UI is "+cscockpitCheckoutTabPage.getValuesFromTotalsSection(deliveryCosts));
		s_assert.assertTrue(cscockpitCheckoutTabPage.getValuesFromTotalsSection(handlingCosts).contains(handlingCost), "Expected handling cost for PC is "+handlingCost+" Actual on UI is "+cscockpitCheckoutTabPage.getValuesFromTotalsSection(handlingCosts));
		s_assert.assertTrue(cscockpitCheckoutTabPage.getValuesFromTotalsSection(totalCV).contains(cvValue), "Expected CV value for PC is "+cvValue+" Actual on UI is "+cscockpitCheckoutTabPage.getValuesFromTotalsSection(totalCV));
		s_assert.assertTrue(cscockpitCheckoutTabPage.getValuesFromTotalsSection(totalQV).contains(qvValue), "Expected QV value for PC is "+qvValue+" Actual on UI is "+cscockpitCheckoutTabPage.getValuesFromTotalsSection(totalQV));
		cscockpitCheckoutTabPage.clickPlaceOrderButtonInCheckoutTab();
		s_assert.assertTrue(cscockpitOrderTabPage.getOrderStatusAfterPlaceOrderInOrderTab().contains("SUBMITTED"),"order is not submitted successfully");
		orderNumber = cscockpitOrderTabPage.getOrderNumberFromCsCockpitUIOnOrderTab();
		driver.get(driver.getStoreFrontURL()+"/"+driver.getCountry());
		storeFrontPCUserPage = storeFrontHomePage.loginAsPCUser(pcEmailID, password);
		storeFrontPCUserPage.clickOnWelcomeDropDown();
		storeFrontOrdersPage = storeFrontPCUserPage.clickOrdersLinkPresentOnWelcomeDropDown();
		s_assert.assertTrue(orderNumber.contains(storeFrontOrdersPage.getFirstOrderNumberFromOrderHistory()),"This Order is not present on the StoreFront of US for PC");
		storeFrontOrdersPage.clickOnFirstAdHocOrder();
		//Assert Subtotal with RFO
		s_assert.assertTrue(storeFrontOrdersPage.getSubTotalFromAutoshipTemplate().contains(priceValue),"Adhoc Order template subtotal on RFO is "+priceValue+" and on UI is "+storeFrontOrdersPage.getSubTotalFromAutoshipTemplate());
		// assert shipping amount with RFO
		s_assert.assertTrue(storeFrontOrdersPage.getShippingAmountFromAutoshipTemplate().contains(deliveryCost),"Adhoc Order template shipping amount on RFO is "+deliveryCost+" and on UI is "+storeFrontOrdersPage.getShippingAmountFromAutoshipTemplate());
		// assert Handling Value with RFO
		//s_assert.assertTrue(storeFrontOrdersPage.getHandlingAmountFromAutoshipTemplate().contains(handlingCost),"Adhoc Order template handling amount on RFO is "+handlingCost+" and on UI is "+storeFrontOrdersPage.getHandlingAmountFromAutoshipTemplate());
		logout();

		//For RC
		driver.get(driver.getStoreFrontURL()+"/"+driver.getCountry());
		while(true){
			randomRCList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_RC_RFO,countryId),RFO_DB);
			rcEmailID  = (String) getValueFromQueryResult(randomRCList, "UserName");  
			accountID = String.valueOf(getValueFromQueryResult(randomRCList, "AccountID")); 
			logger.info("Account Id of user "+accountID);
			try{
				storeFrontRCUserPage = storeFrontHomePage.loginAsRCUser(rcEmailID, password);
			}catch(Exception e){
				driver.get(driver.getStoreFrontURL()+"/"+driver.getCountry());
				storeFrontRCUserPage = storeFrontHomePage.loginAsRCUser(rcEmailID, password);
			}
			boolean isLoginError = driver.getCurrentUrl().contains("error");
			if(isLoginError){
				logger.info("Login error for the user "+rcEmailID);
				driver.get(driver.getStoreFrontURL()+"/"+driver.getCountry());
			}
			else
				break;
		}
		logger.info("login is successful"); 
		logout();
		//get emailId of username
		emailIdFromAccountIdList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_EMAIL_ID_FROM_ACCOUNT_ID,accountID),RFO_DB);
		rcEmailID = String.valueOf(getValueFromQueryResult(emailIdFromAccountIdList, "EmailAddress"));  
		logger.info("emaild of username "+rcEmailID); 
		logger.info("login is successful");
		driver.get(driver.getCSCockpitURL());
		cscockpitCustomerSearchTabPage = cscockpitLoginPage.clickLoginBtn();
		cscockpitCustomerSearchTabPage.selectCustomerTypeFromDropDownInCustomerSearchTab("RETAIL");
		cscockpitCustomerSearchTabPage.selectCountryFromDropDownInCustomerSearchTab(country);
		cscockpitCustomerSearchTabPage.selectAccountStatusFromDropDownInCustomerSearchTab("Active");
		cscockpitCustomerSearchTabPage.enterEmailIdInSearchFieldInCustomerSearchTab(rcEmailID);
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
		orderNotevalueFromUI = cscockpitCheckoutTabPage.getAddedNoteValueInCheckoutTab(TestConstants.ORDER_NOTE+randomNum);
		pstDate = cscockpitCheckoutTabPage.getPSTDate();
		orderDate = cscockpitCheckoutTabPage.converPSTDateToUIFormat(pstDate);
		s_assert.assertTrue(cscockpitCheckoutTabPage.convertUIDateFormatToPSTFormat(orderNotevalueFromUI.split("\\ ")[0]).trim().contains(cscockpitCheckoutTabPage.getPSTDate()) || cscockpitCheckoutTabPage.convertUIDateFormatToPSTFormat(orderNotevalueFromUI.split("\\ ")[0]).trim().contains(orderDate),"CSCockpit added order note date in checkout tab expected"+cscockpitCheckoutTabPage.getPSTDate()+"and on UI" +cscockpitCheckoutTabPage.convertUIDateFormatToPSTFormat(orderNotevalueFromUI.split("\\ ")[0]).trim());
		s_assert.assertTrue(orderNotevalueFromUI.contains("PM")||orderNotevalueFromUI.contains("AM"), "Added order note does not contain time zone");
		s_assert.assertTrue(cscockpitCheckoutTabPage.verifyEditButtonIsPresentForOrderNoteInCheckoutTab(TestConstants.ORDER_NOTE+randomNum), "Added order note does not have Edit button");
		cscockpitCheckoutTabPage.clickOrderNoteEditButton(TestConstants.ORDER_NOTE+randomNum);
		cscockpitCheckoutTabPage.updateOrderNoteOnCheckOutPage(TestConstants.UPDATED_ORDER_NOTE+randomNum);
		orderNotevalueFromUI = cscockpitCheckoutTabPage.getAddedNoteValueInCheckoutTab(TestConstants.UPDATED_ORDER_NOTE+randomNum);
		s_assert.assertTrue(cscockpitCheckoutTabPage.convertUIDateFormatToPSTFormat(orderNotevalueFromUI.split("\\ ")[0]).trim().contains(cscockpitCheckoutTabPage.getPSTDate()) || cscockpitCheckoutTabPage.convertUIDateFormatToPSTFormat(orderNotevalueFromUI.split("\\ ")[0]).trim().contains(orderDate),"CSCockpit added order note date in checkout tab expected after update"+cscockpitCheckoutTabPage.getPSTDate()+"and on UI" +cscockpitCheckoutTabPage.convertUIDateFormatToPSTFormat(orderNotevalueFromUI.split("\\ ")[0]).trim());
		cscockpitCheckoutTabPage.clickAddNewPaymentAddressInCheckoutTab();
		cscockpitCheckoutTabPage.enterBillingInfo();
		cscockpitCheckoutTabPage.clickSaveAddNewPaymentProfilePopUP();
		cscockpitCheckoutTabPage.enterCVVValueInCheckoutTab(TestConstants.SECURITY_CODE);
		cscockpitCheckoutTabPage.clickUseThisCardBtnInCheckoutTab();
		cscockpitCheckoutTabPage.clickPerformSooButton();
		cscockpitCheckoutTabPage.enterPriceValueInSalesOrderOverridePopUp(priceValue);
		cscockpitCheckoutTabPage.enterCVValueInSalesOrderOverrridePoPuP(cvValue);
		cscockpitCheckoutTabPage.enterQVValueInSalesOrderOvverridePopUp(qvValue);
		cscockpitCheckoutTabPage.enterDeliveryCostsInSalesOrderOvverridePopUp(deliveryCost);
		cscockpitCheckoutTabPage.enterHandlingCostsInSalesOrderOvveridePOpUp(handlingCost);
		cscockpitCheckoutTabPage.selectOverrideReasonSooDept();
		cscockpitCheckoutTabPage.selectOverrideReasonSooType();
		cscockpitCheckoutTabPage.selectOverrideReasonSooReason();
		cscockpitCheckoutTabPage.clickUpdateButtonSalesOverridePopUp();
		s_assert.assertTrue(cscockpitCheckoutTabPage.getValuesFromTotalsSection(deliveryCosts).contains(deliveryCost), "Expected delivery cost for RC is "+deliveryCost+" Actual on UI is "+cscockpitCheckoutTabPage.getValuesFromTotalsSection(deliveryCosts));
		s_assert.assertTrue(cscockpitCheckoutTabPage.getValuesFromTotalsSection(handlingCosts).contains(handlingCost), "Expected handling cost for RC is "+handlingCost+" Actual on UI is "+cscockpitCheckoutTabPage.getValuesFromTotalsSection(handlingCosts));
		s_assert.assertTrue(cscockpitCheckoutTabPage.getValuesFromTotalsSection(totalCV).contains(cvValue), "Expected CV value for RC is "+cvValue+" Actual on UI is "+cscockpitCheckoutTabPage.getValuesFromTotalsSection(totalCV));
		s_assert.assertTrue(cscockpitCheckoutTabPage.getValuesFromTotalsSection(totalQV).contains(qvValue), "Expected QV value for RC is "+qvValue+" Actual on UI is "+cscockpitCheckoutTabPage.getValuesFromTotalsSection(totalQV));
		cscockpitCheckoutTabPage.clickPlaceOrderButtonInCheckoutTab();
		s_assert.assertTrue(cscockpitOrderTabPage.getOrderStatusAfterPlaceOrderInOrderTab().contains("SUBMITTED"),"order is not submitted successfully");
		orderNumber = cscockpitOrderTabPage.getOrderNumberFromCsCockpitUIOnOrderTab();
		driver.get(driver.getStoreFrontURL()+"/"+driver.getCountry());
		storeFrontRCUserPage = storeFrontHomePage.loginAsRCUser(rcEmailID, password);
		storeFrontRCUserPage.clickOnWelcomeDropDown();
		storeFrontOrdersPage = storeFrontRCUserPage.clickOrdersLinkPresentOnWelcomeDropDown();
		s_assert.assertTrue(orderNumber.contains(storeFrontOrdersPage.getFirstOrderNumberFromOrderHistory()),"This Order is not present on the StoreFront of US for RC");
		storeFrontOrdersPage.clickOnFirstAdHocOrder();
		//Assert Subtotal with RFO
		s_assert.assertTrue(storeFrontOrdersPage.getSubTotalFromAutoshipTemplate().contains(priceValue),"Adhoc Order template subtotal on RFO is "+priceValue+" and on UI is "+storeFrontOrdersPage.getSubTotalFromAutoshipTemplate());
		// assert shipping amount with RFO
		s_assert.assertTrue(storeFrontOrdersPage.getShippingAmountFromAutoshipTemplate().contains(deliveryCost),"Adhoc Order template shipping amount on RFO is "+deliveryCost+" and on UI is "+storeFrontOrdersPage.getShippingAmountFromAutoshipTemplate());
		// assert Handling Value with RFO
		//s_assert.assertTrue(storeFrontOrdersPage.getHandlingAmountFromAutoshipTemplate().contains(handlingCost),"Adhoc Order template handling amount on RFO is "+handlingCost+" and on UI is "+storeFrontOrdersPage.getHandlingAmountFromAutoshipTemplate());
		logout();
		s_assert.assertAll();
	}

	
	
	//Hybris Project-1954:Verify the Sales Override functionality
	@Test
	public void testVerifyTheSalesOverrideFunctionality_1954() throws InterruptedException{
		RFO_DB = driver.getDBNameRFO();
		String randomCustomerSequenceNumber = null;
		String randomProductSequenceNumber = null;
		String consultantEmailID = null;
		String SKUValue = null;
		String priceValue = "0";
		String cvValue = "0";
		String qvValue = "0";
		String deliveryCost = "0";
		String handlingCost = "0";
		List<Map<String, Object>> randomConsultantList =  null;
		List<Map<String, Object>> emailIdFromAccountIdList =  null;
		List<Map<String, Object>> randomPCList =  null;
		List<Map<String, Object>> randomRCList =  null;
		String accountID = null;
		String country = null;
		if(driver.getCountry().equalsIgnoreCase("ca")){
			country= TestConstants.COUNTRY_DD_VALUE_CA;
		}else{
			country= TestConstants.COUNTRY_DD_VALUE_US;
		}
		String deliveryCosts = "Delivery Costs";
		String handlingCosts = "Handling Costs";
		String totalCV = "Total CV";
		String totalQV = "Total QV";
		String pcEmailID = null;
		String rcEmailID = null;
		String performSOOHeader = "Perform SOO";
		String salesOrderOverride = "SALES ORDER OVERRIDE";
		String overrideReasons = "Override Reasons";
		String associateToOrder = "Associate to Order# (optional)";
		String subtotal = "Subtotal";
		String discount = "Discount";
		String taxes = "Taxes";
		String totalPrice = "Total Price";
		String sku = "SKU";
		String product = "Product";
		String qty = "Qty";
		String price = "Price";
		String adjdPrice = "Adjd Price";
		String CV = "CV";
		String QV = "QV";
		String sales = "Sales";
		String prodDev = "Prod";
		String ops = "Ops";
		String marketing = "Marketing";
		String it = "IT";
		String gA = "G&A";
		String coG = "CoG";
		String appeasement = "Appeasement";
		String testing = "Testing";
		String consignment = "Consignment";
		String donation = "Donation";
		String employeePerks = "Employee";
		String exchange = "Exchange";
		String gifts = "Gifts";
		String incentives = "Incentives";
		String leadership = "Leadership";
		String meetings = "Meetings";
		String publicRelations = "Relations";
		String qAQC = "QA/QC";
		String reship = "Reship";
		String samples = "Samples";
		String upgrade = "Upgrade";
		String sale = "Sale";
		String damaged = "Damaged";
		String lostinTransit = "Transit";
		String missing= "Missing";
		String QCIssue= "Issue";
		String reactiontoProduct= "Reaction";
		String wrongItemSent= "Wrong";
		String notRelatedtoType= "Type";
		String takeBack= "Back";

		driver.get(driver.getStoreFrontURL()+"/"+driver.getCountry());
		while(true){
			randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_CONSULTANT_WITH_ORDERS_AND_AUTOSHIPS_RFO,countryId),RFO_DB);
			consultantEmailID = (String) getValueFromQueryResult(randomConsultantList, "UserName");
			accountID=String.valueOf(getValueFromQueryResult(randomConsultantList, "AccountID"));
			emailIdFromAccountIdList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_EMAIL_ID_FROM_ACCOUNT_ID,accountID),RFO_DB);
			consultantEmailID=(String) getValueFromQueryResult(emailIdFromAccountIdList, "EmailAddress");
			try{
				storeFrontConsultantPage = storeFrontHomePage.loginAsConsultant(consultantEmailID, password);
			}catch(Exception e){
				driver.get(driver.getStoreFrontURL()+"/"+driver.getCountry());
				storeFrontConsultantPage = storeFrontHomePage.loginAsConsultant(consultantEmailID, password);
			}
			boolean isLoginError = driver.getCurrentUrl().contains("error");
			if(isLoginError){
				logger.info("Login error for the user "+consultantEmailID);
				driver.get(driver.getStoreFrontURL()+"/"+driver.getCountry());
			}
			else
				break;
		}
		logout();
		//get emailId of username
		emailIdFromAccountIdList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_EMAIL_ID_FROM_ACCOUNT_ID,accountID),RFO_DB);
		consultantEmailID = String.valueOf(getValueFromQueryResult(emailIdFromAccountIdList, "EmailAddress"));  
		logger.info("emaild of username "+consultantEmailID); 
		logger.info("login is successful");
		driver.get(driver.getCSCockpitURL());
		cscockpitCustomerSearchTabPage = cscockpitLoginPage.clickLoginBtn();
		cscockpitCustomerSearchTabPage.selectCustomerTypeFromDropDownInCustomerSearchTab("CONSULTANT");
		cscockpitCustomerSearchTabPage.selectCountryFromDropDownInCustomerSearchTab(country);
		cscockpitCustomerSearchTabPage.selectAccountStatusFromDropDownInCustomerSearchTab("Active");
		cscockpitCustomerSearchTabPage.enterEmailIdInSearchFieldInCustomerSearchTab(consultantEmailID);
		cscockpitCustomerSearchTabPage.clickSearchBtn();
		randomCustomerSequenceNumber = String.valueOf(cscockpitCustomerSearchTabPage.getRandomCustomerFromSearchResult());
		cscockpitCustomerSearchTabPage.clickCIDNumberInCustomerSearchTab(randomCustomerSequenceNumber);
		cscockpitCustomerTabPage.clickFirstOrderInCustomerTab();
		cscockpitCustomerTabPage.clickPlaceOrderButtonInCustomerTab();
		cscockpitCartTabPage.selectValueFromSortByDDInCartTab("Price: High to Low");
		cscockpitCartTabPage.selectCatalogFromDropDownInCartTab();
		randomProductSequenceNumber = String.valueOf(cscockpitCartTabPage.getRandomProductWithSKUFromSearchResult()); 
		SKUValue = cscockpitCartTabPage.getCustomerSKUValueInCartTab(randomProductSequenceNumber);
		cscockpitCartTabPage.searchSKUValueInCartTab(SKUValue);
		cscockpitCartTabPage.clickAddToCartBtnInCartTab();
		cscockpitCartTabPage.clickCheckoutBtnInCartTab();
		cscockpitCheckoutTabPage.clickAddNewPaymentAddressInCheckoutTab();
		cscockpitCheckoutTabPage.enterBillingInfo();
		cscockpitCheckoutTabPage.clickSaveAddNewPaymentProfilePopUP();
		cscockpitCheckoutTabPage.enterCVVValueInCheckoutTab(TestConstants.SECURITY_CODE);
		cscockpitCheckoutTabPage.clickUseThisCardBtnInCheckoutTab();
		cscockpitCheckoutTabPage.clickPerformSooButton();
		cscockpitCheckoutTabPage.clickSOODepartmentDD();
			cscockpitCheckoutTabPage.clickSOOPopupHeader();
		cscockpitCheckoutTabPage.clickSOOTypeDD();
		cscockpitCheckoutTabPage.clickSOOPopupHeader();
		cscockpitCheckoutTabPage.clickSOOReasonDD();
		cscockpitCheckoutTabPage.clickSOOPopupHeader();
		cscockpitCheckoutTabPage.enterPriceValueInSalesOrderOverridePopUp(priceValue);
		cscockpitCheckoutTabPage.enterCVValueInSalesOrderOverrridePoPuP(cvValue);
		cscockpitCheckoutTabPage.enterQVValueInSalesOrderOvverridePopUp(qvValue);
		cscockpitCheckoutTabPage.enterDeliveryCostsInSalesOrderOvverridePopUp(deliveryCost);
		cscockpitCheckoutTabPage.enterHandlingCostsInSalesOrderOvveridePOpUp(handlingCost);
		cscockpitCheckoutTabPage.selectOverrideReasonSooDept();
		cscockpitCheckoutTabPage.selectOverrideReasonSooType();
		cscockpitCheckoutTabPage.selectOverrideReasonSooReason();
		cscockpitCheckoutTabPage.clickUpdateButtonSalesOverridePopUp();
		priceValue = TestConstants.PRICE_VALUE;
		cvValue = TestConstants.CVVALUE;
		qvValue = TestConstants.QVVALUE;
		deliveryCost = TestConstants.DELIVERY_COST;
		handlingCost = TestConstants.HANDLING_COST;
		cscockpitCheckoutTabPage.clickPerformSooButton();
		cscockpitCheckoutTabPage.enterPriceValueInSalesOrderOverridePopUp(priceValue);
		cscockpitCheckoutTabPage.enterCVValueInSalesOrderOverrridePoPuP(cvValue);
		cscockpitCheckoutTabPage.enterQVValueInSalesOrderOvverridePopUp(qvValue);
		cscockpitCheckoutTabPage.enterDeliveryCostsInSalesOrderOvverridePopUp(deliveryCost);
		cscockpitCheckoutTabPage.enterHandlingCostsInSalesOrderOvveridePOpUp(handlingCost);
		cscockpitCheckoutTabPage.selectOverrideReasonSooDept();
		cscockpitCheckoutTabPage.selectOverrideReasonSooType();
		cscockpitCheckoutTabPage.selectOverrideReasonSooReason();
		cscockpitCheckoutTabPage.clickUpdateButtonSalesOverridePopUp();
		cscockpitCheckoutTabPage.clickPlaceOrderButtonInCheckoutTab();
		s_assert.assertTrue(cscockpitOrderTabPage.getOrderStatusAfterPlaceOrderInOrderTab().contains("SUBMITTED"),"order is not submitted successfully");
		String orderNumber = cscockpitOrderTabPage.getOrderNumberFromCsCockpitUIOnOrderTab();
		driver.get(driver.getStoreFrontURL()+"/"+driver.getCountry());
		storeFrontConsultantPage = storeFrontHomePage.loginAsConsultant(consultantEmailID, password);
		storeFrontConsultantPage.clickOnWelcomeDropDown();
		storeFrontOrdersPage = storeFrontConsultantPage.clickOrdersLinkPresentOnWelcomeDropDown();
		s_assert.assertTrue(orderNumber.contains(storeFrontOrdersPage.getFirstOrderNumberFromOrderHistory()),"This Order is not present on the StoreFront of US for consultant");
		storeFrontOrdersPage.clickOnFirstAdHocOrder();
		//Assert Subtotal with RFO
		s_assert.assertTrue(storeFrontOrdersPage.getSubTotalFromAutoshipTemplate().contains(priceValue),"Adhoc Order template subtotal on RFO is "+priceValue+" and on UI is "+storeFrontOrdersPage.getSubTotalFromAutoshipTemplate());
		// assert shipping amount with RFO
		s_assert.assertTrue(storeFrontOrdersPage.getShippingAmountFromAutoshipTemplate().contains(deliveryCost),"Adhoc Order template shipping amount on RFO is "+deliveryCost+" and on UI is "+storeFrontOrdersPage.getShippingAmountFromAutoshipTemplate());
		// assert Handling Value with RFO
		//s_assert.assertTrue(storeFrontOrdersPage.getHandlingAmountFromAutoshipTemplate().contains(handlingCost),"Adhoc Order template handling amount on RFO is "+handlingCost+" and on UI is "+storeFrontOrdersPage.getHandlingAmountFromAutoshipTemplate());
		logout();
		logout();
		s_assert.assertAll();
	}


	@Test (enabled=false)
	public void testVerifyTheSalesOverrideFunctionality_1954_allthreetypes() throws InterruptedException{
		RFO_DB = driver.getDBNameRFO();
		String randomCustomerSequenceNumber = null;
		String randomProductSequenceNumber = null;
		String consultantEmailID = null;
		String SKUValue = null;
		String priceValue = "0";
		String cvValue = "0";
		String qvValue = "0";
		String deliveryCost = "0";
		String handlingCost = "0";
		List<Map<String, Object>> randomConsultantList =  null;
		List<Map<String, Object>> emailIdFromAccountIdList =  null;
		List<Map<String, Object>> randomPCList =  null;
		List<Map<String, Object>> randomRCList =  null;
		String accountID = null;
		String country = null;
		if(driver.getCountry().equalsIgnoreCase("ca")){
			country= TestConstants.COUNTRY_DD_VALUE_CA;
		}else{
			country= TestConstants.COUNTRY_DD_VALUE_US;
		}
		String deliveryCosts = "Delivery Costs";
		String handlingCosts = "Handling Costs";
		String totalCV = "Total CV";
		String totalQV = "Total QV";
		String pcEmailID = null;
		String rcEmailID = null;
		String performSOOHeader = "Perform SOO";
		String salesOrderOverride = "SALES ORDER OVERRIDE";
		String overrideReasons = "Override Reasons";
		String associateToOrder = "Associate to Order# (optional)";
		String subtotal = "Subtotal";
		String discount = "Discount";
		String taxes = "Taxes";
		String totalPrice = "Total Price";
		String sku = "SKU";
		String product = "Product";
		String qty = "Qty";
		String price = "Price";
		String adjdPrice = "Adjd Price";
		String CV = "CV";
		String QV = "QV";
		String sales = "Sales";
		String prodDev = "Prod";
		String ops = "Ops";
		String marketing = "Marketing";
		String it = "IT";
		String gA = "G&A";
		String coG = "CoG";
		String appeasement = "Appeasement";
		String testing = "Testing";
		String consignment = "Consignment";
		String donation = "Donation";
		String employeePerks = "Employee";
		String exchange = "Exchange";
		String gifts = "Gifts";
		String incentives = "Incentives";
		String leadership = "Leadership";
		String meetings = "Meetings";
		String publicRelations = "Relations";
		String qAQC = "QA/QC";
		String reship = "Reship";
		String samples = "Samples";
		String upgrade = "Upgrade";
		String sale = "Sale";
		String damaged = "Damaged";
		String lostinTransit = "Transit";
		String missing= "Missing";
		String QCIssue= "Issue";
		String reactiontoProduct= "Reaction";
		String wrongItemSent= "Wrong";
		String notRelatedtoType= "Type";
		String takeBack= "Back";

		driver.get(driver.getStoreFrontURL()+"/"+driver.getCountry());
		while(true){
			randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_CONSULTANT_WITH_ORDERS_AND_AUTOSHIPS_RFO,countryId),RFO_DB);
			consultantEmailID = (String) getValueFromQueryResult(randomConsultantList, "UserName");
			accountID=String.valueOf(getValueFromQueryResult(randomConsultantList, "AccountID"));
			emailIdFromAccountIdList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_EMAIL_ID_FROM_ACCOUNT_ID,accountID),RFO_DB);
			consultantEmailID=(String) getValueFromQueryResult(emailIdFromAccountIdList, "EmailAddress");
			try{
				storeFrontConsultantPage = storeFrontHomePage.loginAsConsultant(consultantEmailID, password);
			}catch(Exception e){
				driver.get(driver.getStoreFrontURL()+"/"+driver.getCountry());
				storeFrontConsultantPage = storeFrontHomePage.loginAsConsultant(consultantEmailID, password);
			}
			boolean isLoginError = driver.getCurrentUrl().contains("error");
			if(isLoginError){
				logger.info("Login error for the user "+consultantEmailID);
				driver.get(driver.getStoreFrontURL()+"/"+driver.getCountry());
			}
			else
				break;
		}
		logout();
		//get emailId of username
		emailIdFromAccountIdList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_EMAIL_ID_FROM_ACCOUNT_ID,accountID),RFO_DB);
		consultantEmailID = String.valueOf(getValueFromQueryResult(emailIdFromAccountIdList, "EmailAddress"));  
		logger.info("emaild of username "+consultantEmailID); 
		logger.info("login is successful");
		driver.get(driver.getCSCockpitURL());
		cscockpitCustomerSearchTabPage = cscockpitLoginPage.clickLoginBtn();
		cscockpitCustomerSearchTabPage.selectCustomerTypeFromDropDownInCustomerSearchTab("CONSULTANT");
		cscockpitCustomerSearchTabPage.selectCountryFromDropDownInCustomerSearchTab(country);
		cscockpitCustomerSearchTabPage.selectAccountStatusFromDropDownInCustomerSearchTab("Active");
		cscockpitCustomerSearchTabPage.enterEmailIdInSearchFieldInCustomerSearchTab(consultantEmailID);
		cscockpitCustomerSearchTabPage.clickSearchBtn();
		randomCustomerSequenceNumber = String.valueOf(cscockpitCustomerSearchTabPage.getRandomCustomerFromSearchResult());
		cscockpitCustomerSearchTabPage.clickCIDNumberInCustomerSearchTab(randomCustomerSequenceNumber);
		cscockpitCustomerTabPage.clickFirstOrderInCustomerTab();
		cscockpitCustomerTabPage.clickPlaceOrderButtonInCustomerTab();
		cscockpitCartTabPage.selectValueFromSortByDDInCartTab("Price: High to Low");
		cscockpitCartTabPage.selectCatalogFromDropDownInCartTab();
		randomProductSequenceNumber = String.valueOf(cscockpitCartTabPage.getRandomProductWithSKUFromSearchResult()); 
		SKUValue = cscockpitCartTabPage.getCustomerSKUValueInCartTab(randomProductSequenceNumber);
		cscockpitCartTabPage.searchSKUValueInCartTab(SKUValue);
		cscockpitCartTabPage.clickAddToCartBtnInCartTab();
		cscockpitCartTabPage.clickCheckoutBtnInCartTab();
		cscockpitCheckoutTabPage.clickAddNewPaymentAddressInCheckoutTab();
		cscockpitCheckoutTabPage.enterBillingInfo();
		cscockpitCheckoutTabPage.clickSaveAddNewPaymentProfilePopUP();
		cscockpitCheckoutTabPage.enterCVVValueInCheckoutTab(TestConstants.SECURITY_CODE);
		cscockpitCheckoutTabPage.clickUseThisCardBtnInCheckoutTab();
		cscockpitCheckoutTabPage.clickPerformSooButton();
		//assert SOO popup with all values
		s_assert.assertTrue(cscockpitCheckoutTabPage.verifyPerformSOOPopupValues(performSOOHeader), "Perform SOO header is not present on SOO popup");
		s_assert.assertTrue(cscockpitCheckoutTabPage.verifyPerformSOOPopupValues(salesOrderOverride), "sales Order Override is not present on SOO popup");
		s_assert.assertTrue(cscockpitCheckoutTabPage.verifyPerformSOOPopupValues(overrideReasons), "override Reasons is not present on SOO popup");
		s_assert.assertTrue(cscockpitCheckoutTabPage.verifyPerformSOOPopupValues(associateToOrder), "associate to Order is not present on SOO popup");
		s_assert.assertTrue(cscockpitCheckoutTabPage.verifyPerformSOOPopupValues(subtotal), "Subtotal is not present on SOO popup");
		s_assert.assertTrue(cscockpitCheckoutTabPage.verifyPerformSOOPopupValues(discount), "Discount is not present on SOO popup");
		s_assert.assertTrue(cscockpitCheckoutTabPage.verifyPerformSOOPopupValues(taxes), "taxes is not present on SOO popup");
		s_assert.assertTrue(cscockpitCheckoutTabPage.verifyPerformSOOPopupValues(totalPrice), "Total price is not present on SOO popup");
		s_assert.assertTrue(cscockpitCheckoutTabPage.verifySOOPopupValues(sku), "SKU value is not present on SOO popup for consultant");
		s_assert.assertTrue(cscockpitCheckoutTabPage.verifySOOPopupValues(product), "Product value is not present on SOO popup for consultant");
		s_assert.assertTrue(cscockpitCheckoutTabPage.verifySOOPopupValues(qty), "Qty value is not present on SOO popup for consultant");
		s_assert.assertTrue(cscockpitCheckoutTabPage.verifySOOPopupValues(price), "Price value is not present on SOO popup for consultant");
		s_assert.assertTrue(cscockpitCheckoutTabPage.verifySOOPopupValues(adjdPrice), "Adjd price value is not present on SOO popup for consultant");
		s_assert.assertTrue(cscockpitCheckoutTabPage.verifySOOPopupValues(CV), "CV value is not present on SOO popup for consultant");
		s_assert.assertTrue(cscockpitCheckoutTabPage.verifySOOPopupValues(QV), "QV value is not present on SOO popup for consultant");
		s_assert.assertTrue(cscockpitCheckoutTabPage.verifyCancelThisSOOLink(), "Cancel this SOO link is not present on SOO popup for consultant");
		cscockpitCheckoutTabPage.clickSOODepartmentDD();
		s_assert.assertTrue(cscockpitCheckoutTabPage.verifySOODDValues(sales), "sales value is not present on SOO Department DD for consultant");
		s_assert.assertTrue(cscockpitCheckoutTabPage.verifySOODDValues(prodDev), "Prod Dev value is not present on SOO Department DD for consultant");
		s_assert.assertTrue(cscockpitCheckoutTabPage.verifySOODDValues(ops), "Ops value is not present on SOO Department DD for consultant");
		s_assert.assertTrue(cscockpitCheckoutTabPage.verifySOODDValues(marketing), "Marketing value is not present on SOO Department DD for consultant");
		s_assert.assertTrue(cscockpitCheckoutTabPage.verifySOODDValues(it), "IT value is not present on SOO Department DD for consultant");
		s_assert.assertTrue(cscockpitCheckoutTabPage.verifySOODDValues(gA), "G&A value is not present on SOO Department DD for consultant");
		s_assert.assertTrue(cscockpitCheckoutTabPage.verifySOODDValues(coG), "CoG value is not present on SOO Department DD for consultant");
		cscockpitCheckoutTabPage.clickSOOPopupHeader();
		cscockpitCheckoutTabPage.clickSOOTypeDD();
		s_assert.assertTrue(cscockpitCheckoutTabPage.verifySOODDValues(appeasement), "Appeasement value is not present on SOO Type DD for consultant");
		s_assert.assertTrue(cscockpitCheckoutTabPage.verifySOODDValues(consignment), "Consignment value is not present on SOO Type DD for consultant");
		s_assert.assertTrue(cscockpitCheckoutTabPage.verifySOODDValues(donation), "Donation value is not present on SOO Type DD for consultant");
		s_assert.assertTrue(cscockpitCheckoutTabPage.verifySOODDValues(employeePerks), "Employee perks value is not present on SOO Type DD for consultant");
		s_assert.assertTrue(cscockpitCheckoutTabPage.verifySOODDValues(exchange), "Exchange value is not present on SOO Type DD for consultant");
		s_assert.assertTrue(cscockpitCheckoutTabPage.verifySOODDValues(gifts), "Gifts value is not present on SOO Type DD for consultant");
		s_assert.assertTrue(cscockpitCheckoutTabPage.verifySOODDValues(incentives), "Incentives value is not present on SOO Type DD for consultant");
		s_assert.assertTrue(cscockpitCheckoutTabPage.verifySOODDValues(leadership), "Leadership value is not present on SOO Type DD for consultant");
		s_assert.assertTrue(cscockpitCheckoutTabPage.verifySOODDValues(meetings), "Meetings value is not present on SOO Type DD for consultant");
		s_assert.assertTrue(cscockpitCheckoutTabPage.verifySOODDValues(publicRelations), "Public relations value is not present on SOO Type DD for consultant");
		s_assert.assertTrue(cscockpitCheckoutTabPage.verifySOODDValues(qAQC), "QA/QC value is not present on SOO Type DD for consultant");
		s_assert.assertTrue(cscockpitCheckoutTabPage.verifySOODDValues(reship), "Reship value is not present on SOO Type DD for consultant");
		s_assert.assertTrue(cscockpitCheckoutTabPage.verifySOODDValues(samples), "Samples value is not present on SOO Type DD for consultant");
		s_assert.assertTrue(cscockpitCheckoutTabPage.verifySOODDValues(upgrade), "Upgrade value is not present on SOO Type DD for consultant");
		s_assert.assertTrue(cscockpitCheckoutTabPage.verifySOODDValues(sale), "Sale value is not present on SOO Type DD for consultant");
		s_assert.assertTrue(cscockpitCheckoutTabPage.verifySOODDValues(testing), "Testing value is not present on SOO Type DD for consultant");
		cscockpitCheckoutTabPage.clickSOOPopupHeader();
		cscockpitCheckoutTabPage.clickSOOReasonDD();
		s_assert.assertTrue(cscockpitCheckoutTabPage.verifySOODDValues(damaged), "Damged value is not present on SOO Reason DD for consultant");
		s_assert.assertTrue(cscockpitCheckoutTabPage.verifySOODDValues(lostinTransit), "Lost in transit value is not present on SOO Reason DD for consultant");
		s_assert.assertTrue(cscockpitCheckoutTabPage.verifySOODDValues(missing), "Missing value is not present on SOO Reason DD for consultant");
		s_assert.assertTrue(cscockpitCheckoutTabPage.verifySOODDValues(QCIssue), "QC Issue value is not present on SOO Reason DD for consultant");
		s_assert.assertTrue(cscockpitCheckoutTabPage.verifySOODDValues(reactiontoProduct), "Reaction to Product value is not present on SOO Reason DD for consultant");
		s_assert.assertTrue(cscockpitCheckoutTabPage.verifySOODDValues(wrongItemSent), "Wrong Item Sent value is not present on SOO Reason DD for consultant");
		s_assert.assertTrue(cscockpitCheckoutTabPage.verifySOODDValues(notRelatedtoType), "Not Related to Type value is not present on SOO Reason DD for consultant");
		s_assert.assertTrue(cscockpitCheckoutTabPage.verifySOODDValues(takeBack), "Take Back value is not present on SOO Reason DD for consultant");
		cscockpitCheckoutTabPage.clickSOOPopupHeader();
		cscockpitCheckoutTabPage.enterPriceValueInSalesOrderOverridePopUp(priceValue);
		cscockpitCheckoutTabPage.enterCVValueInSalesOrderOverrridePoPuP(cvValue);
		cscockpitCheckoutTabPage.enterQVValueInSalesOrderOvverridePopUp(qvValue);
		cscockpitCheckoutTabPage.enterDeliveryCostsInSalesOrderOvverridePopUp(deliveryCost);
		cscockpitCheckoutTabPage.enterHandlingCostsInSalesOrderOvveridePOpUp(handlingCost);
		cscockpitCheckoutTabPage.selectOverrideReasonSooDept();
		cscockpitCheckoutTabPage.selectOverrideReasonSooType();
		cscockpitCheckoutTabPage.selectOverrideReasonSooReason();
		cscockpitCheckoutTabPage.clickUpdateButtonSalesOverridePopUp();
		s_assert.assertTrue(cscockpitCheckoutTabPage.getValuesFromTotalsSection(deliveryCosts).contains(deliveryCost), "Expected delivery cost for consultant is "+deliveryCost+" Actual on UI is "+cscockpitCheckoutTabPage.getValuesFromTotalsSection(deliveryCosts));
		s_assert.assertTrue(cscockpitCheckoutTabPage.getValuesFromTotalsSection(handlingCosts).contains(handlingCost), "Expected handling cost for consultant is "+handlingCost+" Actual on UI is "+cscockpitCheckoutTabPage.getValuesFromTotalsSection(handlingCosts));
		s_assert.assertTrue(cscockpitCheckoutTabPage.getValuesFromTotalsSection(totalCV).contains(cvValue), "Expected CV value for consultant is "+cvValue+" Actual on UI is "+cscockpitCheckoutTabPage.getValuesFromTotalsSection(totalCV));
		s_assert.assertTrue(cscockpitCheckoutTabPage.getValuesFromTotalsSection(totalQV).contains(qvValue), "Expected QV value for consultant is "+qvValue+" Actual on UI is "+cscockpitCheckoutTabPage.getValuesFromTotalsSection(totalQV));
		priceValue = TestConstants.PRICE_VALUE;
		cvValue = TestConstants.CVVALUE;
		qvValue = TestConstants.QVVALUE;
		deliveryCost = TestConstants.DELIVERY_COST;
		handlingCost = TestConstants.HANDLING_COST;
		cscockpitCheckoutTabPage.clickPerformSooButton();
		cscockpitCheckoutTabPage.enterPriceValueInSalesOrderOverridePopUp(priceValue);
		cscockpitCheckoutTabPage.enterCVValueInSalesOrderOverrridePoPuP(cvValue);
		cscockpitCheckoutTabPage.enterQVValueInSalesOrderOvverridePopUp(qvValue);
		cscockpitCheckoutTabPage.enterDeliveryCostsInSalesOrderOvverridePopUp(deliveryCost);
		cscockpitCheckoutTabPage.enterHandlingCostsInSalesOrderOvveridePOpUp(handlingCost);
		cscockpitCheckoutTabPage.selectOverrideReasonSooDept();
		cscockpitCheckoutTabPage.selectOverrideReasonSooType();
		cscockpitCheckoutTabPage.selectOverrideReasonSooReason();
		cscockpitCheckoutTabPage.clickUpdateButtonSalesOverridePopUp();
		s_assert.assertTrue(cscockpitCheckoutTabPage.getValuesFromTotalsSection(deliveryCosts).contains(deliveryCost), "Expected delivery cost for consultant is "+deliveryCost+" Actual on UI is "+cscockpitCheckoutTabPage.getValuesFromTotalsSection(deliveryCosts));
		s_assert.assertTrue(cscockpitCheckoutTabPage.getValuesFromTotalsSection(handlingCosts).contains(handlingCost), "Expected handling cost for consultant is "+handlingCost+" Actual on UI is "+cscockpitCheckoutTabPage.getValuesFromTotalsSection(handlingCosts));
		s_assert.assertTrue(cscockpitCheckoutTabPage.getValuesFromTotalsSection(totalCV).contains(cvValue), "Expected CV value for consultant is "+cvValue+" Actual on UI is "+cscockpitCheckoutTabPage.getValuesFromTotalsSection(totalCV));
		s_assert.assertTrue(cscockpitCheckoutTabPage.getValuesFromTotalsSection(totalQV).contains(qvValue), "Expected QV value for consultant is "+qvValue+" Actual on UI is "+cscockpitCheckoutTabPage.getValuesFromTotalsSection(totalQV));
		cscockpitCheckoutTabPage.clickPlaceOrderButtonInCheckoutTab();
		s_assert.assertTrue(cscockpitOrderTabPage.getOrderStatusAfterPlaceOrderInOrderTab().contains("SUBMITTED"),"order is not submitted successfully");
		String orderNumber = cscockpitOrderTabPage.getOrderNumberFromCsCockpitUIOnOrderTab();
		driver.get(driver.getStoreFrontURL()+"/"+driver.getCountry());
		storeFrontConsultantPage = storeFrontHomePage.loginAsConsultant(consultantEmailID, password);
		storeFrontConsultantPage.clickOnWelcomeDropDown();
		storeFrontOrdersPage = storeFrontConsultantPage.clickOrdersLinkPresentOnWelcomeDropDown();
		s_assert.assertTrue(orderNumber.contains(storeFrontOrdersPage.getFirstOrderNumberFromOrderHistory()),"This Order is not present on the StoreFront of US for consultant");
		storeFrontOrdersPage.clickOnFirstAdHocOrder();
		//Assert Subtotal with RFO
		s_assert.assertTrue(storeFrontOrdersPage.getSubTotalFromAutoshipTemplate().contains(priceValue),"Adhoc Order template subtotal on RFO is "+priceValue+" and on UI is "+storeFrontOrdersPage.getSubTotalFromAutoshipTemplate());
		// assert shipping amount with RFO
		s_assert.assertTrue(storeFrontOrdersPage.getShippingAmountFromAutoshipTemplate().contains(deliveryCost),"Adhoc Order template shipping amount on RFO is "+deliveryCost+" and on UI is "+storeFrontOrdersPage.getShippingAmountFromAutoshipTemplate());
		// assert Handling Value with RFO
		//s_assert.assertTrue(storeFrontOrdersPage.getHandlingAmountFromAutoshipTemplate().contains(handlingCost),"Adhoc Order template handling amount on RFO is "+handlingCost+" and on UI is "+storeFrontOrdersPage.getHandlingAmountFromAutoshipTemplate());
		logout();

		//For PC
		driver.get(driver.getStoreFrontURL()+"/"+driver.getCountry());
		while(true){
			randomPCList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_PC_WITH_ORDERS_AND_AUTOSHIPS_RFO,countryId),RFO_DB);
			pcEmailID  = (String) getValueFromQueryResult(randomPCList, "UserName");  
			accountID = String.valueOf(getValueFromQueryResult(randomPCList, "AccountID")); 
			logger.info("Account Id of user "+accountID);
			try{
				storeFrontPCUserPage = storeFrontHomePage.loginAsPCUser(pcEmailID, password);
			}catch(Exception e){
				driver.get(driver.getStoreFrontURL()+"/"+driver.getCountry());
				storeFrontPCUserPage = storeFrontHomePage.loginAsPCUser(pcEmailID, password);
			}
			boolean isLoginError = driver.getCurrentUrl().contains("error");
			if(isLoginError){
				logger.info("Login error for the user "+pcEmailID);
				driver.get(driver.getStoreFrontURL()+"/"+driver.getCountry());
			}
			else
				break;
		}
		logger.info("login is successful"); 
		logout();
		//get emailId of username
		emailIdFromAccountIdList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_EMAIL_ID_FROM_ACCOUNT_ID,accountID),RFO_DB);
		pcEmailID = String.valueOf(getValueFromQueryResult(emailIdFromAccountIdList, "EmailAddress"));  
		logger.info("emaild of username "+pcEmailID); 
		logger.info("login is successful");
		driver.get(driver.getCSCockpitURL());
		cscockpitCustomerSearchTabPage = cscockpitLoginPage.clickLoginBtn();
		cscockpitCustomerSearchTabPage.selectCustomerTypeFromDropDownInCustomerSearchTab("PC");
		cscockpitCustomerSearchTabPage.selectCountryFromDropDownInCustomerSearchTab(country);
		cscockpitCustomerSearchTabPage.selectAccountStatusFromDropDownInCustomerSearchTab("Active");
		cscockpitCustomerSearchTabPage.enterEmailIdInSearchFieldInCustomerSearchTab(pcEmailID);
		cscockpitCustomerSearchTabPage.clickSearchBtn();
		randomCustomerSequenceNumber = String.valueOf(cscockpitCustomerSearchTabPage.getRandomCustomerFromSearchResult());
		cscockpitCustomerSearchTabPage.clickCIDNumberInCustomerSearchTab(randomCustomerSequenceNumber);
		cscockpitCustomerTabPage.clickFirstOrderInCustomerTab();
		cscockpitCustomerTabPage.clickPlaceOrderButtonInCustomerTab();
		cscockpitCartTabPage.selectValueFromSortByDDInCartTab("Price: High to Low");
		cscockpitCartTabPage.selectCatalogFromDropDownInCartTab();
		randomProductSequenceNumber = String.valueOf(cscockpitCartTabPage.getRandomProductWithSKUFromSearchResult()); 
		SKUValue = cscockpitCartTabPage.getCustomerSKUValueInCartTab(randomProductSequenceNumber);
		cscockpitCartTabPage.searchSKUValueInCartTab(SKUValue);
		cscockpitCartTabPage.clickAddToCartBtnInCartTab();
		cscockpitCartTabPage.clickCheckoutBtnInCartTab();
		cscockpitCheckoutTabPage.clickAddNewPaymentAddressInCheckoutTab();
		cscockpitCheckoutTabPage.enterBillingInfo();
		cscockpitCheckoutTabPage.clickSaveAddNewPaymentProfilePopUP();
		cscockpitCheckoutTabPage.enterCVVValueInCheckoutTab(TestConstants.SECURITY_CODE);
		cscockpitCheckoutTabPage.clickUseThisCardBtnInCheckoutTab();
		cscockpitCheckoutTabPage.clickPerformSooButton();
		//assert SOO popup with all values
		s_assert.assertTrue(cscockpitCheckoutTabPage.verifyPerformSOOPopupValues(performSOOHeader), "Perform SOO header is not present on SOO popup");
		s_assert.assertTrue(cscockpitCheckoutTabPage.verifyPerformSOOPopupValues(salesOrderOverride), "sales Order Override is not present on SOO popup");
		s_assert.assertTrue(cscockpitCheckoutTabPage.verifyPerformSOOPopupValues(overrideReasons), "override Reasons is not present on SOO popup");
		s_assert.assertTrue(cscockpitCheckoutTabPage.verifyPerformSOOPopupValues(associateToOrder), "associate to Order is not present on SOO popup");
		s_assert.assertTrue(cscockpitCheckoutTabPage.verifyPerformSOOPopupValues(subtotal), "Subtotal is not present on SOO popup");
		s_assert.assertTrue(cscockpitCheckoutTabPage.verifyPerformSOOPopupValues(discount), "Discount is not present on SOO popup");
		s_assert.assertTrue(cscockpitCheckoutTabPage.verifyPerformSOOPopupValues(taxes), "taxes is not present on SOO popup");
		s_assert.assertTrue(cscockpitCheckoutTabPage.verifyPerformSOOPopupValues(totalPrice), "Total price is not present on SOO popup");
		s_assert.assertTrue(cscockpitCheckoutTabPage.verifySOOPopupValues(sku), "SKU value is not present on SOO popup for PC");
		s_assert.assertTrue(cscockpitCheckoutTabPage.verifySOOPopupValues(product), "Product value is not present on SOO popup for PC");
		s_assert.assertTrue(cscockpitCheckoutTabPage.verifySOOPopupValues(qty), "Qty value is not present on SOO popup for PC");
		s_assert.assertTrue(cscockpitCheckoutTabPage.verifySOOPopupValues(price), "Price value is not present on SOO popup for PC");
		s_assert.assertTrue(cscockpitCheckoutTabPage.verifySOOPopupValues(adjdPrice), "Adjd price value is not present on SOO popup for PC");
		s_assert.assertTrue(cscockpitCheckoutTabPage.verifySOOPopupValues(CV), "CV value is not present on SOO popup for PC");
		s_assert.assertTrue(cscockpitCheckoutTabPage.verifySOOPopupValues(QV), "QV value is not present on SOO popup for PC");
		s_assert.assertTrue(cscockpitCheckoutTabPage.verifyCancelThisSOOLink(), "Cancel this SOO link is not present on SOO popup for PC");
		cscockpitCheckoutTabPage.clickSOODepartmentDD();
		s_assert.assertTrue(cscockpitCheckoutTabPage.verifySOODDValues(sales), "sales value is not present on SOO Department DD for PC");
		s_assert.assertTrue(cscockpitCheckoutTabPage.verifySOODDValues(prodDev), "Prod Dev value is not present on SOO Department DD for PC");
		s_assert.assertTrue(cscockpitCheckoutTabPage.verifySOODDValues(ops), "Ops value is not present on SOO Department DD for PC");
		s_assert.assertTrue(cscockpitCheckoutTabPage.verifySOODDValues(marketing), "Marketing value is not present on SOO Department DD for PC");
		s_assert.assertTrue(cscockpitCheckoutTabPage.verifySOODDValues(it), "IT value is not present on SOO Department DD for PC");
		s_assert.assertTrue(cscockpitCheckoutTabPage.verifySOODDValues(gA), "G&A value is not present on SOO Department DD for PC");
		s_assert.assertTrue(cscockpitCheckoutTabPage.verifySOODDValues(coG), "CoG value is not present on SOO Department DD for PC");
		cscockpitCheckoutTabPage.clickSOOPopupHeader();
		cscockpitCheckoutTabPage.clickSOOTypeDD();
		s_assert.assertTrue(cscockpitCheckoutTabPage.verifySOODDValues(appeasement), "Appeasement value is not present on SOO Type DD for PC");
		s_assert.assertTrue(cscockpitCheckoutTabPage.verifySOODDValues(consignment), "Consignment value is not present on SOO Type DD for PC");
		s_assert.assertTrue(cscockpitCheckoutTabPage.verifySOODDValues(donation), "Donation value is not present on SOO Type DD for PC");
		s_assert.assertTrue(cscockpitCheckoutTabPage.verifySOODDValues(employeePerks), "Employee perks value is not present on SOO Type DD for PC");
		s_assert.assertTrue(cscockpitCheckoutTabPage.verifySOODDValues(exchange), "Exchange value is not present on SOO Type DD for PC");
		s_assert.assertTrue(cscockpitCheckoutTabPage.verifySOODDValues(gifts), "Gifts value is not present on SOO Type DD for PC");
		s_assert.assertTrue(cscockpitCheckoutTabPage.verifySOODDValues(incentives), "Incentives value is not present on SOO Type DD for PC");
		s_assert.assertTrue(cscockpitCheckoutTabPage.verifySOODDValues(leadership), "Leadership value is not present on SOO Type DD for PC");
		s_assert.assertTrue(cscockpitCheckoutTabPage.verifySOODDValues(meetings), "Meetings value is not present on SOO Type DD for PC");
		s_assert.assertTrue(cscockpitCheckoutTabPage.verifySOODDValues(publicRelations), "Public relations value is not present on SOO Type DD for PC");
		s_assert.assertTrue(cscockpitCheckoutTabPage.verifySOODDValues(qAQC), "QA/QC value is not present on SOO Type DD for PC");
		s_assert.assertTrue(cscockpitCheckoutTabPage.verifySOODDValues(reship), "Reship value is not present on SOO Type DD for PC");
		s_assert.assertTrue(cscockpitCheckoutTabPage.verifySOODDValues(samples), "Samples value is not present on SOO Type DD for PC");
		s_assert.assertTrue(cscockpitCheckoutTabPage.verifySOODDValues(upgrade), "Upgrade value is not present on SOO Type DD for PC");
		s_assert.assertTrue(cscockpitCheckoutTabPage.verifySOODDValues(sale), "Sale value is not present on SOO Type DD for PC");
		s_assert.assertTrue(cscockpitCheckoutTabPage.verifySOODDValues(testing), "Testing value is not present on SOO Type DD for PC");
		cscockpitCheckoutTabPage.clickSOOPopupHeader();
		cscockpitCheckoutTabPage.clickSOOReasonDD();
		s_assert.assertTrue(cscockpitCheckoutTabPage.verifySOODDValues(damaged), "Damged value is not present on SOO Reason DD for PC");
		s_assert.assertTrue(cscockpitCheckoutTabPage.verifySOODDValues(lostinTransit), "Lost in transit value is not present on SOO Reason DD for PC");
		s_assert.assertTrue(cscockpitCheckoutTabPage.verifySOODDValues(missing), "Missing value is not present on SOO Reason DD for PC");
		s_assert.assertTrue(cscockpitCheckoutTabPage.verifySOODDValues(QCIssue), "QC Issue value is not present on SOO Reason DD for PC");
		s_assert.assertTrue(cscockpitCheckoutTabPage.verifySOODDValues(reactiontoProduct), "Reaction to Product value is not present on SOO Reason DD for PC");
		s_assert.assertTrue(cscockpitCheckoutTabPage.verifySOODDValues(wrongItemSent), "Wrong Item Sent value is not present on SOO Reason DD for PC");
		s_assert.assertTrue(cscockpitCheckoutTabPage.verifySOODDValues(notRelatedtoType), "Not Related to Type value is not present on SOO Reason DD for PC");
		s_assert.assertTrue(cscockpitCheckoutTabPage.verifySOODDValues(takeBack), "Take Back value is not present on SOO Reason DD for PC");
		cscockpitCheckoutTabPage.clickSOOPopupHeader();
		cscockpitCheckoutTabPage.enterPriceValueInSalesOrderOverridePopUp("0");
		cscockpitCheckoutTabPage.enterCVValueInSalesOrderOverrridePoPuP("0");
		cscockpitCheckoutTabPage.enterQVValueInSalesOrderOvverridePopUp("0");
		cscockpitCheckoutTabPage.enterDeliveryCostsInSalesOrderOvverridePopUp("0");
		cscockpitCheckoutTabPage.enterHandlingCostsInSalesOrderOvveridePOpUp(handlingCost);
		cscockpitCheckoutTabPage.selectOverrideReasonSooDept();
		cscockpitCheckoutTabPage.selectOverrideReasonSooType();
		cscockpitCheckoutTabPage.selectOverrideReasonSooReason();
		cscockpitCheckoutTabPage.clickUpdateButtonSalesOverridePopUp();
		s_assert.assertTrue(cscockpitCheckoutTabPage.getValuesFromTotalsSection(deliveryCosts).contains("0"), "Expected delivery cost for PC is "+"0"+" Actual on UI is "+cscockpitCheckoutTabPage.getValuesFromTotalsSection(deliveryCosts));
		s_assert.assertTrue(cscockpitCheckoutTabPage.getValuesFromTotalsSection(handlingCosts).contains("0"), "Expected handling cost for PC is "+"0"+" Actual on UI is "+cscockpitCheckoutTabPage.getValuesFromTotalsSection(handlingCosts));
		s_assert.assertTrue(cscockpitCheckoutTabPage.getValuesFromTotalsSection(totalCV).contains("0"), "Expected CV value for PC is "+"0"+" Actual on UI is "+cscockpitCheckoutTabPage.getValuesFromTotalsSection(totalCV));
		s_assert.assertTrue(cscockpitCheckoutTabPage.getValuesFromTotalsSection(totalQV).contains("0"), "Expected QV value for PC is "+"0"+" Actual on UI is "+cscockpitCheckoutTabPage.getValuesFromTotalsSection(totalQV));
		cscockpitCheckoutTabPage.clickPerformSooButton();
		cscockpitCheckoutTabPage.enterPriceValueInSalesOrderOverridePopUp(priceValue);
		cscockpitCheckoutTabPage.enterCVValueInSalesOrderOverrridePoPuP(cvValue);
		cscockpitCheckoutTabPage.enterQVValueInSalesOrderOvverridePopUp(qvValue);
		cscockpitCheckoutTabPage.enterDeliveryCostsInSalesOrderOvverridePopUp(deliveryCost);
		cscockpitCheckoutTabPage.enterHandlingCostsInSalesOrderOvveridePOpUp(handlingCost);
		cscockpitCheckoutTabPage.selectOverrideReasonSooDept();
		cscockpitCheckoutTabPage.selectOverrideReasonSooType();
		cscockpitCheckoutTabPage.selectOverrideReasonSooReason();
		cscockpitCheckoutTabPage.clickUpdateButtonSalesOverridePopUp();
		s_assert.assertTrue(cscockpitCheckoutTabPage.getValuesFromTotalsSection(deliveryCosts).contains(deliveryCost), "Expected delivery cost for PC is "+deliveryCost+" Actual on UI is "+cscockpitCheckoutTabPage.getValuesFromTotalsSection(deliveryCosts));
		s_assert.assertTrue(cscockpitCheckoutTabPage.getValuesFromTotalsSection(handlingCosts).contains(handlingCost), "Expected handling cost for PC is "+handlingCost+" Actual on UI is "+cscockpitCheckoutTabPage.getValuesFromTotalsSection(handlingCosts));
		s_assert.assertTrue(cscockpitCheckoutTabPage.getValuesFromTotalsSection(totalCV).contains(cvValue), "Expected CV value for PC is "+cvValue+" Actual on UI is "+cscockpitCheckoutTabPage.getValuesFromTotalsSection(totalCV));
		s_assert.assertTrue(cscockpitCheckoutTabPage.getValuesFromTotalsSection(totalQV).contains(qvValue), "Expected QV value for PC is "+qvValue+" Actual on UI is "+cscockpitCheckoutTabPage.getValuesFromTotalsSection(totalQV));
		cscockpitCheckoutTabPage.clickPlaceOrderButtonInCheckoutTab();
		s_assert.assertTrue(cscockpitOrderTabPage.getOrderStatusAfterPlaceOrderInOrderTab().contains("SUBMITTED"),"order is not submitted successfully");
		orderNumber = cscockpitOrderTabPage.getOrderNumberFromCsCockpitUIOnOrderTab();
		driver.get(driver.getStoreFrontURL()+"/"+driver.getCountry());
		storeFrontPCUserPage = storeFrontHomePage.loginAsPCUser(pcEmailID, password);
		storeFrontPCUserPage.clickOnWelcomeDropDown();
		storeFrontOrdersPage = storeFrontPCUserPage.clickOrdersLinkPresentOnWelcomeDropDown();
		s_assert.assertTrue(orderNumber.contains(storeFrontOrdersPage.getFirstOrderNumberFromOrderHistory()),"This Order is not present on the StoreFront of US for PC");
		storeFrontOrdersPage.clickOnFirstAdHocOrder();
		//Assert Subtotal with RFO
		s_assert.assertTrue(storeFrontOrdersPage.getSubTotalFromAutoshipTemplate().contains(priceValue),"Adhoc Order template subtotal on RFO is "+priceValue+" and on UI is "+storeFrontOrdersPage.getSubTotalFromAutoshipTemplate());
		// assert shipping amount with RFO
		s_assert.assertTrue(storeFrontOrdersPage.getShippingAmountFromAutoshipTemplate().contains(deliveryCost),"Adhoc Order template shipping amount on RFO is "+deliveryCost+" and on UI is "+storeFrontOrdersPage.getShippingAmountFromAutoshipTemplate());
		// assert Handling Value with RFO
		//s_assert.assertTrue(storeFrontOrdersPage.getHandlingAmountFromAutoshipTemplate().contains(handlingCost),"Adhoc Order template handling amount on RFO is "+handlingCost+" and on UI is "+storeFrontOrdersPage.getHandlingAmountFromAutoshipTemplate());
		logout();

		//For RC
		driver.get(driver.getStoreFrontURL()+"/"+driver.getCountry());
		while(true){
			randomRCList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_RC_RFO,countryId),RFO_DB);
			rcEmailID  = (String) getValueFromQueryResult(randomRCList, "UserName");  
			accountID = String.valueOf(getValueFromQueryResult(randomRCList, "AccountID")); 
			logger.info("Account Id of user "+accountID);
			try{
				storeFrontRCUserPage = storeFrontHomePage.loginAsRCUser(pcEmailID, password);
			}catch(Exception e){
				driver.get(driver.getStoreFrontURL()+"/"+driver.getCountry());
				storeFrontRCUserPage = storeFrontHomePage.loginAsRCUser(pcEmailID, password);
			}
			boolean isLoginError = driver.getCurrentUrl().contains("error");
			if(isLoginError){
				logger.info("Login error for the user "+rcEmailID);
				driver.get(driver.getStoreFrontURL()+"/"+driver.getCountry());
			}
			else
				break;
		}
		logger.info("login is successful"); 
		logout();
		//get emailId of username
		emailIdFromAccountIdList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_EMAIL_ID_FROM_ACCOUNT_ID,accountID),RFO_DB);
		rcEmailID = String.valueOf(getValueFromQueryResult(emailIdFromAccountIdList, "EmailAddress"));  
		logger.info("emaild of username "+rcEmailID); 
		logger.info("login is successful");
		driver.get(driver.getCSCockpitURL());
		cscockpitCustomerSearchTabPage = cscockpitLoginPage.clickLoginBtn();
		cscockpitCustomerSearchTabPage.selectCustomerTypeFromDropDownInCustomerSearchTab("RETAIL");
		cscockpitCustomerSearchTabPage.selectCountryFromDropDownInCustomerSearchTab(country);
		cscockpitCustomerSearchTabPage.selectAccountStatusFromDropDownInCustomerSearchTab("Active");
		cscockpitCustomerSearchTabPage.enterEmailIdInSearchFieldInCustomerSearchTab(rcEmailID);
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
		cscockpitCheckoutTabPage.clickAddNewPaymentAddressInCheckoutTab();
		cscockpitCheckoutTabPage.enterBillingInfo();
		cscockpitCheckoutTabPage.clickSaveAddNewPaymentProfilePopUP();
		cscockpitCheckoutTabPage.enterCVVValueInCheckoutTab(TestConstants.SECURITY_CODE);
		cscockpitCheckoutTabPage.clickUseThisCardBtnInCheckoutTab();
		cscockpitCheckoutTabPage.clickPerformSooButton();
		//assert SOO popup with all values
		s_assert.assertTrue(cscockpitCheckoutTabPage.verifyPerformSOOPopupValues(performSOOHeader), "Perform SOO header is not present on SOO popup");
		s_assert.assertTrue(cscockpitCheckoutTabPage.verifyPerformSOOPopupValues(salesOrderOverride), "sales Order Override is not present on SOO popup");
		s_assert.assertTrue(cscockpitCheckoutTabPage.verifyPerformSOOPopupValues(overrideReasons), "override Reasons is not present on SOO popup");
		s_assert.assertTrue(cscockpitCheckoutTabPage.verifyPerformSOOPopupValues(associateToOrder), "associate to Order is not present on SOO popup");
		s_assert.assertTrue(cscockpitCheckoutTabPage.verifyPerformSOOPopupValues(subtotal), "Subtotal is not present on SOO popup");
		s_assert.assertTrue(cscockpitCheckoutTabPage.verifyPerformSOOPopupValues(discount), "Discount is not present on SOO popup");
		s_assert.assertTrue(cscockpitCheckoutTabPage.verifyPerformSOOPopupValues(taxes), "taxes is not present on SOO popup");
		s_assert.assertTrue(cscockpitCheckoutTabPage.verifyPerformSOOPopupValues(totalPrice), "Total price is not present on SOO popup");
		s_assert.assertTrue(cscockpitCheckoutTabPage.verifySOOPopupValues(sku), "SKU value is not present on SOO popup for consultant");
		s_assert.assertTrue(cscockpitCheckoutTabPage.verifySOOPopupValues(product), "Product value is not present on SOO popup for consultant");
		s_assert.assertTrue(cscockpitCheckoutTabPage.verifySOOPopupValues(qty), "Qty value is not present on SOO popup for consultant");
		s_assert.assertTrue(cscockpitCheckoutTabPage.verifySOOPopupValues(price), "Price value is not present on SOO popup for consultant");
		s_assert.assertTrue(cscockpitCheckoutTabPage.verifySOOPopupValues(adjdPrice), "Adjd price value is not present on SOO popup for consultant");
		s_assert.assertTrue(cscockpitCheckoutTabPage.verifySOOPopupValues(CV), "CV value is not present on SOO popup for consultant");
		s_assert.assertTrue(cscockpitCheckoutTabPage.verifySOOPopupValues(QV), "QV value is not present on SOO popup for consultant");
		s_assert.assertTrue(cscockpitCheckoutTabPage.verifyCancelThisSOOLink(), "Cancel this SOO link is not present on SOO popup for consultant");
		cscockpitCheckoutTabPage.clickSOODepartmentDD();
		s_assert.assertTrue(cscockpitCheckoutTabPage.verifySOODDValues(sales), "sales value is not present on SOO Department DD for RC");
		s_assert.assertTrue(cscockpitCheckoutTabPage.verifySOODDValues(prodDev), "Prod Dev value is not present on SOO Department DD for RC");
		s_assert.assertTrue(cscockpitCheckoutTabPage.verifySOODDValues(ops), "Ops value is not present on SOO Department DD for RC");
		s_assert.assertTrue(cscockpitCheckoutTabPage.verifySOODDValues(marketing), "Marketing value is not present on SOO Department DD for RC");
		s_assert.assertTrue(cscockpitCheckoutTabPage.verifySOODDValues(it), "IT value is not present on SOO Department DD for RC");
		s_assert.assertTrue(cscockpitCheckoutTabPage.verifySOODDValues(gA), "G&A value is not present on SOO Department DD for RC");
		s_assert.assertTrue(cscockpitCheckoutTabPage.verifySOODDValues(coG), "CoG value is not present on SOO Department DD for RC");
		cscockpitCheckoutTabPage.clickSOOPopupHeader();
		cscockpitCheckoutTabPage.clickSOOTypeDD();
		s_assert.assertTrue(cscockpitCheckoutTabPage.verifySOODDValues(appeasement), "Appeasement value is not present on SOO Type DD for RC");
		s_assert.assertTrue(cscockpitCheckoutTabPage.verifySOODDValues(consignment), "Consignment value is not present on SOO Type DD for RC");
		s_assert.assertTrue(cscockpitCheckoutTabPage.verifySOODDValues(donation), "Donation value is not present on SOO Type DD for RC");
		s_assert.assertTrue(cscockpitCheckoutTabPage.verifySOODDValues(employeePerks), "Employee perks value is not present on SOO Type DD for RC");
		s_assert.assertTrue(cscockpitCheckoutTabPage.verifySOODDValues(exchange), "Exchange value is not present on SOO Type DD for RC");
		s_assert.assertTrue(cscockpitCheckoutTabPage.verifySOODDValues(gifts), "Gifts value is not present on SOO Type DD for RC");
		s_assert.assertTrue(cscockpitCheckoutTabPage.verifySOODDValues(incentives), "Incentives value is not present on SOO Type DD for RC");
		s_assert.assertTrue(cscockpitCheckoutTabPage.verifySOODDValues(leadership), "Leadership value is not present on SOO Type DD for RC");
		s_assert.assertTrue(cscockpitCheckoutTabPage.verifySOODDValues(meetings), "Meetings value is not present on SOO Type DD for RC");
		s_assert.assertTrue(cscockpitCheckoutTabPage.verifySOODDValues(publicRelations), "Public relations value is not present on SOO Type DD for RC");
		s_assert.assertTrue(cscockpitCheckoutTabPage.verifySOODDValues(qAQC), "QA/QC value is not present on SOO Type DD for RC");
		s_assert.assertTrue(cscockpitCheckoutTabPage.verifySOODDValues(reship), "Reship value is not present on SOO Type DD for RC");
		s_assert.assertTrue(cscockpitCheckoutTabPage.verifySOODDValues(samples), "Samples value is not present on SOO Type DD for RC");
		s_assert.assertTrue(cscockpitCheckoutTabPage.verifySOODDValues(upgrade), "Upgrade value is not present on SOO Type DD for RC");
		s_assert.assertTrue(cscockpitCheckoutTabPage.verifySOODDValues(sale), "Sale value is not present on SOO Type DD for RC");
		s_assert.assertTrue(cscockpitCheckoutTabPage.verifySOODDValues(testing), "Testing value is not present on SOO Type DD for RC");
		cscockpitCheckoutTabPage.clickSOOPopupHeader();
		cscockpitCheckoutTabPage.clickSOOReasonDD();
		s_assert.assertTrue(cscockpitCheckoutTabPage.verifySOODDValues(damaged), "Damged value is not present on SOO Reason DD for RC");
		s_assert.assertTrue(cscockpitCheckoutTabPage.verifySOODDValues(lostinTransit), "Lost in transit value is not present on SOO Reason DD for RC");
		s_assert.assertTrue(cscockpitCheckoutTabPage.verifySOODDValues(missing), "Missing value is not present on SOO Reason DD for RC");
		s_assert.assertTrue(cscockpitCheckoutTabPage.verifySOODDValues(QCIssue), "QC Issue value is not present on SOO Reason DD for RC");
		s_assert.assertTrue(cscockpitCheckoutTabPage.verifySOODDValues(reactiontoProduct), "Reaction to Product value is not present on SOO Reason DD for RC");
		s_assert.assertTrue(cscockpitCheckoutTabPage.verifySOODDValues(wrongItemSent), "Wrong Item Sent value is not present on SOO Reason DD for RC");
		s_assert.assertTrue(cscockpitCheckoutTabPage.verifySOODDValues(notRelatedtoType), "Not Related to Type value is not present on SOO Reason DD for RC");
		s_assert.assertTrue(cscockpitCheckoutTabPage.verifySOODDValues(takeBack), "Take Back value is not present on SOO Reason DD for RC");
		cscockpitCheckoutTabPage.clickSOOPopupHeader();
		cscockpitCheckoutTabPage.enterPriceValueInSalesOrderOverridePopUp("0");
		cscockpitCheckoutTabPage.enterCVValueInSalesOrderOverrridePoPuP("0");
		cscockpitCheckoutTabPage.enterQVValueInSalesOrderOvverridePopUp("0");
		cscockpitCheckoutTabPage.enterDeliveryCostsInSalesOrderOvverridePopUp("0");
		cscockpitCheckoutTabPage.enterHandlingCostsInSalesOrderOvveridePOpUp(handlingCost);
		cscockpitCheckoutTabPage.selectOverrideReasonSooDept();
		cscockpitCheckoutTabPage.selectOverrideReasonSooType();
		cscockpitCheckoutTabPage.selectOverrideReasonSooReason();
		cscockpitCheckoutTabPage.clickUpdateButtonSalesOverridePopUp();
		s_assert.assertTrue(cscockpitCheckoutTabPage.getValuesFromTotalsSection(deliveryCosts).contains("0"), "Expected delivery cost for RC is "+"0"+" Actual on UI is "+cscockpitCheckoutTabPage.getValuesFromTotalsSection(deliveryCosts));
		s_assert.assertTrue(cscockpitCheckoutTabPage.getValuesFromTotalsSection(handlingCosts).contains("0"), "Expected handling cost for RC is "+"0"+" Actual on UI is "+cscockpitCheckoutTabPage.getValuesFromTotalsSection(handlingCosts));
		s_assert.assertTrue(cscockpitCheckoutTabPage.getValuesFromTotalsSection(totalCV).contains("0"), "Expected CV value for RC is "+"0"+" Actual on UI is "+cscockpitCheckoutTabPage.getValuesFromTotalsSection(totalCV));
		s_assert.assertTrue(cscockpitCheckoutTabPage.getValuesFromTotalsSection(totalQV).contains("0"), "Expected QV value for RC is "+"0"+" Actual on UI is "+cscockpitCheckoutTabPage.getValuesFromTotalsSection(totalQV));
		cscockpitCheckoutTabPage.clickPerformSooButton();
		cscockpitCheckoutTabPage.enterPriceValueInSalesOrderOverridePopUp(priceValue);
		cscockpitCheckoutTabPage.enterCVValueInSalesOrderOverrridePoPuP(cvValue);
		cscockpitCheckoutTabPage.enterQVValueInSalesOrderOvverridePopUp(qvValue);
		cscockpitCheckoutTabPage.enterDeliveryCostsInSalesOrderOvverridePopUp(deliveryCost);
		cscockpitCheckoutTabPage.enterHandlingCostsInSalesOrderOvveridePOpUp(handlingCost);
		cscockpitCheckoutTabPage.selectOverrideReasonSooDept();
		cscockpitCheckoutTabPage.selectOverrideReasonSooType();
		cscockpitCheckoutTabPage.selectOverrideReasonSooReason();
		cscockpitCheckoutTabPage.clickUpdateButtonSalesOverridePopUp();
		s_assert.assertTrue(cscockpitCheckoutTabPage.getValuesFromTotalsSection(deliveryCosts).contains(deliveryCost), "Expected delivery cost for RC is "+deliveryCost+" Actual on UI is "+cscockpitCheckoutTabPage.getValuesFromTotalsSection(deliveryCosts));
		s_assert.assertTrue(cscockpitCheckoutTabPage.getValuesFromTotalsSection(handlingCosts).contains(handlingCost), "Expected handling cost for RC is "+handlingCost+" Actual on UI is "+cscockpitCheckoutTabPage.getValuesFromTotalsSection(handlingCosts));
		s_assert.assertTrue(cscockpitCheckoutTabPage.getValuesFromTotalsSection(totalCV).contains(cvValue), "Expected CV value for RC is "+cvValue+" Actual on UI is "+cscockpitCheckoutTabPage.getValuesFromTotalsSection(totalCV));
		s_assert.assertTrue(cscockpitCheckoutTabPage.getValuesFromTotalsSection(totalQV).contains(qvValue), "Expected QV value for RC is "+qvValue+" Actual on UI is "+cscockpitCheckoutTabPage.getValuesFromTotalsSection(totalQV));
		cscockpitCheckoutTabPage.clickPlaceOrderButtonInCheckoutTab();
		s_assert.assertTrue(cscockpitOrderTabPage.getOrderStatusAfterPlaceOrderInOrderTab().contains("SUBMITTED"),"order is not submitted successfully");
		orderNumber = cscockpitOrderTabPage.getOrderNumberFromCsCockpitUIOnOrderTab();
		driver.get(driver.getStoreFrontURL()+"/"+driver.getCountry());
		storeFrontRCUserPage = storeFrontHomePage.loginAsRCUser(rcEmailID, password);
		storeFrontRCUserPage.clickOnWelcomeDropDown();
		storeFrontOrdersPage = storeFrontRCUserPage.clickOrdersLinkPresentOnWelcomeDropDown();
		s_assert.assertTrue(orderNumber.contains(storeFrontOrdersPage.getFirstOrderNumberFromOrderHistory()),"This Order is not present on the StoreFront of US for RC");
		storeFrontOrdersPage.clickOnFirstAdHocOrder();
		//Assert Subtotal with RFO
		s_assert.assertTrue(storeFrontOrdersPage.getSubTotalFromAutoshipTemplate().contains(priceValue),"Adhoc Order template subtotal on RFO is "+priceValue+" and on UI is "+storeFrontOrdersPage.getSubTotalFromAutoshipTemplate());
		// assert shipping amount with RFO
		s_assert.assertTrue(storeFrontOrdersPage.getShippingAmountFromAutoshipTemplate().contains(deliveryCost),"Adhoc Order template shipping amount on RFO is "+deliveryCost+" and on UI is "+storeFrontOrdersPage.getShippingAmountFromAutoshipTemplate());
		// assert Handling Value with RFO
		//s_assert.assertTrue(storeFrontOrdersPage.getHandlingAmountFromAutoshipTemplate().contains(handlingCost),"Adhoc Order template handling amount on RFO is "+handlingCost+" and on UI is "+storeFrontOrdersPage.getHandlingAmountFromAutoshipTemplate());
		logout();
		s_assert.assertAll();
	}


}

