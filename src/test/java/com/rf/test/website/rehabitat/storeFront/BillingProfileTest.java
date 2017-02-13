package com.rf.test.website.rehabitat.storeFront;

import org.testng.annotations.Test;

import com.rf.core.utils.CommonUtils;
import com.rf.core.website.constants.TestConstants;
import com.rf.test.website.rehabitat.storeFront.baseTest.StoreFrontWebsiteBaseTest;

public class BillingProfileTest extends StoreFrontWebsiteBaseTest{

	/***
	 * qTest : TC-505 Billing information
	 * 
	 * Description : This test will validate the Billing Profile section for Consultant/PC/RC Users.
	 * 
	 *     
	 */
	@Test(enabled=true)
	public void testBillingInformation_505(){
		String currentURL = null;
		String urlToAssert = "payment-details";
		//Login as PC user.
		sfHomePage.loginToStoreFront(TestConstants.PC_EMAIL_HAVING_AUTOSHIP,  password,true);
		sfHomePage.clickWelcomeDropdown();
		sfBillingInfoPage = sfHomePage.navigateToBillingInfoPage();
		currentURL = sfBillingInfoPage.getCurrentURL().toLowerCase();
		s_assert.assertTrue(currentURL.contains(urlToAssert), "Expected URL should contain "+urlToAssert+" but actual on UI is " + currentURL + " for PC User Billing Info Page.");
		s_assert.assertTrue(sfBillingInfoPage.isBillingInfoHeaderIsPresent(),"Billing Info Header is not present for PC User");
		s_assert.assertTrue(sfBillingInfoPage.isBillingProfilesSectionHeaderIsPresent(),"Billing Profiles Section Header is not present for PC User");
		s_assert.assertTrue(sfBillingInfoPage.isBillingProfileDetailsListIsPresent(),"Billing Profiles Details List is not present for PC User");
		sfHomePage.clickWelcomeDropdown();
		sfBillingInfoPage.logout();
		//Login as Consultant.
		sfHomePage.loginToStoreFront(TestConstants.CONSULTANT_EMAIL_WITH_CRP_AND_PULSE,  password,true);
		sfHomePage.clickWelcomeDropdown();
		sfBillingInfoPage = sfHomePage.navigateToBillingInfoPage();
		currentURL = sfBillingInfoPage.getCurrentURL().toLowerCase();
		s_assert.assertTrue(currentURL.contains(urlToAssert), "Expected URL should contain "+urlToAssert+" but actual on UI is " + currentURL + " for Consultant Billing Info Page.");
		s_assert.assertTrue(sfBillingInfoPage.isBillingInfoHeaderIsPresent(),"Billing Info Header is not present for Consultant");
		s_assert.assertTrue(sfBillingInfoPage.isBillingProfilesSectionHeaderIsPresent(),"Billing Profiles Section Header is not present for Consultant");
		s_assert.assertTrue(sfBillingInfoPage.isBillingProfileDetailsListIsPresent(),"Billing Profiles Details List is not present for Consultant");
		sfHomePage.clickWelcomeDropdown();
		sfBillingInfoPage.logout();
		//Login as RC.
		sfHomePage.loginToStoreFront(TestConstants.RC_EMAIL_HAVING_ORDER,  password,true);
		sfHomePage.clickWelcomeDropdown();
		sfBillingInfoPage = sfHomePage.navigateToBillingInfoPage();
		currentURL = sfBillingInfoPage.getCurrentURL().toLowerCase();
		s_assert.assertTrue(currentURL.contains(urlToAssert), "Expected URL should contain "+urlToAssert+" but actual on UI is " + currentURL + " for RC User Billing Info Page.");
		s_assert.assertTrue(sfBillingInfoPage.isBillingInfoHeaderIsPresent(),"Billing Info Header is not present for RC User");
		s_assert.assertTrue(sfBillingInfoPage.isBillingProfilesSectionHeaderIsPresent(),"Billing Profiles Section Header is not present for RC User");
		s_assert.assertTrue(sfBillingInfoPage.isBillingProfileDetailsListIsPresent(),"Billing Profiles Details List is not present for RC User");
		sfHomePage.clickWelcomeDropdown();
		sfBillingInfoPage.logout();
		s_assert.assertAll();
	}

	/***
	 * 
	 */
	@Test(enabled=true)
	public void testAddNewBillingAddressToNewlyCreatedBillingProfile_386(){
		String currentURL = null;
		String randomWord = CommonUtils.getRandomWord(5);
		String suggestedBillingAddress = null;
		String urlToAssert = "payment-details";
		String cardName = TestConstants.CARD_NAME + randomWord;
		String firstName = TestConstants.FIRST_NAME;
		String lastName = TestConstants.LAST_NAME;
		String addressLine1 = TestConstants.ADDRESS_LINE_1_US;
		String addressLine2 = TestConstants.ADDRESS_LINE_2_US;
		String city = TestConstants.CITY_US;
		String state = TestConstants.STATE_US;
		String postalCode = TestConstants.POSTAL_CODE_US;
		String phoneNumber = TestConstants.PHONE_NUMBER;
		String addressInWrongFormat = TestConstants.WRONG_ADDRESS_LINE_1_US;
		String wrongCombinationCity = TestConstants.CITY_DALLAS_US;
		String cardLastName = null;

		//Login as consultant user.
		sfHomePage.loginToStoreFront(TestConstants.CONSULTANT_EMAIL_WITH_CRP_AND_PULSE,  password,true);
		sfHomePage.clickWelcomeDropdown();
		sfBillingInfoPage = sfHomePage.navigateToBillingInfoPage();
		currentURL = sfBillingInfoPage.getCurrentURL().toLowerCase();
		s_assert.assertTrue(currentURL.contains(urlToAssert), "Expected URL should contain "+urlToAssert+" but actual on UI is"+currentURL);
		sfBillingInfoPage.clickAddNewBillingProfileLink();
		sfBillingInfoPage.enterUserBillingDetails(TestConstants.CARD_TYPE, TestConstants.CARD_NUMBER_2,cardName, TestConstants.CVV);
		sfBillingInfoPage.clickAddNewBillingAddressLink();
		s_assert.assertTrue(sfBillingInfoPage.isAddNewBillingAddressFormDisplayed(),"ADD NEW BILLING ADDRESS form Block is not Displayed");

		// Filling Address in Wrong format.
		sfBillingInfoPage.enterConsultantAddressDetails(firstName, lastName, addressInWrongFormat, addressLine2, city, state, postalCode, phoneNumber);
		sfBillingInfoPage.clickBillingDetailsNextbutton();
		s_assert.assertTrue(sfBillingInfoPage.isUnknownAddressErrorMessageIsPresentAsExpected(),"Unknown Address Error message is not present");

		// Filling Wrong combination of City and State in Billing address
		sfBillingInfoPage.enterConsultantAddressDetails(firstName, lastName, addressLine1, addressLine2, wrongCombinationCity, state, postalCode, phoneNumber);
		sfBillingInfoPage.clickBillingDetailsNextbutton();
		suggestedBillingAddress = sfBillingInfoPage.getSuggestedBillingAddressFromBlock();
		s_assert.assertTrue(suggestedBillingAddress.contains(city),"Suggested Address City is not found as expected. Expected City : "+city+". Actual City : "+wrongCombinationCity);
		sfBillingInfoPage.clickYesButtonOnAddressSuggestionPopUp();

		// Filling Right Address 
		sfBillingInfoPage.clickAddNewBillingProfileLink();
		sfBillingInfoPage.enterUserBillingDetails(TestConstants.CARD_TYPE, TestConstants.CARD_NUMBER,cardName, TestConstants.CVV);
		sfBillingInfoPage.clickAddNewBillingAddressLink();
		s_assert.assertTrue(sfBillingInfoPage.isAddNewBillingAddressFormDisplayed(),"ADD NEW BILLING ADDRESS form Block is not Displayed");
		sfBillingInfoPage.enterConsultantAddressDetails(firstName, lastName, addressLine1, addressLine2, city, state, postalCode, phoneNumber);
		sfBillingInfoPage.clickBillingDetailsNextbutton();
		sfBillingInfoPage.clickUseAsEnteredButtonOnPopUp();
		cardLastName = sfBillingInfoPage.getLastName(cardName);
		s_assert.assertTrue(sfBillingInfoPage.isNewBillingProfilePresentInRowList(cardLastName),"New Billing Profile is not present in Profiles List");
		s_assert.assertAll();
	}

	/***
	 * qTest : TC-475 Billing information- Default Payment Profiles - Multiple Profiles
	 * 
	 * Description :  This method validates the change of Default billing profile from multiple profiles
	 * 
	 *     
	 */
	@Test(enabled=true)
	public void testBillingInformationDefaultPaymentProfilesMultipleProfiles_475(){
		String currentURL = null;
		String randomWord = CommonUtils.getRandomWord(5);
		String randomWord2 = CommonUtils.getRandomWord(5);
		String urlToAssert = "payment-details";
		String cardNameForFirstProfile = TestConstants.CARD_NAME + randomWord;
		String cardNameForSecondProfile = TestConstants.CARD_NAME + randomWord2;
		String firstName = TestConstants.FIRST_NAME;
		String lastName = TestConstants.LAST_NAME;
		String addressLine1 = TestConstants.ADDRESS_LINE_1_US;
		String addressLine2 = TestConstants.ADDRESS_LINE_2_US;
		String city = TestConstants.CITY_US;
		String state = TestConstants.STATE_US;
		String postalCode = TestConstants.POSTAL_CODE_US;
		String phoneNumber = TestConstants.PHONE_NUMBER;
		String updatedDefaultBillingProfileName = null;
		int countOfDefaultProfiles;
		String cardLastNameForFirstProfile = null;
		String cardLastNameForSecondProfile = null;


		//Login as consultant user.
		sfHomePage.loginToStoreFront(TestConstants.CONSULTANT_EMAIL_WITH_CRP_AND_PULSE,  password,true);
		sfHomePage.clickWelcomeDropdown();
		sfBillingInfoPage = sfHomePage.navigateToBillingInfoPage();
		currentURL = sfBillingInfoPage.getCurrentURL().toLowerCase();
		s_assert.assertTrue(currentURL.contains(urlToAssert), "Expected URL should contain "+urlToAssert+" but actual on UI is "+currentURL);

		// Creating a new profile and setting it as default profile initially.
		sfBillingInfoPage.clickAddNewBillingProfileLink();
		sfBillingInfoPage.enterUserBillingDetails(TestConstants.CARD_TYPE, TestConstants.CARD_NUMBER_2,cardNameForFirstProfile, TestConstants.CVV);
		sfBillingInfoPage.clickAddNewBillingAddressLink();
		sfBillingInfoPage.enterConsultantAddressDetails(firstName, lastName, addressLine1, addressLine2, city, state, postalCode, phoneNumber);
		sfBillingInfoPage.clickBillingDetailsNextbutton();
		sfBillingInfoPage.clickUseAsEnteredButtonOnPopUp();
		cardLastNameForFirstProfile = sfBillingInfoPage.getLastName(cardNameForFirstProfile);
		sfBillingInfoPage.setProfileAsDefault(cardLastNameForFirstProfile);
		s_assert.assertTrue(sfBillingInfoPage.isActionSuccessfulMsgAppearedAsExpected(TestConstants.DEFAULT_PROFILE_UPDATION_MESSAGE),"Default Profile Updation Success msg is not appeared as expected for Profile : " + cardNameForFirstProfile);

		// Creating one more new Billing profile
		sfBillingInfoPage.clickAddNewBillingProfileLink();
		sfBillingInfoPage.enterUserBillingDetails(TestConstants.CARD_TYPE, TestConstants.CARD_NUMBER_2,cardNameForSecondProfile, TestConstants.CVV);
		sfBillingInfoPage.clickAddNewBillingAddressLink();
		sfBillingInfoPage.enterConsultantAddressDetails(firstName, lastName, addressLine1, addressLine2, city, state, postalCode, phoneNumber);
		sfBillingInfoPage.clickBillingDetailsNextbutton();
		sfBillingInfoPage.clickUseAsEnteredButtonOnPopUp();
		cardLastNameForSecondProfile = sfBillingInfoPage.getLastName(cardNameForSecondProfile);


		countOfDefaultProfiles = sfBillingInfoPage.getCountOfDefaultBillingProfiles();
		s_assert.assertEquals(countOfDefaultProfiles,1,"More than 1 Default Profile is present in the Billing Info section. Expected : 1. Actual : "+countOfDefaultProfiles);
		s_assert.assertFalse(sfBillingInfoPage.isDeleteOptionAvailableForDefaultProfile(),"Delete Option is avaialble for Default profile");

		// Setting Second new profile as default profile
		sfBillingInfoPage.setProfileAsDefault(cardLastNameForSecondProfile);
		s_assert.assertTrue(sfBillingInfoPage.isActionSuccessfulMsgAppearedAsExpected(TestConstants.DEFAULT_PROFILE_UPDATION_MESSAGE),"Default Profile Updation Success msg is not appeared as expected for Profile : " + cardNameForSecondProfile);
		updatedDefaultBillingProfileName = sfBillingInfoPage.getDefaultBillingProfileName().toLowerCase();
		s_assert.assertTrue(sfBillingInfoPage.isProfileGetUpdatedAsDefaultProfile(cardLastNameForSecondProfile, updatedDefaultBillingProfileName),cardLastNameForFirstProfile.toUpperCase() + " Profile does not get updated as Default Profile");
		countOfDefaultProfiles = sfBillingInfoPage.getCountOfDefaultBillingProfiles();
		s_assert.assertEquals(countOfDefaultProfiles,1,"More than 1 Default Profile is present in the Billing Info section. Expected : 1. Actual : "+countOfDefaultProfiles);
		s_assert.assertFalse(sfBillingInfoPage.isDeleteOptionAvailableForDefaultProfile(),"Delete Option is avaialble for Default profile");
		s_assert.assertFalse(sfBillingInfoPage.isProfileIsDefaultProfile(cardLastNameForFirstProfile.toLowerCase()),"Initial Default profile : "+cardNameForFirstProfile+ " is still available as Default Profile.");
		s_assert.assertTrue(sfBillingInfoPage.isDeleteOptionAvailableForProfile(cardLastNameForFirstProfile.toLowerCase()),"Delete Option is not available for profile "+cardNameForFirstProfile);
		s_assert.assertAll();
	}

	/***
	 * qTest : TC-426 Billing Profile- Select an Billing Address - New Profile
	 * 
	 * Description : This test will add an existing billing address to newly created billing profile 
	 * on billing info page.
	 * 
	 *     
	 */
	@Test(enabled=true)
	public void testBillingProfileSelectAnBillingAddressNewProfile_426(){
		String currentURL = null;
		int initialCountOfProfiles;
		int countAfterAddingNewProfile;
		int totalAddressInDD;
		String randomWord = CommonUtils.getRandomWord(4);
		String urlToAssert = "payment-details";
		String nameOnCard = TestConstants.CARD_NAME + randomWord;
		//Login as consultant user.
		sfHomePage.loginToStoreFront(TestConstants.CONSULTANT_EMAIL_WITH_CRP_AND_PULSE,  password,true);
		sfHomePage.clickWelcomeDropdown();
		sfBillingInfoPage = sfHomePage.navigateToBillingInfoPage();
		currentURL = sfBillingInfoPage.getCurrentURL().toLowerCase();
		s_assert.assertTrue(currentURL.contains(urlToAssert), "Expected URL should contain "+urlToAssert+" but actual on UI is"+currentURL);
		initialCountOfProfiles = sfBillingInfoPage.getCountOfBillingProfilesPresentInProfilesListSection();
		sfBillingInfoPage.clickAddNewBillingProfileLink();
		sfBillingInfoPage.enterUserBillingDetails(TestConstants.CARD_TYPE, TestConstants.CARD_NUMBER, nameOnCard, TestConstants.CVV);
		s_assert.assertTrue(sfBillingInfoPage.isBillingAddressDropdownIsPresent(),"Billing Address Dropdown is not present");
		totalAddressInDD = sfBillingInfoPage.getCountOfBillingAddressesPresentInDropdown();  
		sfBillingInfoPage.selectRandomAddressFromBillingAddressDropdown(CommonUtils.getRandomNum(1,totalAddressInDD-1));
		sfBillingInfoPage.clickBillingDetailsNextbutton();
		countAfterAddingNewProfile = sfBillingInfoPage.getCountOfBillingProfilesPresentInProfilesListSection();
		s_assert.assertEquals(countAfterAddingNewProfile,initialCountOfProfiles + 1,"Billing Profile is not present in profiles List. Count of Profiles is still same after adding New Billing profile. Expected Count : "+ (initialCountOfProfiles + 1) + ". Actual Count : " + countAfterAddingNewProfile);
		s_assert.assertAll();
	}

	/***
	 * qTest : TC-428 Billing Profile- Select an Billing Address - Blank dropdown
	 * 
	 * Description : This test case validates the error message for Billing Address details.
	 * 
	 *     
	 */
	@Test(enabled=true)
	public void testBillingProfileSelectAnBillingAddressBlankDropdown_428(){
		String currentURL = null;
		String randomWord = CommonUtils.getRandomWord(5);
		String cardType = TestConstants.CARD_TYPE;
		String cardNum = TestConstants.CARD_NUMBER_2;
		String cardName = TestConstants.CARD_NAME + randomWord;
		String cvv =  TestConstants.CVV;
		String urlToAssert = "payment-details";

		//Login as RC user.
		sfHomePage.loginToStoreFront(TestConstants.RC_EMAIL_WITHOUT_ORDER,  password,true);
		sfHomePage.clickWelcomeDropdown();
		sfBillingInfoPage = sfHomePage.navigateToBillingInfoPage();
		currentURL = sfBillingInfoPage.getCurrentURL().toLowerCase();
		s_assert.assertTrue(currentURL.contains(urlToAssert), "Expected URL should contain " + urlToAssert + " but actual on UI is " + currentURL);
		sfBillingInfoPage.clickAddNewBillingProfileLink();
		sfBillingInfoPage.enterUserBillingDetails(cardType, cardNum, cardName, cvv);
		sfBillingInfoPage.clickBillingDetailsNextbutton();
		s_assert.assertTrue(sfBillingInfoPage.isErrrorMsgsForAllMandatoryFieldsForBillingAddressArePresent(),"Error msg for all mandatory fields of billing address are not present as expected");
		s_assert.assertAll();
	}

	/***
	 * qTest : TC-427 Billing Profile- Select an Billing Address - Existing Profile
	 * 
	 * Description : This test will validate the Billing address selected from dropdown for existing profile
	 * 
	 *     
	 */
	@Test(enabled=true)// Need to Verify
	public void testBillingProfileSelectAnBillingAddressExistingProfile_427(){
		String currentURL = null;
		String randomWord = CommonUtils.getRandomWord(5);
		String urlToAssert = "payment-details";
		String cardName = TestConstants.CARD_NAME + randomWord;
		String firstName = TestConstants.FIRST_NAME;
		String lastName = TestConstants.LAST_NAME;
		String addressLine1 = TestConstants.ADDRESS_LINE_1_US;
		String addressLine2 = TestConstants.ADDRESS_LINE_2_US;
		String city = TestConstants.CITY_US;
		String state = TestConstants.STATE_US;
		String postalCode = TestConstants.POSTAL_CODE_US;
		String phoneNumber = TestConstants.PHONE_NUMBER;
		String updatedAddressLine1 = TestConstants.SECOND_ADDRESS_LINE_1_US;
		String updatedAddressLine2 = TestConstants.SECOND_ADDRESS_LINE_2_US;
		String updatedCity = TestConstants.SECOND_CITY_US;
		String updatedPostalCode = TestConstants.SECOND_POSTAL_CODE_US;
		String stateAbbreviation = TestConstants.STATE_US_ABBREVIATION;
		String billingAddressOnUI = null;
		String cardLastName = null;
		int initialAddressCount = 0;
		int finalAddressCount = 0;
		int addressIndex = 0;

		//Login as Consultant user.
		sfHomePage.loginToStoreFront(TestConstants.CONSULTANT_EMAIL_WITH_CRP_AND_PULSE,  password,true);
		sfHomePage.clickWelcomeDropdown();
		sfBillingInfoPage = sfHomePage.navigateToBillingInfoPage();
		currentURL = sfBillingInfoPage.getCurrentURL().toLowerCase();
		s_assert.assertTrue(currentURL.contains(urlToAssert), "Expected URL should contain "+urlToAssert+" but actual on UI is " + currentURL + " for PC User Billing Info Page.");

		// Creating New Billing Profile with specific Address 1
		sfBillingInfoPage.clickAddNewBillingProfileLink();
		sfBillingInfoPage.enterUserBillingDetails(TestConstants.CARD_TYPE, TestConstants.CARD_NUMBER,cardName, TestConstants.CVV);
		sfBillingInfoPage.clickAddNewBillingAddressLink();
		s_assert.assertTrue(sfBillingInfoPage.isAddNewBillingAddressFormDisplayed(),"ADD NEW BILLING ADDRESS form Block is not Displayed");
		sfBillingInfoPage.enterConsultantAddressDetails(firstName, lastName, addressLine1, addressLine2, city, state, postalCode, phoneNumber);
		sfBillingInfoPage.clickBillingDetailsNextbutton();
		sfBillingInfoPage.clickUseAsEnteredButtonOnPopUp();
		cardLastName = sfBillingInfoPage.getLastName(cardName);
		s_assert.assertTrue(sfBillingInfoPage.isNewBillingProfilePresentInRowList(cardLastName),"New Billing Profile is not present in Profiles List");

		// Updating Address and verifying it in Address dropdown
		sfBillingInfoPage.clickBillingProfileEditLink(cardLastName);
		initialAddressCount = sfBillingInfoPage.getCountOfBillingAddressesPresentInDropdownForExistingProfile();
		sfBillingInfoPage.clickAddNewBillingAddressLink();
		sfBillingInfoPage.enterBillingAddressDetailsForExistingBillingProfile(firstName, lastName, updatedAddressLine1, updatedAddressLine2, updatedCity, state, updatedPostalCode, phoneNumber);
		sfBillingInfoPage.clickSavePaymentButton();
		sfBillingInfoPage.clickUseAsEnteredButtonOnPopUp();
		billingAddressOnUI = sfBillingInfoPage.getBillingAddressForSpecificBillingProfile(cardLastName);
		s_assert.assertTrue(sfBillingInfoPage.isBillingAddressOnUIIsFoundAsExpected(updatedAddressLine1, updatedAddressLine2, updatedCity, updatedPostalCode, stateAbbreviation, billingAddressOnUI),"Billing Address on UI is not found as expected.");

		// Editing Billing Profile and selecting initial address from dropdown
		sfBillingInfoPage.clickBillingProfileEditLink(cardLastName);
		s_assert.assertTrue(sfBillingInfoPage.isBillingAddressDropdownPresentForExisitngProfile(),"Billing addresses dropdown is not present");
		finalAddressCount = sfBillingInfoPage.getCountOfBillingAddressesPresentInDropdownForExistingProfile();
		s_assert.assertTrue(finalAddressCount == initialAddressCount+1,"Address count in Dropdown is not found as expected. Expected : " + (initialAddressCount+1) + ". Actual : " + finalAddressCount);
		addressIndex = sfBillingInfoPage.getAddressIndexFromDD(addressLine1, addressLine2, city, postalCode);
		sfBillingInfoPage.selectAddressFromDD(addressIndex);
		sfBillingInfoPage.clickSavePaymentButton();
		sfBillingInfoPage.clickUseAsEnteredButtonOnPopUp();
		billingAddressOnUI = sfBillingInfoPage.getBillingAddressForSpecificBillingProfile(cardLastName);
		s_assert.assertTrue(sfBillingInfoPage.isBillingAddressOnUIIsFoundAsExpected(addressLine1, addressLine2, addressLine2, postalCode, stateAbbreviation, billingAddressOnUI),"Billing Address on UI is not found as expected");
		s_assert.assertAll();
	}

	/***
	 * qTest : TC-474 Billing information- Default Payment Profiles - Single Profile
	 * 
	 * Description : This test case validate the profile on Billing Info page for a user having single profile only
	 * 
	 *     
	 */
	@Test(enabled=true)
	public void testBillingInformationDefaultPaymentProfilesSingleProfile_474(){
		String currentURL = null;
		String urlToAssert = "payment-details";
		int countOfDefaultProfiles = 0;

		//Login as PC user.
		sfHomePage.loginToStoreFront(TestConstants.PC_EMAIL_HAVING_SINGLE_BILLING_PROFILE, password,true);
		sfHomePage.clickWelcomeDropdown();
		sfBillingInfoPage = sfHomePage.navigateToBillingInfoPage();
		currentURL = sfBillingInfoPage.getCurrentURL().toLowerCase();
		s_assert.assertTrue(currentURL.contains(urlToAssert), "Expected URL should contain " + urlToAssert + " but actual on UI is " + currentURL);
		countOfDefaultProfiles = sfBillingInfoPage.getCountOfDefaultBillingProfiles();
		s_assert.assertEquals(countOfDefaultProfiles,1,"More than 1 Default Profile is present in the Billing Info section. Expected : 1. Actual : "+countOfDefaultProfiles);
		s_assert.assertFalse(sfBillingInfoPage.isDeleteOptionAvailableForDefaultProfile(),"Delete Option is avaialble for Default profile");
		s_assert.assertAll();
	}

	/***
	 * qTest : TC-527 Edit a billing profile
	 * 
	 * Description : This test will edit the billing profile and update the billing address from dropdown.
	 * 
	 *     
	 */
	@Test(enabled=true)
	public void testEditABillingProfile_527(){
		String currentURL = null;
		String randomWord = CommonUtils.getRandomWord(5);
		String urlToAssert = "payment-details";
		String cardName = TestConstants.CARD_NAME + randomWord;
		String firstName = TestConstants.FIRST_NAME;
		String lastName = TestConstants.LAST_NAME;
		String addressLine1 = TestConstants.ADDRESS_LINE_1_US;
		String addressLine2 = TestConstants.ADDRESS_LINE_2_US;
		String city = TestConstants.CITY_US;
		String state = TestConstants.STATE_US;
		String postalCode = TestConstants.POSTAL_CODE_US;
		String phoneNumber = TestConstants.PHONE_NUMBER;
		String updatedAddressLine1 = TestConstants.SECOND_ADDRESS_LINE_1_US;
		String updatedAddressLine2 = TestConstants.SECOND_ADDRESS_LINE_2_US;
		String updatedCity = TestConstants.SECOND_CITY_US;
		String updatedPostalCode = TestConstants.SECOND_POSTAL_CODE_US;
		String stateAbbreviation = TestConstants.STATE_US_ABBREVIATION;
		String billingAddressOnUI = null;
		String cardLastName = null;
		int initialAddressCount = 0;
		int finalAddressCount = 0;
		int addressIndex = 0;

		//Login as Consultant user.
		sfHomePage.loginToStoreFront(TestConstants.CONSULTANT_EMAIL_WITH_CRP_AND_PULSE,  password,true);
		sfHomePage.clickWelcomeDropdown();
		sfBillingInfoPage = sfHomePage.navigateToBillingInfoPage();
		currentURL = sfBillingInfoPage.getCurrentURL().toLowerCase();
		s_assert.assertTrue(currentURL.contains(urlToAssert), "Expected URL should contain "+urlToAssert+" but actual on UI is " + currentURL + " for PC User Billing Info Page.");

		// Creating New Billing Profile with specific Address 1
		sfBillingInfoPage.clickAddNewBillingProfileLink();
		sfBillingInfoPage.enterUserBillingDetails(TestConstants.CARD_TYPE, TestConstants.CARD_NUMBER,cardName, TestConstants.CVV);
		sfBillingInfoPage.clickAddNewBillingAddressLink();
		s_assert.assertTrue(sfBillingInfoPage.isAddNewBillingAddressFormDisplayed(),"ADD NEW BILLING ADDRESS form Block is not Displayed");
		sfBillingInfoPage.enterConsultantAddressDetails(firstName, lastName, addressLine1, addressLine2, city, state, postalCode, phoneNumber);
		sfBillingInfoPage.clickBillingDetailsNextbutton();
		sfBillingInfoPage.clickUseAsEnteredButtonOnPopUp();
		cardLastName = sfBillingInfoPage.getLastName(cardName);
		s_assert.assertTrue(sfBillingInfoPage.isNewBillingProfilePresentInRowList(cardLastName),"New Billing Profile is not present in Profiles List");

		// Updating Address and verifying it in Address dropdown
		sfBillingInfoPage.clickBillingProfileEditLink(cardLastName);
		initialAddressCount = sfBillingInfoPage.getCountOfBillingAddressesPresentInDropdownForExistingProfile();
		sfBillingInfoPage.clickAddNewBillingAddressLink();
		sfBillingInfoPage.enterBillingAddressDetailsForExistingBillingProfile(firstName, lastName, updatedAddressLine1, updatedAddressLine2, updatedCity, state, updatedPostalCode, phoneNumber);
		sfBillingInfoPage.clickSavePaymentButton();
		sfBillingInfoPage.clickUseAsEnteredButtonOnPopUp();
		billingAddressOnUI = sfBillingInfoPage.getBillingAddressForSpecificBillingProfile(cardLastName);
		s_assert.assertTrue(sfBillingInfoPage.isBillingAddressOnUIIsFoundAsExpected(updatedAddressLine1, updatedAddressLine2, updatedCity, updatedPostalCode, stateAbbreviation, billingAddressOnUI),"Billing Address on UI is not found as expected after updation");

		// Editing Billing Profile and selecting initial address from dropdown and clicking cancel button for changes
		sfBillingInfoPage.clickBillingProfileEditLink(cardLastName);
		s_assert.assertTrue(sfBillingInfoPage.isBillingAddressDropdownPresentForExisitngProfile(),"Billing addresses dropdown is not present");
		finalAddressCount = sfBillingInfoPage.getCountOfBillingAddressesPresentInDropdownForExistingProfile();
		s_assert.assertTrue(finalAddressCount == initialAddressCount+1,"Address count in Dropdown is not found as expected. Expected : " + (initialAddressCount + 1) + ". Actual : " + finalAddressCount);
		addressIndex = sfBillingInfoPage.getAddressIndexFromDD(addressLine1, addressLine2, city, postalCode);
		sfBillingInfoPage.selectAddressFromDD(addressIndex);
		sfBillingInfoPage.clickCancelButton();
		billingAddressOnUI = sfBillingInfoPage.getBillingAddressForSpecificBillingProfile(cardLastName);
		s_assert.assertFalse(sfBillingInfoPage.isBillingAddressOnUIIsFoundAsExpected(addressLine1, addressLine2, addressLine2, postalCode, stateAbbreviation, billingAddressOnUI),"Billing Address should not be updated after clicking cancel button for updation");
		s_assert.assertTrue(sfBillingInfoPage.isBillingAddressOnUIIsFoundAsExpected(updatedAddressLine1, updatedAddressLine2, updatedCity, updatedPostalCode, stateAbbreviation, billingAddressOnUI),"Billing Address on UI is not found as initial Billing address after clicking cancel button for updation");

		// Editing Billing Profile and selecting initial address from dropdown and clicking save payment button for changes
		sfBillingInfoPage.clickBillingProfileEditLink(cardLastName);
		s_assert.assertTrue(sfBillingInfoPage.isBillingAddressDropdownPresentForExisitngProfile(),"Billing addresses dropdown is not present");
		addressIndex = sfBillingInfoPage.getAddressIndexFromDD(addressLine1, addressLine2, city, postalCode);
		sfBillingInfoPage.selectAddressFromDD(addressIndex);
		sfBillingInfoPage.clickSavePaymentButton();
		sfBillingInfoPage.clickUseAsEnteredButtonOnPopUp();
		billingAddressOnUI = sfBillingInfoPage.getBillingAddressForSpecificBillingProfile(cardLastName);
		s_assert.assertTrue(sfBillingInfoPage.isBillingAddressOnUIIsFoundAsExpected(addressLine1, addressLine2, addressLine2, postalCode, stateAbbreviation, billingAddressOnUI),"Billing Address should not be updated after clicking cancel button for updation");
		s_assert.assertAll();
	}

	/***
	 * qTest : TC-385 Add New Billing Address on Existing Billing Profile
	 * 
	 * Description : This test will add and verify the new billing address into existing billing profile
	 * 
	 *     
	 */
	@Test(enabled=true)
	public void testAddNewBillingAddressToExistingBillingProfile_385(){
		String currentURL = null;
		String randomWord = CommonUtils.getRandomWord(5);
		String suggestedBillingAddress = null;
		String urlToAssert = "payment-details";
		String cardName = TestConstants.CARD_NAME + randomWord;
		String firstName = TestConstants.FIRST_NAME;
		String lastName = TestConstants.LAST_NAME;
		String addressLine1 = TestConstants.ADDRESS_LINE_1_US;
		String addressLine2 = TestConstants.ADDRESS_LINE_2_US;
		String city = TestConstants.CITY_US;
		String state = TestConstants.STATE_US;
		String postalCode = TestConstants.POSTAL_CODE_US;
		String phoneNumber = TestConstants.PHONE_NUMBER;
		String addressInWrongFormat = TestConstants.WRONG_ADDRESS_LINE_1_US;
		String wrongCombinationCity = TestConstants.CITY_DALLAS_US;
		String updatedAddressLine1 = TestConstants.SECOND_ADDRESS_LINE_1_US;
		String updatedAddressLine2 = TestConstants.SECOND_ADDRESS_LINE_2_US;
		String updatedCity = TestConstants.SECOND_CITY_US;
		String updatedPostalCode = TestConstants.SECOND_POSTAL_CODE_US;
		String stateAbbreviation = TestConstants.STATE_US_ABBREVIATION;
		String cardLastName = null;
		String billingAddressOnUI = null;

		// Login as Consultant User
		sfHomePage.loginToStoreFront(TestConstants.CONSULTANT_EMAIL_WITH_CRP_AND_PULSE, password,true);
		sfHomePage.clickWelcomeDropdown();
		sfBillingInfoPage = sfHomePage.navigateToBillingInfoPage();
		currentURL = sfBillingInfoPage.getCurrentURL().toLowerCase();
		s_assert.assertTrue(currentURL.contains(urlToAssert), "Expected URL should contain "+urlToAssert+" but actual on UI is"+currentURL);

		// Adding a New Profile with Specific Address 1
		sfBillingInfoPage.clickAddNewBillingProfileLink();
		sfBillingInfoPage.enterUserBillingDetails(TestConstants.CARD_TYPE, TestConstants.CARD_NUMBER_2,cardName, TestConstants.CVV);
		sfBillingInfoPage.clickAddNewBillingAddressLink();
		s_assert.assertTrue(sfBillingInfoPage.isAddNewBillingAddressFormDisplayed(),"ADD NEW BILLING ADDRESS form Block is not Displayed");
		sfBillingInfoPage.enterConsultantAddressDetails(firstName, lastName, addressLine1, addressLine2, city, state, postalCode, phoneNumber);
		sfBillingInfoPage.clickBillingDetailsNextbutton();
		sfBillingInfoPage.clickUseAsEnteredButtonOnPopUp();
		cardLastName = sfBillingInfoPage.getLastName(cardName);
		s_assert.assertTrue(sfBillingInfoPage.isNewBillingProfilePresentInRowList(cardLastName),"New Billing Profile is not present in Profiles List");

		// Edit Specific Billing Profile
		sfBillingInfoPage.clickBillingProfileEditLink(cardLastName);
		s_assert.assertTrue(sfBillingInfoPage.isCardDetailsFieldsDisabled(),"Card Details Fields are not Disabled after Editing Default Billing Profile");
		s_assert.assertTrue(sfBillingInfoPage.isCardTypeIconsDisabled(),"Card Type Icons are not Disabled after Editing Default Billing Profile");
		//   sfBillingInfoPage.clickAddNewBillingAddressLink();
		//   s_assert.assertTrue(sfBillingInfoPage.isAddNewBillingAddressFormDisplayedForExistingProfile(),"ADD NEW BILLING ADDRESS form Block is not Displayed for existing billing profile");

		// Filling Address in Wrong format.
		sfBillingInfoPage.enterBillingAddressDetailsForExistingBillingProfile(firstName, lastName, addressInWrongFormat, addressLine2, city, state, postalCode, phoneNumber);
		sfBillingInfoPage.clickSavePaymentButton();
		s_assert.assertTrue(sfBillingInfoPage.isUnknownAddressErrorMessageIsPresentAsExpectedForExistingProfile(),"Unknown Address Error message is not present");

		// Filling Wrong combination of City and State in Billing address
		sfBillingInfoPage.enterBillingAddressDetailsForExistingBillingProfile(firstName, lastName, addressLine1, addressLine2, wrongCombinationCity, state, postalCode, phoneNumber);
		sfBillingInfoPage.clickSavePaymentButton();
		suggestedBillingAddress = sfBillingInfoPage.getSuggestedBillingAddressFromBlock();
		s_assert.assertTrue(suggestedBillingAddress.contains(city),"Suggested Address City is not found as expected. Expected City : "+city+". Actual City : "+wrongCombinationCity);
		sfBillingInfoPage.clickUseAsEnteredButtonOnPopUp();

		// Filling Specific Address 2 in right format
		sfBillingInfoPage.clickBillingProfileEditLink(cardLastName);
		//   sfBillingInfoPage.clickAddNewBillingAddressLink();
		//   s_assert.assertTrue(sfBillingInfoPage.isAddNewBillingAddressFormDisplayedForExistingProfile(),"ADD NEW BILLING ADDRESS form Block is not Displayed for existing billing profile");
		sfBillingInfoPage.enterBillingAddressDetailsForExistingBillingProfile(firstName, lastName, updatedAddressLine1, updatedAddressLine2, updatedCity, state, updatedPostalCode, phoneNumber);
		sfBillingInfoPage.clickSavePaymentButton();
		sfBillingInfoPage.clickUseAsEnteredButtonOnPopUp();
		billingAddressOnUI = sfBillingInfoPage.getBillingAddressForSpecificBillingProfile(cardLastName);
		s_assert.assertTrue(sfBillingInfoPage.isAddressFieldPresentAsExpectedOnUI(billingAddressOnUI,updatedAddressLine1),"Address Line 1 : " + updatedAddressLine1 + " is not present in updated Billing Address");
		s_assert.assertTrue(sfBillingInfoPage.isAddressFieldPresentAsExpectedOnUI(billingAddressOnUI,updatedAddressLine2),"Address Line 2 : " + updatedAddressLine2 + " is not present in updated Billing Address");
		s_assert.assertTrue(sfBillingInfoPage.isAddressFieldPresentAsExpectedOnUI(billingAddressOnUI,updatedCity),"City : " + updatedCity + " is not present in updated Billing Address");
		s_assert.assertTrue(sfBillingInfoPage.isAddressFieldPresentAsExpectedOnUI(billingAddressOnUI,updatedPostalCode),"PostalCode : " + updatedPostalCode + " is not present in updated Billing Address");
		s_assert.assertTrue(sfBillingInfoPage.isAddressFieldPresentAsExpectedOnUI(billingAddressOnUI,stateAbbreviation),"State : " + stateAbbreviation + " is not present in updated Billing Address");
		s_assert.assertAll();
	}

	/***
	 * qTest : TC-453 Tokenization - My Account
	 * 
	 * Description : This test will validate the newly created Billing profile, credit card last 4 digits and expiry date of card in billing profile details.
	 * 
	 *     
	 */
	@Test(enabled=true)
	public void testTokenizationMyAccount_453(){
		String currentURL = null;
		String lastFourDigitOfCard = null;
		String billingAddressOnUI = null;
		String cardNum = TestConstants.CARD_NUMBER_2;
		String randomWord = CommonUtils.getRandomWord(5);
		String cardName = TestConstants.CARD_NAME +  randomWord;
		String urlToAssert = "payment-details";
		String firstName = TestConstants.FIRST_NAME;
		String lastName = TestConstants.LAST_NAME;
		String addressLine1 = TestConstants.ADDRESS_LINE_1_US;
		String addressLine2 = TestConstants.ADDRESS_LINE_2_US;
		String city = TestConstants.CITY_US;
		String state = TestConstants.STATE_US;
		String postalCode = TestConstants.POSTAL_CODE_US;
		String phoneNumber = TestConstants.PHONE_NUMBER;
		String stateAbbreviation = TestConstants.STATE_US_ABBREVIATION;
		String cardLastName = null;
		//Login as Consultant.
		sfHomePage.loginToStoreFront(TestConstants.CONSULTANT_EMAIL_WITH_CRP_AND_PULSE,  password,true);
		sfHomePage.clickWelcomeDropdown();
		sfBillingInfoPage = sfHomePage.navigateToBillingInfoPage();
		currentURL = sfBillingInfoPage.getCurrentURL().toLowerCase();
		s_assert.assertTrue(currentURL.contains(urlToAssert), "Expected URL should contain " + urlToAssert + " but actual on UI is " + currentURL);
		sfBillingInfoPage.clickAddNewBillingProfileLink();
		sfBillingInfoPage.enterUserBillingDetails(TestConstants.CARD_TYPE, cardNum,cardName, TestConstants.CVV);
		sfBillingInfoPage.clickAddNewBillingAddressLink();
		s_assert.assertTrue(sfBillingInfoPage.isAddNewBillingAddressFormDisplayed(),"ADD NEW BILLING ADDRESS form Block is not Displayed");
		sfBillingInfoPage.enterConsultantAddressDetails(firstName, lastName, addressLine1, addressLine2, city, state, postalCode, phoneNumber);
		sfBillingInfoPage.clickBillingDetailsNextbutton();
		sfBillingInfoPage.clickUseAsEnteredButtonOnPopUp();
		cardLastName = sfBillingInfoPage.getLastName(cardName);
		s_assert.assertTrue(sfBillingInfoPage.isNewBillingProfilePresentInRowList(cardLastName),"New Billing Profile is not present in Profiles List");
		lastFourDigitOfCard = sfBillingInfoPage.getLastFourDigitOfCreditCardNumberForSpecificBillingProfile(cardLastName);
		s_assert.assertTrue(sfBillingInfoPage.isLastFourDigitMatchesWithSixteenDigitCardNumber(cardNum,lastFourDigitOfCard),"Last 4 Digits of Card Number on UI are not found as Expected. Card Number : " + cardNum + ". Acutal 4 Digits on UI : " +lastFourDigitOfCard);
		s_assert.assertTrue(sfBillingInfoPage.isExpiryDateOfCardIsPresentForSpecificProfile(cardLastName),"Expiry Date is not found as Expected on UI");
		billingAddressOnUI = sfBillingInfoPage.getBillingAddressForSpecificBillingProfile(cardLastName);
		// Asserting Billing Address Details
		s_assert.assertTrue(sfBillingInfoPage.isAddressFieldPresentAsExpectedOnUI(billingAddressOnUI,addressLine1),"Address Line 1 : " + addressLine1 + " is not present in Billing Address");
		s_assert.assertTrue(sfBillingInfoPage.isAddressFieldPresentAsExpectedOnUI(billingAddressOnUI,addressLine2),"Address Line 2 : " + addressLine2 + " is not present in Billing Address");
		s_assert.assertTrue(sfBillingInfoPage.isAddressFieldPresentAsExpectedOnUI(billingAddressOnUI,city),"City : " + city + " is not present in Billing Address");
		s_assert.assertTrue(sfBillingInfoPage.isAddressFieldPresentAsExpectedOnUI(billingAddressOnUI,postalCode),"PostalCode : " + postalCode + " is not present in Billing Address");
		s_assert.assertTrue(sfBillingInfoPage.isAddressFieldPresentAsExpectedOnUI(billingAddressOnUI,stateAbbreviation),"State : " + stateAbbreviation + " is not present in Billing Address");
		s_assert.assertAll();
	}
	/***
	 * qTest : TC-454 Tokenization - Checkout
	 * 
	 * Description : This test will create a new billing profile on checkout page and validate the following entities - billing address, last 4 digit of card and expiry date.   
	 * 
	 *     
	 */
	@Test(enabled=true)
	public void testTokenizationCheckout_454(){
		String currentURL = null;
		String lastFourDigitOfCard = null;
		String billingDetailsOnUI = null;
		String expiryDate = null;
		String cardNum = TestConstants.CARD_NUMBER_2;
		String randomWord = CommonUtils.getRandomWord(5);
		String cardName =  TestConstants.CARD_NAME + randomWord;
		String urlToAssert = "/checkout/multi/";
		String firstName = TestConstants.FIRST_NAME;
		String lastName = TestConstants.LAST_NAME;
		String addressLine1 = TestConstants.ADDRESS_LINE_1_US;
		String addressLine2 = TestConstants.ADDRESS_LINE_2_US;
		String city = TestConstants.CITY_US;
		String state = TestConstants.STATE_US;
		String postalCode = TestConstants.POSTAL_CODE_US;
		String phoneNumber = TestConstants.PHONE_NUMBER;
		String stateAbbreviation = TestConstants.STATE_US_ABBREVIATION;
		String cardLastName = null;

		//Login as Consultant.
		sfHomePage.loginToStoreFront(TestConstants.CONSULTANT_EMAIL_WITH_CRP_AND_PULSE,  password,true);
		sfShopSkinCarePage = sfHomePage.clickAllProducts();
		sfShopSkinCarePage.addProductToCart(TestConstants.PRODUCT_NUMBER, TestConstants.ORDER_TYPE_ADHOC);
		sfCartPage = sfShopSkinCarePage.checkoutTheCartFromPopUp();
		sfCheckoutPage = sfCartPage.checkoutTheCart();
		currentURL = sfCheckoutPage.getCurrentURL().toLowerCase();
		s_assert.assertTrue(currentURL.contains(urlToAssert), "Expected URL should contain "+urlToAssert+" but actual on UI is "+currentURL);
		sfCheckoutPage.clickSaveButton();
		sfCheckoutPage.clickShippingDetailsNextbutton();
		sfCheckoutPage.clickAddNewBillingProfileButton();
		sfCheckoutPage.enterUserBillingDetails(TestConstants.CARD_TYPE, cardNum, cardName, TestConstants.CVV);
		sfCheckoutPage.checkUseMyDeliveryAddressChkBox();
		sfCheckoutPage.enterConsultantAddressDetails(firstName, lastName, addressLine1, addressLine2, city, state, postalCode, phoneNumber);
		sfCheckoutPage.clickBillingDetailsNextbutton();
		sfCheckoutPage.clickUseAsEnteredButtonOnPopUp();
		cardLastName = sfCheckoutPage.getLastName(cardName); 
		s_assert.assertTrue(sfCheckoutPage.isNewBillingDetailsVisibleOnUI(cardLastName),"New Billing Details do not get updated as Default Billing details on Checkout Page");
		billingDetailsOnUI = sfCheckoutPage.getBillingProfileDetailsFromBillingProfile();
		sfCheckoutPage.getCardDetailsFromBillingInfo(cardLastName);
		lastFourDigitOfCard = sfCheckoutPage.getLastFourDigitsOfCardNumberInBillingDetails();
		expiryDate = sfCheckoutPage.getExpiryDateOfCardNumberInBillingDetails();
		s_assert.assertTrue(cardNum.endsWith(lastFourDigitOfCard),"Last 4 Digits of Card Number on UI are not found as Expected. Card Number : " + cardNum + ". Acutal 4 Digits on UI : " +lastFourDigitOfCard);
		s_assert.assertTrue(sfCheckoutPage.isExpiryDateIsPresentAsExpectedInBillingDetails(expiryDate),"Expiry Date on UI is not found as Expected. Expiry Date on UI: "+expiryDate);
		s_assert.assertTrue(sfCheckoutPage.isAddressFieldPresentAsExpectedOnUI(billingDetailsOnUI,addressLine1),"Address Line 1 : " + addressLine1 + " is not present in Billing Address");
		s_assert.assertTrue(sfCheckoutPage.isAddressFieldPresentAsExpectedOnUI(billingDetailsOnUI,city),"City : " + city + " is not present in updated Billing Address");
		s_assert.assertTrue(sfCheckoutPage.isAddressFieldPresentAsExpectedOnUI(billingDetailsOnUI,postalCode),"PostalCode : " + postalCode + " is not present in Billing Address");
		s_assert.assertTrue(sfCheckoutPage.isAddressFieldPresentAsExpectedOnUI(billingDetailsOnUI,stateAbbreviation),"State : " + stateAbbreviation + " is not present in Billing Address");
		s_assert.assertAll();
	}
}