package com.rf.test.website.rehabitat.storeFront;

import org.testng.annotations.Test;

import com.rf.core.utils.CommonUtils;
import com.rf.core.website.constants.TestConstants;
import com.rf.pages.website.rehabitat.storeFront.StoreFrontCartPage;
import com.rf.pages.website.rehabitat.storeFront.StoreFrontShopSkinCarePage;
import com.rf.test.website.rehabitat.storeFront.baseTest.StoreFrontWebsiteBaseTest;

public class PCEnrollmentTest extends StoreFrontWebsiteBaseTest{
	private String email=null;
	private String firstName = null;
	private String lastName = null;
	private String addressLine1 = null;
	private String addressLine2 = null;
	private String city = null;
	private String state = null;
	private String postalCode = null;
	private String phoneNumber = null;
	private String cardType = null;
	private String cardNumber = null;
	private String cardName = null;
	private String CVV = null;
	private String timeStamp=null;
	private String randomWords = null;

	public PCEnrollmentTest() {
		firstName=TestConstants.PC_FIRST_NAME;
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
	@Test(enabled=true)
	public void testPCEnrollment_452(){
		timeStamp = CommonUtils.getCurrentTimeStamp();
		randomWords = CommonUtils.getRandomWord(5);		
		lastName = TestConstants.LAST_NAME+randomWords;
		email = firstName+timeStamp+TestConstants.EMAIL_SUFFIX;
		sfCartPage = new StoreFrontCartPage(driver);
		sfShopSkinCarePage = new StoreFrontShopSkinCarePage(driver);
		sfHomePage.clickLoginIcon();
		sfCheckoutPage=sfHomePage.clickSignUpNowLink();
		sfCheckoutPage.fillNewUserDetails(TestConstants.USER_TYPE_PC, firstName, lastName, email, password);
		sfCheckoutPage.clickCreateAccountButton();
		s_assert.assertTrue(sfCartPage.isPcOneTimeFeeMsgDisplayed(),"PC one time joining fee msg has not displayed");
		sfCartPage.clickAddMoreItemsBtn();
		//sfShopSkinCarePage=sfCartPage.clickAllProducts();// this is a temporary patch
		sfShopSkinCarePage.addProductToCart(TestConstants.PRODUCT_NUMBER, TestConstants.ORDER_TYPE_ADHOC);;
		sfCartPage = sfShopSkinCarePage.checkoutTheCartFromPopUp();
		sfCartPage.enterQuantityOfProductAtCart("1", "2");
		sfCartPage.clickOnUpdateLinkThroughItemNumber("1");
		sfCartPage.clickCheckoutBtn();
		sfCheckoutPage.clickContinueWithoutConsultantLink();
		sfCheckoutPage.clickSaveButton();
		sfCheckoutPage.enterShippingDetails(firstName+" "+lastName, addressLine1, addressLine2, city, state, postalCode, phoneNumber);
		sfCheckoutPage.clickShippingDetailsNextbutton();
		sfCheckoutPage.enterUserBillingDetails(cardType, cardNumber, cardName, CVV);
		//		sfCheckoutPage.checkUseMyDeliveryAddressChkBox();
		sfCheckoutPage.clickBillingDetailsNextbutton();
		sfCheckoutPage.selectIAcknowledgePCChkBox();
		sfCheckoutPage.selectPCTermsAndConditionsChkBox();
		sfCheckoutPage.clickPlaceOrderButton();
		sfCheckoutPage.clickRodanAndFieldsLogo();
		s_assert.assertTrue(sfHomePage.hasPCEnrolledSuccessfully(), "PC has not been enrolled successfully");
		s_assert.assertAll();
	}

	/***
	 * qTest : TC-451 PC User Enrollment/Checkout - Incomplete Enrollment
	 * Description : This test is for incomplete enrollment of  a PC user
	 * 
	 *     
	 */
	@Test(enabled=true)
	public void testPCIncompleteEnrollment_451(){
		timeStamp = CommonUtils.getCurrentTimeStamp();
		randomWords = CommonUtils.getRandomWord(5);		
		lastName = TestConstants.LAST_NAME+randomWords;
		email = firstName+timeStamp+TestConstants.EMAIL_SUFFIX;
		sfCartPage = new StoreFrontCartPage(driver);
		sfShopSkinCarePage = new StoreFrontShopSkinCarePage(driver);
		sfHomePage.clickLoginIcon();
		sfCheckoutPage=sfHomePage.clickSignUpNowLink();
		sfCheckoutPage.fillNewUserDetails(TestConstants.USER_TYPE_PC, firstName, lastName, email, password);
		sfCheckoutPage.clickCreateAccountButton();
//		s_assert.assertTrue(sfCartPage.isPcOneTimeFeeMsgDisplayed(),"PC one time joining fee msg has not displayed");
		s_assert.assertTrue(sfHomePage.isWelcomeUserElementDisplayed(), "PC has not been enrolled partially");
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
	@Test(enabled=true)
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
	@Test(enabled=true)
	public void testPCEnrollmentTCCheckboxNotSelected_481(){
		timeStamp = CommonUtils.getCurrentTimeStamp();
		randomWords = CommonUtils.getRandomWord(5);		
		lastName = TestConstants.LAST_NAME+randomWords;
		email = firstName+timeStamp+TestConstants.EMAIL_SUFFIX;
		sfCartPage = new StoreFrontCartPage(driver);
		sfShopSkinCarePage = new StoreFrontShopSkinCarePage(driver);
		sfHomePage.clickLoginIcon();
		sfCheckoutPage=sfHomePage.clickSignUpNowLink();
		sfCheckoutPage.fillNewUserDetails(TestConstants.USER_TYPE_PC, firstName, lastName, email, password);
		sfCheckoutPage.clickCreateAccountButton();
		s_assert.assertTrue(sfCartPage.isPcOneTimeFeeMsgDisplayed(),"PC one time joining fee msg has not displayed");
		sfCartPage.clickAddMoreItemsBtn();
		sfShopSkinCarePage.addProductToCart(TestConstants.PRODUCT_NUMBER, TestConstants.ORDER_TYPE_ADHOC);;
		sfCartPage = sfShopSkinCarePage.checkoutTheCartFromPopUp();
		sfCartPage.enterQuantityOfProductAtCart("1", "10");
		sfCartPage.clickOnUpdateLinkThroughItemNumber("1");
		sfCartPage.clickCheckoutBtn();
		sfCheckoutPage.clickContinueWithoutConsultantLink();
		sfCheckoutPage.clickSaveButton();
		sfCheckoutPage.enterShippingDetails(firstName+" "+lastName, addressLine1, addressLine2, city, state, postalCode, phoneNumber);
		sfCheckoutPage.clickShippingDetailsNextbutton();
		sfCheckoutPage.enterUserBillingDetails(cardType, cardNumber, cardName, CVV);
		//sfCheckoutPage.checkUseMyDeliveryAddressChkBox();
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
	@Test(enabled=true)
	public void testPCEnrollmentSelectSponsor_495(){
		timeStamp = CommonUtils.getCurrentTimeStamp();
		randomWords = CommonUtils.getRandomWord(5);		
		lastName = TestConstants.LAST_NAME+randomWords;
		email = firstName+timeStamp+TestConstants.EMAIL_SUFFIX;
		String allProduct = "ALL PRODUCTS";
		String dummyConsultant = "xxx";
		String consultantWith2Chars = "Pu";
		String existingConsultant = TestConstants.SPONSOR;
		sfCartPage = new StoreFrontCartPage(driver);
		sfShopSkinCarePage = sfHomePage.navigateToShopSkincareLink(allProduct);
		sfShopSkinCarePage.addProductToCart(TestConstants.PRODUCT_NUMBER, TestConstants.ORDER_TYPE_ADHOC);;
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

	/***
	 * qTest : TC-573 Choose a Consultant- Auto-Assign (Checkout)
	 * Description : This test validates the assigned sponsor to PC after enrollment
	 * 
	 *     
	 */
	@Test(enabled=false)
	public void testAutoAssignConsultantPcEnrollment_573(){
		timeStamp = CommonUtils.getCurrentTimeStamp();
		randomWords = CommonUtils.getRandomWord(5);		
		lastName = TestConstants.LAST_NAME+randomWords;
		email = firstName+timeStamp+TestConstants.EMAIL_SUFFIX;
		String sponserName = null;
		String sponserNameAfterEnrollment = null;
		String existingConsultant = TestConstants.SPONSOR;
		sfCartPage = new StoreFrontCartPage(driver);
		sfShopSkinCarePage = new StoreFrontShopSkinCarePage(driver);
		sfHomePage.clickLoginIcon();
		sfCheckoutPage=sfHomePage.clickSignUpNowLink();
		sfCheckoutPage.fillNewUserDetails(TestConstants.USER_TYPE_PC, firstName, lastName, email, password);
		sfCheckoutPage.clickCreateAccountButton();
		s_assert.assertTrue(sfCartPage.isPcOneTimeFeeMsgDisplayed(),"PC one time joining fee msg has not displayed");
		sfCartPage.clickAddMoreItemsBtn();
		sfShopSkinCarePage.addProductToCart(TestConstants.PRODUCT_NUMBER, TestConstants.ORDER_TYPE_ADHOC);;
		sfCartPage = sfShopSkinCarePage.checkoutTheCartFromPopUp();
		//sfCartPage.enterQuantityOfProductAtCart("1", "10");
		//sfCartPage.clickOnUpdateLinkThroughItemNumber("1");
		sfCartPage.clickCheckoutBtn();
		//Choose sponser and main account fields are autofilled.
		sfCheckoutPage.searchForConsultant(existingConsultant);
		s_assert.assertTrue(sfCheckoutPage.isSponsorResultDisplayed(),"No result found after searching the sponsor with name "+TestConstants.SPONSOR);
		sponserName = sfCheckoutPage.selectAndReturnFirstSponsorFromList();
		sfCheckoutPage.clickSaveButton();
		//Fill shipping details.
		sfCheckoutPage.enterShippingDetails(firstName+" "+lastName, addressLine1, addressLine2, city, state, postalCode, phoneNumber);
		sfCheckoutPage.clickShippingDetailsNextbutton();
		sfCheckoutPage.enterUserBillingDetails(cardType, cardNumber, cardName, CVV);
		//sfCheckoutPage.checkUseMyDeliveryAddressChkBox();
		sfCheckoutPage.clickBillingDetailsNextbutton();
		sfCheckoutPage.selectTermsAndConditionsChkBox();
		sfCheckoutPage.selectPoliciesAndProceduresChkBox();
		sfCheckoutPage.clickPlaceOrderButton();
		sfCheckoutPage.clickRodanAndFieldsLogo();
		s_assert.assertTrue(sfHomePage.hasPCEnrolledSuccessfully(), "PC has not been enrolled successfully");
		//Verify the sponser details while placing order.
		sfShopSkinCarePage = sfHomePage.clickAllProducts();
		sfShopSkinCarePage.addProductToCart(TestConstants.PRODUCT_NUMBER, TestConstants.ORDER_TYPE_ADHOC);;
		sfCartPage = sfShopSkinCarePage.checkoutTheCartFromPopUp();
		sfCheckoutPage = sfCartPage.clickCheckoutBtn();
		sponserNameAfterEnrollment = sfCheckoutPage.getSponsorNameFromAccountInfo();
		s_assert.assertTrue(sponserNameAfterEnrollment.toLowerCase().contains(sponserName.toLowerCase()),"Sponser name is not same as provided during enrollment");
		s_assert.assertAll();
	}

	/***
	 * qTest : TC-574 Choose a Consultant- Auto-Assign (Checkout) - Change sponsor
	 * Description : This test validates the re-assigned sponsor to PC after enrollment
	 * 
	 *     
	 */
	@Test(enabled=false)
	public void testUpdateSponserDuringPCEnrollment_574(){
		timeStamp = CommonUtils.getCurrentTimeStamp();
		randomWords = CommonUtils.getRandomWord(5);		
		lastName = TestConstants.LAST_NAME+randomWords;
		email = firstName+timeStamp+TestConstants.EMAIL_SUFFIX;
		String sponserName = null;
		String secondSponser = null;
		String sponserNameAfterEnrollment = null;
		String existingConsultant = TestConstants.SPONSOR;
		sfCartPage = new StoreFrontCartPage(driver);
		sfShopSkinCarePage = new StoreFrontShopSkinCarePage(driver);
		sfHomePage.clickLoginIcon();
		sfCheckoutPage=sfHomePage.clickSignUpNowLink();
		sfCheckoutPage.fillNewUserDetails(TestConstants.USER_TYPE_PC, firstName, lastName, email, password);
		sfCheckoutPage.clickCreateAccountButton();
		s_assert.assertTrue(sfCartPage.isPcOneTimeFeeMsgDisplayed(),"PC one time joining fee msg has not displayed");
		sfCartPage.clickAddMoreItemsBtn();
		sfShopSkinCarePage.addProductToCart(TestConstants.PRODUCT_NUMBER, TestConstants.ORDER_TYPE_ADHOC);;
		sfCartPage = sfShopSkinCarePage.checkoutTheCartFromPopUp();
		sfCartPage.clickCheckoutBtn();
		//Choose sponser and main account fields are autofilled.
		sfCheckoutPage.searchForConsultant(existingConsultant);
		s_assert.assertTrue(sfCheckoutPage.isSponsorResultDisplayed(),"No result found after searching the sponsor with name "+TestConstants.SPONSOR);
		sponserName = sfCheckoutPage.selectAndReturnFirstSponsorFromList();
		sfCheckoutPage.clickRemoveLink();
		sfCheckoutPage.searchForConsultant(existingConsultant);
		secondSponser = sfCheckoutPage.selectAndReturnSponsorFromList("2");
		sfCheckoutPage.clickSaveButton();
		//Fill shipping details.
		sfCheckoutPage.enterShippingDetails(firstName+" "+lastName, addressLine1, addressLine2, city, state, postalCode, phoneNumber);
		sfCheckoutPage.clickShippingDetailsNextbutton();
		sfCheckoutPage.enterUserBillingDetails(cardType, cardNumber, cardName, CVV);
		sfCheckoutPage.clickBillingDetailsNextbutton();
		sfCheckoutPage.selectTermsAndConditionsChkBox();
		sfCheckoutPage.selectPoliciesAndProceduresChkBox();
		sfCheckoutPage.clickPlaceOrderButton();
		sfCheckoutPage.clickRodanAndFieldsLogo();
		s_assert.assertTrue(sfHomePage.hasPCEnrolledSuccessfully(), "PC has not been enrolled successfully");
		//Verify the sponser details while placing order.
		sfShopSkinCarePage = sfHomePage.clickAllProducts();
		sfShopSkinCarePage.addProductToCart(TestConstants.PRODUCT_NUMBER, TestConstants.ORDER_TYPE_ADHOC);;
		sfCartPage = sfShopSkinCarePage.checkoutTheCartFromPopUp();
		sfCheckoutPage = sfCartPage.clickCheckoutBtn();
		sponserNameAfterEnrollment = sfCheckoutPage.getSponsorNameFromAccountInfo();
		s_assert.assertTrue(sponserNameAfterEnrollment.toLowerCase().contains(secondSponser.toLowerCase()),"Second sponser name is not same as provided during enrollment");
		s_assert.assertAll();
	}

	/***
	 * qTest : TC-575 Choose a Consultant- Auto-Assign (Checkout) - Second time
	 * Description : This test validates the re-assigned sponsor to PC after enrollment
	 * 
	 *     
	 */
	@Test(enabled=false)
	public void testUpdateSponserAfterPCEnrollment_575(){
		timeStamp = CommonUtils.getCurrentTimeStamp();
		randomWords = CommonUtils.getRandomWord(5);		
		lastName = TestConstants.LAST_NAME+randomWords;
		email = firstName+timeStamp+TestConstants.EMAIL_SUFFIX;
		String sponserName = null;
		String secondSponser = null;
		String existingConsultant = TestConstants.SPONSOR;
		sfCartPage = new StoreFrontCartPage(driver);
		sfShopSkinCarePage = new StoreFrontShopSkinCarePage(driver);
		sfHomePage.clickLoginIcon();
		sfCheckoutPage=sfHomePage.clickSignUpNowLink();
		sfCheckoutPage.fillNewUserDetails(TestConstants.USER_TYPE_PC, firstName, lastName, email, password);
		sfCheckoutPage.clickCreateAccountButton();
		s_assert.assertTrue(sfCartPage.isPcOneTimeFeeMsgDisplayed(),"PC one time joining fee msg has not displayed");
		sfCartPage.clickAddMoreItemsBtn();
		sfShopSkinCarePage.addProductToCart(TestConstants.PRODUCT_NUMBER, TestConstants.ORDER_TYPE_ADHOC);;
		sfCartPage = sfShopSkinCarePage.checkoutTheCartFromPopUp();
		sfCartPage.clickCheckoutBtn();
		//Choose sponser and main account fields are autofilled.
		sfCheckoutPage.searchForConsultant(existingConsultant);
		s_assert.assertTrue(sfCheckoutPage.isSponsorResultDisplayed(),"No result found after searching the sponsor with name "+TestConstants.SPONSOR);
		sponserName = sfCheckoutPage.selectAndReturnFirstSponsorFromList();
		sfCheckoutPage.clickRemoveLink();
		sfCheckoutPage.searchForConsultant(existingConsultant);
		secondSponser = sfCheckoutPage.selectAndReturnSponsorFromList("2");
		sfCheckoutPage.clickSaveButton();
		//Fill shipping details.
		sfCheckoutPage.enterShippingDetails(firstName+" "+lastName, addressLine1, addressLine2, city, state, postalCode, phoneNumber);
		sfCheckoutPage.clickShippingDetailsNextbutton();
		sfCheckoutPage.enterUserBillingDetails(cardType, cardNumber, cardName, CVV);
		sfCheckoutPage.clickBillingDetailsNextbutton();
		sfCheckoutPage.selectTermsAndConditionsChkBox();
		sfCheckoutPage.selectPoliciesAndProceduresChkBox();
		sfCheckoutPage.clickPlaceOrderButton();
		sfCheckoutPage.clickRodanAndFieldsLogo();
		s_assert.assertTrue(sfHomePage.hasPCEnrolledSuccessfully(), "PC has not been enrolled successfully");
		//Try to update sponser at main account info.
		sfShopSkinCarePage = sfHomePage.clickAllProducts();
		sfShopSkinCarePage.addProductToCart(TestConstants.PRODUCT_NUMBER, TestConstants.ORDER_TYPE_ADHOC);;
		sfCartPage = sfShopSkinCarePage.checkoutTheCartFromPopUp();
		sfCheckoutPage = sfCartPage.clickCheckoutBtn();
		sfCheckoutPage.clickSaveButton();
		sfCheckoutPage.editMainAccountInfo();
		s_assert.assertTrue(sfCheckoutPage.isChangeSponserLinkDisplayed(),"Change sponser link is present on account info page.");
		s_assert.assertAll();
	}

}