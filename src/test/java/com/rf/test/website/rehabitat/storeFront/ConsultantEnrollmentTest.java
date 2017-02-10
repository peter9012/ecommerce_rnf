package com.rf.test.website.rehabitat.storeFront;

import org.testng.annotations.Test;

import com.rf.core.utils.CommonUtils;
import com.rf.core.website.constants.TestConstants;
import com.rf.test.website.rehabitat.storeFront.baseTest.StoreFrontWebsiteBaseTest;

public class ConsultantEnrollmentTest extends StoreFrontWebsiteBaseTest{
	private String firstName = null;
	private String lastName = null;
	private String email = null;
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

	public ConsultantEnrollmentTest() {
		firstName = TestConstants.FIRST_NAME;
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
	 * qTest : TC-223 Find a Consultant/Search-CID
	 * 
	 * Description : This test validates that while consultant enrollment,valid & active sponsor
	 * should be searched and valid but inactive sponsor should not be searched.
	 * 				
	 */ 
	@Test(enabled=false)  //TODO Inactive sponsor search is pending
	public void testFindAConsultantBySearchCID_223(){
		sfHomePage.clickEnrollNow();
		sfHomePage.searchSponsor(TestConstants.SPONSOR);
		s_assert.assertTrue(sfHomePage.isSponsorResultDisplayed(),"No result found after searching the sponsor with name "+TestConstants.SPONSOR);
		s_assert.assertAll();
	}

	/***
	 * qTest : TC-224 Find a consultant/search- Prefix
	 * 
	 * Description : This test validates that while consultant enrollment:-	 * 
	 * Complete Prefix with active PWS = Sponsor should be available
	 * Complete Prefix with Inactive PWS = Sponsor should NOT be available
	 * Partial Prefix with active PWS = Sponsor should be available
	 * Partial Prefix with Inactive PWS = Sponsor should NOT be available
	 */
	@Test(enabled=false) //TODO The entire test
	public void testFindAConsultantByPrefix_224(){
		sfHomePage.clickEnrollNow();
		sfHomePage.searchSponsor(TestConstants.SPONSOR);
		s_assert.assertAll();
	}

	/***
	 * qTest : TC-231 Consultant Enrollment- Checkout
	 * 
	 * Description : This test validates that complete checkout process for consultant
	 * enrollment
	 *     
	 */
	@Test(enabled=true)
	public void testConsultantEnrollment_231(){
		timeStamp = CommonUtils.getCurrentTimeStamp();
		randomWords = CommonUtils.getRandomWord(5);		
		lastName = TestConstants.LAST_NAME+randomWords;
		email = firstName+timeStamp+TestConstants.EMAIL_SUFFIX;
		String socialInsuranceNumber = String.valueOf(CommonUtils.getRandomNum(100000000, 999999999));
		sfHomePage.clickEnrollNow();
		sfHomePage.searchSponsor(TestConstants.SPONSOR);
		s_assert.assertTrue(sfHomePage.isSponsorResultDisplayed(),"No result found after searching the sponsor with name "+TestConstants.SPONSOR);
		sfHomePage.selectFirstSponsorFromList();
		sfHomePage.enterConsultantEnrollmentDetails(firstName, lastName, email, password, socialInsuranceNumber);
		sfHomePage.clickNextButton();
		s_assert.assertFalse(sfHomePage.isNextButtonEnabledBeforeSelectingKit(), "Next Button is NOT disabled before selecting kit");
		sfHomePage.chooseProductFromKitPage();
		//sfHomePage.selectSubscribeToPulseCheckBox();
		sfHomePage.clickNextButton();
		sfHomePage.clickSaveButton();
		sfHomePage.enterConsultantShippingDetails(firstName, lastName, addressLine1, addressLine2 ,city, state, postalCode, phoneNumber);
		sfHomePage.clickUseAsEnteredButtonOnPopUp();
		sfHomePage.clickShippingDetailsNextbutton();
		sfHomePage.enterUserBillingDetails(cardType, cardNumber, cardName, CVV);
		//sfHomePage.selectBillingAddressFromDD();
		//sfHomePage.checkUseMyDeliveryAddressChkBox();
		sfHomePage.clickBillingDetailsNextbutton();
		sfHomePage.selectPoliciesAndProceduresChkBox();
		sfHomePage.selectIAcknowledgeChkBox();
		sfHomePage.selectTermsAndConditionsChkBox();
		sfHomePage.selectConsentFormChkBox();
		sfHomePage.clickBecomeAConsultant();
		//s_assert.assertTrue(sfHomePage.isEnrollemntSuccessfulMsgDisplayed(), "Expected 'Your order number is' msg has NOT displayed"); 
		sfHomePage.clickRodanAndFieldsLogo();
		s_assert.assertTrue(sfHomePage.isWelcomeUserElementDisplayed(), "Welcome user locator has not displayed after consultant enrollment");
		s_assert.assertAll();
	}

	/***
	 * qTest : TC-261 Request a Sponsor during consultant enrollment
	 * 
	 * Description : This test validates the Request for a Sponsor functionality during
	 * consultant enrollment
	 * 				
	 */
	@Test(enabled=true)
	public void testRequestSponsorConsultantEnrollment_261(){
		timeStamp = CommonUtils.getCurrentTimeStamp();
		randomWords = CommonUtils.getRandomWord(5);		
		lastName = TestConstants.LAST_NAME+randomWords;
		email = firstName+timeStamp+TestConstants.EMAIL_SUFFIX;
		String fieldName;
		sfHomePage.clickEnrollNow();
		sfHomePage.clickIDontHaveSponsorCheckBox();
		sfHomePage.clickSubmitBtnOnRequiredConsultantSponsorPopUp();
		fieldName = "firstName";
		s_assert.assertTrue(sfHomePage.isEmptyFieldValidationForSponsorOnPopupDisplayed(fieldName),"empty field validation is not displayed for "+fieldName);
		fieldName = "lastName";
		s_assert.assertTrue(sfHomePage.isEmptyFieldValidationForSponsorOnPopupDisplayed(fieldName),"empty field validation is not displayed for "+fieldName);
		fieldName = "email";
		s_assert.assertTrue(sfHomePage.isEmptyFieldValidationForSponsorOnPopupDisplayed(fieldName),"empty field validation is not displayed for "+fieldName);
		fieldName = "zipcode";
		s_assert.assertTrue(sfHomePage.isEmptyFieldValidationForSponsorOnPopupDisplayed(fieldName),"empty field validation is not displayed for "+fieldName);
		sfHomePage.enterDetailsInRequiredConsultantSponsorPopUp("", "", "", TestConstants.SPONSOR_ZIP_CODE_US);
		s_assert.assertTrue(sfHomePage.isSubmitBtnOnSponsorPopUpDisabled(),"Submit button is not disabled");
		sfHomePage.enterDetailsInRequiredConsultantSponsorPopUp(TestConstants.SPONSOR_FIRST_NAME, TestConstants.SPONSOR_LAST_NAME, TestConstants.SPONSOR_EMAIL, TestConstants.SPONSOR_INVALID_ZIP_CODE_US);
		fieldName = "zipcode";
		s_assert.assertTrue(sfHomePage.isInvalidFieldValidationForSponsorOnPopupDisplayed(fieldName),"invalid field validation is not displayed for "+fieldName);
		sfHomePage.enterDetailsInRequiredConsultantSponsorPopUp(TestConstants.SPONSOR_FIRST_NAME, TestConstants.SPONSOR_LAST_NAME, TestConstants.SPONSOR_INVALID_EMAIL, TestConstants.SPONSOR_ZIP_CODE_US);
		fieldName = "email";
		s_assert.assertTrue(sfHomePage.isInvalidFieldValidationForSponsorOnPopupDisplayed(fieldName),"invalid field validation is not displayed for "+fieldName);
		sfHomePage.enterDetailsInRequiredConsultantSponsorPopUp(TestConstants.SPONSOR_FIRST_NAME, TestConstants.SPONSOR_LAST_NAME, TestConstants.SPONSOR_EMAIL, TestConstants.SPONSOR_ZIP_CODE_US);
		sfHomePage.clickSubmitBtnOnRequiredConsultantSponsorPopUp();
		s_assert.assertTrue(sfHomePage.isThanksMessageAfterSponsorRequestPresent(),"Thank msg not displayed after sponsor request submit");
		sfHomePage.clickBackToHomePageBtn();
		s_assert.assertTrue(sfHomePage.isHomePageBannerDisplayed(), "User is not on Home Page as home page banner is not displayed");
		s_assert.assertAll();
	}

	/***
	 * qTest : TC-275 User finds a consultant
	 * 
	 * Description : This test validates the sponsor search from 'Find A Consultant' link and Connect To Consultant 
	 * and also validates the pagination in the sponsor search results.
	 * 				
	 */
	@Test(enabled=true)//TODO No COnnect To COnsultant on home page
	public void testSponsorSearchFromFindAConsultantAndPagination_275(){
		sfHomePage.clickFindAConsultantLinkOnHomePage().searchSponsor(TestConstants.SPONSOR_3_CHARS);
		s_assert.assertTrue(sfHomePage.isSponsorResultDisplayed(),"No result found after searching the sponsor with name "+TestConstants.SPONSOR_3_CHARS);
		s_assert.assertTrue(sfHomePage.isTheUserOnNavigationPage("1"),"user is not on navigation page 1");
		sfHomePage.navigateToPaginationInSponsorSearchResult("2");
		s_assert.assertTrue(sfHomePage.isTheUserOnNavigationPage("2"),"user is not on navigation page 2");
		//		sfHomePage.clickRodanAndFieldsLogo();
		//		sfHomePage.selectFirstSponsorFromList();
		//		s_assert.assertTrue(sfHomePage.isSponsorResultDisplayed(),"No result found after searching the sponsor with name after navigating from Connect with Consultant button "+TestConstants.SPONSOR_3_CHARS);
		s_assert.assertAll();
	}

	/***
	 * qTest : TC-273 User enter a name sponsor that doesn't exist in the RF
	 * 
	 * Description : This test validates that while consultant enrollment,if a sponsor is searched that
	 * doesn't exist in R+F system then the 'No Search Results' message should be displayed
	 * 				
	 */
	@Test(enabled=true)
	public void testSearchNonExistingSponsor_273(){
		sfHomePage.clickEnrollNow();
		sfHomePage.searchSponsor(TestConstants.NON_EXISTING_SPONSOR);
		s_assert.assertTrue(sfHomePage.isNoResultMessagePresent(),"'No result found' msg is not present for searching non-existing sponsor");
		s_assert.assertAll();
	}

	/***
	 * qTest : TC-290 User selects Personal Business Porfolio
	 * 
	 * Description : This test validates that consultant enrollment by selecting
	 * Personal Business Potfolio
	 * 				
	 */
	@Test(enabled=true)
	public void testConsultantEnrollmentPersonalBusinessPortfolio_290(){
		timeStamp = CommonUtils.getCurrentTimeStamp();
		randomWords = CommonUtils.getRandomWord(5);		
		lastName = TestConstants.LAST_NAME+randomWords;
		email = firstName+timeStamp+TestConstants.EMAIL_SUFFIX;
		String socialInsuranceNumber = String.valueOf(CommonUtils.getRandomNum(100000000, 999999999));
		String kitName = "BUSINESS PORTFOLIO";
		sfHomePage.clickEnrollNow();
		sfHomePage.searchSponsor(TestConstants.SPONSOR);
		s_assert.assertTrue(sfHomePage.isSponsorResultDisplayed(),"No result found after searching the sponsor with name "+TestConstants.SPONSOR);
		sfHomePage.selectFirstSponsorFromList();
		sfHomePage.enterConsultantEnrollmentDetails(firstName, lastName, email, password, socialInsuranceNumber);
		sfHomePage.clickNextButton();
		sfHomePage.chooseProductFromKitPage(kitName);
		sfHomePage.clickNextButton();
		sfHomePage.clickSaveButton();
		sfHomePage.enterConsultantShippingDetails(firstName, lastName, addressLine1, addressLine2 ,city, state, postalCode, phoneNumber);
		sfHomePage.clickUseAsEnteredButtonOnPopUp();
		sfHomePage.clickShippingDetailsNextbutton();
		sfHomePage.enterUserBillingDetails(cardType, cardNumber, cardName, CVV);
		//sfHomePage.selectBillingAddressFromDD();
		//sfHomePage.checkUseMyDeliveryAddressChkBox();
		sfHomePage.clickBillingDetailsNextbutton();
		sfHomePage.selectPoliciesAndProceduresChkBox();
		sfHomePage.selectIAcknowledgeChkBox();
		sfHomePage.selectTermsAndConditionsChkBox();
		sfHomePage.selectConsentFormChkBox();
		sfHomePage.clickBecomeAConsultant();
		s_assert.assertTrue(sfHomePage.isEnrollemntSuccessfulMsgDisplayed(), "Expected 'Your order number is' msg has NOT displayed"); 
		sfHomePage.clickRodanAndFieldsLogo();
		s_assert.assertTrue(sfHomePage.isWelcomeUserElementDisplayed(), "Welcome user locator has not displayed after consultant enrollment");
		s_assert.assertAll();
	}

	/***
	 * qTest : TC-291 User selects View/hid details CTA in the enrollment kit selected
	 * 
	 * Description : This test verifies the functionality of
	 * View Details link of Kits during consultant enrollment
	 * 				
	 */
	@Test(enabled=true)
	public void testViewHideCTADetailsInEnrollmentKit_291(){
		timeStamp = CommonUtils.getCurrentTimeStamp();
		randomWords = CommonUtils.getRandomWord(5);		
		lastName = TestConstants.LAST_NAME+randomWords;
		email = firstName+timeStamp+TestConstants.EMAIL_SUFFIX;
		int totalKits = 0;
		int randomKit = 0;
		String timeStamp = CommonUtils.getCurrentTimeStamp();
		String socialInsuranceNumber = String.valueOf(CommonUtils.getRandomNum(100000000, 999999999));
		sfHomePage.clickEnrollNow();
		sfHomePage.searchSponsor(TestConstants.SPONSOR);
		s_assert.assertTrue(sfHomePage.isSponsorResultDisplayed(),"No result found after searching the sponsor with name "+TestConstants.SPONSOR);
		sfHomePage.selectFirstSponsorFromList();
		sfHomePage.enterConsultantEnrollmentDetails(firstName, lastName, email, password, socialInsuranceNumber);
		sfHomePage.clickNextButton();
		totalKits = sfHomePage.getTotalKitsDisplayedDuringConsultantEnrollment();
		randomKit = CommonUtils.getRandomNum(1, totalKits);
		sfHomePage.clickAnyViewDetailsLink(String.valueOf(randomKit));
		s_assert.assertTrue(sfHomePage.isKitDetailsDisplayed(String.valueOf(randomKit)), "click View details link has NOT expanded the kit");
		sfHomePage.closeTheExpandedKitDetails(String.valueOf(randomKit));
		s_assert.assertFalse(sfHomePage.isKitDetailsDisplayed(String.valueOf(randomKit)), "close expanded button has NOT closed the kit details");
		s_assert.assertAll();
	}

	/***
	 * qTest : TC-485 North Dakota Error-Valid ND billing address
	 * 
	 * Description : TThis test validates consultant enrollment with 'North Dakota'
	 * State and valid billing address selected.
	 *     
	 */
	@Test(enabled=true)
	public void testNortDakotaConsultantEnrollmentValidBillingAddress_485(){
		timeStamp = CommonUtils.getCurrentTimeStamp();
		randomWords = CommonUtils.getRandomWord(5);		
		lastName = TestConstants.LAST_NAME+randomWords;
		email = firstName+timeStamp+TestConstants.EMAIL_SUFFIX;
		String socialInsuranceNumber = String.valueOf(CommonUtils.getRandomNum(100000000, 999999999));
		String firstName = TestConstants.FIRST_NAME;
		String lastName = TestConstants.LAST_NAME;
		String emailID = TestConstants.FIRST_NAME+timeStamp+TestConstants.EMAIL_SUFFIX;
		String addressLine1 = TestConstants.ADDRESS_LINE_1_US;
		String addressLine2 = TestConstants.ADDRESS_LINE_2_US;
		String city = TestConstants.CITY_US;
		String state = TestConstants.STATE_US;
		String postalCode = TestConstants.POSTAL_CODE_US;
		String phoneNumber = TestConstants.PHONE_NUMBER;
		String cardType = TestConstants.CARD_TYPE;
		String cardNumber = TestConstants.CARD_NUMBER;
		String cardName = TestConstants.CARD_NAME;
		String CVV = TestConstants.CVV;
		String firstNameBA = TestConstants.FIRST_NAME_BILLING_ADDRESS;
		String lastNameBA = TestConstants.LAST_NAME_BILLING_ADDRESS;
		String addressLine1BA = TestConstants.ADDRESS_LINE_1_ND_US;
		String cityBA = TestConstants.CITY_ND_US;
		String stateBA = TestConstants.STATE_ND_US;
		String postalCodeBA = TestConstants.POSTAL_CODE_ND_US;
		String billingDetailsOnOrderReview = null;
		sfHomePage.clickEnrollNow();
		sfHomePage.searchSponsor(TestConstants.SPONSOR);
		s_assert.assertTrue(sfHomePage.isSponsorResultDisplayed(),"No result found after searching the sponsor with name "+TestConstants.SPONSOR);
		sfHomePage.selectFirstSponsorFromList();
		sfHomePage.enterConsultantEnrollmentDetails(firstName, lastName, emailID, password, socialInsuranceNumber);
		sfHomePage.clickNextButton();
		sfHomePage.selectNorthDakotaCheckBoxOnKitPage();
		sfHomePage.clickNextButton();
		sfHomePage.clickSaveButton();
		sfHomePage.enterConsultantShippingDetails(firstName, lastName, addressLine1, addressLine2 ,city, state, postalCode, phoneNumber);
		sfHomePage.clickUseAsEnteredButtonOnPopUp();
		sfHomePage.clickShippingDetailsNextbutton();
		sfHomePage.enterUserBillingDetails(cardType, cardNumber, cardName, CVV);
		sfHomePage.checkUseMyDeliveryAddressChkBox();
		sfHomePage.enterConsultantAddressDetails(firstNameBA, lastNameBA, addressLine1BA, cityBA, stateBA, postalCodeBA, phoneNumber);
		sfHomePage.clickBillingDetailsNextbutton();
		sfHomePage.clickUseAsEnteredButtonOnPopUp();
		billingDetailsOnOrderReview = sfHomePage.getBillingProfileDetails();
		s_assert.assertTrue(billingDetailsOnOrderReview.trim().contains("ND"), "ND billing profile is not saved successfully.");
		sfHomePage.selectPoliciesAndProceduresChkBox();
		sfHomePage.selectIAcknowledgeChkBox();
		sfHomePage.selectTermsAndConditionsChkBox();
		sfHomePage.selectConsentFormChkBox();
		sfHomePage.clickBecomeAConsultant();
		s_assert.assertTrue(sfHomePage.isEnrollemntSuccessfulMsgDisplayed(), "Expected 'Your order number is' msg has NOT displayed"); 
		sfHomePage.clickRodanAndFieldsLogo();
		s_assert.assertTrue(sfHomePage.isWelcomeUserElementDisplayed(), "Welcome user locator has not displayed after consultant enrollment");
		s_assert.assertAll();
	}

	/***
	 * qTest : TC-393 Links to Policies and Procedures, Terms and Conditions on enrollment pages
	 * 
	 * Description : //TODO
	 * 				
	 */
	@Test(enabled=true)
	public void testLinksToPoliciesAndProcedures_393(){
		timeStamp = CommonUtils.getCurrentTimeStamp();
		randomWords = CommonUtils.getRandomWord(5);		
		lastName = TestConstants.LAST_NAME+randomWords;
		email = firstName+timeStamp+TestConstants.EMAIL_SUFFIX;
		String socialInsuranceNumber = String.valueOf(CommonUtils.getRandomNum(100000000, 999999999));
		String policiesAndProceduresPdfUrl = "Policies_Procedures_USA.pdf";
		String pulseProTCUrl = "Pulse_Terms_and_Conditions_CANADA.pdf";
		String crpTCPdfUrl = "CRP_Terms_and_Conditions_CANADA.pdf";
		sfHomePage.clickEnrollNow();

		sfHomePage.clickPoliciesAndProceduresLink();
		String parentWindowID = CommonUtils.getCurrentWindowHandle();
		sfHomePage.switchToChildWindow(parentWindowID);
		s_assert.assertTrue(sfHomePage.getCurrentURL().contains(policiesAndProceduresPdfUrl), "Policies And Procedures PDF has not opened at search sponsor page");
		sfHomePage.switchToParentWindow(parentWindowID);

		sfHomePage.clickPulseProTermsAndConditionsLink();
		parentWindowID = CommonUtils.getCurrentWindowHandle();
		sfHomePage.switchToChildWindow(parentWindowID);
		s_assert.assertTrue(sfHomePage.getCurrentURL().contains(pulseProTCUrl), "Pulse Pro Terms And Conditions PDF has not opened at search sponsor page");
		sfHomePage.switchToParentWindow(parentWindowID);

		sfHomePage.clickCRPTermsAndConditionsLink();
		parentWindowID = CommonUtils.getCurrentWindowHandle();
		sfHomePage.switchToChildWindow(parentWindowID);
		s_assert.assertTrue(sfHomePage.getCurrentURL().contains(crpTCPdfUrl), "CRP Terms And Conditions PDF has not opened at search sponsor page");
		sfHomePage.switchToParentWindow(parentWindowID);

		sfHomePage.searchSponsor(TestConstants.SPONSOR);
		sfHomePage.selectFirstSponsorFromList();
		sfHomePage.enterConsultantEnrollmentDetails(firstName, lastName, email, password, socialInsuranceNumber);
		sfHomePage.clickNextButton();

		sfHomePage.clickPoliciesAndProceduresLink();
		parentWindowID = CommonUtils.getCurrentWindowHandle();
		sfHomePage.switchToChildWindow(parentWindowID);
		s_assert.assertTrue(sfHomePage.getCurrentURL().contains(policiesAndProceduresPdfUrl), "Policies And Procedures PDF has not opened at consultant details page");
		sfHomePage.switchToParentWindow(parentWindowID);

		sfHomePage.clickPulseProTermsAndConditionsLink();
		parentWindowID = CommonUtils.getCurrentWindowHandle();
		sfHomePage.switchToChildWindow(parentWindowID);
		s_assert.assertTrue(sfHomePage.getCurrentURL().contains(pulseProTCUrl), "Pulse Pro Terms And Conditions PDF has not opened at consultant details page");
		sfHomePage.switchToParentWindow(parentWindowID);

		sfHomePage.clickCRPTermsAndConditionsLink();
		parentWindowID = CommonUtils.getCurrentWindowHandle();
		sfHomePage.switchToChildWindow(parentWindowID);
		s_assert.assertTrue(sfHomePage.getCurrentURL().contains(crpTCPdfUrl), "CRP Terms And Conditions PDF has not opened at consultant details page ");
		sfHomePage.switchToParentWindow(parentWindowID);
		s_assert.assertAll();
	}

	/***
	 * qTest : TC-492 Consultant Enrollment- Page 2- North Dakota Checkbox-Checked
	 * 
	 * Description : This test validates that complete checkout process for consultant
	 * enrollment on selecting North Dakota Checkbox
	 *     
	 */
	@Test(enabled=true)
	public void testConsultantEnrollmentNorthDakotaChecked_492(){
		timeStamp = CommonUtils.getCurrentTimeStamp();
		randomWords = CommonUtils.getRandomWord(5);		
		lastName = TestConstants.LAST_NAME+randomWords;
		email = firstName+timeStamp+TestConstants.EMAIL_SUFFIX;
		String socialInsuranceNumber = String.valueOf(CommonUtils.getRandomNum(100000000, 999999999));
		String addressLine1 = TestConstants.ADDRESS_LINE_1_ND_US;
		String city = TestConstants.CITY_ND_US;
		String state = TestConstants.STATE_ND_US;
		String postalCode = TestConstants.POSTAL_CODE_ND_US;
		sfHomePage.clickEnrollNow();
		sfHomePage.searchSponsor(TestConstants.SPONSOR);
		s_assert.assertTrue(sfHomePage.isSponsorResultDisplayed(),"No result found after searching the sponsor with name "+TestConstants.SPONSOR);
		sfHomePage.selectFirstSponsorFromList();
		sfHomePage.enterConsultantEnrollmentDetails(firstName, lastName, email, password, socialInsuranceNumber);
		sfHomePage.clickNextButton();
		sfHomePage.selectNorthDakotaCheckBoxOnKitPage();
		s_assert.assertTrue(sfHomePage.areAllKitsDisabled(), "Kit section is not disbaled ab=fter selecting North Dakota Checkbox");
		sfHomePage.clickNextButton();
		sfHomePage.clickSaveButton();
		sfHomePage.enterConsultantShippingDetails(firstName, lastName, addressLine1, "" ,city, state, postalCode, phoneNumber);
		sfHomePage.clickUseAsEnteredButtonOnPopUp();
		sfHomePage.clickShippingDetailsNextbutton();
		sfHomePage.enterUserBillingDetails(cardType, cardNumber, cardName, CVV);
		sfHomePage.clickBillingDetailsNextbutton();
		sfHomePage.selectPoliciesAndProceduresChkBox();
		sfHomePage.selectIAcknowledgeChkBox();
		sfHomePage.selectTermsAndConditionsChkBox();
		sfHomePage.selectConsentFormChkBox();
		sfHomePage.clickBecomeAConsultant();
		s_assert.assertTrue(sfHomePage.isEnrollemntSuccessfulMsgDisplayed(), "Expected 'Your order number is' msg has NOT displayed"); 
		sfHomePage.clickRodanAndFieldsLogo();
		s_assert.assertTrue(sfHomePage.isWelcomeUserElementDisplayed(), "Welcome user locator has not displayed after consultant enrollment");
		s_assert.assertAll();
	}

	/***
	 * qTest : TC-493 Consultant Enrollment- Page 2- North Dakota Checkbox-Unchecked
	 * 
	 * Description : This test validates that complete checkout process for consultant
	 * enrollment without selecting North Dakota checkbox
	 *     
	 */
	@Test(enabled=true)
	public void testConsultantEnrollmentNorthDakotaUnchecked_493(){
		//Duplicate test, same as testConsultantEnrollment_231
	}

	/***
	 * qTest : TC-373 Consultant Enrollment- Page 1- Apply as a Business Entity Link
	 * 
	 * Description : This test validates that Apply As a Business Entity Link functionality
	 * on sponsor page during consultant enrollment
	 *     
	 */
	@Test(enabled=true)
	public void testConsultantEnrollmentApplyAsBusinessEntityLink_373(){
		sfHomePage.clickEnrollNow();
		sfHomePage.clickApplyingAsBusinessEntityLink();
		s_assert.assertTrue(sfHomePage.isApplyingAsBusinessEntityPopupDisplayed(), "'Applying as Business Entity' popup has NOT displayed");
		sfHomePage.closeApplyingAsBusinessEntityPopUp();
		s_assert.assertFalse(sfHomePage.isApplyingAsBusinessEntityPopupDisplayed(), "'Applying as Business Entity' popup has NOT closed");
		s_assert.assertAll();
	}

	/***
	 * qTest : TC-467 Consultant Enrollment-Page 2- Pulse Pro Subscription - Select and continue
	 * 
	 * Description : This method completes the consultant enrollment by selecting pulse
	 *     
	 */
	@Test
	public void testConsultantEnrollmentWithPulseSubscription_467(){
		timeStamp = CommonUtils.getCurrentTimeStamp();
		randomWords = CommonUtils.getRandomWord(5);		
		lastName = TestConstants.LAST_NAME+randomWords;
		email = firstName+timeStamp+TestConstants.EMAIL_SUFFIX;
		String timeStamp = CommonUtils.getCurrentTimeStamp();
		String socialInsuranceNumber = String.valueOf(CommonUtils.getRandomNum(100000000, 999999999));
		String prefix = firstName+timeStamp;
		sfHomePage.clickEnrollNow();
		sfHomePage.searchSponsor(TestConstants.SPONSOR);
		s_assert.assertTrue(sfHomePage.isSponsorResultDisplayed(),"No result found after searching the sponsor with name "+TestConstants.SPONSOR);
		sfHomePage.selectFirstSponsorFromList();
		sfHomePage.enterConsultantEnrollmentDetails(firstName, lastName, email, password, socialInsuranceNumber);
		sfHomePage.clickNextButton();
		s_assert.assertFalse(sfHomePage.isNextButtonEnabledBeforeSelectingKit(), "Next Button is NOT disabled before selecting kit");
		sfHomePage.chooseProductFromKitPage();
//		sfHomePage.selectSubscribeToPulseCheckBox();
		sfHomePage.enterPrefix(prefix);
		s_assert.assertTrue(sfHomePage.isPrefixAvailable(), "entered prefix "+prefix+" is NOT available");
		sfHomePage.clickNextButton();
		sfHomePage.clickSaveButton();
		sfHomePage.enterConsultantShippingDetails(firstName, lastName, addressLine1, addressLine2 ,city, state, postalCode, phoneNumber);
		sfHomePage.clickShippingDetailsNextbutton();
		sfHomePage.enterUserBillingDetails(cardType, cardNumber, cardName, CVV);
		sfHomePage.clickBillingDetailsNextbutton();
		sfHomePage.selectPoliciesAndProceduresChkBox();
		sfHomePage.selectIAcknowledgeChkBox();
		sfHomePage.selectTermsAndConditionsChkBox();
		sfHomePage.selectConsentFormChkBox();
		sfHomePage.clickBecomeAConsultant();
		s_assert.assertTrue(sfHomePage.isEnrollemntSuccessfulMsgDisplayed(), "Expected 'Your order number is' msg has NOT displayed"); 
		sfHomePage.clickRodanAndFieldsLogo();
		s_assert.assertTrue(sfHomePage.isWelcomeUserElementDisplayed(), "Welcome user locator has not displayed after consultant enrollment");
		sfHomePage.clickWelcomeDropdown();
		sfOrdersPage = sfHomePage.navigateToOrdersPage();
		//Verify autoship order details.
		s_assert.assertTrue(sfOrdersPage.isAutoshipOrderHistoryTableAppeared(),"Autoship Order history Table is not present on orders page");
		sfOrdersPage.clickAndGetFirstOrderNumberFromOrderHistory();
		s_assert.assertTrue(sfOrdersPage.isPulseProOrderPresentOnOrderDetailPage(),"Pulse pro order not present on pulse order detail page.");
		s_assert.assertAll();

	}
	/***
	 * qTest : TC-468 Consultant Enrollment-Page 2- Pulse Pro Subscription - Unselect and continue
	 * 
	 * Description : This method completes the consultant enrollment without selecting pulse
	 *     
	 */
	@Test
	public void testConsultantEnrollmentWithoutPulseSubscription_468(){
		timeStamp = CommonUtils.getCurrentTimeStamp();
		randomWords = CommonUtils.getRandomWord(5);		
		lastName = TestConstants.LAST_NAME+randomWords;
		email = firstName+timeStamp+TestConstants.EMAIL_SUFFIX;
		String socialInsuranceNumber = String.valueOf(CommonUtils.getRandomNum(100000000, 999999999));
		sfHomePage.clickEnrollNow();
		sfHomePage.searchSponsor(TestConstants.SPONSOR);
		s_assert.assertTrue(sfHomePage.isSponsorResultDisplayed(),"No result found after searching the sponsor with name "+TestConstants.SPONSOR);
		sfHomePage.selectFirstSponsorFromList();
		sfHomePage.enterConsultantEnrollmentDetails(firstName, lastName, email, password, socialInsuranceNumber);
		sfHomePage.clickNextButton();
		s_assert.assertFalse(sfHomePage.isNextButtonEnabledBeforeSelectingKit(), "Next Button is NOT disabled before selecting kit");
		sfHomePage.chooseProductFromKitPage();
		sfHomePage.selectSubscribeToPulseCheckBox();
		sfHomePage.clickNextButton();
		sfHomePage.clickSaveButton();
		sfHomePage.enterConsultantShippingDetails(firstName, lastName, addressLine1, addressLine2 ,city, state, postalCode, phoneNumber);
		sfHomePage.clickUseAsEnteredButtonOnPopUp();
		sfHomePage.clickShippingDetailsNextbutton();
		sfHomePage.enterUserBillingDetails(cardType, cardNumber, cardName, CVV);
		sfHomePage.clickBillingDetailsNextbutton();
		sfHomePage.selectPoliciesAndProceduresChkBox();
		sfHomePage.selectIAcknowledgeChkBox();
		sfHomePage.selectTermsAndConditionsChkBox();
		sfHomePage.selectConsentFormChkBox();
		sfHomePage.clickBecomeAConsultant();
		s_assert.assertTrue(sfHomePage.isEnrollemntSuccessfulMsgDisplayed(), "Expected 'Your order number is' msg has NOT displayed"); 
		sfHomePage.clickRodanAndFieldsLogo();
		s_assert.assertTrue(sfHomePage.isWelcomeUserElementDisplayed(), "Welcome user locator has not displayed after consultant enrollment");
		sfHomePage.clickWelcomeDropdown();
		sfOrdersPage = sfHomePage.navigateToOrdersPage();
		//Verify autoship order details.
		s_assert.assertFalse(sfOrdersPage.isAutoshipOrderHistoryTableAppeared(),"Autoship Order history Table is present on orders page");
		sfOrdersPage.clickAndGetFirstOrderNumberFromOrderHistory();
		s_assert.assertFalse(sfOrdersPage.isPulseProOrderPresentOnOrderDetailPage(),"Pulse pro order present on order detail page.");
		s_assert.assertAll();
	}

	/***
	 * qTest : TC-469 Consultant Enrollment-Page 2- Pulse Pro Subscription - Select and continue with existing prefix
	 * 
	 * Description : This method Validates consultant enrollment with existing prefix.
	 *     
	 */
	@Test(enabled=false)
	public void testConsultantEnrollmentWithExistingPrefix_469(){
		timeStamp = CommonUtils.getCurrentTimeStamp();
		randomWords = CommonUtils.getRandomWord(5);		
		lastName = TestConstants.LAST_NAME+randomWords;
		email = firstName+timeStamp+TestConstants.EMAIL_SUFFIX;
		String socialInsuranceNumber = String.valueOf(CommonUtils.getRandomNum(100000000, 999999999));
		String existingPrefix = TestConstants.CONSULTANT_PWS_PREFIX;
		sfHomePage.clickEnrollNow();
		sfHomePage.searchSponsor(TestConstants.SPONSOR);
		s_assert.assertTrue(sfHomePage.isSponsorResultDisplayed(),"No result found after searching the sponsor with name "+TestConstants.SPONSOR);
		sfHomePage.selectFirstSponsorFromList();
		sfHomePage.enterConsultantEnrollmentDetails(firstName, lastName, email, password, socialInsuranceNumber);
		sfHomePage.clickNextButton();
		s_assert.assertFalse(sfHomePage.isNextButtonEnabledBeforeSelectingKit(), "Next Button is NOT disabled before selecting kit");
		sfHomePage.chooseProductFromKitPage();
//		sfHomePage.selectSubscribeToPulseCheckBox();
		sfHomePage.enterPrefix(existingPrefix);
		s_assert.assertFalse(sfHomePage.isPrefixAvailable(), "entered prefix "+existingPrefix+" is available");
		s_assert.assertFalse(sfHomePage.isNextButtonEnabledBeforeSelectingKit(), "Next Button is NOT disabled before selecting valid prefix.");
		s_assert.assertAll();

	}

	/***
	 * qTest : TC-486 North Dakota Error-Empty ND billing address
	 * 
	 * Description : This test validates consultant enrollment with 'North Dakota'
	 * State and empty billing address selected.
	 *     
	 */
	@Test(enabled=true)
	public void testNortDakotaConsultantEnrollmentEmptyBillingAddress_486(){
		timeStamp = CommonUtils.getCurrentTimeStamp();
		randomWords = CommonUtils.getRandomWord(5);		
		lastName = TestConstants.LAST_NAME+randomWords;
		email = firstName+timeStamp+TestConstants.EMAIL_SUFFIX;
		String socialInsuranceNumber = String.valueOf(CommonUtils.getRandomNum(100000000, 999999999));
		String firstName = TestConstants.FIRST_NAME;
		String lastName = TestConstants.LAST_NAME;
		String emailID = TestConstants.FIRST_NAME+timeStamp+TestConstants.EMAIL_SUFFIX;
		String addressLine1 = TestConstants.ADDRESS_LINE_1_US;
		String addressLine2 = TestConstants.ADDRESS_LINE_2_US;
		String city = TestConstants.CITY_US;
		String state = TestConstants.STATE_US;
		String postalCode = TestConstants.POSTAL_CODE_US;
		String phoneNumber = TestConstants.PHONE_NUMBER;
		String cardType = TestConstants.CARD_TYPE;
		String cardNumber = TestConstants.CARD_NUMBER;
		String cardName = TestConstants.CARD_NAME;
		String CVV = TestConstants.CVV;
		sfHomePage.clickEnrollNow();
		sfHomePage.searchSponsor(TestConstants.SPONSOR);
		s_assert.assertTrue(sfHomePage.isSponsorResultDisplayed(),"No result found after searching the sponsor with name "+TestConstants.SPONSOR);
		sfHomePage.selectFirstSponsorFromList();
		sfHomePage.enterConsultantEnrollmentDetails(firstName, lastName, emailID, password, socialInsuranceNumber);
		sfHomePage.clickNextButton();
		sfHomePage.selectNorthDakotaCheckBoxOnKitPage();
		sfHomePage.clickNextButton();
		sfHomePage.clickSaveButton();
		sfHomePage.enterConsultantShippingDetails(firstName, lastName, addressLine1, addressLine2 ,city, state, postalCode, phoneNumber);
		sfHomePage.clickUseAsEnteredButtonOnPopUp();
		sfHomePage.clickShippingDetailsNextbutton();
		sfHomePage.enterUserBillingDetails(cardType, cardNumber, cardName, CVV);
		sfHomePage.checkUseMyDeliveryAddressChkBox();
		sfHomePage.enterConsultantAddressDetails(firstName, lastName, addressLine1, city, state);
		s_assert.assertTrue(sfHomePage.isPopUpForBillingAddressOtherThanNorthDakotaPresent(),"Error Popup for ND billing address is not present");
		sfHomePage.selectChooseAKitOptionOnPopup();
		//Verify user is redirected to kit selection page.
		s_assert.assertTrue(sfHomePage.areAllKitsDisabled(), "Kit section is not disbaled ab=fter selecting North Dakota Checkbox");
		s_assert.assertAll();
	}
	//------------------------TEST USERS CREATION SCRIPTS--------------------------------------------------------------------------------------------------------------------------------

	@Test(enabled=true, groups={"users"})
	public void testConsultantEnrollmentWithPulseAndCRP(){
		navigateToStoreFrontBaseURL();
		timeStamp = CommonUtils.getCurrentTimeStamp();
		randomWords = CommonUtils.getRandomWord(5);		
		lastName = TestConstants.LAST_NAME+randomWords;
		email = firstName+"WPWCRP"+timeStamp+TestConstants.EMAIL_SUFFIX;
		String socialInsuranceNumber = String.valueOf(CommonUtils.getRandomNum(100000000, 999999999));
		String prefix = firstName+timeStamp;
		sfHomePage.clickEnrollNow();
		sfHomePage.searchSponsor(TestConstants.SPONSOR);
		sfHomePage.selectFirstSponsorFromList();
		sfHomePage.enterConsultantEnrollmentDetails(firstName, lastName, email, password, socialInsuranceNumber);
		sfHomePage.clickNextButton();
		sfHomePage.chooseProductFromKitPage();
//		sfHomePage.selectSubscribeToPulseCheckBox();
		sfHomePage.enterPrefix(prefix);
		sfHomePage.clickNextButton();
		sfHomePage.clickSaveButton();
		sfHomePage.enterConsultantShippingDetails(firstName, lastName, addressLine1, addressLine2 ,city, state, postalCode, phoneNumber);
		sfHomePage.clickUseAsEnteredButtonOnPopUp();
		sfHomePage.clickShippingDetailsNextbutton();
		sfHomePage.enterUserBillingDetails(cardType, cardNumber, cardName, CVV);
		sfHomePage.clickBillingDetailsNextbutton();
		sfHomePage.selectPoliciesAndProceduresChkBox();
		sfHomePage.selectIAcknowledgeChkBox();
		sfHomePage.selectTermsAndConditionsChkBox();
		sfHomePage.selectConsentFormChkBox();
		sfHomePage.clickBecomeAConsultant();
		s_assert.assertTrue(sfHomePage.isEnrollemntSuccessfulMsgDisplayed(), "Expected 'Your order number is' msg has NOT displayed"); 
		sfHomePage.addFirstProductForCRPCheckout();
		sfCheckoutPage = sfHomePage.checkoutCRPBag();
		sfCheckoutPage.clickSaveButton();
		sfCheckoutPage.clickShippingDetailsNextbutton();
		sfCheckoutPage.clickBillingDetailsNextbutton();
		sfCheckoutPage.selectTermsAndConditionsCheckBoxForConsulatntCRP();
		//		sfCheckoutPage.selectCheckboxForPoliciesAndProcedures();
		sfCheckoutPage.clickConfirmAutoshipOrderButton();
		s_assert.assertTrue(sfCheckoutPage.isCRPOrderConfirmedSuccessMsgAppeared(),"CRP Order confirmed success messge is not appeared");
		sfCheckoutPage.clickRodanAndFieldsLogo();
		s_assert.assertAll();	
		conultantWithPulseAndWithCRP=email;
		pwsPrefix=prefix;	
	}

	@Test(enabled=true, groups={"users"})
	public void testConsultantEnrollmentWithoutCRPAndWithoutPulse(){
		navigateToStoreFrontBaseURL();
		timeStamp = CommonUtils.getCurrentTimeStamp();
		randomWords = CommonUtils.getRandomWord(5);		
		lastName = TestConstants.LAST_NAME+randomWords;
		email = firstName+"WOCRPWOP"+timeStamp+TestConstants.EMAIL_SUFFIX;
		String socialInsuranceNumber = String.valueOf(CommonUtils.getRandomNum(100000000, 999999999));
		sfHomePage.clickEnrollNow();
		sfHomePage.searchSponsor(TestConstants.SPONSOR);
		s_assert.assertTrue(sfHomePage.isSponsorResultDisplayed(),"No result found after searching the sponsor with name "+TestConstants.SPONSOR);
		sfHomePage.selectFirstSponsorFromList();
		sfHomePage.enterConsultantEnrollmentDetails(firstName, lastName, email, password, socialInsuranceNumber);
		sfHomePage.clickNextButton();
		s_assert.assertFalse(sfHomePage.isNextButtonEnabledBeforeSelectingKit(), "Next Button is NOT disabled before selecting kit");
		sfHomePage.chooseProductFromKitPage();
		sfHomePage.selectSubscribeToPulseCheckBox();
		sfHomePage.clickNextButton();
		sfHomePage.clickSaveButton();
		sfHomePage.enterConsultantShippingDetails(firstName, lastName, addressLine1, addressLine2 ,city, state, postalCode, phoneNumber);
		sfHomePage.clickUseAsEnteredButtonOnPopUp();
		sfHomePage.clickShippingDetailsNextbutton();
		sfHomePage.enterUserBillingDetails(cardType, cardNumber, cardName, CVV);
		//sfHomePage.selectBillingAddressFromDD();
		//sfHomePage.checkUseMyDeliveryAddressChkBox();
		sfHomePage.clickBillingDetailsNextbutton();
		sfHomePage.selectPoliciesAndProceduresChkBox();
		sfHomePage.selectIAcknowledgeChkBox();
		sfHomePage.selectTermsAndConditionsChkBox();
		sfHomePage.selectConsentFormChkBox();
		sfHomePage.clickBecomeAConsultant();
		s_assert.assertTrue(sfHomePage.isEnrollemntSuccessfulMsgDisplayed(), "Expected 'Your order number is' msg has NOT displayed"); 
		sfHomePage.clickRodanAndFieldsLogo();
		s_assert.assertTrue(sfHomePage.isWelcomeUserElementDisplayed(), "Welcome user locator has not displayed after consultant enrollment");
		conultantWithoutPulseAndWithoutCRP=email;
		s_assert.assertAll();
		conultantWithoutPulseAndWithoutCRP=email;
	}

	//CONSULTANT_EMAIL_WITH_CRP_AND_PULSE_FOR_CANCELLATION
	@Test(enabled=true, groups={"users"})
	public void testConsultantEnrollmentWithPulseAndCRPForCancellation(){
		navigateToStoreFrontBaseURL();
		timeStamp = CommonUtils.getCurrentTimeStamp();
		randomWords = CommonUtils.getRandomWord(5);		
		lastName = TestConstants.LAST_NAME+randomWords;
		email = firstName+"WPWCRPFORCANCELLATION"+timeStamp+TestConstants.EMAIL_SUFFIX;
		String socialInsuranceNumber = String.valueOf(CommonUtils.getRandomNum(100000000, 999999999));
		String prefix = firstName+"WPWCRP"+timeStamp;
		sfHomePage.clickEnrollNow();
		sfHomePage.searchSponsor(TestConstants.SPONSOR);
		sfHomePage.selectFirstSponsorFromList();
		sfHomePage.enterConsultantEnrollmentDetails(firstName, lastName, email, password, socialInsuranceNumber);
		sfHomePage.clickNextButton();
		sfHomePage.chooseProductFromKitPage();
		//sfHomePage.selectSubscribeToPulseCheckBox();
		sfHomePage.enterPrefix(prefix);
		sfHomePage.clickNextButton();
		sfHomePage.clickSaveButton();
		sfHomePage.enterConsultantShippingDetails(firstName, lastName, addressLine1, addressLine2 ,city, state, postalCode, phoneNumber);
		sfHomePage.clickUseAsEnteredButtonOnPopUp();
		sfHomePage.clickShippingDetailsNextbutton();
		sfHomePage.enterUserBillingDetails(cardType, cardNumber, cardName, CVV);
		sfHomePage.clickBillingDetailsNextbutton();
		sfHomePage.selectPoliciesAndProceduresChkBox();
		sfHomePage.selectIAcknowledgeChkBox();
		sfHomePage.selectTermsAndConditionsChkBox();
		sfHomePage.selectConsentFormChkBox();
		sfHomePage.clickBecomeAConsultant();
		s_assert.assertTrue(sfHomePage.isEnrollemntSuccessfulMsgDisplayed(), "Expected 'Your order number is' msg has NOT displayed"); 
		sfHomePage.addFirstProductForCRPCheckout();
		sfCheckoutPage = sfHomePage.checkoutCRPBag();
		sfCheckoutPage.clickSaveButton();
		sfCheckoutPage.clickShippingDetailsNextbutton();
		sfCheckoutPage.clickBillingDetailsNextbutton();
		sfCheckoutPage.selectTermsAndConditionsCheckBoxForConsulatntCRP();
		//		sfCheckoutPage.selectCheckboxForPoliciesAndProcedures();
		sfCheckoutPage.clickConfirmAutoshipOrderButton();
		s_assert.assertTrue(sfCheckoutPage.isCRPOrderConfirmedSuccessMsgAppeared(),"CRP Order confirmed success messge is not appeared");
		sfCheckoutPage.clickRodanAndFieldsLogo();
		s_assert.assertAll();	
		conultantWithPulseAndWithCRPForCancellation=email;
	}

	//For CONSULTANT_EMAIL_SPONSOR_WITHOUT_PWS
	@Test(enabled=true, groups={"users"},dependsOnMethods={"testConsultantEnrollmentWithoutCRPAndWithoutPulse"})
	public void testConsultantEnrollmentHavingSponsorWithoutPWS(){
		navigateToStoreFrontBaseURL();
		timeStamp = CommonUtils.getCurrentTimeStamp();
		randomWords = CommonUtils.getRandomWord(5);		
		lastName = TestConstants.LAST_NAME+randomWords;
		email = firstName+"SPONSORWOPWS"+timeStamp+TestConstants.EMAIL_SUFFIX;
		String socialInsuranceNumber = String.valueOf(CommonUtils.getRandomNum(100000000, 999999999));
		sfHomePage.clickEnrollNow();
		sfHomePage.searchSponsor(conultantWithoutPulseAndWithoutCRP);
		s_assert.assertTrue(sfHomePage.isSponsorResultDisplayed(),"No result found after searching the sponsor with name "+TestConstants.SPONSOR);
		sfHomePage.selectFirstSponsorFromList();
		sfHomePage.enterConsultantEnrollmentDetails(firstName, lastName, email, password, socialInsuranceNumber);
		sfHomePage.clickNextButton();
		s_assert.assertFalse(sfHomePage.isNextButtonEnabledBeforeSelectingKit(), "Next Button is NOT disabled before selecting kit");
		sfHomePage.chooseProductFromKitPage();
		sfHomePage.selectSubscribeToPulseCheckBox();
		sfHomePage.clickNextButton();
		sfHomePage.clickSaveButton();
		sfHomePage.enterConsultantShippingDetails(firstName, lastName, addressLine1, addressLine2 ,city, state, postalCode, phoneNumber);
		sfHomePage.clickUseAsEnteredButtonOnPopUp();
		sfHomePage.clickShippingDetailsNextbutton();
		sfHomePage.enterUserBillingDetails(cardType, cardNumber, cardName, CVV);
		//sfHomePage.selectBillingAddressFromDD();
		//sfHomePage.checkUseMyDeliveryAddressChkBox();
		sfHomePage.clickBillingDetailsNextbutton();
		sfHomePage.selectPoliciesAndProceduresChkBox();
		sfHomePage.selectIAcknowledgeChkBox();
		sfHomePage.selectTermsAndConditionsChkBox();
		sfHomePage.selectConsentFormChkBox();
		sfHomePage.clickBecomeAConsultant();
		s_assert.assertTrue(sfHomePage.isEnrollemntSuccessfulMsgDisplayed(), "Expected 'Your order number is' msg has NOT displayed"); 
		sfHomePage.clickRodanAndFieldsLogo();
		s_assert.assertTrue(sfHomePage.isWelcomeUserElementDisplayed(), "Welcome user locator has not displayed after consultant enrollment");
		conultantHavingSponsorWithoutPWS=email;
		s_assert.assertAll();
	}


	//	//Test Case Hybris Phase 2-3720 :: Version : 1 :: Perform Consultant Account termination through my account
	//	@Test(enabled=false)//Duplicate test,covered in Enrollment validation TC-4308
	//	public void testAccountTerminationPageForConsultant_3720() throws InterruptedException {
	//		/*		RFO_DB = driver.getDBNameRFO(); 
	//		List<Map<String, Object>> randomConsultantList =  null;
	//		String consultantEmailID = null;
	//		String accountID = null;
	//		storeFrontHomePage = new StoreFrontHomePage(driver);
	//		while(true){
	//			randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFO.callQueryWithArguement(DBQueries_RFO.GET_RANDOM_ACTIVE_CONSULTANT_WITH_ORDERS_AND_AUTOSHIPS_RFO,countryId),RFO_DB);
	//			consultantEmailID = (String) getValueFromQueryResult(randomConsultantList, "UserName");  
	//			accountID = String.valueOf(getValueFromQueryResult(randomConsultantList, "AccountID"));
	//			logger.info("Account Id of the user is "+accountID);
	//			storeFrontConsultantPage = storeFrontHomePage.loginAsConsultant(consultantEmailID, password);
	//			boolean isLoginError = driver.getCurrentUrl().contains("error");
	//			if(isLoginError){
	//				logger.info("Login error for the user "+consultantEmailID);
	//				driver.get(driver.getURL()+"/"+driver.getCountry());
	//			}
	//			else
	//				break;
	//		}
	//		 */		//s_assert.assertTrue(storeFrontConsultantPage.verifyConsultantPage(),"Consultant Page doesn't contain Welcome User Message");
	//	}

}