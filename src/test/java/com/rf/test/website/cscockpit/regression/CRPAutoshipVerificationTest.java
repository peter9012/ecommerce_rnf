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
	private StoreFrontUpdateCartPage storeFrontUpdateCartPage;


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
	}

	private String RFO_DB = null;

	//Hybris Project-1735:To verify Add New payment profile functionality in the Checkout Page
	@Test(enabled=false)//WIP
	public void testToVerifyAddNewPaymentProfileFunctionalityInTheCheckoutPage_1735(){
		String randomOrderSequenceNumber = null;
		String randomProductSequenceNumber = null;
		RFO_DB = driver.getDBNameRFO();
		String consultantEmailID = null;
		String SKUValue = null;

		int randomNum = CommonUtils.getRandomNum(10000, 1000000);
		String attendeeLastName="IN";
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

		cscockpitOrderSearchTabPage = new CSCockpitOrderSearchTabPage(driver);
		cscockpitOrderTabPage = new CSCockpitOrderTabPage(driver);
		cscockpitCartTabPage = new CSCockpitCartTabPage(driver);
		cscockpitCheckoutTabPage = new CSCockpitCheckoutTabPage(driver);
		cscockpitLoginPage = new CSCockpitLoginPage(driver);

		List<Map<String, Object>> randomConsultantList =  null;
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
		driver.get(driver.getCSCockpitURL());
		cscockpitCustomerSearchTabPage = cscockpitLoginPage.clickLoginBtn();
		cscockpitCustomerSearchTabPage.selectCustomerTypeFromDropDownInCustomerSearchTab("CONSULTANT");
		cscockpitCustomerSearchTabPage.selectCountryFromDropDownInCustomerSearchTab("United States");
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
		//---------------------FOR CA-----------------------
		driver.get(driver.getStoreFrontURL()+"/ca");

		int randomNumber=CommonUtils.getRandomNum(10000, 1000000);
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
		driver.get(driver.getCSCockpitURL());
		cscockpitCustomerSearchTabPage = cscockpitLoginPage.clickLoginBtn();
		cscockpitCustomerSearchTabPage.selectCustomerTypeFromDropDownInCustomerSearchTab("CONSULTANT");
		cscockpitCustomerSearchTabPage.selectCountryFromDropDownInCustomerSearchTab("Canada");
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
	@Test(enabled=false) //WIP
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

		s_assert.assertTrue(cscockpitAutoshipSearchTabPage.verifyCustomerNameCidFieldPresentOnAutoshipPage(),"cusotmer name and cid field not present on the page");
		s_assert.assertTrue(cscockpitAutoshipSearchTabPage.verifySponsorNameCidFieldPresentOnAutoshipPage(),"SponsorName field not present on the page");
		s_assert.assertTrue(cscockpitAutoshipSearchTabPage.verifyTemplateNumberCidFieldPresentOnAutoshipPage(),"TemplateNumber field not present on the page");
		s_assert.assertTrue(cscockpitAutoshipSearchTabPage.verifyRunSelectedButtonPresent(),"Run selected button is not present on the page");
		s_assert.assertTrue(cscockpitAutoshipSearchTabPage.verifySearchAutoshipButtonPresent(),"search autoship button is not present on the page");
		s_assert.assertAll();
	}

	//Hybris Project-1701:To verify CRP Autoship template Page UI
	@Test(enabled=false)//WIP
	public void testVerifyCRPAutoshipTemplate_1701() throws InterruptedException{
		RFO_DB = driver.getDBNameRFO();
		String randomCustomerSequenceNumber = null;
		String cid=null;
		String cidOfInactiveUser=null;
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

		cscockpitCustomerSearchTabPage = cscockpitLoginPage.clickLoginBtn();
		cscockpitCustomerSearchTabPage.selectCustomerTypeFromDropDownInCustomerSearchTab("CONSULTANT");
		//cscockpitCustomerSearchTabPage.selectCountryFromDropDownInCustomerSearchTab("United States");
		cscockpitCustomerSearchTabPage.selectAccountStatusFromDropDownInCustomerSearchTab("Active");
		cscockpitCustomerSearchTabPage.clickSearchBtn();
		randomCustomerSequenceNumber = String.valueOf(cscockpitCustomerSearchTabPage.getRandomCustomerFromSearchResult());
		cid=cscockpitCustomerSearchTabPage.clickAndReturnCIDNumberInCustomerSearchTab(randomCustomerSequenceNumber);
		//Verify Different Section on Customer tab page.
		s_assert.assertTrue(cscockpitCustomerTabPage.verifyAutoshipTemplateSectionInCustomerTab(),"AutoShip Template section is not on Customer Tab Page");
		s_assert.assertTrue(cscockpitCustomerTabPage.verifyCustomerOrderSectionInCustomerTab(),"Customer Order section is not on Customer Tab Page");
		s_assert.assertTrue(cscockpitCustomerTabPage.verifyCustomerBillingInfoSectionInCustomerTab(),"Customer Billing Info section is not on Customer Tab Page");
		s_assert.assertTrue(cscockpitCustomerTabPage.verifyCustomerAddressSectionInCustomerTab(),"Customer Address section is not on Customer Tab Page");
		autoshipNumber=cscockpitCustomerTabPage.getAndClickFirstAutoshipIDInCustomerTab();
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
		//Create an order in order from autoship template section.
		cscockpitAutoshipTemplateTabPage.clickRunNowButtonOnAutoshipTemplateTab();
		cscockpitAutoshipTemplateTabPage.clickOkForRegeneratedIdpopUp();
		s_assert.assertTrue(cscockpitAutoshipTemplateTabPage.verifyConfirmMessagePopUpIsAppearing(),"confirm message is not appearing for US");
		cscockpitAutoshipTemplateTabPage.clickOkConfirmMessagePopUp();
		//verify sub components of order from AutoShip Template section
		s_assert.assertTrue(cscockpitAutoshipTemplateTabPage.verifySectionsOfOrderFromAutoshipTemplateInAutoshipTemplateTab(orderNumberOfOrderFromAutoshipTemplate),"Order Number is not present in order from autoship template On autoship template page.");
		s_assert.assertTrue(cscockpitAutoshipTemplateTabPage.verifySectionsOfOrderFromAutoshipTemplateInAutoshipTemplateTab(orderStatusOfOrderFromAutoshipTemplate),"Order Status is not present in order from autoship template On autoship template page.");
		s_assert.assertTrue(cscockpitAutoshipTemplateTabPage.verifySectionsOfOrderFromAutoshipTemplateInAutoshipTemplateTab(runDateOfOrderFromAutoshipTemplate),"Run date is not present in order from autoship template On autoship template page.");
		s_assert.assertTrue(cscockpitAutoshipTemplateTabPage.verifySectionsOfOrderFromAutoshipTemplateInAutoshipTemplateTab(ShipDateOfOrderFromAutoshipTemplate),"Ship Date is not present in order from autoship template On autoship template page.");
		s_assert.assertTrue(cscockpitAutoshipTemplateTabPage.verifySectionsOfOrderFromAutoshipTemplateInAutoshipTemplateTab(orderTotalOfOrderFromAutoshipTemplate),"Order Total is not present in order from autoship template On autoship template page.");
		s_assert.assertTrue(cscockpitAutoshipTemplateTabPage.verifySectionsOfOrderFromAutoshipTemplateInAutoshipTemplateTab(failedReasonOfOrderFromAutoshipTemplate),"Failed Reason is not present in order from autoship template On autoship template page.");
		//click customer search tab.
		cscockpitAutoshipTemplateTabPage.clickCustomerSearchTab();
		cscockpitCustomerSearchTabPage.selectCustomerTypeFromDropDownInCustomerSearchTab("CONSULTANT");
		cscockpitCustomerSearchTabPage.selectAccountStatusFromDropDownInCustomerSearchTab("Inactive");
		cscockpitCustomerSearchTabPage.clickSearchBtn();
		String randomCustomerSequenceNumberOfInactiveUser = String.valueOf(cscockpitCustomerSearchTabPage.getRandomCustomerFromSearchResult());
		cidOfInactiveUser=cscockpitCustomerSearchTabPage.clickAndReturnCIDNumberInCustomerSearchTab(randomCustomerSequenceNumberOfInactiveUser);
		String autoshipNumberOfInactiveUser=cscockpitCustomerTabPage.getAndClickFirstAutoshipIDInCustomerTab();
		//verify cancel autoship and edit autoship link are not present for inactive user in autoship template header.
		s_assert.assertTrue(cscockpitAutoshipTemplateTabPage.verifyCancelAutoshipTemplateLinkInAutoshipTemplateTab(),"Cancel autoship link in Autoship template header section is present on Autoship template page for inactive user.");
		s_assert.assertFalse(cscockpitAutoshipTemplateTabPage.verifyEditAutoshipTemplateLinkInAutoshipTemplateTab(),"Edit autoship link in Autoship template header section is present on Autoship template page for inactive user.");
		//verify Update and run now  link are not present or disabled for inactive user in order from autoship template section.
		s_assert.assertFalse(cscockpitAutoshipTemplateTabPage.verifyUpdateLinkInOrderFromAutoshipTemplateInAutoshipTemplateTab(),"Update link in order from Autoship template section is active  on Autoship template page for inactive user.");
		s_assert.assertFalse(cscockpitAutoshipTemplateTabPage.verifyRunNowLinkInOrderFromAutoshipTemplateInAutoshipTemplateTab(),"Run now  link in order from Autoship template section is active on Autoship template page for inactive user.");
		s_assert.assertAll();
	}
	// Hybris Project-1702:To verify edit CRP Autoship template
	@Test(enabled=false)//WIP
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
		storeFrontHomePage.clickOnAutoshipCart();
		beforeProductCountInAutoshipCart=storeFrontUpdateCartPage.getProductCountOnAutoShipCartPage();
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
		//randomCustomerSequenceNumber="15";
		cid=cscockpitCustomerSearchTabPage.clickAndReturnCIDNumberInCustomerSearchTab(randomCustomerSequenceNumber);
		//Verify Autoship template Section on Customer tab page.
		s_assert.assertTrue(cscockpitCustomerTabPage.verifyAutoshipTemplateSectionInCustomerTab(),"AutoShip Template section is not on Customer Tab Page");
		autoshipNumber=cscockpitCustomerTabPage.getAndClickAutoshipIDHavingTypeAsCRPAutoshipInCustomerTab();
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
		subtotal=cscockpitAutoshipTemplateTabPage.getSubtotalInAutoshipTemplateTab();
		cscockpitAutoshipTemplateTabPage.updateQuantityOfSecondProduct("3");
		String subtotalAfterUpdateIncrement=cscockpitAutoshipTemplateTabPage.getSubtotalInAutoshipTemplateTab();
		s_assert.assertFalse(subtotal.equalsIgnoreCase(subtotalAfterUpdateIncrement), "Quantity of second product has not been updated");
		cscockpitAutoshipTemplateTabPage.updateQuantityOfSecondProduct("1");
		String subtotalAfterUpdateDecrement=cscockpitAutoshipTemplateTabPage.getSubtotalInAutoshipTemplateTab();
		s_assert.assertTrue(subtotal.equalsIgnoreCase(subtotalAfterUpdateDecrement), "Quantity of second product has not been updated");
		cscockpitAutoshipTemplateTabPage.enterOrderNote(orderNote);
		s_assert.assertTrue(cscockpitAutoshipTemplateTabPage.verifyOrderNoteAdded(orderNote), "order note has not been added");
		subtotal=cscockpitAutoshipTemplateTabPage.getSubtotalInAutoshipTemplateTab();
		cscockpitAutoshipTemplateTabPage.removeProductInOrderDetailInAutoshipTemplateTab(subtotal);
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
		logger.info("login is successful");
		driver.get(driver.getCSCockpitURL());
		cscockpitCustomerSearchTabPage = cscockpitLoginPage.clickLoginBtn();
		cscockpitCustomerSearchTabPage.selectCustomerTypeFromDropDownInCustomerSearchTab("CONSULTANT");
		cscockpitCustomerSearchTabPage.selectCountryFromDropDownInCustomerSearchTab("United States");
		cscockpitCustomerSearchTabPage.selectAccountStatusFromDropDownInCustomerSearchTab("Active");
		cscockpitCustomerSearchTabPage.enterEmailIdInSearchFieldInCustomerSearchTab(consultantEmailID);
		cscockpitCustomerSearchTabPage.clickSearchBtn();
		String newRandomCustomerSequenceNumber = String.valueOf(cscockpitCustomerSearchTabPage.getRandomCustomerFromSearchResult());
		//randomCustomerSequenceNumber="15";
		cid=cscockpitCustomerSearchTabPage.clickAndReturnCIDNumberInCustomerSearchTab(newRandomCustomerSequenceNumber);
		//Verify Autoship template Section on Customer tab page.
		s_assert.assertTrue(cscockpitCustomerTabPage.verifyAutoshipTemplateSectionInCustomerTab(),"AutoShip Template section is not on Customer Tab Page");
		autoshipNumber=cscockpitCustomerTabPage.getAndClickAutoshipIDHavingTypeAsCRPAutoshipInCustomerTab();
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
		subtotal=cscockpitAutoshipTemplateTabPage.getSubtotalInAutoshipTemplateTab();
		cscockpitAutoshipTemplateTabPage.updateQuantityOfSecondProduct("3");
		subtotalAfterUpdateIncrement=cscockpitAutoshipTemplateTabPage.getSubtotalInAutoshipTemplateTab();
		s_assert.assertFalse(subtotal.equalsIgnoreCase(subtotalAfterUpdateIncrement), "Quantity of second product has not been updated");
		cscockpitAutoshipTemplateTabPage.updateQuantityOfSecondProduct("1");
		subtotalAfterUpdateDecrement=cscockpitAutoshipTemplateTabPage.getSubtotalInAutoshipTemplateTab();
		s_assert.assertTrue(subtotal.equalsIgnoreCase(subtotalAfterUpdateDecrement), "Quantity of second product has not been updated");
		cscockpitAutoshipTemplateTabPage.enterOrderNote(orderNote);
		s_assert.assertTrue(cscockpitAutoshipTemplateTabPage.verifyOrderNoteAdded(orderNote), "order note has not been added");
		subtotal=cscockpitAutoshipTemplateTabPage.getSubtotalInAutoshipTemplateTab();
		cscockpitAutoshipTemplateTabPage.removeProductInOrderDetailInAutoshipTemplateTab(subtotal);
		subtotalAfterProductRemoval=cscockpitAutoshipTemplateTabPage.getSubtotalInAutoshipTemplateTab();
		s_assert.assertFalse(subtotal.equalsIgnoreCase(subtotalAfterProductRemoval),"product has not been removed successfully");
		cscockpitAutoshipTemplateTabPage.clickMenuButton();
		cscockpitAutoshipTemplateTabPage.clickLogoutButton();
		//Login to storefront and check the added item in mini cart page.
		driver.get(driver.getStoreFrontURL()+"/us");
		storeFrontConsultantPage = storeFrontHomePage.loginAsConsultant(consultantEmailID, password);
		storeFrontHomePage.clickOnAutoshipCart();
		afterProductCountInAutoshipCart=storeFrontUpdateCartPage.getProductCountOnAutoShipCartPage();
		s_assert.assertFalse(beforeProductCountInAutoshipCart.equalsIgnoreCase(afterProductCountInAutoshipCart), "Product has not been successfully in storefront cart page.");
		s_assert.assertAll();

	}
}
