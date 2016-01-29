package com.rf.test.website.storeFront.regression.expressEnrollment.canadaProvince;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.annotations.Test;

import com.rf.core.utils.CommonUtils;
import com.rf.core.website.constants.TestConstants;
import com.rf.pages.website.storeFront.StoreFrontConsultantPage;
import com.rf.pages.website.storeFront.StoreFrontHomePage;
import com.rf.pages.website.storeFront.StoreFrontShippingInfoPage;
import com.rf.test.website.RFWebsiteBaseTest;

public class EnrollmentTest extends RFWebsiteBaseTest{
	private static final Logger logger = LogManager
			.getLogger(EnrollmentTest.class.getName());

	private StoreFrontHomePage storeFrontHomePage;
	private StoreFrontConsultantPage storeFrontConsultantPage;
	private StoreFrontShippingInfoPage storeFrontShippingInfoPage; 
	private String kitName = null;
	private String regimenName = null;
	private String enrollmentType = null;
	private String addressLine1 = null;
	private String city = null;
	private String postalCode = null;
	private String phoneNumber = null;

	//[Hybris Project-3612,Hybris Project-1670,Hybris Project-1669,Hybris Project-1668,Hybris Project-1667,Hybris Project-1665,Hybris Project-1664,Hybris Project-1663,Hybris Project-1662,Hybris Project-1661]
	@Test(dataProvider="rfTestData")
	public void testExpressEnrollmentCanadaProvince(String province) throws InterruptedException{
		if(driver.getCountry().equalsIgnoreCase("ca")){
			int randomNum = CommonUtils.getRandomNum(10000, 1000000);
			String socialInsuranceNumber = String.valueOf(CommonUtils.getRandomNum(100000000, 999999999));
			enrollmentType = TestConstants.EXPRESS_ENROLLMENT;
			regimenName = TestConstants.REGIMEN_NAME_REDEFINE;
			kitName = TestConstants.KIT_NAME_PERSONAL;			 
			addressLine1 = TestConstants.ADDRESS_LINE_1_CA;
			city = TestConstants.CITY_CA;
			postalCode = TestConstants.POSTAL_CODE_CA;
			phoneNumber = TestConstants.PHONE_NUMBER_CA;

			storeFrontHomePage = new StoreFrontHomePage(driver);
			storeFrontHomePage.hoverOnBecomeAConsultantAndClickEnrollNowLink();

			storeFrontHomePage.searchCID();
			storeFrontHomePage.mouseHoverSponsorDataAndClickContinue();
			storeFrontHomePage.enterUserInformationForEnrollment(kitName, regimenName, enrollmentType, TestConstants.FIRST_NAME+randomNum, TestConstants.LAST_NAME+randomNum, password, addressLine1, city,province, postalCode, phoneNumber);
			storeFrontHomePage.clickEnrollmentNextBtn();
			//storeFrontHomePage.acceptTheVerifyYourShippingAddressPop();		
			storeFrontHomePage.enterCardNumber(TestConstants.CARD_NUMBER);
			storeFrontHomePage.enterNameOnCard(TestConstants.FIRST_NAME+randomNum);
			storeFrontHomePage.selectNewBillingCardExpirationDate();
			storeFrontHomePage.enterSecurityCode(TestConstants.SECURITY_CODE);
			storeFrontHomePage.enterSocialInsuranceNumber(socialInsuranceNumber);
			storeFrontHomePage.enterNameAsItAppearsOnCard(TestConstants.FIRST_NAME);
			storeFrontHomePage.clickEnrollmentNextBtn();
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
			s_assert.assertTrue(storeFrontHomePage.verifyWelcomeDropdownToCheckUserRegistered(), "User NOT registered successfully");

			s_assert.assertAll();

		}
		else{
			logger.info("NOT EXECUTED...Test is ONLY for CANADA env");
		}
	}

	//Hybris Project-1699 :: Version : 1 :: Express Enrollment for Nunavut province and tryto ship adhoc order at quebec address. 
	@Test
	public void testExpressEnrollmentNunavutProvince_1699() throws InterruptedException{
		if(driver.getCountry().equalsIgnoreCase("ca")){
			int randomNum = CommonUtils.getRandomNum(10000, 1000000);
			String socialInsuranceNumber = String.valueOf(CommonUtils.getRandomNum(100000000, 999999999));
			enrollmentType = TestConstants.EXPRESS_ENROLLMENT;
			regimenName = TestConstants.REGIMEN_NAME_REDEFINE;
			kitName = TestConstants.KIT_NAME_PERSONAL;    
			addressLine1 = TestConstants.ADDRESS_LINE_1_QUEBEC;
			city = TestConstants.CITY_QUEBEC;
			postalCode = TestConstants.POSTAL_CODE_QUEBEC;
			phoneNumber = TestConstants.PHONE_NUMBER_CA;

			storeFrontHomePage = new StoreFrontHomePage(driver);
			storeFrontHomePage.hoverOnBecomeAConsultantAndClickEnrollNowLink();

			storeFrontHomePage.searchCID();
			storeFrontHomePage.mouseHoverSponsorDataAndClickContinue();
			storeFrontHomePage.enterUserInformationForEnrollment(kitName, regimenName, enrollmentType, TestConstants.FIRST_NAME+randomNum, TestConstants.LAST_NAME+randomNum, password, addressLine1, city,TestConstants.PROVINCE_NUNAVUT, postalCode, phoneNumber);
			storeFrontHomePage.clickEnrollmentNextBtn();
			//storeFrontHomePage.acceptTheVerifyYourShippingAddressPop();  
			storeFrontHomePage.enterCardNumber(TestConstants.CARD_NUMBER);
			storeFrontHomePage.enterNameOnCard(TestConstants.FIRST_NAME+randomNum);
			storeFrontHomePage.selectNewBillingCardExpirationDate();
			storeFrontHomePage.enterSecurityCode(TestConstants.SECURITY_CODE);
			storeFrontHomePage.enterSocialInsuranceNumber(socialInsuranceNumber);
			storeFrontHomePage.enterNameAsItAppearsOnCard(TestConstants.FIRST_NAME);
			storeFrontHomePage.clickEnrollmentNextBtn();
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
			s_assert.assertTrue(storeFrontHomePage.verifyWelcomeDropdownToCheckUserRegistered(), "User NOT registered successfully");
			storeFrontConsultantPage = new StoreFrontConsultantPage(driver);
			storeFrontConsultantPage.clickOnWelcomeDropDown();
			storeFrontShippingInfoPage = storeFrontConsultantPage.clickShippingLinkPresentOnWelcomeDropDown();
			s_assert.assertTrue(storeFrontShippingInfoPage.verifyShippingInfoPageIsDisplayed(),"shipping info page has not been displayed");
			storeFrontShippingInfoPage.clickAddNewShippingProfileLink();
			String newShippingAddressName = TestConstants.ADDRESS_NAME_US+randomNum;
			String lastName = "ln";
			storeFrontShippingInfoPage.enterNewShippingAddressName(newShippingAddressName+" "+lastName);
			storeFrontShippingInfoPage.enterNewShippingAddressLine1(TestConstants.ADDRESS_LINE_1_QUEBEC);
			storeFrontShippingInfoPage.enterNewShippingAddressCity(TestConstants.CITY_QUEBEC);
			storeFrontShippingInfoPage.selectNewShippingAddressState();
			storeFrontShippingInfoPage.enterNewShippingAddressPostalCode(TestConstants.POSTAL_CODE_QUEBEC);
			storeFrontShippingInfoPage.enterNewShippingAddressPhoneNumber(TestConstants.PHONE_NUMBER_CA);
//			storeFrontShippingInfoPage.selectFirstCardNumber();
//			storeFrontShippingInfoPage.enterNewShippingAddressSecurityCode(TestConstants.SECURITY_CODE);
			storeFrontShippingInfoPage.clickOnSaveShippingProfile();
			s_assert.assertTrue(storeFrontShippingInfoPage.isShippingAddressPresentOnShippingPage(newShippingAddressName), "New Shipping address is not listed on Shipping profile page");
			s_assert.assertAll();

		}
		else{
			logger.info("NOT EXECUTED...Test is ONLY for CANADA env");
		}
	}

	//Hybris Project-1698 :: Version : 1 :: Express Enrollment for Northwest territories province and adding qubec address and make it default.
	@Test
	public void testExpressEnrollmentNorthWestProvince_1698() throws InterruptedException{
		if(driver.getCountry().equalsIgnoreCase("ca")){
			int randomNum = CommonUtils.getRandomNum(10000, 1000000);
			String socialInsuranceNumber = String.valueOf(CommonUtils.getRandomNum(100000000, 999999999));
			enrollmentType = TestConstants.EXPRESS_ENROLLMENT;
			regimenName = TestConstants.REGIMEN_NAME_REDEFINE;
			kitName = TestConstants.KIT_NAME_PERSONAL;			 
			addressLine1 = TestConstants.ADDRESS_LINE_1_CA;
			city = TestConstants.CITY_CA;
			postalCode = TestConstants.POSTAL_CODE_CA;
			phoneNumber = TestConstants.PHONE_NUMBER_CA;

			storeFrontHomePage = new StoreFrontHomePage(driver);
			storeFrontHomePage.hoverOnBecomeAConsultantAndClickEnrollNowLink();

			storeFrontHomePage.searchCID();
			storeFrontHomePage.mouseHoverSponsorDataAndClickContinue();
			storeFrontHomePage.enterUserInformationForEnrollment(kitName, regimenName, enrollmentType, TestConstants.FIRST_NAME+randomNum, TestConstants.LAST_NAME+randomNum, password, addressLine1, city,TestConstants.PROVINCE_NORTHWEST_TERRITORIES, postalCode, phoneNumber);
			storeFrontHomePage.clickEnrollmentNextBtn();
			//storeFrontHomePage.acceptTheVerifyYourShippingAddressPop();		
			storeFrontHomePage.enterCardNumber(TestConstants.CARD_NUMBER);
			storeFrontHomePage.enterNameOnCard(TestConstants.FIRST_NAME+randomNum);
			storeFrontHomePage.selectNewBillingCardExpirationDate();
			storeFrontHomePage.enterSecurityCode(TestConstants.SECURITY_CODE);
			storeFrontHomePage.enterSocialInsuranceNumber(socialInsuranceNumber);
			storeFrontHomePage.enterNameAsItAppearsOnCard(TestConstants.FIRST_NAME);
			storeFrontHomePage.clickEnrollmentNextBtn();
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
			s_assert.assertTrue(storeFrontHomePage.verifyWelcomeDropdownToCheckUserRegistered(), "User NOT registered successfully");
			s_assert.assertAll();

		}else{
			logger.info("NOT EXECUTED...Test is ONLY for CANADA env");
		}
	}

	//Hybris Project-1292 :: Version : 1 :: Customer living in Quebec cannot be enrolled as consultant. 
	@Test
	public void testCustomerLivingInQuebecCannotEnroll_1292() throws InterruptedException{
		if(driver.getCountry().equalsIgnoreCase("ca")){
			enrollmentType = TestConstants.EXPRESS_ENROLLMENT;
			regimenName = TestConstants.REGIMEN_NAME_REDEFINE;
			kitName = TestConstants.KIT_NAME_PERSONAL;			 
			addressLine1 = TestConstants.ADDRESS_LINE_1_QUEBEC;
			city = TestConstants.CITY_QUEBEC;
			postalCode = TestConstants.POSTAL_CODE_QUEBEC;
			phoneNumber = TestConstants.PHONE_NUMBER_CA;

			storeFrontHomePage = new StoreFrontHomePage(driver);
			storeFrontHomePage.hoverOnBecomeAConsultantAndClickEnrollNowLink();

			storeFrontHomePage.searchCID();
			storeFrontHomePage.mouseHoverSponsorDataAndClickContinue();
			storeFrontHomePage.selectEnrollmentKitPage(kitName, regimenName);		
			storeFrontHomePage.chooseEnrollmentOption(enrollmentType);
			s_assert.assertTrue(storeFrontHomePage.verifyQuebecProvinceIsDisabled(),"Quebec province in the province drop down is not disabled");
			s_assert.assertAll();

		}else{
			logger.info("NOT EXECUTED...Test is ONLY for CANADA env");
		}
	}

	// Hybris Project-2190 :: Version : 1 :: Enrolling RC living in Quebec 
	@Test
	public void testQuebecRCEnrollment_2190() throws InterruptedException{
		if(driver.getCountry().equalsIgnoreCase("ca")){
			int randomNum = CommonUtils.getRandomNum(10000, 1000000);
			String newBillingProfileName = TestConstants.NEW_BILLING_PROFILE_NAME+randomNum;
			String lastName = "lN";
			addressLine1 = TestConstants.ADDRESS_LINE_1_QUEBEC;
			city = TestConstants.CITY_QUEBEC;
			postalCode = TestConstants.POSTAL_CODE_QUEBEC;
			phoneNumber = TestConstants.PHONE_NUMBER_CA;

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

			//In the Cart page add one more product
			storeFrontHomePage.addAnotherProduct();

			//Two products are in the Shopping Cart?
			s_assert.assertTrue(storeFrontHomePage.verifyNumberOfProductsInCart("2"), "number of products in the cart is NOT 2");
			logger.info("2 products are successfully added to the cart");

			//Click on Check out
			storeFrontHomePage.clickOnCheckoutButton();

			//Log in or create an account page is displayed?
			s_assert.assertTrue(storeFrontHomePage.isLoginOrCreateAccountPageDisplayed(), "Login or Create Account page is NOT displayed");
			logger.info("Login or Create Account page is displayed");

			//Enter the User information and DO NOT check the "Become a Preferred Customer" checkbox and click the create account button
			storeFrontHomePage.enterNewRCDetails(TestConstants.FIRST_NAME+randomNum, TestConstants.LAST_NAME+randomNum, password);

			//		//CheckoutPage is displayed?
			//		s_assert.assertTrue(storeFrontHomePage.isCheckoutPageDisplayed(), "Checkout page has NOT displayed");
			//		logger.info("Checkout page has displayed");

			//Enter the Main account info and DO NOT check the "Become a Preferred Customer" and click next
			storeFrontHomePage.enterMainAccountInfo(addressLine1, city, TestConstants.PROVINCE_QUEBEC, postalCode, phoneNumber);
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
			s_assert.assertTrue(storeFrontHomePage.verifyWelcomeDropdownToCheckUserRegistered(), "User NOT registered successfully");
			s_assert.assertAll();	
		}else{
			logger.info("NOT EXECUTED...Test is ONLY for CANADA env");
		}
	}

	//Hybris Project-2189 :: Version : 1 :: Enrolling PC living in Quebec 
	@Test
	public void testQuebecPCEnrollment_2189() throws InterruptedException{
		if(driver.getCountry().equalsIgnoreCase("ca")){
			int randomNum = CommonUtils.getRandomNum(10000, 1000000);		
			String newBillingProfileName = TestConstants.NEW_BILLING_PROFILE_NAME+randomNum;
			String lastName = "lN";
			addressLine1 = TestConstants.ADDRESS_LINE_1_QUEBEC;
			city = TestConstants.CITY_QUEBEC;
			postalCode = TestConstants.POSTAL_CODE_QUEBEC;
			phoneNumber = TestConstants.PHONE_NUMBER_CA;

			storeFrontHomePage = new StoreFrontHomePage(driver);
			// Click on our product link that is located at the top of the page and then click in on quick shop
			//			storeFrontHomePage.clickOnShopLink();
			//			storeFrontHomePage.clickOnAllProductsLink();
			storeFrontHomePage.hoverOnShopLinkAndClickAllProductsLinks();

			// Products are displayed?
			s_assert.assertTrue(storeFrontHomePage.areProductsDisplayed(), "quickshop products not displayed");
			logger.info("Quick shop products are displayed");

			//Select a product with the price less than $80 and proceed to buy it
			storeFrontHomePage.applyPriceFilterLowToHigh();
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

			//Enter the User information and DO NOT check the "Become a Preferred Customer" checkbox and click the create account button
			storeFrontHomePage.enterNewPCDetails(TestConstants.FIRST_NAME+randomNum, TestConstants.LAST_NAME+randomNum, password);

			//Pop for PC threshold validation
			s_assert.assertTrue(storeFrontHomePage.isPopUpForPCThresholdPresent(),"Threshold poup for PC validation NOT present");

			//In the Cart page add one more product
			storeFrontHomePage.addAnotherProduct();

			//Click on Check out
			storeFrontHomePage.clickOnCheckoutButton();

			//Enter the Main account info and DO NOT check the "Become a Preferred Customer" and click next
			storeFrontHomePage.enterMainAccountInfo(addressLine1, city, TestConstants.PROVINCE_QUEBEC, postalCode, phoneNumber);
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
			
			s_assert.assertTrue(storeFrontHomePage.verifyPCPerksTermsAndConditionsPopup(),"PC Perks terms and conditions popup not visible when checkboxes for t&c not selected and place order button clicked");
			logger.info("PC Perks terms and conditions popup is visible when checkboxes for t&c not selected and place order button clicked");
			storeFrontHomePage.clickOnPCPerksTermsAndConditionsCheckBoxes();
			storeFrontHomePage.clickPlaceOrderBtn();
			
			storeFrontHomePage.clickOnRodanAndFieldsLogo();
			s_assert.assertTrue(storeFrontHomePage.verifyWelcomeDropdownToCheckUserRegistered(), "User NOT registered successfully");
			s_assert.assertAll();	
		}else{
			logger.info("NOT EXECUTED...Test is ONLY for CANADA env");
		}

	}

	// Hybris Project-2193 :: Version : 1 :: Verify if QAS gives quebec address suggestions for address entered during consultant enrollment.
	@Test
	public void testQASGivesSuggestionsForQuebecAddress_2193() throws InterruptedException{
		if(driver.getCountry().equalsIgnoreCase("ca")){
			int randomNum = CommonUtils.getRandomNum(10000, 1000000);
			enrollmentType = TestConstants.EXPRESS_ENROLLMENT;
			regimenName = TestConstants.REGIMEN_NAME_REDEFINE;
			kitName = TestConstants.KIT_NAME_PERSONAL;    
			addressLine1 = TestConstants.ADDRESS_LINE_1_QUEBEC;
			city = TestConstants.CITY_QUEBEC;
			postalCode = TestConstants.POSTAL_CODE_QUEBEC;
			phoneNumber = TestConstants.PHONE_NUMBER_CA;

			storeFrontHomePage = new StoreFrontHomePage(driver);
			storeFrontHomePage.hoverOnBecomeAConsultantAndClickEnrollNowLink();

			storeFrontHomePage.searchCID();
			storeFrontHomePage.mouseHoverSponsorDataAndClickContinue();
			storeFrontHomePage.enterUserInformationForEnrollment(kitName, regimenName, enrollmentType, TestConstants.FIRST_NAME+randomNum, TestConstants.LAST_NAME+randomNum, password, addressLine1, city,TestConstants.PROVINCE_NUNAVUT, postalCode, phoneNumber);
			storeFrontHomePage.clickEnrollmentNextBtnWithoutHandlingPopUP();
			s_assert.assertTrue(storeFrontHomePage.verifySuggesstionsForEnteredAddressPop(), "QAS pop up for Quebec address suggestions has NOT appeared");

			s_assert.assertAll();
		}else{
			logger.info("NOT EXECUTED...Test is ONLY for CANADA env");
		}
	}

}
