package com.rf.test.website.storeFront.hybris.billingAndShipping;

import java.util.List;
import java.util.Map;

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
import com.rf.pages.website.storeFront.StoreFrontShippingInfoPage;
import com.rf.pages.website.storeFront.StoreFrontUpdateCartPage;
import com.rf.test.website.RFStoreFrontWebsiteBaseTest;
import com.rf.test.website.RFWebsiteBaseTest;

public class AddShippingTest extends RFStoreFrontWebsiteBaseTest{

	private static final Logger logger = LogManager
			.getLogger(AddShippingTest.class.getName());

	private StoreFrontHomePage storeFrontHomePage;
	private StoreFrontConsultantPage storeFrontConsultantPage;
	private StoreFrontShippingInfoPage storeFrontShippingInfoPage;
	private StoreFrontCartAutoShipPage storeFrontCartAutoShipPage;
	private StoreFrontUpdateCartPage storeFrontUpdateCartPage;
	private StoreFrontOrdersPage storeFrontOrdersPage;
	private StoreFrontAccountInfoPage storeFrontAccountInfoPage;
	private StoreFrontBillingInfoPage storeFrontBillingInfoPage;
	private StoreFrontPCUserPage storeFrontPCUserPage;
	private String RFO_DB = null;
	private String city = null;
	private String phoneNumber = null;
	private String postalCode = null;
	private String kitName = null;
	private String regimenName = null;
	private String enrollmentType = null;
	private String addressLine1 = null;
	private String country = null;
	private String state = null;
	private String env = null;
	private int randomNum; 	
	List<Map<String, Object>> randomConsultantList =  null;
	String consultantEmailID = null;
	String accountID = null;

	@BeforeClass
	public void setupDataForAddShipping() throws InterruptedException{	
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

	//Hybris Project-2029 :: Version : 1 :: Add shipping address on 'Shipping Profile' page
	@Test(priority=1)
	public void testAddShippingAddressOnShippingProfilePage_2029() throws InterruptedException{
		randomNum = CommonUtils.getRandomNum(10000, 1000000);
		String lastName = "lN";
		storeFrontConsultantPage = storeFrontHomePage.clickRodanAndFieldsLogo();
		storeFrontConsultantPage.clickOnWelcomeDropDown();
		storeFrontShippingInfoPage = storeFrontConsultantPage.clickShippingLinkPresentOnWelcomeDropDown();
		s_assert.assertTrue(storeFrontShippingInfoPage.verifyShippingInfoPageIsDisplayed(),"shipping info page has not been displayed");
		storeFrontShippingInfoPage.clickAddNewShippingProfileLink();
		String newShippingAddressName = TestConstants.ADDRESS_NAME+randomNum;
		storeFrontShippingInfoPage.enterNewShippingAddressName(newShippingAddressName+" "+lastName);
		storeFrontShippingInfoPage.enterNewShippingAddressLine1(addressLine1);
		storeFrontShippingInfoPage.enterNewShippingAddressCity(city);
		storeFrontShippingInfoPage.selectNewShippingAddressState(state);
		storeFrontShippingInfoPage.enterNewShippingAddressPostalCode(postalCode);
		storeFrontShippingInfoPage.enterNewShippingAddressPhoneNumber(TestConstants.PHONE_NUMBER);
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

		s_assert.assertAll();		
	}

	//Hybris Project-2030 :: Version : 1 :: Add shipping address during checkout 
	@Test(priority=2)
	public void testAddShippingAddressDuringCheckout_2030() throws InterruptedException{
		randomNum = CommonUtils.getRandomNum(10000, 1000000);
		String lastName = "lN";
		storeFrontConsultantPage = storeFrontHomePage.clickRodanAndFieldsLogo();
		storeFrontConsultantPage.clickOnWelcomeDropDown();
		storeFrontShippingInfoPage = storeFrontConsultantPage.clickShippingLinkPresentOnWelcomeDropDown();
		s_assert.assertTrue(storeFrontShippingInfoPage.verifyShippingInfoPageIsDisplayed(),"shipping info page has not been displayed");
		String defaultSelectedShippingAddressNameOnShippingInfo = storeFrontShippingInfoPage.getDefaultSelectedShippingAddress();
		storeFrontConsultantPage = storeFrontHomePage.clickRodanAndFieldsLogo();
		//Continue with checkout page
		storeFrontConsultantPage.hoverOnShopLinkAndClickAllProductsLinksAfterLogin();		
		storeFrontUpdateCartPage.clickAddToBagButton(country);
		storeFrontUpdateCartPage.clickOnCheckoutButton();
		storeFrontUpdateCartPage.clickOnAddANewShippingAddress();

		String newShippingAddressName = TestConstants.ADDRESS_NAME+randomNum;
		storeFrontUpdateCartPage.enterNewShippingAddressName(newShippingAddressName+" "+lastName);
		storeFrontUpdateCartPage.enterNewShippingAddressLine1(addressLine1);
		storeFrontUpdateCartPage.enterNewShippingAddressCity(city);
		storeFrontUpdateCartPage.selectNewShippingAddressStateOnCartPage();
		storeFrontUpdateCartPage.enterNewShippingAddressPostalCode(postalCode);
		storeFrontUpdateCartPage.enterNewShippingAddressPhoneNumber(TestConstants.PHONE_NUMBER);
		storeFrontUpdateCartPage.clickOnSaveShippingProfileAfterEdit();
		s_assert.assertTrue(storeFrontUpdateCartPage.verifyNewShippingAddressIsSelectedByDefaultOnAdhocCart(newShippingAddressName), "New Address has not got associated with the billing profile");
		storeFrontConsultantPage.clickOnRodanAndFieldsLogo();
		storeFrontConsultantPage.clickOnWelcomeDropDown();
		storeFrontShippingInfoPage = storeFrontConsultantPage.clickShippingLinkPresentOnWelcomeDropDown();
		s_assert.assertTrue(storeFrontShippingInfoPage.isShippingAddressPresentOnShippingPage(newShippingAddressName), "New Shipping address is not listed on Shipping profile page");
		String defaultSelectedShippingAddressNameAfterAddAShippingAddress = storeFrontShippingInfoPage.getDefaultSelectedShippingAddress();

		s_assert.assertTrue(storeFrontShippingInfoPage.verifyOldDefaultSelectAddress(defaultSelectedShippingAddressNameOnShippingInfo, defaultSelectedShippingAddressNameAfterAddAShippingAddress), "New Address has not got associated with the billing profile");

		s_assert.assertAll();
	}

	//Hybris Project-2031 :: Version : 1 :: Add shipping address in autoship template 
	@Test(priority=3)
	public void testAddShippingAddressInAutoshipTemplate_2031() throws InterruptedException{
		randomNum = CommonUtils.getRandomNum(10000, 1000000);
		String lastName = "lN";
		//get shipping address name on adhoc order
		storeFrontConsultantPage = storeFrontHomePage.clickRodanAndFieldsLogo();
		storeFrontConsultantPage.clickOnWelcomeDropDown();
		storeFrontOrdersPage =  storeFrontConsultantPage.clickOrdersLinkPresentOnWelcomeDropDown();
		s_assert.assertTrue(storeFrontOrdersPage.verifyOrdersPageIsDisplayed(),"Orders page has not been displayed");
		storeFrontOrdersPage.clickOnWelcomeDropDown();
		storeFrontCartAutoShipPage = storeFrontOrdersPage.clickEditCrpLinkPresentOnWelcomeDropDown();
		storeFrontUpdateCartPage = storeFrontCartAutoShipPage.clickUpdateMoreInfoLink();
		storeFrontUpdateCartPage.clickOnEditShipping();
		storeFrontUpdateCartPage.clickAddNewShippingProfileLink();
		String newShippingAddressName = TestConstants.ADDRESS_NAME+randomNum;

		storeFrontUpdateCartPage.enterNewShippingAddressName(newShippingAddressName+" "+lastName);
		storeFrontUpdateCartPage.enterNewShippingAddressLine1(addressLine1);
		storeFrontUpdateCartPage.enterNewShippingAddressCity(city);
		storeFrontUpdateCartPage.selectNewShippingAddressState(state);
		storeFrontUpdateCartPage.enterNewShippingAddressPostalCode(postalCode);
		storeFrontUpdateCartPage.enterNewShippingAddressPhoneNumber(TestConstants.PHONE_NUMBER);
		storeFrontUpdateCartPage.clickOnSaveShippingProfile();
		storeFrontUpdateCartPage.clickOnUpdateCartShippingNextStepBtn();
		storeFrontUpdateCartPage.clickOnEditShipping();

		//storeFrontUpdateCartPage.clickOnEditShipping();

		//---------------Verify that the new added shipping address is displayed in 'Shipment' section on update autoship cart page------------------------------------------------------------------------

		s_assert.assertTrue(storeFrontUpdateCartPage.isShippingAddressPresent(newShippingAddressName), "New Shipping address NOT selected in update cart under shipping section");
		s_assert.assertTrue(storeFrontUpdateCartPage.isNewlyCreatedShippingProfileIsSelectedByDefault(newShippingAddressName), "New Shipping address NOT selected as default in update cart under shipping section");

		//-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

		storeFrontUpdateCartPage.clickOnUpdateCartShippingNextStepBtn();
		storeFrontUpdateCartPage.clickOnNextStepBtn();
		storeFrontUpdateCartPage.clickUpdateCartBtn();
		storeFrontUpdateCartPage.clickRodanAndFieldsLogo();
		storeFrontUpdateCartPage.clickOnWelcomeDropDown();
		storeFrontShippingInfoPage = storeFrontUpdateCartPage.clickShippingLinkPresentOnWelcomeDropDown();
		s_assert.assertTrue(storeFrontShippingInfoPage.verifyShippingInfoPageIsDisplayed(),"shipping info page has not been displayed");
		s_assert.assertTrue(storeFrontShippingInfoPage.isShippingAddressPresentOnShippingPage(newShippingAddressName), "New Shipping address is not listed on Shipping profile page");
		//s_assert.assertTrue(storeFrontShippingInfoPage.isAutoshipOrderAddressTextPresent(newShippingAddressName), "Autoship order text is not present under the new Shipping Address");
		s_assert.assertFalse(storeFrontShippingInfoPage.verifyRadioButtonNotSelectedByDefault(newShippingAddressName), "Newly created shipping address is selected by default");
		s_assert.assertAll();
	} 

	//Hybris Project-2238:Verify that QAS validation gets perform everytime user adds a shipping address.
	@Test(priority=4)
	public void testQASValidationPerformEveryTimeUserAddsAShippingAddress_2238() throws InterruptedException{
		randomNum = CommonUtils.getRandomNum(10000, 1000000);
		String lastName = "lN";
		storeFrontConsultantPage = storeFrontHomePage.clickRodanAndFieldsLogo();
		storeFrontConsultantPage.hoverOnShopLinkAndClickAllProductsLinksAfterLogin();  
		storeFrontUpdateCartPage.clickAddToBagButton(country);
		storeFrontUpdateCartPage.clickOnCheckoutButton();
		//Add a new shipping address
		storeFrontUpdateCartPage.clickAddNewShippingProfileLink();
		String newShippingAddressName = TestConstants.ADDRESS_NAME+randomNum;

		storeFrontUpdateCartPage.enterNewShippingAddressName(newShippingAddressName+" "+lastName);
		storeFrontUpdateCartPage.enterNewShippingAddressLine1(addressLine1);
		storeFrontUpdateCartPage.enterNewShippingAddressCity(city);
		storeFrontUpdateCartPage.selectNewShippingAddressState(state);
		storeFrontUpdateCartPage.enterNewShippingAddressPostalCode(postalCode);
		storeFrontUpdateCartPage.enterNewShippingAddressPhoneNumber(TestConstants.PHONE_NUMBER);
		storeFrontUpdateCartPage.clickOnSaveShippingProfileWithoutAcceptingQASValidationPopUp();
		//validate QAS Validation PopUp is Displayed after adding a Shipping Profile
		s_assert.assertTrue(storeFrontUpdateCartPage.validateQASValidationPopUpIsDisplayed(),"QAS Validation PopUp is Not Displayed After Adding A Shipping Profile");
		s_assert.assertAll();
	}

	//Hybris Project-2032 :: Version : 1 :: Add shipping address during PC user or Retail user registration
	@Test(priority=5)
	public void testAddShippingAddressDuringPCRegistration_2032() throws InterruptedException{
		randomNum = CommonUtils.getRandomNum(10000, 1000000);		
		String newBillingProfileName = TestConstants.NEW_BILLING_PROFILE_NAME+randomNum;
		String lastName = "lN";
		String firstName=TestConstants.FIRST_NAME+randomNum;
		navigateToStoreFrontBaseURL();
		// Click on our product link that is located at the top of the page and then click in on quick shop
		storeFrontHomePage.hoverOnShopLinkAndClickAllProductsLinks();

		//		// Products are displayed?
		//		s_assert.assertTrue(storeFrontHomePage.areProductsDisplayed(), "quickshop products not displayed");
		//		logger.info("Quick shop products are displayed");
		storeFrontHomePage.selectProductAndProceedToBuy();

		//		//Cart page is displayed?
		//		s_assert.assertTrue(storeFrontHomePage.isCartPageDisplayed(), "Cart page is not displayed");
		//		logger.info("Cart page is displayed");

		//Click on Check out
		storeFrontHomePage.clickOnCheckoutButton();

		//Log in or create an account page is displayed?
		//		s_assert.assertTrue(storeFrontHomePage.isLoginOrCreateAccountPageDisplayed(), "Login or Create Account page is NOT displayed");
		//		logger.info("Login or Create Account page is displayed");

		//Enter the User information and DO NOT check the "Become a Preferred Customer" checkbox and click the create account button
		String pcEmailID = firstName+TestConstants.EMAIL_ADDRESS_SUFFIX;
		storeFrontHomePage.enterNewPCDetails(firstName, TestConstants.LAST_NAME+randomNum, password, pcEmailID);

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

		// add a new shipping profile
		storeFrontUpdateCartPage.clickOnEditShipping();
		storeFrontUpdateCartPage.clickOnAddANewShippingAddress();
		String newShippingAddressName = TestConstants.ADDRESS_NAME+randomNum;

		storeFrontUpdateCartPage.enterNewShippingAddressName(newShippingAddressName+" "+lastName);
		storeFrontUpdateCartPage.enterNewShippingAddressLine1(addressLine1);
		storeFrontUpdateCartPage.enterNewShippingAddressCity(city);
		storeFrontUpdateCartPage.selectNewShippingAddressStateOnCartPage();
		storeFrontUpdateCartPage.enterNewShippingAddressPostalCode(postalCode);
		storeFrontUpdateCartPage.enterNewShippingAddressPhoneNumber(TestConstants.PHONE_NUMBER);
		storeFrontUpdateCartPage.clickOnSaveShippingProfileAfterEdit();
		storeFrontUpdateCartPage.clickOnShippingAddressNextStepBtn();
		storeFrontUpdateCartPage.clickOnEditShipping();

		//storeFrontUpdateCartPage.clickOnEditShipping();

		//---------------Verify that the new added shipping address is displayed in 'Shipment' section on update autoship cart page------------------------------------------------------------------------

		s_assert.assertTrue(storeFrontUpdateCartPage.isShippingAddressPresent(newShippingAddressName), "New Shipping address NOT selected in update cart under shipping section");

		//-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

		storeFrontUpdateCartPage.clickOnShippingAddressNextStepBtn();
		storeFrontUpdateCartPage.clickOnBillingNextStepBtn();

		storeFrontHomePage.clickOnPCPerksTermsAndConditionsCheckBoxes();
		storeFrontHomePage.clickPlaceOrderBtn();
		storeFrontHomePage.clickOnRodanAndFieldsLogo();
		s_assert.assertTrue(storeFrontHomePage.verifyWelcomeDropdownToCheckUserRegistered(), "User NOT registered successfully");
		storeFrontUpdateCartPage.clickOnWelcomeDropDown();
		storeFrontShippingInfoPage = storeFrontUpdateCartPage.clickShippingLinkPresentOnWelcomeDropDown();
		s_assert.assertTrue(storeFrontShippingInfoPage.verifyShippingInfoPageIsDisplayed(),"shipping info page has not been displayed");
		s_assert.assertTrue(storeFrontShippingInfoPage.isShippingAddressPresentOnShippingPage(newShippingAddressName), "New Shipping address is not listed on Shipping profile page");
		//s_assert.assertTrue(storeFrontShippingInfoPage.isAutoshipOrderAddressTextPresent(newShippingAddressName), "Autoship order text is not present under the new Shipping Address");
		s_assert.assertFalse(storeFrontShippingInfoPage.verifyRadioButtonNotSelectedByDefault(newShippingAddressName), "Newly created shipping address is selected by default");
		s_assert.assertTrue(storeFrontShippingInfoPage.verifyRadioButtonIsSelectedByDefault(firstName), "Shipping address was given in main account info is not selected by default");

		storeFrontUpdateCartPage.clickOnAutoshipCart();
		storeFrontUpdateCartPage.clickOnUpdateMoreInfoButton();

		//assert newly created shipping address present on cart page 
		s_assert.assertTrue(storeFrontUpdateCartPage.isShippingAddressPresent(newShippingAddressName), "New Shipping address NOT listed in update cart under shipping section");

		//s_assert.assertTrue(storeFrontHomePage.getUserNameAForVerifyLogin(firstName).contains(firstName),"Profile Name After Login"+firstName+" and on UI is "+storeFrontHomePage.getUserNameAForVerifyLogin(firstName));
		s_assert.assertAll();

	}

	//Hybris Project-2033 :: Version : 1 :: Add shipping address during consultant enrollment
	@Test(priority=6)
	public void testAddShippingAddressDuringConsultantEnrollment_2033() throws InterruptedException{
		randomNum = CommonUtils.getRandomNum(10000, 1000000);
		String socialInsuranceNumber = String.valueOf(CommonUtils.getRandomNum(100000000, 999999999));
		String  consultantEmailAddress=TestConstants.FIRST_NAME+randomNum+TestConstants.EMAIL_ADDRESS_SUFFIX;
		String firstName = TestConstants.FIRST_NAME+randomNum;
		//String shippingAddressName = firstName+" "+TestConstants.LAST_NAME;
		storeFrontHomePage.openPWSSite(country, env);
		storeFrontHomePage.hoverOnBecomeAConsultantAndClickEnrollNowLink();
		storeFrontHomePage.enterUserInformationForEnrollmentWithEmail(kitName, regimenName, enrollmentType, firstName, TestConstants.LAST_NAME,consultantEmailAddress, password, addressLine1, city,state, postalCode, phoneNumber);
		storeFrontHomePage.clickEnrollmentNextBtn();
		storeFrontHomePage.enterCardNumber(TestConstants.CARD_NUMBER);
		storeFrontHomePage.enterNameOnCard(TestConstants.FIRST_NAME+randomNum);
		storeFrontHomePage.selectNewBillingCardExpirationDate();
		storeFrontHomePage.enterSecurityCode(TestConstants.SECURITY_CODE);
		storeFrontHomePage.enterSocialInsuranceNumber(socialInsuranceNumber);
		storeFrontHomePage.enterNameAsItAppearsOnCard(TestConstants.FIRST_NAME);
		storeFrontHomePage.clickEnrollmentNextBtn();
		s_assert.assertTrue(storeFrontHomePage.isTheTermsAndConditionsCheckBoxDisplayed(), "Terms and Conditions checkbox is not visible");

		storeFrontHomePage.checkTheIAcknowledgeCheckBox();		
		storeFrontHomePage.checkTheIAgreeCheckBox();
		storeFrontHomePage.checkThePoliciesAndProceduresCheckBox();
		storeFrontHomePage.checkTheTermsAndConditionsCheckBox();
		storeFrontHomePage.clickOnEnrollMeBtn();
		storeFrontHomePage.clickOnConfirmAutomaticPayment();
		s_assert.assertTrue(storeFrontHomePage.verifyCongratsMessage(), "Congrats Message is not visible");
		storeFrontHomePage.clickOnRodanAndFieldsLogo();
		s_assert.assertTrue(storeFrontHomePage.verifyWelcomeDropdownToCheckUserRegistered(), "User NOT registered successfully");

		storeFrontHomePage.clickOnWelcomeDropDown();
		storeFrontOrdersPage =  storeFrontHomePage.clickOrdersLinkPresentOnWelcomeDropDown();
		s_assert.assertTrue(storeFrontOrdersPage.verifyOrdersPageIsDisplayed(),"Orders page has not been displayed");

		// Get Order Number
		String orderHistoryNumber = storeFrontOrdersPage.getFirstOrderNumberFromOrderHistory();
		storeFrontOrdersPage.clickOrderNumber(orderHistoryNumber);
		s_assert.assertTrue(storeFrontOrdersPage.getShippingAddressFromAdhocTemplate().contains(firstName.toLowerCase()), "Adhoc Order template shipping address on RFO is"+firstName+" and on UI is "+storeFrontOrdersPage.getShippingAddressFromAdhocTemplate());

		// verify on shipping info page
		storeFrontHomePage.clickOnWelcomeDropDown();
		storeFrontShippingInfoPage = storeFrontHomePage.clickShippingLinkPresentOnWelcomeDropDown();
		s_assert.assertTrue(storeFrontShippingInfoPage.verifyShippingInfoPageIsDisplayed(),"shipping info page has not been displayed");
		s_assert.assertTrue(storeFrontShippingInfoPage.isShippingAddressPresentOnShippingPage(firstName), "New Shipping address is not listed on Shipping profile page");
		s_assert.assertTrue(storeFrontShippingInfoPage.verifyRadioButtonNotSelectedByDefault(firstName), "Newly created shipping address is not selected by default");
		s_assert.assertTrue(storeFrontShippingInfoPage.verifyRadioButtonIsSelectedByDefault(firstName), "Shipping address was given in main account info is selected by default");
		s_assert.assertAll();
	}

	//Hybris Project-2034 :: Version : 1 :: Add shipping address during CRP enrollment through my account 
	@Test(priority=7)
	public void testAddShippingAddressDuringCRPEnrollment_2034() throws InterruptedException{
		navigateToStoreFrontBaseURL();
		randomNum = CommonUtils.getRandomNum(10000, 1000000);
		String socialInsuranceNumber = String.valueOf(CommonUtils.getRandomNum(100000000, 999999999));
		enrollmentType = TestConstants.STANDARD_ENROLLMENT;
		regimenName = TestConstants.REGIMEN_NAME_UNBLEMISH;
		String firstName = TestConstants.FIRST_NAME+randomNum;
		String consultantEmail = firstName+TestConstants.EMAIL_ADDRESS_SUFFIX;
		String newShippingAddressName = TestConstants.FIRST_NAME+randomNum;
		//Enroll a consultant without CRP and pulse
		storeFrontHomePage.hoverOnBecomeAConsultantAndClickEnrollNowLink();
		storeFrontHomePage.searchCID();
		storeFrontHomePage.mouseHoverSponsorDataAndClickContinue();
		storeFrontHomePage.enterUserInformationForEnrollmentWithEmail(kitName, regimenName, enrollmentType, firstName, TestConstants.LAST_NAME,consultantEmail, password, addressLine1, city,state, postalCode, phoneNumber);
		storeFrontHomePage.clickNextButton();

		storeFrontHomePage.enterCardNumber(TestConstants.CARD_NUMBER);
		storeFrontHomePage.enterNameOnCard(TestConstants.FIRST_NAME+randomNum);
		storeFrontHomePage.selectNewBillingCardExpirationDate();
		storeFrontHomePage.enterSecurityCode(TestConstants.SECURITY_CODE);
		storeFrontHomePage.enterSocialInsuranceNumber(socialInsuranceNumber);
		storeFrontHomePage.enterNameAsItAppearsOnCard(TestConstants.FIRST_NAME);
		storeFrontHomePage.clickEnrollmentNextBtn();
		storeFrontHomePage.uncheckPulseAndCRPEnrollment();
		s_assert.assertTrue(storeFrontHomePage.verifySubsribeToPulseCheckBoxIsNotSelected(), "Subscribe to pulse checkbox selected after uncheck");
		s_assert.assertTrue(storeFrontHomePage.verifyEnrollToCRPCheckBoxIsNotSelected(), "Enroll to CRP checkbox selected after uncheck");
		storeFrontHomePage.clickEnrollmentNextBtn();
		s_assert.assertTrue(storeFrontHomePage.isTheTermsAndConditionsCheckBoxDisplayed(), "Terms and Conditions checkbox is not visible");
		storeFrontHomePage.checkThePoliciesAndProceduresCheckBox();
		storeFrontHomePage.checkTheIAcknowledgeCheckBox();  
		storeFrontHomePage.checkTheIAgreeCheckBox();
		storeFrontHomePage.checkTheTermsAndConditionsCheckBox();
		storeFrontHomePage.clickOnEnrollMeBtn();
		s_assert.assertTrue(storeFrontHomePage.verifyCongratsMessage(), "Congrats Message is not visible");
		storeFrontHomePage.clickOnRodanAndFieldsLogo();
		s_assert.assertTrue(storeFrontHomePage.verifyWelcomeDropdownToCheckUserRegistered(), "User NOT registered successfully");

		// Enroll in CRP
		storeFrontHomePage.clickOnWelcomeDropDown();
		storeFrontAccountInfoPage = storeFrontHomePage.clickAccountInfoLinkPresentOnWelcomeDropDown();
		s_assert.assertTrue(storeFrontAccountInfoPage.verifyAccountInfoPageIsDisplayed(),"shipping info page has not been displayed");
		storeFrontAccountInfoPage.clickOnYourAccountDropdown();//added
		storeFrontAccountInfoPage.clickOnAutoShipStatus();
		storeFrontAccountInfoPage.clickOnEnrollInCRP();
		storeFrontAccountInfoPage.clickOnAddToCRPButtonCreatingCRPUnderBizSite();
		storeFrontAccountInfoPage.clickOnCRPCheckout();
		storeFrontUpdateCartPage.clickAddNewShippingProfileLink();
		String lastName = "In";
		String newShippingName = newShippingAddressName+randomNum;
		storeFrontUpdateCartPage.enterNewShippingAddressName(newShippingName+" "+lastName);
		storeFrontUpdateCartPage.enterNewShippingAddressLine1(addressLine1);
		storeFrontUpdateCartPage.enterNewShippingAddressCity(city);
		storeFrontUpdateCartPage.selectNewShippingAddressStateOnCartPage();
		storeFrontUpdateCartPage.enterNewShippingAddressPostalCode(postalCode);
		storeFrontUpdateCartPage.enterNewShippingAddressPhoneNumber(TestConstants.PHONE_NUMBER);
		storeFrontUpdateCartPage.clickOnSaveCRPShippingInfo();
		s_assert.assertTrue(storeFrontUpdateCartPage.verifyNewlyCreatedShippingAddressIsSelectedByDefault(newShippingName), "Newly created shipping address is not selected by default");
		storeFrontUpdateCartPage.clickOnUpdateCartShippingNextStepBtnDuringEnrollment();
		storeFrontUpdateCartPage.clickOnBillingNextStepBtn();
		storeFrontUpdateCartPage.clickOnSetupCRPAccountBtn();
		s_assert.assertTrue(storeFrontUpdateCartPage.verifyOrderConfirmation(), "Order Confirmation Message has not been displayed");

		// verify on shipping info page
		storeFrontUpdateCartPage.clickOnWelcomeDropDown();
		storeFrontShippingInfoPage = storeFrontUpdateCartPage.clickShippingLinkPresentOnWelcomeDropDown();
		s_assert.assertTrue(storeFrontShippingInfoPage.verifyShippingInfoPageIsDisplayed(),"shipping info page has not been displayed");
		s_assert.assertTrue(storeFrontShippingInfoPage.isShippingAddressPresentOnShippingPage(newShippingName), "New Shipping address is not listed on Shipping profile page");
		s_assert.assertAll();
	}
}