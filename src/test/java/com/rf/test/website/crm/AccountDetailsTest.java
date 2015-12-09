package com.rf.test.website.crm;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.annotations.Test;

import com.rf.core.utils.CommonUtils;
import com.rf.core.website.constants.TestConstants;
import com.rf.pages.website.CRMHomePage;
import com.rf.pages.website.CRMLoginPage;
import com.rf.pages.website.StoreFrontAccountInfoPage;
import com.rf.pages.website.StoreFrontConsultantPage;
import com.rf.pages.website.StoreFrontHomePage;
import com.rf.pages.website.StoreFrontUpdateCartPage;
import com.rf.test.website.RFWebsiteBaseTest;

public class AccountDetailsTest extends RFWebsiteBaseTest{
	private static final Logger logger = LogManager
			.getLogger(AccountDetailsTest.class.getName());
	private StoreFrontHomePage storeFrontHomePage;
	private StoreFrontConsultantPage storeFrontConsultantPage;
	private StoreFrontAccountInfoPage storeFrontAccountInfoPage;
	private StoreFrontUpdateCartPage storeFrontUpdateCartPage;
	private CRMLoginPage crmLoginpage;
	private CRMHomePage crmHomePage;
	private String kitName = null;
	private String regimenName = null;
	private String enrollmentType = null;
	private String addressLine1 = null;
	private String city = null;
	private String postalCode = null;
	private String phoneNumber = null;
	private String country = null;
	private String pcUserEmailAddress =null;
	private String rcUserEmailAddress = null;
	private String consultantEmailAddress = null;
	
	//Test enroll an Consultant and verify its details in crm.
	@Test(priority=1)
	public void testStandardEnrollmentTermsAndConditions() throws InterruptedException{
		int randomNum = CommonUtils.getRandomNum(10000, 1000000);
		String socialInsuranceNumber = String.valueOf(CommonUtils.getRandomNum(100000000, 999999999));
		country = driver.getCountry();
		enrollmentType = TestConstants.STANDARD_ENROLLMENT;
		regimenName = TestConstants.REGIMEN_NAME_UNBLEMISH;
		String firstName=TestConstants.FIRST_NAME+randomNum;
		consultantEmailAddress = firstName+TestConstants.EMAIL_ADDRESS_SUFFIX;

		if(country.equalsIgnoreCase("CA")){
			kitName = TestConstants.KIT_NAME_EXPRESS;			 
			addressLine1 = TestConstants.ADDRESS_LINE_1_CA;
			city = TestConstants.CITY_CA;
			postalCode = TestConstants.POSTAL_CODE_CA;
			phoneNumber = TestConstants.PHONE_NUMBER_CA;
		}else{
			kitName = TestConstants.KIT_NAME_EXPRESS;
			addressLine1 = TestConstants.ADDRESS_LINE_1_US;
			city = TestConstants.CITY_US;
			postalCode = TestConstants.POSTAL_CODE_US;
			phoneNumber = TestConstants.PHONE_NUMBER_US;
		}

		storeFrontHomePage = new StoreFrontHomePage(driver);
		storeFrontHomePage.hoverOnBecomeAConsultantAndClickEnrollNowLink();
		storeFrontHomePage.searchCID();
		storeFrontHomePage.mouseHoverSponsorDataAndClickContinue();
		storeFrontHomePage.enterUserInformationForEnrollment(kitName, regimenName, enrollmentType, firstName, TestConstants.LAST_NAME, password, addressLine1, city,TestConstants.PROVINCE_ALBERTA, postalCode, phoneNumber,consultantEmailAddress);
		storeFrontHomePage.clickNextButton();
		storeFrontHomePage.enterCardNumber(TestConstants.CARD_NUMBER);
		storeFrontHomePage.enterNameOnCard(TestConstants.FIRST_NAME+randomNum);
		storeFrontHomePage.selectNewBillingCardExpirationDate();
		storeFrontHomePage.enterSecurityCode(TestConstants.SECURITY_CODE);
		storeFrontHomePage.enterSocialInsuranceNumber(socialInsuranceNumber);
		storeFrontHomePage.enterNameAsItAppearsOnCard(TestConstants.FIRST_NAME);
		storeFrontHomePage.clickEnrollmentNextBtn();
		//		s_assert.assertTrue(storeFrontHomePage.verifySubsribeToPulseCheckBoxIsSelected(), "Subscribe to pulse checkbox not selected");
		//		s_assert.assertTrue(storeFrontHomePage.verifyEnrollToCRPCheckBoxIsSelected(), "Enroll to CRP checkbox not selected");
		storeFrontHomePage.clickEnrollmentNextBtn();
		storeFrontHomePage.selectProductAndProceedToAddToCRP();
		//storeFrontHomePage.addQuantityOfProduct("5");
		storeFrontHomePage.clickOnNextBtnAfterAddingProductAndQty();
		s_assert.assertTrue(storeFrontHomePage.isTheTermsAndConditionsCheckBoxDisplayed(), "Terms and Conditions checkbox is not visible");
		storeFrontHomePage.checkThePoliciesAndProceduresCheckBox();
		storeFrontHomePage.checkTheIAcknowledgeCheckBox();		
		storeFrontHomePage.checkTheIAgreeCheckBox();
		storeFrontHomePage.clickOnChargeMyCardAndEnrollMeBtn();
		s_assert.assertTrue(storeFrontHomePage.verifyPopUpForPoliciesAndProcedures(), "PopUp for policies and procedures is not visible");
		storeFrontHomePage.checkTheTermsAndConditionsCheckBox();
		storeFrontHomePage.clickOnChargeMyCardAndEnrollMeBtn();
		storeFrontHomePage.clickOnConfirmAutomaticPayment();
		s_assert.assertTrue(storeFrontHomePage.verifyCongratsMessage(), "Congrats Message is not visible");
		storeFrontHomePage.clickOnRodanAndFieldsLogo();
		s_assert.assertTrue(storeFrontHomePage.verifyWelcomeDropdownToCheckUserRegistered(), "User NOT registered successfully");
		s_assert.assertAll();

	}

	@Test(priority=2)
	public void testVerifyConsultantDetails() throws InterruptedException{
//		consultantEmailAddress="auto16626@xyz.com";
//		phoneNumber = TestConstants.NEW_ADDRESS_PHONE_NUMBER_US;
		driver.get(driver.getCrmURL());
		crmLoginpage = new CRMLoginPage(driver);
		crmHomePage = crmLoginpage.loginUser(TestConstants.CRM_LOGIN_USERNAME, TestConstants.CRM_LOGIN_PASSWORD);
		s_assert.assertTrue(crmHomePage.verifyHomePage(),"Home page does not come after login");
		crmHomePage.searchUserById(consultantEmailAddress);
		crmHomePage.clickaccountNameForAccountDetailsPage(TestConstants.FIRST_NAME);
		s_assert.assertTrue(crmHomePage.verifyAccountTypeForConsultant(),"Account type is not for created Consultant user");
		//s_assert.assertTrue(crmHomePage.verifyPhoneNumber(phoneNumber),"Phone number is not valid for user");
		s_assert.assertTrue(crmHomePage.verifyEmailAddress(consultantEmailAddress),"Email address is not valid for user");
		s_assert.assertTrue(crmHomePage.verifyEditPWSDomainButton(), "Edit Pws Domain button is not present");
		s_assert.assertTrue(crmHomePage.verifyPulseButton(),"Pulse button is not present for this user");
		s_assert.assertTrue(crmHomePage.verifyListHoverAutoShipTitle(),"List hover autoship title is not present for this user");
		s_assert.assertTrue(crmHomePage.verifyListHoverPerformanceKPI(),"Performance KPI link is not present for this user");
		s_assert.assertTrue(crmHomePage.verifyListHoverAccountActivities(),"Account activities link is not present for this user");
		s_assert.assertTrue(crmHomePage.verifyCountryName(),"Country Name is not as expected for this user");
		s_assert.assertAll();
	}
	//Test enroll an PC user and verify its details in crm.
	@Test(priority=3)
	public void testPCEnrollment() throws InterruptedException{
		int randomNum = CommonUtils.getRandomNum(10000, 1000000);		
		String newBillingProfileName = TestConstants.NEW_BILLING_PROFILE_NAME+randomNum;
		String lastName = "lN";
		country = driver.getCountry();
		storeFrontHomePage = new StoreFrontHomePage(driver);
		String firstName=TestConstants.PC_FIRST_NAME+randomNum;
		pcUserEmailAddress = firstName+TestConstants.EMAIL_ADDRESS_SUFFIX;
		
		if(country.equalsIgnoreCase("CA")){			 
			phoneNumber = TestConstants.PHONE_NUMBER_CA;
		}else{
			phoneNumber = TestConstants.PHONE_NUMBER_US;
		}
		// Click on our product link that is located at the top of the page and then click in on quick shop
		storeFrontHomePage.hoverOnShopLinkAndClickAllProductsLinks();

		// Products are displayed?
		s_assert.assertTrue(storeFrontHomePage.areProductsDisplayed(), "quickshop products not displayed");
		logger.info("Quick shop products are displayed");

		//Select a product and proceed to buy it.
		storeFrontHomePage.selectProductAndProceedToBuy();

		//Cart page is displayed?
		s_assert.assertTrue(storeFrontHomePage.isCartPageDisplayed(), "Cart page is not displayed");
		logger.info("Cart page is displayed");

		//1 product is in the Shopping Cart?
		s_assert.assertTrue(storeFrontHomePage.verifyNumberOfProductsInCart("1"), "number of products in the cart is NOT 1");
		logger.info("1 product is successfully added to the cart");

		//Click on Check out
		storeFrontHomePage.clickOnCheckoutButton();

		//Log in or create an account page is displayed?
		s_assert.assertTrue(storeFrontHomePage.isLoginOrCreateAccountPageDisplayed(), "Login or Create Account page is NOT displayed");
		logger.info("Login or Create Account page is displayed");

		//Enter the User information and check the "Become a Preferred Customer" checkbox and click the create account button
		storeFrontHomePage.enterNewPCDetails(firstName, TestConstants.LAST_NAME,pcUserEmailAddress, password);

		//Enter the Main account info and click next
		storeFrontHomePage.enterMainAccountInfo();
		logger.info("Main account details entered");

		storeFrontHomePage.clickOnContinueWithoutSponsorLink();
		storeFrontHomePage.clickOnNextButtonAfterSelectingSponsor();

		storeFrontHomePage.clickOnShippingAddressNextStepBtn();
		//Enter Billing Profile
		storeFrontHomePage.enterNewBillingCardNumber(TestConstants.CARD_NUMBER);
		storeFrontHomePage.enterNewBillingNameOnCard(newBillingProfileName+" "+lastName);
		storeFrontHomePage.selectNewBillingCardExpirationDate();
		storeFrontHomePage.enterNewBillingSecurityCode(TestConstants.SECURITY_CODE);
		storeFrontHomePage.selectNewBillingCardAddress();
		storeFrontHomePage.clickOnSaveBillingProfile();
		storeFrontHomePage.clickOnBillingNextStepBtn();
		storeFrontHomePage.clickOnPCPerksTermsAndConditionsCheckBoxes();
		storeFrontHomePage.clickPlaceOrderBtn();
		//storeFrontHomePage.switchToPreviousTab();
		if(country.equalsIgnoreCase("CA"))
		{storeFrontHomePage.switchToPreviousTab();}
		//storeFrontHomePage.switchToPreviousTab(); 
		storeFrontHomePage.clickOnRodanAndFieldsLogo();
		s_assert.assertTrue(storeFrontHomePage.verifyWelcomeDropdownToCheckUserRegistered(), "User NOT registered successfully");
		//s_assert.assertTrue(storeFrontHomePage.getUserNameAForVerifyLogin(firstName).contains(firstName),"Profile Name After Login"+firstName+" and on UI is "+storeFrontHomePage.getUserNameAForVerifyLogin(firstName));
		s_assert.assertAll();	

	}
	@Test(priority=4)//dependsOnMethods="testPCEnrollment"
	public void testVerifyPCUserDetails() throws InterruptedException{
//		pcUserEmailAddress="auto757408@xyz.com";
//		phoneNumber = TestConstants.NEW_ADDRESS_PHONE_NUMBER_US;
		driver.get(driver.getCrmURL());
		crmLoginpage = new CRMLoginPage(driver);
		crmHomePage = crmLoginpage.loginUser(TestConstants.CRM_LOGIN_USERNAME, TestConstants.CRM_LOGIN_PASSWORD);
		s_assert.assertTrue(crmHomePage.verifyHomePage(),"Home page does not come after login");
		crmHomePage.searchUserById(pcUserEmailAddress);
		crmHomePage.clickaccountNameForAccountDetailsPage(TestConstants.PC_FIRST_NAME);
		s_assert.assertTrue(crmHomePage.verifyAccountTypeForPCUser(),"Account type is not for created PC user");
		s_assert.assertTrue(crmHomePage.verifyPhoneNumber(phoneNumber),"Phone number is not valid for user");
		s_assert.assertTrue(crmHomePage.verifyEmailAddress(pcUserEmailAddress),"Email address is not valid for user");
		s_assert.assertFalse(crmHomePage.verifyEditPWSDomainButton(), "Edit Pws Domain button is present");
		s_assert.assertFalse(crmHomePage.verifyPulseButton(),"Pulse button is present for this user");
		s_assert.assertTrue(crmHomePage.verifyListHoverAutoShipTitle(),"List hover autoship title is not present for this user");
		s_assert.assertFalse(crmHomePage.verifyListHoverPerformanceKPI(),"Performance KPI link is present for this user");
		s_assert.assertFalse(crmHomePage.verifyListHoverAccountActivities(),"Account activities link is present for this user");
		s_assert.assertTrue(crmHomePage.verifyCountryName(),"Country Name is not as expected for this user");
		s_assert.assertAll();
	}
	
	//Test enroll an RC user and verify its details in crm.
	@Test(priority=5)
	public void testRCEnrollment() throws InterruptedException{
		int randomNum = CommonUtils.getRandomNum(10000, 1000000);
		String newBillingProfileName = TestConstants.NEW_BILLING_PROFILE_NAME+randomNum;
		String firstName=TestConstants.RC_FIRST_NAME+randomNum;
		String lastName = "lN";
		country = driver.getCountry();
		rcUserEmailAddress = firstName+TestConstants.EMAIL_ADDRESS_SUFFIX;
		if(country.equalsIgnoreCase("CA")){			 
			phoneNumber = TestConstants.PHONE_NUMBER_CA;
		}else{
			phoneNumber = TestConstants.PHONE_NUMBER_US;
		}
		storeFrontHomePage = new StoreFrontHomePage(driver);
		// Click on our product link that is located at the top of the page and then click in on quick shop
		storeFrontHomePage.hoverOnShopLinkAndClickAllProductsLinks();

		// Products are displayed?
		s_assert.assertTrue(storeFrontHomePage.areProductsDisplayed(), "quickshop products not displayed");
		logger.info("Quick shop products are displayed");

		//Select a product and proceed to buy it
		storeFrontHomePage.selectProductAndProceedToBuy();

		//Cart page is displayed?
		s_assert.assertTrue(storeFrontHomePage.isCartPageDisplayed(), "Cart page is not displayed");
		logger.info("Cart page is displayed");

		//one products are in the Shopping Cart?
		s_assert.assertTrue(storeFrontHomePage.verifyNumberOfProductsInCart("1"), "number of products in the cart is NOT 1");
		logger.info("1 product is successfully added to the cart");

		//Click on Check out
		storeFrontHomePage.clickOnCheckoutButton();

		//Log in or create an account page is displayed?
		s_assert.assertTrue(storeFrontHomePage.isLoginOrCreateAccountPageDisplayed(), "Login or Create Account page is NOT displayed");
		logger.info("Login or Create Account page is displayed");

		//Enter the User information and DO NOT check the "Become a Preferred Customer" checkbox and click the create account button
		storeFrontHomePage.enterNewRCDetails(firstName, TestConstants.LAST_NAME,rcUserEmailAddress, password);

		//Enter the Main account info and DO NOT check the "Become a Preferred Customer" and click next
		storeFrontHomePage.enterMainAccountInfo();
		logger.info("Main account details entered");

		storeFrontHomePage.clickOnContinueWithoutSponsorLink();
		storeFrontHomePage.clickOnNextButtonAfterSelectingSponsor();

		storeFrontHomePage.clickOnShippingAddressNextStepBtn();
		//Enter Billing Profile
		storeFrontHomePage.enterNewBillingCardNumber(TestConstants.CARD_NUMBER);
		storeFrontHomePage.enterNewBillingNameOnCard(newBillingProfileName+" "+lastName);
		storeFrontHomePage.selectNewBillingCardExpirationDate();
		storeFrontHomePage.enterNewBillingSecurityCode(TestConstants.SECURITY_CODE);
		storeFrontHomePage.selectNewBillingCardAddress();
		storeFrontHomePage.clickOnSaveBillingProfile();
		storeFrontHomePage.clickOnBillingNextStepBtn();
		storeFrontHomePage.clickPlaceOrderBtn();
		s_assert.assertTrue(storeFrontHomePage.isOrderPlacedSuccessfully(), "Order Not placed successfully");
		storeFrontHomePage.clickOnRodanAndFieldsLogo();
		s_assert.assertTrue(storeFrontHomePage.verifyWelcomeDropdownToCheckUserRegistered(), "User NOT registered successfully");
		//s_assert.assertTrue(storeFrontHomePage.getUserNameAForVerifyLogin(firstName).contains(firstName),"Profile Name After Login"+firstName+" and on UI is "+storeFrontHomePage.getUserNameAForVerifyLogin(firstName));
		s_assert.assertAll();

	}
	@Test(priority=6)
	public void testVerifyRCUserDetails() throws InterruptedException{
//		rcUserEmailAddress="RCUser677657@xyz.com";
//		phoneNumber = TestConstants.NEW_ADDRESS_PHONE_NUMBER_US;
		driver.get(driver.getCrmURL());
		crmLoginpage = new CRMLoginPage(driver);
		crmHomePage = crmLoginpage.loginUser(TestConstants.CRM_LOGIN_USERNAME, TestConstants.CRM_LOGIN_PASSWORD);
		s_assert.assertTrue(crmHomePage.verifyHomePage(),"Home page does not come after login");
		crmHomePage.searchUserById(rcUserEmailAddress);
		crmHomePage.clickaccountNameForAccountDetailsPage(TestConstants.RC_FIRST_NAME);
		s_assert.assertTrue(crmHomePage.verifyAccountTypeForRCUser(),"Account type is not for created RC user");
		s_assert.assertTrue(crmHomePage.verifyPhoneNumber(phoneNumber),"Phone number is not valid for user");
		s_assert.assertTrue(crmHomePage.verifyEmailAddress(rcUserEmailAddress),"Email address is not valid for user");
		s_assert.assertFalse(crmHomePage.verifyEditPWSDomainButton(), "Edit Pws Domain button is present");
		s_assert.assertFalse(crmHomePage.verifyPulseButton(),"Pulse button is present for this user");
		s_assert.assertFalse(crmHomePage.verifyListHoverAutoShipTitle(),"List hover autoship title is present for this user");
		s_assert.assertFalse(crmHomePage.verifyListHoverPerformanceKPI(),"Performance KPI link is present for this user");
		s_assert.assertFalse(crmHomePage.verifyListHoverAccountActivities(),"Account activities link is present for this user");
		s_assert.assertTrue(crmHomePage.verifyCountryName(),"Country Name is not as expected for this user");
		s_assert.assertAll();
}
	// Hybris Project-4494:View the Account Policies for Consultant
	@Test
	public void testViewAccountPoliciesForConsultant_4494() throws InterruptedException{
		consultantEmailAddress="auto734005@xyz.com";
		driver.get(driver.getCrmURL());
		crmLoginpage = new CRMLoginPage(driver);
		crmHomePage = crmLoginpage.loginUser(TestConstants.CRM_LOGIN_USERNAME, TestConstants.CRM_LOGIN_PASSWORD);
		s_assert.assertTrue(crmHomePage.verifyHomePage(),"Home page does not come after login");
		crmHomePage.searchUserById(consultantEmailAddress);
	
	}
	// Hybris Project-4539:View Billing profile for a PC
	@Test//WIP
	public void testViewBillingProfileForPC_4539() throws InterruptedException{
		consultantEmailAddress="auto734005@xyz.com";
		driver.get(driver.getCrmURL());
		crmLoginpage = new CRMLoginPage(driver);
		crmHomePage = crmLoginpage.loginUser(TestConstants.CRM_LOGIN_USERNAME, TestConstants.CRM_LOGIN_PASSWORD);
		s_assert.assertTrue(crmHomePage.verifyHomePage(),"Home page does not come after login");
		crmHomePage.searchUserById("pc");
		crmHomePage.clickOnAccountNameForAccountDetailPageInAccountSection();
		int countOfBillingProfile=crmHomePage.clickOnBillingProfileAndGetNumberBillingProfile();
		s_assert.assertTrue(crmHomePage.verifyBillingInfoActionField(),"Action field is not present");
		assertEquals(countOfBillingProfile,crmHomePage.getCountOfBillingProfileUnderBillingProfileSection(),"Billing Addresses count is not same");
		s_assert.assertAll();
		
	}
	
	@Test//WIP
	public void testViewBillingProfileForRC_4540() throws InterruptedException{
		consultantEmailAddress="auto92831796394@xyz.com";
		driver.get(driver.getCrmURL());
		crmLoginpage = new CRMLoginPage(driver);
		crmHomePage = crmLoginpage.loginUser(TestConstants.CRM_LOGIN_USERNAME, TestConstants.CRM_LOGIN_PASSWORD);
		s_assert.assertTrue(crmHomePage.verifyHomePage(),"Home page does not come after login");
		crmHomePage.searchUserById("auto92831796394@xyz.com");
		crmHomePage.clickOnAccountNameForAccountDetailPageInAccountSection();
		int countOfBillingProfile=crmHomePage.clickOnBillingProfileAndGetNumberBillingProfile();
		s_assert.assertTrue(crmHomePage.verifyBillingInfoActionField(),"Action field is not present");
		assertEquals(countOfBillingProfile,crmHomePage.getCountOfBillingProfileUnderBillingProfileSection(),"Billing Addresses count is not same");
		s_assert.assertAll();
  }
	@Test //WIP
	public void testviewShippingProfileForConsultant_4542()throws InterruptedException{
		consultantEmailAddress="auto459940@xyz.com";
		driver.get(driver.getCrmURL());
		crmLoginpage = new CRMLoginPage(driver);
		crmHomePage = crmLoginpage.loginUser(TestConstants.CRM_LOGIN_USERNAME, TestConstants.CRM_LOGIN_PASSWORD);
		s_assert.assertTrue(crmHomePage.verifyHomePage(),"Home page does not come after login");
		crmHomePage.searchUserById("auto459940@xyz.com");
		
	}
	@Test //WIP
	public void testviewShippingProfileForPC_4543()throws InterruptedException{
		consultantEmailAddress="auto459940@xyz.com";
		driver.get(driver.getCrmURL());
		crmLoginpage = new CRMLoginPage(driver);
		crmHomePage = crmLoginpage.loginUser(TestConstants.CRM_LOGIN_USERNAME, TestConstants.CRM_LOGIN_PASSWORD);
		s_assert.assertTrue(crmHomePage.verifyHomePage(),"Home page does not come after login");
		crmHomePage.searchUserById("auto459940@xyz.com");
}
}












