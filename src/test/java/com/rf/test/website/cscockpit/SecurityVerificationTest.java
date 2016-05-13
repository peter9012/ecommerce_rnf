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


public class SecurityVerificationTest extends RFWebsiteBaseTest{
	private static final Logger logger = LogManager
			.getLogger(SecurityVerificationTest.class.getName());

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

	public SecurityVerificationTest() {
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

	//Hybris Project-1810:To verify that cscommissionadmin can place order
	@Test
	public void testVerifyCSCommissionAdminCanPlaceOrder_1810() throws InterruptedException{
		String randomCustomerSequenceNumber = null;
		String randomProductSequenceNumber = null;
		String SKUValue = null;
		int randomNum = CommonUtils.getRandomNum(10000, 1000000);
		int randomNumber = CommonUtils.getRandomNum(10000, 1000000);
		String billingProfile = TestConstants.NEW_BILLING_PROFILE_NAME+randomNum;
		String country = null;
		if(driver.getCountry().equalsIgnoreCase("ca")){
			country= TestConstants.COUNTRY_DD_VALUE_CA;
		}else{
			country= TestConstants.COUNTRY_DD_VALUE_US;
		}
		cscockpitLoginPage.enterUsername(TestConstants.CS_COMMISION_ADMIN_USERNAME);
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
		cscockpitCheckoutTabPage.clickAddNewPaymentAddressInCheckoutTab();
		cscockpitCheckoutTabPage.enterBillingInfo(TestConstants.CARD_NUMBER, billingProfile, TestConstants.SECURITY_CODE);
		cscockpitCheckoutTabPage.clickSaveAddNewPaymentProfilePopUP();
		cscockpitCheckoutTabPage.enterCVVValueInCheckoutTab(TestConstants.SECURITY_CODE);
		cscockpitCheckoutTabPage.clickUseThisCardBtnInCheckoutTab();
		cscockpitCheckoutTabPage.clickPlaceOrderButtonInCheckoutTab();
		s_assert.assertTrue(cscockpitOrderTabPage.getCountOfOrdersOnOrdersDetailsPageAfterPlacingOrder()>0, "Order was NOT placed successfully,expected count after placing order in order detail items section >0 but actual count on UI = "+cscockpitOrderTabPage.getCountOfOrdersOnOrdersDetailsPageAfterPlacingOrder());
		s_assert.assertAll();
	}

	//Hybris Project-1820:To verify that cssalessupervisory can place order
	@Test
	public void testVerifyCSSalesSuperVisoryCanPlaceOrder_1820() throws InterruptedException{
		String randomCustomerSequenceNumber = null;
		String randomProductSequenceNumber = null;
		String SKUValue = null;
		String country = null;
		if(driver.getCountry().equalsIgnoreCase("ca")){
			country= TestConstants.COUNTRY_DD_VALUE_CA;
		}else{
			country= TestConstants.COUNTRY_DD_VALUE_US;
		}
		cscockpitLoginPage.enterUsername(TestConstants.CS_SALES_SUPERVISORY_USERNAME);
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
		cscockpitCheckoutTabPage.clickAddNewPaymentAddressInCheckoutTab();
		cscockpitCheckoutTabPage.enterBillingInfo();
		cscockpitCheckoutTabPage.clickSaveAddNewPaymentProfilePopUP();
		cscockpitCheckoutTabPage.enterCVVValueInCheckoutTab(TestConstants.SECURITY_CODE);
		cscockpitCheckoutTabPage.clickUseThisCardBtnInCheckoutTab();
		cscockpitCheckoutTabPage.clickPlaceOrderButtonInCheckoutTab();
		s_assert.assertTrue(cscockpitOrderTabPage.getCountOfOrdersOnOrdersDetailsPageAfterPlacingOrder()>0, "Order was NOT placed successfully,expected count after placing order in order detail items section >0 but actual count on UI = "+cscockpitOrderTabPage.getCountOfOrdersOnOrdersDetailsPageAfterPlacingOrder());
		s_assert.assertAll();
	}

	//Hybris Project-1945:To verify csr place order through Cart Page Find customer
	@Test 
	public void testVerifyCSRPlaceOrderThroughCartPageFindCustomer_1945() throws InterruptedException{
		RFO_DB = driver.getDBNameRFO();
		List<Map<String, Object>> randomConsultantList =  null;
		List<Map<String, Object>> randomConsultantUsernameList =  null;
		String consultantEmailID = null;
		String randomProductSequenceNumber = null;
		String randomCustomerSequenceNumber = null;
		String SKUValue = null;
		String country = null;
		String accountID = null;
		if(driver.getCountry().equalsIgnoreCase("ca")){
			country= TestConstants.COUNTRY_DD_VALUE_CA;
		}else{
			country= TestConstants.COUNTRY_DD_VALUE_US;
		}
		driver.get(driver.getStoreFrontURL()+"/"+driver.getCountry());

		while(true){
			randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_CONSULTANT_WITH_ORDERS_AND_AUTOSHIPS_RFO,countryId),RFO_DB);
			accountID = String.valueOf(getValueFromQueryResult(randomConsultantList, "AccountID"));
			randomConsultantUsernameList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_EMAIL_ID_FROM_ACCOUNT_ID,accountID),RFO_DB);
			consultantEmailID = String.valueOf(getValueFromQueryResult(randomConsultantUsernameList, "EmailAddress"));
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
		cscockpitCheckoutTabPage.clickAddNewPaymentAddressInCheckoutTab();
		cscockpitCheckoutTabPage.enterBillingInfo();
		cscockpitCheckoutTabPage.clickSaveAddNewPaymentProfilePopUP();
		cscockpitCheckoutTabPage.enterCVVValueInCheckoutTab(TestConstants.SECURITY_CODE);
		cscockpitCheckoutTabPage.clickUseThisCardBtnInCheckoutTab();
		cscockpitCheckoutTabPage.clickPlaceOrderButtonInCheckoutTab();
		s_assert.assertTrue(cscockpitOrderTabPage.getOrderStatusAfterPlaceOrderInOrderTab().contains("SUBMITTED"),"order is not submitted successfully");
		String getOrderNumberFromCsCockpitUIOnOrderTabOnCA = cscockpitOrderTabPage.getOrderNumberFromCsCockpitUIOnOrderTab();
		driver.get(driver.getStoreFrontURL()+"/"+driver.getCountry());
		try{
			storeFrontConsultantPage = storeFrontHomePage.loginAsConsultant(consultantEmailID, password);
		}catch(Exception e){
			driver.get(driver.getStoreFrontURL()+"/"+driver.getCountry());
			storeFrontConsultantPage = storeFrontHomePage.loginAsConsultant(consultantEmailID, password);
		}
		storeFrontConsultantPage.clickOnWelcomeDropDown();
		storeFrontOrdersPage = storeFrontConsultantPage.clickOrdersLinkPresentOnWelcomeDropDown();

		s_assert.assertTrue(getOrderNumberFromCsCockpitUIOnOrderTabOnCA.contains(storeFrontOrdersPage.getFirstOrderNumberFromOrderHistory()),"This Order is not present on the StoreFront of CA");
		logout();
		s_assert.assertAll();
	}

	//Hybris Project-1777:To verify User permission for customer search
	@Test
	public void testVerifyUserPermissionForCustomerSearch_1777(){
		cscockpitLoginPage.enterUsername(TestConstants.CS_AGENT_USERNAME);
		cscockpitCustomerSearchTabPage = cscockpitLoginPage.clickLoginBtn();
		cscockpitCustomerSearchTabPage.selectCustomerTypeFromDropDownInCustomerSearchTab("CONSULTANT");
		cscockpitCustomerSearchTabPage.selectAccountStatusFromDropDownInCustomerSearchTab("Active");
		cscockpitCustomerSearchTabPage.clickSearchBtn();
		s_assert.assertTrue(cscockpitCustomerSearchTabPage.getTotalResultsInCustomerSearchOnCustomerSearchTab()>0, "Customer search result has not displayed any result for user = "+TestConstants.CS_AGENT_USERNAME);
		driver.get(driver.getCSCockpitURL());

		//		cscockpitLoginPage.enterUsername(TestConstants.ADMIN_USERNAME);
		//		cscockpitCustomerSearchTabPage = cscockpitLoginPage.clickLoginBtn();
		//		cscockpitCustomerSearchTabPage.selectCustomerTypeFromDropDownInCustomerSearchTab("CONSULTANT");
		//		cscockpitCustomerSearchTabPage.selectAccountStatusFromDropDownInCustomerSearchTab("Active");
		//		cscockpitCustomerSearchTabPage.clickSearchBtn();
		//		s_assert.assertTrue(cscockpitCustomerSearchTabPage.getTotalResultsInCustomerSearchOnCustomerSearchTab()>0, "Customer search result has not displayed any result for user = "+TestConstants.ADMIN_USERNAME);
		driver.get(driver.getCSCockpitURL());
		//		
		cscockpitLoginPage.enterUsername(TestConstants.CS_COMMISION_ADMIN_USERNAME);
		cscockpitCustomerSearchTabPage = cscockpitLoginPage.clickLoginBtn();
		cscockpitCustomerSearchTabPage.selectCustomerTypeFromDropDownInCustomerSearchTab("CONSULTANT");
		cscockpitCustomerSearchTabPage.selectAccountStatusFromDropDownInCustomerSearchTab("Active");
		cscockpitCustomerSearchTabPage.clickSearchBtn();
		s_assert.assertTrue(cscockpitCustomerSearchTabPage.getTotalResultsInCustomerSearchOnCustomerSearchTab()>0, "Customer search result has not displayed any result for user = "+TestConstants.CS_COMMISION_ADMIN_USERNAME);
		driver.get(driver.getCSCockpitURL());

		cscockpitLoginPage.enterUsername(TestConstants.CS_SALES_SUPERVISORY_USERNAME);
		cscockpitCustomerSearchTabPage = cscockpitLoginPage.clickLoginBtn();
		cscockpitCustomerSearchTabPage.selectCustomerTypeFromDropDownInCustomerSearchTab("CONSULTANT");
		cscockpitCustomerSearchTabPage.selectAccountStatusFromDropDownInCustomerSearchTab("Active");
		cscockpitCustomerSearchTabPage.clickSearchBtn();
		s_assert.assertTrue(cscockpitCustomerSearchTabPage.getTotalResultsInCustomerSearchOnCustomerSearchTab()>0, "Customer search result has not displayed any result for user = "+TestConstants.CS_SALES_SUPERVISORY_USERNAME);

		s_assert.assertAll();
	}

	//Hybris Project-1778:To verify User permission for order search
	@Test
	public void testVerifyUserPermissionForOrderSearch_1778(){
		cscockpitCustomerSearchTabPage = cscockpitLoginPage.clickLoginBtn();
		cscockpitCustomerSearchTabPage.clickOrderSearchTab();
		cscockpitOrderSearchTabPage.clickSearchBtn();
		s_assert.assertTrue(cscockpitOrderSearchTabPage.getTotalOrderSearchResultsInOrderSearchTab()>0, "Order search result has not displayed for user ="+TestConstants.CS_AGENT_USERNAME);
		driver.get(driver.getCSCockpitURL());

		//		cscockpitCustomerSearchTabPage = new CSCockpitCustomerSearchTabPage(driver);
		//		cscockpitLoginPage.enterUsername(TestConstants.ADMIN_USERNAME);
		//		cscockpitCustomerSearchTabPage = cscockpitLoginPage.clickLoginBtn();
		//		cscockpitCustomerSearchTabPage.clickOrderSearchTab();
		//		cscockpitOrderSearchTabPage.clickSearchBtn();
		//		s_assert.assertTrue(cscockpitOrderSearchTabPage.getTotalOrderSearchResultsInOrderSearchTab()>0, "Order search result has not displayed for user ="+TestConstants.ADMIN_USERNAME);
		//		driver.get(driver.getCSCockpitURL());

		cscockpitLoginPage.enterUsername(TestConstants.CS_COMMISION_ADMIN_USERNAME);
		cscockpitCustomerSearchTabPage = cscockpitLoginPage.clickLoginBtn();
		cscockpitCustomerSearchTabPage.clickOrderSearchTab();
		cscockpitOrderSearchTabPage.clickSearchBtn();
		s_assert.assertTrue(cscockpitOrderSearchTabPage.getTotalOrderSearchResultsInOrderSearchTab()>0, "Order search result has not displayed for user ="+TestConstants.CS_COMMISION_ADMIN_USERNAME);
		driver.get(driver.getCSCockpitURL());

		cscockpitLoginPage.enterUsername(TestConstants.CS_SALES_SUPERVISORY_USERNAME);
		cscockpitCustomerSearchTabPage = cscockpitLoginPage.clickLoginBtn();
		cscockpitCustomerSearchTabPage.clickOrderSearchTab();
		cscockpitOrderSearchTabPage.clickSearchBtn();
		s_assert.assertTrue(cscockpitOrderSearchTabPage.getTotalOrderSearchResultsInOrderSearchTab()>0, "Order search result has not displayed for user ="+TestConstants.CS_SALES_SUPERVISORY_USERNAME);
		s_assert.assertAll();
	}
}
