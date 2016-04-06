package com.rf.test.website.storeFront.legacy.regression;

import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.annotations.Test;

import com.rf.core.utils.DBUtil;
import com.rf.core.website.constants.TestConstantsRFL;
import com.rf.core.website.constants.dbQueries.DBQueries_RFL;
import com.rf.pages.website.storeFrontLegacy.StoreFrontLegacyHomePage;
import com.rf.test.website.RFLegacyStoreFrontWebsiteBaseTest;

public class UINavigationTest extends RFLegacyStoreFrontWebsiteBaseTest{
	private static final Logger logger = LogManager
			.getLogger(UINavigationTest.class.getName());

	private StoreFrontLegacyHomePage storeFrontLegacyHomePage;
	private String RFL_DB = null;

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

	//Reverse Products-links should be working properly (products, testimonials, in the news, FAQ's, advice, glossary)
	@Test
	public void testReverseProductsLinksWorkingProperly(){
		String subLinkRegimen = TestConstantsRFL.REGIMEN_NAME_REVERSE;
		String subLinkProducts = "Products";
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

		s_assert.assertAll();
	}

	//Unblemish Products-links should be working properly (products, testimonials, in the news, FAQ's, advice, glossary)
	@Test(enabled=false)//WIP
	public void testUnblemishProductsLinksWorkingProperly(){
		String subLinkRegimen = TestConstantsRFL.REGIMEN_NAME_UNBLEMISH;
		String subLinkProducts = "Products";
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

		s_assert.assertAll();
	}

	//Soothe Products-links should be working properly (products, testimonials, in the news, FAQ's, advice, glossary)
	@Test(enabled=false)//WIP
	public void testSootheProductsLinksWorkingProperly(){
		String subLinkRegimen = TestConstantsRFL.REGIMEN_NAME_SOOTHE;
		String subLinkProducts = "Products";
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

		s_assert.assertAll();
	}

	//product philosophy link should be working
	@Test(enabled=true)
	public void testProductsPhilosophyLinkShouldWorkingProper(){
		storeFrontLegacyHomePage =  new StoreFrontLegacyHomePage(driver);
		storeFrontLegacyHomePage.clickProductsBtn();
		//verify Product Philosophy link working?
		storeFrontLegacyHomePage.clickProductPhilosophyLink();
		s_assert.assertTrue(storeFrontLegacyHomePage.validateProductPhilosohyPageDisplayed(),"Product Philosophy page is not displayed!!");
		s_assert.assertAll();
	}

	//Digital Product Catalog- Links should be displayed the information properly
	@Test(enabled=true)
	public void testDigitalProductCatalogLinkShouldDisplayInformationProperly(){
		storeFrontLegacyHomePage =  new StoreFrontLegacyHomePage(driver);
		storeFrontLegacyHomePage.clickProductsBtn();
		//verify Digital Product Catalog- Link should be displayed the information properly?
		storeFrontLegacyHomePage.clickDigitalProductCatalogLink();
		s_assert.assertTrue(storeFrontLegacyHomePage.validateRealResultsLink(),"Real Results link didn't work");
		s_assert.assertTrue(storeFrontLegacyHomePage.validateSolutionToolLink(),"Solution Tool link didn't work");
		s_assert.assertTrue(storeFrontLegacyHomePage.validatePCPerksLink(),"PC Perks link didn't work");
		s_assert.assertTrue(storeFrontLegacyHomePage.validateDigitalProductCatalogLink(),"Digital Product link didn't work");
		s_assert.assertAll();
	}

	//Company Links Should be Present
	@Test(enabled=true)
	public void testCompanyLinksShouldBePresent(){
		storeFrontLegacyHomePage =  new StoreFrontLegacyHomePage(driver);
		storeFrontLegacyHomePage.clickAboutRFBtn();
		//verify company links is present?
		s_assert.assertTrue(storeFrontLegacyHomePage.validateExecutiveTeamLinkPresent(),"Executive Team Link is not present");
		s_assert.assertTrue(storeFrontLegacyHomePage.validateContactUsLinkPresent(),"Contact Us Link is not present");
		s_assert.assertTrue(storeFrontLegacyHomePage.validateWhoWeAreLinkPresent(),"WhoWeAre/Our History Link is not present");
		s_assert.assertTrue(storeFrontLegacyHomePage.validatePressRoomLinkPresent(),"Press Room Link is not present");
		s_assert.assertTrue(storeFrontLegacyHomePage.validateCareersLinkPresent(),"Careers Link is not present");
		s_assert.assertAll();
	}

	//Footer- Privacy Policy link should be redirecting to the appropriate page
	@Test(enabled=false)//WIP
	public void testFooterPrivacyPolicyLinkShouldRedirectionToAppropriatePage(){
		storeFrontLegacyHomePage =  new StoreFrontLegacyHomePage(driver);
		storeFrontLegacyHomePage.clickPrivacyPolicyLink();
		s_assert.assertTrue(storeFrontLegacyHomePage.isPrivacyPolicyPagePresent(), "Privacy policy page is not present after clicked on privacy policy link");
		s_assert.assertTrue(storeFrontLegacyHomePage.getCurrentURL().toLowerCase().contains("privacy"), "Expected url having privacy but actual on UI is "+storeFrontLegacyHomePage.getCurrentURL().toLowerCase());
		s_assert.assertAll();
	}

	//Footer-Terms & Conditions link should redirecting to the appropriate page
	@Test(enabled=false)//WIP
	public void testFooterTermsAndConditionLinkShouldRedirectionToAppropriatePage(){
		storeFrontLegacyHomePage =  new StoreFrontLegacyHomePage(driver);
		storeFrontLegacyHomePage.clickTermsAndConditionsLink();
		s_assert.assertTrue(storeFrontLegacyHomePage.getCurrentURL().toLowerCase().contains("terms"), "Expected url having terms but actual on UI is "+storeFrontLegacyHomePage.getCurrentURL().toLowerCase());
		s_assert.assertAll();
	}

	//Satisfaction Guarantee-link should be redirecting properly 
	@Test(enabled=false)//WIP
	public void testSatisfactionGuaranteeLinkShouldBeRedirectionProperly(){
		storeFrontLegacyHomePage =  new StoreFrontLegacyHomePage(driver);
		storeFrontLegacyHomePage.clickSatisfactionGuaranteeLink();
		s_assert.assertTrue(storeFrontLegacyHomePage.isSatisfactionGuaranteePagePresent(), "Satisfaction guarantee page is not present after clicked on privacy policy link");
		s_assert.assertTrue(storeFrontLegacyHomePage.getCurrentURL().toLowerCase().contains("guarantee"), "Expected url having guarantee but actual on UI is "+storeFrontLegacyHomePage.getCurrentURL().toLowerCase());
		s_assert.assertAll();
	}

	//Real results products- links should be redirecting to the appropriate page
	@Test(enabled=false)//WIP
	public void testProductsLinkShouldBeRedirectionToAppropriatePage(){
		storeFrontLegacyHomePage =  new StoreFrontLegacyHomePage(driver);
		//For REDEFINE
		storeFrontLegacyHomePage.clickProductsBtn();
		String regimen = TestConstantsRFL.REGIMEN_NAME_REDEFINE;
		storeFrontLegacyHomePage.selectRegimen(regimen);
		s_assert.assertTrue(storeFrontLegacyHomePage.getCurrentURL().toLowerCase().contains(regimen.toLowerCase()), "Expected regimen name is "+regimen.toLowerCase()+" Actual on UI is "+storeFrontLegacyHomePage.getCurrentURL().toLowerCase());
		s_assert.assertTrue(storeFrontLegacyHomePage.isRegimenNamePresentAfterClickedOnRegimen(regimen), "regimen name i.e.: "+regimen+" not present");
		//For REVERSE
		storeFrontLegacyHomePage.clickProductsBtn();
		regimen = TestConstantsRFL.REGIMEN_NAME_REVERSE;
		storeFrontLegacyHomePage.selectRegimen(regimen);
		s_assert.assertTrue(storeFrontLegacyHomePage.getCurrentURL().toLowerCase().contains(regimen.toLowerCase()), "Expected regimen name is "+regimen.toLowerCase()+" Actual on UI is "+storeFrontLegacyHomePage.getCurrentURL().toLowerCase());
		s_assert.assertTrue(storeFrontLegacyHomePage.isRegimenNamePresentAfterClickedOnRegimen(regimen), "regimen name i.e.: "+regimen+" not present");
		//For UNBLEMISH
		storeFrontLegacyHomePage.clickProductsBtn();
		regimen = TestConstantsRFL.REGIMEN_NAME_UNBLEMISH;
		storeFrontLegacyHomePage.selectRegimen(regimen);
		s_assert.assertTrue(storeFrontLegacyHomePage.getCurrentURL().toLowerCase().contains(regimen.toLowerCase()), "Expected regimen name is "+regimen.toLowerCase()+" Actual on UI is "+storeFrontLegacyHomePage.getCurrentURL().toLowerCase());
		s_assert.assertTrue(storeFrontLegacyHomePage.isRegimenNamePresentAfterClickedOnRegimen(regimen), "regimen name i.e.: "+regimen+" not present");
		//For SOOTHE
		storeFrontLegacyHomePage.clickProductsBtn();
		regimen = TestConstantsRFL.REGIMEN_NAME_SOOTHE;
		storeFrontLegacyHomePage.selectRegimen(regimen);
		s_assert.assertTrue(storeFrontLegacyHomePage.getCurrentURL().toLowerCase().contains(regimen.toLowerCase()), "Expected regimen name is "+regimen.toLowerCase()+" Actual on UI is "+storeFrontLegacyHomePage.getCurrentURL().toLowerCase());
		s_assert.assertTrue(storeFrontLegacyHomePage.isRegimenNamePresentAfterClickedOnRegimen(regimen), "regimen name i.e.: "+regimen+" not present");
		//For PROMOTIONS
		storeFrontLegacyHomePage.clickProductsBtn();
		regimen = TestConstantsRFL.REGIMEN_NAME_PROMOTIONS;
		storeFrontLegacyHomePage.selectPromotionRegimen();
		s_assert.assertTrue(storeFrontLegacyHomePage.getCurrentURL().toLowerCase().contains(regimen.toLowerCase()), "Expected regimen name is "+regimen.toLowerCase()+" Actual on UI is "+storeFrontLegacyHomePage.getCurrentURL().toLowerCase());
		s_assert.assertTrue(storeFrontLegacyHomePage.isRegimenNamePresentAfterClickedOnRegimen(regimen), "regimen name i.e.: "+regimen+" not present");
		//For ENHANCEMENTS
		storeFrontLegacyHomePage.clickProductsBtn();
		regimen = TestConstantsRFL.REGIMEN_NAME_ENHANCEMENTS;
		storeFrontLegacyHomePage.selectRegimen(regimen);
		s_assert.assertTrue(storeFrontLegacyHomePage.getCurrentURL().toLowerCase().contains(regimen.toLowerCase()), "Expected regimen name is "+regimen.toLowerCase()+" Actual on UI is "+storeFrontLegacyHomePage.getCurrentURL().toLowerCase());
		s_assert.assertTrue(storeFrontLegacyHomePage.isRegimenNamePresentAfterClickedOnRegimen(regimen), "regimen name i.e.: "+regimen+" not present");
		//For ESSENTIALS
		storeFrontLegacyHomePage.clickProductsBtn();
		regimen = TestConstantsRFL.REGIMEN_NAME_ESSENTIALS;
		storeFrontLegacyHomePage.selectRegimen(regimen);
		s_assert.assertTrue(storeFrontLegacyHomePage.getCurrentURL().toLowerCase().contains(regimen.toLowerCase()), "Expected regimen name is "+regimen.toLowerCase()+" Actual on UI is "+storeFrontLegacyHomePage.getCurrentURL().toLowerCase());
		s_assert.assertTrue(storeFrontLegacyHomePage.isRegimenNamePresentAfterClickedOnRegimen(regimen), "regimen name i.e.: "+regimen+" not present");
		s_assert.assertAll();
	}

	//Log in as an existen consultant
	@Test(enabled=false)//WIP
	public void testLoginAsExistingConsultant(){
		RFL_DB = driver.getDBNameRFL();
		List<Map<String, Object>> randomConsultantList =  null;
		String consultantEmailID = null;
		storeFrontLegacyHomePage =  new StoreFrontLegacyHomePage(driver);
		randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFL.GET_RANDOM_ACTIVE_CONSULTANT_EMAILID,RFL_DB);
		consultantEmailID = (String) getValueFromQueryResult(randomConsultantList, "EmailAddress");
		storeFrontLegacyHomePage.loginAsConsultant(consultantEmailID,password);
		s_assert.assertTrue(storeFrontLegacyHomePage.verifyUserSuccessfullyLoggedIn(),"consultant is not logged in successfully");
		s_assert.assertAll();
	}

	//Log in as valid PC customer
	@Test(enabled=false)//WIP
	public void testLoginAsExistingPC(){
		RFL_DB = driver.getDBNameRFL();
		List<Map<String, Object>> randomPCList =  null;
		String pcEmailID = null;
		storeFrontLegacyHomePage =  new StoreFrontLegacyHomePage(driver);
		randomPCList = DBUtil.performDatabaseQuery(DBQueries_RFL.GET_RANDOM_ACTIVE_PC_EMAILID,RFL_DB);
		pcEmailID = (String) getValueFromQueryResult(randomPCList, "EmailAddress");
		storeFrontLegacyHomePage.loginAsPCUser(pcEmailID,password);
		s_assert.assertTrue(storeFrontLegacyHomePage.verifyUserSuccessfullyLoggedIn(),"PC user is not logged in successfully");
		s_assert.assertAll();
	}

	//Log in with a valid RC customer
	@Test(enabled=false)//WIP
	public void testLoginAsExistingRC(){
		RFL_DB = driver.getDBNameRFL();
		List<Map<String, Object>> randomRCList =  null;
		String rcEmailID = null;
		storeFrontLegacyHomePage =  new StoreFrontLegacyHomePage(driver);
		randomRCList = DBUtil.performDatabaseQuery(DBQueries_RFL.GET_RANDOM_ACTIVE_RC_EMAILID,RFL_DB);
		rcEmailID = (String) getValueFromQueryResult(randomRCList, "EmailAddress");
		storeFrontLegacyHomePage.loginAsPCUser(rcEmailID,password);
		s_assert.assertTrue(storeFrontLegacyHomePage.verifyUserSuccessfullyLoggedIn(),"RC user is not logged in successfully");
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

	//Verify Redefine regimen Sections And Subsections should be displayed properly.
	@Test(enabled=false)//WIP
	public void testVerifyRedefineRegimenLinksDisplayedProperly(){
		String regimen = TestConstantsRFL.REGIMEN_NAME_REDEFINE;
		String subSectionRegimen = "Regimen";
		String subSectionProducts = "Products";
		String subSectionResults = "Results";
		String subSectionTestimonials = "Testimonials";
		String subSectionInTheNews = "In the News";
		String subSectionFAQ = "FAQs";
		String subSectionAdvice = "Advice";
		String subSectionGlossary = "Glossary";

		storeFrontLegacyHomePage =  new StoreFrontLegacyHomePage(driver);
		storeFrontLegacyHomePage.clickProductsBtn();
		storeFrontLegacyHomePage.selectRegimen(regimen);

		//Verify visibility of redefine regimen Sections.
		s_assert.assertTrue(storeFrontLegacyHomePage.verifyRedefineRegimenSections(subSectionRegimen),"Redefine regimen section regimen is not displayed");
		s_assert.assertTrue(storeFrontLegacyHomePage.verifyRedefineRegimenSections(subSectionProducts),"Redefine regimen section Products is not displayed");
		s_assert.assertTrue(storeFrontLegacyHomePage.verifyRedefineRegimenSections(subSectionResults),"Redefine regimen section Results is not displayed");
		s_assert.assertTrue(storeFrontLegacyHomePage.verifyRedefineRegimenSections(subSectionTestimonials),"Redefine regimen section testimonials is not displayed");
		s_assert.assertTrue(storeFrontLegacyHomePage.verifyRedefineRegimenSections(subSectionInTheNews),"Redefine regimen section In the news is not displayed");
		s_assert.assertTrue(storeFrontLegacyHomePage.verifyRedefineRegimenSections(subSectionFAQ),"Redefine regimen section FAQ is not displayed");
		s_assert.assertTrue(storeFrontLegacyHomePage.verifyRedefineRegimenSections(subSectionAdvice),"Redefine regimen section Advice is not displayed");
		s_assert.assertTrue(storeFrontLegacyHomePage.verifyRedefineRegimenSections(subSectionGlossary),"Redefine regimen section Glossary is not displayed");

		//Verify Visibility of Redefine Regimen regimen subSections.
		s_assert.assertTrue(storeFrontLegacyHomePage.getCurrentURL().toLowerCase().contains(regimen.toLowerCase()), "Expected regimen name is "+regimen.toLowerCase()+" Actual on UI is "+storeFrontLegacyHomePage.getCurrentURL().toLowerCase());
		s_assert.assertTrue(storeFrontLegacyHomePage.verifyAddToCartButton(),"Add to cart button not present under regimen sublink.");
		storeFrontLegacyHomePage.clickIngredientsAndUsageLink();
		s_assert.assertTrue(storeFrontLegacyHomePage.verifyIngredientsAndUsageInfoVisible(),"Ingredients and usage link is not present under regimen sublink.");
		storeFrontLegacyHomePage.clickAddToCartBtn();
		s_assert.assertTrue(storeFrontLegacyHomePage.verifyCheckoutBtnOnMyShoppingCart(),"Checkout button is not present on my shopping cart page");
		storeFrontLegacyHomePage.navigateToBackPage();

		//Verify Visibility of Redefine Regimen Products subSections.	
		storeFrontLegacyHomePage.clickSubSectionUnderRegimen(subSectionProducts);
		s_assert.assertTrue(storeFrontLegacyHomePage.getCurrentURL().toLowerCase().contains(subSectionProducts.toLowerCase()), "Expected regimen name is "+subSectionProducts.toLowerCase()+" Actual on UI is "+storeFrontLegacyHomePage.getCurrentURL().toLowerCase());		
		s_assert.assertTrue(storeFrontLegacyHomePage.verifyUserIsRedirectedToProductsPage(),"user is not on product page");
		storeFrontLegacyHomePage.clickAddToCartButtonForEssentialsAndEnhancementsAfterLogin();
		s_assert.assertTrue(storeFrontLegacyHomePage.verifyCheckoutBtnOnMyShoppingCart(),"Checkout button is not present on my shopping cart page");
		storeFrontLegacyHomePage.navigateToBackPage();
		storeFrontLegacyHomePage.navigateToBackPage();

		//Verify Visibility of Redefine Regimen results subSections.	
		storeFrontLegacyHomePage.clickSubSectionUnderRegimen(subSectionResults);
		s_assert.assertTrue(storeFrontLegacyHomePage.getCurrentURL().toLowerCase().contains(subSectionResults.toLowerCase()), "Expected regimen name is "+subSectionResults.toLowerCase()+" Actual on UI is "+storeFrontLegacyHomePage.getCurrentURL().toLowerCase());
		s_assert.assertTrue(storeFrontLegacyHomePage.verifyUserIsRedirectedToResultsPage(),"user is not on results page");
		storeFrontLegacyHomePage.navigateToBackPage();

		//Verify Visibility of Redefine Regimen testimonials subSections.
		storeFrontLegacyHomePage.clickSubSectionUnderRegimen(subSectionTestimonials);
		s_assert.assertTrue(storeFrontLegacyHomePage.getCurrentURL().toLowerCase().contains(subSectionTestimonials.toLowerCase()), "Expected regimen name is "+subSectionTestimonials.toLowerCase()+" Actual on UI is "+storeFrontLegacyHomePage.getCurrentURL().toLowerCase());
		s_assert.assertTrue(storeFrontLegacyHomePage.verifyUserIsRedirectedToTestimonialsPage(),"user is not on testimonials page");
		storeFrontLegacyHomePage.navigateToBackPage();

		//Verify Visibility of Redefine Regimen In the news subSections.
		storeFrontLegacyHomePage.clickSubSectionUnderRegimen(subSectionInTheNews);
		s_assert.assertTrue(storeFrontLegacyHomePage.getCurrentURL().toLowerCase().contains("News".toLowerCase()), "Expected regimen name is "+"News".toLowerCase()+" Actual on UI is "+storeFrontLegacyHomePage.getCurrentURL().toLowerCase());
		s_assert.assertTrue(storeFrontLegacyHomePage.verifyUserIsRedirectedToNewsPage(),"user is not on News page");
		storeFrontLegacyHomePage.navigateToBackPage();

		//Verify Visibility of Redefine Regimen Faq's subSections.
		storeFrontLegacyHomePage.clickSubSectionUnderRegimen(subSectionFAQ);
		s_assert.assertTrue(storeFrontLegacyHomePage.getCurrentURL().toLowerCase().contains("CommonQuestions".toLowerCase()), "Expected regimen name is "+"CommonQuestions".toLowerCase()+" Actual on UI is "+storeFrontLegacyHomePage.getCurrentURL().toLowerCase());
		s_assert.assertTrue(storeFrontLegacyHomePage.verifyUserIsRedirectedToFAQsPage(),"user is not on FAQs page");
		storeFrontLegacyHomePage.navigateToBackPage();

		//Verify Visibility of Redefine Regimen Advice subSections.
		storeFrontLegacyHomePage.clickSubSectionUnderRegimen(subSectionAdvice);
		s_assert.assertTrue(storeFrontLegacyHomePage.getCurrentURL().toLowerCase().contains(subSectionAdvice.toLowerCase()), "Expected regimen name is "+subSectionAdvice.toLowerCase()+" Actual on UI is "+storeFrontLegacyHomePage.getCurrentURL().toLowerCase());
		s_assert.assertTrue(storeFrontLegacyHomePage.verifyUserIsRedirectedToAdvicePage(),"user is not on advice page");
		storeFrontLegacyHomePage.navigateToBackPage();

		//Verify Visibility of Redefine Regimen Glossary subSections.
		storeFrontLegacyHomePage.clickSubSectionUnderRegimen(subSectionGlossary);
		s_assert.assertTrue(storeFrontLegacyHomePage.getCurrentURL().toLowerCase().contains(subSectionGlossary.toLowerCase()), "Expected regimen name is "+subSectionGlossary.toLowerCase()+" Actual on UI is "+storeFrontLegacyHomePage.getCurrentURL().toLowerCase());
		s_assert.assertTrue(storeFrontLegacyHomePage.verifyUserIsRedirectedToGlossaryPage(),"user is not on glossary page");
		storeFrontLegacyHomePage.navigateToBackPage();
		s_assert.assertAll();

	}

}
