package com.rf.test.website.storeFront.miniRegression;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.annotations.Test;

import com.rf.core.utils.CommonUtils;
import com.rf.core.utils.DBUtil;
import com.rf.core.website.constants.TestConstants;
import com.rf.core.website.constants.dbQueries.DBQueries_RFO;
import com.rf.pages.website.StoreFrontAccountInfoPage;
import com.rf.pages.website.StoreFrontBillingInfoPage;
import com.rf.pages.website.StoreFrontCartAutoShipPage;
import com.rf.pages.website.StoreFrontConsultantPage;
import com.rf.pages.website.StoreFrontHomePage;
import com.rf.pages.website.StoreFrontOrdersPage;
import com.rf.pages.website.StoreFrontPCUserPage;
import com.rf.pages.website.StoreFrontRCUserPage;
import com.rf.pages.website.StoreFrontShippingInfoPage;
import com.rf.pages.website.StoreFrontUpdateCartPage;
import com.rf.test.website.RFWebsiteBaseTest;


public class EnrollmentTest extends RFWebsiteBaseTest{
	private static final Logger logger = LogManager
			.getLogger(EnrollmentTest.class.getName());

	private StoreFrontHomePage storeFrontHomePage;
	private StoreFrontConsultantPage storeFrontConsultantPage;
	private StoreFrontAccountInfoPage storeFrontAccountInfoPage;
	private StoreFrontUpdateCartPage storeFrontUpdateCartPage;
	private StoreFrontShippingInfoPage storeFrontShippingInfoPage;
	private StoreFrontCartAutoShipPage storeFrontCartAutoShipPage;
	private StoreFrontBillingInfoPage storeFrontBillingInfoPage;
	private StoreFrontOrdersPage storeFrontOrdersPage;
	private StoreFrontRCUserPage storeFrontRCUserPage;
	private StoreFrontPCUserPage storeFrontPCUserPage;

	private String kitName = null;
	private String regimenName = null;
	private String enrollmentType = null;
	private String addressLine1 = null;
	private String city = null;
	private String postalCode = null;
	private String phoneNumber = null;
	private String country = null;
	private String env = null;
	private String RFO_DB = null;

	// Hybris Project-87 :: Version : 1 :: Standard Enrollment Billing Profile Edit Shipping Info 
	@Test//ok
	public void testStandardEnrollmentExpressBusinessKitRedefineRegimenEdit_87() throws InterruptedException{
		int randomNum = CommonUtils.getRandomNum(10000, 1000000);
		int randomNumber = CommonUtils.getRandomNum(10000, 1000000);
		String socialInsuranceNumber = String.valueOf(CommonUtils.getRandomNum(100000000, 999999999));
		String newAddressLine1=null;
		country = driver.getCountry();
		enrollmentType = TestConstants.STANDARD_ENROLLMENT;
		regimenName = TestConstants.REGIMEN_NAME_REDEFINE;

		if(country.equalsIgnoreCase("CA")){
			kitName = TestConstants.KIT_PRICE_EXPRESS_CA;    
			addressLine1 = TestConstants.ADDRESS_LINE_1_CA;
			newAddressLine1 = TestConstants.NEW_ADDRESS_LINE_1_CA;
			city = TestConstants.CITY_CA;
			postalCode = TestConstants.POSTAL_CODE_CA;
			phoneNumber = TestConstants.PHONE_NUMBER_CA;
		}else{
			kitName = TestConstants.KIT_PRICE_EXPRESS_US;
			addressLine1 = TestConstants.ADDRESS_LINE_1_US;
			newAddressLine1 = TestConstants.NEW_ADDRESS_LINE_1_US;
			city = TestConstants.CITY_US;
			postalCode = TestConstants.POSTAL_CODE_US;
			phoneNumber = TestConstants.PHONE_NUMBER_US;
		}

		storeFrontHomePage = new StoreFrontHomePage(driver);
		storeFrontHomePage.clickOnOurBusinessLink();
		storeFrontHomePage.clickOnOurEnrollNowLink();
		storeFrontHomePage.searchCID();
		storeFrontHomePage.mouseHoverSponsorDataAndClickContinue();
		storeFrontHomePage.enterUserInformationForEnrollment(kitName, regimenName, enrollmentType, TestConstants.FIRST_NAME+randomNum, TestConstants.LAST_NAME+randomNum, password, addressLine1, city, postalCode, phoneNumber);
		storeFrontHomePage.clickEnrollmentNextBtn();
		storeFrontHomePage.acceptTheVerifyYourShippingAddressPop();  
		storeFrontHomePage.enterCardNumber(TestConstants.CARD_NUMBER);
		storeFrontHomePage.enterNameOnCard(TestConstants.FIRST_NAME+randomNum);
		storeFrontHomePage.selectNewBillingCardExpirationDate();
		storeFrontHomePage.enterSecurityCode(TestConstants.SECURITY_CODE);
		storeFrontHomePage.enterSocialInsuranceNumber(socialInsuranceNumber);
		storeFrontHomePage.enterNameAsItAppearsOnCard(TestConstants.FIRST_NAME);
		storeFrontHomePage.clickEnrollmentNextBtn();
		s_assert.assertTrue(storeFrontHomePage.verifySubsribeToPulseCheckBoxIsSelected(), "Subscribe to pulse checkbox not selected");
		s_assert.assertTrue(storeFrontHomePage.verifyEnrollToCRPCheckBoxIsSelected(), "Enroll to CRP checkbox not selected");
		storeFrontHomePage.clickEnrollmentNextBtn();
		storeFrontHomePage.selectProductAndProceedToAddToCRP();
		storeFrontHomePage.addQuantityOfProduct("5");
		storeFrontHomePage.clickOnNextBtnAfterAddingProductAndQty();
		s_assert.assertTrue(storeFrontHomePage.isTheTermsAndConditionsCheckBoxDisplayed(), "Terms and Conditions checkbox is not visible");
		storeFrontHomePage.clickOnReviewAndConfirmShippingEditBtn();
		String newFirstName = TestConstants.FIRST_NAME+randomNumber;
		String newLastName = TestConstants.LAST_NAME+randomNumber;
		storeFrontHomePage.enterUserInformationForEnrollment(newFirstName, newLastName, password, newAddressLine1, city, postalCode, phoneNumber);
		storeFrontHomePage.clickNextButton();
		s_assert.assertTrue(storeFrontHomePage.isReviewAndConfirmPageContainsShippingAddress(newAddressLine1), "Shiiping address is not updated on Review and Confirm page after EDIT");
		s_assert.assertTrue(storeFrontHomePage.isReviewAndConfirmPageContainsFirstAndLastName(newFirstName+" "+newLastName), "First and last Name is not updated on Review and Confirm page after EDIT");
		storeFrontHomePage.checkThePoliciesAndProceduresCheckBox();
		storeFrontHomePage.checkTheIAcknowledgeCheckBox();  
		storeFrontHomePage.checkTheIAgreeCheckBox();
		storeFrontHomePage.clickOnEnrollMeBtn();
		s_assert.assertTrue(storeFrontHomePage.verifyPopUpForPoliciesAndProcedures(), "PopUp for policies and procedures is not visible");
		storeFrontHomePage.checkTheTermsAndConditionsCheckBox();
		storeFrontHomePage.clickOnChargeMyCardAndEnrollMeBtn();
		s_assert.assertTrue(storeFrontHomePage.verifyCongratsMessage(), "Congrats Message is not visible");
		storeFrontHomePage.clickOnRodanAndFieldsLogo();
		s_assert.assertTrue(storeFrontHomePage.verifyWelcomeDropdownToCheckUserRegistered(), "User NOT registered successfully");
		s_assert.assertAll();
	}

	//Hybris Project-90:Standard Enrollment Billing Profile - Main Account Info - New
	@Test //ok
	public void testStandardEnrollmentBillingProfile_MainAccountInfo_New_90() throws InterruptedException{
		int randomNum = CommonUtils.getRandomNum(10000, 1000000);
		int randomNumber = CommonUtils.getRandomNum(10000, 1000000);
		String socialInsuranceNumber = String.valueOf(CommonUtils.getRandomNum(100000000, 999999999));
		country = driver.getCountry();
		enrollmentType = TestConstants.STANDARD_ENROLLMENT;
		regimenName = TestConstants.REGIMEN_NAME_REDEFINE;
		String firstName=TestConstants.FIRST_NAME+randomNum;
		String lastName = TestConstants.LAST_NAME;
		String newBillingAddName = TestConstants.FIRST_NAME+randomNumber;
		if(country.equalsIgnoreCase("CA")){
			kitName = TestConstants.KIT_PRICE_EXPRESS_CA;   
			addressLine1 = TestConstants.ADDRESS_LINE_1_CA;
			city = TestConstants.CITY_CA;
			postalCode = TestConstants.POSTAL_CODE_CA;
			phoneNumber = TestConstants.PHONE_NUMBER_CA;
		}else{
			kitName = TestConstants.KIT_PRICE_EXPRESS_US;
			addressLine1 = TestConstants.ADDRESS_LINE_1_US;
			city = TestConstants.CITY_US;
			postalCode = TestConstants.POSTAL_CODE_US;
			phoneNumber = TestConstants.PHONE_NUMBER_US;
		}

		storeFrontHomePage = new StoreFrontHomePage(driver);
		storeFrontHomePage.clickOnOurBusinessLink();
		storeFrontHomePage.clickOnOurEnrollNowLink();
		storeFrontHomePage.searchCID();
		storeFrontHomePage.mouseHoverSponsorDataAndClickContinue();
		storeFrontHomePage.enterUserInformationForEnrollment(kitName, regimenName, enrollmentType, firstName, TestConstants.LAST_NAME+randomNum, password, addressLine1, city, postalCode, phoneNumber);
		storeFrontHomePage.clickNextButton();
		storeFrontHomePage.enterCardNumber(TestConstants.CARD_NUMBER);
		storeFrontHomePage.enterNameOnCard(TestConstants.FIRST_NAME+randomNum);
		storeFrontHomePage.selectNewBillingCardExpirationDate();
		storeFrontHomePage.enterSecurityCode(TestConstants.SECURITY_CODE);
		storeFrontHomePage.enterSocialInsuranceNumber(socialInsuranceNumber);
		storeFrontHomePage.enterNameAsItAppearsOnCard(TestConstants.FIRST_NAME);
		storeFrontHomePage.clickEnrollmentNextBtn();
		s_assert.assertTrue(storeFrontHomePage.verifySubsribeToPulseCheckBoxIsSelected(), "Subscribe to pulse checkbox not selected");
		s_assert.assertTrue(storeFrontHomePage.verifyEnrollToCRPCheckBoxIsSelected(), "Enroll to CRP checkbox not selected");
		storeFrontHomePage.clickEnrollmentNextBtn();
		storeFrontHomePage.selectProductAndProceedToAddToCRP();
		storeFrontHomePage.clickOnNextBtnAfterAddingProductAndQty();
		storeFrontHomePage.clickOnReviewAndConfirmBillingEditBtn();
		storeFrontHomePage.enterNewBillingCardNumber(TestConstants.CARD_NUMBER);
		storeFrontHomePage.enterNewBillingNameOnCard(newBillingAddName+" "+lastName);
		storeFrontHomePage.enterNewBillingSecurityCode(TestConstants.SECURITY_CODE);
		storeFrontHomePage.enterSocialInsuranceNumber(socialInsuranceNumber);
		storeFrontHomePage.enterNameAsItAppearsOnCard(TestConstants.FIRST_NAME);
		storeFrontHomePage.clickEnrollmentNextBtn();
		s_assert.assertTrue(storeFrontHomePage.validateNewBillingAddressPresentOnReviewPage(newBillingAddName),"new billing address is not present on Review and confirm page");
		storeFrontHomePage.checkThePoliciesAndProceduresCheckBox();
		storeFrontHomePage.checkTheIAcknowledgeCheckBox();  
		storeFrontHomePage.checkTheIAgreeCheckBox();
		storeFrontHomePage.checkTheTermsAndConditionsCheckBox();
		storeFrontHomePage.clickOnChargeMyCardAndEnrollMeBtn();
		storeFrontHomePage.clickOnConfirmAutomaticPayment();
		s_assert.assertTrue(storeFrontHomePage.verifyCongratsMessage(), "Congrats Message is not visible");
		storeFrontHomePage.clickOnRodanAndFieldsLogo();
		s_assert.assertTrue(storeFrontHomePage.verifyWelcomeDropdownToCheckUserRegistered(), "User NOT registered successfully");
		s_assert.assertAll();
	}

	//Hybris Project-1306: Corp: PC Enroll- Request a sponsor functionality
	@Test //ok
	public void testPCEnrollRequestASponsorFunctionality() throws InterruptedException{
		int randomNum = CommonUtils.getRandomNum(10000, 1000000);  
		String newBillingProfileName = TestConstants.NEW_BILLING_PROFILE_NAME+randomNum;
		String lastName = "lN";
		String country = driver.getCountry();
		String env = driver.getEnvironment();

		storeFrontHomePage = new StoreFrontHomePage(driver);
		storeFrontHomePage.openPWSSite(country, env);
		storeFrontHomePage.clickOnShopLink();
		storeFrontHomePage.clickOnAllProductsLink();
		// Products are displayed?
		s_assert.assertTrue(storeFrontHomePage.areProductsDisplayed(), "quickshop products not displayed");
		logger.info("Quick shop products are displayed");
		storeFrontHomePage.selectProductAndProceedToBuy();
		//Cart page is displayed?
		s_assert.assertTrue(storeFrontHomePage.isCartPageDisplayed(), "Cart page is not displayed");
		logger.info("Cart page is displayed");

		//Click on Check out
		storeFrontHomePage.clickOnCheckoutButton();
		//Log in or create an account page is displayed?
		s_assert.assertTrue(storeFrontHomePage.isLoginOrCreateAccountPageDisplayed(), "Login or Create Account page is NOT displayed");
		logger.info("Login or Create Account page is displayed");
		//Enter the User information and DO check the "Become a Preferred Customer" checkbox and click the create account button
		storeFrontHomePage.enterNewPCDetails(TestConstants.FIRST_NAME+randomNum, TestConstants.LAST_NAME+randomNum, password);
		storeFrontHomePage.enterMainAccountInfo();
		storeFrontHomePage.clickOnNotYourSponsorLink();
		storeFrontHomePage.enterSponsorIdDuringCreationOfPC("test");
		storeFrontHomePage.mouseHoverSponsorDataAndClickContinueForPC();
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
		s_assert.assertTrue(storeFrontHomePage.validatePCPerksCheckBoxIsDisplayed());
		storeFrontHomePage.clickPlaceOrderBtn();
		s_assert.assertTrue(storeFrontHomePage.verifyPCPerksTermsAndConditionsPopup(),"PC Perks terms and conditions popup not visible when checkboxes for t&c not selected and place order button clicked");
		logger.info("PC Perks terms and conditions popup is visible when checkboxes for t&c not selected and place order button clicked");
		storeFrontHomePage.clickOnPCPerksTermsAndConditionsCheckBoxes();
		storeFrontHomePage.clickPlaceOrderBtn();
		s_assert.assertTrue(storeFrontHomePage.verifyPcPerksConfirmMessage(), "Order Not placed successfully");
		storeFrontHomePage.clickOnRodanAndFieldsLogo();
		s_assert.assertTrue(storeFrontHomePage.verifyWelcomeDropdownToCheckUserRegistered(), "User NOT registered successfully");
		s_assert.assertAll();

	}

	//Hybris Project-94:Express Enrollment Billing Profile Billing Info - Edit
	@Test //ok
	public void testExpressEnrollmentBillingProfileBillingInfo_Edit_94() throws InterruptedException{
		int randomNum = CommonUtils.getRandomNum(10000, 1000000);
		String socialInsuranceNumber = String.valueOf(CommonUtils.getRandomNum(100000000, 999999999));
		country = driver.getCountry();
		enrollmentType = TestConstants.EXPRESS_ENROLLMENT;
		regimenName = TestConstants.REGIMEN_NAME_REDEFINE;
		String firstName=TestConstants.FIRST_NAME+randomNum;

		if(country.equalsIgnoreCase("CA")){
			kitName = TestConstants.KIT_PRICE_EXPRESS_CA;    
			addressLine1 = TestConstants.ADDRESS_LINE_1_CA;
			city = TestConstants.CITY_CA;
			postalCode = TestConstants.POSTAL_CODE_CA;
			phoneNumber = TestConstants.PHONE_NUMBER_CA;
		}else{
			kitName = TestConstants.KIT_PRICE_EXPRESS_US;
			addressLine1 = TestConstants.ADDRESS_LINE_1_US;
			city = TestConstants.CITY_US;
			postalCode = TestConstants.POSTAL_CODE_US;
			phoneNumber = TestConstants.PHONE_NUMBER_US;
		}

		storeFrontHomePage = new StoreFrontHomePage(driver);
		storeFrontHomePage.openPWSSite(country, driver.getEnvironment());
		storeFrontHomePage.clickOnOurBusinessLink();
		storeFrontHomePage.clickOnOurEnrollNowLink();
		storeFrontHomePage.enterUserInformationForEnrollment(kitName, regimenName, enrollmentType, firstName, TestConstants.LAST_NAME+randomNum, password, addressLine1, city, postalCode, phoneNumber);
		storeFrontHomePage.clickNextButton();
		storeFrontHomePage.enterCardNumber(TestConstants.CARD_NUMBER);
		storeFrontHomePage.enterNameOnCard(TestConstants.FIRST_NAME+randomNum);
		storeFrontHomePage.selectNewBillingCardExpirationDate();
		storeFrontHomePage.enterSecurityCode(TestConstants.SECURITY_CODE);
		storeFrontHomePage.enterSocialInsuranceNumber(socialInsuranceNumber);
		storeFrontHomePage.enterNameAsItAppearsOnCard(TestConstants.FIRST_NAME);
		storeFrontHomePage.clickEnrollmentNextBtn();
		storeFrontHomePage.clickOnReviewAndConfirmBillingEditBtn();
		storeFrontHomePage.enterNewBillingCardNumber(TestConstants.CARD_NUMBER_SECOND);
		storeFrontHomePage.selectNewBillingCardExpirationDate();
		String expirationMonth = storeFrontHomePage.getExpirationMonth();
		String expirationYear = storeFrontHomePage.getExpirationYear();
		storeFrontHomePage.enterSecurityCode(TestConstants.SECURITY_CODE);
		storeFrontHomePage.enterSocialInsuranceNumber(socialInsuranceNumber);
		storeFrontHomePage.clickEnrollmentNextBtn();
		s_assert.assertTrue(storeFrontHomePage.validateBillingExpirationDate(expirationYear),"billling date is not as expected");
		s_assert.assertTrue(storeFrontHomePage.validateBillingInfoUpdated(expirationMonth,expirationYear,"4747"),"billing info is not updated as expected");
		s_assert.assertAll();
	}

	// Hybris Phase 2-2029 :: Version : 1 :: Add shipping address on 'Shipping Profile' page  
	@Test //ok
	public void testAddNewShippingAddressOnShippingProfilePage_2029() throws InterruptedException, SQLException{
		int randomNum = CommonUtils.getRandomNum(10000, 1000000);
		RFO_DB = driver.getDBNameRFO(); 
		List<Map<String, Object>> randomConsultantList =  null;
		String consultantEmailID = null;
		String accountID = null;
		storeFrontHomePage = new StoreFrontHomePage(driver);

		while(true){
			randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFO.GET_RANDOM_ACTIVE_CONSULTANT_WITH_ORDERS_AND_AUTOSHIPS_RFO,RFO_DB);
			consultantEmailID = (String) getValueFromQueryResult(randomConsultantList, "UserName");  
			accountID = String.valueOf(getValueFromQueryResult(randomConsultantList, "AccountID"));
			logger.info("Account Id of the user is "+accountID);

			storeFrontConsultantPage = storeFrontHomePage.loginAsConsultant(consultantEmailID, password);
			boolean isLoginError = driver.getCurrentUrl().contains("error");
			if(isLoginError){
				logger.info("SITE NOT FOUND for the user "+consultantEmailID);
				driver.get(driver.getURL()+"/"+driver.getCountry());
			}
			else
				break;
		}


		s_assert.assertTrue(storeFrontConsultantPage.verifyConsultantPage(),"Consultant Page doesn't contain Welcome User Message");
		logger.info("login is successful");

		storeFrontConsultantPage.clickOnWelcomeDropDown();
		storeFrontShippingInfoPage = storeFrontConsultantPage.clickShippingLinkPresentOnWelcomeDropDown();
		s_assert.assertTrue(storeFrontShippingInfoPage.verifyShippingInfoPageIsDisplayed(),"shipping info page has not been displayed");
		storeFrontShippingInfoPage.clickAddNewShippingProfileLink();
		String newShippingAddressName = TestConstants.ADDRESS_NAME_US+randomNum;
		String lastName = "ln";
		storeFrontShippingInfoPage.enterNewShippingAddressName(newShippingAddressName+" "+lastName);
		storeFrontShippingInfoPage.enterNewShippingAddressLine1(TestConstants.ADDRESS_LINE_1_CA);
		storeFrontShippingInfoPage.enterNewShippingAddressCity(TestConstants.CITY_CA);
		storeFrontShippingInfoPage.selectNewShippingAddressState();
		storeFrontShippingInfoPage.enterNewShippingAddressPostalCode(TestConstants.POSTAL_CODE_CA);
		storeFrontShippingInfoPage.enterNewShippingAddressPhoneNumber(TestConstants.PHONE_NUMBER_CA);
		storeFrontShippingInfoPage.selectFirstCardNumber();
		storeFrontShippingInfoPage.enterNewShippingAddressSecurityCode(TestConstants.SECURITY_NUMBER_US);
		storeFrontShippingInfoPage.selectUseThisShippingProfileFutureAutoshipChkbox();
		storeFrontShippingInfoPage.clickOnSaveShippingProfile();

		//--------------- Verify that Newly added Shipping is listed in the Shipping profiles section-----------------------------------------------------------------------------------------------------

		s_assert.assertTrue(storeFrontShippingInfoPage.isShippingAddressPresentOnShippingPage(newShippingAddressName), "New Shipping address is not listed on Shipping profile page");

		//------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

		//--------------- Verify That 'Autoship Order Address' Text is displayed under default shipping Address-------------------------------------------------------------------------------------------

		s_assert.assertTrue(storeFrontShippingInfoPage.isAutoshipOrderAddressTextPresent(newShippingAddressName), "Autoship order text is not present under the new Shipping Address when future autoship checkbox is selected");

		//------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

		storeFrontConsultantPage.clickOnWelcomeDropDown();
		storeFrontCartAutoShipPage = storeFrontConsultantPage.clickEditCrpLinkPresentOnWelcomeDropDown();
		storeFrontUpdateCartPage = storeFrontCartAutoShipPage.clickUpdateMoreInfoLink();
		storeFrontUpdateCartPage.clickOnEditShipping();

		//---------------Verify that the new added shipping address is displayed in 'Shipment' section on update autoship cart page------------------------------------------------------------------------

		s_assert.assertTrue(storeFrontUpdateCartPage.isShippingAddressPresent(newShippingAddressName), "New Shipping address NOT selected in update cart under shipping section");

		//-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

		storeFrontUpdateCartPage.clickRodanAndFieldsLogo();
		s_assert.assertAll();  
	}

	//Hybris Phase 2:2094 Pop up to update autoship shipping profile on changing default selection
	@Test //ok
	public void testChangeDefaultShippingProfile_2094() throws InterruptedException{
		int randomNum = CommonUtils.getRandomNum(10000, 1000000);
		RFO_DB = driver.getDBNameRFO(); 
		List<Map<String, Object>> randomConsultantList =  null;
		String consultantEmailID = null;
		String accountID = null;
		storeFrontHomePage = new StoreFrontHomePage(driver);

		while(true){
			randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFO.GET_RANDOM_ACTIVE_CONSULTANT_WITH_ORDERS_AND_AUTOSHIPS_RFO,RFO_DB);
			consultantEmailID = (String) getValueFromQueryResult(randomConsultantList, "UserName");  
			accountID = String.valueOf(getValueFromQueryResult(randomConsultantList, "AccountID"));
			logger.info("Account Id of the user is "+accountID);

			storeFrontConsultantPage = storeFrontHomePage.loginAsConsultant(consultantEmailID, password);
			boolean isLoginError = driver.getCurrentUrl().contains("error");
			if(isLoginError){
				logger.info("SITE NOT FOUND for the user "+consultantEmailID);
				driver.get(driver.getURL()+"/"+driver.getCountry());
			}
			else
				break;
		}

		s_assert.assertTrue(storeFrontConsultantPage.verifyConsultantPage(),"Consultant Page doesn't contain Welcome User Message");
		logger.info("login is successful");

		storeFrontConsultantPage.clickOnWelcomeDropDown();
		storeFrontShippingInfoPage = storeFrontConsultantPage.clickShippingLinkPresentOnWelcomeDropDown();
		s_assert.assertTrue(storeFrontShippingInfoPage.verifyShippingInfoPageIsDisplayed(),"shipping info page has not been displayed");
		storeFrontShippingInfoPage.clickAddNewShippingProfileLink();
		String newShippingAddressName = TestConstants.ADDRESS_NAME_US+randomNum;
		String lastName = "ln";
		storeFrontShippingInfoPage.enterNewShippingAddressName(newShippingAddressName+" "+lastName);
		storeFrontShippingInfoPage.enterNewShippingAddressLine1(TestConstants.ADDRESS_LINE_1_CA);
		storeFrontShippingInfoPage.enterNewShippingAddressCity(TestConstants.CITY_CA);
		storeFrontShippingInfoPage.selectNewShippingAddressState();
		storeFrontShippingInfoPage.enterNewShippingAddressPostalCode(TestConstants.POSTAL_CODE_CA);
		storeFrontShippingInfoPage.enterNewShippingAddressPhoneNumber(TestConstants.PHONE_NUMBER_CA);
		storeFrontShippingInfoPage.selectFirstCardNumber();
		storeFrontShippingInfoPage.enterNewShippingAddressSecurityCode(TestConstants.SECURITY_NUMBER_US);
		storeFrontShippingInfoPage.selectUseThisShippingProfileFutureAutoshipChkbox();
		storeFrontShippingInfoPage.clickOnSaveShippingProfile();

		//--------------- Verify that Newly added Shipping is listed in the Shipping profiles section-----------------------------------------------------------------------------------------------------

		s_assert.assertTrue(storeFrontShippingInfoPage.isShippingAddressPresentOnShippingPage(newShippingAddressName), "New Shipping address is not listed on Shipping profile page");

		//------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

		storeFrontShippingInfoPage.makeShippingProfileAsDefault(newShippingAddressName);
		s_assert.assertTrue(storeFrontShippingInfoPage.isDefaultAddressRadioBtnSelected(newShippingAddressName),"Default shipping address is not selected");
		s_assert.assertTrue(storeFrontShippingInfoPage.isAutoshipOrderAddressTextPresent(newShippingAddressName),"Autoship order address is not present under default shipping profile");

		storeFrontConsultantPage.clickOnWelcomeDropDown();
		storeFrontOrdersPage = storeFrontConsultantPage.clickOrdersLinkPresentOnWelcomeDropDown();
		storeFrontOrdersPage.clickAutoshipOrderNumber();

		//------------------ Verify that autoship template contains the default shipping profile address by verifying by name------------------------------------------------------------  

		s_assert.assertTrue(storeFrontOrdersPage.isShippingAddressContainsName(newShippingAddressName),"Autoship Template Shipping Address doesn't contains the default shipping address");

		//-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

		s_assert.assertAll();  
	}

	// Hybris Phase 2-2041 :: Version : 1 :: Add new billing profile on 'Billing Profile' page
	@Test //ok
	public void testAddNewBillingProfileOnBillingProfilePage_2041() throws InterruptedException, SQLException{
		int randomNum = CommonUtils.getRandomNum(10000, 1000000);
		RFO_DB = driver.getDBNameRFO(); 
		List<Map<String, Object>> randomConsultantList =  null;
		String consultantEmailID = null;
		String accountID = null;
		storeFrontHomePage = new StoreFrontHomePage(driver);
		String newBillingProfileName = TestConstants.NEW_BILLING_PROFILE_NAME_US+randomNum;
		String lastName = "lN";
		while(true){
			randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFO.GET_RANDOM_ACTIVE_CONSULTANT_WITH_ORDERS_AND_AUTOSHIPS_RFO,RFO_DB);
			consultantEmailID = (String) getValueFromQueryResult(randomConsultantList, "UserName");  
			accountID = String.valueOf(getValueFromQueryResult(randomConsultantList, "AccountID"));
			logger.info("Account Id of the user is "+accountID);

			storeFrontConsultantPage = storeFrontHomePage.loginAsConsultant(consultantEmailID, password);
			boolean isLoginError = driver.getCurrentUrl().contains("error");
			if(isLoginError){
				logger.info("SITE NOT FOUND for the user "+consultantEmailID);
				driver.get(driver.getURL()+"/"+driver.getCountry());
			}
			else
				break;
		}


		s_assert.assertTrue(storeFrontConsultantPage.verifyConsultantPage(),"Consultant Page doesn't contain Welcome User Message");
		logger.info("login is successful");

		storeFrontConsultantPage.clickOnWelcomeDropDown();
		storeFrontBillingInfoPage = storeFrontConsultantPage.clickBillingInfoLinkPresentOnWelcomeDropDown();
		s_assert.assertTrue(storeFrontBillingInfoPage.verifyBillingInfoPageIsDisplayed(),"Billing Info page has not been displayed");

		storeFrontBillingInfoPage.clickAddNewBillingProfileLink();
		storeFrontBillingInfoPage.enterNewBillingCardNumber(TestConstants.CARD_NUMBER);
		storeFrontBillingInfoPage.enterNewBillingNameOnCard(newBillingProfileName+" "+lastName);
		storeFrontBillingInfoPage.selectNewBillingCardExpirationDate(TestConstants.CARD_EXP_MONTH, TestConstants.CARD_EXP_YEAR);
		storeFrontBillingInfoPage.enterNewBillingSecurityCode(TestConstants.SECURITY_CODE);
		storeFrontBillingInfoPage.selectNewBillingCardAddress();
		storeFrontBillingInfoPage.selectUseThisBillingProfileFutureAutoshipChkbox();
		storeFrontBillingInfoPage.clickOnSaveBillingProfile();

		//--------------- Verify that Newly added Billing profile is listed in the Billing profiles section-----------------------------------------------------------------------------------------------------

		s_assert.assertTrue(storeFrontBillingInfoPage.isTheBillingAddressPresentOnPage(newBillingProfileName),"Newly added Billing profile is NOT listed on the page");

		//------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

		storeFrontConsultantPage.clickOnWelcomeDropDown();
		storeFrontOrdersPage = storeFrontConsultantPage.clickOrdersLinkPresentOnWelcomeDropDown();
		storeFrontOrdersPage.clickAutoshipOrderNumber();

		//------------------ Verify that autoship template contains the newly created billing profile ------------------------------------------------------------  

		s_assert.assertTrue(storeFrontOrdersPage.isPaymentMethodContainsName(newBillingProfileName),"Autoship Template Payment Method doesn't contains the new billing profile even when future autoship checkbox is selected");

		//-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------


		storeFrontConsultantPage.clickOnWelcomeDropDown();
		storeFrontOrdersPage = storeFrontConsultantPage.clickOrdersLinkPresentOnWelcomeDropDown();
		storeFrontOrdersPage.clickOnFirstAdHocOrder();

		//------------------ Verify that adhoc orders template doesn't contains the newly created billing profile------------------------------------------------------------

		s_assert.assertFalse(storeFrontOrdersPage.isPaymentMethodContainsName(newBillingProfileName),"AdHoc Orders Template Payment Method contains new billing profile when future autoship checkbox is selected");

		//-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

		storeFrontConsultantPage.clickOnWelcomeDropDown();
		storeFrontCartAutoShipPage = storeFrontConsultantPage.clickEditCrpLinkPresentOnWelcomeDropDown();
		storeFrontUpdateCartPage = storeFrontCartAutoShipPage.clickUpdateMoreInfoLink();
		storeFrontUpdateCartPage.clickOnEditPaymentBillingProfile();

		//------------------ Verify that CRP/PC cart contains the newly created billing profile address as selected ------------------------------------------------------------

		s_assert.assertTrue(storeFrontUpdateCartPage.isNewBillingProfileIsSelectedByDefault(newBillingProfileName),"New Billing Profile is not selected by default on update cart page");

		//-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

		storeFrontConsultantPage = storeFrontUpdateCartPage.clickRodanAndFieldsLogo();
		s_assert.assertAll();  
	}

	//Hybris Phase 2-2043:Add billing profile in autoship template
	@Test //ok
	public void testAddBillingAutoshipCartFutureCheckboxSelected_2043() throws InterruptedException{  
		int randomNum = CommonUtils.getRandomNum(10000, 1000000);

		RFO_DB = driver.getDBNameRFO();
		List<Map<String, Object>> randomConsultantList =  null;
		String consultantEmailID = null;
		String newBillingProfileName = TestConstants.NEW_BILLING_PROFILE_NAME_US+randomNum;
		String lastName = "lN";
		String accountID = null;
		storeFrontUpdateCartPage = new StoreFrontUpdateCartPage(driver);
		storeFrontHomePage = new StoreFrontHomePage(driver);
		while(true){
			randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFO.GET_RANDOM_ACTIVE_CONSULTANT_WITH_ORDERS_AND_AUTOSHIPS_RFO,RFO_DB);
			consultantEmailID = (String) getValueFromQueryResult(randomConsultantList, "UserName");  
			accountID = String.valueOf(getValueFromQueryResult(randomConsultantList, "AccountID"));
			logger.info("Account Id of the user is "+accountID);

			storeFrontConsultantPage = storeFrontHomePage.loginAsConsultant(consultantEmailID, password);
			boolean isLoginError = driver.getCurrentUrl().contains("error");
			if(isLoginError){
				logger.info("SITE NOT FOUND for the user "+consultantEmailID);
				driver.get(driver.getURL()+"/"+driver.getCountry());
			}
			else{
				s_assert.assertTrue(storeFrontConsultantPage.verifyConsultantPage(),"Consultant Page doesn't contain Welcome User Message");
				storeFrontConsultantPage.clickOnWelcomeDropDown();
				if(storeFrontConsultantPage.isEditCrpLinkPresentOnWelcomeDropdown()){
					break;
				}else{
					storeFrontConsultantPage.clickOnWelcomeDropDown();
					driver.pauseExecutionFor(2000);
					logout();
					driver.get(driver.getURL()+"/"+driver.getCountry());
					continue;
				}
			}
		}
		storeFrontCartAutoShipPage = storeFrontConsultantPage.clickEditCrpLinkPresentOnWelcomeDropDown();
		storeFrontUpdateCartPage = storeFrontCartAutoShipPage.clickUpdateMoreInfoLink();
		storeFrontUpdateCartPage.clickOnEditPaymentBillingProfile();
		storeFrontUpdateCartPage.clickAddNewBillingProfileLink();
		storeFrontUpdateCartPage.enterNewBillingCardNumber(TestConstants.CARD_NUMBER);
		storeFrontUpdateCartPage.enterNewBillingNameOnCard(newBillingProfileName+" "+lastName);
		storeFrontUpdateCartPage.selectNewBillingCardExpirationDate();
		storeFrontUpdateCartPage.enterNewBillingSecurityCode(TestConstants.SECURITY_CODE);
		storeFrontUpdateCartPage.selectNewBillingCardAddress();
		storeFrontUpdateCartPage.selectUseThisBillingProfileFutureAutoshipChkbox();
		storeFrontUpdateCartPage.clickOnSaveBillingProfile();
		storeFrontUpdateCartPage.clickOnBillingNextStepBtn(); 
		storeFrontUpdateCartPage.clickOnEditPaymentBillingProfile();
		s_assert.assertTrue(storeFrontUpdateCartPage.isNewBillingProfileIsSelectedByDefault(newBillingProfileName),"New Billing Profile is not selected by default on CRP cart page");
		storeFrontUpdateCartPage.clickOnBillingNextStepBtn();
		storeFrontUpdateCartPage.clickUpdateCartBtn();
		storeFrontConsultantPage = storeFrontUpdateCartPage.clickRodanAndFieldsLogo();
		storeFrontConsultantPage.clickOnWelcomeDropDown();
		storeFrontOrdersPage = storeFrontConsultantPage.clickOrdersLinkPresentOnWelcomeDropDown();
		storeFrontOrdersPage.clickAutoshipOrderNumber();

		//------------------ Verify that autoship template contains the newly added billing profile------------------------------------------------------------ --------------------------------------------- 

		s_assert.assertTrue(storeFrontOrdersPage.isPaymentMethodContainsName(newBillingProfileName),"Autoship Template Payment Method doesn't contains the newly added billing profile");

		//-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

		s_assert.assertAll();
	}

	// Hybris Phase 2-2047 :: Version : 1 :: Edit billing profile on 'Billing Profile' page 
	@Test //ok
	public void testEditBillingProfileOnBillingProfilePage_2047() throws InterruptedException, SQLException{
		int randomNum = CommonUtils.getRandomNum(10000, 1000000);
		RFO_DB = driver.getDBNameRFO();

		List<Map<String, Object>> randomConsultantList =  null;
		String consultantEmailID = null;
		String newBillingProfileName = TestConstants.NEW_BILLING_PROFILE_NAME_US+randomNum;
		String lastName = "lN";
		String accountID = null;
		while(true){
			randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFO.GET_RANDOM_ACTIVE_CONSULTANT_WITH_ORDERS_AND_AUTOSHIPS_RFO,RFO_DB);
			consultantEmailID = (String) getValueFromQueryResult(randomConsultantList, "UserName");  
			accountID = String.valueOf(getValueFromQueryResult(randomConsultantList, "AccountID"));
			logger.info("Account Id of the user is "+accountID);

			storeFrontHomePage = new StoreFrontHomePage(driver);
			storeFrontConsultantPage = storeFrontHomePage.loginAsConsultant(consultantEmailID, password);
			boolean isSiteNotFoundPresent = driver.getCurrentUrl().contains("error");
			if(isSiteNotFoundPresent){
				logger.info("SITE NOT FOUND for the user "+consultantEmailID);
				driver.get(driver.getURL()+"/"+driver.getCountry());
			}
			else
				break;
		}

		s_assert.assertTrue(storeFrontConsultantPage.verifyConsultantPage(),"Consultant Page doesn't contain Welcome User Message");
		logger.info("login is successful");

		storeFrontConsultantPage.clickOnWelcomeDropDown();
		storeFrontBillingInfoPage = storeFrontConsultantPage.clickBillingInfoLinkPresentOnWelcomeDropDown();
		s_assert.assertTrue(storeFrontBillingInfoPage.verifyBillingInfoPageIsDisplayed(),"Billing Info page has not been displayed");

		storeFrontBillingInfoPage.clickOnEditBillingProfile();
		storeFrontBillingInfoPage.enterNewBillingNameOnCard(newBillingProfileName+" "+lastName);
		storeFrontBillingInfoPage.enterNewBillingCardNumber(TestConstants.CARD_NUMBER);
		storeFrontBillingInfoPage.selectNewBillingCardExpirationDate();
		storeFrontBillingInfoPage.enterNewBillingSecurityCode(TestConstants.SECURITY_CODE);
		storeFrontBillingInfoPage.selectNewBillingCardAddress();
		storeFrontBillingInfoPage.selectUseThisBillingProfileFutureAutoshipChkbox();
		storeFrontBillingInfoPage.clickOnSaveBillingProfile();

		//--------------- Verify that Newly edited Billing profile is listed in the Billing profiles section-----------------------------------------------------------------------------------------------------

		s_assert.assertTrue(storeFrontBillingInfoPage.isTheBillingAddressPresentOnPage(newBillingProfileName),"Newly edited Billing profile is NOT listed on the page");

		//------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

		storeFrontConsultantPage.clickOnWelcomeDropDown();
		storeFrontOrdersPage = storeFrontConsultantPage.clickOrdersLinkPresentOnWelcomeDropDown();
		storeFrontOrdersPage.clickAutoshipOrderNumber();

		//------------------ Verify that autoship template contains the newly edited billing profile------------------------------------------------------------  

		s_assert.assertTrue(storeFrontOrdersPage.isPaymentMethodContainsName(newBillingProfileName),"Autoship Template Payment Method doesn't contains the newly edited billing profile when future autoship checkbox is selected");

		//-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------


		storeFrontConsultantPage.clickOnWelcomeDropDown();
		storeFrontOrdersPage = storeFrontConsultantPage.clickOrdersLinkPresentOnWelcomeDropDown();
		storeFrontOrdersPage.clickOnFirstAdHocOrder();

		//------------------ Verify that adhoc orders template doesn't contains the newly edited billing profile ------------------------------------------------------------

		s_assert.assertFalse(storeFrontOrdersPage.isPaymentMethodContainsName(newBillingProfileName),"AdHoc Orders Template Payment Method contains the newly edited billing profile when future autoship checkbox is selected");

		//-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

		storeFrontConsultantPage.clickOnWelcomeDropDown();
		storeFrontCartAutoShipPage = storeFrontConsultantPage.clickEditCrpLinkPresentOnWelcomeDropDown();
		storeFrontUpdateCartPage = storeFrontCartAutoShipPage.clickUpdateMoreInfoLink();
		storeFrontUpdateCartPage.clickOnEditPaymentBillingProfile();
		s_assert.assertTrue(storeFrontUpdateCartPage.isNewBillingProfileIsSelectedByDefault(newBillingProfileName),"New Billing Profile is not selected by default on CRP cart page");
		storeFrontConsultantPage = storeFrontUpdateCartPage.clickRodanAndFieldsLogo();
		s_assert.assertAll();
	}

	//Hybris Project-1886:Discover Card on the storefront: CA-Shouldn't allow, US-Should allow
	@Test//ok
	public void testDiscoverCardOnTheStoreFront_1886() throws InterruptedException{
		int randomNum = CommonUtils.getRandomNum(10000, 1000000);
		RFO_DB = driver.getDBNameRFO();
		List<Map<String, Object>> randomRCList =  null;
		String rcUserEmailID =null;
		String accountId = null;
		String newBillingProfileName = TestConstants.NEW_BILLING_PROFILE_NAME_US+randomNum;
		String lastName = "lN";
		storeFrontHomePage = new StoreFrontHomePage(driver);
		storeFrontUpdateCartPage = new StoreFrontUpdateCartPage(driver);
		while(true){
			randomRCList = 
					DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_RC_HAVING_ORDERS_RFO,"40"),RFO_DB);
			rcUserEmailID = (String) getValueFromQueryResult(randomRCList, "UserName");  
			accountId = String.valueOf(getValueFromQueryResult(randomRCList, "AccountID"));
			logger.info("Account Id of the user is "+accountId);
			storeFrontRCUserPage = storeFrontHomePage.loginAsRCUser(rcUserEmailID, password);
			boolean isLoginError = driver.getCurrentUrl().contains("error");
			if(isLoginError){
				logger.info("Login error for the user "+rcUserEmailID);
				driver.get(driver.getURL()+"/"+driver.getCountry());
			}
			else
				break;
		}
		logger.info("login is successful");
		storeFrontRCUserPage.clickOnShopLink();
		storeFrontRCUserPage.clickOnAllProductsLink();  
		storeFrontUpdateCartPage.clickOnBuyNowButton();
		storeFrontUpdateCartPage.clickOnCheckoutButton();
		storeFrontUpdateCartPage.clickOnContinueWithoutSponsorLink();
		storeFrontUpdateCartPage.clickOnNextButtonAfterSelectingSponsor();
		storeFrontUpdateCartPage.clickOnShippingAddressNextStepBtn();
		storeFrontUpdateCartPage.clickAddNewBillingProfileLink();
		storeFrontUpdateCartPage.enterNewBillingNameOnCard(newBillingProfileName+" "+lastName);
		storeFrontUpdateCartPage.enterNewBillingCardNumber(TestConstants.DISCOVER_CARD_NUMBER);
		storeFrontUpdateCartPage.selectNewBillingCardExpirationDate();
		storeFrontUpdateCartPage.enterNewBillingSecurityCode(TestConstants.SECURITY_CODE);
		storeFrontUpdateCartPage.clickOnSaveBillingProfile();
		s_assert.assertTrue(storeFrontHomePage.validateInvalidCreditCardMessage(), "Please enter a valid credit card message is not displayed");
		s_assert.assertAll();
	}

	//Hybris Project-1890:Create Order With Shipping Method UPS 2Day-CAD$20.00
	@Test//ok
	public void testCreateOrderWithShippingMethodUPS2Day_1890() throws InterruptedException {
		RFO_DB = driver.getDBNameRFO();
		List<Map<String, Object>> randomRCList =  null;
		String rcUserEmailID =null;
		String accountId = null;
		storeFrontHomePage = new StoreFrontHomePage(driver);
		storeFrontOrdersPage=new StoreFrontOrdersPage(driver);
		storeFrontRCUserPage=new StoreFrontRCUserPage(driver);
		storeFrontUpdateCartPage = new StoreFrontUpdateCartPage(driver);
		while(true){
			randomRCList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_RC_HAVING_ORDERS_RFO,"40"),RFO_DB);
			rcUserEmailID = (String) getValueFromQueryResult(randomRCList, "UserName");  
			accountId = String.valueOf(getValueFromQueryResult(randomRCList, "AccountID"));
			logger.info("Account Id of the user is "+accountId);

			storeFrontRCUserPage = storeFrontHomePage.loginAsRCUser(rcUserEmailID, password);
			boolean isError = driver.getCurrentUrl().contains("error");
			if(isError){
				logger.info("login error for the user "+rcUserEmailID);
				driver.get(driver.getURL()+"/"+driver.getCountry());
			}
			else
				break;
		} 
		logger.info("login is successful");
		storeFrontRCUserPage.clickOnShopLink();
		storeFrontRCUserPage.clickOnAllProductsLink();  
		storeFrontUpdateCartPage.clickOnBuyNowButton();
		storeFrontUpdateCartPage.clickOnCheckoutButton();
		storeFrontHomePage.clickEditShippingInShipmentOnCheckoutPage();
		//select UPS 2 Day shipping method
		String selectedShippingMethod=storeFrontHomePage.selectShippingMethodUPS2DayUnderShippingSectionAndGetName();
		System.out.println(selectedShippingMethod);
		//click next on shipping section
		storeFrontHomePage.clickOnShippingAddressNextStepBtn();
		storeFrontHomePage.clickOnBillingNextStepBtn();
		//click place order
		storeFrontHomePage.clickPlaceOrderBtn();
		//Navigate to Orders Page
		storeFrontHomePage.clickOnWelcomeDropDown();
		storeFrontOrdersPage = storeFrontRCUserPage.clickOrdersLinkPresentOnWelcomeDropDown();
		s_assert.assertTrue(storeFrontOrdersPage.verifyOrdersPageIsDisplayed(),"Orders page has not been displayed");
		//click on first adhoc order placed
		storeFrontOrdersPage.clickOnFirstAdHocOrder();
		//validate the shipping Method as selected on Orders detail page
		s_assert.assertTrue(storeFrontOrdersPage.verifyShippingMethodOnTemplateAfterAdhocOrderPlaced(selectedShippingMethod), "Selected Shipping Method didn't match with the method on Orders page!!");
		s_assert.assertAll(); 
	}

	//Hybris Project-1897:To verify the Meet the consultant banner on solution tool page
	@Test //ok
	public void testMeetConsultantBannerOnSolutionToolPage_1897(){
		RFO_DB = driver.getDBNameRFO();  
		String country = driver.getCountry();
		String env = driver.getEnvironment();  
		storeFrontHomePage = new StoreFrontHomePage(driver);
		String PWS = storeFrontHomePage.getBizPWS(country, env);
		PWS = storeFrontHomePage.convertBizSiteToComSite(PWS);
		storeFrontHomePage.openPWS(PWS);
		List<Map<String, Object>> randomConsultantList =  null;
		String consultantEmailID = null;
		String accountId = null;
		while(true){
			randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_CONSULTANT_WITH_ORDERS_AND_AUTOSHIPS_RFO,"40"),RFO_DB);
			consultantEmailID = (String) getValueFromQueryResult(randomConsultantList, "UserName");  
			accountId = String.valueOf(getValueFromQueryResult(randomConsultantList, "AccountID"));
			logger.info("Account Id of the user is "+accountId);
			storeFrontConsultantPage = storeFrontHomePage.loginAsConsultant(consultantEmailID, password);
			boolean isLoginError = driver.getCurrentUrl().contains("error");
			if(isLoginError){
				logger.info("Login error for the user "+consultantEmailID);
				driver.get(driver.getURL()+"/"+driver.getCountry());
			}
			else
				break;
		}
		logger.info("login is successful");
		//click learn more link
		storeFrontHomePage.clickLearnMoreLinkUnderSolutionToolAndSwitchControl();
		//validate consultant info on top right corner..
		s_assert.assertTrue(storeFrontHomePage.validateConsultantNameOnTopRightCorner(),"Consultant Info is not present on right top Corner");
		s_assert.assertAll();
	}

	//Hybris Project-2120:Increase the Quantity
	@Test//ok
	public void testIncreseTheQuantity_2120() throws InterruptedException{
		String qtyIncrease = "2";
		storeFrontHomePage = new StoreFrontHomePage(driver);
		storeFrontHomePage.clickOnShopLink();
		storeFrontHomePage.clickOnAllProductsLink();
		storeFrontHomePage.applyPriceFilterLowToHigh();
		storeFrontHomePage.selectProductAndProceedToBuy();
		double subTotalOfAddedProduct = storeFrontHomePage.getSubTotalOnShoppingCartPage();
		storeFrontHomePage.addQuantityOfProduct(qtyIncrease);
		s_assert.assertTrue(storeFrontHomePage.getAutoshipTemplateUpdatedMsg().contains("Product quantity has been updated."),"update message not coming as expected");
		double subTotalOfAfterUpdate = storeFrontHomePage.getSubTotalOnShoppingCartPage();
		s_assert.assertTrue(storeFrontHomePage.verifySubTotalAccordingToQuantity(qtyIncrease,subTotalOfAddedProduct,subTotalOfAfterUpdate),"subTotal is not updated with increased quantity");
		s_assert.assertAll();
	}

	// Hybris Project-2130:To verify Change date functionality for PC shouldnt be present on the storefront
	@Test//ok
	public void testVerifyChangeDateFunctionalityForPCUser_2130() throws InterruptedException   {
		RFO_DB = driver.getDBNameRFO();
		List<Map<String, Object>> randomPCUserList =  null;
		String pcUserEmailID = null;
		String accountID = null;
		storeFrontHomePage = new StoreFrontHomePage(driver);
		while(true){
			randomPCUserList = DBUtil.performDatabaseQuery((DBQueries_RFO.GET_RANDOM_ACTIVE_PC_WITH_ORDERS_AND_AUTOSHIPS_RFO),RFO_DB);
			pcUserEmailID = (String) getValueFromQueryResult(randomPCUserList, "UserName");
			accountID = String.valueOf(getValueFromQueryResult(randomPCUserList, "AccountID"));
			logger.info("Account Id of the user is "+accountID);  
			storeFrontPCUserPage = storeFrontHomePage.loginAsPCUser(pcUserEmailID, password);
			boolean isSiteNotFoundPresent = driver.getCurrentUrl().contains("error")||driver.getCurrentUrl().contains("sitenotfound");
			if(isSiteNotFoundPresent){
				logger.info("Login error for the user "+pcUserEmailID);
				driver.get(driver.getURL()+"/"+driver.getCountry());
			}
			else{
				storeFrontPCUserPage.clickOnWelcomeDropDown();
				if(storeFrontPCUserPage.isEditCrpLinkPresentOnWelcomeDropdown()){
					break;
				}else{
					storeFrontPCUserPage.clickOnWelcomeDropDown();
					driver.pauseExecutionFor(2000);
					logout();
					driver.get(driver.getURL()+"/"+driver.getCountry());
					continue;
				}
			}
		}
		storeFrontCartAutoShipPage=storeFrontPCUserPage.clickEditCrpLinkPresentOnWelcomeDropDown();
		storeFrontUpdateCartPage=storeFrontCartAutoShipPage.clickUpdateMoreInfoLink();
		storeFrontUpdateCartPage.clickOnEditShipping();
		s_assert.assertFalse(storeFrontUpdateCartPage.checkDateFunctionality(), "check date functionality is present");
		s_assert.assertAll();
	}

	// Hybris Project-2134:EC-789- To Verify subscribe to pulse
	@Test//ok
	public void testVerifySubscribeToPulse_2134() throws InterruptedException	{
		RFO_DB = driver.getDBNameRFO();  
		List<Map<String, Object>> randomConsultantList =  null;
		String consultantWithPWSEmailID = null;
		storeFrontHomePage = new StoreFrontHomePage(driver);
		// Get Consultant with PWS from database
		randomConsultantList =DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguementPWS(DBQueries_RFO.GET_RANDOM_CONSULTANT_WITH_PWS_RFO,driver.getEnvironment(),driver.getCountry(),"40"),RFO_DB);
		consultantWithPWSEmailID = (String) getValueFromQueryResult(randomConsultantList, "UserName");
		storeFrontConsultantPage = storeFrontHomePage.loginAsConsultant(consultantWithPWSEmailID, password);
		logger.info("login is successful");
		storeFrontConsultantPage.clickOnWelcomeDropDown();
		storeFrontAccountInfoPage=storeFrontConsultantPage.clickAccountInfoLinkPresentOnWelcomeDropDown();
		storeFrontAccountInfoPage.clickOnAutoShipStatus();
		//Verify subscribe to pulse(pulse already subscribed)
		s_assert.assertTrue(storeFrontAccountInfoPage.validateSubscribeToPulse(),"pulse is not subscribed for the user");
		s_assert.assertAll();
	}

	//Hybris Project-2148:Check Shipping and Handling Fee for UPS 1Day for Order total 0-999999
	@Test//ok
	public void testCheckShippingAndHandlingFeeForUPS1DayForOrderTotal_0_999999_2148() throws InterruptedException{
		RFO_DB = driver.getDBNameRFO();
		List<Map<String, Object>> randomConsultantList =  null;
		String consultantEmailID = null;
		String accountId = null;
		storeFrontHomePage = new StoreFrontHomePage(driver);
		storeFrontUpdateCartPage = new StoreFrontUpdateCartPage(driver);
		while(true){
			randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_CONSULTANT_WITH_ORDERS_AND_AUTOSHIPS_RFO,"40"),RFO_DB);
			consultantEmailID = (String) getValueFromQueryResult(randomConsultantList, "UserName");		
			accountId = String.valueOf(getValueFromQueryResult(randomConsultantList, "AccountID"));
			logger.info("Account Id of the user is "+accountId);
			storeFrontConsultantPage = storeFrontHomePage.loginAsConsultant(consultantEmailID, password);
			boolean isLoginError = driver.getCurrentUrl().contains("error");
			if(isLoginError){
				logger.info("Login error for the user "+consultantEmailID);
				driver.get(driver.getURL()+"/"+driver.getCountry());
			}
			else
				break;
		}
		s_assert.assertTrue(storeFrontConsultantPage.verifyConsultantPage(),"Consultant User Page doesn't contain Welcome User Message");
		logger.info("login is successful");
		storeFrontHomePage.clickOnShopLink();
		storeFrontHomePage.clickOnAllProductsLink();
		storeFrontConsultantPage.selectProductAndProceedToBuy();

		while(true){
			String total = storeFrontUpdateCartPage.getTotalPriceOfProduct();
			String orderTotal = total.split("\\$")[1].trim();
			System.out.println("Order total for consultant"+orderTotal);
			double totalFromUI = Double.parseDouble(orderTotal);
			if(totalFromUI<0){
				continue;
			}else{
				break;
			}
		}
		storeFrontUpdateCartPage.clickOnCheckoutButton();
		storeFrontUpdateCartPage.selectShippingMethodUPS2DayInOrderSummary();

		String deliveryCharges = String.valueOf(storeFrontUpdateCartPage.getDeliveryCharges());
		logger.info("deliveryCharges ="+deliveryCharges);
		String handlingCharges = String.valueOf(storeFrontUpdateCartPage.getHandlingCharges());
		logger.info("handlingCharges ="+handlingCharges);
		if(driver.getCountry().equalsIgnoreCase("CA")){
			//Assert of shipping cost from UI
			s_assert.assertTrue(deliveryCharges.equalsIgnoreCase("CAD$ 20.00"),"Shipping charges on UI is not As per shipping method selected");
			s_assert.assertTrue(handlingCharges.equalsIgnoreCase("CAD$ 2.50"),"Handling charges on UI is not As per shipping method selected");
		}else if(driver.getCountry().equalsIgnoreCase("US")){
			s_assert.assertTrue(deliveryCharges.equalsIgnoreCase("$23.00"),"Shipping charges on UI is not As per shipping method selected");
			s_assert.assertTrue(handlingCharges.equalsIgnoreCase("$2.50"),"Handling charges on UI is not As per shipping method selected");
		}
		storeFrontUpdateCartPage.clickOnRodanAndFieldsLogo();
		logout();

		driver.get(driver.getURL()+"/"+driver.getCountry());
		List<Map<String, Object>> randomPCUserList =  null;
		String pcUserEmailID = null;
		storeFrontHomePage = new StoreFrontHomePage(driver);
		while(true){
			randomPCUserList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_PC_WITH_ORDERS_AND_AUTOSHIPS_RFO,"40"),RFO_DB);
			pcUserEmailID = (String) getValueFromQueryResult(randomPCUserList, "UserName");		
			logger.info("Account Id of the user is "+accountId);
			storeFrontPCUserPage = storeFrontHomePage.loginAsPCUser(pcUserEmailID, password);
			boolean isError = driver.getCurrentUrl().contains("error");
			if(isError){
				logger.info("SITE NOT FOUND for the user "+pcUserEmailID);
				driver.get(driver.getURL()+"/"+driver.getCountry());
			}
			else
				break;
		}	
		logger.info("login is successful");
		storeFrontHomePage.clickOnShopLink();
		storeFrontHomePage.clickOnAllProductsLink();
		storeFrontConsultantPage.selectProductAndProceedToBuy();

		while(true){
			String total = storeFrontUpdateCartPage.getTotalPriceOfProductForPC();
			String orderTotal = total.split("\\$")[1].trim();
			System.out.println("Order total for consultant"+orderTotal);
			double totalFromUI = Double.parseDouble(orderTotal);
			if(totalFromUI<0){
				continue;
			}else{
				break;
			}
		}
		storeFrontUpdateCartPage.clickOnCheckoutButton();
		storeFrontUpdateCartPage.selectShippingMethodUPS2DayInOrderSummary();

		deliveryCharges = String.valueOf(storeFrontUpdateCartPage.getDeliveryCharges());
		logger.info("deliveryCharges ="+deliveryCharges);
		handlingCharges = String.valueOf(storeFrontUpdateCartPage.getHandlingCharges());
		logger.info("handlingCharges ="+handlingCharges);
		if(driver.getCountry().equalsIgnoreCase("CA")){
			//Assert of shipping cost from UI
			s_assert.assertTrue(deliveryCharges.equalsIgnoreCase("CAD$ 20.00"),"Shipping charges on UI is not As per shipping method selected");
			s_assert.assertTrue(handlingCharges.equalsIgnoreCase("CAD$ 2.50"),"Handling charges on UI is not As per shipping method selected");
		}else if(driver.getCountry().equalsIgnoreCase("US")){
			s_assert.assertTrue(deliveryCharges.equalsIgnoreCase("$23.00"),"Shipping charges on UI is not As per shipping method selected");
			s_assert.assertTrue(handlingCharges.equalsIgnoreCase("$2.50"),"Handling charges on UI is not As per shipping method selected");
		}
		storeFrontUpdateCartPage.clickOnRodanAndFieldsLogo();
		s_assert.assertAll();
	}

	//Hybris Project-2157:Place An order as logged in PC User and check for PC Perk Promo
	@Test//ok
	public void testPlacedAnAdhocOrderAsPCAndChekcForPCPerkPromo_2198() throws InterruptedException{
		RFO_DB = driver.getDBNameRFO();
		List<Map<String, Object>> randomPCUserList =  null;
		String pcUserEmailID = null;
		String accountId = null;
		int randomNum = CommonUtils.getRandomNum(10000, 1000000);
		RFO_DB = driver.getDBNameRFO();
		String newBillingProfileName = TestConstants.NEW_BILLING_PROFILE_NAME_US+randomNum;
		String lastName = "lN";

		storeFrontHomePage = new StoreFrontHomePage(driver);
		while(true){
			randomPCUserList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_PC_WITH_ORDERS_AND_AUTOSHIPS_RFO,"40"),RFO_DB);
			pcUserEmailID = (String) getValueFromQueryResult(randomPCUserList, "UserName");		
			accountId = String.valueOf(getValueFromQueryResult(randomPCUserList, "AccountID"));
			logger.info("Account Id of the user is "+accountId);
			storeFrontPCUserPage = storeFrontHomePage.loginAsPCUser(pcUserEmailID, password);
			boolean isError = driver.getCurrentUrl().contains("error");
			if(isError){
				logger.info("login error for the user "+pcUserEmailID);
				driver.get(driver.getURL()+"/"+driver.getCountry());
			}
			else
				break;
		}	
		logger.info("login is successful");
		storeFrontHomePage.clickOnShopLink();
		storeFrontHomePage.clickOnAllProductsLink();
		storeFrontUpdateCartPage = new StoreFrontUpdateCartPage(driver);
		storeFrontUpdateCartPage.clickOnBuyNowButton();
		storeFrontUpdateCartPage.clickOnCheckoutButton();
		s_assert.assertFalse(storeFrontUpdateCartPage.verifyPCPerksPromoDuringPlaceAdhocOrder(), "Pc perk promo is available on Main Account Info Page");
		storeFrontUpdateCartPage.clickOnShippingAddressNextStepBtn();
		s_assert.assertFalse(storeFrontUpdateCartPage.verifyPCPerksPromoDuringPlaceAdhocOrder(), "Pc perk promo is available on Billing Info Page");
		storeFrontUpdateCartPage.clickOnDefaultBillingProfileEdit();
		storeFrontUpdateCartPage.enterNewBillingNameOnCard(newBillingProfileName+" "+lastName);
		storeFrontUpdateCartPage.enterNewBillingCardNumber(TestConstants.CARD_NUMBER);
		storeFrontUpdateCartPage.selectNewBillingCardExpirationDate();
		storeFrontUpdateCartPage.enterNewBillingSecurityCode(TestConstants.SECURITY_CODE);
		storeFrontUpdateCartPage.selectNewBillingCardAddress();
		storeFrontUpdateCartPage.clickOnSaveBillingProfile();
		storeFrontUpdateCartPage.clickOnBillingNextStepBtn();
		s_assert.assertFalse(storeFrontUpdateCartPage.verifyPCPerksPromoDuringPlaceAdhocOrder(), "Pc perk promo is available on order confirmation Page");
		storeFrontUpdateCartPage.clickPlaceOrderBtn();
		//String orderNumber = storeFrontUpdateCartPage.getOrderNumberAfterPlaceOrder();
		s_assert.assertTrue(storeFrontUpdateCartPage.verifyOrderPlacedConfirmationMessage(), "Order has been not placed successfully");
		s_assert.assertAll();
	}

	// Hybris Project-2233:Verify that user can cancel Pulse subscription through my account.
	@Test//ok
	public void testVerifyUserCanCancelPulseSubscriptionThroughMyAccount_2233() throws InterruptedException {
		RFO_DB = driver.getDBNameRFO();  
		List<Map<String, Object>> randomConsultantList =  null;
		String consultantWithPWSEmailID = null;
		storeFrontHomePage = new StoreFrontHomePage(driver);
		while(true){
			randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguementPWS(DBQueries_RFO.GET_RANDOM_CONSULTANT_WITH_PWS_RFO,driver.getEnvironment(),driver.getCountry(),"40"),RFO_DB);
			consultantWithPWSEmailID = (String) getValueFromQueryResult(randomConsultantList, "UserName");  

			storeFrontConsultantPage = storeFrontHomePage.loginAsConsultant(consultantWithPWSEmailID, password);
			boolean isLoginError = driver.getCurrentUrl().contains("error");
			if(isLoginError){
				logger.info("Login error for the user "+consultantWithPWSEmailID);
				driver.get(driver.getURL()+"/"+driver.getCountry());
			}
			else
				break;
		}
		logger.info("login is successful");
		storeFrontConsultantPage.clickOnWelcomeDropDown();
		storeFrontAccountInfoPage=storeFrontConsultantPage.clickAccountInfoLinkPresentOnWelcomeDropDown();
		storeFrontAccountInfoPage.clickOnAutoShipStatus();
		storeFrontAccountInfoPage.clickCancelMyPulseSubscription();
		storeFrontAccountInfoPage.clickCancelMyPulseSubscriptionNow();
		s_assert.assertTrue(storeFrontAccountInfoPage.isSubscribeToPulseButtonPresent(),"pulse subscription is not cancelled for the user");
		s_assert.assertAll();
	}

}




//	//Hybris Project-3218 :: Version : 1 :: Consultant login after being logout.
//	@Test
//	public void testExpressEnrollmentLoginAfterLogout_3218() throws InterruptedException{
//		int randomNum = CommonUtils.getRandomNum(10000, 1000000);
//		String socialInsuranceNumber = String.valueOf(CommonUtils.getRandomNum(100000000, 999999999));
//		country = driver.getCountry();
//		enrollmentType = TestConstants.STANDARD_ENROLLMENT;
//		regimenName = TestConstants.REGIMEN_NAME_REDEFINE;
//		String firstName = TestConstants.FIRST_NAME+randomNum;
//		String emailId = firstName+TestConstants.EMAIL_ADDRESS_SUFFIX;
//
//		if(country.equalsIgnoreCase("CA")){
//			kitName = TestConstants.KIT_PRICE_BIG_BUSINESS_CA;			 
//			addressLine1 = TestConstants.ADDRESS_LINE_1_CA;
//			city = TestConstants.CITY_CA;
//			postalCode = TestConstants.POSTAL_CODE_CA;
//			phoneNumber = TestConstants.PHONE_NUMBER_CA;
//		}else{
//			kitName = TestConstants.KIT_PRICE_BIG_BUSINESS_US;
//			addressLine1 = TestConstants.ADDRESS_LINE_1_US;
//			city = TestConstants.CITY_US;
//			postalCode = TestConstants.POSTAL_CODE_US;
//			phoneNumber = TestConstants.PHONE_NUMBER_US;
//		}
//
//		storeFrontHomePage = new StoreFrontHomePage(driver);
//		storeFrontHomePage.clickOnOurBusinessLink();
//		storeFrontHomePage.clickOnOurEnrollNowLink();
//		storeFrontHomePage.searchCID();
//		storeFrontHomePage.mouseHoverSponsorDataAndClickContinue();
//		storeFrontHomePage.enterUserInformationForEnrollment(kitName, regimenName, enrollmentType,firstName, TestConstants.LAST_NAME+randomNum, password, addressLine1, city, postalCode, phoneNumber);
//		storeFrontHomePage.clickEnrollmentNextBtn();
//		storeFrontHomePage.acceptTheVerifyYourShippingAddressPop();		
//		storeFrontHomePage.enterCardNumber(TestConstants.CARD_NUMBER);
//		storeFrontHomePage.enterNameOnCard(TestConstants.FIRST_NAME+randomNum);
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
//		s_assert.assertTrue(storeFrontHomePage.verifyWelcomeDropdownToCheckUserRegistered(), "User NOT registered successfully");
//
//		logout();
//		storeFrontConsultantPage = storeFrontHomePage.loginAsConsultant(emailId, password);
//		s_assert.assertTrue(storeFrontConsultantPage.verifyConsultantPage(),"Consultant Page doesn't contain Welcome User Message");
//		logger.info("login is successful");
//		logout();
//		s_assert.assertAll();
//	}	
//
//	//Hybris Project-3920 :: Version : 1 :: Verify creating crp autoship from My Account under .biz site 
//	@Test
//	public void testCreatingCRPAutoshipUnderBizSite_3920() throws InterruptedException{
//		int randomNum = CommonUtils.getRandomNum(10000, 1000000);
//		String socialInsuranceNumber = String.valueOf(CommonUtils.getRandomNum(100000000, 999999999));
//		country = driver.getCountry();
//		enrollmentType = TestConstants.STANDARD_ENROLLMENT;
//		regimenName = TestConstants.REGIMEN_NAME_REDEFINE;
//		env = driver.getEnvironment();		
//		storeFrontHomePage = new StoreFrontHomePage(driver);
//		storeFrontHomePage.openPWSSite(country, env);
//
//		if(country.equalsIgnoreCase("CA")){
//			kitName = TestConstants.KIT_PRICE_EXPRESS_CA;			 
//			addressLine1 = TestConstants.ADDRESS_LINE_1_CA;
//			city = TestConstants.CITY_CA;
//			postalCode = TestConstants.POSTAL_CODE_CA;
//			phoneNumber = TestConstants.PHONE_NUMBER_CA;
//		}else{
//			kitName = TestConstants.KIT_PRICE_EXPRESS_US;
//			addressLine1 = TestConstants.ADDRESS_LINE_1_US;
//			city = TestConstants.CITY_US;
//			postalCode = TestConstants.POSTAL_CODE_US;
//			phoneNumber = TestConstants.PHONE_NUMBER_US;
//		}
//
//		storeFrontHomePage = new StoreFrontHomePage(driver);
//		storeFrontHomePage.clickOnOurBusinessLink();
//		storeFrontHomePage.clickOnOurEnrollNowLink();
//		storeFrontHomePage.enterUserInformationForEnrollment(kitName, regimenName, enrollmentType, TestConstants.FIRST_NAME+randomNum, TestConstants.LAST_NAME+randomNum, password, addressLine1, city, postalCode, phoneNumber);
//		storeFrontHomePage.clickEnrollmentNextBtn();
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
//		storeFrontConsultantPage = new StoreFrontConsultantPage(driver);
//		storeFrontConsultantPage.clickOnWelcomeDropDown();
//		storeFrontAccountInfoPage = storeFrontConsultantPage.clickAccountInfoLinkPresentOnWelcomeDropDown();
//		s_assert.assertTrue(storeFrontAccountInfoPage.verifyAccountInfoPageIsDisplayed(),"shipping info page has not been displayed");
//		storeFrontAccountInfoPage.clickOnAutoShipStatus();
//		storeFrontAccountInfoPage.clickOnEnrollInCRP();
//
//		storeFrontHomePage.clickOnAddToCRPButtonCreatingCRPUnderBizSite();
//		storeFrontHomePage.clickOnCRPCheckout();
//		storeFrontHomePage.clickOnUpdateCartShippingNextStepBtnDuringEnrollment();
//		storeFrontHomePage.clickOnBillingNextStepBtn();
//		storeFrontHomePage.clickOnSetupCRPAccountBtn();
//		s_assert.assertTrue(storeFrontHomePage.verifyOrderConfirmation(), "Order Confirmation Message has not been displayed");
//		storeFrontHomePage.clickOnGoToMyAccountToCheckStatusOfCRP();
//		storeFrontHomePage.clickOnAccountInfoLinkLeftSidePannel();
//		storeFrontAccountInfoPage.clickOnAutoShipStatus();
//		s_assert.assertTrue(storeFrontAccountInfoPage.verifyCurrentCRPStatus(), "Current CRP Status has not been Enrolled");
//		s_assert.assertAll();
//	}
//
//	// Hybris Project-54 :: Version : 1 :: Express EnrollmentTest USD 995 RF Express Business Kit, Personal Regimen REVERSE REGIMEN
//	@Test
//	public void testExpressEnrollmentRFExpressBusinessKitReverseRegimen_54() throws InterruptedException{
//		int randomNum = CommonUtils.getRandomNum(10000, 1000000);
//		String socialInsuranceNumber = String.valueOf(CommonUtils.getRandomNum(100000000, 999999999));
//		country = driver.getCountry();
//		enrollmentType = TestConstants.EXPRESS_ENROLLMENT;
//		regimenName = TestConstants.REGIMEN_NAME_REVERSE;
//
//		if(country.equalsIgnoreCase("CA")){
//			kitName = TestConstants.KIT_PRICE_EXPRESS_CA;			 
//			addressLine1 = TestConstants.ADDRESS_LINE_1_CA;
//			city = TestConstants.CITY_CA;
//			postalCode = TestConstants.POSTAL_CODE_CA;
//			phoneNumber = TestConstants.PHONE_NUMBER_CA;
//		}else{
//			kitName = TestConstants.KIT_PRICE_EXPRESS_US;
//			addressLine1 = TestConstants.ADDRESS_LINE_1_US;
//			city = TestConstants.CITY_US;
//			postalCode = TestConstants.POSTAL_CODE_US;
//			phoneNumber = TestConstants.PHONE_NUMBER_US;
//		}
//
//		storeFrontHomePage = new StoreFrontHomePage(driver);
//		storeFrontHomePage.clickOnOurBusinessLink();
//		storeFrontHomePage.clickOnOurEnrollNowLink();
//		storeFrontHomePage.searchCID();
//		storeFrontHomePage.mouseHoverSponsorDataAndClickContinue();
//		storeFrontHomePage.enterUserInformationForEnrollment(kitName, regimenName, enrollmentType, TestConstants.FIRST_NAME+randomNum, TestConstants.LAST_NAME+randomNum, password, addressLine1, city, postalCode, phoneNumber);
//		storeFrontHomePage.clickEnrollmentNextBtn();
//		storeFrontHomePage.acceptTheVerifyYourShippingAddressPop();		
//		storeFrontHomePage.enterCardNumber(TestConstants.CARD_NUMBER);
//		storeFrontHomePage.enterNameOnCard(TestConstants.FIRST_NAME+randomNum);
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
//		s_assert.assertTrue(storeFrontHomePage.verifyWelcomeDropdownToCheckUserRegistered(), "User NOT registered successfully");
//
//		s_assert.assertAll();
//	}
//
//	//Hybris Project-3619 CCS CA Consultant Express EnrollmentTest
//	@Test
//	public void ccsCAConsultantExpressEnrollmentWithUSSponsor() throws InterruptedException	{
//		int randomNum = CommonUtils.getRandomNum(10000, 1000000);
//		String socialInsuranceNumber = String.valueOf(CommonUtils.getRandomNum(100000000, 999999999));
//		country = driver.getCountry();
//		enrollmentType = TestConstants.EXPRESS_ENROLLMENT;
//		regimenName = TestConstants.REGIMEN_NAME_REVERSE;
//		String firstName = TestConstants.FIRST_NAME+randomNum;
//		String emailId = firstName+TestConstants.EMAIL_ADDRESS_SUFFIX;
//
//		if(country.equalsIgnoreCase("CA")){
//			kitName = TestConstants.KIT_PRICE_EXPRESS_CA;			 
//			addressLine1 = TestConstants.ADDRESS_LINE_1_CA;
//			city = TestConstants.CITY_CA;
//			postalCode = TestConstants.POSTAL_CODE_CA;
//			phoneNumber = TestConstants.PHONE_NUMBER_CA;
//		}else{
//			kitName = TestConstants.KIT_PRICE_EXPRESS_US;
//			addressLine1 = TestConstants.ADDRESS_LINE_1_US;
//			city = TestConstants.CITY_US;
//			postalCode = TestConstants.POSTAL_CODE_US;
//			phoneNumber = TestConstants.PHONE_NUMBER_US;
//		}
//
//		storeFrontHomePage = new StoreFrontHomePage(driver);
//		storeFrontHomePage.clickOnOurBusinessLink();
//		storeFrontHomePage.clickOnOurEnrollNowLink();
//		storeFrontHomePage.searchCID(TestConstants.SPONSOR_ID_US);
//		storeFrontHomePage.mouseHoverSponsorDataAndClickContinue();
//		storeFrontHomePage.enterUserInformationForEnrollment(kitName, regimenName, enrollmentType, firstName, TestConstants.LAST_NAME+randomNum, password, addressLine1, city, postalCode, phoneNumber);
//		storeFrontHomePage.clickEnrollmentNextBtn();
//		storeFrontHomePage.acceptTheVerifyYourShippingAddressPop();		
//		storeFrontHomePage.enterCardNumber(TestConstants.CARD_NUMBER);
//		storeFrontHomePage.enterNameOnCard(TestConstants.FIRST_NAME+randomNum);
//		storeFrontHomePage.selectNewBillingCardExpirationDate();
//		storeFrontHomePage.enterSecurityCode(TestConstants.SECURITY_CODE);
//		storeFrontHomePage.enterSocialInsuranceNumber(socialInsuranceNumber);
//		storeFrontHomePage.enterNameAsItAppearsOnCard(TestConstants.FIRST_NAME);
//		storeFrontHomePage.clickEnrollmentNextBtn();
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
//		logout();
//		storeFrontConsultantPage = storeFrontHomePage.loginAsConsultant(emailId, password);
//		s_assert.assertTrue(storeFrontConsultantPage.verifyConsultantPage(),"Consultant Page doesn't contain Welcome User Message");
//		logger.info("login is successful");
//		logout();
//		s_assert.assertAll();
//	}
//
//	//Hybris Project-3618 CCS RC EnrollmentTest under US Sponsor
//	@Test
//	public void ccsRCEnrollmentUnderUSSponsor() throws InterruptedException
//	{
//		int randomNum = CommonUtils.getRandomNum(10000, 1000000);
//		String newBillingProfileName = TestConstants.NEW_BILLING_PROFILE_NAME+randomNum;
//		String lastName = "lN";
//		storeFrontHomePage = new StoreFrontHomePage(driver);
//		// Click on our product link that is located at the top of the page and then click in on quick shop
//		storeFrontHomePage.clickOnShopLink();
//		storeFrontHomePage.clickOnAllProductsLink();
//
//		// Products are displayed?
//		s_assert.assertTrue(storeFrontHomePage.areProductsDisplayed(), "quickshop products not displayed");
//		logger.info("Quick shop products are displayed");
//
//		//Select a product and proceed to buy it
//		storeFrontHomePage.selectProductAndProceedToBuy();
//		//Cart page is displayed?
//		s_assert.assertTrue(storeFrontHomePage.isCartPageDisplayed(), "Cart page is not displayed");
//		logger.info("Cart page is displayed");
//
//		//In the Cart page add one more product
//		storeFrontHomePage.addAnotherProduct();
//
//		//Two products are in the Shopping Cart?
//		s_assert.assertTrue(storeFrontHomePage.verifyNumberOfProductsInCart("2"), "number of products in the cart is NOT 2");
//		logger.info("2 products are successfully added to the cart");
//
//		//Click on Check out
//		storeFrontHomePage.clickOnCheckoutButton();
//		//Log in or create an account page is displayed?
//		s_assert.assertTrue(storeFrontHomePage.isLoginOrCreateAccountPageDisplayed(), "Login or Create Account page is NOT displayed");
//		logger.info("Login or Create Account page is displayed");
//
//		//Enter the User information and DO NOT check the "Become a Preferred Customer" checkbox and click the create account button
//		storeFrontHomePage.enterNewRCDetails(TestConstants.FIRST_NAME+randomNum, TestConstants.LAST_NAME+randomNum, password);
//
//		//CheckoutPage is displayed?
//		s_assert.assertTrue(storeFrontHomePage.isCheckoutPageDisplayed(), "Checkout page has NOT displayed");
//		logger.info("Checkout page has displayed");
//		//Enter the Main account info and DO NOT check the "Become a Preferred Customer" and click next
//		storeFrontHomePage.enterMainAccountInfo();
//		logger.info("Main account details entered");
//		storeFrontHomePage.searchCID(TestConstants.SPONSOR_ID_US);
//
//		storeFrontHomePage.mouseHoverSponsorDataAndClickContinue();
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
//		storeFrontHomePage.clickOnBillingNextStepBtn();
//		storeFrontHomePage.clickPlaceOrderBtn();
//		s_assert.assertTrue(storeFrontHomePage.isOrderPlacedSuccessfully(), "Order Not placed successfully");
//		s_assert.assertTrue(storeFrontHomePage.verifyWelcomeDropdownToCheckUserRegistered(), "User NOT registered successfully");
//		logout();
//		s_assert.assertAll();	
//	}
//
//	//Hybris Project-3617 CCS PC EnrollmentTest under US Sponsor
//	@Test
//	public void ccsPCEnrollmentUnderUSSponsor() throws InterruptedException {
//		;
//		int randomNum = CommonUtils.getRandomNum(10000, 1000000);  
//		String newBillingProfileName = TestConstants.NEW_BILLING_PROFILE_NAME+randomNum;
//		String lastName = "lN";
//		storeFrontHomePage = new StoreFrontHomePage(driver);
//		// Click on our product link that is located at the top of the page and then click in on quick shop
//		storeFrontHomePage.clickOnShopLink();
//		storeFrontHomePage.clickOnAllProductsLink();
//
//		// Products are displayed?
//		s_assert.assertTrue(storeFrontHomePage.areProductsDisplayed(), "quickshop products not displayed");
//		logger.info("Quick shop products are displayed");
//
//		//Select a product with the price less than $80 and proceed to buy it
//		storeFrontHomePage.applyPriceFilterLowToHigh();
//		storeFrontHomePage.selectProductAndProceedToBuy();
//
//		//Cart page is displayed?
//		s_assert.assertTrue(storeFrontHomePage.isCartPageDisplayed(), "Cart page is not displayed");
//		logger.info("Cart page is displayed");
//
//		//1 product is in the Shopping Cart?
//		s_assert.assertTrue(storeFrontHomePage.verifyNumberOfProductsInCart("1"), "number of products in the cart is NOT 1");
//		logger.info("1 product is successfully added to the cart");
//		//Click on Check out
//		storeFrontHomePage.clickOnCheckoutButton();
//		//Log in or create an account page is displayed?
//		s_assert.assertTrue(storeFrontHomePage.isLoginOrCreateAccountPageDisplayed(), "Login or Create Account page is NOT displayed");
//		logger.info("Login or Create Account page is displayed");
//		//Enter the User information and DO check the "Become a Preferred Customer" checkbox and click the create account button
//		storeFrontHomePage.enterNewPCDetails(TestConstants.FIRST_NAME+randomNum, TestConstants.LAST_NAME+randomNum, password);
//		//In the Cart page add one more product
//
//		//Pop for PC threshold validation
//		s_assert.assertTrue(storeFrontHomePage.isPopUpForPCThresholdPresent(),"Threshold poup for PC validation NOT present");
//		storeFrontHomePage.addAnotherProduct();
//		//Click on Check out
//		storeFrontHomePage.clickOnCheckoutButton();
//		//Enter the Main account info and DO NOT check the "Become a Preferred Customer" and click next
//		storeFrontHomePage.enterMainAccountInfo();
//		logger.info("Main account details entered");
//		storeFrontHomePage.searchCID(TestConstants.SPONSOR_ID_US);
//
//		storeFrontHomePage.mouseHoverSponsorDataAndClickContinue();
//		storeFrontHomePage.clickOnNextButtonAfterSelectingSponsor();
//		storeFrontHomePage.clickOnShippingAddressNextStepBtn();
//
//		//Enter Billing Profile
//		storeFrontHomePage.enterNewBillingCardNumber(TestConstants.CARD_NUMBER);
//		storeFrontHomePage.enterNewBillingNameOnCard(newBillingProfileName+" "+lastName);
//		storeFrontHomePage.selectNewBillingCardExpirationDate();
//		storeFrontHomePage.enterNewBillingSecurityCode(TestConstants.SECURITY_CODE);
//		storeFrontHomePage.selectNewBillingCardAddress();
//		storeFrontHomePage.clickOnSaveBillingProfile();
//		storeFrontHomePage.clickOnBillingNextStepBtn();
//		storeFrontHomePage.clickPlaceOrderBtn();
//		s_assert.assertTrue(storeFrontHomePage.verifyPCPerksTermsAndConditionsPopup(),"PC Perks terms and conditions popup not visible when checkboxes for t&c not selected and place order button clicked");
//		logger.info("PC Perks terms and conditions popup is visible when checkboxes for t&c not selected and place order button clicked");
//		storeFrontHomePage.clickOnPCPerksTermsAndConditionsCheckBoxes();
//		storeFrontHomePage.clickPlaceOrderBtn();
//		storeFrontHomePage.clickOnRodanAndFieldsLogo();
//		s_assert.assertTrue(storeFrontHomePage.verifyWelcomeDropdownToCheckUserRegistered(), "User NOT registered successfully");
//		logout();
//		s_assert.assertAll(); 
//	}
//
//	//Hybris Project-135 :: Version : 1 :: Enroll in pulse from my account - enrolling from 1st till 17th
//	@Test
//	public void testEnrollPulsefromMyAmount_135() throws InterruptedException{
//		int randomNum = CommonUtils.getRandomNum(10000, 1000000);
//		String socialInsuranceNumber = String.valueOf(CommonUtils.getRandomNum(100000000, 999999999));
//		country = driver.getCountry();
//		enrollmentType = TestConstants.STANDARD_ENROLLMENT;
//		regimenName = TestConstants.REGIMEN_NAME_REDEFINE;
//
//		if(country.equalsIgnoreCase("CA")){
//			kitName = TestConstants.KIT_PRICE_BIG_BUSINESS_CA;			 
//			addressLine1 = TestConstants.ADDRESS_LINE_1_CA;
//			city = TestConstants.CITY_CA;
//			postalCode = TestConstants.POSTAL_CODE_CA;
//			phoneNumber = TestConstants.PHONE_NUMBER_CA;
//		}else{
//			kitName = TestConstants.KIT_PRICE_BIG_BUSINESS_US;
//			addressLine1 = TestConstants.ADDRESS_LINE_1_US;
//			city = TestConstants.CITY_US;
//			postalCode = TestConstants.POSTAL_CODE_US;
//			phoneNumber = TestConstants.PHONE_NUMBER_US;
//		}
//
//		storeFrontHomePage = new StoreFrontHomePage(driver);
//		storeFrontHomePage.clickOnOurBusinessLink();
//		storeFrontHomePage.clickOnOurEnrollNowLink();
//		storeFrontHomePage.searchCID();
//		storeFrontHomePage.mouseHoverSponsorDataAndClickContinue();
//		storeFrontHomePage.enterUserInformationForEnrollment(kitName, regimenName, enrollmentType, TestConstants.FIRST_NAME+randomNum, TestConstants.LAST_NAME+randomNum, password, addressLine1, city, postalCode, phoneNumber);
//		storeFrontHomePage.clickEnrollmentNextBtn();
//		storeFrontHomePage.acceptTheVerifyYourShippingAddressPop();		
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
//		storeFrontConsultantPage.clickOnWelcomeDropDown();
//		storeFrontAccountInfoPage = storeFrontConsultantPage.clickAccountInfoLinkPresentOnWelcomeDropDown();
//		s_assert.assertTrue(storeFrontAccountInfoPage.verifyAccountInfoPageIsDisplayed(),"shipping info page has not been displayed");
//		storeFrontAccountInfoPage.clickOnAutoShipStatus();
//		storeFrontAccountInfoPage.clickOnSubscribeToPulseBtn();
//		storeFrontUpdateCartPage = new StoreFrontUpdateCartPage(driver);
//		storeFrontUpdateCartPage.clickOnBillingNextStepBtn();
//		storeFrontUpdateCartPage.clickOnSubscribePulseTermsAndConditionsChkbox();
//		storeFrontUpdateCartPage.clickOnSubscribeBtn();
//		s_assert.assertTrue(storeFrontUpdateCartPage.verifyPulseOrderCreatedMsg(), "Pulse order created msg is NOT present,Pulse might NOT be subscribed successfully");
//		logout();
//		s_assert.assertAll();
//	}

