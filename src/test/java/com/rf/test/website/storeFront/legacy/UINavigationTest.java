package com.rf.test.website.storeFront.legacy;

import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.annotations.Test;

import com.rf.core.utils.CommonUtils;
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
	@Test
	public void testProductsRedefineRegimenLinksRedirectingToTheAppropriatePage(){
		String subLinkRegimen = TestConstantsRFL.REGIMEN_NAME_REDEFINE;
		String subLinkProducts = "Products";
		String subLinkResults = "Results";
		//String subLinkTestimonials = "Testimonials";
		//String subLinkInTheNews = "In the News";
		String subLinkFAQ = "FAQs";
		//String subLinkAdvice = "Advice";
		//String subLinkGlossary = "Glossary";

		storeFrontLegacyHomePage =  new StoreFrontLegacyHomePage(driver);
		storeFrontLegacyHomePage.clickShopSkinCareBtn();
		storeFrontLegacyHomePage.selectRegimen(subLinkRegimen);
		s_assert.assertTrue(storeFrontLegacyHomePage.getCurrentURL().toLowerCase().contains(subLinkRegimen.toLowerCase()), "Expected regimen name is "+subLinkRegimen.toLowerCase()+" Actual on UI is "+storeFrontLegacyHomePage.getCurrentURL().toLowerCase());

		storeFrontLegacyHomePage.clickSubSectionUnderRegimen(subLinkProducts);
		s_assert.assertTrue(storeFrontLegacyHomePage.getCurrentURL().toLowerCase().contains(subLinkProducts.toLowerCase()), "Expected sublink in url is "+subLinkProducts.toLowerCase()+" Actual on UI is "+storeFrontLegacyHomePage.getCurrentURL().toLowerCase());
		s_assert.assertTrue(storeFrontLegacyHomePage.verifyUserIsRedirectedToProductsPage(subLinkRegimen),"user is not on products page");
		storeFrontLegacyHomePage.navigateToBackPage();

		storeFrontLegacyHomePage.clickSubSectionUnderRegimen(subLinkResults);
		s_assert.assertTrue(storeFrontLegacyHomePage.getCurrentURL().toLowerCase().contains(subLinkResults.toLowerCase()), "Expected sublink in url is "+subLinkResults.toLowerCase()+" Actual on UI is "+storeFrontLegacyHomePage.getCurrentURL().toLowerCase());
		s_assert.assertTrue(storeFrontLegacyHomePage.verifyUserIsRedirectedToResultsPage(subLinkRegimen),"user is not on results page");
		storeFrontLegacyHomePage.navigateToBackPage();

		//		storeFrontLegacyHomePage.clickSubSectionUnderRegimen(subLinkTestimonials);
		//		s_assert.assertTrue(storeFrontLegacyHomePage.getCurrentURL().toLowerCase().contains(subLinkTestimonials.toLowerCase()), "Expected sublink in url is "+subLinkTestimonials.toLowerCase()+" Actual on UI is "+storeFrontLegacyHomePage.getCurrentURL().toLowerCase());
		//		s_assert.assertTrue(storeFrontLegacyHomePage.verifyUserIsRedirectedToTestimonialsPage(),"user is not on testimonials page");
		//		storeFrontLegacyHomePage.navigateToBackPage();
		//
		//		storeFrontLegacyHomePage.clickSubSectionUnderRegimen(subLinkInTheNews);
		//		s_assert.assertTrue(storeFrontLegacyHomePage.getCurrentURL().toLowerCase().contains("News".toLowerCase()), "Expected sublink in url is "+"News".toLowerCase()+" Actual on UI is "+storeFrontLegacyHomePage.getCurrentURL().toLowerCase());
		//		s_assert.assertTrue(storeFrontLegacyHomePage.verifyUserIsRedirectedToNewsPage(),"user is not on News page");
		//		storeFrontLegacyHomePage.navigateToBackPage();

		storeFrontLegacyHomePage.clickSubSectionUnderRegimen(subLinkFAQ);
		s_assert.assertTrue(storeFrontLegacyHomePage.getCurrentURL().toLowerCase().contains("CommonQuestions".toLowerCase()), "Expected sublink in url is "+"CommonQuestions".toLowerCase()+" Actual on UI is "+storeFrontLegacyHomePage.getCurrentURL().toLowerCase());
		s_assert.assertTrue(storeFrontLegacyHomePage.verifyUserIsRedirectedToFAQsPage(subLinkRegimen),"user is not on FAQs page");
		storeFrontLegacyHomePage.navigateToBackPage();

		//		storeFrontLegacyHomePage.clickSubSectionUnderRegimen(subLinkAdvice);
		//		s_assert.assertTrue(storeFrontLegacyHomePage.getCurrentURL().toLowerCase().contains(subLinkAdvice.toLowerCase()), "Expected sublink in url is "+subLinkAdvice.toLowerCase()+" Actual on UI is "+storeFrontLegacyHomePage.getCurrentURL().toLowerCase());
		//		s_assert.assertTrue(storeFrontLegacyHomePage.verifyUserIsRedirectedToAdvicePage(),"user is not on advice page");
		//		storeFrontLegacyHomePage.navigateToBackPage();
		//
		//		storeFrontLegacyHomePage.clickSubSectionUnderRegimen(subLinkGlossary);
		//		s_assert.assertTrue(storeFrontLegacyHomePage.getCurrentURL().toLowerCase().contains(subLinkGlossary.toLowerCase()), "Expected sublink in url is "+subLinkGlossary.toLowerCase()+" Actual on UI is "+storeFrontLegacyHomePage.getCurrentURL().toLowerCase());
		//		s_assert.assertTrue(storeFrontLegacyHomePage.verifyUserIsRedirectedToGlossaryPage(),"user is not on glossary page");
		//		storeFrontLegacyHomePage.navigateToBackPage();
		s_assert.assertAll();
	}

	//Reverse Products-links should be working properly (products, testimonials, in the news, FAQ's, advice, glossary)
	@Test
	public void testReverseProductsLinksWorkingProperly(){
		String subLinkRegimen = TestConstantsRFL.REGIMEN_NAME_REVERSE;
		String subLinkProducts = "Products";
		//String subLinkTestimonials = "Testimonials";
		//String subLinkInTheNews = "In the News";
		String subLinkFAQ = "FAQs";
		//String subLinkAdvice = "Advice";
		//String subLinkGlossary = "Glossary";
		String subLinkResults = "Results";

		storeFrontLegacyHomePage =  new StoreFrontLegacyHomePage(driver);
		storeFrontLegacyHomePage.clickShopSkinCareBtn();
		storeFrontLegacyHomePage.selectRegimen(subLinkRegimen);
		s_assert.assertTrue(storeFrontLegacyHomePage.getCurrentURL().toLowerCase().contains(subLinkRegimen.toLowerCase()), "Expected regimen name is "+subLinkRegimen.toLowerCase()+" Actual on UI is "+storeFrontLegacyHomePage.getCurrentURL().toLowerCase());

		storeFrontLegacyHomePage.clickSubSectionUnderRegimen(subLinkProducts);
		s_assert.assertTrue(storeFrontLegacyHomePage.getCurrentURL().toLowerCase().contains(subLinkProducts.toLowerCase()), "Expected sublink in url is "+subLinkProducts.toLowerCase()+" Actual on UI is "+storeFrontLegacyHomePage.getCurrentURL().toLowerCase());
		s_assert.assertTrue(storeFrontLegacyHomePage.verifyUserIsRedirectedToProductsPage(subLinkRegimen),"user is not on products page");
		storeFrontLegacyHomePage.navigateToBackPage();

		//		storeFrontLegacyHomePage.clickSubSectionUnderRegimen(subLinkTestimonials);
		//		s_assert.assertTrue(storeFrontLegacyHomePage.getCurrentURL().toLowerCase().contains(subLinkTestimonials.toLowerCase()), "Expected sublink in url is "+subLinkTestimonials.toLowerCase()+" Actual on UI is "+storeFrontLegacyHomePage.getCurrentURL().toLowerCase());
		//		s_assert.assertTrue(storeFrontLegacyHomePage.verifyUserIsRedirectedToTestimonialsPage(),"user is not on testimonials page");
		//		storeFrontLegacyHomePage.navigateToBackPage();
		//
		//		storeFrontLegacyHomePage.clickSubSectionUnderRegimen(subLinkInTheNews);
		//		s_assert.assertTrue(storeFrontLegacyHomePage.getCurrentURL().toLowerCase().contains("News".toLowerCase()), "Expected sublink in url is "+"News".toLowerCase()+" Actual on UI is "+storeFrontLegacyHomePage.getCurrentURL().toLowerCase());
		//		s_assert.assertTrue(storeFrontLegacyHomePage.verifyUserIsRedirectedToNewsPage(),"user is not on News page");
		//		storeFrontLegacyHomePage.navigateToBackPage();

		storeFrontLegacyHomePage.clickSubSectionUnderRegimen(subLinkFAQ);
		s_assert.assertTrue(storeFrontLegacyHomePage.getCurrentURL().toLowerCase().contains("CommonQuestions".toLowerCase()), "Expected sublink in url is "+"CommonQuestions".toLowerCase()+" Actual on UI is "+storeFrontLegacyHomePage.getCurrentURL().toLowerCase());
		s_assert.assertTrue(storeFrontLegacyHomePage.verifyUserIsRedirectedToFAQsPage(subLinkRegimen),"user is not on FAQs page");
		storeFrontLegacyHomePage.navigateToBackPage();

		storeFrontLegacyHomePage.clickSubSectionUnderRegimen(subLinkResults);
		s_assert.assertTrue(storeFrontLegacyHomePage.getCurrentURL().toLowerCase().contains(subLinkResults.toLowerCase()), "Expected sublink in url is "+subLinkResults.toLowerCase()+" Actual on UI is "+storeFrontLegacyHomePage.getCurrentURL().toLowerCase());
		s_assert.assertTrue(storeFrontLegacyHomePage.verifyUserIsRedirectedToResultsPage(subLinkRegimen),"user is not on results page");
		storeFrontLegacyHomePage.navigateToBackPage();

		//		storeFrontLegacyHomePage.clickSubSectionUnderRegimen(subLinkAdvice);
		//		s_assert.assertTrue(storeFrontLegacyHomePage.getCurrentURL().toLowerCase().contains(subLinkAdvice.toLowerCase()), "Expected sublink in url is "+subLinkAdvice.toLowerCase()+" Actual on UI is "+storeFrontLegacyHomePage.getCurrentURL().toLowerCase());
		//		s_assert.assertTrue(storeFrontLegacyHomePage.verifyUserIsRedirectedToAdvicePage(),"user is not on advice page");
		//		storeFrontLegacyHomePage.navigateToBackPage();
		//
		//		storeFrontLegacyHomePage.clickSubSectionUnderRegimen(subLinkGlossary);
		//		s_assert.assertTrue(storeFrontLegacyHomePage.getCurrentURL().toLowerCase().contains(subLinkGlossary.toLowerCase()), "Expected sublink in url is "+subLinkGlossary.toLowerCase()+" Actual on UI is "+storeFrontLegacyHomePage.getCurrentURL().toLowerCase());
		//		s_assert.assertTrue(storeFrontLegacyHomePage.verifyUserIsRedirectedToGlossaryPage(),"user is not on glossary page");
		//		storeFrontLegacyHomePage.navigateToBackPage();

		s_assert.assertAll();
	}

	//Unblemish Products-links should be working properly (products, testimonials, in the news, FAQ's, advice, glossary)
	@Test
	public void testUnblemishProductsLinksWorkingProperly(){
		String subLinkRegimen = TestConstantsRFL.REGIMEN_NAME_UNBLEMISH;
		String subLinkProducts = "Products";
		//String subLinkTestimonials = "Testimonials";
		//String subLinkInTheNews = "In the News";
		String subLinkFAQ = "FAQs";
		String subLinkResults = "Results";
		//String subLinkAdvice = "Advice";
		//String subLinkGlossary = "Glossary";

		storeFrontLegacyHomePage =  new StoreFrontLegacyHomePage(driver);
		storeFrontLegacyHomePage.clickShopSkinCareBtn();
		storeFrontLegacyHomePage.selectRegimen(subLinkRegimen);
		s_assert.assertTrue(storeFrontLegacyHomePage.getCurrentURL().toLowerCase().contains(subLinkRegimen.toLowerCase()), "Expected regimen name is "+subLinkRegimen.toLowerCase()+" Actual on UI is "+storeFrontLegacyHomePage.getCurrentURL().toLowerCase());

		storeFrontLegacyHomePage.clickSubSectionUnderRegimen(subLinkProducts);
		s_assert.assertTrue(storeFrontLegacyHomePage.getCurrentURL().toLowerCase().contains(subLinkProducts.toLowerCase()), "Expected sublink in url is "+subLinkProducts.toLowerCase()+" Actual on UI is "+storeFrontLegacyHomePage.getCurrentURL().toLowerCase());
		s_assert.assertTrue(storeFrontLegacyHomePage.verifyUserIsRedirectedToProductsPage(subLinkRegimen),"user is not on products page");
		storeFrontLegacyHomePage.navigateToBackPage();

		//		storeFrontLegacyHomePage.clickSubSectionUnderRegimen(subLinkTestimonials);
		//		s_assert.assertTrue(storeFrontLegacyHomePage.getCurrentURL().toLowerCase().contains(subLinkTestimonials.toLowerCase()), "Expected sublink in url is "+subLinkTestimonials.toLowerCase()+" Actual on UI is "+storeFrontLegacyHomePage.getCurrentURL().toLowerCase());
		//		s_assert.assertTrue(storeFrontLegacyHomePage.verifyUserIsRedirectedToTestimonialsPage(),"user is not on testimonials page");
		//		storeFrontLegacyHomePage.navigateToBackPage();
		//
		//		storeFrontLegacyHomePage.clickSubSectionUnderRegimen(subLinkInTheNews);
		//		s_assert.assertTrue(storeFrontLegacyHomePage.getCurrentURL().toLowerCase().contains("News".toLowerCase()), "Expected sublink in url is "+"News".toLowerCase()+" Actual on UI is "+storeFrontLegacyHomePage.getCurrentURL().toLowerCase());
		//		s_assert.assertTrue(storeFrontLegacyHomePage.verifyUserIsRedirectedToNewsPage(),"user is not on News page");
		//		storeFrontLegacyHomePage.navigateToBackPage();

		storeFrontLegacyHomePage.clickSubSectionUnderRegimen(subLinkFAQ);
		s_assert.assertTrue(storeFrontLegacyHomePage.getCurrentURL().toLowerCase().contains("CommonQuestions".toLowerCase()), "Expected sublink in url is "+"CommonQuestions".toLowerCase()+" Actual on UI is "+storeFrontLegacyHomePage.getCurrentURL().toLowerCase());
		s_assert.assertTrue(storeFrontLegacyHomePage.verifyUserIsRedirectedToFAQsPage(subLinkRegimen),"user is not on FAQs page");
		storeFrontLegacyHomePage.navigateToBackPage();

		storeFrontLegacyHomePage.clickSubSectionUnderRegimen(subLinkResults);
		s_assert.assertTrue(storeFrontLegacyHomePage.getCurrentURL().toLowerCase().contains(subLinkResults.toLowerCase()), "Expected sublink in url is "+subLinkResults.toLowerCase()+" Actual on UI is "+storeFrontLegacyHomePage.getCurrentURL().toLowerCase());
		s_assert.assertTrue(storeFrontLegacyHomePage.verifyUserIsRedirectedToResultsPage(subLinkRegimen),"user is not on results page");
		storeFrontLegacyHomePage.navigateToBackPage();

		//		storeFrontLegacyHomePage.clickSubSectionUnderRegimen(subLinkAdvice);
		//		s_assert.assertTrue(storeFrontLegacyHomePage.getCurrentURL().toLowerCase().contains(subLinkAdvice.toLowerCase()), "Expected sublink in url is "+subLinkAdvice.toLowerCase()+" Actual on UI is "+storeFrontLegacyHomePage.getCurrentURL().toLowerCase());
		//		s_assert.assertTrue(storeFrontLegacyHomePage.verifyUserIsRedirectedToAdvicePage(),"user is not on advice page");
		//		storeFrontLegacyHomePage.navigateToBackPage();
		//
		//		storeFrontLegacyHomePage.clickSubSectionUnderRegimen(subLinkGlossary);
		//		s_assert.assertTrue(storeFrontLegacyHomePage.getCurrentURL().toLowerCase().contains(subLinkGlossary.toLowerCase()), "Expected sublink in url is "+subLinkGlossary.toLowerCase()+" Actual on UI is "+storeFrontLegacyHomePage.getCurrentURL().toLowerCase());
		//		s_assert.assertTrue(storeFrontLegacyHomePage.verifyUserIsRedirectedToGlossaryPage(),"user is not on glossary page");
		//		storeFrontLegacyHomePage.navigateToBackPage();

		s_assert.assertAll();
	}

	//Soothe Products-links should be working properly (products, testimonials, in the news, FAQ's, advice, glossary)
	@Test
	public void testSootheProductsLinksWorkingProperly(){
		String subLinkRegimen = TestConstantsRFL.REGIMEN_NAME_SOOTHE;
		String subLinkProducts = "Products";
		//String subLinkTestimonials = "Testimonials";
		//String subLinkInTheNews = "In the News";
		String subLinkFAQ = "FAQs";
		//String subLinkAdvice = "Advice";
		//String subLinkGlossary = "Glossary";
		String subLinkResults = "Results";

		storeFrontLegacyHomePage =  new StoreFrontLegacyHomePage(driver);
		storeFrontLegacyHomePage.clickShopSkinCareBtn();
		storeFrontLegacyHomePage.selectRegimen(subLinkRegimen);
		s_assert.assertTrue(storeFrontLegacyHomePage.getCurrentURL().toLowerCase().contains(subLinkRegimen.toLowerCase()), "Expected regimen name is "+subLinkRegimen.toLowerCase()+" Actual on UI is "+storeFrontLegacyHomePage.getCurrentURL().toLowerCase());

		storeFrontLegacyHomePage.clickSubSectionUnderRegimen(subLinkProducts);
		s_assert.assertTrue(storeFrontLegacyHomePage.getCurrentURL().toLowerCase().contains(subLinkProducts.toLowerCase()), "Expected sublink in url is "+subLinkProducts.toLowerCase()+" Actual on UI is "+storeFrontLegacyHomePage.getCurrentURL().toLowerCase());
		s_assert.assertTrue(storeFrontLegacyHomePage.verifyUserIsRedirectedToProductsPage(subLinkRegimen),"user is not on products page");
		storeFrontLegacyHomePage.navigateToBackPage();
		//
		//		storeFrontLegacyHomePage.clickSubSectionUnderRegimen(subLinkTestimonials);
		//		s_assert.assertTrue(storeFrontLegacyHomePage.getCurrentURL().toLowerCase().contains(subLinkTestimonials.toLowerCase()), "Expected sublink in url is "+subLinkTestimonials.toLowerCase()+" Actual on UI is "+storeFrontLegacyHomePage.getCurrentURL().toLowerCase());
		//		s_assert.assertTrue(storeFrontLegacyHomePage.verifyUserIsRedirectedToTestimonialsPage(),"user is not on testimonials page");
		//		storeFrontLegacyHomePage.navigateToBackPage();
		//
		//		storeFrontLegacyHomePage.clickSubSectionUnderRegimen(subLinkInTheNews);
		//		s_assert.assertTrue(storeFrontLegacyHomePage.getCurrentURL().toLowerCase().contains("News".toLowerCase()), "Expected sublink in url is "+"News".toLowerCase()+" Actual on UI is "+storeFrontLegacyHomePage.getCurrentURL().toLowerCase());
		//		s_assert.assertTrue(storeFrontLegacyHomePage.verifyUserIsRedirectedToNewsPage(),"user is not on News page");
		//		storeFrontLegacyHomePage.navigateToBackPage();

		storeFrontLegacyHomePage.clickSubSectionUnderRegimen(subLinkFAQ);
		s_assert.assertTrue(storeFrontLegacyHomePage.getCurrentURL().toLowerCase().contains("CommonQuestions".toLowerCase()), "Expected sublink in url is "+"CommonQuestions".toLowerCase()+" Actual on UI is "+storeFrontLegacyHomePage.getCurrentURL().toLowerCase());
		s_assert.assertTrue(storeFrontLegacyHomePage.verifyUserIsRedirectedToFAQsPage(subLinkRegimen),"user is not on FAQs page");
		storeFrontLegacyHomePage.navigateToBackPage();

		storeFrontLegacyHomePage.clickSubSectionUnderRegimen(subLinkResults);
		s_assert.assertTrue(storeFrontLegacyHomePage.getCurrentURL().toLowerCase().contains(subLinkResults.toLowerCase()), "Expected sublink in url is "+subLinkResults.toLowerCase()+" Actual on UI is "+storeFrontLegacyHomePage.getCurrentURL().toLowerCase());
		s_assert.assertTrue(storeFrontLegacyHomePage.verifyUserIsRedirectedToResultsPage(subLinkRegimen),"user is not on results page");
		storeFrontLegacyHomePage.navigateToBackPage();

		//		storeFrontLegacyHomePage.clickSubSectionUnderRegimen(subLinkAdvice);
		//		s_assert.assertTrue(storeFrontLegacyHomePage.getCurrentURL().toLowerCase().contains(subLinkAdvice.toLowerCase()), "Expected sublink in url is "+subLinkAdvice.toLowerCase()+" Actual on UI is "+storeFrontLegacyHomePage.getCurrentURL().toLowerCase());
		//		s_assert.assertTrue(storeFrontLegacyHomePage.verifyUserIsRedirectedToAdvicePage(),"user is not on advice page");
		//		storeFrontLegacyHomePage.navigateToBackPage();
		//
		//		storeFrontLegacyHomePage.clickSubSectionUnderRegimen(subLinkGlossary);
		//		s_assert.assertTrue(storeFrontLegacyHomePage.getCurrentURL().toLowerCase().contains(subLinkGlossary.toLowerCase()), "Expected sublink in url is "+subLinkGlossary.toLowerCase()+" Actual on UI is "+storeFrontLegacyHomePage.getCurrentURL().toLowerCase());
		//		s_assert.assertTrue(storeFrontLegacyHomePage.verifyUserIsRedirectedToGlossaryPage(),"user is not on glossary page");
		//		storeFrontLegacyHomePage.navigateToBackPage();

		s_assert.assertAll();
	}

	//product philosophy link should be working
	@Test(enabled=false)//needs updation
	public void testProductsPhilosophyLinkShouldWorkingProper(){
		storeFrontLegacyHomePage =  new StoreFrontLegacyHomePage(driver);
		storeFrontLegacyHomePage.clickShopSkinCareBtn();
		//verify Product Philosophy link working?
		storeFrontLegacyHomePage.clickProductPhilosophyLink();
		s_assert.assertTrue(storeFrontLegacyHomePage.validateProductPhilosohyPageDisplayed(),"Product Philosophy page is not displayed!!");
		s_assert.assertAll();
	}

	//Digital Product Catalog- Links should be displayed the information properly
	@Test(enabled=false)//needs updation
	public void testDigitalProductCatalogLinkShouldDisplayInformationProperly(){
		storeFrontLegacyHomePage =  new StoreFrontLegacyHomePage(driver);
		storeFrontLegacyHomePage.clickShopSkinCareBtn();
		//verify Digital Product Catalog- Link should be displayed the information properly?
		storeFrontLegacyHomePage.clickDigitalProductCatalogLink();
		s_assert.assertTrue(storeFrontLegacyHomePage.validateRealResultsLink(),"Real Results link didn't work");
		s_assert.assertTrue(storeFrontLegacyHomePage.validateSolutionToolLink(),"Solution Tool link didn't work");
		s_assert.assertTrue(storeFrontLegacyHomePage.validatePCPerksLink(),"PC Perks link didn't work");
		s_assert.assertTrue(storeFrontLegacyHomePage.validateDigitalProductCatalogLink(),"Digital Product link didn't work");
		s_assert.assertAll();
	}

	//Company Links Should be Present
	@Test(enabled=false)//needs updation
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
	@Test
	public void testFooterPrivacyPolicyLinkShouldRedirectionToAppropriatePage(){
		storeFrontLegacyHomePage =  new StoreFrontLegacyHomePage(driver);
		storeFrontLegacyHomePage.clickPrivacyPolicyLink();
		/*s_assert.assertTrue(storeFrontLegacyHomePage.isPrivacyPolicyPagePresent(), "Privacy policy page is not present after clicked on privacy policy link");*/
		s_assert.assertTrue(storeFrontLegacyHomePage.getCurrentURL().toLowerCase().contains("privacy"), "Expected url having privacy but actual on UI is "+storeFrontLegacyHomePage.getCurrentURL().toLowerCase());
		s_assert.assertAll();
	}

	//Footer-Terms & Conditions link should redirecting to the appropriate page
	@Test
	public void testFooterTermsAndConditionLinkShouldRedirectionToAppropriatePage(){
		storeFrontLegacyHomePage =  new StoreFrontLegacyHomePage(driver);
		storeFrontLegacyHomePage.clickTermsAndConditionsLink();
		s_assert.assertTrue(storeFrontLegacyHomePage.getCurrentURL().toLowerCase().contains("terms"), "Expected url having terms but actual on UI is "+storeFrontLegacyHomePage.getCurrentURL().toLowerCase());
		s_assert.assertAll();
	}

	//Satisfaction Guarantee-link should be redirecting properly 
	@Test
	public void testSatisfactionGuaranteeLinkShouldBeRedirectionProperly(){
		storeFrontLegacyHomePage =  new StoreFrontLegacyHomePage(driver);
		storeFrontLegacyHomePage.clickSatisfactionGuaranteeLink();
		s_assert.assertTrue(storeFrontLegacyHomePage.isSatisfactionGuaranteePagePresent(), "Satisfaction guarantee page is not present after clicked on privacy policy link");
		s_assert.assertTrue(storeFrontLegacyHomePage.getCurrentURL().toLowerCase().contains("guarantee"), "Expected url having guarantee but actual on UI is "+storeFrontLegacyHomePage.getCurrentURL().toLowerCase());
		s_assert.assertAll();
	}

	//Real results products- links should be redirecting to the appropriate page
	@Test
	public void testProductsLinkShouldBeRedirectionToAppropriatePage(){
		storeFrontLegacyHomePage =  new StoreFrontLegacyHomePage(driver);
		//For REDEFINE
		storeFrontLegacyHomePage.clickShopSkinCareBtn();
		String regimen = TestConstantsRFL.REGIMEN_NAME_REDEFINE;
		storeFrontLegacyHomePage.selectRegimen(regimen);
		s_assert.assertTrue(storeFrontLegacyHomePage.getCurrentURL().toLowerCase().contains(regimen.toLowerCase()), "Expected regimen name is "+regimen.toLowerCase()+" Actual on UI is "+storeFrontLegacyHomePage.getCurrentURL().toLowerCase());
		s_assert.assertTrue(storeFrontLegacyHomePage.isRegimenNamePresentAfterClickedOnRegimen(regimen), "regimen name i.e.: "+regimen+" not present");
		//For REVERSE
		storeFrontLegacyHomePage.clickShopSkinCareBtn();
		regimen = TestConstantsRFL.REGIMEN_NAME_REVERSE;
		storeFrontLegacyHomePage.selectRegimen(regimen);
		s_assert.assertTrue(storeFrontLegacyHomePage.getCurrentURL().toLowerCase().contains(regimen.toLowerCase()), "Expected regimen name is "+regimen.toLowerCase()+" Actual on UI is "+storeFrontLegacyHomePage.getCurrentURL().toLowerCase());
		s_assert.assertTrue(storeFrontLegacyHomePage.isRegimenNamePresentAfterClickedOnRegimen(regimen), "regimen name i.e.: "+regimen+" not present");
		//For UNBLEMISH
		storeFrontLegacyHomePage.clickShopSkinCareBtn();
		regimen = TestConstantsRFL.REGIMEN_NAME_UNBLEMISH;
		storeFrontLegacyHomePage.selectRegimen(regimen);
		s_assert.assertTrue(storeFrontLegacyHomePage.getCurrentURL().toLowerCase().contains(regimen.toLowerCase()), "Expected regimen name is "+regimen.toLowerCase()+" Actual on UI is "+storeFrontLegacyHomePage.getCurrentURL().toLowerCase());
		s_assert.assertTrue(storeFrontLegacyHomePage.isRegimenNamePresentAfterClickedOnRegimen(regimen), "regimen name i.e.: "+regimen+" not present");
		//For SOOTHE
		storeFrontLegacyHomePage.clickShopSkinCareBtn();
		regimen = TestConstantsRFL.REGIMEN_NAME_SOOTHE;
		storeFrontLegacyHomePage.selectRegimen(regimen);
		s_assert.assertTrue(storeFrontLegacyHomePage.getCurrentURL().toLowerCase().contains(regimen.toLowerCase()), "Expected regimen name is "+regimen.toLowerCase()+" Actual on UI is "+storeFrontLegacyHomePage.getCurrentURL().toLowerCase());
		s_assert.assertTrue(storeFrontLegacyHomePage.isRegimenNamePresentAfterClickedOnRegimen(regimen), "regimen name i.e.: "+regimen+" not present");
		//For PROMOTIONS
		storeFrontLegacyHomePage.clickShopSkinCareBtn();
		regimen = TestConstantsRFL.REGIMEN_NAME_PROMOTIONS;
		storeFrontLegacyHomePage.selectPromotionRegimen();
		s_assert.assertTrue(storeFrontLegacyHomePage.getCurrentURL().toLowerCase().contains(regimen.toLowerCase()), "Expected regimen name is "+regimen.toLowerCase()+" Actual on UI is "+storeFrontLegacyHomePage.getCurrentURL().toLowerCase());
		s_assert.assertTrue(storeFrontLegacyHomePage.isRegimenNamePresentAfterClickedOnRegimen(regimen), "regimen name i.e.: "+regimen+" not present");
		//For ENHANCEMENTS
		storeFrontLegacyHomePage.clickShopSkinCareBtn();
		regimen = TestConstantsRFL.REGIMEN_NAME_ENHANCEMENTS;
		storeFrontLegacyHomePage.selectRegimen(regimen);
		s_assert.assertTrue(storeFrontLegacyHomePage.getCurrentURL().toLowerCase().contains(regimen.toLowerCase()), "Expected regimen name is "+regimen.toLowerCase()+" Actual on UI is "+storeFrontLegacyHomePage.getCurrentURL().toLowerCase());
		s_assert.assertTrue(storeFrontLegacyHomePage.isRegimenNamePresentAfterClickedOnRegimen(regimen), "regimen name i.e.: "+regimen+" not present");
		//For ESSENTIALS
		storeFrontLegacyHomePage.clickShopSkinCareBtn();
		regimen = TestConstantsRFL.REGIMEN_NAME_ESSENTIALS;
		storeFrontLegacyHomePage.selectRegimen(regimen);
		s_assert.assertTrue(storeFrontLegacyHomePage.getCurrentURL().toLowerCase().contains(regimen.toLowerCase()), "Expected regimen name is "+regimen.toLowerCase()+" Actual on UI is "+storeFrontLegacyHomePage.getCurrentURL().toLowerCase());
		s_assert.assertTrue(storeFrontLegacyHomePage.isRegimenNamePresentAfterClickedOnRegimen(regimen), "regimen name i.e.: "+regimen+" not present");
		s_assert.assertAll();
	}

	//Log in as an existen consultant
	@Test
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
	@Test
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
	@Test
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

	//Solution Tool-Find a Rodan  + Fields consultant should be working properly
	@Test(enabled=false)
	public void testVerifyUserIsRedirectedToPwsAfterSelectingSponser(){
		String sponsorID = TestConstantsRFL.CID_CONSULTANT;
		String fetchedPWS = null;
		storeFrontLegacyHomePage =  new StoreFrontLegacyHomePage(driver);
		storeFrontLegacyHomePage.clickHomeTabBtn();
		storeFrontLegacyHomePage.clickSolutionToolImageLink();
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

	//Redefine-Sub links should be displayed properly(regimen, products, results, testimonials, in the news, FAQs, Advice, Glossary)
	@Test
	public void testVerifyRedefineRegimenLinksDisplayedProperly(){
		String regimen = TestConstantsRFL.REGIMEN_NAME_REDEFINE;
		String subSectionRegimen = "Regimen";
		String subSectionProducts = "Products";
		String subSectionResults = "Results";
		//String subSectionTestimonials = "Testimonials";
		//String subSectionInTheNews = "In the News";
		String subSectionFAQ = "FAQs";
		//String subSectionAdvice = "Advice";
		//String subSectionGlossary = "Glossary";

		storeFrontLegacyHomePage =  new StoreFrontLegacyHomePage(driver);
		storeFrontLegacyHomePage.clickShopSkinCareBtn();
		storeFrontLegacyHomePage.selectRegimen(regimen);

		//Verify visibility of redefine regimen Sections.
		s_assert.assertTrue(storeFrontLegacyHomePage.verifyRedefineRegimenSections(subSectionRegimen),"Redefine regimen section regimen is not displayed");
		s_assert.assertTrue(storeFrontLegacyHomePage.verifyRedefineRegimenSections(subSectionProducts),"Redefine regimen section Products is not displayed");
		s_assert.assertTrue(storeFrontLegacyHomePage.verifyRedefineRegimenSections(subSectionResults),"Redefine regimen section Results is not displayed");
		//s_assert.assertTrue(storeFrontLegacyHomePage.verifyRedefineRegimenSections(subSectionTestimonials),"Redefine regimen section testimonials is not displayed");
		//s_assert.assertTrue(storeFrontLegacyHomePage.verifyRedefineRegimenSections(subSectionInTheNews),"Redefine regimen section In the news is not displayed");
		s_assert.assertTrue(storeFrontLegacyHomePage.verifyRedefineRegimenSections(subSectionFAQ),"Redefine regimen section FAQ is not displayed");
		//s_assert.assertTrue(storeFrontLegacyHomePage.verifyRedefineRegimenSections(subSectionAdvice),"Redefine regimen section Advice is not displayed");
		//s_assert.assertTrue(storeFrontLegacyHomePage.verifyRedefineRegimenSections(subSectionGlossary),"Redefine regimen section Glossary is not displayed");

		s_assert.assertAll();
	}

	//log out with a valid user
	@Test
	public void testLogoutWithAValidUser(){
		RFL_DB = driver.getDBNameRFL();
		String whyRF = "Why R+F";
		//		String programsAndIncentives = "Programs and Incentives";
		//		String incomeIllustrator = "Income Illustrator";
		//		String events = "Events";
		//		String meetOurCommunity = "Meet Our Community";
		//		String enrollNow = "Enroll Now";
		//		String gettingStarted = "Getting Started";
		//		String businessKits = "Business Kits";
		//		String redefineYourFuture = "Redefine Your Future";
		String consultantEmailID = null;
		storeFrontLegacyHomePage =  new StoreFrontLegacyHomePage(driver);
		List<Map<String, Object>> randomConsultantList =  null;
		storeFrontLegacyHomePage =  new StoreFrontLegacyHomePage(driver);
		randomConsultantList = DBUtil.performDatabaseQuery(DBQueries_RFL.GET_RANDOM_ACTIVE_CONSULTANT_EMAILID,RFL_DB);
		consultantEmailID = (String) getValueFromQueryResult(randomConsultantList, "EmailAddress");
		storeFrontLegacyHomePage.loginAsConsultant(consultantEmailID,password);
		s_assert.assertTrue(storeFrontLegacyHomePage.verifyUserSuccessfullyLoggedIn(),"consultant is not logged in successfully");
		logout();
		s_assert.assertTrue(storeFrontLegacyHomePage.isForgotPasswordLinkPresent(),"User is not logout successfully");
		//		driver.getURL();
		//		storeFrontLegacyHomePage.clickBeAConsultantBtn();
		//		s_assert.assertTrue(storeFrontLegacyHomePage.isSublinkOfBusinessSystemPresent(whyRF), "Why R+F link is not present under business system");
		//		s_assert.assertTrue(storeFrontLegacyHomePage.isSublinkOfBusinessSystemPresent(programsAndIncentives), "Programs And Incentives link is not present under business system");
		//		s_assert.assertTrue(storeFrontLegacyHomePage.isSublinkOfBusinessSystemPresent(incomeIllustrator), "Income Illustrator link is not present under business system");
		//		s_assert.assertTrue(storeFrontLegacyHomePage.isSublinkOfBusinessSystemPresent(events), "Events link is not present under business system");
		//		s_assert.assertTrue(storeFrontLegacyHomePage.isSublinkOfBusinessSystemPresent(meetOurCommunity), "Meet Our Community link is not present under business system");
		//		s_assert.assertTrue(storeFrontLegacyHomePage.isSublinkOfBusinessSystemPresent(enrollNow), "enroll Now link is not present under business system");
		//		storeFrontLegacyHomePage.clickSublinkOfBusinessSystem(whyRF);
		//		s_assert.assertTrue(storeFrontLegacyHomePage.isSublinkOfBusinessSystemPresent(gettingStarted), "Getting Started link is not present under Why R+F for business system");
		//		s_assert.assertTrue(storeFrontLegacyHomePage.isSublinkOfBusinessSystemPresent(businessKits), "Business Kits link is not present under Why R+F for business system");
		//		s_assert.assertTrue(storeFrontLegacyHomePage.isSublinkOfBusinessSystemPresent(redefineYourFuture), "Redefine Your Future link is not present under Why R+F for business system");
		//		s_assert.assertTrue(storeFrontLegacyHomePage.isSublinkOfBusinessSystemPresent(enrollNow), "Enroll Now link is not present under Why R+F for business system");
		//		s_assert.assertTrue(storeFrontLegacyHomePage.getSelectedHighlightLinkName().contains(gettingStarted), "Expected selected and highlight link name is: "+gettingStarted+" Actual on UI: "+storeFrontLegacyHomePage.getSelectedHighlightLinkName());
		//		storeFrontLegacyHomePage.clickSublinkOfBusinessSystem(businessKits);
		//		s_assert.assertTrue(storeFrontLegacyHomePage.getSelectedHighlightLinkName().contains(businessKits), "Expected selected and highlight link name is: "+businessKits+" Actual on UI: "+storeFrontLegacyHomePage.getSelectedHighlightLinkName());
		//		s_assert.assertTrue(storeFrontLegacyHomePage.getCurrentURL().toLowerCase().contains("business/kits"), "Expected url contains is: business/kits but Actual on UI is "+storeFrontLegacyHomePage.getCurrentURL().toLowerCase());
		//		storeFrontLegacyHomePage.clickSublinkOfBusinessSystem(gettingStarted);
		//		storeFrontLegacyHomePage.clickClickhereLink();
		//		s_assert.assertTrue(storeFrontLegacyHomePage.isClickHereLinkRedirectinToAppropriatePage("PP_11th_Edition.pdf"), "Click here link of business system is not redirecting to PP_11th_Edition.pdf page");
		s_assert.assertAll();
	}

	//Corporate_BUsinessSystem_ Direct Selling
	@Test
	public void testCorporateBusinessSystemDirectSelling(){
		storeFrontLegacyHomePage =  new StoreFrontLegacyHomePage(driver);
		storeFrontLegacyHomePage.clickAboutRFBtn();
		/*storeFrontLegacyHomePage.clickClickhereLink();*/
		storeFrontLegacyHomePage.clickClickhereLinkToLearnDirectSelling();
		s_assert.assertTrue(storeFrontLegacyHomePage.isClickHereLinkRedirectinToAppropriatePage("directselling.org"), "Click here link of business system is not redirecting to http://directselling.org/ page");
		s_assert.assertAll();
	}

	//Corporate_BUsinessSystem_GettingStarted 
	@Test(enabled=false)//needs updation
	public void testCorporateBusinessSystemGettingStarted(){
		String gettingStarted = "Getting Started";
		String whyRF = "Why R+F";
		String redefineYourFuture = "Redefine Your Future";
		String enrollNow = "Enroll Now";
		int randomNum = CommonUtils.getRandomNum(10000, 1000000);
		int ssnRandomNum1 = CommonUtils.getRandomNum(100, 999);
		int ssnRandomNum2 = CommonUtils.getRandomNum(00, 99);
		int ssnRandomNum3 = CommonUtils.getRandomNum(1000, 9999);
		String firstName = TestConstantsRFL.FIRST_NAME;
		String lastName = TestConstantsRFL.LAST_NAME+randomNum;
		String emailAddress = firstName+randomNum+"@xyz.com";
		String addressLine1 =  TestConstantsRFL.ADDRESS_LINE1;
		String postalCode = TestConstantsRFL.POSTAL_CODE;
		String cardNumber = TestConstantsRFL.CARD_NUMBER;
		String nameOnCard = firstName;
		String expMonth = TestConstantsRFL.EXP_MONTH;
		String expYear = TestConstantsRFL.EXP_YEAR;
		String CID = TestConstantsRFL.CID_CONSULTANT;
		String kitName = "Big Business Launch Kit";
		String regimen = "Redefine";
		String enrollemntType = "Express";
		String phnNumber1 = "415";
		String phnNumber2 = "780";
		String phnNumber3 = "9099";
		storeFrontLegacyHomePage =  new StoreFrontLegacyHomePage(driver);
		storeFrontLegacyHomePage.clickBeAConsultantBtn();
		storeFrontLegacyHomePage.clickSublinkOfBusinessSystem(whyRF);
		s_assert.assertTrue(storeFrontLegacyHomePage.isSublinkOfBusinessSystemPresent(gettingStarted), "Getting Started link is not present under Why R+F for business system");
		storeFrontLegacyHomePage.clickSublinkOfBusinessSystem(gettingStarted);
		storeFrontLegacyHomePage.clickClickhereLink();
		s_assert.assertTrue(storeFrontLegacyHomePage.isClickHereLinkRedirectinToAppropriatePage("PP_11th_Edition.pdf"), "Click here link of business system is not redirecting to PP_11th_Edition.pdf page");
		storeFrontLegacyHomePage.clickSublinkOfBusinessSystem(redefineYourFuture);
		storeFrontLegacyHomePage.clickDetailsLink();
		s_assert.assertTrue(storeFrontLegacyHomePage.isClickHereLinkRedirectinToAppropriatePage("REDEFINE-Your-Future-with-BBL-020813.pdf"), "Details link of redefine your future is not redirecting to REDEFINE-Your-Future-with-BBL-020813.pdf");
		storeFrontLegacyHomePage.clickSublinkOfBusinessSystem(enrollNow);
		storeFrontLegacyHomePage.enterCID(CID);
		storeFrontLegacyHomePage.clickSearchResults();
		storeFrontLegacyHomePage.selectEnrollmentKit(kitName);
		storeFrontLegacyHomePage.selectRegimenAndClickNext(regimen);
		storeFrontLegacyHomePage.selectEnrollmentType(enrollemntType);
		storeFrontLegacyHomePage.enterSetUpAccountInformation(firstName, lastName, emailAddress, password, addressLine1, postalCode, phnNumber1, phnNumber2, phnNumber3);
		storeFrontLegacyHomePage.clickSetUpAccountNextBtn();
		storeFrontLegacyHomePage.enterBillingInformation(cardNumber, nameOnCard, expMonth, expYear);
		storeFrontLegacyHomePage.enterAccountInformation(ssnRandomNum1, ssnRandomNum2, ssnRandomNum3, firstName);
		storeFrontLegacyHomePage.clickCompleteAccountNextBtn();
		storeFrontLegacyHomePage.clickTermsAndConditions();
		storeFrontLegacyHomePage.chargeMyCardAndEnrollMe();
		s_assert.assertTrue(storeFrontLegacyHomePage.isCongratulationsMessageAppeared(),"Congratulations Message not appeared");
		s_assert.assertAll();
	}

	//Corporate_BUsinessSystem_ SuccessStories
	@Test
	public void testCorporateBusinessSystemSuccessStories(){
		String meetOurCommunity = "Meet Our Community";
		String rfxCircleAG = "RFx Circle: A-G";
		String rfxCircleHZ = "RFx Circle: H-Z";
		String eliteVAL = "Elite V: A-L";
		String eliteVMZ = "Elite V: M-Z";
		String carAchieversAC= "Car Achievers: A-C";
		String carAchieversDE = "Car Achievers: D-E";
		String carAchieversFG = "Car Achievers: F-G";
		String carAchieversHL = "Car Achievers: H-L";
		String carAchieversMR = "Car Achievers: M-R";
		String carAchieversSZ = "Car Achievers: S-Z";
		storeFrontLegacyHomePage =  new StoreFrontLegacyHomePage(driver);
		storeFrontLegacyHomePage.clickBeAConsultantBtn();
		storeFrontLegacyHomePage.clickSublinkOfBusinessSystem(meetOurCommunity);
		storeFrontLegacyHomePage.clickSublinkOfBusinessSystem(rfxCircleAG);
		s_assert.assertTrue(storeFrontLegacyHomePage.getCurrentURL().contains("RFxcircleAG"), "Expected url contains is: RFxcircleAG but Actual on UI is "+storeFrontLegacyHomePage.getCurrentURL());
		storeFrontLegacyHomePage.clickSublinkOfBusinessSystem(rfxCircleHZ);
		s_assert.assertTrue(storeFrontLegacyHomePage.getCurrentURL().contains("RFxcircleHZ"), "Expected url contains is: RFxcircleHZ but Actual on UI is "+storeFrontLegacyHomePage.getCurrentURL());
		storeFrontLegacyHomePage.clickSublinkOfBusinessSystem(eliteVAL);
		s_assert.assertTrue(storeFrontLegacyHomePage.getCurrentURL().contains("EliteVAL"), "Expected url contains is: EliteVAL but Actual on UI is "+storeFrontLegacyHomePage.getCurrentURL());
		storeFrontLegacyHomePage.clickSublinkOfBusinessSystem(eliteVMZ);
		s_assert.assertTrue(storeFrontLegacyHomePage.getCurrentURL().contains("EliteVMZ"), "Expected url contains is: EliteVMZ but Actual on UI is "+storeFrontLegacyHomePage.getCurrentURL());
		storeFrontLegacyHomePage.clickSublinkOfBusinessSystem(carAchieversAC);
		s_assert.assertTrue(storeFrontLegacyHomePage.getCurrentURL().contains("CarAchieversAC"), "Expected url contains is: CarAchieversAC but Actual on UI is "+storeFrontLegacyHomePage.getCurrentURL());
		storeFrontLegacyHomePage.clickSublinkOfBusinessSystem(carAchieversDE);
		s_assert.assertTrue(storeFrontLegacyHomePage.getCurrentURL().contains("CarAchieversDE"), "Expected url contains is: CarAchieversDE but Actual on UI is "+storeFrontLegacyHomePage.getCurrentURL());
		storeFrontLegacyHomePage.clickSublinkOfBusinessSystem(carAchieversFG);
		s_assert.assertTrue(storeFrontLegacyHomePage.getCurrentURL().contains("CarAchieversFG"), "Expected url contains is: CarAchieversFG but Actual on UI is "+storeFrontLegacyHomePage.getCurrentURL());
		storeFrontLegacyHomePage.clickSublinkOfBusinessSystem(carAchieversHL);
		s_assert.assertTrue(storeFrontLegacyHomePage.getCurrentURL().contains("CarAchieversHL"), "Expected url contains is: CarAchieversHL but Actual on UI is "+storeFrontLegacyHomePage.getCurrentURL());
		storeFrontLegacyHomePage.clickSublinkOfBusinessSystem(carAchieversMR);
		s_assert.assertTrue(storeFrontLegacyHomePage.getCurrentURL().contains("CarAchieversMR"), "Expected url contains is: CarAchieversMR but Actual on UI is "+storeFrontLegacyHomePage.getCurrentURL());
		storeFrontLegacyHomePage.clickSublinkOfBusinessSystem(carAchieversSZ);
		s_assert.assertTrue(storeFrontLegacyHomePage.getCurrentURL().contains("CarAchieversSZ"), "Expected url contains is: CarAchieversSZ but Actual on UI is "+storeFrontLegacyHomePage.getCurrentURL());
		s_assert.assertAll();
	}

	//The Compensation Plan section Compensation Plan is displayed
	@Test//(enabled=false)//needs updation
	public void testCompensationPlanSectionIsDisplayed(){
		String firstSubSectionUnderBusinessSystem = "Why R+F";
		String secondSubSectionUnderBusinessSystem = "Programs and Incentives";
		//String thirdSubSectionUnderBusinessSystem = "Income Illustrator";
		String fourthSubSectionUnderBusinessSystem = "Events";
		String fifthSubSectionUnderBusinessSystem = "Meet Our Community";
		String sixthSubSectionUnderBusinessSystem = "Enroll Now";

		String firstSubSectionUnderProgramsAndIncentives = "Compensation Plan";
		String secondSubSectionUnderProgramsAndIncentives = "Programs and Incentives";
		String thirdSubSectionUnderProgramsAndIncentives = "Enroll Now";

		storeFrontLegacyHomePage =  new StoreFrontLegacyHomePage(driver);
		storeFrontLegacyHomePage.clickBeAConsultantBtn();
		s_assert.assertTrue(storeFrontLegacyHomePage.getCurrentURL().toLowerCase().contains("Business".toLowerCase()), "URL does not contain Business Actual on UI is "+storeFrontLegacyHomePage.getCurrentURL().toLowerCase());
		s_assert.assertTrue(storeFrontLegacyHomePage.verifySubSectionPresentAtBusinessSystemPage(firstSubSectionUnderBusinessSystem),""+firstSubSectionUnderBusinessSystem+" subTitle not present under business system page");
		s_assert.assertTrue(storeFrontLegacyHomePage.verifySubSectionPresentAtBusinessSystemPage(secondSubSectionUnderBusinessSystem),""+secondSubSectionUnderBusinessSystem+" subTitle not present under business system page");
		//s_assert.assertTrue(storeFrontLegacyHomePage.verifySubSectionPresentAtBusinessSystemPage(thirdSubSectionUnderBusinessSystem),""+thirdSubSectionUnderBusinessSystem+" subTitle not present under business system page");
		s_assert.assertTrue(storeFrontLegacyHomePage.verifySubSectionPresentAtBusinessSystemPage(fourthSubSectionUnderBusinessSystem),""+fourthSubSectionUnderBusinessSystem+" subTitle not present under business system page");
		s_assert.assertTrue(storeFrontLegacyHomePage.verifySubSectionPresentAtBusinessSystemPage(fifthSubSectionUnderBusinessSystem),""+fifthSubSectionUnderBusinessSystem+" subTitle not present under business system page");
		s_assert.assertTrue(storeFrontLegacyHomePage.verifySubSectionPresentAtBusinessSystemPage(sixthSubSectionUnderBusinessSystem),""+sixthSubSectionUnderBusinessSystem+" subTitle not present under business system page");
		storeFrontLegacyHomePage.clickSubSectionUnderBusinessSystem(secondSubSectionUnderBusinessSystem);
		s_assert.assertTrue(storeFrontLegacyHomePage.getSelectedHighlightLinkName().contains("Programs and Incentives"), "Expected selected and highlight link name is: "+"Programs and Incentives but Actual on UI: "+storeFrontLegacyHomePage.getSelectedHighlightLinkName());
		//s_assert.assertTrue(storeFrontLegacyHomePage.verifySubSectionPresentAtProgramsAndIncentives(firstSubSectionUnderProgramsAndIncentives),""+firstSubSectionUnderProgramsAndIncentives+" subTitle not present under Programs and Incentives page");
		s_assert.assertTrue(storeFrontLegacyHomePage.verifySubSectionPresentAtProgramsAndIncentives(secondSubSectionUnderProgramsAndIncentives),""+secondSubSectionUnderProgramsAndIncentives+" subTitle not present under Programs and Incentives page");
		s_assert.assertTrue(storeFrontLegacyHomePage.verifySubSectionPresentAtProgramsAndIncentives(thirdSubSectionUnderProgramsAndIncentives),""+thirdSubSectionUnderProgramsAndIncentives+" subTitle not present under Programs and Incentives page");
		storeFrontLegacyHomePage.clickToReadIncomeDisclosure();
		s_assert.assertTrue(storeFrontLegacyHomePage.getCurrentUrlOpenedWindow().contains("RF-Income-Disclosure-Statement.pdf"),"current url is not a valid and Expected url");
		s_assert.assertAll();
	}

	//The Compensation Plan section Programs and Incentives is displayed
	@Test//(enabled=false)//needs updation
	public void testCompensationPlanProgramsAndIncentivesIsDisplayed(){
		String firstSubSectionUnderBusinessSystem = "Why R+F";
		String secondSubSectionUnderBusinessSystem = "Programs and Incentives";
		//String thirdSubSectionUnderBusinessSystem = "Income Illustrator";
		String fourthSubSectionUnderBusinessSystem = "Events";
		String fifthSubSectionUnderBusinessSystem = "Meet Our Community";
		String sixthSubSectionUnderBusinessSystem = "Enroll Now";

		//String firstSubSectionUnderProgramsAndIncentives = "Compensation Plan";
		String secondSubSectionUnderProgramsAndIncentives = "Programs and Incentives";
		String thirdSubSectionUnderProgramsAndIncentives = "Enroll Now";

		storeFrontLegacyHomePage =  new StoreFrontLegacyHomePage(driver);
		storeFrontLegacyHomePage.clickBeAConsultantBtn();
		s_assert.assertTrue(storeFrontLegacyHomePage.getCurrentURL().toLowerCase().contains("Business".toLowerCase()), "URL does not contain Business Actual on UI is "+storeFrontLegacyHomePage.getCurrentURL().toLowerCase());
		s_assert.assertTrue(storeFrontLegacyHomePage.verifySubSectionPresentAtBusinessSystemPage(firstSubSectionUnderBusinessSystem),""+firstSubSectionUnderBusinessSystem+" subTitle not present under business system page");
		s_assert.assertTrue(storeFrontLegacyHomePage.verifySubSectionPresentAtBusinessSystemPage(secondSubSectionUnderBusinessSystem),""+secondSubSectionUnderBusinessSystem+" subTitle not present under business system page");
		//s_assert.assertTrue(storeFrontLegacyHomePage.verifySubSectionPresentAtBusinessSystemPage(thirdSubSectionUnderBusinessSystem),""+thirdSubSectionUnderBusinessSystem+" subTitle not present under business system page");
		s_assert.assertTrue(storeFrontLegacyHomePage.verifySubSectionPresentAtBusinessSystemPage(fourthSubSectionUnderBusinessSystem),""+fourthSubSectionUnderBusinessSystem+" subTitle not present under business system page");
		s_assert.assertTrue(storeFrontLegacyHomePage.verifySubSectionPresentAtBusinessSystemPage(fifthSubSectionUnderBusinessSystem),""+fifthSubSectionUnderBusinessSystem+" subTitle not present under business system page");
		s_assert.assertTrue(storeFrontLegacyHomePage.verifySubSectionPresentAtBusinessSystemPage(sixthSubSectionUnderBusinessSystem),""+sixthSubSectionUnderBusinessSystem+" subTitle not present under business system page");
		storeFrontLegacyHomePage.clickSubSectionUnderBusinessSystem(secondSubSectionUnderBusinessSystem);
		//s_assert.assertTrue(storeFrontLegacyHomePage.verifySubSectionPresentAtProgramsAndIncentives(firstSubSectionUnderProgramsAndIncentives),""+firstSubSectionUnderProgramsAndIncentives+" subTitle not present under Programs and Incentives page");
		s_assert.assertTrue(storeFrontLegacyHomePage.verifySubSectionPresentAtProgramsAndIncentives(secondSubSectionUnderProgramsAndIncentives),""+secondSubSectionUnderProgramsAndIncentives+" subTitle not present under Programs and Incentives page");
		s_assert.assertTrue(storeFrontLegacyHomePage.verifySubSectionPresentAtProgramsAndIncentives(thirdSubSectionUnderProgramsAndIncentives),""+thirdSubSectionUnderProgramsAndIncentives+" subTitle not present under Programs and Incentives page");
		storeFrontLegacyHomePage.clickSubSectionUnderBusinessSystem(secondSubSectionUnderBusinessSystem);
		s_assert.assertTrue(storeFrontLegacyHomePage.getSelectedHighlightLinkName().contains("Programs and Incentives"), "Expected selected and highlight link name is: "+"Programs and Incentives"+" Actual on UI: "+storeFrontLegacyHomePage.getSelectedHighlightLinkName());
		//		storeFrontLegacyHomePage.clickDetailsLinkUnderProgramsIncentivePage("Fast Start Program");
		//		s_assert.assertTrue(storeFrontLegacyHomePage.getCurrentUrlOpenedWindow().contains("Fast_Start_Flyer_2013_Secured.pdf"),"current url is not a valid and Expected url");
		//		storeFrontLegacyHomePage.clickDetailsLinkUnderProgramsIncentivePage("RF");
		//		s_assert.assertTrue(storeFrontLegacyHomePage.getCurrentUrlOpenedWindow().contains("2016_RFx_Circle_TCs.pdf"),"current url is not a valid and Expected url");
		//		storeFrontLegacyHomePage.clickDetailsLinkUnderProgramsIncentivePage("Elite V Program");
		//		s_assert.assertTrue(storeFrontLegacyHomePage.getCurrentUrlOpenedWindow().contains("Elite_V_Flyer_Hawaii_2016.pdf"),"current url is not a valid and Expected url");
		//		storeFrontLegacyHomePage.clickDetailsLinkUnderProgramsIncentivePage("Road to RF");
		//		s_assert.assertTrue(storeFrontLegacyHomePage.getCurrentUrlOpenedWindow().contains("Road-to-RFx-Car-Incentive-Program-Flyer-09.24.11.pdf"),"current url is not a valid and Expected url");
		//		storeFrontLegacyHomePage.clickDetailsLinkUnderProgramsIncentivePage("RF Center for Leadership");
		//		s_assert.assertTrue(storeFrontLegacyHomePage.getCurrentUrlOpenedWindow().contains("RFCL_Spring2016_Flyer.pdf"),"current url is not a valid and Expected url");
		//		storeFrontLegacyHomePage.clickDetailsLinkUnderProgramsIncentivePage("Premier V Trip");
		//		s_assert.assertTrue(storeFrontLegacyHomePage.getCurrentUrlOpenedWindow().contains("PremierV_Flyer_F16_USA.pdf"),"current url is not a valid and Expected url");
		s_assert.assertAll();
	}

	//The Compensation Plan section Enroll Now is displayed
	@Test(enabled=false)//needs updation
	public void testCompensationPlanSectionEnrollNowIsDisplayed(){
		String firstSubSectionUnderBusinessSystem = "Why R+F";
		String secondSubSectionUnderBusinessSystem = "Programs and Incentives";
		String thirdSubSectionUnderBusinessSystem = "Income Illustrator";
		String fourthSubSectionUnderBusinessSystem = "Events";
		String fifthSubSectionUnderBusinessSystem = "Meet Our Community";
		String sixthSubSectionUnderBusinessSystem = "Enroll Now";

		String firstSubSectionUnderProgramsAndIncentives = "Compensation Plan";
		String secondSubSectionUnderProgramsAndIncentives = "Programs and Incentives";
		String thirdSubSectionUnderProgramsAndIncentives = "Enroll Now";

		storeFrontLegacyHomePage =  new StoreFrontLegacyHomePage(driver);
		storeFrontLegacyHomePage.clickBeAConsultantBtn();
		s_assert.assertTrue(storeFrontLegacyHomePage.getCurrentURL().toLowerCase().contains("Business".toLowerCase()), "URL does not contain Business Actual on UI is "+storeFrontLegacyHomePage.getCurrentURL().toLowerCase());
		s_assert.assertTrue(storeFrontLegacyHomePage.verifySubSectionPresentAtBusinessSystemPage(firstSubSectionUnderBusinessSystem),""+firstSubSectionUnderBusinessSystem+" subTitle not present under business system page");
		s_assert.assertTrue(storeFrontLegacyHomePage.verifySubSectionPresentAtBusinessSystemPage(secondSubSectionUnderBusinessSystem),""+secondSubSectionUnderBusinessSystem+" subTitle not present under business system page");
		s_assert.assertTrue(storeFrontLegacyHomePage.verifySubSectionPresentAtBusinessSystemPage(thirdSubSectionUnderBusinessSystem),""+thirdSubSectionUnderBusinessSystem+" subTitle not present under business system page");
		s_assert.assertTrue(storeFrontLegacyHomePage.verifySubSectionPresentAtBusinessSystemPage(fourthSubSectionUnderBusinessSystem),""+thirdSubSectionUnderBusinessSystem+" subTitle not present under business system page");
		s_assert.assertTrue(storeFrontLegacyHomePage.verifySubSectionPresentAtBusinessSystemPage(fifthSubSectionUnderBusinessSystem),""+fifthSubSectionUnderBusinessSystem+" subTitle not present under business system page");
		s_assert.assertTrue(storeFrontLegacyHomePage.verifySubSectionPresentAtBusinessSystemPage(sixthSubSectionUnderBusinessSystem),""+sixthSubSectionUnderBusinessSystem+" subTitle not present under business system page");
		storeFrontLegacyHomePage.clickSubSectionUnderBusinessSystem(secondSubSectionUnderBusinessSystem);
		s_assert.assertTrue(storeFrontLegacyHomePage.verifySubSectionPresentAtProgramsAndIncentives(firstSubSectionUnderProgramsAndIncentives),""+firstSubSectionUnderProgramsAndIncentives+" subTitle not present under Programs and Incentives page");
		s_assert.assertTrue(storeFrontLegacyHomePage.verifySubSectionPresentAtProgramsAndIncentives(secondSubSectionUnderProgramsAndIncentives),""+secondSubSectionUnderProgramsAndIncentives+" subTitle not present under Programs and Incentives page");
		s_assert.assertTrue(storeFrontLegacyHomePage.verifySubSectionPresentAtProgramsAndIncentives(thirdSubSectionUnderProgramsAndIncentives),""+thirdSubSectionUnderProgramsAndIncentives+" subTitle not present under Programs and Incentives page");
		storeFrontLegacyHomePage.clickSubSectionUnderBusinessSystem(thirdSubSectionUnderProgramsAndIncentives);
		s_assert.assertTrue(storeFrontLegacyHomePage.getCurrentURL().contains("NewEnrollment/SearchSponsor"),"current url is not a valid and Expected url");
		s_assert.assertAll();
	}

	//Contact us-link should be redirecting properly
	@Test
	public void testContactUsLinkShouldBeRedirectingProperly(){
		storeFrontLegacyHomePage =  new StoreFrontLegacyHomePage(driver);
		storeFrontLegacyHomePage.clickContactUsAtFooter();
		s_assert.assertTrue(storeFrontLegacyHomePage.getCurrentURL().toLowerCase().contains("Contact".toLowerCase()), "URL does not contain Contact Actual on UI is "+storeFrontLegacyHomePage.getCurrentURL().toLowerCase());
		s_assert.assertTrue(storeFrontLegacyHomePage.verifylinkIsRedirectedToContactUsPage(),"link is not redirected to contact us page");
		s_assert.assertAll();
	}

	//Essentials Products-links should be working properly (Real results, PC perks, solution tool, digital product catalog)
	@Test(enabled=false)//needs updation
	public void testVerifyEssentialRegimenProductLinksWorkingProperly(){
		String regimen = TestConstantsRFL.REGIMEN_NAME_ESSENTIALS;
		String subSectionLinkRealResults = "Real Results";
		String subSectionLinkPCPerks = "PC PERKS";
		String subSectionSolutionTool = "Solution Tool";
		String subSectionDigitalProductCatalogue = "Digital Product Catalog";

		storeFrontLegacyHomePage =  new StoreFrontLegacyHomePage(driver);
		storeFrontLegacyHomePage.clickShopSkinCareBtn();
		storeFrontLegacyHomePage.selectRegimen(regimen);

		//Verify visibility of Essentials regimen Sections.
		s_assert.assertTrue(storeFrontLegacyHomePage.verifyEssentialsRegimenSections(subSectionLinkRealResults),"Essential regimen section real results is not displayed");
		s_assert.assertTrue(storeFrontLegacyHomePage.verifyEssentialsRegimenSections(subSectionLinkPCPerks),"Essential regimen section PC Perks is not displayed");
		s_assert.assertTrue(storeFrontLegacyHomePage.verifyEssentialsRegimenSections(subSectionSolutionTool),"Essential regimen section Solution tool is not displayed");
		s_assert.assertTrue(storeFrontLegacyHomePage.verifyEssentialsRegimenSections(subSectionDigitalProductCatalogue),"Essential regimen section Digital product catalogue is not displayed");

		//Verify Visibility of Essentials Regimen Real Results subSections.	
		storeFrontLegacyHomePage.clickSubSectionUnderRegimen(subSectionLinkRealResults);
		s_assert.assertTrue(storeFrontLegacyHomePage.getCurrentURL().toLowerCase().contains("Results".toLowerCase()), "Expected regimen name is "+"Results".toLowerCase()+" Actual on UI is "+storeFrontLegacyHomePage.getCurrentURL().toLowerCase());
		s_assert.assertTrue(storeFrontLegacyHomePage.verifyUserIsRedirectedToRealResultsPage(),"user is not on Real results page");
		storeFrontLegacyHomePage.navigateToBackPage();

		//Verify Visibility of Essentials Regimen PC Perks subSections.		
		storeFrontLegacyHomePage.clickSubSectionUnderRegimen(subSectionLinkPCPerks);
		s_assert.assertTrue(storeFrontLegacyHomePage.getCurrentURL().toLowerCase().contains("PC".toLowerCase()), "Expected regimen name is "+"PCPerks".toLowerCase()+" Actual on UI is "+storeFrontLegacyHomePage.getCurrentURL().toLowerCase());		
		s_assert.assertTrue(storeFrontLegacyHomePage.verifyUserIsRedirectedToPCPerksPage(),"user is not on PC Perks page");
		storeFrontLegacyHomePage.navigateToBackPage();

		//Verify Visibility of Essentials Regimen Solution tool subSections.	
		storeFrontLegacyHomePage.clickSubSectionUnderRegimen(subSectionSolutionTool);
		s_assert.assertTrue(storeFrontLegacyHomePage.getCurrentURL().toLowerCase().contains("Solution".toLowerCase()), "Expected regimen name is "+"SolutionTool".toLowerCase()+" Actual on UI is "+storeFrontLegacyHomePage.getCurrentURL().toLowerCase());
		s_assert.assertTrue(storeFrontLegacyHomePage.verifyUserIsRedirectedToSolutionToolPage(),"user is not on Solution tool page");
		storeFrontLegacyHomePage.navigateToBackPage();

		//Verify Visibility of Essentials Regimen Digital product catalogue subSections.	
		storeFrontLegacyHomePage.clickSubSectionUnderRegimen(subSectionDigitalProductCatalogue);
		s_assert.assertTrue(storeFrontLegacyHomePage.getCurrentURL().toLowerCase().contains("Digimag".toLowerCase()), "Expected regimen name is "+"Digimag".toLowerCase()+" Actual on UI is "+storeFrontLegacyHomePage.getCurrentURL().toLowerCase());
		s_assert.assertTrue(storeFrontLegacyHomePage.verifyUserIsRedirectedToDigitalProductCataloguePage(),"user is not on Digital product catalogue page");
		storeFrontLegacyHomePage.navigateToBackPage();
		s_assert.assertAll();
	}

	//Enhancements Products-links should be working properly (Real results, PC perks, solution tool, digital product catalog)
	@Test(enabled=false)//needs updation
	public void testVerifyEnhancementsRegimenProductLinksWorkingProperly(){
		String regimen = TestConstantsRFL.REGIMEN_NAME_ENHANCEMENTS;
		String subSectionLinkRealResults = "Real Results";
		String subSectionLinkPCPerks = "PC PERKS";
		String subSectionSolutionTool = "Solution Tool";
		String subSectionDigitalProductCatalogue = "Digital Product Catalog";

		storeFrontLegacyHomePage =  new StoreFrontLegacyHomePage(driver);
		storeFrontLegacyHomePage.clickShopSkinCareBtn();
		storeFrontLegacyHomePage.selectRegimen(regimen);

		//Verify visibility of Essentials regimen Sections.
		s_assert.assertTrue(storeFrontLegacyHomePage.verifyEnhancementsRegimenSections(subSectionLinkRealResults),"Enhancements regimen section real results is not displayed");
		s_assert.assertTrue(storeFrontLegacyHomePage.verifyEnhancementsRegimenSections(subSectionLinkPCPerks),"Enhancements regimen section PC Perks is not displayed");
		s_assert.assertTrue(storeFrontLegacyHomePage.verifyEnhancementsRegimenSections(subSectionSolutionTool),"Enhancements regimen section Solution tool is not displayed");
		s_assert.assertTrue(storeFrontLegacyHomePage.verifyEnhancementsRegimenSections(subSectionDigitalProductCatalogue),"Enhancements regimen section Digital product catalogue is not displayed");

		//Verify Visibility of Essentials Regimen Real Results subSections.	
		storeFrontLegacyHomePage.clickSubSectionUnderRegimen(subSectionLinkRealResults);
		s_assert.assertTrue(storeFrontLegacyHomePage.getCurrentURL().toLowerCase().contains("Results".toLowerCase()), "Expected regimen name is "+"Results".toLowerCase()+" Actual on UI is "+storeFrontLegacyHomePage.getCurrentURL().toLowerCase());
		s_assert.assertTrue(storeFrontLegacyHomePage.verifyUserIsRedirectedToRealResultsPage(),"user is not on Real results page");
		storeFrontLegacyHomePage.navigateToBackPage();

		//Verify Visibility of Essentials Regimen PC Perks subSections.		
		storeFrontLegacyHomePage.clickSubSectionUnderRegimen(subSectionLinkPCPerks);
		s_assert.assertTrue(storeFrontLegacyHomePage.getCurrentURL().toLowerCase().contains("PC".toLowerCase()), "Expected regimen name is "+"PCPerks".toLowerCase()+" Actual on UI is "+storeFrontLegacyHomePage.getCurrentURL().toLowerCase());		
		s_assert.assertTrue(storeFrontLegacyHomePage.verifyUserIsRedirectedToPCPerksPage(),"user is not on PC Perks page");
		storeFrontLegacyHomePage.navigateToBackPage();

		//Verify Visibility of Essentials Regimen Solution tool subSections.	
		storeFrontLegacyHomePage.clickSubSectionUnderRegimen(subSectionSolutionTool);
		s_assert.assertTrue(storeFrontLegacyHomePage.getCurrentURL().toLowerCase().contains("Solution".toLowerCase()), "Expected regimen name is "+"SolutionTool".toLowerCase()+" Actual on UI is "+storeFrontLegacyHomePage.getCurrentURL().toLowerCase());
		s_assert.assertTrue(storeFrontLegacyHomePage.verifyUserIsRedirectedToSolutionToolPage(),"user is not on Solution tool page");
		storeFrontLegacyHomePage.navigateToBackPage();

		//Verify Visibility of Essentials Regimen Digital product catalogue subSections.	
		storeFrontLegacyHomePage.clickSubSectionUnderRegimen(subSectionDigitalProductCatalogue);
		s_assert.assertTrue(storeFrontLegacyHomePage.getCurrentURL().toLowerCase().contains("Digimag".toLowerCase()), "Expected regimen name is "+"Digimag".toLowerCase()+" Actual on UI is "+storeFrontLegacyHomePage.getCurrentURL().toLowerCase());
		s_assert.assertTrue(storeFrontLegacyHomePage.verifyUserIsRedirectedToDigitalProductCataloguePage(),"user is not on Digital product catalogue page");
		storeFrontLegacyHomePage.navigateToBackPage();
		s_assert.assertAll();
	}

	//Corporate_ FindAConsultant
	@Test(enabled=true)
	public void testCorporateFindAConsultant(){
		String expectedURL = "LocatePWS.aspx?fromHome=1";
		storeFrontLegacyHomePage =  new StoreFrontLegacyHomePage(driver);
		/*storeFrontLegacyHomePage.clickHomeTabBtn();*/
		/*storeFrontLegacyHomePage.clickFindAConsultantImageLink();*/
		storeFrontLegacyHomePage.clickConnectWithAConsultant();
		s_assert.assertTrue(storeFrontLegacyHomePage.getCurrentURL().contains(expectedURL), "Current url expected is: "+expectedURL+" while actual on UI is "+storeFrontLegacyHomePage.getCurrentURL());
		s_assert.assertAll();
	}

	//Corporate_BUsinessSystem_CompensationPlan
	@Test(enabled=false)//needs updation
	public void testCorporateBusinessCompensationPlan(){
		int randomNum = CommonUtils.getRandomNum(10000, 1000000);
		int ssnRandomNum1 = CommonUtils.getRandomNum(100, 999);
		int ssnRandomNum2 = CommonUtils.getRandomNum(00, 99);
		int ssnRandomNum3 = CommonUtils.getRandomNum(1000, 9999);
		String firstName = TestConstantsRFL.FIRST_NAME;
		String lastName = TestConstantsRFL.LAST_NAME+randomNum;
		String emailAddress = firstName+randomNum+"@xyz.com";
		String addressLine1 =  TestConstantsRFL.ADDRESS_LINE1;
		String postalCode = TestConstantsRFL.POSTAL_CODE;
		String cardNumber = TestConstantsRFL.CARD_NUMBER;
		String nameOnCard = firstName;
		String expMonth = TestConstantsRFL.EXP_MONTH;
		String expYear = TestConstantsRFL.EXP_YEAR;
		String CID = TestConstantsRFL.CID_CONSULTANT;
		String kitName = "Big Business Launch Kit";
		String regimen = "Redefine";
		String enrollemntType = "Express";
		String phnNumber1 = "415";
		String phnNumber2 = "780";
		String phnNumber3 = "9099";
		String secondSubSectionUnderBusinessSystem = "Programs and Incentives";
		String firstSubSectionUnderProgramsAndIncentives = "Compensation Plan";
		String secondSubSectionUnderProgramsAndIncentives = "Programs and Incentives";
		storeFrontLegacyHomePage =  new StoreFrontLegacyHomePage(driver);
		storeFrontLegacyHomePage.clickBeAConsultantBtn();
		storeFrontLegacyHomePage.clickSubSectionUnderBusinessSystem(secondSubSectionUnderBusinessSystem);
		storeFrontLegacyHomePage.clickSubSectionUnderBusinessSystem(firstSubSectionUnderProgramsAndIncentives);
		//Verify Compensation page
		s_assert.assertTrue(storeFrontLegacyHomePage.getSelectedHighlightLinkName().contains("Compensation Plan"), "Expected selected and highlight link name is: "+"Compensation Plan"+" Actual on UI: "+storeFrontLegacyHomePage.getSelectedHighlightLinkName());
		s_assert.assertTrue(storeFrontLegacyHomePage.getCurrentURL().toLowerCase().contains("Compensation".toLowerCase()), "Expected url should contain: "+"Compensation"+" Actual on UI: "+storeFrontLegacyHomePage.getCurrentURL());
		storeFrontLegacyHomePage.clickToReadIncomeDisclosure();
		s_assert.assertTrue(storeFrontLegacyHomePage.getCurrentUrlOpenedWindow().contains("RF-Income-Disclosure-Statement.pdf"),"current url is not a valid and Expected url");
		//Click program and incentives link
		storeFrontLegacyHomePage.clickSubSectionUnderBusinessSystem(secondSubSectionUnderProgramsAndIncentives);
		s_assert.assertTrue(storeFrontLegacyHomePage.getCurrentURL().toLowerCase().contains("ProgramsIncentives".toLowerCase()), "Expected url should contain: "+"ProgramsIncentives"+" Actual on UI: "+storeFrontLegacyHomePage.getCurrentURL());
		//click details link under fast start program section
		storeFrontLegacyHomePage.clickDetailsLinkUnderProgramsIncentivePage("Fast Start Program");
		s_assert.assertTrue(storeFrontLegacyHomePage.getCurrentUrlOpenedWindow().contains("Fast_Start_Flyer_2013_Secured.pdf"),"current url is not as expected under detail page of fast start program section page");
		//click details link under Road to RFx car incentive program section
		storeFrontLegacyHomePage.clickDetailsLinkUnderProgramsIncentivePage("Road to RF");
		s_assert.assertTrue(storeFrontLegacyHomePage.getCurrentUrlOpenedWindow().contains("Road-to-RFx-Car-Incentive-Program-Flyer-09.24.11.pdf"),"current url is not as expected under detail page of road to rfx car incentive program section page");
		//click details link under Elite V  program section
		storeFrontLegacyHomePage.clickDetailsLinkUnderProgramsIncentivePage("Elite V Program");
		s_assert.assertTrue(storeFrontLegacyHomePage.getCurrentUrlOpenedWindow().contains("Elite_V_Flyer_Hawaii_2016.pdf"),"current url is not as expected under detail page of Elite V  program section page");
		//click details link under RFx Circle program section
		storeFrontLegacyHomePage.clickDetailsLinkUnderProgramsIncentivePage("RF");
		s_assert.assertTrue(storeFrontLegacyHomePage.getCurrentUrlOpenedWindow().contains("2016_RFx_Circle_TCs.pdf"),"current url is not as expected under detail page of RFx Circle  program section page");
		storeFrontLegacyHomePage.navigateToBackPage();
		storeFrontLegacyHomePage.navigateToBackPage();
		//complete enroll flow.
		storeFrontLegacyHomePage.clickEnrollNowBtnOnBusinessPage();
		storeFrontLegacyHomePage.enterCID(CID);
		storeFrontLegacyHomePage.clickSearchResults();
		storeFrontLegacyHomePage.selectEnrollmentKit(kitName);
		storeFrontLegacyHomePage.selectRegimenAndClickNext(regimen);
		storeFrontLegacyHomePage.selectEnrollmentType(enrollemntType);
		storeFrontLegacyHomePage.enterSetUpAccountInformation(firstName, lastName, emailAddress, password, addressLine1, postalCode, phnNumber1, phnNumber2, phnNumber3);
		storeFrontLegacyHomePage.clickSetUpAccountNextBtn();
		storeFrontLegacyHomePage.enterBillingInformation(cardNumber, nameOnCard, expMonth, expYear);
		storeFrontLegacyHomePage.enterAccountInformation(ssnRandomNum1, ssnRandomNum2, ssnRandomNum3, firstName);
		storeFrontLegacyHomePage.clickCompleteAccountNextBtn();
		storeFrontLegacyHomePage.clickTermsAndConditions();
		storeFrontLegacyHomePage.chargeMyCardAndEnrollMe();
		s_assert.assertTrue(storeFrontLegacyHomePage.isCongratulationsMessageAppeared(),"");
		s_assert.assertAll();
	}

	//Corporate_BUsinessSystem_ IncomeIllustrator  
	@Test(enabled=false)//needs updation
	public void corporateBusinessSystemIncomeIllustrator(){
		String incomeIllustrator = "Income Illustrator";
		storeFrontLegacyHomePage =  new StoreFrontLegacyHomePage(driver);
		storeFrontLegacyHomePage.clickBeAConsultantBtn();
		storeFrontLegacyHomePage.clickSublinkOfBusinessSystem(incomeIllustrator);
		storeFrontLegacyHomePage.clickStartNowBtn();
		s_assert.assertTrue(storeFrontLegacyHomePage.isStartNowBtnRedirectinToAppropriatePage("IncomeIllustrator/index.html"), "start now btn of income illustrator is not redirecting to IncomeIllustrator/index.html");
		s_assert.assertAll();

	}

	//The Getting Started section Redefine Your Future is displayed
	@Test(enabled=false)
	public void testGettingStartedSectionRedefineYourFutureIsDisplayed(){
		storeFrontLegacyHomePage =  new StoreFrontLegacyHomePage(driver);
		storeFrontLegacyHomePage.clickBeAConsultantBtn();
		//verify the sub-menu title under the title business system?
		s_assert.assertTrue(storeFrontLegacyHomePage.validateEnrollNowLinkPresent(),"Enroll Now Link is not present");
		s_assert.assertTrue(storeFrontLegacyHomePage.validateMeetOurCommunityLinkPresent(),"Meet Our Community is not present");
		s_assert.assertTrue(storeFrontLegacyHomePage.validateEventsLinkPresent(),"Events Link is not present");
		//s_assert.assertTrue(storeFrontLegacyHomePage.validateIncomeIllustratorLinkPresent(),"Income Illustrator Link is not present");
		s_assert.assertTrue(storeFrontLegacyHomePage.validateProgramsAndIncentivesLinkPresent(),"ProgramIncentives Link is not present");
		s_assert.assertTrue(storeFrontLegacyHomePage.validateWhyRFLinkPresent(),"Why RF Link is not present");
		//Navigate to Redefine Your Future section-
		storeFrontLegacyHomePage.clickWhyRFLinkUnderBusinessSystem();
		storeFrontLegacyHomePage.clickRedefineYourFutureLinkUnderWhyRF();
		//verify url for 'Redefine Your Future'
		s_assert.assertTrue(storeFrontLegacyHomePage.validateRedefineYourFuturePageDisplayed(),"'Redefine Your Future' Page Is not displayed");
		s_assert.assertAll();
	}

	//The Getting Started Section Business Kit is displayed
	@Test(enabled=false)
	public void testGettingStartedSectionBusinessKitDisplayed(){
		storeFrontLegacyHomePage =  new StoreFrontLegacyHomePage(driver);
		storeFrontLegacyHomePage.clickBeAConsultantBtn();
		//verify the sub-menu title under the title business system?
		s_assert.assertTrue(storeFrontLegacyHomePage.validateEnrollNowLinkPresent(),"Enroll Now Link is not present");
		s_assert.assertTrue(storeFrontLegacyHomePage.validateMeetOurCommunityLinkPresent(),"Meet Our Community is not present");
		s_assert.assertTrue(storeFrontLegacyHomePage.validateEventsLinkPresent(),"Events Link is not present");
		//s_assert.assertTrue(storeFrontLegacyHomePage.validateIncomeIllustratorLinkPresent(),"Income Illustrator Link is not present");
		s_assert.assertTrue(storeFrontLegacyHomePage.validateProgramsAndIncentivesLinkPresent(),"ProgramIncentives Link is not present");
		s_assert.assertTrue(storeFrontLegacyHomePage.validateWhyRFLinkPresent(),"Why RF Link is not present");
		//Navigate to business kit section-
		storeFrontLegacyHomePage.clickWhyRFLinkUnderBusinessSystem();
		storeFrontLegacyHomePage.clickBusinessKitsUnderWhyRF();
		//verify that the 'Business Kits' Section displays the information?
		s_assert.assertTrue(storeFrontLegacyHomePage.validateBusinessKitSectionIsDisplayed(),"Business Kit Section is not displayed with the Information");
		s_assert.assertAll();
	}

	//Company Contact us Link should be displayed properly
	@Test(enabled=false)
	public void testCompanyContactUsLink(){
		storeFrontLegacyHomePage =  new StoreFrontLegacyHomePage(driver);
		storeFrontLegacyHomePage.clickAboutRFBtn();
		//verify company Contact Us Link?
		s_assert.assertTrue(storeFrontLegacyHomePage.validateCompanyContactUsLink(),"Company Contact Us link didn't work");
		s_assert.assertAll();
	}

	//Company Press Room Link should be displayed properly
	@Test(enabled=false)
	public void testCompanyPressRoomLink(){
		storeFrontLegacyHomePage =  new StoreFrontLegacyHomePage(driver);
		storeFrontLegacyHomePage.clickAboutRFBtn();
		//verify company Press Room Link?
		s_assert.assertTrue(storeFrontLegacyHomePage.validateCompanyPressRoomLink(),"Company Press Room link didn't work");
		s_assert.assertAll();
	}

	//Company Careers Link should be displayed properly
	@Test(enabled=false)
	public void testCompanyCareersLink(){
		storeFrontLegacyHomePage =  new StoreFrontLegacyHomePage(driver);
		storeFrontLegacyHomePage.clickAboutRFBtn();
		//verify company careers Link?
		s_assert.assertTrue(storeFrontLegacyHomePage.validateCompanyCareersLink(),"Company careers link didn't work");
		s_assert.assertAll();
	}

	//Company Our Story Link should be displayed properly
	@Test(enabled=false)
	public void testCompanyOurStoryLink(){
		storeFrontLegacyHomePage =  new StoreFrontLegacyHomePage(driver);
		storeFrontLegacyHomePage.clickAboutRFBtn();
		//verify Our Story Link?
		s_assert.assertTrue(storeFrontLegacyHomePage.validateWhoWeAreLink(),"Who We Are link didn't work");
		s_assert.assertAll();
	}

	//Company Execute Link should be displayed properly
	@Test(enabled=false)
	public void testCompanyExecuteTeamLink(){
		storeFrontLegacyHomePage =  new StoreFrontLegacyHomePage(driver);
		storeFrontLegacyHomePage.clickAboutRFBtn();
		//verify Execute Team Link?
		s_assert.assertTrue(storeFrontLegacyHomePage.validateExecuteTeamLink(),"Execute Team link didn't work");
		s_assert.assertAll();
	}

	//The Getting Started section Enroll Now is displayed
	@Test(enabled=false)//needs updation
	public void testGettingStartedSectionEnrollNowIsDisplayed(){
		storeFrontLegacyHomePage =  new StoreFrontLegacyHomePage(driver);
		storeFrontLegacyHomePage.clickBeAConsultantBtn();
		//verify the sub-menu title under the title business system?
		s_assert.assertTrue(storeFrontLegacyHomePage.validateEnrollNowLinkPresent(),"Enroll Now Link is not present");
		s_assert.assertTrue(storeFrontLegacyHomePage.validateMeetOurCommunityLinkPresent(),"Meet Our Link is not present");
		s_assert.assertTrue(storeFrontLegacyHomePage.validateEventsLinkPresent(),"Events Link is not present");
		s_assert.assertTrue(storeFrontLegacyHomePage.validateIncomeIllustratorLinkPresent(),"Income Illustrator Link is not present");
		s_assert.assertTrue(storeFrontLegacyHomePage.validateProgramsAndIncentivesLinkPresent(),"ProgramIncentives Link is not present");
		s_assert.assertTrue(storeFrontLegacyHomePage.validateWhyRFLinkPresent(),"Why RF Link is not present");
		//Navigate to Redefine Your Future section-
		storeFrontLegacyHomePage.clickWhyRFLinkUnderBusinessSystem();
		storeFrontLegacyHomePage.clickEnrollNowLinkUnderWhyRF();
		//verify url for 'Enroll Now'
		s_assert.assertTrue(storeFrontLegacyHomePage.validateSearchSponsorPageDisplayed(),"'Search Sponsor' Page Is not displayed");
		s_assert.assertAll();
	}

	//Disclaimer-link should be redirecting properly
	@Test
	public void testDisclaimerLinkShouldBeRedirectedProperly(){
		storeFrontLegacyHomePage =  new StoreFrontLegacyHomePage(driver);
		//verify Disclaimer in footer should redirect properly?
		s_assert.assertTrue(storeFrontLegacyHomePage.validateDisclaimerLinkInFooter(),"'Disclaimer Link' doesn't redirect to disclaimer page");
		s_assert.assertAll();
	}

	//Company-PFC Foundation link should be redirecting properly 
	@Test(enabled=false)//needs updation
	public void testCompanyPFCFoundationLinkShouldRedirectProperly(){
		storeFrontLegacyHomePage =  new StoreFrontLegacyHomePage(driver);
		storeFrontLegacyHomePage.clickAboutRFBtn();
		//verify company PFC Foundation link?
		storeFrontLegacyHomePage.clickGivingBackLinkUnderAboutRF();
		s_assert.assertTrue(storeFrontLegacyHomePage.validateCompanyPFCFoundationPageDisplayed(),"'Company PFC Foundation' page is not displayed");
		s_assert.assertAll();
	}

	//Corporate_ R+FInTheNews
	@Test(enabled=true)
	public void testCorporateRFInTheNews(){
		String expectedURL = "Company/PR";
		storeFrontLegacyHomePage =  new StoreFrontLegacyHomePage(driver);
/*		storeFrontLegacyHomePage.clickHomeTabBtn();
		storeFrontLegacyHomePage.clickRFInTheNewsImageLink();*/
		storeFrontLegacyHomePage.clickCompanyPressRoomLink();
		s_assert.assertTrue(storeFrontLegacyHomePage.getCurrentURL().contains(expectedURL), "Current url expected is: "+expectedURL+" while actual on UI is "+storeFrontLegacyHomePage.getCurrentURL());
		s_assert.assertAll();
	}
}