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
import com.rf.pages.website.storeFront.StoreFrontConsultantPage;
import com.rf.pages.website.storeFront.StoreFrontHomePage;
import com.rf.pages.website.storeFront.StoreFrontOrdersPage;
import com.rf.pages.website.storeFront.StoreFrontPCUserPage;
import com.rf.pages.website.storeFront.StoreFrontRCUserPage;
import com.rf.pages.website.storeFront.StoreFrontUpdateCartPage;
import com.rf.test.website.RFWebsiteBaseTest;


public class TaxVerificationTest extends RFWebsiteBaseTest{
	private static final Logger logger = LogManager
			.getLogger(TaxVerificationTest.class.getName());

	//-------------------------------------------------Pages---------------------------------------------------------
	private CSCockpitLoginPage cscockpitLoginPage;	
	private CSCockpitAutoshipSearchTabPage cscockpitAutoshipSearchTabPage;
	private CSCockpitCheckoutTabPage cscockpitCheckoutTabPage;
	private CSCockpitCustomerSearchTabPage cscockpitCustomerSearchTabPage;
	private CSCockpitCustomerTabPage cscockpitCustomerTabPage;
	private CSCockpitOrderSearchTabPage cscockpitOrderSearchTabPage;
	private CSCockpitAutoshipTemplateUpdateTabPage cscockpitAutoshipTemplateUpdateTabPage;
	private CSCockpitOrderTabPage cscockpitOrderTabPage;
	private CSCockpitCartTabPage cscockpitCartTabPage;
	private CSCockpitAutoshipTemplateTabPage cscockpitAutoshipTemplateTabPage;
	private CSCockpitCommitTaxTabPage cscockpitCommitTaxTabPage;
	private CSCockpitAutoshipCartTabPage cscockpitAutoshipCartTabPage;
	private StoreFrontHomePage storeFrontHomePage; 
	private StoreFrontConsultantPage storeFrontConsultantPage;
	private StoreFrontOrdersPage storeFrontOrdersPage;
	private StoreFrontPCUserPage storeFrontPCUserPage;
	private StoreFrontRCUserPage storeFrontRCUserPage;	
	private StoreFrontUpdateCartPage storeFrontUpdateCartPage;

	//-----------------------------------------------------------------------------------------------------------------

	public TaxVerificationTest() {
		cscockpitLoginPage = new CSCockpitLoginPage(driver);
		cscockpitAutoshipSearchTabPage = new CSCockpitAutoshipSearchTabPage(driver);
		cscockpitCheckoutTabPage = new CSCockpitCheckoutTabPage(driver);
		cscockpitCustomerSearchTabPage = new CSCockpitCustomerSearchTabPage(driver);
		cscockpitCustomerTabPage = new CSCockpitCustomerTabPage(driver);
		cscockpitOrderSearchTabPage = new CSCockpitOrderSearchTabPage(driver);
		cscockpitAutoshipTemplateUpdateTabPage = new CSCockpitAutoshipTemplateUpdateTabPage(driver);
		cscockpitOrderTabPage = new CSCockpitOrderTabPage(driver);
		cscockpitCartTabPage = new CSCockpitCartTabPage(driver);
		cscockpitCommitTaxTabPage = new CSCockpitCommitTaxTabPage(driver);
		cscockpitAutoshipTemplateTabPage = new CSCockpitAutoshipTemplateTabPage(driver);
		cscockpitAutoshipCartTabPage = new CSCockpitAutoshipCartTabPage(driver);
		storeFrontHomePage = new StoreFrontHomePage(driver);
		storeFrontConsultantPage = new StoreFrontConsultantPage(driver);
		storeFrontOrdersPage = new StoreFrontOrdersPage(driver);
		storeFrontPCUserPage = new StoreFrontPCUserPage(driver);
		storeFrontRCUserPage = new StoreFrontRCUserPage(driver);
		storeFrontUpdateCartPage = new StoreFrontUpdateCartPage(driver);
	}

	private String RFO_DB = null;

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
		driver.get(driver.getStoreFrontURL()+"/"+driver.getCountry());
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
		//Place an Adhoc Order
		storeFrontConsultantPage.hoverOnShopLinkAndClickAllProductsLinksAfterLogin();
		storeFrontUpdateCartPage.clickAddToBagButtonWithoutFilter();
		storeFrontUpdateCartPage.clickOnCheckoutButton();
		storeFrontUpdateCartPage.clickOnShippingAddressNextStepBtn();
		storeFrontUpdateCartPage.clickOnBillingNextStepBtn();
		storeFrontUpdateCartPage.clickPlaceOrderBtn();
		String orderNumber = storeFrontUpdateCartPage.getOrderNumberAfterPlaceOrder();
		s_assert.assertTrue(storeFrontUpdateCartPage.verifyOrderPlacedConfirmationMessage(), "Order has been not placed successfully");
		storeFrontConsultantPage = storeFrontUpdateCartPage.clickOnRodanAndFieldsLogo();
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
		s_assert.assertAll();
	}

	//Hybris Project-1534:To verify the Canada tax for British Columbia for Preferred Customer Order
	@Test
	public void testVerifyCanadaTaxForBritishColumbiaForPCOrder_1534(){
		int randomNumber=CommonUtils.getRandomNum(10000, 1000000);
		String randomCustomerSequenceNumber = null;
		String randomProductSequenceNumber = null;
		String SKUValue = null;
		String attendentFirstName=TestConstants.FIRST_NAME+randomNumber;
		String attendeeLastName = TestConstants.LAST_NAME+randomNumber;
		String  addressLine=TestConstants.ADDRESS_LINE_1_CA;
		String city=TestConstants.CITY_CA;
		String postalCode=TestConstants.POSTAL_CODE_CA;
		String Country=TestConstants.COUNTRY_DD_VALUE_CA;
		String Province=TestConstants.PROVINCE_BRITISH_COULMBIA;
		String phoneNumber=TestConstants.PHONE_NUMBER_CA;
		double gstFromUI = 0.00;
		double calculatedGstAmount = 0.00;
		double pstFromUI = 0.00;
		double calculatedPstAmount = 0.00;
		driver.get(driver.getCSCockpitURL());
		cscockpitCustomerSearchTabPage = cscockpitLoginPage.clickLoginBtn();
		cscockpitCustomerSearchTabPage.selectCustomerTypeFromDropDownInCustomerSearchTab("PC");
		cscockpitCustomerSearchTabPage.selectCountryFromDropDownInCustomerSearchTab("Canada");
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
		cscockpitCustomerTabPage.enterShippingInfoInAddNewPaymentProfilePopupWithoutSaveBtn(attendentFirstName, attendeeLastName, addressLine, city, postalCode, Country, Province, phoneNumber);
		cscockpitCheckoutTabPage.clickOnCreateNewAddressButtonInAutoshipTemplateTabPage();
		cscockpitCheckoutTabPage.clickUseEnteredAddressOfCreateNewAddressShippingAddressPopup();
		cscockpitCheckoutTabPage.clickAddNewPaymentAddressInCheckoutTab();
		cscockpitCheckoutTabPage.enterBillingInfo();
		cscockpitCheckoutTabPage.clickSaveAddNewPaymentProfilePopUP();
		cscockpitCheckoutTabPage.enterCVVValueInCheckoutTab(TestConstants.SECURITY_CODE);
		cscockpitCheckoutTabPage.clickUseThisCardBtnInCheckoutTab();
		cscockpitCheckoutTabPage.clickPlaceOrderButtonInCheckoutTab();
		double subTotal = cscockpitOrderTabPage.getSubTotalFromUI();
		double deliveryCost = cscockpitOrderTabPage.getDeliveryCostFromUI();
		double handlingCost = cscockpitOrderTabPage.getHandlingCostFromUI();
		if(cscockpitCheckoutTabPage.isGSTTaxPresentInUI()==true){
			gstFromUI = cscockpitOrderTabPage.getGstAmountFromUI();
			calculatedGstAmount = cscockpitOrderTabPage.calculateAmountAccordingToPercent(subTotal, deliveryCost, handlingCost, TestConstants.FIVE_PERCENT_TAX);
			s_assert.assertTrue(cscockpitOrderTabPage.validateGstAmountOnUI(gstFromUI,calculatedGstAmount),"GST Amount on UI is "+gstFromUI+" and calculated GST Amount is "+calculatedGstAmount+"");
		}else{
			logger.info("GST NOT PRESENT FOR THIS ORDER");
		}
		if(cscockpitOrderTabPage.isPstTaxPresentInUI() == true){
			pstFromUI = cscockpitOrderTabPage.getPstAmountFromUI();
			calculatedPstAmount = cscockpitOrderTabPage.calculateAmountAccordingToPercent(subTotal, deliveryCost, handlingCost, TestConstants.SEVEN_PERCENT_TAX);
			s_assert.assertTrue(cscockpitOrderTabPage.validatePstAmountOnUI(pstFromUI,calculatedPstAmount),"PST Amount on UI is "+pstFromUI+" and calculated GST Amount is "+calculatedPstAmount);
		}else{
			logger.info("PST NOT PRESENT FOR THIS ORDER");
		}

		s_assert.assertAll();
	}

	//Hybris Project-1535:To verify the Canada tax for Alberta for Preferred Customer Order
	@Test
	public void testVerifyCanadaTaxForAlbertaForPCOrder_1535(){
		int randomNumber=CommonUtils.getRandomNum(10000, 1000000);
		String randomCustomerSequenceNumber = null;
		String randomProductSequenceNumber = null;
		String SKUValue = null;
		String attendentFirstName=TestConstants.FIRST_NAME+randomNumber;
		String attendeeLastName = TestConstants.LAST_NAME+randomNumber;
		String  addressLine=TestConstants.ADDRESS_LINE_1_CA;
		String city=TestConstants.CITY_CA;
		String postalCode=TestConstants.POSTAL_CODE_CA;
		String Country=TestConstants.COUNTRY_DD_VALUE_CA;
		String Province=TestConstants.PROVINCE_CA;
		String phoneNumber=TestConstants.PHONE_NUMBER_CA;
		double gstFromUI = 0.00;
		double calculatedGstAmount = 0.00;
		double pstFromUI = 0.00;
		double calculatedPstAmount = 0.00;
		driver.get(driver.getCSCockpitURL());
		cscockpitCustomerSearchTabPage = cscockpitLoginPage.clickLoginBtn();
		cscockpitCustomerSearchTabPage.selectCustomerTypeFromDropDownInCustomerSearchTab("PC");
		cscockpitCustomerSearchTabPage.selectCountryFromDropDownInCustomerSearchTab("Canada");
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
		cscockpitCustomerTabPage.enterShippingInfoInAddNewPaymentProfilePopupWithoutSaveBtn(attendentFirstName, attendeeLastName, addressLine, city, postalCode, Country, Province, phoneNumber);
		cscockpitCheckoutTabPage.clickOnCreateNewAddressButtonInAutoshipTemplateTabPage();
		cscockpitCheckoutTabPage.clickUseEnteredAddressOfCreateNewAddressShippingAddressPopup();
		cscockpitCheckoutTabPage.clickAddNewPaymentAddressInCheckoutTab();
		cscockpitCheckoutTabPage.enterBillingInfo();
		cscockpitCheckoutTabPage.clickSaveAddNewPaymentProfilePopUP();
		cscockpitCheckoutTabPage.enterCVVValueInCheckoutTab(TestConstants.SECURITY_CODE);
		cscockpitCheckoutTabPage.clickUseThisCardBtnInCheckoutTab();
		cscockpitCheckoutTabPage.clickPlaceOrderButtonInCheckoutTab();
		double subTotal = cscockpitOrderTabPage.getSubTotalFromUI();
		double deliveryCost = cscockpitOrderTabPage.getDeliveryCostFromUI();
		double handlingCost = cscockpitOrderTabPage.getHandlingCostFromUI();
		if(cscockpitCheckoutTabPage.isGSTTaxPresentInUI()==true){
			gstFromUI = cscockpitOrderTabPage.getGstAmountFromUI();
			calculatedGstAmount = cscockpitOrderTabPage.calculateAmountAccordingToPercent(subTotal, deliveryCost, handlingCost, TestConstants.FIVE_PERCENT_TAX);
			s_assert.assertTrue(cscockpitOrderTabPage.validateGstAmountOnUI(gstFromUI,calculatedGstAmount),"GST Amount on UI is "+gstFromUI+" and calculated GST Amount is "+calculatedGstAmount+"");
		}else{
			logger.info("GST NOT PRESENT FOR THIS ORDER");
		}
		if(cscockpitOrderTabPage.isPstTaxPresentInUI() == true){
			pstFromUI = cscockpitOrderTabPage.getPstAmountFromUI();
			calculatedPstAmount = cscockpitOrderTabPage.calculateAmountAccordingToPercent(subTotal, deliveryCost, handlingCost, TestConstants.SEVEN_PERCENT_TAX);
			s_assert.assertTrue(cscockpitOrderTabPage.validatePstAmountOnUI(pstFromUI,calculatedPstAmount),"PST Amount on UI is "+pstFromUI+" and calculated GST Amount is "+calculatedPstAmount);
		}else{
			logger.info("PST NOT PRESENT FOR THIS ORDER");
		}

		s_assert.assertAll();
	}

	//Hybris Project-1536:To verify the Canada tax for saskatchewan for Preferred Customer Order
	@Test
	public void testVerifyCanadaTaxForSaskatchewanForPCOrder_1536(){
		int randomNumber=CommonUtils.getRandomNum(10000, 1000000);
		String randomCustomerSequenceNumber = null;
		String randomProductSequenceNumber = null;
		String SKUValue = null;
		String attendentFirstName=TestConstants.FIRST_NAME+randomNumber;
		String attendeeLastName = TestConstants.LAST_NAME+randomNumber;
		String  addressLine=TestConstants.ADDRESS_LINE_1_CA;
		String city=TestConstants.CITY_CA;
		String postalCode=TestConstants.POSTAL_CODE_CA;
		String Country=TestConstants.COUNTRY_DD_VALUE_CA;
		String Province=TestConstants.PROVINCE_SASKATCHEWAN;
		String phoneNumber=TestConstants.PHONE_NUMBER_CA;
		double gstFromUI = 0.00;
		double calculatedGstAmount = 0.00;
		double pstFromUI = 0.00;
		double calculatedPstAmount = 0.00;
		driver.get(driver.getCSCockpitURL());
		cscockpitCustomerSearchTabPage = cscockpitLoginPage.clickLoginBtn();
		cscockpitCustomerSearchTabPage.selectCustomerTypeFromDropDownInCustomerSearchTab("PC");
		cscockpitCustomerSearchTabPage.selectCountryFromDropDownInCustomerSearchTab("Canada");
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
		cscockpitCustomerTabPage.enterShippingInfoInAddNewPaymentProfilePopupWithoutSaveBtn(attendentFirstName, attendeeLastName, addressLine, city, postalCode, Country, Province, phoneNumber);
		cscockpitCheckoutTabPage.clickOnCreateNewAddressButtonInAutoshipTemplateTabPage();
		cscockpitCheckoutTabPage.clickUseEnteredAddressOfCreateNewAddressShippingAddressPopup();
		cscockpitCheckoutTabPage.clickAddNewPaymentAddressInCheckoutTab();
		cscockpitCheckoutTabPage.enterBillingInfo();
		cscockpitCheckoutTabPage.clickSaveAddNewPaymentProfilePopUP();
		cscockpitCheckoutTabPage.enterCVVValueInCheckoutTab(TestConstants.SECURITY_CODE);
		cscockpitCheckoutTabPage.clickUseThisCardBtnInCheckoutTab();
		cscockpitCheckoutTabPage.clickPlaceOrderButtonInCheckoutTab();
		double subTotal = cscockpitOrderTabPage.getSubTotalFromUI();
		double deliveryCost = cscockpitOrderTabPage.getDeliveryCostFromUI();
		double handlingCost = cscockpitOrderTabPage.getHandlingCostFromUI();
		if(cscockpitCheckoutTabPage.isGSTTaxPresentInUI()==true){
			gstFromUI = cscockpitOrderTabPage.getGstAmountFromUI();
			calculatedGstAmount = cscockpitOrderTabPage.calculateAmountAccordingToPercent(subTotal, deliveryCost, handlingCost, TestConstants.FIVE_PERCENT_TAX);
			s_assert.assertTrue(cscockpitOrderTabPage.validateGstAmountOnUI(gstFromUI,calculatedGstAmount),"GST Amount on UI is "+gstFromUI+" and calculated GST Amount is "+calculatedGstAmount+"");
		}else{
			logger.info("GST NOT PRESENT FOR THIS ORDER");
		}
		if(cscockpitOrderTabPage.isPstTaxPresentInUI() == true){
			pstFromUI = cscockpitOrderTabPage.getPstAmountFromUI();
			calculatedPstAmount = cscockpitOrderTabPage.calculateAmountAccordingToPercent(subTotal, deliveryCost, handlingCost, TestConstants.FIVE_PERCENT_TAX);
			s_assert.assertTrue(cscockpitOrderTabPage.validatePstAmountOnUI(pstFromUI,calculatedPstAmount),"PST Amount on UI is "+pstFromUI+" and calculated GST Amount is "+calculatedPstAmount);
		}else{
			logger.info("PST NOT PRESENT FOR THIS ORDER");
		}

		s_assert.assertAll();
	}

	//Hybris Project-1537:To verify the Canada tax for Manitoba for Preferred Customer Order
	@Test
	public void testVerifyCanadaTaxForManitobaForPCOrder_1537(){
		int randomNumber=CommonUtils.getRandomNum(10000, 1000000);
		String randomCustomerSequenceNumber = null;
		String randomProductSequenceNumber = null;
		String SKUValue = null;
		String attendentFirstName=TestConstants.FIRST_NAME+randomNumber;
		String attendeeLastName = TestConstants.LAST_NAME+randomNumber;
		String  addressLine=TestConstants.ADDRESS_LINE_1_CA;
		String city=TestConstants.CITY_CA;
		String postalCode=TestConstants.POSTAL_CODE_CA;
		String Country=TestConstants.COUNTRY_DD_VALUE_CA;
		String Province=TestConstants.PROVINCE_MANITOBA;
		String phoneNumber=TestConstants.PHONE_NUMBER_CA;
		double gstFromUI = 0.00;
		double calculatedGstAmount = 0.00;
		double pstFromUI = 0.00;
		double calculatedPstAmount = 0.00;
		driver.get(driver.getCSCockpitURL());
		cscockpitCustomerSearchTabPage = cscockpitLoginPage.clickLoginBtn();
		cscockpitCustomerSearchTabPage.selectCustomerTypeFromDropDownInCustomerSearchTab("PC");
		cscockpitCustomerSearchTabPage.selectCountryFromDropDownInCustomerSearchTab("Canada");
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
		cscockpitCustomerTabPage.enterShippingInfoInAddNewPaymentProfilePopupWithoutSaveBtn(attendentFirstName, attendeeLastName, addressLine, city, postalCode, Country, Province, phoneNumber);
		cscockpitCheckoutTabPage.clickOnCreateNewAddressButtonInAutoshipTemplateTabPage();
		cscockpitCheckoutTabPage.clickUseEnteredAddressOfCreateNewAddressShippingAddressPopup();
		cscockpitCheckoutTabPage.clickAddNewPaymentAddressInCheckoutTab();
		cscockpitCheckoutTabPage.enterBillingInfo();
		cscockpitCheckoutTabPage.clickSaveAddNewPaymentProfilePopUP();
		cscockpitCheckoutTabPage.enterCVVValueInCheckoutTab(TestConstants.SECURITY_CODE);
		cscockpitCheckoutTabPage.clickUseThisCardBtnInCheckoutTab();
		cscockpitCheckoutTabPage.clickPlaceOrderButtonInCheckoutTab();
		double subTotal = cscockpitOrderTabPage.getSubTotalFromUI();
		double deliveryCost = cscockpitOrderTabPage.getDeliveryCostFromUI();
		double handlingCost = cscockpitOrderTabPage.getHandlingCostFromUI();
		if(cscockpitCheckoutTabPage.isGSTTaxPresentInUI()==true){
			gstFromUI = cscockpitOrderTabPage.getGstAmountFromUI();
			calculatedGstAmount = cscockpitOrderTabPage.calculateAmountAccordingToPercent(subTotal, deliveryCost, handlingCost, TestConstants.FIVE_PERCENT_TAX);
			s_assert.assertTrue(cscockpitOrderTabPage.validateGstAmountOnUI(gstFromUI,calculatedGstAmount),"GST Amount on UI is "+gstFromUI+" and calculated GST Amount is "+calculatedGstAmount+"");
		}else{
			logger.info("GST NOT PRESENT FOR THIS ORDER");
		}
		if(cscockpitOrderTabPage.isPstTaxPresentInUI() == true){
			pstFromUI = cscockpitOrderTabPage.getPstAmountFromUI();
			calculatedPstAmount = cscockpitOrderTabPage.calculateAmountAccordingToPercent(subTotal, deliveryCost, handlingCost, TestConstants.EIGHT_PERCENT_TAX);
			s_assert.assertTrue(cscockpitOrderTabPage.validatePstAmountOnUI(pstFromUI,calculatedPstAmount),"PST Amount on UI is "+pstFromUI+" and calculated GST Amount is "+calculatedPstAmount);
		}else{
			logger.info("PST NOT PRESENT FOR THIS ORDER");
		}

		s_assert.assertAll();
	}

	//Hybris Project-1538:To verify the Canada tax for Ontario for Preferred Customer Order
	@Test
	public void testVerifyCanadaTaxForOntarioForPCOrder_1538(){
		int randomNumber=CommonUtils.getRandomNum(10000, 1000000);
		String randomCustomerSequenceNumber = null;
		String randomProductSequenceNumber = null;
		String SKUValue = null;
		String attendentFirstName=TestConstants.FIRST_NAME+randomNumber;
		String attendeeLastName = TestConstants.LAST_NAME+randomNumber;
		String  addressLine=TestConstants.ADDRESS_LINE_1_CA;
		String city=TestConstants.CITY_CA;
		String postalCode=TestConstants.POSTAL_CODE_CA;
		String Country=TestConstants.COUNTRY_DD_VALUE_CA;
		String Province=TestConstants.PROVINCE_ONTARIO;
		String phoneNumber=TestConstants.PHONE_NUMBER_CA;
		double gstFromUI = 0.00;
		double calculatedGstAmount = 0.00;
		double pstFromUI = 0.00;
		double calculatedPstAmount = 0.00;
		driver.get(driver.getCSCockpitURL());
		cscockpitCustomerSearchTabPage = cscockpitLoginPage.clickLoginBtn();
		cscockpitCustomerSearchTabPage.selectCustomerTypeFromDropDownInCustomerSearchTab("PC");
		cscockpitCustomerSearchTabPage.selectCountryFromDropDownInCustomerSearchTab("Canada");
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
		cscockpitCustomerTabPage.enterShippingInfoInAddNewPaymentProfilePopupWithoutSaveBtn(attendentFirstName, attendeeLastName, addressLine, city, postalCode, Country, Province, phoneNumber);
		cscockpitCheckoutTabPage.clickOnCreateNewAddressButtonInAutoshipTemplateTabPage();
		cscockpitCheckoutTabPage.clickUseEnteredAddressOfCreateNewAddressShippingAddressPopup();
		cscockpitCheckoutTabPage.clickAddNewPaymentAddressInCheckoutTab();
		cscockpitCheckoutTabPage.enterBillingInfo();
		cscockpitCheckoutTabPage.clickSaveAddNewPaymentProfilePopUP();
		cscockpitCheckoutTabPage.enterCVVValueInCheckoutTab(TestConstants.SECURITY_CODE);
		cscockpitCheckoutTabPage.clickUseThisCardBtnInCheckoutTab();
		cscockpitCheckoutTabPage.clickPlaceOrderButtonInCheckoutTab();
		double subTotal = cscockpitOrderTabPage.getSubTotalFromUI();
		double deliveryCost = cscockpitOrderTabPage.getDeliveryCostFromUI();
		double handlingCost = cscockpitOrderTabPage.getHandlingCostFromUI();
		if(cscockpitCheckoutTabPage.isGSTTaxPresentInUI()==true){
			gstFromUI = cscockpitOrderTabPage.getGstAmountFromUI();
			calculatedGstAmount = cscockpitOrderTabPage.calculateAmountAccordingToPercent(subTotal, deliveryCost, handlingCost, TestConstants.FIVE_PERCENT_TAX);
			s_assert.assertTrue(cscockpitOrderTabPage.validateGstAmountOnUI(gstFromUI,calculatedGstAmount),"GST Amount on UI is "+gstFromUI+" and calculated GST Amount is "+calculatedGstAmount+"");
		}else{
			logger.info("GST NOT PRESENT FOR THIS ORDER");
		}
		if(cscockpitOrderTabPage.isPstTaxPresentInUI() == true){
			pstFromUI = cscockpitOrderTabPage.getPstAmountFromUI();
			calculatedPstAmount = cscockpitOrderTabPage.calculateAmountAccordingToPercent(subTotal, deliveryCost, handlingCost, TestConstants.SEVEN_PERCENT_TAX);
			s_assert.assertTrue(cscockpitOrderTabPage.validatePstAmountOnUI(pstFromUI,calculatedPstAmount),"PST Amount on UI is "+pstFromUI+" and calculated GST Amount is "+calculatedPstAmount);
		}else{
			logger.info("PST NOT PRESENT FOR THIS ORDER");
		}

		s_assert.assertAll();
	}

	//Hybris Project-1539:To verify the Canada tax for Quebec for Preferred Customer Order
	@Test
	public void testVerifyCanadaTaxForQuebecForPCOrder_1539(){
		int randomNumber=CommonUtils.getRandomNum(10000, 1000000);
		String randomCustomerSequenceNumber = null;
		String randomProductSequenceNumber = null;
		String SKUValue = null;
		String attendentFirstName=TestConstants.FIRST_NAME+randomNumber;
		String attendeeLastName = TestConstants.LAST_NAME+randomNumber;
		String  addressLine=TestConstants.ADDRESS_LINE_1_CA;
		String city=TestConstants.CITY_CA;
		String postalCode=TestConstants.POSTAL_CODE_CA;
		String Country=TestConstants.COUNTRY_DD_VALUE_CA;
		String Province=TestConstants.PROVINCE_QUEBEC;
		String phoneNumber=TestConstants.PHONE_NUMBER_CA;
		double gstFromUI = 0.00;
		double calculatedGstAmount = 0.00;
		double pstFromUI = 0.00;
		double calculatedPstAmount = 0.00;
		driver.get(driver.getCSCockpitURL());
		cscockpitCustomerSearchTabPage = cscockpitLoginPage.clickLoginBtn();
		cscockpitCustomerSearchTabPage.selectCustomerTypeFromDropDownInCustomerSearchTab("PC");
		cscockpitCustomerSearchTabPage.selectCountryFromDropDownInCustomerSearchTab("Canada");
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
		cscockpitCustomerTabPage.enterShippingInfoInAddNewPaymentProfilePopupWithoutSaveBtn(attendentFirstName, attendeeLastName, addressLine, city, postalCode, Country, Province, phoneNumber);
		cscockpitCheckoutTabPage.clickOnCreateNewAddressButtonInAutoshipTemplateTabPage();
		cscockpitCheckoutTabPage.clickUseEnteredAddressOfCreateNewAddressShippingAddressPopup();
		cscockpitCheckoutTabPage.clickAddNewPaymentAddressInCheckoutTab();
		cscockpitCheckoutTabPage.enterBillingInfo();
		cscockpitCheckoutTabPage.clickSaveAddNewPaymentProfilePopUP();
		cscockpitCheckoutTabPage.enterCVVValueInCheckoutTab(TestConstants.SECURITY_CODE);
		cscockpitCheckoutTabPage.clickUseThisCardBtnInCheckoutTab();
		cscockpitCheckoutTabPage.clickPlaceOrderButtonInCheckoutTab();
		double subTotal = cscockpitOrderTabPage.getSubTotalFromUI();
		double deliveryCost = cscockpitOrderTabPage.getDeliveryCostFromUI();
		double handlingCost = cscockpitOrderTabPage.getHandlingCostFromUI();
		if(cscockpitCheckoutTabPage.isGSTTaxPresentInUI()==true){
			gstFromUI = cscockpitOrderTabPage.getGstAmountFromUI();
			calculatedGstAmount = cscockpitOrderTabPage.calculateAmountAccordingToPercent(subTotal, deliveryCost, handlingCost, TestConstants.FIVE_PERCENT_TAX);
			s_assert.assertTrue(cscockpitOrderTabPage.validateGstAmountOnUI(gstFromUI,calculatedGstAmount),"GST Amount on UI is "+gstFromUI+" and calculated GST Amount is "+calculatedGstAmount+"");
		}else{
			logger.info("GST NOT PRESENT FOR THIS ORDER");
		}
		if(cscockpitOrderTabPage.isPstTaxPresentInUI() == true){
			pstFromUI = cscockpitOrderTabPage.getPstAmountFromUI();
			calculatedPstAmount = cscockpitOrderTabPage.calculateAmountAccordingToPercent(subTotal, deliveryCost, handlingCost, TestConstants.NINE_PERCENT_TAX);
			s_assert.assertTrue(cscockpitOrderTabPage.validatePstAmountOnUI(pstFromUI,calculatedPstAmount),"PST Amount on UI is "+pstFromUI+" and calculated GST Amount is "+calculatedPstAmount);
		}else{
			logger.info("PST NOT PRESENT FOR THIS ORDER");
		}

		s_assert.assertAll();
	}

	//Hybris Project-1540:To verify the Canada tax for Nova Scotia for Preferred Customer Order
	@Test
	public void testVerifyCanadaTaxForNovaScotiaForPCOrder_1540(){
		int randomNumber=CommonUtils.getRandomNum(10000, 1000000);
		String randomCustomerSequenceNumber = null;
		String randomProductSequenceNumber = null;
		String SKUValue = null;
		String attendentFirstName=TestConstants.FIRST_NAME+randomNumber;
		String attendeeLastName = TestConstants.LAST_NAME+randomNumber;
		String  addressLine=TestConstants.ADDRESS_LINE_1_CA;
		String city=TestConstants.CITY_CA;
		String postalCode=TestConstants.POSTAL_CODE_CA;
		String Country=TestConstants.COUNTRY_DD_VALUE_CA;
		String Province=TestConstants.PROVINCE_NOVA_SCOTIA;
		String phoneNumber=TestConstants.PHONE_NUMBER_CA;
		double gstFromUI = 0.00;
		double calculatedGstAmount = 0.00;
		double pstFromUI = 0.00;
		double calculatedPstAmount = 0.00;
		driver.get(driver.getCSCockpitURL());
		cscockpitCustomerSearchTabPage = cscockpitLoginPage.clickLoginBtn();
		cscockpitCustomerSearchTabPage.selectCustomerTypeFromDropDownInCustomerSearchTab("PC");
		cscockpitCustomerSearchTabPage.selectCountryFromDropDownInCustomerSearchTab("Canada");
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
		cscockpitCustomerTabPage.enterShippingInfoInAddNewPaymentProfilePopupWithoutSaveBtn(attendentFirstName, attendeeLastName, addressLine, city, postalCode, Country, Province, phoneNumber);
		cscockpitCheckoutTabPage.clickOnCreateNewAddressButtonInAutoshipTemplateTabPage();
		cscockpitCheckoutTabPage.clickUseEnteredAddressOfCreateNewAddressShippingAddressPopup();
		cscockpitCheckoutTabPage.clickAddNewPaymentAddressInCheckoutTab();
		cscockpitCheckoutTabPage.enterBillingInfo();
		cscockpitCheckoutTabPage.clickSaveAddNewPaymentProfilePopUP();
		cscockpitCheckoutTabPage.enterCVVValueInCheckoutTab(TestConstants.SECURITY_CODE);
		cscockpitCheckoutTabPage.clickUseThisCardBtnInCheckoutTab();
		cscockpitCheckoutTabPage.clickPlaceOrderButtonInCheckoutTab();
		double subTotal = cscockpitOrderTabPage.getSubTotalFromUI();
		double deliveryCost = cscockpitOrderTabPage.getDeliveryCostFromUI();
		double handlingCost = cscockpitOrderTabPage.getHandlingCostFromUI();
		if(cscockpitCheckoutTabPage.isGSTTaxPresentInUI()==true){
			gstFromUI = cscockpitOrderTabPage.getGstAmountFromUI();
			calculatedGstAmount = cscockpitOrderTabPage.calculateAmountAccordingToPercent(subTotal, deliveryCost, handlingCost, TestConstants.FIVE_PERCENT_TAX);
			s_assert.assertTrue(cscockpitOrderTabPage.validateGstAmountOnUI(gstFromUI,calculatedGstAmount),"GST Amount on UI is "+gstFromUI+" and calculated GST Amount is "+calculatedGstAmount+"");
		}else{
			logger.info("GST NOT PRESENT FOR THIS ORDER");
		}
		if(cscockpitOrderTabPage.isPstTaxPresentInUI() == true){
			pstFromUI = cscockpitOrderTabPage.getPstAmountFromUI();
			calculatedPstAmount = cscockpitOrderTabPage.calculateAmountAccordingToPercent(subTotal, deliveryCost, handlingCost, TestConstants.SEVEN_PERCENT_TAX);
			s_assert.assertTrue(cscockpitOrderTabPage.validatePstAmountOnUI(pstFromUI,calculatedPstAmount),"PST Amount on UI is "+pstFromUI+" and calculated GST Amount is "+calculatedPstAmount);
		}else{
			logger.info("PST NOT PRESENT FOR THIS ORDER");
		}

		s_assert.assertAll();
	}

	//Hybris Project-1541:To verify the Canada tax for New Brunswick for Preferred Customer Order
	@Test
	public void testVerifyCanadaTaxForNewBrunswickForPCOrder_1541(){
		int randomNumber=CommonUtils.getRandomNum(10000, 1000000);
		String randomCustomerSequenceNumber = null;
		String randomProductSequenceNumber = null;
		String SKUValue = null;
		String attendentFirstName=TestConstants.FIRST_NAME+randomNumber;
		String attendeeLastName = TestConstants.LAST_NAME+randomNumber;
		String  addressLine=TestConstants.ADDRESS_LINE_1_CA;
		String city=TestConstants.CITY_CA;
		String postalCode=TestConstants.POSTAL_CODE_CA;
		String Country=TestConstants.COUNTRY_DD_VALUE_CA;
		String Province=TestConstants.PROVINCE_NEW_BRUNSWICK;
		String phoneNumber=TestConstants.PHONE_NUMBER_CA;
		double gstFromUI = 0.00;
		double calculatedGstAmount = 0.00;
		double pstFromUI = 0.00;
		double calculatedPstAmount = 0.00;
		driver.get(driver.getCSCockpitURL());
		cscockpitCustomerSearchTabPage = cscockpitLoginPage.clickLoginBtn();
		cscockpitCustomerSearchTabPage.selectCustomerTypeFromDropDownInCustomerSearchTab("PC");
		cscockpitCustomerSearchTabPage.selectCountryFromDropDownInCustomerSearchTab("Canada");
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
		cscockpitCustomerTabPage.enterShippingInfoInAddNewPaymentProfilePopupWithoutSaveBtn(attendentFirstName, attendeeLastName, addressLine, city, postalCode, Country, Province, phoneNumber);
		cscockpitCheckoutTabPage.clickOnCreateNewAddressButtonInAutoshipTemplateTabPage();
		cscockpitCheckoutTabPage.clickUseEnteredAddressOfCreateNewAddressShippingAddressPopup();
		cscockpitCheckoutTabPage.clickAddNewPaymentAddressInCheckoutTab();
		cscockpitCheckoutTabPage.enterBillingInfo();
		cscockpitCheckoutTabPage.clickSaveAddNewPaymentProfilePopUP();
		cscockpitCheckoutTabPage.enterCVVValueInCheckoutTab(TestConstants.SECURITY_CODE);
		cscockpitCheckoutTabPage.clickUseThisCardBtnInCheckoutTab();
		cscockpitCheckoutTabPage.clickPlaceOrderButtonInCheckoutTab();
		double subTotal = cscockpitOrderTabPage.getSubTotalFromUI();
		double deliveryCost = cscockpitOrderTabPage.getDeliveryCostFromUI();
		double handlingCost = cscockpitOrderTabPage.getHandlingCostFromUI();
		if(cscockpitCheckoutTabPage.isGSTTaxPresentInUI()==true){
			gstFromUI = cscockpitOrderTabPage.getGstAmountFromUI();
			calculatedGstAmount = cscockpitOrderTabPage.calculateAmountAccordingToPercent(subTotal, deliveryCost, handlingCost, TestConstants.FIVE_PERCENT_TAX);
			s_assert.assertTrue(cscockpitOrderTabPage.validateGstAmountOnUI(gstFromUI,calculatedGstAmount),"GST Amount on UI is "+gstFromUI+" and calculated GST Amount is "+calculatedGstAmount+"");
		}else{
			logger.info("GST NOT PRESENT FOR THIS ORDER");
		}
		if(cscockpitOrderTabPage.isPstTaxPresentInUI() == true){
			pstFromUI = cscockpitOrderTabPage.getPstAmountFromUI();
			calculatedPstAmount = cscockpitOrderTabPage.calculateAmountAccordingToPercent(subTotal, deliveryCost, handlingCost, TestConstants.SEVEN_PERCENT_TAX);
			s_assert.assertTrue(cscockpitOrderTabPage.validatePstAmountOnUI(pstFromUI,calculatedPstAmount),"PST Amount on UI is "+pstFromUI+" and calculated GST Amount is "+calculatedPstAmount);
		}else{
			logger.info("PST NOT PRESENT FOR THIS ORDER");
		}
		s_assert.assertAll();
	}

	//Hybris Project-1542:To verify the Canada tax for Prince Edward Island for Preferred Customer Order
	@Test
	public void testVerifyCanadaTaxForPrinceEdwardIslandForPCOrder_1542(){
		int randomNumber=CommonUtils.getRandomNum(10000, 1000000);
		String randomCustomerSequenceNumber = null;
		String randomProductSequenceNumber = null;
		String SKUValue = null;
		String attendentFirstName=TestConstants.FIRST_NAME+randomNumber;
		String attendeeLastName = TestConstants.LAST_NAME+randomNumber;
		String  addressLine=TestConstants.ADDRESS_LINE_1_CA;
		String city=TestConstants.CITY_CA;
		String postalCode=TestConstants.POSTAL_CODE_CA;
		String Country=TestConstants.COUNTRY_DD_VALUE_CA;
		String Province=TestConstants.PROVINCE_PRINCE_EDWARD_ISLAND;
		String phoneNumber=TestConstants.PHONE_NUMBER_CA;
		double gstFromUI = 0.00;
		double calculatedGstAmount = 0.00;
		double pstFromUI = 0.00;
		double calculatedPstAmount = 0.00;
		driver.get(driver.getCSCockpitURL());
		cscockpitCustomerSearchTabPage = cscockpitLoginPage.clickLoginBtn();
		cscockpitCustomerSearchTabPage.selectCustomerTypeFromDropDownInCustomerSearchTab("PC");
		cscockpitCustomerSearchTabPage.selectCountryFromDropDownInCustomerSearchTab("Canada");
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
		cscockpitCustomerTabPage.enterShippingInfoInAddNewPaymentProfilePopupWithoutSaveBtn(attendentFirstName, attendeeLastName, addressLine, city, postalCode, Country, Province, phoneNumber);
		cscockpitCheckoutTabPage.clickOnCreateNewAddressButtonInAutoshipTemplateTabPage();
		cscockpitCheckoutTabPage.clickUseEnteredAddressOfCreateNewAddressShippingAddressPopup();
		cscockpitCheckoutTabPage.clickAddNewPaymentAddressInCheckoutTab();
		cscockpitCheckoutTabPage.enterBillingInfo();
		cscockpitCheckoutTabPage.clickSaveAddNewPaymentProfilePopUP();
		cscockpitCheckoutTabPage.enterCVVValueInCheckoutTab(TestConstants.SECURITY_CODE);
		cscockpitCheckoutTabPage.clickUseThisCardBtnInCheckoutTab();
		cscockpitCheckoutTabPage.clickPlaceOrderButtonInCheckoutTab();
		double subTotal = cscockpitOrderTabPage.getSubTotalFromUI();
		double deliveryCost = cscockpitOrderTabPage.getDeliveryCostFromUI();
		double handlingCost = cscockpitOrderTabPage.getHandlingCostFromUI();
		if(cscockpitCheckoutTabPage.isGSTTaxPresentInUI()==true){
			gstFromUI = cscockpitOrderTabPage.getGstAmountFromUI();
			calculatedGstAmount = cscockpitOrderTabPage.calculateAmountAccordingToPercent(subTotal, deliveryCost, handlingCost, TestConstants.FIVE_PERCENT_TAX);
			s_assert.assertTrue(cscockpitOrderTabPage.validateGstAmountOnUI(gstFromUI,calculatedGstAmount),"GST Amount on UI is "+gstFromUI+" and calculated GST Amount is "+calculatedGstAmount+"");
		}else{
			logger.info("GST NOT PRESENT FOR THIS ORDER");
		}
		if(cscockpitOrderTabPage.isPstTaxPresentInUI() == true){
			pstFromUI = cscockpitOrderTabPage.getPstAmountFromUI();
			calculatedPstAmount = cscockpitOrderTabPage.calculateAmountAccordingToPercent(subTotal, deliveryCost, handlingCost, TestConstants.SEVEN_PERCENT_TAX);
			s_assert.assertTrue(cscockpitOrderTabPage.validatePstAmountOnUI(pstFromUI,calculatedPstAmount),"PST Amount on UI is "+pstFromUI+" and calculated GST Amount is "+calculatedPstAmount);
		}else{
			logger.info("PST NOT PRESENT FOR THIS ORDER");
		}
		s_assert.assertAll();
	}

	//Hybris Project-1543:To verify the Canada tax for New foundland for Preferred Customer Order
	@Test
	public void testVerifyCanadaTaxForNewFoundlandForPCOrder_1543(){
		int randomNumber=CommonUtils.getRandomNum(10000, 1000000);
		String randomCustomerSequenceNumber = null;
		String randomProductSequenceNumber = null;
		String SKUValue = null;
		String attendentFirstName=TestConstants.FIRST_NAME+randomNumber;
		String attendeeLastName = TestConstants.LAST_NAME+randomNumber;
		String  addressLine=TestConstants.ADDRESS_LINE_1_CA;
		String city=TestConstants.CITY_CA;
		String postalCode=TestConstants.POSTAL_CODE_CA;
		String Country=TestConstants.COUNTRY_DD_VALUE_CA;
		String Province=TestConstants.PROVINCE_NEW_FOUNDLAND_AND_LABRADOR;
		String phoneNumber=TestConstants.PHONE_NUMBER_CA;
		double gstFromUI = 0.00;
		double calculatedGstAmount = 0.00;
		double pstFromUI = 0.00;
		double calculatedPstAmount = 0.00;
		driver.get(driver.getCSCockpitURL());
		cscockpitCustomerSearchTabPage = cscockpitLoginPage.clickLoginBtn();
		cscockpitCustomerSearchTabPage.selectCustomerTypeFromDropDownInCustomerSearchTab("PC");
		cscockpitCustomerSearchTabPage.selectCountryFromDropDownInCustomerSearchTab("Canada");
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
		cscockpitCustomerTabPage.enterShippingInfoInAddNewPaymentProfilePopupWithoutSaveBtn(attendentFirstName, attendeeLastName, addressLine, city, postalCode, Country, Province, phoneNumber);
		cscockpitCheckoutTabPage.clickOnCreateNewAddressButtonInAutoshipTemplateTabPage();
		cscockpitCheckoutTabPage.clickUseEnteredAddressOfCreateNewAddressShippingAddressPopup();
		cscockpitCheckoutTabPage.clickAddNewPaymentAddressInCheckoutTab();
		cscockpitCheckoutTabPage.enterBillingInfo();
		cscockpitCheckoutTabPage.clickSaveAddNewPaymentProfilePopUP();
		cscockpitCheckoutTabPage.enterCVVValueInCheckoutTab(TestConstants.SECURITY_CODE);
		cscockpitCheckoutTabPage.clickUseThisCardBtnInCheckoutTab();
		cscockpitCheckoutTabPage.clickPlaceOrderButtonInCheckoutTab();
		double subTotal = cscockpitOrderTabPage.getSubTotalFromUI();
		double deliveryCost = cscockpitOrderTabPage.getDeliveryCostFromUI();
		double handlingCost = cscockpitOrderTabPage.getHandlingCostFromUI();
		if(cscockpitCheckoutTabPage.isGSTTaxPresentInUI()==true){
			gstFromUI = cscockpitOrderTabPage.getGstAmountFromUI();
			calculatedGstAmount = cscockpitOrderTabPage.calculateAmountAccordingToPercent(subTotal, deliveryCost, handlingCost, TestConstants.FIVE_PERCENT_TAX);
			s_assert.assertTrue(cscockpitOrderTabPage.validateGstAmountOnUI(gstFromUI,calculatedGstAmount),"GST Amount on UI is "+gstFromUI+" and calculated GST Amount is "+calculatedGstAmount+"");
		}else{
			logger.info("GST NOT PRESENT FOR THIS ORDER");
		}
		if(cscockpitOrderTabPage.isPstTaxPresentInUI() == true){
			pstFromUI = cscockpitOrderTabPage.getPstAmountFromUI();
			calculatedPstAmount = cscockpitOrderTabPage.calculateAmountAccordingToPercent(subTotal, deliveryCost, handlingCost, TestConstants.SEVEN_PERCENT_TAX);
			s_assert.assertTrue(cscockpitOrderTabPage.validatePstAmountOnUI(pstFromUI,calculatedPstAmount),"PST Amount on UI is "+pstFromUI+" and calculated GST Amount is "+calculatedPstAmount);
		}else{
			logger.info("PST NOT PRESENT FOR THIS ORDER");
		}
		s_assert.assertAll();
	}

	//Hybris Project-1544:To verify the Canada tax for Yukon for Preferred Customer Order
	@Test
	public void testVerifyCanadaTaxForYukonForPCOrder_1544(){
		int randomNumber=CommonUtils.getRandomNum(10000, 1000000);
		String randomCustomerSequenceNumber = null;
		String randomProductSequenceNumber = null;
		String SKUValue = null;
		String attendentFirstName=TestConstants.FIRST_NAME+randomNumber;
		String attendeeLastName = TestConstants.LAST_NAME+randomNumber;
		String  addressLine=TestConstants.ADDRESS_LINE_1_CA;
		String city=TestConstants.CITY_CA;
		String postalCode=TestConstants.POSTAL_CODE_CA;
		String Country=TestConstants.COUNTRY_DD_VALUE_CA;
		String Province=TestConstants.PROVINCE_YUKON;
		String phoneNumber=TestConstants.PHONE_NUMBER_CA;
		double gstFromUI = 0.00;
		double calculatedGstAmount = 0.00;
		double pstFromUI = 0.00;
		double calculatedPstAmount = 0.00;
		driver.get(driver.getCSCockpitURL());
		cscockpitCustomerSearchTabPage = cscockpitLoginPage.clickLoginBtn();
		cscockpitCustomerSearchTabPage.selectCustomerTypeFromDropDownInCustomerSearchTab("PC");
		cscockpitCustomerSearchTabPage.selectCountryFromDropDownInCustomerSearchTab("Canada");
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
		cscockpitCustomerTabPage.enterShippingInfoInAddNewPaymentProfilePopupWithoutSaveBtn(attendentFirstName, attendeeLastName, addressLine, city, postalCode, Country, Province, phoneNumber);
		cscockpitCheckoutTabPage.clickOnCreateNewAddressButtonInAutoshipTemplateTabPage();
		cscockpitCheckoutTabPage.clickUseEnteredAddressOfCreateNewAddressShippingAddressPopup();
		cscockpitCheckoutTabPage.clickAddNewPaymentAddressInCheckoutTab();
		cscockpitCheckoutTabPage.enterBillingInfo();
		cscockpitCheckoutTabPage.clickSaveAddNewPaymentProfilePopUP();
		cscockpitCheckoutTabPage.enterCVVValueInCheckoutTab(TestConstants.SECURITY_CODE);
		cscockpitCheckoutTabPage.clickUseThisCardBtnInCheckoutTab();
		cscockpitCheckoutTabPage.clickPlaceOrderButtonInCheckoutTab();
		double subTotal = cscockpitOrderTabPage.getSubTotalFromUI();
		double deliveryCost = cscockpitOrderTabPage.getDeliveryCostFromUI();
		double handlingCost = cscockpitOrderTabPage.getHandlingCostFromUI();
		if(cscockpitCheckoutTabPage.isGSTTaxPresentInUI()==true){
			gstFromUI = cscockpitOrderTabPage.getGstAmountFromUI();
			calculatedGstAmount = cscockpitOrderTabPage.calculateAmountAccordingToPercent(subTotal, deliveryCost, handlingCost, TestConstants.FIVE_PERCENT_TAX);
			s_assert.assertTrue(cscockpitOrderTabPage.validateGstAmountOnUI(gstFromUI,calculatedGstAmount),"GST Amount on UI is "+gstFromUI+" and calculated GST Amount is "+calculatedGstAmount+"");
		}else{
			logger.info("GST NOT PRESENT FOR THIS ORDER");
		}
		if(cscockpitOrderTabPage.isPstTaxPresentInUI() == true){
			pstFromUI = cscockpitOrderTabPage.getPstAmountFromUI();
			calculatedPstAmount = cscockpitOrderTabPage.calculateAmountAccordingToPercent(subTotal, deliveryCost, handlingCost, TestConstants.SEVEN_PERCENT_TAX);
			s_assert.assertTrue(cscockpitOrderTabPage.validatePstAmountOnUI(pstFromUI,calculatedPstAmount),"PST Amount on UI is "+pstFromUI+" and calculated GST Amount is "+calculatedPstAmount);
		}else{
			logger.info("PST NOT PRESENT FOR THIS ORDER");
		}
		s_assert.assertAll();
	}

	//Hybris Project-1545:To verify the Canada tax for Northwest Territories for Preferred Customer Order
	@Test
	public void testVerifyCanadaTaxForNorthwestTerritoriesForPCOrder_1545(){
		int randomNumber=CommonUtils.getRandomNum(10000, 1000000);
		String randomCustomerSequenceNumber = null;
		String randomProductSequenceNumber = null;
		String SKUValue = null;
		String attendentFirstName=TestConstants.FIRST_NAME+randomNumber;
		String attendeeLastName = TestConstants.LAST_NAME+randomNumber;
		String  addressLine=TestConstants.ADDRESS_LINE_1_CA;
		String city=TestConstants.CITY_CA;
		String postalCode=TestConstants.POSTAL_CODE_CA;
		String Country=TestConstants.COUNTRY_DD_VALUE_CA;
		String Province=TestConstants.PROVINCE_NORTHWEST_TERRITORIES;
		String phoneNumber=TestConstants.PHONE_NUMBER_CA;
		double gstFromUI = 0.00;
		double calculatedGstAmount = 0.00;
		double pstFromUI = 0.00;
		double calculatedPstAmount = 0.00;
		driver.get(driver.getCSCockpitURL());
		cscockpitCustomerSearchTabPage = cscockpitLoginPage.clickLoginBtn();
		cscockpitCustomerSearchTabPage.selectCustomerTypeFromDropDownInCustomerSearchTab("PC");
		cscockpitCustomerSearchTabPage.selectCountryFromDropDownInCustomerSearchTab("Canada");
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
		cscockpitCustomerTabPage.enterShippingInfoInAddNewPaymentProfilePopupWithoutSaveBtn(attendentFirstName, attendeeLastName, addressLine, city, postalCode, Country, Province, phoneNumber);
		cscockpitCheckoutTabPage.clickOnCreateNewAddressButtonInAutoshipTemplateTabPage();
		cscockpitCheckoutTabPage.clickUseEnteredAddressOfCreateNewAddressShippingAddressPopup();
		cscockpitCheckoutTabPage.clickAddNewPaymentAddressInCheckoutTab();
		cscockpitCheckoutTabPage.enterBillingInfo();
		cscockpitCheckoutTabPage.clickSaveAddNewPaymentProfilePopUP();
		cscockpitCheckoutTabPage.enterCVVValueInCheckoutTab(TestConstants.SECURITY_CODE);
		cscockpitCheckoutTabPage.clickUseThisCardBtnInCheckoutTab();
		cscockpitCheckoutTabPage.clickPlaceOrderButtonInCheckoutTab();
		double subTotal = cscockpitOrderTabPage.getSubTotalFromUI();
		double deliveryCost = cscockpitOrderTabPage.getDeliveryCostFromUI();
		double handlingCost = cscockpitOrderTabPage.getHandlingCostFromUI();
		if(cscockpitCheckoutTabPage.isGSTTaxPresentInUI()==true){
			gstFromUI = cscockpitOrderTabPage.getGstAmountFromUI();
			calculatedGstAmount = cscockpitOrderTabPage.calculateAmountAccordingToPercent(subTotal, deliveryCost, handlingCost, TestConstants.FIVE_PERCENT_TAX);
			s_assert.assertTrue(cscockpitOrderTabPage.validateGstAmountOnUI(gstFromUI,calculatedGstAmount),"GST Amount on UI is "+gstFromUI+" and calculated GST Amount is "+calculatedGstAmount+"");
		}else{
			logger.info("GST NOT PRESENT FOR THIS ORDER");
		}
		if(cscockpitOrderTabPage.isPstTaxPresentInUI() == true){
			pstFromUI = cscockpitOrderTabPage.getPstAmountFromUI();
			calculatedPstAmount = cscockpitOrderTabPage.calculateAmountAccordingToPercent(subTotal, deliveryCost, handlingCost, TestConstants.SEVEN_PERCENT_TAX);
			s_assert.assertTrue(cscockpitOrderTabPage.validatePstAmountOnUI(pstFromUI,calculatedPstAmount),"PST Amount on UI is "+pstFromUI+" and calculated GST Amount is "+calculatedPstAmount);
		}else{
			logger.info("PST NOT PRESENT FOR THIS ORDER");
		}
		s_assert.assertAll();
	}

	//Hybris Project-1546:To verify the Canada tax for Nunavat for Preferred Customer Order
	@Test
	public void testVerifyCanadaTaxForNunavatForPCOrder_1546(){
		int randomNumber=CommonUtils.getRandomNum(10000, 1000000);
		String randomCustomerSequenceNumber = null;
		String randomProductSequenceNumber = null;
		String SKUValue = null;
		String attendentFirstName=TestConstants.FIRST_NAME+randomNumber;
		String attendeeLastName = TestConstants.LAST_NAME+randomNumber;
		String  addressLine=TestConstants.ADDRESS_LINE_1_CA;
		String city=TestConstants.CITY_CA;
		String postalCode=TestConstants.POSTAL_CODE_CA;
		String Country=TestConstants.COUNTRY_DD_VALUE_CA;
		String Province=TestConstants.PROVINCE_NUNAVUT;
		String phoneNumber=TestConstants.PHONE_NUMBER_CA;
		double gstFromUI = 0.00;
		double calculatedGstAmount = 0.00;
		double pstFromUI = 0.00;
		double calculatedPstAmount = 0.00;
		driver.get(driver.getCSCockpitURL());
		cscockpitCustomerSearchTabPage = cscockpitLoginPage.clickLoginBtn();
		cscockpitCustomerSearchTabPage.selectCustomerTypeFromDropDownInCustomerSearchTab("PC");
		cscockpitCustomerSearchTabPage.selectCountryFromDropDownInCustomerSearchTab("Canada");
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
		cscockpitCustomerTabPage.enterShippingInfoInAddNewPaymentProfilePopupWithoutSaveBtn(attendentFirstName, attendeeLastName, addressLine, city, postalCode, Country, Province, phoneNumber);
		cscockpitCheckoutTabPage.clickOnCreateNewAddressButtonInAutoshipTemplateTabPage();
		cscockpitCheckoutTabPage.clickUseEnteredAddressOfCreateNewAddressShippingAddressPopup();
		cscockpitCheckoutTabPage.clickAddNewPaymentAddressInCheckoutTab();
		cscockpitCheckoutTabPage.enterBillingInfo();
		cscockpitCheckoutTabPage.clickSaveAddNewPaymentProfilePopUP();
		cscockpitCheckoutTabPage.enterCVVValueInCheckoutTab(TestConstants.SECURITY_CODE);
		cscockpitCheckoutTabPage.clickUseThisCardBtnInCheckoutTab();
		cscockpitCheckoutTabPage.clickPlaceOrderButtonInCheckoutTab();
		double subTotal = cscockpitOrderTabPage.getSubTotalFromUI();
		double deliveryCost = cscockpitOrderTabPage.getDeliveryCostFromUI();
		double handlingCost = cscockpitOrderTabPage.getHandlingCostFromUI();
		if(cscockpitCheckoutTabPage.isGSTTaxPresentInUI()==true){
			gstFromUI = cscockpitOrderTabPage.getGstAmountFromUI();
			calculatedGstAmount = cscockpitOrderTabPage.calculateAmountAccordingToPercent(subTotal, deliveryCost, handlingCost, TestConstants.FIVE_PERCENT_TAX);
			s_assert.assertTrue(cscockpitOrderTabPage.validateGstAmountOnUI(gstFromUI,calculatedGstAmount),"GST Amount on UI is "+gstFromUI+" and calculated GST Amount is "+calculatedGstAmount+"");
		}else{
			logger.info("GST NOT PRESENT FOR THIS ORDER");
		}
		if(cscockpitOrderTabPage.isPstTaxPresentInUI() == true){
			pstFromUI = cscockpitOrderTabPage.getPstAmountFromUI();
			calculatedPstAmount = cscockpitOrderTabPage.calculateAmountAccordingToPercent(subTotal, deliveryCost, handlingCost, TestConstants.SEVEN_PERCENT_TAX);
			s_assert.assertTrue(cscockpitOrderTabPage.validatePstAmountOnUI(pstFromUI,calculatedPstAmount),"PST Amount on UI is "+pstFromUI+" and calculated GST Amount is "+calculatedPstAmount);
		}else{
			logger.info("PST NOT PRESENT FOR THIS ORDER");
		}
		s_assert.assertAll();
	}

	//Hybris Project-1547:To verify the Canada tax for British Columbia for Consultant Order
	@Test
	public void testVerifyTheCanadaTaxForBritishColumbiaForConsultantOrder_1547(){
		int randomNumber=CommonUtils.getRandomNum(10000, 1000000);
		String randomCustomerSequenceNumber = null;
		String randomProductSequenceNumber = null;
		String SKUValue = null;
		String attendentFirstName=TestConstants.FIRST_NAME+randomNumber;
		String attendeeLastName = "lastname";
		String  addressLine=TestConstants.ADDRESS_LINE_1_CA;
		String city=TestConstants.CITY_CA;
		String postalCode=TestConstants.POSTAL_CODE_CA;
		String Country=TestConstants.COUNTRY_DD_VALUE_CA;
		String province=TestConstants.PROVINCE_BRITISH_COULMBIA;
		String phoneNumber=TestConstants.PHONE_NUMBER_CA;
		double gstFromUI = 0.00;
		double calculatedGstAmount = 0.00;
		double pstFromUI = 0.00;
		double calculatedPstAmount = 0.00;
		cscockpitCustomerSearchTabPage = cscockpitLoginPage.clickLoginBtn();
		cscockpitCustomerSearchTabPage.selectCustomerTypeFromDropDownInCustomerSearchTab("CONSULTANT");
		cscockpitCustomerSearchTabPage.selectCountryFromDropDownInCustomerSearchTab("Canada");
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
		cscockpitCustomerTabPage.enterShippingInfoInAddNewPaymentProfilePopupWithoutSaveBtn(attendentFirstName, attendeeLastName, addressLine, city, postalCode, Country, province, phoneNumber);
		cscockpitCheckoutTabPage.clickOnCreateNewAddressButtonInAutoshipTemplateTabPage();
		cscockpitCheckoutTabPage.clickUseEnteredAddressOfCreateNewAddressShippingAddressPopup();
		cscockpitCheckoutTabPage.clickAddNewPaymentAddressInCheckoutTab();
		cscockpitCheckoutTabPage.enterBillingInfo();
		cscockpitCheckoutTabPage.clickSaveAddNewPaymentProfilePopUP();
		cscockpitCheckoutTabPage.enterCVVValueInCheckoutTab(TestConstants.SECURITY_CODE);
		cscockpitCheckoutTabPage.clickUseThisCardBtnInCheckoutTab();
		cscockpitCheckoutTabPage.clickPlaceOrderButtonInCheckoutTab();
		double subTotal = cscockpitOrderTabPage.getSubTotalFromUI();
		double deliveryCost = cscockpitOrderTabPage.getDeliveryCostFromUI();
		double handlingCost = cscockpitOrderTabPage.getHandlingCostFromUI();
		if(cscockpitCheckoutTabPage.isGSTTaxPresentInUI()==true){
			gstFromUI = cscockpitOrderTabPage.getGstAmountFromUI();
			calculatedGstAmount = cscockpitOrderTabPage.calculateAmountAccordingToPercent(subTotal, deliveryCost, handlingCost, TestConstants.FIVE_PERCENT_TAX);
			s_assert.assertTrue(cscockpitOrderTabPage.validateGstAmountOnUI(gstFromUI,calculatedGstAmount),"GST Amount on UI is "+gstFromUI+" and calculated GST Amount is "+calculatedGstAmount+"");
		}else{
			logger.info("GST NOT PRESENT FOR THIS ORDER");
		}
		if(cscockpitOrderTabPage.isPstTaxPresentInUI() == true){
			pstFromUI = cscockpitOrderTabPage.getPstAmountFromUI();
			calculatedPstAmount = cscockpitOrderTabPage.calculateAmountAccordingToPercent(subTotal, deliveryCost, handlingCost, TestConstants.SEVEN_PERCENT_TAX);
			s_assert.assertTrue(cscockpitOrderTabPage.validatePstAmountOnUI(pstFromUI,calculatedPstAmount),"PST Amount on UI is "+pstFromUI+" and calculated GST Amount is "+calculatedPstAmount);
		}else{
			logger.info("PST NOT PRESENT FOR THIS ORDER");
		}
		s_assert.assertAll();
	}

	//Hybris Project-1548:To verify the Canada tax for Alberta for Consultant Order
	@Test
	public void testToVerifyCanadaTaxForAlbertaForConsultantOrder_1548(){ 
		String randomCustomerSequenceNumber = null;
		String randomProductSequenceNumber = null;
		String SKUValue = null;
		int randomNumber=CommonUtils.getRandomNum(10000, 1000000);
		String attendentFirstName=TestConstants.FIRST_NAME+randomNumber;
		String attendeeLastName = TestConstants.LAST_NAME+randomNumber;
		String  addressLine=TestConstants.ADDRESS_LINE_1_CA;
		String city=TestConstants.CITY_CA;
		String postalCode=TestConstants.POSTAL_CODE_CA;
		String Country=TestConstants.COUNTRY_DD_VALUE_CA;
		String province=TestConstants.PROVINCE_CA;
		String phoneNumber=TestConstants.PHONE_NUMBER_CA;

		double gstFromUI = 0.00;
		double calculatedGstAmount = 0.00;
		double pstFromUI = 0.00;
		double calculatedPstAmount = 0.00;
		cscockpitCustomerSearchTabPage = cscockpitLoginPage.clickLoginBtn();
		cscockpitCustomerSearchTabPage.selectCustomerTypeFromDropDownInCustomerSearchTab("CONSULTANT");
		cscockpitCustomerSearchTabPage.selectCountryFromDropDownInCustomerSearchTab("Canada");
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
		cscockpitCustomerTabPage.enterShippingInfoInAddNewPaymentProfilePopupWithoutSaveBtn(attendentFirstName, attendeeLastName, addressLine, city, postalCode, Country, province, phoneNumber);
		cscockpitCheckoutTabPage.clickOnCreateNewAddressButtonInAutoshipTemplateTabPage();
		cscockpitCheckoutTabPage.clickUseEnteredAddressOfCreateNewAddressShippingAddressPopup();
		cscockpitCheckoutTabPage.clickAddNewPaymentAddressInCheckoutTab();
		cscockpitCheckoutTabPage.enterBillingInfo();
		cscockpitCheckoutTabPage.clickSaveAddNewPaymentProfilePopUP();
		cscockpitCheckoutTabPage.enterCVVValueInCheckoutTab(TestConstants.SECURITY_CODE);
		cscockpitCheckoutTabPage.clickUseThisCardBtnInCheckoutTab();
		cscockpitCheckoutTabPage.clickPlaceOrderButtonInCheckoutTab();
		double subTotal = cscockpitOrderTabPage.getSubTotalFromUI();
		double deliveryCost = cscockpitOrderTabPage.getDeliveryCostFromUI();
		double handlingCost = cscockpitOrderTabPage.getHandlingCostFromUI();
		if(cscockpitCheckoutTabPage.isGSTTaxPresentInUI()==true){
			gstFromUI = cscockpitOrderTabPage.getGstAmountFromUI();
			calculatedGstAmount = cscockpitOrderTabPage.calculateAmountAccordingToPercent(subTotal, deliveryCost, handlingCost, TestConstants.FIVE_PERCENT_TAX);
			s_assert.assertTrue(cscockpitOrderTabPage.validateGstAmountOnUI(gstFromUI,calculatedGstAmount),"GST Amount on UI is "+gstFromUI+" and calculated GST Amount is "+calculatedGstAmount+"");
		}else{
			logger.info("GST NOT PRESENT FOR THIS ORDER");
		}
		if(cscockpitOrderTabPage.isPstTaxPresentInUI() == true){
			pstFromUI = cscockpitOrderTabPage.getPstAmountFromUI();
			calculatedPstAmount = cscockpitOrderTabPage.calculateAmountAccordingToPercent(subTotal, deliveryCost, handlingCost, TestConstants.SEVEN_PERCENT_TAX);
			s_assert.assertTrue(cscockpitOrderTabPage.validatePstAmountOnUI(pstFromUI,calculatedPstAmount),"PST Amount on UI is "+pstFromUI+" and calculated GST Amount is "+calculatedPstAmount);
		}else{
			logger.info("PST NOT PRESENT FOR THIS ORDER");
		}
		s_assert.assertAll();
	}

	//Hybris Project-1549:To verify the Canada tax for saskatchewan for Consultant Order
	@Test
	public void testToVerifyCanadaTaxForSaskatchewanForConsultantOrder_1549(){ 
		String randomCustomerSequenceNumber = null;
		String randomProductSequenceNumber = null;
		String SKUValue = null;
		int randomNumber=CommonUtils.getRandomNum(10000, 1000000);
		String attendentFirstName=TestConstants.FIRST_NAME+randomNumber;
		String attendeeLastName = TestConstants.LAST_NAME+randomNumber;
		String  addressLine=TestConstants.ADDRESS_LINE_1_CA;
		String city=TestConstants.CITY_CA;
		String postalCode=TestConstants.POSTAL_CODE_CA;
		String Country=TestConstants.COUNTRY_DD_VALUE_CA;
		String province=TestConstants.PROVINCE_SASKATCHEWAN;
		String phoneNumber=TestConstants.PHONE_NUMBER_CA;
		double gstFromUI = 0.00;
		double calculatedGstAmount = 0.00;
		double pstFromUI = 0.00;
		double calculatedPstAmount = 0.00;
		cscockpitCustomerSearchTabPage = cscockpitLoginPage.clickLoginBtn();
		cscockpitCustomerSearchTabPage.selectCustomerTypeFromDropDownInCustomerSearchTab("CONSULTANT");
		cscockpitCustomerSearchTabPage.selectCountryFromDropDownInCustomerSearchTab("Canada");
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
		cscockpitCustomerTabPage.enterShippingInfoInAddNewPaymentProfilePopupWithoutSaveBtn(attendentFirstName, attendeeLastName, addressLine, city, postalCode, Country, province, phoneNumber);
		cscockpitCheckoutTabPage.clickOnCreateNewAddressButtonInAutoshipTemplateTabPage();
		cscockpitCheckoutTabPage.clickUseEnteredAddressOfCreateNewAddressShippingAddressPopup();
		cscockpitCheckoutTabPage.clickAddNewPaymentAddressInCheckoutTab();
		cscockpitCheckoutTabPage.enterBillingInfo();
		cscockpitCheckoutTabPage.clickSaveAddNewPaymentProfilePopUP();
		cscockpitCheckoutTabPage.enterCVVValueInCheckoutTab(TestConstants.SECURITY_CODE);
		cscockpitCheckoutTabPage.clickUseThisCardBtnInCheckoutTab();
		cscockpitCheckoutTabPage.clickPlaceOrderButtonInCheckoutTab();
		double subTotal = cscockpitOrderTabPage.getSubTotalFromUI();
		double deliveryCost = cscockpitOrderTabPage.getDeliveryCostFromUI();
		double handlingCost = cscockpitOrderTabPage.getHandlingCostFromUI();
		if(cscockpitCheckoutTabPage.isGSTTaxPresentInUI()==true){
			gstFromUI = cscockpitOrderTabPage.getGstAmountFromUI();
			calculatedGstAmount = cscockpitOrderTabPage.calculateAmountAccordingToPercent(subTotal, deliveryCost, handlingCost, TestConstants.FIVE_PERCENT_TAX);
			s_assert.assertTrue(cscockpitOrderTabPage.validateGstAmountOnUI(gstFromUI,calculatedGstAmount),"GST Amount on UI is "+gstFromUI+" and calculated GST Amount is "+calculatedGstAmount+"");
		}else{
			logger.info("GST NOT PRESENT FOR THIS ORDER");
		}
		if(cscockpitOrderTabPage.isPstTaxPresentInUI() == true){
			pstFromUI = cscockpitOrderTabPage.getPstAmountFromUI();
			calculatedPstAmount = cscockpitOrderTabPage.calculateAmountAccordingToPercent(subTotal, deliveryCost, handlingCost, TestConstants.FIVE_PERCENT_TAX);
			s_assert.assertTrue(cscockpitOrderTabPage.validatePstAmountOnUI(pstFromUI,calculatedPstAmount),"PST Amount on UI is "+pstFromUI+" and calculated GST Amount is "+calculatedPstAmount);
		}else{
			logger.info("PST NOT PRESENT FOR THIS ORDER");
		}
		s_assert.assertAll();
	}

	//Hybris Project-1550:To verify the Canada tax for Manitoba for Consultant Order
	@Test
	public void testToVerifyCanadaTaxForManitobaForConsultantOrder_1550(){ 
		String randomCustomerSequenceNumber = null;
		String randomProductSequenceNumber = null;
		String SKUValue = null;
		int randomNumber=CommonUtils.getRandomNum(10000, 1000000);
		String attendentFirstName=TestConstants.FIRST_NAME+randomNumber;
		String attendeeLastName = TestConstants.LAST_NAME+randomNumber;
		String  addressLine=TestConstants.ADDRESS_LINE_1_CA;
		String city=TestConstants.CITY_CA;
		String postalCode=TestConstants.POSTAL_CODE_CA;
		String Country=TestConstants.COUNTRY_DD_VALUE_CA;
		String province=TestConstants.PROVINCE_MANITOBA;
		String phoneNumber=TestConstants.PHONE_NUMBER_CA;
		double gstFromUI = 0.00;
		double calculatedGstAmount = 0.00;
		double pstFromUI = 0.00;
		double calculatedPstAmount = 0.00;
		cscockpitCustomerSearchTabPage = cscockpitLoginPage.clickLoginBtn();
		cscockpitCustomerSearchTabPage.selectCustomerTypeFromDropDownInCustomerSearchTab("CONSULTANT");
		cscockpitCustomerSearchTabPage.selectCountryFromDropDownInCustomerSearchTab("Canada");
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
		cscockpitCustomerTabPage.enterShippingInfoInAddNewPaymentProfilePopupWithoutSaveBtn(attendentFirstName, attendeeLastName, addressLine, city, postalCode, Country, province, phoneNumber);
		cscockpitCheckoutTabPage.clickOnCreateNewAddressButtonInAutoshipTemplateTabPage();
		cscockpitCheckoutTabPage.clickUseEnteredAddressOfCreateNewAddressShippingAddressPopup();
		cscockpitCheckoutTabPage.clickAddNewPaymentAddressInCheckoutTab();
		cscockpitCheckoutTabPage.enterBillingInfo();
		cscockpitCheckoutTabPage.clickSaveAddNewPaymentProfilePopUP();
		cscockpitCheckoutTabPage.enterCVVValueInCheckoutTab(TestConstants.SECURITY_CODE);
		cscockpitCheckoutTabPage.clickUseThisCardBtnInCheckoutTab();
		cscockpitCheckoutTabPage.clickPlaceOrderButtonInCheckoutTab();
		double subTotal = cscockpitOrderTabPage.getSubTotalFromUI();
		double deliveryCost = cscockpitOrderTabPage.getDeliveryCostFromUI();
		double handlingCost = cscockpitOrderTabPage.getHandlingCostFromUI();
		if(cscockpitCheckoutTabPage.isGSTTaxPresentInUI()==true){
			gstFromUI = cscockpitOrderTabPage.getGstAmountFromUI();
			calculatedGstAmount = cscockpitOrderTabPage.calculateAmountAccordingToPercent(subTotal, deliveryCost, handlingCost, TestConstants.FIVE_PERCENT_TAX);
			s_assert.assertTrue(cscockpitOrderTabPage.validateGstAmountOnUI(gstFromUI,calculatedGstAmount),"GST Amount on UI is "+gstFromUI+" and calculated GST Amount is "+calculatedGstAmount+"");
		}else{
			logger.info("GST NOT PRESENT FOR THIS ORDER");
		}
		if(cscockpitOrderTabPage.isPstTaxPresentInUI() == true){
			pstFromUI = cscockpitOrderTabPage.getPstAmountFromUI();
			calculatedPstAmount = cscockpitOrderTabPage.calculateAmountAccordingToPercent(subTotal, deliveryCost, handlingCost, TestConstants.EIGHT_PERCENT_TAX);
			s_assert.assertTrue(cscockpitOrderTabPage.validatePstAmountOnUI(pstFromUI,calculatedPstAmount),"PST Amount on UI is "+pstFromUI+" and calculated GST Amount is "+calculatedPstAmount);
		}else{
			logger.info("PST NOT PRESENT FOR THIS ORDER");
		}
		s_assert.assertAll();
	}

	//Hybris Project-1551:To verify the Canada tax for Ontario for Consultant Order
	@Test
	public void testToVerifyCanadaTaxForOntarioForConsultantOrder_1551(){ 
		String randomCustomerSequenceNumber = null;
		String randomProductSequenceNumber = null;
		String SKUValue = null;
		int randomNumber=CommonUtils.getRandomNum(10000, 1000000);
		String attendentFirstName=TestConstants.FIRST_NAME+randomNumber;
		String attendeeLastName = TestConstants.LAST_NAME+randomNumber;
		String  addressLine=TestConstants.ADDRESS_LINE_1_CA;
		String city=TestConstants.CITY_CA;
		String postalCode=TestConstants.POSTAL_CODE_CA;
		String Country=TestConstants.COUNTRY_DD_VALUE_CA;
		String province=TestConstants.PROVINCE_ONTARIO;
		String phoneNumber=TestConstants.PHONE_NUMBER_CA;
		double gstFromUI = 0.00;
		double calculatedGstAmount = 0.00;
		double pstFromUI = 0.00;
		double calculatedPstAmount = 0.00;
		cscockpitCustomerSearchTabPage = cscockpitLoginPage.clickLoginBtn();
		cscockpitCustomerSearchTabPage.selectCustomerTypeFromDropDownInCustomerSearchTab("CONSULTANT");
		cscockpitCustomerSearchTabPage.selectCountryFromDropDownInCustomerSearchTab("Canada");
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
		cscockpitCustomerTabPage.enterShippingInfoInAddNewPaymentProfilePopupWithoutSaveBtn(attendentFirstName, attendeeLastName, addressLine, city, postalCode, Country, province, phoneNumber);
		cscockpitCheckoutTabPage.clickOnCreateNewAddressButtonInAutoshipTemplateTabPage();
		cscockpitCheckoutTabPage.clickUseEnteredAddressOfCreateNewAddressShippingAddressPopup();
		cscockpitCheckoutTabPage.clickAddNewPaymentAddressInCheckoutTab();
		cscockpitCheckoutTabPage.enterBillingInfo();
		cscockpitCheckoutTabPage.clickSaveAddNewPaymentProfilePopUP();
		cscockpitCheckoutTabPage.enterCVVValueInCheckoutTab(TestConstants.SECURITY_CODE);
		cscockpitCheckoutTabPage.clickUseThisCardBtnInCheckoutTab();
		cscockpitCheckoutTabPage.clickPlaceOrderButtonInCheckoutTab();
		double subTotal = cscockpitOrderTabPage.getSubTotalFromUI();
		double deliveryCost = cscockpitOrderTabPage.getDeliveryCostFromUI();
		double handlingCost = cscockpitOrderTabPage.getHandlingCostFromUI();
		if(cscockpitCheckoutTabPage.isGSTTaxPresentInUI()==true){
			gstFromUI = cscockpitOrderTabPage.getGstAmountFromUI();
			calculatedGstAmount = cscockpitOrderTabPage.calculateAmountAccordingToPercent(subTotal, deliveryCost, handlingCost, TestConstants.FIVE_PERCENT_TAX);
			s_assert.assertTrue(cscockpitOrderTabPage.validateGstAmountOnUI(gstFromUI,calculatedGstAmount),"GST Amount on UI is "+gstFromUI+" and calculated GST Amount is "+calculatedGstAmount+"");
		}else{
			logger.info("GST NOT PRESENT FOR THIS ORDER");
		}
		if(cscockpitOrderTabPage.isPstTaxPresentInUI() == true){
			pstFromUI = cscockpitOrderTabPage.getPstAmountFromUI();
			calculatedPstAmount = cscockpitOrderTabPage.calculateAmountAccordingToPercent(subTotal, deliveryCost, handlingCost, TestConstants.SEVEN_PERCENT_TAX);
			s_assert.assertTrue(cscockpitOrderTabPage.validatePstAmountOnUI(pstFromUI,calculatedPstAmount),"PST Amount on UI is "+pstFromUI+" and calculated GST Amount is "+calculatedPstAmount);
		}else{
			logger.info("PST NOT PRESENT FOR THIS ORDER");
		}
		s_assert.assertAll();
	}

	//Hybris Project-1553:To verify the Canada tax for Nova Scotia for Consultant Order
	@Test
	public void testToVerifyCanadaTaxForNovaScotiaForConsultantOrder_1553(){ 
		int randomNumber=CommonUtils.getRandomNum(10000, 1000000);
		String randomCustomerSequenceNumber = null;
		String randomProductSequenceNumber = null;
		String SKUValue = null;
		String attendentFirstName=TestConstants.FIRST_NAME+randomNumber;
		String attendeeLastName = TestConstants.LAST_NAME+randomNumber;
		String  addressLine=TestConstants.ADDRESS_LINE_1_CA;
		String city=TestConstants.CITY_CA;
		String postalCode=TestConstants.POSTAL_CODE_CA;
		String Country=TestConstants.COUNTRY_DD_VALUE_CA;
		String province=TestConstants.PROVINCE_NOVA_SCOTIA;
		String phoneNumber=TestConstants.PHONE_NUMBER_CA;
		double gstFromUI = 0.00;
		double calculatedGstAmount = 0.00;
		double pstFromUI = 0.00;
		double calculatedPstAmount = 0.00;
		cscockpitCustomerSearchTabPage = cscockpitLoginPage.clickLoginBtn();
		cscockpitCustomerSearchTabPage.selectCustomerTypeFromDropDownInCustomerSearchTab("CONSULTANT");
		cscockpitCustomerSearchTabPage.selectCountryFromDropDownInCustomerSearchTab("Canada");
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
		cscockpitCustomerTabPage.enterShippingInfoInAddNewPaymentProfilePopupWithoutSaveBtn(attendentFirstName, attendeeLastName, addressLine, city, postalCode, Country, province, phoneNumber);
		cscockpitCheckoutTabPage.clickOnCreateNewAddressButtonInAutoshipTemplateTabPage();
		cscockpitCheckoutTabPage.clickUseEnteredAddressOfCreateNewAddressShippingAddressPopup();
		cscockpitCheckoutTabPage.clickAddNewPaymentAddressInCheckoutTab();
		cscockpitCheckoutTabPage.enterBillingInfo();
		cscockpitCheckoutTabPage.clickSaveAddNewPaymentProfilePopUP();
		cscockpitCheckoutTabPage.enterCVVValueInCheckoutTab(TestConstants.SECURITY_CODE);
		cscockpitCheckoutTabPage.clickUseThisCardBtnInCheckoutTab();
		cscockpitCheckoutTabPage.clickPlaceOrderButtonInCheckoutTab();
		double subTotal = cscockpitOrderTabPage.getSubTotalFromUI();
		double deliveryCost = cscockpitOrderTabPage.getDeliveryCostFromUI();
		double handlingCost = cscockpitOrderTabPage.getHandlingCostFromUI();
		if(cscockpitCheckoutTabPage.isGSTTaxPresentInUI()==true){
			gstFromUI = cscockpitOrderTabPage.getGstAmountFromUI();
			calculatedGstAmount = cscockpitOrderTabPage.calculateAmountAccordingToPercent(subTotal, deliveryCost, handlingCost, TestConstants.FIVE_PERCENT_TAX);
			s_assert.assertTrue(cscockpitOrderTabPage.validateGstAmountOnUI(gstFromUI,calculatedGstAmount),"GST Amount on UI is "+gstFromUI+" and calculated GST Amount is "+calculatedGstAmount+"");
		}else{
			logger.info("GST NOT PRESENT FOR THIS ORDER");
		}
		if(cscockpitOrderTabPage.isPstTaxPresentInUI() == true){
			pstFromUI = cscockpitOrderTabPage.getPstAmountFromUI();
			calculatedPstAmount = cscockpitOrderTabPage.calculateAmountAccordingToPercent(subTotal, deliveryCost, handlingCost, TestConstants.SEVEN_PERCENT_TAX);
			s_assert.assertTrue(cscockpitOrderTabPage.validatePstAmountOnUI(pstFromUI,calculatedPstAmount),"PST Amount on UI is "+pstFromUI+" and calculated GST Amount is "+calculatedPstAmount);
		}else{
			logger.info("PST NOT PRESENT FOR THIS ORDER");
		}
		s_assert.assertAll();
	}

	//Hybris Project-1554:To verify the Canada tax for New Brunswick for Consultant Order
	@Test
	public void testToVerifyCanadaTaxForNewBrunswickForConsultantOrder_1554(){ 
		int randomNumber=CommonUtils.getRandomNum(10000, 1000000);
		String randomCustomerSequenceNumber = null;
		String randomProductSequenceNumber = null;
		String SKUValue = null;
		String attendentFirstName=TestConstants.FIRST_NAME+randomNumber;
		String attendeeLastName = TestConstants.LAST_NAME+randomNumber;
		String  addressLine=TestConstants.ADDRESS_LINE_1_CA;
		String city=TestConstants.CITY_CA;
		String postalCode=TestConstants.POSTAL_CODE_CA;
		String Country=TestConstants.COUNTRY_DD_VALUE_CA;
		String province=TestConstants.PROVINCE_NEW_BRUNSWICK;
		String phoneNumber=TestConstants.PHONE_NUMBER_CA;
		double gstFromUI = 0.00;
		double calculatedGstAmount = 0.00;
		double pstFromUI = 0.00;
		double calculatedPstAmount = 0.00;
		cscockpitCustomerSearchTabPage = cscockpitLoginPage.clickLoginBtn();
		cscockpitCustomerSearchTabPage.selectCustomerTypeFromDropDownInCustomerSearchTab("CONSULTANT");
		cscockpitCustomerSearchTabPage.selectCountryFromDropDownInCustomerSearchTab("Canada");
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
		cscockpitCustomerTabPage.enterShippingInfoInAddNewPaymentProfilePopupWithoutSaveBtn(attendentFirstName, attendeeLastName, addressLine, city, postalCode, Country, province, phoneNumber);
		cscockpitCheckoutTabPage.clickOnCreateNewAddressButtonInAutoshipTemplateTabPage();
		cscockpitCheckoutTabPage.clickUseEnteredAddressOfCreateNewAddressShippingAddressPopup();
		cscockpitCheckoutTabPage.clickAddNewPaymentAddressInCheckoutTab();
		cscockpitCheckoutTabPage.enterBillingInfo();
		cscockpitCheckoutTabPage.clickSaveAddNewPaymentProfilePopUP();
		cscockpitCheckoutTabPage.enterCVVValueInCheckoutTab(TestConstants.SECURITY_CODE);
		cscockpitCheckoutTabPage.clickUseThisCardBtnInCheckoutTab();
		cscockpitCheckoutTabPage.clickPlaceOrderButtonInCheckoutTab();
		double subTotal = cscockpitOrderTabPage.getSubTotalFromUI();
		double deliveryCost = cscockpitOrderTabPage.getDeliveryCostFromUI();
		double handlingCost = cscockpitOrderTabPage.getHandlingCostFromUI();
		if(cscockpitCheckoutTabPage.isGSTTaxPresentInUI()==true){
			gstFromUI = cscockpitOrderTabPage.getGstAmountFromUI();
			calculatedGstAmount = cscockpitOrderTabPage.calculateAmountAccordingToPercent(subTotal, deliveryCost, handlingCost, TestConstants.FIVE_PERCENT_TAX);
			s_assert.assertTrue(cscockpitOrderTabPage.validateGstAmountOnUI(gstFromUI,calculatedGstAmount),"GST Amount on UI is "+gstFromUI+" and calculated GST Amount is "+calculatedGstAmount+"");
		}else{
			logger.info("GST NOT PRESENT FOR THIS ORDER");
		}
		if(cscockpitOrderTabPage.isPstTaxPresentInUI() == true){
			pstFromUI = cscockpitOrderTabPage.getPstAmountFromUI();
			calculatedPstAmount = cscockpitOrderTabPage.calculateAmountAccordingToPercent(subTotal, deliveryCost, handlingCost, TestConstants.SEVEN_PERCENT_TAX);
			s_assert.assertTrue(cscockpitOrderTabPage.validatePstAmountOnUI(pstFromUI,calculatedPstAmount),"PST Amount on UI is "+pstFromUI+" and calculated GST Amount is "+calculatedPstAmount);
		}else{
			logger.info("PST NOT PRESENT FOR THIS ORDER");
		}
		s_assert.assertAll();
	}

	//Hybris Project-1555:To verify the Canada tax for Prince Edward Island for Consultant Order
	@Test
	public void testToVerifyCanadaTaxForPrinceEdwardIslandForConsultantOrder_1555(){ 
		int randomNumber=CommonUtils.getRandomNum(10000, 1000000);
		String randomCustomerSequenceNumber = null;
		String randomProductSequenceNumber = null;
		String SKUValue = null;
		String attendentFirstName=TestConstants.FIRST_NAME+randomNumber;
		String attendeeLastName = TestConstants.LAST_NAME+randomNumber;
		String  addressLine=TestConstants.ADDRESS_LINE_1_CA;
		String city=TestConstants.CITY_CA;
		String postalCode=TestConstants.POSTAL_CODE_CA;
		String Country=TestConstants.COUNTRY_DD_VALUE_CA;
		String province=TestConstants.PROVINCE_PRINCE_EDWARD_ISLAND;
		String phoneNumber=TestConstants.PHONE_NUMBER_CA;
		double gstFromUI = 0.00;
		double calculatedGstAmount = 0.00;
		double pstFromUI = 0.00;
		double calculatedPstAmount = 0.00;
		cscockpitCustomerSearchTabPage = cscockpitLoginPage.clickLoginBtn();
		cscockpitCustomerSearchTabPage.selectCustomerTypeFromDropDownInCustomerSearchTab("CONSULTANT");
		cscockpitCustomerSearchTabPage.selectCountryFromDropDownInCustomerSearchTab("Canada");
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
		cscockpitCustomerTabPage.enterShippingInfoInAddNewPaymentProfilePopupWithoutSaveBtn(attendentFirstName, attendeeLastName, addressLine, city, postalCode, Country, province, phoneNumber);
		cscockpitCheckoutTabPage.clickOnCreateNewAddressButtonInAutoshipTemplateTabPage();
		cscockpitCheckoutTabPage.clickUseEnteredAddressOfCreateNewAddressShippingAddressPopup();
		cscockpitCheckoutTabPage.clickAddNewPaymentAddressInCheckoutTab();
		cscockpitCheckoutTabPage.enterBillingInfo();
		cscockpitCheckoutTabPage.clickSaveAddNewPaymentProfilePopUP();
		cscockpitCheckoutTabPage.enterCVVValueInCheckoutTab(TestConstants.SECURITY_CODE);
		cscockpitCheckoutTabPage.clickUseThisCardBtnInCheckoutTab();
		cscockpitCheckoutTabPage.clickPlaceOrderButtonInCheckoutTab();
		double subTotal = cscockpitOrderTabPage.getSubTotalFromUI();
		double deliveryCost = cscockpitOrderTabPage.getDeliveryCostFromUI();
		double handlingCost = cscockpitOrderTabPage.getHandlingCostFromUI();
		if(cscockpitCheckoutTabPage.isGSTTaxPresentInUI()==true){
			gstFromUI = cscockpitOrderTabPage.getGstAmountFromUI();
			calculatedGstAmount = cscockpitOrderTabPage.calculateAmountAccordingToPercent(subTotal, deliveryCost, handlingCost, TestConstants.FIVE_PERCENT_TAX);
			s_assert.assertTrue(cscockpitOrderTabPage.validateGstAmountOnUI(gstFromUI,calculatedGstAmount),"GST Amount on UI is "+gstFromUI+" and calculated GST Amount is "+calculatedGstAmount+"");
		}else{
			logger.info("GST NOT PRESENT FOR THIS ORDER");
		}
		if(cscockpitOrderTabPage.isPstTaxPresentInUI() == true){
			pstFromUI = cscockpitOrderTabPage.getPstAmountFromUI();
			calculatedPstAmount = cscockpitOrderTabPage.calculateAmountAccordingToPercent(subTotal, deliveryCost, handlingCost, TestConstants.SEVEN_PERCENT_TAX);
			s_assert.assertTrue(cscockpitOrderTabPage.validatePstAmountOnUI(pstFromUI,calculatedPstAmount),"PST Amount on UI is "+pstFromUI+" and calculated GST Amount is "+calculatedPstAmount);
		}else{
			logger.info("PST NOT PRESENT FOR THIS ORDER");
		}
		s_assert.assertAll();
	}

	//Hybris Project-1556:To verify the Canada tax for New foundland for Consultant Order
	@Test
	public void testToVerifyCanadaTaxForNewFoundlandForConsultantOrder_1556(){ 
		int randomNumber=CommonUtils.getRandomNum(10000, 1000000);
		String randomCustomerSequenceNumber = null;
		String randomProductSequenceNumber = null;
		String SKUValue = null;
		String attendentFirstName=TestConstants.FIRST_NAME+randomNumber;
		String attendeeLastName = TestConstants.LAST_NAME+randomNumber;
		String  addressLine=TestConstants.ADDRESS_LINE_1_CA;
		String city=TestConstants.CITY_CA;
		String postalCode=TestConstants.POSTAL_CODE_CA;
		String Country=TestConstants.COUNTRY_DD_VALUE_CA;
		String province=TestConstants.PROVINCE_NEW_FOUNDLAND_AND_LABRADOR;
		String phoneNumber=TestConstants.PHONE_NUMBER_CA;
		double gstFromUI = 0.00;
		double calculatedGstAmount = 0.00;
		double pstFromUI = 0.00;
		double calculatedPstAmount = 0.00;
		cscockpitCustomerSearchTabPage = cscockpitLoginPage.clickLoginBtn();
		cscockpitCustomerSearchTabPage.selectCustomerTypeFromDropDownInCustomerSearchTab("CONSULTANT");
		cscockpitCustomerSearchTabPage.selectCountryFromDropDownInCustomerSearchTab("Canada");
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
		cscockpitCustomerTabPage.enterShippingInfoInAddNewPaymentProfilePopupWithoutSaveBtn(attendentFirstName, attendeeLastName, addressLine, city, postalCode, Country, province, phoneNumber);
		cscockpitCheckoutTabPage.clickOnCreateNewAddressButtonInAutoshipTemplateTabPage();
		cscockpitCheckoutTabPage.clickUseEnteredAddressOfCreateNewAddressShippingAddressPopup();
		cscockpitCheckoutTabPage.clickAddNewPaymentAddressInCheckoutTab();
		cscockpitCheckoutTabPage.enterBillingInfo();
		cscockpitCheckoutTabPage.clickSaveAddNewPaymentProfilePopUP();
		cscockpitCheckoutTabPage.enterCVVValueInCheckoutTab(TestConstants.SECURITY_CODE);
		cscockpitCheckoutTabPage.clickUseThisCardBtnInCheckoutTab();
		cscockpitCheckoutTabPage.clickPlaceOrderButtonInCheckoutTab();
		double subTotal = cscockpitOrderTabPage.getSubTotalFromUI();
		double deliveryCost = cscockpitOrderTabPage.getDeliveryCostFromUI();
		double handlingCost = cscockpitOrderTabPage.getHandlingCostFromUI();
		if(cscockpitCheckoutTabPage.isGSTTaxPresentInUI()==true){
			gstFromUI = cscockpitOrderTabPage.getGstAmountFromUI();
			calculatedGstAmount = cscockpitOrderTabPage.calculateAmountAccordingToPercent(subTotal, deliveryCost, handlingCost, TestConstants.FIVE_PERCENT_TAX);
			s_assert.assertTrue(cscockpitOrderTabPage.validateGstAmountOnUI(gstFromUI,calculatedGstAmount),"GST Amount on UI is "+gstFromUI+" and calculated GST Amount is "+calculatedGstAmount+"");
		}else{
			logger.info("GST NOT PRESENT FOR THIS ORDER");
		}
		if(cscockpitOrderTabPage.isPstTaxPresentInUI() == true){
			pstFromUI = cscockpitOrderTabPage.getPstAmountFromUI();
			calculatedPstAmount = cscockpitOrderTabPage.calculateAmountAccordingToPercent(subTotal, deliveryCost, handlingCost, TestConstants.SEVEN_PERCENT_TAX);
			s_assert.assertTrue(cscockpitOrderTabPage.validatePstAmountOnUI(pstFromUI,calculatedPstAmount),"PST Amount on UI is "+pstFromUI+" and calculated GST Amount is "+calculatedPstAmount);
		}else{
			logger.info("PST NOT PRESENT FOR THIS ORDER");
		}
		s_assert.assertAll();
	}

	//Hybris Project-1557:To verify the Canada tax for Yukon for Consultant Order
	@Test
	public void testToVerifyCanadaTaxForYukonForConsultantOrder_1557(){ 
		int randomNumber=CommonUtils.getRandomNum(10000, 1000000);
		String randomCustomerSequenceNumber = null;
		String randomProductSequenceNumber = null;
		String SKUValue = null;
		String attendentFirstName=TestConstants.FIRST_NAME+randomNumber;
		String attendeeLastName = TestConstants.LAST_NAME+randomNumber;
		String  addressLine=TestConstants.ADDRESS_LINE_1_CA;
		String city=TestConstants.CITY_CA;
		String postalCode=TestConstants.POSTAL_CODE_CA;
		String Country=TestConstants.COUNTRY_DD_VALUE_CA;
		String province=TestConstants.PROVINCE_YUKON;
		String phoneNumber=TestConstants.PHONE_NUMBER_CA;
		double gstFromUI = 0.00;
		double calculatedGstAmount = 0.00;
		double pstFromUI = 0.00;
		double calculatedPstAmount = 0.00;
		cscockpitCustomerSearchTabPage = cscockpitLoginPage.clickLoginBtn();
		cscockpitCustomerSearchTabPage.selectCustomerTypeFromDropDownInCustomerSearchTab("CONSULTANT");
		cscockpitCustomerSearchTabPage.selectCountryFromDropDownInCustomerSearchTab("Canada");
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
		cscockpitCustomerTabPage.enterShippingInfoInAddNewPaymentProfilePopupWithoutSaveBtn(attendentFirstName, attendeeLastName, addressLine, city, postalCode, Country, province, phoneNumber);
		cscockpitCheckoutTabPage.clickOnCreateNewAddressButtonInAutoshipTemplateTabPage();
		cscockpitCheckoutTabPage.clickUseEnteredAddressOfCreateNewAddressShippingAddressPopup();
		cscockpitCheckoutTabPage.clickAddNewPaymentAddressInCheckoutTab();
		cscockpitCheckoutTabPage.enterBillingInfo();
		cscockpitCheckoutTabPage.clickSaveAddNewPaymentProfilePopUP();
		cscockpitCheckoutTabPage.enterCVVValueInCheckoutTab(TestConstants.SECURITY_CODE);
		cscockpitCheckoutTabPage.clickUseThisCardBtnInCheckoutTab();
		cscockpitCheckoutTabPage.clickPlaceOrderButtonInCheckoutTab();
		double subTotal = cscockpitOrderTabPage.getSubTotalFromUI();
		double deliveryCost = cscockpitOrderTabPage.getDeliveryCostFromUI();
		double handlingCost = cscockpitOrderTabPage.getHandlingCostFromUI();
		if(cscockpitCheckoutTabPage.isGSTTaxPresentInUI()==true){
			gstFromUI = cscockpitOrderTabPage.getGstAmountFromUI();
			calculatedGstAmount = cscockpitOrderTabPage.calculateAmountAccordingToPercent(subTotal, deliveryCost, handlingCost, TestConstants.FIVE_PERCENT_TAX);
			s_assert.assertTrue(cscockpitOrderTabPage.validateGstAmountOnUI(gstFromUI,calculatedGstAmount),"GST Amount on UI is "+gstFromUI+" and calculated GST Amount is "+calculatedGstAmount+"");
		}else{
			logger.info("GST NOT PRESENT FOR THIS ORDER");
		}
		if(cscockpitOrderTabPage.isPstTaxPresentInUI() == true){
			pstFromUI = cscockpitOrderTabPage.getPstAmountFromUI();
			calculatedPstAmount = cscockpitOrderTabPage.calculateAmountAccordingToPercent(subTotal, deliveryCost, handlingCost, TestConstants.SEVEN_PERCENT_TAX);
			s_assert.assertTrue(cscockpitOrderTabPage.validatePstAmountOnUI(pstFromUI,calculatedPstAmount),"PST Amount on UI is "+pstFromUI+" and calculated GST Amount is "+calculatedPstAmount);
		}else{
			logger.info("PST NOT PRESENT FOR THIS ORDER");
		}
		s_assert.assertAll();
	}

	//Hybris Project-1558:To verify the Canada tax for Northwest Territories for Consultant Order
	@Test
	public void testToVerifyCanadaTaxForNorthwestTerritoriesForConsultantOrder_1558(){ 
		int randomNumber=CommonUtils.getRandomNum(10000, 1000000);
		String randomCustomerSequenceNumber = null;
		String randomProductSequenceNumber = null;
		String SKUValue = null;
		String attendentFirstName=TestConstants.FIRST_NAME+randomNumber;
		String attendeeLastName = TestConstants.LAST_NAME+randomNumber;
		String  addressLine=TestConstants.ADDRESS_LINE_1_CA;
		String city=TestConstants.CITY_CA;
		String postalCode=TestConstants.POSTAL_CODE_CA;
		String Country=TestConstants.COUNTRY_DD_VALUE_CA;
		String province=TestConstants.PROVINCE_NORTHWEST_TERRITORIES;
		String phoneNumber=TestConstants.PHONE_NUMBER_CA;
		double gstFromUI = 0.00;
		double calculatedGstAmount = 0.00;
		double pstFromUI = 0.00;
		double calculatedPstAmount = 0.00;
		cscockpitCustomerSearchTabPage = cscockpitLoginPage.clickLoginBtn();
		cscockpitCustomerSearchTabPage.selectCustomerTypeFromDropDownInCustomerSearchTab("CONSULTANT");
		cscockpitCustomerSearchTabPage.selectCountryFromDropDownInCustomerSearchTab("Canada");
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
		cscockpitCustomerTabPage.enterShippingInfoInAddNewPaymentProfilePopupWithoutSaveBtn(attendentFirstName, attendeeLastName, addressLine, city, postalCode, Country, province, phoneNumber);
		cscockpitCheckoutTabPage.clickOnCreateNewAddressButtonInAutoshipTemplateTabPage();
		cscockpitCheckoutTabPage.clickUseEnteredAddressOfCreateNewAddressShippingAddressPopup();
		cscockpitCheckoutTabPage.clickAddNewPaymentAddressInCheckoutTab();
		cscockpitCheckoutTabPage.enterBillingInfo();
		cscockpitCheckoutTabPage.clickSaveAddNewPaymentProfilePopUP();
		cscockpitCheckoutTabPage.enterCVVValueInCheckoutTab(TestConstants.SECURITY_CODE);
		cscockpitCheckoutTabPage.clickUseThisCardBtnInCheckoutTab();
		cscockpitCheckoutTabPage.clickPlaceOrderButtonInCheckoutTab();
		double subTotal = cscockpitOrderTabPage.getSubTotalFromUI();
		double deliveryCost = cscockpitOrderTabPage.getDeliveryCostFromUI();
		double handlingCost = cscockpitOrderTabPage.getHandlingCostFromUI();
		if(cscockpitCheckoutTabPage.isGSTTaxPresentInUI()==true){
			gstFromUI = cscockpitOrderTabPage.getGstAmountFromUI();
			calculatedGstAmount = cscockpitOrderTabPage.calculateAmountAccordingToPercent(subTotal, deliveryCost, handlingCost, TestConstants.FIVE_PERCENT_TAX);
			s_assert.assertTrue(cscockpitOrderTabPage.validateGstAmountOnUI(gstFromUI,calculatedGstAmount),"GST Amount on UI is "+gstFromUI+" and calculated GST Amount is "+calculatedGstAmount+"");
		}else{
			logger.info("GST NOT PRESENT FOR THIS ORDER");
		}
		if(cscockpitOrderTabPage.isPstTaxPresentInUI() == true){
			pstFromUI = cscockpitOrderTabPage.getPstAmountFromUI();
			calculatedPstAmount = cscockpitOrderTabPage.calculateAmountAccordingToPercent(subTotal, deliveryCost, handlingCost, TestConstants.SEVEN_PERCENT_TAX);
			s_assert.assertTrue(cscockpitOrderTabPage.validatePstAmountOnUI(pstFromUI,calculatedPstAmount),"PST Amount on UI is "+pstFromUI+" and calculated GST Amount is "+calculatedPstAmount);
		}else{
			logger.info("PST NOT PRESENT FOR THIS ORDER");
		}
		s_assert.assertAll();
	}

	//Hybris Project-1559:To verify the Canada tax for Nunavat for Consultant Order
	@Test
	public void testToVerifyCanadaTaxForNunavatForConsultantOrder_1559(){ 
		int randomNumber=CommonUtils.getRandomNum(10000, 1000000);
		String randomCustomerSequenceNumber = null;
		String randomProductSequenceNumber = null;
		String SKUValue = null;
		String attendentFirstName=TestConstants.FIRST_NAME+randomNumber;
		String attendeeLastName = TestConstants.LAST_NAME+randomNumber;
		String  addressLine=TestConstants.ADDRESS_LINE_1_CA;
		String city=TestConstants.CITY_CA;
		String postalCode=TestConstants.POSTAL_CODE_CA;
		String Country=TestConstants.COUNTRY_DD_VALUE_CA;
		String province=TestConstants.PROVINCE_NUNAVUT;
		String phoneNumber=TestConstants.PHONE_NUMBER_CA;
		double gstFromUI = 0.00;
		double calculatedGstAmount = 0.00;
		double pstFromUI = 0.00;
		double calculatedPstAmount = 0.00;
		cscockpitCustomerSearchTabPage = cscockpitLoginPage.clickLoginBtn();
		cscockpitCustomerSearchTabPage.selectCustomerTypeFromDropDownInCustomerSearchTab("CONSULTANT");
		cscockpitCustomerSearchTabPage.selectCountryFromDropDownInCustomerSearchTab("Canada");
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
		cscockpitCustomerTabPage.enterShippingInfoInAddNewPaymentProfilePopupWithoutSaveBtn(attendentFirstName, attendeeLastName, addressLine, city, postalCode, Country, province, phoneNumber);
		cscockpitCheckoutTabPage.clickOnCreateNewAddressButtonInAutoshipTemplateTabPage();
		cscockpitCheckoutTabPage.clickUseEnteredAddressOfCreateNewAddressShippingAddressPopup();
		cscockpitCheckoutTabPage.clickAddNewPaymentAddressInCheckoutTab();
		cscockpitCheckoutTabPage.enterBillingInfo();
		cscockpitCheckoutTabPage.clickSaveAddNewPaymentProfilePopUP();
		cscockpitCheckoutTabPage.enterCVVValueInCheckoutTab(TestConstants.SECURITY_CODE);
		cscockpitCheckoutTabPage.clickUseThisCardBtnInCheckoutTab();
		cscockpitCheckoutTabPage.clickPlaceOrderButtonInCheckoutTab();
		double subTotal = cscockpitOrderTabPage.getSubTotalFromUI();
		double deliveryCost = cscockpitOrderTabPage.getDeliveryCostFromUI();
		double handlingCost = cscockpitOrderTabPage.getHandlingCostFromUI();
		if(cscockpitCheckoutTabPage.isGSTTaxPresentInUI()==true){
			gstFromUI = cscockpitOrderTabPage.getGstAmountFromUI();
			calculatedGstAmount = cscockpitOrderTabPage.calculateAmountAccordingToPercent(subTotal, deliveryCost, handlingCost, TestConstants.FIVE_PERCENT_TAX);
			s_assert.assertTrue(cscockpitOrderTabPage.validateGstAmountOnUI(gstFromUI,calculatedGstAmount),"GST Amount on UI is "+gstFromUI+" and calculated GST Amount is "+calculatedGstAmount+"");
		}else{
			logger.info("GST NOT PRESENT FOR THIS ORDER");
		}
		if(cscockpitOrderTabPage.isPstTaxPresentInUI() == true){
			pstFromUI = cscockpitOrderTabPage.getPstAmountFromUI();
			calculatedPstAmount = cscockpitOrderTabPage.calculateAmountAccordingToPercent(subTotal, deliveryCost, handlingCost, TestConstants.SEVEN_PERCENT_TAX);
			s_assert.assertTrue(cscockpitOrderTabPage.validatePstAmountOnUI(pstFromUI,calculatedPstAmount),"PST Amount on UI is "+pstFromUI+" and calculated GST Amount is "+calculatedPstAmount);
		}else{
			logger.info("PST NOT PRESENT FOR THIS ORDER");
		}
		s_assert.assertAll();
	}

	//Hybris Project-1560:To verify the Canada tax for Alberta for Retail Order
	@Test
	public void testToVerifyCanadaTaxForAlbertaForRCOrder_1560(){ 
		int randomNumber=CommonUtils.getRandomNum(10000, 1000000);
		String randomCustomerSequenceNumber = null;
		String randomProductSequenceNumber = null;
		String SKUValue = null;
		String attendentFirstName=TestConstants.FIRST_NAME+randomNumber;
		String attendeeLastName = TestConstants.LAST_NAME+randomNumber;
		String  addressLine=TestConstants.ADDRESS_LINE_1_CA;
		String city=TestConstants.CITY_CA;
		String postalCode=TestConstants.POSTAL_CODE_CA;
		String Country=TestConstants.COUNTRY_DD_VALUE_CA;
		String province=TestConstants.PROVINCE_ALBERTA;
		String phoneNumber=TestConstants.PHONE_NUMBER_CA;
		double gstFromUI = 0.00;
		double calculatedGstAmount = 0.00;
		double pstFromUI = 0.00;
		double calculatedPstAmount = 0.00;
		cscockpitCustomerSearchTabPage = cscockpitLoginPage.clickLoginBtn();
		cscockpitCustomerSearchTabPage.selectCustomerTypeFromDropDownInCustomerSearchTab("RETAIL");
		cscockpitCustomerSearchTabPage.selectCountryFromDropDownInCustomerSearchTab("Canada");
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
		cscockpitCustomerTabPage.enterShippingInfoInAddNewPaymentProfilePopupWithoutSaveBtn(attendentFirstName, attendeeLastName, addressLine, city, postalCode, Country, province, phoneNumber);
		cscockpitCheckoutTabPage.clickOnCreateNewAddressButtonInAutoshipTemplateTabPage();
		cscockpitCheckoutTabPage.clickUseEnteredAddressOfCreateNewAddressShippingAddressPopup();
		cscockpitCheckoutTabPage.clickAddNewPaymentAddressInCheckoutTab();
		cscockpitCheckoutTabPage.enterBillingInfo();
		cscockpitCheckoutTabPage.clickSaveAddNewPaymentProfilePopUP();
		cscockpitCheckoutTabPage.enterCVVValueInCheckoutTab(TestConstants.SECURITY_CODE);
		cscockpitCheckoutTabPage.clickUseThisCardBtnInCheckoutTab();
		cscockpitCheckoutTabPage.clickPlaceOrderButtonInCheckoutTab();
		double subTotal = cscockpitOrderTabPage.getSubTotalFromUI();
		double deliveryCost = cscockpitOrderTabPage.getDeliveryCostFromUI();
		double handlingCost = cscockpitOrderTabPage.getHandlingCostFromUI();
		if(cscockpitCheckoutTabPage.isGSTTaxPresentInUI()==true){
			gstFromUI = cscockpitOrderTabPage.getGstAmountFromUI();
			calculatedGstAmount = cscockpitOrderTabPage.calculateAmountAccordingToPercent(subTotal, deliveryCost, handlingCost, TestConstants.FIVE_PERCENT_TAX);
			s_assert.assertTrue(cscockpitOrderTabPage.validateGstAmountOnUI(gstFromUI,calculatedGstAmount),"GST Amount on UI is "+gstFromUI+" and calculated GST Amount is "+calculatedGstAmount+"");
		}else{
			logger.info("GST NOT PRESENT FOR THIS ORDER");
		}
		if(cscockpitOrderTabPage.isPstTaxPresentInUI() == true){
			pstFromUI = cscockpitOrderTabPage.getPstAmountFromUI();
			calculatedPstAmount = cscockpitOrderTabPage.calculateAmountAccordingToPercent(subTotal, deliveryCost, handlingCost, TestConstants.SEVEN_PERCENT_TAX);
			s_assert.assertTrue(cscockpitOrderTabPage.validatePstAmountOnUI(pstFromUI,calculatedPstAmount),"PST Amount on UI is "+pstFromUI+" and calculated GST Amount is "+calculatedPstAmount);
		}else{
			logger.info("PST NOT PRESENT FOR THIS ORDER");
		}
		s_assert.assertAll();
	}

	//Hybris Project-1561:To verify the Canada tax for saskatchewan for Retail Order
	@Test
	public void testToVerifyCanadaTaxForSaskatchewanForRCOrder_1561(){ 
		int randomNumber=CommonUtils.getRandomNum(10000, 1000000);
		String randomCustomerSequenceNumber = null;
		String randomProductSequenceNumber = null;
		String SKUValue = null;
		String attendentFirstName=TestConstants.FIRST_NAME+randomNumber;
		String attendeeLastName = TestConstants.LAST_NAME+randomNumber;
		String  addressLine=TestConstants.ADDRESS_LINE_1_CA;
		String city=TestConstants.CITY_CA;
		String postalCode=TestConstants.POSTAL_CODE_CA;
		String Country=TestConstants.COUNTRY_DD_VALUE_CA;
		String province=TestConstants.PROVINCE_SASKATCHEWAN;
		String phoneNumber=TestConstants.PHONE_NUMBER_CA;
		double gstFromUI = 0.00;
		double calculatedGstAmount = 0.00;
		double pstFromUI = 0.00;
		double calculatedPstAmount = 0.00;
		cscockpitCustomerSearchTabPage = cscockpitLoginPage.clickLoginBtn();
		cscockpitCustomerSearchTabPage.selectCustomerTypeFromDropDownInCustomerSearchTab("RETAIL");
		cscockpitCustomerSearchTabPage.selectCountryFromDropDownInCustomerSearchTab("Canada");
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
		cscockpitCustomerTabPage.enterShippingInfoInAddNewPaymentProfilePopupWithoutSaveBtn(attendentFirstName, attendeeLastName, addressLine, city, postalCode, Country, province, phoneNumber);
		cscockpitCheckoutTabPage.clickOnCreateNewAddressButtonInAutoshipTemplateTabPage();
		cscockpitCheckoutTabPage.clickUseEnteredAddressOfCreateNewAddressShippingAddressPopup();
		cscockpitCheckoutTabPage.clickAddNewPaymentAddressInCheckoutTab();
		cscockpitCheckoutTabPage.enterBillingInfo();
		cscockpitCheckoutTabPage.clickSaveAddNewPaymentProfilePopUP();
		cscockpitCheckoutTabPage.enterCVVValueInCheckoutTab(TestConstants.SECURITY_CODE);
		cscockpitCheckoutTabPage.clickUseThisCardBtnInCheckoutTab();
		cscockpitCheckoutTabPage.clickPlaceOrderButtonInCheckoutTab();
		double subTotal = cscockpitOrderTabPage.getSubTotalFromUI();
		double deliveryCost = cscockpitOrderTabPage.getDeliveryCostFromUI();
		double handlingCost = cscockpitOrderTabPage.getHandlingCostFromUI();
		if(cscockpitCheckoutTabPage.isGSTTaxPresentInUI()==true){
			gstFromUI = cscockpitOrderTabPage.getGstAmountFromUI();
			calculatedGstAmount = cscockpitOrderTabPage.calculateAmountAccordingToPercent(subTotal, deliveryCost, handlingCost, TestConstants.FIVE_PERCENT_TAX);
			s_assert.assertTrue(cscockpitOrderTabPage.validateGstAmountOnUI(gstFromUI,calculatedGstAmount),"GST Amount on UI is "+gstFromUI+" and calculated GST Amount is "+calculatedGstAmount+"");
		}else{
			logger.info("GST NOT PRESENT FOR THIS ORDER");
		}
		if(cscockpitOrderTabPage.isPstTaxPresentInUI() == true){
			pstFromUI = cscockpitOrderTabPage.getPstAmountFromUI();
			calculatedPstAmount = cscockpitOrderTabPage.calculateAmountAccordingToPercent(subTotal, deliveryCost, handlingCost, TestConstants.FIVE_PERCENT_TAX);
			s_assert.assertTrue(cscockpitOrderTabPage.validatePstAmountOnUI(pstFromUI,calculatedPstAmount),"PST Amount on UI is "+pstFromUI+" and calculated GST Amount is "+calculatedPstAmount);
		}else{
			logger.info("PST NOT PRESENT FOR THIS ORDER");
		}
		s_assert.assertAll();
	}

	//Hybris Project-1562:To verify the Canada tax for Manitoba for Retail Order
	@Test
	public void testToVerifyCanadaTaxForManitobaForRCOrder_1562(){ 
		int randomNumber=CommonUtils.getRandomNum(10000, 1000000);
		String randomCustomerSequenceNumber = null;
		String randomProductSequenceNumber = null;
		String SKUValue = null;
		String attendentFirstName=TestConstants.FIRST_NAME+randomNumber;
		String attendeeLastName = TestConstants.LAST_NAME+randomNumber;
		String  addressLine=TestConstants.ADDRESS_LINE_1_CA;
		String city=TestConstants.CITY_CA;
		String postalCode=TestConstants.POSTAL_CODE_CA;
		String Country=TestConstants.COUNTRY_DD_VALUE_CA;
		String province=TestConstants.PROVINCE_MANITOBA;
		String phoneNumber=TestConstants.PHONE_NUMBER_CA;
		double gstFromUI = 0.00;
		double calculatedGstAmount = 0.00;
		double pstFromUI = 0.00;
		double calculatedPstAmount = 0.00;
		cscockpitCustomerSearchTabPage = cscockpitLoginPage.clickLoginBtn();
		cscockpitCustomerSearchTabPage.selectCustomerTypeFromDropDownInCustomerSearchTab("RETAIL");
		cscockpitCustomerSearchTabPage.selectCountryFromDropDownInCustomerSearchTab("Canada");
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
		cscockpitCustomerTabPage.enterShippingInfoInAddNewPaymentProfilePopupWithoutSaveBtn(attendentFirstName, attendeeLastName, addressLine, city, postalCode, Country, province, phoneNumber);
		cscockpitCheckoutTabPage.clickOnCreateNewAddressButtonInAutoshipTemplateTabPage();
		cscockpitCheckoutTabPage.clickUseEnteredAddressOfCreateNewAddressShippingAddressPopup();
		cscockpitCheckoutTabPage.clickAddNewPaymentAddressInCheckoutTab();
		cscockpitCheckoutTabPage.enterBillingInfo();
		cscockpitCheckoutTabPage.clickSaveAddNewPaymentProfilePopUP();
		cscockpitCheckoutTabPage.enterCVVValueInCheckoutTab(TestConstants.SECURITY_CODE);
		cscockpitCheckoutTabPage.clickUseThisCardBtnInCheckoutTab();
		cscockpitCheckoutTabPage.clickPlaceOrderButtonInCheckoutTab();
		double subTotal = cscockpitOrderTabPage.getSubTotalFromUI();
		double deliveryCost = cscockpitOrderTabPage.getDeliveryCostFromUI();
		double handlingCost = cscockpitOrderTabPage.getHandlingCostFromUI();
		if(cscockpitCheckoutTabPage.isGSTTaxPresentInUI()==true){
			gstFromUI = cscockpitOrderTabPage.getGstAmountFromUI();
			calculatedGstAmount = cscockpitOrderTabPage.calculateAmountAccordingToPercent(subTotal, deliveryCost, handlingCost, TestConstants.FIVE_PERCENT_TAX);
			s_assert.assertTrue(cscockpitOrderTabPage.validateGstAmountOnUI(gstFromUI,calculatedGstAmount),"GST Amount on UI is "+gstFromUI+" and calculated GST Amount is "+calculatedGstAmount+"");
		}else{
			logger.info("GST NOT PRESENT FOR THIS ORDER");
		}
		if(cscockpitOrderTabPage.isPstTaxPresentInUI() == true){
			pstFromUI = cscockpitOrderTabPage.getPstAmountFromUI();
			calculatedPstAmount = cscockpitOrderTabPage.calculateAmountAccordingToPercent(subTotal, deliveryCost, handlingCost, TestConstants.EIGHT_PERCENT_TAX);
			s_assert.assertTrue(cscockpitOrderTabPage.validatePstAmountOnUI(pstFromUI,calculatedPstAmount),"PST Amount on UI is "+pstFromUI+" and calculated GST Amount is "+calculatedPstAmount);
		}else{
			logger.info("PST NOT PRESENT FOR THIS ORDER");
		}
		s_assert.assertAll();
	}

	//Hybris Project-1563:To verify the Canada tax for Ontario for Retail Order
	@Test
	public void testToVerifyCanadaTaxForOntarioForRCOrder_1563(){ 
		int randomNumber=CommonUtils.getRandomNum(10000, 1000000);
		String randomCustomerSequenceNumber = null;
		String randomProductSequenceNumber = null;
		String SKUValue = null;
		String attendentFirstName=TestConstants.FIRST_NAME+randomNumber;
		String attendeeLastName = TestConstants.LAST_NAME+randomNumber;
		String  addressLine=TestConstants.ADDRESS_LINE_1_CA;
		String city=TestConstants.CITY_CA;
		String postalCode=TestConstants.POSTAL_CODE_CA;
		String Country=TestConstants.COUNTRY_DD_VALUE_CA;
		String province=TestConstants.PROVINCE_ONTARIO;
		String phoneNumber=TestConstants.PHONE_NUMBER_CA;
		double gstFromUI = 0.00;
		double calculatedGstAmount = 0.00;
		double pstFromUI = 0.00;
		double calculatedPstAmount = 0.00;
		cscockpitCustomerSearchTabPage = cscockpitLoginPage.clickLoginBtn();
		cscockpitCustomerSearchTabPage.selectCustomerTypeFromDropDownInCustomerSearchTab("RETAIL");
		cscockpitCustomerSearchTabPage.selectCountryFromDropDownInCustomerSearchTab("Canada");
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
		cscockpitCustomerTabPage.enterShippingInfoInAddNewPaymentProfilePopupWithoutSaveBtn(attendentFirstName, attendeeLastName, addressLine, city, postalCode, Country, province, phoneNumber);
		cscockpitCheckoutTabPage.clickOnCreateNewAddressButtonInAutoshipTemplateTabPage();
		cscockpitCheckoutTabPage.clickUseEnteredAddressOfCreateNewAddressShippingAddressPopup();
		cscockpitCheckoutTabPage.clickAddNewPaymentAddressInCheckoutTab();
		cscockpitCheckoutTabPage.enterBillingInfo();
		cscockpitCheckoutTabPage.clickSaveAddNewPaymentProfilePopUP();
		cscockpitCheckoutTabPage.enterCVVValueInCheckoutTab(TestConstants.SECURITY_CODE);
		cscockpitCheckoutTabPage.clickUseThisCardBtnInCheckoutTab();
		cscockpitCheckoutTabPage.clickPlaceOrderButtonInCheckoutTab();
		double subTotal = cscockpitOrderTabPage.getSubTotalFromUI();
		double deliveryCost = cscockpitOrderTabPage.getDeliveryCostFromUI();
		double handlingCost = cscockpitOrderTabPage.getHandlingCostFromUI();
		if(cscockpitCheckoutTabPage.isGSTTaxPresentInUI()==true){
			gstFromUI = cscockpitOrderTabPage.getGstAmountFromUI();
			calculatedGstAmount = cscockpitOrderTabPage.calculateAmountAccordingToPercent(subTotal, deliveryCost, handlingCost, TestConstants.FIVE_PERCENT_TAX);
			s_assert.assertTrue(cscockpitOrderTabPage.validateGstAmountOnUI(gstFromUI,calculatedGstAmount),"GST Amount on UI is "+gstFromUI+" and calculated GST Amount is "+calculatedGstAmount+"");
		}else{
			logger.info("GST NOT PRESENT FOR THIS ORDER");
		}
		if(cscockpitOrderTabPage.isPstTaxPresentInUI() == true){
			pstFromUI = cscockpitOrderTabPage.getPstAmountFromUI();
			calculatedPstAmount = cscockpitOrderTabPage.calculateAmountAccordingToPercent(subTotal, deliveryCost, handlingCost, TestConstants.SEVEN_PERCENT_TAX);
			s_assert.assertTrue(cscockpitOrderTabPage.validatePstAmountOnUI(pstFromUI,calculatedPstAmount),"PST Amount on UI is "+pstFromUI+" and calculated GST Amount is "+calculatedPstAmount);
		}else{
			logger.info("PST NOT PRESENT FOR THIS ORDER");
		}
		s_assert.assertAll();
	}

	//Hybris Project-1564:To verify the Canada tax for Quebec for Retail Order
	@Test
	public void testToVerifyCanadaTaxForQuebecForRCOrder_1564(){ 
		int randomNumber=CommonUtils.getRandomNum(10000, 1000000);
		String randomCustomerSequenceNumber = null;
		String randomProductSequenceNumber = null;
		String SKUValue = null;
		String attendentFirstName=TestConstants.FIRST_NAME+randomNumber;
		String attendeeLastName = TestConstants.LAST_NAME+randomNumber;
		String  addressLine=TestConstants.ADDRESS_LINE_1_CA;
		String city=TestConstants.CITY_CA;
		String postalCode=TestConstants.POSTAL_CODE_CA;
		String Country=TestConstants.COUNTRY_DD_VALUE_CA;
		String province=TestConstants.PROVINCE_QUEBEC;
		String phoneNumber=TestConstants.PHONE_NUMBER_CA;
		double gstFromUI = 0.00;
		double calculatedGstAmount = 0.00;
		double pstFromUI = 0.00;
		double calculatedPstAmount = 0.00;
		cscockpitCustomerSearchTabPage = cscockpitLoginPage.clickLoginBtn();
		cscockpitCustomerSearchTabPage.selectCustomerTypeFromDropDownInCustomerSearchTab("RETAIL");
		cscockpitCustomerSearchTabPage.selectCountryFromDropDownInCustomerSearchTab("Canada");
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
		cscockpitCustomerTabPage.enterShippingInfoInAddNewPaymentProfilePopupWithoutSaveBtn(attendentFirstName, attendeeLastName, addressLine, city, postalCode, Country, province, phoneNumber);
		cscockpitCheckoutTabPage.clickOnCreateNewAddressButtonInAutoshipTemplateTabPage();
		cscockpitCheckoutTabPage.clickUseEnteredAddressOfCreateNewAddressShippingAddressPopup();
		cscockpitCheckoutTabPage.clickAddNewPaymentAddressInCheckoutTab();
		cscockpitCheckoutTabPage.enterBillingInfo();
		cscockpitCheckoutTabPage.clickSaveAddNewPaymentProfilePopUP();
		cscockpitCheckoutTabPage.enterCVVValueInCheckoutTab(TestConstants.SECURITY_CODE);
		cscockpitCheckoutTabPage.clickUseThisCardBtnInCheckoutTab();
		cscockpitCheckoutTabPage.clickPlaceOrderButtonInCheckoutTab();
		double subTotal = cscockpitOrderTabPage.getSubTotalFromUI();
		double deliveryCost = cscockpitOrderTabPage.getDeliveryCostFromUI();
		double handlingCost = cscockpitOrderTabPage.getHandlingCostFromUI();
		if(cscockpitCheckoutTabPage.isGSTTaxPresentInUI()==true){
			gstFromUI = cscockpitOrderTabPage.getGstAmountFromUI();
			calculatedGstAmount = cscockpitOrderTabPage.calculateAmountAccordingToPercent(subTotal, deliveryCost, handlingCost, TestConstants.FIVE_PERCENT_TAX);
			s_assert.assertTrue(cscockpitOrderTabPage.validateGstAmountOnUI(gstFromUI,calculatedGstAmount),"GST Amount on UI is "+gstFromUI+" and calculated GST Amount is "+calculatedGstAmount+"");
		}else{
			logger.info("GST NOT PRESENT FOR THIS ORDER");
		}
		if(cscockpitOrderTabPage.isPstTaxPresentInUI() == true){
			pstFromUI = cscockpitOrderTabPage.getPstAmountFromUI();
			calculatedPstAmount = cscockpitOrderTabPage.calculateAmountAccordingToPercent(subTotal, deliveryCost, handlingCost, TestConstants.NINE_PERCENT_TAX);
			s_assert.assertTrue(cscockpitOrderTabPage.validatePstAmountOnUI(pstFromUI,calculatedPstAmount),"PST Amount on UI is "+pstFromUI+" and calculated GST Amount is "+calculatedPstAmount);
		}else{
			logger.info("PST NOT PRESENT FOR THIS ORDER");
		}
		s_assert.assertAll();
	}

	//Hybris Project-1565:To verify the Canada tax for Nova Scotia for Retail Order
	@Test
	public void testToVerifyCanadaTaxForNovaScotiaForRCOrder_1565(){ 
		int randomNumber=CommonUtils.getRandomNum(10000, 1000000);
		String randomCustomerSequenceNumber = null;
		String randomProductSequenceNumber = null;
		String SKUValue = null;
		String attendentFirstName=TestConstants.FIRST_NAME+randomNumber;
		String attendeeLastName = TestConstants.LAST_NAME+randomNumber;
		String  addressLine=TestConstants.ADDRESS_LINE_1_CA;
		String city=TestConstants.CITY_CA;
		String postalCode=TestConstants.POSTAL_CODE_CA;
		String Country=TestConstants.COUNTRY_DD_VALUE_CA;
		String province=TestConstants.PROVINCE_NOVA_SCOTIA;
		String phoneNumber=TestConstants.PHONE_NUMBER_CA;
		double gstFromUI = 0.00;
		double calculatedGstAmount = 0.00;
		double pstFromUI = 0.00;
		double calculatedPstAmount = 0.00;
		cscockpitCustomerSearchTabPage = cscockpitLoginPage.clickLoginBtn();
		cscockpitCustomerSearchTabPage.selectCustomerTypeFromDropDownInCustomerSearchTab("RETAIL");
		cscockpitCustomerSearchTabPage.selectCountryFromDropDownInCustomerSearchTab("Canada");
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
		cscockpitCustomerTabPage.enterShippingInfoInAddNewPaymentProfilePopupWithoutSaveBtn(attendentFirstName, attendeeLastName, addressLine, city, postalCode, Country, province, phoneNumber);
		cscockpitCheckoutTabPage.clickOnCreateNewAddressButtonInAutoshipTemplateTabPage();
		cscockpitCheckoutTabPage.clickUseEnteredAddressOfCreateNewAddressShippingAddressPopup();
		cscockpitCheckoutTabPage.clickAddNewPaymentAddressInCheckoutTab();
		cscockpitCheckoutTabPage.enterBillingInfo();
		cscockpitCheckoutTabPage.clickSaveAddNewPaymentProfilePopUP();
		cscockpitCheckoutTabPage.enterCVVValueInCheckoutTab(TestConstants.SECURITY_CODE);
		cscockpitCheckoutTabPage.clickUseThisCardBtnInCheckoutTab();
		cscockpitCheckoutTabPage.clickPlaceOrderButtonInCheckoutTab();
		double subTotal = cscockpitOrderTabPage.getSubTotalFromUI();
		double deliveryCost = cscockpitOrderTabPage.getDeliveryCostFromUI();
		double handlingCost = cscockpitOrderTabPage.getHandlingCostFromUI();
		if(cscockpitCheckoutTabPage.isGSTTaxPresentInUI()==true){
			gstFromUI = cscockpitOrderTabPage.getGstAmountFromUI();
			calculatedGstAmount = cscockpitOrderTabPage.calculateAmountAccordingToPercent(subTotal, deliveryCost, handlingCost, TestConstants.FIVE_PERCENT_TAX);
			s_assert.assertTrue(cscockpitOrderTabPage.validateGstAmountOnUI(gstFromUI,calculatedGstAmount),"GST Amount on UI is "+gstFromUI+" and calculated GST Amount is "+calculatedGstAmount+"");
		}else{
			logger.info("GST NOT PRESENT FOR THIS ORDER");
		}
		if(cscockpitOrderTabPage.isPstTaxPresentInUI() == true){
			pstFromUI = cscockpitOrderTabPage.getPstAmountFromUI();
			calculatedPstAmount = cscockpitOrderTabPage.calculateAmountAccordingToPercent(subTotal, deliveryCost, handlingCost, TestConstants.SEVEN_PERCENT_TAX);
			s_assert.assertTrue(cscockpitOrderTabPage.validatePstAmountOnUI(pstFromUI,calculatedPstAmount),"PST Amount on UI is "+pstFromUI+" and calculated GST Amount is "+calculatedPstAmount);
		}else{
			logger.info("PST NOT PRESENT FOR THIS ORDER");
		}
		s_assert.assertAll();
	}

	//Hybris Project-1566:To verify the Canada tax for New Brunswick for Retail Order
	@Test
	public void testToVerifyCanadaTaxForNewBrunswickForRCOrder_1566(){ 
		int randomNumber=CommonUtils.getRandomNum(10000, 1000000);
		String randomCustomerSequenceNumber = null;
		String randomProductSequenceNumber = null;
		String SKUValue = null;
		String attendentFirstName=TestConstants.FIRST_NAME+randomNumber;
		String attendeeLastName = TestConstants.LAST_NAME+randomNumber;
		String  addressLine=TestConstants.ADDRESS_LINE_1_CA;
		String city=TestConstants.CITY_CA;
		String postalCode=TestConstants.POSTAL_CODE_CA;
		String Country=TestConstants.COUNTRY_DD_VALUE_CA;
		String province=TestConstants.PROVINCE_NEW_BRUNSWICK;
		String phoneNumber=TestConstants.PHONE_NUMBER_CA;
		double gstFromUI = 0.00;
		double calculatedGstAmount = 0.00;
		double pstFromUI = 0.00;
		double calculatedPstAmount = 0.00;
		cscockpitCustomerSearchTabPage = cscockpitLoginPage.clickLoginBtn();
		cscockpitCustomerSearchTabPage.selectCustomerTypeFromDropDownInCustomerSearchTab("RETAIL");
		cscockpitCustomerSearchTabPage.selectCountryFromDropDownInCustomerSearchTab("Canada");
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
		cscockpitCustomerTabPage.enterShippingInfoInAddNewPaymentProfilePopupWithoutSaveBtn(attendentFirstName, attendeeLastName, addressLine, city, postalCode, Country, province, phoneNumber);
		cscockpitCheckoutTabPage.clickOnCreateNewAddressButtonInAutoshipTemplateTabPage();
		cscockpitCheckoutTabPage.clickUseEnteredAddressOfCreateNewAddressShippingAddressPopup();
		cscockpitCheckoutTabPage.clickAddNewPaymentAddressInCheckoutTab();
		cscockpitCheckoutTabPage.enterBillingInfo();
		cscockpitCheckoutTabPage.clickSaveAddNewPaymentProfilePopUP();
		cscockpitCheckoutTabPage.enterCVVValueInCheckoutTab(TestConstants.SECURITY_CODE);
		cscockpitCheckoutTabPage.clickUseThisCardBtnInCheckoutTab();
		cscockpitCheckoutTabPage.clickPlaceOrderButtonInCheckoutTab();
		double subTotal = cscockpitOrderTabPage.getSubTotalFromUI();
		double deliveryCost = cscockpitOrderTabPage.getDeliveryCostFromUI();
		double handlingCost = cscockpitOrderTabPage.getHandlingCostFromUI();
		if(cscockpitCheckoutTabPage.isGSTTaxPresentInUI()==true){
			gstFromUI = cscockpitOrderTabPage.getGstAmountFromUI();
			calculatedGstAmount = cscockpitOrderTabPage.calculateAmountAccordingToPercent(subTotal, deliveryCost, handlingCost, TestConstants.FIVE_PERCENT_TAX);
			s_assert.assertTrue(cscockpitOrderTabPage.validateGstAmountOnUI(gstFromUI,calculatedGstAmount),"GST Amount on UI is "+gstFromUI+" and calculated GST Amount is "+calculatedGstAmount+"");
		}else{
			logger.info("GST NOT PRESENT FOR THIS ORDER");
		}
		if(cscockpitOrderTabPage.isPstTaxPresentInUI() == true){
			pstFromUI = cscockpitOrderTabPage.getPstAmountFromUI();
			calculatedPstAmount = cscockpitOrderTabPage.calculateAmountAccordingToPercent(subTotal, deliveryCost, handlingCost, TestConstants.SEVEN_PERCENT_TAX);
			s_assert.assertTrue(cscockpitOrderTabPage.validatePstAmountOnUI(pstFromUI,calculatedPstAmount),"PST Amount on UI is "+pstFromUI+" and calculated GST Amount is "+calculatedPstAmount);
		}else{
			logger.info("PST NOT PRESENT FOR THIS ORDER");
		}
		s_assert.assertAll();
	}

	//Hybris Project-1604:To verify the Canada tax for Yukon for Create PCPerks Autoship
	@Test
	public void testVerifyCanadaTaxForYukonForPCPerksAutoship_1604(){
		int randomNumber=CommonUtils.getRandomNum(10000, 1000000);
		String randomCustomerSequenceNumber = null;
		String randomProductSequenceNumber = null;
		String SKUValue = null;
		String attendentFirstName=TestConstants.FIRST_NAME+randomNumber;
		String attendeeLastName = TestConstants.LAST_NAME+randomNumber;
		String  addressLine=TestConstants.ADDRESS_LINE_1_CA;
		String city=TestConstants.CITY_CA;
		String postalCode=TestConstants.POSTAL_CODE_CA;
		String Country=TestConstants.COUNTRY_DD_VALUE_CA;
		String Province=TestConstants.PROVINCE_YUKON;
		String phoneNumber=TestConstants.PHONE_NUMBER_CA;
		double gstFromUI = 0.00;
		double calculatedGstAmount = 0.00;
		double pstFromUI = 0.00;
		double calculatedPstAmount = 0.00;
		//Login to cscockpit.
		cscockpitCustomerSearchTabPage = cscockpitLoginPage.clickLoginBtn();
		cscockpitCustomerSearchTabPage.selectCustomerTypeFromDropDownInCustomerSearchTab("PC");
		cscockpitCustomerSearchTabPage.selectCountryFromDropDownInCustomerSearchTab("Canada");
		cscockpitCustomerSearchTabPage.selectAccountStatusFromDropDownInCustomerSearchTab("Active");
		cscockpitCustomerSearchTabPage.clickSearchBtn();
		randomCustomerSequenceNumber = String.valueOf(cscockpitCustomerSearchTabPage.getRandomCustomerFromSearchResult());
		cscockpitCustomerSearchTabPage.clickCIDNumberInCustomerSearchTab(randomCustomerSequenceNumber);
		cscockpitCustomerTabPage.clickAutoshipIdOnCustomerTab();
		cscockpitAutoshipTemplateTabPage.clickEditTemplateLinkInAutoshipTemplateTab();
		s_assert.assertTrue(cscockpitAutoshipTemplateTabPage.verifyCancelEditLinkInAutoshipTemplateTab(),"Cancel Edit link is not on Autoship template Tab Page");
		cscockpitAutoshipTemplateTabPage.clickAddMoreLinesLinkInAutoShipTemplateTab();
		cscockpitAutoshipCartTabPage.selectValueFromSortByDDInCartTab("Price: High to Low");
		cscockpitAutoshipCartTabPage.selectCatalogFromDropDownInCartTab(); 
		randomProductSequenceNumber = String.valueOf(cscockpitAutoshipCartTabPage.getRandomProductWithSKUFromSearchResult()); 
		SKUValue = cscockpitAutoshipCartTabPage.getCustomerSKUValueInCartTab(randomProductSequenceNumber);
		cscockpitAutoshipCartTabPage.searchSKUValueInCartTab(SKUValue);
		cscockpitAutoshipCartTabPage.clickAddToCartBtnInCartTab();
		cscockpitAutoshipCartTabPage.clickCheckoutBtnInCartTab();
		cscockpitAutoshipTemplateUpdateTabPage.clickAddNewAddressUnderDeliveryAddressInCheckoutTab();
		s_assert.assertTrue(cscockpitAutoshipTemplateTabPage.verifyDeliveryAndShippingAddressPopupPresent("Create Delivery Address"),"Create Delivery Address Popup is not Present");
		cscockpitAutoshipTemplateUpdateTabPage.enterShippingInfoInAddNewPaymentProfilePopupWithoutSaveBtn(attendentFirstName, attendeeLastName, addressLine, city, postalCode, Country, Province, phoneNumber);
		cscockpitAutoshipTemplateUpdateTabPage.clickCreateNewAddressButtonInPopupAutoshipTemplateTabPage();
		cscockpitAutoshipTemplateUpdateTabPage.clickUseEnteredAddressOfCreateNewAddressShippingAddressPopup();
		cscockpitAutoshipTemplateUpdateTabPage.clickAddNewPaymentAddressInCheckoutTab();
		cscockpitAutoshipTemplateUpdateTabPage.enterBillingInfo();
		cscockpitAutoshipTemplateUpdateTabPage.clickSaveAddNewPaymentProfilePopUP();
		cscockpitAutoshipTemplateUpdateTabPage.enterCVVValueInCheckoutTab(TestConstants.SECURITY_CODE);
		cscockpitAutoshipTemplateUpdateTabPage.clickUseThisCardBtnInCheckoutTab();
		cscockpitAutoshipTemplateUpdateTabPage.clickUpdateAutoshipTemplateInAutoshipTemplateUpdateTab();
		double subTotal = cscockpitAutoshipTemplateTabPage.getSubTotalFromUI();
		double deliveryCost = cscockpitAutoshipTemplateTabPage.getDeliveryCostFromUI();
		double handlingCost = cscockpitAutoshipTemplateTabPage.getHandlingCostFromUI();
		if(cscockpitAutoshipTemplateTabPage.isGSTTaxPresentInUI()==true){
			gstFromUI = cscockpitAutoshipTemplateTabPage.getGstAmountFromUI();
			calculatedGstAmount = cscockpitAutoshipTemplateTabPage.calculateAmountAccordingToPercent(subTotal, deliveryCost, handlingCost, TestConstants.FIVE_PERCENT_TAX);
			s_assert.assertTrue(cscockpitAutoshipTemplateTabPage.validateGstAmountOnUI(gstFromUI,calculatedGstAmount),"GST Amount on UI is "+gstFromUI+" and calculated GST Amount is "+calculatedGstAmount+"");
		}else{
			logger.info("GST NOT PRESENT FOR THIS ORDER");
		}
		if(cscockpitAutoshipTemplateTabPage.isPstTaxPresentInUI() == true){
			pstFromUI = cscockpitAutoshipTemplateTabPage.getPstAmountFromUI();
			calculatedPstAmount = cscockpitAutoshipTemplateTabPage.calculateAmountAccordingToPercent(subTotal, deliveryCost, handlingCost, TestConstants.SEVEN_PERCENT_TAX);
			s_assert.assertTrue(cscockpitAutoshipTemplateTabPage.validatePstAmountOnUI(pstFromUI,calculatedPstAmount),"PST Amount on UI is "+pstFromUI+" and calculated GST Amount is "+calculatedPstAmount);
		}else{
			logger.info("PST NOT PRESENT FOR THIS ORDER");
		}

		s_assert.assertAll();
	}

	//Hybris Project-1605:To verify the Canada tax for Northwest Territories for Create PCPerks Autoship
	@Test
	public void testVerifyCanadaTaxForNorthwestTerritoriesForPCPerksAutoship_1605(){
		int randomNumber=CommonUtils.getRandomNum(10000, 1000000);
		String randomCustomerSequenceNumber = null;
		String randomProductSequenceNumber = null;
		String SKUValue = null;
		String attendentFirstName=TestConstants.FIRST_NAME+randomNumber;
		String attendeeLastName = TestConstants.LAST_NAME+randomNumber;
		String  addressLine=TestConstants.ADDRESS_LINE_1_CA;
		String city=TestConstants.CITY_CA;
		String postalCode=TestConstants.POSTAL_CODE_CA;
		String Country=TestConstants.COUNTRY_DD_VALUE_CA;
		String Province=TestConstants.PROVINCE_NORTHWEST_TERRITORIES;
		String phoneNumber=TestConstants.PHONE_NUMBER_CA;
		double gstFromUI = 0.00;
		double calculatedGstAmount = 0.00;
		double pstFromUI = 0.00;
		double calculatedPstAmount = 0.00;
		//Login to cscockpit.
		cscockpitCustomerSearchTabPage = cscockpitLoginPage.clickLoginBtn();
		cscockpitCustomerSearchTabPage.selectCustomerTypeFromDropDownInCustomerSearchTab("PC");
		cscockpitCustomerSearchTabPage.selectCountryFromDropDownInCustomerSearchTab("Canada");
		cscockpitCustomerSearchTabPage.selectAccountStatusFromDropDownInCustomerSearchTab("Active");
		cscockpitCustomerSearchTabPage.clickSearchBtn();
		randomCustomerSequenceNumber = String.valueOf(cscockpitCustomerSearchTabPage.getRandomCustomerFromSearchResult());
		cscockpitCustomerSearchTabPage.clickCIDNumberInCustomerSearchTab(randomCustomerSequenceNumber);
		cscockpitCustomerTabPage.clickAutoshipIdOnCustomerTab();
		cscockpitAutoshipTemplateTabPage.clickEditTemplateLinkInAutoshipTemplateTab();
		s_assert.assertTrue(cscockpitAutoshipTemplateTabPage.verifyCancelEditLinkInAutoshipTemplateTab(),"Cancel Edit link is not on Autoship template Tab Page");
		cscockpitAutoshipTemplateTabPage.clickAddMoreLinesLinkInAutoShipTemplateTab();
		cscockpitAutoshipCartTabPage.selectValueFromSortByDDInCartTab("Price: High to Low");
		cscockpitAutoshipCartTabPage.selectCatalogFromDropDownInCartTab(); 
		randomProductSequenceNumber = String.valueOf(cscockpitAutoshipCartTabPage.getRandomProductWithSKUFromSearchResult()); 
		SKUValue = cscockpitAutoshipCartTabPage.getCustomerSKUValueInCartTab(randomProductSequenceNumber);
		cscockpitAutoshipCartTabPage.searchSKUValueInCartTab(SKUValue);
		cscockpitAutoshipCartTabPage.clickAddToCartBtnInCartTab();
		cscockpitAutoshipCartTabPage.clickCheckoutBtnInCartTab();
		cscockpitAutoshipTemplateUpdateTabPage.clickAddNewAddressUnderDeliveryAddressInCheckoutTab();
		s_assert.assertTrue(cscockpitAutoshipTemplateTabPage.verifyDeliveryAndShippingAddressPopupPresent("Create Delivery Address"),"Create Delivery Address Popup is not Present");
		cscockpitAutoshipTemplateUpdateTabPage.enterShippingInfoInAddNewPaymentProfilePopupWithoutSaveBtn(attendentFirstName, attendeeLastName, addressLine, city, postalCode, Country, Province, phoneNumber);
		cscockpitAutoshipTemplateUpdateTabPage.clickCreateNewAddressButtonInPopupAutoshipTemplateTabPage();
		cscockpitAutoshipTemplateUpdateTabPage.clickUseEnteredAddressOfCreateNewAddressShippingAddressPopup();
		cscockpitAutoshipTemplateUpdateTabPage.clickAddNewPaymentAddressInCheckoutTab();
		cscockpitAutoshipTemplateUpdateTabPage.enterBillingInfo();
		cscockpitAutoshipTemplateUpdateTabPage.clickSaveAddNewPaymentProfilePopUP();
		cscockpitAutoshipTemplateUpdateTabPage.enterCVVValueInCheckoutTab(TestConstants.SECURITY_CODE);
		cscockpitAutoshipTemplateUpdateTabPage.clickUseThisCardBtnInCheckoutTab();
		cscockpitAutoshipTemplateUpdateTabPage.clickUpdateAutoshipTemplateInAutoshipTemplateUpdateTab();
		double subTotal = cscockpitAutoshipTemplateTabPage.getSubTotalFromUI();
		double deliveryCost = cscockpitAutoshipTemplateTabPage.getDeliveryCostFromUI();
		double handlingCost = cscockpitAutoshipTemplateTabPage.getHandlingCostFromUI();
		if(cscockpitAutoshipTemplateTabPage.isGSTTaxPresentInUI()==true){
			gstFromUI = cscockpitAutoshipTemplateTabPage.getGstAmountFromUI();
			calculatedGstAmount = cscockpitAutoshipTemplateTabPage.calculateAmountAccordingToPercent(subTotal, deliveryCost, handlingCost, TestConstants.FIVE_PERCENT_TAX);
			s_assert.assertTrue(cscockpitAutoshipTemplateTabPage.validateGstAmountOnUI(gstFromUI,calculatedGstAmount),"GST Amount on UI is "+gstFromUI+" and calculated GST Amount is "+calculatedGstAmount+"");
		}else{
			logger.info("GST NOT PRESENT FOR THIS ORDER");
		}
		if(cscockpitAutoshipTemplateTabPage.isPstTaxPresentInUI() == true){
			pstFromUI = cscockpitAutoshipTemplateTabPage.getPstAmountFromUI();
			calculatedPstAmount = cscockpitAutoshipTemplateTabPage.calculateAmountAccordingToPercent(subTotal, deliveryCost, handlingCost, TestConstants.SEVEN_PERCENT_TAX);
			s_assert.assertTrue(cscockpitAutoshipTemplateTabPage.validatePstAmountOnUI(pstFromUI,calculatedPstAmount),"PST Amount on UI is "+pstFromUI+" and calculated GST Amount is "+calculatedPstAmount);
		}else{
			logger.info("PST NOT PRESENT FOR THIS ORDER");
		}

		s_assert.assertAll();
	}

	//Hybris Project-1606:To verify the Canada tax for Nunavat for Create PCPerks Autoship
	@Test
	public void testVerifyCanadaTaxForNunavatForPCPerksAutoship_1606(){
		int randomNumber=CommonUtils.getRandomNum(10000, 1000000);
		String randomCustomerSequenceNumber = null;
		String randomProductSequenceNumber = null;
		String SKUValue = null;
		String attendentFirstName=TestConstants.FIRST_NAME+randomNumber;
		String attendeeLastName = TestConstants.LAST_NAME+randomNumber;
		String  addressLine=TestConstants.ADDRESS_LINE_1_CA;
		String city=TestConstants.CITY_CA;
		String postalCode=TestConstants.POSTAL_CODE_CA;
		String Country=TestConstants.COUNTRY_DD_VALUE_CA;
		String Province=TestConstants.PROVINCE_NUNAVUT;
		String phoneNumber=TestConstants.PHONE_NUMBER_CA;
		double gstFromUI = 0.00;
		double calculatedGstAmount = 0.00;
		double pstFromUI = 0.00;
		double calculatedPstAmount = 0.00;
		//Login to cscockpit.
		cscockpitCustomerSearchTabPage = cscockpitLoginPage.clickLoginBtn();
		cscockpitCustomerSearchTabPage.selectCustomerTypeFromDropDownInCustomerSearchTab("PC");
		cscockpitCustomerSearchTabPage.selectCountryFromDropDownInCustomerSearchTab("Canada");
		cscockpitCustomerSearchTabPage.selectAccountStatusFromDropDownInCustomerSearchTab("Active");
		cscockpitCustomerSearchTabPage.clickSearchBtn();
		randomCustomerSequenceNumber = String.valueOf(cscockpitCustomerSearchTabPage.getRandomCustomerFromSearchResult());
		cscockpitCustomerSearchTabPage.clickCIDNumberInCustomerSearchTab(randomCustomerSequenceNumber);
		cscockpitCustomerTabPage.clickAutoshipIdOnCustomerTab();
		cscockpitAutoshipTemplateTabPage.clickEditTemplateLinkInAutoshipTemplateTab();
		s_assert.assertTrue(cscockpitAutoshipTemplateTabPage.verifyCancelEditLinkInAutoshipTemplateTab(),"Cancel Edit link is not on Autoship template Tab Page");
		cscockpitAutoshipTemplateTabPage.clickAddMoreLinesLinkInAutoShipTemplateTab();
		cscockpitAutoshipCartTabPage.selectValueFromSortByDDInCartTab("Price: High to Low");
		cscockpitAutoshipCartTabPage.selectCatalogFromDropDownInCartTab(); 
		randomProductSequenceNumber = String.valueOf(cscockpitAutoshipCartTabPage.getRandomProductWithSKUFromSearchResult()); 
		SKUValue = cscockpitAutoshipCartTabPage.getCustomerSKUValueInCartTab(randomProductSequenceNumber);
		cscockpitAutoshipCartTabPage.searchSKUValueInCartTab(SKUValue);
		cscockpitAutoshipCartTabPage.clickAddToCartBtnInCartTab();
		cscockpitAutoshipCartTabPage.clickCheckoutBtnInCartTab();
		cscockpitAutoshipTemplateUpdateTabPage.clickAddNewAddressUnderDeliveryAddressInCheckoutTab();
		s_assert.assertTrue(cscockpitAutoshipTemplateTabPage.verifyDeliveryAndShippingAddressPopupPresent("Create Delivery Address"),"Create Delivery Address Popup is not Present");
		cscockpitAutoshipTemplateUpdateTabPage.enterShippingInfoInAddNewPaymentProfilePopupWithoutSaveBtn(attendentFirstName, attendeeLastName, addressLine, city, postalCode, Country, Province, phoneNumber);
		cscockpitAutoshipTemplateUpdateTabPage.clickCreateNewAddressButtonInPopupAutoshipTemplateTabPage();
		cscockpitAutoshipTemplateUpdateTabPage.clickUseEnteredAddressOfCreateNewAddressShippingAddressPopup();
		cscockpitAutoshipTemplateUpdateTabPage.clickAddNewPaymentAddressInCheckoutTab();
		cscockpitAutoshipTemplateUpdateTabPage.enterBillingInfo();
		cscockpitAutoshipTemplateUpdateTabPage.clickSaveAddNewPaymentProfilePopUP();
		cscockpitAutoshipTemplateUpdateTabPage.enterCVVValueInCheckoutTab(TestConstants.SECURITY_CODE);
		cscockpitAutoshipTemplateUpdateTabPage.clickUseThisCardBtnInCheckoutTab();
		cscockpitAutoshipTemplateUpdateTabPage.clickUpdateAutoshipTemplateInAutoshipTemplateUpdateTab();
		double subTotal = cscockpitAutoshipTemplateTabPage.getSubTotalFromUI();
		double deliveryCost = cscockpitAutoshipTemplateTabPage.getDeliveryCostFromUI();
		double handlingCost = cscockpitAutoshipTemplateTabPage.getHandlingCostFromUI();
		if(cscockpitAutoshipTemplateTabPage.isGSTTaxPresentInUI()==true){
			gstFromUI = cscockpitAutoshipTemplateTabPage.getGstAmountFromUI();
			calculatedGstAmount = cscockpitAutoshipTemplateTabPage.calculateAmountAccordingToPercent(subTotal, deliveryCost, handlingCost, TestConstants.FIVE_PERCENT_TAX);
			s_assert.assertTrue(cscockpitAutoshipTemplateTabPage.validateGstAmountOnUI(gstFromUI,calculatedGstAmount),"GST Amount on UI is "+gstFromUI+" and calculated GST Amount is "+calculatedGstAmount+"");
		}else{
			logger.info("GST NOT PRESENT FOR THIS ORDER");
		}
		if(cscockpitAutoshipTemplateTabPage.isPstTaxPresentInUI() == true){
			pstFromUI = cscockpitAutoshipTemplateTabPage.getPstAmountFromUI();
			calculatedPstAmount = cscockpitAutoshipTemplateTabPage.calculateAmountAccordingToPercent(subTotal, deliveryCost, handlingCost, TestConstants.SEVEN_PERCENT_TAX);
			s_assert.assertTrue(cscockpitAutoshipTemplateTabPage.validatePstAmountOnUI(pstFromUI,calculatedPstAmount),"PST Amount on UI is "+pstFromUI+" and calculated GST Amount is "+calculatedPstAmount);
		}else{
			logger.info("PST NOT PRESENT FOR THIS ORDER");
		}

		s_assert.assertAll();
	}

	//Hybris Project-1607:To verify the Canada tax for Bristish Columbia for PCPerks Autoship
	@Test
	public void testVerifyCanadaTaxForBritishColumbiaForPCPerksAutoship_1607(){
		int randomNumber=CommonUtils.getRandomNum(10000, 1000000);
		String randomCustomerSequenceNumber = null;
		String randomProductSequenceNumber = null;
		String SKUValue = null;
		String attendentFirstName=TestConstants.FIRST_NAME+randomNumber;
		String attendeeLastName = TestConstants.LAST_NAME+randomNumber;
		String  addressLine=TestConstants.ADDRESS_LINE_1_CA;
		String city=TestConstants.CITY_CA;
		String postalCode=TestConstants.POSTAL_CODE_CA;
		String Country=TestConstants.COUNTRY_DD_VALUE_CA;
		String Province=TestConstants.PROVINCE_BRITISH_COULMBIA;
		String phoneNumber=TestConstants.PHONE_NUMBER_CA;
		double gstFromUI = 0.00;
		double calculatedGstAmount = 0.00;
		double pstFromUI = 0.00;
		double calculatedPstAmount = 0.00;
		//Login to cscockpit.
		cscockpitCustomerSearchTabPage = cscockpitLoginPage.clickLoginBtn();
		cscockpitCustomerSearchTabPage.selectCustomerTypeFromDropDownInCustomerSearchTab("PC");
		cscockpitCustomerSearchTabPage.selectCountryFromDropDownInCustomerSearchTab("Canada");
		cscockpitCustomerSearchTabPage.selectAccountStatusFromDropDownInCustomerSearchTab("Active");
		cscockpitCustomerSearchTabPage.clickSearchBtn();
		randomCustomerSequenceNumber = String.valueOf(cscockpitCustomerSearchTabPage.getRandomCustomerFromSearchResult());
		cscockpitCustomerSearchTabPage.clickCIDNumberInCustomerSearchTab(randomCustomerSequenceNumber);
		cscockpitCustomerTabPage.clickAutoshipIdOnCustomerTab();
		cscockpitAutoshipTemplateTabPage.clickEditTemplateLinkInAutoshipTemplateTab();
		s_assert.assertTrue(cscockpitAutoshipTemplateTabPage.verifyCancelEditLinkInAutoshipTemplateTab(),"Cancel Edit link is not on Autoship template Tab Page");
		cscockpitAutoshipTemplateTabPage.clickRemoveLinkToRemoveProductFromAutoshipCart();	
		if(cscockpitAutoshipTemplateTabPage.verifyThresholdPopupForOrderTotal()){
			cscockpitAutoshipTemplateTabPage.clickOKOfThresholdPopupInAutoshipTemplateTab();
		}
		cscockpitAutoshipTemplateTabPage.clickAddMoreLinesLinkInAutoShipTemplateTab();
		cscockpitAutoshipCartTabPage.selectValueFromSortByDDInCartTab("Price: High to Low");
		cscockpitAutoshipCartTabPage.selectCatalogFromDropDownInCartTab(); 
		randomProductSequenceNumber = String.valueOf(cscockpitAutoshipCartTabPage.getRandomProductWithSKUFromSearchResult()); 
		SKUValue = cscockpitAutoshipCartTabPage.getCustomerSKUValueInCartTab(randomProductSequenceNumber);
		cscockpitAutoshipCartTabPage.searchSKUValueInCartTab(SKUValue);
		cscockpitAutoshipCartTabPage.clickAddToCartBtnInCartTab();
		cscockpitAutoshipCartTabPage.clickCheckoutBtnInCartTab();
		cscockpitAutoshipTemplateUpdateTabPage.clickAddNewAddressUnderDeliveryAddressInCheckoutTab();
		s_assert.assertTrue(cscockpitAutoshipTemplateTabPage.verifyDeliveryAndShippingAddressPopupPresent("Create Delivery Address"),"Create Delivery Address Popup is not Present");
		cscockpitAutoshipTemplateUpdateTabPage.enterShippingInfoInAddNewPaymentProfilePopupWithoutSaveBtn(attendentFirstName, attendeeLastName, addressLine, city, postalCode, Country, Province, phoneNumber);
		cscockpitAutoshipTemplateUpdateTabPage.clickCreateNewAddressButtonInPopupAutoshipTemplateTabPage();
		cscockpitAutoshipTemplateUpdateTabPage.clickUseEnteredAddressOfCreateNewAddressShippingAddressPopup();
		cscockpitAutoshipTemplateUpdateTabPage.clickAddNewPaymentAddressInCheckoutTab();
		cscockpitAutoshipTemplateUpdateTabPage.enterBillingInfo();
		cscockpitAutoshipTemplateUpdateTabPage.clickSaveAddNewPaymentProfilePopUP();
		cscockpitAutoshipTemplateUpdateTabPage.enterCVVValueInCheckoutTab(TestConstants.SECURITY_CODE);
		cscockpitAutoshipTemplateUpdateTabPage.clickUseThisCardBtnInCheckoutTab();
		cscockpitAutoshipTemplateUpdateTabPage.clickUpdateAutoshipTemplateInAutoshipTemplateUpdateTab();
		double subTotal = cscockpitAutoshipTemplateTabPage.getSubTotalFromUI();
		double deliveryCost = cscockpitAutoshipTemplateTabPage.getDeliveryCostFromUI();
		double handlingCost = cscockpitAutoshipTemplateTabPage.getHandlingCostFromUI();
		if(cscockpitAutoshipTemplateTabPage.isGSTTaxPresentInUI()==true){
			gstFromUI = cscockpitAutoshipTemplateTabPage.getGstAmountFromUI();
			calculatedGstAmount = cscockpitAutoshipTemplateTabPage.calculateAmountAccordingToPercent(subTotal, deliveryCost, handlingCost, TestConstants.FIVE_PERCENT_TAX);
			s_assert.assertTrue(cscockpitAutoshipTemplateTabPage.validateGstAmountOnUI(gstFromUI,calculatedGstAmount),"GST Amount on UI is "+gstFromUI+" and calculated GST Amount is "+calculatedGstAmount+"");
		}else{
			logger.info("GST NOT PRESENT FOR THIS ORDER");
		}
		if(cscockpitAutoshipTemplateTabPage.isPstTaxPresentInUI() == true){
			pstFromUI = cscockpitAutoshipTemplateTabPage.getPstAmountFromUI();
			calculatedPstAmount = cscockpitAutoshipTemplateTabPage.calculateAmountAccordingToPercent(subTotal, deliveryCost, handlingCost, TestConstants.SEVEN_PERCENT_TAX);
			s_assert.assertTrue(cscockpitAutoshipTemplateTabPage.validatePstAmountOnUI(pstFromUI,calculatedPstAmount),"PST Amount on UI is "+pstFromUI+" and calculated GST Amount is "+calculatedPstAmount);
		}else{
			logger.info("PST NOT PRESENT FOR THIS ORDER");
		}

		s_assert.assertAll();
	}

	//Hybris Project-1608:To verify the Canada tax for Alberta for PCPerks Autoship
	@Test
	public void testVerifyCanadaTaxForAlbertaForPCPerksAutoship_1608(){
		int randomNumber=CommonUtils.getRandomNum(10000, 1000000);
		String randomCustomerSequenceNumber = null;
		String randomProductSequenceNumber = null;
		String SKUValue = null;
		String attendentFirstName=TestConstants.FIRST_NAME+randomNumber;
		String attendeeLastName = TestConstants.LAST_NAME+randomNumber;
		String  addressLine=TestConstants.ADDRESS_LINE_1_CA;
		String city=TestConstants.CITY_CA;
		String postalCode=TestConstants.POSTAL_CODE_CA;
		String Country=TestConstants.COUNTRY_DD_VALUE_CA;
		String Province=TestConstants.PROVINCE_ALBERTA;
		String phoneNumber=TestConstants.PHONE_NUMBER_CA;
		double gstFromUI = 0.00;
		double calculatedGstAmount = 0.00;
		double pstFromUI = 0.00;
		double calculatedPstAmount = 0.00;
		//Login to cscockpit.
		cscockpitCustomerSearchTabPage = cscockpitLoginPage.clickLoginBtn();
		cscockpitCustomerSearchTabPage.selectCustomerTypeFromDropDownInCustomerSearchTab("PC");
		cscockpitCustomerSearchTabPage.selectCountryFromDropDownInCustomerSearchTab("Canada");
		cscockpitCustomerSearchTabPage.selectAccountStatusFromDropDownInCustomerSearchTab("Active");
		cscockpitCustomerSearchTabPage.clickSearchBtn();
		randomCustomerSequenceNumber = String.valueOf(cscockpitCustomerSearchTabPage.getRandomCustomerFromSearchResult());
		cscockpitCustomerSearchTabPage.clickCIDNumberInCustomerSearchTab(randomCustomerSequenceNumber);
		cscockpitCustomerTabPage.clickAutoshipIdOnCustomerTab();
		cscockpitAutoshipTemplateTabPage.clickEditTemplateLinkInAutoshipTemplateTab();
		s_assert.assertTrue(cscockpitAutoshipTemplateTabPage.verifyCancelEditLinkInAutoshipTemplateTab(),"Cancel Edit link is not on Autoship template Tab Page");
		cscockpitAutoshipTemplateTabPage.clickRemoveLinkToRemoveProductFromAutoshipCart();	
		if(cscockpitAutoshipTemplateTabPage.verifyThresholdPopupForOrderTotal()){
			cscockpitAutoshipTemplateTabPage.clickOKOfThresholdPopupInAutoshipTemplateTab();
		}
		cscockpitAutoshipTemplateTabPage.clickAddMoreLinesLinkInAutoShipTemplateTab();
		cscockpitAutoshipCartTabPage.selectValueFromSortByDDInCartTab("Price: High to Low");
		cscockpitAutoshipCartTabPage.selectCatalogFromDropDownInCartTab(); 
		randomProductSequenceNumber = String.valueOf(cscockpitAutoshipCartTabPage.getRandomProductWithSKUFromSearchResult()); 
		SKUValue = cscockpitAutoshipCartTabPage.getCustomerSKUValueInCartTab(randomProductSequenceNumber);
		cscockpitAutoshipCartTabPage.searchSKUValueInCartTab(SKUValue);
		cscockpitAutoshipCartTabPage.clickAddToCartBtnInCartTab();
		cscockpitAutoshipCartTabPage.clickCheckoutBtnInCartTab();
		cscockpitAutoshipTemplateUpdateTabPage.clickAddNewAddressUnderDeliveryAddressInCheckoutTab();
		s_assert.assertTrue(cscockpitAutoshipTemplateTabPage.verifyDeliveryAndShippingAddressPopupPresent("Create Delivery Address"),"Create Delivery Address Popup is not Present");
		cscockpitAutoshipTemplateUpdateTabPage.enterShippingInfoInAddNewPaymentProfilePopupWithoutSaveBtn(attendentFirstName, attendeeLastName, addressLine, city, postalCode, Country, Province, phoneNumber);
		cscockpitAutoshipTemplateUpdateTabPage.clickCreateNewAddressButtonInPopupAutoshipTemplateTabPage();
		cscockpitAutoshipTemplateUpdateTabPage.clickUseEnteredAddressOfCreateNewAddressShippingAddressPopup();
		cscockpitAutoshipTemplateUpdateTabPage.clickAddNewPaymentAddressInCheckoutTab();
		cscockpitAutoshipTemplateUpdateTabPage.enterBillingInfo();
		cscockpitAutoshipTemplateUpdateTabPage.clickSaveAddNewPaymentProfilePopUP();
		cscockpitAutoshipTemplateUpdateTabPage.enterCVVValueInCheckoutTab(TestConstants.SECURITY_CODE);
		cscockpitAutoshipTemplateUpdateTabPage.clickUseThisCardBtnInCheckoutTab();
		cscockpitAutoshipTemplateUpdateTabPage.clickUpdateAutoshipTemplateInAutoshipTemplateUpdateTab();
		double subTotal = cscockpitAutoshipTemplateTabPage.getSubTotalFromUI();
		double deliveryCost = cscockpitAutoshipTemplateTabPage.getDeliveryCostFromUI();
		double handlingCost = cscockpitAutoshipTemplateTabPage.getHandlingCostFromUI();
		if(cscockpitAutoshipTemplateTabPage.isGSTTaxPresentInUI()==true){
			gstFromUI = cscockpitAutoshipTemplateTabPage.getGstAmountFromUI();
			calculatedGstAmount = cscockpitAutoshipTemplateTabPage.calculateAmountAccordingToPercent(subTotal, deliveryCost, handlingCost, TestConstants.FIVE_PERCENT_TAX);
			s_assert.assertTrue(cscockpitAutoshipTemplateTabPage.validateGstAmountOnUI(gstFromUI,calculatedGstAmount),"GST Amount on UI is "+gstFromUI+" and calculated GST Amount is "+calculatedGstAmount+"");
		}else{
			logger.info("GST NOT PRESENT FOR THIS ORDER");
		}
		if(cscockpitAutoshipTemplateTabPage.isPstTaxPresentInUI() == true){
			pstFromUI = cscockpitAutoshipTemplateTabPage.getPstAmountFromUI();
			calculatedPstAmount = cscockpitAutoshipTemplateTabPage.calculateAmountAccordingToPercent(subTotal, deliveryCost, handlingCost, TestConstants.SEVEN_PERCENT_TAX);
			s_assert.assertTrue(cscockpitAutoshipTemplateTabPage.validatePstAmountOnUI(pstFromUI,calculatedPstAmount),"PST Amount on UI is "+pstFromUI+" and calculated GST Amount is "+calculatedPstAmount);
		}else{
			logger.info("PST NOT PRESENT FOR THIS ORDER");
		}

		s_assert.assertAll();
	}

	//Hybris Project-1609:To verify the Canada tax for saskatchewan for PCPerks Autoship
	@Test
	public void testVerifyCanadaTaxForSaskatchewanForPCPerksAutoship_1609(){
		int randomNumber=CommonUtils.getRandomNum(10000, 1000000);
		String randomCustomerSequenceNumber = null;
		String randomProductSequenceNumber = null;
		String SKUValue = null;
		String attendentFirstName=TestConstants.FIRST_NAME+randomNumber;
		String attendeeLastName = TestConstants.LAST_NAME+randomNumber;
		String  addressLine=TestConstants.ADDRESS_LINE_1_CA;
		String city=TestConstants.CITY_CA;
		String postalCode=TestConstants.POSTAL_CODE_CA;
		String Country=TestConstants.COUNTRY_DD_VALUE_CA;
		String Province=TestConstants.PROVINCE_SASKATCHEWAN;
		String phoneNumber=TestConstants.PHONE_NUMBER_CA;
		double gstFromUI = 0.00;
		double calculatedGstAmount = 0.00;
		double pstFromUI = 0.00;
		double calculatedPstAmount = 0.00;
		//Login to cscockpit.
		cscockpitCustomerSearchTabPage = cscockpitLoginPage.clickLoginBtn();
		cscockpitCustomerSearchTabPage.selectCustomerTypeFromDropDownInCustomerSearchTab("PC");
		cscockpitCustomerSearchTabPage.selectCountryFromDropDownInCustomerSearchTab("Canada");
		cscockpitCustomerSearchTabPage.selectAccountStatusFromDropDownInCustomerSearchTab("Active");
		cscockpitCustomerSearchTabPage.clickSearchBtn();
		randomCustomerSequenceNumber = String.valueOf(cscockpitCustomerSearchTabPage.getRandomCustomerFromSearchResult());
		cscockpitCustomerSearchTabPage.clickCIDNumberInCustomerSearchTab(randomCustomerSequenceNumber);
		cscockpitCustomerTabPage.clickAutoshipIdOnCustomerTab();
		cscockpitAutoshipTemplateTabPage.clickEditTemplateLinkInAutoshipTemplateTab();
		s_assert.assertTrue(cscockpitAutoshipTemplateTabPage.verifyCancelEditLinkInAutoshipTemplateTab(),"Cancel Edit link is not on Autoship template Tab Page");
		cscockpitAutoshipTemplateTabPage.clickRemoveLinkToRemoveProductFromAutoshipCart();	
		if(cscockpitAutoshipTemplateTabPage.verifyThresholdPopupForOrderTotal()){
			cscockpitAutoshipTemplateTabPage.clickOKOfThresholdPopupInAutoshipTemplateTab();
		}
		cscockpitAutoshipTemplateTabPage.clickAddMoreLinesLinkInAutoShipTemplateTab();
		cscockpitAutoshipCartTabPage.selectValueFromSortByDDInCartTab("Price: High to Low");
		cscockpitAutoshipCartTabPage.selectCatalogFromDropDownInCartTab(); 
		randomProductSequenceNumber = String.valueOf(cscockpitAutoshipCartTabPage.getRandomProductWithSKUFromSearchResult()); 
		SKUValue = cscockpitAutoshipCartTabPage.getCustomerSKUValueInCartTab(randomProductSequenceNumber);
		cscockpitAutoshipCartTabPage.searchSKUValueInCartTab(SKUValue);
		cscockpitAutoshipCartTabPage.clickAddToCartBtnInCartTab();
		cscockpitAutoshipCartTabPage.clickCheckoutBtnInCartTab();
		cscockpitAutoshipTemplateUpdateTabPage.clickAddNewAddressUnderDeliveryAddressInCheckoutTab();
		s_assert.assertTrue(cscockpitAutoshipTemplateTabPage.verifyDeliveryAndShippingAddressPopupPresent("Create Delivery Address"),"Create Delivery Address Popup is not Present");
		cscockpitAutoshipTemplateUpdateTabPage.enterShippingInfoInAddNewPaymentProfilePopupWithoutSaveBtn(attendentFirstName, attendeeLastName, addressLine, city, postalCode, Country, Province, phoneNumber);
		cscockpitAutoshipTemplateUpdateTabPage.clickCreateNewAddressButtonInPopupAutoshipTemplateTabPage();
		cscockpitAutoshipTemplateUpdateTabPage.clickUseEnteredAddressOfCreateNewAddressShippingAddressPopup();
		cscockpitAutoshipTemplateUpdateTabPage.clickAddNewPaymentAddressInCheckoutTab();
		cscockpitAutoshipTemplateUpdateTabPage.enterBillingInfo();
		cscockpitAutoshipTemplateUpdateTabPage.clickSaveAddNewPaymentProfilePopUP();
		cscockpitAutoshipTemplateUpdateTabPage.enterCVVValueInCheckoutTab(TestConstants.SECURITY_CODE);
		cscockpitAutoshipTemplateUpdateTabPage.clickUseThisCardBtnInCheckoutTab();
		cscockpitAutoshipTemplateUpdateTabPage.clickUpdateAutoshipTemplateInAutoshipTemplateUpdateTab();
		double subTotal = cscockpitAutoshipTemplateTabPage.getSubTotalFromUI();
		double deliveryCost = cscockpitAutoshipTemplateTabPage.getDeliveryCostFromUI();
		double handlingCost = cscockpitAutoshipTemplateTabPage.getHandlingCostFromUI();
		if(cscockpitAutoshipTemplateTabPage.isGSTTaxPresentInUI()==true){
			gstFromUI = cscockpitAutoshipTemplateTabPage.getGstAmountFromUI();
			calculatedGstAmount = cscockpitAutoshipTemplateTabPage.calculateAmountAccordingToPercent(subTotal, deliveryCost, handlingCost, TestConstants.FIVE_PERCENT_TAX);
			s_assert.assertTrue(cscockpitAutoshipTemplateTabPage.validateGstAmountOnUI(gstFromUI,calculatedGstAmount),"GST Amount on UI is "+gstFromUI+" and calculated GST Amount is "+calculatedGstAmount+"");
		}else{
			logger.info("GST NOT PRESENT FOR THIS ORDER");
		}
		if(cscockpitAutoshipTemplateTabPage.isPstTaxPresentInUI() == true){
			pstFromUI = cscockpitAutoshipTemplateTabPage.getPstAmountFromUI();
			calculatedPstAmount = cscockpitAutoshipTemplateTabPage.calculateAmountAccordingToPercent(subTotal, deliveryCost, handlingCost, TestConstants.FIVE_PERCENT_TAX);
			s_assert.assertTrue(cscockpitAutoshipTemplateTabPage.validatePstAmountOnUI(pstFromUI,calculatedPstAmount),"PST Amount on UI is "+pstFromUI+" and calculated GST Amount is "+calculatedPstAmount);
		}else{
			logger.info("PST NOT PRESENT FOR THIS ORDER");
		}

		s_assert.assertAll();
	}

	//Hybris Project-1610:To verify the Canada tax for Manitoba for PCPerks Autoship
	@Test
	public void testVerifyCanadaTaxForManitobaForPCPerksAutoship_1610(){
		int randomNumber=CommonUtils.getRandomNum(10000, 1000000);
		String randomCustomerSequenceNumber = null;
		String randomProductSequenceNumber = null;
		String SKUValue = null;
		String attendentFirstName=TestConstants.FIRST_NAME+randomNumber;
		String attendeeLastName = TestConstants.LAST_NAME+randomNumber;
		String  addressLine=TestConstants.ADDRESS_LINE_1_CA;
		String city=TestConstants.CITY_CA;
		String postalCode=TestConstants.POSTAL_CODE_CA;
		String Country=TestConstants.COUNTRY_DD_VALUE_CA;
		String Province=TestConstants.PROVINCE_MANITOBA;
		String phoneNumber=TestConstants.PHONE_NUMBER_CA;
		double gstFromUI = 0.00;
		double calculatedGstAmount = 0.00;
		double pstFromUI = 0.00;
		double calculatedPstAmount = 0.00;
		//Login to cscockpit.
		cscockpitCustomerSearchTabPage = cscockpitLoginPage.clickLoginBtn();
		cscockpitCustomerSearchTabPage.selectCustomerTypeFromDropDownInCustomerSearchTab("PC");
		cscockpitCustomerSearchTabPage.selectCountryFromDropDownInCustomerSearchTab("Canada");
		cscockpitCustomerSearchTabPage.selectAccountStatusFromDropDownInCustomerSearchTab("Active");
		cscockpitCustomerSearchTabPage.clickSearchBtn();
		randomCustomerSequenceNumber = String.valueOf(cscockpitCustomerSearchTabPage.getRandomCustomerFromSearchResult());
		cscockpitCustomerSearchTabPage.clickCIDNumberInCustomerSearchTab(randomCustomerSequenceNumber);
		cscockpitCustomerTabPage.clickAutoshipIdOnCustomerTab();
		cscockpitAutoshipTemplateTabPage.clickEditTemplateLinkInAutoshipTemplateTab();
		s_assert.assertTrue(cscockpitAutoshipTemplateTabPage.verifyCancelEditLinkInAutoshipTemplateTab(),"Cancel Edit link is not on Autoship template Tab Page");
		cscockpitAutoshipTemplateTabPage.clickRemoveLinkToRemoveProductFromAutoshipCart();	
		if(cscockpitAutoshipTemplateTabPage.verifyThresholdPopupForOrderTotal()){
			cscockpitAutoshipTemplateTabPage.clickOKOfThresholdPopupInAutoshipTemplateTab();
		}
		cscockpitAutoshipTemplateTabPage.clickAddMoreLinesLinkInAutoShipTemplateTab();
		cscockpitAutoshipCartTabPage.selectValueFromSortByDDInCartTab("Price: High to Low");
		cscockpitAutoshipCartTabPage.selectCatalogFromDropDownInCartTab(); 
		randomProductSequenceNumber = String.valueOf(cscockpitAutoshipCartTabPage.getRandomProductWithSKUFromSearchResult()); 
		SKUValue = cscockpitAutoshipCartTabPage.getCustomerSKUValueInCartTab(randomProductSequenceNumber);
		cscockpitAutoshipCartTabPage.searchSKUValueInCartTab(SKUValue);
		cscockpitAutoshipCartTabPage.clickAddToCartBtnInCartTab();
		cscockpitAutoshipCartTabPage.clickCheckoutBtnInCartTab();
		cscockpitAutoshipTemplateUpdateTabPage.clickAddNewAddressUnderDeliveryAddressInCheckoutTab();
		s_assert.assertTrue(cscockpitAutoshipTemplateTabPage.verifyDeliveryAndShippingAddressPopupPresent("Create Delivery Address"),"Create Delivery Address Popup is not Present");
		cscockpitAutoshipTemplateUpdateTabPage.enterShippingInfoInAddNewPaymentProfilePopupWithoutSaveBtn(attendentFirstName, attendeeLastName, addressLine, city, postalCode, Country, Province, phoneNumber);
		cscockpitAutoshipTemplateUpdateTabPage.clickCreateNewAddressButtonInPopupAutoshipTemplateTabPage();
		cscockpitAutoshipTemplateUpdateTabPage.clickUseEnteredAddressOfCreateNewAddressShippingAddressPopup();
		cscockpitAutoshipTemplateUpdateTabPage.clickAddNewPaymentAddressInCheckoutTab();
		cscockpitAutoshipTemplateUpdateTabPage.enterBillingInfo();
		cscockpitAutoshipTemplateUpdateTabPage.clickSaveAddNewPaymentProfilePopUP();
		cscockpitAutoshipTemplateUpdateTabPage.enterCVVValueInCheckoutTab(TestConstants.SECURITY_CODE);
		cscockpitAutoshipTemplateUpdateTabPage.clickUseThisCardBtnInCheckoutTab();
		cscockpitAutoshipTemplateUpdateTabPage.clickUpdateAutoshipTemplateInAutoshipTemplateUpdateTab();
		double subTotal = cscockpitAutoshipTemplateTabPage.getSubTotalFromUI();
		double deliveryCost = cscockpitAutoshipTemplateTabPage.getDeliveryCostFromUI();
		double handlingCost = cscockpitAutoshipTemplateTabPage.getHandlingCostFromUI();
		if(cscockpitAutoshipTemplateTabPage.isGSTTaxPresentInUI()==true){
			gstFromUI = cscockpitAutoshipTemplateTabPage.getGstAmountFromUI();
			calculatedGstAmount = cscockpitAutoshipTemplateTabPage.calculateAmountAccordingToPercent(subTotal, deliveryCost, handlingCost, TestConstants.FIVE_PERCENT_TAX);
			s_assert.assertTrue(cscockpitAutoshipTemplateTabPage.validateGstAmountOnUI(gstFromUI,calculatedGstAmount),"GST Amount on UI is "+gstFromUI+" and calculated GST Amount is "+calculatedGstAmount+"");
		}else{
			logger.info("GST NOT PRESENT FOR THIS ORDER");
		}
		if(cscockpitAutoshipTemplateTabPage.isPstTaxPresentInUI() == true){
			pstFromUI = cscockpitAutoshipTemplateTabPage.getPstAmountFromUI();
			calculatedPstAmount = cscockpitAutoshipTemplateTabPage.calculateAmountAccordingToPercent(subTotal, deliveryCost, handlingCost, TestConstants.EIGHT_PERCENT_TAX);
			s_assert.assertTrue(cscockpitAutoshipTemplateTabPage.validatePstAmountOnUI(pstFromUI,calculatedPstAmount),"PST Amount on UI is "+pstFromUI+" and calculated GST Amount is "+calculatedPstAmount);
		}else{
			logger.info("PST NOT PRESENT FOR THIS ORDER");
		}

		s_assert.assertAll();
	}

	//Hybris Project-1612:To verify the Canada tax for Quebec for PCPerks Autoship
	@Test
	public void testVerifyCanadaTaxForQubecForPCPerksAutoship_1612(){
		int randomNumber=CommonUtils.getRandomNum(10000, 1000000);
		String randomCustomerSequenceNumber = null;
		String randomProductSequenceNumber = null;
		String SKUValue = null;
		String attendentFirstName=TestConstants.FIRST_NAME+randomNumber;
		String attendeeLastName = TestConstants.LAST_NAME+randomNumber;
		String  addressLine=TestConstants.ADDRESS_LINE_1_CA;
		String city=TestConstants.CITY_CA;
		String postalCode=TestConstants.POSTAL_CODE_CA;
		String Country=TestConstants.COUNTRY_DD_VALUE_CA;
		String Province=TestConstants.PROVINCE_QUEBEC;
		String phoneNumber=TestConstants.PHONE_NUMBER_CA;
		double gstFromUI = 0.00;
		double calculatedGstAmount = 0.00;
		double pstFromUI = 0.00;
		double calculatedPstAmount = 0.00;
		//Login to cscockpit.
		cscockpitCustomerSearchTabPage = cscockpitLoginPage.clickLoginBtn();
		cscockpitCustomerSearchTabPage.selectCustomerTypeFromDropDownInCustomerSearchTab("PC");
		cscockpitCustomerSearchTabPage.selectCountryFromDropDownInCustomerSearchTab("Canada");
		cscockpitCustomerSearchTabPage.selectAccountStatusFromDropDownInCustomerSearchTab("Active");
		cscockpitCustomerSearchTabPage.clickSearchBtn();
		randomCustomerSequenceNumber = String.valueOf(cscockpitCustomerSearchTabPage.getRandomCustomerFromSearchResult());
		cscockpitCustomerSearchTabPage.clickCIDNumberInCustomerSearchTab(randomCustomerSequenceNumber);
		cscockpitCustomerTabPage.clickAutoshipIdOnCustomerTab();
		cscockpitAutoshipTemplateTabPage.clickEditTemplateLinkInAutoshipTemplateTab();
		s_assert.assertTrue(cscockpitAutoshipTemplateTabPage.verifyCancelEditLinkInAutoshipTemplateTab(),"Cancel Edit link is not on Autoship template Tab Page");
		cscockpitAutoshipTemplateTabPage.clickRemoveLinkToRemoveProductFromAutoshipCart();	
		if(cscockpitAutoshipTemplateTabPage.verifyThresholdPopupForOrderTotal()){
			cscockpitAutoshipTemplateTabPage.clickOKOfThresholdPopupInAutoshipTemplateTab();
		}
		cscockpitAutoshipTemplateTabPage.clickAddMoreLinesLinkInAutoShipTemplateTab();
		cscockpitAutoshipCartTabPage.selectValueFromSortByDDInCartTab("Price: High to Low");
		cscockpitAutoshipCartTabPage.selectCatalogFromDropDownInCartTab(); 
		randomProductSequenceNumber = String.valueOf(cscockpitAutoshipCartTabPage.getRandomProductWithSKUFromSearchResult()); 
		SKUValue = cscockpitAutoshipCartTabPage.getCustomerSKUValueInCartTab(randomProductSequenceNumber);
		cscockpitAutoshipCartTabPage.searchSKUValueInCartTab(SKUValue);
		cscockpitAutoshipCartTabPage.clickAddToCartBtnInCartTab();
		cscockpitAutoshipCartTabPage.clickCheckoutBtnInCartTab();
		cscockpitAutoshipTemplateUpdateTabPage.clickAddNewAddressUnderDeliveryAddressInCheckoutTab();
		s_assert.assertTrue(cscockpitAutoshipTemplateTabPage.verifyDeliveryAndShippingAddressPopupPresent("Create Delivery Address"),"Create Delivery Address Popup is not Present");
		cscockpitAutoshipTemplateUpdateTabPage.enterShippingInfoInAddNewPaymentProfilePopupWithoutSaveBtn(attendentFirstName, attendeeLastName, addressLine, city, postalCode, Country, Province, phoneNumber);
		cscockpitAutoshipTemplateUpdateTabPage.clickCreateNewAddressButtonInPopupAutoshipTemplateTabPage();
		cscockpitAutoshipTemplateUpdateTabPage.clickUseEnteredAddressOfCreateNewAddressShippingAddressPopup();
		cscockpitAutoshipTemplateUpdateTabPage.clickAddNewPaymentAddressInCheckoutTab();
		cscockpitAutoshipTemplateUpdateTabPage.enterBillingInfo();
		cscockpitAutoshipTemplateUpdateTabPage.clickSaveAddNewPaymentProfilePopUP();
		cscockpitAutoshipTemplateUpdateTabPage.enterCVVValueInCheckoutTab(TestConstants.SECURITY_CODE);
		cscockpitAutoshipTemplateUpdateTabPage.clickUseThisCardBtnInCheckoutTab();
		cscockpitAutoshipTemplateUpdateTabPage.clickUpdateAutoshipTemplateInAutoshipTemplateUpdateTab();
		double subTotal = cscockpitAutoshipTemplateTabPage.getSubTotalFromUI();
		double deliveryCost = cscockpitAutoshipTemplateTabPage.getDeliveryCostFromUI();
		double handlingCost = cscockpitAutoshipTemplateTabPage.getHandlingCostFromUI();
		if(cscockpitAutoshipTemplateTabPage.isGSTTaxPresentInUI()==true){
			gstFromUI = cscockpitAutoshipTemplateTabPage.getGstAmountFromUI();
			calculatedGstAmount = cscockpitAutoshipTemplateTabPage.calculateAmountAccordingToPercent(subTotal, deliveryCost, handlingCost, TestConstants.FIVE_PERCENT_TAX);
			s_assert.assertTrue(cscockpitAutoshipTemplateTabPage.validateGstAmountOnUI(gstFromUI,calculatedGstAmount),"GST Amount on UI is "+gstFromUI+" and calculated GST Amount is "+calculatedGstAmount+"");
		}else{
			logger.info("GST NOT PRESENT FOR THIS ORDER");
		}
		if(cscockpitAutoshipTemplateTabPage.isPstTaxPresentInUI() == true){
			pstFromUI = cscockpitAutoshipTemplateTabPage.getPstAmountFromUI();
			calculatedPstAmount = cscockpitAutoshipTemplateTabPage.calculateAmountAccordingToPercent(subTotal, deliveryCost, handlingCost, TestConstants.NINE_PERCENT_TAX);
			s_assert.assertTrue(cscockpitAutoshipTemplateTabPage.validatePstAmountOnUI(pstFromUI,calculatedPstAmount),"PST Amount on UI is "+pstFromUI+" and calculated GST Amount is "+calculatedPstAmount);
		}else{
			logger.info("PST NOT PRESENT FOR THIS ORDER");
		}

		s_assert.assertAll();
	}

	//Hybris Project-1591:To verify the Canada tax for Yukon for CRP Autoship
	@Test
	public void testVerifyCanadaTaxForYukonForCRPAutoship_1591(){
		RFO_DB = driver.getDBNameRFO();
		int randomNumber=CommonUtils.getRandomNum(10000, 1000000);
		String randomCustomerSequenceNumber = null;
		String randomProductSequenceNumber = null;
		String SKUValue = null;
		String attendentFirstName=TestConstants.FIRST_NAME+randomNumber;
		String attendeeLastName = TestConstants.LAST_NAME+randomNumber;
		String  addressLine=TestConstants.ADDRESS_LINE_1_CA;
		String city=TestConstants.CITY_CA;
		String postalCode=TestConstants.POSTAL_CODE_CA;
		String Country=TestConstants.COUNTRY_DD_VALUE_CA;
		String Province=TestConstants.PROVINCE_YUKON;
		String phoneNumber=TestConstants.PHONE_NUMBER_CA;
		double gstFromUI = 0.00;
		double calculatedGstAmount = 0.00;
		double pstFromUI = 0.00;
		double calculatedPstAmount = 0.00;
		//Declare variables.
		List<Map<String, Object>> randomConsultantList =  null;
		List<Map<String, Object>> randomConsultantUsernameList =  null;
		String consultantEmailID=null;
		String accountID=null;
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
		//Login to cscockpit.
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
		cscockpitAutoshipTemplateTabPage.clickEditTemplateLinkInAutoshipTemplateTab();
		s_assert.assertTrue(cscockpitAutoshipTemplateTabPage.verifyCancelEditLinkInAutoshipTemplateTab(),"Cancel Edit link is not on Autoship template Tab Page");
		cscockpitAutoshipTemplateTabPage.clickRemoveLinkToRemoveProductFromAutoshipCart();	
		if(cscockpitAutoshipTemplateTabPage.verifyThresholdPopupInAutoshipTemplateTab()){
			cscockpitAutoshipTemplateTabPage.clickOKOfThresholdPopupInAutoshipTemplateTab();
		}
		cscockpitAutoshipTemplateTabPage.clickAddMoreLinesLinkInAutoShipTemplateTab();
		cscockpitAutoshipCartTabPage.selectValueFromSortByDDInCartTab("Price: High to Low");
		cscockpitAutoshipCartTabPage.selectCatalogFromDropDownInCartTab(); 
		randomProductSequenceNumber = String.valueOf(cscockpitAutoshipCartTabPage.getRandomProductWithSKUFromSearchResult()); 
		SKUValue = cscockpitAutoshipCartTabPage.getCustomerSKUValueInCartTab(randomProductSequenceNumber);
		cscockpitAutoshipCartTabPage.searchSKUValueInCartTab(SKUValue);
		cscockpitAutoshipCartTabPage.clickAddToCartBtnTillProductAddedInCartTab();
		cscockpitAutoshipCartTabPage.clickCheckoutBtnInCartTab();
		cscockpitAutoshipTemplateUpdateTabPage.clickAddNewAddressUnderDeliveryAddressInCheckoutTab();
		s_assert.assertTrue(cscockpitAutoshipTemplateTabPage.verifyDeliveryAndShippingAddressPopupPresent("Create Delivery Address"),"Create Delivery Address Popup is not Present");
		cscockpitAutoshipTemplateUpdateTabPage.enterShippingInfoInAddNewPaymentProfilePopupWithoutSaveBtn(attendentFirstName, attendeeLastName, addressLine, city, postalCode, Country, Province, phoneNumber);
		cscockpitAutoshipTemplateUpdateTabPage.clickCreateNewAddressButtonInPopupAutoshipTemplateTabPage();
		cscockpitAutoshipTemplateUpdateTabPage.clickUseEnteredAddressOfCreateNewAddressShippingAddressPopup();
		cscockpitAutoshipTemplateUpdateTabPage.clickAddNewPaymentAddressInCheckoutTab();
		cscockpitAutoshipTemplateUpdateTabPage.enterBillingInfo();
		cscockpitAutoshipTemplateUpdateTabPage.clickSaveAddNewPaymentProfilePopUP();
		cscockpitAutoshipTemplateUpdateTabPage.enterCVVValueInCheckoutTab(TestConstants.SECURITY_CODE);
		cscockpitAutoshipTemplateUpdateTabPage.clickUseThisCardBtnInCheckoutTab();
		cscockpitAutoshipTemplateUpdateTabPage.clickUpdateAutoshipTemplateInAutoshipTemplateUpdateTab();
		double subTotal = cscockpitAutoshipTemplateTabPage.getSubTotalFromUI();
		double deliveryCost = cscockpitAutoshipTemplateTabPage.getDeliveryCostFromUI();
		double handlingCost = cscockpitAutoshipTemplateTabPage.getHandlingCostFromUI();
		if(cscockpitAutoshipTemplateTabPage.isGSTTaxPresentInUI()==true){
			gstFromUI = cscockpitAutoshipTemplateTabPage.getGstAmountFromUI();
			calculatedGstAmount = cscockpitAutoshipTemplateTabPage.calculateAmountAccordingToPercent(subTotal, deliveryCost, handlingCost, TestConstants.FIVE_PERCENT_TAX);
			s_assert.assertTrue(cscockpitAutoshipTemplateTabPage.validateGstAmountOnUI(gstFromUI,calculatedGstAmount),"GST Amount on UI is "+gstFromUI+" and calculated GST Amount is "+calculatedGstAmount+"");
		}else{
			logger.info("GST NOT PRESENT FOR THIS ORDER");
		}
		if(cscockpitAutoshipTemplateTabPage.isPstTaxPresentInUI() == true){
			pstFromUI = cscockpitAutoshipTemplateTabPage.getPstAmountFromUI();
			calculatedPstAmount = cscockpitAutoshipTemplateTabPage.calculateAmountAccordingToPercent(subTotal, deliveryCost, handlingCost, TestConstants.NINE_PERCENT_TAX);
			s_assert.assertTrue(cscockpitAutoshipTemplateTabPage.validatePstAmountOnUI(pstFromUI,calculatedPstAmount),"PST Amount on UI is "+pstFromUI+" and calculated GST Amount is "+calculatedPstAmount);
		}else{
			logger.info("PST NOT PRESENT FOR THIS ORDER");
		}

		s_assert.assertAll();
	}

	//Hybris Project-1592:To verify the Canada tax for Northwest Territories for CRP Autoship
	@Test
	public void testVerifyCanadaTaxForNorthwestTerritoriesForCRPAutoship_1592(){
		RFO_DB = driver.getDBNameRFO();
		int randomNumber=CommonUtils.getRandomNum(10000, 1000000);
		String randomCustomerSequenceNumber = null;
		String randomProductSequenceNumber = null;
		String SKUValue = null;
		String attendentFirstName=TestConstants.FIRST_NAME+randomNumber;
		String attendeeLastName = TestConstants.LAST_NAME+randomNumber;
		String  addressLine=TestConstants.ADDRESS_LINE_1_CA;
		String city=TestConstants.CITY_CA;
		String postalCode=TestConstants.POSTAL_CODE_CA;
		String Country=TestConstants.COUNTRY_DD_VALUE_CA;
		String Province=TestConstants.PROVINCE_NORTHWEST_TERRITORIES;
		String phoneNumber=TestConstants.PHONE_NUMBER_CA;
		double gstFromUI = 0.00;
		double calculatedGstAmount = 0.00;
		double pstFromUI = 0.00;
		double calculatedPstAmount = 0.00;
		//Declare variables.
		List<Map<String, Object>> randomConsultantList =  null;
		List<Map<String, Object>> randomConsultantUsernameList =  null;
		String consultantEmailID=null;
		String accountID=null;
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
		//Login to cscockpit.
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
		cscockpitAutoshipTemplateTabPage.clickEditTemplateLinkInAutoshipTemplateTab();
		s_assert.assertTrue(cscockpitAutoshipTemplateTabPage.verifyCancelEditLinkInAutoshipTemplateTab(),"Cancel Edit link is not on Autoship template Tab Page");
		cscockpitAutoshipTemplateTabPage.clickRemoveLinkToRemoveProductFromAutoshipCart();	
		if(cscockpitAutoshipTemplateTabPage.verifyThresholdPopupInAutoshipTemplateTab()){
			cscockpitAutoshipTemplateTabPage.clickOKOfThresholdPopupInAutoshipTemplateTab();
		}
		cscockpitAutoshipTemplateTabPage.clickAddMoreLinesLinkInAutoShipTemplateTab();
		cscockpitAutoshipCartTabPage.selectValueFromSortByDDInCartTab("Price: High to Low");
		cscockpitAutoshipCartTabPage.selectCatalogFromDropDownInCartTab(); 
		randomProductSequenceNumber = String.valueOf(cscockpitAutoshipCartTabPage.getRandomProductWithSKUFromSearchResult()); 
		SKUValue = cscockpitAutoshipCartTabPage.getCustomerSKUValueInCartTab(randomProductSequenceNumber);
		cscockpitAutoshipCartTabPage.searchSKUValueInCartTab(SKUValue);
		cscockpitAutoshipCartTabPage.clickAddToCartBtnTillProductAddedInCartTab();
		cscockpitAutoshipCartTabPage.clickCheckoutBtnInCartTab();
		cscockpitAutoshipTemplateUpdateTabPage.clickAddNewAddressUnderDeliveryAddressInCheckoutTab();
		s_assert.assertTrue(cscockpitAutoshipTemplateTabPage.verifyDeliveryAndShippingAddressPopupPresent("Create Delivery Address"),"Create Delivery Address Popup is not Present");
		cscockpitAutoshipTemplateUpdateTabPage.enterShippingInfoInAddNewPaymentProfilePopupWithoutSaveBtn(attendentFirstName, attendeeLastName, addressLine, city, postalCode, Country, Province, phoneNumber);
		cscockpitAutoshipTemplateUpdateTabPage.clickCreateNewAddressButtonInPopupAutoshipTemplateTabPage();
		cscockpitAutoshipTemplateUpdateTabPage.clickUseEnteredAddressOfCreateNewAddressShippingAddressPopup();
		cscockpitAutoshipTemplateUpdateTabPage.clickAddNewPaymentAddressInCheckoutTab();
		cscockpitAutoshipTemplateUpdateTabPage.enterBillingInfo();
		cscockpitAutoshipTemplateUpdateTabPage.clickSaveAddNewPaymentProfilePopUP();
		cscockpitAutoshipTemplateUpdateTabPage.enterCVVValueInCheckoutTab(TestConstants.SECURITY_CODE);
		cscockpitAutoshipTemplateUpdateTabPage.clickUseThisCardBtnInCheckoutTab();
		cscockpitAutoshipTemplateUpdateTabPage.clickUpdateAutoshipTemplateInAutoshipTemplateUpdateTab();
		double subTotal = cscockpitAutoshipTemplateTabPage.getSubTotalFromUI();
		double deliveryCost = cscockpitAutoshipTemplateTabPage.getDeliveryCostFromUI();
		double handlingCost = cscockpitAutoshipTemplateTabPage.getHandlingCostFromUI();
		if(cscockpitAutoshipTemplateTabPage.isGSTTaxPresentInUI()==true){
			gstFromUI = cscockpitAutoshipTemplateTabPage.getGstAmountFromUI();
			calculatedGstAmount = cscockpitAutoshipTemplateTabPage.calculateAmountAccordingToPercent(subTotal, deliveryCost, handlingCost, TestConstants.FIVE_PERCENT_TAX);
			s_assert.assertTrue(cscockpitAutoshipTemplateTabPage.validateGstAmountOnUI(gstFromUI,calculatedGstAmount),"GST Amount on UI is "+gstFromUI+" and calculated GST Amount is "+calculatedGstAmount+"");
		}else{
			logger.info("GST NOT PRESENT FOR THIS ORDER");
		}
		if(cscockpitAutoshipTemplateTabPage.isPstTaxPresentInUI() == true){
			pstFromUI = cscockpitAutoshipTemplateTabPage.getPstAmountFromUI();
			calculatedPstAmount = cscockpitAutoshipTemplateTabPage.calculateAmountAccordingToPercent(subTotal, deliveryCost, handlingCost, TestConstants.SEVEN_PERCENT_TAX);
			s_assert.assertTrue(cscockpitAutoshipTemplateTabPage.validatePstAmountOnUI(pstFromUI,calculatedPstAmount),"PST Amount on UI is "+pstFromUI+" and calculated GST Amount is "+calculatedPstAmount);
		}else{
			logger.info("PST NOT PRESENT FOR THIS ORDER");
		}

		s_assert.assertAll();
	}

	//Hybris Project-1593:To verify the Canada tax for Nunavat for CRP Autoship
	@Test
	public void testVerifyCanadaTaxForNunavutForCRPAutoship_1593(){
		RFO_DB = driver.getDBNameRFO();
		int randomNumber=CommonUtils.getRandomNum(10000, 1000000);
		String randomCustomerSequenceNumber = null;
		String randomProductSequenceNumber = null;
		String SKUValue = null;
		String attendentFirstName=TestConstants.FIRST_NAME+randomNumber;
		String attendeeLastName = TestConstants.LAST_NAME+randomNumber;
		String  addressLine=TestConstants.ADDRESS_LINE_1_CA;
		String city=TestConstants.CITY_CA;
		String postalCode=TestConstants.POSTAL_CODE_CA;
		String Country=TestConstants.COUNTRY_DD_VALUE_CA;
		String Province=TestConstants.PROVINCE_NUNAVUT;
		String phoneNumber=TestConstants.PHONE_NUMBER_CA;
		double gstFromUI = 0.00;
		double calculatedGstAmount = 0.00;
		double pstFromUI = 0.00;
		double calculatedPstAmount = 0.00;
		//Declare variables.
		List<Map<String, Object>> randomConsultantList =  null;
		List<Map<String, Object>> randomConsultantUsernameList =  null;
		String consultantEmailID=null;
		String accountID=null;
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
		//Login to cscockpit.
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
		cscockpitAutoshipTemplateTabPage.clickEditTemplateLinkInAutoshipTemplateTab();
		s_assert.assertTrue(cscockpitAutoshipTemplateTabPage.verifyCancelEditLinkInAutoshipTemplateTab(),"Cancel Edit link is not on Autoship template Tab Page");
		cscockpitAutoshipTemplateTabPage.clickRemoveLinkToRemoveProductFromAutoshipCart();	
		if(cscockpitAutoshipTemplateTabPage.verifyThresholdPopupInAutoshipTemplateTab()){
			cscockpitAutoshipTemplateTabPage.clickOKOfThresholdPopupInAutoshipTemplateTab();
		}
		cscockpitAutoshipTemplateTabPage.clickAddMoreLinesLinkInAutoShipTemplateTab();
		cscockpitAutoshipCartTabPage.selectValueFromSortByDDInCartTab("Price: High to Low");
		cscockpitAutoshipCartTabPage.selectCatalogFromDropDownInCartTab(); 
		randomProductSequenceNumber = String.valueOf(cscockpitAutoshipCartTabPage.getRandomProductWithSKUFromSearchResult()); 
		SKUValue = cscockpitAutoshipCartTabPage.getCustomerSKUValueInCartTab(randomProductSequenceNumber);
		cscockpitAutoshipCartTabPage.searchSKUValueInCartTab(SKUValue);
		cscockpitAutoshipCartTabPage.clickAddToCartBtnTillProductAddedInCartTab();
		cscockpitAutoshipCartTabPage.clickCheckoutBtnInCartTab();
		cscockpitAutoshipTemplateUpdateTabPage.clickAddNewAddressUnderDeliveryAddressInCheckoutTab();
		s_assert.assertTrue(cscockpitAutoshipTemplateTabPage.verifyDeliveryAndShippingAddressPopupPresent("Create Delivery Address"),"Create Delivery Address Popup is not Present");
		cscockpitAutoshipTemplateUpdateTabPage.enterShippingInfoInAddNewPaymentProfilePopupWithoutSaveBtn(attendentFirstName, attendeeLastName, addressLine, city, postalCode, Country, Province, phoneNumber);
		cscockpitAutoshipTemplateUpdateTabPage.clickCreateNewAddressButtonInPopupAutoshipTemplateTabPage();
		cscockpitAutoshipTemplateUpdateTabPage.clickUseEnteredAddressOfCreateNewAddressShippingAddressPopup();
		cscockpitAutoshipTemplateUpdateTabPage.clickAddNewPaymentAddressInCheckoutTab();
		cscockpitAutoshipTemplateUpdateTabPage.enterBillingInfo();
		cscockpitAutoshipTemplateUpdateTabPage.clickSaveAddNewPaymentProfilePopUP();
		cscockpitAutoshipTemplateUpdateTabPage.enterCVVValueInCheckoutTab(TestConstants.SECURITY_CODE);
		cscockpitAutoshipTemplateUpdateTabPage.clickUseThisCardBtnInCheckoutTab();
		cscockpitAutoshipTemplateUpdateTabPage.clickUpdateAutoshipTemplateInAutoshipTemplateUpdateTab();
		double subTotal = cscockpitAutoshipTemplateTabPage.getSubTotalFromUI();
		double deliveryCost = cscockpitAutoshipTemplateTabPage.getDeliveryCostFromUI();
		double handlingCost = cscockpitAutoshipTemplateTabPage.getHandlingCostFromUI();
		if(cscockpitAutoshipTemplateTabPage.isGSTTaxPresentInUI()==true){
			gstFromUI = cscockpitAutoshipTemplateTabPage.getGstAmountFromUI();
			calculatedGstAmount = cscockpitAutoshipTemplateTabPage.calculateAmountAccordingToPercent(subTotal, deliveryCost, handlingCost, TestConstants.FIVE_PERCENT_TAX);
			s_assert.assertTrue(cscockpitAutoshipTemplateTabPage.validateGstAmountOnUI(gstFromUI,calculatedGstAmount),"GST Amount on UI is "+gstFromUI+" and calculated GST Amount is "+calculatedGstAmount+"");
		}else{
			logger.info("GST NOT PRESENT FOR THIS ORDER");
		}
		if(cscockpitAutoshipTemplateTabPage.isPstTaxPresentInUI() == true){
			pstFromUI = cscockpitAutoshipTemplateTabPage.getPstAmountFromUI();
			calculatedPstAmount = cscockpitAutoshipTemplateTabPage.calculateAmountAccordingToPercent(subTotal, deliveryCost, handlingCost, TestConstants.SEVEN_PERCENT_TAX);
			s_assert.assertTrue(cscockpitAutoshipTemplateTabPage.validatePstAmountOnUI(pstFromUI,calculatedPstAmount),"PST Amount on UI is "+pstFromUI+" and calculated GST Amount is "+calculatedPstAmount);
		}else{
			logger.info("PST NOT PRESENT FOR THIS ORDER");
		}
		s_assert.assertAll();
	}
	//--
	//Hybris Project-1567:To verify the Canada tax for Prince Edward Island for Retail Order
	@Test
	public void testToVerifyCanadaTaxForPrinceEdwardIslandForRCOrder_1567(){ 
		int randomNumber=CommonUtils.getRandomNum(10000, 1000000);
		String randomCustomerSequenceNumber = null;
		String randomProductSequenceNumber = null;
		String SKUValue = null;
		String attendentFirstName=TestConstants.FIRST_NAME+randomNumber;
		String attendeeLastName = TestConstants.LAST_NAME+randomNumber;
		String  addressLine=TestConstants.ADDRESS_LINE_1_CA;
		String city=TestConstants.CITY_CA;
		String postalCode=TestConstants.POSTAL_CODE_CA;
		String Country=TestConstants.COUNTRY_DD_VALUE_CA;
		String province=TestConstants.PROVINCE_PRINCE_EDWARD_ISLAND;
		String phoneNumber=TestConstants.PHONE_NUMBER_CA;
		double gstFromUI = 0.00;
		double calculatedGstAmount = 0.00;
		double pstFromUI = 0.00;
		double calculatedPstAmount = 0.00;
		cscockpitCustomerSearchTabPage = cscockpitLoginPage.clickLoginBtn();
		cscockpitCustomerSearchTabPage.selectCustomerTypeFromDropDownInCustomerSearchTab("RETAIL");
		cscockpitCustomerSearchTabPage.selectCountryFromDropDownInCustomerSearchTab("Canada");
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
		cscockpitCustomerTabPage.enterShippingInfoInAddNewPaymentProfilePopupWithoutSaveBtn(attendentFirstName, attendeeLastName, addressLine, city, postalCode, Country, province, phoneNumber);
		cscockpitCheckoutTabPage.clickOnCreateNewAddressButtonInAutoshipTemplateTabPage();
		cscockpitCheckoutTabPage.clickUseEnteredAddressOfCreateNewAddressShippingAddressPopup();
		cscockpitCheckoutTabPage.clickAddNewPaymentAddressInCheckoutTab();
		cscockpitCheckoutTabPage.enterBillingInfo();
		cscockpitCheckoutTabPage.clickSaveAddNewPaymentProfilePopUP();
		cscockpitCheckoutTabPage.enterCVVValueInCheckoutTab(TestConstants.SECURITY_CODE);
		cscockpitCheckoutTabPage.clickUseThisCardBtnInCheckoutTab();
		cscockpitCheckoutTabPage.clickPlaceOrderButtonInCheckoutTab();
		double subTotal = cscockpitOrderTabPage.getSubTotalFromUI();
		double deliveryCost = cscockpitOrderTabPage.getDeliveryCostFromUI();
		double handlingCost = cscockpitOrderTabPage.getHandlingCostFromUI();
		if(cscockpitCheckoutTabPage.isGSTTaxPresentInUI()==true){
			gstFromUI = cscockpitOrderTabPage.getGstAmountFromUI();
			calculatedGstAmount = cscockpitOrderTabPage.calculateAmountAccordingToPercent(subTotal, deliveryCost, handlingCost, TestConstants.FIVE_PERCENT_TAX);
			s_assert.assertTrue(cscockpitOrderTabPage.validateGstAmountOnUI(gstFromUI,calculatedGstAmount),"GST Amount on UI is "+gstFromUI+" and calculated GST Amount is "+calculatedGstAmount+"");
		}else{
			logger.info("GST NOT PRESENT FOR THIS ORDER");
		}
		if(cscockpitOrderTabPage.isPstTaxPresentInUI() == true){
			pstFromUI = cscockpitOrderTabPage.getPstAmountFromUI();
			calculatedPstAmount = cscockpitOrderTabPage.calculateAmountAccordingToPercent(subTotal, deliveryCost, handlingCost, TestConstants.SEVEN_PERCENT_TAX);
			s_assert.assertTrue(cscockpitOrderTabPage.validatePstAmountOnUI(pstFromUI,calculatedPstAmount),"PST Amount on UI is "+pstFromUI+" and calculated GST Amount is "+calculatedPstAmount);
		}else{
			logger.info("PST NOT PRESENT FOR THIS ORDER");
		}
		s_assert.assertAll();
	}

	//Hybris Project-1568:To verify the Canada tax for New foundland for Retail Order
	@Test
	public void testToVerifyCanadaTaxForNewFoundlandForRCOrder_1568(){ 
		int randomNumber=CommonUtils.getRandomNum(10000, 1000000);
		String randomCustomerSequenceNumber = null;
		String randomProductSequenceNumber = null;
		String SKUValue = null;
		String attendentFirstName=TestConstants.FIRST_NAME+randomNumber;
		String attendeeLastName = TestConstants.LAST_NAME+randomNumber;
		String  addressLine=TestConstants.ADDRESS_LINE_1_CA;
		String city=TestConstants.CITY_CA;
		String postalCode=TestConstants.POSTAL_CODE_CA;
		String Country=TestConstants.COUNTRY_DD_VALUE_CA;
		String province=TestConstants.PROVINCE_NEW_FOUNDLAND_AND_LABRADOR;
		String phoneNumber=TestConstants.PHONE_NUMBER_CA;
		double gstFromUI = 0.00;
		double calculatedGstAmount = 0.00;
		double pstFromUI = 0.00;
		double calculatedPstAmount = 0.00;
		cscockpitCustomerSearchTabPage = cscockpitLoginPage.clickLoginBtn();
		cscockpitCustomerSearchTabPage.selectCustomerTypeFromDropDownInCustomerSearchTab("RETAIL");
		cscockpitCustomerSearchTabPage.selectCountryFromDropDownInCustomerSearchTab("Canada");
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
		cscockpitCustomerTabPage.enterShippingInfoInAddNewPaymentProfilePopupWithoutSaveBtn(attendentFirstName, attendeeLastName, addressLine, city, postalCode, Country, province, phoneNumber);
		cscockpitCheckoutTabPage.clickOnCreateNewAddressButtonInAutoshipTemplateTabPage();
		cscockpitCheckoutTabPage.clickUseEnteredAddressOfCreateNewAddressShippingAddressPopup();
		cscockpitCheckoutTabPage.clickAddNewPaymentAddressInCheckoutTab();
		cscockpitCheckoutTabPage.enterBillingInfo();
		cscockpitCheckoutTabPage.clickSaveAddNewPaymentProfilePopUP();
		cscockpitCheckoutTabPage.enterCVVValueInCheckoutTab(TestConstants.SECURITY_CODE);
		cscockpitCheckoutTabPage.clickUseThisCardBtnInCheckoutTab();
		cscockpitCheckoutTabPage.clickPlaceOrderButtonInCheckoutTab();
		double subTotal = cscockpitOrderTabPage.getSubTotalFromUI();
		double deliveryCost = cscockpitOrderTabPage.getDeliveryCostFromUI();
		double handlingCost = cscockpitOrderTabPage.getHandlingCostFromUI();
		if(cscockpitCheckoutTabPage.isGSTTaxPresentInUI()==true){
			gstFromUI = cscockpitOrderTabPage.getGstAmountFromUI();
			calculatedGstAmount = cscockpitOrderTabPage.calculateAmountAccordingToPercent(subTotal, deliveryCost, handlingCost, TestConstants.FIVE_PERCENT_TAX);
			s_assert.assertTrue(cscockpitOrderTabPage.validateGstAmountOnUI(gstFromUI,calculatedGstAmount),"GST Amount on UI is "+gstFromUI+" and calculated GST Amount is "+calculatedGstAmount+"");
		}else{
			logger.info("GST NOT PRESENT FOR THIS ORDER");
		}
		if(cscockpitOrderTabPage.isPstTaxPresentInUI() == true){
			pstFromUI = cscockpitOrderTabPage.getPstAmountFromUI();
			calculatedPstAmount = cscockpitOrderTabPage.calculateAmountAccordingToPercent(subTotal, deliveryCost, handlingCost, TestConstants.SEVEN_PERCENT_TAX);
			s_assert.assertTrue(cscockpitOrderTabPage.validatePstAmountOnUI(pstFromUI,calculatedPstAmount),"PST Amount on UI is "+pstFromUI+" and calculated GST Amount is "+calculatedPstAmount);
		}else{
			logger.info("PST NOT PRESENT FOR THIS ORDER");
		}
		s_assert.assertAll();
	}

	//Hybris Project-1569:To verify the Canada tax for Yukon for Retail Order
	@Test
	public void testToVerifyCanadaTaxForYukonForRCOrder_1569(){ 
		int randomNumber=CommonUtils.getRandomNum(10000, 1000000);
		String randomCustomerSequenceNumber = null;
		String randomProductSequenceNumber = null;
		String SKUValue = null;
		String attendentFirstName=TestConstants.FIRST_NAME+randomNumber;
		String attendeeLastName = TestConstants.LAST_NAME+randomNumber;
		String  addressLine=TestConstants.ADDRESS_LINE_1_CA;
		String city=TestConstants.CITY_CA;
		String postalCode=TestConstants.POSTAL_CODE_CA;
		String Country=TestConstants.COUNTRY_DD_VALUE_CA;
		String province=TestConstants.PROVINCE_YUKON;
		String phoneNumber=TestConstants.PHONE_NUMBER_CA;
		double gstFromUI = 0.00;
		double calculatedGstAmount = 0.00;
		double pstFromUI = 0.00;
		double calculatedPstAmount = 0.00;
		cscockpitCustomerSearchTabPage = cscockpitLoginPage.clickLoginBtn();
		cscockpitCustomerSearchTabPage.selectCustomerTypeFromDropDownInCustomerSearchTab("RETAIL");
		cscockpitCustomerSearchTabPage.selectCountryFromDropDownInCustomerSearchTab("Canada");
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
		cscockpitCustomerTabPage.enterShippingInfoInAddNewPaymentProfilePopupWithoutSaveBtn(attendentFirstName, attendeeLastName, addressLine, city, postalCode, Country, province, phoneNumber);
		cscockpitCheckoutTabPage.clickOnCreateNewAddressButtonInAutoshipTemplateTabPage();
		cscockpitCheckoutTabPage.clickUseEnteredAddressOfCreateNewAddressShippingAddressPopup();
		cscockpitCheckoutTabPage.clickAddNewPaymentAddressInCheckoutTab();
		cscockpitCheckoutTabPage.enterBillingInfo();
		cscockpitCheckoutTabPage.clickSaveAddNewPaymentProfilePopUP();
		cscockpitCheckoutTabPage.enterCVVValueInCheckoutTab(TestConstants.SECURITY_CODE);
		cscockpitCheckoutTabPage.clickUseThisCardBtnInCheckoutTab();
		cscockpitCheckoutTabPage.clickPlaceOrderButtonInCheckoutTab();
		double subTotal = cscockpitOrderTabPage.getSubTotalFromUI();
		double deliveryCost = cscockpitOrderTabPage.getDeliveryCostFromUI();
		double handlingCost = cscockpitOrderTabPage.getHandlingCostFromUI();
		if(cscockpitCheckoutTabPage.isGSTTaxPresentInUI()==true){
			gstFromUI = cscockpitOrderTabPage.getGstAmountFromUI();
			calculatedGstAmount = cscockpitOrderTabPage.calculateAmountAccordingToPercent(subTotal, deliveryCost, handlingCost, TestConstants.FIVE_PERCENT_TAX);
			s_assert.assertTrue(cscockpitOrderTabPage.validateGstAmountOnUI(gstFromUI,calculatedGstAmount),"GST Amount on UI is "+gstFromUI+" and calculated GST Amount is "+calculatedGstAmount+"");
		}else{
			logger.info("GST NOT PRESENT FOR THIS ORDER");
		}
		if(cscockpitOrderTabPage.isPstTaxPresentInUI() == true){
			pstFromUI = cscockpitOrderTabPage.getPstAmountFromUI();
			calculatedPstAmount = cscockpitOrderTabPage.calculateAmountAccordingToPercent(subTotal, deliveryCost, handlingCost, TestConstants.SEVEN_PERCENT_TAX);
			s_assert.assertTrue(cscockpitOrderTabPage.validatePstAmountOnUI(pstFromUI,calculatedPstAmount),"PST Amount on UI is "+pstFromUI+" and calculated GST Amount is "+calculatedPstAmount);
		}else{
			logger.info("PST NOT PRESENT FOR THIS ORDER");
		}
		s_assert.assertAll();
	}

	//Hybris Project-1586:To verify the Canada tax for Quebec for CRP Autoship
	@Test(enabled=false)//WIP
	public void testVerifyCanadaTaxForQubecForCRPAutoship_1586(){
		RFO_DB = driver.getDBNameRFO();
		int randomNumber=CommonUtils.getRandomNum(10000, 1000000);
		String randomCustomerSequenceNumber = null;
		String randomProductSequenceNumber = null;
		String SKUValue = null;
		String attendentFirstName=TestConstants.FIRST_NAME+randomNumber;
		String attendeeLastName = TestConstants.LAST_NAME+randomNumber;
		String  addressLine=TestConstants.ADDRESS_LINE_1_CA;
		String city=TestConstants.CITY_CA;
		String postalCode=TestConstants.POSTAL_CODE_CA;
		String Country=TestConstants.COUNTRY_DD_VALUE_CA;
		String Province=TestConstants.PROVINCE_QUEBEC;
		String phoneNumber=TestConstants.PHONE_NUMBER_CA;
		double gstFromUI = 0.00;
		double calculatedGstAmount = 0.00;
		double pstFromUI = 0.00;
		double calculatedPstAmount = 0.00;
		//Declare variables.
		List<Map<String, Object>> randomConsultantList =  null;
		List<Map<String, Object>> randomConsultantUsernameList =  null;
		String consultantEmailID=null;
		String accountID=null;
		//Get Email from database.
		randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_CONSULTANT_WITH_ORDERS_AND_AUTOSHIPS_RFO,"40"),RFO_DB);
		accountID = String.valueOf(getValueFromQueryResult(randomConsultantList, "AccountID"));
		randomConsultantUsernameList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_EMAIL_ID_FROM_ACCOUNT_ID,accountID),RFO_DB);
		consultantEmailID = String.valueOf(getValueFromQueryResult(randomConsultantUsernameList, "EmailAddress"));  
		//Login to cscockpit.	
		cscockpitCustomerSearchTabPage = cscockpitLoginPage.clickLoginBtn();
		cscockpitCustomerSearchTabPage.selectCustomerTypeFromDropDownInCustomerSearchTab("CONSULTANT");
		cscockpitCustomerSearchTabPage.selectCountryFromDropDownInCustomerSearchTab("Canada");
		cscockpitCustomerSearchTabPage.selectAccountStatusFromDropDownInCustomerSearchTab("Active");
		cscockpitCustomerSearchTabPage.enterEmailIdInSearchFieldInCustomerSearchTab(consultantEmailID);
		cscockpitCustomerSearchTabPage.clickSearchBtn();
		randomCustomerSequenceNumber = String.valueOf(cscockpitCustomerSearchTabPage.getRandomCustomerFromSearchResult());
		cscockpitCustomerSearchTabPage.clickCIDNumberInCustomerSearchTab(randomCustomerSequenceNumber);
		cscockpitCustomerTabPage.getAndClickAutoshipIDHavingTypeAsCRPAutoshipAndStatusIsPending();
		cscockpitAutoshipTemplateTabPage.clickEditTemplateLinkInAutoshipTemplateTab();
		s_assert.assertTrue(cscockpitAutoshipTemplateTabPage.verifyCancelEditLinkInAutoshipTemplateTab(),"Cancel Edit link is not on Autoship template Tab Page");
		cscockpitAutoshipTemplateTabPage.clickRemoveLinkToRemoveProductFromAutoshipCart();	
		if(cscockpitAutoshipTemplateTabPage.verifyThresholdPopupInAutoshipTemplateTab()){
			cscockpitAutoshipTemplateTabPage.clickOKOfThresholdPopupInAutoshipTemplateTab();
		}
		cscockpitAutoshipTemplateTabPage.clickAddMoreLinesLinkInAutoShipTemplateTab();
		cscockpitAutoshipCartTabPage.selectValueFromSortByDDInCartTab("Price: High to Low");
		cscockpitAutoshipCartTabPage.selectCatalogFromDropDownInCartTab(); 
		randomProductSequenceNumber = String.valueOf(cscockpitAutoshipCartTabPage.getRandomProductWithSKUFromSearchResult()); 
		SKUValue = cscockpitAutoshipCartTabPage.getCustomerSKUValueInCartTab(randomProductSequenceNumber);
		cscockpitAutoshipCartTabPage.searchSKUValueInCartTab(SKUValue);
		cscockpitAutoshipCartTabPage.clickAddToCartBtnInCartTab();
		cscockpitAutoshipCartTabPage.clickCheckoutBtnInCartTab();
		cscockpitAutoshipTemplateUpdateTabPage.clickAddNewAddressUnderDeliveryAddressInCheckoutTab();
		s_assert.assertTrue(cscockpitAutoshipTemplateTabPage.verifyDeliveryAndShippingAddressPopupPresent("Create Delivery Address"),"Create Delivery Address Popup is not Present");
		cscockpitAutoshipTemplateUpdateTabPage.enterShippingInfoInAddNewPaymentProfilePopupWithoutSaveBtn(attendentFirstName, attendeeLastName, addressLine, city, postalCode, Country, Province, phoneNumber);
		cscockpitAutoshipTemplateUpdateTabPage.clickCreateNewAddressButtonInPopupAutoshipTemplateTabPage();
		cscockpitAutoshipTemplateUpdateTabPage.clickUseEnteredAddressOfCreateNewAddressShippingAddressPopup();
		cscockpitAutoshipTemplateUpdateTabPage.clickAddNewPaymentAddressInCheckoutTab();
		cscockpitAutoshipTemplateUpdateTabPage.enterBillingInfo();
		cscockpitAutoshipTemplateUpdateTabPage.clickSaveAddNewPaymentProfilePopUP();
		cscockpitAutoshipTemplateUpdateTabPage.enterCVVValueInCheckoutTab(TestConstants.SECURITY_CODE);
		cscockpitAutoshipTemplateUpdateTabPage.clickUseThisCardBtnInCheckoutTab();
		cscockpitAutoshipTemplateUpdateTabPage.clickUpdateAutoshipTemplateInAutoshipTemplateUpdateTab();
		double subTotal = cscockpitAutoshipTemplateTabPage.getSubTotalFromUI();
		double deliveryCost = cscockpitAutoshipTemplateTabPage.getDeliveryCostFromUI();
		double handlingCost = cscockpitAutoshipTemplateTabPage.getHandlingCostFromUI();
		if(cscockpitAutoshipTemplateTabPage.isGSTTaxPresentInUI()==true){
			gstFromUI = cscockpitAutoshipTemplateTabPage.getGstAmountFromUI();
			calculatedGstAmount = cscockpitAutoshipTemplateTabPage.calculateAmountAccordingToPercent(subTotal, deliveryCost, handlingCost, TestConstants.FIVE_PERCENT_TAX);
			s_assert.assertTrue(cscockpitAutoshipTemplateTabPage.validateGstAmountOnUI(gstFromUI,calculatedGstAmount),"GST Amount on UI is "+gstFromUI+" and calculated GST Amount is "+calculatedGstAmount+"");
		}else{
			logger.info("GST NOT PRESENT FOR THIS ORDER");
		}
		if(cscockpitAutoshipTemplateTabPage.isPstTaxPresentInUI() == true){
			pstFromUI = cscockpitAutoshipTemplateTabPage.getPstAmountFromUI();
			calculatedPstAmount = cscockpitAutoshipTemplateTabPage.calculateAmountAccordingToPercent(subTotal, deliveryCost, handlingCost, TestConstants.SEVEN_PERCENT_TAX);
			s_assert.assertTrue(cscockpitAutoshipTemplateTabPage.validatePstAmountOnUI(pstFromUI,calculatedPstAmount),"PST Amount on UI is "+pstFromUI+" and calculated GST Amount is "+calculatedPstAmount);
		}else{
			logger.info("PST NOT PRESENT FOR THIS ORDER");
		}
		s_assert.assertAll();
	}

	//Hybris Project-1584:To verify the Canada tax for Manitoba for CRP Autoship
	@Test
	public void testVerifyCanadaTaxForManitobaForCRPAutoship_1584(){
		RFO_DB = driver.getDBNameRFO();
		int randomNumber=CommonUtils.getRandomNum(10000, 1000000);
		String randomCustomerSequenceNumber = null;
		String randomProductSequenceNumber = null;
		String SKUValue = null;
		String attendentFirstName=TestConstants.FIRST_NAME+randomNumber;
		String attendeeLastName = TestConstants.LAST_NAME+randomNumber;
		String  addressLine=TestConstants.ADDRESS_LINE_1_CA;
		String city=TestConstants.CITY_CA;
		String postalCode=TestConstants.POSTAL_CODE_CA;
		String Country=TestConstants.COUNTRY_DD_VALUE_CA;
		String Province=TestConstants.PROVINCE_MANITOBA;
		String phoneNumber=TestConstants.PHONE_NUMBER_CA;
		double gstFromUI = 0.00;
		double calculatedGstAmount = 0.00;
		double pstFromUI = 0.00;
		double calculatedPstAmount = 0.00;
		//Declare variables.
		List<Map<String, Object>> randomConsultantList =  null;
		List<Map<String, Object>> randomConsultantUsernameList =  null;
		String consultantEmailID=null;
		String accountID=null;
		//Get Email from database.
		randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_CONSULTANT_WITH_ORDERS_AND_AUTOSHIPS_RFO,"40"),RFO_DB);
		accountID = String.valueOf(getValueFromQueryResult(randomConsultantList, "AccountID"));
		randomConsultantUsernameList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_EMAIL_ID_FROM_ACCOUNT_ID,accountID),RFO_DB);
		consultantEmailID = String.valueOf(getValueFromQueryResult(randomConsultantUsernameList, "EmailAddress"));  
		//Login to cscockpit.	
		cscockpitCustomerSearchTabPage = cscockpitLoginPage.clickLoginBtn();
		cscockpitCustomerSearchTabPage.selectCustomerTypeFromDropDownInCustomerSearchTab("CONSULTANT");
		cscockpitCustomerSearchTabPage.selectCountryFromDropDownInCustomerSearchTab("Canada");
		cscockpitCustomerSearchTabPage.selectAccountStatusFromDropDownInCustomerSearchTab("Active");
		cscockpitCustomerSearchTabPage.enterEmailIdInSearchFieldInCustomerSearchTab(consultantEmailID);
		cscockpitCustomerSearchTabPage.clickSearchBtn();
		randomCustomerSequenceNumber = String.valueOf(cscockpitCustomerSearchTabPage.getRandomCustomerFromSearchResult());
		cscockpitCustomerSearchTabPage.clickCIDNumberInCustomerSearchTab(randomCustomerSequenceNumber);
		cscockpitCustomerTabPage.getAndClickAutoshipIDHavingTypeAsCRPAutoshipAndStatusIsPending();
		cscockpitAutoshipTemplateTabPage.clickEditTemplateLinkInAutoshipTemplateTab();
		s_assert.assertTrue(cscockpitAutoshipTemplateTabPage.verifyCancelEditLinkInAutoshipTemplateTab(),"Cancel Edit link is not on Autoship template Tab Page");
		cscockpitAutoshipTemplateTabPage.clickRemoveLinkToRemoveProductFromAutoshipCart();	
		if(cscockpitAutoshipTemplateTabPage.verifyThresholdPopupInAutoshipTemplateTab()){
			cscockpitAutoshipTemplateTabPage.clickOKOfThresholdPopupInAutoshipTemplateTab();
		}
		cscockpitAutoshipTemplateTabPage.clickAddMoreLinesLinkInAutoShipTemplateTab();
		cscockpitAutoshipCartTabPage.selectValueFromSortByDDInCartTab("Price: High to Low");
		cscockpitAutoshipCartTabPage.selectCatalogFromDropDownInCartTab(); 
		randomProductSequenceNumber = String.valueOf(cscockpitAutoshipCartTabPage.getRandomProductWithSKUFromSearchResult()); 
		SKUValue = cscockpitAutoshipCartTabPage.getCustomerSKUValueInCartTab(randomProductSequenceNumber);
		cscockpitAutoshipCartTabPage.searchSKUValueInCartTab(SKUValue);
		cscockpitAutoshipCartTabPage.clickAddToCartBtnInCartTab();
		cscockpitAutoshipCartTabPage.clickCheckoutBtnInCartTab();
		cscockpitAutoshipTemplateUpdateTabPage.clickAddNewAddressUnderDeliveryAddressInCheckoutTab();
		s_assert.assertTrue(cscockpitAutoshipTemplateTabPage.verifyDeliveryAndShippingAddressPopupPresent("Create Delivery Address"),"Create Delivery Address Popup is not Present");
		cscockpitAutoshipTemplateUpdateTabPage.enterShippingInfoInAddNewPaymentProfilePopupWithoutSaveBtn(attendentFirstName, attendeeLastName, addressLine, city, postalCode, Country, Province, phoneNumber);
		cscockpitAutoshipTemplateUpdateTabPage.clickCreateNewAddressButtonInPopupAutoshipTemplateTabPage();
		cscockpitAutoshipTemplateUpdateTabPage.clickUseEnteredAddressOfCreateNewAddressShippingAddressPopup();
		cscockpitAutoshipTemplateUpdateTabPage.clickAddNewPaymentAddressInCheckoutTab();
		cscockpitAutoshipTemplateUpdateTabPage.enterBillingInfo();
		cscockpitAutoshipTemplateUpdateTabPage.clickSaveAddNewPaymentProfilePopUP();
		cscockpitAutoshipTemplateUpdateTabPage.enterCVVValueInCheckoutTab(TestConstants.SECURITY_CODE);
		cscockpitAutoshipTemplateUpdateTabPage.clickUseThisCardBtnInCheckoutTab();
		cscockpitAutoshipTemplateUpdateTabPage.clickUpdateAutoshipTemplateInAutoshipTemplateUpdateTab();
		double subTotal = cscockpitAutoshipTemplateTabPage.getSubTotalFromUI();
		double deliveryCost = cscockpitAutoshipTemplateTabPage.getDeliveryCostFromUI();
		double handlingCost = cscockpitAutoshipTemplateTabPage.getHandlingCostFromUI();
		if(cscockpitAutoshipTemplateTabPage.isGSTTaxPresentInUI()==true){
			gstFromUI = cscockpitAutoshipTemplateTabPage.getGstAmountFromUI();
			calculatedGstAmount = cscockpitAutoshipTemplateTabPage.calculateAmountAccordingToPercent(subTotal, deliveryCost, handlingCost, TestConstants.FIVE_PERCENT_TAX);
			s_assert.assertTrue(cscockpitAutoshipTemplateTabPage.validateGstAmountOnUI(gstFromUI,calculatedGstAmount),"GST Amount on UI is "+gstFromUI+" and calculated GST Amount is "+calculatedGstAmount+"");
		}else{
			logger.info("GST NOT PRESENT FOR THIS ORDER");
		}
		if(cscockpitAutoshipTemplateTabPage.isPstTaxPresentInUI() == true){
			pstFromUI = cscockpitAutoshipTemplateTabPage.getPstAmountFromUI();
			calculatedPstAmount = cscockpitAutoshipTemplateTabPage.calculateAmountAccordingToPercent(subTotal, deliveryCost, handlingCost, TestConstants.SEVEN_PERCENT_TAX);
			s_assert.assertTrue(cscockpitAutoshipTemplateTabPage.validatePstAmountOnUI(pstFromUI,calculatedPstAmount),"PST Amount on UI is "+pstFromUI+" and calculated GST Amount is "+calculatedPstAmount);
		}else{
			logger.info("PST NOT PRESENT FOR THIS ORDER");
		}
		s_assert.assertAll();
	}

	//Hybris Project-1570:To verify the Canada tax for Nunavat for Retail Order
	@Test
	public void testToVerifyCanadaTaxForNunavatForRCOrder_1570(){ 
		int randomNumber=CommonUtils.getRandomNum(10000, 1000000);
		String randomCustomerSequenceNumber = null;
		String randomProductSequenceNumber = null;
		String SKUValue = null;
		String attendentFirstName=TestConstants.FIRST_NAME+randomNumber;
		String attendeeLastName = TestConstants.LAST_NAME+randomNumber;
		String  addressLine=TestConstants.ADDRESS_LINE_1_CA;
		String city=TestConstants.CITY_CA;
		String postalCode=TestConstants.POSTAL_CODE_CA;
		String Country=TestConstants.COUNTRY_DD_VALUE_CA;
		String province=TestConstants.PROVINCE_NUNAVUT;
		String phoneNumber=TestConstants.PHONE_NUMBER_CA;
		double gstFromUI = 0.00;
		double calculatedGstAmount = 0.00;
		double pstFromUI = 0.00;
		double calculatedPstAmount = 0.00;
		cscockpitCustomerSearchTabPage = cscockpitLoginPage.clickLoginBtn();
		cscockpitCustomerSearchTabPage.selectCustomerTypeFromDropDownInCustomerSearchTab("RETAIL");
		cscockpitCustomerSearchTabPage.selectCountryFromDropDownInCustomerSearchTab("Canada");
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
		cscockpitCustomerTabPage.enterShippingInfoInAddNewPaymentProfilePopupWithoutSaveBtn(attendentFirstName, attendeeLastName, addressLine, city, postalCode, Country, province, phoneNumber);
		cscockpitCheckoutTabPage.clickOnCreateNewAddressButtonInAutoshipTemplateTabPage();
		cscockpitCheckoutTabPage.clickUseEnteredAddressOfCreateNewAddressShippingAddressPopup();
		cscockpitCheckoutTabPage.clickAddNewPaymentAddressInCheckoutTab();
		cscockpitCheckoutTabPage.enterBillingInfo();
		cscockpitCheckoutTabPage.clickSaveAddNewPaymentProfilePopUP();
		cscockpitCheckoutTabPage.enterCVVValueInCheckoutTab(TestConstants.SECURITY_CODE);
		cscockpitCheckoutTabPage.clickUseThisCardBtnInCheckoutTab();
		cscockpitCheckoutTabPage.clickPlaceOrderButtonInCheckoutTab();
		double subTotal = cscockpitOrderTabPage.getSubTotalFromUI();
		double deliveryCost = cscockpitOrderTabPage.getDeliveryCostFromUI();
		double handlingCost = cscockpitOrderTabPage.getHandlingCostFromUI();
		if(cscockpitCheckoutTabPage.isGSTTaxPresentInUI()==true){
			gstFromUI = cscockpitOrderTabPage.getGstAmountFromUI();
			calculatedGstAmount = cscockpitOrderTabPage.calculateAmountAccordingToPercent(subTotal, deliveryCost, handlingCost, TestConstants.FIVE_PERCENT_TAX);
			s_assert.assertTrue(cscockpitOrderTabPage.validateGstAmountOnUI(gstFromUI,calculatedGstAmount),"GST Amount on UI is "+gstFromUI+" and calculated GST Amount is "+calculatedGstAmount+"");
		}else{
			logger.info("GST NOT PRESENT FOR THIS ORDER");
		}
		if(cscockpitOrderTabPage.isPstTaxPresentInUI() == true){
			pstFromUI = cscockpitOrderTabPage.getPstAmountFromUI();
			calculatedPstAmount = cscockpitOrderTabPage.calculateAmountAccordingToPercent(subTotal, deliveryCost, handlingCost, TestConstants.SEVEN_PERCENT_TAX);
			s_assert.assertTrue(cscockpitOrderTabPage.validatePstAmountOnUI(pstFromUI,calculatedPstAmount),"PST Amount on UI is "+pstFromUI+" and calculated GST Amount is "+calculatedPstAmount);
		}else{
			logger.info("PST NOT PRESENT FOR THIS ORDER");
		}
		s_assert.assertAll();
	}

	// Hybris Project-1581:To verify the Canada tax for Bristish Columbia for CRP Autoship
	@Test
	public void testVerifyCanadaTaxForBritishColumbiaForCRPAutoship_1581(){
		RFO_DB = driver.getDBNameRFO();
		int randomNumber=CommonUtils.getRandomNum(10000, 1000000);
		String randomCustomerSequenceNumber = null;
		String randomProductSequenceNumber = null;
		String SKUValue = null;
		String attendentFirstName=TestConstants.FIRST_NAME+randomNumber;
		String attendeeLastName = TestConstants.LAST_NAME+randomNumber;
		String  addressLine=TestConstants.ADDRESS_LINE_1_CA;
		String city=TestConstants.CITY_CA;
		String postalCode=TestConstants.POSTAL_CODE_CA;
		String Country=TestConstants.COUNTRY_DD_VALUE_CA;
		String Province=TestConstants.PROVINCE_BRITISH_COULMBIA;
		String phoneNumber=TestConstants.PHONE_NUMBER_CA;
		double gstFromUI = 0.00;
		double calculatedGstAmount = 0.00;
		double pstFromUI = 0.00;
		double calculatedPstAmount = 0.00;
		//Declare variables.
		List<Map<String, Object>> randomConsultantList =  null;
		List<Map<String, Object>> randomConsultantUsernameList =  null;
		String consultantEmailID=null;
		String accountID=null;
		//Get Email from database.
		randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_CONSULTANT_WITH_ORDERS_AND_AUTOSHIPS_RFO,"40"),RFO_DB);
		accountID = String.valueOf(getValueFromQueryResult(randomConsultantList, "AccountID"));
		randomConsultantUsernameList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_EMAIL_ID_FROM_ACCOUNT_ID,accountID),RFO_DB);
		consultantEmailID = String.valueOf(getValueFromQueryResult(randomConsultantUsernameList, "EmailAddress"));  
		//Login to cscockpit.	
		cscockpitCustomerSearchTabPage = cscockpitLoginPage.clickLoginBtn();
		cscockpitCustomerSearchTabPage.selectCustomerTypeFromDropDownInCustomerSearchTab("CONSULTANT");
		cscockpitCustomerSearchTabPage.selectCountryFromDropDownInCustomerSearchTab("Canada");
		cscockpitCustomerSearchTabPage.selectAccountStatusFromDropDownInCustomerSearchTab("Active");
		cscockpitCustomerSearchTabPage.enterEmailIdInSearchFieldInCustomerSearchTab(consultantEmailID);
		cscockpitCustomerSearchTabPage.clickSearchBtn();
		randomCustomerSequenceNumber = String.valueOf(cscockpitCustomerSearchTabPage.getRandomCustomerFromSearchResult());
		cscockpitCustomerSearchTabPage.clickCIDNumberInCustomerSearchTab(randomCustomerSequenceNumber);
		cscockpitCustomerTabPage.getAndClickAutoshipIDHavingTypeAsCRPAutoshipAndStatusIsPending();
		cscockpitAutoshipTemplateTabPage.clickEditTemplateLinkInAutoshipTemplateTab();
		s_assert.assertTrue(cscockpitAutoshipTemplateTabPage.verifyCancelEditLinkInAutoshipTemplateTab(),"Cancel Edit link is not on Autoship template Tab Page");
		cscockpitAutoshipTemplateTabPage.clickRemoveLinkToRemoveProductFromAutoshipCart();	
		if(cscockpitAutoshipTemplateTabPage.verifyThresholdPopupInAutoshipTemplateTab()){
			cscockpitAutoshipTemplateTabPage.clickOKOfThresholdPopupInAutoshipTemplateTab();
		}
		cscockpitAutoshipTemplateTabPage.clickAddMoreLinesLinkInAutoShipTemplateTab();
		cscockpitAutoshipCartTabPage.selectValueFromSortByDDInCartTab("Price: High to Low");
		cscockpitAutoshipCartTabPage.selectCatalogFromDropDownInCartTab(); 
		randomProductSequenceNumber = String.valueOf(cscockpitAutoshipCartTabPage.getRandomProductWithSKUFromSearchResult()); 
		SKUValue = cscockpitAutoshipCartTabPage.getCustomerSKUValueInCartTab(randomProductSequenceNumber);
		cscockpitAutoshipCartTabPage.searchSKUValueInCartTab(SKUValue);
		cscockpitAutoshipCartTabPage.clickAddToCartBtnInCartTab();
		cscockpitAutoshipCartTabPage.clickCheckoutBtnInCartTab();
		cscockpitAutoshipTemplateUpdateTabPage.clickAddNewAddressUnderDeliveryAddressInCheckoutTab();
		s_assert.assertTrue(cscockpitAutoshipTemplateTabPage.verifyDeliveryAndShippingAddressPopupPresent("Create Delivery Address"),"Create Delivery Address Popup is not Present");
		cscockpitAutoshipTemplateUpdateTabPage.enterShippingInfoInAddNewPaymentProfilePopupWithoutSaveBtn(attendentFirstName, attendeeLastName, addressLine, city, postalCode, Country, Province, phoneNumber);
		cscockpitAutoshipTemplateUpdateTabPage.clickCreateNewAddressButtonInPopupAutoshipTemplateTabPage();
		cscockpitAutoshipTemplateUpdateTabPage.clickUseEnteredAddressOfCreateNewAddressShippingAddressPopup();
		cscockpitAutoshipTemplateUpdateTabPage.clickAddNewPaymentAddressInCheckoutTab();
		cscockpitAutoshipTemplateUpdateTabPage.enterBillingInfo();
		cscockpitAutoshipTemplateUpdateTabPage.clickSaveAddNewPaymentProfilePopUP();
		cscockpitAutoshipTemplateUpdateTabPage.enterCVVValueInCheckoutTab(TestConstants.SECURITY_CODE);
		cscockpitAutoshipTemplateUpdateTabPage.clickUseThisCardBtnInCheckoutTab();
		cscockpitAutoshipTemplateUpdateTabPage.clickUpdateAutoshipTemplateInAutoshipTemplateUpdateTab();
		double subTotal = cscockpitAutoshipTemplateTabPage.getSubTotalFromUI();
		double deliveryCost = cscockpitAutoshipTemplateTabPage.getDeliveryCostFromUI();
		double handlingCost = cscockpitAutoshipTemplateTabPage.getHandlingCostFromUI();
		if(cscockpitAutoshipTemplateTabPage.isGSTTaxPresentInUI()==true){
			gstFromUI = cscockpitAutoshipTemplateTabPage.getGstAmountFromUI();
			calculatedGstAmount = cscockpitAutoshipTemplateTabPage.calculateAmountAccordingToPercent(subTotal, deliveryCost, handlingCost, TestConstants.FIVE_PERCENT_TAX);
			s_assert.assertTrue(cscockpitAutoshipTemplateTabPage.validateGstAmountOnUI(gstFromUI,calculatedGstAmount),"GST Amount on UI is "+gstFromUI+" and calculated GST Amount is "+calculatedGstAmount+"");
		}else{
			logger.info("GST NOT PRESENT FOR THIS ORDER");
		}
		if(cscockpitAutoshipTemplateTabPage.isPstTaxPresentInUI() == true){
			pstFromUI = cscockpitAutoshipTemplateTabPage.getPstAmountFromUI();
			calculatedPstAmount = cscockpitAutoshipTemplateTabPage.calculateAmountAccordingToPercent(subTotal, deliveryCost, handlingCost, TestConstants.SEVEN_PERCENT_TAX);
			s_assert.assertTrue(cscockpitAutoshipTemplateTabPage.validatePstAmountOnUI(pstFromUI,calculatedPstAmount),"PST Amount on UI is "+pstFromUI+" and calculated GST Amount is "+calculatedPstAmount);
		}else{
			logger.info("PST NOT PRESENT FOR THIS ORDER");
		}
		s_assert.assertAll();
	}

	//Hybris Project-1582:To verify the Canada tax for Alberta for CRP Autoship
	@Test
	public void testVerifyCanadaTaxForAlbertaForCRPAutoship_1582(){
		RFO_DB = driver.getDBNameRFO();
		int randomNumber=CommonUtils.getRandomNum(10000, 1000000);
		String randomCustomerSequenceNumber = null;
		String randomProductSequenceNumber = null;
		String SKUValue = null;
		String attendentFirstName=TestConstants.FIRST_NAME+randomNumber;
		String attendeeLastName = TestConstants.LAST_NAME+randomNumber;
		String  addressLine=TestConstants.ADDRESS_LINE_1_CA;
		String city=TestConstants.CITY_CA;
		String postalCode=TestConstants.POSTAL_CODE_CA;
		String Country=TestConstants.COUNTRY_DD_VALUE_CA;
		String Province=TestConstants.PROVINCE_ALBERTA;
		String phoneNumber=TestConstants.PHONE_NUMBER_CA;
		double gstFromUI = 0.00;
		double calculatedGstAmount = 0.00;
		double pstFromUI = 0.00;
		double calculatedPstAmount = 0.00;
		//Declare variables.
		List<Map<String, Object>> randomConsultantList =  null;
		List<Map<String, Object>> randomConsultantUsernameList =  null;
		String consultantEmailID=null;
		String accountID=null;
		//Get Email from database.
		randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_CONSULTANT_WITH_ORDERS_AND_AUTOSHIPS_RFO,"40"),RFO_DB);
		accountID = String.valueOf(getValueFromQueryResult(randomConsultantList, "AccountID"));
		randomConsultantUsernameList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_EMAIL_ID_FROM_ACCOUNT_ID,accountID),RFO_DB);
		consultantEmailID = String.valueOf(getValueFromQueryResult(randomConsultantUsernameList, "EmailAddress"));  
		//Login to cscockpit.	
		cscockpitCustomerSearchTabPage = cscockpitLoginPage.clickLoginBtn();
		cscockpitCustomerSearchTabPage.selectCustomerTypeFromDropDownInCustomerSearchTab("CONSULTANT");
		cscockpitCustomerSearchTabPage.selectCountryFromDropDownInCustomerSearchTab("Canada");
		cscockpitCustomerSearchTabPage.selectAccountStatusFromDropDownInCustomerSearchTab("Active");
		cscockpitCustomerSearchTabPage.enterEmailIdInSearchFieldInCustomerSearchTab(consultantEmailID);
		cscockpitCustomerSearchTabPage.clickSearchBtn();
		randomCustomerSequenceNumber = String.valueOf(cscockpitCustomerSearchTabPage.getRandomCustomerFromSearchResult());
		cscockpitCustomerSearchTabPage.clickCIDNumberInCustomerSearchTab(randomCustomerSequenceNumber);
		cscockpitCustomerTabPage.getAndClickAutoshipIDHavingTypeAsCRPAutoshipAndStatusIsPending();
		cscockpitAutoshipTemplateTabPage.clickEditTemplateLinkInAutoshipTemplateTab();
		s_assert.assertTrue(cscockpitAutoshipTemplateTabPage.verifyCancelEditLinkInAutoshipTemplateTab(),"Cancel Edit link is not on Autoship template Tab Page");
		cscockpitAutoshipTemplateTabPage.clickRemoveLinkToRemoveProductFromAutoshipCart();	
		if(cscockpitAutoshipTemplateTabPage.verifyThresholdPopupInAutoshipTemplateTab()){
			cscockpitAutoshipTemplateTabPage.clickOKOfThresholdPopupInAutoshipTemplateTab();
		}
		cscockpitAutoshipTemplateTabPage.clickAddMoreLinesLinkInAutoShipTemplateTab();
		cscockpitAutoshipCartTabPage.selectValueFromSortByDDInCartTab("Price: High to Low");
		cscockpitAutoshipCartTabPage.selectCatalogFromDropDownInCartTab(); 
		randomProductSequenceNumber = String.valueOf(cscockpitAutoshipCartTabPage.getRandomProductWithSKUFromSearchResult()); 
		SKUValue = cscockpitAutoshipCartTabPage.getCustomerSKUValueInCartTab(randomProductSequenceNumber);
		cscockpitAutoshipCartTabPage.searchSKUValueInCartTab(SKUValue);
		cscockpitAutoshipCartTabPage.clickAddToCartBtnInCartTab();
		cscockpitAutoshipCartTabPage.clickCheckoutBtnInCartTab();
		cscockpitAutoshipTemplateUpdateTabPage.clickAddNewAddressUnderDeliveryAddressInCheckoutTab();
		s_assert.assertTrue(cscockpitAutoshipTemplateTabPage.verifyDeliveryAndShippingAddressPopupPresent("Create Delivery Address"),"Create Delivery Address Popup is not Present");
		cscockpitAutoshipTemplateUpdateTabPage.enterShippingInfoInAddNewPaymentProfilePopupWithoutSaveBtn(attendentFirstName, attendeeLastName, addressLine, city, postalCode, Country, Province, phoneNumber);
		cscockpitAutoshipTemplateUpdateTabPage.clickCreateNewAddressButtonInPopupAutoshipTemplateTabPage();
		cscockpitAutoshipTemplateUpdateTabPage.clickUseEnteredAddressOfCreateNewAddressShippingAddressPopup();
		cscockpitAutoshipTemplateUpdateTabPage.clickAddNewPaymentAddressInCheckoutTab();
		cscockpitAutoshipTemplateUpdateTabPage.enterBillingInfo();
		cscockpitAutoshipTemplateUpdateTabPage.clickSaveAddNewPaymentProfilePopUP();
		cscockpitAutoshipTemplateUpdateTabPage.enterCVVValueInCheckoutTab(TestConstants.SECURITY_CODE);
		cscockpitAutoshipTemplateUpdateTabPage.clickUseThisCardBtnInCheckoutTab();
		cscockpitAutoshipTemplateUpdateTabPage.clickUpdateAutoshipTemplateInAutoshipTemplateUpdateTab();
		double subTotal = cscockpitAutoshipTemplateTabPage.getSubTotalFromUI();
		double deliveryCost = cscockpitAutoshipTemplateTabPage.getDeliveryCostFromUI();
		double handlingCost = cscockpitAutoshipTemplateTabPage.getHandlingCostFromUI();
		if(cscockpitAutoshipTemplateTabPage.isGSTTaxPresentInUI()==true){
			gstFromUI = cscockpitAutoshipTemplateTabPage.getGstAmountFromUI();
			calculatedGstAmount = cscockpitAutoshipTemplateTabPage.calculateAmountAccordingToPercent(subTotal, deliveryCost, handlingCost, TestConstants.FIVE_PERCENT_TAX);
			s_assert.assertTrue(cscockpitAutoshipTemplateTabPage.validateGstAmountOnUI(gstFromUI,calculatedGstAmount),"GST Amount on UI is "+gstFromUI+" and calculated GST Amount is "+calculatedGstAmount+"");
		}else{
			logger.info("GST NOT PRESENT FOR THIS ORDER");
		}
		if(cscockpitAutoshipTemplateTabPage.isPstTaxPresentInUI() == true){
			pstFromUI = cscockpitAutoshipTemplateTabPage.getPstAmountFromUI();
			calculatedPstAmount = cscockpitAutoshipTemplateTabPage.calculateAmountAccordingToPercent(subTotal, deliveryCost, handlingCost, TestConstants.SEVEN_PERCENT_TAX);
			s_assert.assertTrue(cscockpitAutoshipTemplateTabPage.validatePstAmountOnUI(pstFromUI,calculatedPstAmount),"PST Amount on UI is "+pstFromUI+" and calculated GST Amount is "+calculatedPstAmount);
		}else{
			logger.info("PST NOT PRESENT FOR THIS ORDER");
		}
		s_assert.assertAll();
	}

	//Hybris Project-1583:To verify the Canada tax for saskatchewan for CRP Autoship
	@Test
	public void testVerifyCanadaTaxForSaskatchewanForCRPAutoship_1583(){
		RFO_DB = driver.getDBNameRFO();
		int randomNumber=CommonUtils.getRandomNum(10000, 1000000);
		String randomCustomerSequenceNumber = null;
		String randomProductSequenceNumber = null;
		String SKUValue = null;
		String attendentFirstName=TestConstants.FIRST_NAME+randomNumber;
		String attendeeLastName = TestConstants.LAST_NAME+randomNumber;
		String  addressLine=TestConstants.ADDRESS_LINE_1_CA;
		String city=TestConstants.CITY_CA;
		String postalCode=TestConstants.POSTAL_CODE_CA;
		String Country=TestConstants.COUNTRY_DD_VALUE_CA;
		String Province=TestConstants.PROVINCE_SASKATCHEWAN;
		String phoneNumber=TestConstants.PHONE_NUMBER_CA;
		double gstFromUI = 0.00;
		double calculatedGstAmount = 0.00;
		double pstFromUI = 0.00;
		double calculatedPstAmount = 0.00;
		//Declare variables.
		List<Map<String, Object>> randomConsultantList =  null;
		List<Map<String, Object>> randomConsultantUsernameList =  null;
		String consultantEmailID=null;
		String accountID=null;
		//Get Email from database.
		randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_CONSULTANT_WITH_ORDERS_AND_AUTOSHIPS_RFO,"40"),RFO_DB);
		accountID = String.valueOf(getValueFromQueryResult(randomConsultantList, "AccountID"));
		randomConsultantUsernameList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_EMAIL_ID_FROM_ACCOUNT_ID,accountID),RFO_DB);
		consultantEmailID = String.valueOf(getValueFromQueryResult(randomConsultantUsernameList, "EmailAddress"));  
		//Login to cscockpit.	
		cscockpitCustomerSearchTabPage = cscockpitLoginPage.clickLoginBtn();
		cscockpitCustomerSearchTabPage.selectCustomerTypeFromDropDownInCustomerSearchTab("CONSULTANT");
		cscockpitCustomerSearchTabPage.selectCountryFromDropDownInCustomerSearchTab("Canada");
		cscockpitCustomerSearchTabPage.selectAccountStatusFromDropDownInCustomerSearchTab("Active");
		cscockpitCustomerSearchTabPage.enterEmailIdInSearchFieldInCustomerSearchTab(consultantEmailID);
		cscockpitCustomerSearchTabPage.clickSearchBtn();
		randomCustomerSequenceNumber = String.valueOf(cscockpitCustomerSearchTabPage.getRandomCustomerFromSearchResult());
		cscockpitCustomerSearchTabPage.clickCIDNumberInCustomerSearchTab(randomCustomerSequenceNumber);
		cscockpitCustomerTabPage.getAndClickAutoshipIDHavingTypeAsCRPAutoshipAndStatusIsPending();
		cscockpitAutoshipTemplateTabPage.clickEditTemplateLinkInAutoshipTemplateTab();
		s_assert.assertTrue(cscockpitAutoshipTemplateTabPage.verifyCancelEditLinkInAutoshipTemplateTab(),"Cancel Edit link is not on Autoship template Tab Page");
		cscockpitAutoshipTemplateTabPage.clickRemoveLinkToRemoveProductFromAutoshipCart();	
		if(cscockpitAutoshipTemplateTabPage.verifyThresholdPopupInAutoshipTemplateTab()){
			cscockpitAutoshipTemplateTabPage.clickOKOfThresholdPopupInAutoshipTemplateTab();
		}
		cscockpitAutoshipTemplateTabPage.clickAddMoreLinesLinkInAutoShipTemplateTab();
		cscockpitAutoshipCartTabPage.selectValueFromSortByDDInCartTab("Price: High to Low");
		cscockpitAutoshipCartTabPage.selectCatalogFromDropDownInCartTab(); 
		randomProductSequenceNumber = String.valueOf(cscockpitAutoshipCartTabPage.getRandomProductWithSKUFromSearchResult()); 
		SKUValue = cscockpitAutoshipCartTabPage.getCustomerSKUValueInCartTab(randomProductSequenceNumber);
		cscockpitAutoshipCartTabPage.searchSKUValueInCartTab(SKUValue);
		cscockpitAutoshipCartTabPage.clickAddToCartBtnInCartTab();
		cscockpitAutoshipCartTabPage.clickCheckoutBtnInCartTab();
		cscockpitAutoshipTemplateUpdateTabPage.clickAddNewAddressUnderDeliveryAddressInCheckoutTab();
		s_assert.assertTrue(cscockpitAutoshipTemplateTabPage.verifyDeliveryAndShippingAddressPopupPresent("Create Delivery Address"),"Create Delivery Address Popup is not Present");
		cscockpitAutoshipTemplateUpdateTabPage.enterShippingInfoInAddNewPaymentProfilePopupWithoutSaveBtn(attendentFirstName, attendeeLastName, addressLine, city, postalCode, Country, Province, phoneNumber);
		cscockpitAutoshipTemplateUpdateTabPage.clickCreateNewAddressButtonInPopupAutoshipTemplateTabPage();
		cscockpitAutoshipTemplateUpdateTabPage.clickUseEnteredAddressOfCreateNewAddressShippingAddressPopup();
		cscockpitAutoshipTemplateUpdateTabPage.clickAddNewPaymentAddressInCheckoutTab();
		cscockpitAutoshipTemplateUpdateTabPage.enterBillingInfo();
		cscockpitAutoshipTemplateUpdateTabPage.clickSaveAddNewPaymentProfilePopUP();
		cscockpitAutoshipTemplateUpdateTabPage.enterCVVValueInCheckoutTab(TestConstants.SECURITY_CODE);
		cscockpitAutoshipTemplateUpdateTabPage.clickUseThisCardBtnInCheckoutTab();
		cscockpitAutoshipTemplateUpdateTabPage.clickUpdateAutoshipTemplateInAutoshipTemplateUpdateTab();
		double subTotal = cscockpitAutoshipTemplateTabPage.getSubTotalFromUI();
		double deliveryCost = cscockpitAutoshipTemplateTabPage.getDeliveryCostFromUI();
		double handlingCost = cscockpitAutoshipTemplateTabPage.getHandlingCostFromUI();
		if(cscockpitAutoshipTemplateTabPage.isGSTTaxPresentInUI()==true){
			gstFromUI = cscockpitAutoshipTemplateTabPage.getGstAmountFromUI();
			calculatedGstAmount = cscockpitAutoshipTemplateTabPage.calculateAmountAccordingToPercent(subTotal, deliveryCost, handlingCost, TestConstants.FIVE_PERCENT_TAX);
			s_assert.assertTrue(cscockpitAutoshipTemplateTabPage.validateGstAmountOnUI(gstFromUI,calculatedGstAmount),"GST Amount on UI is "+gstFromUI+" and calculated GST Amount is "+calculatedGstAmount+"");
		}else{
			logger.info("GST NOT PRESENT FOR THIS ORDER");
		}
		if(cscockpitAutoshipTemplateTabPage.isPstTaxPresentInUI() == true){
			pstFromUI = cscockpitAutoshipTemplateTabPage.getPstAmountFromUI();
			calculatedPstAmount = cscockpitAutoshipTemplateTabPage.calculateAmountAccordingToPercent(subTotal, deliveryCost, handlingCost, TestConstants.FIVE_PERCENT_TAX);
			s_assert.assertTrue(cscockpitAutoshipTemplateTabPage.validatePstAmountOnUI(pstFromUI,calculatedPstAmount),"PST Amount on UI is "+pstFromUI+" and calculated GST Amount is "+calculatedPstAmount);
		}else{
			logger.info("PST NOT PRESENT FOR THIS ORDER");
		}
		s_assert.assertAll();
	}

	//Hybris Project-1571:To verify the Canada tax for Bristish Columbia for Create CRP Autoship
	@Test
	public void testVerifyCanadaTaxForBritishColumbiaForCreateCRPAutoship_1571() throws InterruptedException{
		String randomProductSequenceNumber = null;
		RFO_DB = driver.getDBNameRFO();
		String SKUValue = null;
		int randomNumber=CommonUtils.getRandomNum(10000, 1000000);
		String randomCustomerSequenceNumber = null;
		String attendentFirstName=TestConstants.FIRST_NAME+randomNumber;
		String attendeeLastName = TestConstants.LAST_NAME+randomNumber;
		String  addressLine=TestConstants.ADDRESS_LINE_1_CA;
		String city=TestConstants.CITY_CA;
		String postalCode=TestConstants.POSTAL_CODE_CA;
		String Country=TestConstants.COUNTRY_DD_VALUE_CA;
		String province=TestConstants.PROVINCE_BRITISH_COULMBIA;
		String phoneNumber=TestConstants.PHONE_NUMBER_CA;
		double gstFromUI = 0.00;
		double calculatedGstAmount = 0.00;
		double pstFromUI = 0.00;
		double calculatedPstAmount = 0.00;
		cscockpitCustomerSearchTabPage = cscockpitLoginPage.clickLoginBtn();
		cscockpitCustomerSearchTabPage.selectCustomerTypeFromDropDownInCustomerSearchTab("CONSULTANT");
		cscockpitCustomerSearchTabPage.selectCountryFromDropDownInCustomerSearchTab("Canada");
		cscockpitCustomerSearchTabPage.selectAccountStatusFromDropDownInCustomerSearchTab("Active");
		cscockpitCustomerSearchTabPage.clickSearchBtn();
		randomCustomerSequenceNumber = String.valueOf(cscockpitCustomerSearchTabPage.getRandomCustomerFromSearchResult());
		cscockpitCustomerSearchTabPage.clickAndReturnCIDNumberInCustomerSearchTab(randomCustomerSequenceNumber);
		if(cscockpitCustomerTabPage.isCreateAutoshipTemplateBtnPresent() == false){
			if(cscockpitCustomerTabPage.isCRPAutoshipIDHavingStatusIsPendingPresent()==true){
				cscockpitCustomerTabPage.getAndClickAutoshipIDHavingTypeAsCRPAutoshipAndStatusIsPending();
				cscockpitAutoshipTemplateTabPage.clickCancelAutoship();
				cscockpitAutoshipTemplateTabPage.clickConfirmCancelAutoshipTemplatePopup();
				cscockpitAutoshipTemplateTabPage.clickCustomerTab();
			}
		}
		if(cscockpitCustomerTabPage.isCreateAutoshipTemplateBtnPresent() == false){
			if(cscockpitCustomerTabPage.isPulseTemplateAutoshipIDHavingStatusIsPendingPresent()==true){
				cscockpitCustomerTabPage.getAndClickPulseTemplateAutoshipIDHavingStatusIsPending();
				cscockpitAutoshipTemplateTabPage.clickCancelAutoship();
				cscockpitAutoshipTemplateTabPage.clickConfirmCancelAutoshipTemplatePopup();
				cscockpitAutoshipTemplateTabPage.clickCustomerTab();
			}
		}
		cscockpitCustomerTabPage.clickCreateAutoshipTemplateBtn();
		cscockpitAutoshipCartTabPage.selectValueFromSortByDDInCartTab("Price: High to Low");
		cscockpitAutoshipCartTabPage.selectCatalogFromDropDownInCartTab();	
		randomProductSequenceNumber = String.valueOf(cscockpitAutoshipCartTabPage.getRandomProductWithSKUFromSearchResult()); 
		SKUValue = cscockpitAutoshipCartTabPage.getCustomerSKUValueInCartTab(randomProductSequenceNumber);
		cscockpitAutoshipCartTabPage.searchSKUValueInCartTab(SKUValue);
		cscockpitAutoshipCartTabPage.clickAddToCartBtnInCartTab();
		cscockpitAutoshipCartTabPage.clickCheckoutBtnInCartTab();
		cscockpitCheckoutTabPage.clickAddNewAddressUnderDeliveryAddressInCheckoutTab();
		cscockpitCustomerTabPage.enterShippingInfoInAddNewPaymentProfilePopupWithoutSaveBtn(attendentFirstName, attendeeLastName, addressLine, city, postalCode, Country, province, phoneNumber);
		cscockpitCheckoutTabPage.clickOnCreateNewAddressButtonInAutoshipTemplateTabPage();
		cscockpitCheckoutTabPage.clickUseEnteredAddressOfCreateNewAddressShippingAddressPopup();
		cscockpitCheckoutTabPage.clickAddNewPaymentAddressInCheckoutTab();
		cscockpitCheckoutTabPage.enterBillingInfo();
		cscockpitCheckoutTabPage.clickSaveAddNewPaymentProfilePopUP();
		cscockpitCheckoutTabPage.enterCVVValueInCheckoutTab(TestConstants.SECURITY_CODE);
		cscockpitCheckoutTabPage.clickUseThisCardBtnInCheckoutTab();
		cscockpitAutoshipTemplateUpdateTabPage.clickCreateAutoshipTemplateBtn();
		double subTotal = cscockpitAutoshipTemplateTabPage.getSubTotalFromUI();
		double deliveryCost = cscockpitAutoshipTemplateTabPage.getDeliveryCostFromUI();
		double handlingCost = cscockpitAutoshipTemplateTabPage.getHandlingCostFromUI();
		if(cscockpitAutoshipTemplateTabPage.isGSTTaxPresentInUI()==true){
			gstFromUI = cscockpitAutoshipTemplateTabPage.getGstAmountFromUI();
			calculatedGstAmount = cscockpitAutoshipTemplateTabPage.calculateAmountAccordingToPercent(subTotal, deliveryCost, handlingCost, TestConstants.FIVE_PERCENT_TAX);
			s_assert.assertTrue(cscockpitAutoshipTemplateTabPage.validateGstAmountOnUI(gstFromUI,calculatedGstAmount),"GST Amount on UI is "+gstFromUI+" and calculated GST Amount is "+calculatedGstAmount+"");
		}else{
			logger.info("GST NOT PRESENT FOR THIS ORDER");
		}
		if(cscockpitAutoshipTemplateTabPage.isPstTaxPresentInUI() == true){
			pstFromUI = cscockpitAutoshipTemplateTabPage.getPstAmountFromUI();
			calculatedPstAmount = cscockpitAutoshipTemplateTabPage.calculateAmountAccordingToPercent(subTotal, deliveryCost, handlingCost, TestConstants.SEVEN_PERCENT_TAX);
			s_assert.assertTrue(cscockpitAutoshipTemplateTabPage.validatePstAmountOnUI(pstFromUI,calculatedPstAmount),"PST Amount on UI is "+pstFromUI+" and calculated GST Amount is "+calculatedPstAmount);
		}else{
			logger.info("PST NOT PRESENT FOR THIS ORDER");
		}
		s_assert.assertAll();
	}

	//Hybris Project-1572:To verify the Canada tax for Alberta for Create CRP Autoship
	@Test(enabled=false)//test needs updation
	public void testVerifyCanadaTaxForAlbertaForCreateCRPAutoship_1572() throws InterruptedException{
		String randomProductSequenceNumber = null;
		RFO_DB = driver.getDBNameRFO();
		String SKUValue = null;
		int randomNumber=CommonUtils.getRandomNum(10000, 1000000);
		String randomCustomerSequenceNumber = null;
		String attendentFirstName=TestConstants.FIRST_NAME+randomNumber;
		String attendeeLastName = TestConstants.LAST_NAME+randomNumber;
		String  addressLine=TestConstants.ADDRESS_LINE_1_CA;
		String city=TestConstants.CITY_CA;
		String postalCode=TestConstants.POSTAL_CODE_CA;
		String Country=TestConstants.COUNTRY_DD_VALUE_CA;
		String province=TestConstants.PROVINCE_ALBERTA;
		String phoneNumber=TestConstants.PHONE_NUMBER_CA;
		double gstFromUI = 0.00;
		double calculatedGstAmount = 0.00;
		double pstFromUI = 0.00;
		double calculatedPstAmount = 0.00;
		cscockpitCustomerSearchTabPage = cscockpitLoginPage.clickLoginBtn();
		cscockpitCustomerSearchTabPage.selectCustomerTypeFromDropDownInCustomerSearchTab("CONSULTANT");
		cscockpitCustomerSearchTabPage.selectCountryFromDropDownInCustomerSearchTab("Canada");
		cscockpitCustomerSearchTabPage.selectAccountStatusFromDropDownInCustomerSearchTab("Active");
		cscockpitCustomerSearchTabPage.clickSearchBtn();
		randomCustomerSequenceNumber = String.valueOf(cscockpitCustomerSearchTabPage.getRandomCustomerFromSearchResult());
		cscockpitCustomerSearchTabPage.clickAndReturnCIDNumberInCustomerSearchTab(randomCustomerSequenceNumber);
		if(cscockpitCustomerTabPage.isCreateAutoshipTemplateBtnPresent() == false){
			if(cscockpitCustomerTabPage.isCRPAutoshipIDHavingStatusIsPendingPresent()==true){
				cscockpitCustomerTabPage.getAndClickAutoshipIDHavingTypeAsCRPAutoshipAndStatusIsPending();
				cscockpitAutoshipTemplateTabPage.clickCancelAutoship();
				cscockpitAutoshipTemplateTabPage.clickConfirmCancelAutoshipTemplatePopup();
				cscockpitAutoshipTemplateTabPage.clickCustomerTab();
			}
		}
		if(cscockpitCustomerTabPage.isCreateAutoshipTemplateBtnPresent() == false){
			if(cscockpitCustomerTabPage.isPulseTemplateAutoshipIDHavingStatusIsPendingPresent()==true){
				cscockpitCustomerTabPage.getAndClickPulseTemplateAutoshipIDHavingStatusIsPending();
				cscockpitAutoshipTemplateTabPage.clickCancelAutoship();
				cscockpitAutoshipTemplateTabPage.clickConfirmCancelAutoshipTemplatePopup();
				cscockpitAutoshipTemplateTabPage.clickCustomerTab();
			}
		}
		cscockpitCustomerTabPage.clickCreateAutoshipTemplateBtn();
		cscockpitAutoshipCartTabPage.selectValueFromSortByDDInCartTab("Price: High to Low");
		cscockpitAutoshipCartTabPage.selectCatalogFromDropDownInCartTab();	
		randomProductSequenceNumber = String.valueOf(cscockpitAutoshipCartTabPage.getRandomProductWithSKUFromSearchResult()); 
		SKUValue = cscockpitAutoshipCartTabPage.getCustomerSKUValueInCartTab(randomProductSequenceNumber);
		cscockpitAutoshipCartTabPage.searchSKUValueInCartTab(SKUValue);
		cscockpitAutoshipCartTabPage.clickAddToCartBtnInCartTab();
		cscockpitAutoshipCartTabPage.clickCheckoutBtnInCartTab();
		cscockpitCheckoutTabPage.clickAddNewAddressUnderDeliveryAddressInCheckoutTab();
		cscockpitCustomerTabPage.enterShippingInfoInAddNewPaymentProfilePopupWithoutSaveBtn(attendentFirstName, attendeeLastName, addressLine, city, postalCode, Country, province, phoneNumber);
		cscockpitCheckoutTabPage.clickOnCreateNewAddressButtonInAutoshipTemplateTabPage();
		cscockpitCheckoutTabPage.clickUseEnteredAddressOfCreateNewAddressShippingAddressPopup();
		cscockpitCheckoutTabPage.clickAddNewPaymentAddressInCheckoutTab();
		cscockpitCheckoutTabPage.enterBillingInfo();
		cscockpitCheckoutTabPage.clickSaveAddNewPaymentProfilePopUP();
		cscockpitCheckoutTabPage.enterCVVValueInCheckoutTab(TestConstants.SECURITY_CODE);
		cscockpitCheckoutTabPage.clickUseThisCardBtnInCheckoutTab();
		cscockpitAutoshipTemplateUpdateTabPage.clickCreateAutoshipTemplateBtn();
		double subTotal = cscockpitAutoshipTemplateTabPage.getSubTotalFromUI();
		double deliveryCost = cscockpitAutoshipTemplateTabPage.getDeliveryCostFromUI();
		double handlingCost = cscockpitAutoshipTemplateTabPage.getHandlingCostFromUI();
		if(cscockpitAutoshipTemplateTabPage.isGSTTaxPresentInUI()==true){
			gstFromUI = cscockpitAutoshipTemplateTabPage.getGstAmountFromUI();
			calculatedGstAmount = cscockpitAutoshipTemplateTabPage.calculateAmountAccordingToPercent(subTotal, deliveryCost, handlingCost, TestConstants.FIVE_PERCENT_TAX);
			s_assert.assertTrue(cscockpitAutoshipTemplateTabPage.validateGstAmountOnUI(gstFromUI,calculatedGstAmount),"GST Amount on UI is "+gstFromUI+" and calculated GST Amount is "+calculatedGstAmount+"");
		}else{
			logger.info("GST NOT PRESENT FOR THIS ORDER");
		}
		if(cscockpitAutoshipTemplateTabPage.isPstTaxPresentInUI() == true){
			pstFromUI = cscockpitAutoshipTemplateTabPage.getPstAmountFromUI();
			calculatedPstAmount = cscockpitAutoshipTemplateTabPage.calculateAmountAccordingToPercent(subTotal, deliveryCost, handlingCost, TestConstants.SEVEN_PERCENT_TAX);
			s_assert.assertTrue(cscockpitAutoshipTemplateTabPage.validatePstAmountOnUI(pstFromUI,calculatedPstAmount),"PST Amount on UI is "+pstFromUI+" and calculated GST Amount is "+calculatedPstAmount);
		}else{
			logger.info("PST NOT PRESENT FOR THIS ORDER");
		}
		s_assert.assertAll();
	}

	//Hybris Project-1573:To verify the Canada tax for saskatchewan for Create CRP Autoship
	@Test
	public void testVerifyCanadaTaxForSaskatchewanForCreateCRPAutoship_1573() throws InterruptedException{
		String randomProductSequenceNumber = null;
		RFO_DB = driver.getDBNameRFO();
		String SKUValue = null;
		int randomNumber=CommonUtils.getRandomNum(10000, 1000000);
		String randomCustomerSequenceNumber = null;
		String attendentFirstName=TestConstants.FIRST_NAME+randomNumber;
		String attendeeLastName = TestConstants.LAST_NAME+randomNumber;
		String  addressLine=TestConstants.ADDRESS_LINE_1_CA;
		String city=TestConstants.CITY_CA;
		String postalCode=TestConstants.POSTAL_CODE_CA;
		String Country=TestConstants.COUNTRY_DD_VALUE_CA;
		String province=TestConstants.PROVINCE_SASKATCHEWAN;
		String phoneNumber=TestConstants.PHONE_NUMBER_CA;
		double gstFromUI = 0.00;
		double calculatedGstAmount = 0.00;
		double pstFromUI = 0.00;
		double calculatedPstAmount = 0.00;
		cscockpitCustomerSearchTabPage = cscockpitLoginPage.clickLoginBtn();
		cscockpitCustomerSearchTabPage.selectCustomerTypeFromDropDownInCustomerSearchTab("CONSULTANT");
		cscockpitCustomerSearchTabPage.selectCountryFromDropDownInCustomerSearchTab("Canada");
		cscockpitCustomerSearchTabPage.selectAccountStatusFromDropDownInCustomerSearchTab("Active");
		cscockpitCustomerSearchTabPage.clickSearchBtn();
		randomCustomerSequenceNumber = String.valueOf(cscockpitCustomerSearchTabPage.getRandomCustomerFromSearchResult());
		cscockpitCustomerSearchTabPage.clickAndReturnCIDNumberInCustomerSearchTab(randomCustomerSequenceNumber);
		if(cscockpitCustomerTabPage.isCreateAutoshipTemplateBtnPresent() == false){
			if(cscockpitCustomerTabPage.isCRPAutoshipIDHavingStatusIsPendingPresent()==true){
				cscockpitCustomerTabPage.getAndClickAutoshipIDHavingTypeAsCRPAutoshipAndStatusIsPending();
				cscockpitAutoshipTemplateTabPage.clickCancelAutoship();
				cscockpitAutoshipTemplateTabPage.clickConfirmCancelAutoshipTemplatePopup();
				cscockpitAutoshipTemplateTabPage.clickCustomerTab();
			}
		}
		if(cscockpitCustomerTabPage.isCreateAutoshipTemplateBtnPresent() == false){
			if(cscockpitCustomerTabPage.isPulseTemplateAutoshipIDHavingStatusIsPendingPresent()==true){
				cscockpitCustomerTabPage.getAndClickPulseTemplateAutoshipIDHavingStatusIsPending();
				cscockpitAutoshipTemplateTabPage.clickCancelAutoship();
				cscockpitAutoshipTemplateTabPage.clickConfirmCancelAutoshipTemplatePopup();
				cscockpitAutoshipTemplateTabPage.clickCustomerTab();
			}
		}
		cscockpitCustomerTabPage.clickCreateAutoshipTemplateBtn();
		cscockpitAutoshipCartTabPage.selectValueFromSortByDDInCartTab("Price: High to Low");
		cscockpitAutoshipCartTabPage.selectCatalogFromDropDownInCartTab();	
		randomProductSequenceNumber = String.valueOf(cscockpitAutoshipCartTabPage.getRandomProductWithSKUFromSearchResult()); 
		SKUValue = cscockpitAutoshipCartTabPage.getCustomerSKUValueInCartTab(randomProductSequenceNumber);
		cscockpitAutoshipCartTabPage.searchSKUValueInCartTab(SKUValue);
		cscockpitAutoshipCartTabPage.clickAddToCartBtnInCartTab();
		cscockpitAutoshipCartTabPage.clickCheckoutBtnInCartTab();
		cscockpitCheckoutTabPage.clickAddNewAddressUnderDeliveryAddressInCheckoutTab();
		cscockpitCustomerTabPage.enterShippingInfoInAddNewPaymentProfilePopupWithoutSaveBtn(attendentFirstName, attendeeLastName, addressLine, city, postalCode, Country, province, phoneNumber);
		cscockpitCheckoutTabPage.clickOnCreateNewAddressButtonInAutoshipTemplateTabPage();
		cscockpitCheckoutTabPage.clickUseEnteredAddressOfCreateNewAddressShippingAddressPopup();
		cscockpitCheckoutTabPage.clickAddNewPaymentAddressInCheckoutTab();
		cscockpitCheckoutTabPage.enterBillingInfo();
		cscockpitCheckoutTabPage.clickSaveAddNewPaymentProfilePopUP();
		cscockpitCheckoutTabPage.enterCVVValueInCheckoutTab(TestConstants.SECURITY_CODE);
		cscockpitCheckoutTabPage.clickUseThisCardBtnInCheckoutTab();
		cscockpitAutoshipTemplateUpdateTabPage.clickCreateAutoshipTemplateBtn();
		double subTotal = cscockpitAutoshipTemplateTabPage.getSubTotalFromUI();
		double deliveryCost = cscockpitAutoshipTemplateTabPage.getDeliveryCostFromUI();
		double handlingCost = cscockpitAutoshipTemplateTabPage.getHandlingCostFromUI();
		if(cscockpitAutoshipTemplateTabPage.isGSTTaxPresentInUI()==true){
			gstFromUI = cscockpitAutoshipTemplateTabPage.getGstAmountFromUI();
			calculatedGstAmount = cscockpitAutoshipTemplateTabPage.calculateAmountAccordingToPercent(subTotal, deliveryCost, handlingCost, TestConstants.FIVE_PERCENT_TAX);
			s_assert.assertTrue(cscockpitAutoshipTemplateTabPage.validateGstAmountOnUI(gstFromUI,calculatedGstAmount),"GST Amount on UI is "+gstFromUI+" and calculated GST Amount is "+calculatedGstAmount+"");
		}else{
			logger.info("GST NOT PRESENT FOR THIS ORDER");
		}
		if(cscockpitAutoshipTemplateTabPage.isPstTaxPresentInUI() == true){
			pstFromUI = cscockpitAutoshipTemplateTabPage.getPstAmountFromUI();
			calculatedPstAmount = cscockpitAutoshipTemplateTabPage.calculateAmountAccordingToPercent(subTotal, deliveryCost, handlingCost, TestConstants.FIVE_PERCENT_TAX);
			s_assert.assertTrue(cscockpitAutoshipTemplateTabPage.validatePstAmountOnUI(pstFromUI,calculatedPstAmount),"PST Amount on UI is "+pstFromUI+" and calculated GST Amount is "+calculatedPstAmount);
		}else{
			logger.info("PST NOT PRESENT FOR THIS ORDER");
		}
		s_assert.assertAll();
	}

	//Hybris Project-1574:To verify the Canada tax for Manitoba for Create CRP Autoship
	@Test
	public void testVerifyCanadaTaxForManitobaForCreateCRPAutoship_1574() throws InterruptedException{
		String randomProductSequenceNumber = null;
		RFO_DB = driver.getDBNameRFO();
		String SKUValue = null;
		int randomNumber=CommonUtils.getRandomNum(10000, 1000000);
		String randomCustomerSequenceNumber = null;
		String attendentFirstName=TestConstants.FIRST_NAME+randomNumber;
		String attendeeLastName = TestConstants.LAST_NAME+randomNumber;
		String  addressLine=TestConstants.ADDRESS_LINE_1_CA;
		String city=TestConstants.CITY_CA;
		String postalCode=TestConstants.POSTAL_CODE_CA;
		String Country=TestConstants.COUNTRY_DD_VALUE_CA;
		String province=TestConstants.PROVINCE_MANITOBA;
		String phoneNumber=TestConstants.PHONE_NUMBER_CA;
		double gstFromUI = 0.00;
		double calculatedGstAmount = 0.00;
		double pstFromUI = 0.00;
		double calculatedPstAmount = 0.00;
		cscockpitCustomerSearchTabPage = cscockpitLoginPage.clickLoginBtn();
		cscockpitCustomerSearchTabPage.selectCustomerTypeFromDropDownInCustomerSearchTab("CONSULTANT");
		cscockpitCustomerSearchTabPage.selectCountryFromDropDownInCustomerSearchTab("Canada");
		cscockpitCustomerSearchTabPage.selectAccountStatusFromDropDownInCustomerSearchTab("Active");
		cscockpitCustomerSearchTabPage.clickSearchBtn();
		randomCustomerSequenceNumber = String.valueOf(cscockpitCustomerSearchTabPage.getRandomCustomerFromSearchResult());
		cscockpitCustomerSearchTabPage.clickAndReturnCIDNumberInCustomerSearchTab(randomCustomerSequenceNumber);
		if(cscockpitCustomerTabPage.isCreateAutoshipTemplateBtnPresent() == false){
			if(cscockpitCustomerTabPage.isCRPAutoshipIDHavingStatusIsPendingPresent()==true){
				cscockpitCustomerTabPage.getAndClickAutoshipIDHavingTypeAsCRPAutoshipAndStatusIsPending();
				cscockpitAutoshipTemplateTabPage.clickCancelAutoship();
				cscockpitAutoshipTemplateTabPage.clickConfirmCancelAutoshipTemplatePopup();
				cscockpitAutoshipTemplateTabPage.clickCustomerTab();
			}
		}
		if(cscockpitCustomerTabPage.isCreateAutoshipTemplateBtnPresent() == false){
			if(cscockpitCustomerTabPage.isPulseTemplateAutoshipIDHavingStatusIsPendingPresent()==true){
				cscockpitCustomerTabPage.getAndClickPulseTemplateAutoshipIDHavingStatusIsPending();
				cscockpitAutoshipTemplateTabPage.clickCancelAutoship();
				cscockpitAutoshipTemplateTabPage.clickConfirmCancelAutoshipTemplatePopup();
				cscockpitAutoshipTemplateTabPage.clickCustomerTab();
			}
		}
		cscockpitCustomerTabPage.clickCreateAutoshipTemplateBtn();
		cscockpitAutoshipCartTabPage.selectValueFromSortByDDInCartTab("Price: High to Low");
		cscockpitAutoshipCartTabPage.selectCatalogFromDropDownInCartTab();	
		randomProductSequenceNumber = String.valueOf(cscockpitAutoshipCartTabPage.getRandomProductWithSKUFromSearchResult()); 
		SKUValue = cscockpitAutoshipCartTabPage.getCustomerSKUValueInCartTab(randomProductSequenceNumber);
		cscockpitAutoshipCartTabPage.searchSKUValueInCartTab(SKUValue);
		cscockpitAutoshipCartTabPage.clickAddToCartBtnInCartTab();
		cscockpitAutoshipCartTabPage.clickCheckoutBtnInCartTab();
		cscockpitCheckoutTabPage.clickAddNewAddressUnderDeliveryAddressInCheckoutTab();
		cscockpitCustomerTabPage.enterShippingInfoInAddNewPaymentProfilePopupWithoutSaveBtn(attendentFirstName, attendeeLastName, addressLine, city, postalCode, Country, province, phoneNumber);
		cscockpitCheckoutTabPage.clickOnCreateNewAddressButtonInAutoshipTemplateTabPage();
		cscockpitCheckoutTabPage.clickUseEnteredAddressOfCreateNewAddressShippingAddressPopup();
		cscockpitCheckoutTabPage.clickAddNewPaymentAddressInCheckoutTab();
		cscockpitCheckoutTabPage.enterBillingInfo();
		cscockpitCheckoutTabPage.clickSaveAddNewPaymentProfilePopUP();
		cscockpitCheckoutTabPage.enterCVVValueInCheckoutTab(TestConstants.SECURITY_CODE);
		cscockpitCheckoutTabPage.clickUseThisCardBtnInCheckoutTab();
		cscockpitAutoshipTemplateUpdateTabPage.clickCreateAutoshipTemplateBtn();
		double subTotal = cscockpitAutoshipTemplateTabPage.getSubTotalFromUI();
		double deliveryCost = cscockpitAutoshipTemplateTabPage.getDeliveryCostFromUI();
		double handlingCost = cscockpitAutoshipTemplateTabPage.getHandlingCostFromUI();
		if(cscockpitAutoshipTemplateTabPage.isGSTTaxPresentInUI()==true){
			gstFromUI = cscockpitAutoshipTemplateTabPage.getGstAmountFromUI();
			calculatedGstAmount = cscockpitAutoshipTemplateTabPage.calculateAmountAccordingToPercent(subTotal, deliveryCost, handlingCost, TestConstants.FIVE_PERCENT_TAX);
			s_assert.assertTrue(cscockpitAutoshipTemplateTabPage.validateGstAmountOnUI(gstFromUI,calculatedGstAmount),"GST Amount on UI is "+gstFromUI+" and calculated GST Amount is "+calculatedGstAmount+"");
		}else{
			logger.info("GST NOT PRESENT FOR THIS ORDER");
		}
		if(cscockpitAutoshipTemplateTabPage.isPstTaxPresentInUI() == true){
			pstFromUI = cscockpitAutoshipTemplateTabPage.getPstAmountFromUI();
			calculatedPstAmount = cscockpitAutoshipTemplateTabPage.calculateAmountAccordingToPercent(subTotal, deliveryCost, handlingCost, TestConstants.SEVEN_PERCENT_TAX);
			s_assert.assertTrue(cscockpitAutoshipTemplateTabPage.validatePstAmountOnUI(pstFromUI,calculatedPstAmount),"PST Amount on UI is "+pstFromUI+" and calculated GST Amount is "+calculatedPstAmount);
		}else{
			logger.info("PST NOT PRESENT FOR THIS ORDER");
		}
		s_assert.assertAll();
	}

	//Hybris Project-1575:To verify the Canada tax for ontario for Create CRP Autoship
	@Test
	public void testVerifyCanadaTaxForOntarioForCreateCRPAutoship_1575() throws InterruptedException{
		String randomProductSequenceNumber = null;
		RFO_DB = driver.getDBNameRFO();
		String SKUValue = null;
		int randomNumber=CommonUtils.getRandomNum(10000, 1000000);
		String randomCustomerSequenceNumber = null;
		String attendentFirstName=TestConstants.FIRST_NAME+randomNumber;
		String attendeeLastName = TestConstants.LAST_NAME+randomNumber;
		String  addressLine=TestConstants.ADDRESS_LINE_1_CA;
		String city=TestConstants.CITY_CA;
		String postalCode=TestConstants.POSTAL_CODE_CA;
		String Country=TestConstants.COUNTRY_DD_VALUE_CA;
		String province=TestConstants.PROVINCE_ONTARIO;
		String phoneNumber=TestConstants.PHONE_NUMBER_CA;
		double gstFromUI = 0.00;
		double calculatedGstAmount = 0.00;
		double pstFromUI = 0.00;
		double calculatedPstAmount = 0.00;
		cscockpitCustomerSearchTabPage = cscockpitLoginPage.clickLoginBtn();
		cscockpitCustomerSearchTabPage.selectCustomerTypeFromDropDownInCustomerSearchTab("CONSULTANT");
		cscockpitCustomerSearchTabPage.selectCountryFromDropDownInCustomerSearchTab("Canada");
		cscockpitCustomerSearchTabPage.selectAccountStatusFromDropDownInCustomerSearchTab("Active");
		cscockpitCustomerSearchTabPage.clickSearchBtn();
		randomCustomerSequenceNumber = String.valueOf(cscockpitCustomerSearchTabPage.getRandomCustomerFromSearchResult());
		cscockpitCustomerSearchTabPage.clickAndReturnCIDNumberInCustomerSearchTab(randomCustomerSequenceNumber);
		if(cscockpitCustomerTabPage.isCreateAutoshipTemplateBtnPresent() == false){
			if(cscockpitCustomerTabPage.isCRPAutoshipIDHavingStatusIsPendingPresent()==true){
				cscockpitCustomerTabPage.getAndClickAutoshipIDHavingTypeAsCRPAutoshipAndStatusIsPending();
				cscockpitAutoshipTemplateTabPage.clickCancelAutoship();
				cscockpitAutoshipTemplateTabPage.clickConfirmCancelAutoshipTemplatePopup();
				cscockpitAutoshipTemplateTabPage.clickCustomerTab();
			}
		}
		if(cscockpitCustomerTabPage.isCreateAutoshipTemplateBtnPresent() == false){
			if(cscockpitCustomerTabPage.isPulseTemplateAutoshipIDHavingStatusIsPendingPresent()==true){
				cscockpitCustomerTabPage.getAndClickPulseTemplateAutoshipIDHavingStatusIsPending();
				cscockpitAutoshipTemplateTabPage.clickCancelAutoship();
				cscockpitAutoshipTemplateTabPage.clickConfirmCancelAutoshipTemplatePopup();
				cscockpitAutoshipTemplateTabPage.clickCustomerTab();
			}
		}
		cscockpitCustomerTabPage.clickCreateAutoshipTemplateBtn();
		cscockpitAutoshipCartTabPage.selectValueFromSortByDDInCartTab("Price: High to Low");
		cscockpitAutoshipCartTabPage.selectCatalogFromDropDownInCartTab();	
		randomProductSequenceNumber = String.valueOf(cscockpitAutoshipCartTabPage.getRandomProductWithSKUFromSearchResult()); 
		SKUValue = cscockpitAutoshipCartTabPage.getCustomerSKUValueInCartTab(randomProductSequenceNumber);
		cscockpitAutoshipCartTabPage.searchSKUValueInCartTab(SKUValue);
		cscockpitAutoshipCartTabPage.clickAddToCartBtnInCartTab();
		cscockpitAutoshipCartTabPage.clickCheckoutBtnInCartTab();
		cscockpitCheckoutTabPage.clickAddNewAddressUnderDeliveryAddressInCheckoutTab();
		cscockpitCustomerTabPage.enterShippingInfoInAddNewPaymentProfilePopupWithoutSaveBtn(attendentFirstName, attendeeLastName, addressLine, city, postalCode, Country, province, phoneNumber);
		cscockpitCheckoutTabPage.clickOnCreateNewAddressButtonInAutoshipTemplateTabPage();
		cscockpitCheckoutTabPage.clickUseEnteredAddressOfCreateNewAddressShippingAddressPopup();
		cscockpitCheckoutTabPage.clickAddNewPaymentAddressInCheckoutTab();
		cscockpitCheckoutTabPage.enterBillingInfo();
		cscockpitCheckoutTabPage.clickSaveAddNewPaymentProfilePopUP();
		cscockpitCheckoutTabPage.enterCVVValueInCheckoutTab(TestConstants.SECURITY_CODE);
		cscockpitCheckoutTabPage.clickUseThisCardBtnInCheckoutTab();
		cscockpitAutoshipTemplateUpdateTabPage.clickCreateAutoshipTemplateBtn();
		double subTotal = cscockpitAutoshipTemplateTabPage.getSubTotalFromUI();
		double deliveryCost = cscockpitAutoshipTemplateTabPage.getDeliveryCostFromUI();
		double handlingCost = cscockpitAutoshipTemplateTabPage.getHandlingCostFromUI();
		if(cscockpitAutoshipTemplateTabPage.isGSTTaxPresentInUI()==true){
			gstFromUI = cscockpitAutoshipTemplateTabPage.getGstAmountFromUI();
			calculatedGstAmount = cscockpitAutoshipTemplateTabPage.calculateAmountAccordingToPercent(subTotal, deliveryCost, handlingCost, TestConstants.FIVE_PERCENT_TAX);
			s_assert.assertTrue(cscockpitAutoshipTemplateTabPage.validateGstAmountOnUI(gstFromUI,calculatedGstAmount),"GST Amount on UI is "+gstFromUI+" and calculated GST Amount is "+calculatedGstAmount+"");
		}else{
			logger.info("GST NOT PRESENT FOR THIS ORDER");
		}
		if(cscockpitAutoshipTemplateTabPage.isPstTaxPresentInUI() == true){
			pstFromUI = cscockpitAutoshipTemplateTabPage.getPstAmountFromUI();
			calculatedPstAmount = cscockpitAutoshipTemplateTabPage.calculateAmountAccordingToPercent(subTotal, deliveryCost, handlingCost, TestConstants.SEVEN_PERCENT_TAX);
			s_assert.assertTrue(cscockpitAutoshipTemplateTabPage.validatePstAmountOnUI(pstFromUI,calculatedPstAmount),"PST Amount on UI is "+pstFromUI+" and calculated GST Amount is "+calculatedPstAmount);
		}else{
			logger.info("PST NOT PRESENT FOR THIS ORDER");
		}
		s_assert.assertAll();
	}

	//Hybris Project-1577:To verify the Canada tax for New Foundland for Create CRP Autoship
	@Test
	public void testVerifyCanadaTaxForNewFoundlandForCreateCRPAutoship_1577() throws InterruptedException{
		String randomProductSequenceNumber = null;
		RFO_DB = driver.getDBNameRFO();
		String SKUValue = null;
		int randomNumber=CommonUtils.getRandomNum(10000, 1000000);
		String randomCustomerSequenceNumber = null;
		String attendentFirstName=TestConstants.FIRST_NAME+randomNumber;
		String attendeeLastName = TestConstants.LAST_NAME+randomNumber;
		String  addressLine=TestConstants.ADDRESS_LINE_1_CA;
		String city=TestConstants.CITY_CA;
		String postalCode=TestConstants.POSTAL_CODE_CA;
		String Country=TestConstants.COUNTRY_DD_VALUE_CA;
		String province=TestConstants.PROVINCE_NEW_FOUNDLAND_AND_LABRADOR;
		String phoneNumber=TestConstants.PHONE_NUMBER_CA;
		double gstFromUI = 0.00;
		double calculatedGstAmount = 0.00;
		double pstFromUI = 0.00;
		double calculatedPstAmount = 0.00;
		cscockpitCustomerSearchTabPage = cscockpitLoginPage.clickLoginBtn();
		cscockpitCustomerSearchTabPage.selectCustomerTypeFromDropDownInCustomerSearchTab("CONSULTANT");
		cscockpitCustomerSearchTabPage.selectCountryFromDropDownInCustomerSearchTab("Canada");
		cscockpitCustomerSearchTabPage.selectAccountStatusFromDropDownInCustomerSearchTab("Active");
		cscockpitCustomerSearchTabPage.clickSearchBtn();
		randomCustomerSequenceNumber = String.valueOf(cscockpitCustomerSearchTabPage.getRandomCustomerFromSearchResult());
		cscockpitCustomerSearchTabPage.clickAndReturnCIDNumberInCustomerSearchTab(randomCustomerSequenceNumber);
		if(cscockpitCustomerTabPage.isCreateAutoshipTemplateBtnPresent() == false){
			if(cscockpitCustomerTabPage.isCRPAutoshipIDHavingStatusIsPendingPresent()==true){
				cscockpitCustomerTabPage.getAndClickAutoshipIDHavingTypeAsCRPAutoshipAndStatusIsPending();
				cscockpitAutoshipTemplateTabPage.clickCancelAutoship();
				cscockpitAutoshipTemplateTabPage.clickConfirmCancelAutoshipTemplatePopup();
				cscockpitAutoshipTemplateTabPage.clickCustomerTab();
			}
		}
		if(cscockpitCustomerTabPage.isCreateAutoshipTemplateBtnPresent() == false){
			if(cscockpitCustomerTabPage.isPulseTemplateAutoshipIDHavingStatusIsPendingPresent()==true){
				cscockpitCustomerTabPage.getAndClickPulseTemplateAutoshipIDHavingStatusIsPending();
				cscockpitAutoshipTemplateTabPage.clickCancelAutoship();
				cscockpitAutoshipTemplateTabPage.clickConfirmCancelAutoshipTemplatePopup();
				cscockpitAutoshipTemplateTabPage.clickCustomerTab();
			}
		}
		cscockpitCustomerTabPage.clickCreateAutoshipTemplateBtn();
		cscockpitAutoshipCartTabPage.selectValueFromSortByDDInCartTab("Price: High to Low");
		cscockpitAutoshipCartTabPage.selectCatalogFromDropDownInCartTab();	
		randomProductSequenceNumber = String.valueOf(cscockpitAutoshipCartTabPage.getRandomProductWithSKUFromSearchResult()); 
		SKUValue = cscockpitAutoshipCartTabPage.getCustomerSKUValueInCartTab(randomProductSequenceNumber);
		cscockpitAutoshipCartTabPage.searchSKUValueInCartTab(SKUValue);
		cscockpitAutoshipCartTabPage.clickAddToCartBtnInCartTab();
		cscockpitAutoshipCartTabPage.clickCheckoutBtnInCartTab();
		cscockpitCheckoutTabPage.clickAddNewAddressUnderDeliveryAddressInCheckoutTab();
		cscockpitCustomerTabPage.enterShippingInfoInAddNewPaymentProfilePopupWithoutSaveBtn(attendentFirstName, attendeeLastName, addressLine, city, postalCode, Country, province, phoneNumber);
		cscockpitCheckoutTabPage.clickOnCreateNewAddressButtonInAutoshipTemplateTabPage();
		cscockpitCheckoutTabPage.clickUseEnteredAddressOfCreateNewAddressShippingAddressPopup();
		cscockpitCheckoutTabPage.clickAddNewPaymentAddressInCheckoutTab();
		cscockpitCheckoutTabPage.enterBillingInfo();
		cscockpitCheckoutTabPage.clickSaveAddNewPaymentProfilePopUP();
		cscockpitCheckoutTabPage.enterCVVValueInCheckoutTab(TestConstants.SECURITY_CODE);
		cscockpitCheckoutTabPage.clickUseThisCardBtnInCheckoutTab();
		cscockpitAutoshipTemplateUpdateTabPage.clickCreateAutoshipTemplateBtn();
		double subTotal = cscockpitAutoshipTemplateTabPage.getSubTotalFromUI();
		double deliveryCost = cscockpitAutoshipTemplateTabPage.getDeliveryCostFromUI();
		double handlingCost = cscockpitAutoshipTemplateTabPage.getHandlingCostFromUI();
		if(cscockpitAutoshipTemplateTabPage.isGSTTaxPresentInUI()==true){
			gstFromUI = cscockpitAutoshipTemplateTabPage.getGstAmountFromUI();
			calculatedGstAmount = cscockpitAutoshipTemplateTabPage.calculateAmountAccordingToPercent(subTotal, deliveryCost, handlingCost, TestConstants.FIVE_PERCENT_TAX);
			s_assert.assertTrue(cscockpitAutoshipTemplateTabPage.validateGstAmountOnUI(gstFromUI,calculatedGstAmount),"GST Amount on UI is "+gstFromUI+" and calculated GST Amount is "+calculatedGstAmount+"");
		}else{
			logger.info("GST NOT PRESENT FOR THIS ORDER");
		}
		if(cscockpitAutoshipTemplateTabPage.isPstTaxPresentInUI() == true){
			pstFromUI = cscockpitAutoshipTemplateTabPage.getPstAmountFromUI();
			calculatedPstAmount = cscockpitAutoshipTemplateTabPage.calculateAmountAccordingToPercent(subTotal, deliveryCost, handlingCost, TestConstants.SEVEN_PERCENT_TAX);
			s_assert.assertTrue(cscockpitAutoshipTemplateTabPage.validatePstAmountOnUI(pstFromUI,calculatedPstAmount),"PST Amount on UI is "+pstFromUI+" and calculated GST Amount is "+calculatedPstAmount);
		}else{
			logger.info("PST NOT PRESENT FOR THIS ORDER");
		}
		s_assert.assertAll();
	}

	//Hybris Project-1578:To verify the Canada tax for Yukon for Create CRP Autoship
	@Test
	public void testVerifyCanadaTaxForYukonForCreateCRPAutoship_1578() throws InterruptedException{
		String randomProductSequenceNumber = null;
		RFO_DB = driver.getDBNameRFO();
		String SKUValue = null;
		int randomNumber=CommonUtils.getRandomNum(10000, 1000000);
		String randomCustomerSequenceNumber = null;
		String attendentFirstName=TestConstants.FIRST_NAME+randomNumber;
		String attendeeLastName = TestConstants.LAST_NAME+randomNumber;
		String  addressLine=TestConstants.ADDRESS_LINE_1_CA;
		String city=TestConstants.CITY_CA;
		String postalCode=TestConstants.POSTAL_CODE_CA;
		String Country=TestConstants.COUNTRY_DD_VALUE_CA;
		String province=TestConstants.PROVINCE_YUKON;
		String phoneNumber=TestConstants.PHONE_NUMBER_CA;
		double gstFromUI = 0.00;
		double calculatedGstAmount = 0.00;
		double pstFromUI = 0.00;
		double calculatedPstAmount = 0.00;
		cscockpitCustomerSearchTabPage = cscockpitLoginPage.clickLoginBtn();
		cscockpitCustomerSearchTabPage.selectCustomerTypeFromDropDownInCustomerSearchTab("CONSULTANT");
		cscockpitCustomerSearchTabPage.selectCountryFromDropDownInCustomerSearchTab("Canada");
		cscockpitCustomerSearchTabPage.selectAccountStatusFromDropDownInCustomerSearchTab("Active");
		cscockpitCustomerSearchTabPage.clickSearchBtn();
		randomCustomerSequenceNumber = String.valueOf(cscockpitCustomerSearchTabPage.getRandomCustomerFromSearchResult());
		cscockpitCustomerSearchTabPage.clickAndReturnCIDNumberInCustomerSearchTab(randomCustomerSequenceNumber);
		if(cscockpitCustomerTabPage.isCreateAutoshipTemplateBtnPresent() == false){
			if(cscockpitCustomerTabPage.isCRPAutoshipIDHavingStatusIsPendingPresent()==true){
				cscockpitCustomerTabPage.getAndClickAutoshipIDHavingTypeAsCRPAutoshipAndStatusIsPending();
				cscockpitAutoshipTemplateTabPage.clickCancelAutoship();
				cscockpitAutoshipTemplateTabPage.clickConfirmCancelAutoshipTemplatePopup();
				cscockpitAutoshipTemplateTabPage.clickCustomerTab();
			}
		}
		if(cscockpitCustomerTabPage.isCreateAutoshipTemplateBtnPresent() == false){
			if(cscockpitCustomerTabPage.isPulseTemplateAutoshipIDHavingStatusIsPendingPresent()==true){
				cscockpitCustomerTabPage.getAndClickPulseTemplateAutoshipIDHavingStatusIsPending();
				cscockpitAutoshipTemplateTabPage.clickCancelAutoship();
				cscockpitAutoshipTemplateTabPage.clickConfirmCancelAutoshipTemplatePopup();
				cscockpitAutoshipTemplateTabPage.clickCustomerTab();
			}
		}
		cscockpitCustomerTabPage.clickCreateAutoshipTemplateBtn();
		cscockpitAutoshipCartTabPage.selectValueFromSortByDDInCartTab("Price: High to Low");
		cscockpitAutoshipCartTabPage.selectCatalogFromDropDownInCartTab();	
		randomProductSequenceNumber = String.valueOf(cscockpitAutoshipCartTabPage.getRandomProductWithSKUFromSearchResult()); 
		SKUValue = cscockpitAutoshipCartTabPage.getCustomerSKUValueInCartTab(randomProductSequenceNumber);
		cscockpitAutoshipCartTabPage.searchSKUValueInCartTab(SKUValue);
		cscockpitAutoshipCartTabPage.clickAddToCartBtnInCartTab();
		cscockpitAutoshipCartTabPage.clickCheckoutBtnInCartTab();
		cscockpitCheckoutTabPage.clickAddNewAddressUnderDeliveryAddressInCheckoutTab();
		cscockpitCustomerTabPage.enterShippingInfoInAddNewPaymentProfilePopupWithoutSaveBtn(attendentFirstName, attendeeLastName, addressLine, city, postalCode, Country, province, phoneNumber);
		cscockpitCheckoutTabPage.clickOnCreateNewAddressButtonInAutoshipTemplateTabPage();
		cscockpitCheckoutTabPage.clickUseEnteredAddressOfCreateNewAddressShippingAddressPopup();
		cscockpitCheckoutTabPage.clickAddNewPaymentAddressInCheckoutTab();
		cscockpitCheckoutTabPage.enterBillingInfo();
		cscockpitCheckoutTabPage.clickSaveAddNewPaymentProfilePopUP();
		cscockpitCheckoutTabPage.enterCVVValueInCheckoutTab(TestConstants.SECURITY_CODE);
		cscockpitCheckoutTabPage.clickUseThisCardBtnInCheckoutTab();
		cscockpitAutoshipTemplateUpdateTabPage.clickCreateAutoshipTemplateBtn();
		double subTotal = cscockpitAutoshipTemplateTabPage.getSubTotalFromUI();
		double deliveryCost = cscockpitAutoshipTemplateTabPage.getDeliveryCostFromUI();
		double handlingCost = cscockpitAutoshipTemplateTabPage.getHandlingCostFromUI();
		if(cscockpitAutoshipTemplateTabPage.isGSTTaxPresentInUI()==true){
			gstFromUI = cscockpitAutoshipTemplateTabPage.getGstAmountFromUI();
			calculatedGstAmount = cscockpitAutoshipTemplateTabPage.calculateAmountAccordingToPercent(subTotal, deliveryCost, handlingCost, TestConstants.FIVE_PERCENT_TAX);
			s_assert.assertTrue(cscockpitAutoshipTemplateTabPage.validateGstAmountOnUI(gstFromUI,calculatedGstAmount),"GST Amount on UI is "+gstFromUI+" and calculated GST Amount is "+calculatedGstAmount+"");
		}else{
			logger.info("GST NOT PRESENT FOR THIS ORDER");
		}
		if(cscockpitAutoshipTemplateTabPage.isPstTaxPresentInUI() == true){
			pstFromUI = cscockpitAutoshipTemplateTabPage.getPstAmountFromUI();
			calculatedPstAmount = cscockpitAutoshipTemplateTabPage.calculateAmountAccordingToPercent(subTotal, deliveryCost, handlingCost, TestConstants.SEVEN_PERCENT_TAX);
			s_assert.assertTrue(cscockpitAutoshipTemplateTabPage.validatePstAmountOnUI(pstFromUI,calculatedPstAmount),"PST Amount on UI is "+pstFromUI+" and calculated GST Amount is "+calculatedPstAmount);
		}else{
			logger.info("PST NOT PRESENT FOR THIS ORDER");
		}
		s_assert.assertAll();
	}

	//Hybris Project-1579:To verify the Canada tax for Northwest Territories for Create CRP Autoship
	@Test
	public void testVerifyCanadaTaxForNorthwestTerritoriesForCreateCRPAutoship_1579() throws InterruptedException{
		String randomProductSequenceNumber = null;
		RFO_DB = driver.getDBNameRFO();
		String SKUValue = null;
		int randomNumber=CommonUtils.getRandomNum(10000, 1000000);
		String randomCustomerSequenceNumber = null;
		String attendentFirstName=TestConstants.FIRST_NAME+randomNumber;
		String attendeeLastName = TestConstants.LAST_NAME+randomNumber;
		String  addressLine=TestConstants.ADDRESS_LINE_1_CA;
		String city=TestConstants.CITY_CA;
		String postalCode=TestConstants.POSTAL_CODE_CA;
		String Country=TestConstants.COUNTRY_DD_VALUE_CA;
		String province=TestConstants.PROVINCE_NORTHWEST_TERRITORIES;
		String phoneNumber=TestConstants.PHONE_NUMBER_CA;
		double gstFromUI = 0.00;
		double calculatedGstAmount = 0.00;
		double pstFromUI = 0.00;
		double calculatedPstAmount = 0.00;
		cscockpitCustomerSearchTabPage = cscockpitLoginPage.clickLoginBtn();
		cscockpitCustomerSearchTabPage.selectCustomerTypeFromDropDownInCustomerSearchTab("CONSULTANT");
		cscockpitCustomerSearchTabPage.selectCountryFromDropDownInCustomerSearchTab("Canada");
		cscockpitCustomerSearchTabPage.selectAccountStatusFromDropDownInCustomerSearchTab("Active");
		cscockpitCustomerSearchTabPage.clickSearchBtn();
		randomCustomerSequenceNumber = String.valueOf(cscockpitCustomerSearchTabPage.getRandomCustomerFromSearchResult());
		cscockpitCustomerSearchTabPage.clickAndReturnCIDNumberInCustomerSearchTab(randomCustomerSequenceNumber);
		if(cscockpitCustomerTabPage.isCreateAutoshipTemplateBtnPresent() == false){
			if(cscockpitCustomerTabPage.isCRPAutoshipIDHavingStatusIsPendingPresent()==true){
				cscockpitCustomerTabPage.getAndClickAutoshipIDHavingTypeAsCRPAutoshipAndStatusIsPending();
				cscockpitAutoshipTemplateTabPage.clickCancelAutoship();
				cscockpitAutoshipTemplateTabPage.clickConfirmCancelAutoshipTemplatePopup();
				cscockpitAutoshipTemplateTabPage.clickCustomerTab();
			}
		}
		if(cscockpitCustomerTabPage.isCreateAutoshipTemplateBtnPresent() == false){
			if(cscockpitCustomerTabPage.isPulseTemplateAutoshipIDHavingStatusIsPendingPresent()==true){
				cscockpitCustomerTabPage.getAndClickPulseTemplateAutoshipIDHavingStatusIsPending();
				cscockpitAutoshipTemplateTabPage.clickCancelAutoship();
				cscockpitAutoshipTemplateTabPage.clickConfirmCancelAutoshipTemplatePopup();
				cscockpitAutoshipTemplateTabPage.clickCustomerTab();
			}
		}
		cscockpitCustomerTabPage.clickCreateAutoshipTemplateBtn();
		cscockpitAutoshipCartTabPage.selectValueFromSortByDDInCartTab("Price: High to Low");
		cscockpitAutoshipCartTabPage.selectCatalogFromDropDownInCartTab();	
		randomProductSequenceNumber = String.valueOf(cscockpitAutoshipCartTabPage.getRandomProductWithSKUFromSearchResult()); 
		SKUValue = cscockpitAutoshipCartTabPage.getCustomerSKUValueInCartTab(randomProductSequenceNumber);
		cscockpitAutoshipCartTabPage.searchSKUValueInCartTab(SKUValue);
		cscockpitAutoshipCartTabPage.clickAddToCartBtnInCartTab();
		cscockpitAutoshipCartTabPage.clickCheckoutBtnInCartTab();
		cscockpitCheckoutTabPage.clickAddNewAddressUnderDeliveryAddressInCheckoutTab();
		cscockpitCustomerTabPage.enterShippingInfoInAddNewPaymentProfilePopupWithoutSaveBtn(attendentFirstName, attendeeLastName, addressLine, city, postalCode, Country, province, phoneNumber);
		cscockpitCheckoutTabPage.clickOnCreateNewAddressButtonInAutoshipTemplateTabPage();
		cscockpitCheckoutTabPage.clickUseEnteredAddressOfCreateNewAddressShippingAddressPopup();
		cscockpitCheckoutTabPage.clickAddNewPaymentAddressInCheckoutTab();
		cscockpitCheckoutTabPage.enterBillingInfo();
		cscockpitCheckoutTabPage.clickSaveAddNewPaymentProfilePopUP();
		cscockpitCheckoutTabPage.enterCVVValueInCheckoutTab(TestConstants.SECURITY_CODE);
		cscockpitCheckoutTabPage.clickUseThisCardBtnInCheckoutTab();
		cscockpitAutoshipTemplateUpdateTabPage.clickCreateAutoshipTemplateBtn();
		double subTotal = cscockpitAutoshipTemplateTabPage.getSubTotalFromUI();
		double deliveryCost = cscockpitAutoshipTemplateTabPage.getDeliveryCostFromUI();
		double handlingCost = cscockpitAutoshipTemplateTabPage.getHandlingCostFromUI();
		if(cscockpitAutoshipTemplateTabPage.isGSTTaxPresentInUI()==true){
			gstFromUI = cscockpitAutoshipTemplateTabPage.getGstAmountFromUI();
			calculatedGstAmount = cscockpitAutoshipTemplateTabPage.calculateAmountAccordingToPercent(subTotal, deliveryCost, handlingCost, TestConstants.FIVE_PERCENT_TAX);
			s_assert.assertTrue(cscockpitAutoshipTemplateTabPage.validateGstAmountOnUI(gstFromUI,calculatedGstAmount),"GST Amount on UI is "+gstFromUI+" and calculated GST Amount is "+calculatedGstAmount+"");
		}else{
			logger.info("GST NOT PRESENT FOR THIS ORDER");
		}
		if(cscockpitAutoshipTemplateTabPage.isPstTaxPresentInUI() == true){
			pstFromUI = cscockpitAutoshipTemplateTabPage.getPstAmountFromUI();
			calculatedPstAmount = cscockpitAutoshipTemplateTabPage.calculateAmountAccordingToPercent(subTotal, deliveryCost, handlingCost, TestConstants.SEVEN_PERCENT_TAX);
			s_assert.assertTrue(cscockpitAutoshipTemplateTabPage.validatePstAmountOnUI(pstFromUI,calculatedPstAmount),"PST Amount on UI is "+pstFromUI+" and calculated GST Amount is "+calculatedPstAmount);
		}else{
			logger.info("PST NOT PRESENT FOR THIS ORDER");
		}
		s_assert.assertAll();
	}

	//Hybris Project-1580:To verify the Canada tax for Nunavat for Create CRP Autoship
	@Test
	public void testVerifyCanadaTaxForNunavatForCreateCRPAutoship_1580() throws InterruptedException{
		String randomProductSequenceNumber = null;
		RFO_DB = driver.getDBNameRFO();
		String SKUValue = null;
		int randomNumber=CommonUtils.getRandomNum(10000, 1000000);
		String randomCustomerSequenceNumber = null;
		String attendentFirstName=TestConstants.FIRST_NAME+randomNumber;
		String attendeeLastName = TestConstants.LAST_NAME+randomNumber;
		String  addressLine=TestConstants.ADDRESS_LINE_1_CA;
		String city=TestConstants.CITY_CA;
		String postalCode=TestConstants.POSTAL_CODE_CA;
		String Country=TestConstants.COUNTRY_DD_VALUE_CA;
		String province=TestConstants.PROVINCE_NUNAVUT;
		String phoneNumber=TestConstants.PHONE_NUMBER_CA;
		double gstFromUI = 0.00;
		double calculatedGstAmount = 0.00;
		double pstFromUI = 0.00;
		double calculatedPstAmount = 0.00;
		cscockpitCustomerSearchTabPage = cscockpitLoginPage.clickLoginBtn();
		cscockpitCustomerSearchTabPage.selectCustomerTypeFromDropDownInCustomerSearchTab("CONSULTANT");
		cscockpitCustomerSearchTabPage.selectCountryFromDropDownInCustomerSearchTab("Canada");
		cscockpitCustomerSearchTabPage.selectAccountStatusFromDropDownInCustomerSearchTab("Active");
		cscockpitCustomerSearchTabPage.clickSearchBtn();
		randomCustomerSequenceNumber = String.valueOf(cscockpitCustomerSearchTabPage.getRandomCustomerFromSearchResult());
		cscockpitCustomerSearchTabPage.clickAndReturnCIDNumberInCustomerSearchTab(randomCustomerSequenceNumber);
		if(cscockpitCustomerTabPage.isCreateAutoshipTemplateBtnPresent() == false){
			if(cscockpitCustomerTabPage.isCRPAutoshipIDHavingStatusIsPendingPresent()==true){
				cscockpitCustomerTabPage.getAndClickAutoshipIDHavingTypeAsCRPAutoshipAndStatusIsPending();
				cscockpitAutoshipTemplateTabPage.clickCancelAutoship();
				cscockpitAutoshipTemplateTabPage.clickConfirmCancelAutoshipTemplatePopup();
				cscockpitAutoshipTemplateTabPage.clickCustomerTab();
			}
		}
		if(cscockpitCustomerTabPage.isCreateAutoshipTemplateBtnPresent() == false){
			if(cscockpitCustomerTabPage.isPulseTemplateAutoshipIDHavingStatusIsPendingPresent()==true){
				cscockpitCustomerTabPage.getAndClickPulseTemplateAutoshipIDHavingStatusIsPending();
				cscockpitAutoshipTemplateTabPage.clickCancelAutoship();
				cscockpitAutoshipTemplateTabPage.clickConfirmCancelAutoshipTemplatePopup();
				cscockpitAutoshipTemplateTabPage.clickCustomerTab();
			}
		}
		cscockpitCustomerTabPage.clickCreateAutoshipTemplateBtn();
		cscockpitAutoshipCartTabPage.selectValueFromSortByDDInCartTab("Price: High to Low");
		cscockpitAutoshipCartTabPage.selectCatalogFromDropDownInCartTab();	
		randomProductSequenceNumber = String.valueOf(cscockpitAutoshipCartTabPage.getRandomProductWithSKUFromSearchResult()); 
		SKUValue = cscockpitAutoshipCartTabPage.getCustomerSKUValueInCartTab(randomProductSequenceNumber);
		cscockpitAutoshipCartTabPage.searchSKUValueInCartTab(SKUValue);
		cscockpitAutoshipCartTabPage.clickAddToCartBtnInCartTab();
		cscockpitAutoshipCartTabPage.clickCheckoutBtnInCartTab();
		cscockpitCheckoutTabPage.clickAddNewAddressUnderDeliveryAddressInCheckoutTab();
		cscockpitCustomerTabPage.enterShippingInfoInAddNewPaymentProfilePopupWithoutSaveBtn(attendentFirstName, attendeeLastName, addressLine, city, postalCode, Country, province, phoneNumber);
		cscockpitCheckoutTabPage.clickOnCreateNewAddressButtonInAutoshipTemplateTabPage();
		cscockpitCheckoutTabPage.clickUseEnteredAddressOfCreateNewAddressShippingAddressPopup();
		cscockpitCheckoutTabPage.clickAddNewPaymentAddressInCheckoutTab();
		cscockpitCheckoutTabPage.enterBillingInfo();
		cscockpitCheckoutTabPage.clickSaveAddNewPaymentProfilePopUP();
		cscockpitCheckoutTabPage.enterCVVValueInCheckoutTab(TestConstants.SECURITY_CODE);
		cscockpitCheckoutTabPage.clickUseThisCardBtnInCheckoutTab();
		cscockpitAutoshipTemplateUpdateTabPage.clickCreateAutoshipTemplateBtn();
		double subTotal = cscockpitAutoshipTemplateTabPage.getSubTotalFromUI();
		double deliveryCost = cscockpitAutoshipTemplateTabPage.getDeliveryCostFromUI();
		double handlingCost = cscockpitAutoshipTemplateTabPage.getHandlingCostFromUI();
		if(cscockpitAutoshipTemplateTabPage.isGSTTaxPresentInUI()==true){
			gstFromUI = cscockpitAutoshipTemplateTabPage.getGstAmountFromUI();
			calculatedGstAmount = cscockpitAutoshipTemplateTabPage.calculateAmountAccordingToPercent(subTotal, deliveryCost, handlingCost, TestConstants.FIVE_PERCENT_TAX);
			s_assert.assertTrue(cscockpitAutoshipTemplateTabPage.validateGstAmountOnUI(gstFromUI,calculatedGstAmount),"GST Amount on UI is "+gstFromUI+" and calculated GST Amount is "+calculatedGstAmount+"");
		}else{
			logger.info("GST NOT PRESENT FOR THIS ORDER");
		}
		if(cscockpitAutoshipTemplateTabPage.isPstTaxPresentInUI() == true){
			pstFromUI = cscockpitAutoshipTemplateTabPage.getPstAmountFromUI();
			calculatedPstAmount = cscockpitAutoshipTemplateTabPage.calculateAmountAccordingToPercent(subTotal, deliveryCost, handlingCost, TestConstants.SEVEN_PERCENT_TAX);
			s_assert.assertTrue(cscockpitAutoshipTemplateTabPage.validatePstAmountOnUI(pstFromUI,calculatedPstAmount),"PST Amount on UI is "+pstFromUI+" and calculated GST Amount is "+calculatedPstAmount);
		}else{
			logger.info("PST NOT PRESENT FOR THIS ORDER");
		}
		s_assert.assertAll();
	}		
}