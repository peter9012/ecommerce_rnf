package com.rf.test.website.storeFront.hybris.billingAndShipping;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import com.rf.core.utils.CommonUtils;
import com.rf.core.utils.DBUtil;
import com.rf.core.website.constants.TestConstants;
import com.rf.core.website.constants.dbQueries.DBQueries_RFO;
import com.rf.pages.website.storeFront.StoreFrontAccountInfoPage;
import com.rf.pages.website.storeFront.StoreFrontBillingInfoPage;
import com.rf.pages.website.storeFront.StoreFrontCartAutoShipPage;
import com.rf.pages.website.storeFront.StoreFrontConsultantPage;
import com.rf.pages.website.storeFront.StoreFrontHomePage;
import com.rf.pages.website.storeFront.StoreFrontOrdersPage;
import com.rf.pages.website.storeFront.StoreFrontPCUserPage;
import com.rf.pages.website.storeFront.StoreFrontUpdateCartPage;
import com.rf.test.website.RFWebsiteBaseTest;
import com.rf.test.website.RFStoreFrontWebsiteBaseTest;

public class AddBillingTest extends RFStoreFrontWebsiteBaseTest{
	private static final Logger logger = LogManager
			.getLogger(AddBillingTest.class.getName());

	private StoreFrontHomePage storeFrontHomePage;
	private StoreFrontBillingInfoPage storeFrontBillingInfoPage;
	private StoreFrontOrdersPage storeFrontOrdersPage;
	private StoreFrontCartAutoShipPage storeFrontCartAutoShipPage;
	private StoreFrontUpdateCartPage storeFrontUpdateCartPage;
	private StoreFrontConsultantPage storeFrontConsultantPage;
	private StoreFrontPCUserPage storeFrontPCUserPage;
	private StoreFrontAccountInfoPage storeFrontAccountInfoPage;
	private String city = null;
	private String state = null;
	private String phoneNumber = null;
	private String postalCode = null;
	private String kitName = null;
	private String regimenName = null;
	private String enrollmentType = null;
	private String addressLine1 = null;
	private String country = null;
	private String RFO_DB = null;
	private String env = null;
	private int randomNum; 	
	List<Map<String, Object>> randomConsultantList =  null;
	String consultantEmailID = null;
	String accountID = null;

	@BeforeClass
	public void setupDataForAddBilling() throws InterruptedException{	
		storeFrontHomePage = new StoreFrontHomePage(driver);
		storeFrontBillingInfoPage = new StoreFrontBillingInfoPage(driver);
		storeFrontOrdersPage = new StoreFrontOrdersPage(driver);
		storeFrontCartAutoShipPage = new StoreFrontCartAutoShipPage(driver);
		storeFrontUpdateCartPage = new StoreFrontUpdateCartPage(driver);
		storeFrontPCUserPage = new StoreFrontPCUserPage(driver);
		storeFrontAccountInfoPage = new StoreFrontAccountInfoPage(driver);

		randomNum = CommonUtils.getRandomNum(10000, 1000000);
		RFO_DB = driver.getDBNameRFO(); 
		env = driver.getEnvironment();
		country = driver.getCountry();

		kitName = TestConstants.KIT_NAME_BIG_BUSINESS; 	
		enrollmentType = TestConstants.EXPRESS_ENROLLMENT;
		regimenName = TestConstants.REGIMEN_NAME_REDEFINE;

		if(country.equalsIgnoreCase("CA")){				 
			addressLine1 = TestConstants.ADDRESS_LINE_1_CA;
			city = TestConstants.CITY_CA;
			postalCode = TestConstants.POSTAL_CODE_CA;
			phoneNumber = TestConstants.PHONE_NUMBER_CA;
			state = TestConstants.PROVINCE_CA;
		}else{			
			addressLine1 = TestConstants.ADDRESS_LINE_1_US;
			city = TestConstants.CITY_US;
			state = TestConstants.STATE_US;
			postalCode = TestConstants.NEW_ADDRESS_POSTAL_CODE_US;
			phoneNumber = TestConstants.NEW_ADDRESS_PHONE_NUMBER_US;			
		}

		while(true){
			randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_CONSULTANT_WITH_ORDERS_AND_AUTOSHIPS_RFO,countryId),RFO_DB);
			consultantEmailID = (String) getValueFromQueryResult(randomConsultantList, "UserName");  
			accountID = String.valueOf(getValueFromQueryResult(randomConsultantList, "AccountID"));
			logger.info("Account Id of the user is "+accountID);
			//storeFrontHomePage = new StoreFrontHomePage(driver);
			storeFrontConsultantPage = storeFrontHomePage.loginAsConsultant(consultantEmailID, password);
			boolean isError = driver.getCurrentUrl().contains("error");
			if(isError){
				logger.info("Login error for the user "+consultantEmailID);
				driver.get(driver.getURL());
			}
			else{
				storeFrontConsultantPage.clickOnWelcomeDropDown();
				if(storeFrontHomePage.isEditCRPLinkPresent()==true)
					break;
				else
					driver.get(driver.getURL());				
			}

		}
		logger.info("login is successful");		
	}

	// Hybris Phase 2-2041 :: Version : 1 :: Add new billing profile on 'Billing Profile' page
	@Test(priority=1)
	public void testAddNewBillingProfileOnBillingProfilePage_2041() throws InterruptedException, SQLException{
//		String newBillingProfileName = TestConstants.NEW_BILLING_PROFILE_NAME_US+randomNum;
//		String lastName = "lN";
//		String defaultBillingProfileName = null;
//		randomNum = CommonUtils.getRandomNum(10000, 1000000);
//		storeFrontConsultantPage = storeFrontHomePage.clickRodanAndFieldsLogo();
//		storeFrontConsultantPage.clickOnWelcomeDropDown();
//		storeFrontBillingInfoPage = storeFrontConsultantPage.clickBillingInfoLinkPresentOnWelcomeDropDown();
//		s_assert.assertTrue(storeFrontBillingInfoPage.verifyBillingInfoPageIsDisplayed(),"Billing Info page has not been displayed");
//		int totalBillingProfiles = storeFrontBillingInfoPage.getTotalBillingAddressesDisplayed();
//		if(totalBillingProfiles>1){
//			defaultBillingProfileName = storeFrontBillingInfoPage.getDefaultSelectedBillingAddressName();
//		}
//		else{
//			defaultBillingProfileName = storeFrontBillingInfoPage.getFirstBillingProfileName();
//		}
//		logger.info("Default Billing profile is "+defaultBillingProfileName);
//		storeFrontBillingInfoPage.clickAddNewBillingProfileLink();
//		storeFrontBillingInfoPage.enterNewBillingCardNumber(TestConstants.CARD_NUMBER);
//		storeFrontBillingInfoPage.enterNewBillingNameOnCard(newBillingProfileName+" "+lastName);
//		storeFrontBillingInfoPage.selectNewBillingCardExpirationDate(TestConstants.CARD_EXP_MONTH, TestConstants.CARD_EXP_YEAR);
//		storeFrontBillingInfoPage.enterNewBillingSecurityCode(TestConstants.SECURITY_CODE);
//		storeFrontBillingInfoPage.selectNewBillingCardAddress();
//		storeFrontBillingInfoPage.selectUseThisBillingProfileFutureAutoshipChkbox();
//		storeFrontBillingInfoPage.clickOnSaveBillingProfile();
//
//		//--------------- Verify that Newly added Billing profile is listed in the Billing profiles section-----------------------------------------------------------------------------------------------------
//
//		s_assert.assertTrue(storeFrontBillingInfoPage.isTheBillingAddressPresentOnPage(newBillingProfileName),"Newly added Billing profile is NOT listed on the billing info page");
//
//		//------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
//
//		s_assert.assertFalse(storeFrontBillingInfoPage.isDefaultBillingAddressSelected(newBillingProfileName),"Newly created billing profile is DEFAULT selected on the billing info page");
//		s_assert.assertTrue(storeFrontBillingInfoPage.isDefaultBillingAddressSelected(defaultBillingProfileName),"Old Default billing profile is not DEFAULT selected on the billing info page");
//
//		storeFrontHomePage.clickOnRodanAndFieldsLogo();
//		storeFrontHomePage.hoverOnShopLinkAndClickAllProductsLinksAfterLogin();
//		storeFrontHomePage.clickAddToBagButton(country);
//		s_assert.assertTrue(storeFrontHomePage.isCartPageDisplayed(), "Cart page is not displayed");
//		logger.info("Cart page is displayed");
//		storeFrontHomePage.clickOnCheckoutButton();
//		storeFrontHomePage.clickOnShippingAddressNextStepBtn();
//		s_assert.assertTrue(storeFrontHomePage.isTheBillingAddressPresentOnPage(newBillingProfileName),"Newly created Billing profile is NOT listed on the Adhoc cart");
//		s_assert.assertTrue(storeFrontHomePage.isBillingProfileIsSelectedByDefault(defaultBillingProfileName),"Old Default billing profile is not DEFAULT selected on the Adhoc cart");
//		storeFrontHomePage.clickOnRodanAndFieldsLogo();
//
//		storeFrontConsultantPage.clickOnWelcomeDropDown();
//		storeFrontCartAutoShipPage = storeFrontConsultantPage.clickEditCrpLinkPresentOnWelcomeDropDown();
//		storeFrontUpdateCartPage = storeFrontCartAutoShipPage.clickUpdateMoreInfoLink();
//		storeFrontUpdateCartPage.clickOnEditPaymentBillingProfile();
//
//		//------------------ Verify that CRP/PC cart contains the newly created billing profile address as selected ------------------------------------------------------------
//
//		s_assert.assertTrue(storeFrontUpdateCartPage.isNewBillingProfileIsSelectedByDefault(newBillingProfileName),"New Billing Profile is not selected by default on autoship cart page");
//
//		//-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
//
//		s_assert.assertAll();
		
		String newBillingProfileName = TestConstants.NEW_BILLING_PROFILE_NAME_US+randomNum;
		String lastName = "lN";
		String defaultBillingProfileName = null;
		randomNum = CommonUtils.getRandomNum(10000, 1000000);
		storeFrontBillingInfoPage = storeFrontConsultantPage.clickBillingInfoLinkPresentOnWelcomeDropDown();
		s_assert.assertTrue(storeFrontBillingInfoPage.verifyBillingInfoPageIsDisplayed(),"Billing Info page has not been displayed");
		int totalBillingProfiles = storeFrontBillingInfoPage.getTotalBillingAddressesDisplayed();
		if(totalBillingProfiles>1){
			defaultBillingProfileName = storeFrontBillingInfoPage.getDefaultSelectedBillingAddressName();
		}
		else{
			defaultBillingProfileName = storeFrontBillingInfoPage.getFirstBillingProfileName();
		}
		logger.info("Default Billing profile is "+defaultBillingProfileName);
		storeFrontBillingInfoPage.clickAddNewBillingProfileLink();
		storeFrontBillingInfoPage.enterNewBillingCardNumber(TestConstants.CARD_NUMBER);
		storeFrontBillingInfoPage.enterNewBillingNameOnCard(newBillingProfileName+" "+lastName);
		storeFrontBillingInfoPage.selectNewBillingCardExpirationDate(TestConstants.CARD_EXP_MONTH, TestConstants.CARD_EXP_YEAR);
		storeFrontBillingInfoPage.enterNewBillingSecurityCode(TestConstants.SECURITY_CODE);
		storeFrontBillingInfoPage.selectNewBillingCardAddress();
		storeFrontBillingInfoPage.selectUseThisBillingProfileFutureAutoshipChkbox();
		storeFrontBillingInfoPage.clickOnSaveBillingProfile();

		//--------------- Verify that Newly added Billing profile is listed in the Billing profiles section-----------------------------------------------------------------------------------------------------

		s_assert.assertTrue(storeFrontBillingInfoPage.isTheBillingAddressPresentOnPage(newBillingProfileName),"Newly added Billing profile is NOT listed on the billing info page");

		//------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

		s_assert.assertFalse(storeFrontBillingInfoPage.isDefaultBillingAddressSelected(newBillingProfileName),"Newly created billing profile is DEFAULT selected on the billing info page");
		s_assert.assertTrue(storeFrontBillingInfoPage.isDefaultBillingAddressSelected(defaultBillingProfileName),"Old Default billing profile is not DEFAULT selected on the billing info page");

		storeFrontHomePage.clickRFStamp();
		storeFrontHomePage.clickRFMenuBars();
		storeFrontHomePage.clickShopSkinCareOnMenuBar();
		storeFrontHomePage.clickAllProductsLink();
		storeFrontHomePage.clickAddToBagButton(country);
		s_assert.assertTrue(storeFrontHomePage.isCartPageDisplayed(), "Cart page is not displayed");
		logger.info("Cart page is displayed");
		storeFrontHomePage.clickOnCheckoutButton();
		storeFrontHomePage.clickOnShippingAddressNextStepBtn();
		s_assert.assertTrue(storeFrontHomePage.isTheBillingAddressPresentOnPage(newBillingProfileName),"Newly created Billing profile is NOT listed on the Adhoc cart");
		s_assert.assertTrue(storeFrontHomePage.isBillingProfileIsSelectedByDefault(defaultBillingProfileName),"Old Default billing profile is not DEFAULT selected on the Adhoc cart");
		storeFrontHomePage.clickOnRodanAndFieldsLogo();

		storeFrontConsultantPage.clickOnWelcomeDropDown();
		storeFrontCartAutoShipPage = storeFrontConsultantPage.clickEditCrpLinkPresentOnWelcomeDropDown();
		storeFrontUpdateCartPage = storeFrontCartAutoShipPage.clickUpdateMoreInfoLink();
		storeFrontUpdateCartPage.clickOnEditPaymentBillingProfile();

		//------------------ Verify that CRP/PC cart contains the newly created billing profile address as selected ------------------------------------------------------------

		s_assert.assertTrue(storeFrontUpdateCartPage.isNewBillingProfileIsSelectedByDefault(newBillingProfileName),"New Billing Profile is not selected by default on autoship cart page");

		//-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

		s_assert.assertAll();
	}

//	// Hybris Phase 2-2341:Add new billing profile | My Account | checkbox UN-CHECKED
//	@Test(priority=2)
//	public void testAddBillingProfileMyAccountFutureAutoshipCheckboxNotChecked_2341() throws InterruptedException{
//		String newBillingProfileName = TestConstants.NEW_BILLING_PROFILE_NAME_US+randomNum;
//		String lastName = "lN";
//		String defaultBillingProfileName = null;
//		randomNum = CommonUtils.getRandomNum(10000, 1000000);
//		storeFrontConsultantPage = storeFrontHomePage.clickRodanAndFieldsLogo();
//		storeFrontConsultantPage.clickOnWelcomeDropDown();
//		storeFrontBillingInfoPage = storeFrontConsultantPage.clickBillingInfoLinkPresentOnWelcomeDropDown();
//		s_assert.assertTrue(storeFrontBillingInfoPage.verifyBillingInfoPageIsDisplayed(),"Billing Info page has not been displayed");
//		int totalBillingProfiles = storeFrontBillingInfoPage.getTotalBillingAddressesDisplayed();
//		if(totalBillingProfiles>1){
//			defaultBillingProfileName = storeFrontBillingInfoPage.getDefaultSelectedBillingAddressName();
//		}
//		else{
//			defaultBillingProfileName = storeFrontBillingInfoPage.getFirstBillingProfileName();
//		}
//		logger.info("Default Billing profile is "+defaultBillingProfileName);
//		storeFrontBillingInfoPage.clickAddNewBillingProfileLink();
//		storeFrontBillingInfoPage.enterNewBillingCardNumber(TestConstants.CARD_NUMBER);
//		storeFrontBillingInfoPage.enterNewBillingNameOnCard(newBillingProfileName+" "+lastName);
//		storeFrontBillingInfoPage.selectNewBillingCardExpirationDate(TestConstants.CARD_EXP_MONTH, TestConstants.CARD_EXP_YEAR);
//		storeFrontBillingInfoPage.enterNewBillingSecurityCode(TestConstants.SECURITY_CODE);
//		storeFrontBillingInfoPage.selectNewBillingCardAddress();
//		storeFrontBillingInfoPage.clickOnSaveBillingProfile();
//
//		//--------------- Verify that Newly added Billing profile is listed in the Billing profiles section-----------------------------------------------------------------------------------------------------
//
//		s_assert.assertTrue(storeFrontBillingInfoPage.isTheBillingAddressPresentOnPage(newBillingProfileName),"Newly added Billing profile is NOT listed on the billing info page");
//
//		//------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
//
//		s_assert.assertFalse(storeFrontBillingInfoPage.isDefaultBillingAddressSelected(newBillingProfileName),"Newly created billing profile is DEFAULT selected on the billing info page");
//		s_assert.assertTrue(storeFrontBillingInfoPage.isDefaultBillingAddressSelected(defaultBillingProfileName),"Old Default billing profile is not DEFAULT selected on the billing info page");
//
//		storeFrontHomePage.clickOnRodanAndFieldsLogo();
//		storeFrontHomePage.hoverOnShopLinkAndClickAllProductsLinksAfterLogin();
//		storeFrontHomePage.clickAddToBagButton(country);
//		s_assert.assertTrue(storeFrontHomePage.isCartPageDisplayed(), "Cart page is not displayed");
//		logger.info("Cart page is displayed");
//		storeFrontHomePage.clickOnCheckoutButton();
//		storeFrontHomePage.clickOnShippingAddressNextStepBtn();
//		s_assert.assertTrue(storeFrontHomePage.isTheBillingAddressPresentOnPage(newBillingProfileName),"Newly created Billing profile is NOT listed on the Adhoc cart");
//		s_assert.assertTrue(storeFrontHomePage.isBillingProfileIsSelectedByDefault(defaultBillingProfileName),"Old Default billing profile is not DEFAULT selected on the Adhoc cart");
//		storeFrontHomePage.clickOnRodanAndFieldsLogo();
//
//		storeFrontConsultantPage.clickOnWelcomeDropDown();
//		storeFrontCartAutoShipPage = storeFrontConsultantPage.clickEditCrpLinkPresentOnWelcomeDropDown();
//		storeFrontUpdateCartPage = storeFrontCartAutoShipPage.clickUpdateMoreInfoLink();
//		storeFrontUpdateCartPage.clickOnEditPaymentBillingProfile();
//
//		//------------------ Verify that CRP/PC cart contains the newly created billing profile address as selected ------------------------------------------------------------
//
//		s_assert.assertTrue(storeFrontHomePage.isTheBillingAddressPresentOnPage(newBillingProfileName),"Newly created Billing profile is NOT listed on the autoship cart");
//		//s_assert.assertFalse(storeFrontHomePage.isBillingProfileIsSelectedByDefault(newBillingProfileName),"Old Default billing profile is not DEFAULT selected on the Autoship cart");
//
//		//-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
//
//		s_assert.assertAll();  
//	}
//
//	//Hybris Phase 2-4327:View new billing profile on 'Billing Profile' page	
//	@Test(enabled=false)
//	public void testViewNewBillingProfile_HP2_4327() throws InterruptedException, SQLException{
//		int totalBillingAddressesFromDB = 0;
//		List<Map<String, Object>> billingAddressCountList =  null;
//		storeFrontConsultantPage = storeFrontHomePage.clickRodanAndFieldsLogo();
//		storeFrontConsultantPage.clickOnWelcomeDropDown();
//		storeFrontBillingInfoPage = storeFrontConsultantPage.clickBillingInfoLinkPresentOnWelcomeDropDown();
//		s_assert.assertTrue(storeFrontBillingInfoPage.verifyBillingInfoPageIsDisplayed(),"Billing Info page has not been displayed");
//		billingAddressCountList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_BILLING_ADDRESS_COUNT_QUERY,consultantEmailID),RFO_DB);
//		totalBillingAddressesFromDB = (Integer) getValueFromQueryResult(billingAddressCountList, "count");
//		logger.info("Total Billing Profiles from RFO DB are "+totalBillingAddressesFromDB);
//		s_assert.assertEquals(totalBillingAddressesFromDB,storeFrontBillingInfoPage.getTotalBillingAddressesDisplayed(),"Billing Addresses count on UI is "+storeFrontBillingInfoPage.getTotalBillingAddressesDisplayed()+" while the total billing addresses in DB is "+totalBillingAddressesFromDB);		
//		s_assert.assertAll();
//	}
//
//	// Hybris Phase 2-2042 :: Version : 1 :: Add billing profile during checkout 
//	@Test(priority=4)
//	public void testAddBillingProfileDuringCheckout_2042() throws InterruptedException{
//		randomNum = CommonUtils.getRandomNum(10000, 1000000);
//		String newBillingProfileName = TestConstants.NEW_BILLING_PROFILE_NAME_US+randomNum;
//		String lastName = "lN";
//		storeFrontConsultantPage = storeFrontHomePage.clickRodanAndFieldsLogo();
//		storeFrontConsultantPage.hoverOnShopLinkAndClickAllProductsLinksAfterLogin();
//		storeFrontUpdateCartPage.clickOnBuyNowButton();
//		storeFrontUpdateCartPage.clickOnCheckoutButton();
//		storeFrontUpdateCartPage.clickOnShippingAddressNextStepBtn();
//		storeFrontUpdateCartPage.clickAddNewBillingProfileLink();
//		storeFrontUpdateCartPage.enterNewBillingCardNumber(TestConstants.CARD_NUMBER);
//		storeFrontUpdateCartPage.enterNewBillingNameOnCard(newBillingProfileName+" "+lastName);
//		storeFrontUpdateCartPage.selectNewBillingCardExpirationDate();
//		storeFrontUpdateCartPage.enterNewBillingSecurityCode(TestConstants.SECURITY_CODE);
//		storeFrontUpdateCartPage.selectNewBillingCardAddress();
//		storeFrontUpdateCartPage.clickOnSaveBillingProfile();
//		storeFrontUpdateCartPage.clickOnBillingNextStepBtn(); 
//		storeFrontUpdateCartPage.clickBillingEditAfterSave();
//		s_assert.assertTrue(storeFrontUpdateCartPage.isNewBillingProfileIsSelectedByDefaultAfterClickOnEdit(newBillingProfileName),"New Billing Profile is not selected by default on CRP cart page");
//		storeFrontUpdateCartPage.clickOnBillingNextStepBtn();
//		storeFrontUpdateCartPage.clickPlaceOrderBtn();
//		storeFrontConsultantPage = storeFrontUpdateCartPage.clickRodanAndFieldsLogo();
//		storeFrontConsultantPage.clickOnWelcomeDropDown();
//		storeFrontBillingInfoPage = storeFrontConsultantPage.clickBillingInfoLinkPresentOnWelcomeDropDown();
//		s_assert.assertTrue(storeFrontBillingInfoPage.verifyBillingInfoPageIsDisplayed(),"Billing Info page has not been displayed");
//
//		//--------------- Verify that Newly added Billing profile is listed in the Billing profiles section-----------------------------------------------------------------------------------------------------
//
//		s_assert.assertTrue(storeFrontBillingInfoPage.isTheBillingAddressPresentOnPage(newBillingProfileName),"Newly added Billing profile is NOT listed on the page");
//
//		//------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
//
//		//--------------- Verify that Newly added Billing profile is default selected in the Billing profiles section-----------------------------------------------------------------------------------------------------
//
//		s_assert.assertTrue(storeFrontBillingInfoPage.isDefaultAddressRadioBtnSelected(newBillingProfileName),"Newly added Billing profile is NOT default on the page");
//
//		//------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
//
//		s_assert.assertAll();
//
//	}
//
//	//Hybris Phase 2-2043:Add billing profile in autoship template
//	@Test(priority=5)
//	public void testAddBillingAutoshipCartFutureCheckboxSelected_2043() throws InterruptedException{ 
//		randomNum = CommonUtils.getRandomNum(10000, 1000000);
//		String newBillingProfileName = TestConstants.NEW_BILLING_PROFILE_NAME_US+randomNum;
//		String lastName = "lN";
//		storeFrontConsultantPage = storeFrontHomePage.clickRodanAndFieldsLogo();
//		storeFrontConsultantPage.clickOnWelcomeDropDown();
//		storeFrontCartAutoShipPage = storeFrontConsultantPage.clickEditCrpLinkPresentOnWelcomeDropDown();
//		storeFrontUpdateCartPage = storeFrontCartAutoShipPage.clickUpdateMoreInfoLink();
//		storeFrontUpdateCartPage.clickOnEditPaymentBillingProfile();
//		storeFrontUpdateCartPage.clickAddNewBillingProfileLink();
//		storeFrontUpdateCartPage.enterNewBillingCardNumber(TestConstants.CARD_NUMBER);
//		storeFrontUpdateCartPage.enterNewBillingNameOnCard(newBillingProfileName+" "+lastName);
//		storeFrontUpdateCartPage.selectNewBillingCardExpirationDate();
//		storeFrontUpdateCartPage.enterNewBillingSecurityCode(TestConstants.SECURITY_CODE);
//		storeFrontUpdateCartPage.selectNewBillingCardAddress();
//		storeFrontUpdateCartPage.selectUseThisBillingProfileFutureAutoshipChkbox();
//		storeFrontUpdateCartPage.clickOnSaveBillingProfile();
//		storeFrontUpdateCartPage.clickOnBillingNextStepBtn(); 
//		storeFrontUpdateCartPage.clickOnEditPaymentBillingProfile();
//		s_assert.assertTrue(storeFrontUpdateCartPage.isNewBillingProfileIsSelectedByDefault(newBillingProfileName),"New Billing Profile is not selected by default on CRP cart page");
//		storeFrontUpdateCartPage.clickOnBillingNextStepBtn();
//		storeFrontUpdateCartPage.clickUpdateCartBtn();
//		storeFrontConsultantPage = storeFrontUpdateCartPage.clickRodanAndFieldsLogo();
//		storeFrontConsultantPage.clickOnWelcomeDropDown();
//		storeFrontBillingInfoPage = storeFrontConsultantPage.clickBillingInfoLinkPresentOnWelcomeDropDown();
//		s_assert.assertTrue(storeFrontBillingInfoPage.verifyBillingInfoPageIsDisplayed(),"Billing Info page has not been displayed");
//
//		//--------------- Verify that Newly added Billing profile is listed in the Billing profiles section-----------------------------------------------------------------------------------------------------
//
//		s_assert.assertTrue(storeFrontBillingInfoPage.isTheBillingAddressPresentOnPage(newBillingProfileName),"Newly added Billing profile is NOT listed on the page");
//
//		//------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
//
//		//--------------- Verify that Newly added Billing profile is NOT default selected in the Billing profiles section-----------------------------------------------------------------------------------------------------
//
//		s_assert.assertFalse(storeFrontBillingInfoPage.isDefaultAddressRadioBtnSelected(newBillingProfileName),"Newly added Billing profile is NOT default on the page");
//
//		//------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
//
//		s_assert.assertAll();
//	}
//
//	//Hybris Project-4467 ADD a billing profile from AUTOSHIP CART page, having "Use this billing profile for your future auto-ship" check box NOT CHECKED
//	@Test(priority=6)
//	public void testAddBillingAutoshipCartFutureCheckboxNotSelected_4467() throws InterruptedException{  
//		randomNum = CommonUtils.getRandomNum(10000, 1000000);
//		String newBillingProfileName = TestConstants.NEW_BILLING_PROFILE_NAME_US+randomNum;
//		String lastName = "lN";
//		storeFrontConsultantPage = storeFrontHomePage.clickRodanAndFieldsLogo();
//		storeFrontConsultantPage.clickOnWelcomeDropDown();
//		storeFrontCartAutoShipPage = storeFrontConsultantPage.clickEditCrpLinkPresentOnWelcomeDropDown();
//		storeFrontUpdateCartPage = storeFrontCartAutoShipPage.clickUpdateMoreInfoLink();
//		storeFrontUpdateCartPage.clickOnEditPaymentBillingProfile();
//		storeFrontUpdateCartPage.clickAddNewBillingProfileLink();
//		storeFrontUpdateCartPage.enterNewBillingCardNumber(TestConstants.CARD_NUMBER);
//		storeFrontUpdateCartPage.enterNewBillingNameOnCard(newBillingProfileName+" "+lastName);
//		storeFrontUpdateCartPage.selectNewBillingCardExpirationDate();
//		storeFrontUpdateCartPage.enterNewBillingSecurityCode(TestConstants.SECURITY_CODE);
//		storeFrontUpdateCartPage.selectNewBillingCardAddress();
//		storeFrontUpdateCartPage.clickOnSaveBillingProfile();
//		storeFrontUpdateCartPage.clickOnBillingNextStepBtn(); 
//		storeFrontUpdateCartPage.clickOnEditPaymentBillingProfile();
//		s_assert.assertFalse(storeFrontUpdateCartPage.isNewBillingProfileIsSelectedByDefault(newBillingProfileName),"New Billing Profile is selected by default on CRP cart page");
//		storeFrontUpdateCartPage.clickOnBillingNextStepBtn();
//		storeFrontUpdateCartPage.clickUpdateCartBtn();
//		storeFrontConsultantPage = storeFrontUpdateCartPage.clickRodanAndFieldsLogo();
//		storeFrontConsultantPage.clickOnWelcomeDropDown();
//		storeFrontBillingInfoPage = storeFrontConsultantPage.clickBillingInfoLinkPresentOnWelcomeDropDown();
//		s_assert.assertTrue(storeFrontBillingInfoPage.verifyBillingInfoPageIsDisplayed(),"Billing Info page has not been displayed");
//
//		//--------------- Verify that Newly added Billing profile is listed in the Billing profiles section-----------------------------------------------------------------------------------------------------
//
//		s_assert.assertTrue(storeFrontBillingInfoPage.isTheBillingAddressPresentOnPage(newBillingProfileName),"Newly added Billing profile is NOT listed on the page");
//
//		//------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
//
//		//--------------- Verify that Newly added Billing profile is default selected in the Billing profiles section-----------------------------------------------------------------------------------------------------
//
//		s_assert.assertFalse(storeFrontBillingInfoPage.isDefaultAddressRadioBtnSelected(newBillingProfileName),"Newly added Billing profile is NOT default on the page");
//
//		//------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
//
//		s_assert.assertAll();
//	}
//
//	//Hybris Project-2389:Verify that QAS validation DO NOT get perform anytime user adds a billing address.
//	@Test(priority=7)
//	public void testQASValidationDoNotPerformAnyTimeUserAddsABillingAddress_2389() throws InterruptedException	{
//		randomNum = CommonUtils.getRandomNum(10000, 1000000);
//		String lastName = "lN";
//		String newBillingProfileName = TestConstants.NEW_BILLING_PROFILE_NAME_US+randomNum;
//		storeFrontConsultantPage = storeFrontHomePage.clickRodanAndFieldsLogo();
//		storeFrontConsultantPage.hoverOnShopLinkAndClickAllProductsLinksAfterLogin();  
//		storeFrontUpdateCartPage.clickAddToBagButton(driver.getCountry());
//		storeFrontUpdateCartPage.clickOnCheckoutButton();
//		//click Next on Shipping Address Section
//		storeFrontUpdateCartPage.clickOnShippingAddressNextStepBtn();
//		//Add a New Billing Address
//		storeFrontUpdateCartPage.clickAddNewBillingProfileLink();
//		storeFrontUpdateCartPage.enterNewBillingCardNumber(TestConstants.CARD_NUMBER);
//		storeFrontUpdateCartPage.enterNewBillingNameOnCard(newBillingProfileName+" "+lastName);
//		storeFrontUpdateCartPage.selectNewBillingCardExpirationDate();
//		storeFrontUpdateCartPage.enterNewBillingSecurityCode(TestConstants.SECURITY_CODE);
//		storeFrontUpdateCartPage.selectNewBillingCardAddress();
//		storeFrontUpdateCartPage.clickOnSaveBillingProfile();
//		//validate QAS Validation PopUp is Not Displayed after Adding a Billing Profile
//		s_assert.assertFalse(storeFrontUpdateCartPage.validateQASValidationPopUpIsDisplayed(),"QAS Validation PopUp is Displayed After Adding A Biling Profile");
//		s_assert.assertAll();
//	}
//
//	//Hybris Project-2044:Add billing profile during PC user or Retail user registration
//	@Test(priority=8)
//	public void testAddBillingProfileDuringPCRegistration_2044() throws InterruptedException{
//		randomNum = CommonUtils.getRandomNum(10000, 1000000);
//		String newBillingProfileName = TestConstants.NEW_BILLING_PROFILE_NAME+randomNum;
//		String lastName = "lN";
//		String firstName=TestConstants.FIRST_NAME+randomNum;
//		// Click on our product link that is located at the top of the page and then click in on quick shop
//		/*storeFrontHomePage.clickOnShopLink();
//			storeFrontHomePage.clickOnAllProductsLink();*/
//		navigateToStoreFrontBaseURL();
//		storeFrontHomePage.hoverOnShopLinkAndClickAllProductsLinks();
//
//		// Products are displayed?
//		s_assert.assertTrue(storeFrontHomePage.areProductsDisplayed(), "quickshop products not displayed");
//		logger.info("Quick shop products are displayed");
//
//		//Select a product with the price less than $80 and proceed to buy it
//		storeFrontHomePage.selectProductAndProceedToBuy();
//
//		//Click on Check out
//		storeFrontHomePage.clickOnCheckoutButton();
//
//		//Enter the User information and DO NOT check the "Become a Preferred Customer" checkbox and click the create account button
//		storeFrontHomePage.enterNewPCDetails(firstName, TestConstants.LAST_NAME+randomNum, password);
//
//		//Enter the Main account info and DO NOT check the "Become a Preferred Customer" and click next
//		storeFrontHomePage.enterMainAccountInfo();
//		logger.info("Main account details entered");
//
//		storeFrontHomePage.clickOnContinueWithoutSponsorLink();
//		storeFrontHomePage.clickOnNextButtonAfterSelectingSponsor();
//
//		storeFrontHomePage.clickOnShippingAddressNextStepBtn();
//		//Enter Billing Profile
//		storeFrontHomePage.enterNewBillingCardNumber(TestConstants.CARD_NUMBER);
//		storeFrontHomePage.enterNewBillingNameOnCard(newBillingProfileName+" "+lastName);
//		storeFrontHomePage.selectNewBillingCardExpirationDate();
//		storeFrontHomePage.enterNewBillingSecurityCode(TestConstants.SECURITY_CODE);
//		storeFrontHomePage.selectNewBillingCardAddress();
//		storeFrontHomePage.clickOnSaveBillingProfile();
//		storeFrontUpdateCartPage.clickOnBillingNextStepBtn(); 
//		storeFrontUpdateCartPage.clickBillingEditAfterSave();
//		s_assert.assertTrue(storeFrontUpdateCartPage.isNewBillingProfileIsSelectedByDefaultAfterClickOnEdit(newBillingProfileName),"New Billing Profile is not selected by default on CRP cart page");
//		storeFrontUpdateCartPage.clickOnBillingNextStepBtn();
//		storeFrontHomePage.clickOnPCPerksTermsAndConditionsCheckBoxes();
//		storeFrontUpdateCartPage.clickPlaceOrderBtn();
//		storeFrontUpdateCartPage.clickRodanAndFieldsLogo();
//		storeFrontPCUserPage.clickOnWelcomeDropDown();
//		storeFrontBillingInfoPage = storeFrontPCUserPage.clickBillingInfoLinkPresentOnWelcomeDropDown();
//		s_assert.assertTrue(storeFrontBillingInfoPage.verifyBillingInfoPageIsDisplayed(),"Billing Info page has not been displayed");
//
//		//--------------- Verify that Newly added Billing profile is listed in the Billing profiles section-----------------------------------------------------------------------------------------------------
//
//		s_assert.assertTrue(storeFrontBillingInfoPage.isTheBillingAddressPresentOnPage(newBillingProfileName),"Newly added Billing profile is NOT listed on the page");
//		storeFrontConsultantPage.clickOnWelcomeDropDown();
//		storeFrontCartAutoShipPage = storeFrontConsultantPage.clickEditCrpLinkPresentOnWelcomeDropDown();
//		storeFrontUpdateCartPage = storeFrontCartAutoShipPage.clickUpdateMoreInfoLink();
//		storeFrontUpdateCartPage.clickOnEditPaymentBillingProfile();
//
//		//------------------ Verify that CRP/PC cart contains the newly created billing profile address as selected ------------------------------------------------------------
//
//		s_assert.assertTrue(storeFrontUpdateCartPage.isNewBillingProfileIsSelectedByDefault(newBillingProfileName),"New Billing Profile is not selected by default on update cart page");
//
//		//-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
//
//		s_assert.assertAll();  
//	}
//
//	// Hybris Project-2045 :: Version : 1 :: Add billing address during consultant enrollment 
//	@Test(priority=9)
//	public void testAddBillingAddressConsultantEnrollment_2045() throws InterruptedException{
//		randomNum = CommonUtils.getRandomNum(10000, 1000000);
//		String socialInsuranceNumber = String.valueOf(CommonUtils.getRandomNum(100000000, 999999999));
//		storeFrontHomePage.openPWSSite(country, env);
//		String newBillingProfileName = TestConstants.NEW_BILLING_PROFILE_NAME_US+randomNum;
//		String lastName = "lN";
//		storeFrontHomePage.hoverOnBecomeAConsultantAndClickEnrollNowLink();
//		storeFrontHomePage.enterUserInformationForEnrollment(kitName, regimenName, enrollmentType, TestConstants.FIRST_NAME+randomNum, TestConstants.LAST_NAME+randomNum, password, addressLine1, city,state,postalCode, phoneNumber);
//		storeFrontHomePage.clickEnrollmentNextBtn();
//		//storeFrontHomePage.acceptTheVerifyYourShippingAddressPop();		
//		storeFrontHomePage.enterCardNumber(TestConstants.CARD_NUMBER);
//		storeFrontHomePage.enterNameOnCard(newBillingProfileName+" "+lastName);
//		storeFrontHomePage.selectNewBillingCardExpirationDate();
//		storeFrontHomePage.enterSecurityCode(TestConstants.SECURITY_CODE);
//		storeFrontHomePage.enterSocialInsuranceNumber(socialInsuranceNumber);
//		storeFrontHomePage.enterNameAsItAppearsOnCard(TestConstants.FIRST_NAME);
//		storeFrontHomePage.clickEnrollmentNextBtn();
//		s_assert.assertTrue(storeFrontHomePage.isTheTermsAndConditionsCheckBoxDisplayed(), "Terms and Conditions checkbox is not visible");
//		storeFrontHomePage.checkThePoliciesAndProceduresCheckBox();
//		storeFrontHomePage.checkTheIAcknowledgeCheckBox();		
//		storeFrontHomePage.checkTheIAgreeCheckBox();
//		storeFrontHomePage.clickOnEnrollMeBtn();
//		s_assert.assertTrue(storeFrontHomePage.verifyPopUpForTermsAndConditions(), "PopUp for terms and conditions is not visible");
//		storeFrontHomePage.checkTheTermsAndConditionsCheckBox();
//		storeFrontHomePage.clickOnChargeMyCardAndEnrollMeBtn();
//		storeFrontHomePage.clickOnConfirmAutomaticPayment();
//		s_assert.assertTrue(storeFrontHomePage.verifyCongratsMessage(), "Congrats Message is not visible");
//		storeFrontHomePage.clickOnRodanAndFieldsLogo();
//		storeFrontConsultantPage.clickOnWelcomeDropDown();
//		storeFrontOrdersPage = storeFrontConsultantPage.clickOrdersLinkPresentOnWelcomeDropDown();
//		storeFrontOrdersPage.clickAutoshipOrderNumber();
//
//		//------------------ Verify that autoship template contains the newly created billing profile ------------------------------------------------------------  
//
//		s_assert.assertTrue(storeFrontOrdersPage.isPaymentMethodContainsName(newBillingProfileName),"Autoship Template Payment Method doesn't contains the new billing profile even when future autoship checkbox is selected");
//
//		//-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
//
//		storeFrontConsultantPage.clickOnWelcomeDropDown();
//		storeFrontBillingInfoPage = storeFrontConsultantPage.clickBillingInfoLinkPresentOnWelcomeDropDown();
//		s_assert.assertTrue(storeFrontBillingInfoPage.verifyBillingInfoPageIsDisplayed(),"Billing Info page has not been displayed");
//
//		//--------------- Verify that Newly added Billing profile is listed in the Billing profiles section-----------------------------------------------------------------------------------------------------
//
//		s_assert.assertTrue(storeFrontBillingInfoPage.isTheBillingAddressPresentOnPage(newBillingProfileName),"Newly added Billing profile is NOT listed on the page");
//
//		//------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
//
//		s_assert.assertAll();
//	}
//
//	//Hybris Project-2046 :: Version : 1 :: Add billing profile during CRP enrollment through my account 
//	@Test(priority=10)
//	public void testAddNewBillingProfileDuringCRPEnrollment_2046() throws InterruptedException{
//		int randomNum = CommonUtils.getRandomNum(10000, 1000000);
//		int randomNumber = CommonUtils.getRandomNum(10000, 1000000);
//		String socialInsuranceNumber = String.valueOf(CommonUtils.getRandomNum(100000000, 999999999));
//		enrollmentType = TestConstants.STANDARD_ENROLLMENT;
//		String firstName = TestConstants.FIRST_NAME+randomNum;
//		String lastName = "lN";
//		String consultantEmail = firstName+TestConstants.EMAIL_ADDRESS_SUFFIX;
//		String newBillingProfileName = TestConstants.NEW_BILLING_PROFILE_NAME_US+randomNumber;
//		navigateToStoreFrontBaseURL();
//		//Enroll a consultant without CRP and pulse
//		storeFrontHomePage.hoverOnBecomeAConsultantAndClickEnrollNowLink();
//		storeFrontHomePage.searchCID();
//		storeFrontHomePage.mouseHoverSponsorDataAndClickContinue();
//		storeFrontHomePage.enterUserInformationForEnrollmentWithEmail(kitName, regimenName, enrollmentType, firstName, TestConstants.LAST_NAME,consultantEmail, password, addressLine1, city,state, postalCode, phoneNumber);
//		storeFrontHomePage.clickNextButton();
//
//		storeFrontHomePage.enterCardNumber(TestConstants.CARD_NUMBER);
//		storeFrontHomePage.enterNameOnCard(TestConstants.FIRST_NAME+randomNum);
//		storeFrontHomePage.selectNewBillingCardExpirationDate();
//		storeFrontHomePage.enterSecurityCode(TestConstants.SECURITY_CODE);
//		storeFrontHomePage.enterSocialInsuranceNumber(socialInsuranceNumber);
//		storeFrontHomePage.enterNameAsItAppearsOnCard(TestConstants.FIRST_NAME);
//		storeFrontHomePage.clickEnrollmentNextBtn();
//		storeFrontHomePage.uncheckPulseAndCRPEnrollment();
//		s_assert.assertTrue(storeFrontHomePage.verifySubsribeToPulseCheckBoxIsNotSelected(), "Subscribe to pulse checkbox selected after uncheck");
//		s_assert.assertTrue(storeFrontHomePage.verifyEnrollToCRPCheckBoxIsNotSelected(), "Enroll to CRP checkbox selected after uncheck");
//		storeFrontHomePage.clickEnrollmentNextBtn();
//		s_assert.assertTrue(storeFrontHomePage.isTheTermsAndConditionsCheckBoxDisplayed(), "Terms and Conditions checkbox is not visible");
//		storeFrontHomePage.checkThePoliciesAndProceduresCheckBox();
//		storeFrontHomePage.checkTheIAcknowledgeCheckBox();		
//		storeFrontHomePage.checkTheIAgreeCheckBox();
//		storeFrontHomePage.checkTheTermsAndConditionsCheckBox();
//		storeFrontHomePage.clickOnEnrollMeBtn();
//		s_assert.assertTrue(storeFrontHomePage.verifyCongratsMessage(), "Congrats Message is not visible");
//		storeFrontHomePage.clickOnRodanAndFieldsLogo();
//		s_assert.assertTrue(storeFrontHomePage.verifyWelcomeDropdownToCheckUserRegistered(), "User NOT registered successfully");
//
//		//Add New Billing Profile during enroll the consultant in CRP 
//		storeFrontConsultantPage.clickOnWelcomeDropDown();
//		storeFrontAccountInfoPage = storeFrontConsultantPage.clickAccountInfoLinkPresentOnWelcomeDropDown();
//		s_assert.assertTrue(storeFrontAccountInfoPage.verifyAccountInfoPageIsDisplayed(),"shipping info page has not been displayed");
//		storeFrontAccountInfoPage.clickOnYourAccountDropdown();
//		storeFrontAccountInfoPage.clickOnAutoShipStatus();
//		storeFrontAccountInfoPage.clickOnEnrollInCRP();
//		storeFrontHomePage.clickOnAddToCRPButtonCreatingCRPUnderBizSite();
//		storeFrontHomePage.clickOnCRPCheckout();
//		storeFrontHomePage.clickOnUpdateCartShippingNextStepBtnDuringEnrollment();
//		storeFrontBillingInfoPage.clickAddNewBillingProfileLink();
//		storeFrontBillingInfoPage.enterNewBillingCardNumber(TestConstants.CARD_NUMBER);
//		storeFrontBillingInfoPage.enterNewBillingNameOnCard(newBillingProfileName+" "+lastName);
//		storeFrontBillingInfoPage.selectNewBillingCardExpirationDate(TestConstants.CARD_EXP_MONTH, TestConstants.CARD_EXP_YEAR);
//		storeFrontBillingInfoPage.enterNewBillingSecurityCode(TestConstants.SECURITY_CODE);
//		storeFrontBillingInfoPage.selectNewBillingCardAddress();
//		//storeFrontBillingInfoPage.selectUseThisBillingProfileFutureAutoshipChkbox();
//		storeFrontBillingInfoPage.clickOnSaveBillingProfile();
//		s_assert.assertTrue(storeFrontBillingInfoPage.isTheBillingAddressPresentOnPage(newBillingProfileName),"Newly Created Billing Address not present on page");
//		s_assert.assertTrue(storeFrontBillingInfoPage.isDefaultAddressRadioBtnSelected(newBillingProfileName),"Radio button is not selected for billing address");
//		storeFrontHomePage.clickOnBillingNextStepBtn();
//		storeFrontHomePage.clickOnSetupCRPAccountBtn();
//		s_assert.assertTrue(storeFrontHomePage.verifyOrderConfirmation(), "Order Confirmation Message has not been displayed");
//		storeFrontHomePage.clickOnGoToMyAccountToCheckStatusOfCRP();
//		storeFrontHomePage.clickOnYourAccountDropdown();
//		storeFrontAccountInfoPage.clickOnAutoShipStatus();
//		s_assert.assertTrue(storeFrontAccountInfoPage.verifyCurrentCRPStatus(), "Current CRP Status has not been Enrolled");
//		storeFrontConsultantPage.clickOnWelcomeDropDown();
//		storeFrontBillingInfoPage =storeFrontConsultantPage.clickBillingInfoLinkPresentOnWelcomeDropDown();
//		s_assert.assertTrue(storeFrontBillingInfoPage.verifyBillingInfoPageIsDisplayed(),"billing info page has not been displayed");
//		s_assert.assertTrue(storeFrontBillingInfoPage.isTheBillingAddressPresentOnPage(newBillingProfileName),"Newly Created Billing Address not present on Billing info page");
//		//s_assert.assertTrue(storeFrontBillingInfoPage.isAutoshipOrderAddressTextPresent(newBillingProfileName),"AutoShip order text is not present under billing address");
//		s_assert.assertFalse(storeFrontBillingInfoPage.isDefaultAddressRadioBtnSelected(newBillingProfileName),"Radio button is selected for newly created billing address");
//		s_assert.assertAll();
//
//	}
	
	
}
