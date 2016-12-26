package com.rf.test.website.rehabitat.storeFront;

import org.testng.annotations.Test;

import com.rf.core.utils.CommonUtils;
import com.rf.core.website.constants.TestConstants;
import com.rf.test.website.rehabitat.storeFront.baseTest.StoreFrontWebsiteBaseTest;

public class ConsultantEnrollmentTest extends StoreFrontWebsiteBaseTest{

	/***
	 * qTest : TC-223 Find a Consultant/Search-CID
	 * 
	 * Description : This test validates that while consultant enrollment,valid & active sponsor
	 * should be searched and valid but inactive sponsor should not be searched.
	 * 				
	 */ 
	@Test  //TODO Inactive sponsor search is pending
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
	@Test //TODO The entire test
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
	@Test
	public void testConsultantEnrollment_231(){
		int randomNum = CommonUtils.getRandomNum(10000, 1000000);
		String socialInsuranceNumber = String.valueOf(CommonUtils.getRandomNum(100000000, 999999999));
		String firstName = TestConstants.FIRST_NAME;
		String lastName = TestConstants.LAST_NAME;
		String emailID = TestConstants.FIRST_NAME+randomNum+TestConstants.EMAIL_SUFFIX;
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
//		String confirmationMessageFromUI = null;
//		String expectedConfirmationMessage = "Your enrollment kit order number is";   
		sfHomePage.clickEnrollNow();
		sfHomePage.searchSponsor(TestConstants.SPONSOR);
		s_assert.assertTrue(sfHomePage.isSponsorResultDisplayed(),"No result found after searching the sponsor with name "+TestConstants.SPONSOR);
		sfHomePage.selectFirstSponsorFromList();
		sfHomePage.enterConsultantEnrollmentDetails(firstName, lastName, emailID, password, socialInsuranceNumber);
		sfHomePage.clickNextButton();
		sfHomePage.chooseProductFromKitPage();
		sfHomePage.clickNextButton();
		sfHomePage.clickSaveButton();
		sfHomePage.enterConsultantShippingDetails(firstName, lastName, addressLine1, addressLine2 ,city, state, postalCode, phoneNumber);
		sfHomePage.clickShippingDetailsNextbutton();
		sfHomePage.enterConsultantBillingDetails(cardType, cardNumber, cardName, CVV);
		//sfHomePage.selectBillingAddressFromDD();
		sfHomePage.checkUseMyDeliveryAddressChkBox();
		sfHomePage.clickBillingDetailsNextbutton();
		sfHomePage.selectTermsAndConditionsChkBox();
		sfHomePage.selectPoliciesAndProceduresChkBox();
		sfHomePage.clickBecomeAConsultant();
		//confirmationMessageFromUI = sfHomePage.getConfirmationMsgOfConsultantEnrollment();
		//s_assert.assertTrue(confirmationMessageFromUI.contains(expectedConfirmationMessage), "Expected confirmation message is"+expectedConfirmationMessage+" but actual on UI is "+confirmationMessageFromUI); 
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
	@Test //TODO
	public void testRequestSponsorConsultantEnrollment_261(){
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
		s_assert.assertAll();
	}

	/***
	 * qTest : TC-275 User finds a consultant
	 * 
	 * Description : This test validates the sponsor search from 'Find A Consultant' link
	 * and also validates the pagination in the sponsor search results.
	 * 				
	 */
	@Test
	public void testSponsorSearchFromFindAConsultantAndPagination_261(){
		sfHomePage.clickFindAConsultantLinkOnHomePage().searchSponsor(TestConstants.SPONSOR);
		s_assert.assertTrue(sfHomePage.isSponsorResultDisplayed(),"No result found after searching the sponsor with name "+TestConstants.SPONSOR);
		s_assert.assertTrue(sfHomePage.isTheUserOnNavigationPage("1"),"user is not on navigation page 1");
		sfHomePage.navigateToPaginationInSponsorSearchResult("2");
		s_assert.assertTrue(sfHomePage.isTheUserOnNavigationPage("2"),"user is not on navigation page 2");
		s_assert.assertAll();
	}

	/***
	 * qTest : TC-273 User enter a name sponsor that doesn't exist in the RF
	 * 
	 * Description : This test validates that while consultant enrollment,if a sponsor is searched that
	 * doesn't exist in R+F system then the 'No Search Results' message should be displayed
	 * 				
	 */
	@Test
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
	@Test
	public void testConsultantEnrollmentPersonalBusinessPortfolio_290(){
		int randomNum = CommonUtils.getRandomNum(10000, 1000000);
		String socialInsuranceNumber = String.valueOf(CommonUtils.getRandomNum(100000000, 999999999));
		String firstName = TestConstants.FIRST_NAME;
		String lastName = TestConstants.LAST_NAME;
		String emailID = TestConstants.FIRST_NAME+randomNum+TestConstants.EMAIL_SUFFIX;
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
		String kitName = "BUSINESS PORTFOLIO";
//		String confirmationMessageFromUI = null;
//		String expectedConfirmationMessage = "Your enrollment kit order number is";   
		sfHomePage.clickEnrollNow();
		sfHomePage.searchSponsor(TestConstants.SPONSOR);
		s_assert.assertTrue(sfHomePage.isSponsorResultDisplayed(),"No result found after searching the sponsor with name "+TestConstants.SPONSOR);
		sfHomePage.selectFirstSponsorFromList();
		sfHomePage.enterConsultantEnrollmentDetails(firstName, lastName, emailID, password, socialInsuranceNumber);
		sfHomePage.clickNextButton();
		sfHomePage.chooseProductFromKitPage(kitName);
		sfHomePage.clickNextButton();
		sfHomePage.clickSaveButton();
		sfHomePage.enterConsultantShippingDetails(firstName, lastName, addressLine1, addressLine2 ,city, state, postalCode, phoneNumber);
		sfHomePage.clickShippingDetailsNextbutton();
		sfHomePage.enterConsultantBillingDetails(cardType, cardNumber, cardName, CVV);
		//sfHomePage.selectBillingAddressFromDD();
		sfHomePage.checkUseMyDeliveryAddressChkBox();
		sfHomePage.clickBillingDetailsNextbutton();
		sfHomePage.selectTermsAndConditionsChkBox();
		sfHomePage.selectPoliciesAndProceduresChkBox();
		sfHomePage.clickBecomeAConsultant();
		//confirmationMessageFromUI = sfHomePage.getConfirmationMsgOfConsultantEnrollment();
		//s_assert.assertTrue(confirmationMessageFromUI.contains(expectedConfirmationMessage), "Expected confirmation message is"+expectedConfirmationMessage+" but actual on UI is "+confirmationMessageFromUI); 
		s_assert.assertTrue(sfHomePage.isWelcomeUserElementDisplayed(), "Welcome user locator has not displayed after consultant enrollment");
		s_assert.assertAll();
	}

	/***
	 * qTest : TC-291 User selects View/hid details CTA in the enrollment kit selected
	 * 
	 * Description : //todo
	 * 				
	 */
	@Test//TODO
	public void testViewHideCTADetailsInEnrollmentKit_291(){
		int randomNum = CommonUtils.getRandomNum(10000, 1000000);
		String socialInsuranceNumber = String.valueOf(CommonUtils.getRandomNum(100000000, 999999999));
		String firstName = TestConstants.FIRST_NAME;
		String lastName = TestConstants.LAST_NAME;
		String emailID = TestConstants.FIRST_NAME+randomNum+TestConstants.EMAIL_SUFFIX;
		sfHomePage.clickEnrollNow();
		sfHomePage.searchSponsor(TestConstants.SPONSOR);
		s_assert.assertTrue(sfHomePage.isSponsorResultDisplayed(),"No result found after searching the sponsor with name "+TestConstants.SPONSOR);
		sfHomePage.selectFirstSponsorFromList();
		sfHomePage.enterConsultantEnrollmentDetails(firstName, lastName, emailID, password, socialInsuranceNumber);
		sfHomePage.clickNextButton();
		s_assert.assertAll();
	}
	
	/***
	 * qTest : TC-485 North Dakota Error-Valid ND billing address
	 * 
	 * Description : This test validates that complete checkout process for consultant
	 * enrollment
	 *     
	 */
	@Test
	public void testNortDakotaConsultantEnrollmentValidBillingAddress_485(){
		int randomNum = CommonUtils.getRandomNum(10000, 1000000);
		String socialInsuranceNumber = String.valueOf(CommonUtils.getRandomNum(100000000, 999999999));
		String firstName = TestConstants.FIRST_NAME;
		String lastName = TestConstants.LAST_NAME;
		String emailID = TestConstants.FIRST_NAME+randomNum+TestConstants.EMAIL_SUFFIX;
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
//		String confirmationMessageFromUI = null;
//		String expectedConfirmationMessage = "Your enrollment kit order number is";   
		sfHomePage.clickEnrollNow();
		sfHomePage.searchSponsor(TestConstants.SPONSOR);
		s_assert.assertTrue(sfHomePage.isSponsorResultDisplayed(),"No result found after searching the sponsor with name "+TestConstants.SPONSOR);
		sfHomePage.selectFirstSponsorFromList();
		sfHomePage.enterConsultantEnrollmentDetails(firstName, lastName, emailID, password, socialInsuranceNumber);
		sfHomePage.clickNextButton();
		sfHomePage.chooseProductFromKitPage();
		sfHomePage.clickNextButton();
		sfHomePage.clickSaveButton();
		sfHomePage.enterConsultantShippingDetails(firstName, lastName, addressLine1, addressLine2 ,city, state, postalCode, phoneNumber);
		sfHomePage.clickShippingDetailsNextbutton();
		sfHomePage.enterConsultantBillingDetails(cardType, cardNumber, cardName, CVV);
		//sfHomePage.selectBillingAddressFromDD();
		sfHomePage.checkUseMyDeliveryAddressChkBox();
		sfHomePage.clickBillingDetailsNextbutton();
		sfHomePage.selectTermsAndConditionsChkBox();
		sfHomePage.selectPoliciesAndProceduresChkBox();
		sfHomePage.clickBecomeAConsultant();
		//confirmationMessageFromUI = sfHomePage.getConfirmationMsgOfConsultantEnrollment();
		//s_assert.assertTrue(confirmationMessageFromUI.contains(expectedConfirmationMessage), "Expected confirmation message is"+expectedConfirmationMessage+" but actual on UI is "+confirmationMessageFromUI); 
		s_assert.assertTrue(sfHomePage.isWelcomeUserElementDisplayed(), "Welcome user locator has not displayed after consultant enrollment");
		s_assert.assertAll();
	}
	
	


	//Test Case Hybris Phase 2-3720 :: Version : 1 :: Perform Consultant Account termination through my account
	@Test(enabled=false)//Duplicate test,covered in Enrollment validation TC-4308
	public void testAccountTerminationPageForConsultant_3720() throws InterruptedException {
		/*		RFO_DB = driver.getDBNameRFO(); 
		List<Map<String, Object>> randomConsultantList =  null;
		String consultantEmailID = null;
		String accountID = null;
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
				driver.get(driver.getURL()+"/"+driver.getCountry());
			}
			else
				break;
		}
		 */		//s_assert.assertTrue(storeFrontConsultantPage.verifyConsultantPage(),"Consultant Page doesn't contain Welcome User Message");
	}


}