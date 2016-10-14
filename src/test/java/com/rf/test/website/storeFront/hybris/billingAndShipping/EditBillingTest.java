//package com.rf.test.website.storeFront.hybris.billingAndShipping;
//
//import java.sql.SQLException;
//import java.util.List;
//import java.util.Map;
//
//import org.apache.logging.log4j.LogManager;
//import org.apache.logging.log4j.Logger;
//import org.testng.annotations.BeforeClass;
//import org.testng.annotations.Test;
//
//import com.rf.core.utils.CommonUtils;
//import com.rf.core.utils.DBUtil;
//import com.rf.core.website.constants.TestConstants;
//import com.rf.core.website.constants.dbQueries.DBQueries_RFO;
//import com.rf.pages.website.storeFront.StoreFrontAccountInfoPage;
//import com.rf.pages.website.storeFront.StoreFrontBillingInfoPage;
//import com.rf.pages.website.storeFront.StoreFrontCartAutoShipPage;
//import com.rf.pages.website.storeFront.StoreFrontConsultantPage;
//import com.rf.pages.website.storeFront.StoreFrontHomePage;
//import com.rf.pages.website.storeFront.StoreFrontOrdersPage;
//import com.rf.pages.website.storeFront.StoreFrontPCUserPage;
//import com.rf.pages.website.storeFront.StoreFrontUpdateCartPage;
//import com.rf.test.website.RFWebsiteBaseTest;
//import com.rf.test.website.RFStoreFrontWebsiteBaseTest;
//
//public class EditBillingTest extends RFStoreFrontWebsiteBaseTest{
//	private static final Logger logger = LogManager
//			.getLogger(EditBillingTest.class.getName());
//
//	private StoreFrontHomePage storeFrontHomePage;
//	private StoreFrontBillingInfoPage storeFrontBillingInfoPage;
//	private StoreFrontOrdersPage storeFrontOrdersPage;
//	private StoreFrontCartAutoShipPage storeFrontCartAutoShipPage;
//	private StoreFrontUpdateCartPage storeFrontUpdateCartPage;
//	private StoreFrontConsultantPage storeFrontConsultantPage;
//	private StoreFrontPCUserPage storeFrontPCUserPage;
//	private StoreFrontAccountInfoPage storeFrontAccountInfoPage;
//	private String city = null;
//	private String state = null;
//	private String phoneNumber = null;
//	private String postalCode = null;
//	private String kitName = null;
//	private String regimenName = null;
//	private String enrollmentType = null;
//	private String addressLine1 = null;
//	private String country = null;
//	private String RFO_DB = null;
//	private String env = null;
//	private int randomNum; 	
//	List<Map<String, Object>> randomConsultantList =  null;
//	String consultantEmailID = null;
//	String accountID = null;
//
//	@BeforeClass
//	public void setupDataForEditBilling() throws InterruptedException{	
//		storeFrontHomePage = new StoreFrontHomePage(driver);
//		storeFrontBillingInfoPage = new StoreFrontBillingInfoPage(driver);
//		storeFrontOrdersPage = new StoreFrontOrdersPage(driver);
//		storeFrontCartAutoShipPage = new StoreFrontCartAutoShipPage(driver);
//		storeFrontUpdateCartPage = new StoreFrontUpdateCartPage(driver);
//		storeFrontPCUserPage = new StoreFrontPCUserPage(driver);
//		storeFrontAccountInfoPage = new StoreFrontAccountInfoPage(driver);
//
//		randomNum = CommonUtils.getRandomNum(10000, 1000000);
//		RFO_DB = driver.getDBNameRFO(); 
//		env = driver.getEnvironment();
//		country = driver.getCountry();
//
//		kitName = TestConstants.KIT_NAME_BIG_BUSINESS; 	
//		enrollmentType = TestConstants.EXPRESS_ENROLLMENT;
//		regimenName = TestConstants.REGIMEN_NAME_REDEFINE;
//
//		if(country.equalsIgnoreCase("CA")){				 
//			addressLine1 = TestConstants.ADDRESS_LINE_1_CA;
//			city = TestConstants.CITY_CA;
//			postalCode = TestConstants.POSTAL_CODE_CA;
//			phoneNumber = TestConstants.PHONE_NUMBER_CA;
//			state = TestConstants.PROVINCE_CA;
//		}else{			
//			addressLine1 = TestConstants.ADDRESS_LINE_1_US;
//			city = TestConstants.CITY_US;
//			state = TestConstants.STATE_US;
//			postalCode = TestConstants.NEW_ADDRESS_POSTAL_CODE_US;
//			phoneNumber = TestConstants.NEW_ADDRESS_PHONE_NUMBER_US;			
//		}
//
//		while(true){
//			randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_CONSULTANT_WITH_ORDERS_AND_AUTOSHIPS_RFO,countryId),RFO_DB);
//			consultantEmailID = (String) getValueFromQueryResult(randomConsultantList, "UserName");  
//			accountID = String.valueOf(getValueFromQueryResult(randomConsultantList, "AccountID"));
//			logger.info("Account Id of the user is "+accountID);
//			//storeFrontHomePage = new StoreFrontHomePage(driver);
//			storeFrontConsultantPage = storeFrontHomePage.loginAsConsultant(consultantEmailID, password);
//			boolean isError = driver.getCurrentUrl().contains("error");
//			if(isError){
//				logger.info("Login error for the user "+consultantEmailID);
//				driver.get(driver.getURL());
//			}
//			else{
//				storeFrontConsultantPage.clickOnWelcomeDropDown();
//				if(storeFrontHomePage.isEditCRPLinkPresent()==true)
//					break;
//				else
//					driver.get(driver.getURL());				
//			}
//
//		}
//		logger.info("login is successful");		
//	}
//
//	// Hybris Phase 2-2047 :: Version : 1 :: Edit billing profile on 'Billing Profile' page 
//	@Test(priority=1)
//	public void testEditBillingProfileOnBillingProfilePage_2047() throws InterruptedException, SQLException{
//		randomNum = CommonUtils.getRandomNum(10000, 1000000);
//		String newBillingProfileName = TestConstants.NEW_BILLING_PROFILE_NAME_US+randomNum;
//		String lastName = "lN";
//		String defaultBillingProfileName = null;
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
//		boolean isFirstBillingProfileIsDefault=storeFrontBillingInfoPage.isFirstBillingProfileIsDefault();
//		storeFrontBillingInfoPage.clickOnEditOfFirstBillingProfile();
//		storeFrontBillingInfoPage.enterNewBillingNameOnCard(newBillingProfileName+" "+lastName);
//		storeFrontBillingInfoPage.enterNewBillingCardNumber(TestConstants.CARD_NUMBER);
//		storeFrontBillingInfoPage.selectNewBillingCardExpirationDate();
//		storeFrontBillingInfoPage.enterNewBillingSecurityCode(TestConstants.SECURITY_CODE);
//		storeFrontBillingInfoPage.selectNewBillingCardAddress();
//		storeFrontBillingInfoPage.selectUseThisBillingProfileFutureAutoshipChkbox();
//		storeFrontBillingInfoPage.clickOnSaveBillingProfile();
//		//--------------- Verify that Newly edited Billing profile is listed in the Billing profiles section-----------------------------------------------------------------------------------------------------
//
//		s_assert.assertTrue(storeFrontBillingInfoPage.isTheBillingAddressPresentOnPage(newBillingProfileName),"Newly edited Billing profile is NOT listed on the billing info page");
//		if(isFirstBillingProfileIsDefault==true)
//			s_assert.assertTrue(storeFrontBillingInfoPage.isDefaultBillingAddressSelected(newBillingProfileName),"Already Default billing profile is not DEFAULT selected on billing info page");
//		else
//			s_assert.assertTrue(storeFrontBillingInfoPage.isDefaultBillingAddressSelected(defaultBillingProfileName),"Already Default billing profile is not DEFAULT selected on billing info page");	
//
//
//		//-----------------------Verify That newly edited Billing Profile is Selected for Adhoc order Cart page-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
//
//		storeFrontHomePage.hoverOnShopLinkAndClickAllProductsLinksAfterLogin();
//		storeFrontHomePage.clickAddToBagButton(country);
//		s_assert.assertTrue(storeFrontHomePage.isCartPageDisplayed(), "Cart page is not displayed");
//		logger.info("Cart page is displayed");
//		storeFrontHomePage.clickOnCheckoutButton();
//		storeFrontHomePage.clickOnShippingAddressNextStepBtn();
//		s_assert.assertTrue(storeFrontHomePage.isTheBillingAddressPresentOnPage(newBillingProfileName),"Newly edited Billing profile is NOT listed on Adhoc cart");
//		if(isFirstBillingProfileIsDefault==true)
//			s_assert.assertTrue(storeFrontHomePage.isBillingProfileIsSelectedByDefault(newBillingProfileName),"Already Default billing profile is not DEFAULT selected on adhoc cart");
//		else	
//			s_assert.assertTrue(storeFrontHomePage.isBillingProfileIsSelectedByDefault(defaultBillingProfileName),"Already Default billing profile is not DEFAULT selected on adhoc cart");
//		storeFrontHomePage.clickOnRodanAndFieldsLogo();
//
//		//--------------------------------------Verify That newly edited Billing Profile is selected for autoship order-----------------------------------------------------------------------------------------------------------------------------------------------
//		storeFrontConsultantPage.clickOnWelcomeDropDown();
//		storeFrontCartAutoShipPage = storeFrontConsultantPage.clickEditCrpLinkPresentOnWelcomeDropDown();
//		storeFrontUpdateCartPage = storeFrontCartAutoShipPage.clickUpdateMoreInfoLink();
//		storeFrontUpdateCartPage.clickOnEditPaymentBillingProfile();
//		s_assert.assertTrue(storeFrontHomePage.isTheBillingAddressPresentOnPage(newBillingProfileName),"Newly edited Billing profile is NOT listed on autoship cart");
//		s_assert.assertTrue(storeFrontUpdateCartPage.isNewBillingProfileIsSelectedByDefault(newBillingProfileName),"Newly edited Billing profile is not selected by default on CRP cart page");
//		s_assert.assertAll();
//	}
//
//	//Hybris Phase 2-2342:Edit billing profile | My Account | Not check
//	@Test(priority=2)
//	public void testEditBillingProfileMyAccountFutureAutoshipNotChecked_2342() throws InterruptedException{
//		randomNum = CommonUtils.getRandomNum(10000, 1000000);
//		String newBillingProfileName = TestConstants.NEW_BILLING_PROFILE_NAME_US+randomNum;
//		String lastName = "lN";
//		String defaultBillingProfileName = null;
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
//		boolean isFirstBillingProfileIsDefault=storeFrontBillingInfoPage.isFirstBillingProfileIsDefault();
//		storeFrontBillingInfoPage.clickOnEditOfFirstBillingProfile();
//		storeFrontBillingInfoPage.enterNewBillingNameOnCard(newBillingProfileName+" "+lastName);
//		storeFrontBillingInfoPage.enterNewBillingCardNumber(TestConstants.CARD_NUMBER);
//		storeFrontBillingInfoPage.selectNewBillingCardExpirationDate();
//		storeFrontBillingInfoPage.enterNewBillingSecurityCode(TestConstants.SECURITY_CODE);
//		storeFrontBillingInfoPage.selectNewBillingCardAddress();
//		storeFrontBillingInfoPage.clickOnSaveBillingProfile();
//		//--------------- Verify that Newly edited Billing profile is listed in the Billing profiles section-----------------------------------------------------------------------------------------------------
//
//		s_assert.assertTrue(storeFrontBillingInfoPage.isTheBillingAddressPresentOnPage(newBillingProfileName),"Newly edited Billing profile is NOT listed on the billing info page");
//		if(isFirstBillingProfileIsDefault==true)
//			s_assert.assertTrue(storeFrontBillingInfoPage.isDefaultBillingAddressSelected(newBillingProfileName),"Already Default billing profile is not DEFAULT selected on billing info page");
//		else
//			s_assert.assertTrue(storeFrontBillingInfoPage.isDefaultBillingAddressSelected(defaultBillingProfileName),"Already Default billing profile is not DEFAULT selected on billing info page");	
//
//		//-----------------------Verify That newly edited Billing Profile is Selected for Adhoc order Cart page-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
//
//		storeFrontHomePage.hoverOnShopLinkAndClickAllProductsLinksAfterLogin();
//		storeFrontHomePage.clickAddToBagButton(country);
//		s_assert.assertTrue(storeFrontHomePage.isCartPageDisplayed(), "Cart page is not displayed");
//		logger.info("Cart page is displayed");
//		storeFrontHomePage.clickOnCheckoutButton();
//		storeFrontHomePage.clickOnShippingAddressNextStepBtn();
//		s_assert.assertTrue(storeFrontHomePage.isTheBillingAddressPresentOnPage(newBillingProfileName),"Newly edited Billing profile is NOT listed on Adhoc cart");
//		if(isFirstBillingProfileIsDefault==true)
//			s_assert.assertTrue(storeFrontBillingInfoPage.isDefaultBillingAddressSelected(newBillingProfileName),"Already Default billing profile is not DEFAULT selected on billing info page");
//		else
//			s_assert.assertTrue(storeFrontBillingInfoPage.isDefaultBillingAddressSelected(defaultBillingProfileName),"Already Default billing profile is not DEFAULT selected on billing info page");
//		storeFrontHomePage.clickOnRodanAndFieldsLogo();
//
//		//--------------------------------------Verify That newly edited Billing Profile is selected for autoship order-----------------------------------------------------------------------------------------------------------------------------------------------
//		storeFrontConsultantPage.clickOnWelcomeDropDown();
//		storeFrontCartAutoShipPage = storeFrontConsultantPage.clickEditCrpLinkPresentOnWelcomeDropDown();
//		storeFrontUpdateCartPage = storeFrontCartAutoShipPage.clickUpdateMoreInfoLink();
//		storeFrontUpdateCartPage.clickOnEditPaymentBillingProfile();
//		s_assert.assertTrue(storeFrontHomePage.isTheBillingAddressPresentOnPage(newBillingProfileName),"Newly edited Billing profile is NOT listed on autoship cart");
//		s_assert.assertAll();
//	}
//
//	//Hybris Phase 2-2048:Edit billing profile during checkout
//	@Test(priority=3)
//	public void testEditBillingAdhocCheckoutFutureChecboxSelected_2048() throws InterruptedException{  
//		randomNum = CommonUtils.getRandomNum(10000, 1000000);
//		String newBillingProfileName = TestConstants.NEW_BILLING_PROFILE_NAME_US+randomNum;
//		String lastName = "lN";
//		storeFrontConsultantPage = storeFrontHomePage.clickRodanAndFieldsLogo();
//		storeFrontConsultantPage.hoverOnShopLinkAndClickAllProductsLinksAfterLogin();
//		storeFrontUpdateCartPage.clickAddToBagButton(country);
//		storeFrontUpdateCartPage.clickOnCheckoutButton();
//		storeFrontUpdateCartPage.clickOnShippingAddressNextStepBtn();
//		storeFrontUpdateCartPage.clickAddNewBillingProfileLink();
//		storeFrontUpdateCartPage.enterNewBillingCardNumber(TestConstants.CARD_NUMBER);
//		storeFrontUpdateCartPage.enterNewBillingNameOnCard(newBillingProfileName+" "+lastName);
//		storeFrontUpdateCartPage.selectNewBillingCardExpirationDate();
//		storeFrontUpdateCartPage.enterNewBillingSecurityCode(TestConstants.SECURITY_CODE);
//		storeFrontUpdateCartPage.selectNewBillingCardAddress();
//		storeFrontUpdateCartPage.clickOnSaveBillingProfile();
//		s_assert.assertTrue(storeFrontHomePage.isTheBillingAddressPresentOnPage(newBillingProfileName),"Newly edited Billing profile is NOT listed on Adhoc cart");
//		randomNum = CommonUtils.getRandomNum(10000, 1000000);
//		String editedBillingProfileName = newBillingProfileName+randomNum;
//		storeFrontUpdateCartPage.clickOnEditOnNotDefaultAddressOfBilling();
//		storeFrontUpdateCartPage.enterNewBillingCardNumber(TestConstants.CARD_NUMBER);
//		storeFrontUpdateCartPage.enterNewBillingNameOnCard(editedBillingProfileName+" "+lastName);
//		storeFrontUpdateCartPage.selectNewBillingCardExpirationDate();
//		storeFrontUpdateCartPage.enterNewBillingSecurityCode(TestConstants.SECURITY_CODE);
//		storeFrontUpdateCartPage.selectNewBillingCardAddress();
//		storeFrontUpdateCartPage.selectUseThisBillingProfileFutureAutoshipChkbox();
//		storeFrontUpdateCartPage.clickOnSaveBillingProfile();
//		s_assert.assertTrue(storeFrontHomePage.isTheBillingAddressPresentOnPage(editedBillingProfileName),"Newly edited Billing profile is NOT listed on Adhoc cart");
//		s_assert.assertTrue(storeFrontHomePage.isBillingProfileIsSelectedByDefault(newBillingProfileName),"Already Default billing profile is not DEFAULT selected on adhoc cart");
//		storeFrontUpdateCartPage.clickOnBillingNextStepBtn(); 
//		storeFrontUpdateCartPage.clickPlaceOrderBtn();
//		//		s_assert.assertFalse(storeFrontUpdateCartPage.isNewEditedBillingProfileIsPresentOnOrderConfirmationPage(newBillingProfileName+randomNum),"New Billing Profile is not selected by default on CRP cart page");
//		//		s_assert.assertTrue(storeFrontUpdateCartPage.isDefaultBillingProfileIsPresentOrderConfirmationPage(newBillingProfileName),"New Billing Profile is not selected by default on CRP cart page");
//		storeFrontConsultantPage = storeFrontUpdateCartPage.clickRodanAndFieldsLogo();
//		storeFrontConsultantPage.clickOnWelcomeDropDown();
//		storeFrontBillingInfoPage.clickBillingInfoLinkPresentOnWelcomeDropDown();
//		s_assert.assertTrue(storeFrontBillingInfoPage.verifyBillingInfoPageIsDisplayed(),"Billing Info Page is not displayed");
//		s_assert.assertTrue(storeFrontBillingInfoPage.isTheBillingAddressPresentOnPage(editedBillingProfileName),"Edited billing address is not present on billing info page");
//		s_assert.assertFalse(storeFrontBillingInfoPage.isBillingProfileIsSelectedByDefault(editedBillingProfileName),"Edited billing profile is selected as default profile");
//		s_assert.assertTrue(storeFrontBillingInfoPage.isBillingProfileIsSelectedByDefault(newBillingProfileName),"Default billing profile is not selected as default profile");
//		s_assert.assertAll();
//	}
//
//	// Hybris Phase 2-2049 :: Version : 1 :: Edit billing profile in autoship template
//	@Test(priority=4)
//	public void testEditBillingProfileInAutoshipTemplate_2049() throws InterruptedException, SQLException{
//		randomNum = CommonUtils.getRandomNum(10000, 1000000);
//		String newBillingProfileName = TestConstants.NEW_BILLING_PROFILE_NAME_US+randomNum;
//		String lastName = "lN";
//		storeFrontConsultantPage = storeFrontHomePage.clickRodanAndFieldsLogo();
//		storeFrontConsultantPage.clickOnWelcomeDropDown();
//		storeFrontCartAutoShipPage = storeFrontConsultantPage.clickEditCrpLinkPresentOnWelcomeDropDown();
//		storeFrontUpdateCartPage = storeFrontCartAutoShipPage.clickUpdateMoreInfoLink();
//		storeFrontUpdateCartPage.clickOnEditPaymentBillingProfile();
//		storeFrontUpdateCartPage.clickOnDefaultBillingProfileEdit();
//		storeFrontUpdateCartPage.enterNewBillingCardNumber(TestConstants.CARD_NUMBER);
//		storeFrontUpdateCartPage.enterNewBillingNameOnCard(newBillingProfileName+" "+lastName);
//		storeFrontUpdateCartPage.selectNewBillingCardExpirationDate();
//		storeFrontUpdateCartPage.enterNewBillingSecurityCode(TestConstants.SECURITY_CODE);
//		storeFrontUpdateCartPage.selectNewBillingCardAddress();
//		storeFrontUpdateCartPage.selectUseThisBillingProfileFutureAutoshipChkbox();
//		storeFrontUpdateCartPage.clickOnSaveBillingProfile();
//		storeFrontUpdateCartPage.clickOnNextStepBtn();
//		storeFrontUpdateCartPage.clickUpdateCartBtn();
//		storeFrontUpdateCartPage.clickOnEditPaymentBillingProfile();
//		s_assert.assertTrue(storeFrontUpdateCartPage.isNewBillingProfileIsSelectedByDefault(newBillingProfileName),"New Billing Profile is not selected by default on CRP cart page");
//
//		storeFrontConsultantPage = storeFrontUpdateCartPage.clickRodanAndFieldsLogo();
//		storeFrontConsultantPage.clickOnWelcomeDropDown();
//		storeFrontBillingInfoPage = storeFrontConsultantPage.clickBillingInfoLinkPresentOnWelcomeDropDown();
//		//--------------- Verify that Newly added Billing profile is listed in the Billing profiles section-----------------------------------------------------------------------------------------------------
//
//		s_assert.assertTrue(storeFrontBillingInfoPage.isTheBillingAddressPresentOnPage(newBillingProfileName),"Newly edited Billing profile is NOT listed on the page");
//
//		s_assert.assertFalse(storeFrontBillingInfoPage.isDefaultAddressRadioBtnSelected(newBillingProfileName),"Newly edit Billing profile is default on the page");
//
//		storeFrontConsultantPage.clickOnWelcomeDropDown();
//		storeFrontOrdersPage = storeFrontConsultantPage.clickOrdersLinkPresentOnWelcomeDropDown();
//		storeFrontOrdersPage.clickAutoshipOrderNumber();
//
//		//------------------ Verify that autoship template doesn't contains the newly added billing profile------------------------------------------------------------	---------------------------------------------	
//
//		s_assert.assertTrue(storeFrontOrdersPage.isPaymentMethodContainsName(newBillingProfileName),"Autoship Template Payment Method doesn't contains the newly added billing profile");
//
//		//-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
//
//		s_assert.assertAll();
//	}
//
//
//	// Hybris Project-2050 :: Version : 1 :: Edit billing profile during PC user or Retail user registration
//	@Test(priority=6)
//	public void testEditBillingProfileDuringPCRegistration_2050() throws InterruptedException{
//		randomNum = CommonUtils.getRandomNum(10000, 1000000);  
//		String newBillingProfileName = TestConstants.NEW_BILLING_PROFILE_NAME+randomNum;
//		String editedBillingProfileName = TestConstants.EDIT_BILLING_PROFILE_NAME+randomNum;
//		String lastName = "lN";
//		String firstName=TestConstants.FIRST_NAME+randomNum;
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
//		storeFrontHomePage.clickOnEditBillingProfile();
//		storeFrontHomePage.enterNewBillingNameOnCard(editedBillingProfileName+" "+lastName);
//		storeFrontHomePage.enterNewBillingSecurityCode(TestConstants.SECURITY_CODE);
//		storeFrontHomePage.clickOnSaveBillingProfile();
//		storeFrontUpdateCartPage.clickOnBillingNextStepBtn(); 
//		storeFrontHomePage.clickOnPCPerksTermsAndConditionsCheckBoxes();
//		storeFrontUpdateCartPage.clickPlaceOrderBtn();
//		storeFrontUpdateCartPage.clickRodanAndFieldsLogo();
//		storeFrontConsultantPage.clickOnWelcomeDropDown();
//		storeFrontBillingInfoPage = storeFrontPCUserPage.clickBillingInfoLinkPresentOnWelcomeDropDown();
//		s_assert.assertTrue(storeFrontBillingInfoPage.verifyBillingInfoPageIsDisplayed(),"Billing Info page has not been displayed");
//
//		//--------------- Verify that Newly added Billing profile is listed in the Billing profiles section-----------------------------------------------------------------------------------------------------
//
//		s_assert.assertTrue(storeFrontBillingInfoPage.isTheBillingAddressPresentOnPage(editedBillingProfileName),"Newly added Billing profile is NOT listed on the page");
//		storeFrontConsultantPage.clickOnWelcomeDropDown();
//		storeFrontCartAutoShipPage = storeFrontConsultantPage.clickEditCrpLinkPresentOnWelcomeDropDown();
//		storeFrontUpdateCartPage = storeFrontCartAutoShipPage.clickUpdateMoreInfoLink();
//		storeFrontUpdateCartPage.clickOnEditPaymentBillingProfile();
//
//		//------------------ Verify that CRP/PC cart contains the newly created billing profile address as selected ------------------------------------------------------------
//
//		s_assert.assertTrue(storeFrontUpdateCartPage.isNewBillingProfileIsSelectedByDefault(editedBillingProfileName),"New Billing Profile is not selected by default on update cart page");
//		s_assert.assertAll();
//	}
//
//	//Hybris Project-2051:Edit billing address during consultant enrollment
//	@Test(priority=7)
//	public void testEditBillingAddressDuringConsultantEnrollment_2051() throws InterruptedException	  {
//		int randomNum = CommonUtils.getRandomNum(10000, 1000000);
//		int randomNumber = CommonUtils.getRandomNum(10000, 1000000);
//		String socialInsuranceNumber = String.valueOf(CommonUtils.getRandomNum(100000000, 999999999));
//		String secondSocialInsuranceNumber = String.valueOf(CommonUtils.getRandomNum(100000000, 999999999));
//		String newBillingProfileName = TestConstants.NEW_BILLING_PROFILE_NAME_US+randomNumber;
//		String lastName = "lN";
//		storeFrontHomePage.openPWSSite(country, env);
//		storeFrontHomePage.hoverOnBecomeAConsultantAndClickEnrollNowLink();
//		storeFrontHomePage.enterUserInformationForEnrollment(kitName, regimenName, enrollmentType, TestConstants.FIRST_NAME+randomNum, TestConstants.LAST_NAME+randomNum, password, addressLine1, city,state, postalCode, phoneNumber);
//		storeFrontHomePage.clickEnrollmentNextBtn();
//		storeFrontHomePage.enterCardNumber(TestConstants.CARD_NUMBER);
//		storeFrontHomePage.enterNameOnCard(TestConstants.FIRST_NAME+randomNum);
//		storeFrontHomePage.selectNewBillingCardExpirationDate();
//		storeFrontHomePage.enterSecurityCode(TestConstants.SECURITY_CODE);
//		storeFrontHomePage.selectNewBillingCardAddress();
//		storeFrontHomePage.enterSocialInsuranceNumber(socialInsuranceNumber);
//		storeFrontHomePage.enterNameAsItAppearsOnCard(TestConstants.FIRST_NAME);
//		storeFrontHomePage.clickEnrollmentNextBtn();
//		//Edit the Billing Address on review and confirm page
//		storeFrontHomePage.clickOnEditBillingOnReviewAndConfirmPage();
//		//Edit Billing Info and rename first Name
//		storeFrontHomePage.enterNameOnCard(newBillingProfileName+" "+lastName);
//		storeFrontHomePage.enterSecurityCode(TestConstants.SECURITY_CODE);
//		storeFrontHomePage.enterSocialInsuranceNumber(secondSocialInsuranceNumber);
//		storeFrontHomePage.selectNewBillingCardAddress();
//		storeFrontHomePage.clickEnrollmentNextBtn();
//		//validate updated Account Info Details on review and confirmation page
//		s_assert.assertTrue(storeFrontHomePage.isTheTermsAndConditionsCheckBoxDisplayed(), "Terms and Conditions checkbox is not visible");
//		storeFrontHomePage.checkThePoliciesAndProceduresCheckBox();
//		storeFrontHomePage.checkTheIAcknowledgeCheckBox();  
//		storeFrontHomePage.checkTheIAgreeCheckBox();
//		storeFrontHomePage.checkTheTermsAndConditionsCheckBox();
//		storeFrontHomePage.clickOnChargeMyCardAndEnrollMeBtn();
//		storeFrontHomePage.clickOnConfirmAutomaticPayment();
//		s_assert.assertTrue(storeFrontHomePage.verifyCongratsMessage(), "Congrats Message is not visible");
//		storeFrontHomePage.clickOnRodanAndFieldsLogo();
//		s_assert.assertTrue(storeFrontHomePage.verifyWelcomeDropdownToCheckUserRegistered(), "User NOT registered successfully");
//		storeFrontConsultantPage.clickOnWelcomeDropDown();
//		storeFrontBillingInfoPage=storeFrontConsultantPage.clickBillingInfoLinkPresentOnWelcomeDropDown();
//		s_assert.assertTrue(storeFrontBillingInfoPage.verifyBillingInfoPageIsDisplayed(),"Billing info page is not displayed");
//		s_assert.assertTrue(storeFrontBillingInfoPage.isTheBillingAddressPresentOnPage(newBillingProfileName)," Billing address is not on Billing page");
//		s_assert.assertAll();
//	}
//
//	//Hybris Project-2052:Edit billing profile during CRP enrollment through my account
//	@Test(priority=8)
//	public void editBillingprofileDuringCRPEnrollmentThroughMyAccount_2052() throws InterruptedException{
//		int randomNum = CommonUtils.getRandomNum(10000, 1000000);
//		String socialInsuranceNumber = String.valueOf(CommonUtils.getRandomNum(100000000, 999999999));
//		String firstName = TestConstants.FIRST_NAME+randomNum;
//		String enrollmentType = TestConstants.STANDARD_ENROLLMENT;
//		String consultantEmail = firstName+TestConstants.EMAIL_ADDRESS_SUFFIX;
//		navigateToStoreFrontBaseURL();
//		//Enroll a consultant without CRP and pulse
//		storeFrontHomePage.hoverOnBecomeAConsultantAndClickEnrollNowLink();
//		storeFrontHomePage.searchCID();
//		storeFrontHomePage.mouseHoverSponsorDataAndClickContinue();
//		storeFrontHomePage.enterUserInformationForEnrollmentWithEmail(kitName, regimenName, enrollmentType, firstName, TestConstants.LAST_NAME,consultantEmail, password, addressLine1, city,state, postalCode, phoneNumber);
//		storeFrontHomePage.clickNextButton();
//
//		String firstBillingName = TestConstants.FIRST_NAME+randomNum;
//		storeFrontHomePage.enterCardNumber(TestConstants.CARD_NUMBER);
//		storeFrontHomePage.enterNameOnCard(firstBillingName);
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
//		// add a new billing profile
//		String newBillingProfileName = TestConstants.NEW_BILLING_PROFILE_NAME_US+randomNum;
//		storeFrontHomePage.clickOnWelcomeDropDown();
//		storeFrontBillingInfoPage = storeFrontHomePage.clickBillingInfoLinkPresentOnWelcomeDropDown();
//		s_assert.assertTrue(storeFrontBillingInfoPage.verifyBillingInfoPageIsDisplayed(),"Billing Info page has not been displayed");
//
//		storeFrontBillingInfoPage.clickAddNewBillingProfileLink();
//		storeFrontBillingInfoPage.enterNewBillingCardNumber(TestConstants.CARD_NUMBER);
//		storeFrontBillingInfoPage.enterNewBillingNameOnCard(newBillingProfileName);
//		storeFrontBillingInfoPage.selectNewBillingCardExpirationDate(TestConstants.CARD_EXP_MONTH, TestConstants.CARD_EXP_YEAR);
//		storeFrontBillingInfoPage.enterNewBillingSecurityCode(TestConstants.SECURITY_CODE);
//		storeFrontBillingInfoPage.selectNewBillingCardAddress();
//		storeFrontBillingInfoPage.clickOnSaveBillingProfile();
//
//		//--------------- Verify that Newly added Billing profile is listed in the Billing profiles section-----------------------------------------------------------------------------------------------------
//
//		s_assert.assertTrue(storeFrontBillingInfoPage.isTheBillingAddressPresentOnPage(newBillingProfileName),"Newly added Billing profile is NOT listed on the page");
//
//
//		// Enroll in CRP
//		storeFrontHomePage.clickOnWelcomeDropDown();
//		storeFrontAccountInfoPage = storeFrontHomePage.clickAccountInfoLinkPresentOnWelcomeDropDown();
//		s_assert.assertTrue(storeFrontAccountInfoPage.verifyAccountInfoPageIsDisplayed(),"shipping info page has not been displayed");
//		storeFrontAccountInfoPage.clickOnYourAccountDropdown();//added
//		storeFrontAccountInfoPage.clickOnAutoShipStatus();
//		storeFrontAccountInfoPage.clickOnEnrollInCRP();
//		storeFrontAccountInfoPage.clickOnAddToCRPButtonCreatingCRPUnderBizSite();
//		storeFrontAccountInfoPage.clickOnCRPCheckout();
//		String lastName = "In";
//		String newBillingProfileNameAfterEdit = TestConstants.NEW_BILLING_PROFILE_NAME+randomNum;
//		storeFrontUpdateCartPage.clickOnBillingNextStepButtonDuringEnrollInCRP();
//		//storeFrontUpdateCartPage.clickOnUpdateCartShippingNextStepBtnDuringEnrollment();
//		storeFrontUpdateCartPage.clickOnEditBillingProfileDuringEnrollInCRP(newBillingProfileName);
//		storeFrontUpdateCartPage.enterNewBillingCardNumber(TestConstants.CARD_NUMBER);
//		storeFrontUpdateCartPage.enterNewBillingNameOnCard(newBillingProfileNameAfterEdit+" "+lastName);
//		storeFrontUpdateCartPage.selectNewBillingCardExpirationDate();
//		storeFrontUpdateCartPage.enterNewBillingSecurityCode(TestConstants.SECURITY_CODE);
//		storeFrontUpdateCartPage.selectNewBillingCardAddress();
//		storeFrontUpdateCartPage.clickOnSaveBillingProfile();
//		s_assert.assertTrue(storeFrontUpdateCartPage.isNewBillingProfileIsSelectedByDefault(newBillingProfileNameAfterEdit),"New Billing Profile is not selected by default on CRP cart page");
//		storeFrontUpdateCartPage.clickOnBillingNextStepBtn(); 
//		storeFrontUpdateCartPage.clickOnSetupCRPAccountBtn();
//
//		// verify on billing info page
//		storeFrontUpdateCartPage.clickRodanAndFieldsLogo();
//		storeFrontUpdateCartPage.clickOnWelcomeDropDown();
//		storeFrontBillingInfoPage = storeFrontUpdateCartPage.clickBillingInfoLinkPresentOnWelcomeDropDown();
//		s_assert.assertFalse(storeFrontUpdateCartPage.isNewBillingProfileIsSelectedByDefault(newBillingProfileNameAfterEdit),"Edited Billing Profile is selected by default on Billing Profile page");
//		s_assert.assertTrue(storeFrontUpdateCartPage.isNewBillingProfileIsSelectedByDefault(firstBillingName),"Old Billing Profile is not selected by default on Billing Profile page");
//		//s_assert.assertTrue(storeFrontBillingInfoPage.isAutoshipOrderAddressTextPresent(newBillingProfileNameAfterEdit),"Default selected shipping address does not have autoship text");
//		s_assert.assertAll(); 
//	}
//
//}
