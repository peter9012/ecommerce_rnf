package com.rf.test.website.storeFront.hybris.radiantDataMigration;

import java.util.List;
import java.util.Map;

import org.testng.annotations.Test;

import com.rf.core.utils.CommonUtils;
import com.rf.core.website.constants.TestConstants;
import com.rf.pages.website.storeFront.StoreFrontAccountInfoPage;
import com.rf.pages.website.storeFront.StoreFrontBillingInfoPage;
import com.rf.pages.website.storeFront.StoreFrontHomePage;
import com.rf.pages.website.storeFront.StoreFrontRCUserPage;
import com.rf.pages.website.storeFront.StoreFrontShippingInfoPage;
import com.rf.pages.website.storeFront.StoreFrontUpdateCartPage;
import com.rf.test.website.RFWebsiteBaseTest;

public class MigratedRetailCustomerTests extends RFWebsiteBaseTest{
	public String emailID=null;
	private StoreFrontHomePage storeFrontHomePage;
	private StoreFrontRCUserPage storeFrontRCUserPage;
	private StoreFrontShippingInfoPage storeFrontShippingInfoPage;
	private StoreFrontUpdateCartPage storeFrontUpdateCartPage;
	private StoreFrontBillingInfoPage storeFrontBillingInfoPage;
	private StoreFrontAccountInfoPage storeFrontAccountInfoPage;
	private String city = null;
	private String phoneNumber = null;
	private String postalCode = null;
	private String addressLine1 = null;
	private String state = null;
	private int randomNum; 	
	String country=null;
	String env = null;
	List<Map<String, Object>> randomConsultantList =  null;
	String rcUserEmailID = "wallleanne1@gmail.com";
	String accountID = null;
	String countryID=null;

	/*Placed adhoc order with Add Edit Shipping and Billing profile during checkout*/
	@Test
	public void testAddAndEditShippingAddressBillingProfileDuringCheckoutForRC() throws InterruptedException{
		randomNum = CommonUtils.getRandomNum(10000, 1000000);
		String lastName = "lN";
		addressLine1 = TestConstants.ADDRESS_LINE_1_CA;
		city = TestConstants.CITY_CA;
		postalCode = TestConstants.POSTAL_CODE_CA;
		phoneNumber = TestConstants.PHONE_NUMBER_CA;
		state = TestConstants.PROVINCE_CA;
		countryId = "40";
		storeFrontHomePage = new StoreFrontHomePage(driver);
		storeFrontRCUserPage = storeFrontHomePage.loginAsRCUser(rcUserEmailID, password);
		//Continue with checkout page
		storeFrontRCUserPage.hoverOnShopLinkAndClickAllProductsLinksAfterLogin();
		storeFrontUpdateCartPage = new StoreFrontUpdateCartPage(driver);
		storeFrontUpdateCartPage.clickAddToBagButton(country);
		storeFrontUpdateCartPage.clickOnCheckoutButton();
		storeFrontHomePage.enterMainAccountInfoAndClearPreviousField();
		storeFrontUpdateCartPage.clickOnContinueWithoutSponsorLink();
		storeFrontUpdateCartPage.clickOnNextButtonAfterSelectingSponsor();
		storeFrontUpdateCartPage.clickUseAsEnteredOnQASPopup();
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
		storeFrontUpdateCartPage.clickOnSaveBillingProfile();
		s_assert.assertTrue(storeFrontHomePage.isTheBillingAddressPresentOnPage(editedBillingProfileName),"Newly edited Billing profile is NOT listed on Adhoc cart while edit the billing profile");
		s_assert.assertTrue(storeFrontHomePage.isBillingProfileIsSelectedByDefault(editedBillingProfileName),"Already Default billing profile is not DEFAULT selected on adhoc cart while edit the billing profile");

		storeFrontUpdateCartPage.clickOnBillingNextStepBtn();
		storeFrontUpdateCartPage.clickPlaceOrderBtn();
		s_assert.assertTrue(storeFrontUpdateCartPage.verifyOrderPlacedConfirmationMessage(),"order is not placed successfully");
		s_assert.assertTrue(storeFrontUpdateCartPage.getUpdatedAddressPresentOnOrderConfirmationPage().toLowerCase().contains(editShippingAddressName.toLowerCase()),"Edited shipping address name expected is"+editShippingAddressName+"while shippig address coming on order confirmation page is while edit the address "+storeFrontUpdateCartPage.getUpdatedAddressPresentOnOrderConfirmationPage());
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
		storeFrontRCUserPage = storeFrontHomePage.loginAsRCUser(rcUserEmailID, password);
		storeFrontRCUserPage.clickOnWelcomeDropDown();
		storeFrontShippingInfoPage = storeFrontRCUserPage.clickShippingLinkPresentOnWelcomeDropDown();
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

	// Add/Edit/Delete new billing profile on 'Billing Profile' page
			@Test
			public void testAddNewBillingProfileOnBillingProfilePage() throws InterruptedException{
				int randomNum = CommonUtils.getRandomNum(10000, 1000000);
				int randomNumber = CommonUtils.getRandomNum(10000, 1000000);
				int randomNum1 = CommonUtils.getRandomNum(10000, 1000000);
				String billingProfileName = TestConstants.NEW_BILLING_PROFILE_NAME+randomNum;
				String newBillingProfileName = TestConstants.NEW_BILLING_PROFILE_NAME+randomNumber;
				String newEditedBillingProfileName = TestConstants.NEW_BILLING_PROFILE_NAME+randomNum1;
				String lastName = "lN";
				storeFrontHomePage = new StoreFrontHomePage(driver);
				storeFrontRCUserPage = storeFrontHomePage.loginAsRCUser(rcUserEmailID, password);
				storeFrontRCUserPage.clickOnWelcomeDropDown();
				storeFrontBillingInfoPage = storeFrontRCUserPage.clickBillingInfoLinkPresentOnWelcomeDropDown();
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
			
			// Update My AccountInfo
			@Test
			public void testAccountInformationUpdateForRC() throws InterruptedException{
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
				storeFrontRCUserPage = storeFrontHomePage.loginAsRCUser(rcUserEmailID, password);
				storeFrontRCUserPage.clickOnWelcomeDropDown();
				storeFrontAccountInfoPage = storeFrontRCUserPage.clickAccountInfoLinkPresentOnWelcomeDropDown();
				storeFrontAccountInfoPage.updateFirstName(firstName);
				storeFrontAccountInfoPage.updateLastName(lastName);
				storeFrontAccountInfoPage.updateAddressWithCityAndPostalCode(addressLine1, city, postalCode);
				String stateName = storeFrontAccountInfoPage.updateStateAndReturnName(state);
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
	
}
