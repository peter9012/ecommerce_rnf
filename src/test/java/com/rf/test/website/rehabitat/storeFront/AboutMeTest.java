package com.rf.test.website.rehabitat.storeFront;

import org.testng.annotations.Test;

import com.rf.core.utils.CommonUtils;
import com.rf.core.website.constants.TestConstants;
import com.rf.test.website.rehabitat.storeFront.baseTest.StoreFrontWebsiteBaseTest;

public class AboutMeTest extends StoreFrontWebsiteBaseTest{

	/***
	 * qTest : TC-578 PWS sites - Access Join URL
	 * Description : This test validate the Enroll Now, Events, Meet Our Community,
	 *  Programs and Incentives, Why RF links when accessed join url of consultant
	 * 
	 *     
	 */
	@Test(enabled=true)
	public void testPWSSitesAccessJoinURL_578(){
		String homePageURL = null;
		String currentURL = null;
		String prefix = TestConstants.CONSULTANT_PWS_PREFIX;
		String urlToAssertForEventsLink = "/events";
		String urlToAssertForEnrollNowLink = "/enrollment/consultant-registration";
		String urlToAssertForProgramsAndIncentivesLink = "/programs-incentives";
		String urlToAssertForMeetOurCommunityLink = "/meet-our-community";
		String urlToAssertForWhyRFLink = "/why-rf";

		homePageURL = sfHomePage.getCurrentURL();
		sfHomePage.navigateToUrl(homePageURL + "/pws/" + prefix + "/join");
		// Navigating to Events Link 
		sfHomePage.clickEvents();
		currentURL = sfHomePage.getCurrentURL().toLowerCase();
		s_assert.assertTrue(currentURL.endsWith(urlToAssertForEventsLink),"Expected URL should contain "+urlToAssertForEventsLink+" but actual on UI is "+currentURL);
		s_assert.assertTrue(sfHomePage.isEventsPageHeaderDisplayed(),"Events page header is not displayed");
		// Navigating to Enroll now Link
		sfHomePage.clickRodanAndFieldsLogo();
		sfHomePage.clickEnrollNow();
		currentURL = sfHomePage.getCurrentURL().toLowerCase();
		s_assert.assertTrue(currentURL.endsWith(urlToAssertForEnrollNowLink),"Expected URL should contain "+urlToAssertForEnrollNowLink+" but actual on UI is "+currentURL);
		s_assert.assertTrue(sfHomePage.isEnrollAsConsultantPageHeaderDisplayed(),"Enroll as consultant page header is not displayed");
		// Navigating to Programs and Incentives Link 
		sfHomePage.clickRodanAndFieldsLogo();
		sfHomePage.clickProgramsAndIncentives();
		currentURL = sfHomePage.getCurrentURL().toLowerCase();
		s_assert.assertTrue(currentURL.endsWith(urlToAssertForProgramsAndIncentivesLink),"Expected URL should contain "+urlToAssertForProgramsAndIncentivesLink+" but actual on UI is "+currentURL);
		s_assert.assertTrue(sfHomePage.isProgramsAndIncentivesPageHeaderDisplayed(),"Programs and incentives page header is not displayed");
		// Navigating to Meet our community Link
		sfHomePage.clickRodanAndFieldsLogo();
		sfHomePage.clickMeetOurCommunityLink();
		currentURL = sfHomePage.getCurrentURL().toLowerCase();
		s_assert.assertTrue(currentURL.endsWith(urlToAssertForMeetOurCommunityLink),"Expected URL should contain "+urlToAssertForMeetOurCommunityLink+" but actual on UI is "+currentURL);
		s_assert.assertTrue(sfHomePage.isMeetOurCommunityPageHeaderDisplayed(),"Meet our community page header is not displayed");
		// Navigating to Why RF Link 
		sfHomePage.clickRodanAndFieldsLogo();
		sfHomePage.clickWhyRF();
		currentURL = sfHomePage.getCurrentURL().toLowerCase();
		s_assert.assertTrue(currentURL.endsWith(urlToAssertForWhyRFLink),"Expected URL should contain "+urlToAssertForWhyRFLink+" but actual on UI is "+currentURL);
		s_assert.assertTrue(sfHomePage.isWhyRFPageHeaderDisplayed(),"Why RF page header is not displayed");
		s_assert.assertAll();
	}

	/***
	 * qTest : TC-576 PWS sites - Enroll with Pulse
	 * Description : This test validates the enrollment of consultant with pulse.  
	 * 
	 *     
	 */
	@Test(enabled=true)
	public void testPWSSitesEnrollWithPulse_576(){
		String timeStamp = CommonUtils.getCurrentTimeStamp();
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
		String prefix = TestConstants.FIRST_NAME+timeStamp;
		String currentURL = null;
		String urlToAssert = "/pws/" + prefix.toLowerCase();
		sfHomePage.clickEnrollNow();
		sfHomePage.searchSponsor(TestConstants.SPONSOR);
		s_assert.assertTrue(sfHomePage.isSponsorResultDisplayed(),"No result found after searching the sponsor with name "+TestConstants.SPONSOR);
		sfHomePage.selectFirstSponsorFromList();
		sfHomePage.enterConsultantEnrollmentDetails(firstName, lastName, emailID, password, socialInsuranceNumber);
		sfHomePage.clickNextButton();
		s_assert.assertFalse(sfHomePage.isNextButtonEnabledBeforeSelectingKit(), "Next Button is NOT disabled before selecting kit");
		sfHomePage.chooseProductFromKitPage();
		sfHomePage.selectSubscribeToPulseCheckBox();
		sfHomePage.enterPrefix(prefix);
		s_assert.assertTrue(sfHomePage.isEmailAvailableMsgAppearedForPWS(),"Email available msg is not present for entered prefix : " + prefix);
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
		s_assert.assertTrue(sfHomePage.isEnrollemntSuccessfulMsgDisplayed(), "Expected 'ENROLLMENT SUCCESSFUL' msg has NOT displayed"); 
		sfHomePage.clickRodanAndFieldsLogo();
		currentURL = sfHomePage.getCurrentURL();
		s_assert.assertTrue(currentURL.contains(urlToAssert),"Expected URL should contain "+urlToAssert+" but actual on UI is "+currentURL);
		s_assert.assertAll();
	}

	/***
	 * qTest : TC-577 PWS sites - About sponsor page
	 * Description : This test validate the About me page navigated through the Sponsor name
	 *  from the top for Anonymous user, Consultant, PC and RC.
	 * 
	 *     
	 */
	@Test(enabled=true)
	public void testPWSSitesAboutSponsorPage_577(){
		String homePageURL = null;
		String currentURL = null;
		String urlToAssert = "about-me";
		String prefix = TestConstants.CONSULTANT_PWS_PREFIX;
		homePageURL = sfHomePage.getCurrentURL();
		sfHomePage.navigateToUrl(homePageURL + "/pws/" + prefix);
		// For Anonymous User
		sfAboutMePage = sfHomePage.clickSponsorNameLink();
		currentURL = sfAboutMePage.getCurrentURL();
		s_assert.assertTrue(sfAboutMePage.isContactMeHeaderPresentOnAboutMePage(),"Contact me header is not present on the about me page of sponsor");
		s_assert.assertTrue(currentURL.contains(urlToAssert),"Expected URL should contain "+urlToAssert+" but actual on UI is "+currentURL);
		// For Consultant
		sfHomePage.clickRodanAndFieldsLogo();
		sfHomePage.loginToStoreFront(TestConstants.CONSULTANT_EMAIL_WITH_CRP_AND_PULSE,password,true);
		sfHomePage.clickSponsorNameLink();
		currentURL = sfAboutMePage.getCurrentURL();
		s_assert.assertTrue(sfAboutMePage.isContactMeHeaderPresentOnAboutMePage(),"Contact me header is not present on the about me page of sponsor");
		s_assert.assertTrue(currentURL.contains(urlToAssert),"Expected URL should contain "+urlToAssert+" but actual on UI is "+currentURL);
		sfHomePage.clickWelcomeDropdown();
		sfHomePage.logout();
		// For PC
		sfHomePage.clickRodanAndFieldsLogo();
		sfHomePage.loginToStoreFront(TestConstants.PC_EMAIL_HAVING_AUTOSHIP,password,true);
		sfHomePage.clickSponsorNameLink();
		currentURL = sfAboutMePage.getCurrentURL();
		s_assert.assertTrue(sfAboutMePage.isContactMeHeaderPresentOnAboutMePage(),"Contact me header is not present on the about me page of sponsor");
		s_assert.assertTrue(currentURL.contains(urlToAssert),"Expected URL should contain "+urlToAssert+" but actual on UI is "+currentURL);
		sfHomePage.clickWelcomeDropdown();
		sfHomePage.logout();
		// For RC
		sfHomePage.clickRodanAndFieldsLogo();
		sfHomePage.loginToStoreFront(TestConstants.RC_EMAIL_HAVING_ORDER,password,true);
		sfHomePage.clickSponsorNameLink();
		currentURL = sfAboutMePage.getCurrentURL();
		s_assert.assertTrue(sfAboutMePage.isContactMeHeaderPresentOnAboutMePage(),"Contact me header is not present on the about me page of sponsor");
		s_assert.assertTrue(currentURL.contains(urlToAssert),"Expected URL should contain "+urlToAssert+" but actual on UI is "+currentURL);
		sfHomePage.clickWelcomeDropdown();
		sfHomePage.logout();
		s_assert.assertAll();
	}

	/***
	 * qTest : TC-616 As Anonymous user redirect to consultant's About me page
	 * Description : This test validates the presence of Expected ques and answer on about me page
	 * 
	 *     
	 */
	@Test(enabled=true)
	public void testAsAnonymousUserRedirectToConsultantsAboutMePage_616(){
		String homePageURL = null;
		String currentURL = null;
		String urlToAssert = "about-me";
		String prefix = TestConstants.CONSULTANT_PWS_PREFIX;
		// As an anonymous user
		homePageURL = sfHomePage.getCurrentURL();
		sfHomePage.navigateToUrl(homePageURL + "/pws/" + prefix);
		sfAboutMePage = sfHomePage.clickSponsorNameLink();
		currentURL = sfHomePage.getCurrentURL();
		s_assert.assertTrue(currentURL.contains(urlToAssert),"Expected URL should contain "+urlToAssert+" but actual on UI is "+currentURL);
		s_assert.assertTrue(sfAboutMePage.isExpectedQuesPresentOnAboutMePage(TestConstants.ABOUT_ME_PAGE_QUES_FROM_PREFIX_URL),"Expected Ques is not present on about me page when navigated from prefix url");
		s_assert.assertTrue(sfAboutMePage.isAnswerOfExpectedQuesPresentOnAboutMePage(TestConstants.ABOUT_ME_PAGE_QUES_FROM_PREFIX_URL),"Answer description of Ques is not present on about me page when navigated from prefix url");
		sfHomePage.navigateToUrl(homePageURL + "/pws/" + prefix + "/join");
		sfHomePage.clickSponsorNameLink();
		s_assert.assertTrue(sfAboutMePage.isExpectedQuesPresentOnAboutMePage(TestConstants.ABOUT_ME_PAGE_QUES_FROM_JOIN_URL),"Expected Ques is not present on about me page when navigated from join url");
		s_assert.assertTrue(sfAboutMePage.isAnswerOfExpectedQuesPresentOnAboutMePage(TestConstants.ABOUT_ME_PAGE_QUES_FROM_JOIN_URL),"Answer description of Ques is not present on about me page when navigated from join url");
		s_assert.assertAll();
	}

	/***
	 * qTest : TC-581 Personalizing PWS (About Me Page) - Submission Guidelines
	 * Description : This test validates Personalizing PWS (About Me Page) - Submission Guidelines
	 *    
	 */
	@Test(enabled=false)
	public void testPersonalizingPWSAboutMePageSubmissionGuidelines_581(){
		String homePageURL = null;
		String currentURL = null;
		String currentWindowID=null;
		String urlToAssert = "about-me";
		String aboutMeEdit="about-me/edit";
		String submissionGuidelineUrl="PWS-Profile-Guidelines.pdf";
		String prefix = TestConstants.CONSULTANT_PWS_PREFIX;

		homePageURL = sfHomePage.getCurrentURL();
		sfHomePage.navigateToUrl(homePageURL + "/pws/" + prefix);
		sfHomePage.loginToStoreFront(TestConstants.CONSULTANT_EMAIL_WITH_CRP_AND_PULSE, password,true);
		sfHomePage.clickWelcomeDropdown();
		sfAboutMePage=sfHomePage.navigateToEditPWSPage();
		currentURL = sfAboutMePage.getCurrentURL();
		s_assert.assertTrue(currentURL.contains(urlToAssert),"Expected URL should contain "+urlToAssert+" but actual on UI is "+currentURL);
		sfAboutMePage.clickPersonalizeMyProfileButton();
		currentURL = sfAboutMePage.getCurrentURL();
		s_assert.assertTrue(sfAboutMePage.isSubmissionGuidelinesDisplayed() && currentURL.contains(aboutMeEdit),"Expected user should redirected to: "+aboutMeEdit +" Page after click on 'personalize My profile' button but actual user is redirected to "+currentURL);
		currentWindowID = CommonUtils.getCurrentWindowHandle();
		sfAboutMePage.clickSubmissionGuidelinesLink();
		sfAboutMePage.switchToChildWindow(currentWindowID);
		currentURL = sfAboutMePage.getCurrentURL();
		s_assert.assertTrue(currentURL.contains(submissionGuidelineUrl), "Expected URL should contain "+submissionGuidelineUrl+" but actual on UI is"+currentURL);
		s_assert.assertAll();
	}
	
	/***
	 * qTest : TC-618 Add City and Province to Consultants About me page
	 * Description : This test validates add city and province on consultant about me page
	 *    
	 */
	@Test(enabled=false)
	public void testAddCityAndProvinceToConsultantsAboutMePage_618(){
		String homePageURL = null;
		String currentURL = null;
		String city="San Francisco";
		String visibleStateName="New York";
		String pageHeader=null;
		String urlToAssert = "about-me";
		String aboutMeEdit="about-me/edit";
		String prefix = TestConstants.CONSULTANT_PWS_PREFIX;
		homePageURL = sfHomePage.getCurrentURL();

		sfHomePage.navigateToUrl(homePageURL + "/pws/" + prefix);
		sfHomePage.loginToStoreFront(TestConstants.CONSULTANT_EMAIL_WITH_CRP_AND_PULSE, password,true);
		sfHomePage.clickWelcomeDropdown();
		sfAboutMePage=sfHomePage.navigateToEditPWSPage();
		currentURL = sfAboutMePage.getCurrentURL();
		s_assert.assertTrue(currentURL.contains(urlToAssert),"Expected URL should contain "+urlToAssert+" but actual on UI is "+currentURL);
		sfAboutMePage.clickPersonalizeMyProfileButton();
		currentURL = sfAboutMePage.getCurrentURL();
		s_assert.assertTrue(sfAboutMePage.isSubmissionGuidelinesDisplayed() && currentURL.contains(aboutMeEdit),"Expected user should redirected to: "+aboutMeEdit +" Page after click on 'personalize My profile' button but actual user is redirected to "+currentURL);
		sfAboutMePage.typeCityOnAboutMePage(city);
		sfAboutMePage.selectProvinceOnAboutMePage(visibleStateName);
		sfAboutMePage.clickSaveButton();
		pageHeader=sfAboutMePage.getCityStateTextOnAboutMePageHeader();
		s_assert.assertTrue(pageHeader.contains(city),"Expected about me page header should contains:"+city+ "But actual on UI is"+pageHeader);
		s_assert.assertTrue(pageHeader.contains(visibleStateName),"Expected about me page header should contains:"+visibleStateName+ "But actual on UI is"+pageHeader);
		s_assert.assertAll();
	}
	
	/***
	 * qTest : TC-619 Add Year information on the About Me page
	 * Description : This test validates add year information on the About Me page and verify the year is editable or not
	 *    
	 */
	@Test(enabled=false)
	public void testAddYearInformationOnTheAboutMePage_619(){
		String homePageURL = null;
		String currentURL = null;
		String labelName=null;
		String showSinceText=null;
		String urlToAssert = "about-me";
		String aboutMeEdit="about-me/edit";
		String prefix = TestConstants.CONSULTANT_PWS_PREFIX;

		homePageURL = sfHomePage.getCurrentURL();
		sfHomePage.navigateToUrl(homePageURL + "/pws/" + prefix);
		sfHomePage.loginToStoreFront(TestConstants.CONSULTANT_EMAIL_WITH_CRP_AND_PULSE, password,true);
		sfHomePage.clickWelcomeDropdown();
		sfAboutMePage=sfHomePage.navigateToEditPWSPage();
		currentURL = sfAboutMePage.getCurrentURL();
		s_assert.assertTrue(currentURL.contains(urlToAssert),"Expected URL should contain "+urlToAssert+" but actual on UI is "+currentURL);
		sfAboutMePage.clickPersonalizeMyProfileButton();
		currentURL = sfAboutMePage.getCurrentURL();
		s_assert.assertTrue(sfAboutMePage.isSubmissionGuidelinesDisplayed() && currentURL.contains(aboutMeEdit),"Expected user should redirected to: "+aboutMeEdit +" Page after click on 'personalize My profile' button but actual user is redirected to "+currentURL);
		labelName=sfAboutMePage.selectCheckboxShowSinceAndReturnLabelName();
		s_assert.assertTrue(sfAboutMePage.isConsultantSinceEditable(),"Expected consultant since year should not be editable");
		sfAboutMePage.clickSaveButton();
		showSinceText=sfAboutMePage.getShowSinceTextOnAboutMePageHeader();
		s_assert.assertTrue(labelName.contains(showSinceText),"Expected about me page header should contain:"+labelName+" But actual on UI is:"+showSinceText);
		sfAboutMePage.clickPersonalizeMyProfileButton();
		currentURL = sfAboutMePage.getCurrentURL();
		s_assert.assertTrue(sfAboutMePage.isSubmissionGuidelinesDisplayed() && currentURL.contains(aboutMeEdit),"Expected user should redirected to: "+aboutMeEdit +" Page after click on 'personalize My profile' button but actual user is redirected to "+currentURL);
		sfAboutMePage.deSelectCheckboxShowSince();
		sfAboutMePage.clickSaveButton();
		s_assert.assertFalse(sfAboutMePage.isShowSincePresentOnAboutMePageHeader(),"Expected show since consultant should not be present on about me page after deselect checkbox");
		s_assert.assertAll();
	}
	
	/***
	 * qTest : TC-620 Add phone number and email address on the About Me page
	 * Description : This test validates Add phone number and email address on the About Me page
	 *    
	 */
	@Test(enabled=false)
	public void testAddPhoneNumberAndEmailAddressOnTheAboutMePage_620(){
		String homePageURL = null;
		String currentURL = null;
		String phoneNo=TestConstants.PHONE_NUMBER;
		String email=TestConstants.CONSULTANT_EMAIL_WITH_CRP_AND_PULSE;
		String phoneNumberOnAboutMePage=null;
		String emailOnAboutMePage=null;
		String urlToAssert = "about-me";
		String aboutMeEdit="about-me/edit";
		String prefix = TestConstants.CONSULTANT_PWS_PREFIX;

		homePageURL = sfHomePage.getCurrentURL();
		sfHomePage.navigateToUrl(homePageURL + "/pws/" + prefix);
		sfHomePage.loginToStoreFront(TestConstants.CONSULTANT_EMAIL_WITH_CRP_AND_PULSE, password,true);
		sfHomePage.clickWelcomeDropdown();
		sfAboutMePage=sfHomePage.navigateToEditPWSPage();
		currentURL = sfAboutMePage.getCurrentURL();
		s_assert.assertTrue(currentURL.contains(urlToAssert),"Expected URL should contain "+urlToAssert+" but actual on UI is "+currentURL);
		sfAboutMePage.clickPersonalizeMyProfileButton();
		currentURL = sfAboutMePage.getCurrentURL();
		s_assert.assertTrue(sfAboutMePage.isSubmissionGuidelinesDisplayed() && currentURL.contains(aboutMeEdit),"Expected user should redirected to: "+aboutMeEdit +" Page after click on 'personalize My profile' button but actual user is redirected to "+currentURL);
		sfAboutMePage.typePhoneAndEmailOnAboutMePage(phoneNo, email);
		sfAboutMePage.clickSaveButton();
		phoneNumberOnAboutMePage=sfAboutMePage.getPhoneNumberFromAboutMePageHeader();
		emailOnAboutMePage=sfAboutMePage.getEmailFromAboutMePageHeader();
		s_assert.assertTrue(phoneNumberOnAboutMePage.contains(phoneNo),"Expected about me page should contain:"+phoneNo+"But actual on UI is:"+phoneNumberOnAboutMePage);
		s_assert.assertTrue(emailOnAboutMePage.contains(email),"Expected about me page should contain:"+email+"But actual on UI is:"+emailOnAboutMePage);
		s_assert.assertAll();
	}

	/***
	 * qTest : TC-621 Do not enter or select information in any of Contact or Social media fields
	 * Description : This test validates remove details of social media and contact and verify system will not display any detail on about me page
	 *    
	 */
	@Test(enabled=false)
	public void testDontEnterOrSelectInformationInAnyOfContactOrSocialMediaFields_621(){
		String homePageURL = null;
		String currentURL = null;
		String urlToAssert = "about-me";
		String aboutMeEdit="about-me/edit";
		String prefix = TestConstants.CONSULTANT_PWS_PREFIX;

		homePageURL = sfHomePage.getCurrentURL();
		sfHomePage.navigateToUrl(homePageURL + "/pws/" + prefix);
		sfHomePage.loginToStoreFront(TestConstants.CONSULTANT_EMAIL_WITH_CRP_AND_PULSE, password,true);
		sfHomePage.clickWelcomeDropdown();
		sfAboutMePage=sfHomePage.navigateToEditPWSPage();
		currentURL = sfAboutMePage.getCurrentURL();
		s_assert.assertTrue(currentURL.contains(urlToAssert),"Expected URL should contain "+urlToAssert+" but actual on UI is "+currentURL);
		sfAboutMePage.clickPersonalizeMyProfileButton();
		currentURL = sfAboutMePage.getCurrentURL();
		s_assert.assertTrue(sfAboutMePage.isSubmissionGuidelinesDisplayed() && currentURL.contains(aboutMeEdit),"Expected user should redirected to: "+aboutMeEdit +" Page after click on 'personalize My profile' button but actual user is redirected to "+currentURL);
		sfAboutMePage.clearContactAndSocialMediaFields();
		sfAboutMePage.clickSaveButton();
		s_assert.assertFalse(sfAboutMePage.isContactOrSocialMediaDetailsPresentOnAboutMePage(),"Expected contact details or social media fields should not be displayed on about me page");
		s_assert.assertAll();
	}

	/***
	 * qTest : TC-622 Add social Media account to their About Me page
	 * Description : This test validates Add social Media account to their About Me page
	 *    
	 */
	@Test(enabled=false)
	public void testAddSocialMediaAccountToTheirAboutMePage_622(){
		String homePageURL = null;
		String currentURL = null;
		String fbId="facebook@xyz.com";
		String twitterID="twitter@xyz.com";
		String pinterestId="pinterest@xyz.com";
		String urlToAssert = "about-me";
		String aboutMeEdit="about-me/edit";
		String prefix = TestConstants.CONSULTANT_PWS_PREFIX;

		homePageURL = sfHomePage.getCurrentURL();
		sfHomePage.navigateToUrl(homePageURL + "/pws/" + prefix);
		sfHomePage.loginToStoreFront(TestConstants.CONSULTANT_EMAIL_WITH_CRP_AND_PULSE, password,true);
		sfHomePage.clickWelcomeDropdown();
		sfAboutMePage=sfHomePage.navigateToEditPWSPage();
		currentURL = sfAboutMePage.getCurrentURL();
		s_assert.assertTrue(currentURL.contains(urlToAssert),"Expected URL should contain "+urlToAssert+" but actual on UI is "+currentURL);
		sfAboutMePage.clickPersonalizeMyProfileButton();
		currentURL = sfAboutMePage.getCurrentURL();
		s_assert.assertTrue(sfAboutMePage.isSubmissionGuidelinesDisplayed() && currentURL.contains(aboutMeEdit),"Expected user should redirected to: "+aboutMeEdit +" Page after click on 'personalize My profile' button but actual user is redirected to "+currentURL);
		sfAboutMePage.enterSocialMediaFieldsOnAboutMePage(fbId, twitterID, pinterestId);
		sfAboutMePage.clickSaveButton();
		s_assert.assertFalse(sfAboutMePage.isInstagramLinkDisplayedOnAboutMePage(),"Expected Instagram details should not be displayed on about me page");
		s_assert.assertAll();
	}

}