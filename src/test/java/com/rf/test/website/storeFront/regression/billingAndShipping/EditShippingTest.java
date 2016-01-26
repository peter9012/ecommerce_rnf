package com.rf.test.website.storeFront.regression.billingAndShipping;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.annotations.Test;
import com.rf.core.utils.CommonUtils;
import com.rf.core.utils.DBUtil;
import com.rf.core.website.constants.TestConstants;
import com.rf.core.website.constants.dbQueries.DBQueries_RFO;
import com.rf.pages.website.StoreFrontAccountInfoPage;
import com.rf.pages.website.StoreFrontCartAutoShipPage;
import com.rf.pages.website.StoreFrontConsultantPage;
import com.rf.pages.website.StoreFrontHomePage;
import com.rf.pages.website.StoreFrontOrdersAutoshipStatusPage;
import com.rf.pages.website.StoreFrontOrdersPage;
import com.rf.pages.website.StoreFrontPCUserPage;
import com.rf.pages.website.StoreFrontShippingInfoPage;
import com.rf.pages.website.StoreFrontUpdateCartPage;
import com.rf.test.website.RFWebsiteBaseTest;

public class EditShippingTest extends RFWebsiteBaseTest{
	private static final Logger logger = LogManager
			.getLogger(EditShippingTest.class.getName());

	private StoreFrontConsultantPage storeFrontConsultantPage;
	private StoreFrontShippingInfoPage storeFrontShippingInfoPage;
	private StoreFrontCartAutoShipPage storeFrontCartAutoShipPage;
	private StoreFrontUpdateCartPage storeFrontUpdateCartPage;
	private StoreFrontPCUserPage storeFrontPCUserPage;
	private StoreFrontAccountInfoPage storeFrontAccountInfoPage;
	private String RFO_DB = null;
	private String city = null;
	private String addressLine = null;
	private String profileName = null;
	private String phoneNumber = null;
	private String postalCode = null;
	private String kitName = null;
	private String regimenName = null;
	private String enrollmentType = null;
	private String addressLine1 = null;
	private String country = null;
	private String env = null;
	private String newCity; 
	private String newAddressLine;
	private String newPhoneNumber;
	private String newPostalCode; 
	private String newProfileName;
	private StoreFrontHomePage storeFrontHomePage;
	private StoreFrontOrdersPage storeFrontOrdersPage;
	private StoreFrontOrdersAutoshipStatusPage storeFrontOrdersAutoshipStatusPage;

	// Hybris Phase 2-2035 :: Version : 1 :: Edit shipping address on 'Shipping Profile' page
	@Test
	public void testEditShippingAddressOnShippingProfilePage_2035() throws InterruptedException{
		int randomNum = CommonUtils.getRandomNum(10000, 1000000);
		RFO_DB = driver.getDBNameRFO(); 
		String country = driver.getCountry();
		String addressLine1 = null;
		String city = null;
		String postalCode = null;
		List<Map<String, Object>> randomConsultantList =  null;
		String consultantEmailID = null;
		String accountID = null;
		String lastName = "lN";

		if(country.equalsIgnoreCase("us")){
			addressLine1 = TestConstants.ADDRESS_LINE_1_US;
			city = TestConstants.CITY_US;
			postalCode = TestConstants.POSTAL_CODE_US;
		}
		else if(country.equalsIgnoreCase("ca")){
			addressLine1 = TestConstants.ADDRESS_LINE_1_CA;
			city = TestConstants.CITY_CA;
			postalCode = TestConstants.POSTAL_CODE_CA;
		} 
		storeFrontHomePage = new StoreFrontHomePage(driver);
		storeFrontUpdateCartPage = new StoreFrontUpdateCartPage(driver);
		while(true){
			randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_CONSULTANT_WITH_ORDERS_AND_AUTOSHIPS_RFO,countryId),RFO_DB);
			consultantEmailID = (String) getValueFromQueryResult(randomConsultantList, "UserName");  
			accountID = String.valueOf(getValueFromQueryResult(randomConsultantList, "AccountID"));
			logger.info("Account Id of the user is "+accountID);
			storeFrontConsultantPage = storeFrontHomePage.loginAsConsultant(consultantEmailID, password);
			boolean isError = driver.getCurrentUrl().contains("error");
			if(isError){
				logger.info("login error for the user "+consultantEmailID);
				driver.get(driver.getURL());
			}
			else
				break;
		}
		//s_assert.assertTrue(storeFrontConsultantPage.verifyConsultantPage(),"Consultant Page doesn't contain Welcome User Message");
		logger.info("login is successful");

		storeFrontConsultantPage.clickOnWelcomeDropDown();
		storeFrontShippingInfoPage = storeFrontConsultantPage.clickShippingLinkPresentOnWelcomeDropDown();
		s_assert.assertTrue(storeFrontShippingInfoPage.verifyShippingInfoPageIsDisplayed(),"shipping info page has not been displayed");
		storeFrontShippingInfoPage.clickOnEditForFirstAddress();
		String newShippingAdrressName = TestConstants.ADDRESS_NAME+randomNum;
		storeFrontShippingInfoPage.enterNewShippingAddressName(newShippingAdrressName+" "+lastName);
		storeFrontShippingInfoPage.enterNewShippingAddressLine1(addressLine1);
		storeFrontShippingInfoPage.enterNewShippingAddressCity(city);
		storeFrontShippingInfoPage.selectNewShippingAddressState();
		storeFrontShippingInfoPage.enterNewShippingAddressPostalCode(postalCode);
		storeFrontShippingInfoPage.enterNewShippingAddressPhoneNumber(TestConstants.PHONE_NUMBER);
		//storeFrontShippingInfoPage.selectFirstCardNumber();
		//storeFrontShippingInfoPage.enterNewShippingAddressSecurityCode(TestConstants.SECURITY_NUMBER);
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

		storeFrontUpdateCartPage.clickRodanAndFieldsLogo();
		s_assert.assertAll();

	}

	// Hybris Project-2036 :: Version : 1 :: Edit shipping address during checkout 
	@Test
	public void testEditShippingAddressDuringCheckout_2036() throws InterruptedException{
		RFO_DB = driver.getDBNameRFO();
		int randomNum = CommonUtils.getRandomNum(10000, 1000000);
		List<Map<String, Object>> randomConsultantList =  null;
		String consultantEmailID = null;
		String accountID = null;
		if(driver.getCountry().equalsIgnoreCase("us")){
			city = TestConstants.CITY_US;
			addressLine = TestConstants.ADDRESS_LINE_1_US;
			profileName = TestConstants.FIRST_NAME+randomNum;
			phoneNumber = TestConstants.PHONE_NUMBER_US;
			postalCode = TestConstants.POSTAL_CODE_US;
		}else{
			city = TestConstants.CITY_CA;
			addressLine = TestConstants.ADDRESS_LINE_1_CA;
			profileName = TestConstants.FIRST_NAME+randomNum;
			phoneNumber = TestConstants.PHONE_NUMBER_CA;
			postalCode = TestConstants.POSTAL_CODE_CA;
		}
		storeFrontHomePage = new StoreFrontHomePage(driver);
		while(true){
			randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_CONSULTANT_WITH_ORDERS_AND_AUTOSHIPS_RFO,countryId),RFO_DB);
			consultantEmailID = (String) getValueFromQueryResult(randomConsultantList, "UserName");  
			accountID = String.valueOf(getValueFromQueryResult(randomConsultantList, "AccountID"));
			logger.info("Account Id of the user is "+accountID);
			storeFrontConsultantPage = storeFrontHomePage.loginAsConsultant(consultantEmailID, password);
			boolean isLoginError = driver.getCurrentUrl().contains("error");
			if(isLoginError){
				logger.info("Login error for the user "+consultantEmailID);
				driver.get(driver.getURL());
			}
			else
				break;
		}

		logger.info("login is successful");
		storeFrontConsultantPage.clickOnWelcomeDropDown();
		storeFrontShippingInfoPage = storeFrontConsultantPage.clickShippingLinkPresentOnWelcomeDropDown();
		String defaultSelectedShippingName = storeFrontShippingInfoPage.getDefaultSelectedShippingAddress();

		storeFrontHomePage.hoverOnShopLinkAndClickAllProductsLinks();
		storeFrontHomePage.selectProductAndProceedToBuy();
		storeFrontHomePage.clickOnPlaceOrderButton();
		storeFrontUpdateCartPage = new StoreFrontUpdateCartPage(driver);
		storeFrontUpdateCartPage.clickOnConfirmationOK();

		//String userNameBeforeEdit = storeFrontUpdateCartPage.userNameBeforeEdit();
		//String getDefaultSelectedShippingProfile = storeFrontUpdateCartPage.getDefaultShippingProfileName();
		String newShippingProfileName = TestConstants.ADDRESS_NAME_US;
		if(storeFrontUpdateCartPage.isOnlyOneShippingProfilePresentOnAdhocCart()==true) 
			storeFrontUpdateCartPage.addAshippingProfileAndClickOnItsEdit(city, addressLine, newShippingProfileName, phoneNumber, postalCode);
		else
			storeFrontUpdateCartPage.clickOnEditOnNonDefaultAddress();		
		storeFrontUpdateCartPage.enterNewShippingAddressName(profileName+" "+TestConstants.LAST_NAME);
		storeFrontUpdateCartPage.enterNewShippingAddressLine1(addressLine);
		storeFrontUpdateCartPage.enterNewShippingAddressCity(city);
		storeFrontUpdateCartPage.enterNewShippingAddressPhoneNumber(phoneNumber);
		storeFrontUpdateCartPage.enterNewShippingAddressPostalCode(postalCode);
		storeFrontUpdateCartPage.clickOnSaveShippingProfileAfterEdit();
		storeFrontUpdateCartPage.clickOnUseAsEnteredButton();

		// verify that updated address is listed on cart page
		s_assert.assertTrue(storeFrontUpdateCartPage.isShippingAddressPresent(profileName), "New Shipping address NOT added to update cart");
		storeFrontUpdateCartPage.clickOnShippingAddressNextStepBtn();
		storeFrontUpdateCartPage.clickOnBillingNextStepBtn();
		storeFrontUpdateCartPage.clickPlaceOrderBtn();
		s_assert.assertTrue(storeFrontUpdateCartPage.verifyOrderPlacedConfirmationMessage(),"order is not placed successfully");
		s_assert.assertTrue(storeFrontUpdateCartPage.getUpdatedAddressPresentOnOrderConfirmationPage().toLowerCase().contains(profileName.toLowerCase()),"shipping address name expected is"+profileName+"while shippig address coming on order confirmation page is "+storeFrontUpdateCartPage.getUpdatedAddressPresentOnOrderConfirmationPage());

		storeFrontUpdateCartPage.clickOnWelcomeDropDown();
		storeFrontUpdateCartPage.clickShippingLinkPresentOnWelcomeDropDown();
		s_assert.assertTrue(storeFrontShippingInfoPage.verifyShippingInfoPageIsDisplayed(),"shipping info page has not been displayed");
		String addressnameAfterEdit = storeFrontShippingInfoPage.getDefaultSelectedShippingAddress();
		System.out.println("After "+addressnameAfterEdit);
		// assert of default selected shipping address
		s_assert.assertTrue(storeFrontShippingInfoPage.verifyOldDefaultSelectAddress(defaultSelectedShippingName, addressnameAfterEdit), "New Shipping address is not listed on Shipping profile page");
		s_assert.assertAll();

	}

	//Hybris Project-2037 :: Version : 1 :: Edit shipping address in autoship template 
	@Test
	public void testEditShippingAddressInAutoshipTemplate_2037() throws InterruptedException{
		RFO_DB = driver.getDBNameRFO(); 
		List<Map<String, Object>> randomConsultantList =  null;
		String consultantEmailID = null;
		String accountID = null;
		int randomNum = CommonUtils.getRandomNum(10000, 1000000);
		int randomNumber = CommonUtils.getRandomNum(10000, 1000000);
		String lastName = "lN";
		if(driver.getCountry().equalsIgnoreCase("us")){
			city = TestConstants.CITY_US;
			addressLine = TestConstants.ADDRESS_LINE_1_US;
			profileName = TestConstants.NEW_SHIPPING_PROFILE_FIRST_NAME_US+randomNumber;
			phoneNumber = TestConstants.PHONE_NUMBER_US;
			postalCode = TestConstants.POSTAL_CODE_US;
			newCity = TestConstants.NEW_ADDRESS_CITY_US;
			newAddressLine = TestConstants.NEW_ADDRESS_LINE_1_US;
			newPhoneNumber = TestConstants.NEW_ADDRESS_PHONE_NUMBER_US;
			newPostalCode = TestConstants.NEW_ADDRESS_POSTAL_CODE_US;
			newProfileName = TestConstants.NEW_SHIPPING_PROFILE_FIRST_NAME_US+randomNum;

		}else{
			city = TestConstants.CITY_CA;
			addressLine = TestConstants.ADDRESS_LINE_1_CA;
			profileName = TestConstants.NEW_SHIPPING_PROFILE_FIRST_NAME_CA+randomNumber;
			phoneNumber = TestConstants.PHONE_NUMBER_CA;
			postalCode = TestConstants.POSTAL_CODE_CA;
			newCity = TestConstants.NEW_ADDRESS_LINE_1_CITY_CA;
			newAddressLine = TestConstants.NEW_ADDRESS_LINE_1_CA;
			newPhoneNumber = TestConstants.NEW_ADDRESS_LINE_PHONE_NUMBER_CA;
			newPostalCode = TestConstants.POSTAL_CODE_CA;
			newProfileName = TestConstants.NEW_SHIPPING_PROFILE_FIRST_NAME_CA+randomNum;
		}
		storeFrontHomePage = new StoreFrontHomePage(driver);
		while(true){
			randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguementPWS(DBQueries_RFO.GET_RANDOM_CONSULTANT_WITH_PWS_RFO,driver.getEnvironment(),driver.getCountry(),countryId),RFO_DB);
			consultantEmailID = (String) getValueFromQueryResult(randomConsultantList, "UserName");  
			accountID = String.valueOf(getValueFromQueryResult(randomConsultantList, "AccountID"));
			logger.info("Account Id of the user is "+accountID);
			storeFrontConsultantPage = storeFrontHomePage.loginAsConsultant(consultantEmailID, password);
			boolean isLoginError = driver.getCurrentUrl().contains("error");
			if(isLoginError){
				logger.info("Login error for the user "+consultantEmailID);
				driver.get(driver.getURL());
			}
			else
				break;
		}

		logger.info("login is successful");
		storeFrontConsultantPage.clickOnWelcomeDropDown();
		storeFrontShippingInfoPage = storeFrontConsultantPage.clickShippingLinkPresentOnWelcomeDropDown();
		String defaultSelectedShippingName = storeFrontShippingInfoPage.getDefaultSelectedShippingAddress();
		storeFrontHomePage.clickOnWelcomeDropDown();
		storeFrontCartAutoShipPage = storeFrontHomePage.clickEditCrpLinkPresentOnWelcomeDropDown();
		storeFrontUpdateCartPage = storeFrontCartAutoShipPage.clickUpdateMoreInfoLink();

		storeFrontUpdateCartPage.clickOnEditShipping();

		if(storeFrontUpdateCartPage.isOnlyOneShippingProfilePresentOnAdhocCart()==true) 
			storeFrontUpdateCartPage.addAshippingProfileAndClickOnItsEdit(city, addressLine, profileName, phoneNumber, postalCode);
		else
			storeFrontUpdateCartPage.clickOnEditOnNonDefaultAddress();

		//String defaultSelectedShippingAddress = storeFrontUpdateCartPage.getDefaultSelectedShippingAddressNameFromCartPage();
		//storeFrontUpdateCartPage.addAshippingProfile(city,addressLine,profileName,phoneNumber,postalCode);
		//storeFrontUpdateCartPage.clickOnEditOnNotDefaultAddress();
		//Edit the default shipping Address
		storeFrontShippingInfoPage = new StoreFrontShippingInfoPage(driver);
		storeFrontShippingInfoPage.enterNewShippingAddressName(newProfileName+" "+lastName);
		storeFrontShippingInfoPage.enterNewShippingAddressLine1(newAddressLine);
		storeFrontShippingInfoPage.enterNewShippingAddressCity(newCity);
		storeFrontShippingInfoPage.enterNewShippingAddressPhoneNumber(newPhoneNumber);
		storeFrontShippingInfoPage.enterNewShippingAddressPostalCode(newPostalCode);
		storeFrontUpdateCartPage.clickOnSaveShippingProfileOnUpdateCrpPage();
		storeFrontUpdateCartPage.clickOnUseAsEnteredButton();
		s_assert.assertTrue(storeFrontUpdateCartPage.isShippingAddressPresent(newProfileName),"Edited Shipping Address is not on cart page");
		s_assert.assertTrue(storeFrontUpdateCartPage.verifyNewlyEditedShippingAddressIsSelected(newProfileName),"newly edited shipping address is not choosen by default");
		storeFrontUpdateCartPage.clickOnUpdateCartShippingNextStepBtn();
		storeFrontUpdateCartPage.clickOnNextStepBtn();
		storeFrontUpdateCartPage.clickUpdateCartBtn();
		//s_assert.assertTrue(storeFrontUpdateCartPage.verifyUpdatedAddressPresentUpdateCartPg(profileNameSecond),"shipping address not updated in update crp cart page");
		s_assert.assertTrue(storeFrontUpdateCartPage.validateCartUpdated(),"Cart has not Been Updated");
		storeFrontUpdateCartPage.clickOnRodanAndFieldsLogo();
		storeFrontUpdateCartPage.clickOnWelcomeDropDown();
		storeFrontShippingInfoPage=storeFrontUpdateCartPage.clickShippingLinkPresentOnWelcomeDropDown();
		s_assert.assertTrue(storeFrontShippingInfoPage.isShippingAddressPresentOnShippingPage(newProfileName),"Edited Shipping Address is not on Shipping Info Page");
		s_assert.assertFalse(storeFrontShippingInfoPage.isDefaultAddressRadioBtnSelected(newProfileName),"Edited Shipping Address Radio button is Selected on Shipping Info Page");  
		s_assert.assertTrue(storeFrontUpdateCartPage.getDefaultSelectedShippingAddressNameFromShippingInfoPage().contains(defaultSelectedShippingName),"Default selected shipping address is not the expected");
		s_assert.assertAll();
	}

	// Hybris Project-2038 :: Version : 1 :: Edit shipping address during PC user or Retail user registration  
	@Test
	public void testEditShippingAddressDuringPCEnrollment_2038() throws InterruptedException{
		int randomNum = CommonUtils.getRandomNum(10000, 1000000);  
		String newBillingProfileName = TestConstants.NEW_BILLING_PROFILE_NAME+randomNum;
		String lastName = "lN";
		country = driver.getCountry();
		int randomNumber = CommonUtils.getRandomNum(10000, 1000000);
		String firstName=TestConstants.FIRST_NAME+randomNum;
		String emailAddress = firstName+"@xyz.com";
		String newShippingAddressName = TestConstants.FIRST_NAME+randomNumber;

		storeFrontHomePage = new StoreFrontHomePage(driver);
		storeFrontHomePage.hoverOnShopLinkAndClickAllProductsLinks();

		// Products are displayed?
		s_assert.assertTrue(storeFrontHomePage.areProductsDisplayed(), "quickshop products not displayed");
		logger.info("Quick shop products are displayed");

		//Select a product and proceed to buy it
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
		storeFrontPCUserPage = new StoreFrontPCUserPage(driver);
		storeFrontPCUserPage.clickOnWelcomeDropDown();
		storeFrontShippingInfoPage=storeFrontPCUserPage.clickShippingLinkPresentOnWelcomeDropDown();
		s_assert.assertTrue(storeFrontShippingInfoPage.isDefaultAddressRadioBtnSelected(newShippingAddressName),"Default adddress is not the edited address");
		s_assert.assertTrue(storeFrontShippingInfoPage.isAutoshipOrderAddressTextPresent(newShippingAddressName),"Default selected shipping address does not have autoship text");
		s_assert.assertAll();
	}

	//Hybris Project-2039 :: Version : 1 :: Edit shipping address during consultant enrollment 
	@Test
	public void testEditShippingAddressDuringConsultantEnrollment_2039() throws InterruptedException{
		int randomNum = CommonUtils.getRandomNum(10000, 1000000);
		int randomNumber = CommonUtils.getRandomNum(10000, 1000000);
		String socialInsuranceNumber = String.valueOf(CommonUtils.getRandomNum(100000000, 999999999));
		country = driver.getCountry();
		enrollmentType = TestConstants.EXPRESS_ENROLLMENT;
		regimenName = TestConstants.REGIMEN_NAME_UNBLEMISH;
		env = driver.getEnvironment();		
		storeFrontHomePage = new StoreFrontHomePage(driver);
		storeFrontHomePage.openPWSSite(country, env);
		String sRandName = RandomStringUtils.randomAlphabetic(12);
		String newShippingAddressName = TestConstants.FIRST_NAME+randomNumber;
		if(country.equalsIgnoreCase("CA")){

			kitName = TestConstants.KIT_NAME_BIG_BUSINESS;			 
			addressLine1 = TestConstants.ADDRESS_LINE_1_CA;
			city = TestConstants.CITY_CA;
			postalCode = TestConstants.CONSULTANT_POSTAL_CODE_CA;
			phoneNumber = TestConstants.PHONE_NUMBER_CA;
		}else{

			kitName = TestConstants.KIT_NAME_BIG_BUSINESS;
			addressLine1 = TestConstants.NEW_ADDRESS_LINE1_US;
			city = TestConstants.NEW_ADDRESS_CITY_US;
			postalCode = TestConstants.NEW_ADDRESS_POSTAL_CODE_US;
			phoneNumber = TestConstants.NEW_ADDRESS_PHONE_NUMBER_US;
		}

		storeFrontHomePage.hoverOnBecomeAConsultantAndClickEnrollNowLink();		
		storeFrontHomePage.enterUserInformationForEnrollment(kitName, regimenName, enrollmentType, TestConstants.FIRST_NAME+randomNum, sRandName, TestConstants.PASSWORD, addressLine1, city, postalCode, phoneNumber);
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
		storeFrontConsultantPage = new StoreFrontConsultantPage(driver);
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
	@Test
	public void testEditShippingAddressDuringCRPEnrollment_2040() throws InterruptedException{
		int randomNum = CommonUtils.getRandomNum(10000, 1000000);
		int randomNumber = CommonUtils.getRandomNum(10000, 1000000);
		String socialInsuranceNumber = String.valueOf(CommonUtils.getRandomNum(100000000, 999999999));
		country = driver.getCountry();
		enrollmentType = TestConstants.STANDARD_ENROLLMENT;
		regimenName = TestConstants.REGIMEN_NAME_UNBLEMISH;
		String firstName = TestConstants.FIRST_NAME+randomNum;
		String lastName = "lN";
		String consultantEmail = firstName+TestConstants.EMAIL_ADDRESS_SUFFIX;
		String newShippingAddressName = TestConstants.FIRST_NAME+randomNumber;
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
		//Enroll a consultant without CRP and pulse
		storeFrontHomePage = new StoreFrontHomePage(driver);
		storeFrontHomePage.hoverOnBecomeAConsultantAndClickEnrollNowLink();
		storeFrontHomePage.searchCID();
		storeFrontHomePage.mouseHoverSponsorDataAndClickContinue();
		storeFrontHomePage.enterUserInformationForEnrollmentWithEmail(kitName, regimenName, enrollmentType, firstName, TestConstants.LAST_NAME,consultantEmail, password, addressLine1, city, postalCode, phoneNumber);
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
		storeFrontConsultantPage=new StoreFrontConsultantPage(driver);
		storeFrontConsultantPage.clickOnWelcomeDropDown();
		storeFrontAccountInfoPage = storeFrontConsultantPage.clickAccountInfoLinkPresentOnWelcomeDropDown();
		s_assert.assertTrue(storeFrontAccountInfoPage.verifyAccountInfoPageIsDisplayed(),"shipping info page has not been displayed");
		storeFrontAccountInfoPage.clickOnYourAccountDropdown();//added
		storeFrontOrdersAutoshipStatusPage=storeFrontAccountInfoPage.clickOnAutoShipStatus();
		storeFrontOrdersAutoshipStatusPage.clickOnEnrollInCRP();
		storeFrontOrdersAutoshipStatusPage.clickAddToCRPButton(driver.getCountry());
		storeFrontOrdersAutoshipStatusPage.clickOnCRPCheckout();
		storeFrontOrdersAutoshipStatusPage.clickOnEditShipping();
		storeFrontUpdateCartPage=new StoreFrontUpdateCartPage(driver);
		storeFrontUpdateCartPage.enterNewShippingAddressName(newShippingAddressName+" "+lastName);
		storeFrontUpdateCartPage.clickOnSaveShippingProfile();
		s_assert.assertTrue(storeFrontUpdateCartPage.verifyUpdatedShippingAddress(newShippingAddressName),"Updated shipping address is not present");
		storeFrontHomePage.clickOnUpdateCartShippingNextStepBtnDuringEnrollment();
		storeFrontHomePage.clickOnBillingNextStepBtn();
		storeFrontHomePage.clickOnSetupCRPAccountBtn();
		s_assert.assertTrue(storeFrontHomePage.verifyOrderConfirmation(), "Order Confirmation Message has not been displayed");
		storeFrontHomePage.clickOnRodanAndFieldsLogo();
		//verify the default shipping address is new shipping address
		storeFrontConsultantPage = new StoreFrontConsultantPage(driver);
		storeFrontConsultantPage.clickOnWelcomeDropDown();
		storeFrontShippingInfoPage=storeFrontConsultantPage.clickShippingLinkPresentOnWelcomeDropDown();
		s_assert.assertTrue(storeFrontShippingInfoPage.isDefaultAddressRadioBtnSelected(newShippingAddressName),"Default adddress is not the edited address");
		s_assert.assertTrue(storeFrontShippingInfoPage.isAutoshipOrderAddressTextPresent(newShippingAddressName),"Default selected shipping address does not have autoship text");
		s_assert.assertAll();
	}

}


