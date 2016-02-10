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


public class CheckoutVerificationTest extends RFWebsiteBaseTest{
	private static final Logger logger = LogManager
			.getLogger(CheckoutVerificationTest.class.getName());

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

	public CheckoutVerificationTest() {
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

	//Hybris Project-1944:To verify Checkout Page UI while placing the order
	@Test 
	public void testVerifyCheckoutPageUIWhilePlacingOrder_1944(){
		String randomProductSequenceNumber = null;
		String SKUValue = null;

		//-------------------FOR US----------------------------------	
		cscockpitCustomerSearchTabPage = cscockpitLoginPage.clickLoginBtn();
		cscockpitCustomerSearchTabPage.selectCustomerTypeFromDropDownInCustomerSearchTab("CONSULTANT");
		cscockpitCustomerSearchTabPage.selectCountryFromDropDownInCustomerSearchTab("United States");
		cscockpitCustomerSearchTabPage.selectAccountStatusFromDropDownInCustomerSearchTab("Active");
		cscockpitCustomerSearchTabPage.clickSearchBtn();
		String randomCustomerSequenceNumber = String.valueOf(cscockpitCustomerSearchTabPage.getRandomCustomerFromSearchResult());
		String firstName = cscockpitCustomerSearchTabPage.getfirstNameOfTheCustomerInCustomerSearchTab(randomCustomerSequenceNumber);
		cscockpitCustomerSearchTabPage.clickCIDNumberInCustomerSearchTab(randomCustomerSequenceNumber);
		cscockpitCustomerTabPage.clickPlaceOrderButtonInCustomerTab();
		cscockpitCartTabPage.selectValueFromSortByDDInCartTab("Price: High to Low");
		cscockpitCartTabPage.selectCatalogFromDropDownInCartTab();	
		randomProductSequenceNumber = String.valueOf(cscockpitCartTabPage.getRandomProductWithSKUFromSearchResult()); 
		SKUValue = cscockpitCartTabPage.getCustomerSKUValueInCartTab(randomProductSequenceNumber);
		cscockpitCartTabPage.searchSKUValueInCartTab(SKUValue);
		cscockpitCartTabPage.clickAddToCartBtnInCartTab();
		cscockpitCartTabPage.clickCheckoutBtnInCartTab();
		String fullName = cscockpitCheckoutTabPage.getNameFromCartSectionInCheckoutTab(firstName);
		s_assert.assertTrue(fullName.contains(firstName),"full name on UI in cart section"+fullName+"while expected "+firstName);
		s_assert.assertTrue(cscockpitCheckoutTabPage.verifyBasePriceIsPresentInCartSectionInCheckoutTab(), "Base price is not present in cart section of checkeout tab");
		s_assert.assertTrue(cscockpitCheckoutTabPage.verifyTotalPriceIsPresentInCartSectionInCheckoutTab(), "Total price is not present in cart section of checkeout tab");
		s_assert.assertTrue(cscockpitCheckoutTabPage.verifyEntryCVIsPresentInCartSectionInCheckoutTab(), "Entry CV is not present in cart section of checkeout tab");
		s_assert.assertTrue(cscockpitCheckoutTabPage.verifyEntryQVIsPresentInCartSectionInCheckoutTab(), "Entry QV is not present in cart section of checkeout tab");
		s_assert.assertTrue(cscockpitCheckoutTabPage.getSelectedDeliveryAddressInCheckoutTab().contains(firstName),"selected delivery address on UI "+cscockpitCheckoutTabPage.getSelectedDeliveryAddressInCheckoutTab()+" does not contain "+firstName);
		s_assert.assertTrue(cscockpitCheckoutTabPage.verifyNewAddressIsPresentInDeliverySectionInCheckoutTab(), "New address button is not present in delivery section of checkeout tab");
		s_assert.assertTrue(cscockpitCheckoutTabPage.getDeliverModeTypeInCheckoutTab().contains("FedEx Ground (HD)"),"CSCockpit checkout tab delivery mode type expected = FedEx Ground (HD) and on UI = " +cscockpitCheckoutTabPage.getDeliverModeTypeInCheckoutTab());
		s_assert.assertTrue(cscockpitCheckoutTabPage.verifySubtotalTxtIsPresentInTotalsSectionInCheckoutTab(), "Subtotal is not present in totals section of checkeout tab");
		s_assert.assertTrue(cscockpitCheckoutTabPage.verifyDiscountTxtIsPresentInTotalsSectionInCheckoutTab(), "Discount is not present in totals section of checkeout tab");
		s_assert.assertTrue(cscockpitCheckoutTabPage.verifyDeliverCostsTxtIsPresentInTotalsSectionInCheckoutTab(), "Delivery Costs is not present in totals section of checkeout tab");
		s_assert.assertTrue(cscockpitCheckoutTabPage.verifyHandlingCostsTxtIsPresentInTotalsSectionInCheckoutTab(), "Handling Costs is not present in totals section of checkeout tab");
		s_assert.assertTrue(cscockpitCheckoutTabPage.verifyTaxesTxtIsPresentInTotalsSectionInCheckoutTab(), "taxes is not present in totals section of checkeout tab");
		s_assert.assertTrue(cscockpitCheckoutTabPage.verifyTotalPriceTxtIsPresentInTotalsSectionInCheckoutTab(), "Total price is not present in totals section of checkeout tab");
		s_assert.assertTrue(cscockpitCheckoutTabPage.verifyTotalCVTxtIsPresentInTotalsSectionInCheckoutTab(), "Total CV is not present in totals section of checkeout tab");
		s_assert.assertTrue(cscockpitCheckoutTabPage.verifyTotalQVTxtIsPresentInTotalsSectionInCheckoutTab(), "Total QV is not present in totals section of checkeout tab");
		s_assert.assertTrue(cscockpitCheckoutTabPage.verifyCommissionDateCalenderIconTxtIsPresentInCustomSectionInCheckoutTab(), "Commission date calender icon is not present in custom section of checkeout tab");
		s_assert.assertTrue(cscockpitCheckoutTabPage.verifyOrderNotesTextInOrderInfoSectionInCheckoutTab(), "Order notes text is not present in order note info section of checkeout tab");
		s_assert.assertTrue(cscockpitCheckoutTabPage.verifyOrderNotesTextAreaInOrderInfoSectionInCheckoutTab(), "Order notes text area is not present in order note info section of checkeout tab");
		s_assert.assertTrue(cscockpitCheckoutTabPage.verifyOrderNoteAddBtnInOrderInfoSectionInCheckoutTab(), "Order notes add button is not present in order note info section of checkeout tab");
		s_assert.assertTrue(cscockpitCheckoutTabPage.verifyNoPromotionsAppliedInAppliedPromotionsSectionInCheckoutTab(), "No promotion txt is not present in promotion section of checkeout tab");
		//payment Section
		s_assert.assertTrue(cscockpitCheckoutTabPage.verifyCreditCardNumberSectionInCheckoutTab(), "Credit card number section is not present in order note info section of checkeout tab");
		s_assert.assertTrue(cscockpitCheckoutTabPage.verifyCreditCardOwnerSectionInCheckoutTab(), "Credit card owner section is not present in order note info section of checkeout tab");
		s_assert.assertTrue(cscockpitCheckoutTabPage.verifyCreditCardTypeSectionInCheckoutTab(), "Credit card type section is not present in order note info section of checkeout tab");
		s_assert.assertTrue(cscockpitCheckoutTabPage.verifyCreditCardMonthSectionInCheckoutTab(), "Credit card month section is not present in order note info section of checkeout tab");
		s_assert.assertTrue(cscockpitCheckoutTabPage.verifyCreditCardValidToYearSectionInCheckoutTab(), "Credit card valid to year section is not present in order note info section of checkeout tab");
		s_assert.assertTrue(cscockpitCheckoutTabPage.verifyPaymentBillingAddressSectionInCheckoutTab(), "Billing address section is not present in order note info section of checkeout tab");
		s_assert.assertTrue(cscockpitCheckoutTabPage.verifyPaymentAmountSectionInCheckoutTab(), "Payment amount section is not present in order note info section of checkeout tab");
		s_assert.assertTrue(cscockpitCheckoutTabPage.verifyCreditCardCv2SectionInCheckoutTab(), "Credit Card Cv2 section is not present in order note info section of checkeout tab");
		s_assert.assertTrue(cscockpitCheckoutTabPage.verifyUseThisCardBtnInCheckoutTab(), "Use this card button is not present in order note info section of checkeout tab");
		//Place Order Section
		s_assert.assertTrue(cscockpitCheckoutTabPage.verifyPlaceOrderBtnIsPresentInCheckoutTab(), "Place order button is not present in order note info section of checkeout tab");
		s_assert.assertTrue(cscockpitCheckoutTabPage.verifyPerformSOOBtnIsPresentInCheckoutTab(), "Perform SOO button is not present in order note info section of checkeout tab");
		s_assert.assertTrue(cscockpitCheckoutTabPage.verifyTestOrderChkBoxIsPresentInCheckoutTab(), "Txt order checkbox is not present in order note info section of checkeout tab");
		s_assert.assertTrue(cscockpitCheckoutTabPage.verifyDoNotShipChkBoxIsPresentInCheckoutTab(), "Do Not Ship checkbox is not present in order note info section of checkeout tab");

		//-------------------FOR CA----------------------------------
		driver.get(driver.getCSCockpitURL());
		driver.pauseExecutionFor(5000);
		cscockpitCustomerSearchTabPage = cscockpitLoginPage.clickLoginBtn();
		cscockpitCustomerSearchTabPage.selectCustomerTypeFromDropDownInCustomerSearchTab("CONSULTANT");
		cscockpitCustomerSearchTabPage.selectCountryFromDropDownInCustomerSearchTab("Canada");
		cscockpitCustomerSearchTabPage.selectAccountStatusFromDropDownInCustomerSearchTab("Active");
		cscockpitCustomerSearchTabPage.clickSearchBtn();
		randomCustomerSequenceNumber = String.valueOf(cscockpitCustomerSearchTabPage.getRandomCustomerFromSearchResult());
		firstName = cscockpitCustomerSearchTabPage.getfirstNameOfTheCustomerInCustomerSearchTab(randomCustomerSequenceNumber);
		cscockpitCustomerSearchTabPage.clickCIDNumberInCustomerSearchTab(randomCustomerSequenceNumber);
		cscockpitCustomerTabPage.clickPlaceOrderButtonInCustomerTab();
		cscockpitCartTabPage.selectValueFromSortByDDInCartTab("Price: High to Low");
		cscockpitCartTabPage.selectCatalogFromDropDownInCartTab();	
		randomProductSequenceNumber = String.valueOf(cscockpitCartTabPage.getRandomProductWithSKUFromSearchResult()); 
		SKUValue = cscockpitCartTabPage.getCustomerSKUValueInCartTab(randomProductSequenceNumber);
		cscockpitCartTabPage.searchSKUValueInCartTab(SKUValue);
		cscockpitCartTabPage.clickAddToCartBtnInCartTab();
		cscockpitCartTabPage.clickCheckoutBtnInCartTab();
		fullName = cscockpitCheckoutTabPage.getNameFromCartSectionInCheckoutTab(firstName);
		s_assert.assertTrue(fullName.contains(firstName),"full name on UI in cart section_CA"+fullName+"while expected "+firstName);
		s_assert.assertTrue(cscockpitCheckoutTabPage.verifyBasePriceIsPresentInCartSectionInCheckoutTab(), "Base price is not present in cart section of checkeout tab_CA");
		s_assert.assertTrue(cscockpitCheckoutTabPage.verifyTotalPriceIsPresentInCartSectionInCheckoutTab(), "Total price is not present in cart section of checkeout tab_CA");
		s_assert.assertTrue(cscockpitCheckoutTabPage.verifyEntryCVIsPresentInCartSectionInCheckoutTab(), "Entry CV is not present in cart section of checkeout tab_CA");
		s_assert.assertTrue(cscockpitCheckoutTabPage.verifyEntryQVIsPresentInCartSectionInCheckoutTab(), "Entry QV is not present in cart section of checkeout tab_CA");
		s_assert.assertTrue(cscockpitCheckoutTabPage.getSelectedDeliveryAddressInCheckoutTab().contains(firstName),"selected delivery address on UI_CA "+cscockpitCheckoutTabPage.getSelectedDeliveryAddressInCheckoutTab()+" does not contain "+firstName);
		s_assert.assertTrue(cscockpitCheckoutTabPage.verifyNewAddressIsPresentInDeliverySectionInCheckoutTab(), "New address button is not present in delivery section of checkeout tab_CA");
		s_assert.assertTrue(cscockpitCheckoutTabPage.getDeliverModeTypeInCheckoutTab().contains("UPS Ground (HD)"),"CSCockpit checkout tab delivery mode type expected = FedEx Ground (HD) and on UI_CA = " +cscockpitCheckoutTabPage.getDeliverModeTypeInCheckoutTab());
		s_assert.assertTrue(cscockpitCheckoutTabPage.verifySubtotalTxtIsPresentInTotalsSectionInCheckoutTab(), "Subtotal is not present in totals section of checkeout tab_CA");
		s_assert.assertTrue(cscockpitCheckoutTabPage.verifyDiscountTxtIsPresentInTotalsSectionInCheckoutTab(), "Discount is not present in totals section of checkeout tab_CA");
		s_assert.assertTrue(cscockpitCheckoutTabPage.verifyDeliverCostsTxtIsPresentInTotalsSectionInCheckoutTab(), "Delivery Costs is not present in totals section of checkeout tab_CA");
		s_assert.assertTrue(cscockpitCheckoutTabPage.verifyHandlingCostsTxtIsPresentInTotalsSectionInCheckoutTab(), "Handling Costs is not present in totals section of checkeout tab_CA");
		s_assert.assertTrue(cscockpitCheckoutTabPage.verifyTotalPriceTxtIsPresentInTotalsSectionInCheckoutTab(), "Total price is not present in totals section of checkeout tab_CA");
		s_assert.assertTrue(cscockpitCheckoutTabPage.verifyTotalCVTxtIsPresentInTotalsSectionInCheckoutTab(), "Total CV is not present in totals section of checkeout tab_CA");
		s_assert.assertTrue(cscockpitCheckoutTabPage.verifyTotalQVTxtIsPresentInTotalsSectionInCheckoutTab(), "Total QV is not present in totals section of checkeout tab_CA");
		s_assert.assertTrue(cscockpitCheckoutTabPage.verifyCommissionDateCalenderIconTxtIsPresentInCustomSectionInCheckoutTab(), "Commission date calender icon is not present in custom section of checkeout tab_CA");
		s_assert.assertTrue(cscockpitCheckoutTabPage.verifyOrderNotesTextInOrderInfoSectionInCheckoutTab(), "Order notes text is not present in order note info section of checkeout tab_CA");
		s_assert.assertTrue(cscockpitCheckoutTabPage.verifyOrderNotesTextAreaInOrderInfoSectionInCheckoutTab(), "Order notes text area is not present in order note info section of checkeout tab_CA");
		s_assert.assertTrue(cscockpitCheckoutTabPage.verifyOrderNoteAddBtnInOrderInfoSectionInCheckoutTab(), "Order notes add button is not present in order note info section of checkeout tab_CA");
		s_assert.assertTrue(cscockpitCheckoutTabPage.verifyNoPromotionsAppliedInAppliedPromotionsSectionInCheckoutTab(), "No promotion txt is not present in promotion section of checkeout tab_CA");
		//payment Section
		s_assert.assertTrue(cscockpitCheckoutTabPage.verifyCreditCardNumberSectionInCheckoutTab(), "Credit card number section is not present in order note info section of checkeout tab_CA");
		s_assert.assertTrue(cscockpitCheckoutTabPage.verifyCreditCardOwnerSectionInCheckoutTab(), "Credit card owner section is not present in order note info section of checkeout tab_CA");
		s_assert.assertTrue(cscockpitCheckoutTabPage.verifyCreditCardTypeSectionInCheckoutTab(), "Credit card type section is not present in order note info section of checkeout tab_CA");
		s_assert.assertTrue(cscockpitCheckoutTabPage.verifyCreditCardMonthSectionInCheckoutTab(), "Credit card month section is not present in order note info section of checkeout tab_CA");
		s_assert.assertTrue(cscockpitCheckoutTabPage.verifyCreditCardValidToYearSectionInCheckoutTab(), "Credit card valid to year section is not present in order note info section of checkeout tab_CA");
		s_assert.assertTrue(cscockpitCheckoutTabPage.verifyPaymentBillingAddressSectionInCheckoutTab(), "Billing address section is not present in order note info section of checkeout tab_CA");
		s_assert.assertTrue(cscockpitCheckoutTabPage.verifyPaymentAmountSectionInCheckoutTab(), "Payment amount section is not present in order note info section of checkeout tab_CA");
		s_assert.assertTrue(cscockpitCheckoutTabPage.verifyCreditCardCv2SectionInCheckoutTab(), "Credit Card Cv2 section is not present in order note info section of checkeout tab_CA");
		s_assert.assertTrue(cscockpitCheckoutTabPage.verifyUseThisCardBtnInCheckoutTab(), "Use this card button is not present in order note info section of checkeout tab_CA");
		//Place Order Section
		s_assert.assertTrue(cscockpitCheckoutTabPage.verifyPlaceOrderBtnIsPresentInCheckoutTab(), "Place order button is not present in order note info section of checkeout tab_CA");
		s_assert.assertTrue(cscockpitCheckoutTabPage.verifyPerformSOOBtnIsPresentInCheckoutTab(), "Perform SOO button is not present in order note info section of checkeout tab_CA");
		s_assert.assertTrue(cscockpitCheckoutTabPage.verifyTestOrderChkBoxIsPresentInCheckoutTab(), "Test order checkbox is not present in order note info section of checkeout tab_CA");
		s_assert.assertTrue(cscockpitCheckoutTabPage.verifyDoNotShipChkBoxIsPresentInCheckoutTab(), "Do Not Ship checkbox is not present in order note info section of checkeout tab_CA");
		s_assert.assertAll();
	}


}
