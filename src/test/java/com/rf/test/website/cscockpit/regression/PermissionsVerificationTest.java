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
import com.rf.pages.website.cscockpit.CSCockpitAutoshipTemplateTabPage;
import com.rf.pages.website.cscockpit.CSCockpitAutoshipTemplateUpdateTabPage;
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


public class PermissionsVerificationTest extends RFWebsiteBaseTest{
	private static final Logger logger = LogManager
			.getLogger(PermissionsVerificationTest.class.getName());

	//-------------------------------------------------Pages---------------------------------------------------------
	private CSCockpitLoginPage cscockpitLoginPage;	
	private CSCockpitCheckoutTabPage cscockpitCheckoutTabPage;
	private CSCockpitCustomerSearchTabPage cscockpitCustomerSearchTabPage;
	private CSCockpitCustomerTabPage cscockpitCustomerTabPage;
	private CSCockpitOrderSearchTabPage cscockpitOrderSearchTabPage;
	private CSCockpitOrderTabPage cscockpitOrderTabPage;
	private CSCockpitCartTabPage cscockpitCartTabPage;
	private CSCockpitAutoshipTemplateTabPage cscockpitAutoshipTemplateTabPage;
	private CSCockpitAutoshipTemplateUpdateTabPage cscockpitAutoshipTemplateUpdateTabPage;
	private CSCockpitAutoshipCartTabPage cscockpitAutoshipCartTabPage;
	private StoreFrontHomePage storeFrontHomePage; 
	private StoreFrontConsultantPage storeFrontConsultantPage;
	private StoreFrontOrdersPage storeFrontOrdersPage;
	private StoreFrontPCUserPage storeFrontPCUserPage;
	private StoreFrontRCUserPage storeFrontRCUserPage;	
	private StoreFrontUpdateCartPage storeFrontUpdateCartPage;


	//-----------------------------------------------------------------------------------------------------------------

	public PermissionsVerificationTest() {
		cscockpitLoginPage = new CSCockpitLoginPage(driver);
		cscockpitCheckoutTabPage = new CSCockpitCheckoutTabPage(driver);
		cscockpitCustomerSearchTabPage = new CSCockpitCustomerSearchTabPage(driver);
		cscockpitCustomerTabPage = new CSCockpitCustomerTabPage(driver);
		cscockpitOrderSearchTabPage = new CSCockpitOrderSearchTabPage(driver);
		cscockpitOrderTabPage = new CSCockpitOrderTabPage(driver);
		cscockpitCartTabPage = new CSCockpitCartTabPage(driver);
		cscockpitAutoshipTemplateTabPage = new CSCockpitAutoshipTemplateTabPage(driver);
		cscockpitAutoshipTemplateUpdateTabPage = new CSCockpitAutoshipTemplateUpdateTabPage(driver);
		cscockpitAutoshipCartTabPage = new CSCockpitAutoshipCartTabPage(driver);
		storeFrontHomePage = new StoreFrontHomePage(driver);
		storeFrontConsultantPage = new StoreFrontConsultantPage(driver);
		storeFrontOrdersPage = new StoreFrontOrdersPage(driver);
		storeFrontPCUserPage = new StoreFrontPCUserPage(driver);
		storeFrontRCUserPage = new StoreFrontRCUserPage(driver);
		storeFrontUpdateCartPage = new StoreFrontUpdateCartPage(driver);

	}
	private String RFO_DB = null;

	//Hybris Project-1805:To verify that CSR can Edit Autoship for consultant and PC
	@Test
	public void testVerifyCSRCanEditAutoshipForConsultantAndPC_1805(){ 
		String randomCustomerSequenceNumber = null;
		RFO_DB = driver.getDBNameRFO();
		int randomNum = CommonUtils.getRandomNum(10000, 1000000);
		List<Map<String, Object>> randomConsultantList =  null;
		List<Map<String, Object>> randomConsultantUsernameList =  null;
		String accountID=null;
		String autoshipNumber=null;
		String consultantEmailID=null;
		//----------------------------------------
		driver.get(driver.getStoreFrontURL()+"/"+driver.getCountry());
		while(true){
			randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_CONSULTANT_WITH_ORDERS_AND_AUTOSHIPS_RFO,countryId),RFO_DB);
			accountID = String.valueOf(getValueFromQueryResult(randomConsultantList, "AccountID"));
			randomConsultantUsernameList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_EMAIL_ID_FROM_ACCOUNT_ID,accountID),RFO_DB);
			consultantEmailID = String.valueOf(getValueFromQueryResult(randomConsultantUsernameList, "EmailAddress"));  
			storeFrontConsultantPage = storeFrontHomePage.loginAsConsultant(consultantEmailID, password);
			boolean isLoginError = driver.getCurrentUrl().contains("error");
			if(isLoginError){
				logger.info("Login error for the user "+consultantEmailID);
				driver.get(driver.getStoreFrontURL()+"/"+driver.getCountry());
			}
			else{
				break;
			}
		}
		logout();
		logger.info("emaild of consultant username "+consultantEmailID);
		driver.get(driver.getCSCockpitURL()); 
		cscockpitCustomerSearchTabPage = cscockpitLoginPage.clickLoginBtn();
		cscockpitCustomerSearchTabPage.selectCustomerTypeFromDropDownInCustomerSearchTab("CONSULTANT");
		cscockpitCustomerSearchTabPage.selectAccountStatusFromDropDownInCustomerSearchTab("Active");
		cscockpitCustomerSearchTabPage.enterEmailIdInSearchFieldInCustomerSearchTab(consultantEmailID);
		cscockpitCustomerSearchTabPage.clickSearchBtn();
		randomCustomerSequenceNumber = String.valueOf(cscockpitCustomerSearchTabPage.getRandomCustomerFromSearchResult());
		cscockpitCustomerSearchTabPage.clickAndReturnCIDNumberInCustomerSearchTab(randomCustomerSequenceNumber);
		autoshipNumber=cscockpitCustomerTabPage.getAndClickAutoshipIDHavingTypeAsCRPAutoshipAndStatusIsPending();
		cscockpitAutoshipTemplateTabPage.clickEditTemplateLinkInAutoshipTemplateTab();
		s_assert.assertTrue(cscockpitAutoshipTemplateTabPage.verifyCancelEditLinkInAutoshipTemplateTab(),"Cancel Edit link is not on Autoship template Tab Page");
		s_assert.assertTrue(cscockpitAutoshipTemplateTabPage.verifyNextCRPCartInAutoshipTemplateTab(),"Next crp cart link is not on Autoship template Tab Page");
		//Update quantity of product in autoship template and verify it.
		cscockpitAutoshipTemplateTabPage.addProductInAutoShipCartTillHaveRequiredProduct(3,TestConstants.NEW_BILLING_PROFILE_NAME+randomNum);
		String count=cscockpitAutoshipTemplateTabPage.getQuantityOfProductInAutoshipTemplateTabPage("2");
		String qty=Integer.toString(Integer.parseInt(count)+9);
		cscockpitAutoshipTemplateTabPage.updateQuantityOfSecondProduct(qty);
		s_assert.assertTrue(cscockpitAutoshipTemplateTabPage.getQuantityOfProductInAutoshipTemplateTabPage("2").equals(qty), "qty of product has not been updated");
		//Remove Product from autoship template and verify it.
		int countofDistinctProduct=cscockpitAutoshipTemplateTabPage.getCountOfProductInAutoshipTemplateTabPage();
		cscockpitAutoshipTemplateTabPage.clickRemoveLinkToRemoveProductFromAutoshipCart();
		int newCountofDistinctProduct=cscockpitAutoshipTemplateTabPage.getCountOfProductInAutoshipTemplateTabPage();
		s_assert.assertTrue(!(newCountofDistinctProduct==countofDistinctProduct), "product has not been successfully removed from autoship cart");
		//Add product to autoship cart and verify it.
		int totalQtyBefore=cscockpitAutoshipTemplateTabPage.getQuantityOfAllProductInAutoshipTemplateTabPage();
		cscockpitAutoshipTemplateTabPage.clickAddMoreLinesLinkInAutoShipTemplateTab();
		cscockpitAutoshipCartTabPage.selectValueFromSortByDDInCartTab("Price: High to Low");
		cscockpitAutoshipCartTabPage.selectCatalogFromDropDownInCartTab(); 
		String randomProductSequenceNumber = String.valueOf(cscockpitAutoshipCartTabPage.getRandomProductWithSKUFromSearchResult()); 
		String SKUValue = cscockpitAutoshipCartTabPage.getCustomerSKUValueInCartTab(randomProductSequenceNumber);
		cscockpitAutoshipCartTabPage.searchSKUValueInCartTab(SKUValue);
		SKUValue = cscockpitAutoshipCartTabPage.clickAddToCartBtnInCartTab(SKUValue);
		//String qtyOfProduct =  cscockpitAutoshipCartTabPage.getQtyOfProductAddedInToCart(SKUValue);
		cscockpitAutoshipCartTabPage.clickCheckoutBtnInCartTab();
		cscockpitAutoshipTemplateUpdateTabPage.clickAddNewPaymentAddressInCheckoutTab();
		cscockpitAutoshipTemplateUpdateTabPage.enterBillingInfo(TestConstants.CARD_NUMBER,TestConstants.NEW_BILLING_PROFILE_NAME+randomNum,TestConstants.SECURITY_CODE);
		cscockpitAutoshipTemplateUpdateTabPage.clickSaveAddNewPaymentProfilePopUP();
		cscockpitAutoshipTemplateUpdateTabPage.enterCVVValueInCheckoutTab(TestConstants.SECURITY_CODE);
		cscockpitAutoshipTemplateUpdateTabPage.clickUseThisCardBtnInCheckoutTab();
		cscockpitAutoshipTemplateUpdateTabPage.clickUpdateAutoshipTemplateInAutoshipTemplateUpdateTab();
		int totalQtyAfter=cscockpitAutoshipTemplateTabPage.getQuantityOfAllProductInAutoshipTemplateTabPage();
		s_assert.assertTrue(!(totalQtyAfter==totalQtyBefore), "Product has not been successfully added to cart");
		s_assert.assertAll();
	}

	// Hybris Project-1807:To verify that cscommissionadmin can Edit Autoship for consultant and PC
	@Test(enabled=false)//WIP
	public void testVerifyCSComissionCanEditAutoshipForConsultantAndPC_1807(){ 
		String randomCustomerSequenceNumber = null;
		RFO_DB = driver.getDBNameRFO();
		int randomNum = CommonUtils.getRandomNum(10000, 1000000);
		List<Map<String, Object>> randomConsultantList =  null;
		List<Map<String, Object>> randomConsultantUsernameList =  null;
		String accountID=null;
		String autoshipNumber=null;
		String consultantEmailID=null;
		//----------------------------------------
		driver.get(driver.getStoreFrontURL()+"/"+driver.getCountry());
		while(true){
			randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_CONSULTANT_WITH_ORDERS_AND_AUTOSHIPS_RFO,countryId),RFO_DB);
			accountID = String.valueOf(getValueFromQueryResult(randomConsultantList, "AccountID"));
			randomConsultantUsernameList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_EMAIL_ID_FROM_ACCOUNT_ID,accountID),RFO_DB);
			consultantEmailID = String.valueOf(getValueFromQueryResult(randomConsultantUsernameList, "EmailAddress"));  
			storeFrontConsultantPage = storeFrontHomePage.loginAsConsultant(consultantEmailID, password);
			boolean isLoginError = driver.getCurrentUrl().contains("error");
			if(isLoginError){
				logger.info("Login error for the user "+consultantEmailID);
				driver.get(driver.getStoreFrontURL()+"/"+driver.getCountry());
			}
			else{
				break;
			}
		}
		logout();
		logger.info("emaild of consultant username "+consultantEmailID);
		driver.get(driver.getCSCockpitURL()); 
		cscockpitLoginPage.enterUsername(TestConstants.CS_COMMISION_ADMIN_USERNAME);
		cscockpitCustomerSearchTabPage = cscockpitLoginPage.clickLoginBtn();
		cscockpitCustomerSearchTabPage.selectCustomerTypeFromDropDownInCustomerSearchTab("CONSULTANT");
		cscockpitCustomerSearchTabPage.selectAccountStatusFromDropDownInCustomerSearchTab("Active");
		cscockpitCustomerSearchTabPage.enterEmailIdInSearchFieldInCustomerSearchTab(consultantEmailID);
		cscockpitCustomerSearchTabPage.clickSearchBtn();
		randomCustomerSequenceNumber = String.valueOf(cscockpitCustomerSearchTabPage.getRandomCustomerFromSearchResult());
		cscockpitCustomerSearchTabPage.clickAndReturnCIDNumberInCustomerSearchTab(randomCustomerSequenceNumber);
		autoshipNumber=cscockpitCustomerTabPage.getAndClickAutoshipIDHavingTypeAsCRPAutoshipAndStatusIsPending();
		cscockpitAutoshipTemplateTabPage.clickEditTemplateLinkInAutoshipTemplateTab();
		s_assert.assertTrue(cscockpitAutoshipTemplateTabPage.verifyCancelEditLinkInAutoshipTemplateTab(),"Cancel Edit link is not on Autoship template Tab Page");
		s_assert.assertTrue(cscockpitAutoshipTemplateTabPage.verifyNextCRPCartInAutoshipTemplateTab(),"Next crp cart link is not on Autoship template Tab Page");
		//Update quantity of product in autoship template and verify it.
		cscockpitAutoshipTemplateTabPage.addProductInAutoShipCartTillHaveRequiredProduct(3,TestConstants.NEW_BILLING_PROFILE_NAME+randomNum);
		String count=cscockpitAutoshipTemplateTabPage.getQuantityOfProductInAutoshipTemplateTabPage("2");
		String qty=Integer.toString(Integer.parseInt(count)+9);
		cscockpitAutoshipTemplateTabPage.updateQuantityOfSecondProduct(qty);
		s_assert.assertTrue(cscockpitAutoshipTemplateTabPage.getQuantityOfProductInAutoshipTemplateTabPage("2").equals(qty), "qty of product has not been updated");
		//Remove Product from autoship template and verify it.
		int countofDistinctProduct=cscockpitAutoshipTemplateTabPage.getCountOfProductInAutoshipTemplateTabPage();
		cscockpitAutoshipTemplateTabPage.clickRemoveLinkToRemoveProductFromAutoshipCart();
		int newCountofDistinctProduct=cscockpitAutoshipTemplateTabPage.getCountOfProductInAutoshipTemplateTabPage();
		s_assert.assertTrue(!(newCountofDistinctProduct==countofDistinctProduct), "product has not been successfully removed from autoship cart");
		//Add product to autoship cart and verify it.
		int totalQtyBefore=cscockpitAutoshipTemplateTabPage.getQuantityOfAllProductInAutoshipTemplateTabPage();
		cscockpitAutoshipTemplateTabPage.clickAddMoreLinesLinkInAutoShipTemplateTab();
		cscockpitAutoshipCartTabPage.selectValueFromSortByDDInCartTab("Price: High to Low");
		cscockpitAutoshipCartTabPage.selectCatalogFromDropDownInCartTab(); 
		String randomProductSequenceNumber = String.valueOf(cscockpitAutoshipCartTabPage.getRandomProductWithSKUFromSearchResult()); 
		String SKUValue = cscockpitAutoshipCartTabPage.getCustomerSKUValueInCartTab(randomProductSequenceNumber);
		cscockpitAutoshipCartTabPage.searchSKUValueInCartTab(SKUValue);
		SKUValue = cscockpitAutoshipCartTabPage.clickAddToCartBtnInCartTab(SKUValue);
		//String qtyOfProduct =  cscockpitAutoshipCartTabPage.getQtyOfProductAddedInToCart(SKUValue);
		cscockpitAutoshipCartTabPage.clickCheckoutBtnInCartTab();
		cscockpitAutoshipTemplateUpdateTabPage.clickAddNewPaymentAddressInCheckoutTab();
		cscockpitAutoshipTemplateUpdateTabPage.enterBillingInfo(TestConstants.CARD_NUMBER,TestConstants.NEW_BILLING_PROFILE_NAME+randomNum,TestConstants.SECURITY_CODE);
		cscockpitAutoshipTemplateUpdateTabPage.clickSaveAddNewPaymentProfilePopUP();
		cscockpitAutoshipTemplateUpdateTabPage.enterCVVValueInCheckoutTab(TestConstants.SECURITY_CODE);
		cscockpitAutoshipTemplateUpdateTabPage.clickUseThisCardBtnInCheckoutTab();
		cscockpitAutoshipTemplateUpdateTabPage.clickUpdateAutoshipTemplateInAutoshipTemplateUpdateTab();
		int totalQtyAfter=cscockpitAutoshipTemplateTabPage.getQuantityOfAllProductInAutoshipTemplateTabPage();
		s_assert.assertTrue(!(totalQtyAfter==totalQtyBefore), "Product has not been successfully added to cart");
		s_assert.assertAll();
	}

	//Hybris Project-1796:To verify that cssalessupervisory can Cancel Autoship for consultant and PC
	@Test(enabled=false)//WIP
	public void testToVerifyCSSalessupervisoryCanCancelAutoshipForConsultantAndPC_1796() throws InterruptedException{
		String randomOrderSequenceNumber = null;
		RFO_DB = driver.getDBNameRFO();
		String consultantEmailID = null;
		String accountID = null;
		cscockpitLoginPage = new CSCockpitLoginPage(driver);

		List<Map<String, Object>> randomConsultantList =  null;
		List<Map<String, Object>> randomConsultantUsernameList =  null;
		driver.get(driver.getStoreFrontURL()+"/us");
		storeFrontHomePage = new StoreFrontHomePage(driver);
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
				break;
		}
		logout();

		logger.info("emaild of consultant username "+consultantEmailID);
		logger.info("login is successful");
		driver.get(driver.getCSCockpitURL());
		cscockpitLoginPage.enterUsername(TestConstants.CS_SALES_SUPERVISORY_USERNAME);
		cscockpitCustomerSearchTabPage = cscockpitLoginPage.clickLoginBtn();
		cscockpitCustomerSearchTabPage.selectCustomerTypeFromDropDownInCustomerSearchTab("CONSULTANT");
		cscockpitCustomerSearchTabPage.selectCountryFromDropDownInCustomerSearchTab("United States");
		cscockpitCustomerSearchTabPage.selectAccountStatusFromDropDownInCustomerSearchTab("Active");
		cscockpitCustomerSearchTabPage.enterEmailIdInSearchFieldInCustomerSearchTab(consultantEmailID);
		cscockpitCustomerSearchTabPage.clickSearchBtn();
		randomOrderSequenceNumber = String.valueOf(cscockpitOrderSearchTabPage.getRandomOrdersFromOrderResultSearchFirstPageInOrderSearchTab());
		cscockpitOrderSearchTabPage.clickOrderNumberInOrderSearchResultsInOrderSearchTab(randomOrderSequenceNumber);
		cscockpitCustomerTabPage.getAndClickAutoshipIDHavingTypeAsCRPAutoshipAndStatusIsPending();
		cscockpitAutoshipTemplateTabPage.clickCancelAutoship();

		s_assert.assertTrue(cscockpitAutoshipTemplateTabPage.isCancelAutoshipPopupAlertPresent(), "Cancel Autoship Popup is not Present");

		cscockpitAutoshipTemplateTabPage.clickCancelButtonOfCancelAutoshipTemplatePopup();

		s_assert.assertTrue(cscockpitAutoshipTemplateTabPage.IsPageTabSelected("Autoship Template"), "We are here on Autoship Template Tab");

		//---------------------FOR CA-----------------------
		driver.get(driver.getStoreFrontURL()+"/ca");
		storeFrontHomePage = new StoreFrontHomePage(driver);
		while(true){
			randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_CONSULTANT_WITH_ORDERS_AND_AUTOSHIPS_RFO,"40"),RFO_DB);
			accountID = String.valueOf(getValueFromQueryResult(randomConsultantList, "AccountID"));
			randomConsultantUsernameList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_EMAIL_ID_FROM_ACCOUNT_ID,accountID),RFO_DB);
			consultantEmailID = String.valueOf(getValueFromQueryResult(randomConsultantUsernameList, "EmailAddress"));
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

		logger.info("emaild of consultant username "+consultantEmailID);
		logger.info("login is successful");
		driver.get(driver.getCSCockpitURL());
		cscockpitLoginPage.enterUsername(TestConstants.CS_SALES_SUPERVISORY_USERNAME);
		cscockpitCustomerSearchTabPage = cscockpitLoginPage.clickLoginBtn();
		cscockpitCustomerSearchTabPage.selectCustomerTypeFromDropDownInCustomerSearchTab("CONSULTANT");
		cscockpitCustomerSearchTabPage.selectCountryFromDropDownInCustomerSearchTab("Canada");
		cscockpitCustomerSearchTabPage.selectAccountStatusFromDropDownInCustomerSearchTab("Active");
		cscockpitCustomerSearchTabPage.enterEmailIdInSearchFieldInCustomerSearchTab(consultantEmailID);
		cscockpitCustomerSearchTabPage.clickSearchBtn();
		randomOrderSequenceNumber = String.valueOf(cscockpitOrderSearchTabPage.getRandomOrdersFromOrderResultSearchFirstPageInOrderSearchTab());
		cscockpitOrderSearchTabPage.clickOrderNumberInOrderSearchResultsInOrderSearchTab(randomOrderSequenceNumber);
		cscockpitCustomerTabPage.getAndClickAutoshipIDHavingTypeAsCRPAutoshipAndStatusIsPending();
		cscockpitAutoshipTemplateTabPage.clickCancelAutoship();

		s_assert.assertTrue(cscockpitAutoshipTemplateTabPage.isCancelAutoshipPopupAlertPresent(), "Cancel Autoship Popup is not Present");

		cscockpitAutoshipTemplateTabPage.clickCancelButtonOfCancelAutoshipTemplatePopup();

		s_assert.assertTrue(cscockpitAutoshipTemplateTabPage.IsPageTabSelected("Autoship Template"), "We are here on Autoship Template Tab");
		s_assert.assertAll();
	}

	//Hybris Project-1795:To verify that cscommissionadmin can Cancel Autoship for consultant and PC
	@Test(enabled=false)//WIP
	public void testToVerifyCSCommissionAdminCanCancelAutoshipForConsultantAndPC_1795() throws InterruptedException{
		String randomOrderSequenceNumber = null;
		RFO_DB = driver.getDBNameRFO();
		String consultantEmailID = null;
		String accountID = null;
		cscockpitLoginPage = new CSCockpitLoginPage(driver);

		List<Map<String, Object>> randomConsultantList =  null;
		List<Map<String, Object>> randomConsultantUsernameList =  null;
		driver.get(driver.getStoreFrontURL()+"/us");
		storeFrontHomePage = new StoreFrontHomePage(driver);
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
				break;
		}
		logout();

		logger.info("emaild of consultant username "+consultantEmailID);
		logger.info("login is successful");
		driver.get(driver.getCSCockpitURL());
		cscockpitLoginPage.enterUsername(TestConstants.CS_COMMISION_ADMIN_USERNAME);
		cscockpitCustomerSearchTabPage = cscockpitLoginPage.clickLoginBtn();
		cscockpitCustomerSearchTabPage.selectCustomerTypeFromDropDownInCustomerSearchTab("CONSULTANT");
		cscockpitCustomerSearchTabPage.selectCountryFromDropDownInCustomerSearchTab("United States");
		cscockpitCustomerSearchTabPage.selectAccountStatusFromDropDownInCustomerSearchTab("Active");
		cscockpitCustomerSearchTabPage.enterEmailIdInSearchFieldInCustomerSearchTab(consultantEmailID);
		cscockpitCustomerSearchTabPage.clickSearchBtn();
		randomOrderSequenceNumber = String.valueOf(cscockpitOrderSearchTabPage.getRandomOrdersFromOrderResultSearchFirstPageInOrderSearchTab());
		cscockpitOrderSearchTabPage.clickOrderNumberInOrderSearchResultsInOrderSearchTab(randomOrderSequenceNumber);
		cscockpitCustomerTabPage.getAndClickAutoshipIDHavingTypeAsCRPAutoshipAndStatusIsPending();
		cscockpitAutoshipTemplateTabPage.clickCancelAutoship();

		s_assert.assertTrue(cscockpitAutoshipTemplateTabPage.isCancelAutoshipPopupAlertPresent(), "Cancel Autoship Popup is not Present");

		cscockpitAutoshipTemplateTabPage.clickCancelButtonOfCancelAutoshipTemplatePopup();

		s_assert.assertTrue(cscockpitAutoshipTemplateTabPage.IsPageTabSelected("Autoship Template"), "We are here on Autoship Template Tab");

		//---------------------FOR CA-----------------------
		driver.get(driver.getStoreFrontURL()+"/ca");
		storeFrontHomePage = new StoreFrontHomePage(driver);
		while(true){
			randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_CONSULTANT_WITH_ORDERS_AND_AUTOSHIPS_RFO,"40"),RFO_DB);
			accountID = String.valueOf(getValueFromQueryResult(randomConsultantList, "AccountID"));
			randomConsultantUsernameList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_EMAIL_ID_FROM_ACCOUNT_ID,accountID),RFO_DB);
			consultantEmailID = String.valueOf(getValueFromQueryResult(randomConsultantUsernameList, "EmailAddress"));
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

		logger.info("emaild of consultant username "+consultantEmailID);
		logger.info("login is successful");
		driver.get(driver.getCSCockpitURL());
		cscockpitLoginPage.enterUsername(TestConstants.CS_COMMISION_ADMIN_USERNAME);
		cscockpitCustomerSearchTabPage = cscockpitLoginPage.clickLoginBtn();
		cscockpitCustomerSearchTabPage.selectCustomerTypeFromDropDownInCustomerSearchTab("CONSULTANT");
		cscockpitCustomerSearchTabPage.selectCountryFromDropDownInCustomerSearchTab("Canada");
		cscockpitCustomerSearchTabPage.selectAccountStatusFromDropDownInCustomerSearchTab("Active");
		cscockpitCustomerSearchTabPage.enterEmailIdInSearchFieldInCustomerSearchTab(consultantEmailID);
		cscockpitCustomerSearchTabPage.clickSearchBtn();
		randomOrderSequenceNumber = String.valueOf(cscockpitOrderSearchTabPage.getRandomOrdersFromOrderResultSearchFirstPageInOrderSearchTab());
		cscockpitOrderSearchTabPage.clickOrderNumberInOrderSearchResultsInOrderSearchTab(randomOrderSequenceNumber);
		cscockpitCustomerTabPage.getAndClickAutoshipIDHavingTypeAsCRPAutoshipAndStatusIsPending();
		cscockpitAutoshipTemplateTabPage.clickCancelAutoship();

		s_assert.assertTrue(cscockpitAutoshipTemplateTabPage.isCancelAutoshipPopupAlertPresent(), "Cancel Autoship Popup is not Present");

		cscockpitAutoshipTemplateTabPage.clickCancelButtonOfCancelAutoshipTemplatePopup();

		s_assert.assertTrue(cscockpitAutoshipTemplateTabPage.IsPageTabSelected("Autoship Template"), "We are here on Autoship Template Tab");
		s_assert.assertAll();
	}
}
