package com.rf.test.website.cscockpit;

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
import com.rf.pages.website.cscockpit.CSCockpitCustomerSearchTabPage;
import com.rf.pages.website.cscockpit.CSCockpitCustomerTabPage;
import com.rf.pages.website.cscockpit.CSCockpitLoginPage;
import com.rf.pages.website.cscockpit.CSCockpitOrderSearchTabPage;
import com.rf.pages.website.cscockpit.CSCockpitOrderTabPage;
import com.rf.pages.website.storeFront.StoreFrontAccountInfoPage;
import com.rf.pages.website.storeFront.StoreFrontBillingInfoPage;
import com.rf.pages.website.storeFront.StoreFrontCartAutoShipPage;
import com.rf.pages.website.storeFront.StoreFrontConsultantPage;
import com.rf.pages.website.storeFront.StoreFrontHomePage;
import com.rf.pages.website.storeFront.StoreFrontOrdersPage;
import com.rf.pages.website.storeFront.StoreFrontPCUserPage;
import com.rf.pages.website.storeFront.StoreFrontRCUserPage;
import com.rf.pages.website.storeFront.StoreFrontUpdateCartPage;
import com.rf.test.website.RFWebsiteBaseTest;

public class CRPAutoshipVerificationTest extends RFWebsiteBaseTest{
	private static final Logger logger = LogManager
			.getLogger(CRPAutoshipVerificationTest.class.getName());

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
	private CSCockpitAutoshipCartTabPage cscockpitAutoshipCartTabPage;
	private CSCockpitAutoshipTemplateUpdateTabPage cscockpitAutoshipTemplateUpdateTabPage;
	private StoreFrontHomePage storeFrontHomePage; 
	private StoreFrontConsultantPage storeFrontConsultantPage;
	private StoreFrontOrdersPage storeFrontOrdersPage;
	private StoreFrontPCUserPage storeFrontPCUserPage;
	private StoreFrontRCUserPage storeFrontRCUserPage;	
	private StoreFrontCartAutoShipPage storeFrontCartAutoShipPage;
	private StoreFrontUpdateCartPage storeFrontUpdateCartPage;
	private StoreFrontAccountInfoPage storeFrontAccountInfoPage;
	private StoreFrontBillingInfoPage storeFrontBillingInfoPage;

	//-----------------------------------------------------------------------------------------------------------------

	public CRPAutoshipVerificationTest() {
		cscockpitLoginPage = new CSCockpitLoginPage(driver);
		cscockpitAutoshipSearchTabPage = new CSCockpitAutoshipSearchTabPage(driver);
		cscockpitCheckoutTabPage = new CSCockpitCheckoutTabPage(driver);
		cscockpitCustomerSearchTabPage = new CSCockpitCustomerSearchTabPage(driver);
		cscockpitCustomerTabPage = new CSCockpitCustomerTabPage(driver);
		cscockpitOrderSearchTabPage = new CSCockpitOrderSearchTabPage(driver);
		cscockpitOrderTabPage = new CSCockpitOrderTabPage(driver);
		cscockpitCartTabPage = new CSCockpitCartTabPage(driver);
		cscockpitAutoshipTemplateTabPage = new CSCockpitAutoshipTemplateTabPage(driver);
		cscockpitAutoshipCartTabPage = new CSCockpitAutoshipCartTabPage(driver);
		cscockpitAutoshipTemplateUpdateTabPage = new CSCockpitAutoshipTemplateUpdateTabPage(driver);
		storeFrontHomePage = new StoreFrontHomePage(driver);
		storeFrontConsultantPage = new StoreFrontConsultantPage(driver);
		storeFrontOrdersPage = new StoreFrontOrdersPage(driver);
		storeFrontPCUserPage = new StoreFrontPCUserPage(driver);
		storeFrontRCUserPage = new StoreFrontRCUserPage(driver);
		storeFrontUpdateCartPage = new StoreFrontUpdateCartPage(driver);
		storeFrontAccountInfoPage = new StoreFrontAccountInfoPage(driver);
		storeFrontCartAutoShipPage = new StoreFrontCartAutoShipPage(driver);
		cscockpitCartTabPage = new CSCockpitCartTabPage(driver);


	}

	private String RFO_DB = null;

	//Hybris Project-1735:To verify Add New payment profile functionality in the Checkout Page
	@Test
	public void testToVerifyAddNewPaymentProfileFunctionalityInTheCheckoutPage_1735(){
		String randomOrderSequenceNumber = null;
		String randomProductSequenceNumber = null;
		RFO_DB = driver.getDBNameRFO();
		String consultantEmailID = null;
		String SKUValue = null;
		String accountID = null;
		String addressLine= null;
		String city= null;
		String postalCode= null;
		String Country= null;
		String Province= null;
		String phoneNumber= null;

		int randomNum = CommonUtils.getRandomNum(10000, 1000000);
		String attendeeLastName="IN";
		String newBillingProfileName = TestConstants.NEW_BILLING_PROFILE_NAME_US+randomNum;
		String securityCode=TestConstants.SECURITY_CODE;
		String cardNumber=TestConstants.CARD_NUMBER;
		String attendeeFirstName=TestConstants.FIRST_NAME+randomNum;
		if(driver.getCountry().equalsIgnoreCase("us")){
			addressLine=TestConstants.NEW_ADDRESS_LINE1_US;
			city=TestConstants.NEW_ADDRESS_CITY_US;
			postalCode=TestConstants.NEW_ADDRESS_POSTAL_CODE_US;
			Country=TestConstants.COUNTRY_DD_VALUE_US;
			Province=TestConstants.PROVINCE_ALABAMA_US;
			phoneNumber=TestConstants.NEW_ADDRESS_PHONE_NUMBER_US;
		}else{
			addressLine=TestConstants.ADDRESS_LINE_1_CA;
			city=TestConstants.CITY_CA;
			postalCode=TestConstants.POSTAL_CODE_CA;
			Country=TestConstants.COUNTRY_DD_VALUE_CA;
			Province=TestConstants.PROVINCE_CA;
			phoneNumber=TestConstants.PHONE_NUMBER_CA;
		}
		List<Map<String, Object>> randomConsultantList =  null;
		driver.get(driver.getStoreFrontURL()+"/"+driver.getCountry());
		storeFrontHomePage = new StoreFrontHomePage(driver);
		while(true){
			randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_CONSULTANT_WITH_ORDERS_AND_AUTOSHIPS_RFO,countryId),RFO_DB);
			consultantEmailID = (String) getValueFromQueryResult(randomConsultantList, "UserName");
			accountID = String.valueOf(getValueFromQueryResult(randomConsultantList, "AccountID"));

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
		List<Map<String, Object>> randomConsultantUsernameList =  null;
		randomConsultantUsernameList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_EMAIL_ID_FROM_ACCOUNT_ID,accountID),RFO_DB);
		consultantEmailID = String.valueOf(getValueFromQueryResult(randomConsultantUsernameList, "EmailAddress"));  
		logger.info("emaild of consultant username "+consultantEmailID);
		logger.info("login is successful");
		driver.get(driver.getCSCockpitURL());
		cscockpitCustomerSearchTabPage = cscockpitLoginPage.clickLoginBtn();
		cscockpitCustomerSearchTabPage.selectCustomerTypeFromDropDownInCustomerSearchTab("CONSULTANT");
		cscockpitCustomerSearchTabPage.selectCountryFromDropDownInCustomerSearchTab(Country);
		cscockpitCustomerSearchTabPage.selectAccountStatusFromDropDownInCustomerSearchTab("Active");
		cscockpitCustomerSearchTabPage.enterEmailIdInSearchFieldInCustomerSearchTab(consultantEmailID);
		cscockpitCustomerSearchTabPage.clickSearchBtn();
		randomOrderSequenceNumber = String.valueOf(cscockpitOrderSearchTabPage.getRandomOrdersFromOrderResultSearchFirstPageInOrderSearchTab());
		cscockpitOrderSearchTabPage.clickOrderNumberInOrderSearchResultsInOrderSearchTab(randomOrderSequenceNumber);
		cscockpitOrderTabPage.clickPlaceAnOrderButtonInOrderTab();
		cscockpitCartTabPage.selectValueFromSortByDDInCartTab("Price: High to Low");
		cscockpitCartTabPage.selectCatalogFromDropDownInCartTab();
		randomProductSequenceNumber = String.valueOf(cscockpitCartTabPage.getRandomProductWithSKUFromSearchResult()); 
		SKUValue = cscockpitCartTabPage.getCustomerSKUValueInCartTab(randomProductSequenceNumber);
		cscockpitCartTabPage.searchSKUValueInCartTab(SKUValue);
		cscockpitCartTabPage.clickAddToCartBtnInCartTab();
		cscockpitCartTabPage.clickCheckoutBtnInCartTab();
		cscockpitCheckoutTabPage.clickAddNewPaymentAddressInCheckoutTab();
		cscockpitCheckoutTabPage.clickCloseAddNewPaymentProfilePopUp();
		cscockpitCheckoutTabPage.clickAddNewPaymentAddressInCheckoutTab();
		cscockpitCheckoutTabPage.clickSaveAddNewPaymentProfilePopUP();
		s_assert.assertTrue(cscockpitCheckoutTabPage.getErrorMessageOfPopupWithoutFillingDataInCheckoutTab().contains("Please review credit card information entered"),"CSCockpit checkout tab popup error message expected = Please review credit card information entered and on UI = " +cscockpitCheckoutTabPage.getErrorMessageOfPopupWithoutFillingDataInCheckoutTab());
		cscockpitCheckoutTabPage.enterPaymentDetailsInPopUpInCheckoutTab(cardNumber, newBillingProfileName,securityCode,"09","2023","VISA");
		cscockpitCheckoutTabPage.clickAddNewAddressLinkInPopUpInCheckoutTab();
		cscockpitCheckoutTabPage.enterShippingDetailsInPopUpInCheckoutTab(attendeeFirstName,attendeeLastName,addressLine,city,postalCode,Country,Province,phoneNumber);
		cscockpitCheckoutTabPage.enterSecurityCodeInPaymentPopUpInCheckOutTab(securityCode);
		cscockpitCheckoutTabPage.clickSaveOfShippingAddressPopUpInCheckoutTab();
		s_assert.assertTrue(cscockpitCheckoutTabPage.getNewBillingAddressNameInCheckoutTab().contains(attendeeFirstName),"CSCockpit checkout tab newly created billing address name expected ="+ attendeeFirstName+ "and on UI = " +cscockpitCheckoutTabPage.getNewBillingAddressNameInCheckoutTab());

		cscockpitCheckoutTabPage.enterCVVValueInCheckoutTab(TestConstants.SECURITY_CODE);
		cscockpitCheckoutTabPage.clickUseThisCardBtnInCheckoutTab();
		cscockpitCheckoutTabPage.clickPlaceOrderButtonInCheckoutTab();
		s_assert.assertTrue(cscockpitOrderTabPage.validateNewAddressOnOrderTabPage(newBillingProfileName),"newly added address is not saved successfully");
		s_assert.assertAll();
	}

	//Hybris Project-1722:Verify the Find Autoship Page UI
	@Test
	public void testVerifyTheFindAutoshipPageUI_1722(){
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

		cscockpitLoginPage = new CSCockpitLoginPage(driver);
		driver.get(driver.getCSCockpitURL());
		cscockpitCustomerSearchTabPage = cscockpitLoginPage.clickLoginBtn();
		cscockpitCustomerSearchTabPage.clickFindAutoshipInLeftNavigation();
		s_assert.assertTrue(cscockpitAutoshipSearchTabPage.verifySearchByFieldPresentOnAutoshipSearch(),"Search By field is not present On autoship Search page");
		cscockpitAutoshipSearchTabPage.clickSearchByDropDown();
		s_assert.assertTrue(cscockpitAutoshipSearchTabPage.isSearchByDropDownValuePresent(searchByDDValue_All),"search By field DD option "+searchByDDValue_All+" is not present on UI");
		s_assert.assertTrue(cscockpitAutoshipSearchTabPage.isSearchByDropDownValuePresent(searchByDDValue_All_Due_Today),"search By field DD option "+searchByDDValue_All_Due_Today+" is not present on UI");
		s_assert.assertTrue(cscockpitAutoshipSearchTabPage.isSearchByDropDownValuePresent(searchByDDValue_NextDueDate),"search By field DD option "+searchByDDValue_NextDueDate+" is not present on UI");

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

		s_assert.assertTrue(cscockpitAutoshipSearchTabPage.verifyCustomerNameCidFieldPresentOnAutoshipPage(),"customer name and cid field not present on the page");
		s_assert.assertTrue(cscockpitAutoshipSearchTabPage.verifySponsorNameCidFieldPresentOnAutoshipPage(),"SponsorName field not present on the page");
		s_assert.assertTrue(cscockpitAutoshipSearchTabPage.verifyTemplateNumberCidFieldPresentOnAutoshipPage(),"TemplateNumber field not present on the page");
		s_assert.assertTrue(cscockpitAutoshipSearchTabPage.verifyRunSelectedButtonPresent(),"Run selected button is not present on the page");
		s_assert.assertTrue(cscockpitAutoshipSearchTabPage.verifySearchAutoshipButtonPresent(),"search autoship button is not present on the page");
		s_assert.assertAll();
	}


	// Hybris Project-1702:To verify edit CRP Autoship template
	@Test
	public void testVerifyEditCRPAutoshipTemplate_1702() throws InterruptedException{
		RFO_DB = driver.getDBNameRFO();
		String countryCode= driver.getCountry();
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
		String country = null;

		if(driver.getCountry().equalsIgnoreCase("ca")){
			country= TestConstants.COUNTRY_DD_VALUE_CA;
		}else{
			country= TestConstants.COUNTRY_DD_VALUE_US;
		}
		//-------------------FOR US----------------------------------
		List<Map<String, Object>> randomConsultantList =  null;
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
		storeFrontHomePage.clickOnAutoshipCart();
		beforeProductCountInAutoshipCart=storeFrontUpdateCartPage.getProductCountOnAutoShipCartPage();
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
		driver.get(driver.getStoreFrontURL()+"/"+countryCode);
		storeFrontConsultantPage = storeFrontHomePage.loginAsConsultant(consultantEmailID, password);
		storeFrontHomePage.clickOnAutoshipCart();
		afterProductCountInAutoshipCart=storeFrontUpdateCartPage.getProductCountOnAutoShipCartPage();
		s_assert.assertFalse(beforeProductCountInAutoshipCart.equalsIgnoreCase(afterProductCountInAutoshipCart), "Product has not been successfully in storefront cart page.");
		logout(); 
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

	// Hybris Project-1705:To verify change CRP date
	@Test
	public void testVerifyChangeCRPDate_1705() throws InterruptedException{
		RFO_DB = driver.getDBNameRFO();
		String countryCode= driver.getCountry();
		String randomCustomerSequenceNumber = null;
		String consultantEmailID=null;
		String country = null;

		if(driver.getCountry().equalsIgnoreCase("ca")){
			country= TestConstants.COUNTRY_DD_VALUE_CA;
		}else{
			country= TestConstants.COUNTRY_DD_VALUE_US;
		}

		//-------------------FOR US----------------------------------
		List<Map<String, Object>> randomConsultantList =  null;
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
		cscockpitCustomerSearchTabPage.clickAndReturnCIDNumberInCustomerSearchTab(randomCustomerSequenceNumber);
		cscockpitCustomerTabPage.getAndClickAutoshipIDHavingTypeAsCRPAutoshipAndStatusIsPending();
		cscockpitAutoshipTemplateTabPage.clickEditAutoshiptemplate();
		String nextDueDate = cscockpitAutoshipTemplateTabPage.getCRPAutoshipDateFromCalendar();
		String modifiedDate = cscockpitAutoshipTemplateTabPage.addOneMoreDayInCRPAutoshipDate(nextDueDate);
		cscockpitAutoshipTemplateTabPage.enterCRPAutoshipDate(modifiedDate);
		cscockpitAutoshipTemplateTabPage.clickCustomerTab();
		String day = modifiedDate.split("\\ ")[1];
		s_assert.assertTrue(cscockpitCustomerTabPage.getNextDueDateOfCRPAutoshipAndStatusIsPending().split("\\/")[1].contains(day.split("\\,")[0]),"Expected day of CRP is "+day.split("\\,")[0]+"Actual on UI "+cscockpitCustomerTabPage.getNextDueDateOfCRPAutoshipAndStatusIsPending().split("\\/"));
		driver.get(driver.getStoreFrontURL()+"/"+countryCode);
		try{
			storeFrontConsultantPage = storeFrontHomePage.loginAsConsultant(consultantEmailID, password);
		}catch(Exception e){
			driver.get(driver.getStoreFrontURL()+"/"+countryCode);
			storeFrontConsultantPage = storeFrontHomePage.loginAsConsultant(consultantEmailID, password);
		}
		storeFrontConsultantPage.clickOnWelcomeDropDown();
		storeFrontAccountInfoPage = storeFrontConsultantPage.clickAccountInfoLinkPresentOnWelcomeDropDown();
		storeFrontAccountInfoPage.clickOnYourAccountDropdown();
		storeFrontAccountInfoPage.clickOnAutoShipStatus();
		String crpDate = storeFrontAccountInfoPage.getNextDueDateOfCRPTemplate();
		s_assert.assertTrue(crpDate.trim().split("\\ ")[1].contains(day.split("\\,")[0]), "Expected next day of CRP is "+day.split("\\,")[0]+"Actual on UI in storeFront "+crpDate);
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
		String country = null;

		if(driver.getCountry().equalsIgnoreCase("ca")){
			country= TestConstants.COUNTRY_DD_VALUE_CA;
		}else{
			country= TestConstants.COUNTRY_DD_VALUE_US;
		}
		//-------------------FOR US----------------------------------
		List<Map<String, Object>> randomConsultantList =  null;
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
		//cancel CRP
		storeFrontConsultantPage.clickOnWelcomeDropDown();
		storeFrontAccountInfoPage=storeFrontConsultantPage.clickAccountInfoLinkPresentOnWelcomeDropDown();
		storeFrontAccountInfoPage.clickOnYourAccountDropdown();
		storeFrontAccountInfoPage.clickOnAutoShipStatus();
		if(storeFrontAccountInfoPage.verifyCurrentCRPStatus()){
			storeFrontAccountInfoPage.clickOnCancelMyCRP();
		}
		//validate CRP has been cancelled..
		s_assert.assertTrue(storeFrontAccountInfoPage.verifyCRPCancelled(), "CRP has not been cancelled");
		storeFrontAccountInfoPage.clickOnCancelMyPulseSubscription();
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
		int day = cscockpitAutoshipTemplateTabPage.getDayFromDate(currentPSTDate);
		String oneMonthExtendedDate = null;
		if(day<=17){
			oneMonthExtendedDate = cscockpitAutoshipTemplateTabPage.getOneMonthOutDate(PSTDate);
		}else{
			oneMonthExtendedDate = cscockpitAutoshipTemplateTabPage.getOneMonthOutDateAfter17(PSTDate);
		}
		s_assert.assertTrue(oneMonthExtendedDate.trim().contains(nextDueDate), "Expected next due date of CRP is "+nextDueDate+"Actual on UI in Cscockpit "+oneMonthExtendedDate.trim());
		driver.get(driver.getStoreFrontURL()+"/"+driver.getCountry());
		try{
			storeFrontConsultantPage = storeFrontHomePage.loginAsConsultant(consultantEmailID, password);
		}catch(Exception e){
			driver.get(driver.getStoreFrontURL()+"/"+driver.getCountry());
			storeFrontConsultantPage = storeFrontHomePage.loginAsConsultant(consultantEmailID, password);
		}
		storeFrontConsultantPage.clickOnWelcomeDropDown();
		storeFrontAccountInfoPage = storeFrontConsultantPage.clickAccountInfoLinkPresentOnWelcomeDropDown();
		storeFrontAccountInfoPage.clickOnYourAccountDropdown();
		storeFrontAccountInfoPage.clickOnAutoShipStatus();
		String crpDate = storeFrontAccountInfoPage.getNextDueDateOfCRPTemplate();
		s_assert.assertTrue(crpDate.trim().contains(oneMonthExtendedDate.trim()), "Expected next due date of CRP is "+oneMonthExtendedDate+"Actual on UI in storeFront "+crpDate);
		logout();
		s_assert.assertAll();
	}

	//Hybris Project-4722:Verify that ' Pulse fee' product not available to add into CRP cart.
	@Test
	public void testVerifyPulseFeeProductNotAvailableToAddIntoCrpCart_4722() throws InterruptedException{
		RFO_DB = driver.getDBNameRFO();
		String randomCustomerSequenceNumber = null;
		String consultantEmailID = null;
		String accountID = null;
		String country = null;
		if(driver.getCountry().equalsIgnoreCase("ca")){
			country= TestConstants.COUNTRY_DD_VALUE_CA;
		}else{
			country= TestConstants.COUNTRY_DD_VALUE_US;
		}

		//-------------------FOR US----------------------------------
		driver.get(driver.getStoreFrontURL()+"/"+driver.getCountry());
		List<Map<String, Object>> randomConsultantList =  null;
		while(true){
			randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_CONSULTANT_WITH_ORDERS_AND_AUTOSHIPS_RFO,countryId),RFO_DB);
			consultantEmailID = (String) getValueFromQueryResult(randomConsultantList, "UserName");  
			accountID = String.valueOf(getValueFromQueryResult(randomConsultantList, "AccountID")); 
			logger.info("Account Id of user "+accountID);
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
				continue;
			}
			else{
				storeFrontConsultantPage.clickOnWelcomeDropDown();
				storeFrontAccountInfoPage = storeFrontConsultantPage.clickAccountInfoLinkPresentOnWelcomeDropDown();
				storeFrontAccountInfoPage.clickOnYourAccountDropdown();
				storeFrontAccountInfoPage.clickOnAutoShipStatus();
				if(storeFrontAccountInfoPage.verifyCRPCancelled()==true){
					logout();
					driver.get(driver.getStoreFrontURL()+"/"+driver.getCountry());
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
		cscockpitCustomerSearchTabPage.selectCountryFromDropDownInCustomerSearchTab(country);
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
		String country = null;
		if(driver.getCountry().equalsIgnoreCase("ca")){
			country= TestConstants.COUNTRY_DD_VALUE_CA;
		}else{
			country= TestConstants.COUNTRY_DD_VALUE_US;
		}
		driver.get(driver.getStoreFrontURL()+"/"+driver.getCountry());
		List<Map<String, Object>> randomPCList =  null;
		while(true){
			randomPCList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_PC_WITH_ORDERS_AND_AUTOSHIPS_RFO,countryId),RFO_DB);
			pcEmailID = (String) getValueFromQueryResult(randomPCList, "UserName");  
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
		List<Map<String, Object>> randomConsultantUsernameList =  null;
		randomConsultantUsernameList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_EMAIL_ID_FROM_ACCOUNT_ID,accountID),RFO_DB);
		pcEmailID = String.valueOf(getValueFromQueryResult(randomConsultantUsernameList, "EmailAddress"));  
		logger.info("emaild of consultant username "+pcEmailID); 
		driver.get(driver.getCSCockpitURL());  
		cscockpitCustomerSearchTabPage = cscockpitLoginPage.clickLoginBtn();
		cscockpitCustomerSearchTabPage.selectCustomerTypeFromDropDownInCustomerSearchTab("PC");
		cscockpitCustomerSearchTabPage.selectCountryFromDropDownInCustomerSearchTab(country);
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

	//Hybris Project-1715:To verify that Run now button should not be displayed for cancelled Pcperks Autoship
	@Test
	public void testVerifyRunNowButtonNotDisplayedForCancelledPCPerksAutoship_1715() throws InterruptedException{
		String randomCustomerSequenceNumber = null;
		RFO_DB = driver.getDBNameRFO();
		int randomNum = CommonUtils.getRandomNum(10000, 1000000);
		String pcEmailAddress=null;

		cscockpitCustomerSearchTabPage = cscockpitLoginPage.clickLoginBtn();
		cscockpitCustomerSearchTabPage.selectCustomerTypeFromDropDownInCustomerSearchTab("PC");
		cscockpitCustomerSearchTabPage.selectAccountStatusFromDropDownInCustomerSearchTab("Active");
		cscockpitCustomerSearchTabPage.clickSearchBtn();
		randomCustomerSequenceNumber = String.valueOf(cscockpitCustomerSearchTabPage.getRandomCustomerFromSearchResult());
		pcEmailAddress=cscockpitCustomerSearchTabPage.getEmailIdOfTheCustomerInCustomerSearchTab(randomCustomerSequenceNumber);
		cscockpitCustomerSearchTabPage.clickAndReturnCIDNumberInCustomerSearchTab(randomCustomerSequenceNumber);
		cscockpitCustomerTabPage.getAndClickAutoshipIDHavingTypeAsPCAutoshipAndStatusIsPending();
		cscockpitAutoshipTemplateTabPage.clickCancelAutoship();
		cscockpitAutoshipTemplateTabPage.selectPCPerksCancellationReasonFromDropDownInAutoShipTemplateTab();
		cscockpitAutoshipTemplateTabPage.selectPCPerksRequestSourceFromDropDownInAutoShipTemplateTab();
		cscockpitAutoshipTemplateTabPage.enterPCPerksCancellationMessage("Automation termination"+randomNum);
		cscockpitAutoshipTemplateTabPage.clickConfirmCancelAutoshipTemplatePopup();
		cscockpitAutoshipTemplateTabPage.clickMenuButton();
		cscockpitAutoshipTemplateTabPage.clickLogoutButton();
		cscockpitCustomerSearchTabPage = cscockpitLoginPage.clickLoginBtn();
		cscockpitCustomerSearchTabPage.selectCustomerTypeFromDropDownInCustomerSearchTab("PC");
		cscockpitCustomerSearchTabPage.selectAccountStatusFromDropDownInCustomerSearchTab("Inactive");
		cscockpitCustomerSearchTabPage.enterEmailIdInSearchFieldInCustomerSearchTab(pcEmailAddress);
		cscockpitCustomerSearchTabPage.clickSearchBtn();
		randomCustomerSequenceNumber = String.valueOf(cscockpitCustomerSearchTabPage.getRandomCustomerFromSearchResult());
		cscockpitCustomerSearchTabPage.clickAndReturnCIDNumberInCustomerSearchTab(randomCustomerSequenceNumber);
		cscockpitCustomerTabPage.getAndClickAutoshipIDHavingTypeAsPCAutoshipAndStatusIsCancelled();
		s_assert.assertTrue(cscockpitAutoshipTemplateTabPage.verifyDisabledRunNowLinkInOrderFromAutoshipTemplateInAutoshipTemplateTab(),"Run Now button is enabled in order from autoship template section");
		s_assert.assertAll();

	}

	//Hybris Project-1703:To verify update CRP autoship Template
	@Test
	public void testVerifyUpdateCRPAutoshipTemplate_1703() throws InterruptedException{
		int randomNum = CommonUtils.getRandomNum(10000, 1000000);
		RFO_DB = driver.getDBNameRFO();
		String randomCustomerSequenceNumber = null;
		String autoshipNumber=null;
		List<Map<String, Object>> randomConsultantList =  null;
		storeFrontHomePage = new StoreFrontHomePage(driver);
		String country = null;
		if(driver.getCountry().equalsIgnoreCase("ca")){
			country= TestConstants.COUNTRY_DD_VALUE_CA;
		}else{
			country= TestConstants.COUNTRY_DD_VALUE_US;
		}
		driver.get(driver.getStoreFrontURL()+"/"+driver.getCountry());
		String consultantEmailID;
		String accountID;
		while(true){
			randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_CONSULTANT_WITH_ORDERS_AND_AUTOSHIPS_RFO,countryId),RFO_DB);
			consultantEmailID = (String) getValueFromQueryResult(randomConsultantList, "UserName");
			accountID = String.valueOf(getValueFromQueryResult(randomConsultantList, "AccountID"));
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
			else{
				break;
			}
		}
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
		cscockpitCustomerSearchTabPage.selectCountryFromDropDownInCustomerSearchTab(country);
		cscockpitCustomerSearchTabPage.selectAccountStatusFromDropDownInCustomerSearchTab("Active");
		cscockpitCustomerSearchTabPage.enterEmailIdInSearchFieldInCustomerSearchTab(consultantEmailID);
		cscockpitCustomerSearchTabPage.clickSearchBtn();

		randomCustomerSequenceNumber = String.valueOf(cscockpitCustomerSearchTabPage.getRandomCustomerFromSearchResult());
		cscockpitCustomerSearchTabPage.clickAndReturnCIDNumberInCustomerSearchTab(randomCustomerSequenceNumber);
		//Verify Different Section on Customer tab page.
		s_assert.assertTrue(cscockpitCustomerTabPage.verifyAutoshipTemplateSectionInCustomerTab(),"AutoShip Template section is not on Customer Tab Page");
		s_assert.assertTrue(cscockpitCustomerTabPage.verifyCustomerOrderSectionInCustomerTab(),"Customer Order section is not on Customer Tab Page");
		s_assert.assertTrue(cscockpitCustomerTabPage.verifyCustomerBillingInfoSectionInCustomerTab(),"Customer Billing Info section is not on Customer Tab Page");
		s_assert.assertTrue(cscockpitCustomerTabPage.verifyCustomerAddressSectionInCustomerTab(),"Customer Address section is not on Customer Tab Page");
		autoshipNumber=cscockpitCustomerTabPage.getAndClickAutoshipIDHavingTypeAsCRPAutoshipAndStatusIsPending();
		cscockpitAutoshipTemplateTabPage.clickEditAutoshiptemplate();
		cscockpitAutoshipTemplateTabPage.enterOrderNotesInCheckoutTab(TestConstants.ORDER_NOTE+randomNum);
		String orderNotevalueFromUI = cscockpitAutoshipTemplateTabPage.getAddedNoteValueInCheckoutTab(TestConstants.ORDER_NOTE+randomNum);
		String pstDate = cscockpitAutoshipTemplateTabPage.getPSTDate();
		String orderDate = cscockpitAutoshipTemplateTabPage.converPSTDateToUIFormat(pstDate);
		s_assert.assertTrue(cscockpitAutoshipTemplateTabPage.convertUIDateFormatToPSTFormat(orderNotevalueFromUI.split("\\ ")[0]).trim().contains(cscockpitAutoshipTemplateTabPage.getPSTDate()) || cscockpitAutoshipTemplateTabPage.convertUIDateFormatToPSTFormat(orderNotevalueFromUI.split("\\ ")[0]).trim().contains(orderDate),"CSCockpit added order note date in checkout tab expected"+cscockpitAutoshipTemplateTabPage.getPSTDate()+"and on UI" +cscockpitAutoshipTemplateTabPage.convertUIDateFormatToPSTFormat(orderNotevalueFromUI.split("\\ ")[0]).trim());
		s_assert.assertTrue(orderNotevalueFromUI.contains("PM")||orderNotevalueFromUI.contains("AM"), "Added order note does not contain time zone");
		s_assert.assertTrue(cscockpitAutoshipTemplateTabPage.verifyEditButtonIsPresentForOrderNoteInCheckoutTab(TestConstants.ORDER_NOTE+randomNum), "Added order note does not have Edit button");
		cscockpitAutoshipTemplateTabPage.clickAddMoreLinesLinkInAutoShipTemplateTab();
		cscockpitAutoshipCartTabPage.selectValueFromSortByDDInCartTab("Price: High to Low");
		cscockpitAutoshipCartTabPage.selectCatalogFromDropDownInCartTab();	
		String randomProductSequenceNumber = String.valueOf(cscockpitAutoshipCartTabPage.getRandomProductWithSKUFromSearchResult()); 
		String SKUValue = cscockpitAutoshipCartTabPage.getCustomerSKUValueInCartTab(randomProductSequenceNumber);
		cscockpitAutoshipCartTabPage.searchSKUValueInCartTab(SKUValue);
		SKUValue = cscockpitAutoshipCartTabPage.clickAddToCartBtnInCartTab(SKUValue);
		String qtyOfProduct =  cscockpitAutoshipCartTabPage.getQtyOfProductAddedInToCart(SKUValue);
		cscockpitAutoshipCartTabPage.clickCheckoutBtnInCartTab();
		cscockpitAutoshipTemplateUpdateTabPage.clickAddNewPaymentAddressInCheckoutTab();
		cscockpitAutoshipTemplateUpdateTabPage.enterBillingInfo();
		cscockpitAutoshipTemplateUpdateTabPage.clickSaveAddNewPaymentProfilePopUP();
		cscockpitAutoshipTemplateUpdateTabPage.enterCVVValueInCheckoutTab(TestConstants.SECURITY_CODE);
		cscockpitAutoshipTemplateUpdateTabPage.clickUseThisCardBtnInCheckoutTab();
		cscockpitAutoshipTemplateUpdateTabPage.clickUpdateAutoshipTemplateInAutoshipTemplateUpdateTab();
		cscockpitAutoshipTemplateUpdateTabPage.clickAutoshipSearchTab();
		cscockpitAutoshipSearchTabPage.enterTemplateNumber(autoshipNumber);
		cscockpitAutoshipSearchTabPage.clickSearchAutoshipButton();
		randomCustomerSequenceNumber = String.valueOf(cscockpitCustomerSearchTabPage.getRandomCustomerFromSearchResult());
		cscockpitCustomerSearchTabPage.clickAndReturnCIDNumberInCustomerSearchTab(randomCustomerSequenceNumber);
		s_assert.assertTrue(cscockpitAutoshipSearchTabPage.isAutoshipTemplateDisplayedInAutoshipTemplateTab(), "After click autoship template id user is not redirecting to autoship template page");
		//adding multiple notes
		for(int i=0;i<2;i++){
			cscockpitAutoshipTemplateTabPage.enterOrderNotesInCheckoutTab(TestConstants.ORDER_NOTE+randomNum+i);
			orderNotevalueFromUI = cscockpitAutoshipTemplateTabPage.getAddedNoteValueInCheckoutTab(TestConstants.ORDER_NOTE+randomNum+i);
			pstDate = cscockpitAutoshipTemplateTabPage.getPSTDate();
			orderDate = cscockpitAutoshipTemplateTabPage.converPSTDateToUIFormat(pstDate);
			s_assert.assertTrue(cscockpitAutoshipTemplateTabPage.convertUIDateFormatToPSTFormat(orderNotevalueFromUI.split("\\ ")[0]).trim().contains(cscockpitAutoshipTemplateTabPage.getPSTDate()) || cscockpitAutoshipTemplateTabPage.convertUIDateFormatToPSTFormat(orderNotevalueFromUI.split("\\ ")[0]).trim().contains(orderDate),"CSCockpit added order note date in checkout tab expected"+cscockpitAutoshipTemplateTabPage.getPSTDate()+"and on UI" +cscockpitAutoshipTemplateTabPage.convertUIDateFormatToPSTFormat(orderNotevalueFromUI.split("\\ ")[0]).trim());
			s_assert.assertTrue(orderNotevalueFromUI.contains("PM")||orderNotevalueFromUI.contains("AM"), "Added order note does not contain time zone");
		}
		driver.get(driver.getStoreFrontURL()+"/"+driver.getCountry());
		try{
			storeFrontConsultantPage = storeFrontHomePage.loginAsConsultant(consultantEmailID, password);
		}catch(Exception e){
			driver.get(driver.getStoreFrontURL()+"/"+driver.getCountry());
			storeFrontConsultantPage = storeFrontHomePage.loginAsConsultant(consultantEmailID, password);
		}
		storeFrontConsultantPage.clickOnAutoshipCart();
		s_assert.assertTrue(storeFrontUpdateCartPage.getQtyOfAddedProduct(SKUValue).contains(qtyOfProduct), "Expected No Of Quantity of Add product in store front is "+qtyOfProduct+" Actual on UI is "+storeFrontUpdateCartPage.getQtyOfAddedProduct(SKUValue));
		logout();
		s_assert.assertAll();
	}

	//Hybris Project-1704:To verify Cancel autoship button not displayed in the CRP Checkout page
	@Test
	public void testVerifyCancelAutoshipButtonNotDisplayedInCRPCheckoutPage_1704(){
		RFO_DB = driver.getDBNameRFO();
		String countryCode= driver.getCountry();
		String randomCustomerSequenceNumber = null;
		List<Map<String, Object>> randomConsultantList =  null;
		String consultantEmailID;
		String accountID;
		String country = null;
		if(driver.getCountry().equalsIgnoreCase("ca")){
			country= TestConstants.COUNTRY_DD_VALUE_CA;
		}else{
			country= TestConstants.COUNTRY_DD_VALUE_US;
		}
		storeFrontHomePage = new StoreFrontHomePage(driver);
		driver.get(driver.getStoreFrontURL()+"/"+driver.getCountry());
		while(true){
			randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_CONSULTANT_WITH_ORDERS_AND_AUTOSHIPS_RFO,countryId),RFO_DB);
			consultantEmailID = (String) getValueFromQueryResult(randomConsultantList, "UserName");
			accountID = String.valueOf(getValueFromQueryResult(randomConsultantList, "AccountID"));
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
			else{
				break;
			}
		}
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
		cscockpitCustomerSearchTabPage.selectCountryFromDropDownInCustomerSearchTab(country);
		cscockpitCustomerSearchTabPage.selectAccountStatusFromDropDownInCustomerSearchTab("Active");
		cscockpitCustomerSearchTabPage.enterEmailIdInSearchFieldInCustomerSearchTab(consultantEmailID);
		cscockpitCustomerSearchTabPage.clickSearchBtn();
		randomCustomerSequenceNumber = String.valueOf(cscockpitCustomerSearchTabPage.getRandomCustomerFromSearchResult());
		cscockpitCustomerSearchTabPage.clickAndReturnCIDNumberInCustomerSearchTab(randomCustomerSequenceNumber);
		//Verify Different Section on Customer tab page.
		s_assert.assertTrue(cscockpitCustomerTabPage.verifyAutoshipTemplateSectionInCustomerTab(),"AutoShip Template section is not on Customer Tab Page");
		s_assert.assertTrue(cscockpitCustomerTabPage.verifyCustomerOrderSectionInCustomerTab(),"Customer Order section is not on Customer Tab Page");
		s_assert.assertTrue(cscockpitCustomerTabPage.verifyCustomerBillingInfoSectionInCustomerTab(),"Customer Billing Info section is not on Customer Tab Page");
		s_assert.assertTrue(cscockpitCustomerTabPage.verifyCustomerAddressSectionInCustomerTab(),"Customer Address section is not on Customer Tab Page");
		cscockpitCustomerTabPage.getAndClickAutoshipIDHavingTypeAsCRPAutoshipAndStatusIsPending();
		cscockpitAutoshipTemplateTabPage.clickEditAutoshiptemplate();
		cscockpitAutoshipTemplateTabPage.clickAddMoreLinesLinkInAutoShipTemplateTab();
		cscockpitAutoshipCartTabPage.selectValueFromSortByDDInCartTab("Price: High to Low");
		cscockpitAutoshipCartTabPage.selectCatalogFromDropDownInCartTab();	
		String randomProductSequenceNumber = String.valueOf(cscockpitAutoshipCartTabPage.getRandomProductWithSKUFromSearchResult()); 
		String SKUValue = cscockpitAutoshipCartTabPage.getCustomerSKUValueInCartTab(randomProductSequenceNumber);
		cscockpitAutoshipCartTabPage.searchSKUValueInCartTab(SKUValue);
		SKUValue = cscockpitAutoshipCartTabPage.clickAddToCartBtnInCartTab(SKUValue);
		String qtyOfProduct =  cscockpitAutoshipCartTabPage.getQtyOfProductAddedInToCart(SKUValue);
		cscockpitAutoshipCartTabPage.clickCheckoutBtnInCartTab();
		s_assert.assertFalse(cscockpitAutoshipTemplateTabPage.verifyCancelAutoshipTemplateLinkInAutoshipTemplateTab(),"Cancel autoship link in Autoship template header section is present on Autoship template page for inactive user.");
		cscockpitAutoshipTemplateUpdateTabPage.clickAddNewPaymentAddressInCheckoutTab();
		cscockpitAutoshipTemplateUpdateTabPage.enterBillingInfo();
		cscockpitAutoshipTemplateUpdateTabPage.clickSaveAddNewPaymentProfilePopUP();
		cscockpitAutoshipTemplateUpdateTabPage.enterCVVValueInCheckoutTab(TestConstants.SECURITY_CODE);
		cscockpitAutoshipTemplateUpdateTabPage.clickUseThisCardBtnInCheckoutTab();
		cscockpitAutoshipTemplateUpdateTabPage.clickUpdateAutoshipTemplateInAutoshipTemplateUpdateTab();
		driver.get(driver.getStoreFrontURL()+"/"+countryCode);
		storeFrontConsultantPage = storeFrontHomePage.loginAsConsultant(consultantEmailID, password);
		storeFrontConsultantPage.clickOnAutoshipCart();
		s_assert.assertTrue(storeFrontUpdateCartPage.getQtyOfAddedProduct(SKUValue).contains(qtyOfProduct), "Expected No Of Quantity of Add product in store front is "+qtyOfProduct+" Actual on UI is "+storeFrontUpdateCartPage.getQtyOfAddedProduct(SKUValue));
		logout();
		s_assert.assertAll();
	}

	//Hybris Project-1709:To verify delay PC Perks
	@Test
	public void testVerifyDelayPCPerks_1709() throws InterruptedException{
		RFO_DB = driver.getDBNameRFO();
		String randomCustomerSequenceNumber = null;
		storeFrontHomePage = new StoreFrontHomePage(driver);

		String accountID;
		List<Map<String, Object>> randomPCList =  null;
		String pcEmailID = null;
		String country = null;
		if(driver.getCountry().equalsIgnoreCase("ca")){
			country= TestConstants.COUNTRY_DD_VALUE_CA;
		}else{
			country= TestConstants.COUNTRY_DD_VALUE_US;
		}
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
		List<Map<String, Object>> randomPCUsernameList =  null;
		randomPCUsernameList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_EMAIL_ID_FROM_ACCOUNT_ID,accountID),RFO_DB);
		pcEmailID = String.valueOf(getValueFromQueryResult(randomPCUsernameList, "EmailAddress"));  
		logger.info("emaild of consultant username "+pcEmailID); 
		driver.get(driver.getCSCockpitURL());  
		cscockpitCustomerSearchTabPage = cscockpitLoginPage.clickLoginBtn();
		cscockpitCustomerSearchTabPage.selectCustomerTypeFromDropDownInCustomerSearchTab("PC");
		cscockpitCustomerSearchTabPage.selectCountryFromDropDownInCustomerSearchTab(country);
		cscockpitCustomerSearchTabPage.selectAccountStatusFromDropDownInCustomerSearchTab("Active");
		cscockpitCustomerSearchTabPage.enterEmailIdInSearchFieldInCustomerSearchTab(pcEmailID);
		cscockpitCustomerSearchTabPage.clickSearchBtn();
		randomCustomerSequenceNumber = String.valueOf(cscockpitCustomerSearchTabPage.getRandomCustomerFromSearchResult());
		cscockpitCustomerSearchTabPage.clickAndReturnCIDNumberInCustomerSearchTab(randomCustomerSequenceNumber);
		cscockpitCustomerTabPage.getAndClickAutoshipIDHavingTypeAsPCAutoshipAndStatusIsPending();
		cscockpitAutoshipTemplateTabPage.clickDelayAutoshipLink();
		String noOfDays = "30";
		cscockpitAutoshipTemplateTabPage.selectNextDelayAutoshipDateInPopup(noOfDays);
		String delayAutoshipDate = cscockpitAutoshipTemplateTabPage.getNextDelayAutoshipDateFromPopup(noOfDays).split("\\(")[0].trim();
		cscockpitAutoshipTemplateTabPage.clickSaveBtnInPopUP();
		String delayAutoshipDateInMonthForm = cscockpitAutoshipTemplateTabPage.convertCartDateToFormat(delayAutoshipDate);

		cscockpitCustomerTabPage.clickCustomerTab();
		String nextDueDate = cscockpitCustomerTabPage.getNextDueDateOfAutoshipTemplate();
		String dueDateWithCompleteYear = nextDueDate.split("\\/")[0]+"/"+nextDueDate.split("\\/")[1]+"/20"+nextDueDate.split("\\/")[2];
		System.out.println("Complete date "+dueDateWithCompleteYear);
		s_assert.assertTrue(dueDateWithCompleteYear.trim().contains(delayAutoshipDate), "Expected delayed autoship date is "+delayAutoshipDate+" Actual On UI "+dueDateWithCompleteYear);

		driver.get(driver.getStoreFrontURL()+"/"+driver.getCountry());
		try{
			storeFrontPCUserPage = storeFrontHomePage.loginAsPCUser(pcEmailID, password);
		}catch(Exception e){
			driver.get(driver.getStoreFrontURL()+"/"+driver.getCountry());
			storeFrontPCUserPage = storeFrontHomePage.loginAsPCUser(pcEmailID, password);
		}
		storeFrontPCUserPage.clickOnWelcomeDropDown();
		storeFrontAccountInfoPage = storeFrontPCUserPage.clickAccountInfoLinkPresentOnWelcomeDropDown();
		storeFrontAccountInfoPage.clickOnYourAccountDropdown();
		storeFrontAccountInfoPage.clickOnPcPerksStatus();
		String dueDate = storeFrontAccountInfoPage.getNextDueDateOfCRPTemplate();
		s_assert.assertTrue(dueDate.trim().contains(delayAutoshipDateInMonthForm), "Expected delayed autoship date is "+delayAutoshipDateInMonthForm+" Actual On store front "+dueDate);
		logout();
		s_assert.assertAll();
	}

	//Hybris Project-1710:To verify PC perks Autoship template Page UI
	@Test
	public void testVerifyPCPerksAutoshipTemplatePageUI_1710(){
		RFO_DB = driver.getDBNameRFO();
		String randomCustomerSequenceNumber = null;
		String cid=null;
		String autoshipNumber=null;
		String orderSectionBasePrice="Base Price";
		String orderSectionAdjPrice="Adj Price";
		String orderSectionTotalCVPrice="Total CV";
		String orderSectionTotalQVPrice="Total QV";
		String orderSectionQuantity="Qty";
		String country = null;
		if(driver.getCountry().equalsIgnoreCase("ca")){
			country= TestConstants.COUNTRY_DD_VALUE_CA;
		}else{
			country= TestConstants.COUNTRY_DD_VALUE_US;
		}
		List<Map<String, Object>> randomPCList =  null;
		driver.get(driver.getStoreFrontURL()+"/"+driver.getCountry());
		storeFrontHomePage = new StoreFrontHomePage(driver);
		String pcEmailID = null;
		String accountID = null;
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
		logout();
		//get emailId of username
		List<Map<String, Object>> randomPCUsernameList =  null;
		randomPCUsernameList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_EMAIL_ID_FROM_ACCOUNT_ID,accountID),RFO_DB);
		pcEmailID = String.valueOf(getValueFromQueryResult(randomPCUsernameList, "EmailAddress"));  
		logger.info("emaild of consultant username "+pcEmailID);
		logger.info("login is successful");
		driver.get(driver.getCSCockpitURL());
		cscockpitCustomerSearchTabPage = cscockpitLoginPage.clickLoginBtn();
		cscockpitCustomerSearchTabPage.selectCustomerTypeFromDropDownInCustomerSearchTab("PC");
		cscockpitCustomerSearchTabPage.selectCountryFromDropDownInCustomerSearchTab(country);
		cscockpitCustomerSearchTabPage.selectAccountStatusFromDropDownInCustomerSearchTab("Active");
		cscockpitCustomerSearchTabPage.enterEmailIdInSearchFieldInCustomerSearchTab(pcEmailID);
		cscockpitCustomerSearchTabPage.clickSearchBtn();
		randomCustomerSequenceNumber = String.valueOf(cscockpitCustomerSearchTabPage.getRandomCustomerFromSearchResult());
		cid=cscockpitCustomerSearchTabPage.clickAndReturnCIDNumberInCustomerSearchTab(randomCustomerSequenceNumber);
		//Verify Different Section on Customer tab page.
		s_assert.assertTrue(cscockpitCustomerTabPage.verifyAutoshipTemplateSectionInCustomerTab(),"AutoShip Template section is not on Customer Tab Page");
		s_assert.assertTrue(cscockpitCustomerTabPage.verifyCustomerOrderSectionInCustomerTab(),"Customer Order section is not on Customer Tab Page");
		s_assert.assertTrue(cscockpitCustomerTabPage.verifyCustomerBillingInfoSectionInCustomerTab(),"Customer Billing Info section is not on Customer Tab Page");
		s_assert.assertTrue(cscockpitCustomerTabPage.verifyCustomerAddressSectionInCustomerTab(),"Customer Address section is not on Customer Tab Page");
		autoshipNumber=cscockpitCustomerTabPage.getAndClickAutoshipIDHavingTypeAsPCAutoshipAndStatusIsPending();
		//Verify Different section on autoShip template tab Page.
		s_assert.assertTrue(cscockpitAutoshipTemplateTabPage.verifyAutoshipTemplateHeaderSectionInAutoshipTemplateTab(),"Autoship template header section is not present on Autoship template page.");
		s_assert.assertTrue(cscockpitAutoshipTemplateTabPage.verifyAppliedPromotionSectionInAutoshipTemplateTab(),"Applied Promotion section is not present on Autoship template page.");
		s_assert.assertTrue(cscockpitAutoshipTemplateTabPage.verifyShippingAddressSectionInAutoshipTemplateTab(),"Shipping Address section is not present on Autoship template page.");
		s_assert.assertTrue(cscockpitAutoshipTemplateTabPage.verifyPaymentInfoSectionInAutoshipTemplateTab(),"Payment info section is not present on Autoship template page.");
		s_assert.assertTrue(cscockpitAutoshipTemplateTabPage.verifyOrderFromThisAutoshipTemplateSectionInAutoshipTemplateTab(),"Order from this autoship template section is not present on Autoship template page.");
		//Verify Sub components of autoship template section in autoship template tab page.
		s_assert.assertTrue(cscockpitAutoshipTemplateTabPage.getAutoshipTemplateCIDFromAutoshipTemplateSectionInAutoshipTemplateTab().contains(cid),"Autoship template header section cid Expected "+cid+"While on UI"+cscockpitAutoshipTemplateTabPage.getAutoshipTemplateCIDFromAutoshipTemplateSectionInAutoshipTemplateTab());
		s_assert.assertTrue(cscockpitAutoshipTemplateTabPage.getAutoshipTemplateCIDFromAutoshipTemplateSectionInAutoshipTemplateTab().contains(autoshipNumber),"Autoship template header section Autoship id expected "+autoshipNumber+"While on UI"+cscockpitAutoshipTemplateTabPage.getAutoshipTemplateCIDFromAutoshipTemplateSectionInAutoshipTemplateTab());
		s_assert.assertFalse(cscockpitAutoshipTemplateTabPage.verifyCancelAutoshipTemplateLinkInAutoshipTemplateTab(),"Cancel autoship link in Autoship template header section is not present on Autoship template page.");
		s_assert.assertTrue(cscockpitAutoshipTemplateTabPage.verifyEditAutoshipTemplateLinkInAutoshipTemplateTab(),"Edit autoship link in Autoship template header section is not present on Autoship template page.");
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
		String countryID = String.valueOf(getValueFromQueryResult(shippingAddressList, "CountryID"));
		if(countryID.equals("40")){
			countryID = "canada"; 
		}else if(countryID.equals("236")){
			countryID = "United States";
		}
		String shippingAddressFromDB = firstName.trim()+" "+lastName.trim()+"\n"+ addressLine1+"\n"+locale+", "+region+" "+postalCode+"\n"+countryID.toUpperCase();
		shippingAddressFromDB = shippingAddressFromDB.trim().toLowerCase();
		logger.info("created Shipping Address is "+shippingAddressFromDB);
		//Assert Shipping address details.
		s_assert.assertTrue(cscockpitAutoshipTemplateTabPage.getShippingAddressNameInAutoshipTemplateTab().contains(firstName.trim()+" "+lastName.trim()),"Shipping Address Name Expected is "+firstName.trim()+" "+lastName.trim()+" While on UI"+cscockpitAutoshipTemplateTabPage.getShippingAddressNameInAutoshipTemplateTab());
		s_assert.assertTrue(cscockpitAutoshipTemplateTabPage.getShippingAddressLine1InAutoshipTemplateTab().contains(addressLine1),"Shipping Address Line 1 Expected is "+addressLine1+" While on UI"+cscockpitAutoshipTemplateTabPage.getShippingAddressLine1InAutoshipTemplateTab());
		s_assert.assertTrue(cscockpitAutoshipTemplateTabPage.getShippingAddressLocaleRegionPostCodeInAutoshipTemplateTab().contains(locale),"Shipping Address Locale Expected is "+locale+" While on UI"+cscockpitAutoshipTemplateTabPage.getShippingAddressLocaleRegionPostCodeInAutoshipTemplateTab());
		s_assert.assertTrue(cscockpitAutoshipTemplateTabPage.getShippingAddressLocaleRegionPostCodeInAutoshipTemplateTab().contains(region),"Shipping Address Region Expected is "+region+" While on UI"+cscockpitAutoshipTemplateTabPage.getShippingAddressLocaleRegionPostCodeInAutoshipTemplateTab());
		s_assert.assertTrue(cscockpitAutoshipTemplateTabPage.getShippingAddressLocaleRegionPostCodeInAutoshipTemplateTab().contains(postalCode),"Shipping Address PostCode Expected is "+postalCode+" While on UI"+cscockpitAutoshipTemplateTabPage.getShippingAddressLocaleRegionPostCodeInAutoshipTemplateTab());
		s_assert.assertTrue(cscockpitAutoshipTemplateTabPage.getShippingAddressCountryInAutoshipTemplateTab().toLowerCase().contains(countryID.toLowerCase()),"Shipping Address Country Expected is "+countryID+" While on UI"+cscockpitAutoshipTemplateTabPage.getShippingAddressCountryInAutoshipTemplateTab());
		//Assert Billing address details.
		s_assert.assertTrue(cscockpitAutoshipTemplateTabPage.getPaymentAddressNameInAutoshipTemplateTab().toLowerCase().length()>0,"Payment Address Name Expected but on UI"+cscockpitAutoshipTemplateTabPage.getPaymentAddressNameInAutoshipTemplateTab());
		//verify components of order from AutoShip Template section
		s_assert.assertTrue(cscockpitAutoshipTemplateTabPage.getTextOfOrderFromAutoshipTemplateInAutoshipTemplateTab().contains("Number of Consecutive Autoship Orders From Template"),"Text Of order from autoship template Expected is= Number of Consecutive Autoship Orders From Template While on UI="+cscockpitAutoshipTemplateTabPage.getTextOfOrderFromAutoshipTemplateInAutoshipTemplateTab());
		//click customer search tab.
		cscockpitAutoshipTemplateTabPage.clickMenuButton();
		cscockpitAutoshipTemplateTabPage.clickLogoutButton();
		cscockpitCustomerSearchTabPage = cscockpitLoginPage.clickLoginBtn();
		cscockpitCustomerSearchTabPage.selectCustomerTypeFromDropDownInCustomerSearchTab("PC");
		cscockpitCustomerSearchTabPage.selectCountryFromDropDownInCustomerSearchTab(country);
		cscockpitCustomerSearchTabPage.selectAccountStatusFromDropDownInCustomerSearchTab("Inactive");
		cscockpitCustomerSearchTabPage.clickSearchBtn();
		String randomCustomerSequenceNumberOfInactiveUser = String.valueOf(cscockpitCustomerSearchTabPage.getRandomCustomerFromSearchResult());
		cscockpitCustomerSearchTabPage.clickAndReturnCIDNumberInCustomerSearchTab(randomCustomerSequenceNumberOfInactiveUser);
		if(cscockpitCustomerTabPage.isAutoshipTemplateHavingNoEntries()==false){
			cscockpitCustomerTabPage.getAndClickAutoshipIDHavingTypeAsPCAutoshipAndStatusIsCancelled();
			//verify cancel autoship and edit autoship link are not present for inactive user in autoship template header.
			s_assert.assertTrue(cscockpitAutoshipTemplateTabPage.verifyCancelAutoshipTemplateLinkInAutoshipTemplateTab(),"Cancel autoship link in Autoship template header section is present on Autoship template page for inactive user.");
			s_assert.assertFalse(cscockpitAutoshipTemplateTabPage.isEditTemplateBtnDisabled(),"Edit autoship link in Autoship template header section is present on Autoship template page for inactive user.");
			//verify Update and run now  link are not present or disabled for inactive user in order from autoship template section.
			s_assert.assertFalse(cscockpitAutoshipTemplateTabPage.verifyUpdateLinkInOrderFromAutoshipTemplateInAutoshipTemplateTab(),"Update link in order from Autoship template section is active  on Autoship template page for inactive user.");
			s_assert.assertFalse(cscockpitAutoshipTemplateTabPage.verifyRunNowLinkInOrderFromAutoshipTemplateInAutoshipTemplateTab(),"Run now  link in order from Autoship template section is active on Autoship template page for inactive user.");
		}
		s_assert.assertAll();
	}

	//Hybris Project-1711:To verify edit Pcperks Cart and Check for Threshold limit
	@Test
	public void testVerifyEditPCPerksCartAndCheckForThresholdLimit_1711(){
		RFO_DB = driver.getDBNameRFO();
		String randomCustomerSequenceNumber = null;
		List<Map<String, Object>> randomPCList =  null;
		storeFrontHomePage = new StoreFrontHomePage(driver);
		String pcEmailID = null;
		String accountID = null;
		String country = null;
		if(driver.getCountry().equalsIgnoreCase("ca")){
			country= TestConstants.COUNTRY_DD_VALUE_CA;
		}else{
			country= TestConstants.COUNTRY_DD_VALUE_US;
		}
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
		logout();
		//get emailId of username
		List<Map<String, Object>> randomPCUsernameList =  null;
		randomPCUsernameList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_EMAIL_ID_FROM_ACCOUNT_ID,accountID),RFO_DB);
		pcEmailID = String.valueOf(getValueFromQueryResult(randomPCUsernameList, "EmailAddress"));  
		logger.info("emaild of consultant username "+pcEmailID);
		logger.info("login is successful");
		driver.get(driver.getCSCockpitURL());
		cscockpitCustomerSearchTabPage = cscockpitLoginPage.clickLoginBtn();
		cscockpitCustomerSearchTabPage.selectCustomerTypeFromDropDownInCustomerSearchTab("PC");
		cscockpitCustomerSearchTabPage.selectCountryFromDropDownInCustomerSearchTab(country);
		cscockpitCustomerSearchTabPage.selectAccountStatusFromDropDownInCustomerSearchTab("Active");
		cscockpitCustomerSearchTabPage.enterEmailIdInSearchFieldInCustomerSearchTab(pcEmailID);
		cscockpitCustomerSearchTabPage.clickSearchBtn();
		randomCustomerSequenceNumber = String.valueOf(cscockpitCustomerSearchTabPage.getRandomCustomerFromSearchResult());
		cscockpitCustomerSearchTabPage.clickAndReturnCIDNumberInCustomerSearchTab(randomCustomerSequenceNumber);
		cscockpitCustomerTabPage.getAndClickAutoshipIDHavingTypeAsPCAutoshipAndStatusIsPending();
		cscockpitAutoshipTemplateTabPage.clickEditTemplateLinkInAutoshipTemplateTab();
		s_assert.assertTrue(cscockpitAutoshipTemplateTabPage.verifyCancelEditLinkInAutoshipTemplateTab(),"Cancel Edit link is not on Autoship template Tab Page");
		cscockpitAutoshipTemplateTabPage.clickRemoveLinkOfOrderDetailForOrderTotal();
		s_assert.assertTrue(cscockpitAutoshipTemplateTabPage.verifyThresholdPopupForOrderTotal(),"Threshold popup does not appear");
		cscockpitAutoshipTemplateTabPage.clickOKOfThresholdPopupInAutoshipTemplateTab();
		cscockpitAutoshipTemplateTabPage.enterQtyOfFirstProduct("10");
		cscockpitAutoshipTemplateTabPage.clickUpdateLinkOfOrderDetail();
		s_assert.assertFalse(cscockpitAutoshipTemplateTabPage.verifyThresholdPopupForOrderTotal(),"Threshold popup is present when quantity update");
		cscockpitAutoshipTemplateTabPage.clickAddMoreLinesLinkInAutoShipTemplateTab();
		cscockpitAutoshipCartTabPage.selectValueFromSortByDDInCartTab("Consultant Price: Low to High");
		cscockpitAutoshipCartTabPage.selectCatalogFromDropDownInCartTab();	
		String randomProductSequenceNumber = String.valueOf(cscockpitAutoshipCartTabPage.getRandomProductWithSKUFromSearchResult()); 
		String SKUValue = cscockpitAutoshipCartTabPage.getCustomerSKUValueInCartTab(randomProductSequenceNumber);
		cscockpitAutoshipCartTabPage.searchSKUValueInCartTab(SKUValue);
		SKUValue = cscockpitAutoshipCartTabPage.clickAddToCartBtnTillProductAddedInCartTab(SKUValue);
		String qtyOfProduct = cscockpitAutoshipCartTabPage.getQtyOfProductAddedInToCart(SKUValue);
		cscockpitAutoshipCartTabPage.clickCheckoutBtnInCartTab();
		cscockpitAutoshipTemplateUpdateTabPage.clickAddNewPaymentAddressInCheckoutTab();
		cscockpitAutoshipTemplateUpdateTabPage.enterBillingInfo();
		cscockpitAutoshipTemplateUpdateTabPage.clickSaveAddNewPaymentProfilePopUP();
		cscockpitAutoshipTemplateUpdateTabPage.enterCVVValueInCheckoutTab(TestConstants.SECURITY_CODE);
		cscockpitAutoshipTemplateUpdateTabPage.clickUseThisCardBtnInCheckoutTab();
		cscockpitAutoshipTemplateUpdateTabPage.clickUpdateAutoshipTemplateInAutoshipTemplateUpdateTab();
		driver.get(driver.getStoreFrontURL()+"/"+driver.getCountry());
		try{
			storeFrontPCUserPage = storeFrontHomePage.loginAsPCUser(pcEmailID, password);
		}catch(Exception e){
			driver.get(driver.getStoreFrontURL()+"/"+driver.getCountry());
			storeFrontPCUserPage = storeFrontHomePage.loginAsPCUser(pcEmailID, password);
		}
		storeFrontPCUserPage.clickOnAutoshipCart();
		s_assert.assertTrue(storeFrontUpdateCartPage.getQtyOfAddedProduct(SKUValue).contains(qtyOfProduct), "Expected No Of Quantity of Add product in store front is "+qtyOfProduct+" Actual on UI is "+storeFrontUpdateCartPage.getQtyOfAddedProduct(SKUValue));
		logout();
		s_assert.assertAll();
	}

	// Hybris Project-1713:To verify Cancel autoship button not displayed in the PC Checkout page
	@Test
	public void testVerifyCancelAutoshipButtonNotDisplayedInPCCheckoutPage_1713() throws InterruptedException{
		RFO_DB = driver.getDBNameRFO();
		int randomNum = CommonUtils.getRandomNum(10000, 1000000);
		int randomNumber = CommonUtils.getRandomNum(10000, 1000000);
		String randomCustomerSequenceNumber = null;
		List<Map<String, Object>> randomPCUserList =  null;
		List<Map<String, Object>> emailIdFromAccountIdList =  null;
		String pcUserEmailID=null;
		String accountId=null;
		String country = null;
		if(driver.getCountry().equalsIgnoreCase("ca")){
			country= TestConstants.COUNTRY_DD_VALUE_CA;
		}else{
			country= TestConstants.COUNTRY_DD_VALUE_US;
		}

		//-------------------FOR US----------------------------------
		driver.get(driver.getStoreFrontURL()+"/"+driver.getCountry());
		while(true){
			randomPCUserList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_PC_WITH_ORDERS_AND_AUTOSHIPS_RFO,countryId),RFO_DB);
			accountId = String.valueOf(getValueFromQueryResult(randomPCUserList, "AccountID"));
			emailIdFromAccountIdList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_EMAIL_ID_FROM_ACCOUNT_ID,accountId),RFO_DB);
			pcUserEmailID=(String) getValueFromQueryResult(emailIdFromAccountIdList, "EmailAddress"); 
			logger.info("Account Id of the user is "+accountId);
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
		logger.info("emaild of PC user "+pcUserEmailID);
		logger.info("login is successful");
		driver.get(driver.getCSCockpitURL());
		cscockpitCustomerSearchTabPage = cscockpitLoginPage.clickLoginBtn();
		cscockpitCustomerSearchTabPage.selectCustomerTypeFromDropDownInCustomerSearchTab("PC");
		cscockpitCustomerSearchTabPage.selectCountryFromDropDownInCustomerSearchTab(country);
		cscockpitCustomerSearchTabPage.selectAccountStatusFromDropDownInCustomerSearchTab("Active");
		cscockpitCustomerSearchTabPage.enterEmailIdInSearchFieldInCustomerSearchTab(pcUserEmailID);
		cscockpitCustomerSearchTabPage.clickSearchBtn();
		randomCustomerSequenceNumber = String.valueOf(cscockpitCustomerSearchTabPage.getRandomCustomerFromSearchResult());
		cscockpitCustomerSearchTabPage.clickAndReturnCIDNumberInCustomerSearchTab(randomCustomerSequenceNumber);
		//Verify Different Section on Customer tab page.
		s_assert.assertTrue(cscockpitCustomerTabPage.verifyAutoshipTemplateSectionInCustomerTab(),"AutoShip Template section is not on Customer Tab Page for US");
		s_assert.assertTrue(cscockpitCustomerTabPage.verifyCustomerOrderSectionInCustomerTab(),"Customer Order section is not on Customer Tab Page for US");
		s_assert.assertTrue(cscockpitCustomerTabPage.verifyCustomerBillingInfoSectionInCustomerTab(),"Customer Billing Info section is not on Customer Tab Page for US");
		s_assert.assertTrue(cscockpitCustomerTabPage.verifyCustomerAddressSectionInCustomerTab(),"Customer Address section is not on Customer Tab Page for US");
		cscockpitCustomerTabPage.getAndClickAutoshipIDHavingTypeAsPCAutoshipAndStatusIsPending();
		cscockpitAutoshipTemplateTabPage.clickEditAutoshiptemplate();
		cscockpitAutoshipTemplateTabPage.clickAddMoreLinesLinkInAutoShipTemplateTab();
		cscockpitAutoshipCartTabPage.selectValueFromSortByDDInCartTab("Price: High to Low");
		cscockpitAutoshipCartTabPage.selectCatalogFromDropDownInCartTab(); 
		String randomProductSequenceNumber = String.valueOf(cscockpitAutoshipCartTabPage.getRandomProductWithSKUFromSearchResult()); 
		String SKUValue = cscockpitAutoshipCartTabPage.getCustomerSKUValueInCartTab(randomProductSequenceNumber);
		cscockpitAutoshipCartTabPage.searchSKUValueInCartTab(SKUValue);
		SKUValue = cscockpitAutoshipCartTabPage.clickAddToCartBtnInCartTab(SKUValue);
		String qtyOfProduct =  cscockpitAutoshipCartTabPage.getQtyOfProductAddedInToCart(SKUValue);
		cscockpitAutoshipCartTabPage.clickCheckoutBtnInCartTab();
		s_assert.assertFalse(cscockpitAutoshipTemplateTabPage.verifyCancelAutoshipTemplateLinkInAutoshipTemplateTab(),"Cancel autoship link in Autoship template header section is present on Autoship template page for inactive user.");
		cscockpitAutoshipTemplateUpdateTabPage.clickAddNewPaymentAddressInCheckoutTab();
		cscockpitAutoshipTemplateUpdateTabPage.enterBillingInfo(TestConstants.CARD_NUMBER,TestConstants.NEW_BILLING_PROFILE_NAME+randomNum,TestConstants.SECURITY_CODE);
		cscockpitAutoshipTemplateUpdateTabPage.clickSaveAddNewPaymentProfilePopUP();
		cscockpitAutoshipTemplateUpdateTabPage.enterCVVValueInCheckoutTab(TestConstants.SECURITY_CODE);
		cscockpitAutoshipTemplateUpdateTabPage.clickUseThisCardBtnInCheckoutTab();
		cscockpitAutoshipTemplateUpdateTabPage.clickUpdateAutoshipTemplateInAutoshipTemplateUpdateTab();
		cscockpitAutoshipTemplateUpdateTabPage.clickMenuButton();
		cscockpitAutoshipTemplateUpdateTabPage.clickLogoutButton();
		driver.get(driver.getStoreFrontURL()+"/"+driver.getCountry());
		try{
			storeFrontPCUserPage = storeFrontHomePage.loginAsPCUser(pcUserEmailID, password);
		}catch(Exception e){
			driver.get(driver.getStoreFrontURL()+"/"+driver.getCountry());
			storeFrontPCUserPage = storeFrontHomePage.loginAsPCUser(pcUserEmailID, password);
		}
		storeFrontPCUserPage.clickOnWelcomeDropDown();
		storeFrontPCUserPage.clickEditCrpLinkPresentOnWelcomeDropDown();
		s_assert.assertTrue(storeFrontUpdateCartPage.getQtyOfAddedProduct(SKUValue).contains(qtyOfProduct), "Expected No Of Quantity of Add product in store front for US is "+qtyOfProduct+" Actual on UI for US is "+storeFrontUpdateCartPage.getQtyOfAddedProduct(SKUValue));
		logout();
		s_assert.assertAll();
	}

	//Hybris Project-1714:To verify cancel PC Perks
	@Test
	public void testVerifyCancelPCPerks_1714(){
		RFO_DB = driver.getDBNameRFO();
		int randomNum = CommonUtils.getRandomNum(10000, 1000000);
		int randomNumber = CommonUtils.getRandomNum(10000, 1000000);
		String randomCustomerSequenceNumber = null;
		List<Map<String, Object>> randomPCUserList =  null;
		List<Map<String, Object>> emailIdFromAccountIdList =  null;
		String pcUserEmailID=null;
		String accountId=null;
		String country = null;
		if(driver.getCountry().equalsIgnoreCase("ca")){
			country= TestConstants.COUNTRY_DD_VALUE_CA;
		}else{
			country= TestConstants.COUNTRY_DD_VALUE_US;
		}

		//-------------------FOR US----------------------------------
		driver.get(driver.getStoreFrontURL()+"/"+driver.getCountry());
		while(true){
			randomPCUserList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_PC_WITH_ORDERS_AND_AUTOSHIPS_RFO,countryId),RFO_DB);
			accountId = String.valueOf(getValueFromQueryResult(randomPCUserList, "AccountID"));
			emailIdFromAccountIdList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_EMAIL_ID_FROM_ACCOUNT_ID,accountId),RFO_DB);
			pcUserEmailID=(String) getValueFromQueryResult(emailIdFromAccountIdList, "EmailAddress"); 
			logger.info("Account Id of the user is "+accountId);
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
		logger.info("emaild of PC user "+pcUserEmailID);
		logger.info("login is successful");
		driver.get(driver.getCSCockpitURL());
		cscockpitCustomerSearchTabPage = cscockpitLoginPage.clickLoginBtn();
		cscockpitCustomerSearchTabPage.selectCustomerTypeFromDropDownInCustomerSearchTab("PC");
		cscockpitCustomerSearchTabPage.selectCountryFromDropDownInCustomerSearchTab(country);
		cscockpitCustomerSearchTabPage.selectAccountStatusFromDropDownInCustomerSearchTab("Active");
		cscockpitCustomerSearchTabPage.enterEmailIdInSearchFieldInCustomerSearchTab(pcUserEmailID);
		cscockpitCustomerSearchTabPage.clickSearchBtn();
		randomCustomerSequenceNumber = String.valueOf(cscockpitCustomerSearchTabPage.getRandomCustomerFromSearchResult());
		cscockpitCustomerSearchTabPage.clickAndReturnCIDNumberInCustomerSearchTab(randomCustomerSequenceNumber);
		//Verify Different Section on Customer tab page.
		s_assert.assertTrue(cscockpitCustomerTabPage.verifyAutoshipTemplateSectionInCustomerTab(),"AutoShip Template section is not on Customer Tab Page for US");
		s_assert.assertTrue(cscockpitCustomerTabPage.verifyCustomerOrderSectionInCustomerTab(),"Customer Order section is not on Customer Tab Page for US");
		s_assert.assertTrue(cscockpitCustomerTabPage.verifyCustomerBillingInfoSectionInCustomerTab(),"Customer Billing Info section is not on Customer Tab Page for US");
		s_assert.assertTrue(cscockpitCustomerTabPage.verifyCustomerAddressSectionInCustomerTab(),"Customer Address section is not on Customer Tab Page for US");
		String autoshipId=cscockpitCustomerTabPage.getAndClickAutoshipIDHavingTypeAsPCAutoshipAndStatusIsPending();
		cscockpitAutoshipTemplateTabPage.clickCancelAutoship();
		cscockpitAutoshipTemplateTabPage.selectPCPerksCancellationReasonFromDropDownInAutoShipTemplateTab();
		cscockpitAutoshipTemplateTabPage.selectPCPerksRequestSourceFromDropDownInAutoShipTemplateTab();
		cscockpitAutoshipTemplateTabPage.enterPCPerksCancellationMessage("Automation termination"+randomNum);
		cscockpitAutoshipTemplateTabPage.clickConfirmCancelAutoshipTemplatePopup();
		cscockpitAutoshipTemplateTabPage.clickCustomerTab();
		String statusAfterAutoshipCancelled=cscockpitCustomerTabPage.getStatusOfAutoShipIdFromAutoshipTemplate(autoshipId);
		s_assert.assertTrue(statusAfterAutoshipCancelled.equals("Cancelled"),"Autoship status after cancelled autoship is not cancelled for US");
		cscockpitCustomerTabPage.clickAutoshipIDHavingTypeAsPCAutoshipAndStatusAsCancelled(autoshipId);
		s_assert.assertTrue(cscockpitAutoshipTemplateTabPage.verifyCancelAutoshipTemplateLinkInAutoshipTemplateTab(),"Cancel autoship link is present after cancelling Autoship");
		cscockpitAutoshipTemplateTabPage.clickMenuButton();
		cscockpitAutoshipTemplateTabPage.clickLogoutButton();
		driver.get(driver.getStoreFrontURL()+"/"+driver.getCountry());
		try{
			storeFrontPCUserPage = storeFrontHomePage.loginAsPCUser(pcUserEmailID, password);
		}catch(Exception e){
			driver.get(driver.getStoreFrontURL()+"/"+driver.getCountry());
			storeFrontPCUserPage = storeFrontHomePage.loginAsPCUser(pcUserEmailID, password);
		}
		s_assert.assertTrue(driver.getCurrentUrl().contains("error"), "Login is successful with autoship cancelled PC user for US");
		s_assert.assertAll();
	}

	//Hybris Project-1742:To verify Add card functionality in the PCperks Edit Autoship template Page
	@Test
	public void testVerifyAddCardFunctionalityInThePCPerksEditAutoshipTemplatePage_1742(){
		RFO_DB = driver.getDBNameRFO();
		String randomCustomerSequenceNumber = null;
		List<Map<String, Object>> randomPCList =  null;
		List<Map<String, Object>> randomPCUsernameList =  null;
		storeFrontHomePage = new StoreFrontHomePage(driver);
		String pcEmailID = null;
		String accountID = null;
		int randomNum = CommonUtils.getRandomNum(10000, 1000000);
		int randomNumber = CommonUtils.getRandomNum(10000, 1000000);
		String attendentFirstName = TestConstants.FIRST_NAME+randomNum;
		String attendeeLastName = TestConstants.LAST_NAME;
		String city = null;
		String postal = null;
		String province = null;
		String phoneNumber = null;
		String country = null;
		String addressLine = null;
		if(driver.getCountry().equalsIgnoreCase("us")){
			addressLine = TestConstants.ADDRESS_LINE_1_US;
			city = TestConstants.CITY_US;
			postal = TestConstants.POSTAL_CODE_US;
			province = TestConstants.PROVINCE_ALABAMA_US;
			phoneNumber = TestConstants.PHONE_NUMBER_US;
			country =  TestConstants.COUNTRY_DD_VALUE_US;
		}else if(driver.getCountry().equalsIgnoreCase("ca")){
			addressLine = TestConstants.ADDRESS_LINE_1_CA;
			city = TestConstants.CITY_CA;
			postal = TestConstants.POSTAL_CODE_CA;
			province = TestConstants.PROVINCE_CA;
			phoneNumber = TestConstants.PHONE_NUMBER_CA;
			country =  TestConstants.COUNTRY_DD_VALUE_CA;
		}
		//-----------------------------------------------------
		driver.get(driver.getStoreFrontURL()+"/"+driver.getCountry());
		while(true){
			randomPCList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_PC_WITH_ORDERS_AND_AUTOSHIPS_RFO,countryId),RFO_DB); 
			accountID = String.valueOf(getValueFromQueryResult(randomPCList, "AccountID")); 
			randomPCUsernameList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_EMAIL_ID_FROM_ACCOUNT_ID,accountID),RFO_DB);
			pcEmailID = String.valueOf(getValueFromQueryResult(randomPCUsernameList, "EmailAddress"));  
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
		logout();
		logger.info("emaild of PC username "+pcEmailID);
		logger.info("login is successful");
		driver.get(driver.getCSCockpitURL());
		cscockpitCustomerSearchTabPage = cscockpitLoginPage.clickLoginBtn();
		cscockpitCustomerSearchTabPage.selectCustomerTypeFromDropDownInCustomerSearchTab("PC");
		cscockpitCustomerSearchTabPage.selectCountryFromDropDownInCustomerSearchTab(country);
		cscockpitCustomerSearchTabPage.selectAccountStatusFromDropDownInCustomerSearchTab("Active");
		cscockpitCustomerSearchTabPage.enterEmailIdInSearchFieldInCustomerSearchTab(pcEmailID);
		cscockpitCustomerSearchTabPage.clickSearchBtn();
		randomCustomerSequenceNumber = String.valueOf(cscockpitCustomerSearchTabPage.getRandomCustomerFromSearchResult());
		cscockpitCustomerSearchTabPage.clickAndReturnCIDNumberInCustomerSearchTab(randomCustomerSequenceNumber);
		cscockpitCustomerTabPage.getAndClickAutoshipIDHavingTypeAsPCAutoshipAndStatusIsPending();
		cscockpitAutoshipTemplateTabPage.clickEditTemplateLinkInAutoshipTemplateTab();
		cscockpitAutoshipTemplateTabPage.clickAddCardBtnInPaymentInfoSection();
		s_assert.assertTrue(cscockpitAutoshipTemplateTabPage.isAddNewPaymentProfilePopup(), "Add a new payment profile popup is not present");
		cscockpitAutoshipTemplateTabPage.clickCloseOfAddANewPaymentProfilePopup();
		cscockpitAutoshipTemplateTabPage.clickAddCardBtnInPaymentInfoSection();
		cscockpitAutoshipTemplateTabPage.clickSaveAddNewPaymentProfilePopUP();
		s_assert.assertTrue(cscockpitAutoshipTemplateTabPage.isReviewCreditCardDetailsErrorMsgPresent(), "Review credit card error msg is not present");
		String profileName = TestConstants.NEW_BILLING_PROFILE_NAME+randomNum;
		cscockpitAutoshipTemplateTabPage.enterBillingInfoWithoutSelectAddress(profileName);
		cscockpitAutoshipTemplateTabPage.clickSaveAddNewPaymentProfilePopUP();
		s_assert.assertTrue(cscockpitAutoshipTemplateTabPage.isSelectBillingAddressMsgPresent(), "select billing address error popup msg is not present");
		cscockpitAutoshipTemplateTabPage.selectBillingAddressInPaymentProfilePopup();
		cscockpitAutoshipTemplateTabPage.clickSaveAddNewPaymentProfilePopUP();
		s_assert.assertTrue(cscockpitAutoshipTemplateTabPage.getPaymentAddressNameInAutoshipTemplateTab().toLowerCase().contains(profileName.toLowerCase()),"Payment Address Name Expected"+profileName+" but on UI"+cscockpitAutoshipTemplateTabPage.getPaymentAddressNameInAutoshipTemplateTab());
		cscockpitAutoshipTemplateTabPage.clickAddCardBtnInPaymentInfoSection();
		cscockpitAutoshipTemplateTabPage.clickAddANewAddressOfAddANewPaymentProfilePopup();
		s_assert.assertTrue(cscockpitAutoshipTemplateTabPage.isAddNewPaymentProfilePopup(), "Add a new payment profile popup is not present");
		cscockpitAutoshipTemplateTabPage.clickCloseOfAddANewPaymentProfilePopup();
		cscockpitAutoshipTemplateTabPage.clickAddCardBtnInPaymentInfoSection();
		cscockpitAutoshipTemplateTabPage.clickSaveAddNewPaymentProfilePopUP();
		s_assert.assertTrue(cscockpitAutoshipTemplateTabPage.isReviewCreditCardDetailsErrorMsgPresent(), "Review credit card error msg is not present");
		profileName = TestConstants.NEW_BILLING_PROFILE_NAME+randomNumber;
		cscockpitAutoshipTemplateTabPage.enterBillingInfoWithoutEnterCreditCardAndAddress(profileName);
		cscockpitCustomerTabPage.enterShippingInfoInAddNewPaymentProfilePopupWithoutSaveBtn(attendentFirstName, attendeeLastName, addressLine, city, postal, country, province, phoneNumber);
		cscockpitAutoshipTemplateTabPage.clickSaveAddNewPaymentProfilePopUP();
		s_assert.assertTrue(cscockpitAutoshipTemplateTabPage.isReviewCreditCardDetailsErrorMsgPresent(), "Review credit card error msg is not present");
		cscockpitAutoshipTemplateTabPage.enterCreditCardNumberInPaymentProfilePopup();
		cscockpitAutoshipTemplateTabPage.clickSaveAddNewPaymentProfilePopUP();
		s_assert.assertTrue(cscockpitAutoshipTemplateTabPage.getDefaultSelectedPaymentInfoAddress().contains(profileName),"New profile name is not default selected in payment profile");
		s_assert.assertAll();

	}

	//Hybris Project-1712:To verify Update Pcperks autoship template
	@Test
	public void testToVerifyUpdatePCPerksAutoshipTemplate_1712(){
		RFO_DB = driver.getDBNameRFO();
		String randomCustomerSequenceNumber = null;
		List<Map<String, Object>> randomPCList =  null;
		storeFrontHomePage = new StoreFrontHomePage(driver);
		String pcEmailID = null;
		String accountID = null;
		String country = null;
		if(driver.getCountry().equalsIgnoreCase("ca")){
			country= TestConstants.COUNTRY_DD_VALUE_CA;
		}else{
			country= TestConstants.COUNTRY_DD_VALUE_US;
		}
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
		logout();
		//get emailId of username
		List<Map<String, Object>> randomPCUsernameList =  null;
		randomPCUsernameList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_EMAIL_ID_FROM_ACCOUNT_ID,accountID),RFO_DB);
		pcEmailID = String.valueOf(getValueFromQueryResult(randomPCUsernameList, "EmailAddress"));  
		logger.info("emaild of consultant username "+pcEmailID);
		logger.info("login is successful");
		driver.get(driver.getCSCockpitURL());
		cscockpitCustomerSearchTabPage = cscockpitLoginPage.clickLoginBtn();
		cscockpitCustomerSearchTabPage.selectCustomerTypeFromDropDownInCustomerSearchTab("PC");
		cscockpitCustomerSearchTabPage.selectCountryFromDropDownInCustomerSearchTab(country);
		cscockpitCustomerSearchTabPage.selectAccountStatusFromDropDownInCustomerSearchTab("Active");
		cscockpitCustomerSearchTabPage.enterEmailIdInSearchFieldInCustomerSearchTab(pcEmailID);
		cscockpitCustomerSearchTabPage.clickSearchBtn();
		randomCustomerSequenceNumber = String.valueOf(cscockpitCustomerSearchTabPage.getRandomCustomerFromSearchResult());
		cscockpitCustomerSearchTabPage.clickAndReturnCIDNumberInCustomerSearchTab(randomCustomerSequenceNumber);
		cscockpitCustomerTabPage.getAndClickAutoshipIDHavingTypeAsPCAutoshipAndStatusIsPending();
		cscockpitAutoshipTemplateTabPage.clickEditTemplateLinkInAutoshipTemplateTab();

		cscockpitAutoshipTemplateTabPage.clickAddMoreLinesLinkInAutoShipTemplateTab();
		cscockpitAutoshipCartTabPage.selectValueFromSortByDDInCartTab("Price: High to Low");
		cscockpitAutoshipCartTabPage.selectCatalogFromDropDownInCartTab();	
		String randomProductSequenceNumber = String.valueOf(cscockpitAutoshipCartTabPage.getRandomProductWithSKUFromSearchResult()); 
		String SKUValue = cscockpitAutoshipCartTabPage.getCustomerSKUValueInCartTab(randomProductSequenceNumber);
		cscockpitAutoshipCartTabPage.searchSKUValueInCartTab(SKUValue);
		SKUValue = cscockpitAutoshipCartTabPage.clickAddToCartBtnTillProductAddedInCartTab(SKUValue);
		String qtyOfProduct = cscockpitAutoshipCartTabPage.getQtyOfProductAddedInToCart(SKUValue);
		cscockpitAutoshipCartTabPage.clickCheckoutBtnInCartTab();
		cscockpitAutoshipTemplateUpdateTabPage.clickAddNewPaymentAddressInCheckoutTab();
		cscockpitAutoshipTemplateUpdateTabPage.enterBillingInfo();
		cscockpitAutoshipTemplateUpdateTabPage.clickSaveAddNewPaymentProfilePopUP();
		cscockpitAutoshipTemplateUpdateTabPage.enterCVVValueInCheckoutTab(TestConstants.SECURITY_CODE);
		cscockpitAutoshipTemplateUpdateTabPage.clickUseThisCardBtnInCheckoutTab();
		cscockpitAutoshipTemplateUpdateTabPage.clickUpdateAutoshipTemplateInAutoshipTemplateUpdateTab();
		driver.get(driver.getStoreFrontURL()+"/"+driver.getCountry());
		try{
			storeFrontPCUserPage = storeFrontHomePage.loginAsPCUser(pcEmailID, password);
		}catch(Exception e){
			driver.get(driver.getStoreFrontURL()+"/"+driver.getCountry());
			storeFrontPCUserPage = storeFrontHomePage.loginAsPCUser(pcEmailID, password);
		}
		storeFrontPCUserPage.clickOnAutoshipCart();
		s_assert.assertTrue(storeFrontUpdateCartPage.getQtyOfAddedProduct(SKUValue).contains(qtyOfProduct), "Expected No Of Quantity of Add product in store front is "+qtyOfProduct+" Actual on UI is "+storeFrontUpdateCartPage.getQtyOfAddedProduct(SKUValue));
		logout();
		s_assert.assertAll();
	}

	//Hybris Project-1707:To verify cancel CRP
	@Test
	public void testToVerifyCancelCRP_1707() throws InterruptedException{
		String randomOrderSequenceNumber = null;
		RFO_DB = driver.getDBNameRFO();
		String consultantEmailID = null;
		String accountID = null;
		String country = null;
		if(driver.getCountry().equalsIgnoreCase("ca")){
			country= TestConstants.COUNTRY_DD_VALUE_CA;
		}else{
			country= TestConstants.COUNTRY_DD_VALUE_US;
		}
		cscockpitLoginPage = new CSCockpitLoginPage(driver);

		List<Map<String, Object>> randomConsultantList =  null;
		List<Map<String, Object>> randomConsultantUsernameList =  null;
		driver.get(driver.getStoreFrontURL()+"/"+driver.getCountry());
		storeFrontHomePage = new StoreFrontHomePage(driver);
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

		logger.info("emaild of consultant username "+consultantEmailID);
		logger.info("login is successful");
		driver.get(driver.getCSCockpitURL());
		cscockpitCustomerSearchTabPage = cscockpitLoginPage.clickLoginBtn();
		cscockpitCustomerSearchTabPage.selectCustomerTypeFromDropDownInCustomerSearchTab("CONSULTANT");
		cscockpitCustomerSearchTabPage.selectCountryFromDropDownInCustomerSearchTab(country);
		cscockpitCustomerSearchTabPage.selectAccountStatusFromDropDownInCustomerSearchTab("Active");
		cscockpitCustomerSearchTabPage.enterEmailIdInSearchFieldInCustomerSearchTab(consultantEmailID);
		cscockpitCustomerSearchTabPage.clickSearchBtn();
		randomOrderSequenceNumber = String.valueOf(cscockpitOrderSearchTabPage.getRandomOrdersFromOrderResultSearchFirstPageInOrderSearchTab());
		cscockpitOrderSearchTabPage.clickOrderNumberInOrderSearchResultsInOrderSearchTab(randomOrderSequenceNumber);
		String clickedAutoshipTemplateId = cscockpitCustomerTabPage.getAndClickAutoshipIDHavingTypeAsCRPAutoshipAndStatusIsPending();
		cscockpitAutoshipTemplateTabPage.clickCancelAutoship();
		cscockpitAutoshipTemplateTabPage.clickCancelButtonOfCancelAutoshipTemplatePopup();
		cscockpitAutoshipTemplateTabPage.clickCancelAutoship();
		cscockpitAutoshipTemplateTabPage.clickConfirmCancelAutoshipTemplatePopup();
		cscockpitAutoshipTemplateTabPage.clickCustomerTab();
		cscockpitCustomerTabPage.clickAutoshipTemplateIdViaUsingId(clickedAutoshipTemplateId);
		s_assert.assertTrue(cscockpitAutoshipTemplateTabPage.verifyCancelAutoshipTemplateLinkInAutoshipTemplateTab(), "Cancel Autoship Button is Present/Visible and not Disabled");
		s_assert.assertTrue(cscockpitAutoshipTemplateTabPage.isEditTemplateInAutoshipTemplateTabPresent(), "Edit Template Button is Present/Visible and not Disabled");
		driver.get(driver.getStoreFrontURL()+"/"+driver.getCountry());
		try{
			storeFrontConsultantPage = storeFrontHomePage.loginAsConsultant(consultantEmailID, password);
		}catch(Exception e){
			driver.get(driver.getStoreFrontURL()+"/"+driver.getCountry());
			storeFrontConsultantPage = storeFrontHomePage.loginAsConsultant(consultantEmailID, password);
		}
		storeFrontConsultantPage.clickOnWelcomeDropDown();
		storeFrontConsultantPage.clickAccountInfoLinkPresentOnWelcomeDropDown();
		storeFrontAccountInfoPage.clickOnYourAccountDropdown();
		storeFrontAccountInfoPage.clickOnAutoShipStatus();
		s_assert.assertTrue(storeFrontAccountInfoPage.verifyCRPCancelled(), "Enroll In CRP Button is not Present/Cancelled");
		s_assert.assertAll();
	}

	//Hybris Project-1706:To verify CRP Run autoship
	@Test
	public void testToVerifyCRPRunAutoship_1706(){
		String randomOrderSequenceNumber = null;
		RFO_DB = driver.getDBNameRFO();
		String consultantEmailID = null;
		String accountID = null;
		String country = null;
		if(driver.getCountry().equalsIgnoreCase("ca")){
			country= TestConstants.COUNTRY_DD_VALUE_CA;
		}else{
			country= TestConstants.COUNTRY_DD_VALUE_US;
		}
		cscockpitLoginPage = new CSCockpitLoginPage(driver);

		List<Map<String, Object>> randomConsultantList =  null;
		List<Map<String, Object>> randomConsultantUsernameList =  null;
		driver.get(driver.getStoreFrontURL()+"/"+driver.getCountry());
		storeFrontHomePage = new StoreFrontHomePage(driver);
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

		logger.info("emaild of consultant username "+consultantEmailID);
		logger.info("login is successful");
		driver.get(driver.getCSCockpitURL());
		cscockpitCustomerSearchTabPage = cscockpitLoginPage.clickLoginBtn();
		cscockpitCustomerSearchTabPage.selectCustomerTypeFromDropDownInCustomerSearchTab("CONSULTANT");
		cscockpitCustomerSearchTabPage.selectCountryFromDropDownInCustomerSearchTab(country);
		cscockpitCustomerSearchTabPage.selectAccountStatusFromDropDownInCustomerSearchTab("Active");
		cscockpitCustomerSearchTabPage.enterEmailIdInSearchFieldInCustomerSearchTab(consultantEmailID);
		cscockpitCustomerSearchTabPage.clickSearchBtn();
		randomOrderSequenceNumber = String.valueOf(cscockpitOrderSearchTabPage.getRandomOrdersFromOrderResultSearchFirstPageInOrderSearchTab());
		cscockpitOrderSearchTabPage.clickOrderNumberInOrderSearchResultsInOrderSearchTab(randomOrderSequenceNumber);
		cscockpitCustomerTabPage.getAndClickAutoshipIDHavingTypeAsCRPAutoshipAndStatusIsPending();
		cscockpitAutoshipTemplateTabPage.clickEditAutoshiptemplate();
		int numberOfConsecutiveAutoshipTemplateBeforeRunNow = cscockpitAutoshipTemplateTabPage.getNumberOfConsecutiveAutoshipOrdersFromTemplate();
		cscockpitAutoshipTemplateTabPage.clickRunNowButtonOnAutoshipTemplateTab();
		cscockpitAutoshipTemplateTabPage.clickOkConfirmMessagePopUp();
		int numberOfConsecutiveAutoshipTemplateAfterRunNow = cscockpitAutoshipTemplateTabPage.getNumberOfConsecutiveAutoshipOrdersFromTemplate();
		s_assert.assertTrue(numberOfConsecutiveAutoshipTemplateBeforeRunNow+1==numberOfConsecutiveAutoshipTemplateAfterRunNow, "Autoship Order not placed, Expected NumberOfConsecutiveAutoshipTemplateAfterRunNow :"+ numberOfConsecutiveAutoshipTemplateBeforeRunNow+1+"& Actual Number Of Consecutive Autoship Template After Run Now :"+ numberOfConsecutiveAutoshipTemplateAfterRunNow);
		s_assert.assertAll();

	}

	//Hybris Project-1724:To verify the View order page UI from the find autoship search results page
	@Test
	public void testVerifyViewOrderPageUIFromTheFindAutoshipSearchResultsPage_1724(){
		String orderID = "Order ID";
		String orderDate = "Order Date";
		String grandTotal = "Grand Total";
		String detail = "Detail";
		cscockpitLoginPage = new CSCockpitLoginPage(driver);
		driver.get(driver.getCSCockpitURL());
		cscockpitCustomerSearchTabPage = cscockpitLoginPage.clickLoginBtn();
		cscockpitCustomerSearchTabPage.clickFindAutoshipInLeftNavigation();
		s_assert.assertTrue(cscockpitAutoshipSearchTabPage.verifySearchByFieldPresentOnAutoshipSearch(),"Search By field is not present On autoship Search page");
		cscockpitAutoshipSearchTabPage.clickSearchAutoshipButton();
		String randomCustomerSequenceNumber = String.valueOf(cscockpitCustomerSearchTabPage.getRandomCustomerFromSearchResult());
		cscockpitCustomerSearchTabPage.clickViewOrderBtn(randomCustomerSequenceNumber);
		s_assert.assertTrue(cscockpitAutoshipSearchTabPage.isViewOrderPopupPresent(),"View Order popup is not present");
		s_assert.assertTrue(cscockpitAutoshipSearchTabPage.verifyViewOrderPopupValues(orderID), "Order Id column is not present in view order detail popup");
		s_assert.assertTrue(cscockpitAutoshipSearchTabPage.verifyViewOrderPopupValues(orderDate), "Order date column is not present in view order detail popup");
		s_assert.assertTrue(cscockpitAutoshipSearchTabPage.verifyViewOrderPopupValues(grandTotal), "Grand total column is not present in view order detail popup");
		s_assert.assertTrue(cscockpitAutoshipSearchTabPage.verifyViewOrderPopupValues(detail), "Detail column is not present in view order detail popup");
		cscockpitAutoshipSearchTabPage.clickCloseOfViewOrderDetailPopup();
		s_assert.assertFalse(cscockpitAutoshipSearchTabPage.isViewOrderPopupPresent(),"View Order popup is present");
		cscockpitCustomerSearchTabPage.clickViewOrderBtn(randomCustomerSequenceNumber);
		s_assert.assertTrue(cscockpitAutoshipSearchTabPage.isViewOrderPopupPresent(),"View Order popup is not present");
		s_assert.assertAll();
	}

	//Hybris Project-1736:To verify create payment address functionality in the CRP Checkout Page
	@Test
	public void testVerifyCreatePaymentAddressFunctionalityInCRPCheckoutPage_1736() throws InterruptedException{
		int randomNum1 = CommonUtils.getRandomNum(10000, 1000000);
		int randomNum = CommonUtils.getRandomNum(10000, 1000000);
		RFO_DB = driver.getDBNameRFO();
		String attendentFirstName = TestConstants.FIRST_NAME+randomNum1;
		String attendeeLastName = TestConstants.LAST_NAME;
		String city = null;
		String postal = null;
		String province = null;
		String phoneNumber = null;
		String addressLine = null;
		String randomCustomerSequenceNumber = null;
		String consultantEmailID;
		String accountID;
		String country = null;
		if(driver.getCountry().equalsIgnoreCase("ca")){
			country= TestConstants.COUNTRY_DD_VALUE_CA;
			addressLine = TestConstants.ADDRESS_LINE_1_CA;
			city = TestConstants.CITY_CA;
			postal = TestConstants.POSTAL_CODE_CA;
			province = TestConstants.PROVINCE_ALBERTA;
			phoneNumber = TestConstants.PHONE_NUMBER_CA;
		}else{
			country= TestConstants.COUNTRY_DD_VALUE_US;
			addressLine = TestConstants.ADDRESS_LINE_1_US;
			city = TestConstants.CITY_US;
			postal = TestConstants.POSTAL_CODE_US;
			province = TestConstants.PROVINCE_ALABAMA_US;
			phoneNumber = TestConstants.PHONE_NUMBER_US;
		}
		storeFrontHomePage = new StoreFrontHomePage(driver);
		driver.get(driver.getStoreFrontURL()+"/"+driver.getCountry());
		List<Map<String, Object>> randomConsultantList = null;
		while(true){
			randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_CONSULTANT_WITH_ORDERS_AND_AUTOSHIPS_RFO,countryId),RFO_DB);
			consultantEmailID = (String) getValueFromQueryResult(randomConsultantList, "UserName");
			accountID = String.valueOf(getValueFromQueryResult(randomConsultantList, "AccountID"));
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
			else{
				break;
			}
		}
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
		cscockpitCustomerSearchTabPage.selectCountryFromDropDownInCustomerSearchTab(country);
		cscockpitCustomerSearchTabPage.selectAccountStatusFromDropDownInCustomerSearchTab("Active");
		cscockpitCustomerSearchTabPage.enterEmailIdInSearchFieldInCustomerSearchTab(consultantEmailID);
		cscockpitCustomerSearchTabPage.clickSearchBtn();
		randomCustomerSequenceNumber = String.valueOf(cscockpitCustomerSearchTabPage.getRandomCustomerFromSearchResult());
		cscockpitCustomerSearchTabPage.clickAndReturnCIDNumberInCustomerSearchTab(randomCustomerSequenceNumber);
		cscockpitCustomerTabPage.getAndClickAutoshipIDHavingTypeAsCRPAutoshipAndStatusIsPending();
		cscockpitAutoshipTemplateTabPage.clickEditAutoshiptemplate();
		cscockpitAutoshipTemplateTabPage.clickAddMoreLinesLinkInAutoShipTemplateTab();
		cscockpitAutoshipCartTabPage.selectValueFromSortByDDInCartTab("Price: High to Low");
		cscockpitAutoshipCartTabPage.selectCatalogFromDropDownInCartTab();	
		String randomProductSequenceNumber = String.valueOf(cscockpitAutoshipCartTabPage.getRandomProductWithSKUFromSearchResult()); 
		String SKUValue = cscockpitAutoshipCartTabPage.getCustomerSKUValueInCartTab(randomProductSequenceNumber);
		cscockpitAutoshipCartTabPage.searchSKUValueInCartTab(SKUValue);
		cscockpitAutoshipCartTabPage.clickAddToCartBtnInCartTab(SKUValue);
		cscockpitAutoshipCartTabPage.clickCheckoutBtnInCartTab();
		cscockpitAutoshipTemplateUpdateTabPage.clickAddNewPaymentAddressInCheckoutTab();
		s_assert.assertTrue(cscockpitAutoshipTemplateUpdateTabPage.isAddNewPaymentProfilePopup(), "Add a new payment profile popup is not present");
		cscockpitAutoshipTemplateUpdateTabPage.clickCloseOfAddANewPaymentProfilePopup();
		cscockpitAutoshipTemplateUpdateTabPage.clickAddNewPaymentAddressInCheckoutTab();
		cscockpitAutoshipTemplateUpdateTabPage.clickSaveAddNewPaymentProfilePopUP();
		s_assert.assertTrue(cscockpitAutoshipTemplateUpdateTabPage.isReviewCreditCardDetailsErrorMsgPresent(), "Review credit card error msg is not present");
		String profileName = TestConstants.NEW_BILLING_PROFILE_NAME+randomNum;
		cscockpitAutoshipTemplateUpdateTabPage.enterBillingInfoWithoutEnterCreditCardAndAddress(profileName);
		cscockpitAutoshipTemplateUpdateTabPage.clickAddANewAddressOfAddANewPaymentProfilePopup();
		cscockpitAutoshipTemplateUpdateTabPage.enterShippingInfoInAddNewPaymentProfilePopupWithoutSaveBtn(attendentFirstName, attendeeLastName, addressLine, city, postal, country, province, phoneNumber);
		cscockpitAutoshipTemplateUpdateTabPage.clickSaveAddNewPaymentProfilePopUP();
		s_assert.assertTrue(cscockpitAutoshipTemplateUpdateTabPage.isReviewCreditCardDetailsErrorMsgPresent(), "Review credit card error msg is not present");
		cscockpitAutoshipTemplateUpdateTabPage.enterCreditCardNumberInPaymentProfilePopup();
		cscockpitAutoshipTemplateUpdateTabPage.enterCVVNumberInPaymentProfilePopup();
		cscockpitAutoshipTemplateUpdateTabPage.clickSaveAddNewPaymentProfilePopUP();
		cscockpitAutoshipTemplateUpdateTabPage.enterCVVValueInCheckoutTab(TestConstants.SECURITY_CODE);
		cscockpitAutoshipTemplateUpdateTabPage.clickUseThisCardBtnInCheckoutTab();
		cscockpitAutoshipTemplateUpdateTabPage.clickUpdateAutoshipTemplateInAutoshipTemplateUpdateTab();
		driver.get(driver.getStoreFrontURL()+"/"+driver.getCountry());
		try{
			storeFrontConsultantPage = storeFrontHomePage.loginAsConsultant(consultantEmailID, password);
		}catch(Exception e){
			driver.get(driver.getStoreFrontURL()+"/"+driver.getCountry());
			storeFrontConsultantPage = storeFrontHomePage.loginAsConsultant(consultantEmailID, password);
		}
		storeFrontConsultantPage.clickOnWelcomeDropDown();
		StoreFrontBillingInfoPage storeFrontBillingInfoPage = storeFrontConsultantPage.clickBillingInfoLinkPresentOnWelcomeDropDown();
		s_assert.assertTrue(storeFrontHomePage.isTheBillingAddressPresentOnPage(profileName),"Newly added billing profile is not present");
		logout();
		s_assert.assertAll();
	}


	//Hybris Project-1741:To verify Add card functionality in the CRP Edit Autoship template Page
	@Test
	public void testVerifyAddCardFunctionalityInCRPEditAutoshipTemplate_1741(){
		RFO_DB = driver.getDBNameRFO();
		String randomCustomerSequenceNumber = null;
		int randomNum = CommonUtils.getRandomNum(10000, 1000000);
		int randomNum2 = CommonUtils.getRandomNum(10000, 1000000);
		String attendentFirstName = TestConstants.FIRST_NAME+randomNum;
		String attendeeLastName = TestConstants.LAST_NAME;
		String accountID = null;
		String consultantEmailID;
		String city = null;
		String postal = null;
		String province = null;
		String phoneNumber = null;
		String country = null;
		String addressLine = null;
		if(driver.getCountry().equalsIgnoreCase("ca")){
			country= TestConstants.COUNTRY_DD_VALUE_CA;
			addressLine = TestConstants.ADDRESS_LINE_1_CA;
			city = TestConstants.CITY_CA;
			postal = TestConstants.POSTAL_CODE_CA;
			province = TestConstants.PROVINCE_ALBERTA;
			phoneNumber = TestConstants.PHONE_NUMBER_CA;
		}else{
			country= TestConstants.COUNTRY_DD_VALUE_US;
			addressLine = TestConstants.ADDRESS_LINE_1_US;
			city = TestConstants.CITY_US;
			postal = TestConstants.POSTAL_CODE_US;
			province = TestConstants.PROVINCE_ALABAMA_US;
			phoneNumber = TestConstants.PHONE_NUMBER_US;
		}
		storeFrontHomePage = new StoreFrontHomePage(driver);
		driver.get(driver.getStoreFrontURL()+"/"+driver.getCountry());
		List<Map<String, Object>> randomConsultantList = null;
		while(true){
			randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_CONSULTANT_WITH_ORDERS_AND_AUTOSHIPS_RFO,countryId),RFO_DB);
			consultantEmailID = (String) getValueFromQueryResult(randomConsultantList, "UserName");
			accountID = String.valueOf(getValueFromQueryResult(randomConsultantList, "AccountID"));
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
			else{
				break;
			}
		}
		logout();
		//get emailId of username
		List<Map<String, Object>> randomConsultantUsernameList =  null;
		randomConsultantUsernameList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_EMAIL_ID_FROM_ACCOUNT_ID,accountID),RFO_DB);
		consultantEmailID = String.valueOf(getValueFromQueryResult(randomConsultantUsernameList, "EmailAddress"));  
		logger.info("emaild of consultant username "+consultantEmailID);


		driver.get(driver.getCSCockpitURL());
		cscockpitCustomerSearchTabPage = cscockpitLoginPage.clickLoginBtn();
		cscockpitCustomerSearchTabPage.selectCustomerTypeFromDropDownInCustomerSearchTab("CONSULTANT");
		cscockpitCustomerSearchTabPage.selectCountryFromDropDownInCustomerSearchTab(country);
		cscockpitCustomerSearchTabPage.selectAccountStatusFromDropDownInCustomerSearchTab("Active");
		cscockpitCustomerSearchTabPage.enterEmailIdInSearchFieldInCustomerSearchTab(consultantEmailID);
		cscockpitCustomerSearchTabPage.clickSearchBtn();
		randomCustomerSequenceNumber = String.valueOf(cscockpitCustomerSearchTabPage.getRandomCustomerFromSearchResult());
		cscockpitCustomerSearchTabPage.clickAndReturnCIDNumberInCustomerSearchTab(randomCustomerSequenceNumber);
		cscockpitCustomerTabPage.getAndClickAutoshipIDHavingTypeAsCRPAutoshipAndStatusIsPending();
		cscockpitAutoshipTemplateTabPage.clickEditTemplateLinkInAutoshipTemplateTab();
		cscockpitAutoshipTemplateTabPage.clickAddCardBtnInPaymentInfoSection();
		s_assert.assertTrue(cscockpitAutoshipTemplateTabPage.isAddNewPaymentProfilePopup(), "Add a new payment profile popup is not present");
		cscockpitAutoshipTemplateTabPage.clickCloseOfAddANewPaymentProfilePopup();
		cscockpitAutoshipTemplateTabPage.clickAddCardBtnInPaymentInfoSection();
		cscockpitAutoshipTemplateTabPage.clickSaveAddNewPaymentProfilePopUP();
		s_assert.assertTrue(cscockpitAutoshipTemplateTabPage.isReviewCreditCardDetailsErrorMsgPresent(), "Review credit card error msg is not present");
		String profileName = TestConstants.NEW_BILLING_PROFILE_NAME+randomNum;
		cscockpitAutoshipTemplateTabPage.enterBillingInfoWithoutSelectAddress(profileName);
		cscockpitAutoshipTemplateTabPage.clickSaveAddNewPaymentProfilePopUP();
		s_assert.assertTrue(cscockpitAutoshipTemplateTabPage.isSelectBillingAddressMsgPresent(), "select billing address error popup msg is not present");
		cscockpitAutoshipTemplateTabPage.selectBillingAddressInPaymentProfilePopup();
		cscockpitAutoshipTemplateTabPage.clickSaveAddNewPaymentProfilePopUP();
		s_assert.assertTrue(cscockpitAutoshipTemplateTabPage.getPaymentAddressNameInAutoshipTemplateTab().toLowerCase().trim().contains(profileName.toLowerCase().trim()),"Payment Address Name Expected"+profileName+" but on UI"+cscockpitAutoshipTemplateTabPage.getPaymentAddressNameInAutoshipTemplateTab());
		cscockpitAutoshipTemplateTabPage.clickAddCardBtnInPaymentInfoSection();
		cscockpitAutoshipTemplateTabPage.clickAddANewAddressOfAddANewPaymentProfilePopup();
		s_assert.assertTrue(cscockpitAutoshipTemplateTabPage.isAddNewPaymentProfilePopup(), "Add a new payment profile popup is not present");
		cscockpitAutoshipTemplateTabPage.clickCloseOfAddANewPaymentProfilePopup();
		cscockpitAutoshipTemplateTabPage.clickAddCardBtnInPaymentInfoSection();
		cscockpitAutoshipTemplateTabPage.clickSaveAddNewPaymentProfilePopUP();
		s_assert.assertTrue(cscockpitAutoshipTemplateTabPage.isReviewCreditCardDetailsErrorMsgPresent(), "Review credit card error msg is not present");
		profileName = TestConstants.NEW_BILLING_PROFILE_NAME+randomNum2;
		cscockpitAutoshipTemplateTabPage.enterBillingInfoWithoutEnterCreditCardAndAddress(profileName);
		cscockpitCustomerTabPage.enterShippingInfoInAddNewPaymentProfilePopupWithoutSaveBtn(attendentFirstName, attendeeLastName, addressLine, city, postal, country, province, phoneNumber);
		cscockpitAutoshipTemplateTabPage.clickSaveAddNewPaymentProfilePopUP();
		s_assert.assertTrue(cscockpitAutoshipTemplateTabPage.isReviewCreditCardDetailsErrorMsgPresent(), "Review credit card error msg is not present");
		cscockpitAutoshipTemplateTabPage.enterCreditCardNumberInPaymentProfilePopup();
		cscockpitAutoshipTemplateTabPage.clickSaveAddNewPaymentProfilePopUP();
		cscockpitAutoshipTemplateTabPage.clickPaymentInfoAddressDropDown();
		s_assert.assertTrue(cscockpitAutoshipTemplateTabPage.getPaymentInfoAddressNameFromAddressDropDown().contains(profileName),"Expected profile name is "+profileName+"Actual on UI "+cscockpitAutoshipTemplateTabPage.getPaymentInfoAddressNameFromAddressDropDown());
		s_assert.assertAll();
	}

	//Hybris Project-1815:To verify cancel Pulse
	@Test
	public void testVerifyPulse_1815() throws InterruptedException{
		RFO_DB = driver.getDBNameRFO();
		int randomNum = CommonUtils.getRandomNum(10000, 1000000);
		int randomNumber = CommonUtils.getRandomNum(10000, 1000000);
		String randomCustomerSequenceNumber = null;
		List<Map<String, Object>> randomConsultantList =  null;
		List<Map<String, Object>> emailIdFromAccountIdList =  null;
		String consultantEmailID=null;
		String accountId=null;
		String autoshipId=null;
		String country = null;
		if(driver.getCountry().equalsIgnoreCase("ca")){
			country= TestConstants.COUNTRY_DD_VALUE_CA;
		}else{
			country= TestConstants.COUNTRY_DD_VALUE_US;
		}


		driver.get(driver.getStoreFrontURL()+"/"+driver.getCountry());
		while(true){
			randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_CONSULTANT_WITH_ORDERS_AND_AUTOSHIPS_RFO,countryId),RFO_DB);
			accountId = String.valueOf(getValueFromQueryResult(randomConsultantList, "AccountID"));
			emailIdFromAccountIdList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_EMAIL_ID_FROM_ACCOUNT_ID,accountId),RFO_DB);
			consultantEmailID=(String) getValueFromQueryResult(emailIdFromAccountIdList, "EmailAddress"); 
			logger.info("Account Id of the user is "+accountId);
			try{
				storeFrontConsultantPage = storeFrontHomePage.loginAsConsultant(consultantEmailID, password);
			}catch(Exception e){
				driver.get(driver.getStoreFrontURL()+"/"+driver.getCountry());
				storeFrontConsultantPage = storeFrontHomePage.loginAsConsultant(consultantEmailID, password);
			}
			boolean isError = driver.getCurrentUrl().contains("error");
			if(isError){
				logger.info("login error for the user "+consultantEmailID);
				driver.get(driver.getStoreFrontURL()+"/"+driver.getCountry());
			}
			else
				break;
		} 
		logout();
		logger.info("emaild of Consultant user "+consultantEmailID);
		logger.info("login is successful");
		driver.get(driver.getCSCockpitURL());
		cscockpitCustomerSearchTabPage = cscockpitLoginPage.clickLoginBtn();
		cscockpitCustomerSearchTabPage.selectCustomerTypeFromDropDownInCustomerSearchTab("CONSULTANT");
		cscockpitCustomerSearchTabPage.selectCountryFromDropDownInCustomerSearchTab(country);
		cscockpitCustomerSearchTabPage.selectAccountStatusFromDropDownInCustomerSearchTab("Active");
		cscockpitCustomerSearchTabPage.enterEmailIdInSearchFieldInCustomerSearchTab(consultantEmailID);
		cscockpitCustomerSearchTabPage.clickSearchBtn();
		randomCustomerSequenceNumber = String.valueOf(cscockpitCustomerSearchTabPage.getRandomCustomerFromSearchResult());
		cscockpitCustomerSearchTabPage.clickAndReturnCIDNumberInCustomerSearchTab(randomCustomerSequenceNumber);
		//Verify Different Section on Customer tab page.
		s_assert.assertTrue(cscockpitCustomerTabPage.verifyAutoshipTemplateSectionInCustomerTab(),"AutoShip Template section is not on Customer Tab Page for US");
		s_assert.assertTrue(cscockpitCustomerTabPage.verifyCustomerOrderSectionInCustomerTab(),"Customer Order section is not on Customer Tab Page for US");
		s_assert.assertTrue(cscockpitCustomerTabPage.verifyCustomerBillingInfoSectionInCustomerTab(),"Customer Billing Info section is not on Customer Tab Page for US");
		s_assert.assertTrue(cscockpitCustomerTabPage.verifyCustomerAddressSectionInCustomerTab(),"Customer Address section is not on Customer Tab Page for US");
		if(cscockpitCustomerTabPage.isCRPAutoshipIDHavingStatusIsPendingPresent()){
			autoshipId=cscockpitCustomerTabPage.getAndClickAutoshipIDHavingTypeAsCRPAutoshipAndStatusIsPending();
			cscockpitAutoshipTemplateTabPage.clickCancelAutoship();
			s_assert.assertTrue(cscockpitAutoshipTemplateTabPage.isCancelAutoshipPopupAlertPresent(), "Cancel Autoship Popup is not Present");
			cscockpitAutoshipTemplateTabPage.clickCancelButtonOfCancelAutoshipTemplatePopup();
			s_assert.assertFalse(cscockpitAutoshipTemplateTabPage.isCancelAutoshipPopupAlertPresent(), "Cancel Autoship Popup is Present after clicking cancel button");
			cscockpitAutoshipTemplateTabPage.clickCancelAutoship();
			cscockpitAutoshipTemplateTabPage.clickConfirmCancelAutoshipTemplatePopup();
			s_assert.assertTrue(cscockpitAutoshipTemplateTabPage.verifyCancelAutoshipTemplateLinkInAutoshipTemplateTab(),"Cancel autoship link is present after cancelling Autoship");
			cscockpitAutoshipTemplateTabPage.clickCustomerTab();
		}else{
			autoshipId=cscockpitCustomerTabPage.getAutoshipIDHavingTypeAsCRPAutoshipAndStatusIsCancelled();
		}
		String statusAfterAutoshipCancelled=cscockpitCustomerTabPage.getStatusOfAutoShipIdForCRPAutoshipTypeFromAutoshipTemplate(autoshipId);
		s_assert.assertTrue(statusAfterAutoshipCancelled.equals("Cancelled"),"Autoship status after cancelled autoship is not cancelled for US");
		cscockpitCustomerTabPage.getAndClickAutoshipIDHavingTypeAsCRPAutoshipAndStatusIsCancelled(autoshipId);
		s_assert.assertTrue(cscockpitAutoshipTemplateTabPage.verifyCancelAutoshipTemplateLinkInAutoshipTemplateTab(),"Cancel autoship link is present after cancelling Autoship");
		cscockpitAutoshipTemplateTabPage.clickMenuButton();
		cscockpitAutoshipTemplateTabPage.clickLogoutButton();
		driver.get(driver.getStoreFrontURL()+"/"+driver.getCountry());
		try{
			storeFrontConsultantPage = storeFrontHomePage.loginAsConsultant(consultantEmailID, password);
		}catch(Exception e){
			driver.get(driver.getStoreFrontURL()+"/"+driver.getCountry());
			storeFrontConsultantPage = storeFrontHomePage.loginAsConsultant(consultantEmailID, password);
		}
		storeFrontConsultantPage.clickOnWelcomeDropDown();
		storeFrontConsultantPage.clickAccountInfoLinkPresentOnWelcomeDropDown();
		storeFrontConsultantPage.clickOnYourAccountDropdown();
		storeFrontConsultantPage.clickOnAutoshipStatusLink();
		s_assert.assertTrue(storeFrontAccountInfoPage.verifyCRPCancelled(), "Login is successful with autoship cancelled PC user");
		s_assert.assertAll();
		logout();
	}

	//Hybris Project-1744:To verify Edit card functionality in the PCperks Edit Autoship template Page
	@Test
	public void testVerifyEditCardFunctionalityInPCPerksEditAutoshipTemplatePage_1744(){
		RFO_DB = driver.getDBNameRFO();
		String randomCustomerSequenceNumber = null;
		String accountID = null;
		String pcEmailID;
		int randomNum = CommonUtils.getRandomNum(10000, 1000000);
		int randomNum2 = CommonUtils.getRandomNum(10000, 1000000);
		String attendentFirstName = TestConstants.FIRST_NAME+randomNum;
		String attendeeLastName = TestConstants.LAST_NAME;
		String city = null;
		String postal = null;
		String province = null;
		String phoneNumber = null;
		String country = null;
		String addressLine = null;
		String attention = "Attention";
		String line1 = "Line 1";
		if(driver.getCountry().equalsIgnoreCase("ca")){
			country= TestConstants.COUNTRY_DD_VALUE_CA;
			addressLine = TestConstants.ADDRESS_LINE_1_CA;
			city = TestConstants.CITY_CA;
			postal = TestConstants.POSTAL_CODE_CA;
			province = TestConstants.PROVINCE_ALBERTA;
			phoneNumber = TestConstants.PHONE_NUMBER_CA;
		}else{
			country= TestConstants.COUNTRY_DD_VALUE_US;
			addressLine = TestConstants.ADDRESS_LINE_1_US;
			city = TestConstants.CITY_US;
			postal = TestConstants.POSTAL_CODE_US;
			province = TestConstants.PROVINCE_ALABAMA_US;
			phoneNumber = TestConstants.PHONE_NUMBER_US;
		}
		storeFrontHomePage = new StoreFrontHomePage(driver);
		driver.get(driver.getStoreFrontURL()+"/"+driver.getCountry());
		List<Map<String, Object>> randomPCList = null;
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
		logout();
		//get emailId of username
		List<Map<String, Object>> randomConsultantUsernameList =  null;
		randomConsultantUsernameList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_EMAIL_ID_FROM_ACCOUNT_ID,accountID),RFO_DB);
		pcEmailID = String.valueOf(getValueFromQueryResult(randomConsultantUsernameList, "EmailAddress"));  
		logger.info("emaild of PC username "+pcEmailID);

		driver.get(driver.getCSCockpitURL());
		cscockpitCustomerSearchTabPage = cscockpitLoginPage.clickLoginBtn();
		cscockpitCustomerSearchTabPage.selectCustomerTypeFromDropDownInCustomerSearchTab("PC");
		cscockpitCustomerSearchTabPage.selectCountryFromDropDownInCustomerSearchTab(country);
		cscockpitCustomerSearchTabPage.selectAccountStatusFromDropDownInCustomerSearchTab("Active");
		cscockpitCustomerSearchTabPage.enterEmailIdInSearchFieldInCustomerSearchTab(pcEmailID);
		cscockpitCustomerSearchTabPage.clickSearchBtn();
		randomCustomerSequenceNumber = String.valueOf(cscockpitCustomerSearchTabPage.getRandomCustomerFromSearchResult());
		cscockpitCustomerSearchTabPage.clickAndReturnCIDNumberInCustomerSearchTab(randomCustomerSequenceNumber);
		cscockpitCustomerTabPage.getAndClickAutoshipIDHavingTypeAsPCAutoshipAndStatusIsPending();
		cscockpitAutoshipTemplateTabPage.clickEditAutoshiptemplate();
		cscockpitAutoshipTemplateTabPage.clickPaymentInfoAddressDropDown();
		cscockpitAutoshipTemplateTabPage.clickFirstCreditCardInPaymentAddressDropDown();
		cscockpitAutoshipTemplateTabPage.clickEditUnderPaymentInfoSection();
		s_assert.assertTrue(cscockpitAutoshipTemplateTabPage.isAddNewPaymentProfilePopup(), "Add a new payment profile popup is not present");
		cscockpitAutoshipTemplateTabPage.clickCloseOfEditPaymentProfilePopup();
		cscockpitAutoshipTemplateTabPage.clickEditUnderPaymentInfoSection();
		cscockpitAutoshipTemplateTabPage.clickSaveAddNewPaymentProfilePopUP();
		s_assert.assertTrue(cscockpitAutoshipTemplateTabPage.isReviewCreditCardDetailsErrorMsgPresent(), "Review credit card error msg is not present");
		String profileName = TestConstants.NEW_BILLING_PROFILE_NAME+randomNum;
		cscockpitAutoshipTemplateTabPage.enterBillingInfoWithoutSelectAddress(profileName);	
		cscockpitAutoshipTemplateTabPage.clickSaveAddNewPaymentProfilePopUP();
		s_assert.assertTrue(cscockpitAutoshipTemplateTabPage.getPaymentAddressNameInAutoshipTemplateTab().toLowerCase().trim().contains(profileName.toLowerCase().trim()),"Payment Address Name Expected"+profileName+" but on UI"+cscockpitAutoshipTemplateTabPage.getPaymentAddressNameInAutoshipTemplateTab());
		cscockpitAutoshipTemplateTabPage.clickEditUnderPaymentInfoSection();
		cscockpitAutoshipTemplateTabPage.clickEditAddressInEditPaymentProfilePopup();
		s_assert.assertTrue(cscockpitAutoshipTemplateTabPage.isAddressValuesPresentInPaymentProfilePopup(attention), "Attention field is not present in payment profile popup");
		s_assert.assertTrue(cscockpitAutoshipTemplateTabPage.isAddressValuesPresentInPaymentProfilePopup(line1), "line 1 field is not present in payment profile popup");
		cscockpitAutoshipTemplateTabPage.clickCloseOfEditPaymentProfilePopup();
		cscockpitAutoshipTemplateTabPage.clickEditUnderPaymentInfoSection();
		profileName = TestConstants.NEW_BILLING_PROFILE_NAME+randomNum2;
		cscockpitAutoshipTemplateTabPage.enterBillingInfoWithoutSelectAddress(profileName);
		cscockpitCustomerTabPage.enterShippingInfoInAddNewPaymentProfilePopupWithoutSaveBtn(attendentFirstName, attendeeLastName, addressLine, city, postal, country, province, phoneNumber);
		cscockpitAutoshipTemplateTabPage.clickSaveAddNewPaymentProfilePopUP();
		s_assert.assertTrue(cscockpitAutoshipTemplateTabPage.getPaymentAddressNameInAutoshipTemplateTab().toLowerCase().trim().contains(profileName.toLowerCase().trim()),"Payment Address Name Expected"+profileName+" but on UI"+cscockpitAutoshipTemplateTabPage.getPaymentAddressNameInAutoshipTemplateTab());
		s_assert.assertAll();
	}


	//Hybris Project-1743:To verify Edit card functionality in the CRP Edit Autoship template Page
	@Test
	public void testVerifyEditCardFunctionalityInCRPEditAutoshipTemplatePage_1743(){
		RFO_DB = driver.getDBNameRFO();
		String randomCustomerSequenceNumber = null;
		String accountID = null;
		String pcEmailID;
		int randomNum = CommonUtils.getRandomNum(10000, 1000000);
		int randomNum2 = CommonUtils.getRandomNum(10000, 1000000);
		String attendentFirstName = TestConstants.FIRST_NAME+randomNum;
		String attendeeLastName = TestConstants.LAST_NAME;
		String city = null;
		String postal = null;
		String province = null;
		String phoneNumber = null;
		String country = null;
		String addressLine = null;
		String attention = "Attention";
		String line1 = "Line 1";
		if(driver.getCountry().equalsIgnoreCase("ca")){
			country= TestConstants.COUNTRY_DD_VALUE_CA;
			addressLine = TestConstants.ADDRESS_LINE_1_CA;
			city = TestConstants.CITY_CA;
			postal = TestConstants.POSTAL_CODE_CA;
			province = TestConstants.PROVINCE_ALBERTA;
			phoneNumber = TestConstants.PHONE_NUMBER_CA;
		}else{
			country= TestConstants.COUNTRY_DD_VALUE_US;
			addressLine = TestConstants.ADDRESS_LINE_1_US;
			city = TestConstants.CITY_US;
			postal = TestConstants.POSTAL_CODE_US;
			province = TestConstants.PROVINCE_ALABAMA_US;
			phoneNumber = TestConstants.PHONE_NUMBER_US;
		}
		storeFrontHomePage = new StoreFrontHomePage(driver);
		driver.get(driver.getStoreFrontURL()+"/"+driver.getCountry());
		List<Map<String, Object>> randomConsultantList = null;
		while(true){
			randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_CONSULTANT_WITH_ORDERS_AND_AUTOSHIPS_RFO,countryId),RFO_DB);
			pcEmailID = (String) getValueFromQueryResult(randomConsultantList, "UserName");
			accountID = String.valueOf(getValueFromQueryResult(randomConsultantList, "AccountID"));

			try{
				storeFrontConsultantPage = storeFrontHomePage.loginAsConsultant(pcEmailID, password);
			}catch(Exception e){
				driver.get(driver.getStoreFrontURL()+"/"+driver.getCountry());
				storeFrontConsultantPage = storeFrontHomePage.loginAsConsultant(pcEmailID, password);
			}
			boolean isLoginError = driver.getCurrentUrl().contains("error");
			if(isLoginError){
				logger.info("Login error for the user "+pcEmailID);
				driver.get(driver.getStoreFrontURL()+"/"+driver.getCountry());
			}
			else{
				break;
			}
		}
		logout();
		//get emailId of username
		List<Map<String, Object>> randomConsultantUsernameList =  null;
		randomConsultantUsernameList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_EMAIL_ID_FROM_ACCOUNT_ID,accountID),RFO_DB);
		pcEmailID = String.valueOf(getValueFromQueryResult(randomConsultantUsernameList, "EmailAddress"));  
		logger.info("emaild of consultant username "+pcEmailID);

		driver.get(driver.getCSCockpitURL());
		cscockpitCustomerSearchTabPage = cscockpitLoginPage.clickLoginBtn();
		cscockpitCustomerSearchTabPage.selectCustomerTypeFromDropDownInCustomerSearchTab("CONSULTANT");
		cscockpitCustomerSearchTabPage.selectCountryFromDropDownInCustomerSearchTab(country);
		cscockpitCustomerSearchTabPage.selectAccountStatusFromDropDownInCustomerSearchTab("Active");
		cscockpitCustomerSearchTabPage.enterEmailIdInSearchFieldInCustomerSearchTab(pcEmailID);
		cscockpitCustomerSearchTabPage.clickSearchBtn();
		randomCustomerSequenceNumber = String.valueOf(cscockpitCustomerSearchTabPage.getRandomCustomerFromSearchResult());
		cscockpitCustomerSearchTabPage.clickAndReturnCIDNumberInCustomerSearchTab(randomCustomerSequenceNumber);
		cscockpitCustomerTabPage.getAndClickAutoshipIDHavingTypeAsCRPAutoshipAndStatusIsPending();
		cscockpitAutoshipTemplateTabPage.clickEditAutoshiptemplate();
		cscockpitAutoshipTemplateTabPage.clickPaymentInfoAddressDropDown();
		cscockpitAutoshipTemplateTabPage.clickFirstCreditCardInPaymentAddressDropDown();
		cscockpitAutoshipTemplateTabPage.clickEditUnderPaymentInfoSection();
		s_assert.assertTrue(cscockpitAutoshipTemplateTabPage.isAddNewPaymentProfilePopup(), "Add a new payment profile popup is not present");
		cscockpitAutoshipTemplateTabPage.clickCloseOfEditPaymentProfilePopup();
		cscockpitAutoshipTemplateTabPage.clickEditUnderPaymentInfoSection();
		cscockpitAutoshipTemplateTabPage.clickSaveAddNewPaymentProfilePopUP();
		s_assert.assertTrue(cscockpitAutoshipTemplateTabPage.isReviewCreditCardDetailsErrorMsgPresent(), "Review credit card error msg is not present");
		String profileName = TestConstants.NEW_BILLING_PROFILE_NAME+randomNum;
		cscockpitAutoshipTemplateTabPage.enterBillingInfoWithoutSelectAddress(profileName);	
		cscockpitAutoshipTemplateTabPage.clickSaveAddNewPaymentProfilePopUP();
		s_assert.assertTrue(cscockpitAutoshipTemplateTabPage.getPaymentAddressNameInAutoshipTemplateTab().toLowerCase().trim().contains(profileName.toLowerCase().trim()),"Payment Address Name Expected"+profileName+" but on UI"+cscockpitAutoshipTemplateTabPage.getPaymentAddressNameInAutoshipTemplateTab());
		cscockpitAutoshipTemplateTabPage.clickEditUnderPaymentInfoSection();
		cscockpitAutoshipTemplateTabPage.clickEditAddressInEditPaymentProfilePopup();
		s_assert.assertTrue(cscockpitAutoshipTemplateTabPage.isAddressValuesPresentInPaymentProfilePopup(attention), "Attention field is not present in payment profile popup");
		s_assert.assertTrue(cscockpitAutoshipTemplateTabPage.isAddressValuesPresentInPaymentProfilePopup(line1), "line 1 field is not present in payment profile popup");
		cscockpitAutoshipTemplateTabPage.clickCloseOfEditPaymentProfilePopup();
		cscockpitAutoshipTemplateTabPage.clickEditUnderPaymentInfoSection();
		profileName = TestConstants.NEW_BILLING_PROFILE_NAME+randomNum2;
		cscockpitAutoshipTemplateTabPage.enterBillingInfoWithoutSelectAddress(profileName);
		cscockpitAutoshipTemplateTabPage.enterShippingInfoInAddNewPaymentProfilePopupWithoutSaveBtn(attendentFirstName, attendeeLastName, addressLine, city, postal, country, province, phoneNumber);
		cscockpitAutoshipTemplateTabPage.clickSaveAddNewPaymentProfilePopUP();
		s_assert.assertTrue(cscockpitAutoshipTemplateTabPage.getPaymentAddressNameInAutoshipTemplateTab().toLowerCase().trim().contains(profileName.toLowerCase().trim()),"Payment Address Name Expected"+profileName+" but on UI"+cscockpitAutoshipTemplateTabPage.getPaymentAddressNameInAutoshipTemplateTab());
		s_assert.assertAll();
	}

	// Hybris Project-1816:To verify Add New payment profile functionality in the CRP Checkout Page
	@Test
	public void testVerifyAddNewPaymentProfileFunctionalityInCRPCheckout_1816() throws InterruptedException{
		RFO_DB = driver.getDBNameRFO();
		String randomCustomerSequenceNumber = null;
		int randomNum = CommonUtils.getRandomNum(10000, 1000000);
		int randomNumb = CommonUtils.getRandomNum(10000, 1000000);
		int randomNumber = CommonUtils.getRandomNum(10000, 1000000);
		int randomNumbers = CommonUtils.getRandomNum(10000, 1000000);

		String consultantEmailID=null;
		String accountId=null;
		String SKUValue=null;
		String attendeeLastName=null;
		String newBillingProfileName=null;
		String securityCode=null;
		String cardNumber=null;
		String attendeeFirstName=null;
		String addressLine=null;
		String city=null;
		String postalCode=null;
		String country=null;
		String Province=null;
		String phoneNumber=null;
		String profileName=null;
		String secondProfileName=null;

		profileName=TestConstants.NEW_BILLING_PROFILE_NAME+randomNum;
		secondProfileName=TestConstants.NEW_BILLING_PROFILE_NAME+randomNumb;
		attendeeLastName="IN";
		newBillingProfileName = TestConstants.NEW_BILLING_PROFILE_NAME_US+randomNumber;
		securityCode=TestConstants.SECURITY_CODE;
		cardNumber=TestConstants.CARD_NUMBER;
		attendeeFirstName=TestConstants.FIRST_NAME+randomNumbers;
		if(driver.getCountry().equalsIgnoreCase("us")){
			addressLine=TestConstants.NEW_ADDRESS_LINE1_US;
			city=TestConstants.NEW_ADDRESS_CITY_US;
			postalCode=TestConstants.NEW_ADDRESS_POSTAL_CODE_US;
			country=TestConstants.COUNTRY_DD_VALUE_US;
			Province=TestConstants.PROVINCE_ALABAMA_US;
			phoneNumber=TestConstants.NEW_ADDRESS_PHONE_NUMBER_US;
		}else{
			addressLine=TestConstants.ADDRESS_LINE_1_CA;
			city=TestConstants.CITY_CA;
			postalCode=TestConstants.POSTAL_CODE_CA;
			country=TestConstants.COUNTRY_DD_VALUE_CA;
			Province=TestConstants.PROVINCE_CA;
			phoneNumber=TestConstants.PHONE_NUMBER_CA;
		}

		//-------------------FOR US----------------------------------
		List<Map<String, Object>> randomConsultantList =  null;
		List<Map<String, Object>> emailIdFromAccountIdList =  null;
		driver.get(driver.getStoreFrontURL()+"/"+driver.getCountry());
		while(true){
			randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_CONSULTANT_WITH_ORDERS_AND_AUTOSHIPS_RFO,countryId),RFO_DB);
			accountId = String.valueOf(getValueFromQueryResult(randomConsultantList, "AccountID"));
			emailIdFromAccountIdList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_EMAIL_ID_FROM_ACCOUNT_ID,accountId),RFO_DB);
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
		logger.info("login is successful");
		logout(); 
		driver.get(driver.getCSCockpitURL());
		cscockpitCustomerSearchTabPage = cscockpitLoginPage.clickLoginBtn();
		cscockpitCustomerSearchTabPage.selectCustomerTypeFromDropDownInCustomerSearchTab("CONSULTANT");
		cscockpitCustomerSearchTabPage.selectCountryFromDropDownInCustomerSearchTab(country);
		cscockpitCustomerSearchTabPage.selectAccountStatusFromDropDownInCustomerSearchTab("Active");
		cscockpitCustomerSearchTabPage.enterEmailIdInSearchFieldInCustomerSearchTab(consultantEmailID);
		cscockpitCustomerSearchTabPage.clickSearchBtn();
		randomCustomerSequenceNumber = String.valueOf(cscockpitCustomerSearchTabPage.getRandomCustomerFromSearchResult());
		cscockpitCustomerSearchTabPage.clickAndReturnCIDNumberInCustomerSearchTab(randomCustomerSequenceNumber);
		//Verify Autoship template Section on Customer tab page.
		s_assert.assertTrue(cscockpitCustomerTabPage.verifyAutoshipTemplateSectionInCustomerTab(),"AutoShip Template section is not on Customer Tab Page");
		cscockpitCustomerTabPage.getAndClickAutoshipIDHavingTypeAsCRPAutoshipAndStatusIsPending();
		cscockpitAutoshipTemplateTabPage.clickEditTemplateLinkInAutoshipTemplateTab();
		s_assert.assertTrue(cscockpitAutoshipTemplateTabPage.verifyCancelEditLinkInAutoshipTemplateTab(),"Cancel Edit link is not on Autoship template Tab Page");
		cscockpitAutoshipTemplateTabPage.clickAddMoreLinesLinkInAutoShipTemplateTab();
		cscockpitAutoshipCartTabPage.selectValueFromSortByDDInCartTab("Price: High to Low");
		cscockpitAutoshipCartTabPage.selectCatalogFromDropDownInCartTab(); 
		String randomProductSequenceNumber = String.valueOf(cscockpitAutoshipCartTabPage.getRandomProductWithSKUFromSearchResult()); 
		SKUValue = cscockpitAutoshipCartTabPage.getCustomerSKUValueInCartTab(randomProductSequenceNumber);
		cscockpitAutoshipCartTabPage.searchSKUValueInCartTab(SKUValue);
		cscockpitAutoshipCartTabPage.clickAddToCartBtnInCartTab();
		cscockpitAutoshipCartTabPage.clickCheckoutBtnInCartTab();
		//Add new payment profile.
		cscockpitAutoshipTemplateUpdateTabPage.clickAddNewPaymentAddressInCheckoutTab();
		s_assert.assertTrue(cscockpitAutoshipTemplateUpdateTabPage.isAddNewPaymentProfilePopup(), "Add a new payment profile popup is not present");
		cscockpitAutoshipTemplateUpdateTabPage.clickCloseOfAddANewPaymentProfilePopup();
		s_assert.assertFalse(cscockpitAutoshipTemplateUpdateTabPage.isAddNewPaymentProfilePopup(), "Add a new payment profile popup is not present");
		cscockpitAutoshipTemplateUpdateTabPage.clickAddNewPaymentAddressInCheckoutTab();
		cscockpitAutoshipTemplateUpdateTabPage.clickSaveAddNewPaymentProfilePopUP();
		s_assert.assertTrue(cscockpitAutoshipTemplateUpdateTabPage.getErrorMessageOfPopupWithoutFillingDataInCheckoutTab().contains("Please review credit card information entered"),"CSCockpit checkout tab popup error message expected = Please review credit card information entered and on UI = " +cscockpitCheckoutTabPage.getErrorMessageOfPopupWithoutFillingDataInCheckoutTab());
		cscockpitAutoshipTemplateUpdateTabPage.enterBillingInfoWithoutSelectAddress(profileName);
		cscockpitAutoshipTemplateUpdateTabPage.clickSaveAddNewPaymentProfilePopUP();
		s_assert.assertTrue(cscockpitAutoshipTemplateTabPage.isSelectBillingAddressMsgPresent(), "select billing address error popup msg is not present");
		cscockpitAutoshipTemplateTabPage.selectBillingAddressInPaymentProfilePopup();
		cscockpitAutoshipTemplateUpdateTabPage.clickSaveAddNewPaymentProfilePopUP();
		s_assert.assertTrue(cscockpitAutoshipTemplateTabPage.getPaymentAddressNameInAutoshipTemplateTab().toLowerCase().contains(profileName.toLowerCase()),"Payment Address Name Expected"+profileName+" but on UI"+cscockpitAutoshipTemplateTabPage.getPaymentAddressNameInAutoshipTemplateTab());
		//Add new payment profile with payment address.
		cscockpitAutoshipTemplateUpdateTabPage.clickAddNewPaymentAddressInCheckoutTab();
		cscockpitAutoshipTemplateUpdateTabPage.clickAddNewAddressLinkInPopUpInCheckoutTab();
		cscockpitAutoshipTemplateUpdateTabPage.clickCloseOfAddANewPaymentProfilePopup();
		s_assert.assertFalse(cscockpitAutoshipTemplateUpdateTabPage.isAddNewPaymentProfilePopup(), "Add a new payment profile popup is not present");
		cscockpitAutoshipTemplateUpdateTabPage.clickAddNewPaymentAddressInCheckoutTab();
		cscockpitAutoshipTemplateUpdateTabPage.clickSaveAddNewPaymentProfilePopUP();
		s_assert.assertTrue(cscockpitAutoshipTemplateUpdateTabPage.getErrorMessageOfPopupWithoutFillingDataInCheckoutTab().contains("Please review credit card information entered"),"CSCockpit checkout tab popup error message expected = Please review credit card information entered and on UI = " +cscockpitCheckoutTabPage.getErrorMessageOfPopupWithoutFillingDataInCheckoutTab());
		cscockpitAutoshipTemplateUpdateTabPage.enterBillingInfoWithoutEnterCreditCardAndAddress(newBillingProfileName);
		cscockpitAutoshipTemplateUpdateTabPage.enterShippingDetailsInPopUpInCheckoutTab(attendeeFirstName,attendeeLastName,addressLine,city,postalCode,country,Province,phoneNumber);
		cscockpitAutoshipTemplateUpdateTabPage.clickSaveAddNewPaymentProfilePopUP();
		s_assert.assertTrue(cscockpitAutoshipTemplateUpdateTabPage.isReviewCreditCardDetailsErrorMsgPresent(), "Review credit card error msg is not present");
		cscockpitAutoshipTemplateUpdateTabPage.enterCreditCardNumberInPaymentProfilePopup();
		cscockpitAutoshipTemplateUpdateTabPage.clickSaveAddNewPaymentProfilePopUP();
		s_assert.assertTrue(cscockpitCheckoutTabPage.getNewBillingAddressNameInCheckoutTab().contains(attendeeFirstName),"CSCockpit checkout tab newly created billing address name expected ="+ attendeeFirstName+ "and on UI = " +cscockpitCheckoutTabPage.getNewBillingAddressNameInCheckoutTab());
		//Add new payment profile by selecting newly created billing address.
		cscockpitAutoshipTemplateUpdateTabPage.clickAddNewPaymentAddressInCheckoutTab();
		cscockpitAutoshipTemplateUpdateTabPage.clickAddNewAddressLinkInPopUpInCheckoutTab();
		cscockpitAutoshipTemplateUpdateTabPage.enterBillingInfo(cardNumber, secondProfileName, securityCode,attendeeFirstName);
		cscockpitAutoshipTemplateUpdateTabPage.clickSaveAddNewPaymentProfilePopUP();
		cscockpitAutoshipTemplateUpdateTabPage.enterCVVValueInCheckoutTab(TestConstants.SECURITY_CODE);
		cscockpitAutoshipTemplateUpdateTabPage.clickUseThisCardBtnInCheckoutTab();
		cscockpitAutoshipTemplateUpdateTabPage.clickUpdateAutoshipTemplateInAutoshipTemplateUpdateTab();
		cscockpitAutoshipTemplateUpdateTabPage.clickMenuButton();
		cscockpitAutoshipTemplateUpdateTabPage.clickLogoutButton();
		//Check the billing address on storefront.
		driver.get(driver.getStoreFrontURL()+"/"+driver.getCountry());
		try{
			storeFrontConsultantPage = storeFrontHomePage.loginAsConsultant(consultantEmailID, password);
		}catch(Exception e){
			driver.get(driver.getStoreFrontURL()+"/"+driver.getCountry());
			storeFrontConsultantPage = storeFrontHomePage.loginAsConsultant(consultantEmailID, password);
		}
		storeFrontConsultantPage.clickOnWelcomeDropDown();
		storeFrontBillingInfoPage=storeFrontConsultantPage.clickBillingInfoLinkPresentOnWelcomeDropDown();
		s_assert.assertTrue(storeFrontBillingInfoPage.isTheBillingAddressPresentOnPage(secondProfileName),"billing profile is not present on storefront UI");
		logout();
		s_assert.assertAll();
	}

	//Hybris Project-1746:To verify Add New Ahipping address functionality on PCPerk Autoship template Page
	@Test
	public void testVerifyAddNewShippingAddressFunctionalityOnPCPerks_1746(){
		RFO_DB = driver.getDBNameRFO();
		int randomNum = CommonUtils.getRandomNum(10000, 1000000);
		int randomNumber = CommonUtils.getRandomNum(10000, 1000000);
		String attendentFirstName = TestConstants.FIRST_NAME+randomNum;
		String attendentNewFirstName = TestConstants.FIRST_NAME+randomNumber;
		String attendeeLastName = TestConstants.LAST_NAME;
		String city = null;
		String postal = null;
		String province = null;
		String phoneNumber = null;
		String country = null;
		String addressLine = null;
		String addressLine2=null;
		String addresstoPass=null;
		String randomCustomerSequenceNumber = null;
		List<Map<String, Object>> randomPCList =  null;
		List<Map<String, Object>> randomPCUsernameList =  null;
		String pcEmailID = null;
		String accountID = null;
		storeFrontHomePage = new StoreFrontHomePage(driver);

		if(driver.getCountry().equalsIgnoreCase("us")){
			addressLine = TestConstants.ADDRESS_LINE_1_US;
			addressLine2=TestConstants.ADDRESS_LINE_2_US;
			addresstoPass="Hemlock";
			city = TestConstants.CITY_US;
			postal = TestConstants.POSTAL_CODE_US;
			province = TestConstants.PROVINCE_ALABAMA_US;
			phoneNumber = TestConstants.PHONE_NUMBER_US;
			country=TestConstants.COUNTRY_DD_VALUE_US;

		}else if(driver.getCountry().equalsIgnoreCase("ca")){
			addressLine=TestConstants.ADDRESS_LINE_1_CA;
			addressLine2=TestConstants.ADDRESS_LINE_2_CA;
			addresstoPass="54";
			city=TestConstants.CITY_CA;
			postal=TestConstants.POSTAL_CODE_CA;
			country=TestConstants.COUNTRY_DD_VALUE_CA;
			province=TestConstants.PROVINCE_CA;
			phoneNumber=TestConstants.PHONE_NUMBER_CA;
		}

		//----------------------FOR US------------------------------------
		driver.get(driver.getStoreFrontURL()+"/"+driver.getCountry());
		while(true){
			randomPCList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_PC_WITH_ORDERS_AND_AUTOSHIPS_RFO,countryId),RFO_DB);
			accountID = String.valueOf(getValueFromQueryResult(randomPCList, "AccountID")); 
			randomPCUsernameList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_EMAIL_ID_FROM_ACCOUNT_ID,accountID),RFO_DB);
			pcEmailID = String.valueOf(getValueFromQueryResult(randomPCUsernameList, "EmailAddress"));
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
		logout();
		logger.info("emaild of PC User username "+pcEmailID);
		logger.info("login is successful");
		driver.get(driver.getCSCockpitURL());
		cscockpitCustomerSearchTabPage = cscockpitLoginPage.clickLoginBtn();
		cscockpitCustomerSearchTabPage.selectCustomerTypeFromDropDownInCustomerSearchTab("PC");
		cscockpitCustomerSearchTabPage.selectCountryFromDropDownInCustomerSearchTab(country);
		cscockpitCustomerSearchTabPage.selectAccountStatusFromDropDownInCustomerSearchTab("Active");
		cscockpitCustomerSearchTabPage.enterEmailIdInSearchFieldInCustomerSearchTab(pcEmailID);
		cscockpitCustomerSearchTabPage.clickSearchBtn();
		randomCustomerSequenceNumber = String.valueOf(cscockpitCustomerSearchTabPage.getRandomCustomerFromSearchResult());
		cscockpitCustomerSearchTabPage.clickAndReturnCIDNumberInCustomerSearchTab(randomCustomerSequenceNumber);
		//Verify Autoship template Section on Customer tab page.
		s_assert.assertTrue(cscockpitCustomerTabPage.verifyAutoshipTemplateSectionInCustomerTab(),"AutoShip Template section is not on Customer Tab Page");
		cscockpitCustomerTabPage.getAndClickAutoshipIDHavingTypeAsPCAutoshipAndStatusIsPending();
		cscockpitAutoshipTemplateTabPage.clickEditTemplateLinkInAutoshipTemplateTab();
		s_assert.assertTrue(cscockpitAutoshipTemplateTabPage.verifyCancelEditLinkInAutoshipTemplateTab(),"Cancel Edit link is not on Autoship template Tab Page");
		cscockpitAutoshipTemplateTabPage.clickAddNewAddressButtonUnderShippingAddress();
		s_assert.assertTrue(cscockpitAutoshipTemplateTabPage.verifyDeliveryAndShippingAddressPopupPresent("Create/Edit Shipping Address"),"Create/Edit Shipping Address Popup is not Present");
		cscockpitAutoshipTemplateTabPage.clickCloseButtonToCloseDeliveryAndShippingAddressPopUp("Create/Edit Shipping Address");
		s_assert.assertFalse(cscockpitAutoshipTemplateTabPage.verifyDeliveryAndShippingAddressPopupPresent("Create/Edit Shipping Address"),"Create/Edit Shipping Address Popup is  Present");
		cscockpitAutoshipTemplateTabPage.clickAddNewAddressButtonUnderShippingAddress();
		cscockpitAutoshipTemplateTabPage.clickCreateNewAddressButtonInPopupAutoshipTemplateTabPage();
		s_assert.assertTrue(cscockpitAutoshipTemplateTabPage.verifyPopUpMessageForEmptyTextFieldsInDeliveryAndShippingAddressPopup().contains("Attention should contain First Name and Last Name"), "Warning Message for keeping all empty fields in Shipping Address Popup is not Present ");
		cscockpitAutoshipTemplateTabPage.clickOkButtonToCloseWarningPopUp();
		cscockpitAutoshipTemplateTabPage.enterShippingInfoInAddNewPaymentProfilePopupWithoutSaveBtn(attendentFirstName, attendeeLastName, addressLine, city, postal, country, province, phoneNumber);
		cscockpitAutoshipTemplateTabPage.clickCreateNewAddressButtonInPopupAutoshipTemplateTabPage();
		s_assert.assertTrue(cscockpitAutoshipTemplateTabPage.isQASpopupPresent(), "QAS popup is not present while creating  new shipping address");
		cscockpitAutoshipTemplateTabPage.clickUseEnteredAddressbtnInEditAddressPopup();
		String getShippingAddressLineFromUI=cscockpitAutoshipTemplateTabPage.getShippingAddressLine1UnderShippingAddress();
		s_assert.assertTrue(getShippingAddressLineFromUI.equalsIgnoreCase(addressLine),"Newly created shipping address is not selected from dropdown");
		cscockpitAutoshipTemplateTabPage.clickAddNewAddressButtonUnderShippingAddress();
		cscockpitAutoshipTemplateTabPage.enterShippingInfoInAddNewPaymentProfilePopupWithoutSaveBtn(attendentNewFirstName, attendeeLastName, addressLine2, city, postal, country, province, phoneNumber);
		cscockpitAutoshipTemplateTabPage.clickCreateNewAddressButtonInPopupAutoshipTemplateTabPage();
		s_assert.assertTrue(cscockpitAutoshipTemplateTabPage.isQASpopupPresent(), "QAS popup is not present while creating  new shipping address");
		String defaultSuggestionSelected =cscockpitAutoshipTemplateTabPage.selectAndReturnAddressFromDropdownBeforeClickingUseThisAddressBtnInPopup(addresstoPass);;
		cscockpitAutoshipTemplateTabPage.clickButtonsInDeliveryAndShippingAddressPopup("Use this address");
		String getNewShippingAddressLineFromUI=cscockpitAutoshipTemplateTabPage.getShippingAddressLine1UnderShippingAddress();
		s_assert.assertTrue(defaultSuggestionSelected.contains(getNewShippingAddressLineFromUI),"Newly created shipping address is not selected from dropdown");
		s_assert.assertAll();
	}

	//Hybris Project-4723:Verify that CRP scheduling date has to be between 1st and 17th (including).
	@Test
	public void testVerifyCRPSchedulingDateHasToBeBetween1stTo17th_4723(){
		RFO_DB = driver.getDBNameRFO();
		String country = null;
		if(driver.getCountry().equalsIgnoreCase("ca")){
			country= TestConstants.COUNTRY_DD_VALUE_CA;
		}else{
			country= TestConstants.COUNTRY_DD_VALUE_US;
		}
		//Get Email from database.
		List<Map<String, Object>> randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_CONSULTANT_WITH_ORDERS_AND_AUTOSHIPS_RFO,countryId),RFO_DB);
		String accountID = String.valueOf(getValueFromQueryResult(randomConsultantList, "AccountID"));
		List<Map<String, Object>> randomConsultantUsernameList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_EMAIL_ID_FROM_ACCOUNT_ID,accountID),RFO_DB);
		String consultantEmailID = String.valueOf(getValueFromQueryResult(randomConsultantUsernameList, "EmailAddress"));

		cscockpitCustomerSearchTabPage = cscockpitLoginPage.clickLoginBtn();
		cscockpitCustomerSearchTabPage.selectCustomerTypeFromDropDownInCustomerSearchTab("CONSULTANT");
		cscockpitCustomerSearchTabPage.selectCountryFromDropDownInCustomerSearchTab(country);
		cscockpitCustomerSearchTabPage.selectAccountStatusFromDropDownInCustomerSearchTab("Active");
		cscockpitCustomerSearchTabPage.enterEmailIdInSearchFieldInCustomerSearchTab(consultantEmailID);
		cscockpitCustomerSearchTabPage.clickSearchBtn();
		String randomCustomerSequenceNumber = String.valueOf(cscockpitCustomerSearchTabPage.getRandomCustomerFromSearchResult());
		cscockpitCustomerSearchTabPage.clickAndReturnCIDNumberInCustomerSearchTab(randomCustomerSequenceNumber);
		cscockpitCustomerTabPage.getAndClickAutoshipIDHavingTypeAsCRPAutoshipAndStatusIsPending();
		cscockpitAutoshipTemplateTabPage.clickEditTemplateLinkInAutoshipTemplateTab();
		String date = cscockpitAutoshipTemplateTabPage.getCRPAutoshipDateFromCalendar();
		String dateAfterOneMonth = cscockpitAutoshipTemplateTabPage.getOneMonthExtendedDateAfter17(date, "22");
		cscockpitAutoshipTemplateTabPage.enterCRPAutoshipDate(dateAfterOneMonth);
		s_assert.assertTrue(cscockpitAutoshipTemplateTabPage.isSelectValidFutureDatePopupPresent(), "Select valid future date popup is not present");
		cscockpitAutoshipTemplateTabPage.clickOkConfirmMessagePopUp();
		s_assert.assertAll();
	}

	//Hybris Project-4782:CSCockpit _Change date for PC perks autoship template
	@Test
	public void testChangeDateForPCPerksAutoshipTemplate_4782(){
		RFO_DB = driver.getDBNameRFO();
		String country = null;
		if(driver.getCountry().equalsIgnoreCase("ca")){
			country= TestConstants.COUNTRY_DD_VALUE_CA;
		}else{
			country= TestConstants.COUNTRY_DD_VALUE_US;
		}
		//Get Email from database.
		List<Map<String, Object>> randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_CONSULTANT_WITH_ORDERS_AND_AUTOSHIPS_RFO,countryId),RFO_DB);
		String accountID = String.valueOf(getValueFromQueryResult(randomConsultantList, "AccountID"));
		List<Map<String, Object>> randomConsultantUsernameList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_EMAIL_ID_FROM_ACCOUNT_ID,accountID),RFO_DB);
		String consultantEmailID = String.valueOf(getValueFromQueryResult(randomConsultantUsernameList, "EmailAddress"));
		cscockpitCustomerSearchTabPage = cscockpitLoginPage.clickLoginBtn();
		cscockpitCustomerSearchTabPage.selectCustomerTypeFromDropDownInCustomerSearchTab("CONSULTANT");
		cscockpitCustomerSearchTabPage.selectCountryFromDropDownInCustomerSearchTab(country);
		cscockpitCustomerSearchTabPage.selectAccountStatusFromDropDownInCustomerSearchTab("Active");
		cscockpitCustomerSearchTabPage.enterEmailIdInSearchFieldInCustomerSearchTab(consultantEmailID);
		cscockpitCustomerSearchTabPage.clickSearchBtn();
		String randomCustomerSequenceNumber = String.valueOf(cscockpitCustomerSearchTabPage.getRandomCustomerFromSearchResult());
		cscockpitCustomerSearchTabPage.clickAndReturnCIDNumberInCustomerSearchTab(randomCustomerSequenceNumber);
		cscockpitCustomerTabPage.getAndClickAutoshipIDHavingTypeAsCRPAutoshipAndStatusIsPending();
		cscockpitAutoshipTemplateTabPage.clickEditTemplateLinkInAutoshipTemplateTab();
		String date = cscockpitAutoshipTemplateTabPage.getCRPAutoshipDateFromCalendar();
		String modifiedDate = cscockpitAutoshipTemplateTabPage.addOneMoreDayInCRPAutoshipDate(date);
		cscockpitAutoshipTemplateTabPage.enterCRPAutoshipDate(modifiedDate);
		String updatedDate = cscockpitAutoshipTemplateTabPage.getCRPAutoshipDateFromCalendar();
		s_assert.assertTrue(modifiedDate.contains(updatedDate), "Expected date after update is"+modifiedDate+"Actual on UI is "+updatedDate);
		s_assert.assertAll();
	}

	//Hybris Project-4783:CSCockpit _Delay teh PC perks autoship template by 30 days
	@Test
	public void testDelayPCPerksAutoshipTemplateBy30Days_4783() throws InterruptedException{
		RFO_DB = driver.getDBNameRFO();
		String randomCustomerSequenceNumber = null;
		storeFrontHomePage = new StoreFrontHomePage(driver);
		String accountID;
		List<Map<String, Object>> randomPCList =  null;
		String pcEmailID = null;
		String country = null;
		if(driver.getCountry().equalsIgnoreCase("ca")){
			country= TestConstants.COUNTRY_DD_VALUE_CA;
		}else{
			country= TestConstants.COUNTRY_DD_VALUE_US;
		}
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
		List<Map<String, Object>> randomPCUsernameList =  null;
		randomPCUsernameList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_EMAIL_ID_FROM_ACCOUNT_ID,accountID),RFO_DB);
		pcEmailID = String.valueOf(getValueFromQueryResult(randomPCUsernameList, "EmailAddress"));  
		logger.info("emaild of consultant username "+pcEmailID); 
		driver.get(driver.getCSCockpitURL());  
		cscockpitCustomerSearchTabPage = cscockpitLoginPage.clickLoginBtn();
		cscockpitCustomerSearchTabPage.selectCustomerTypeFromDropDownInCustomerSearchTab("PC");
		cscockpitCustomerSearchTabPage.selectCountryFromDropDownInCustomerSearchTab(country);
		cscockpitCustomerSearchTabPage.selectAccountStatusFromDropDownInCustomerSearchTab("Active");
		cscockpitCustomerSearchTabPage.enterEmailIdInSearchFieldInCustomerSearchTab(pcEmailID);
		cscockpitCustomerSearchTabPage.clickSearchBtn();
		randomCustomerSequenceNumber = String.valueOf(cscockpitCustomerSearchTabPage.getRandomCustomerFromSearchResult());
		cscockpitCustomerSearchTabPage.clickAndReturnCIDNumberInCustomerSearchTab(randomCustomerSequenceNumber);
		cscockpitCustomerTabPage.getAndClickAutoshipIDHavingTypeAsPCAutoshipAndStatusIsPending();
		cscockpitAutoshipTemplateTabPage.clickDelayAutoshipLink();
		String noOfDays = "30";
		cscockpitAutoshipTemplateTabPage.selectNextDelayAutoshipDateInPopup(noOfDays);
		String delayAutoshipDate = cscockpitAutoshipTemplateTabPage.getNextDelayAutoshipDateFromPopup(noOfDays).split("\\(")[0].trim();
		cscockpitAutoshipTemplateTabPage.clickSaveBtnInPopUP();
		String delayAutoshipDateInMonthForm = cscockpitAutoshipTemplateTabPage.convertCartDateToFormat(delayAutoshipDate);
		driver.get(driver.getStoreFrontURL()+"/"+driver.getCountry());
		storeFrontPCUserPage = storeFrontHomePage.loginAsPCUser(pcEmailID, password);
		storeFrontPCUserPage.clickOnWelcomeDropDown();
		storeFrontAccountInfoPage = storeFrontPCUserPage.clickAccountInfoLinkPresentOnWelcomeDropDown();
		storeFrontAccountInfoPage.clickOnYourAccountDropdown();
		storeFrontAccountInfoPage.clickOnPcPerksStatus();
		String dueDate = storeFrontAccountInfoPage.getNextDueDateOfCRPTemplate();
		s_assert.assertTrue(dueDate.trim().contains(delayAutoshipDateInMonthForm), "Expected delayed autoship date is "+delayAutoshipDateInMonthForm+" Actual On store front "+dueDate);
		logout();
		s_assert.assertAll();
	}

	//Hybris Project-5285:Expired Credit Card: To verify that shipped orders can be returned
	@Test
	public void testVerifyShippedOrdersCanBeReturned_5285(){
		cscockpitCustomerSearchTabPage = cscockpitLoginPage.clickLoginBtn();
		cscockpitCustomerSearchTabPage.clickFindOrderLinkOnLeftNavigation();
		cscockpitOrderSearchTabPage.selectOrderStatusOnOrderSearchTab("Shipped");
		cscockpitOrderSearchTabPage.clickSearchBtn();
		String randomCustomerSequenceNumber = String.valueOf(cscockpitCustomerSearchTabPage.getRandomCustomerFromSearchResult());
		cscockpitCustomerSearchTabPage.clickAndReturnCIDNumberInCustomerSearchTab(randomCustomerSequenceNumber);
		s_assert.assertTrue(cscockpitOrderTabPage.verifyRefundOrderButtonPresentOnOrderTab(), "Refund Order button is not present for shipped order");
		s_assert.assertAll();
	}

	//Hybris Project-1939:To Verify that PC account status is Inactive when Pcperks Autoship cancelled
	@Test
	public void testVerifyPCAccountStatusIsInactiveWhenPCPerksAutoshipCancelled_1939() throws InterruptedException{
		int randomNum = CommonUtils.getRandomNum(10000, 1000000);
		String randomCustomerSequenceNumber = null;
		String country = null;
		if(driver.getCountry().equalsIgnoreCase("ca")){
			country= TestConstants.COUNTRY_DD_VALUE_CA;
		}else{
			country= TestConstants.COUNTRY_DD_VALUE_US;
		}

		//Login to cscockpit.	
		cscockpitCustomerSearchTabPage = cscockpitLoginPage.clickLoginBtn();
		cscockpitCustomerSearchTabPage.selectCustomerTypeFromDropDownInCustomerSearchTab("PC");
		cscockpitCustomerSearchTabPage.selectCountryFromDropDownInCustomerSearchTab(country);
		cscockpitCustomerSearchTabPage.selectAccountStatusFromDropDownInCustomerSearchTab("Active");
		cscockpitCustomerSearchTabPage.clickSearchBtn();
		randomCustomerSequenceNumber = String.valueOf(cscockpitCustomerSearchTabPage.getRandomCustomerFromSearchResult());
		String PCEmailID = cscockpitCustomerSearchTabPage.getEmailIdOfTheCustomerInCustomerSearchTab(randomCustomerSequenceNumber);
		String cid=cscockpitCustomerSearchTabPage.clickAndReturnCIDNumberInCustomerSearchTab(randomCustomerSequenceNumber);
		if(cscockpitCustomerTabPage.isPCAutoshipIDHavingStatusIsPendingPresent()){
			cscockpitCustomerTabPage.getAndClickAutoshipIDHavingTypeAsPCAutoshipAndStatusIsPending();
			cscockpitAutoshipTemplateTabPage.clickCancelAutoship();
			cscockpitAutoshipTemplateTabPage.selectPCPerksCancellationReasonFromDropDownInAutoShipTemplateTab();
			cscockpitAutoshipTemplateTabPage.selectPCPerksRequestSourceFromDropDownInAutoShipTemplateTab();
			cscockpitAutoshipTemplateTabPage.enterPCPerksCancellationMessage("Automation termination"+randomNum);
			cscockpitAutoshipTemplateTabPage.clickConfirmCancelAutoshipTemplatePopup();
		}
		cscockpitAutoshipTemplateTabPage.clickCustomerSearchTab();
		cscockpitCustomerSearchTabPage.selectCustomerTypeFromDropDownInCustomerSearchTab("PC");
		cscockpitCustomerSearchTabPage.selectCountryFromDropDownInCustomerSearchTab(country);
		cscockpitCustomerSearchTabPage.selectAccountStatusFromDropDownInCustomerSearchTab("Inactive");
		cscockpitCustomerSearchTabPage.enterCIDInOrderSearchTab(cid);
		cscockpitCustomerSearchTabPage.enterEmailIdInSearchFieldInCustomerSearchTab(PCEmailID);
		cscockpitCustomerSearchTabPage.clickSearchBtn();
		randomCustomerSequenceNumber = String.valueOf(cscockpitCustomerSearchTabPage.getRandomCustomerFromSearchResult());
		s_assert.assertTrue(cscockpitCustomerSearchTabPage.getAccountStatusOfTheCustomerInCustomerSearchTab(randomCustomerSequenceNumber).contains("INACTIVE"),"Account status of pc user is not Inactive after cancelling pc perks autoship.");
		s_assert.assertAll();  
	}

	//Hybris Project-4784:CSCockpit _Delay the PC perks autoship template by 60 days
	@Test
	public void testDelayPCPerksAutoshipTemplateBy60Days_4784() throws InterruptedException{
		RFO_DB = driver.getDBNameRFO();
		String randomCustomerSequenceNumber = null;
		storeFrontHomePage = new StoreFrontHomePage(driver);
		String accountID;
		List<Map<String, Object>> randomPCList =  null;
		String pcEmailID = null;
		String country = null;
		if(driver.getCountry().equalsIgnoreCase("ca")){
			country= TestConstants.COUNTRY_DD_VALUE_CA;
		}else{
			country= TestConstants.COUNTRY_DD_VALUE_US;
		}
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
		List<Map<String, Object>> randomPCUsernameList =  null;
		randomPCUsernameList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_EMAIL_ID_FROM_ACCOUNT_ID,accountID),RFO_DB);
		pcEmailID = String.valueOf(getValueFromQueryResult(randomPCUsernameList, "EmailAddress"));  
		logger.info("emaild of consultant username "+pcEmailID); 
		driver.get(driver.getCSCockpitURL());  
		cscockpitCustomerSearchTabPage = cscockpitLoginPage.clickLoginBtn();
		cscockpitCustomerSearchTabPage.selectCustomerTypeFromDropDownInCustomerSearchTab("PC");
		cscockpitCustomerSearchTabPage.selectCountryFromDropDownInCustomerSearchTab(country);
		cscockpitCustomerSearchTabPage.selectAccountStatusFromDropDownInCustomerSearchTab("Active");
		cscockpitCustomerSearchTabPage.enterEmailIdInSearchFieldInCustomerSearchTab(pcEmailID);
		cscockpitCustomerSearchTabPage.clickSearchBtn();
		randomCustomerSequenceNumber = String.valueOf(cscockpitCustomerSearchTabPage.getRandomCustomerFromSearchResult());
		cscockpitCustomerSearchTabPage.clickAndReturnCIDNumberInCustomerSearchTab(randomCustomerSequenceNumber);
		cscockpitCustomerTabPage.getAndClickAutoshipIDHavingTypeAsPCAutoshipAndStatusIsPending();
		cscockpitAutoshipTemplateTabPage.clickDelayAutoshipLink();
		String noOfDays = "60";
		cscockpitAutoshipTemplateTabPage.selectNextDelayAutoshipDateInPopup(noOfDays);
		String delayAutoshipDate = cscockpitAutoshipTemplateTabPage.getNextDelayAutoshipDateFromPopup(noOfDays).split("\\(")[0].trim();
		cscockpitAutoshipTemplateTabPage.clickSaveBtnInPopUP();
		String delayAutoshipDateInMonthForm = cscockpitAutoshipTemplateTabPage.convertCartDateToFormat(delayAutoshipDate);
		driver.get(driver.getStoreFrontURL()+"/"+driver.getCountry());
		try{
			storeFrontPCUserPage = storeFrontHomePage.loginAsPCUser(pcEmailID, password);
		}catch(Exception e){
			driver.get(driver.getStoreFrontURL()+"/"+driver.getCountry());
			storeFrontPCUserPage = storeFrontHomePage.loginAsPCUser(pcEmailID, password);
		}
		storeFrontPCUserPage.clickOnWelcomeDropDown();
		storeFrontAccountInfoPage = storeFrontPCUserPage.clickAccountInfoLinkPresentOnWelcomeDropDown();
		storeFrontAccountInfoPage.clickOnYourAccountDropdown();
		storeFrontAccountInfoPage.clickOnPcPerksStatus();
		String dueDate = storeFrontAccountInfoPage.getNextDueDateOfCRPTemplate();
		s_assert.assertTrue(dueDate.trim().contains(delayAutoshipDateInMonthForm), "Expected delayed autoship date is "+delayAutoshipDateInMonthForm+" Actual On store front "+dueDate);
		logout();
		s_assert.assertAll();
	}

	//Hybris Project-4790:To verify Add New Shipping address functionality on PCPerk Autoship Checkout Page
	@Test
	public void testAddNewShippingAddressFunctionalityOnPCPerksAutoshipCheckoutPage_4790() throws InterruptedException{
		RFO_DB = driver.getDBNameRFO();
		int randomNum = CommonUtils.getRandomNum(10000, 1000000);
		String attendentFirstName = TestConstants.FIRST_NAME+randomNum;
		String attendeeLastName = TestConstants.LAST_NAME;
		String city = null;
		String postal = null;
		String province = null;
		String phoneNumber = null;
		String country = null;
		String addressLine = null;
		String randomCustomerSequenceNumber = null;
		if(driver.getCountry().equalsIgnoreCase("us")){
			addressLine = TestConstants.ADDRESS_LINE_1_US;
			city = TestConstants.CITY_US;
			postal = TestConstants.POSTAL_CODE_US;
			province = TestConstants.PROVINCE_ALABAMA_US;
			phoneNumber = TestConstants.PHONE_NUMBER_US;
			country=TestConstants.COUNTRY_DD_VALUE_US;

		}else if(driver.getCountry().equalsIgnoreCase("ca")){
			addressLine=TestConstants.ADDRESS_LINE_1_CA;
			city=TestConstants.CITY_CA;
			postal=TestConstants.POSTAL_CODE_CA;
			country=TestConstants.COUNTRY_DD_VALUE_CA;
			province=TestConstants.PROVINCE_CA;
			phoneNumber=TestConstants.PHONE_NUMBER_CA;
		}
		cscockpitCustomerSearchTabPage = cscockpitLoginPage.clickLoginBtn();
		cscockpitCustomerSearchTabPage.selectCustomerTypeFromDropDownInCustomerSearchTab("PC");
		cscockpitCustomerSearchTabPage.selectCountryFromDropDownInCustomerSearchTab(country);
		cscockpitCustomerSearchTabPage.selectAccountStatusFromDropDownInCustomerSearchTab("Active");
		cscockpitCustomerSearchTabPage.clickSearchBtn();
		randomCustomerSequenceNumber = String.valueOf(cscockpitCustomerSearchTabPage.getRandomCustomerFromSearchResult());
		cscockpitCustomerSearchTabPage.clickAndReturnCIDNumberInCustomerSearchTab(randomCustomerSequenceNumber);
		//Verify Autoship template Section on Customer tab page.
		s_assert.assertTrue(cscockpitCustomerTabPage.verifyAutoshipTemplateSectionInCustomerTab(),"AutoShip Template section is not on Customer Tab Page");
		cscockpitCustomerTabPage.getAndClickAutoshipIDHavingTypeAsPCAutoshipAndStatusIsPending();
		cscockpitAutoshipTemplateTabPage.clickEditTemplateLinkInAutoshipTemplateTab();
		cscockpitAutoshipTemplateTabPage.clickAddMoreLinesLinkInAutoShipTemplateTab();
		cscockpitAutoshipCartTabPage.selectValueFromSortByDDInCartTab("Price: High to Low");
		cscockpitAutoshipCartTabPage.selectCatalogFromDropDownInCartTab(); 
		String randomProductSequenceNumber = String.valueOf(cscockpitAutoshipCartTabPage.getRandomProductWithSKUFromSearchResult()); 
		String SKUValue = cscockpitAutoshipCartTabPage.getCustomerSKUValueInCartTab(randomProductSequenceNumber);
		cscockpitAutoshipCartTabPage.searchSKUValueInCartTab(SKUValue);
		cscockpitAutoshipCartTabPage.clickAddToCartBtnInCartTab();
		cscockpitAutoshipCartTabPage.clickCheckoutBtnInCartTab();
		cscockpitAutoshipTemplateUpdateTabPage.clickAddNewAddressUnderDeliveryAddressInCheckoutTab();
		s_assert.assertTrue(cscockpitAutoshipTemplateTabPage.verifyDeliveryAndShippingAddressPopupPresent("Create Delivery Address"),"Create Delivery Address Popup is not Present");
		cscockpitAutoshipTemplateUpdateTabPage.enterShippingInfoInAddNewPaymentProfilePopupWithoutSaveBtn(attendentFirstName, attendeeLastName, addressLine, city, postal, country, province, phoneNumber);
		cscockpitAutoshipTemplateUpdateTabPage.clickCreateNewAddressButtonInPopupAutoshipTemplateTabPage();
		cscockpitAutoshipTemplateUpdateTabPage.clickUseEnteredAddressOfCreateNewAddressShippingAddressPopup();
		cscockpitAutoshipTemplateUpdateTabPage.clickAddNewPaymentAddressInCheckoutTab();
		cscockpitAutoshipTemplateUpdateTabPage.enterBillingInfo();
		cscockpitAutoshipTemplateUpdateTabPage.clickSaveAddNewPaymentProfilePopUP();
		cscockpitAutoshipTemplateUpdateTabPage.enterCVVValueInCheckoutTab(TestConstants.SECURITY_CODE);
		cscockpitAutoshipTemplateUpdateTabPage.clickUseThisCardBtnInCheckoutTab();
		cscockpitAutoshipTemplateUpdateTabPage.clickUpdateAutoshipTemplateInAutoshipTemplateUpdateTab();
		String getNewShippingAddressNameFromUI=cscockpitAutoshipTemplateTabPage.getShippingAddressNameInAutoshipTemplateTab();
		s_assert.assertTrue(getNewShippingAddressNameFromUI.contains(attendentFirstName), "Expected address name is "+attendentFirstName+" Actual on UI "+getNewShippingAddressNameFromUI);
		s_assert.assertAll();
	}

	//Hybris Project-4794:Verify if only US product is displayed on PCPerk Cart page
	@Test
	public void testVerifyOnlyUSProductIsDisplayedOnPCPerkCartPage_4794(){
		RFO_DB = driver.getDBNameRFO();
		String randomCustomerSequenceNumber = null;
		String country = null;
		if(driver.getCountry().equalsIgnoreCase("ca")){
			country= TestConstants.COUNTRY_DD_VALUE_CA;
		}else{
			country= TestConstants.COUNTRY_DD_VALUE_US;
		}

		cscockpitCustomerSearchTabPage = cscockpitLoginPage.clickLoginBtn();
		cscockpitCustomerSearchTabPage.selectCustomerTypeFromDropDownInCustomerSearchTab("PC");
		cscockpitCustomerSearchTabPage.selectCountryFromDropDownInCustomerSearchTab(country);
		cscockpitCustomerSearchTabPage.selectAccountStatusFromDropDownInCustomerSearchTab("Active");
		cscockpitCustomerSearchTabPage.clickSearchBtn();
		randomCustomerSequenceNumber = String.valueOf(cscockpitCustomerSearchTabPage.getRandomCustomerFromSearchResult());
		cscockpitCustomerSearchTabPage.clickAndReturnCIDNumberInCustomerSearchTab(randomCustomerSequenceNumber);
		//Verify Autoship template Section on Customer tab page.
		s_assert.assertTrue(cscockpitCustomerTabPage.verifyAutoshipTemplateSectionInCustomerTab(),"AutoShip Template section is not on Customer Tab Page");
		cscockpitCustomerTabPage.getAndClickAutoshipIDHavingTypeAsPCAutoshipAndStatusIsPending();
		cscockpitAutoshipTemplateTabPage.clickEditTemplateLinkInAutoshipTemplateTab();
		cscockpitAutoshipTemplateTabPage.clickAddMoreLinesLinkInAutoShipTemplateTab();
		s_assert.assertTrue(cscockpitAutoshipCartTabPage.getCountOfOptionsInCatalogFromDropDownInCartTab()==1,"Expect no of options is 1 but actual on UI "+cscockpitAutoshipCartTabPage.getCountOfOptionsInCatalogFromDropDownInCartTab());
		if(country.equalsIgnoreCase("us")){
			s_assert.assertTrue(cscockpitAutoshipCartTabPage.getSelectProductCatalogValue().contains("US Product Catalog"),"Expect name of options is US Product Catalog but actual on UI "+cscockpitAutoshipCartTabPage.getSelectProductCatalogValue());
		}else{
			s_assert.assertTrue(cscockpitAutoshipCartTabPage.getSelectProductCatalogValue().contains("Canada Product Catalog"),"Expect name of options is Candada Product Catalog but actual on UI "+cscockpitAutoshipCartTabPage.getSelectProductCatalogValue());
		}
		s_assert.assertAll();
	}


	//Hybris Project-2678:CSCockpit _Edit PC perks autoship template
	@Test
	public void testVerifyEditPCPerksAutoshipTemplate_2678(){
		String country = null;
		if(driver.getCountry().equalsIgnoreCase("ca")){
			country= TestConstants.COUNTRY_DD_VALUE_CA;
		}else{
			country= TestConstants.COUNTRY_DD_VALUE_US;
		}
		cscockpitCustomerSearchTabPage = cscockpitLoginPage.clickLoginBtn();
		cscockpitCustomerSearchTabPage.selectCustomerTypeFromDropDownInCustomerSearchTab("PC");
		cscockpitCustomerSearchTabPage.selectCountryFromDropDownInCustomerSearchTab(country);
		cscockpitCustomerSearchTabPage.selectAccountStatusFromDropDownInCustomerSearchTab("Active");
		cscockpitCustomerSearchTabPage.clickSearchBtn();
		String randomCustomerSequenceNumber = String.valueOf(cscockpitCustomerSearchTabPage.getRandomCustomerFromSearchResult());
		cscockpitCustomerSearchTabPage.clickAndReturnCIDNumberInCustomerSearchTab(randomCustomerSequenceNumber);
		//Verify Autoship template Section on Customer tab page.
		s_assert.assertTrue(cscockpitCustomerTabPage.verifyAutoshipTemplateSectionInCustomerTab(),"AutoShip Template section is not on Customer Tab Page");
		cscockpitCustomerTabPage.getAndClickAutoshipIDHavingTypeAsPCAutoshipAndStatusIsPending();
		cscockpitAutoshipTemplateTabPage.clickEditTemplateLinkInAutoshipTemplateTab();
		String quantityOfProduct = cscockpitAutoshipTemplateTabPage.getQuantityOfProductInAutoshipTemplateTabPage("1");
		int updateQuantity = Integer.parseInt(quantityOfProduct)+1;
		String updatedQuantity = ""+updateQuantity;
		cscockpitAutoshipTemplateTabPage.enterQtyOfFirstProduct(updatedQuantity);
		cscockpitAutoshipTemplateTabPage.clickUpdateLinkOfOrderDetail();
		s_assert.assertTrue(cscockpitAutoshipTemplateTabPage.getQuantityOfProductInAutoshipTemplateTabPage("1").contains(updatedQuantity), "Expected quantity of product after updation "+updatedQuantity+"Actual On UI "+cscockpitAutoshipTemplateTabPage.getQuantityOfProductInAutoshipTemplateTabPage("1"));
		//decrease the qty
		cscockpitAutoshipTemplateTabPage.enterQtyOfFirstProduct(quantityOfProduct);
		cscockpitAutoshipTemplateTabPage.clickUpdateLinkOfOrderDetail();
		s_assert.assertTrue(cscockpitAutoshipTemplateTabPage.getQuantityOfProductInAutoshipTemplateTabPage("1").contains(quantityOfProduct), "Expected quantity of product after updation "+quantityOfProduct+"Actual On UI "+cscockpitAutoshipTemplateTabPage.getQuantityOfProductInAutoshipTemplateTabPage("1"));
		cscockpitAutoshipTemplateTabPage.clickAddMoreLinesLinkInAutoShipTemplateTab();
		cscockpitAutoshipCartTabPage.selectValueFromSortByDDInCartTab("Price: High to Low");
		cscockpitAutoshipCartTabPage.selectCatalogFromDropDownInCartTab(); 
		String randomProductSequenceNumber = String.valueOf(cscockpitAutoshipCartTabPage.getRandomProductWithSKUFromSearchResult()); 
		String SKUValue = cscockpitAutoshipCartTabPage.getCustomerSKUValueInCartTab(randomProductSequenceNumber);
		cscockpitAutoshipCartTabPage.searchSKUValueInCartTab(SKUValue);
		String noOfProductTobeAdded = "2";
		cscockpitAutoshipCartTabPage.addProductToCartPageTillRequiredDistinctProducts(noOfProductTobeAdded);
		cscockpitAutoshipCartTabPage.clickCheckoutBtnInCartTab();
		String totalNoOfProductInCart = ""+cscockpitAutoshipTemplateUpdateTabPage.getCountOfProduct();
		s_assert.assertTrue(totalNoOfProductInCart.contains(noOfProductTobeAdded), "Expected count of product is "+noOfProductTobeAdded+" Actual on UI is "+totalNoOfProductInCart);
		s_assert.assertAll();
	}

	//Hybris Project-130:change shipping method on autoship - PC
	@Test
	public void testVerifyChangeShippingMethodOnAutoshipPC_130() throws InterruptedException{
		RFO_DB = driver.getDBNameRFO();
		List<Map<String, Object>> randomPCList =  null;
		String pcEmailID = null;
		String country = null;
		if(driver.getCountry().equalsIgnoreCase("ca")){
			country= TestConstants.COUNTRY_DD_VALUE_CA;
		}else{
			country= TestConstants.COUNTRY_DD_VALUE_US;
		}
		storeFrontHomePage = new StoreFrontHomePage(driver);
		driver.get(driver.getStoreFrontURL()+"/"+driver.getCountry());
		String accountID;
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

		//get emailId of username
		List<Map<String, Object>> randomPCUsernameList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_EMAIL_ID_FROM_ACCOUNT_ID,accountID),RFO_DB);
		pcEmailID = String.valueOf(getValueFromQueryResult(randomPCUsernameList, "EmailAddress"));  
		logger.info("emaild of username "+pcEmailID); 
		storeFrontPCUserPage.hoverOnShopLinkAndClickAllProductsLinksAfterLogin();  
		storeFrontHomePage.clickOnAddToPcPerksButton();
		storeFrontUpdateCartPage = storeFrontCartAutoShipPage.clickUpdateMoreInfoLink();
		storeFrontUpdateCartPage.clickOnEditShipping();
		String selectedShippingMethod = storeFrontUpdateCartPage.selectAndGetShippingMethodName();
		logger.info("Shipping Method selected is: "+selectedShippingMethod);
		storeFrontUpdateCartPage.clickOnUpdateCartShippingNextStepBtn();
		storeFrontUpdateCartPage.clickOnNextStepBtn();
		storeFrontUpdateCartPage.clickUpdateCartBtn();  
		//assert in CSCockpit
		driver.get(driver.getCSCockpitURL());  
		cscockpitCustomerSearchTabPage = cscockpitLoginPage.clickLoginBtn();
		cscockpitCustomerSearchTabPage.selectCustomerTypeFromDropDownInCustomerSearchTab("PC");
		cscockpitCustomerSearchTabPage.selectCountryFromDropDownInCustomerSearchTab(country);
		cscockpitCustomerSearchTabPage.selectAccountStatusFromDropDownInCustomerSearchTab("Active");
		cscockpitCustomerSearchTabPage.enterEmailIdInSearchFieldInCustomerSearchTab(pcEmailID);
		cscockpitCustomerSearchTabPage.clickSearchBtn();
		String randomCustomerSequenceNumber = String.valueOf(cscockpitCustomerSearchTabPage.getRandomCustomerFromSearchResult());
		cscockpitCustomerSearchTabPage.clickAndReturnCIDNumberInCustomerSearchTab(randomCustomerSequenceNumber);
		cscockpitCustomerTabPage.getAndClickAutoshipIDHavingTypeAsPCAutoshipAndStatusIsPending();
		s_assert.assertTrue(cscockpitAutoshipTemplateTabPage.getShippingMethodNameFromUIUnderShippingAddressInAutoshipTemplateTab().contains(selectedShippingMethod), "Expected shipping method name on cscockpit is "+selectedShippingMethod+" Actual on UI is "+cscockpitAutoshipTemplateTabPage.getShippingMethodNameFromUIUnderShippingAddressInAutoshipTemplateTab());
		s_assert.assertAll();
	}
}
