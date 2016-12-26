package com.rf.test.website.rehabitat.storeFront;

import org.testng.annotations.Test;

import com.rf.core.utils.CommonUtils;
import com.rf.core.website.constants.TestConstants;
import com.rf.test.website.rehabitat.storeFront.baseTest.StoreFrontWebsiteBaseTest;

public class BillingProfileTest extends StoreFrontWebsiteBaseTest{

	/***
	 * qTest : TC-385 Add New Billing Address on Existing Billing Profile
	 * 
	 * Description : This test will add a new billing address to existing billing profile 
	 * on billing info page.
	 * 
	 *     
	 */
	@Test//Incomplete billing profile is not present on billing info page
	public void testAddNewBillingAddressToExistingBillingProfile_385(){
		String currentURL = null;
		String urlToAssert = "payment-details";
		//Login as consultant user.
		sfHomePage.loginToStoreFront(TestConstants.CONSULTANT_USERNAME, password);
		sfHomePage.clickWelcomeDropdown();
		sfBillingInfoPage = sfHomePage.navigateToBillingInfoPage();
		currentURL = sfAccountInfoPage.getCurrentURL().toLowerCase();
		s_assert.assertTrue(currentURL.contains(urlToAssert), "Expected URL should contain "+urlToAssert+" but actual on UI is"+currentURL);
		//Click Edit Of randomely choosen Billing Profile and click add new address.
		//sfBillingInfoPage.clicke
		s_assert.assertAll();
	}

	/***
	 * qTest : TC-386 Add Billing Profile With New Billing Address
	 * 
	 * Description : This test will add a new billing address to newly created billing profile 
	 * on billing info page.
	 * 
	 *     
	 */
	@Test
	public void testAddNewBillingAddressToNewlyCreatedBillingProfile_386(){
		String currentURL = null;
		String randomWord = CommonUtils.getRandomWord(5);
		String suggestedBillingAddress = null;
		String urlToAssert = "payment-details";
		String firstName = TestConstants.FIRST_NAME + randomWord;
		String lastName = TestConstants.LAST_NAME;
		String addressLine1 = TestConstants.ADDRESS_LINE_1_US;
		String addressLine2 = TestConstants.ADDRESS_LINE_2_US;
		String city = TestConstants.CITY_US;
		String state = TestConstants.STATE_US;
		String postalCode = TestConstants.POSTAL_CODE_US;
		String phoneNumber = TestConstants.PHONE_NUMBER;
		String addressInWrongFormat = TestConstants.WRONG_ADDRESS_LINE_1_US;
		String wrongCombinationCity = TestConstants.CITY_DALLAS_US;
		//Login as consultant user.
		sfHomePage.loginToStoreFront(TestConstants.CONSULTANT_USERNAME, password);
		sfHomePage.clickWelcomeDropdown();
		sfBillingInfoPage = sfHomePage.navigateToBillingInfoPage();
		currentURL = sfBillingInfoPage.getCurrentURL().toLowerCase();
		s_assert.assertTrue(currentURL.contains(urlToAssert), "Expected URL should contain "+urlToAssert+" but actual on UI is"+currentURL);
		sfBillingInfoPage.clickAddNewBillingProfileLink();
		s_assert.assertTrue(sfBillingInfoPage.isAddNewBillingInfoSubHeaderPresent(),"ADD NEW BILLING PROFILE Sub Header is not present");
		sfBillingInfoPage.enterConsultantBillingDetails(TestConstants.CARD_TYPE, TestConstants.CARD_NUMBER, TestConstants.CARD_NAME, TestConstants.CVV);
		sfBillingInfoPage.clickAddNewBillingAddressLink();
		s_assert.assertTrue(sfBillingInfoPage.isAddNewBillingAddressFormDisplayed(),"ADD NEW BILLING ADDRESS form Block is not Displayed");
		s_assert.assertTrue(sfBillingInfoPage.isAddNewBillingAddressBlockHeaderPresent(),"ADD NEW BILLING ADDRESS Sub Header is not present");
		// Filling Address in Wrong format.
		sfBillingInfoPage.enterConsultantAddressDetails(firstName, lastName, addressInWrongFormat, addressLine2, city, state, postalCode, phoneNumber);
		sfBillingInfoPage.clickSaveButtonForBillingDetails();
		s_assert.assertTrue(sfBillingInfoPage.isUnknownAddressErrorMessageIsPresentAsExpected(),"Unknown Address Error message is not present");
		// Filling Wrong combination of City and State in Billing address
		sfBillingInfoPage.enterConsultantAddressDetails(firstName, lastName, addressLine1, addressLine2, wrongCombinationCity, state, postalCode, phoneNumber);
		sfBillingInfoPage.clickSaveButtonForBillingDetails();
		suggestedBillingAddress = sfBillingInfoPage.getSuggestedBillingAddressFromBlock();
		s_assert.assertTrue(suggestedBillingAddress.contains(city),"Suggested Address City is not found as expected. Expected City : "+city+". Actual City : "+wrongCombinationCity);
		sfBillingInfoPage.clickOnAddressSuggestionModalCloseBtn();
		// Filling Right Address 
		sfBillingInfoPage.enterConsultantAddressDetails(firstName, lastName, addressLine1, addressLine2, city, state, postalCode, phoneNumber);
		sfBillingInfoPage.clickSaveButtonForBillingDetails();
		sfBillingInfoPage.clickUseAsEnteredButtonOnPopUp();
		s_assert.assertTrue(sfBillingInfoPage.isCardDetailsAddedSuccessfulMsgAppearedAsExpected(),"Card Details Added SuccessFul Msg is not present as Expected");
		s_assert.assertTrue(sfBillingInfoPage.isNewBillingProfilePresentInRowList(firstName),"New Billing Profile is not present in Profiles List");
		s_assert.assertAll();
	}
}