package com.rf.test.website.storeFront.hybris.radiantDataMigration;

import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.annotations.Test;

import com.rf.core.utils.CommonUtils;
import com.rf.core.website.constants.TestConstants;
import com.rf.pages.website.storeFront.StoreFrontAccountInfoPage;
import com.rf.pages.website.storeFront.StoreFrontBillingInfoPage;
import com.rf.pages.website.storeFront.StoreFrontCartAutoShipPage;
import com.rf.pages.website.storeFront.StoreFrontConsultantPage;
import com.rf.pages.website.storeFront.StoreFrontHomePage;
import com.rf.pages.website.storeFront.StoreFrontShippingInfoPage;
import com.rf.pages.website.storeFront.StoreFrontUpdateCartPage;
import com.rf.test.website.RFWebsiteBaseTest;
import com.rf.test.website.storeFront.hybris.OrderValidationTest;

public class MigratedConsultantTests extends RFWebsiteBaseTest{

	private static final Logger logger = LogManager
			.getLogger(OrderValidationTest.class.getName());
	public String emailID=null;
	private StoreFrontHomePage storeFrontHomePage;
	private StoreFrontConsultantPage storeFrontConsultantPage;
	private StoreFrontAccountInfoPage storeFrontAccountInfoPage;
	private StoreFrontShippingInfoPage storeFrontShippingInfoPage;
	private StoreFrontCartAutoShipPage storeFrontCartAutoShipPage;
	private StoreFrontUpdateCartPage storeFrontUpdateCartPage;
	private StoreFrontBillingInfoPage storeFrontBillingInfoPage;
	private String city = null;
	private String phoneNumber = null;
	private String postalCode = null;
	private String addressLine1 = null;
	private String state = null;
	private int randomNum; 	
	String country=null;
	String env = null;
	List<Map<String, Object>> randomConsultantList =  null;
	String consultantEmailID = "abced943001xyz.com";
	String accountID = null;
	String countryID=null;

	/*Placed adhoc order with Add Edit Shipping and Billing profile during checkout*/
	@Test
	public void testAddAndEditShippingAddressBillingProfileDuringCheckout() throws InterruptedException{
		randomNum = CommonUtils.getRandomNum(10000, 1000000);
		String lastName = "lN";
		addressLine1 = TestConstants.ADDRESS_LINE_1_CA;
		city = TestConstants.CITY_CA;
		postalCode = TestConstants.POSTAL_CODE_CA;
		phoneNumber = TestConstants.PHONE_NUMBER_CA;
		state = TestConstants.PROVINCE_CA;
		countryId = "40";
		storeFrontHomePage = new StoreFrontHomePage(driver);
		storeFrontConsultantPage = storeFrontHomePage.loginAsConsultant(consultantEmailID, password);
		//Continue with checkout page
		storeFrontConsultantPage.hoverOnShopLinkAndClickAllProductsLinksAfterLogin();
		storeFrontUpdateCartPage = new StoreFrontUpdateCartPage(driver);
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
		s_assert.assertTrue(storeFrontUpdateCartPage.verifyNewShippingAddressIsSelectedByDefaultOnAdhocCart(newShippingAddressName), "Newly added Address has not got associated with the billing profile");

		//Edit shipping address
		int randomNum1 = CommonUtils.getRandomNum(10000, 1000000);
		String editShippingAddressName = TestConstants.ADDRESS_NAME+randomNum1;
		storeFrontUpdateCartPage.clickOnEditOfShippingProfile(newShippingAddressName);	
		storeFrontUpdateCartPage.enterNewShippingAddressName(editShippingAddressName+" "+TestConstants.LAST_NAME);
		storeFrontUpdateCartPage.enterNewShippingAddressLine1(addressLine1);
		storeFrontUpdateCartPage.enterNewShippingAddressCity(city);
		storeFrontUpdateCartPage.enterNewShippingAddressPhoneNumber(phoneNumber);
		storeFrontUpdateCartPage.enterNewShippingAddressPostalCode(postalCode);
		storeFrontUpdateCartPage.clickOnSaveShippingProfileAfterEdit();
		// verify that updated address is listed on cart page
		s_assert.assertTrue(storeFrontUpdateCartPage.isShippingAddressPresent(editShippingAddressName), "Edited Shipping address NOT added to update cart");
		storeFrontUpdateCartPage.clickOnShippingAddressNextStepBtn();

		//Add new Billing profile
		randomNum = CommonUtils.getRandomNum(10000, 1000000);
		String newBillingProfileName = TestConstants.NEW_BILLING_PROFILE_NAME_US+randomNum;
		storeFrontUpdateCartPage.clickAddNewBillingProfileLink();
		storeFrontUpdateCartPage.enterNewBillingCardNumber(TestConstants.CARD_NUMBER);
		storeFrontUpdateCartPage.enterNewBillingNameOnCard(newBillingProfileName+" "+lastName);
		storeFrontUpdateCartPage.selectNewBillingCardExpirationDate("OCT","2025");
		storeFrontUpdateCartPage.enterNewBillingSecurityCode(TestConstants.SECURITY_CODE);
		storeFrontUpdateCartPage.selectNewBillingCardAddress();
		storeFrontUpdateCartPage.clickOnSaveBillingProfile();
		s_assert.assertTrue(storeFrontHomePage.isTheBillingAddressPresentOnPage(newBillingProfileName),"Newly edited Billing profile is NOT listed on Adhoc cart while add the billing profile");
		s_assert.assertTrue(storeFrontHomePage.isBillingProfileIsSelectedByDefault(newBillingProfileName),"Already Default billing profile is not DEFAULT selected on adhoc cart while add the billing profile");

		// Edit the billing profile
		randomNum1 = CommonUtils.getRandomNum(10000, 1000000);
		String editedBillingProfileName = TestConstants.NEW_BILLING_PROFILE_NAME+randomNum1;
		storeFrontUpdateCartPage.clickOnEditDefaultBillingProfile();
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
		storeFrontUpdateCartPage.clickPlaceOrderBtn();
		s_assert.assertTrue(storeFrontUpdateCartPage.verifyOrderPlacedConfirmationMessage(),"order is not placed successfully");
		s_assert.assertAll();
	}

	// Add, Edit, Delete shipping profile at ShippingInfo page
	@Test
	public void testAddEditDeleteShippingAddressOnShippingProfilePage() throws InterruptedException{
		randomNum = CommonUtils.getRandomNum(10000, 1000000);
		String lastName = "lN";
		addressLine1 = TestConstants.ADDRESS_LINE_1_CA;
		city = TestConstants.CITY_CA;
		postalCode = TestConstants.POSTAL_CODE_CA;
		phoneNumber = TestConstants.PHONE_NUMBER_CA;
		state = TestConstants.PROVINCE_CA;
		countryId = "40";
		storeFrontHomePage = new StoreFrontHomePage(driver);
		storeFrontConsultantPage = storeFrontHomePage.loginAsConsultant(consultantEmailID, password);
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
		storeFrontShippingInfoPage.clickOnSaveShippingProfile();
		//--------------- Verify that Newly added Shipping is listed in the Shipping profiles section-----------------------------------------------------------------------------------------------------
		s_assert.assertTrue(storeFrontShippingInfoPage.isShippingAddressPresentOnShippingPage(newShippingAddressName), "Newly Added Shipping address is not listed on Shipping profile page");

		// Added 2nd new billing profile
		randomNum = CommonUtils.getRandomNum(10000, 1000000);
		storeFrontShippingInfoPage.clickAddNewShippingProfileLink();
		newShippingAddressName = TestConstants.ADDRESS_NAME+randomNum;
		storeFrontShippingInfoPage.enterNewShippingAddressName(newShippingAddressName+" "+lastName);
		storeFrontShippingInfoPage.enterNewShippingAddressLine1(addressLine1);
		storeFrontShippingInfoPage.enterNewShippingAddressCity(city);
		storeFrontShippingInfoPage.selectNewShippingAddressState(state);
		storeFrontShippingInfoPage.enterNewShippingAddressPostalCode(postalCode);
		storeFrontShippingInfoPage.enterNewShippingAddressPhoneNumber(TestConstants.PHONE_NUMBER);
		storeFrontShippingInfoPage.clickOnSaveShippingProfile();
		//--------------- Verify that Newly added Shipping is listed in the Shipping profiles section-----------------------------------------------------------------------------------------------------
		s_assert.assertTrue(storeFrontShippingInfoPage.isShippingAddressPresentOnShippingPage(newShippingAddressName), "2nd Newly Added Shipping address is not listed on Shipping profile page");

		// Edit the shipping address
		int randomNum1 = CommonUtils.getRandomNum(10000, 1000000);
		String editShippingAddressName = TestConstants.ADDRESS_NAME+randomNum1;
		storeFrontShippingInfoPage.clickOnEditOfShippingProfile(newShippingAddressName);
		storeFrontShippingInfoPage.enterNewShippingAddressName(editShippingAddressName+" "+lastName);
		storeFrontShippingInfoPage.enterNewShippingAddressLine1(addressLine1);
		storeFrontShippingInfoPage.enterNewShippingAddressCity(city);
		storeFrontShippingInfoPage.selectNewShippingAddressState(state);
		storeFrontShippingInfoPage.enterNewShippingAddressPostalCode(postalCode);
		storeFrontShippingInfoPage.enterNewShippingAddressPhoneNumber(TestConstants.PHONE_NUMBER);
		storeFrontShippingInfoPage.clickOnSaveShippingProfile();

		//--------------- Verify that Newly added Shipping is listed in the Billing profiles section-----------------------------------------------------------------------------------------------------
		s_assert.assertTrue(storeFrontShippingInfoPage.isShippingAddressPresentOnShippingPage(editShippingAddressName), "Newly Edited Shipping address is not selected listed on Shipping profile page");
		//--------------- Verify that Newly added Shipping is deleted in the shipping profiles section-----------------------------------------------------------------------------------------------------
		storeFrontShippingInfoPage.deleteShippingAddress(editShippingAddressName);
		s_assert.assertFalse(storeFrontShippingInfoPage.isShippingAddressPresentOnShippingPage(editShippingAddressName), "Newly Edited Shipping address is listed on Shipping profile page after delete");
		s_assert.assertAll();		
	}

	// Update My AccountInfo
	@Test
	public void testAccountInformationUpdateForConsultant() throws InterruptedException{
		int randomNumPhone = CommonUtils.getRandomNum(10000, 99999);
		int randomNum = CommonUtils.getRandomNum(10000, 1000000);
		String city = null;
		String stateName = null;
		String postalCode = null;
		String phoneNumber = null;
		String addressLine1 = null;
		country = driver.getCountry();
		city = TestConstants.CONSULTANT_CITY_FOR_ACCOUNT_INFORMATION_CA;
		postalCode = TestConstants.CONSULTANT_POSTAL_CODE_FOR_ACCOUNT_INFORMATION_CA;
		phoneNumber = "99999"+randomNumPhone;
		addressLine1 =  TestConstants.CONSULTANT_ADDRESS_LINE1_FOR_ACCOUNT_INFORMATION_CA;
		stateName = TestConstants.CONSULTANT_STATE_FOR_ACCOUNT_INFORMATION_CA;
		storeFrontHomePage = new StoreFrontHomePage(driver);
		storeFrontConsultantPage = storeFrontHomePage.loginAsConsultant(consultantEmailID, password);
		logger.info("login is successful");
		storeFrontConsultantPage.clickOnWelcomeDropDown();
		storeFrontAccountInfoPage = storeFrontConsultantPage.clickAccountInfoLinkPresentOnWelcomeDropDown();
		String firstName = TestConstants.CONSULTANT_FIRST_NAME_FOR_ACCOUNT_INFORMATION+randomNum;
		String lastName = TestConstants.CONSULTANT_LAST_NAME_FOR_ACCOUNT_INFORMATION+randomNum;
		storeFrontAccountInfoPage.updateFirstName(firstName);
		storeFrontAccountInfoPage.updateLastName(lastName);
		storeFrontAccountInfoPage.updateAddressWithCityAndPostalCode(addressLine1, city, postalCode);
		String state = storeFrontAccountInfoPage.updateStateAndReturnName(stateName);
		logger.info("State/province selected is "+state);
		storeFrontAccountInfoPage.updateMainPhnNumber(phoneNumber);
		storeFrontAccountInfoPage.clickSaveAccountBtn();
		s_assert.assertTrue(storeFrontAccountInfoPage.isProfileHasUpdatedMessagePresent(), "'Your profile has been updated' message has not appeared after saving the account info");
		s_assert.assertTrue(storeFrontAccountInfoPage.verifyFirstNameFromUIForAccountInfo(firstName), "First Name on UI is not updated");
		s_assert.assertTrue(storeFrontAccountInfoPage.verifyLasttNameFromUIForAccountInfo(lastName), "Last Name on UI is not updated");
		s_assert.assertTrue(storeFrontAccountInfoPage.verifyCityFromUIForAccountInfo(city), "City on UI is not updated");
		s_assert.assertTrue(storeFrontAccountInfoPage.verifyProvinceFromUIForAccountInfo(state), "State/Province on UI is not updated");
		s_assert.assertTrue(storeFrontAccountInfoPage.verifyPostalCodeFromUIForAccountInfo(postalCode), "Postal Code on UI is not updated");
		s_assert.assertTrue(storeFrontAccountInfoPage.verifyMainPhoneNumberFromUIForAccountInfo(phoneNumber), "Phone Number on UI is not updated");
		s_assert.assertAll();
	}


	// Add, Edit, Delete billing profile on 'Billing Profile' page
	@Test
	public void testAddNewBillingProfileOnBillingProfilePage_2041() throws InterruptedException{
		int randomNum = CommonUtils.getRandomNum(10000, 1000000);
		int randomNumber = CommonUtils.getRandomNum(10000, 1000000);
		String newBillingProfileName = TestConstants.NEW_BILLING_PROFILE_NAME+randomNum;
		String newEditedBillingProfileName = TestConstants.NEW_BILLING_PROFILE_NAME+randomNumber;
		String lastName = "lN";
		storeFrontHomePage = new StoreFrontHomePage(driver);
		storeFrontConsultantPage = storeFrontHomePage.loginAsConsultant(consultantEmailID, password);
		storeFrontConsultantPage.clickOnWelcomeDropDown();
		storeFrontBillingInfoPage = storeFrontConsultantPage.clickBillingInfoLinkPresentOnWelcomeDropDown();
		s_assert.assertTrue(storeFrontBillingInfoPage.verifyBillingInfoPageIsDisplayed(),"Billing Info page has not been displayed");
		//Add a new billing profile on billing info page.
		storeFrontBillingInfoPage.clickAddNewBillingProfileLink();
		storeFrontBillingInfoPage.enterNewBillingCardNumber(TestConstants.CARD_NUMBER);
		storeFrontBillingInfoPage.enterNewBillingNameOnCard(newBillingProfileName+" "+lastName);
		storeFrontBillingInfoPage.selectNewBillingCardExpirationDate(TestConstants.CARD_EXP_MONTH, TestConstants.CARD_EXP_YEAR);
		storeFrontBillingInfoPage.enterNewBillingSecurityCode(TestConstants.SECURITY_CODE);
		storeFrontBillingInfoPage.selectNewBillingCardAddress();
		storeFrontBillingInfoPage.clickOnSaveBillingProfile();
		//--------------- Verify that Newly added Billing profile is listed in the Billing profiles section-----------------------------------------------------------------------------------------------------
		s_assert.assertTrue(storeFrontBillingInfoPage.isTheBillingAddressPresentOnPage(newBillingProfileName),"Newly added Billing profile is NOT listed on the page");
		//Adding 2nd billing profile
		randomNum = CommonUtils.getRandomNum(10000, 1000000);
		newBillingProfileName = TestConstants.NEW_BILLING_PROFILE_NAME+randomNum;
		storeFrontBillingInfoPage.clickAddNewBillingProfileLink();
		storeFrontBillingInfoPage.enterNewBillingCardNumber(TestConstants.CARD_NUMBER);
		storeFrontBillingInfoPage.enterNewBillingNameOnCard(newBillingProfileName+" "+lastName);
		storeFrontBillingInfoPage.selectNewBillingCardExpirationDate(TestConstants.CARD_EXP_MONTH, TestConstants.CARD_EXP_YEAR);
		storeFrontBillingInfoPage.enterNewBillingSecurityCode(TestConstants.SECURITY_CODE);
		storeFrontBillingInfoPage.selectNewBillingCardAddress();
		storeFrontBillingInfoPage.clickOnSaveBillingProfile();
		//--------------- Verify that Newly added Billing profile is listed in the Billing profiles section-----------------------------------------------------------------------------------------------------
		s_assert.assertTrue(storeFrontBillingInfoPage.isTheBillingAddressPresentOnPage(newBillingProfileName),"2nd Newly added Billing profile is NOT listed on the page");
		//Edit newly created billing profile.
		storeFrontBillingInfoPage.clickOnEditOfBillingProfile(newBillingProfileName);
		storeFrontBillingInfoPage.enterNewBillingNameOnCard(newEditedBillingProfileName+" "+lastName);
		storeFrontBillingInfoPage.enterNewBillingSecurityCode(TestConstants.SECURITY_CODE);
		storeFrontBillingInfoPage.clickOnSaveBillingProfile();
		s_assert.assertTrue(storeFrontBillingInfoPage.isTheBillingAddressPresentOnPage(newEditedBillingProfileName),"Edited Billing profile is NOT listed on the page");
		//Delete newly created billing profile.
		storeFrontBillingInfoPage.clickOnDeleteOfBillingProfile(newEditedBillingProfileName);
		s_assert.assertFalse(storeFrontBillingInfoPage.isTheBillingAddressPresentOnPage(newEditedBillingProfileName),"Edited Billing profile is NOT listed on the page");
		s_assert.assertAll();  
	}

	/*Placed autoship order with Add Edit Shipping and Billing profile during checkout*/
	@Test
	public void testAddAndEditShippingAddressBillingProfileDuringAutoshipCheckout() throws InterruptedException{
		randomNum = CommonUtils.getRandomNum(10000, 1000000);
		String lastName = "lN";
		addressLine1 = TestConstants.ADDRESS_LINE_1_CA;
		city = TestConstants.CITY_CA;
		postalCode = TestConstants.POSTAL_CODE_CA;
		phoneNumber = TestConstants.PHONE_NUMBER_CA;
		state = TestConstants.PROVINCE_CA;
		countryId = "40";
		storeFrontHomePage = new StoreFrontHomePage(driver);
		storeFrontConsultantPage = storeFrontHomePage.loginAsConsultant(consultantEmailID, password);

		//Continue with checkout page
		storeFrontConsultantPage.hoverOnShopLinkAndClickAllProductsLinksAfterLogin();
		storeFrontConsultantPage.clickOnAddToCRPButtonAfterCancelMyCRP();
		storeFrontCartAutoShipPage = new StoreFrontCartAutoShipPage(driver);
		storeFrontUpdateCartPage = storeFrontCartAutoShipPage.clickUpdateMoreInfoLink();
		storeFrontUpdateCartPage.clickOnEditShipping();
		//Adding new shipping profile 
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

		s_assert.assertTrue(storeFrontUpdateCartPage.isShippingAddressPresent(newShippingAddressName), "Newly added Shipping address NOT selected in update cart under shipping section");
		s_assert.assertTrue(storeFrontUpdateCartPage.isNewlyCreatedShippingProfileIsSelectedByDefault(newShippingAddressName), "Newly added Shipping address NOT selected as default in update cart under shipping section");

		//-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

		//Edit shipping address
		int randomNum1 = CommonUtils.getRandomNum(10000, 1000000);
		String editShippingAddressName = TestConstants.ADDRESS_NAME+randomNum1;
		storeFrontUpdateCartPage.clickOnEditOfShippingProfile(newShippingAddressName);	
		storeFrontUpdateCartPage.enterNewShippingAddressName(editShippingAddressName+" "+TestConstants.LAST_NAME);
		storeFrontUpdateCartPage.enterNewShippingAddressLine1(addressLine1);
		storeFrontUpdateCartPage.enterNewShippingAddressCity(city);
		storeFrontUpdateCartPage.enterNewShippingAddressPhoneNumber(phoneNumber);
		storeFrontUpdateCartPage.enterNewShippingAddressPostalCode(postalCode);
		storeFrontUpdateCartPage.clickOnSaveShippingProfile();
		storeFrontUpdateCartPage.clickOnUpdateCartShippingNextStepBtn();
		storeFrontUpdateCartPage.clickOnEditShipping();

		s_assert.assertTrue(storeFrontUpdateCartPage.isShippingAddressPresent(editShippingAddressName),"Edited Shipping Address is not on cart page");
		s_assert.assertTrue(storeFrontUpdateCartPage.verifyNewlyEditedShippingAddressIsSelected(editShippingAddressName),"newly edited shipping address is not choosen by default");

		// Adding a new billing profile
		randomNum = CommonUtils.getRandomNum(10000, 1000000);
		String newBillingProfileName = TestConstants.NEW_BILLING_PROFILE_NAME_US+randomNum;
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
		//Edit the newly added billing profile
		randomNum1 = CommonUtils.getRandomNum(10000, 1000000);
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
		storeFrontUpdateCartPage.clickOnUpdateCartShippingNextStepBtn();
		storeFrontUpdateCartPage.clickOnNextStepBtn();
		storeFrontUpdateCartPage.clickUpdateCartBtn();
		//validate cart has been updated?
		s_assert.assertTrue(storeFrontUpdateCartPage.validateCartUpdated(), "cart is not updated!! ");
	}


}
