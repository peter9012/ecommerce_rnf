package com.rf.test.website.storeFront.hybris.billingAndShipping;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.annotations.AfterClass;
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

public class AddEditBillingTest extends RFWebsiteBaseTest{
	private static final Logger logger = LogManager
			.getLogger(AddEditBillingTest.class.getName());

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
	String countryID=null;

	@BeforeClass
	public void setupDataForAddBilling() throws InterruptedException{	
		storeFrontHomePage = new StoreFrontHomePage(driver);
		storeFrontBillingInfoPage = new StoreFrontBillingInfoPage(driver);
		storeFrontOrdersPage = new StoreFrontOrdersPage(driver);
		storeFrontCartAutoShipPage = new StoreFrontCartAutoShipPage(driver);
		storeFrontUpdateCartPage = new StoreFrontUpdateCartPage(driver);
		storeFrontPCUserPage = new StoreFrontPCUserPage(driver);
		storeFrontAccountInfoPage = new StoreFrontAccountInfoPage(driver);
		storeFrontConsultantPage = new StoreFrontConsultantPage(driver);

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
			countryId = "40";
		}else{			
			addressLine1 = TestConstants.ADDRESS_LINE_1_US;
			city = TestConstants.CITY_US;
			state = TestConstants.STATE_US;
			postalCode = TestConstants.NEW_ADDRESS_POSTAL_CODE_US;
			phoneNumber = TestConstants.NEW_ADDRESS_PHONE_NUMBER_US;
			countryId = "236";
		}				
		navigateToStoreFrontBaseURL();
		setStoreFrontPassword(driver.getStoreFrontPassword());
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
				driver.get(driver.getURL()+"/"+driver.getCountry());				
			}
			else{
				storeFrontConsultantPage.clickOnWelcomeDropDown();
				if(storeFrontHomePage.isEditCRPLinkPresent()==true){
					break;
				}
				else{
					driver.get(driver.getURL()+"/"+driver.getCountry());					
				}
			}

		}
		logger.info("login is successful");
	}

	// Hybris Phase 2-2041 :: Version : 1 :: Add new billing profile on 'Billing Profile' page
	// Hybris Phase 2-2047 :: Version : 1 :: Edit billing profile on 'Billing Profile' page
	@Test(priority=1)
	public void testAddAndEditNewBillingProfileOnBillingProfilePage_2041_2047() throws InterruptedException, SQLException{
		String newBillingProfileName = TestConstants.NEW_BILLING_PROFILE_NAME_US+randomNum;
		String lastName = "lN";
		String defaultBillingProfileName = null;
		randomNum = CommonUtils.getRandomNum(10000, 1000000);
		int randomNum1 = CommonUtils.getRandomNum(10000, 1000000);
		storeFrontConsultantPage = storeFrontHomePage.loginAsConsultant(consultantEmailID, password);
		storeFrontConsultantPage.clickOnWelcomeDropDown();
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
		storeFrontUpdateCartPage.selectNewBillingCardExpirationDate("OCT","2025");
		storeFrontBillingInfoPage.enterNewBillingSecurityCode(TestConstants.SECURITY_CODE);
		storeFrontBillingInfoPage.selectNewBillingCardAddress();
		storeFrontBillingInfoPage.selectUseThisBillingProfileFutureAutoshipChkbox();
		storeFrontBillingInfoPage.clickOnSaveBillingProfile();

		//--------------- Verify that Newly added Billing profile is listed in the Billing profiles section-----------------------------------------------------------------------------------------------------

		s_assert.assertTrue(storeFrontBillingInfoPage.isBillingAddressPresentOnPage(newBillingProfileName),"Newly added Billing profile is NOT listed on the billing info page");

		//------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

		s_assert.assertFalse(storeFrontBillingInfoPage.isDefaultBillingAddressSelected(newBillingProfileName),"Newly created billing profile is DEFAULT selected on the billing info page when we add a new billing info");
		s_assert.assertTrue(storeFrontBillingInfoPage.isDefaultBillingAddressSelected(defaultBillingProfileName),"Old Default billing profile is not DEFAULT selected on the billing info page when we add a new billing info");

		//Edit the newly added billing profile
		String editedBillingProfileName = TestConstants.NEW_BILLING_PROFILE_NAME_US+randomNum1;
		storeFrontBillingInfoPage.clickOnEditOfBillingProfile(newBillingProfileName+" "+lastName);
		storeFrontBillingInfoPage.enterNewBillingNameOnCard(editedBillingProfileName+" "+lastName);
		storeFrontBillingInfoPage.enterNewBillingCardNumber(TestConstants.CARD_NUMBER);
		storeFrontUpdateCartPage.selectNewBillingCardExpirationDate("OCT","2025");
		storeFrontBillingInfoPage.enterNewBillingSecurityCode(TestConstants.SECURITY_CODE);
		storeFrontBillingInfoPage.selectNewBillingCardAddress();
		storeFrontBillingInfoPage.selectUseThisBillingProfileFutureAutoshipChkbox();
		storeFrontBillingInfoPage.clickOnSaveBillingProfile();
		//--------------- Verify that Newly edited Billing profile is listed in the Billing profiles section-----------------------------------------------------------------------------------------------------

		s_assert.assertTrue(storeFrontBillingInfoPage.isBillingAddressPresentOnPage(editedBillingProfileName),"Newly edited Billing profile is NOT listed on the billing info page when we edit the billing info");

		s_assert.assertTrue(storeFrontBillingInfoPage.isDefaultBillingAddressSelected(defaultBillingProfileName),"Already Default billing profile is not DEFAULT selected on billing info page when we edit the billing info");


		//------------------------------Verify billing profile for adhoc cart page ------------------------------------------//

		//storeFrontHomePage.clickOnRodanAndFieldsLogo();
		storeFrontHomePage.hoverOnShopLinkAndClickAllProductsLinksAfterLogin();
		storeFrontHomePage.clickAddToBagButton(country);
		s_assert.assertTrue(storeFrontHomePage.isCartPageDisplayed(), "Cart page is not displayed");
		logger.info("Cart page is displayed");
		storeFrontHomePage.clickOnCheckoutButton();
		storeFrontHomePage.clickOnShippingAddressNextStepBtn();
		s_assert.assertTrue(storeFrontHomePage.isBillingAddressPresentOnPage(editedBillingProfileName),"Newly/Edited Billing profile is NOT listed on the Adhoc cart");
		s_assert.assertTrue(storeFrontHomePage.isBillingProfileIsSelectedByDefault(defaultBillingProfileName),"Old Default billing profile is not DEFAULT selected on the Adhoc cart");
		storeFrontHomePage.clickOnRodanAndFieldsLogo();

		storeFrontConsultantPage.clickOnWelcomeDropDown();
		storeFrontCartAutoShipPage = storeFrontConsultantPage.clickEditCrpLinkPresentOnWelcomeDropDown();
		storeFrontUpdateCartPage = storeFrontCartAutoShipPage.clickUpdateMoreInfoLink();
		storeFrontUpdateCartPage.clickOnEditPaymentBillingProfile();

		//------------------ Verify that CRP/PC cart contains the newly created billing profile address as selected ------------------------------------------------------------

		s_assert.assertTrue(storeFrontUpdateCartPage.isNewBillingProfileIsSelectedByDefault(editedBillingProfileName),"Newly/Edited Billing Profile is not selected by default on autoship cart page");

		//-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

		s_assert.assertAll();  
	}

	// Hybris Phase 2-2341:Add new billing profile | My Account | checkbox UN-CHECKED
	// Hybris Phase 2-2342:Edit billing profile | My Account | Not check
	@Test(priority=2)
	public void testAddAndEditBillingProfileMyAccountFutureAutoshipCheckboxNotChecked_2341_2342() throws InterruptedException{
		String newBillingProfileName = TestConstants.NEW_BILLING_PROFILE_NAME_US+randomNum;
		String lastName = "lN";
		String defaultBillingProfileName = null;
		randomNum = CommonUtils.getRandomNum(10000, 1000000);
		storeFrontConsultantPage = storeFrontHomePage.loginAsConsultant(consultantEmailID, password);
		storeFrontConsultantPage.clickOnWelcomeDropDown();
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
		storeFrontUpdateCartPage.selectNewBillingCardExpirationDate("OCT","2025");

		storeFrontBillingInfoPage.enterNewBillingSecurityCode(TestConstants.SECURITY_CODE);
		storeFrontBillingInfoPage.selectNewBillingCardAddress();
		storeFrontBillingInfoPage.clickOnSaveBillingProfile();

		//--------------- Verify that Newly added Billing profile is listed in the Billing profiles section-----------------------------------------------------------------------------------------------------

		s_assert.assertTrue(storeFrontBillingInfoPage.isBillingAddressPresentOnPage(newBillingProfileName),"Newly added Billing profile is NOT listed on the billing info page");

		//------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

		s_assert.assertFalse(storeFrontBillingInfoPage.isDefaultBillingAddressSelected(newBillingProfileName),"Newly created billing profile is DEFAULT selected on the billing info page when we add a new billing info");
		s_assert.assertTrue(storeFrontBillingInfoPage.isDefaultBillingAddressSelected(defaultBillingProfileName),"Old Default billing profile is not DEFAULT selected on the billing info page when we add a new billing info");

		//Edit the newly added billing profile
		int randomNum1 = CommonUtils.getRandomNum(10000, 1000000);
		String editedBillingProfileName = TestConstants.NEW_BILLING_PROFILE_NAME_US+randomNum1;
		storeFrontBillingInfoPage.clickOnEditOfBillingProfile(newBillingProfileName+" "+lastName);

		storeFrontBillingInfoPage.enterNewBillingNameOnCard(editedBillingProfileName+" "+lastName);
		storeFrontBillingInfoPage.enterNewBillingCardNumber(TestConstants.CARD_NUMBER);
		storeFrontUpdateCartPage.selectNewBillingCardExpirationDate("OCT","2025");
		storeFrontBillingInfoPage.enterNewBillingSecurityCode(TestConstants.SECURITY_CODE);
		storeFrontBillingInfoPage.selectNewBillingCardAddress();
		storeFrontBillingInfoPage.clickOnSaveBillingProfile();
		//--------------- Verify that Newly edited Billing profile is listed in the Billing profiles section-----------------------------------------------------------------------------------------------------

		s_assert.assertTrue(storeFrontBillingInfoPage.isBillingAddressPresentOnPage(editedBillingProfileName),"Newly edited Billing profile is NOT listed on the billing info pagewhen we edit the billing info ");

		s_assert.assertTrue(storeFrontBillingInfoPage.isDefaultBillingAddressSelected(defaultBillingProfileName),"Already Default billing profile is not DEFAULT selected on billing info page when we edit the billing info");


		//-----------------------Verify That newly/edited Billing Profile is Selected for Adhoc order Cart page-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

		storeFrontHomePage.hoverOnShopLinkAndClickAllProductsLinksAfterLogin();
		storeFrontHomePage.clickAddToBagButton(country);
		s_assert.assertTrue(storeFrontHomePage.isCartPageDisplayed(), "Cart page is not displayed");
		logger.info("Cart page is displayed");
		storeFrontHomePage.clickOnCheckoutButton();
		storeFrontHomePage.clickOnShippingAddressNextStepBtn();
		s_assert.assertTrue(storeFrontHomePage.isBillingAddressPresentOnPage(editedBillingProfileName),"Newly/Edited Billing profile is NOT listed on the Adhoc cart");
		s_assert.assertTrue(storeFrontHomePage.isBillingProfileIsSelectedByDefault(defaultBillingProfileName),"Old Default billing profile is not DEFAULT selected on the Adhoc cart");
		storeFrontHomePage.clickOnRodanAndFieldsLogo();
		storeFrontConsultantPage.clickOnWelcomeDropDown();
		storeFrontCartAutoShipPage = storeFrontConsultantPage.clickEditCrpLinkPresentOnWelcomeDropDown();
		storeFrontUpdateCartPage = storeFrontCartAutoShipPage.clickUpdateMoreInfoLink();
		storeFrontUpdateCartPage.clickOnEditPaymentBillingProfile();

		//------------------ Verify that CRP/PC cart contains the newly/Edited created billing profile address as selected ------------------------------------------------------------

		s_assert.assertTrue(storeFrontHomePage.isBillingAddressPresentOnPage(editedBillingProfileName),"Newly/Edit created Billing profile is NOT listed on the autoship cart");
		s_assert.assertFalse(storeFrontHomePage.isBillingProfileIsSelectedByDefault(newBillingProfileName),"Old Default billing profile is not DEFAULT selected on the Autoship cart");

		//-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

		s_assert.assertAll();  
	}

	//Hybris Phase 2-4327:View new billing profile on 'Billing Profile' page	
	@Test(enabled=false)//Covered in other billig tests
	public void testViewNewBillingProfile_HP2_4327() throws InterruptedException, SQLException{
		//		int totalBillingAddressesFromDB = 0;
		List<Map<String, Object>> billingAddressCountList =  null;
		storeFrontConsultantPage = storeFrontHomePage.loginAsConsultant(consultantEmailID, password);
		storeFrontConsultantPage.clickOnWelcomeDropDown();
		storeFrontBillingInfoPage = storeFrontConsultantPage.clickBillingInfoLinkPresentOnWelcomeDropDown();
		s_assert.assertTrue(storeFrontBillingInfoPage.verifyBillingInfoPageIsDisplayed(),"Billing Info page has not been displayed");
		billingAddressCountList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_BILLING_ADDRESS_COUNT_QUERY,consultantEmailID),RFO_DB);
		//		totalBillingAddressesFromDB = (Integer) getValueFromQueryResult(billingAddressCountList, "count");
		//		logger.info("Total Billing Profiles from RFO DB are "+totalBillingAddressesFromDB);
		//		s_assert.assertEquals(totalBillingAddressesFromDB,storeFrontBillingInfoPage.getTotalBillingAddressesDisplayed(),"Billing Addresses count on UI is "+storeFrontBillingInfoPage.getTotalBillingAddressesDisplayed()+" while the total billing addresses in DB is "+totalBillingAddressesFromDB);		
		s_assert.assertAll();
	}

	// Hybris Phase 2-2042 :: Version : 1 :: Add billing profile during checkout
	// Hybris Phase 2-2048:Edit billing profile during checkout
	@Test(priority=4)
	public void testAddAndEditBillingProfileDuringCheckout_2042_2048() throws InterruptedException{
		randomNum = CommonUtils.getRandomNum(10000, 1000000);
		String newBillingProfileName = TestConstants.NEW_BILLING_PROFILE_NAME_US+randomNum;
		String lastName = "lN";
		storeFrontConsultantPage = storeFrontHomePage.loginAsConsultant(consultantEmailID, password);
		storeFrontConsultantPage.hoverOnShopLinkAndClickAllProductsLinksAfterLogin();
		storeFrontUpdateCartPage.clickOnBuyNowButton();
		storeFrontUpdateCartPage.clickOnCheckoutButton();
		storeFrontUpdateCartPage.clickOnShippingAddressNextStepBtn();
		storeFrontUpdateCartPage.clickAddNewBillingProfileLink();
		storeFrontUpdateCartPage.enterNewBillingCardNumber(TestConstants.CARD_NUMBER);
		storeFrontUpdateCartPage.enterNewBillingNameOnCard(newBillingProfileName+" "+lastName);
		storeFrontUpdateCartPage.selectNewBillingCardExpirationDate("OCT","2025");
		storeFrontUpdateCartPage.enterNewBillingSecurityCode(TestConstants.SECURITY_CODE);
		storeFrontUpdateCartPage.selectNewBillingCardAddress();
		storeFrontUpdateCartPage.clickOnSaveBillingProfile();
		s_assert.assertTrue(storeFrontHomePage.isTheBillingAddressPresentOnPage(newBillingProfileName),"Newly edited Billing profile is NOT listed on Adhoc cart while add the billing profile");
		s_assert.assertTrue(storeFrontHomePage.isBillingProfileIsSelectedByDefault(newBillingProfileName),"Already Default billing profile is not DEFAULT selected on adhoc cart while add the billing profile");

		int randomNum1 = CommonUtils.getRandomNum(10000, 1000000);
		String editedBillingProfileName = TestConstants.NEW_BILLING_PROFILE_NAME+randomNum1;
		storeFrontUpdateCartPage.clickOnEditOnNotDefaultAddressOfBilling();
		storeFrontUpdateCartPage.enterNewBillingCardNumber(TestConstants.CARD_NUMBER);
		storeFrontUpdateCartPage.enterNewBillingNameOnCard(editedBillingProfileName+" "+lastName);
		storeFrontUpdateCartPage.selectNewBillingCardExpirationDate("OCT","2025");
		storeFrontUpdateCartPage.enterNewBillingSecurityCode(TestConstants.SECURITY_CODE);
		storeFrontUpdateCartPage.selectNewBillingCardAddress();
		storeFrontUpdateCartPage.selectUseThisBillingProfileFutureAutoshipChkbox();
		storeFrontUpdateCartPage.clickOnSaveBillingProfile();
		s_assert.assertTrue(storeFrontHomePage.isTheBillingAddressPresentOnPage(editedBillingProfileName),"Newly edited Billing profile is NOT listed on Adhoc cart while edit the billing profile");
		s_assert.assertTrue(storeFrontHomePage.isBillingProfileIsSelectedByDefault(editedBillingProfileName),"Already Default billing profile is not DEFAULT selected on adhoc cart while edit the billing profile");

		storeFrontUpdateCartPage.clickOnBillingNextStepBtn(); 
		storeFrontUpdateCartPage.clickBillingEditAfterSave();
		s_assert.assertTrue(storeFrontUpdateCartPage.isNewBillingProfileIsSelectedByDefaultAfterClickOnEdit(newBillingProfileName),"New Billing Profile is not selected by default on CRP cart page");
		storeFrontUpdateCartPage.clickOnBillingNextStepBtn();
		storeFrontUpdateCartPage.clickPlaceOrderBtn();
		storeFrontConsultantPage = storeFrontUpdateCartPage.clickOnRodanAndFieldsLogo();
		storeFrontConsultantPage.clickOnWelcomeDropDown();
		storeFrontBillingInfoPage = storeFrontConsultantPage.clickBillingInfoLinkPresentOnWelcomeDropDown();
		s_assert.assertTrue(storeFrontBillingInfoPage.verifyBillingInfoPageIsDisplayed(),"Billing Info page has not been displayed");

		//--------------- Verify that Newly added Billing profile is listed in the Billing profiles section-----------------------------------------------------------------------------------------------------

		s_assert.assertTrue(storeFrontBillingInfoPage.isBillingAddressPresentOnPage(newBillingProfileName),"Newly added Billing profile is NOT listed on the page");

		//------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

		//--------------- Verify that Newly added Billing profile is default selected in the Billing profiles section-----------------------------------------------------------------------------------------------------

		s_assert.assertTrue(storeFrontBillingInfoPage.isDefaultAddressRadioBtnSelected(newBillingProfileName),"Newly added Billing profile is NOT default on the page");

		//------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

		s_assert.assertAll();

	}

	//Hybris Phase 2-2043:Add billing profile in autoship template
	//Hybris Phase 2-2049 :: Version : 1 :: Edit billing profile in autoship template
	@Test(priority=5)
	public void testAddAndEditBillingAutoshipCartFutureCheckboxSelected_2043_2049() throws InterruptedException{ 
		randomNum = CommonUtils.getRandomNum(10000, 1000000);
		String newBillingProfileName = TestConstants.NEW_BILLING_PROFILE_NAME_US+randomNum;
		String lastName = "lN";
		storeFrontConsultantPage = storeFrontHomePage.loginAsConsultant(consultantEmailID, password);
		storeFrontConsultantPage.clickOnWelcomeDropDown();
		storeFrontCartAutoShipPage = storeFrontConsultantPage.clickEditCrpLinkPresentOnWelcomeDropDown();
		storeFrontUpdateCartPage = storeFrontCartAutoShipPage.clickUpdateMoreInfoLink();
		storeFrontUpdateCartPage.clickOnEditPaymentBillingProfile();
		storeFrontUpdateCartPage.clickAddNewBillingProfileLink();
		storeFrontUpdateCartPage.enterNewBillingCardNumber(TestConstants.CARD_NUMBER);
		storeFrontUpdateCartPage.enterNewBillingNameOnCard(newBillingProfileName+" "+lastName);
		storeFrontUpdateCartPage.selectNewBillingCardExpirationDate("OCT","2025");
		storeFrontUpdateCartPage.enterNewBillingSecurityCode(TestConstants.SECURITY_CODE);
		storeFrontUpdateCartPage.selectNewBillingCardAddress();
		storeFrontUpdateCartPage.selectUseThisBillingProfileFutureAutoshipChkbox();
		storeFrontUpdateCartPage.clickOnSaveBillingProfile();
		storeFrontUpdateCartPage.clickOnBillingNextStepBtn(); 
		storeFrontUpdateCartPage.clickOnEditPaymentBillingProfile();
		s_assert.assertTrue(storeFrontUpdateCartPage.isNewBillingProfileIsSelectedByDefault(newBillingProfileName),"New Billing Profile is not selected by default on CRP cart page while add a new billing profile");
		int randomNum1 = CommonUtils.getRandomNum(10000, 1000000);
		String editedBillingProfileName = TestConstants.NEW_BILLING_PROFILE_NAME+randomNum1;
		storeFrontUpdateCartPage.clickOnEditOfBillingProfile(newBillingProfileName+" "+lastName);
		storeFrontUpdateCartPage.enterNewBillingCardNumber(TestConstants.CARD_NUMBER);
		storeFrontUpdateCartPage.enterNewBillingNameOnCard(editedBillingProfileName+" "+lastName);
		storeFrontUpdateCartPage.selectNewBillingCardExpirationDate("OCT","2025");
		storeFrontUpdateCartPage.enterNewBillingSecurityCode(TestConstants.SECURITY_CODE);
		storeFrontUpdateCartPage.selectNewBillingCardAddress();
		storeFrontUpdateCartPage.selectUseThisBillingProfileFutureAutoshipChkbox();
		storeFrontUpdateCartPage.clickOnSaveBillingProfile();
		storeFrontUpdateCartPage.clickOnBillingNextStepBtn(); 
		storeFrontUpdateCartPage.clickOnEditPaymentBillingProfile();
		s_assert.assertTrue(storeFrontUpdateCartPage.isNewBillingProfileIsSelectedByDefault(editedBillingProfileName),"Edited Billing Profile is not selected by default on CRP cart page while edit billing profile");
		storeFrontUpdateCartPage.clickOnBillingNextStepBtn();
		storeFrontUpdateCartPage.clickUpdateCartBtn();
		storeFrontConsultantPage = storeFrontUpdateCartPage.clickOnRodanAndFieldsLogo();
		storeFrontConsultantPage.clickOnWelcomeDropDown();
		storeFrontBillingInfoPage = storeFrontConsultantPage.clickBillingInfoLinkPresentOnWelcomeDropDown();
		s_assert.assertTrue(storeFrontBillingInfoPage.verifyBillingInfoPageIsDisplayed(),"Billing Info page has not been displayed");

		//--------------- Verify that Newly added Billing profile is listed in the Billing profiles section-----------------------------------------------------------------------------------------------------
		s_assert.assertTrue(storeFrontBillingInfoPage.isBillingAddressPresentOnPage(editedBillingProfileName),"Newly added/Edited Billing profile is NOT listed on the page");

		//------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

		//--------------- Verify that Newly added Billing profile is NOT default selected in the Billing profiles section-----------------------------------------------------------------------------------------------------

		s_assert.assertFalse(storeFrontBillingInfoPage.isDefaultAddressRadioBtnSelected(editedBillingProfileName),"Newly added/Edited Billing profile is NOT default on the page");

		//------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

		s_assert.assertAll();
	}

	//Hybris Project-4467 ADD a billing profile from AUTOSHIP CART page, having "Use this billing profile for your future auto-ship" check box NOT CHECKED
	@Test(priority=6)
	public void testAddAndEditBillingAutoshipCartFutureCheckboxNotSelected_4467() throws InterruptedException{  
		randomNum = CommonUtils.getRandomNum(10000, 1000000);
		String newBillingProfileName = TestConstants.NEW_BILLING_PROFILE_NAME_US+randomNum;
		String lastName = "lN";
		storeFrontConsultantPage = storeFrontHomePage.loginAsConsultant(consultantEmailID, password);
		storeFrontConsultantPage.clickOnWelcomeDropDown();
		storeFrontCartAutoShipPage = storeFrontConsultantPage.clickEditCrpLinkPresentOnWelcomeDropDown();
		storeFrontUpdateCartPage = storeFrontCartAutoShipPage.clickUpdateMoreInfoLink();
		storeFrontUpdateCartPage.clickOnEditPaymentBillingProfile();
		storeFrontUpdateCartPage.clickAddNewBillingProfileLink();
		storeFrontUpdateCartPage.enterNewBillingCardNumber(TestConstants.CARD_NUMBER);
		storeFrontUpdateCartPage.enterNewBillingNameOnCard(newBillingProfileName+" "+lastName);
		storeFrontUpdateCartPage.selectNewBillingCardExpirationDate("OCT","2025");
		storeFrontUpdateCartPage.enterNewBillingSecurityCode(TestConstants.SECURITY_CODE);
		storeFrontUpdateCartPage.selectNewBillingCardAddress();
		storeFrontUpdateCartPage.clickOnSaveBillingProfile();
		storeFrontUpdateCartPage.clickOnBillingNextStepBtn(); 
		storeFrontUpdateCartPage.clickOnEditPaymentBillingProfile();
		s_assert.assertFalse(storeFrontUpdateCartPage.isNewBillingProfileIsSelectedByDefault(newBillingProfileName),"New Billing Profile is selected by default on CRP cart page while we add a new billing profile");
		int randomNum1 = CommonUtils.getRandomNum(10000, 1000000);
		String editedBillingProfileName = TestConstants.NEW_BILLING_PROFILE_NAME+randomNum1;
		storeFrontUpdateCartPage.clickOnEditOfBillingProfile(newBillingProfileName+" "+lastName);
		storeFrontUpdateCartPage.enterNewBillingCardNumber(TestConstants.CARD_NUMBER);
		storeFrontUpdateCartPage.enterNewBillingNameOnCard(editedBillingProfileName+" "+lastName);
		storeFrontUpdateCartPage.selectNewBillingCardExpirationDate("OCT","2025");
		storeFrontUpdateCartPage.enterNewBillingSecurityCode(TestConstants.SECURITY_CODE);
		storeFrontUpdateCartPage.selectNewBillingCardAddress();
		storeFrontUpdateCartPage.clickOnSaveBillingProfile();
		storeFrontUpdateCartPage.clickOnBillingNextStepBtn(); 
		storeFrontUpdateCartPage.clickOnEditPaymentBillingProfile();
		s_assert.assertFalse(storeFrontUpdateCartPage.isNewBillingProfileIsSelectedByDefault(editedBillingProfileName),"Edited Billing Profile is selected by default on CRP cart page while we edit billing profile");
		storeFrontUpdateCartPage.clickOnBillingNextStepBtn();
		storeFrontUpdateCartPage.clickUpdateCartBtn();
		storeFrontConsultantPage = storeFrontUpdateCartPage.clickOnRodanAndFieldsLogo();
		storeFrontConsultantPage.clickOnWelcomeDropDown();
		storeFrontBillingInfoPage = storeFrontConsultantPage.clickBillingInfoLinkPresentOnWelcomeDropDown();
		s_assert.assertTrue(storeFrontBillingInfoPage.verifyBillingInfoPageIsDisplayed(),"Billing Info page has not been displayed");

		//--------------- Verify that Newly added Billing profile is listed in the Billing profiles section-----------------------------------------------------------------------------------------------------

		s_assert.assertTrue(storeFrontBillingInfoPage.isTheBillingAddressPresentOnPage(editedBillingProfileName),"Newly added/Edited Billing profile is NOT listed on the page");

		//------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

		//--------------- Verify that Newly added Billing profile is default selected in the Billing profiles section-----------------------------------------------------------------------------------------------------

		s_assert.assertFalse(storeFrontBillingInfoPage.isDefaultAddressRadioBtnSelected(editedBillingProfileName),"Newly added/Edited Billing profile is NOT default on the page");

		//------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

		s_assert.assertAll();
	}

	// Hybris Project-4468:EDIT a billing profile from AD-HOC CHECKOUT page, having "Use this billing profile for your future use" NOT Selected
	@Test(priority=7)
	public void testEditBillingAdhocCheckoutFutureChecboxNotSelected_4468() throws InterruptedException{
		randomNum = CommonUtils.getRandomNum(10000, 1000000);
		String newBillingProfileName = TestConstants.NEW_BILLING_PROFILE_NAME_US+randomNum;
		String lastName = "lN";
		storeFrontConsultantPage = storeFrontHomePage.loginAsConsultant(consultantEmailID, password);
		storeFrontConsultantPage.hoverOnShopLinkAndClickAllProductsLinksAfterLogin();		
		storeFrontUpdateCartPage.clickOnBuyNowButton();
		storeFrontUpdateCartPage.clickOnCheckoutButton();
		storeFrontUpdateCartPage.clickOnShippingAddressNextStepBtn();
		storeFrontUpdateCartPage.clickOnEditDefaultBillingProfile();
		storeFrontUpdateCartPage.enterNewBillingCardNumber(TestConstants.CARD_NUMBER);
		storeFrontUpdateCartPage.enterNewBillingNameOnCard(newBillingProfileName+" "+lastName);
		storeFrontUpdateCartPage.selectNewBillingCardExpirationDate("OCT","2025");
		storeFrontUpdateCartPage.enterNewBillingSecurityCode(TestConstants.SECURITY_CODE);
		storeFrontUpdateCartPage.selectNewBillingCardAddress();
		storeFrontUpdateCartPage.clickOnSaveBillingProfile();
		storeFrontUpdateCartPage.clickOnBillingNextStepBtn(); 
		storeFrontUpdateCartPage.clickBillingEditAfterSave();
		s_assert.assertTrue(storeFrontUpdateCartPage.isNewBillingProfileIsSelectedByDefaultAfterClickOnEdit(newBillingProfileName),"New Billing Profile is not selected by default on CRP cart page");
		storeFrontUpdateCartPage.clickOnBillingNextStepBtn();
		storeFrontUpdateCartPage.clickPlaceOrderBtn();
		storeFrontConsultantPage = storeFrontUpdateCartPage.clickOnRodanAndFieldsLogo();
		storeFrontConsultantPage.clickOnWelcomeDropDown();
		storeFrontBillingInfoPage = storeFrontConsultantPage.clickBillingInfoLinkPresentOnWelcomeDropDown();
		//--------------- Verify that Newly added Billing profile is listed in the Billing profiles section-----------------------------------------------------------------------------------------------------
		s_assert.assertTrue(storeFrontBillingInfoPage.isBillingAddressPresentOnPage(newBillingProfileName),"Newly edited default Billing profile is NOT listed on the page");
		s_assert.assertTrue(storeFrontBillingInfoPage.isBillingProfileIsSelectedByDefault(newBillingProfileName),"Newly edited default Billing profile is not selected as default profile");
		s_assert.assertAll();
	}

	//Hybris Project-2389:Verify that QAS validation DO NOT get perform anytime user adds a billing address.
	@Test(priority=8)
	public void testQASValidationDoNotPerformAnyTimeUserAddsABillingAddress_2389() throws InterruptedException	{
		randomNum = CommonUtils.getRandomNum(10000, 1000000);
		String lastName = "lN";
		String newBillingProfileName = TestConstants.NEW_BILLING_PROFILE_NAME_US+randomNum;
		storeFrontConsultantPage = storeFrontHomePage.loginAsConsultant(consultantEmailID, password);
		storeFrontConsultantPage.hoverOnShopLinkAndClickAllProductsLinksAfterLogin();  
		storeFrontUpdateCartPage.clickAddToBagButton(driver.getCountry());
		storeFrontUpdateCartPage.clickOnCheckoutButton();
		//click Next on Shipping Address Section
		storeFrontUpdateCartPage.clickOnShippingAddressNextStepBtn();
		//Add a New Billing Address
		storeFrontUpdateCartPage.clickAddNewBillingProfileLink();
		storeFrontUpdateCartPage.enterNewBillingCardNumber(TestConstants.CARD_NUMBER);
		storeFrontUpdateCartPage.enterNewBillingNameOnCard(newBillingProfileName+" "+lastName);
		storeFrontUpdateCartPage.selectNewBillingCardExpirationDate("OCT","2025");
		storeFrontUpdateCartPage.enterNewBillingSecurityCode(TestConstants.SECURITY_CODE);
		storeFrontUpdateCartPage.selectNewBillingCardAddress();
		storeFrontUpdateCartPage.clickOnSaveBillingProfile();
		//validate QAS Validation PopUp is Not Displayed after Adding a Billing Profile
		s_assert.assertFalse(storeFrontUpdateCartPage.validateQASValidationPopUpIsDisplayed(),"QAS Validation PopUp is Displayed After Adding A Biling Profile");
		s_assert.assertAll();
	}

	//Hybris Project-2046 :: Version : 1 :: Add billing profile during CRP enrollment through my account 
	//Hybris Project-2052:Edit billing profile during CRP enrollment through my account
	@Test(priority=9)
	public void testAddAndEditNewBillingProfileDuringCRPEnrollment_2046_2052() throws InterruptedException{
		int randomNumber = CommonUtils.getRandomNum(10000, 1000000);
		String lastName = "lN";
		String newBillingProfileName = TestConstants.NEW_BILLING_PROFILE_NAME_US+randomNumber;
		storeFrontConsultantPage = storeFrontHomePage.loginAsConsultant(consultantEmailID, password);
		storeFrontConsultantPage.clickOnWelcomeDropDown();
		storeFrontAccountInfoPage = storeFrontConsultantPage.clickAccountInfoLinkPresentOnWelcomeDropDown();
		s_assert.assertTrue(storeFrontAccountInfoPage.verifyAccountInfoPageIsDisplayed(),"shipping info page has not been displayed");
		storeFrontAccountInfoPage.clickOnYourAccountDropdown();
		storeFrontAccountInfoPage.clickOnAutoShipStatus();
		if(storeFrontAccountInfoPage.verifyCRPCancelled()==false){
			storeFrontAccountInfoPage.clickOnCancelMyCRP();
		}
		storeFrontAccountInfoPage.clickOnEnrollInCRP();
		storeFrontHomePage.clickOnAddToCRPButtonCreatingCRPUnderBizSite();
		storeFrontHomePage.clickOnCRPCheckout();
		storeFrontHomePage.clickOnUpdateCartShippingNextStepBtnDuringEnrollment();
		storeFrontBillingInfoPage.clickAddNewBillingProfileLink();
		int randomNum1 = CommonUtils.getRandomNum(10000, 1000000);
		newBillingProfileName = TestConstants.NEW_BILLING_PROFILE_NAME+randomNum1;
		storeFrontBillingInfoPage.enterNewBillingCardNumber(TestConstants.CARD_NUMBER);
		storeFrontBillingInfoPage.enterNewBillingNameOnCard(newBillingProfileName+" "+lastName);
		storeFrontBillingInfoPage.selectNewBillingCardExpirationDate(TestConstants.CARD_EXP_MONTH, TestConstants.CARD_EXP_YEAR);
		storeFrontBillingInfoPage.enterNewBillingSecurityCode(TestConstants.SECURITY_CODE);
		storeFrontBillingInfoPage.selectNewBillingCardAddress();
		storeFrontBillingInfoPage.clickOnSaveBillingProfile();
		s_assert.assertTrue(storeFrontBillingInfoPage.isBillingAddressPresentOnPage(newBillingProfileName),"Newly Created Billing Address not present on page");
		s_assert.assertTrue(storeFrontBillingInfoPage.isDefaultAddressRadioBtnSelected(newBillingProfileName),"Radio button is not selected for billing address");
		storeFrontHomePage.clickOnEditOfBillingProfile(newBillingProfileName);
		int randomNum2 = CommonUtils.getRandomNum(10000, 1000000);
		String editedBillingProfileName = TestConstants.NEW_BILLING_PROFILE_NAME+randomNum2;
		storeFrontHomePage.enterEditedCardNumber(TestConstants.CARD_NUMBER);
		storeFrontHomePage.enterNewBillingNameOnCard(editedBillingProfileName+" "+lastName);
		storeFrontUpdateCartPage.selectNewBillingCardExpirationDate("OCT","2025");
		storeFrontHomePage.enterNewBillingSecurityCode(TestConstants.SECURITY_CODE);
		storeFrontHomePage.selectNewBillingCardAddress();
		storeFrontHomePage.clickOnSaveBillingProfile();
		s_assert.assertTrue(storeFrontBillingInfoPage.isBillingAddressPresentOnPage(editedBillingProfileName),"Newly Edited Billing Address not present on page");
		storeFrontHomePage.clickOnBillingNextStepBtn();
		storeFrontHomePage.clickOnSetupCRPAccountBtn();
		s_assert.assertTrue(storeFrontHomePage.verifyOrderConfirmation(), "Order Confirmation Message has not been displayed");
		storeFrontHomePage.clickOnGoToMyAccountToCheckStatusOfCRP();
		storeFrontHomePage.clickOnYourAccountDropdown();
		storeFrontAccountInfoPage.clickOnAutoShipStatus();
		s_assert.assertTrue(storeFrontAccountInfoPage.verifyCurrentCRPStatus(), "Current CRP Status has not been Enrolled");
		storeFrontConsultantPage.clickOnWelcomeDropDown();
		storeFrontBillingInfoPage =storeFrontConsultantPage.clickBillingInfoLinkPresentOnWelcomeDropDown();
		s_assert.assertTrue(storeFrontBillingInfoPage.verifyBillingInfoPageIsDisplayed(),"billing info page has not been displayed");
		s_assert.assertTrue(storeFrontBillingInfoPage.isBillingAddressPresentOnPage(editedBillingProfileName),"Newly Created/Edited Billing Address not present on Billing info page");
		//s_assert.assertTrue(storeFrontBillingInfoPage.isAutoshipOrderAddressTextPresent(newBillingProfileName),"AutoShip order text is not present under billing address");
		s_assert.assertFalse(storeFrontBillingInfoPage.isDefaultAddressRadioBtnSelected(editedBillingProfileName),"Radio button is selected for newly created/Edited billing address");
		s_assert.assertAll();
	}

	//Hybris Project-2044:Add billing profile during PC user or Retail user registration
	//Hybris Project-2050 :: Version : 1 :: Edit billing profile during PC user or Retail user registration
	@Test(priority=10)
	public void testAddAndEditBillingProfileDuringPCRegistration_2044_2050() throws InterruptedException{
		randomNum = CommonUtils.getRandomNum(10000, 1000000);
		String newBillingProfileName = TestConstants.NEW_BILLING_PROFILE_NAME+randomNum;
		String lastName = "lN";
		String firstName=TestConstants.FIRST_NAME+randomNum;
		// Click on our product link that is located at the top of the page and then click in on quick shop
		/*storeFrontHomePage.clickOnShopLink();
	    storeFrontHomePage.clickOnAllProductsLink();*/
		storeFrontHomePage.hoverOnShopLinkAndClickAllProductsLinks();

		// Products are displayed?
		s_assert.assertTrue(storeFrontHomePage.areProductsDisplayed(), "quickshop products not displayed");
		logger.info("Quick shop products are displayed");

		//Select a product with the price less than $80 and proceed to buy it
		storeFrontHomePage.selectProductAndProceedToBuy();

		//Click on Check out
		storeFrontHomePage.clickOnCheckoutButton();

		//Enter the User information and DO NOT check the "Become a Preferred Customer" checkbox and click the create account button
		storeFrontHomePage.enterNewPCDetails(firstName, TestConstants.LAST_NAME+randomNum, password);

		//Enter the Main account info and DO NOT check the "Become a Preferred Customer" and click next
		storeFrontHomePage.enterMainAccountInfo();
		logger.info("Main account details entered");

		storeFrontHomePage.clickOnContinueWithoutSponsorLink();
		storeFrontHomePage.clickOnNextButtonAfterSelectingSponsor();

		storeFrontHomePage.clickOnShippingAddressNextStepBtn();
		//Enter Billing Profile
		storeFrontHomePage.enterNewBillingCardNumber(TestConstants.CARD_NUMBER);
		storeFrontHomePage.enterNewBillingNameOnCard(newBillingProfileName+" "+lastName);
		storeFrontUpdateCartPage.selectNewBillingCardExpirationDate("OCT","2025");
		storeFrontHomePage.enterNewBillingSecurityCode(TestConstants.SECURITY_CODE);
		storeFrontHomePage.selectNewBillingCardAddress();
		storeFrontHomePage.clickOnSaveBillingProfile();
		storeFrontUpdateCartPage.clickOnBillingNextStepBtn(); 
		storeFrontUpdateCartPage.clickBillingEditAfterSave();
		s_assert.assertTrue(storeFrontUpdateCartPage.isNewBillingProfileIsSelectedByDefaultAfterClickOnEdit(newBillingProfileName),"New Billing Profile is not selected by default on CRP cart page while we edit the billing profile");

		storeFrontUpdateCartPage.clickOnEditOfBillingProfile(newBillingProfileName+" "+lastName);
		int randomNum1 = CommonUtils.getRandomNum(10000, 1000000);
		String editedBillingProfileName = TestConstants.NEW_BILLING_PROFILE_NAME+randomNum1;
		storeFrontHomePage.enterEditedCardNumber(TestConstants.CARD_NUMBER);
		storeFrontHomePage.enterNewBillingNameOnCard(editedBillingProfileName+" "+lastName);
		storeFrontUpdateCartPage.selectNewBillingCardExpirationDate("OCT","2025");
		storeFrontHomePage.enterNewBillingSecurityCode(TestConstants.SECURITY_CODE);
		storeFrontHomePage.selectNewBillingCardAddress();
		storeFrontHomePage.clickOnSaveBillingProfile();
		storeFrontUpdateCartPage.clickOnBillingNextStepBtn(); 
		storeFrontUpdateCartPage.clickBillingEditAfterSave();
		s_assert.assertTrue(storeFrontUpdateCartPage.isNewBillingProfileIsSelectedByDefaultAfterClickOnEdit(editedBillingProfileName),"Edit Billing Profile is not selected by default on CRP cart page while we edit the billing profile");

		storeFrontUpdateCartPage.clickOnBillingNextStepBtn();
		storeFrontHomePage.clickOnPCPerksTermsAndConditionsCheckBoxes();
		storeFrontUpdateCartPage.clickPlaceOrderBtn();
		storeFrontUpdateCartPage.clickOnRodanAndFieldsLogo();
		storeFrontPCUserPage.clickOnWelcomeDropDown();
		storeFrontBillingInfoPage = storeFrontPCUserPage.clickBillingInfoLinkPresentOnWelcomeDropDown();
		s_assert.assertTrue(storeFrontBillingInfoPage.verifyBillingInfoPageIsDisplayed(),"Billing Info page has not been displayed");

		//--------------- Verify that Newly added Billing profile is listed in the Billing profiles section-----------------------------------------------------------------------------------------------------

		s_assert.assertTrue(storeFrontBillingInfoPage.isTheBillingAddressPresentOnPage(editedBillingProfileName),"Newly added/Edited Billing profile is NOT listed on the page");
		storeFrontPCUserPage.clickOnWelcomeDropDown();
		storeFrontPCUserPage.clickEditPCPerksLinkPresentOnWelcomeDropDown();
		storeFrontCartAutoShipPage.clickUpdateMoreInfoLink();
		storeFrontUpdateCartPage.clickOnEditPaymentBillingProfile();

		//------------------ Verify that CRP/PC cart contains the newly created billing profile address as selected ------------------------------------------------------------

		s_assert.assertTrue(storeFrontUpdateCartPage.isNewBillingProfileIsSelectedByDefault(editedBillingProfileName),"Newly/Edited Billing Profile is not selected by default on update cart page");

		//-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

		s_assert.assertAll();  
	}

	// Hybris Project-2045 :: Version : 1 :: Add billing address during consultant enrollment 
	// Hybris Project-2051:Edit billing address during consultant enrollment
	@Test(priority=11)
	public void testAddAndEditBillingAddressConsultantEnrollment_2045_2051() throws InterruptedException{
		randomNum = CommonUtils.getRandomNum(10000, 1000000);
		String socialInsuranceNumber = String.valueOf(CommonUtils.getRandomNum(100000000, 999999999));
		storeFrontHomePage.openPWSSite(country, env);
		String newBillingProfileName = TestConstants.NEW_BILLING_PROFILE_NAME_US+randomNum;
		String lastName = "lN";
		storeFrontHomePage.hoverOnBecomeAConsultantAndClickEnrollNowLink();
		storeFrontHomePage.enterUserInformationForEnrollment(kitName, regimenName, enrollmentType, TestConstants.FIRST_NAME+randomNum, TestConstants.LAST_NAME+randomNum, password, addressLine1, city,state,postalCode, phoneNumber);
		storeFrontHomePage.clickNextButton();
		//storeFrontHomePage.acceptTheVerifyYourShippingAddressPop();		
		storeFrontHomePage.enterCardNumber(TestConstants.CARD_NUMBER);
		storeFrontHomePage.enterNameOnCard(newBillingProfileName+" "+lastName);
		storeFrontUpdateCartPage.selectNewBillingCardExpirationDate("OCT","2025");
		storeFrontHomePage.enterSecurityCode(TestConstants.SECURITY_CODE);
		storeFrontHomePage.enterSocialInsuranceNumber(socialInsuranceNumber);
		storeFrontHomePage.enterNameAsItAppearsOnCard(TestConstants.FIRST_NAME);
		storeFrontHomePage.clickNextButton();
		storeFrontHomePage.clickOnEditBillingOnReviewAndConfirmPage();
		int randomNum1 = CommonUtils.getRandomNum(10000, 1000000);
		String editedBillingProfileName = TestConstants.NEW_BILLING_PROFILE_NAME+randomNum1;
		storeFrontHomePage.enterEditedCardNumber(TestConstants.CARD_NUMBER);
		storeFrontHomePage.enterNewBillingNameOnCard(editedBillingProfileName+" "+lastName);
		storeFrontUpdateCartPage.selectNewBillingCardExpirationDate("OCT","2025");
		storeFrontHomePage.enterNewBillingSecurityCode(TestConstants.SECURITY_CODE);
		storeFrontHomePage.selectNewBillingCardAddress();
		storeFrontHomePage.enterSocialInsuranceNumber(socialInsuranceNumber);
		storeFrontHomePage.enterNameAsItAppearsOnCard(TestConstants.FIRST_NAME);
		storeFrontHomePage.clickNextButton();
		s_assert.assertTrue(storeFrontHomePage.isTheTermsAndConditionsCheckBoxDisplayed(), "Terms and Conditions checkbox is not visible");
		storeFrontHomePage.checkThePoliciesAndProceduresCheckBox();
		storeFrontHomePage.checkTheIAcknowledgeCheckBox();		
		storeFrontHomePage.checkTheIAgreeCheckBox();
		storeFrontHomePage.clickOnEnrollMeBtn();
		s_assert.assertTrue(storeFrontHomePage.verifyPopUpForTermsAndConditions(), "PopUp for terms and conditions is not visible");
		storeFrontHomePage.checkTheTermsAndConditionsCheckBox();
		storeFrontHomePage.clickOnChargeMyCardAndEnrollMeBtn();
		storeFrontHomePage.clickOnConfirmAutomaticPayment();
		s_assert.assertTrue(storeFrontHomePage.verifyCongratsMessage(), "Congrats Message is not visible");
		storeFrontHomePage.clickOnRodanAndFieldsLogo();
		storeFrontConsultantPage.clickOnWelcomeDropDown();
		storeFrontOrdersPage = storeFrontConsultantPage.clickOrdersLinkPresentOnWelcomeDropDown();
		storeFrontOrdersPage.clickAutoshipOrderNumber();

		//------------------ Verify that autoship template contains the newly created billing profile ------------------------------------------------------------  

		s_assert.assertTrue(storeFrontOrdersPage.isPaymentMethodContainsName(editedBillingProfileName),"Autoship Template Payment Method doesn't contains the newly/Edited billing profile even when future autoship checkbox is selected");

		//-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

		storeFrontConsultantPage.clickOnWelcomeDropDown();
		storeFrontBillingInfoPage = storeFrontConsultantPage.clickBillingInfoLinkPresentOnWelcomeDropDown();
		s_assert.assertTrue(storeFrontBillingInfoPage.verifyBillingInfoPageIsDisplayed(),"Billing Info page has not been displayed");
		//--------------- Verify that Newly added Billing profile is listed in the Billing profiles section-----------------------------------------------------------------------------------------------------
		s_assert.assertTrue(storeFrontBillingInfoPage.isTheBillingAddressPresentOnPage(editedBillingProfileName),"Newly added/Edited Billing profile is NOT listed on the page");
		//------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
		s_assert.assertAll();
	}	
}