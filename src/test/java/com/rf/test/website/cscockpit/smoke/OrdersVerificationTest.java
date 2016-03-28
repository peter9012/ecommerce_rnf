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
		storeFrontUpdateCartPage = new StoreFrontUpdateCartPage(driver);
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

		while(true){
			randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_CONSULTANT_WITH_ORDERS_AND_AUTOSHIPS_RFO,countryId),RFO_DB);
			accountID=String.valueOf(getValueFromQueryResult(randomConsultantList, "AccountID"));
			emailIdFromAccountIdList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_EMAIL_ID_FROM_ACCOUNT_ID,accountID),RFO_DB);
			consultantEmailID=(String) getValueFromQueryResult(emailIdFromAccountIdList, "EmailAddress");
			cscockpitCustomerSearchTabPage = cscockpitLoginPage.clickLoginBtn();
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
		String accountID = null;

		//-------------------FOR US----------------------------------
		driver.get(driver.getStoreFrontURL()+"/us");
		List<Map<String, Object>> randomConsultantList =  null;
		List<Map<String, Object>> randomConsultantUsernameList =  null;
		String consultantEmailID=null;
		
		while(true){
			randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_CONSULTANT_WITH_ORDERS_AND_AUTOSHIPS_RFO,"236"),RFO_DB);
			accountID=String.valueOf(getValueFromQueryResult(randomConsultantList, "AccountID"));
			randomConsultantUsernameList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_EMAIL_ID_FROM_ACCOUNT_ID,accountID),RFO_DB);
			consultantEmailID = (String) getValueFromQueryResult(randomConsultantUsernameList, "EmailAddress");
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
		List<Map<String, Object>>sponsorIdList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_ACCOUNT_NUMBER_FOR_PWS,accountID),RFO_DB);
		String	cid = String.valueOf(getValueFromQueryResult(sponsorIdList, "AccountNumber"));
		//Get order number from account Id
		List<Map<String, Object>> orderNumberList=DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_ORDER_NUMBER_FROM_ACCOUNT_ID,accountID),RFO_DB);
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
		cscockpitOrderSearchTabPage.clickSearchBtn();
		s_assert.assertTrue(cscockpitOrderSearchTabPage.verifySectionsIsPresentInOrderSearchTab(firstName), "First name section is not present in order search tab");
		s_assert.assertTrue(cscockpitOrderSearchTabPage.verifySectionsIsPresentInOrderSearchTab(lastName), "Last name section is not present in order search tab");
		s_assert.assertTrue(cscockpitOrderSearchTabPage.verifySectionsIsPresentInOrderSearchTab(RFCID), "RFCID section is not present in order search tab");

		cscockpitCustomerSearchTabPage.selectCountryFromDropDownInCustomerSearchTab(TestConstants.COUNTRY_DD_VALUE_CA);
		cscockpitOrderSearchTabPage.clickSearchBtn();
		s_assert.assertTrue(cscockpitOrderSearchTabPage.verifySectionsIsPresentInOrderSearchTab(firstName), "First name section is not present in order search tab");
		s_assert.assertTrue(cscockpitOrderSearchTabPage.verifySectionsIsPresentInOrderSearchTab(lastName), "Last name section is not present in order search tab");
		s_assert.assertTrue(cscockpitOrderSearchTabPage.verifySectionsIsPresentInOrderSearchTab(RFCID), "RFCID section is not present in order search tab");
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

		driver.get(driver.getStoreFrontURL()+"/us");
		List<Map<String, Object>> randomConsultantList =  null;
		List<Map<String, Object>> randomConsultantUsernameList =  null;
		storeFrontHomePage = new StoreFrontHomePage(driver);
		while(true){
			randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_CONSULTANT_WITH_ORDERS_AND_AUTOSHIPS_RFO,"236"),RFO_DB);
			accountID=String.valueOf(getValueFromQueryResult(randomConsultantList, "AccountID"));
			randomConsultantUsernameList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_EMAIL_ID_FROM_ACCOUNT_ID,accountID),RFO_DB);
			consultantEmailID = (String) getValueFromQueryResult(randomConsultantUsernameList, "EmailAddress");
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
