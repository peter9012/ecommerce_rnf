package com.rf.test.website.rehabitat.storeFront;

import org.testng.annotations.Test;

import com.rf.core.utils.CommonUtils;
import com.rf.core.website.constants.TestConstants;
import com.rf.pages.website.rehabitat.storeFront.StoreFrontCartPage;
import com.rf.pages.website.rehabitat.storeFront.StoreFrontShopSkinCarePage;
import com.rf.test.website.rehabitat.storeFront.baseTest.StoreFrontWebsiteBaseTest;

public class PCEnrollmentTest extends StoreFrontWebsiteBaseTest{
	public String email=null;
	private String firstName = null;
	private String lastName = null;
	String addressLine1 = null;
	String addressLine2 = null;
	String city = null;
	String state = null;
	String postalCode = null;
	String phoneNumber = null;
	String cardType = null;
	String cardNumber = null;
	String cardName = null;
	String CVV = null;
	int randomNum; 

	public PCEnrollmentTest() {
		randomNum = CommonUtils.getRandomNum(10000, 1000000);
		firstName=TestConstants.PC_FIRST_NAME;
		lastName = TestConstants.LAST_NAME;
		email = firstName+randomNum+TestConstants.EMAIL_SUFFIX;
		addressLine1 = TestConstants.ADDRESS_LINE_1_US;
		addressLine2 = TestConstants.ADDRESS_LINE_2_US;
		city = TestConstants.CITY_US;
		state = TestConstants.STATE_US;
		postalCode = TestConstants.POSTAL_CODE_US;
		phoneNumber = TestConstants.PHONE_NUMBER;
		cardType = TestConstants.CARD_TYPE;
		cardNumber = TestConstants.CARD_NUMBER;
		cardName = TestConstants.CARD_NAME;
		CVV = TestConstants.CVV;
	}

	/***
	 * qTest : TC-452 PC User Enrollment/Checkout - Complete Enrollment
	 * Description : This test is for successfully enrolling a PC user
	 * 
	 *     
	 */
	@Test
	public void testPCEnrollment_452(){
		sfCartPage = new StoreFrontCartPage(driver);
		sfShopSkinCarePage = new StoreFrontShopSkinCarePage(driver);
		sfHomePage.clickLoginIcon();
		sfCheckoutPage=sfHomePage.clickSignUpNowLink();
		sfCheckoutPage.fillNewUserDetails(TestConstants.USER_TYPE_PC, firstName, lastName, email, password);
		sfCheckoutPage.clickCreateAccountButton();
		s_assert.assertTrue(sfCartPage.isPcOneTimeFeeMsgDisplayed(),"PC one time joining fee msg has not displayed");
		sfCartPage.clickAddMoreItemsBtn();
		sfShopSkinCarePage.selectFirstProduct();
		sfCartPage = sfShopSkinCarePage.checkoutTheCartFromPopUp();
		sfCartPage.enterQuantityOfProductAtCart("1", "10");
		sfCartPage.clickOnUpdateLinkThroughItemNumber("1");
		sfCartPage.clickCheckoutBtn();
		sfCheckoutPage.clickContinueWithoutConsultantLink();
		sfCheckoutPage.clickSaveButton();
		sfCheckoutPage.enterShippingDetails(firstName+" "+lastName, addressLine1, addressLine2, city, state, postalCode, phoneNumber);
		sfCheckoutPage.clickShippingDetailsNextbutton();
		sfCheckoutPage.enterUserBillingDetails(cardType, cardNumber, cardName, CVV);
		sfCheckoutPage.checkUseMyDeliveryAddressChkBox();
		sfCheckoutPage.clickBillingDetailsNextbutton();
		sfCheckoutPage.selectTermsAndConditionsChkBox();
		sfCheckoutPage.selectPoliciesAndProceduresChkBox();
		sfCheckoutPage.clickPlaceOrderButton();
		s_assert.assertTrue(sfHomePage.hasPCEnrolledSuccessfully(), "PC has not been enrolled successfully");
		s_assert.assertAll();
	}
	
	/***
	 * qTest : TC-451 PC User Enrollment/Checkout - Incomplete Enrollment
	 * Description : This test is for incomplete enrollment of  a PC user
	 * 
	 *     
	 */
	@Test
	public void testPCIncompleteEnrollment_451(){
		sfCartPage = new StoreFrontCartPage(driver);
		sfShopSkinCarePage = new StoreFrontShopSkinCarePage(driver);
		sfHomePage.clickLoginIcon();
		sfCheckoutPage=sfHomePage.clickSignUpNowLink();
		sfCheckoutPage.fillNewUserDetails(TestConstants.USER_TYPE_PC, firstName, lastName, email, password);
		sfCheckoutPage.clickCreateAccountButton();
		s_assert.assertTrue(sfCartPage.isPcOneTimeFeeMsgDisplayed(),"PC one time joining fee msg has not displayed");
		s_assert.assertTrue(sfHomePage.hasPCEnrolledSuccessfully(), "PC has not been enrolled successfully");
		sfHomePage.clickWelcomeDropdown();
		sfHomePage.logout();
		sfHomePage.loginToStoreFront(email, password);
		s_assert.assertFalse(sfHomePage.isWelcomeUserElementDisplayed(), "Incomplete enrolled PC user "+email+"  has successfuly logged in");
		s_assert.assertAll();
	}
	
	/***
	 * qTest : TC-480 PC User Enrollment/Checkout - T&C checkbox selected
	 * Description : This test is for successfully enrolling a PC user
	 * with T&C checkbox Selected
	 * 
	 *     
	 */
	@Test
	public void testPCEnrollmentTCCheckboxSelected_480(){
		//duplicate test,same as testPCEnrollment_452
	}
	
	/***
	 * qTest : TC-481 PC User Enrollment/Checkout - T&C checkbox unselected
	 *  Description : This test is for successfully enrolling a PC user
	 * with T&C checkbox NOT selected
	 * 
	 *     
	 */
	@Test
	public void testPCEnrollmentTCCheckboxNotSelected_481(){
		sfCartPage = new StoreFrontCartPage(driver);
		sfShopSkinCarePage = new StoreFrontShopSkinCarePage(driver);
		sfHomePage.clickLoginIcon();
		sfCheckoutPage=sfHomePage.clickSignUpNowLink();
		sfCheckoutPage.fillNewUserDetails(TestConstants.USER_TYPE_PC, firstName, lastName, email, password);
		sfCheckoutPage.clickCreateAccountButton();
		s_assert.assertTrue(sfCartPage.isPcOneTimeFeeMsgDisplayed(),"PC one time joining fee msg has not displayed");
		sfCartPage.clickAddMoreItemsBtn();
		sfShopSkinCarePage.selectFirstProduct();
		sfCartPage = sfShopSkinCarePage.checkoutTheCartFromPopUp();
		sfCartPage.enterQuantityOfProductAtCart("1", "10");
		sfCartPage.clickOnUpdateLinkThroughItemNumber("1");
		sfCartPage.clickCheckoutBtn();
		sfCheckoutPage.clickContinueWithoutConsultantLink();
		sfCheckoutPage.clickSaveButton();
		sfCheckoutPage.enterShippingDetails(firstName+" "+lastName, addressLine1, addressLine2, city, state, postalCode, phoneNumber);
		sfCheckoutPage.clickShippingDetailsNextbutton();
		sfCheckoutPage.enterUserBillingDetails(cardType, cardNumber, cardName, CVV);
		sfCheckoutPage.checkUseMyDeliveryAddressChkBox();
		sfCheckoutPage.clickBillingDetailsNextbutton();
		sfCheckoutPage.clickPlaceOrderButton();
		s_assert.assertTrue(sfCheckoutPage.isPopUpForTermsAndConditionsCheckboxDisplayed(), "Pop up has not been displayed after NOT selecting the T&C checkboxes");
		s_assert.assertAll();
	}
	
	/***
	 * qTest : TC-495 PC user can select sponsor from corp
	 * Description : This test is for checking the functionality of selecting the 
	 * sponsor during PC enrollment
	 * 
	 *     
	 */
	@Test
	public void testPCEnrollmentSelectSponsor_495(){
		String allProduct = "ALL PRODUCTS";
		String dummyConsultant = "xxx";
		String consultantWith2Chars = "Pu";
		String existingConsultant = TestConstants.SPONSOR;
		sfCartPage = new StoreFrontCartPage(driver);
		sfShopSkinCarePage = sfHomePage.navigateToShopSkincareLink(allProduct);
		sfShopSkinCarePage.addFirstProductToBag();
		sfShopSkinCarePage.checkoutTheCartFromPopUp();
		sfCheckoutPage=sfShopSkinCarePage.checkoutTheCart();
		sfCheckoutPage.fillNewUserDetails(TestConstants.USER_TYPE_PC, firstName, lastName, email, password);
		sfCheckoutPage.clickCreateAccountButton();
		sfCheckoutPage = sfCartPage.clickCheckoutBtn();
		sfCheckoutPage.searchForConsultant(dummyConsultant);
		s_assert.assertTrue(sfCheckoutPage.isNoResultFoundMsgDisplayedForSearchedConsultant(), "No results found for msg has NOT displayed for "+dummyConsultant);
		sfCheckoutPage.searchForConsultant(consultantWith2Chars);
		s_assert.assertTrue(sfCheckoutPage.isValidationMsgDisplayedForSearchedConsultant(), "Please enter at least 3 characters msg has NOT displayed for "+consultantWith2Chars);
		sfCheckoutPage.searchForConsultant(existingConsultant);
		s_assert.assertTrue(sfCheckoutPage.isSponsorResultDisplayed(),"No result found after searching the sponsor with name "+TestConstants.SPONSOR);
		sfCheckoutPage.selectFirstSponsorFromList();
		s_assert.assertTrue(sfCheckoutPage.isSelectedConsultantBoxDisplayed(), "consultant box of the selected sponsor has NOT displayed");
		sfHomePage.clickRemoveLink();
		s_assert.assertFalse(sfCheckoutPage.isSelectedConsultantBoxDisplayed(), "consultant box of the selected sponsor has NOT removed");
		sfCheckoutPage.searchForConsultant(existingConsultant);
		s_assert.assertTrue(sfCheckoutPage.isSponsorResultDisplayed(),"No result found after searching the sponsor with name "+TestConstants.SPONSOR);
		s_assert.assertAll();
	}
	
}