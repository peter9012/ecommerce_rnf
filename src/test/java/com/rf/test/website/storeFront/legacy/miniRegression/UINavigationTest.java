package com.rf.test.website.storeFront.legacy.miniRegression;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.annotations.Test;

import com.rf.core.utils.CommonUtils;
import com.rf.core.website.constants.TestConstantsRFL;
import com.rf.pages.website.storeFrontLegacy.StoreFrontLegacyHomePage;
import com.rf.test.website.RFLegacyStoreFrontWebsiteBaseTest;

public class UINavigationTest extends RFLegacyStoreFrontWebsiteBaseTest{
	private static final Logger logger = LogManager
			.getLogger(UINavigationTest.class.getName());

	private StoreFrontLegacyHomePage storeFrontLegacyHomePage;

	//Products-Redefine-Regimen-Links should be redirecting to the appropriate page
	@Test(enabled=true)
	public void testProductsRedefineRegimenLinksRedirectingToTheAppropriatePage(){
		String subLinkRegimen = TestConstantsRFL.REGIMEN_NAME_REDEFINE;
		String subLinkProducts = "Products";
		String subLinkResults = "Results";
		String subLinkTestimonials = "Testimonials";
		String subLinkInTheNews = "In the News";
		String subLinkFAQ = "FAQs";
		String subLinkAdvice = "Advice";
		String subLinkGlossary = "Glossary";

		storeFrontLegacyHomePage =  new StoreFrontLegacyHomePage(driver);
		storeFrontLegacyHomePage.clickProductsBtn();
		storeFrontLegacyHomePage.selectRegimen(subLinkRegimen);
		s_assert.assertTrue(storeFrontLegacyHomePage.getCurrentURL().toLowerCase().contains(subLinkRegimen.toLowerCase()), "Expected regimen name is "+subLinkRegimen.toLowerCase()+" Actual on UI is "+storeFrontLegacyHomePage.getCurrentURL().toLowerCase());

		storeFrontLegacyHomePage.clickSubSectionUnderRegimen(subLinkProducts);
		s_assert.assertTrue(storeFrontLegacyHomePage.getCurrentURL().toLowerCase().contains(subLinkProducts.toLowerCase()), "Expected sublink in url is "+subLinkProducts.toLowerCase()+" Actual on UI is "+storeFrontLegacyHomePage.getCurrentURL().toLowerCase());
		s_assert.assertTrue(storeFrontLegacyHomePage.verifyUserIsRedirectedToProductsPage(),"user is not on products page");
		storeFrontLegacyHomePage.navigateToBackPage();

		storeFrontLegacyHomePage.clickSubSectionUnderRegimen(subLinkResults);
		s_assert.assertTrue(storeFrontLegacyHomePage.getCurrentURL().toLowerCase().contains(subLinkResults.toLowerCase()), "Expected sublink in url is "+subLinkResults.toLowerCase()+" Actual on UI is "+storeFrontLegacyHomePage.getCurrentURL().toLowerCase());
		s_assert.assertTrue(storeFrontLegacyHomePage.verifyUserIsRedirectedToResultsPage(),"user is not on results page");
		storeFrontLegacyHomePage.navigateToBackPage();

		storeFrontLegacyHomePage.clickSubSectionUnderRegimen(subLinkTestimonials);
		s_assert.assertTrue(storeFrontLegacyHomePage.getCurrentURL().toLowerCase().contains(subLinkTestimonials.toLowerCase()), "Expected sublink in url is "+subLinkTestimonials.toLowerCase()+" Actual on UI is "+storeFrontLegacyHomePage.getCurrentURL().toLowerCase());
		s_assert.assertTrue(storeFrontLegacyHomePage.verifyUserIsRedirectedToTestimonialsPage(),"user is not on testimonials page");
		storeFrontLegacyHomePage.navigateToBackPage();

		storeFrontLegacyHomePage.clickSubSectionUnderRegimen(subLinkInTheNews);
		s_assert.assertTrue(storeFrontLegacyHomePage.getCurrentURL().toLowerCase().contains("News".toLowerCase()), "Expected sublink in url is "+"News".toLowerCase()+" Actual on UI is "+storeFrontLegacyHomePage.getCurrentURL().toLowerCase());
		s_assert.assertTrue(storeFrontLegacyHomePage.verifyUserIsRedirectedToNewsPage(),"user is not on News page");
		storeFrontLegacyHomePage.navigateToBackPage();

		storeFrontLegacyHomePage.clickSubSectionUnderRegimen(subLinkFAQ);
		s_assert.assertTrue(storeFrontLegacyHomePage.getCurrentURL().toLowerCase().contains("CommonQuestions".toLowerCase()), "Expected sublink in url is "+"CommonQuestions".toLowerCase()+" Actual on UI is "+storeFrontLegacyHomePage.getCurrentURL().toLowerCase());
		s_assert.assertTrue(storeFrontLegacyHomePage.verifyUserIsRedirectedToFAQsPage(),"user is not on FAQs page");
		storeFrontLegacyHomePage.navigateToBackPage();

		storeFrontLegacyHomePage.clickSubSectionUnderRegimen(subLinkAdvice);
		s_assert.assertTrue(storeFrontLegacyHomePage.getCurrentURL().toLowerCase().contains(subLinkAdvice.toLowerCase()), "Expected sublink in url is "+subLinkAdvice.toLowerCase()+" Actual on UI is "+storeFrontLegacyHomePage.getCurrentURL().toLowerCase());
		s_assert.assertTrue(storeFrontLegacyHomePage.verifyUserIsRedirectedToAdvicePage(),"user is not on advice page");
		storeFrontLegacyHomePage.navigateToBackPage();

		storeFrontLegacyHomePage.clickSubSectionUnderRegimen(subLinkGlossary);
		s_assert.assertTrue(storeFrontLegacyHomePage.getCurrentURL().toLowerCase().contains(subLinkGlossary.toLowerCase()), "Expected sublink in url is "+subLinkGlossary.toLowerCase()+" Actual on UI is "+storeFrontLegacyHomePage.getCurrentURL().toLowerCase());
		s_assert.assertTrue(storeFrontLegacyHomePage.verifyUserIsRedirectedToGlossaryPage(),"user is not on glossary page");
		storeFrontLegacyHomePage.navigateToBackPage();

		storeFrontLegacyHomePage.clickIngredientsAndUsageLink();
		s_assert.assertTrue(storeFrontLegacyHomePage.verifyIngredientsAndUsageInfoVisible(),"Ingredients and usage link is not present under regimen sublink.");

		s_assert.assertAll();

	}

	//Footer-Terms & Conditions link should redirecting to the appropriate page
	@Test(enabled=true)
	public void testFooterTermsAndConditionLinkShouldRedirectionToAppropriatePage(){
		storeFrontLegacyHomePage =  new StoreFrontLegacyHomePage(driver);
		storeFrontLegacyHomePage.clickTermsAndConditionsLink();
		s_assert.assertTrue(storeFrontLegacyHomePage.getCurrentURL().toLowerCase().contains("terms"), "Expected url having terms but actual on UI is "+storeFrontLegacyHomePage.getCurrentURL().toLowerCase());
		s_assert.assertAll();
	}

	//Satisfaction Guarantee-link should be redirecting properly 
	@Test(enabled=true)
	public void testSatisfactionGuaranteeLinkShouldBeRedirectionProperly(){
		storeFrontLegacyHomePage =  new StoreFrontLegacyHomePage(driver);
		storeFrontLegacyHomePage.clickSatisfactionGuaranteeLink();
		s_assert.assertTrue(storeFrontLegacyHomePage.isSatisfactionGuaranteePagePresent(), "Satisfaction guarantee page is not present after clicked on privacy policy link");
		s_assert.assertTrue(storeFrontLegacyHomePage.getCurrentURL().toLowerCase().contains("guarantee"), "Expected url having guarantee but actual on UI is "+storeFrontLegacyHomePage.getCurrentURL().toLowerCase());
		s_assert.assertAll();
	}

	//Verify solution tool rodan and fields consultant be working properly.
	@Test(enabled=true)
	public void testVerifyUserIsRedirectedToPwsAfterSelectingSponser(){
		String sponsorID = TestConstantsRFL.CID_CONSULTANT;
		String fetchedPWS = null;

		storeFrontLegacyHomePage =  new StoreFrontLegacyHomePage(driver);
		storeFrontLegacyHomePage.clickProductsBtn();
		storeFrontLegacyHomePage.clickSolutionToolUnderProduct();
		s_assert.assertTrue(storeFrontLegacyHomePage.verifySolutionToolPage(),"Solution tool page is displayed");
		storeFrontLegacyHomePage.clickFindRodanFieldConsultantLink();
		storeFrontLegacyHomePage.enterIDNumberAsSponsorForPCAndRC(sponsorID);
		storeFrontLegacyHomePage.clickBeginSearchBtn();
		fetchedPWS = storeFrontLegacyHomePage.getPWSFromFindConsultantPage();
		storeFrontLegacyHomePage.selectSponsorRadioBtnOnFindConsultantPage();
		storeFrontLegacyHomePage.clickSelectAndContinueBtnForPCAndRC();
		s_assert.assertFalse(storeFrontLegacyHomePage.getCurrentURL().contains(fetchedPWS),"Expected pws is: "+fetchedPWS +"While actual on UI: "+storeFrontLegacyHomePage.getCurrentURL());
		s_assert.assertAll();	
	}

	//The Getting Started Section Business Kit is displayed
	@Test(enabled=true)
	public void testGettingStartedSectionBusinessKitDisplayed(){
		storeFrontLegacyHomePage =  new StoreFrontLegacyHomePage(driver);
		storeFrontLegacyHomePage.clickBusinessSystemBtn();
		//verify the sub-menu title under the title business system?
		s_assert.assertTrue(storeFrontLegacyHomePage.validateEnrollNowLinkPresent(),"Enroll Now Link is not present");
		s_assert.assertTrue(storeFrontLegacyHomePage.validateMeetOurCommunityLinkPresent(),"Meet Our Link is not present");
		s_assert.assertTrue(storeFrontLegacyHomePage.validateEventsLinkPresent(),"Events Link is not present");
		s_assert.assertTrue(storeFrontLegacyHomePage.validateIncomeIllustratorLinkPresent(),"Income Illustrator Link is not present");
		s_assert.assertTrue(storeFrontLegacyHomePage.validateProgramsAndIncentivesLinkPresent(),"ProgramIncentives Link is not present");
		s_assert.assertTrue(storeFrontLegacyHomePage.validateWhyRFLinkPresent(),"Why RF Link is not present");
		//Navigate to business kit section-
		storeFrontLegacyHomePage.clickWhyRFLinkUnderBusinessSystem();
		storeFrontLegacyHomePage.clickBusinessKitsUnderWhyRF();
		//verify that the 'Business Kits' Section displays the information?
		s_assert.assertTrue(storeFrontLegacyHomePage.validateBusinessKitSectionIsDisplayed(),"Business Kit Section is not displayed with the Information");
		s_assert.assertAll();
	}

}