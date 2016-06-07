package com.rf.test.website.storeFront.hybris.billingAndShipping;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import javax.mail.internet.NewsAddress;

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
import com.rf.pages.website.storeFront.StoreFrontOrdersAutoshipStatusPage;
import com.rf.pages.website.storeFront.StoreFrontOrdersPage;
import com.rf.pages.website.storeFront.StoreFrontPCUserPage;
import com.rf.pages.website.storeFront.StoreFrontShippingInfoPage;
import com.rf.pages.website.storeFront.StoreFrontUpdateCartPage;
import com.rf.test.website.RFStoreFrontWebsiteBaseTest;
import com.rf.test.website.RFWebsiteBaseTest;

public class EditShippingTest extends RFStoreFrontWebsiteBaseTest{
	private static final Logger logger = LogManager
			.getLogger(EditShippingTest.class.getName());
	private StoreFrontOrdersAutoshipStatusPage storeFrontOrdersAutoshipStatusPage;

	private StoreFrontHomePage storeFrontHomePage;
	private StoreFrontConsultantPage storeFrontConsultantPage;
	private StoreFrontShippingInfoPage storeFrontShippingInfoPage;
	private StoreFrontCartAutoShipPage storeFrontCartAutoShipPage;
	private StoreFrontUpdateCartPage storeFrontUpdateCartPage;
	private StoreFrontOrdersPage storeFrontOrdersPage;
	private StoreFrontAccountInfoPage storeFrontAccountInfoPage;
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
	public void setupDataForEditShipping() throws InterruptedException{	
		storeFrontHomePage = new StoreFrontHomePage(driver);
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


	// Hybris Phase 2-2035 :: Version : 1 :: Edit shipping address on 'Shipping Profile' page
	@Test(priority=1)
	public void testEditShippingAddressOnShippingProfilePage_2035() throws InterruptedException{
		randomNum = CommonUtils.getRandomNum(10000, 1000000);
		String lastName = "lN";
		storeFrontShippingInfoPage = storeFrontConsultantPage.clickShippingLinkPresentOnWelcomeDropDown();
		//s_assert.assertTrue(storeFrontShippingInfoPage.verifyShippingInfoPageIsDisplayed(),"shipping info page has not been displayed");
		storeFrontShippingInfoPage.clickOnEditForFirstAddress();
		String newShippingAdrressName = TestConstants.ADDRESS_NAME+randomNum;
		storeFrontShippingInfoPage.enterNewShippingAddressName(newShippingAdrressName+" "+lastName);
		storeFrontShippingInfoPage.enterNewShippingAddressLine1(addressLine1);
		storeFrontShippingInfoPage.enterNewShippingAddressCity(city);
		storeFrontShippingInfoPage.selectNewShippingAddressState(state);
		storeFrontShippingInfoPage.enterNewShippingAddressPostalCode(postalCode);
		storeFrontShippingInfoPage.enterNewShippingAddressPhoneNumber(TestConstants.PHONE_NUMBER);
		storeFrontShippingInfoPage.selectUseThisShippingProfileFutureAutoshipChkbox();
		storeFrontShippingInfoPage.clickOnSaveShippingProfile();

		//--------------- Verify that Newly added Shipping is listed in the Billing profiles section-----------------------------------------------------------------------------------------------------

		s_assert.assertTrue(storeFrontShippingInfoPage.isShippingAddressPresentOnShippingPage(newShippingAdrressName), "New Shipping address is not selected listed on Shipping profile page");

		//------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

		//--------------- Verify That 'Autoship Order Address' Text is displayed under default shipping Address-------------------------------------------------------------------------------------------

		s_assert.assertTrue(storeFrontShippingInfoPage.isAutoshipOrderAddressTextPresent(newShippingAdrressName), "Autoship order text not present under the new Shipping Address");

		//------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

		storeFrontConsultantPage.clickOnWelcomeDropDown();
		storeFrontCartAutoShipPage = storeFrontConsultantPage.clickEditCrpLinkPresentOnWelcomeDropDown();
		storeFrontUpdateCartPage = storeFrontCartAutoShipPage.clickUpdateMoreInfoLink();
		storeFrontUpdateCartPage.clickOnEditShipping();

		//---------------Verify that the new added shipping address is displayed in 'Shipment' section on update autoship cart page------------------------------------------------------------------------

		s_assert.assertTrue(storeFrontUpdateCartPage.isShippingAddressPresent(newShippingAdrressName), "New Shipping address NOT added to update cart");

		//-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

		s_assert.assertAll();
	}

	// Hybris Project-2036 :: Version : 1 :: Edit shipping address during checkout 
	@Test(priority=2)
	public void testEditShippingAddressDuringCheckout_2036() throws InterruptedException{
		randomNum = CommonUtils.getRandomNum(10000, 1000000);		
		storeFrontShippingInfoPage = storeFrontConsultantPage.clickShippingLinkPresentOnWelcomeDropDown();
		String defaultSelectedShippingName = storeFrontShippingInfoPage.getDefaultSelectedShippingAddress();
		storeFrontHomePage.hoverOnShopLinkAndClickAllProductsLinks();
		storeFrontHomePage.selectProductAndProceedToBuy();
		storeFrontHomePage.clickOnPlaceOrderButton();
		storeFrontUpdateCartPage.clickOnConfirmationOK();
		String newShippingProfileName = TestConstants.ADDRESS_NAME_US+randomNum;
		if(storeFrontUpdateCartPage.isOnlyOneShippingProfilePresentOnAdhocCart()==true) 
			storeFrontUpdateCartPage.addAshippingProfileAndClickOnItsEdit(city,state, addressLine1, newShippingProfileName, phoneNumber, postalCode);
		else
			storeFrontUpdateCartPage.clickOnEditOnNonDefaultAddress();		
		storeFrontUpdateCartPage.enterNewShippingAddressName(newShippingProfileName+" "+TestConstants.LAST_NAME);
		storeFrontUpdateCartPage.enterNewShippingAddressLine1(addressLine1);
		storeFrontUpdateCartPage.enterNewShippingAddressCity(city);
		storeFrontUpdateCartPage.enterNewShippingAddressPhoneNumber(phoneNumber);
		storeFrontUpdateCartPage.enterNewShippingAddressPostalCode(postalCode);
		storeFrontUpdateCartPage.clickOnSaveShippingProfileAfterEdit();
		// verify that updated address is listed on cart page
		s_assert.assertTrue(storeFrontUpdateCartPage.isShippingAddressPresent(newShippingProfileName), "New Shipping address NOT added to update cart");
		storeFrontUpdateCartPage.clickOnShippingAddressNextStepBtn();
		storeFrontUpdateCartPage.clickOnBillingNextStepBtn();
		storeFrontUpdateCartPage.clickPlaceOrderBtn();
		s_assert.assertTrue(storeFrontUpdateCartPage.verifyOrderPlacedConfirmationMessage(),"order is not placed successfully");
		s_assert.assertTrue(storeFrontUpdateCartPage.getUpdatedAddressPresentOnOrderConfirmationPage().toLowerCase().contains(newShippingProfileName.toLowerCase()),"shipping address name expected is"+newShippingProfileName+"while shippig address coming on order confirmation page is "+storeFrontUpdateCartPage.getUpdatedAddressPresentOnOrderConfirmationPage());

		storeFrontUpdateCartPage.clickOnWelcomeDropDown();
		storeFrontUpdateCartPage.clickShippingLinkPresentOnWelcomeDropDown();
		s_assert.assertTrue(storeFrontShippingInfoPage.verifyShippingInfoPageIsDisplayed(),"shipping info page has not been displayed");
		String addressnameAfterEdit = storeFrontShippingInfoPage.getDefaultSelectedShippingAddress();
		// assert of default selected shipping address
		s_assert.assertTrue(storeFrontShippingInfoPage.verifyOldDefaultSelectAddress(defaultSelectedShippingName, addressnameAfterEdit), "New Shipping address is not listed on Shipping profile page");
		s_assert.assertAll();
	}

	//Hybris Project-2037 :: Version : 1 :: Edit shipping address in autoship template 
	@Test(priority=3)
	public void testEditShippingAddressInAutoshipTemplate_2037() throws InterruptedException{
		randomNum = CommonUtils.getRandomNum(10000, 1000000);
		String lastName = "lN";
		storeFrontShippingInfoPage = storeFrontConsultantPage.clickShippingLinkPresentOnWelcomeDropDown();
		String defaultSelectedShippingName = storeFrontShippingInfoPage.getDefaultSelectedShippingAddress();
		String newShippingProfileName = TestConstants.ADDRESS_NAME_US+randomNum;
		storeFrontHomePage.clickOnWelcomeDropDown();
		storeFrontCartAutoShipPage = storeFrontHomePage.clickEditCrpLinkPresentOnWelcomeDropDown();
		storeFrontUpdateCartPage = storeFrontCartAutoShipPage.clickUpdateMoreInfoLink();

		storeFrontUpdateCartPage.clickOnEditShipping();

		if(storeFrontUpdateCartPage.isOnlyOneShippingProfilePresentOnAdhocCart()==true) 
			storeFrontUpdateCartPage.addAshippingProfileAndClickOnItsEdit(city,state, addressLine1, newShippingProfileName, phoneNumber, postalCode);
		else
			storeFrontUpdateCartPage.clickOnEditOnNonDefaultAddress();
		storeFrontShippingInfoPage.enterNewShippingAddressName(newShippingProfileName+" "+lastName);
		storeFrontShippingInfoPage.enterNewShippingAddressLine1(addressLine1);
		storeFrontShippingInfoPage.enterNewShippingAddressCity(city);
		storeFrontShippingInfoPage.enterNewShippingAddressPhoneNumber(phoneNumber);
		storeFrontShippingInfoPage.enterNewShippingAddressPostalCode(postalCode);
		storeFrontUpdateCartPage.clickOnSaveShippingProfileOnUpdateCrpPage();
		s_assert.assertTrue(storeFrontUpdateCartPage.isShippingAddressPresent(newShippingProfileName),"Edited Shipping Address is not on cart page");
		s_assert.assertTrue(storeFrontUpdateCartPage.verifyNewlyEditedShippingAddressIsSelected(newShippingProfileName),"newly edited shipping address is not choosen by default");
		storeFrontUpdateCartPage.clickOnUpdateCartShippingNextStepBtn();
		storeFrontUpdateCartPage.clickOnNextStepBtn();
		storeFrontUpdateCartPage.clickUpdateCartBtn();
		//s_assert.assertTrue(storeFrontUpdateCartPage.verifyUpdatedAddressPresentUpdateCartPg(profileNameSecond),"shipping address not updated in update crp cart page");
		s_assert.assertTrue(storeFrontUpdateCartPage.validateCartUpdated(),"Cart has not Been Updated");
		storeFrontUpdateCartPage.clickOnRodanAndFieldsLogo();
		storeFrontUpdateCartPage.clickOnWelcomeDropDown();
		storeFrontShippingInfoPage=storeFrontUpdateCartPage.clickShippingLinkPresentOnWelcomeDropDown();
		s_assert.assertTrue(storeFrontShippingInfoPage.isShippingAddressPresentOnShippingPage(newShippingProfileName),"Edited Shipping Address is not on Shipping Info Page");
		s_assert.assertFalse(storeFrontShippingInfoPage.isDefaultAddressRadioBtnSelected(newShippingProfileName),"Edited Shipping Address Radio button is Selected on Shipping Info Page");  
		s_assert.assertTrue(storeFrontUpdateCartPage.getDefaultSelectedShippingAddressNameFromShippingInfoPage().contains(defaultSelectedShippingName),"Default selected shipping address is not the expected");
		s_assert.assertAll();
	}

	//Hybris Project-2094:Popup to update autoship shipping profile on changing default selection
	@Test(priority=4)
	public void testPopUpToUpdateAutoshipShippingProfileOnChangingDefaultSelection_2094() throws InterruptedException			 {
		randomNum = CommonUtils.getRandomNum(10000, 1000000);
		String lastName = "lN";
		storeFrontShippingInfoPage=storeFrontHomePage.clickShippingLinkPresentOnWelcomeDropDown();

		storeFrontShippingInfoPage.clickAddNewShippingProfileLink();
		String newShippingAddressName = TestConstants.ADDRESS_NAME+randomNum;
		storeFrontShippingInfoPage.enterNewShippingAddressName(newShippingAddressName+" "+lastName);
		storeFrontShippingInfoPage.enterNewShippingAddressLine1(addressLine1);
		storeFrontShippingInfoPage.enterNewShippingAddressCity(city);
		storeFrontShippingInfoPage.selectNewShippingAddressState(state);
		storeFrontShippingInfoPage.enterNewShippingAddressPostalCode(postalCode);
		storeFrontShippingInfoPage.enterNewShippingAddressPhoneNumber(TestConstants.PHONE_NUMBER);
		storeFrontShippingInfoPage.clickOnSaveShippingProfile();
		//change default shipping profile selection and validate Update AutoShip PopUp
		storeFrontShippingInfoPage.makeShippingProfileAsDefault(newShippingAddressName);
		s_assert.assertTrue(storeFrontShippingInfoPage.validateUpdateAutoShipPopUpPresent(), "Update AutoShip PopUp is not present!!");
		s_assert.assertAll();
	}

	//Hybris Phase 2-4326: View shipping address on 'Shipping Profile' page
	@Test(enabled=false)//(priority=5)
	public void testShippingAddressOnShippingProfile_HP2_4326() throws InterruptedException, SQLException{
		List<Map<String, Object>> shippingAddressCountList =  null;
		List<Map<String, Object>> defaultShippingAddressList =  null;
		String shippingAddressName=null;

		int totalShippingAddressesFromDB = 0;
		storeFrontShippingInfoPage = storeFrontConsultantPage.clickShippingLinkPresentOnWelcomeDropDown();
		s_assert.assertTrue(storeFrontShippingInfoPage.verifyShippingInfoPageIsDisplayed(),"shipping info page has not been displayed");

		//------------------The same number of billing addresses is shown in RFO and Front end----------------------------------------------------------------------------------------------------------------------------
		shippingAddressCountList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_SHIPPING_ADDRESS_COUNT_QUERY,consultantEmailID),RFO_DB);
		totalShippingAddressesFromDB = (Integer) getValueFromQueryResult(shippingAddressCountList, "count");			
		assertEquals("Shipping Addresses count on UI is different from DB", totalShippingAddressesFromDB,storeFrontShippingInfoPage.getTotalShippingAddressesDisplayed());			

		//-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

		if(totalShippingAddressesFromDB > 1){

			//---------------------------------Radio button is checked for the default shipping address on Front end as per RFO--------------------------------------------------------------------------------------------
			defaultShippingAddressList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_DEFAULT_BILLING_ADDRESS_QUERY,consultantEmailID),RFO_DB);
			shippingAddressName = (String) getValueFromQueryResult(defaultShippingAddressList, "AddressLine1");
			assertTrue("Default radio button in Shipping page is not selected", storeFrontShippingInfoPage.isDefaultShippingAddressSelected(shippingAddressName));
		}

		//-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
		s_assert.assertAll();
	}

	// Hybris Project-2038 :: Version : 1 :: Edit shipping address during PC user or Retail user registration  
	@Test(priority=6)
	public void testEditShippingAddressDuringPCEnrollment_2038() throws InterruptedException{
		logout();
		randomNum = CommonUtils.getRandomNum(10000, 1000000);  
		String newBillingProfileName = TestConstants.NEW_BILLING_PROFILE_NAME+randomNum;
		String lastName = "lN";
		int randomNumber = CommonUtils.getRandomNum(10000, 1000000);
		String firstName=TestConstants.FIRST_NAME+randomNum;
		String emailAddress = firstName+"@xyz.com";
		String newShippingAddressName = TestConstants.FIRST_NAME+randomNumber;
		navigateToStoreFrontBaseURL();
		storeFrontHomePage.hoverOnShopLinkAndClickAllProductsLinks();

		// Products are displayed?
//		s_assert.assertTrue(storeFrontHomePage.areProductsDisplayed(), "quickshop products not displayed");
//		logger.info("Quick shop products are displayed");

		//Select a product and proceed to buy it
		storeFrontHomePage.selectProductAndProceedToBuy();

//		//Cart page is displayed?
//		s_assert.assertTrue(storeFrontHomePage.isCartPageDisplayed(), "Cart page is not displayed");
//		logger.info("Cart page is displayed");

//		//1 product is in the Shopping Cart?
//		s_assert.assertTrue(storeFrontHomePage.verifyNumberOfProductsInCart("1"), "number of products in the cart is NOT 1");
//		logger.info("1 product is successfully added to the cart");

		//Click on Check out
		storeFrontHomePage.clickOnCheckoutButton();

//		//Log in or create an account page is displayed?
//		s_assert.assertTrue(storeFrontHomePage.isLoginOrCreateAccountPageDisplayed(), "Login or Create Account page is NOT displayed");
//		logger.info("Login or Create Account page is displayed");

		//Enter the User information and DO NOT check the "Become a Preferred Customer" checkbox and click the create account button
		storeFrontHomePage.enterNewPCDetails(firstName, TestConstants.LAST_NAME+randomNum, password,emailAddress);

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
		//Edit the shipping Address in ShipMent Section. 
		storeFrontHomePage.clickEditShippingInShipmentOnCheckoutPage();
		storeFrontHomePage.clickEditShipping();
		storeFrontHomePage.enterNewShippingAddressName(newShippingAddressName+" "+TestConstants.LAST_NAME);
		storeFrontHomePage.clickOnSaveShippingProfileAfterEdit();
		//assert for edited shipping address
		s_assert.assertTrue(storeFrontHomePage.verifyUpdatedShippingAddress(newShippingAddressName),"Updated shipping address is not present");
		s_assert.assertTrue(storeFrontHomePage.isDefaultShippingAddressRadioBtnSelected(newShippingAddressName),"Default adddress is not the edited address");
		storeFrontHomePage.clickOnShippingAddressNextStepBtn();
		storeFrontHomePage.clickOnBillingNextStepBtn();
		//verify new shipping address on order confirmation page
		s_assert.assertTrue(storeFrontHomePage.verifyShippingAddressOnOrderPage(newShippingAddressName),"Shipping address on order page is not the edited address");
		storeFrontHomePage.clickOnPCPerksTermsAndConditionsCheckBoxes();
		storeFrontHomePage.clickPlaceOrderBtn();
		storeFrontHomePage.clickOnRodanAndFieldsLogo();
		s_assert.assertTrue(storeFrontHomePage.verifyWelcomeDropdownToCheckUserRegistered(), "User NOT registered successfully");
		//verify the default shipping address is new shipping address
		storeFrontPCUserPage.clickOnWelcomeDropDown();
		storeFrontShippingInfoPage=storeFrontPCUserPage.clickShippingLinkPresentOnWelcomeDropDown();
		s_assert.assertTrue(storeFrontShippingInfoPage.isDefaultAddressRadioBtnSelected(newShippingAddressName),"Default adddress is not the edited address");
		//s_assert.assertTrue(storeFrontShippingInfoPage.isAutoshipOrderAddressTextPresent(newShippingAddressName),"Default selected shipping address does not have autoship text");
		s_assert.assertAll();
	}

	//Hybris Project-2039 :: Version : 1 :: Edit shipping address during consultant enrollment 
	@Test(priority=7)
	public void testEditShippingAddressDuringConsultantEnrollment_2039() throws InterruptedException{
		logout();
		randomNum = CommonUtils.getRandomNum(10000, 1000000);
		int randomNumber = CommonUtils.getRandomNum(10000, 1000000);
		String socialInsuranceNumber = String.valueOf(CommonUtils.getRandomNum(100000000, 999999999));
		storeFrontHomePage.openPWSSite(country, env);
		String sRandName = RandomStringUtils.randomAlphabetic(12);
		String newShippingAddressName = TestConstants.FIRST_NAME+randomNumber;
		storeFrontHomePage.hoverOnBecomeAConsultantAndClickEnrollNowLink();		
		storeFrontHomePage.enterUserInformationForEnrollment(kitName, regimenName, enrollmentType, TestConstants.FIRST_NAME+randomNum, sRandName, TestConstants.PASSWORD, addressLine1, city,state, postalCode, phoneNumber);
		storeFrontHomePage.clickEnrollmentNextBtn();		
		storeFrontHomePage.enterCardNumber(TestConstants.CARD_NUMBER);
		storeFrontHomePage.enterNameOnCard(TestConstants.FIRST_NAME+randomNum);
		storeFrontHomePage.selectNewBillingCardExpirationDate();
		storeFrontHomePage.enterSecurityCode(TestConstants.SECURITY_CODE);
		storeFrontHomePage.enterSocialInsuranceNumber(socialInsuranceNumber);
		storeFrontHomePage.enterNameAsItAppearsOnCard(TestConstants.FIRST_NAME);
		storeFrontHomePage.clickEnrollmentNextBtn();
		storeFrontAccountInfoPage=storeFrontHomePage.clickOnEditShippingOnReviewAndConfirmPage();
		storeFrontHomePage.editFirstName(newShippingAddressName);
		storeFrontHomePage.enterPassword(password);
		storeFrontHomePage.enterConfirmPassword(password);
		storeFrontHomePage.clickEnrollmentNextBtn();
		s_assert.assertTrue(storeFrontHomePage.isTheTermsAndConditionsCheckBoxDisplayed(), "Terms and Conditions checkbox is not visible");
		storeFrontHomePage.checkThePoliciesAndProceduresCheckBox();
		storeFrontHomePage.checkTheIAcknowledgeCheckBox();		
		storeFrontHomePage.checkTheIAgreeCheckBox();
		storeFrontHomePage.checkTheTermsAndConditionsCheckBox();
		storeFrontHomePage.clickOnEnrollMeBtn();
		storeFrontHomePage.clickOnConfirmAutomaticPayment();
		s_assert.assertTrue(storeFrontHomePage.verifyCongratsMessage(), "Congrats Message is not visible");
		storeFrontHomePage.clickOnRodanAndFieldsLogo();
		s_assert.assertTrue(storeFrontHomePage.verifyWelcomeDropdownToCheckUserRegistered(), "User NOT registered successfully");
		storeFrontConsultantPage.clickOnWelcomeDropDown();
		storeFrontOrdersPage=storeFrontConsultantPage.clickOrdersLinkPresentOnWelcomeDropDown();
		String orderNumber=storeFrontOrdersPage.getFirstOrderNumberFromOrderHistory();
		storeFrontOrdersPage.clickOrderNumber(orderNumber);
		s_assert.assertTrue(storeFrontOrdersPage.isShippingAddressContainsName(newShippingAddressName),"Modified shipping address is not present on order page");
		storeFrontConsultantPage.clickOnWelcomeDropDown();
		storeFrontShippingInfoPage=storeFrontConsultantPage.clickShippingLinkPresentOnWelcomeDropDown();
		s_assert.assertTrue(storeFrontShippingInfoPage.verifyShippingInfoPageIsDisplayed(),"Shipping info page is not displayed");
		s_assert.assertFalse(storeFrontShippingInfoPage.isShippingAddressPresentOnShippingPage(TestConstants.FIRST_NAME+randomNum),"Old Shipping address is present on shipping page");
		s_assert.assertTrue(storeFrontShippingInfoPage.isShippingAddressPresentOnShippingPage(newShippingAddressName),"Shipping address is not on shipping page");
		s_assert.assertAll(); 

	}

	//Hybris Project-2040 :: Version : 1 :: Edit shipping address during CRP enrollment through my account 
	@Test(priority=8)
	public void testEditShippingAddressDuringCRPEnrollment_2040() throws InterruptedException{
		logout();
		randomNum = CommonUtils.getRandomNum(10000, 1000000);
		int randomNumber = CommonUtils.getRandomNum(10000, 1000000);
		String socialInsuranceNumber = String.valueOf(CommonUtils.getRandomNum(100000000, 999999999));
		enrollmentType = TestConstants.STANDARD_ENROLLMENT;
		regimenName = TestConstants.REGIMEN_NAME_UNBLEMISH;
		String firstName = TestConstants.FIRST_NAME+randomNum;
		String lastName = "lN";
		String consultantEmail = firstName+TestConstants.EMAIL_ADDRESS_SUFFIX;
		String newShippingAddressName = TestConstants.FIRST_NAME+randomNumber;
		navigateToStoreFrontBaseURL();
		//Enroll a consultant without CRP and pulse
		storeFrontHomePage.hoverOnBecomeAConsultantAndClickEnrollNowLink();
		storeFrontHomePage.searchCID();
		storeFrontHomePage.mouseHoverSponsorDataAndClickContinue();
		storeFrontHomePage.enterUserInformationForEnrollmentWithEmail(kitName, regimenName, enrollmentType, firstName, TestConstants.LAST_NAME,consultantEmail, password, addressLine1, city,state,postalCode, phoneNumber);
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

		//Edit shipping address during enroll the consultant in CRP 
		storeFrontConsultantPage.clickOnWelcomeDropDown();
		storeFrontAccountInfoPage = storeFrontConsultantPage.clickAccountInfoLinkPresentOnWelcomeDropDown();
		s_assert.assertTrue(storeFrontAccountInfoPage.verifyAccountInfoPageIsDisplayed(),"shipping info page has not been displayed");
		storeFrontAccountInfoPage.clickOnYourAccountDropdown();//added
		storeFrontOrdersAutoshipStatusPage=storeFrontAccountInfoPage.clickOnAutoShipStatus();
		storeFrontOrdersAutoshipStatusPage.clickOnEnrollInCRP();
		storeFrontOrdersAutoshipStatusPage.clickAddToCRPButton(driver.getCountry());
		storeFrontOrdersAutoshipStatusPage.clickOnCRPCheckout();
		storeFrontOrdersAutoshipStatusPage.clickOnEditShipping();
		storeFrontUpdateCartPage.enterNewShippingAddressName(newShippingAddressName+" "+lastName);
		storeFrontUpdateCartPage.clickOnSaveShippingProfile();
		s_assert.assertTrue(storeFrontUpdateCartPage.verifyUpdatedShippingAddress(newShippingAddressName),"Updated shipping address is not present");
		storeFrontHomePage.clickOnUpdateCartShippingNextStepBtnDuringEnrollment();
		storeFrontHomePage.clickOnBillingNextStepBtn();
		storeFrontHomePage.clickOnSetupCRPAccountBtn();
		s_assert.assertTrue(storeFrontHomePage.verifyOrderConfirmation(), "Order Confirmation Message has not been displayed");
		storeFrontHomePage.clickOnRodanAndFieldsLogo();
		storeFrontHomePage.handlePolicies();
		//verify the default shipping address is new shipping address
		storeFrontConsultantPage.clickOnWelcomeDropDown();
		storeFrontShippingInfoPage=storeFrontConsultantPage.clickShippingLinkPresentOnWelcomeDropDown();
		s_assert.assertTrue(storeFrontShippingInfoPage.isDefaultAddressRadioBtnSelected(newShippingAddressName),"Default adddress is not the edited address");
		//s_assert.assertTrue(storeFrontShippingInfoPage.isAutoshipOrderAddressTextPresent(newShippingAddressName),"Default selected shipping address does not have autoship text");
		s_assert.assertAll();
	}

	//Hybris Project-2239:Verify that QAS validation gets perform everytime user edits a shipping address.
	@Test(priority=9)
	public void testQASValidationPerformEveryTimeUserEditsAShippingAddress_2239() throws InterruptedException		 {
		randomNum = CommonUtils.getRandomNum(10000, 1000000);
		String lastName = "lN";
		String addressLine2 = null;
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
		storeFrontUpdateCartPage.clickOnSaveShippingProfile();
		//Edit the default shipping address
		storeFrontUpdateCartPage.clickOnEditForDefaultShippingAddress(); 
		//Enter new Shipping Address Line 1
		storeFrontUpdateCartPage.enterNewShippingAddressLine1(addressLine2);
		storeFrontUpdateCartPage.clickOnSaveShippingProfileWithoutAcceptingQASValidationPopUp();
		//validate QAS Validation PopUp is Displayed after Editing a Shipping Profile
		s_assert.assertTrue(storeFrontUpdateCartPage.validateQASValidationPopUpIsDisplayed(),"QAS Validation PopUp is Not Displayed After Editing A Shipping Profile");
		s_assert.assertAll();
	}


}