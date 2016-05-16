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
import com.rf.pages.website.storeFront.StoreFrontAccountInfoPage;
import com.rf.pages.website.storeFront.StoreFrontConsultantPage;
import com.rf.pages.website.storeFront.StoreFrontHomePage;
import com.rf.pages.website.storeFront.StoreFrontOrdersPage;
import com.rf.pages.website.storeFront.StoreFrontPCUserPage;
import com.rf.pages.website.storeFront.StoreFrontRCUserPage;
import com.rf.pages.website.storeFront.StoreFrontUpdateCartPage;
import com.rf.test.website.RFWebsiteBaseTest;


public class PulseVerificationTest extends RFWebsiteBaseTest{
	private static final Logger logger = LogManager
			.getLogger(PulseVerificationTest.class.getName());

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
	private StoreFrontAccountInfoPage storeFrontAccountInfoPage;

	//-----------------------------------------------------------------------------------------------------------------

	public PulseVerificationTest() {
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
		storeFrontAccountInfoPage = new StoreFrontAccountInfoPage(driver);
	}

	private String RFO_DB = null;

	//Hybris Project-1717:To verify pulse Autoship template Page UI
	@Test
	public void testVerifyPulseAutoshipTemplatePageUI_1717() throws InterruptedException{
		String randomCustomerSequenceNumber = null;
		String consultantEmailID = null;
		String autoshipNumber=null;
		String accountID=null;

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
		String country = null;
		if(driver.getCountry().equalsIgnoreCase("ca")){
			country= TestConstants.COUNTRY_DD_VALUE_CA;
		}else{
			country= TestConstants.COUNTRY_DD_VALUE_US;
		}
		driver.get(driver.getStoreFrontURL()+"/"+driver.getCountry());
		List<Map<String, Object>> randomConsultantList =  null;
		List<Map<String, Object>> emailIdFromAccountIdList =  null;
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
		cscockpitCustomerSearchTabPage.clickCIDNumberInCustomerSearchTab(randomCustomerSequenceNumber);
		//Verify Different Section on Customer tab page.
		s_assert.assertTrue(cscockpitCustomerTabPage.verifyAutoshipTemplateSectionInCustomerTab(),"AutoShip Template section is not on Customer Tab Page");
		s_assert.assertTrue(cscockpitCustomerTabPage.verifyCustomerOrderSectionInCustomerTab(),"Customer Order section is not on Customer Tab Page");
		s_assert.assertTrue(cscockpitCustomerTabPage.verifyCustomerBillingInfoSectionInCustomerTab(),"Customer Billing Info section is not on Customer Tab Page");
		s_assert.assertTrue(cscockpitCustomerTabPage.verifyCustomerAddressSectionInCustomerTab(),"Customer Address section is not on Customer Tab Page");
		if(cscockpitCustomerTabPage.isPulseTemplateAutoshipIDHavingStatusIsPendingPresent()==false){
			cscockpitCustomerTabPage.clickCreatePulseTemplateBtn();
			cscockpitCustomerTabPage.clickCreatePulseTemplateBtnOnPopup();
		}
		autoshipNumber=cscockpitCustomerTabPage.getAndClickPulseTemplateAutoshipIDHavingStatusIsPending();
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
		//s_assert.assertTrue(cscockpitAutoshipTemplateTabPage.verifyAppliedPromotionsInAutoshipTemplateTab(appliedPromotionDescription),"Applied promotion description is not present&quot..
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
		String shippingAddressFromDB = firstName.trim()+" "+lastName.trim()+"\n"+ addressLine1+"\n"+locale+", "+region+" "+postalCode+"\n"+country.toUpperCase();
		shippingAddressFromDB = shippingAddressFromDB.trim().toLowerCase();
		logger.info("created Shipping Address is "+shippingAddressFromDB);
		//Assert Shipping address details.
		s_assert.assertTrue(cscockpitAutoshipTemplateTabPage.getShippingAddressNameInAutoshipTemplateTab().contains(firstName.trim()),"Shipping Address Name Expected is "+firstName.trim()+" While on UI"+cscockpitAutoshipTemplateTabPage.getShippingAddressNameInAutoshipTemplateTab());
		s_assert.assertTrue(cscockpitAutoshipTemplateTabPage.getShippingAddressLine1InAutoshipTemplateTab().contains(addressLine1),"Shipping Address Line 1 Expected is "+addressLine1+" While on UI"+cscockpitAutoshipTemplateTabPage.getShippingAddressLine1InAutoshipTemplateTab());
		s_assert.assertTrue(cscockpitAutoshipTemplateTabPage.getShippingAddressLocaleRegionPostCodeInAutoshipTemplateTab().contains(locale),"Shipping Address Locale Expected is "+locale+" While on UI"+cscockpitAutoshipTemplateTabPage.getShippingAddressLocaleRegionPostCodeInAutoshipTemplateTab());
		s_assert.assertTrue(cscockpitAutoshipTemplateTabPage.getShippingAddressLocaleRegionPostCodeInAutoshipTemplateTab().contains(region),"Shipping Address Region Expected is "+region+" While on UI"+cscockpitAutoshipTemplateTabPage.getShippingAddressLocaleRegionPostCodeInAutoshipTemplateTab());
		s_assert.assertTrue(cscockpitAutoshipTemplateTabPage.getShippingAddressLocaleRegionPostCodeInAutoshipTemplateTab().contains(postalCode),"Shipping Address PostCode Expected is "+postalCode+" While on UI"+cscockpitAutoshipTemplateTabPage.getShippingAddressLocaleRegionPostCodeInAutoshipTemplateTab());
		s_assert.assertTrue(cscockpitAutoshipTemplateTabPage.getShippingAddressCountryInAutoshipTemplateTab().contains(country),"Shipping Address Country Expected is "+country+" While on UI"+cscockpitAutoshipTemplateTabPage.getShippingAddressCountryInAutoshipTemplateTab());
		//Assert Billing address details.
		//s_assert.assertTrue(cscockpitAutoshipTemplateTabPage.getPaymentAddressNameInAutoshipTemplateTab().toLowerCase().contains(firstName.trim().toLowerCase()+" "+lastName.trim().toLowerCase()),"Payment Address Name Expected is "+firstName.trim()+" "+lastName.trim()+" While on UI"+cscockpitAutoshipTemplateTabPage.getPaymentAddressNameInAutoshipTemplateTab());
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
		driver.get(driver.getStoreFrontURL()+"/"+driver.getCountry());
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
		cscockpitCustomerSearchTabPage.selectCountryFromDropDownInCustomerSearchTab(country);
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

	//Hybris Project-1716:Verify Create a Pulse Autoship
	@Test
	public void testCreateAPulseAutoship_1716() throws InterruptedException{
		String randomCustomerSequenceNumber = null;
		String consultantEmailID = null;
		String accountID = null;
		RFO_DB = driver.getDBNameRFO();

		String country = null;
		if(driver.getCountry().equalsIgnoreCase("ca")){
			country= TestConstants.COUNTRY_DD_VALUE_CA;
		}else{
			country= TestConstants.COUNTRY_DD_VALUE_US;
		}
		driver.get(driver.getStoreFrontURL()+"/"+driver.getCountry());
		List<Map<String, Object>> randomConsultantList =  null;
		while(true){
			randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_CONSULTANT_WITH_ORDERS_AND_AUTOSHIPS_RFO,countryId),RFO_DB);
			consultantEmailID = (String) getValueFromQueryResult(randomConsultantList, "UserName");  
			accountID=String.valueOf(getValueFromQueryResult(randomConsultantList, "AccountID"));
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
		storeFrontAccountInfoPage = storeFrontConsultantPage.clickAccountInfoLinkPresentOnWelcomeDropDown();
		storeFrontAccountInfoPage.clickOnYourAccountDropdown();
		storeFrontAccountInfoPage.clickOnAutoShipStatus();
		storeFrontAccountInfoPage.clickOnCancelMyPulseSubscription();
		logout();

		List<Map<String, Object>> emailIdFromAccountIdList =  null;
		emailIdFromAccountIdList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_EMAIL_ID_FROM_ACCOUNT_ID,accountID),RFO_DB);
		consultantEmailID = String.valueOf(getValueFromQueryResult(emailIdFromAccountIdList, "EmailAddress"));
		driver.get(driver.getCSCockpitURL());		
		cscockpitCustomerSearchTabPage = cscockpitLoginPage.clickLoginBtn();
		cscockpitCustomerSearchTabPage.selectCustomerTypeFromDropDownInCustomerSearchTab("CONSULTANT");
		cscockpitCustomerSearchTabPage.selectCountryFromDropDownInCustomerSearchTab(country);
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
		String nextBillDate = storeFrontAccountInfoPage.getNextBillDateOfPulseTemplate();
		s_assert.assertTrue(storeFrontAccountInfoPage.getPulseStatusFromUI().contains("Enrolled"), "Expected pulse status is Enrolled But on UI"+storeFrontAccountInfoPage.getPulseStatusFromUI());
		s_assert.assertTrue(nextDueDate.contains(nextBillDate.trim()), "Expected next bill date of pulse template is "+nextDueDate+"actual on UI is "+nextBillDate);
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
		String country = null;
		if(driver.getCountry().equalsIgnoreCase("ca")){
			country= TestConstants.COUNTRY_DD_VALUE_CA;
		}else{
			country= TestConstants.COUNTRY_DD_VALUE_US;
		}
		List<Map<String, Object>> randomConsultantList =  null;
		List<Map<String, Object>> emailIdFromAccountIdList =  null;
		driver.get(driver.getStoreFrontURL()+"/"+driver.getCountry());
		while(true){
			randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_CONSULTANT_WITH_ORDERS_AND_AUTOSHIPS_RFO,countryId),RFO_DB);
			//			consultantEmailID = (String) getValueFromQueryResult(randomConsultantList, "UserName");
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
		driver.get(driver.getStoreFrontURL()+"/"+driver.getCountry());
		storeFrontConsultantPage = storeFrontHomePage.loginAsConsultant(consultantEmailID, password);
		storeFrontConsultantPage.clickOnWelcomeDropDown();
		storeFrontAccountInfoPage = storeFrontConsultantPage.clickAccountInfoLinkPresentOnWelcomeDropDown();
		storeFrontAccountInfoPage.clickOnYourAccountDropdown();
		storeFrontAccountInfoPage.clickOnAutoShipStatus();
		String crpDate = storeFrontAccountInfoPage.getNextDueDateOfPulseSubscriptionTemplate();
		s_assert.assertTrue(crpDate.trim().split("\\ ")[1].contains(day.split("\\,")[0]), "Expected next day of CRP is "+day.split("\\,")[0]+"Actual on UI in storeFront "+crpDate);
		logout();
		s_assert.assertAll();
	}
}