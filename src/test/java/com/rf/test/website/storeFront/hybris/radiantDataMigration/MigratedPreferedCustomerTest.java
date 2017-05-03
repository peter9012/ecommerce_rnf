package com.rf.test.website.storeFront.hybris.radiantDataMigration;

import java.sql.SQLException;
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
import com.rf.pages.website.storeFront.StoreFrontHomePage;
import com.rf.pages.website.storeFront.StoreFrontPCUserPage;
import com.rf.pages.website.storeFront.StoreFrontRCUserPage;
import com.rf.pages.website.storeFront.StoreFrontShippingInfoPage;
import com.rf.pages.website.storeFront.StoreFrontUpdateCartPage;
import com.rf.test.website.RFWebsiteBaseTest;

public class MigratedPreferedCustomerTest extends RFWebsiteBaseTest{
	private static final Logger logger = LogManager
			.getLogger(MigratedPreferedCustomerTest.class.getName());
	public String emailID = null;
	private StoreFrontHomePage storeFrontHomePage;
	private StoreFrontAccountInfoPage storeFrontAccountInfoPage;
	private StoreFrontPCUserPage storeFrontPCUserPage;
	private StoreFrontBillingInfoPage storeFrontBillingInfoPage;
	private StoreFrontUpdateCartPage storeFrontUpdateCartPage;
	private StoreFrontShippingInfoPage storeFrontShippingInfoPage;
	private StoreFrontCartAutoShipPage storeFrontCartAutoShipPage;
	private String addressLine1 = null;
	private String city = null;
	private String state = null;
	private String postalCode = null;
	private String phoneNumber = null;
	private String country = null;
	public String pcUserEmailID = "daniel.kenney@dlapiper.com";
	public String accountId = "6935172";

	//Placed adhoc order with Add Edit Shipping and Billing profile during checkout
	@Test
	public void testAddAndEditShippingAddressBillingProfileDuringCheckout() throws InterruptedException{
		int randomNum = CommonUtils.getRandomNum(10000, 1000000);
		int randomNum1 = CommonUtils.getRandomNum(10000, 1000000);
		String lastName = "lN";
		addressLine1 = TestConstants.ADDRESS_LINE_1_CA;
		city = TestConstants.CITY_CA;
		postalCode = TestConstants.POSTAL_CODE_CA;
		phoneNumber = TestConstants.PHONE_NUMBER_CA;
		state = TestConstants.PROVINCE_CA;
		String shippingAddressName = TestConstants.ADDRESS_NAME+randomNum;
		String newBillingProfileName = TestConstants.NEW_BILLING_PROFILE_NAME_US+randomNum;
		String editShippingAddressName = TestConstants.ADDRESS_NAME+randomNum1;
		String editedBillingProfileName = TestConstants.NEW_BILLING_PROFILE_NAME+randomNum1;
		
		storeFrontHomePage = new StoreFrontHomePage(driver);
		storeFrontPCUserPage = storeFrontHomePage.loginAsPCUser(pcUserEmailID, password);
		//Continue with checkout page
		storeFrontPCUserPage.hoverOnShopLinkAndClickAllProductsLinksAfterLogin();
		storeFrontUpdateCartPage = new StoreFrontUpdateCartPage(driver);
		storeFrontUpdateCartPage.clickAddToBagButton(country);
		storeFrontUpdateCartPage.clickOnCheckoutButton();
		//Add a new shipping profile.
		storeFrontUpdateCartPage.clickOnAddANewShippingAddress();
		storeFrontUpdateCartPage.enterNewShippingAddressName(shippingAddressName+" "+lastName);
		storeFrontUpdateCartPage.enterNewShippingAddressLine1(addressLine1);
		storeFrontUpdateCartPage.enterNewShippingAddressCity(city);
		storeFrontUpdateCartPage.selectNewShippingAddressStateOnCartPage();
		storeFrontUpdateCartPage.enterNewShippingAddressPostalCode(postalCode);
		storeFrontUpdateCartPage.enterNewShippingAddressPhoneNumber(TestConstants.PHONE_NUMBER);
		storeFrontUpdateCartPage.clickOnSaveShippingProfileAfterEdit();
		s_assert.assertTrue(storeFrontUpdateCartPage.verifyNewShippingAddressIsSelectedByDefaultOnAdhocCart(shippingAddressName), "Newly added Address has not got associated with the billing profile");
        //Edit shipping address
		storeFrontUpdateCartPage.clickOnEditOfShippingProfile(shippingAddressName);	
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
		storeFrontUpdateCartPage.clickOnEditDefaultBillingProfile();
		storeFrontUpdateCartPage.enterNewBillingCardNumber(TestConstants.CARD_NUMBER);
		storeFrontUpdateCartPage.enterNewBillingNameOnCard(editedBillingProfileName+" "+lastName);
		storeFrontUpdateCartPage.selectNewBillingCardExpirationDate("OCT","2025");
		storeFrontUpdateCartPage.enterNewBillingSecurityCode(TestConstants.SECURITY_CODE);
		storeFrontUpdateCartPage.selectNewBillingCardAddress();
		storeFrontUpdateCartPage.clickOnSaveBillingProfile();
		s_assert.assertTrue(storeFrontHomePage.isTheBillingAddressPresentOnPage(editedBillingProfileName),"Newly edited Billing profile is NOT listed on Adhoc cart while edit the billing profile");
		s_assert.assertTrue(storeFrontHomePage.isBillingProfileIsSelectedByDefault(editedBillingProfileName),"Already Default billing profile is not DEFAULT selected on adhoc cart while edit the billing profile");	
		storeFrontUpdateCartPage.clickOnBillingNextStepBtn();
		storeFrontUpdateCartPage.clickPlaceOrderBtn();
		s_assert.assertTrue(storeFrontUpdateCartPage.verifyOrderPlacedConfirmationMessage(),"order is not placed successfully");
		s_assert.assertTrue(storeFrontUpdateCartPage.getUpdatedAddressPresentOnOrderConfirmationPage().toLowerCase().contains(editShippingAddressName.toLowerCase()),"Edited shipping address name expected is"+editShippingAddressName+"while shippig address coming on order confirmation page is while edit the address "+storeFrontUpdateCartPage.getUpdatedAddressPresentOnOrderConfirmationPage());
		s_assert.assertTrue(storeFrontUpdateCartPage.getUpdatedBillingAddressPresentOnOrderConfirmationPage().toLowerCase().contains(editedBillingProfileName.toLowerCase()),"Edited billing address name expected is"+editedBillingProfileName+"while shippig address coming on order confirmation page is while edit the address "+storeFrontUpdateCartPage.getUpdatedBillingAddressPresentOnOrderConfirmationPage());
		s_assert.assertAll();
	}
	// Add/Edit/Delete new billing profile on 'Billing Profile' page
	@Test
	public void testAddNewBillingProfileOnBillingProfilePage() throws InterruptedException, SQLException{
		int randomNum = CommonUtils.getRandomNum(10000, 1000000);
		int randomNumber = CommonUtils.getRandomNum(10000, 1000000);
		int randomNum1 = CommonUtils.getRandomNum(10000, 1000000);
		String billingProfileName = TestConstants.NEW_BILLING_PROFILE_NAME+randomNum;
		String newBillingProfileName = TestConstants.NEW_BILLING_PROFILE_NAME+randomNumber;
		String newEditedBillingProfileName = TestConstants.NEW_BILLING_PROFILE_NAME+randomNum1;
		String lastName = "lN";
		storeFrontHomePage = new StoreFrontHomePage(driver);
		storeFrontPCUserPage = storeFrontHomePage.loginAsPCUser(pcUserEmailID, password);
		storeFrontPCUserPage.clickOnWelcomeDropDown();
		storeFrontBillingInfoPage = storeFrontPCUserPage.clickBillingInfoLinkPresentOnWelcomeDropDown();
		s_assert.assertTrue(storeFrontBillingInfoPage.verifyBillingInfoPageIsDisplayed(),"Billing Info page has not been displayed");
		//Add a new billing profile on billing info page.
		storeFrontBillingInfoPage.clickAddNewBillingProfileLink();
		storeFrontBillingInfoPage.enterNewBillingCardNumber(TestConstants.CARD_NUMBER);
		storeFrontBillingInfoPage.enterNewBillingNameOnCard(billingProfileName+" "+lastName);
		storeFrontBillingInfoPage.selectNewBillingCardExpirationDate(TestConstants.CARD_EXP_MONTH, TestConstants.CARD_EXP_YEAR);
		storeFrontBillingInfoPage.enterNewBillingSecurityCode(TestConstants.SECURITY_CODE);
		storeFrontBillingInfoPage.selectNewBillingCardAddress();
		storeFrontBillingInfoPage.clickOnSaveBillingProfile();
		//--------------- Verify that Newly added Billing profile is listed in the Billing profiles section-----------------------------------------------------------------------------------------------------
		s_assert.assertTrue(storeFrontBillingInfoPage.isTheBillingAddressPresentOnPage(billingProfileName),"Newly added Billing profile is NOT listed on the page");
		//Adding 2nd billing profile
		storeFrontBillingInfoPage.clickAddNewBillingProfileLink();
		storeFrontBillingInfoPage.enterNewBillingCardNumber(TestConstants.CARD_NUMBER);
		storeFrontBillingInfoPage.enterNewBillingNameOnCard(newBillingProfileName+" "+lastName);
		storeFrontBillingInfoPage.selectNewBillingCardExpirationDate(TestConstants.CARD_EXP_MONTH, TestConstants.CARD_EXP_YEAR);
		storeFrontBillingInfoPage.enterNewBillingSecurityCode(TestConstants.SECURITY_CODE);
		storeFrontBillingInfoPage.selectNewBillingCardAddress();
		storeFrontBillingInfoPage.clickOnSaveBillingProfile();
		//--------------- Verify that Newly added Billing profile is listed in the Billing profiles section-----------------------------------------------------------------------------------------------------
		s_assert.assertTrue(storeFrontBillingInfoPage.isTheBillingAddressPresentOnPage(newBillingProfileName),"2nd Newly added Billing profile is NOT listed on the page");
		//Edit 2nd billing profile.
		storeFrontBillingInfoPage.clickOnEditOfBillingProfile(newBillingProfileName);
		storeFrontBillingInfoPage.enterNewBillingNameOnCard(newEditedBillingProfileName+" "+lastName);
		storeFrontBillingInfoPage.enterNewBillingSecurityCode(TestConstants.SECURITY_CODE);
		storeFrontBillingInfoPage.clickOnSaveBillingProfile();
		s_assert.assertTrue(storeFrontBillingInfoPage.isTheBillingAddressPresentOnPage(newEditedBillingProfileName),"Edited Billing profile is NOT listed on the page");
		//Delete 2nd billing profile.
		storeFrontBillingInfoPage.clickOnDeleteOfBillingProfile(newEditedBillingProfileName);
		s_assert.assertFalse(storeFrontBillingInfoPage.isTheBillingAddressPresentOnPage(newEditedBillingProfileName),"Edited Billing profile is NOT listed on the page");
		s_assert.assertAll();		
	}
	// Add, Edit, Delete shipping profile at ShippingInfo page
	@Test
	public void testAddAndEditShippingAddressOnShippingProfilePage() throws InterruptedException{
		int randomNum = CommonUtils.getRandomNum(10000, 1000000);
		int randomNumber = CommonUtils.getRandomNum(10000, 1000000);
		int randomNum1 = CommonUtils.getRandomNum(10000, 1000000);
		String shippingAddressName = TestConstants.ADDRESS_NAME+randomNum;
		String newShippingAddressName = TestConstants.ADDRESS_NAME+randomNumber;
		String editShippingAddressName = TestConstants.ADDRESS_NAME+randomNum1;
		String lastName = "lN";
		addressLine1 = TestConstants.ADDRESS_LINE_1_CA;
		city = TestConstants.CITY_CA;
		postalCode = TestConstants.POSTAL_CODE_CA;
		phoneNumber = TestConstants.PHONE_NUMBER_CA;
		state = TestConstants.PROVINCE_CA;
		countryId = "40";
		storeFrontHomePage = new StoreFrontHomePage(driver);
		storeFrontPCUserPage = storeFrontHomePage.loginAsPCUser(pcUserEmailID, password);
		storeFrontPCUserPage.clickOnWelcomeDropDown();
		storeFrontShippingInfoPage = storeFrontPCUserPage.clickShippingLinkPresentOnWelcomeDropDown();
		s_assert.assertTrue(storeFrontShippingInfoPage.verifyShippingInfoPageIsDisplayed(),"shipping info page has not been displayed");
		storeFrontShippingInfoPage.clickAddNewShippingProfileLink();
		storeFrontShippingInfoPage.enterNewShippingAddressName(shippingAddressName+" "+lastName);
		storeFrontShippingInfoPage.enterNewShippingAddressLine1(addressLine1);
		storeFrontShippingInfoPage.enterNewShippingAddressCity(city);
		storeFrontShippingInfoPage.selectNewShippingAddressState(state);
		storeFrontShippingInfoPage.enterNewShippingAddressPostalCode(postalCode);
		storeFrontShippingInfoPage.enterNewShippingAddressPhoneNumber(TestConstants.PHONE_NUMBER);
		storeFrontShippingInfoPage.clickOnSaveShippingProfile();
		//--------------- Verify that Newly added Shipping is listed in the Shipping profiles section-----------------------------------------------------------------------------------------------------
		s_assert.assertTrue(storeFrontShippingInfoPage.isShippingAddressPresentOnShippingPage(shippingAddressName), "Newly Added Shipping address is not listed on Shipping profile page");
		// Added 2nd new shipping profile
		storeFrontShippingInfoPage.clickAddNewShippingProfileLink();
		storeFrontShippingInfoPage.enterNewShippingAddressName(newShippingAddressName+" "+lastName);
		storeFrontShippingInfoPage.enterNewShippingAddressLine1(addressLine1);
		storeFrontShippingInfoPage.enterNewShippingAddressCity(city);
		storeFrontShippingInfoPage.selectNewShippingAddressState(state);
		storeFrontShippingInfoPage.enterNewShippingAddressPostalCode(postalCode);
		storeFrontShippingInfoPage.enterNewShippingAddressPhoneNumber(TestConstants.PHONE_NUMBER);
		storeFrontShippingInfoPage.clickOnSaveShippingProfile();
		//--------------- Verify that Newly added Shipping is listed in the Shipping profiles section-----------------------------------------------------------------------------------------------------
		s_assert.assertTrue(storeFrontShippingInfoPage.isShippingAddressPresentOnShippingPage(newShippingAddressName), "2nd Newly Added Shipping address is not listed on Shipping profile page");
		// Edit 2nd shipping address
		storeFrontShippingInfoPage.clickOnEditOfShippingProfile(newShippingAddressName);
		storeFrontShippingInfoPage.enterNewShippingAddressName(editShippingAddressName+" "+lastName);
		storeFrontShippingInfoPage.enterNewShippingAddressLine1(addressLine1);
		storeFrontShippingInfoPage.enterNewShippingAddressCity(city);
		storeFrontShippingInfoPage.selectNewShippingAddressState(state);
		storeFrontShippingInfoPage.enterNewShippingAddressPostalCode(postalCode);
		storeFrontShippingInfoPage.enterNewShippingAddressPhoneNumber(TestConstants.PHONE_NUMBER);
		storeFrontShippingInfoPage.clickOnSaveShippingProfile();
		//--------------- Verify that Newly added Shipping is listed in the shipping profiles section-----------------------------------------------------------------------------------------------------
		s_assert.assertTrue(storeFrontShippingInfoPage.isShippingAddressPresentOnShippingPage(editShippingAddressName), "Newly Edited Shipping address is not selected listed on Shipping profile page");
		//Delete 2nd shipping profile.
		storeFrontShippingInfoPage.deleteShippingAddress(editShippingAddressName);
		s_assert.assertFalse(storeFrontShippingInfoPage.isShippingAddressPresentOnShippingPage(editShippingAddressName), "Newly Edited Shipping address is listed on Shipping profile page after delete");
		s_assert.assertAll();		
	}
	// Update My AccountInfo
	@Test
	public void testAccountInformationUpdateForPC() throws InterruptedException{
		int randomNumPhone = CommonUtils.getRandomNum(10000, 99999);
		int randomNum = CommonUtils.getRandomNum(10000, 1000000);
		country = driver.getCountry();
		addressLine1 = TestConstants.ADDRESS_LINE_1_CA;
		city = TestConstants.CITY_CA;
		postalCode = TestConstants.POSTAL_CODE_CA;
		phoneNumber = "99999"+randomNumPhone;
		state = TestConstants.PROVINCE_CA;
		String firstName = TestConstants.FIRST_NAME+randomNum;
		String lastName = TestConstants.LAST_NAME+randomNum;

		storeFrontHomePage = new StoreFrontHomePage(driver);
		storeFrontPCUserPage = storeFrontHomePage.loginAsPCUser(pcUserEmailID, password);
		logger.info("login is successful");
		storeFrontPCUserPage.clickOnWelcomeDropDown();
		storeFrontAccountInfoPage = storeFrontPCUserPage.clickAccountInfoLinkPresentOnWelcomeDropDown();
		storeFrontAccountInfoPage.updateFirstName(firstName);
		storeFrontAccountInfoPage.updateLastName(lastName);
		storeFrontAccountInfoPage.updateAddressWithCityAndPostalCode(addressLine1, city, postalCode);
		String stateName = storeFrontAccountInfoPage.updateStateAndReturnName(state);
		logger.info("State/province selected is "+stateName);
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
	//Placed autoship order with Add Edit Shipping and Billing profile during checkout
	@Test
	public void testAddAndEditShippingAddressBillingProfileDuringAutoshipCheckout() throws InterruptedException{
		int randomNum = CommonUtils.getRandomNum(10000, 1000000);
		int randomNum1 = CommonUtils.getRandomNum(10000, 1000000);
		String lastName = "lN";
		String newShippingAddressName = TestConstants.ADDRESS_NAME+randomNum;
		String newBillingProfileName = TestConstants.NEW_BILLING_PROFILE_NAME_US+randomNum;
		String editShippingAddressName = TestConstants.ADDRESS_NAME+randomNum1;
		String editedBillingProfileName = TestConstants.NEW_BILLING_PROFILE_NAME+randomNum1;
		addressLine1 = TestConstants.ADDRESS_LINE_1_CA;
		city = TestConstants.CITY_CA;
		postalCode = TestConstants.POSTAL_CODE_CA;
		phoneNumber = TestConstants.PHONE_NUMBER_CA;
		state = TestConstants.PROVINCE_CA;
		countryId = "40";
		storeFrontHomePage = new StoreFrontHomePage(driver);
		storeFrontPCUserPage = storeFrontHomePage.loginAsPCUser(pcUserEmailID, password);

		//Continue with checkout page
		storeFrontPCUserPage.hoverOnShopLinkAndClickAllProductsLinksAfterLogin();
		storeFrontPCUserPage.clickOnAddtoPCPerksButton();
		storeFrontCartAutoShipPage = new StoreFrontCartAutoShipPage(driver);
		storeFrontUpdateCartPage = storeFrontCartAutoShipPage.clickUpdateMoreInfoLink();
		storeFrontUpdateCartPage.clickOnEditShipping();
		//Adding new shipping profile 
		storeFrontUpdateCartPage.clickAddNewShippingProfileLink();
		storeFrontUpdateCartPage.enterNewShippingAddressName(newShippingAddressName+" "+lastName);
		storeFrontUpdateCartPage.enterNewShippingAddressLine1(addressLine1);
		storeFrontUpdateCartPage.enterNewShippingAddressCity(city);
		storeFrontUpdateCartPage.selectNewShippingAddressState(state);
		storeFrontUpdateCartPage.enterNewShippingAddressPostalCode(postalCode);
		storeFrontUpdateCartPage.enterNewShippingAddressPhoneNumber(TestConstants.PHONE_NUMBER);
		storeFrontUpdateCartPage.clickOnSaveShippingProfile();
		storeFrontUpdateCartPage.clickOnUpdateCartShippingNextStepBtn();
		storeFrontUpdateCartPage.clickOnEditShipping();

		//---------------Verify that the new added shipping address is displayed in 'Shipment' section on update autoship cart page------------------------------------------------------------------------

		s_assert.assertTrue(storeFrontUpdateCartPage.isShippingAddressPresent(newShippingAddressName), "Newly added Shipping address NOT selected in update cart under shipping section");
		s_assert.assertTrue(storeFrontUpdateCartPage.isNewlyCreatedShippingProfileIsSelectedByDefault(newShippingAddressName), "Newly added Shipping address NOT selected as default in update cart under shipping section");

		//-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

		//Edit shipping address
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
